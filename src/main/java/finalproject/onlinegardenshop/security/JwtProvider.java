package finalproject.onlinegardenshop.security;

import finalproject.onlinegardenshop.entity.Users;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

/**
 * Component for handling JWT (JSON Web Token) operations.
 * <p>
 * This component provides methods for generating, validating, and extracting claims from JWT tokens.
 * It utilizes two secret keys for signing access tokens and refresh tokens.
 * </p>
 *
 * @Slf4j                 - Lombok annotation for generating a logger field.
 * @Component             - Indicates that an annotated class is a "component".
 *                          Such classes are considered as candidates for auto-detection
 *                          when using annotation-based configuration and classpath scanning.
 *
 * @author A-R
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
public class JwtProvider {

    /**
     * Secret key for signing access tokens.
     */
    private final SecretKey jwtAccessSecret;

    /**
     * Secret key for signing refresh tokens.
     */
    private final SecretKey jwtRefreshSecret;

    /**
     * Constructor to initialize JwtProvider with secret keys for access and refresh tokens.
     *
     * @param jwtAccessSecret  the secret key for signing access tokens.
     * @param jwtRefreshSecret the secret key for signing refresh tokens.
     */

    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    /**
     * Generates an access token for a user.
     *
     * @param user the user for whom the access token is generated.
     * @return the generated access token.
     */
    public String generateAccessToken(@NonNull Users user ) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .subject(user.getEmail())
                .expiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("roles", Set.of(user.getRole()))
                .claim("email", user.getEmail())
                .compact();
    }

    /**
     * Generates a refresh token for a user.
     *
     * @param user the user for whom the refresh token is generated.
     * @return the generated refresh token.
     */
    public String generateRefreshToken(@NonNull Users user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .subject(user.getEmail())
                .expiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    /**
     * Validates an access token.
     *
     * @param accessToken the access token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    /**
     * Validates a refresh token.
     *
     * @param refreshToken the refresh token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    /**
     * Validates a token with a specified secret key.
     *
     * @param token the token to validate.
     * @param secret the secret key used for validation.
     * @return true if the token is valid, false otherwise.
     */
    private boolean validateToken(@NonNull String token, @NonNull SecretKey secret) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported token", e);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Malformed token", e);
        } catch (Exception e) {
            log.error("Invalid token", e);
        }
        return false;
    }

    /**
     * Extracts claims from an access token.
     *
     * @param token the access token.
     * @return the claims extracted from the token.
     */
    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    /**
     * Extracts claims from a refresh token.
     *
     * @param token the refresh token.
     * @return the claims extracted from the token.
     */
    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    /**
     * Extracts claims from a token using a specified secret key.
     *
     * @param token the token.
     * @param secret the secret key used for extracting claims.
     * @return the claims extracted from the token.
     */
    private Claims getClaims(@NonNull String token, @NonNull SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                . parseSignedClaims(token)
                . getPayload();
    }

}

