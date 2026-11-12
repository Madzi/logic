package io.github.madzi.logic.engine;

import static org.junit.jupiter.api.Assertions.*;

import io.github.madzi.logic.core.relation.Relation;
import org.junit.jupiter.api.Test;

public class ReproductionTest {
  @Test
  public void testInverseFactPresence() {
    Engine engine = Engine.create();
    engine.fact(Fact.of("greeks", "people", Relation.A()));

    assertTrue(engine.find("greeks", "people").isPresent(), "Direct fact should be found");
    assertTrue(engine.find("people", "greeks").isPresent(), "Inverse fact should be found");

    var inverse = engine.find("people", "greeks").get();
    assertNull(inverse.major(), "Inverse fact should be marked as Known Fact");

    // A' (transposed A) is more certain than I
    // A:  [T, F, U, T]
    // A': [T, U, F, T]  (certainty = 3)
    // I:  [T, U, U, T]  (certainty = 2)
    assertEquals(3, getCertainty(inverse.conclusion().relation()));
  }

  private int getCertainty(io.github.madzi.logic.core.relation.Relation2 relation) {
    int knownCount = 0;
    for (io.github.madzi.logic.core.Logic l : relation.dkscale().scale()) {
      if (l != io.github.madzi.logic.core.Logic.UNKNOWN) knownCount++;
    }
    return knownCount;
  }

  @Test
  public void testKnownFactPriority() {
    Engine engine = Engine.create();
    // A: people -> mortals
    engine.fact(Fact.of("people", "mortals", Relation.A()));

    var result = engine.find("mortals", "people");
    assertTrue(result.isPresent());
    assertNull(result.get().major(), "Should be marked as Known Fact (major == null)");
    // Relation.A().inverse() should be A'
  }
}
