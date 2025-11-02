package dev.hireben.demo.common_libs.utility.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import dev.hireben.demo.common_libs.utility.jwt.api.JwtIssuer;
import dev.hireben.demo.common_libs.utility.jwt.api.JwtVerifier;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
final class JwtImplTests {

  private final String ISSUER = getClass().getSimpleName();
  private final SecretKey SYMM_KEY = Jwts.SIG.HS256.key().build();
  private final KeyPair KEY_PAIR = Jwts.SIG.RS256.keyPair().build();

  // =============================================================================

  @Test
  void testJwtVerifierConstructorWithNullSymmetricKey() {
    Exception exception = assertThrows(NullPointerException.class, () -> new JwtVerifierImpl((SecretKey) null));
    assertEquals("Symmetric key must not be null", exception.getMessage());
  }

  // -----------------------------------------------------------------------------

  @Test
  void testJwtVerifierConstructorWithNullPublicKey() {
    Exception exception = assertThrows(NullPointerException.class, () -> new JwtVerifierImpl((PublicKey) null));
    assertEquals("Public key must not be null", exception.getMessage());
  }

  // -----------------------------------------------------------------------------

  @Test
  void testJwtIssuerConstructorWithNullSymmetricKey() {
    Exception exception = assertThrows(NullPointerException.class, () -> new JwtIssuerImpl(ISSUER, (SecretKey) null));
    assertEquals("Symmetric key must not be null", exception.getMessage());
  }

  // -----------------------------------------------------------------------------

  @Test
  void testJwtIssuerConstructorWithNullPrivateKey() {
    Exception exception = assertThrows(NullPointerException.class, () -> new JwtIssuerImpl(ISSUER, (PrivateKey) null));
    assertEquals("Private key must not be null", exception.getMessage());
  }

  // -----------------------------------------------------------------------------

  @Test
  void testIssueAndParseTokenWithoutKey() {
    JwtIssuer issuer = new JwtIssuerImpl(ISSUER);
    JwtVerifier verifier = new JwtVerifierImpl();

    String token = issuer.issueToken(null, null, null, null, null);
    Assertions.assertThat(token).isNotBlank();

    Claims claims = verifier.verifyToken(token);
    assertNotNull(claims);
    assertNotNull(claims.getId());
    assertNotNull(claims.getIssuedAt());
  }

  // -----------------------------------------------------------------------------

  @Test
  void testIssueAndParseTokenWithSymmetricKey() {
    JwtIssuer issuer = new JwtIssuerImpl(ISSUER, SYMM_KEY);
    JwtVerifier verifier = new JwtVerifierImpl(SYMM_KEY);

    String token = issuer.issueToken(null, null, null, null, null);
    Assertions.assertThat(token).isNotBlank();

    Claims claims = verifier.verifyToken(token);
    assertNotNull(claims);
    assertNotNull(claims.getId());
    assertNotNull(claims.getIssuedAt());
  }

  // -----------------------------------------------------------------------------

  @Test
  void testIssueAndParseTokenWithAsymmetricKeys() {
    JwtIssuer issuer = new JwtIssuerImpl(ISSUER, KEY_PAIR.getPrivate());
    JwtVerifier verifier = new JwtVerifierImpl(KEY_PAIR.getPublic());

    String token = issuer.issueToken(null, null, null, null, null);
    Assertions.assertThat(token).isNotBlank();

    Claims claims = verifier.verifyToken(token);
    assertNotNull(claims);
    assertNotNull(claims.getId());
    assertNotNull(claims.getIssuedAt());
  }

}
