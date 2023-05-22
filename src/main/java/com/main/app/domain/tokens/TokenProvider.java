package com.main.app.domain.tokens;

import com.main.app.domain.model.Korisnik;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.UUID;

@Component
public class TokenProvider {

    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 15 * 60 * 1000; // 15 minuta
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1 * 24 * 60 * 60 * 1000; //1 dan
    private static final String SECRET_KEY = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1a2b3c4d5e6f7g8h9iaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

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
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);

        Claims claims = Jwts.claims().setSubject(korisnik.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
