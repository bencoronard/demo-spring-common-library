package dev.hireben.demo.common_libs.factory;

import java.security.KeyPair;

import javax.crypto.SecretKey;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import dev.hireben.demo.common_libs.utility.JwtClient;
import io.jsonwebtoken.Jwts;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
final class JwtClientFactoryTests {

  private final String ISSUER = this.getClass().getSimpleName();

  private final KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();
  private final SecretKey symmKey = Jwts.SIG.HS256.key().build();

  // =============================================================================

  @Test
  void testBuildJwtClientWithoutKey() {
    JwtClient expectedClient = new JwtClient(ISSUER);
    JwtClient actualClient = JwtClientFactory.buildJwtClientWithoutKey(ISSUER);

    Assertions.assertThat(actualClient)
        .usingRecursiveComparison()
        .isEqualTo(expectedClient);
  }

  // -----------------------------------------------------------------------------

  @Test
  void testBuildJwtClientWithSymmetricKey() {
    JwtClient expectedClient = new JwtClient(ISSUER, symmKey);
    JwtClient actualClient = JwtClientFactory.buildJwtClientWithSymmetricKey(ISSUER, symmKey);

    Assertions.assertThat(actualClient)
        .usingRecursiveComparison()
        .isEqualTo(expectedClient);
  }

  // -----------------------------------------------------------------------------

  @Test
  void testBuildJwtClientWithAsymmmetricKeys() {
    JwtClient expectedClient = new JwtClient(ISSUER, keyPair);
    JwtClient actualClient = JwtClientFactory.buildJwtClientWithAsymmmetricKeys(ISSUER, keyPair);

    Assertions.assertThat(actualClient)
        .usingRecursiveComparison()
        .isEqualTo(expectedClient);
  }

}
