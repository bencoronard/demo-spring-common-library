package dev.hireben.demo.common_libs.utility.jwt;

import java.security.PublicKey;
import java.util.Objects;

import javax.crypto.SecretKey;

import dev.hireben.demo.common_libs.utility.jwt.api.JwtVerifier;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

final class JwtVerifierImpl implements JwtVerifier {

  private final JwtParser parser;
  private final boolean secured;

  // =============================================================================

  JwtVerifierImpl() {
    secured = false;
    parser = Jwts.parser().unsecured().build();
  }

  // -----------------------------------------------------------------------------

  JwtVerifierImpl(SecretKey key) {
    Objects.requireNonNull(key, "Symmetric key must not be null");
    secured = true;
    parser = Jwts.parser().verifyWith(key).build();
  }

  // -----------------------------------------------------------------------------

  JwtVerifierImpl(PublicKey key) {
    Objects.requireNonNull(key, "Public key must not be null");
    secured = true;
    parser = Jwts.parser().verifyWith(key).build();
  }

  // =============================================================================

  @Override
  public Claims verifyToken(String token) {
    return secured ? parser.parseSignedClaims(token).getPayload() : parser.parseUnsecuredClaims(token).getPayload();
  }

}
