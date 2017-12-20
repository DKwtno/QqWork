package com.qq.client.tools;

import java.util.HashMap;

import com.qq.client.data.QqGroupChatData;

public class ManageQqGroupChatData {
	private static HashMap hm=new HashMap<String, QqGroupChatData>(); 
	public static void removeQqGroupChatData(String GroupTitle){
		hm.remove(GroupTitle);
	}
	//加入
	public static void addQqGroupChatData(String GroupTitle,QqGroupChatData qqGChatD)
	{
		hm.put(GroupTitle, qqGChatD);
	}
	//取出
	public static QqGroupChatData getQqGroupChatData(String GroupTitle)
	{
		return (QqGroupChatData)hm.get(GroupTitle);
	}
}
