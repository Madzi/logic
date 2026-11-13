package io.github.madzi.logic.console;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import io.github.madzi.logic.core.LogicPresenter;
import io.github.madzi.logic.core.relation.Relation2;
import io.github.madzi.logic.core.relation.RelationPresenter;
import io.github.madzi.logic.engine.Deduction;
import io.github.madzi.logic.engine.Engine;
import io.github.madzi.logic.engine.Fact;

/**
 * Session.
 */
public class Session {

    private record Mood(int figure, String major, String minor, String conclusion) {}
    private static final Map<Mood, String> MODI = new HashMap<>();

    static {
        figure(1, "AAA", "BARBARA", "EAE", "CELARENT", "AII", "DARII", "EIO", "FERIO");
        figure(2, "EAE", "CESARE", "AEE", "CAMESTRES", "EIO", "FESTINO", "AOO", "BAROCO");
        figure(3, "AAI", "DARAPTI", "IAI", "DISAMIS", "AII", "DATISI", "EAO", "FELAPTON", "OAO", "BOCARDO", "EIO", "FERISON");
        figure(4, "AAI", "BAMALIP", "AEE", "CALEMES", "IAI", "DIMATIS", "EAO", "FESAPO", "EIO", "FRESISON");
    }

    private static void figure(int figure, String... moodAndName) {
        for (int i = 0; i < moodAndName.length; i += 2) {
            String m = moodAndName[i];
            MODI.put(new Mood(figure, m.substring(0, 1), m.substring(1, 2), m.substring(2, 3)), moodAndName[i + 1]);
        }
    }

    private final Parser parser = new Parser();
    private final Engine engine = Engine.create();
    private final LogicPresenter logicPresenter = LogicPresenter.FUT;
    private final RelationPresenter relationPresenter = new RelationPresenter(logicPresenter);
    private final RelationNames names = new RelationNames();
    private final List<String> lines = new ArrayList<>();

    public LogicPresenter presenter() {
        return logicPresenter;
    }

    public Set<String> relationNames() {
        return names.all();
    }

    public String apply(final Command command, final String line) {
        String message = switch (command) {
            case Command.AddFact(var relation, var subject, var predicate) -> {
                engine.fact(Fact.of(subject, predicate, resolve(relation)));
                yield "=> Accepted";
            }
            case Command.Query(var subject, var predicate) ->
                describe(engine.find(subject, predicate), subject, predicate);
            default ->
                throw new IllegalArgumentException("Not a logic command: " + command);
        };
        lines.add(line);
        return message;
    }

    public void save(final Path file) throws IOException {
        Files.writeString(file, String.join("\n", lines) + "\n");
    }

    public List<String> load(final Path file) throws IOException {
        List<String> content = Files.readAllLines(file);
        engine.clear();
        lines.clear();
        List<String> messages = new ArrayList<>();
        int number = 0;
        for (String line : content) {
            number++;
            try {
                parser.parse(line).ifPresent(command -> messages.add(apply(command, line.strip())));
            } catch (final IllegalArgumentException exception) {
                messages.add("=> ERROR (line " + number + "): " + exception.getMessage());
            }
        }
        return messages;
    }

    private Relation2 resolve(final String name) {
        if (name.startsWith("{") && name.endsWith("}")) {
            return (Relation2) relationPresenter.parse(name.substring(1, name.length() - 1));
        }
        return names.byName(name).orElseThrow(() -> new IllegalArgumentException("Unknown relation: " + name));
    }

    private String describe(final Optional<Deduction> result, final String subject, final String predicate) {
        if (result.isEmpty()) {
            return "=> Not found";
        }
        Deduction deduction = result.get();
        String how = deduction.major() == null && deduction.minor() == null ? "Known Fact" : modus(deduction);
        return "=> Found: %s: %s -> %s :: %s".formatted(display(deduction.conclusion().relation()), subject, predicate, how);
    }

    private String display(final Relation2 relation) {
        return names.nameOf(relation).orElseGet(() -> "{" + relationPresenter.write(relation) + "}");
    }

    private String modus(final Deduction deduction) {
        var major = deduction.minor();
        var minor = deduction.major();
        if (major == null || minor == null) {
            return "UNKNOWN";
        }
        String majorRel = display(major.relation());
        String minorRel = display(minor.relation());
        String conclusionRel = display(deduction.conclusion().relation());
        int figure = figure(major.subject(), major.predicate(), minor.subject(), minor.predicate());
        String name = MODI.get(new Mood(figure, majorRel, minorRel, conclusionRel));
        return name != null ? name : majorRel + minorRel + conclusionRel;
    }

    private static int figure(final Object majorSubject, final Object majorPredicate, final Object minorSubject, final Object minorPredicate) {
        if (Objects.equals(majorSubject, minorPredicate)) {
            return 1;
        }
        if (Objects.equals(majorPredicate, minorPredicate)) {
            return 2;
        }
        if (Objects.equals(majorSubject, minorSubject)) {
            return 3;
        }
        if (Objects.equals(majorPredicate, minorSubject)) {
            return 4;
        }
        return 0;
    }
}
