package com.example.Jwt.example.jwtsecurity;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/*
 * 
 * The JwtService class centralizes all JWT-related operations by injecting configuration properties for the signing key and expiration time, 
 * extracting specific claims (like username and expiration) from incoming tokens, 
 * generating new signed tokens with arbitrary claims and timestamps, 
 * validating tokens against user details and expiry, and managing the cryptographic signing key via the JJWT Keys.hmacShaKeyFor utility. 
 * This encapsulation ensures a single, reusable component for JWT handling across the application.
 * 
 * */

@Component
public class JwtService {
    
	/*
	 * injecting the SECRET_KEY and expirationTime fields from the application.properties
	 * */
	
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    
    @Value("${jwt.expiration-time}")
    private long expirationTime;
    
    /*
     * Claim Extraction : 
     * extractUsername and extractExpiration both delegate to the generic extractClaim method, which in turn calls extractAllClaims to parse the token’s payload. 
     * Internally, extractAllClaims uses JJWT’s Jwts.parser() API with the configured signing key to verify the signature and produce a Claims object, 
     * from which individual claims are retrieved via the provided resolver function. 
     * This approach cleanly separates parsing, signature verification, and claim extraction into distinct steps.
     * 
     * JWT claims are individual pieces of information asserted about a user or system, carried in the token’s payload as name/value pairs to convey identity 
     * or metadata; for example, a claim named 'sub' might carry the username of the authenticated user.
     * 
     * Claims are name/value pairs in the JWT payload that assert facts about the user or other data. 
     * Standard claims like iss (issuer), sub (subject), and exp (expiration) are defined by the spec, and applications can include custom claims. 
     * Receivers read these claims to identify the token holder and verify the token’s validity without extra database lookups.
     *
     * Example of a Claim :
     *     {
  	 *		  "sub": "alice@example.com"
  	 *		}
  	 *
     * */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /*
     * 
     * Token Generation :
     * The two overloaded generateToken methods build a new JWT using JJWT’s fluent Jwts.builder() interface. 
     * They set custom claims, subject (the username), issuance and expiration timestamps computed from Instant.now() plus the injected expirationTime, 
     * and then cryptographically sign the token using signWith(getSigningKey(), Jwts.SIG.HS256). Finally, compact() serializes the JWT into the standard three-part Base64URL string.
     * 
     * */
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(expirationTime)))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }
    
    /*
     * 
     * Token Validation: 
     * validateToken first extracts the username from the token and compares it to the authenticated user’s username, 
     * then confirms that the token has not yet expired by checking extractExpiration(token).before(new Date()). 
     * This dual check guards both identity integrity and temporal validity, returning true only when both conditions pass.
     * 
     * */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) 
        		&& !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
}