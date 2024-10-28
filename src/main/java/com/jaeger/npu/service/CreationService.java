package com.jaeger.npu.service;

import com.jaeger.npu.model.dto.CreationDTO;
import com.jaeger.npu.model.dto.RatingDTO;
import com.jaeger.npu.model.entity.Creation;
import com.jaeger.npu.model.entity.Rating;
import com.jaeger.npu.model.entity.User;
import com.jaeger.npu.repository.CreationRepository;
import com.jaeger.npu.repository.ElementRepository;
import com.jaeger.npu.repository.RatingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Service class handling creation-related operations including file uploads, creation management, and ratings.
 * This service integrates with AWS S3 for image storage and manages creation entities in the database.
 */
@Service
@Transactional
public class CreationService {

    @Autowired
    private CreationRepository creationRepository;

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private S3Client s3Client;

    private static final String BUCKET_NAME = "creation-images";

    /**
     * Creates a new creation with an uploaded image file.
     *
     * @param file        The image file to be uploaded to S3
     * @param creationDTO Data transfer object containing creation details
     * @return The ID of the newly created creation
     * @throws IOException If there's an error handling the file upload or creation process
     */
    public Long createCreation(MultipartFile file, CreationDTO creationDTO) throws IOException {
        try {
            String s3Url = uploadFileToS3(file);
            Creation creation = buildCreation(creationDTO, s3Url);
            Creation savedCreation = creationRepository.save(creation);
            return savedCreation.getId();
        } catch (Exception e) {
            throw new IOException("Failed to create creation: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a creation by its ID.
     *
     * @param id The ID of the creation to retrieve
     * @return The Creation entity
     * @throws ResponseStatusException with HTTP 404 if the creation is not found
     */
    public Creation getCreationById(Long id) {
        return creationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Creation not found with id: " + id));
    }

    /**
     * Searches for creations by element name with pagination.
     *
     * @param elementName The name of the element to search for
     * @param pageable   Pagination information
     * @return A page of creations containing the specified element
     */
    public Page<Creation> searchByElement(String elementName, Pageable pageable) {
        return creationRepository.findByElementsNameContainingIgnoreCase(elementName, pageable);
    }

    /**
     * Retrieves all ratings for a specific creation.
     *
     * @param creationId The ID of the creation to get ratings for
     * @return A set of ratings for the specified creation
     * @throws ResponseStatusException with HTTP 404 if no ratings are found
     */
    public Set<Rating> getRatingsForCreation(Long creationId) {
        Creation creation = getCreationById(creationId);
        return this.ratingRepository.findAllByCreation(creation).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No ratings found for this creationId: " + creation.getId())
        );
    }

    /**
     * Adds a new rating to a creation.
     *
     * @param creationId The ID of the creation to rate
     * @param ratingDTO  Data transfer object containing rating details
     * @return The newly created Rating entity
     * @throws IllegalStateException if the user has already rated this creation
     */
    public Rating rateCreation(Long creationId, RatingDTO ratingDTO) {
        Creation creation = getCreationById(creationId);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ratingRepository.findByCreationAndRatedBy(creation, currentUser)
                .ifPresent(rating -> {
                    throw new IllegalStateException("You have already rated this creation");
                });

        Rating rating = Rating.builder()
                .creativityScore(ratingDTO.getCreativityScore())
                .uniquenessScore(ratingDTO.getUniquenessScore())
                .creation(creation)
                .ratedBy(currentUser)
                .build();

        return ratingRepository.save(rating);
    }

    /**
     * Uploads a file to AWS S3 and returns the URL.
     *
     * @param file The file to upload
     * @return The URL of the uploaded file in S3
     * @throws IOException If there's an error during file upload
     */
    private String uploadFileToS3(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                BUCKET_NAME,
                "EU_CENTRAL_1",
                fileName);
    }

    /**
     * Builds a Creation entity from a DTO and S3 URL.
     *
     * @param creationDTO Data transfer object containing creation details
     * @param s3Url      The URL of the uploaded image in S3
     * @return A new Creation entity
     */
    private Creation buildCreation(CreationDTO creationDTO, String s3Url) {
        return Creation.builder()
                .title(creationDTO.getTitle())
                .description(creationDTO.getDescription())
                .imageUrl(s3Url)
                .createdAt(LocalDateTime.now())
                .user(creationDTO.getUser())
                .elements(creationDTO.getElements())
                .build();
    }
}
