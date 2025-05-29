package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.document.TargetPictureDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateTargetPictureRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.document.TargetPicture;
import com.tekerasoft.tekeramarketplace.repository.nosql.TargetPictureRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class DigitalFashionService {
    @Value("${spring.mind-creator.api}")
    private String nodeApiUrl;
    private final TargetPictureRepository targetPictureRepository;
    private final FileService fileService;

    public DigitalFashionService(TargetPictureRepository targetPictureRepository,
                                 TargetPictureRepository digitalFashionRepository,
                                 FileService fileService) {
        this.targetPictureRepository = digitalFashionRepository;
        this.fileService = fileService;
    }

    public ApiResponse<?> createTargetPicture(CreateTargetPictureRequest req) {
        try {
            String filePath = fileService.targetPicUpload(req.getPicture());
            String mindPath = processAndStoreMindMap(req.getPicture());
            TargetPicture targetPicture = new TargetPicture();
            targetPicture.setTargetPic(filePath);
            targetPicture.setMindPath(mindPath);
            targetPictureRepository.save(targetPicture);
            return new ApiResponse<>("File Saved", HttpStatus.CREATED.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String processAndStoreMindMap(MultipartFile imageFile) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // MultipartBody
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // MultipartFile → geçici dosyaya yaz
            Path tempFile = Files.createTempFile("upload-", imageFile.getOriginalFilename());
            Files.write(tempFile, imageFile.getBytes());

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

            InputStream mindStream = new ByteArrayInputStream(mindContent);

            String uploadedPath = fileService.mindFileUpload(
                    mindStream,
                    mindContent.length,
                    "application/octet-stream",
                    ".mind"
            );

            // Temp dosyayı temizle
            Files.deleteIfExists(tempFile);

            return uploadedPath;
        } catch (Exception e) {
            throw new RuntimeException("Mind map işleme hatası: " + e.getMessage(), e);
        }
    }

    public Page<TargetPictureDto> getAllTargetPictures(Pageable pageable) {
        return targetPictureRepository.findAll(pageable).map(TargetPictureDto::toDto);
    }

    public ApiResponse<?> deleteTargetPicture(String id) {
        try {
            targetPictureRepository.deleteById(id);
            return new ApiResponse<>("TargetPicture deleted", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
