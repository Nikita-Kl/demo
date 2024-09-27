package com.example.sweater.repos;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.UserSale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.Id;
import java.util.List;

public interface SaleRepo extends CrudRepository<UserSale, Long> {
    // Добавим методы для поиска всех продаж по пользователю и сообщению
    List<UserSale> findAllByUserId(Long userId);
    List<UserSale> findAllByMessageId(Long messageId);
}