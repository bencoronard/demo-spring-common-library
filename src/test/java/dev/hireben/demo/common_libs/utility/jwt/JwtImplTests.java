package dev.hireben.demo.common_libs.utility.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.jsonwebtoken.Jwts;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
final class JwtImplTests {

  private final String ISSUER = this.getClass().getSimpleName();
  private final SecretKey symmKey = Jwts.SIG.HS256.key().build();
  private final KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();

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
  // -----------------------------------------------------------------------------
  // -----------------------------------------------------------------------------
  // -----------------------------------------------------------------------------
  // -----------------------------------------------------------------------------
  // -----------------------------------------------------------------------------
  // -----------------------------------------------------------------------------
  // -----------------------------------------------------------------------------
  // -----------------------------------------------------------------------------
  // -----------------------------------------------------------------------------

}
