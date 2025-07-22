// TokenService.java

package me.project.authorization_service.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import me.project.authorization_service.dto.Jwt;
import me.project.authorization_service.exception.InvalidRefreshTokenException;
import me.project.authorization_service.exception.InvalidUsernameException;
import me.project.authorization_service.exception.UserNotFoundException;
import me.project.authorization_service.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    @Value("${jwt.expirationMsRefresh}")
    private long expirationMsRefresh;

    @Autowired
    private UserDetailsService userDetailsService;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Генерация пары токенов (access + refresh) для пользователя
     */
    public Jwt createToken(Client client) {
        String accessToken = createAccessToken(client);
        String refreshToken = createRefreshToken(client);
        return new Jwt(accessToken, refreshToken);
    }

    private String createAccessToken(Client client) {
        return createToken(client, expirationMs);
    }

    private String createRefreshToken(Client client) {
        return createToken(client, expirationMsRefresh);
    }

    /**
     * Создание JWT с нужными claims и временем жизни
     */
    private String createToken(Client client, long validity) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", client.getUsername());
        claims.put("email", client.getEmail());
        claims.put("role", client.getRole().name());

        List<String> authorities = client.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("authorities", authorities);

        return Jwts.builder()
                .subject(client.getUsername())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * Извлечение username из токена (из поля subject)
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Обобщённый метод для извлечения claim'а
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Парсинг всех claims из токена
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Неверный JWT токен", e);
        }
    }

    /**
     * Проверка, истёк ли срок действия токена
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Извлечение даты истечения
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Валидация токена: проверяет, не пустой ли username и не просрочен ли
     */
    public boolean validateToken(String token) {
        String username = extractUsername(token);
        return (username != null && !isTokenExpired(token) && !isTokenBlacklisted(token));
    }

    /**
     * Обновление токенов с помощью refresh token
     */
    public Jwt refreshToken(Jwt jwt) {

        String refreshToken = jwt.refreshToken();
        if (refreshToken.isEmpty() ||
                isTokenBlacklisted(refreshToken) ||
                isTokenExpired(refreshToken)) {
            throw new InvalidRefreshTokenException("problem with revoked refresh token");
        }

        String username = extractUsername(refreshToken);
        if (username == null || username.isEmpty()) {
            throw new InvalidUsernameException("invalid extracted username");
        }

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException("cannot find user " + username);
        }

        blacklistToken(jwt.accessToken());
        blacklistToken(jwt.refreshToken());
        return createToken((Client) userDetails);
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        String role = extractClaim(token, claims -> (String) claims.get("role"));
        if (role == null || role.isBlank()) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }
}