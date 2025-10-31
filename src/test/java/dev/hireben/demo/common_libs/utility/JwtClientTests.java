package dev.hireben.demo.common_libs.utility;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
final class JwtClientTests {

  private final String ISSUER = this.getClass().getSimpleName();

  private final JwtClient withoutKey = new JwtClient(ISSUER);
  private final JwtClient withSymmKey = new JwtClient(ISSUER, Jwts.SIG.HS256.key().build());
  private final JwtClient withAsymmKeys = new JwtClient(ISSUER, Jwts.SIG.RS256.keyPair().build());

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
