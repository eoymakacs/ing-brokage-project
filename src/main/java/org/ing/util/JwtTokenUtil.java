package org.ing.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtTokenUtil {

    private static final String SECRET_KEY = "your_secret_key_here"; // Ensure to use a secure key

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours expiration
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}