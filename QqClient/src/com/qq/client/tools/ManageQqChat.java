/**
 * 这是一个管理用户聊天界面的类
 * 还需要添加一个remove方法，实现关闭好友列表后的退出
 */
package com.qq.client.tools;

import java.util.*;
import com.qq.client.view.*;
public class ManageQqChat {

	private static HashMap hm=new HashMap<String, QqChat>();
	
	//删除
	public static void removeQqChat(String loginIdAnFriendId){
		hm.remove(loginIdAnFriendId);
	}
	//加入
	public static void addQqChat(String loginIdAnFriendId,QqChat qqChat)
	{
		hm.put(loginIdAnFriendId, qqChat);
	}
	//取出
	public static QqChat getQqChat(String loginIdAnFriendId )
	{
		return (QqChat)hm.get(loginIdAnFriendId);
	}
	
}
