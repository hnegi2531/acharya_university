package com.au.service;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RSAEncryptionService {

	 @Value("${secretkey}")
	 private String secret;

	 String ALGO = "AES/ECB/PKCS5Padding";
	 
	public SecretKeySpec getKey() throws Exception {

		SecretKeySpec myDesKey;
		StringBuffer sb = new StringBuffer(16);
		sb.append(secret);
		while (sb.length() < 16) {
			sb.append("0");
		}
		if (sb.length() > 16) {
			sb.setLength(16);
		}
		byte[] data = sb.toString().getBytes("UTF-8");
		// Genrate the Key
		myDesKey = new SecretKeySpec(data, "AES");
		// Create the cipher
		return myDesKey;
	}

	public String doEncryption(String s) throws Exception {
		// Initialize the cipher for encryption
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, getKey());

		// sensitive information
		byte[] text = s.getBytes();

		// Encrypt the text
		byte[] textEncrypted = c.doFinal(text);

		return Base64.getEncoder().encodeToString(textEncrypted);

	}

	public String doDecryption(String decode) throws Exception {

		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, getKey());

		// Decrypt the text
		byte[] s = Base64.getDecoder().decode(decode);
		byte[] textDecrypted = c.doFinal(s);

		return (new String(textDecrypted));
	}

	
}