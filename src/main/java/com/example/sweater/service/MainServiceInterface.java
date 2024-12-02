package com.example.sweater.service;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.Review;
import com.example.sweater.domain.User;
import com.example.sweater.domain.UserSale;
import com.example.sweater.domain.dto.MessageDto;
import com.example.sweater.domain.dto.ReviewDto;
import com.example.sweater.domain.dto.UserSaleDto;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface MainServiceInterface {
    public UserSaleDto convertToDto(UserSale userSale);

    public List<UserSaleDto> convertToDtoList(List<UserSale> userSales);

    public ReviewDto convertToDto(Review review);

    public MessageDto convertToDto(Message message);

    public List<MessageDto> convertToMessageDtoList(List<Message> messages);

    public String getMainPage(String filter, Long compareUserId, User currentUser, Model model, HttpServletRequest request);

    public void saveMessage(User user, Message message, MultipartFile file) throws IOException;

    public void saveFile(Message message, MultipartFile file) throws IOException;

    public void addUserSale(User currentUser, Long userId, Message message, Long cost);

    public String getUserSalePage(User currentUser, User user, Model model, Message message);

    public String addReview(User currentUser, Long userId, Message message, String reviewText, RedirectAttributes redirectAttributes);

    public String getUserMessages(User currentUser, User user, Model model, Message message);

    public void suggestRandomMessages(Model model, User currentUser);



    public String updateMessage(
            User currentUser, User user, Message message, String text, String tag, String title,
            String fullText, String costStr, MultipartFile file, HttpServletRequest request) throws IOException;

    public String getReceipt(Long saleId, Model model);

    public void findClosestUser(Model model, Long compareUserId, User selectedUser, Set<Message> selectedUserMessages);
}
