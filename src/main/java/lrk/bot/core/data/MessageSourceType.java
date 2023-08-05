package lrk.bot.core.data;

@SuppressWarnings("unused")
public enum MessageSourceType {
    BotOnlineEvent,//Bot登录成功
    BotOfflineEventActive,//Bot主动离线
    BotOfflineEventForce,//Bot被挤下线
    BotOfflineEventDropped,//Bot被服务器断开或因网络问题而掉线
    BotReloginEvent,//Bot主动重新登录
    FriendInputStatusChangedEvent,//好友输入状态改变
    FriendNickChangedEvent,//好友昵称改变
    BotGroupPermissionChangeEvent,//Bot在群里的权限被改变, 操作人一定是群主
    BotMuteEvent,//Bot被禁言
    BotUnmuteEvent,//Bot被取消禁言
    BotJoinGroupEvent,//Bot加入了一个新群
    BotLeaveEventActive,//Bot主动退出一个群
    BotLeaveEventKick,//Bot被踢出一个群
    BotLeaveEventDisband,//Bot因群主解散群而退出群, 操作人一定是群主
    GroupRecallEvent,//群消息撤回
    FriendRecallEvent,//好友消息撤回
    NudgeEvent,//戳一戳事件
    GroupNameChangeEvent,//某个群名改变
    GroupEntranceAnnouncementChangeEvent,//某群入群公告改变
    GroupMuteAllEvent,//全员禁言
    GroupAllowAnonymousChatEvent,//匿名聊天
    GroupAllowConfessTalkEvent,//坦白说
    GroupAllowMemberInviteEvent,//允许群员邀请好友加群
    MemberJoinEvent,//新人入群的事件
    MemberLeaveEventKick,//成员被踢出群(该成员不是Bot)
    MemberLeaveEventQuit,//成员主动离群(该成员不是Bot)
    MemberCardChangeEvent,//群名片改动
    MemberSpecialTitleChangeEvent,//群头衔改动(只有群主有操作限权)
    MemberPermissionChangeEvent,//成员权限改变的事件(该成员不是Bot)
    MemberMuteEvent,//群成员被禁言事件(该成员不是Bot)
    MemberUnmuteEvent,//群成员被取消禁言事件(该成员不是Bot)
    MemberHonorChangeEvent,//群员称号改变
    NewFriendRequestEvent,//添加好友申请
    MemberJoinRequestEvent,//用户入群申请(Bot需要有管理员权限)
    BotInvitedJoinGroupRequestEvent,//Bot被邀请入群申请
    OtherClientOnlineEvent,//其他客户端上线
    OtherClientOfflineEvent,//其他客户端下线
    CommandExecutedEvent,//命令被执行
    FriendMessage,//好友消息
    FriendSyncMessage,//同步好友消息
    GroupMessage,//群消息
    GroupSyncMessage,//同步群消息
    TempMessage,//群临时消息
    TempSyncMessage,//同步群临时消息
    StrangerMessage,//陌生人消息
    StrangerSyncMessage,//同步陌生人消息
    OtherClientMessage//其他客户端消息
}