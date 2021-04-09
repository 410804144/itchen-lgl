package vip.itchen.support;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 3600L * 24 * 2 * 1000;

    /**
     * generate token for uid
     */
    public static String generateToken(String uid, String jwtSecret) {
        Map<String, Object> claims = new HashMap<>(16);
        return doGenerateToken(claims, uid, jwtSecret, JWT_TOKEN_VALIDITY);
    }

    /**
     * generate token for uid
     */
    public static String generateToken(String uid, String jwtSecret, Long tokenValidity) {
        Map<String, Object> claims = new HashMap<>(16);
        return doGenerateToken(claims, uid, jwtSecret, tokenValidity);
    }

    /**
     * while creating the token -
     * 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
     * 2. Sign the JWT using the HS512 algorithm and secret key.
     * 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
     * 4. compaction of the JWT to a URL-safe string
     */
    private static String doGenerateToken(Map<String, Object> claims, String subject, String jwtSecret, Long tokenValidity) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * for retrieving any information from token we will need the secret key
     */
    public static Claims getAllClaimsFromToken(String token, String jwtSecret) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }
}