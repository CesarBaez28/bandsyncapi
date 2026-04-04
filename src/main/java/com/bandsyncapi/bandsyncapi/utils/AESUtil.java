package com.bandsyncapi.bandsyncapi.utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bandsyncapi.bandsyncapi.exceptions.DecryptionException;

import jakarta.annotation.PostConstruct;

/**
 * Utility class for AES encryption and decryption
 */
@Component
public class AESUtil {

  private static final String ALGORITHM = "AES/GCM/NoPadding";

  private static final int IV_LENGTH_BYTE = 12;

  private static final int TAG_LENGTH_BITS = 128;

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  @Value("${app.master-secret-key}")
  private String masterSecretKey;

  private SecretKeySpec secretKeySpec;

  /*
   * Initialize the secret key spec after the master secret key is injected
   */
  @PostConstruct
  public void init() {
    byte[] decodedKey = Base64.getDecoder().decode(masterSecretKey);
    this.secretKeySpec = new SecretKeySpec(decodedKey, "AES");
  }

  /*
   * Encrypt a plain text using AES encryption
   * 
   * @param plainText - the text to encrypt
   * 
   * @return - the encrypted text in Base64 encoding
   */
  public String encrypt(String plainText) {
    try {
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      byte[] iv = new byte[IV_LENGTH_BYTE];
      SECURE_RANDOM.nextBytes(iv);

      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new GCMParameterSpec(TAG_LENGTH_BITS, iv));

      byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      var byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
      byteBuffer.put(iv);
      byteBuffer.put(cipherText);

      return Base64.getEncoder().encodeToString(byteBuffer.array());

    } catch (Exception e) {
      throw new DecryptionException("Error during decryption", e);
    }
  }

  /*
   * Decrypt a cipher text using AES decryption
   * 
   * @param cipherText - the text to decrypt in Base64 encoding
   * 
   * @return - the decrypted plain text
   */
  public String decrypt(String cipherText) {
    try {
      byte[] decodedCipherText = Base64.getDecoder().decode(cipherText);
      ByteBuffer byteBuffer = ByteBuffer.wrap(decodedCipherText);

      byte[] iv = new byte[IV_LENGTH_BYTE];
      byteBuffer.get(iv);

      byte[] cipherBytes = new byte[byteBuffer.remaining()];
      byteBuffer.get(cipherBytes);

      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new GCMParameterSpec(TAG_LENGTH_BITS, iv));

      byte[] plainTextBytes = cipher.doFinal(cipherBytes);

      return new String(plainTextBytes, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new DecryptionException("Error during decryption", e);
    }
  }
}
