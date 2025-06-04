package com.tekerasoft.tekeramarketplace.kafka;

import com.tekerasoft.tekeramarketplace.dto.payload.MindMapMessage;
import com.tekerasoft.tekeramarketplace.model.entity.TargetPicture;
import com.tekerasoft.tekeramarketplace.repository.TargetPictureRepository;
import com.tekerasoft.tekeramarketplace.service.DigitalFashionService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;


@Service
public class KafkaConsumer {

    @Value("${spring.minio.bucket-name}")
    private String bucketName;

    private final TargetPictureRepository targetPictureRepository;
    private final DigitalFashionService digitalFashionService;
    private final MinioClient minioClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    public KafkaConsumer(TargetPictureRepository targetPictureRepository,
                         DigitalFashionService digitalFashionService, MinioClient minioClient) {
        this.targetPictureRepository = targetPictureRepository;
        this.digitalFashionService = digitalFashionService;
        this.minioClient = minioClient;
    }

    @KafkaListener(topics = "mindmap-processing-topic", groupId = "tekeraGroup")
    public void processMindMap(MindMapMessage message) {
        try (InputStream imageStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(message.getFilePath())
                        .build())) {

            String mindFilePath = digitalFashionService.processAndStoreMindMap(imageStream);

            targetPictureRepository.findById(message.getId()).ifPresent(tp -> {
                tp.setMindPath(mindFilePath);
                targetPictureRepository.save(tp);
            });

        } catch (Exception e) {
            LOGGER.error("processMindMap: {}", e.getMessage(), e);
            throw new RuntimeException("Mind map işleme hatası: " + e.getMessage(), e);
        }
    }


}
