package com.jaeger.npu.controller;

import com.jaeger.npu.model.dto.CreationDTO;
import com.jaeger.npu.model.dto.RatingDTO;
import com.jaeger.npu.model.entity.Creation;
import com.jaeger.npu.model.entity.Rating;
import com.jaeger.npu.service.CreationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/creations")
public class CreationController {

    @Autowired
    private CreationService creationService;

    @PostMapping
    public ResponseEntity<?> createCreation(
            @RequestPart("file") MultipartFile file,
            @RequestPart("creation")CreationDTO creationDTO)  {
        try {
            return ResponseEntity.ok(creationService.createCreation(file, creationDTO));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Creation>> searchByElement(
            @RequestParam String elementName,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(creationService.searchByElement(elementName, pageable));
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<Rating> rateCreation(
            @PathVariable Long id,
            @RequestBody @Valid RatingDTO ratingDTO) {
        return ResponseEntity.ok(creationService.rateCreation(id, ratingDTO));
    }

    @GetMapping("/get-ratings/{id}")
    public ResponseEntity<?> getRatingsForCreation(@PathVariable Long id) {
        return ResponseEntity.ok(creationService.getRatingsForCreation(id));
    }
}
