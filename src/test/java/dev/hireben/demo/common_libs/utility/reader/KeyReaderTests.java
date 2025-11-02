package dev.hireben.demo.common_libs.utility.reader;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
final class KeyReaderTests {

  private final String RSA_PRIVATE_KEY_PKCS8;
  private final String RSA_PUBLIC_KEY_X509;

  // =============================================================================

  KeyReaderTests() throws IOException {
    byte[] privateKeyFile = getClass().getResourceAsStream("/rsa-private-pkcs8.pem").readAllBytes();
    byte[] publicKeyFile = getClass().getResourceAsStream("/rsa-public-x509.pem").readAllBytes();

    RSA_PRIVATE_KEY_PKCS8 = new String(privateKeyFile);
    RSA_PUBLIC_KEY_X509 = new String(publicKeyFile);
  }

  // -----------------------------------------------------------------------------

  @Test
  void testReadRsaPrivateKeyPkcs8() throws NoSuchAlgorithmException, InvalidKeySpecException {
    PrivateKey key = KeyReader.readRsaPrivateKeyPkcs8(RSA_PRIVATE_KEY_PKCS8);
    assertNotNull(key);
    assertNotNull(key.getEncoded());
    assertEquals("RSA", key.getAlgorithm());
    assertEquals("PKCS#8", key.getFormat());
  }

  // -----------------------------------------------------------------------------

  @Test
  void testReadRsaPublicKeyX509() throws NoSuchAlgorithmException,
      InvalidKeySpecException {
    PublicKey key = KeyReader.readRsaPublicKeyX509(RSA_PUBLIC_KEY_X509);
    assertNotNull(key);
    assertNotNull(key.getEncoded());
    assertEquals("RSA", key.getAlgorithm());
    assertEquals("X.509", key.getFormat());
  }

  // -----------------------------------------------------------------------------

  @Test
  void testPublicKeyMatchesPrivateKey() throws NoSuchAlgorithmException,
      InvalidKeySpecException {
    PrivateKey privateKey = KeyReader.readRsaPrivateKeyPkcs8(RSA_PRIVATE_KEY_PKCS8);
    PublicKey publicKey = KeyReader.readRsaPublicKeyX509(RSA_PUBLIC_KEY_X509);

    KeyFactory factory = KeyFactory.getInstance("RSA");
    RSAPrivateCrtKey privateCrtKey = (RSAPrivateCrtKey) privateKey;
    RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
        privateCrtKey.getModulus(),
        privateCrtKey.getPublicExponent());

    PublicKey derivedPublicKey = factory.generatePublic(publicKeySpec);

    assertArrayEquals(publicKey.getEncoded(), derivedPublicKey.getEncoded());
  }

}
