package com.main.app.service;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

@Service
public class AktivacijaService {

    private static final String SECRET_KEY = "tajni_kljuc";

    public String generisiAktivacioniLink(Long korisnikId) {
        String timestamp = Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        String podaci = korisnikId + ":" + timestamp;

        String hmac = generisiHmac(podaci);

        String link = "https://localhost:443/korisnik/aktivacija/" + korisnikId + "?timestamp=" + timestamp + "&hmac=" + hmac;

        return link;
    }

    String generisiHmac(String podaci) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);

            byte[] hmacBytes = mac.doFinal(podaci.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hmacBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Greška prilikom generisanja HMAC-a: " + e.getMessage());
        }
    }
}

