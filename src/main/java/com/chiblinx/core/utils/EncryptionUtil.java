package com.chiblinx.core.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class EncryptionUtil {

  public String generateSHA256Hash(String plainText) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] encodedHash = digest.digest(plainText.getBytes(StandardCharsets.UTF_8));

    StringBuilder hexString = new StringBuilder();
    for (byte b : encodedHash) {
      hexString.append(String.format("%02x", b));
    }

    return hexString.toString();
  }

}
