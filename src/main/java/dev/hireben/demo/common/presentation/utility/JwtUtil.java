package dev.hireben.demo.common.presentation.utility;

import java.security.Key;
import java.security.PublicKey;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

import javax.crypto.SecretKey;

import org.springframework.lang.Nullable;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.validation.constraints.NotBlank;

public class JwtUtil {

  // ---------------------------------------------------------------------------//
  // Dependencies
  // ---------------------------------------------------------------------------//\

  private final Supplier<JwtBuilder> builder;
  private final JwtParser parser;

  // ---------------------------------------------------------------------------//
  // Constructors
  // ---------------------------------------------------------------------------//

  public JwtUtil(
      @Nullable Key signingKey,
      @Nullable Key verificationKey,
      @NotBlank String issuer) {

    this.builder = signingKey == null ? () -> Jwts.builder().issuer(issuer)
        : () -> Jwts.builder().signWith(signingKey).issuer(issuer);

    // Handle verification key setup
    if (verificationKey == null) {
      this.parser = Jwts.parser().build();
    } else if (verificationKey instanceof SecretKey symmetricKey) {
      this.parser = Jwts.parser().verifyWith(symmetricKey).build();
    } else if (verificationKey instanceof PublicKey publicKey) {
      this.parser = Jwts.parser().verifyWith(publicKey).build();
    } else {
      throw new IllegalArgumentException(
          String.format("Unsupported key type for verification: %s", verificationKey.getClass()));
    }
  }

  // ---------------------------------------------------------------------------//
  // Methods
  // ---------------------------------------------------------------------------//

  public String issueToken(String recipient, Map<String, Object> claims, TemporalAmount ttl) {
    Instant now = Instant.now();
    return this.builder.get()
        .subject(recipient)
        .claims(claims)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plus(ttl)))
        .compact();
  }

  // ---------------------------------------------------------------------------//

  public Claims validateToken(String token) {
    return parser.parseSignedClaims(token).getPayload();
  }

}
