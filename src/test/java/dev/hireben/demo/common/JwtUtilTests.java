package dev.hireben.demo.common;

import java.security.Key;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JwtUtilTests {

  // ---------------------------------------------------------------------------//
  // Fields
  // ---------------------------------------------------------------------------//

  private Key privateKey;
  private Key publicKey;
  private Key symmetricKey;

  // ---------------------------------------------------------------------------//
  // Methods
  // ---------------------------------------------------------------------------//

  @BeforeAll
  void setup() {
    privateKey = null;
    publicKey = null;
    symmetricKey = null;
  }

  // ---------------------------------------------------------------------------//

  @Test
  void test() {
  }

}
