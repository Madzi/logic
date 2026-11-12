package io.github.madzi.logic.calc.internal;

import io.github.madzi.logic.calc.Calc;
import io.github.madzi.logic.core.relation.Relation;

/** Base implementation of {@link Calc} with stub methods. */
public class BaseCalc implements Calc {

  @Override
  public void clear() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void define(final String name, final Relation<?> relation) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Relation<?> evaluate(final String text) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
