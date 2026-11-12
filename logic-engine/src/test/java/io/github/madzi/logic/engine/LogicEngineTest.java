package io.github.madzi.logic.engine;

import io.github.madzi.logic.core.relation.Relation2;
import io.github.madzi.logic.core.relation.Relation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса LogicEngine.
 */
public class LogicEngineTest {

    @Test
    public void testSimpleDeduction() {
        Engine engine = Engine.create();
        
        // Все люди смертны (A: humans -> mortals)
        engine.fact(Fact.of("humans", "mortals", Relation.A()));
        
        // Сократ - человек (A: Socrates -> humans)
        engine.fact(Fact.of("Socrates", "humans", Relation.A()));
        
        // Следовательно, Сократ смертен
        var result = engine.find("Socrates", "mortals");
        
        assertTrue(result.isPresent(), "Дедукция должна быть найдена");
        
        Deduction deduction = result.get();
        assertNotNull(deduction.major());
        assertNotNull(deduction.minor());
        assertEquals(Relation.A(), deduction.conclusion().relation());
    }
    
    @Test
    public void testFactAlreadyExists() {
        Engine engine = Engine.create();
        
        // Добавляем тот же факт дважды
        engine.fact(Fact.of("humans", "mortals", Relation.A()));
        engine.fact(Fact.of("humans", "mortals", Relation.A()));
        
        var result = engine.find("humans", "mortals");
        assertTrue(result.isPresent());
    }
    
    @Test
    public void testNoDeduction() {
        Engine engine = Engine.create();
        
        // Добавляем факты, которые не образуют силлогизм
        engine.fact(Fact.of("A", "B", Relation.A()));
        engine.fact(Fact.of("C", "D", Relation.A()));
        
        var result = engine.find("A", "D");
        assertFalse(result.isPresent(), "Дедукция не должна быть найдена");
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
        assertTrue(result.isPresent(), "Цепочка дедукций A -> B -> C -> D должна привести к A -> D");
        assertEquals(Relation.A(), result.get().conclusion().relation());
    }
}
