package com.example.sweater.service;

import com.example.sweater.domain.User;
import com.example.sweater.domain.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserServiceInterface extends UserDetailsService {

    public UserDto getUserDto(Long id);

    public boolean addUser(User user);

    public boolean activateUser(String code);

    public List<User> findAll();

    public void saveUser(User user, String username, Map<String, String> form);
}
