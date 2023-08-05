package lrk.bot.core.data.messagetype;

import com.google.gson.*;

@SuppressWarnings("unused")
public abstract class Message{
	public final String type = "";

	@Override
	public abstract String toString();
	
	public abstract JsonObject toJsonObject();
}
