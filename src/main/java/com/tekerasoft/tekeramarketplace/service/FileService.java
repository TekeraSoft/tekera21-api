package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.model.entity.Company;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FileService {

    @Value("${spring.minio.bucket-name}")
    private String bucketName;

    @Value("${spring.minio.url}")
    private String minioUrl;

    private final MinioClient minioClient;
    private static Logger logger = LoggerFactory.getLogger(FileService.class);

    public FileService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String productFileUpload(MultipartFile file,String companyName,String slug, String color) {
        try {
            BucketExistsArgs args = BucketExistsArgs.builder().bucket(bucketName).build();
            if (!minioClient.bucketExists(args)) {
                MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
                minioClient.makeBucket(makeBucketArgs);
            }
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = slug + "-clr_"+ color + fileExtension;
            InputStream inputStream = file.getInputStream();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object("/products/" + companyName + "/" + fileName)
                    .contentType(file.getContentType())
                    .stream(inputStream, file.getSize(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            return "/products/"+companyName+"/"+fileName;
        } catch (Exception e) {
            throw new RuntimeException("MinIO upload error: " + e.getMessage(), e);
        }
    }

    public String folderFileUpload(MultipartFile file,String folderName){
        try {
            BucketExistsArgs args = BucketExistsArgs.builder().bucket(bucketName).build();
            if(!minioClient.bucketExists(args)) {
                MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
                minioClient.makeBucket(makeBucketArgs);
            }
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID() + fileExtension;
            InputStream inputStream = file.getInputStream();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(folderName+"/"+fileName)
                    .contentType(file.getContentType())
                    .stream(inputStream, file.getSize(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            return folderName + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("MinIO upload error: " + e.getMessage(), e);
        }
    }

    public String targetPicUpload(MultipartFile file) {
        try {
            BucketExistsArgs args = BucketExistsArgs.builder().bucket(bucketName).build();
            if(!minioClient.bucketExists(args)) {
                MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
                minioClient.makeBucket(makeBucketArgs);
            }
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID() + fileExtension;
            InputStream inputStream = file.getInputStream();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object("/target-pics/"+fileName)
                    .contentType(file.getContentType())
                    .stream(inputStream, file.getSize(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            return "/target-pics/"+fileName;
        }catch (Exception e) {
            throw new RuntimeException("MinIO upload error: " + e.getMessage(), e);
        }
    }

    public String mindFileUpload(InputStream inputStream, long size, String contentType, String extension) {
        try {
            // Bucket kontrolü
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // Dosya adı üretimi
            String fileName = UUID.randomUUID() + (extension != null ? extension : "");

            // MinIO'ya yükle
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("target-pics/" + fileName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType != null ? contentType : "application/octet-stream")
                            .build()
            );

            return "target-pics/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("MinIO upload error: " + e.getMessage(), e);
        }
    }

    public String productVideoUpload(InputStream inputStream, long size, String contentType, String extension,
                                     String companyName) {
        try {
            // Bucket kontrolü
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // Dosya adı üretimi
            String fileName = UUID.randomUUID() + (extension != null ? extension : "");
            String companyNameConvert = companyName.toLowerCase().replaceAll("\\s+", "_");
            // MinIO'ya yükle
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("products/"+ companyNameConvert + "/" + fileName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType != null ? contentType : "application/octet-stream")
                            .build()
            );

            return "products/" + companyNameConvert + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("MinIO upload error: " + e.getMessage(), e);
        }
    }

    public void deleteFileProduct(String fileName) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            throw new RuntimeException("MinIO upload error: " + e.getMessage(), e);
        }
    }

    public void deleteInFolderFile(String path) {
       try {
           RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                   .bucket(bucketName)
                   .object(path)
                   .build();
           minioClient.removeObject(removeObjectArgs);
       } catch (Exception e) {
           throw new RuntimeException("MinIO upload error: " + e.getMessage(), e);
       }
    }

    public Page<String> listUserMedia(String companyName, Pageable pageable) throws Exception {
        String prefix = "products/" + companyName + "/";
        List<String> allMedia = new ArrayList<>();

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(prefix)
                        .recursive(true)
                        .build());

        for (Result<Item> result : results) {
            Item item = result.get();
            String objectName = item.objectName();
            allMedia.add(objectName);
        }

        // Sayfalama hesaplama
        int total = allMedia.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), total);
        List<String> pageContent = allMedia.subList(start, end);

        return new PageImpl<>(pageContent, pageable, total);
    }

    public String generatePresignedUploadUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object("temp/" + objectName)
                            .method(Method.PUT)
                            .expiry(8, TimeUnit.MINUTES)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Presigned URL oluşturulamadı", e);
        }
    }

    @Scheduled(cron = "0 0/30 * * * *")
    public void cleanOldTempVideos() {
        Iterable<Result<Item>> items = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix("temp/")
                .recursive(true)
                .build());

        items.forEach(itemResult -> {
            try {
                Item item = itemResult.get();
                Instant lastModified = item.lastModified().toInstant();
                if (lastModified.isBefore(Instant.now().minus(30, ChronoUnit.MINUTES))) {
                    minioClient.removeObject(RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(item.objectName())
                            .build());
                }
            } catch (Exception e) {
                logger.warn("Failed to delete temp file: {}", e.getMessage());
            }
        });
    }

    public void copyObject(String sourcePath, String destinationPath) {
        try {
            CopySource source = CopySource.builder()
                    .bucket(bucketName)
                    .object(sourcePath)
                    .build();

            CopyObjectArgs args = CopyObjectArgs.builder()
                    .bucket(bucketName)
                    .object(destinationPath)
                    .source(source)
                    .build();

            minioClient.copyObject(args);
        } catch (Exception e) {
            logger.error("Copy object error: {}", e.getMessage(), e);
        }
    }
}
