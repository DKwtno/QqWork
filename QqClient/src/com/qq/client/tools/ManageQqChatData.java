package com.qq.client.tools;

import java.util.HashMap;

import com.qq.client.data.QqChatData;

public class ManageQqChatData {
	private static HashMap hm=new HashMap<String, QqChatData>(); 
	public static void removeQqChatData(String GroupTitle){
		hm.remove(GroupTitle);
	}
	//加入
	public static void addQqChatData(String GroupTitle,QqChatData qqGChatD)
	{
		hm.put(GroupTitle, qqGChatD);
	}
	//取出
	public static QqChatData getQqChatData(String GroupTitle)
	{
		return (QqChatData)hm.get(GroupTitle);
	}
}
