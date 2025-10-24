package dev.hireben.demo.common.utility;

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
public class JwtUtilTests {

  private JwtUtil withAsymmKeys;
  private JwtUtil withSymmKey;
  private JwtUtil withoutKey;

  // -----------------------------------------------------------------------------

  private final String ISSUER = this.getClass().getSimpleName();

  // =============================================================================

  @BeforeAll
  void setup() {

    KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();
    SecretKey symmKey = Jwts.SIG.HS256.key().build();

    withAsymmKeys = new JwtUtil(keyPair.getPrivate(), keyPair.getPublic(), ISSUER);
    withSymmKey = new JwtUtil(symmKey, symmKey, ISSUER);
    withoutKey = new JwtUtil(null, null, ISSUER);
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

  // -----------------------------------------------------------------------------

}
