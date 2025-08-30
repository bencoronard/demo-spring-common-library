package dev.hireben.demo.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.hireben.demo.common.presentation.utility.JwtUtil;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTestDemo {

  private SecretKey key;

  @BeforeEach
  public void setup() {
    key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  }

  @Test
  public void testIssueTokenWithAllFields() {
    JwtUtil jwtUtil = new JwtUtil(key, key, "issuer");

    Instant nbf = Instant.now().plusSeconds(60);
    Duration ttl = Duration.ofMinutes(5);

    Map<String, Object> claims = new HashMap<>();
    claims.put("role", "admin");

    List<String> audiences = List.of("aud1", "aud2");

    String token = jwtUtil.issueToken("subject", audiences, claims, ttl, nbf);
    Claims parsed = jwtUtil.parseToken(token);

    assertEquals("subject", parsed.getSubject());
    assertEquals("admin", parsed.get("role"));
    assertEquals("issuer", parsed.getIssuer());
    assertTrue(parsed.getAudience().contains("aud1"));
  }

  @Test
  public void testIssueTokenWithoutNbfAndTtl() {
    JwtUtil jwtUtil = new JwtUtil(key, key, "issuer");

    String token = jwtUtil.issueToken("user", null, null, null, null);
    Claims claims = jwtUtil.parseToken(token);

    assertEquals("user", claims.getSubject());
    assertEquals("issuer", claims.getIssuer());
    assertNotNull(claims.getIssuedAt());
    assertNotNull(claims.getId());
  }

  @Test
  public void testIssueTokenWithPastExpirationShouldThrow() {
    JwtUtil jwtUtil = new JwtUtil(key, key, "issuer");

    Instant nbf = Instant.now().plusSeconds(10);
    Duration ttl = Duration.ofSeconds(-20);

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> jwtUtil.issueToken("user", null, null, ttl, nbf));

    assertEquals("Token expiration cannot be in the past", exception.getMessage());
  }

  @Test
  public void testUnsecuredTokenParse() {
    JwtUtil jwtUtil = new JwtUtil(null, null, "issuer");

    String token = jwtUtil.issueToken("unsecured", null, null, null, null);
    Claims claims = jwtUtil.parseToken(token);

    assertEquals("unsecured", claims.getSubject());
  }

  @Test
  public void testUnsupportedKeyThrows() {
    Key unsupportedKey = new Key() {
      @Override
      public String getAlgorithm() {
        return "none";
      }

      @Override
      public String getFormat() {
        return null;
      }

      @Override
      public byte[] getEncoded() {
        return new byte[0];
      }
    };

    assertThrows(IllegalArgumentException.class, () -> new JwtUtil(key, unsupportedKey, "issuer"));
  }
}
