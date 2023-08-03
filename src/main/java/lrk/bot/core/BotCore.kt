package lrk.bot.core

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import lrk.bot.core.data.MessageSourceType
import lrk.bot.core.data.MessageSourceType.*
import lrk.bot.core.event.Event
import lrk.bot.core.event.FriendMessageEvent
import lrk.bot.core.event.GroupMessageEvent
import lrk.bot.core.listener.EventHandler
import lrk.bot.core.listener.Listener
import lrk.bot.main.DataBridge
import lrk.bot.main.RobotNotification
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Modifier
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.system.exitProcess

open class BotCore private constructor(
    private var host: String,
    private var port: Int,
    private var verifyKey: String,
    private var qq: Long
) {
    private val listeners = ArrayList<Listener>()
    private var session: String
    private var timer = Timer()

    init {
        session = verify()
        RobotNotification.Info("SessionKey:$session")
        if (bind() == "success") {
            RobotNotification.Info("绑定成功:QQ=$qq")
        } else {
            RobotNotification.Warning("绑定失败:" + bind() + "[QQ=" + qq + "]")
            exitProcess(1)
        }
        timer.schedule(object : TimerTask() {
            override fun run() {
                var messageCount: Int
                try {
                    if (this@BotCore.getMessageCount().also { messageCount = it } != 0) {
                        for (i in 0 until messageCount) {
                            val message: JsonObject? = getNextMessage()
                            handleMessage(message)
                        }
                    }
                } catch (e1: Exception) {
                    RobotNotification.Warning("${this.javaClass.name} : ${e1.message}")
                }
            }
        }, 0, 100)
    }


    @Throws(IOException::class)
    fun getMessageCount(): Int {
        val url = URL("http://$host:$port/countMessage?sessionKey=$session")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Charset", "UTF-8")
        connection.useCaches = false
        val result = JsonParser.parseReader(InputStreamReader(connection.inputStream)).asJsonObject
        return if (result["code"].toString() == "0") {
            connection.disconnect()
            result["data"].toString().toInt()
        } else {
            connection.disconnect()
            0
        }
    }

    @Throws(IOException::class)
    fun getNextMessage(): JsonObject? {
        val url = URL("http://$host:$port/fetchMessage?sessionKey=$session&count=1")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Charset", "UTF-8")
        connection.useCaches = false
        val result = JsonParser.parseReader(InputStreamReader(connection.inputStream)).asJsonObject
        return if (result["code"].toString() == "0") {
            connection.disconnect()
            result
        } else {
            connection.disconnect()
            null
        }
    }

    //登录验证
    @Throws(IOException::class)
    private fun verify(): String {
        val url = URL("http://$host:$port/verify")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Charset", "UTF-8")
        connection.useCaches = false
        val data = JsonObject()
        data.addProperty("verifyKey", verifyKey)
        connection.doOutput = true
        connection.outputStream.write(data.toString().toByteArray(StandardCharsets.UTF_8))
        val result =
            JsonParser.parseReader(InputStreamReader(connection.inputStream)).asJsonObject["session"].toString()
                .replace("\"", "")
        connection.disconnect()
        return result
    }

    //绑定SessionKey和QQ
    @Throws(IOException::class)
    private fun bind(): String {
        val url = URL("http://$host:$port/bind")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Charset", "UTF-8")
        connection.useCaches = false
        val data = JsonObject()
        data.addProperty("sessionKey", session)
        data.addProperty("qq", qq)
        connection.doOutput = true
        connection.outputStream.write(data.toString().toByteArray(StandardCharsets.UTF_8))
        val result = JsonParser.parseReader(InputStreamReader(connection.inputStream)).asJsonObject["msg"].toString()
            .replace("\"", "")
        connection.disconnect()
        return result
    }


    //发送好友消息
    @Throws(IOException::class)
    fun sendFriendMessage(data: JsonObject): JsonObject {
        val url = URL("http://$host:$port/sendFriendMessage")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Charset", "UTF-8")
        connection.useCaches = false
        connection.doOutput = true
        data.addProperty("sessionKey", session)
        connection.outputStream.write(data.toString().toByteArray(StandardCharsets.UTF_8))
        val result = JsonParser.parseReader(InputStreamReader(connection.inputStream)).asJsonObject
        connection.disconnect()
        return result
    }

    //发送群消息
    @Throws(IOException::class)
    fun sendGroupMessage(data: JsonObject): JsonObject {
        val url = URL("http://$host:$port/sendGroupMessage")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Charset", "UTF-8")
        connection.useCaches = false
        connection.doOutput = true
        data.addProperty("sessionKey", session)
        connection.outputStream.write(data.toString().toByteArray(StandardCharsets.UTF_8))
        val result = JsonParser.parseReader(InputStreamReader(connection.inputStream)).asJsonObject
        connection.disconnect()
        return result
    }

    //戳一戳
    @Throws(IOException::class)
    fun nudge(data: JsonObject): JsonObject {
        val url = URL("http://$host:$port/sendNudge")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Charset", "UTF-8")
        connection.useCaches = false
        connection.doOutput = true
        data.addProperty("sessionKey", session)
        connection.outputStream.write(data.toString().toByteArray(StandardCharsets.UTF_8))
        val result = JsonParser.parseReader(InputStreamReader(connection.inputStream)).asJsonObject
        connection.disconnect()
        return result
    }

    //撤回消息
    @Throws(IOException::class)
    fun recall(data: JsonObject): JsonObject {
        val url = URL("http://$host:$port/recall")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Charset", "UTF-8")
        connection.useCaches = false
        connection.doOutput = true
        data.addProperty("sessionKey", session)
        connection.outputStream.write(data.toString().toByteArray(StandardCharsets.UTF_8))
        val result = JsonParser.parseReader(InputStreamReader(connection.inputStream)).asJsonObject
        connection.disconnect()
        return result
    }

    //撤回消息
    @Throws(IOException::class)
    fun mute(data: JsonObject): JsonObject {
        val url = URL("http://$host:$port/mute")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Charset", "UTF-8")
        connection.useCaches = false
        connection.doOutput = true
        data.addProperty("sessionKey", session)
        connection.outputStream.write(data.toString().toByteArray(StandardCharsets.UTF_8))
        val result = JsonParser.parseReader(InputStreamReader(connection.inputStream)).asJsonObject
        connection.disconnect()
        return result
    }

    //对收到的消息进行处理
    private fun handleMessage(message: JsonObject?) {
        val messageSourceType = MessageSourceType.valueOf(message!!["data"].asJsonArray[0].asJsonObject["type"].asString)
        val data: JsonObject = message.getAsJsonArray("data")[0].asJsonObject
        when (messageSourceType) {
            FriendMessage -> callEvent(FriendMessageEvent(this, data))
            GroupMessage -> callEvent(GroupMessageEvent(this, data))
            GroupRecallEvent -> {
                RobotNotification.Info("GroupRecall: $data")
            }
            else -> {
                RobotNotification.Info("$data")
            }
        }
    }

    fun addListener(listener: Listener): Boolean {
        listener.onEnabled()
        return listeners.add(listener)
    }

    fun removeListener(listener: Listener): Boolean {
        listener.onRemoved()
        return listeners.remove(listener)
    }

    private fun callEvent(event: Event) {
        val invokeObjects = ArrayList<InvokeObject>()
        for (listener in listeners) {
            for (method in listener.javaClass.methods) {
                if (method.parameterCount != 1) { //Listener的处理函数仅允许1个参数(接收的消息对象)
                    continue
                }
                method.getAnnotation(EventHandler::class.java) ?: continue //Listener的处理函数必须有EventHandler注解
                if (!method.parameterTypes[0].isInstance(event)) { //Listener的处理函数的唯一参数必须是event的实例
                    continue
                }
                if (!Modifier.isPublic(method.modifiers)) { //Listener的处理函数必须使用public访问修饰符
                    continue
                }
                invokeObjects.add(InvokeObject(listener, method)) //添加到列表中(InvokeObject代表一个Listener的一个处理函数,即一个事件)
            }
        }
        invokeObjects.sortWith(Comparator.comparingInt { obj: InvokeObject -> obj.priority }) //通过EventHandler的参数获取事件优先级并排序
        for (`object` in invokeObjects) {
            if (event.isCancelled && `object`.isIgnoreCancelled) { //可以通过event.setCancelled()取消事件,object.isIgnoreCancelled()可以无视取消任然执行
                continue
            }
            `object`.invoke(event)
        }
    }

    companion object DEFAULT :
        BotCore(
            DataBridge.getRobotProp("Port").split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0],
            DataBridge.getRobotProp("Port").split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt(),
            DataBridge.getRobotProp("MiraiApiHttpVerifyKey"),
            DataBridge.getRobotProp("QQ").toLong()
        )
}
