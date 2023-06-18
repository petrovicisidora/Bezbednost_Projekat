package com.main.app.service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionService {

    private static final String SECRET_KEY = "MySecretKey12345"; // Fiksni ključ, treba ga zaštititi u stvarnom okruženju

    public SecretKey generateSecretKey() {
        // Generisanje AES ključa na osnovu fiksnog stringa
        byte[] keyBytes = SECRET_KEY.getBytes();
        return new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plaintext, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes); // Base64 enkodiranje šifrovanih bajtova
    }

    public String decrypt(String encryptedText, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText); // Base64 dekodiranje šifrovanog teksta
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
