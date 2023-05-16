package com.main.app.domain.tokens;

import com.main.app.domain.model.Korisnik;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;


import java.util.Date;

@Component
public class TokenProvider {

    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60 * 1000; // 30 minuta
    private static final String SECRET_KEY = "your-secret-key"; // Postavite tajni ključ koji će se koristiti za potpisivanje tokena

    public String generateAccessToken(Korisnik korisnik) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);

        Claims claims = Jwts.claims().setSubject(korisnik.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // Implementirajte generisanje refresh tokena prema vašim potrebama
    public String generateRefreshToken(Korisnik korisnik) {
        // Implementacija za generisanje refresh tokena
        return null;
    }
}
