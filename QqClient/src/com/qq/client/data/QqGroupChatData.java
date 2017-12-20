package com.qq.client.data;

import java.util.LinkedList;

import com.qq.common.Message;
/**
 * QQ群的聊天记录
 * @author weizhiwei
 *
 */
public class QqGroupChatData {
	public static String title;
	LinkedList<Message> textArea;
	public QqGroupChatData(String GroupTitle){
		this.title=GroupTitle;
		textArea=new LinkedList<Message>();
	}
	public void addText(Message text){
		textArea.add(text);
	}
	public LinkedList<Message> getText(){
		return textArea;
	}
}
