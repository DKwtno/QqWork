package com.qq.client.tools;

import java.util.HashMap;
import java.util.LinkedList;

import com.qq.common.Message;

public class ManageQqGroupMember {
	private static HashMap hm=new HashMap<String, LinkedList<Message>>(); 
	public static void removeQqGroupMemebr(String GroupTitle){
		hm.remove(GroupTitle);
	}
	//加入
	public static void addQqGroupMemeber(String GroupTitle,LinkedList<Message> qqGMemebr)
	{
		hm.put(GroupTitle, qqGMemebr);
	}
	//取出
	public static LinkedList<Message> getQqGroupMemebr(String GroupTitle )
	{
		return (LinkedList<Message>)hm.get(GroupTitle);
	}
}
