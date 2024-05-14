package com.example.myapplication.token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TokenProvider {

    private static final String SECRET_KEY = "yourSecretKey";

    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256))
                .compact();
    }
}
