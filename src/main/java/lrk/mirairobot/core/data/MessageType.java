package lrk.mirairobot.core.data;

public enum MessageType {
   Source,//消息的时间,ID等信息
   Quote,//被引用的原消息
   At,//@消息
   AtAll,//@全体
   Face,//表情消息
   Plain,//文字消息
   Image,//图片消息
   FlashImage,//闪照消息
   Voice,//语音消息
   Xml,//XML卡片消息
   Json,//JSON卡片消息
   App,//应用消息
   Poke,//戳一戳
   Dice,//未知
   MusicShare,//音乐分享
   Forward,//聊天记录分享
   File,//文件消息
   MiraiCode//I don't know
}
