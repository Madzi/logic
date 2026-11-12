package io.github.madzi.logic.engine;

import io.github.madzi.logic.engine.internal.LogicEngine;
import java.util.Optional;

public interface Engine {
  void clear();

  Engine fact(Fact fact);

  Optional<Deduction> find(String subject, String predicate);

  static Engine create() {
    return new LogicEngine();
  }
}
