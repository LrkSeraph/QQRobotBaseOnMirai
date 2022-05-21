package lrk.mirairobot.core;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import com.google.gson.*;
import java.nio.charset.*;
import lrk.mirairobot.event.*;
import lrk.mirairobot.listener.*;
import lrk.mirairobot.main.*;
import lrk.mirairobot.data.*;

public class BotCore{

	String session = "";
	String verifyKey = "";
	String host = "";
	int port;
	long qq;
	private ArrayList<Listener> listeners = new ArrayList<Listener>();
	
	Timer core = new Timer();
	public BotCore(String host,int port,String verifyKey,long qq) throws Exception{
		this.host = host;
		this.port = port;
		this.qq = qq;
		this.verifyKey = verifyKey;
		session = verify();
		RobotNotification.Info("SessionKey:"+session);
		if(bind().equals("success")){
			RobotNotification.Info("绑定成功:QQ="+qq);
		}else{
			RobotNotification.Warnning("绑定失败:"+bind());
			System.exit(1);
		}
		core.schedule(new TimerTask(){
			@Override
			public void run(){
				int messageCount = 0;
				try{
    				if((messageCount = getMessageCount()) != 0){
    					for(int i = 0;i < messageCount;i++){
    						JsonObject message = getNextMessage();
    						handleMessage(message);
    					}
    				}
				}catch (Exception e){}
			}
		},0,100);
	}
	//登录验证
	private String verify() throws IOException,MalformedURLException,ProtocolException{
		URL url = new URL("http://" + host + ":" + port + "/verify");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        JsonObject data = new JsonObject();
        data.addProperty("verifyKey",verifyKey);
        connection.setDoOutput(true);
        connection.getOutputStream().write(data.toString().getBytes(StandardCharsets.UTF_8));
        return (String)JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject().get("session").toString().replace("\"","");
	}
	//绑定SessionKey和QQ
	private String bind() throws IOException,MalformedURLException,ProtocolException{
		URL url = new URL("http://" + host + ":" + port + "/bind");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        JsonObject data = new JsonObject();
        data.addProperty("sessionKey",session);
        data.addProperty("qq",qq);
        connection.setDoOutput(true);
        connection.getOutputStream().write(data.toString().getBytes(StandardCharsets.UTF_8));
        return (String)JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject().get("msg").toString().replace("\"","");
	}
	//获取下一条待处理的消息
	public JsonObject getNextMessage() throws IOException,MalformedURLException,ProtocolException{
		URL url = new URL("http://" + host + ":" + port + "/fetchMessage?sessionKey="+session+"&count=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        JsonObject result = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
        if(result.get("code").toString().equals("0")){
        	return result;
        }else{
        	return null;
        }
	}
	//获取队列中的消息数量
	public int getMessageCount() throws IOException,MalformedURLException,ProtocolException{
		URL url = new URL("http://" + host + ":" + port + "/countMessage?sessionKey="+session);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        JsonObject result = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
		if(result.get("code").toString().equals("0")){
        	return Integer.parseInt(result.get("data").toString());
        }else{
        	return 0;
        }
	}
	//对收到的消息进行处理
	private void handleMessage(JsonObject message){
		MessageFromType messageFromType = MessageFromType.valueOf(message.get("data").getAsJsonArray().get(0).getAsJsonObject().get("type").getAsString());
		JsonObject data = message.getAsJsonArray("data").get(0).getAsJsonObject();
		switch(messageFromType){
			case FriendMessage:{
				callEvent(new FriendMessageEvent(this,data));
				break;
			}
			case GroupMessage:{
				callEvent(new GroupMessageEvent(this,data));
				break;
			}
		}
	}
	//发送好友消息
	public JsonObject sendFriendMessage(JsonObject data) throws IOException,MalformedURLException,ProtocolException{
		URL url = new URL("http://" + host + ":" + port + "/sendFriendMessage");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        data.addProperty("sessionKey",session);
        connection.getOutputStream().write(data.toString().getBytes(StandardCharsets.UTF_8));
        return JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
	}
	//发送群消息
	public JsonObject sendGroupMessage(JsonObject data) throws IOException,MalformedURLException,ProtocolException{
		URL url = new URL("http://" + host + ":" + port + "/sendGroupMessage");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        data.addProperty("sessionKey",session);
        connection.getOutputStream().write(data.toString().getBytes(StandardCharsets.UTF_8));
        return JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
	}
	//戳一戳
	public JsonObject nudge(JsonObject data) throws IOException,MalformedURLException,ProtocolException{
		URL url = new URL("http://" + host + ":" + port + "/sendNudge");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        data.addProperty("sessionKey",session);
        connection.getOutputStream().write(data.toString().getBytes(StandardCharsets.UTF_8));
        return JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
	}
	
	public boolean addListener(Listener listener) {
        return listeners.add(listener);
    }

    public boolean removeListener(Listener listener) {
        return listeners.remove(listener);
    }

    public void callEvent(Event event) {
        ArrayList<InvokeObject> invokeObjects = new ArrayList<>();
        for (Listener listener : listeners) {
            for (Method method : listener.getClass().getMethods()) {
                if (method.getParameterCount() != 1) {//Listener的处理函数仅允许1个参数(接收的消息对象)
                    continue;
                }
                EventHandler eventHandler = method.getAnnotation(EventHandler.class);
                if (eventHandler == null) {//Listener的处理函数必须有EventHandler注解
                    continue;
                }
                if (!method.getParameterTypes()[0].isInstance(event)) {//Listener的处理函数的唯一参数必须继承lrk.mirairobot.event.Event
                    continue;
                }
                if (!Modifier.isPublic(method.getModifiers())) {//Listener的处理函数必须使用public访问修饰符
                    continue;
                }
                invokeObjects.add(new InvokeObject(listener, method));//添加到列表中(InvokeObject代表一个Listener的一个处理函数,即一个事件)
            }
        }
        invokeObjects.sort(Comparator.comparingInt(InvokeObject::getPriority));//通过EventHandler的参数获取事件优先级并排序

        for (InvokeObject object : invokeObjects) {
            if (event.isCancelled() && object.isIgnoreCancelled()) {//可以通过event.isCancelled()取消事件,object.isIgnoreCancelled()可以无视取消任然执行,精妙的逻辑
                continue;
            }
            object.invoke(event);
        }
    }
}


