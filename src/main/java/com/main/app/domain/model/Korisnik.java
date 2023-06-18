package com.main.app.domain.model;

import com.main.app.enums.Roles;
import com.main.app.enums.Status;
import com.main.app.service.EncryptionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.*;
import javax.persistence.*;
import java.io.Serializable;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;

import java.util.ArrayList;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "korisnik")
public class Korisnik implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(columnDefinition = "boolean default false")
    private boolean blocked;

    @Embedded
    private LoginWithEmail emailLogin;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  null;
    }

    @PrePersist
    @PreUpdate
    private void encryptSensitiveData() {
        encryptAddress();
        encryptPhoneNumber();
    }

    @PostLoad
    private void decryptSensitiveData() {
        decryptAddress();
        decryptPhoneNumber();
    }






    private void encryptAddress() {
        EncryptionService encryptionService = new EncryptionService();
        try {
            if (address != null) {
                SecretKey secretKey = encryptionService.generateSecretKey();
                String generatedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
                System.out.println("Generated Key: " + generatedKey);
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encryptedBytes = cipher.doFinal(address.getBytes());
                address = Base64.getEncoder().encodeToString(encryptedBytes); // Base64 encoding
            }
        } catch (InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            // Logovanje greške ili rukovanje izuzetkom
            e.printStackTrace(); // Može se promeniti način logovanja prema potrebama vaše aplikacije
        }
    }


    public void decryptAddress() {
        EncryptionService encryptionService = new EncryptionService();
        try {
            if (address != null) {
                SecretKey secretKey = encryptionService.generateSecretKey(); // Pretpostavljamo da ključ prethodno generišete i čuvate izvan ove klase
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] encryptedBytes = Base64.getDecoder().decode(address); // Base64 decoding
                byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                address = new String(decryptedBytes);
                System.out.println("Adresa je: " + address);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            // Logovanje greške ili rukovanje izuzetkom
            e.printStackTrace(); // Može se promeniti način logovanja prema potrebama vaše aplikacije
        }
    }

    private void encryptPhoneNumber() {
        EncryptionService encryptionService = new EncryptionService();
        try {
            if (phoneNumber != null) {
                SecretKey secretKey = encryptionService.generateSecretKey();
                String generatedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
                System.out.println("Generated Key: " + generatedKey);
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encryptedBytes = cipher.doFinal(phoneNumber.getBytes());
                phoneNumber = Base64.getEncoder().encodeToString(encryptedBytes);
            }
        } catch (InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public void decryptPhoneNumber() {
        EncryptionService encryptionService = new EncryptionService();
        try {
            if (phoneNumber != null) {
                SecretKey secretKey = encryptionService.generateSecretKey();
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] encryptedBytes = Base64.getDecoder().decode(phoneNumber);
                byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                phoneNumber = new String(decryptedBytes);
                System.out.println("Broj telefona je: " + phoneNumber);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }

}
