package lrk.bot.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.*;

public class RobotNotification {
    private static final Logger logger = Logger.getLogger("RobotLog");
    private static final ConsoleHandler CONSOLE_HANDLER;
    private static final RobotLogFormatter formatter;
    private static final File logs;
    public static Mode mode;
    private static FileHandler fileHandler;

    static {
        logs = new File(DataBridge.getHOME().getPath() + "/robot.log");
        mode = Mode.valueOf(DataBridge.getRobotProp("Mode"));
        formatter = new RobotLogFormatter();
        CONSOLE_HANDLER = new ConsoleHandler();
        try {
            fileHandler = new FileHandler(DataBridge.getHOME().getPath() + "/robot.log", true);
        } catch (IOException ignored) {
        }
        CONSOLE_HANDLER.setFormatter(formatter);
        fileHandler.setFormatter(formatter);
        logger.addHandler(CONSOLE_HANDLER);
        logger.addHandler(fileHandler);
        try (FileOutputStream fileOutputStream = new FileOutputStream(logs, true)) {
            fileOutputStream.write(("---------------" + new Date() + "---------------\n").getBytes());
        } catch (IOException e) {
            RobotNotification.Warning("IO Error: Log Write Failed");
            System.exit(1);
        }
    }

    public static void Info(String message) {
        LogRecord logs = new LogRecord(Level.INFO, message);
        logger.setLevel(Level.INFO);
        CONSOLE_HANDLER.setLevel(Level.INFO);
        fileHandler.setLevel(Level.INFO);
        switch (mode) {
            case NORMAL -> {
                CONSOLE_HANDLER.publish(logs);
                fileHandler.publish(logs);
            }
            case QUIET -> fileHandler.publish(logs);
            case NO_LOG_SAVE -> CONSOLE_HANDLER.publish(logs);
        }
    }

    public static void Warning(String message) {
        LogRecord logs = new LogRecord(Level.WARNING, message);
        logger.setLevel(Level.WARNING);
        fileHandler.setLevel(Level.WARNING);
        CONSOLE_HANDLER.setLevel(Level.WARNING);
        CONSOLE_HANDLER.publish(logs);
        fileHandler.publish(logs);
    }

    public static void Config(String message) {
        LogRecord logs = new LogRecord(Level.CONFIG, message);
        logger.setLevel(Level.CONFIG);
        fileHandler.setLevel(Level.CONFIG);
        CONSOLE_HANDLER.setLevel(Level.CONFIG);
        CONSOLE_HANDLER.publish(logs);
    }

    public enum Mode {
        QUIET,
        NORMAL,
        NO_LOG_SAVE
    }
}

class RobotLogFormatter extends Formatter {

    @Override
    public String format(LogRecord p1) {
        return "[" + p1.getLevel() + "]\t" + p1.getMessage() + "\n";
    }


}
