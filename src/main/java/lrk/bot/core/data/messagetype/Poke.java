package lrk.bot.core.data.messagetype;

import com.google.gson.JsonObject;

//戳一戳

@SuppressWarnings("unused")
public class Poke extends Message {
	private final String type = "Poke";
    private final PokeType pokeType;

    public Poke(PokeType PokeType) {
        this.pokeType = PokeType;
	}
	
	public String getName(){
        return pokeType.name();
	}
	
	@Override
	public String toString() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
        data.addProperty("name", pokeType.name());
		return data.toString();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject data = new JsonObject();
		data.addProperty("type",type);
        data.addProperty("name", pokeType.name());
		return data;
	}

    public enum PokeType {
		Poke,//戳一戳
		ShowLove,//比心
		Like,//点赞
		HeartBroken,//心碎
		SixSixSix,//666
		FangDaZhao//放大招
	}
}
