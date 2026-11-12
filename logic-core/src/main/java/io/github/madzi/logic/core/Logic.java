package io.github.madzi.logic.core;

public enum Logic {
  /** Represents logical false. */
  FALSE(-1),
  /** Represents logical unknown. */
  UNKNOWN(0),
  /** Represents logical true. */
  TRUE(1);

  /** The numeric value representing this logical state. */
  private final int val;

  /** Constructs a Logic enum with the given numeric value. */
  Logic(final int val) {
    this.val = val;
  }

  /** Returns the logical negation of this value. */
  public Logic not() {
    return Logic.valueOf(-val);
  }

  /** Returns the logical AND of this value and another. */
  public Logic and(final Logic logic) {
    return Logic.valueOf(Math.min(val, logic.val));
  }

  /** Returns the logical OR of this value and another. */
  public Logic or(final Logic logic) {
    return Logic.valueOf(Math.max(val, logic.val));
  }

  /** Returns a Logic value from an integer. */
  public static Logic valueOf(final int obj) {
    return obj < 0 ? FALSE : obj > 0 ? TRUE : UNKNOWN;
  }

  /** Returns a Logic value from a Boolean object. */
  public static Logic valueOf(final Boolean bool) {
    return bool == null ? UNKNOWN : bool ? TRUE : FALSE;
  }
}
