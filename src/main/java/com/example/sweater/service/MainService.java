package com.example.sweater.service;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.Review;
import com.example.sweater.domain.User;
import com.example.sweater.domain.UserSale;
import com.example.sweater.domain.dto.MessageDto;
import com.example.sweater.domain.dto.ReviewDto;
import com.example.sweater.domain.dto.UserSaleDto;
import com.example.sweater.repos.MessageRepo;
import com.example.sweater.repos.ReviewRepo;
import com.example.sweater.repos.SaleRepo;
import com.example.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MainService {
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SaleRepo userSaleRepo;
    @Autowired
    private ReviewRepo reviewRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public UserSaleDto convertToDto(UserSale userSale) {
        return new UserSaleDto(
                userSale.getId(),
                userSale.getUser().getId(),
                userSale.getUser().getUsername(),
                userSale.getMessage().getId(),
                userSale.getMessage().getTitle(),
                userSale.getCost()
        );
    }

    public List<UserSaleDto> convertToDtoList(List<UserSale> userSales) {
        return userSales.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ReviewDto convertToDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getReview_text(),
                review.getAuthor_review().getUsername(),
                review.getMessageReview().getId()
        );
    }

    public MessageDto convertToDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getText(),
                message.getTitle(),
                message.getFull_text(),
                message.getTag(),
                message.getCost(),
                message.getAuthor().getUsername(),
                message.getReviews().stream().map(Review::getReview_text).collect(Collectors.toList()),
                message.getFilename()
        );
    }

    public List<MessageDto> convertToMessageDtoList(List<Message> messages) {
        return messages.stream().map(this::convertToDto).collect(Collectors.toList());
    }



    @Transactional
    public String getMainPage(String filter, Long compareUserId, User currentUser, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        }
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        userRepo.refresh(currentUser.getId());

        Iterable<Message> messages = filter.isEmpty() ? messageRepo.findAll() : messageRepo.findByTitleContaining(filter);
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        model.addAttribute("user", currentUser);

        Set<Message> currentUserMessages = new HashSet<>(currentUser.getSelectedMessages());

        if (compareUserId == null) {
            compareUserId = currentUser.getId();
        }
        User selectedUser = userRepo.findById(compareUserId).orElse(null);
        if (selectedUser == null) {
            model.addAttribute("compareMessage", "Selected user does not exist");
            return "main";
        }

        Set<Message> selectedUserMessages = selectedUser.getSelectedMessages();
        findClosestUser(model, compareUserId, selectedUser, selectedUserMessages);

        suggestRandomMessages(model, currentUser);

        return "main";
    }

    private void findClosestUser(Model model, Long compareUserId, User selectedUser, Set<Message> selectedUserMessages) {
        List<User> otherUsers = userRepo.findAllByIdNot(compareUserId);
        User closestUser = null;
        Set<Message> closestUserMessages = null;
        int minDifference = Integer.MAX_VALUE;

        for (User user : otherUsers) {
            Set<Message> userMessages = user.getSelectedMessages();
            int messageCountDifference = Math.abs(userMessages.size() - selectedUserMessages.size());
            if (messageCountDifference == 1) {
                int totalElements = Math.max(userMessages.size(), selectedUserMessages.size());
                Set<Message> union = new HashSet<>(userMessages);
                union.addAll(selectedUserMessages);
                Set<Message> intersection = new HashSet<>(userMessages);
                intersection.retainAll(selectedUserMessages);

                int jaccardDifference = totalElements - intersection.size();

                if (jaccardDifference < minDifference) {
                    minDifference = jaccardDifference;
                    closestUser = user;
                    closestUserMessages = userMessages;
                }
            }
        }

        if (closestUser == null) {
            model.addAttribute("compareMessage", "Не найден пользователь с разницей в сообщениях");
        } else {
            Set<Message> missingMessages = new HashSet<>(closestUserMessages);
            missingMessages.removeAll(selectedUserMessages);

            if (missingMessages.size() > 1) {
                missingMessages = missingMessages.stream().limit(1).collect(Collectors.toSet());
            }

            String missingMessageIds = missingMessages.stream()
                    .map(Message::getId)
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            model.addAttribute("missingMessages", missingMessages);
            model.addAttribute("compareMessage", "Ближайший пользователь " + closestUser.getId() +
                    " С пропущенными сообщениями: " + missingMessageIds);
        }
    }

    private void suggestRandomMessages(Model model, User currentUser) {
        if (currentUser != null) {
            List<Message> availableMessages = userRepo.findAll().stream()
                    .filter(user -> !user.getId().equals(currentUser.getId()))
                    .map(User::getSelectedMessages)
                    .filter(Objects::nonNull)
                    .flatMap(Set::stream)
                    .collect(Collectors.toList());

            if (!availableMessages.isEmpty()) {
                Random random = new Random();
                Message randomMessage = availableMessages.get(random.nextInt(availableMessages.size()));
                model.addAttribute("randomMessage", randomMessage);
            } else {
                model.addAttribute("compareMessage", "No messages available from other users.");
            }
        }
    }

    public void saveMessage(User user, Message message, MultipartFile file) throws IOException {
        message.setAuthor(user);
        saveFile(message, file);
        messageRepo.save(message);
    }

    public void saveFile(Message message, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));
            message.setFilename(resultFilename);
        }
    }

    public void addUserSale(User currentUser, Long userId, Message message, Long cost) {
        if (message != null) {
            UserSale userSale = new UserSale();
            userSale.setUser(currentUser);
            userSale.setMessage(message);
            userSale.setCost(cost);
            userSaleRepo.save(userSale);
        }
    }
    @Transactional
    public String getUserSalePage(User currentUser, User user, Model model, Message message) {
        Set<Message> messages = user.getMessages();
        List<Review> reviews = Collections.emptyList();

        if (message != null) {
            reviews = reviewRepo.findAllByMessageReview(message); // Получение отзывов для сообщения
        }

        model.addAttribute("userChannel", user);
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("reviews", reviews);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("messageId", message != null ? message.getId() : null);


        try {
            if (message != null && !currentUser.getSelectedMessages().contains(message)) {
                currentUser.getSelectedMessages().add(message);
                userRepo.save(currentUser);
            }
        } catch (Exception e) {
        }

        return "redirect:/user-sale/" + user.getId() + (message != null ? "?message=" + message.getId() : "");
    }


    @Transactional
    public String addReview(User currentUser, Long userId, Message message, String reviewText, RedirectAttributes redirectAttributes) {
        if (message != null && !reviewText.isEmpty()) {
            Review review = new Review(reviewText, currentUser, message);
            reviewRepo.save(review);
            redirectAttributes.addFlashAttribute("reviewSuccess", "Ваш отзыв успешно опубликован!");
        } else {
            redirectAttributes.addFlashAttribute("reviewError", "Отзыв не может быть пустым!");
        }

        return "redirect:/user-sale/" + userId + "?message=" + (message != null ? message.getId() : "");
    }

    @Transactional
    public String getUserMessages(User currentUser, User user, Model model, Message message) {
        Set<Message> messages = user.getMessages();

        model.addAttribute("userChannel", user);
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));

        return "userMessages";
    }

    /**
     * Обновление сообщения.
     */
    @Transactional
    public String updateMessage(
            User currentUser, User user, Message message, String text, String tag, String title,
            String fullText, String costStr, MultipartFile file, HttpServletRequest request) throws IOException {
        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(title)) {
                message.setTitle(title);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            if (!StringUtils.isEmpty(fullText)) {
                message.setFull_text(fullText);
            }
            Long cost = Long.parseLong(costStr.replaceAll("[^\\d]", ""));
            if (cost != null) {
                message.setCost(cost);
            }
            saveFile(message, file);
            messageRepo.save(message);
        }

        // Обновление сессии пользователя
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        }
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        userRepo.refresh(currentUser.getId());

        return "redirect:/user-messages/" + user.getId();
    }

    /**
     * Получение чека для продажи.
     */
    @Transactional
    public String getReceipt(Long saleId, Model model) {
        UserSale userSale = userSaleRepo.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        model.addAttribute("userSale", userSale);
        return "check";
    }
}
