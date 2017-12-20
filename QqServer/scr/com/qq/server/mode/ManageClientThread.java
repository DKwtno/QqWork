package com.qq.server.model;

import java.util.*;
public class ManageClientThread {

	public static HashMap<String, SerConClientThread> hm=new HashMap<String, SerConClientThread>();
	
	//向hm中添加一个客户端通讯线程
	public static  void addClientThread(String uid,SerConClientThread ct)
	{
		hm.put(uid, ct);
	}
	public static  void removeClientThread(String uid)
	{
		hm.remove(uid);
	}
	public static SerConClientThread getClientThread(String uid) throws Exception
	{
		SerConClientThread a=(SerConClientThread)hm.get(uid);
		return a;
	}
	
	//返回当前在线的人的情况
	public static String getAllOnLineUserid()
	{
		//使用迭代器完成
		Iterator it=hm.keySet().iterator();
		String res="";
		while(it.hasNext())
		{
			res+=it.next().toString()+" ";
		}
		return res;
	}
}
