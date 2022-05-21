package lrk.mirairobot.core.data.messagetype;
import com.google.gson.*;

public abstract class Message{
	public final String type = "";
	
	public Message() {}

	@Override
	public abstract String toString();
	
	public abstract JsonObject toJsonObject();
}
