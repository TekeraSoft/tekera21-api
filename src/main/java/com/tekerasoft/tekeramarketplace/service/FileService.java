package com.tekerasoft.tekeramarketplace.service;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class FileService {

    @Value("${spring.minio.bucket-name}")
    private String bucketName;

    private final MinioClient minioClient;

    public FileService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String productFileUpload(MultipartFile file,String companyName,String slug) {
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
            String fileName = slug + fileExtension;
            String companyNameConvert = companyName.toLowerCase().replaceAll("\\s+", "_");
            InputStream inputStream = file.getInputStream();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object("/products/" + companyNameConvert + "/" + fileName)
                    .contentType(file.getContentType())
                    .stream(inputStream, file.getSize(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            return "/products/"+companyNameConvert+"/"+fileName;
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
}
