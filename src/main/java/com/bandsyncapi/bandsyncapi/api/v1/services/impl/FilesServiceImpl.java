package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.services.FilesService;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Implementation of FilesService
 */
@Service
@Slf4j
public class FilesServiceImpl implements FilesService {

  private final S3Client s3Client;

  @Value("${aws.bucket.name}")
  private String bucketName;

  @Value("${aws.region}")
  private String region;

  public FilesServiceImpl(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  @Override
  public String uploadFile(MultipartFile file, String directory) throws IOException {
    String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
    
    log.info("Uploading musical band logo: {}", fileName);

    var request = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(directory + "/" + fileName)
        .build();

    s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

    log.info("logo saved successfully");

    return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + directory + "/" + fileName;
  }
}
