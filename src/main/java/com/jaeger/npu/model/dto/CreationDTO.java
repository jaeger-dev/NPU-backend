package com.jaeger.npu.model.dto;

import com.jaeger.npu.model.entity.Element;
import com.jaeger.npu.model.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class CreationDTO {
    private String title;
    private String description;
    private User user;
    private Set<Element> elements;
}
