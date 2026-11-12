package io.github.madzi.logic.console;

import io.github.madzi.logic.core.LogicPresenter;
import io.github.madzi.logic.core.relation.Relation;
import io.github.madzi.logic.core.relation.Relation2;
import io.github.madzi.logic.core.relation.RelationPresenter;
import io.github.madzi.logic.engine.Deduction;
import io.github.madzi.logic.engine.Engine;
import io.github.madzi.logic.engine.Fact;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogicConsole {

  private final List<String> session = new ArrayList<>();
  private final Engine engine = Engine.create();
  private final LogicPresenter logicPresenter = LogicPresenter.FUT;
  private final RelationPresenter relationPresenter = new RelationPresenter(logicPresenter);

  private static final Map<Relation2, String> RELATION_NAMES;
  private static final Map<String, Relation2> NAME_TO_RELATION;

  static {
    RELATION_NAMES = new java.util.HashMap<>();
    NAME_TO_RELATION = new java.util.HashMap<>();

    java.util.function.BiConsumer<Relation2, String> add =
        (rel, name) -> {
          RELATION_NAMES.putIfAbsent(rel, name);
          NAME_TO_RELATION.put(name, rel);
        };

    add.accept(Relation.A(), "A");
    add.accept(Relation.E(), "E");
    add.accept(Relation.I(), "I");
    add.accept(Relation.O(), "O");

    add.accept(Relation.create(Relation.A().dkscale().not()), "!A");
    add.accept(Relation.create(Relation.E().dkscale().not()), "!E");
    add.accept(Relation.create(Relation.I().dkscale().not()), "!I");
    add.accept(Relation.create(Relation.O().dkscale().not()), "!O");

    add.accept(Relation.A().inverse(), "A'");
    add.accept(Relation.E().inverse(), "E'");
    add.accept(Relation.I().inverse(), "I'");
    add.accept(Relation.O().inverse(), "O'");

    add.accept(Relation.create(Relation.A().dkscale().not()).inverse(), "!A'");
    add.accept(Relation.create(Relation.E().dkscale().not()).inverse(), "!E'");
    add.accept(Relation.create(Relation.I().dkscale().not()).inverse(), "!I'");
    add.accept(Relation.create(Relation.O().dkscale().not()).inverse(), "!O'");
  }

  private static final Pattern FACT_PATTERN =
      Pattern.compile("^([\\w'!]+|\\{[TFU]{4}\\}):\\s*(\\w+)\\s*->\\s*(\\w+)$");
  private static final Pattern QUERY_PATTERN =
      Pattern.compile("^\\?\\s*:\\s*(\\w+)\\s*->\\s*(\\w+)$");

  public void start() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("=== Logic Console ===");
    System.out.println(
        "Logic alphabet: FALSE="
            + logicPresenter.neg()
            + ", UNKNOWN="
            + logicPresenter.unk()
            + ", TRUE="
            + logicPresenter.pos()
            + "\n");
    System.out.println("Relations:");
    System.out.println("A: S -> P   - means: `All S are P`");
    System.out.println("E: S -> P   - means: `No S are P`");
    System.out.println("I: S -> P   - means: `Some S are P`");
    System.out.println("O: S -> P   - means: `Some S are not P`");
    help();
    while (true) {
      System.out.print("> ");
      String line = scanner.nextLine().trim();
      if ("exit".equals(line)) {
        break;
      } else if ("help".equals(line)) {
        help();
        continue;
      } else if (line.startsWith("save ")) {
        save(line.substring(5).trim());
        continue;
      } else if (line.startsWith("load ")) {
        load(line.substring(5).trim());
        continue;
      }
      if (line.isEmpty()) {
        continue;
      }
      try {
        processLine(line);
      } catch (IllegalArgumentException e) {
        System.out.println("-> ERROR: " + e.getMessage());
      }
    }
  }

  private void save(final String fileName) {
    try {
      Files.writeString(Paths.get(fileName), String.join("\n", session));
      System.out.println("-> Session saved to: " + fileName);
    } catch (final IOException exception) {
      System.err.println("Unable to save file: " + fileName + " :: " + exception.getMessage());
    }
  }

  private void load(final String fileName) {
    try {
      session.clear();
      engine.clear();
      Files.readAllLines(Paths.get(fileName)).forEach(this::processLine);
      System.out.println("-> Session loaded from: " + fileName);
    } catch (final IOException exception) {
      System.err.println("Unable to load file: " + fileName + " :: " + exception.getMessage());
    }
  }

  private void help() {
    System.out.println(" ---- ");
    System.out.println(" help                                - this screen");
    System.out.println(" save <file.ext>                     - save session");
    System.out.println(" load <file.ext>                     - load session");
    System.out.println(" exit                                - exit");
    System.out.println(
        " REL: S -> P                         - add fact (REL: A, E, I, O, A', !A, !A' etc.)");
    System.out.println(
        " ? : S -> P                          - query deduction (example: ?: greeks -> mortals)");
    System.out.println(" ---- ");
  }

  private void processLine(String line) {
    if (!line.startsWith("%")) {
      session.add(line);
    }

    if (FACT_PATTERN.matcher(line).matches()) {
      parseFact(line);
    } else if (QUERY_PATTERN.matcher(line).matches()) {
      parseQuery(line);
    } else {
      throw new IllegalArgumentException("Unknown command format");
    }
  }

  private void parseFact(String line) {
    Matcher matcher = FACT_PATTERN.matcher(line);
    if (!matcher.matches()) throw new IllegalArgumentException("Invalid fact format");

    String relationName = matcher.group(1);
    String subject = matcher.group(2);
    String predicate = matcher.group(3);

    Relation2 relation = resolveRelation(relationName);
    engine.fact(Fact.of(subject, predicate, relation));
    System.out.println("-> Accepted");
  }

  private void parseQuery(String line) {
    Matcher matcher = QUERY_PATTERN.matcher(line);
    if (!matcher.matches()) throw new IllegalArgumentException("Invalid query format");

    String subject = matcher.group(1);
    String predicate = matcher.group(2);

    Optional<Deduction> result = engine.find(subject, predicate);
    printResult(result, subject, predicate);
  }

  private Relation2 resolveRelation(String name) {
    if (name.startsWith("{") && name.endsWith("}")) {
      String matrix = name.substring(1, name.length() - 1);
      return (Relation2) relationPresenter.parse(matrix);
    } else {
      Relation2 relation = NAME_TO_RELATION.get(name);
      if (relation == null) {
        throw new IllegalArgumentException("Unknown relation: " + name);
      }
      return relation;
    }
  }

  private void printResult(Optional<Deduction> result, String subject, String predicate) {
    if (result.isEmpty()) {
      System.out.println("-> Not found");
      return;
    }

    Deduction deduction = result.get();
    String relationStr = formatRelation(deduction.conclusion().relation());

    if (deduction.major() == null && deduction.minor() == null) {
      System.out.printf("-> Found: %s: %s -> %s :: Known Fact%n", relationStr, subject, predicate);
    } else {
      String modus = detectModus(deduction);
      System.out.printf("-> Found: %s: %s -> %s :: %s%n", relationStr, subject, predicate, modus);
    }
  }

  private String formatRelation(Relation2 relation) {
    return RELATION_NAMES.getOrDefault(relation, "{" + relationPresenter.write(relation) + "}");
  }

  private String detectModus(Deduction deduction) {
    if (deduction.major() == null || deduction.minor() == null) return "UNKNOWN";

    String majorRel = formatRelation(deduction.major().relation());
    String minorRel = formatRelation(deduction.minor().relation());
    String conclusionRel = formatRelation(deduction.conclusion().relation());

    String pattern = majorRel + minorRel + conclusionRel;

    // Figure 1: M-P, S-M => S-P
    if (Objects.equals(deduction.major().predicate(), deduction.minor().subject())) {
      if (majorRel.equals("A") && minorRel.equals("A")) return "BARBARA";
      if (majorRel.equals("E") && minorRel.equals("A")) return "CELARENT";
      if (majorRel.equals("A") && minorRel.equals("I")) return "DARII";
      if (majorRel.equals("E") && minorRel.equals("I")) return "FERIO";
    }

    // Figure 2: P-M, S-M => S-P
    if (Objects.equals(deduction.major().predicate(), deduction.minor().predicate())) {
      if (majorRel.equals("E") && minorRel.equals("A")) return "CESARE";
      if (majorRel.equals("A") && minorRel.equals("E")) return "CAMESTRES";
      if (majorRel.equals("E") && minorRel.equals("I")) return "FESTINO";
      if (majorRel.equals("A") && minorRel.equals("O")) return "BAROCO";
    }

    // Figure 3: M-P, M-S => S-P
    if (Objects.equals(deduction.major().subject(), deduction.minor().subject())) {
      if (majorRel.equals("A") && minorRel.equals("A")) return "DARAPTI";
      if (majorRel.equals("I") && minorRel.equals("A")) return "DISAMIS";
      if (majorRel.equals("A") && minorRel.equals("I")) return "DATISI";
      if (majorRel.equals("E") && minorRel.equals("A")) return "FELAPTON";
      if (majorRel.equals("O") && minorRel.equals("A")) return "BOCARDO";
      if (majorRel.equals("E") && minorRel.equals("I")) return "FERISON";
    }

    // Figure 4: P-M, M-S => S-P
    if (Objects.equals(deduction.major().subject(), deduction.minor().predicate())) {
      if (majorRel.equals("A") && minorRel.equals("A")) return "BAMALIP";
      if (majorRel.equals("A") && minorRel.equals("E")) return "CALEMES";
      if (majorRel.equals("I") && minorRel.equals("A")) return "DIMATIS";
      if (majorRel.equals("E") && minorRel.equals("A")) return "FESAPO";
      if (majorRel.equals("E") && minorRel.equals("I")) return "FRESISON";
    }

    return pattern;
  }

  public static void main(final String... args) {
    new LogicConsole().start();
  }
}
