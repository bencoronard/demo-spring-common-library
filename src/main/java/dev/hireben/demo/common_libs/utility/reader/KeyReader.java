package dev.hireben.demo.common_libs.utility.reader;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KeyReader {

  public PrivateKey readRsaPrivateKeyPkcs8(String content) throws NoSuchAlgorithmException, InvalidKeySpecException {
    String keyBase64 = content
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replaceAll("\\s+", "")
        .replace("-----END PRIVATE KEY-----", "");

    byte[] key = Base64.getDecoder().decode(keyBase64);

    KeyFactory factory = KeyFactory.getInstance("RSA");
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(key);

    return factory.generatePrivate(spec);
  }

  // -----------------------------------------------------------------------------

  public PublicKey readRsaPublicKeyX509(String content) throws NoSuchAlgorithmException, InvalidKeySpecException {
    String keyBase64 = content
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replaceAll("\\s+", "")
        .replace("-----END PUBLIC KEY-----", "");

    byte[] key = Base64.getDecoder().decode(keyBase64);

    KeyFactory factory = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec spec = new X509EncodedKeySpec(key);

    return factory.generatePublic(spec);
  }

}
