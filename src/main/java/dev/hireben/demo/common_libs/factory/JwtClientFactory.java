package dev.hireben.demo.common_libs.factory;

import java.security.KeyPair;

import javax.crypto.SecretKey;

import dev.hireben.demo.common_libs.utility.JwtClient;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JwtClientFactory {

  public JwtClient buildJwtClientWithoutKey(String issuer) {
    return new JwtClient(issuer);
  }

  // -----------------------------------------------------------------------------

  public JwtClient buildJwtClientWithSymmetricKey(String issuer, SecretKey key) {
    return new JwtClient(issuer, key);
  }

  // -----------------------------------------------------------------------------

  public JwtClient buildJwtClientWithAsymmmetricKeys(String issuer, KeyPair keyPair) {
    return new JwtClient(issuer, keyPair);
  }

}
