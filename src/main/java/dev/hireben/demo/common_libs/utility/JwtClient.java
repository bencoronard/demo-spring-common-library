package dev.hireben.demo.common_libs.utility;

import java.security.KeyPair;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

public final class JwtClient {

  private final Supplier<JwtBuilder> builder;
  private final JwtParser parser;
  private final boolean secured;

  // =============================================================================

  public JwtClient(String issuer) {
    secured = false;
    builder = () -> Jwts.builder().issuer(issuer);
    parser = Jwts.parser().unsecured().build();
  }

  // -----------------------------------------------------------------------------

  public JwtClient(String issuer, SecretKey key) {
    secured = true;
    builder = () -> Jwts.builder().signWith(key).issuer(issuer);
    parser = Jwts.parser().verifyWith(key).build();
  }

  // -----------------------------------------------------------------------------

  public JwtClient(String issuer, KeyPair keyPair) {
    secured = true;
    builder = keyPair.getPrivate() != null ? () -> Jwts.builder().signWith(keyPair.getPrivate()).issuer(issuer) : null;
    parser = Jwts.parser().verifyWith(keyPair.getPublic()).build();
  }

  // =============================================================================

  public String issueToken(
      String subject,
      Collection<String> audiences,
      Map<String, Object> claims,
      TemporalAmount ttl,
      Instant nbf) {

    if (builder == null) {
      return null;
    }

    Instant now = Instant.now();
    Instant tokenEffective = now;

    JwtBuilder jwt = builder.get();

    if (nbf != null) {
      tokenEffective = nbf;
      jwt.notBefore(Date.from(tokenEffective));
    }

    if (ttl != null) {
      Instant expAt = tokenEffective.plus(ttl);
      if (expAt.isBefore(now)) {
        throw new IllegalArgumentException("Token expiration cannot be in the past");
      }
      jwt.expiration(Date.from(expAt));
    }

    jwt.id(UUID.randomUUID().toString())
        .subject(subject)
        .issuedAt(Date.from(now));

    if (audiences != null && !audiences.isEmpty()) {
      audiences.forEach(audience -> jwt.audience().add(audience));
    }

    if (claims != null && !claims.isEmpty()) {
      jwt.claims(claims);
    }

    return jwt.compact();
  }

  // -----------------------------------------------------------------------------

  public Claims parseToken(String token) {
    return secured ? parser.parseSignedClaims(token).getPayload()
        : parser.parseUnsecuredClaims(token).getPayload();
  }

}
