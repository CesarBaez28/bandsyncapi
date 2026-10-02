package com.bandsyncapi.bandsyncapi.utils;

/**
 * Util class with helper functions related to AWS logic
 */
public class AwsUtils {

  private AwsUtils() {
  }

  /**
   * Get file name form aws url
   * it is always in the last position for example:
   * https://amazonaws.com/test/fileName.jpg
   * 
   * @param url - aws url
   * @return - file name
   * @throws IllegalArgumentException if the url url is null or empty
   */
  public static String getFileNameFromAwsUrl(String url) {
    if (url == null || url.isEmpty()) {
      throw new IllegalArgumentException("URL must not be null or empty");
    }

    String[] splitUrl = url.split("/");
    return splitUrl[splitUrl.length - 1];
  }
}
