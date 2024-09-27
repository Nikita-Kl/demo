package com.example.sweater.repos;

import com.example.sweater.domain.Review;
import com.example.sweater.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Id;
import java.util.List;

public interface ReviewRepo extends CrudRepository<Review, Long> {

    List<Review> findAllByMessageReview(Message message);

    List<Id> findById(int id);
    List<Review>findMessageReviewById(long id);
}
