package dev.hireben.demo.common.presentation.utility;

import java.security.Key;
import java.security.PublicKey;
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

public class JwtUtil {

  // ---------------------------------------------------------------------------//
  // Dependencies
  // ---------------------------------------------------------------------------//\

  private final Supplier<JwtBuilder> builder;
  private final JwtParser parser;
  private final boolean secured;

  // ---------------------------------------------------------------------------//
  // Constructors
  // ---------------------------------------------------------------------------//

  public JwtUtil(
      Key signingKey,
      Key verificationKey,
      String issuer) {

    this.builder = signingKey == null ? () -> Jwts.builder().issuer(issuer)
        : () -> Jwts.builder().signWith(signingKey).issuer(issuer);

    if (verificationKey == null) {
      this.secured = false;
      this.parser = Jwts.parser().build();
    } else if (verificationKey instanceof SecretKey symmetricKey) {
      this.secured = true;
      this.parser = Jwts.parser().verifyWith(symmetricKey).build();
    } else if (verificationKey instanceof PublicKey publicKey) {
      this.secured = true;
      this.parser = Jwts.parser().verifyWith(publicKey).build();
    } else {
      throw new IllegalArgumentException(
          String.format("Unsupported key type for verification: %s", verificationKey.getClass()));
    }
  }

  // ---------------------------------------------------------------------------//
  // Methods
  // ---------------------------------------------------------------------------//

  public String issueToken(
      String subject,
      Collection<String> audiences,
      Map<String, Object> claims,
      TemporalAmount ttl,
      Instant nbf) {

    Instant now = Instant.now();
    Instant tokenEffective = now;

    JwtBuilder jwt = this.builder.get();

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

  // ---------------------------------------------------------------------------//

  public Claims parseToken(String token) {
    return secured ? parser.parseSignedClaims(token).getPayload() : parser.parseUnsecuredClaims(token).getPayload();
  }

}
