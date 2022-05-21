package lrk.mirairobot.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.*;

public class RobotNotification {
	public static Mode mode;
	private static final Logger logger = Logger.getLogger("RobotLog");
	private static final ConsoleHandler consolehander;
	private static FileHandler filehander;
	private static final RobotLogFormatter formatter;
	private static final File logs;

	static {
		logs = new File(DataBridge.getHOME().getPath() + "/robot.log");
		mode = Mode.valueOf(DataBridge.getRobotProp("Mode"));
		formatter = new RobotLogFormatter();
		consolehander = new ConsoleHandler();
		try {
			filehander = new FileHandler(DataBridge.getHOME().getPath() + "/robot.log", true);
		} catch (IOException ignored) {
		}
		consolehander.setFormatter(formatter);
		filehander.setFormatter(formatter);
		logger.addHandler(consolehander);
		logger.addHandler(filehander);
		try {
			new FileOutputStream(logs, true).write(("---------------" + new Date() + "---------------\n").getBytes());
		} catch (IOException ignored) {
		}
	}
	public static void Info(String message){
		LogRecord logs = new LogRecord(Level.INFO,message);
		logger.setLevel(Level.INFO);
		consolehander.setLevel(Level.INFO);
		filehander.setLevel(Level.INFO);
		switch(mode) {
			case NORMAL: {
				consolehander.publish(logs);
				filehander.publish(logs);
				break;
			}
			case QUIET: {
				filehander.publish(logs);
				break;
			}
			case NO_LOG_SAVE: {
				consolehander.publish(logs);
				break;
			}
		}
	}

	public static void Warning(String message) {
		LogRecord logs = new LogRecord(Level.WARNING, message);
		logger.setLevel(Level.WARNING);
		filehander.setLevel(Level.WARNING);
		consolehander.setLevel(Level.WARNING);
		consolehander.publish(logs);
		filehander.publish(logs);
	}

	public static void Config(String message) {
		LogRecord logs = new LogRecord(Level.CONFIG, message);
		logger.setLevel(Level.CONFIG);
		filehander.setLevel(Level.CONFIG);
		consolehander.setLevel(Level.CONFIG);
		consolehander.publish(logs);
	}

	public static enum Mode {
		QUIET,
		NORMAL,
		NO_LOG_SAVE
	}
}
class RobotLogFormatter extends Formatter {

	@Override
	public String format(LogRecord p1) {
		return "["+p1.getLevel()+"]\t"+p1.getMessage()+"\n";
	}
	
	
}
