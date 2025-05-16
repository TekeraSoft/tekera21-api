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

    public String productFileUpload(MultipartFile file,String slug) {
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
            String fileName = slug + "-" + fileExtension;
            InputStream inputStream = file.getInputStream();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object("product/"+fileName)
                    .contentType(file.getContentType())
                    .stream(inputStream, file.getSize(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            return fileName;
        }catch (ErrorResponseException e) {
            throw new RuntimeException("MinIO Error Response: " + e.getMessage(), e);
         } catch (InsufficientDataException e) {
             throw new RuntimeException("Insufficient data during file upload: " + e.getMessage(), e);
         } catch (InternalException e) {
             throw new RuntimeException("Internal error during file upload: " + e.getMessage(), e);
         } catch (InvalidKeyException e) {
             throw new RuntimeException("Invalid key error: " + e.getMessage(), e);
         } catch (InvalidResponseException e) {
             throw new RuntimeException("Invalid response from MinIO: " + e.getMessage(), e);
         } catch (IOException e) {
             throw new RuntimeException("IO error during file upload: " + e.getMessage(), e);
         } catch (NoSuchAlgorithmException e) {
             throw new RuntimeException("Algorithm not found: " + e.getMessage(), e);
         } catch (ServerException e) {
             throw new RuntimeException("Server error: " + e.getMessage(), e);
         } catch (XmlParserException e) {
             throw new RuntimeException("XML Parsing error: " + e.getMessage(), e);
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
            return fileName;
        }catch (ErrorResponseException e) {
        throw new RuntimeException("MinIO Error Response: " + e.getMessage(), e);
         } catch (InsufficientDataException e) {
             throw new RuntimeException("Insufficient data during file upload: " + e.getMessage(), e);
         } catch (InternalException e) {
             throw new RuntimeException("Internal error during file upload: " + e.getMessage(), e);
         } catch (InvalidKeyException e) {
             throw new RuntimeException("Invalid key error: " + e.getMessage(), e);
         } catch (InvalidResponseException e) {
             throw new RuntimeException("Invalid response from MinIO: " + e.getMessage(), e);
         } catch (IOException e) {
             throw new RuntimeException("IO error during file upload: " + e.getMessage(), e);
         } catch (NoSuchAlgorithmException e) {
             throw new RuntimeException("Algorithm not found: " + e.getMessage(), e);
         } catch (ServerException e) {
             throw new RuntimeException("Server error: " + e.getMessage(), e);
         } catch (XmlParserException e) {
             throw new RuntimeException("XML Parsing error: " + e.getMessage(), e);
         }
    }

    public void deleteFileProduct(String fileName) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build();
            minioClient.removeObject(removeObjectArgs);
        }catch (ErrorResponseException e) {
            throw new RuntimeException("MinIO Error Response: " + e.getMessage(), e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException("Insufficient data during file upload: " + e.getMessage(), e);
        } catch (InternalException e) {
            throw new RuntimeException("Internal error during file upload: " + e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key error: " + e.getMessage(), e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException("Invalid response from MinIO: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("IO error during file upload: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not found: " + e.getMessage(), e);
        } catch (ServerException e) {
            throw new RuntimeException("Server error: " + e.getMessage(), e);
        } catch (XmlParserException e) {
            throw new RuntimeException("XML Parsing error: " + e.getMessage(), e);
        }
    }

    public void deleteInFolderFile(String fileName,String folderName) {
       try {
           RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                   .bucket(bucketName)
                   .object(folderName+"/"+fileName)
                   .build();
           minioClient.removeObject(removeObjectArgs);
       }catch (ErrorResponseException e) {
           throw new RuntimeException("MinIO Error Response: " + e.getMessage(), e);
       } catch (InsufficientDataException e) {
           throw new RuntimeException("Insufficient data during file upload: " + e.getMessage(), e);
       } catch (InternalException e) {
           throw new RuntimeException("Internal error during file upload: " + e.getMessage(), e);
       } catch (InvalidKeyException e) {
           throw new RuntimeException("Invalid key error: " + e.getMessage(), e);
       } catch (InvalidResponseException e) {
           throw new RuntimeException("Invalid response from MinIO: " + e.getMessage(), e);
       } catch (IOException e) {
           throw new RuntimeException("IO error during file upload: " + e.getMessage(), e);
       } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException("Algorithm not found: " + e.getMessage(), e);
       } catch (ServerException e) {
           throw new RuntimeException("Server error: " + e.getMessage(), e);
       } catch (XmlParserException e) {
           throw new RuntimeException("XML Parsing error: " + e.getMessage(), e);
       }
    }
}
