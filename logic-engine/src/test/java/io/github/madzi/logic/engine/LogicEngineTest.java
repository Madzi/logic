package io.github.madzi.logic.engine;

import static org.junit.jupiter.api.Assertions.*;

import io.github.madzi.logic.core.relation.Relation;
import org.junit.jupiter.api.Test;

/** Tests for the LogicEngine class. */
public class LogicEngineTest {

  @Test
  public void testSimpleDeduction() {
    Engine engine = Engine.create();

    // All people are mortal (A: humans -> mortals)
    engine.fact(Fact.of("humans", "mortals", Relation.A()));

    // Socrates is a human (A: Socrates -> humans)
    engine.fact(Fact.of("Socrates", "humans", Relation.A()));

    // Therefore, Socrates is mortal
    var result = engine.find("Socrates", "mortals");

    assertTrue(result.isPresent(), "Deduction should be found");

    Deduction deduction = result.get();
    assertNotNull(deduction.major());
    assertNotNull(deduction.minor());
    assertEquals(Relation.A(), deduction.conclusion().relation());
  }

  @Test
  public void testFactAlreadyExists() {
    Engine engine = Engine.create();

    // Adding the same fact twice
    engine.fact(Fact.of("humans", "mortals", Relation.A()));
    engine.fact(Fact.of("humans", "mortals", Relation.A()));

    var result = engine.find("humans", "mortals");
    assertTrue(result.isPresent());
  }

  @Test
  public void testNoDeduction() {
    Engine engine = Engine.create();

    // Adding facts that do not form a syllogism
    engine.fact(Fact.of("A", "B", Relation.A()));
    engine.fact(Fact.of("C", "D", Relation.A()));

    var result = engine.find("A", "D");
    assertFalse(result.isPresent(), "Deduction should not be found");
  }

  @Test
  public void testClear() {
    Engine engine = Engine.create();

    engine.fact(Fact.of("humans", "mortals", Relation.A()));
    engine.fact(Fact.of("Socrates", "humans", Relation.A()));

    assertNotNull(engine.find("Socrates", "mortals"));

    engine.clear();

    assertFalse(engine.find("Socrates", "mortals").isPresent());
  }

  @Test
  public void testChainedDeduction() {
    Engine engine = Engine.create();

    // A -> B, B -> C, C -> D => A -> D
    engine.fact(Fact.of("A", "B", Relation.A()));
    engine.fact(Fact.of("B", "C", Relation.A()));
    engine.fact(Fact.of("C", "D", Relation.A()));

    var result = engine.find("A", "D");
    assertTrue(result.isPresent(), "Chain of deductions A -> B -> C -> D should lead to A -> D");
    assertEquals(Relation.A(), result.get().conclusion().relation());
  }
}
