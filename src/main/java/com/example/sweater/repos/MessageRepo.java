package com.example.sweater.repos;

import com.example.sweater.domain.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.Id;
import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Long> {
    List<Message> findByTag(String tag);
    List<Id> findById(int id);
    List<Message>findMessageById(long id);

    @Query("select m from Message m where lower(m.title) like lower(concat('%', :keyword, '%'))")
    Iterable<Message> findByTitleContaining(@Param("keyword") String keyword);
}
