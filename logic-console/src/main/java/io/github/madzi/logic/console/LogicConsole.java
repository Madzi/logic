package io.github.madzi.logic.console;

import io.github.madzi.logic.core.Engine;
import io.github.madzi.logic.core.Logic;
import io.github.madzi.logic.core.LogicPresenter;
import io.github.madzi.logic.core.Relation;
import io.github.madzi.logic.core.RelationPresenter;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogicConsole {

    private final Engine engine = Engine.create();
    
    private final LogicPresenter logicPresenter = LogicPresenter.FUT;
    private final RelationPresenter relationPresenter = new RelationPresenter(logicPresenter);

    private static final Pattern RELATION_CMD = Pattern.compile("^([a-z]{1,3})\\(([^,)]+),([^)]+)\\)$");
    private static final Pattern STATUS_CMD = Pattern.compile("^([fut])\\(([^)]+)\\)$");
    private static final Pattern QUERY_STATUS_CMD = Pattern.compile("^\\?\\(([^)]+)\\)$");
    private static final Pattern QUERY_RELATION_CMD = Pattern.compile("^\\?\\(([^,)]+),([^)]+)\\)$");
    
    private static final Pattern DEFINE_CMD = Pattern.compile("^define\\(([^,)]+),([^)]+)\\)$");

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== LOGIC CONSOLE ===");
        System.out.println("Used engine: logic-core.");
        System.out.println("Logic symbols: FALSE=" + logicPresenter.neg() + 
                           ", UNKNOWN=" + logicPresenter.unk() + 
                           ", TRUE=" + logicPresenter.pos() + "\n");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if ("exit".equalsIgnoreCase(line)) {
                break;
            }
            if (line.isEmpty()) continue;

            try {
                // Санитизация: убираем пробелы и в нижний регистр
                String sanitizedLine = line.replace(" ", "").toLowerCase();
                processLine(sanitizedLine);
            } catch (Exception e) {
                System.out.println("-> ERROR: " + e.getMessage());
            }
        }
    }

    private void help() {
        System.out.println(" ---- ");
        System.out.println(" help - this screen");
        System.out.println(" exit - leave program");
    }

    private void processLine(String line) {
        // 1. Команда DEFINE: регистрация нового отношения через презентор
        Matcher mDefine = DEFINE_CMD.matcher(line);
        if (mDefine.matches()) {
            String relName = mDefine.group(1).toUpperCase();
            String scaleText = mDefine.group(2).toUpperCase(); // Презентор FUT ожидает верхний регистр

            // Магия презентора: парсим строку в объект Relation
            Relation customRelation = relationPresenter.parse(scaleText);
            engine.defineRelation(relName, customRelation);
            
            System.out.println("-> define(" + relName.toLowerCase() + "," + scaleText.toLowerCase() + ")");
            return;
        }

        // 2. Запрос отношения: ?(x,y)
        Matcher mQueryRel = QUERY_RELATION_CMD.matcher(line);
        if (mQueryRel.matches()) {
            String subject = mQueryRel.group(1);
            String predicate = mQueryRel.group(2);

            engine.inferRelation(subject, predicate).ifPresentOrElse(
                relName -> System.out.println("-> " + relName.toLowerCase() + "(" + subject + "," + predicate + ")"),
                () -> System.out.println("-> " + logicPresenter.unk().toLowerCase() + "(" + subject + "," + predicate + ")")
            );
            return;
        }

        // 3. Запрос статуса понятия: ?(x)
        Matcher mQueryStatus = QUERY_STATUS_CMD.matcher(line);
        if (mQueryStatus.matches()) {
            String concept = mQueryStatus.group(1);

            Logic logic = engine.checkStatus(concept);
            // Используем logicPresenter для каноничного вывода буквы (F, U, T)
            String statusText = logicPresenter.write(logic).toLowerCase();
            
            System.out.println("-> " + statusText + "(" + concept + ")");
            return;
        }

        // 4. Задание онтологического статуса: t(x), f(x), u(x)
        Matcher mStatus = STATUS_CMD.matcher(line);
        if (mStatus.matches()) {
            String statusSign = mStatus.group(1).toUpperCase();
            String concept = mStatus.group(2);

            // Презентор сам парсит текстовый маркер в тип Logic
            Logic logic = logicPresenter.parse(statusSign);
            engine.status(concept, logic);
            
            System.out.println("-> " + statusSign.toLowerCase() + "(" + concept + ")");
            return;
        }

        // 5. Добавление факта: a(x,y), e(x,y)
        Matcher mRel = RELATION_CMD.matcher(line);
        if (mRel.matches()) {
            String relName = mRel.group(1).toUpperCase();
            String subject = mRel.group(2);
            String predicate = mRel.group(3);

            engine.fact(relName, subject, predicate);
            System.out.println("-> " + relName.toLowerCase() + "(" + subject + "," + predicate + ")");
            return;
        }

        System.out.println("-> ERROR: Unknown command.");
    }

    static public void main(final String... args) {
        new LogicConsole().start();
    }
}
