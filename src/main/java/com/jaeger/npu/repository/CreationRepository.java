package com.jaeger.npu.repository;

import com.jaeger.npu.model.entity.Creation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CreationRepository extends JpaRepository<Creation, Long> {
    Page<Creation> findByElementsNameContainingIgnoreCase(String elementName, Pageable pageable);

}
