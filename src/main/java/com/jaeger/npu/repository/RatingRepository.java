package com.jaeger.npu.repository;

import com.jaeger.npu.model.entity.Creation;
import com.jaeger.npu.model.entity.Rating;
import com.jaeger.npu.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Object> findByCreationAndRatedBy(Creation creation, User currentUser);
    Optional<Set<Rating>> findAllByCreation(Creation creation);

}
