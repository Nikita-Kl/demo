package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.Review;
import com.example.sweater.domain.User;
import com.example.sweater.domain.UserSale;
import com.example.sweater.repos.MessageRepo;
import com.example.sweater.repos.ReviewRepo;
import com.example.sweater.repos.SaleRepo;
import com.example.sweater.repos.UserRepo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private SaleRepo userSaleRepo;
    @Autowired
    private UserRepo userRepo;
    @Value("${upload.path}")
    private String uploadPath;
    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    @Transactional
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false) Long compareUserId,
            @AuthenticationPrincipal User currentUser,
            HttpServletRequest request,
            Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        }
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        userRepo.refresh(currentUser.getId());

        Iterable<Message> messages;
        if (!filter.isEmpty()) {
            messages = messageRepo.findByTitleContaining(filter);
        } else {
            messages = messageRepo.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        model.addAttribute("user", currentUser);

        Set<Message> currentUserMessages = new HashSet<>(currentUser.getSelectedMessages());  // Создаем копию для изменений



        // Поиск и сравнение сообщений с другими пользователями, если у пользователя два и более сообщения
        if (compareUserId == null) {
            compareUserId = currentUser.getId();
        }

        User selectedUser = userRepo.findById(compareUserId).orElse(null);
        if (selectedUser == null) {
            model.addAttribute("compareMessage", "Selected user does not exist");
            return "main";
        }

        Set<Message> selectedUserMessages = selectedUser.getSelectedMessages();
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



            if (currentUser != null ) {

                List<Message> availableMessages = otherUsers.stream()
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
                return "main";

        }
        return "main";


    }


    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        message.setAuthor(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            SaveFile(message, file);
            model.addAttribute("message", null);
            messageRepo.save(message);
        }
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    private void SaveFile(Message message, MultipartFile file) throws IOException {
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
    @Autowired private ReviewRepo reviewRepo;
    @GetMapping("/user-sale/{user}")
    public String userSale(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message
    ) {
        Set<Message> messages = user.getMessages();
        List<Review> reviews = Collections.emptyList();
        if (message != null) {
            reviews = reviewRepo.findAllByMessageReview(message);
        }

        model.addAttribute("userChannel", user);
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("reviews", reviews);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("messageId", message.getId());

        return "userSale";
    }

    @GetMapping("/user-salee/{user}")
    public String userSalee(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message             // Внедряем зависимость репозитория отзывов
    ) {
        Set<Message> messages = user.getMessages();
        List<Review> reviews = Collections.emptyList();
        if (message != null) {
            reviews = reviewRepo.findAllByMessageReview(message); // Получаем отзывы для сообщения
        }

        model.addAttribute("userChannel", user);
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("reviews", reviews); // Добавляем отзывы в модель
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("messageId", message.getId());


        System.out.println("Содержимое до проверки: " + currentUser.getSelectedMessages());
        System.out.println(currentUser.getSelectedMessages().contains(message) == false);
        System.out.println(currentUser.getSelectedMessages().contains(message));
        System.out.println(currentUser.getSelectedMessages());


        try {
            if (!currentUser.getSelectedMessages().contains(message)) {
                currentUser.getSelectedMessages().add(message);
                userRepo.save(currentUser);
            }
        } catch (Exception e) {
            // Логирование ошибки или уведомление пользователя
        }


        return "redirect:/user-sale/" + user.getId() + "?message=" + message.getId();
    }
    @PostMapping("/user-sale/{userId}/add-review")
    public String addReview(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long userId,
            @RequestParam("messageId") Message message,
            @RequestParam String reviewText,
            RedirectAttributes redirectAttributes
    ) {
        if (message != null && !reviewText.isEmpty()) {
            Review review = new Review(reviewText, currentUser, message);
            reviewRepo.save(review);
            redirectAttributes.addFlashAttribute("reviewSuccess", "Ваш отзыв успешно опубликован!.");
        } else {
            redirectAttributes.addFlashAttribute("reviewError", "Отзыв не может быть пустым!.");
        }

        return "redirect:/user-sale/" + userId + "?message=" + message.getId();
    }


    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message
    ) {
        Set<Message> messages = user.getMessages();

        model.addAttribute("userChannel", user);
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));

        return "userMessages";
    }
    @PostMapping("/user-messages/{user}")
    public String updateMessage( @AuthenticationPrincipal User currentUser,
                                 @PathVariable User user,
                                 @RequestParam("id") Message message,
                                 @RequestParam("text") String text,
                                 @RequestParam("tag") String tag,
                                 @RequestParam("title") String title,
                                 @RequestParam("full_text") String full_text,
                                 HttpServletRequest request,
                                 @RequestParam("cost") String costStr,
                                 @RequestParam("file") MultipartFile file) throws IOException {

        if(message.getAuthor().equals(currentUser)){
            if(!StringUtils.isEmpty(text)){
                message.setText(text);
            }
            if(!StringUtils.isEmpty(title)){
                message.setTitle(title);
            }
            if(!StringUtils.isEmpty(tag)){
                message.setTag(tag);
            }
            if(!StringUtils.isEmpty(full_text)){
                message.setFull_text(full_text);
            }
            Long cost = Long.parseLong(costStr.replaceAll("[^\\d]", ""));
            if(!StringUtils.isEmpty(cost)){
                message.setCost(cost);
            }
            SaveFile(message,file);
            messageRepo.save(message);
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        }
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        userRepo.refresh(currentUser.getId());


        return "redirect:/user-messages/" + user.getId();
    }


    @PostMapping("/user-sale/{userId}/add-sale")
    public String addUserSale(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long userId,
            @RequestParam("messageId") Message message,
            @RequestParam("cost") String costStr,
            RedirectAttributes redirectAttributes
    ) {
        // Проверяем, что сообщение и стоимость не пустые
        if (message != null) {
            // Создаем новую запись UserSale
            UserSale userSale = new UserSale();
            userSale.setUser(currentUser);
            userSale.setMessage(message);
            Long cost = Long.parseLong(costStr.replaceAll("[^\\d]", ""));
            userSale.setCost(cost);

            // Сохраняем новую запись в базе данных
            userSaleRepo.save(userSale);
            return "redirect:/user-sale/" + userSale.getId() + "/check";
            // Добавляем флеш-сообщение об успешном сохранении
        } else {
            // Добавляем флеш-сообщение об ошибке
            redirectAttributes.addFlashAttribute("saleError", "Сообщение и стоимость не могут быть пустыми!");
        }

        // Перенаправляем пользователя на страницу с продажами
        return "redirect:/user-sale/" + userId + "?message=" + message.getId();
    }
    @GetMapping("/user-sale/{saleId}/check")
    public String getReceipt(@PathVariable Long saleId, Model model) {
        UserSale userSale = userSaleRepo.findById(saleId).orElseThrow(() -> new RuntimeException("Sale not found"));
        model.addAttribute("userSale", userSale);
        return "check";
    }

}

