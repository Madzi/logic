package io.github.madzi.logic.console;

import io.github.madzi.logic.core.Engine;
import io.github.madzi.logic.core.Logic;
import io.github.madzi.logic.core.LogicPresenter;
import io.github.madzi.logic.core.Relation;
import io.github.madzi.logic.core.RelationPresenter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogicConsole {

    private List<String> session = new ArrayList<>();
    private Engine engine = Engine.create();

    private final LogicPresenter logicPresenter = LogicPresenter.FUT;
    private final RelationPresenter relationPresenter = new RelationPresenter(logicPresenter);

    private static final Pattern RELATION_CMD = Pattern.compile("^(?i)([A-Z]{1,3})\\(([^,)]+),([^)]+)\\)$");
    private static final Pattern STATUS_CMD = Pattern.compile("^(?i)([FTU])\\(([^)]+)\\)$");
    private static final Pattern QUERY_STATUS_CMD = Pattern.compile("^\\?\\(([^)]+)\\)$");
    private static final Pattern QUERY_RELATION_CMD = Pattern.compile("^\\?\\(([^,)]+),([^)]+)\\)$");
    private static final Pattern DEFINE_CMD = Pattern.compile("^(?i)DEFINE\\(([^,)]+),([^)]+)\\)$");
 
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== LOGIC CONSOLE ===");
        System.out.println("Used engine: logic-core.");
        System.out.println("Logic symbols: FALSE=" + logicPresenter.neg()
                + ", UNKNOWN=" + logicPresenter.unk()
                + ", TRUE=" + logicPresenter.pos() + "\n");

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
                save(line.substring(5));
                continue;
            } else if (line.startsWith("load ")) {
                load(line.substring(5));
                continue;
            }
            if (line.isEmpty()) {
                continue;
            }

            try {
                processLine(line);
            } catch (Exception e) {
                System.out.println("-> ERROR: " + e.getMessage());
            }
        }
    }

    private void save(final String fileName) {
        try {
            Files.writeString(Paths.get(fileName), String.join("\n", session));
        } catch (final IOException exception) {
            System.err.println("Unable to save file: " + fileName + " :: " + exception.getMessage());
        }
    }

    private void load(final String fileName) {
        try {
            session.clear();
            engine = Engine.create();
            Files.readAllLines(Paths.get(fileName)).forEach(this::processLine);
        } catch (final IOException exception) {
            System.err.println("Unable to load file: " + fileName + " :: " + exception.getMessage());
        }
    }

    private void help() {
        System.out.println(" ---- ");
        System.out.println(" help - this screen");
        System.out.println(" save <filename.ext> - store session into file");
        System.out.println(" load <filename.ext> - load session from file");
        System.out.println(" exit - leave program");
    }

    private void processLine(String line) {
        var cleanLine = line.replace(" ", "");
        Matcher mDefine = DEFINE_CMD.matcher(cleanLine);
        if (mDefine.matches()) {
            session.add(line);
            String relName = mDefine.group(1).toUpperCase();
            String scaleText = mDefine.group(2).toUpperCase();

            Relation customRelation = relationPresenter.parse(scaleText);
            engine.defineRelation(relName, customRelation);

            System.out.println("-> DEFINE(" + relName + "," + scaleText + ")");
            return;
        }

        Matcher mQueryRel = QUERY_RELATION_CMD.matcher(cleanLine);
        if (mQueryRel.matches()) {
            String subject = mQueryRel.group(1);
            String predicate = mQueryRel.group(2);

            engine.inferRelation(subject, predicate).ifPresentOrElse(
                    relName -> System.out.println("-> " + relName + "(" + subject + "," + predicate + ")"),
                    () -> System.out.println("-> " + logicPresenter.unk() + "(" + subject + "," + predicate + ")")
            );
            return;
        }

        Matcher mQueryStatus = QUERY_STATUS_CMD.matcher(cleanLine);
        if (mQueryStatus.matches()) {
            String concept = mQueryStatus.group(1);

            Logic logic = engine.checkStatus(concept);
            String statusText = logicPresenter.write(logic);

            System.out.println("-> " + statusText + "(" + concept + ")");
            return;
        }

        Matcher mStatus = STATUS_CMD.matcher(cleanLine);
        if (mStatus.matches()) {
            session.add(line);
            String statusSign = mStatus.group(1).toUpperCase();
            String concept = mStatus.group(2);

            Logic logic = logicPresenter.parse(statusSign);
            engine.status(concept, logic);

            System.out.println("-> " + statusSign + "(" + concept + ")");
            return;
        }

        Matcher mRel = RELATION_CMD.matcher(cleanLine);
        if (mRel.matches()) {
            session.add(line);
            String relName = mRel.group(1).toUpperCase();
            String subject = mRel.group(2);
            String predicate = mRel.group(3);

            engine.fact(relName, subject, predicate);
            System.out.println("-> " + relName + "(" + subject + "," + predicate + ")");
            return;
        }

        System.out.println("-> ERROR: Unknown syntax.");
    }

    static public void main(final String... args) {
        new LogicConsole().start();
    }
}
