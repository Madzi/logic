package io.github.madzi.logic.core.scale;

import io.github.madzi.logic.core.Logic;

public record DKScale2(Logic xy, Logic xy_, Logic x_y, Logic x_y_) implements DKScale<DKScale2> {
  /** Length of the scale. */
  private static final int LENGTH = 4;
  private static final int IDX_XY = 0;
  private static final int IDX_X_NOT_Y = 1;
  private static final int IDX_NOT_X_Y = 2;
  private static final int IDX_NOT_X_NOT_Y = 3;

  /** Constructs a DKScale2 from an array of Logics. */
  public DKScale2(final Logic[] logics) {
    this(checkLength(logics)[IDX_XY], logics[IDX_X_NOT_Y], logics[IDX_NOT_X_Y], logics[IDX_NOT_X_NOT_Y]);
  }

  /** Constructs a DKScale2 from two Logics x and y. */
  public DKScale2(final Logic x, final Logic y) {
    this(x.and(y), x.and(y.not()), x.not().and(y), x.not().and(y.not()));
  }

  /** Eliminates Y from this scale. */
  public DKScale1 eliminateY() {
    return new DKScale1(xy.or(xy_), x_y.or(x_y_));
  }

  /** Eliminates X from this scale. */
  public DKScale1 eliminateX() {
    return new DKScale1(xy.or(x_y), xy_.or(x_y_));
  }

  /** Swaps X and Y in this scale. */
  public DKScale2 swapXY() {
    return new DKScale2(xy, x_y, xy_, x_y_);
  }

  /** Lifts this scale as XY to DKScale3. */
  public DKScale3 liftAsXY() {
    return new DKScale3(xy, xy, xy_, xy_, x_y, x_y, x_y_, x_y_);
  }

  /** Lifts this scale as XZ to DKScale3. */
  public DKScale3 liftAsXZ() {
    return new DKScale3(xy, xy_, xy, xy_, x_y, x_y_, x_y, x_y_);
  }

  /** Lifts this scale as YZ to DKScale3. */
  public DKScale3 liftAsYZ() {
    return new DKScale3(xy, xy_, x_y, x_y_, xy, xy_, x_y, x_y_);
  }

  @Override
  public Logic[] scale() {
    return new Logic[] {xy, xy_, x_y, x_y_};
  }

  @Override
  public int size() {
    return LENGTH;
  }

  @Override
  public DKScale2 not() {
    return new DKScale2(xy.not(), xy_.not(), x_y.not(), x_y_.not());
  }

  @Override
  public DKScale2 and(final DKScale2 other) {
    return new DKScale2(
        xy.and(other.xy), xy_.and(other.xy_), x_y.and(other.x_y), x_y_.and(other.x_y_));
  }

  @Override
  public DKScale2 or(final DKScale2 other) {
    return new DKScale2(xy.or(other.xy), xy_.or(other.xy_), x_y.or(other.x_y), x_y_.or(other.x_y_));
  }

  private static Logic[] checkLength(final Logic[] logics) {
    if (null == logics || logics.length != LENGTH) {
      throw new IllegalArgumentException("unsupported array size");
    }
    return logics;
  }
}
