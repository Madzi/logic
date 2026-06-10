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
    
    private static final Pattern FACT_PATTERN = Pattern.compile(
        "^(\\w+|\\{[TFU]{4}\\}):\\s*(\\w+)\\s*->\\s*(\\w+)$"
    );
    private static final Pattern QUERY_PATTERN = Pattern.compile(
        "^\\?\\s*:\\s*(\\w+)\\s*->\\s*(\\w+)$"
    );

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Logic Console ===");
        System.out.println("Logic alphabet: FALSE=" + logicPresenter.neg() 
            + ", UNKNOWN=" + logicPresenter.unk() + ", TRUE=" + logicPresenter.pos() + "\n");
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
            if (line.isEmpty()) { continue; }
            try { processLine(line); } 
            catch (IllegalArgumentException e) { System.out.println("-> ERROR: " + e.getMessage()); }
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
        System.out.println(" [A|E|I|O|name]: S -> P              - add fact (example: A: greeks -> people)");
        System.out.println(" ? : S -> P                          - query deduction (example: ?: greeks -> mortals)");
        System.out.println(" ---- ");
    }

    private void processLine(String line) {
        if (!line.startsWith("%")) { session.add(line); }
        
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
        engine.fact(new Fact(subject, predicate, relation));
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
            return switch (name) {
                case "A" -> Relation.A();
                case "E" -> Relation.E();
                case "I" -> Relation.I();
                case "O" -> Relation.O();
                default -> throw new IllegalArgumentException("Unknown relation: " + name);
            };
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
            System.out.printf("-> Found: %s: %s -> %s :: Known Fact%n", 
                relationStr, subject, predicate);
        } else {
            String modus = detectModus(deduction);
            System.out.printf("-> Found: %s: %s -> %s :: %s%n", 
                relationStr, subject, predicate, modus);
        }
    }

    private String formatRelation(Relation2 relation) {
        if (relation.equals(Relation.A())) return "A";
        if (relation.equals(Relation.E())) return "E";
        if (relation.equals(Relation.I())) return "I";
        if (relation.equals(Relation.O())) return "O";
        return "{" + relationPresenter.write(relation) + "}";
    }

    private String detectModus(Deduction deduction) {
        Relation2 major = deduction.major().relation();
        Relation2 minor = deduction.minor().relation();
        
        if (Objects.equals(deduction.major().predicate(), deduction.minor().subject())) {
            // Первая фигура: M->P, S->M => S-P
            if (major.equals(Relation.A()) && minor.equals(Relation.A())) return "BARBARA";
            if (major.equals(Relation.E()) && minor.equals(Relation.A())) return "CELARENT";
            if (major.equals(Relation.I()) && minor.equals(Relation.A())) return "DARIITI";
            if (major.equals(Relation.O()) && minor.equals(Relation.A())) return "FERIO";
        }
        
        if (Objects.equals(deduction.major().predicate(), deduction.minor().predicate())) {
            // Вторая фигура: P->M, S->M => S-P
            if (major.equals(Relation.E()) && minor.equals(Relation.A())) return "CESARE";
            if (major.equals(Relation.A()) && minor.equals(Relation.E())) return "CAMESTRES";
            if (major.equals(Relation.I()) && minor.equals(Relation.E())) return "FESTINO";
            if (major.equals(Relation.O()) && minor.equals(Relation.E())) return "BAMALPO";
        }
        
        if (Objects.equals(deduction.major().subject(), deduction.minor().subject())) {
            // Третья фигура: M->P, M->S => S-P
            if (major.equals(Relation.A()) && minor.equals(Relation.A())) return "BAconTINi";
            if (major.equals(Relation.E()) && minor.equals(Relation.A())) return "Felapton";
            if (major.equals(Relation.I()) && minor.equals(Relation.A())) return "Datisi";
            if (major.equals(Relation.O()) && minor.equals(Relation.A())) return "Ferison";
        }
        
        if (Objects.equals(deduction.major().subject(), deduction.minor().predicate())) {
            // Четвёртая фигура: P->M, M->S => S-P
            if (major.equals(Relation.A()) && minor.equals(Relation.I())) return "BAMALPO";
        }
        
        return "UNKNOWN";
    }

    public static void main(final String... args) { new LogicConsole().start(); }
}
