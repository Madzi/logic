package io.github.madzi.logic.core;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class SyllogismBarbaraTest {

    private final Atom<String> greeks = Atom.create("greeks");
    private final Atom<String> people = Atom.create("people");
    private final Atom<String> mortals = Atom.create("mortals");
    private final Atom<String> stones = Atom.create("stones");
    private final Atom<String> minerals = Atom.create("minerals");

    private Engine engine;

    @BeforeEach
    void setUp() {
        engine = Engine.create();
    }

    @Test
    void testBarbaraSyllogism() {
        engine.fact("A", greeks, people);
        engine.fact("A", people, mortals);
        engine.status(greeks, Logic.TRUE);
        engine.status(mortals, Logic.TRUE);
        Optional<String> deduction = engine.inferRelation(greeks, mortals);
        Assertions.assertTrue(deduction.isPresent());
        Assertions.assertEquals("A", deduction.get());
    }

    @Test
    void testStatusInference() {
        engine.fact("A", greeks, people);
        engine.fact("A", people, mortals);
        engine.status(greeks, Logic.TRUE);
        Logic inferredStatus = engine.checkStatus(mortals);
        Assertions.assertEquals(Logic.TRUE, inferredStatus);
    }

    @Test
    void testBamalipSyllogism() {
        engine.fact("A", greeks, people);
        engine.fact("A", people, mortals);
        engine.status(greeks, Logic.TRUE);
        engine.status(mortals, Logic.TRUE);
        Optional<String> deduction = engine.inferRelation(greeks, mortals);
        Assertions.assertTrue(deduction.isPresent());
        Assertions.assertEquals("A", deduction.get());
    }

    @Test
    void testCelarentSyllogism() {
        engine.fact("E", greeks, stones);
        engine.fact("A", stones, minerals);
        engine.status(greeks, Logic.TRUE);
        engine.status(minerals, Logic.TRUE);
        Optional<String> deduction = engine.inferRelation(greeks, minerals);
        Assertions.assertTrue(deduction.isPresent());
        Assertions.assertEquals("O", deduction.get());
    }
}
