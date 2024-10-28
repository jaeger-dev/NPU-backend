package com.jaeger.npu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

    @NotNull(message = "Creativity score is required")
    @Min(value = 1, message = "Creativity score must be between 1 and 5")
    @Max(value = 5, message = "Creativity score must be between 1 and 5")
    private Integer creativityScore;

    @NotNull(message = "Uniqueness score is required")
    @Min(value = 1, message = "Uniqueness score must be between 1 and 5")
    @Max(value = 5, message = "Uniqueness score must be between 1 and 5")
    private Integer uniquenessScore;

}