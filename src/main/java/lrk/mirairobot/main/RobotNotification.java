package lrk.mirairobot.main;
import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.io.FileOutputStream;
import java.util.Date;

public class RobotNotification
{
	public static Mode mode;
	private static Logger logger = Logger.getLogger("Botlog");
	private static ConsoleHandler consolehander;
	private static FileHandler filehander;
	private static RobotLogFormater formatter;
	private static File logs;
	static{
		logs = new File(DataBridge.getHOME().getPath()+"/robot.log");
		mode = Mode.valueOf(DataBridge.getRobotProp("Mode"));
		formatter = new RobotLogFormater();
		consolehander = new ConsoleHandler();
		try{
			filehander = new FileHandler(DataBridge.getHOME().getPath()+"/robot.log",true);
		}catch(IOException e){}
		consolehander.setFormatter(formatter);
		filehander.setFormatter(formatter);
		logger.addHandler(consolehander);
		logger.addHandler(filehander);
		try{
			new FileOutputStream(logs,true).write(("---------------"+new Date()+"---------------\n").getBytes());
		}catch(IOException e){}
	}
	public static void Info(String message){
		LogRecord logs = new LogRecord(Level.INFO,message);
		logger.setLevel(Level.INFO);
		consolehander.setLevel(Level.INFO);
		filehander.setLevel(Level.INFO);
		switch(mode){
			case NORMAL:{
				consolehander.publish(logs);
				filehander.publish(logs);
				break;
			}
			case QUIET:{
					filehander.publish(logs);
					break;
			}
			case NO_LOG_SAVE:{
					consolehander.publish(logs);
					break;
			}
		}
	}
	public static void Warnning(String message){
		LogRecord logs = new LogRecord(Level.WARNING,message);
		logger.setLevel(Level.WARNING);
		filehander.setLevel(Level.WARNING);
		consolehander.setLevel(Level.WARNING);
		consolehander.publish(logs);
		filehander.publish(logs);
	}
	public static void Config(String message){
		LogRecord logs = new LogRecord(Level.CONFIG,message);
		logger.setLevel(Level.CONFIG);
		filehander.setLevel(Level.CONFIG);
		consolehander.setLevel(Level.CONFIG);
		consolehander.publish(logs);
	}
	public static enum Mode{
		QUIET,
		NORMAL,
		NO_LOG_SAVE
	}
}
class RobotLogFormater extends Formatter {

	@Override
	public String format(LogRecord p1) {
		return "["+p1.getLevel()+"]\t"+p1.getMessage()+"\n";
	}
	
	
}
