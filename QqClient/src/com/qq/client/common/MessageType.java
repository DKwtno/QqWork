/**
 * 定义包的种类
 */
package com.qq.common;

public interface MessageType {

	String message_succeed="1";//表明是登陆成功
	String message_fail="2";//表明失败
	String message_comm_mes="3";//普通信息包
	String message_get_onLineFriend="4";//要求在线好友的包
	String message_ret_onLineFriend="5";//返回在线好友的包
	String message_register="6";//表明是注册成功
	String message_id="7";//表明是账号不重复
	String message_history="8";//表明要历史记录
	String message_exit="9";//表明要退出
	String message_unread="10";//表示有未读消息
	String message_already_online="11";//表示已经在线
	String message_add_groupchatmember="12";//表示添加群组好友
	String message_ret_groupchatmember="13";//表示返回群组好友
	String message_group_mess="14";//表示群组的消息
	String message_group_hist="15";//表示群组的历史记录
	String message_group="16";//返回已注册的群组
	String message_remove_group_member="17";//删除群成员
}
