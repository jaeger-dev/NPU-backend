package com.jaeger.npu.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer creativityScore;
    private Integer uniquenessScore;

    @ManyToOne
    @JoinColumn(name = "creation_id")
    private Creation creation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User ratedBy;
}
