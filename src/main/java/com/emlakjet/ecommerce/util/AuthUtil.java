package com.emlakjet.ecommerce.util;


import com.emlakjet.ecommerce.exception.ServiceException;
import com.emlakjet.ecommerce.model.CommerceUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.emlakjet.ecommerce.constants.Messages.EXPIRED_TOKEN;
import static com.emlakjet.ecommerce.constants.Messages.INVALID_TOKEN;

@Component
public class AuthUtil {
    private static final String SECRET_KEY = "jBIQn7lQCP6kq3lSEtnCuw4mbc3r4bha1rXA6oA2WqFHQyC5zUyAk";
    private static final long EXPIRATION_TIME = 43200000; // 12 hours

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * Extracts the username from the given token.
     *
     * @param token the JWT token
     * @return the username from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a claim from the given token using the given claims resolver.
     *
     * @param token          the JWT token
     * @param claimsResolver the resolver to use to extract the claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * @return the currently authenticated user, or {@code null} if no user is authenticated
     */
    public CommerceUser getUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return (CommerceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Retrieves the ID of the currently authenticated user from the security context.
     *
     * @return the ID of the currently authenticated user, or {@code null} if no user is authenticated
     */
    public String getUserId() {
        var user = getUser();
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    /**
     * Generates a JWT token for the given user details with default claims.
     *
     * @param userDetails the user details for which the token is generated
     * @return a JWT token string
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token for the given user details with the given extra claims and the default expiration time.
     *
     * @param extraClaims the extra claims to include in the token
     * @param userDetails the user details for which the token is generated
     * @return a JWT token string
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, EXPIRATION_TIME);
    }

    /**
     * Retrieves the default expiration time for JWT tokens in milliseconds.
     *
     * @return the default expiration time for JWT tokens in milliseconds
     */
    public long getExpirationTime() {
        return EXPIRATION_TIME;
    }

    /**
     * Generates a JWT token string for the given user details with the given extra claims and expiration time.
     *
     * @param extraClaims the extra claims to include in the token
     * @param userDetails the user details for which the token is generated
     * @param expiration  the expiration time for the token in milliseconds
     * @return a JWT token string
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    /**
     * Verifies that the given token is valid for the given user details.
     * A token is valid if it has not expired and its subject matches the given user details.
     *
     * @param token       the JWT token to verify
     * @param userDetails the user details for which the token is verified
     * @return whether the given token is valid for the given user details
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        var username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token the JWT token to check
     * @return {@code true} if the token is expired, {@code false} otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token the JWT token from which the expiration date is extracted
     * @return the expiration date of the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the given JWT token.
     * The token is verified against the secret key before claims are extracted.
     *
     * @param token the JWT token from which all claims are extracted
     * @return a claims object containing all claims from the token
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException e) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, EXPIRED_TOKEN);
        }
    }
}
