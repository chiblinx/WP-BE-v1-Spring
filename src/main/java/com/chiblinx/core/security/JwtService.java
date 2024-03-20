package com.chiblinx.core.security;

import com.chiblinx.core.security.properties.JwtConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtService {

  private final JwtConfigProperties jwtConfigProperties;

  private static PublicKey getPublicKey(String encodedKey) throws Exception {
    byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePublic(keySpec);
  }

  private static PrivateKey getPrivateKey(String encodedKey) throws Exception {
    byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePrivate(keySpec);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws Exception {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) throws Exception {
    return Jwts
        .parser()
        .verifyWith(getPublicKey(jwtConfigProperties.getPublicKey()))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private Date extractExpiration(String token) throws Exception {
    return extractClaim(token, Claims::getExpiration);
  }

  public String extractUsername(String token) throws Exception {
    return extractClaim(token, Claims::getSubject);
  }

  public String generateToken(UserDetails userDetails, Integer expiry) throws Exception {
    return generateToken(new HashMap<>(), userDetails, expiry);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails,
      Integer expiry)
      throws Exception {
    return Jwts
        .builder().claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiry))
        .signWith(getPrivateKey(jwtConfigProperties.getPrivateKey()), SIG.RS512)
        .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) throws Exception {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) throws Exception {
    return extractExpiration(token).before(new Date());
  }

}
