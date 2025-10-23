package dev.hireben.demo.common.utility;

import java.security.KeyPair;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.jsonwebtoken.Jwts;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtUtilTests {

  private final String ISSUER = this.getClass().getName();
  private JwtUtil withAsymmKeys;
  private JwtUtil withSymmKey;
  private JwtUtil withoutKey;

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
  void testIssueTokenWithAsymmetricKeys() {
    withAsymmKeys.issueToken(ISSUER, null, null, null, null);
  }

  // -----------------------------------------------------------------------------

  @Test
  void testIssueTokenWithSymmetricKey() {
    withSymmKey.issueToken(ISSUER, null, null, null, null);
  }

  // -----------------------------------------------------------------------------

  @Test
  void testIssueTokenWithoutKey() {
    withoutKey.issueToken(ISSUER, null, null, null, null);
  }

}
