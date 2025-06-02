package com.tekerasoft.tekeramarketplace.kafka;

import com.tekerasoft.tekeramarketplace.dto.payload.MindMapMessage;
import com.tekerasoft.tekeramarketplace.model.entity.TargetPicture;
import com.tekerasoft.tekeramarketplace.repository.releational.TargetPictureRepository;
import com.tekerasoft.tekeramarketplace.service.DigitalFashionService;
import com.tekerasoft.tekeramarketplace.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;


@Service
public class KafkaConsumer {

    private final FileService fileService;
    private final TargetPictureRepository targetPictureRepository;
    private final DigitalFashionService digitalFashionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    public KafkaConsumer(FileService fileService, TargetPictureRepository targetPictureRepository,
                         DigitalFashionService digitalFashionService) {
        this.fileService = fileService;
        this.targetPictureRepository = targetPictureRepository;
        this.digitalFashionService = digitalFashionService;
    }

    @KafkaListener(topics = "mindmap-processing-topic", groupId = "tekeraGroup")
    public void processMindMap(MindMapMessage message) {
        try (InputStream imageStream = new ByteArrayInputStream(message.getImageFile())) {
            String mindFilePath = digitalFashionService.processAndStoreMindMap(
                    imageStream
            );

            Optional<TargetPicture> tp = targetPictureRepository.findById(message.getId());
            tp.ifPresent(targetPicture -> {
                targetPicture.setMindPath(mindFilePath);
                targetPictureRepository.save(targetPicture);
            });
        } catch (Exception e) {
            System.err.println("Mind map processing failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
