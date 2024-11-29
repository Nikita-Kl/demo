package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.Review;
import com.example.sweater.domain.User;
import com.example.sweater.domain.UserSale;
import com.example.sweater.repos.MessageRepo;
import com.example.sweater.repos.ReviewRepo;
import com.example.sweater.repos.SaleRepo;
import com.example.sweater.repos.UserRepo;
import com.example.sweater.service.MainService;
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
    @Autowired
    MainService mainService;
    @Value("${upload.path}")
    private String uploadPath;


    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false) Long compareUserId,
            @AuthenticationPrincipal User currentUser,
            HttpServletRequest request,
            Model model) {
        return mainService.getMainPage(filter, compareUserId, currentUser, model, request);
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
            return "main";
        }
        mainService.saveMessage(user, message, file);
        return "redirect:/main";
    }

    @PostMapping("/user-sale/{userId}/add-sale")
    public String addUserSale(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long userId,
            @RequestParam("messageId") Message message,
            @RequestParam("cost") String costStr,
            RedirectAttributes redirectAttributes
    ) {
        Long cost = Long.parseLong(costStr.replaceAll("[^\\d]", ""));
        mainService.addUserSale(currentUser, userId, message, cost);
        return "redirect:/user-sale/" + userId + "?message=" + message.getId();
    }


    @GetMapping("/user-salee/{user}")
    public String userSalee(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message
    ) {
        return mainService.getUserSalePage(currentUser, user, model, message);
    }

    @PostMapping("/user-sale/{userId}/add-review")
    public String addReview(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long userId,
            @RequestParam("messageId") Message message,
            @RequestParam String reviewText,
            RedirectAttributes redirectAttributes
    ) {
        return mainService.addReview(currentUser, userId, message, reviewText, redirectAttributes);
    }


    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message
    ) {
        return mainService.getUserMessages(currentUser, user, model, message);
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("title") String title,
            @RequestParam("full_text") String fullText,
            HttpServletRequest request,
            @RequestParam("cost") String costStr,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return mainService.updateMessage(
                currentUser, user, message, text, tag, title, fullText, costStr, file, request);
    }

    @GetMapping("/user-sale/{saleId}/check")
    public String getReceipt(@PathVariable Long saleId, Model model) {
        return mainService.getReceipt(saleId, model);
    }

}

