package com.main.app.service;

import com.main.app.domain.model.Korisnik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class AktivacijaService {

    private static final String SECRET_KEY = "tajni_kljuc";

    @Autowired
    private final KorisnikService korisnikService;

    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    public AktivacijaService(KorisnikService korisnikService, JavaMailSender javaMailSender) {
        this.korisnikService = korisnikService;
        this.mailSender = javaMailSender;
    }


    public String generisiAktivacioniLink(Long korisnikId) {
        String timestamp = Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        String podaci = korisnikId + ":" + timestamp;

        String hmac = generisiHmac(podaci);

        String link = "https://localhost:443/korisnik/aktivacija/" + korisnikId + "?timestamp=" + timestamp + "&hmac=" + hmac;

        return link;
    }

    public String generisiHmac(String podaci) {
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

    private String kodPoslatNaMejl;

    public boolean resetujSifru(String email, String kod, String novaSifra) {
        Korisnik korisnik = korisnikService.getKorisnikByEmail(email);

        if (korisnik == null) {
            throw new RuntimeException("Korisnik sa datim emailom ne postoji.");
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String podaci = korisnik.getId() + ":" + timestamp;

        if (kod == null || !kod.equals(kodPoslatNaMejl)) {
            throw new RuntimeException("Neispravan kod za oporavak naloga.");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String enkodiranaSifra = encoder.encode(novaSifra);

        korisnik.setPassword(enkodiranaSifra);

        korisnikService.saveKorisnik(korisnik);

        return true;
    }

    public void posaljiKodZaResetovanje(String email, String kod) {
        String subject = "Resetovanje šifre";
        String message = "Vaš kod za resetovanje šifre je: " + kod;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try {
            mailSender.send(mailMessage);
            System.out.println("Kod za resetovanje šifre poslat na: " + email);
            kodPoslatNaMejl = kod; // Dodela vrednosti kodu poslatom na mejl
        } catch (MailException e) {
            System.out.println("Greška prilikom slanja e-pošte: " + e.getMessage());
        }
    }




}

