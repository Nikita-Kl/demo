package com.example.sweater.repos;

import com.example.sweater.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findById(int id);


    @Query("select u from User u where u.id = :id")
    void refresh(@Param("id") Long id);

    List<User> findAllByIdNot(Long id);

    User findByActivationCode(String code);
}
