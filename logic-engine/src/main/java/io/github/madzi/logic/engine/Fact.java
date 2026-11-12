package io.github.madzi.logic.engine;

import io.github.madzi.logic.core.relation.Relation2;
import java.util.Objects;

public record Fact(Axis axis, Relation2 relation) {
  public int commonAxis(Fact other) {
    int result = 0;
    if (Objects.equals(axis.x(), other.axis.x()) || Objects.equals(axis.x(), other.axis.y())) {
      result++;
    }
    if (Objects.equals(axis.y(), other.axis.x()) || Objects.equals(axis.y(), other.axis.y())) {
      result++;
    }
    return result;
  }

  public Fact inverse() {
    return new Fact(axis.swapXY(), relation.inverse());
  }

  public String subject() {
    return axis.x();
  }

  public String predicate() {
    return axis.y();
  }

  public static Fact of(String subject, String predicate, Relation2 relation) {
    return new Fact(new Axis(subject, predicate), relation);
  }
}
