package lrk.bot.core.data;

public enum MessageSourceType {
	GroupRecallEvent,//群消息撤回事件
	BotOnlineEvent, //机器人上线事件
	FriendMessage,//好友消息
	GroupMessage,//群消息
	TempMessage,//群临时消息
	StrangerMessage,//陌生人消息
	OtherClientMessage//其他客户端消息
}