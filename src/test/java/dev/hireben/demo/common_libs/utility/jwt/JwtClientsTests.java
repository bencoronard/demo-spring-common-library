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
  private final SecretKey SYMM_KEY = Jwts.SIG.HS256.key().build();
  private final KeyPair KEY_PAIR = Jwts.SIG.RS256.keyPair().build();

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
    JwtVerifier expected = new JwtVerifierImpl(SYMM_KEY);
    JwtVerifier actual = JwtClients.newVerifierWithSymmetricKey(SYMM_KEY);

    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  // -----------------------------------------------------------------------------

  @Test
  void testNewVerifierWithPublicKey() {
    JwtVerifier expected = new JwtVerifierImpl(KEY_PAIR.getPublic());
    JwtVerifier actual = JwtClients.newVerifierWithPublicKey(KEY_PAIR.getPublic());

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
    JwtIssuer expected = new JwtIssuerImpl(ISSUER, SYMM_KEY);
    JwtIssuer actual = JwtClients.newIssuerWithSymmetricKey(ISSUER, SYMM_KEY);

    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  // -----------------------------------------------------------------------------

  @Test
  void testNewIssuerWithPrivateKey() {
    JwtIssuer expected = new JwtIssuerImpl(ISSUER, KEY_PAIR.getPrivate());
    JwtIssuer actual = JwtClients.newIssuerWithPrivateKey(ISSUER, KEY_PAIR.getPrivate());

    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

}
