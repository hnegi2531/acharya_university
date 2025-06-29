package com.au.util;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.util.Arrays;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class EncryptionUtilAll {

	 private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	    private static final String SECRET_KEY = "YourSecretKey123"; // 16 bytes for AES-128

	    // Encrypt method using AES with CBC and IV
	    public static String encrypt(String data) throws Exception {
	        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
	        Cipher cipher = Cipher.getInstance(ALGORITHM);

	        // Generate a random IV (Initialization Vector)
	        byte[] iv = new byte[16]; // 16 bytes for AES-128
	        new SecureRandom().nextBytes(iv);
	        IvParameterSpec ivSpec = new IvParameterSpec(iv);

	        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
	        byte[] encryptedBytes = cipher.doFinal(data.getBytes());

	        // Concatenate the IV and the encrypted data and encode it in Base64
	        byte[] ivAndEncryptedData = new byte[iv.length + encryptedBytes.length];
	        System.arraycopy(iv, 0, ivAndEncryptedData, 0, iv.length);
	        System.arraycopy(encryptedBytes, 0, ivAndEncryptedData, iv.length, encryptedBytes.length);

	        return Base64.getEncoder().encodeToString(ivAndEncryptedData);
	    }

	    // Decrypt method using AES with CBC and IV
	    public static String decrypt(String encryptedData) throws Exception {
	        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
	        Cipher cipher = Cipher.getInstance(ALGORITHM);

	        // Decode the Base64 encoded string
	        byte[] ivAndEncryptedData = Base64.getDecoder().decode(encryptedData);

	        // Extract the IV (first 16 bytes)
	        byte[] iv = Arrays.copyOfRange(ivAndEncryptedData, 0, 16);
	        IvParameterSpec ivSpec = new IvParameterSpec(iv);

	        // Extract the actual encrypted data (after the IV)
	        byte[] encryptedBytes = Arrays.copyOfRange(ivAndEncryptedData, 16, ivAndEncryptedData.length);

	        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
	        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

	        return new String(decryptedBytes);
	    }
}
