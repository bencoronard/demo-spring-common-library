package dev.hireben.demo.common_libs.utility.jwt;

import java.security.KeyPair;

import javax.crypto.SecretKey;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import dev.hireben.demo.common_libs.utility.jwt.api.JwtIssuer;
import dev.hireben.demo.common_libs.utility.jwt.api.JwtVerifier;
import io.jsonwebtoken.Jwts;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
final class JwtClientsTests {

  private final String ISSUER = this.getClass().getSimpleName();
  private final SecretKey symmKey = Jwts.SIG.HS256.key().build();
  private final KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();

  // =============================================================================

  @Test
  void testNewVerifier() {
    JwtVerifier expected = new JwtVerifierImpl();
    JwtVerifier actual = JwtClients.newVerifier();

    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  // -----------------------------------------------------------------------------

  @Test
  void testNewVerifierWithSymmetricKey() {
    JwtVerifier expected = new JwtVerifierImpl(symmKey);
    JwtVerifier actual = JwtClients.newVerifierWithSymmetricKey(symmKey);

    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  // -----------------------------------------------------------------------------

  @Test
  void testNewVerifierWithPublicKey() {
    JwtVerifier expected = new JwtVerifierImpl(keyPair.getPublic());
    JwtVerifier actual = JwtClients.newVerifierWithPublicKey(keyPair.getPublic());

    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  // -----------------------------------------------------------------------------

  @Test
  void testNewIssuer() {
    JwtIssuer expected = new JwtIssuerImpl(ISSUER);
    JwtIssuer actual = JwtClients.newIssuer(ISSUER);

    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  // -----------------------------------------------------------------------------

  @Test
  void testNewIssuerWithSymmetricKey() {
    JwtIssuer expected = new JwtIssuerImpl(ISSUER, symmKey);
    JwtIssuer actual = JwtClients.newIssuerWithSymmetricKey(ISSUER, symmKey);

    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  // -----------------------------------------------------------------------------

  @Test
  void testNewIssuerWithPrivateKey() {
    JwtIssuer expected = new JwtIssuerImpl(ISSUER, keyPair.getPrivate());
    JwtIssuer actual = JwtClients.newIssuerWithPrivateKey(ISSUER, keyPair.getPrivate());

    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

}
