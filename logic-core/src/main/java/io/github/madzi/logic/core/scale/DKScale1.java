package io.github.madzi.logic.core.scale;

import io.github.madzi.logic.core.Logic;

public record DKScale1(Logic x, Logic x_) implements DKScale<DKScale1> {
  /** Length of the scale. */
  private static final int LENGTH = 2;
  private static final int IDX_X  = 0;
  private static final int IDX_NOT_X = 1;

  /** Constructs a DKScale1 from an array of Logics. */
  public DKScale1(final Logic[] logics) {
    this(checkLength(logics)[IDX_X], logics[IDX_NOT_X]);
  }

  /** Constructs a DKScale1 from a single Logic value. */
  public DKScale1(final Logic x) {
    this(x, x.not());
  }

  /** Lifts this scale as X to DKScale2. */
  public DKScale2 liftAsX() {
    return new DKScale2(x, x, x_, x_);
  }

  /** Lifts this scale as Y to DKScale2. */
  public DKScale2 liftAsY() {
    return new DKScale2(x, x_, x, x_);
  }

  @Override
  public DKScale1 not() {
    return new DKScale1(x.not(), x_.not());
  }

  @Override
  public DKScale1 and(final DKScale1 other) {
    return new DKScale1(x.and(other.x), x_.and(other.x_));
  }

  @Override
  public DKScale1 or(final DKScale1 other) {
    return new DKScale1(x.or(other.x), x_.or(other.x_));
  }

  @Override
  public Logic[] scale() {
    return new Logic[] {x, x_};
  }

  @Override
  public int size() {
    return LENGTH;
  }

  private static Logic[] checkLength(final Logic[] logics) {
    if (null == logics || logics.length != LENGTH) {
      throw new IllegalArgumentException("unsupported array size");
    }
    return logics;
  }
}
