package com.jaeger.npu.repository;

import com.jaeger.npu.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
