package dev.hireben.demo.common;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import dev.hireben.demo.common.presentation.utility.JwtUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtUtilTests {

  // ---------------------------------------------------------------------------//
  // Fields
  // ---------------------------------------------------------------------------//

  private final String ISSUER = this.getClass().getName();
  private JwtUtil withAsymmKeys;
  private JwtUtil withSymmKey;
  private JwtUtil withoutKey;

  // ---------------------------------------------------------------------------//
  // Methods
  // ---------------------------------------------------------------------------//

  @BeforeAll
  void setup() throws NoSuchAlgorithmException {
    // Generate RSA key pair
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
    keyPairGen.initialize(2048);
    KeyPair keyPair = keyPairGen.generateKeyPair();

    // Generate AES symmetric key
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(256);
    SecretKey symmKey = keyGen.generateKey();

    // Construct JwtUtil instances
    this.withAsymmKeys = new JwtUtil(keyPair.getPrivate(), keyPair.getPublic(), ISSUER);
    this.withSymmKey = new JwtUtil(symmKey, symmKey, ISSUER);
    this.withoutKey = new JwtUtil(null, null, ISSUER);
  }

  // ---------------------------------------------------------------------------//

  @Test
  void testIssueTokenWithAsymmetricKeys() {
    withAsymmKeys.issueToken(ISSUER, null, null, null, null);
  }

  // ---------------------------------------------------------------------------//

  @Test
  void testIssueTokenWithSymmetricKey() {
    withSymmKey.issueToken(ISSUER, null, null, null, null);
  }

  // ---------------------------------------------------------------------------//

  @Test
  void testIssueTokenWithoutKey() {
    withoutKey.issueToken(ISSUER, null, null, null, null);
  }

}
