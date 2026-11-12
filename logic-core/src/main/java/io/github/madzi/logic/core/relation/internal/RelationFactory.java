package io.github.madzi.logic.core.relation.internal;

import io.github.madzi.logic.core.relation.Relation1;
import io.github.madzi.logic.core.relation.Relation2;
import io.github.madzi.logic.core.relation.Relation3;
import io.github.madzi.logic.core.relation.Relation4;
import io.github.madzi.logic.core.scale.DKScale1;
import io.github.madzi.logic.core.scale.DKScale2;
import io.github.madzi.logic.core.scale.DKScale3;
import io.github.madzi.logic.core.scale.DKScale4;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Factory for creating and caching Relation instances. */
public enum RelationFactory {
  /** Singleton instance of this factory. */
  INSTANCE;

  /** Cache for Relation1 instances. */
  private final Map<DKScale1, Relation1> relations1 = new ConcurrentHashMap<>();

  /** Cache for Relation2 instances. */
  private final Map<DKScale2, Relation2> relations2 = new ConcurrentHashMap<>();

  /** Cache for Relation3 instances. */
  private final Map<DKScale3, Relation3> relations3 = new ConcurrentHashMap<>();

  /** Cache for Relation4 instances. */
  private final Map<DKScale4, Relation4> relations4 = new ConcurrentHashMap<>();

  /** Creates or retrieves a cached Relation1 instance from the given scale. */
  public Relation1 createRelation(final DKScale1 scale) {
    return relations1.computeIfAbsent(scale, key -> new Relation1(scale));
  }

  /** Creates or retrieves a cached Relation2 instance from the given scale. */
  public Relation2 createRelation(final DKScale2 scale) {
    return relations2.computeIfAbsent(scale, key -> new Relation2(scale));
  }

  /** Creates or retrieves a cached Relation3 instance from the given scale. */
  public Relation3 createRelation(final DKScale3 scale) {
    return relations3.computeIfAbsent(scale, key -> new Relation3(scale));
  }

  /** Creates or retrieves a cached Relation4 instance from the given scale. */
  public Relation4 createRelation(final DKScale4 scale) {
    return relations4.computeIfAbsent(scale, key -> new Relation4(scale));
  }
}
