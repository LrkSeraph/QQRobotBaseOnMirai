package lrk.bot.main

import lrk.tools.miraiutils.Utils
import java.io.*
import java.nio.file.Files
import java.util.*
import javax.imageio.ImageIO
import kotlin.system.exitProcess

object DataBridge {
    @JvmStatic
    lateinit var HOME: File
    private lateinit var TEMP: File
    private lateinit var Robot_Properties: Properties
    private lateinit var Console: Scanner
    private lateinit var OS: String
    private lateinit var Robot_Prop: File

    init {
        try {
            init()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @Throws(Exception::class)
    private fun init() {
        //获取用户输入
        Console = Scanner(System.`in`)
        //判断操作系统，设置相关变量与机器人数据根目录
        OS = System.getProperty("os.name")
        when (OS) {
            "Linux" -> {
                if (File("/storage").exists()) {
                    OS = "Android"
                    HOME = File("/storage/emulated/0/Robot")
                } else {
                    HOME = File(System.getProperty("user.home") + "/Robot")
                }
                TEMP = File(HOME.path + "/tmp")
            }

            else -> {
                print("Not Linux/Android")
                exitProcess(1)
            }
        }
        if (!HOME.exists()) {
            if (!HOME.mkdirs()) {
                RobotNotification.Warning("IO Error: Robot HomeDir Create Failed")
                exitProcess(1)
            }
            if (!TEMP.mkdirs()) {
                RobotNotification.Warning("IO Error: Robot TempDir Create Failed")
                exitProcess(1)
            }
        }
        //如果机器人配置文件不存在就新建一个
        Robot_Prop = File(HOME.path + "/robot.properties")
        if (!Robot_Prop.exists()) {
            if (!Robot_Prop.createNewFile()) {
                RobotNotification.Warning("IO Error: Robot Properties File Create Failed")
                exitProcess(1)
            }
        }
        Robot_Properties = Properties()
        try {
            Robot_Properties.load(Files.newInputStream(Robot_Prop.toPath()))
        } catch (ignored: IOException) {
        }
        //初始化配置
        Robot_Properties.computeIfAbsent("OS") { OS }
        initRobotProp("Port", "设置MiraiConsoleLoader的IP和端口(例:0.0.0.0:8888):", "未设置")
        initRobotProp("QQ", "设置已登陆在MiraiConsoleLoader上的QQ机器人账号:", "未设置")
        initRobotProp("Mode", "设置机器人的日志等级(QUIET,NORMAL,NO_LOG_SAVE):", "错误的设置")
        initRobotProp(
            "MiraiApiHttpVerifyKey",
            "设置mirai-api-http的连接验证密钥(请确保您在安装此插件后已修改mirai-api-http的配置文件):",
            "未设置"
        )
    }

    //初始化获取机器人关键参数
    private fun initRobotProp(value: String, requireMessage: String, errorMessage: String) {
        if (Robot_Properties[value] == null) {
            print(requireMessage)
            var v: String?
            if (Console.next().also { v = it } == null) {
                println(errorMessage)
                exitProcess(1)
            }
            Robot_Properties[value] = v
            refreshProperties()
        }
    }

    private fun refreshProperties() {
        try {
            Robot_Properties.store(FileOutputStream(Robot_Prop, false), "Robot Properties")
            Robot_Properties.clear()
            Robot_Properties.load(Files.newInputStream(Robot_Prop.toPath()))
        } catch (e: IOException) {
            RobotNotification.Warning("IO Error: Robot Properties File Refresh Failed")
            exitProcess(1)
        }
    }

    @JvmStatic
    //给其他类获取机器人配置信息
    fun setRobotProp(key: String?, value: String): Boolean {
        Robot_Properties[key] = value
        return Robot_Properties[key] == value
    }

    @JvmStatic
    fun getRobotProp(key: String?): String {
        var value = ""
        if (Robot_Properties[key] != null) {
            value = Robot_Properties[key] as String
        }
        return value
    }

    //文件读取类
    private fun getFileByte(path: String): ByteArray? {
        val bytes: ByteArray? = try {
            FileInputStream(path).use { fileInputStream -> fileInputStream.readAllBytes() }
        } catch (e: IOException) {
            RobotNotification.Warning("IO Error:" + e.message)
            null
        }
        return Objects.requireNonNull(bytes)
    }

    @JvmStatic
    fun getFileBase64(path: String): String {
        return Base64.getEncoder().encodeToString(getFileByte(path))
    }

    @JvmStatic
    fun openTempFileOutput(name: String, append: Boolean): FileOutputStream? {
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(TEMP.path + File.separator + name, append)
        } catch (ignored: FileNotFoundException) {
        }
        return out
    }

    @JvmStatic
    @Throws(IOException::class)
    fun getImage(util: Utils): String {
        val buffer = ByteArrayOutputStream()
        ImageIO.write(util.getImage(), "png", buffer)
        util.dispose()
        return Base64.getEncoder().encodeToString(buffer.toByteArray())
    }

}
