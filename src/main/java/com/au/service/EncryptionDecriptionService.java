package com.au.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EncryptionDecriptionService {	
	 
		 @Value("${secretkey}")
		 private String secret;

		public SecretKeySpec setKey(String myKey) {
			MessageDigest sha = null;
			try {
				byte[] key = myKey.getBytes("UTF-8");
				sha = MessageDigest.getInstance("SHA-1");
				key = sha.digest(key);
				key = Arrays.copyOf(key, 32);
				SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
				return secretKey;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		}

		public String encryption(String strToEncrypt) {
			try {
				SecretKeySpec secretKey  = setKey(secret);
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
			} catch (Exception e) {
				System.out.println("Error while encrypting: " + e.toString());
			}
			return null;
		}

		public String decryption(String strToDecrypt) {
			try {
				SecretKeySpec secretKey  =setKey(secret);
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
				return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
			} catch (Exception e) {
				System.out.println("Error while decrypting: " + e.toString());
			}
			return null;
		}
	}

