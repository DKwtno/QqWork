package com.qq.client.tools;

import java.util.HashMap;

import com.qq.client.view.QqChat;
import com.qq.client.view.QqGroupChat;

/**
 * 管理QQ群
 * @author weizhiwei
 *
 */
public class ManageQqGroupChat {
	private static HashMap hm=new HashMap<String, QqGroupChat>(); 
	public static void removeQqGroupChat(String GroupTitle){
		hm.remove(GroupTitle);
	}
	//加入
	public static void addQqGroupChat(String GroupTitle,QqGroupChat qqGChat)
	{
		hm.put(GroupTitle, qqGChat);
	}
	//取出
	public static QqGroupChat getQqGroupChat(String GroupTitle )
	{
		return (QqGroupChat)hm.get(GroupTitle);
	}
	
}
