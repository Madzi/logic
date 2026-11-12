package io.github.madzi.logic.core.scale;

import io.github.madzi.logic.core.Logic;

public sealed interface DKScale<D extends DKScale<?>>
    permits DKScale1, DKScale2, DKScale3, DKScale4 {
  /** Returns the scale of Logic values. */
  Logic[] scale();

  /** Returns the size of this scale. */
  int size();

  /** Returns the negation of this scale. */
  D not();

  /** Returns the logical AND of this scale with another. */
  D and(D other);

  /** Returns the logical OR of this scale with another. */
  D or(D other);
}
