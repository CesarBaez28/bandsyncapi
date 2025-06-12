package com.bandsyncapi.bandsyncapi.api.v1.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service to upload and get files
 */
public interface FilesService {

  /**
   * Upload file
   * 
   * @param file - file
   * @param directory - The specific directory to upload the file 
   * @return - file url 
   */
  String uploadFile (MultipartFile file, String directory) throws IOException;
}
