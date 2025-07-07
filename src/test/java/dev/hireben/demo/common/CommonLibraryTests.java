package dev.hireben.demo.common;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dev.hireben.demo.common.domain.dto.Paginable;
import dev.hireben.demo.common.domain.dto.Paginated;

@SuppressWarnings({ "unlikely-arg-type", "unused" })
class CommonLibraryTests {

  // ---------------------------------------------------------------------------//
  // Methods
  // ---------------------------------------------------------------------------//

  @Test
  void testMain() {
    Paginable paginable = Paginable.builder().build();
    Paginated<Object> paginated = Paginated.builder().build();
    assertTrue(!paginable.equals(paginated), "Hello, JUnit!");
  }

}
