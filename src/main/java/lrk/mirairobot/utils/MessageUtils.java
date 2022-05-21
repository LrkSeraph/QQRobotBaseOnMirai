package lrk.mirairobot.utils;

import lrk.mirairobot.core.data.messagetype.*;
import lrk.mirairobot.core.*;
import com.google.gson.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import lrk.mirairobot.main.DataBridge;

@SuppressWarnings("unused")
public class MessageUtils {

    public static JsonObject sendMessageToFriend(long qq, Message... messages) {
        JsonObject object = new JsonObject();
        object.addProperty("target", qq);
       	JsonArray messageChain = new JsonArray();
       	for(Message message:messages){
       		messageChain.add(message.toJsonObject());
       	}
       	object.add("messageChain",messageChain);
        return object;
    }
    
    public static JsonObject sendMessageToGroup(long groupID, Message... messages) {
        JsonObject object = new JsonObject();
        object.addProperty("target",groupID);
       	JsonArray messageChain = new JsonArray();
       	for(Message message:messages){
       		messageChain.add(message.toJsonObject());
       	}
       	object.add("messageChain",messageChain);
        return object;
    }
    /*戳一戳
    *@param target 目标QQ号
    */
    public static JsonObject sendGroupNudge(long groupID, long target){
    	JsonObject data = new JsonObject();
    	data.addProperty("target",target);
    	data.addProperty("subject",groupID);
    	data.addProperty("kind","Group");
    	return data;
    }
}