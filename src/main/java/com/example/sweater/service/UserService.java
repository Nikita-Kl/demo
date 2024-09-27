package com.example.sweater.service;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private MailSender mailSender;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }
    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        sendMessage(user);
        return true;
    }

    private void sendMessage(User user) {
        if(!StringUtils.isEmpty(user.getEmail())){
            String message = String.format("Здравствуйте, %s \n" + "Добро пожаловать на наш сайт!. " +
                    "Пожалуйста, для завершение регистрации нажмите на эту ссылку: http://localhost:8085/activate/%s",
                    user.getUsername(), user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }
    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if(user == null){
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepo.save(user);
        return true;
    }
    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {

        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);
    }

}