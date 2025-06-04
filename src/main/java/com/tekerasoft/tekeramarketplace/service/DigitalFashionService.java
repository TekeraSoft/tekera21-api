package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.TargetPictureDto;
import com.tekerasoft.tekeramarketplace.dto.payload.MindMapMessage;
import com.tekerasoft.tekeramarketplace.dto.request.CreateTargetPictureRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.entity.TargetPicture;
import com.tekerasoft.tekeramarketplace.repository.FabricRepository;
import com.tekerasoft.tekeramarketplace.repository.TargetPictureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class DigitalFashionService {

    @Value("${spring.mind-creator.api}")
    private String nodeApiUrl;

    private final static Logger LOGGER = LoggerFactory.getLogger(DigitalFashionService.class);

    private final TargetPictureRepository targetPictureRepository;
    private final FabricRepository fabricRepository;
    private final FileService fileService;
    private final KafkaTemplate<String, MindMapMessage> kafkaTemplate;

    public DigitalFashionService(TargetPictureRepository targetPictureRepository, FabricRepository fabricRepository,
                                 FileService fileService, KafkaTemplate<String, MindMapMessage> kafkaTemplate) {
        this.targetPictureRepository = targetPictureRepository;
        this.fabricRepository = fabricRepository;
        this.fileService = fileService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public ApiResponse<?> createTargetPicture(CreateTargetPictureRequest req) {
        try {
            String filePath = fileService.targetPicUpload(req.getImage());
            String defaultContentPath = fileService.targetPicUpload(req.getDefaultContent());
            TargetPicture targetPicture = new TargetPicture();
            targetPicture.setTargetPic(filePath);
            targetPicture.setProductId(req.getProductId());
            targetPicture.setDefaultContent(defaultContentPath);

            TargetPicture tp = targetPictureRepository.save(targetPicture);

            kafkaTemplate.send("mindmap-processing-topic", new MindMapMessage(tp.getId(),filePath));

            return new ApiResponse<>("File queued for processing", HttpStatus.ACCEPTED.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String processAndStoreMindMap(InputStream imageStream) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // InputStream → geçici dosya
            Path tempFile = Files.createTempFile("upload-", UUID.randomUUID().toString());
            Files.copy(imageStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            FileSystemResource fileResource = new FileSystemResource(tempFile.toFile());
            body.add("images", fileResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    nodeApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    byte[].class
            );

            byte[] mindContent = response.getBody();
            assert mindContent != null;

            try (InputStream mindStream = new ByteArrayInputStream(mindContent)) {
                String uploadedPath = fileService.mindFileUpload(
                        mindStream,
                        mindContent.length,
                        "application/octet-stream",
                        ".mind"
                );

                Files.deleteIfExists(tempFile);
                return uploadedPath;
            }
        } catch (Exception e) {
            LOGGER.error("Mind map işleme hatası: {}", e.getMessage());
            throw new RuntimeException("Mind map işleme hatası: " + e.getMessage(), e);
        }
    }

    public Page<TargetPictureDto> getAllTargetPictures(Pageable pageable) {
        return targetPictureRepository.findAll(pageable).map(TargetPictureDto::toDto);
    }

    public TargetPictureDto getTargetPictureAndContent(String productId, String targetId) {
        return targetPictureRepository.findByIdAndProductId(UUID.fromString(targetId),productId).map(TargetPictureDto::toDto).orElse(null);
    }

    public ApiResponse<?> deleteTargetPicture(String id) {
        try {
            targetPictureRepository.deleteById(UUID.fromString(id));
            return new ApiResponse<>("TargetPicture deleted", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
