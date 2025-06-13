package com.bandsyncapi.bandsyncapi.api.v1.services.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bandsyncapi.bandsyncapi.api.v1.services.FilesService;
import com.bandsyncapi.bandsyncapi.exceptions.FileStorageException;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

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
    if (!validateImage(file)) return "";
    
    String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
    log.info("Uploading musical band logo: {}", fileName);

    try {
      var request = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(directory + "/" + fileName)
          .build();
  
      s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
  
      log.info("logo saved successfully");
  
      return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + directory + "/" + fileName;
    } catch (IOException e) {
      log.error("Error al leer archivo: {}", e.getMessage(), e);
      throw new FileStorageException("No se pudo leer el archivo que se intentó subir");      
    } catch (S3Exception e) {
      log.error("Error al guardar el archivo a S3: {}",  e.awsErrorDetails().errorMessage(), e);
      throw new FileStorageException("Ocurrió un error al intentar guardar la imagen");
    }
  }

  /**
   * Validate if the image is valid
   * 
   * @param file - file to validate
   * @return - boolean if it is valid or not
   */
  private boolean validateImage(MultipartFile file) {
    if (file == null || file.isEmpty()) return false;
    
    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new FileStorageException("El archivo no es una imagen válida");
    }

    if (file.getSize() > 5 * 1024 * 1024) { // 5MB
      throw new FileStorageException("El tamaño de la imagen excede el límite permitido");
    }

    return true;
  }
}
