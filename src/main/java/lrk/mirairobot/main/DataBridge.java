package lrk.mirairobot.main;

import lrk.tools.miraiutils.Utils;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

public class DataBridge {

    private static final DataBridge instance = new DataBridge();
    public static File HOME, TEMP;
    private static Properties Robot_Properties;
    public Scanner Console;
    public String OS;
    public File Robot_Prop;

    private DataBridge() {
        try {
            init();
        } catch (Exception ignored) {
        }
    }

    public static File getHOME() {
        return HOME;
    }

//    public static DataBridge getInstance() {
//        return instance;
//    }

    //给其他类获取机器人配置信息
    public static boolean setRobotProp(String key, String value) {
        Robot_Properties.put(key, value);
        return Robot_Properties.get(key).equals(value);
    }

    public static String getRobotProp(String key) {
        String value = null;
        if (Robot_Properties.get(key) != null) {
            value = (String) Robot_Properties.get(key);
        }
        return value;
    }

    //文件读取类
    public static byte[] getFileByte(String path) {
        byte[] bytes = null;
        try {
            bytes = new FileInputStream(path).readAllBytes();
        } catch (IOException e) {
            RobotNotification.Warning("IO Error:" + e.getMessage());
        }
        return bytes;
    }

    public static String getFileBase64(String path) {
        return Base64.getEncoder().encodeToString(getFileByte(path));
    }

    public static FileOutputStream openTempFileOutput(String name, boolean append) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(TEMP.getPath() + File.separator + name, append);
        } catch (FileNotFoundException ignored) {
        }
        return out;
    }

    public static String getImage(Utils util) throws IOException {
        //File image = new File(HOME.getPath() + "/tmp/" + System.currentTimeMillis() + ".png");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        //ImageIO.write(util.getImage(), "png", openFileOutput(image.getPath(), false));
        ImageIO.write(util.getImage(), "png", buffer);
        util.dispose();
        //return getFileBase64(image.getPath());
        return Base64.getEncoder().encodeToString(buffer.toByteArray());
    }

    private void init() throws Exception {
        //获取用户输入
        Console = new Scanner(System.in);
        //判断操作系统，设置相关变量与机器人数据根目录
        OS = System.getProperty("os.name");
        switch (OS) {
            case "Linux": {
                if (new File("/storage").exists()) {
                    OS = "Android";
                    HOME = new File("/storage/emulated/0/Robot");
                } else {
                    HOME = new File(System.getenv("HOME") + "/Robot");
                }
                TEMP = new File(HOME.getPath() + "/tmp");
                break;
            }
            case "Windows NT":
            default: {
                System.out.print("Not Linux/Android");
                System.exit(1);
            }
        }
        if (!HOME.exists()) {
            if (!HOME.mkdirs()) {
                RobotNotification.Warning("IO Error: Robot Home Create Failed");
                System.exit(1);
            }
            new File(HOME.getPath() + "/tmp").mkdirs();
        }
        //如果机器人配置文件不存在就新建一个
        Robot_Prop = new File(HOME.getPath() + "/robot.properties");
        if (!Robot_Prop.exists()) {
            if (!Robot_Prop.createNewFile()) {
                RobotNotification.Warning("IO Error: Robot Properties File Create Failed");
                System.exit(1);
            }
        }
        Robot_Properties = new Properties();
        try {
            Robot_Properties.load(Files.newInputStream(Robot_Prop.toPath()));
        } catch (IOException ignored) {
        }
        //初始化配置
        Robot_Properties.computeIfAbsent("OS", k -> OS);
        initRobotProp("Port", "设置MiraiConsoleLoader的IP和端口(例:0.0.0.0:8888):", "未设置");
        initRobotProp("QQ", "设置已登陆在MiralConsoleLoader上的QQ机器人账号:", "未设置");
        initRobotProp("Mode", "设置机器人的日志等级(QUIET,NORMAL,NO_LOG_SAVE):", "错误的设置");
        initRobotProp("MiraiApiHttpVerifyKey", "设置mirai-api-http的连接验证密钥(请确保您在安装此插件后已修改miari-api-http的配置文件):", "未设置");

    }

    //初始化获取机器人关键参数
    private void initRobotProp(String value, String RequireMessage, String ErrorMessage) {
        if (Robot_Properties.get(value) == null) {
            System.out.print(RequireMessage);
            String v;
            if ((v = Console.next()) == null) {
                System.out.println(ErrorMessage);
                System.exit(1);
            }
            Robot_Properties.put(value, v);
            refreshProperties();
        }
    }

    private void refreshProperties() {
        try {
            Robot_Properties.store(new FileOutputStream(Robot_Prop, false), "Robot Properties");
            Robot_Properties.clear();
            Robot_Properties.load(Files.newInputStream(Robot_Prop.toPath()));
        } catch (IOException ignored) {
        }
    }
}
