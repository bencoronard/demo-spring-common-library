package dev.hireben.demo.common_libs.utility;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.security.KeyPair;
import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
final class JwtClientTests {

  private JwtClient withoutKey;
  private JwtClient withSymmKey;
  private JwtClient withAsymmKeys;

  // -----------------------------------------------------------------------------

  private final String ISSUER = this.getClass().getSimpleName();

  // =============================================================================

  @BeforeAll
  void setup() {

    SecretKey symmKey = Jwts.SIG.HS256.key().build();
    KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();

    withoutKey = new JwtClient(ISSUER);
    withSymmKey = new JwtClient(ISSUER, symmKey);
    withAsymmKeys = new JwtClient(ISSUER, keyPair);
  }

  // =============================================================================

  @Test
  void testIssueAndParseTokenWithAsymmetricKeys() {
    String token = withAsymmKeys.issueToken(null, null, null, null, null);
    assertThat(token).isNotBlank();

    Claims claims = withAsymmKeys.parseToken(token);
    assertNotNull(claims);
    assertNotNull(claims.getId());
    assertNotNull(claims.getIssuedAt());
  }

  // -----------------------------------------------------------------------------

  @Test
  void testIssueAndParseTokenWithSymmetricKey() {
    String token = withSymmKey.issueToken(null, null, null, null, null);
    assertThat(token).isNotBlank();

    Claims claims = withSymmKey.parseToken(token);
    assertNotNull(claims);
    assertNotNull(claims.getId());
    assertNotNull(claims.getIssuedAt());
  }

  // -----------------------------------------------------------------------------

  @Test
  void testIssueAndParseTokenWithoutKey() {
    String token = withoutKey.issueToken(null, null, null, null, null);
    assertThat(token).isNotBlank();

    Claims claims = withoutKey.parseToken(token);
    assertNotNull(claims);
    assertNotNull(claims.getId());
    assertNotNull(claims.getIssuedAt());
  }

}
