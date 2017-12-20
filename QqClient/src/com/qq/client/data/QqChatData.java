package com.qq.client.data;

import java.util.LinkedList;

import com.qq.common.Message;

/**
 * 处理聊天记录缓存
 * @author weizhiwei
 *
 */
public class QqChatData {
	public static String title;
	LinkedList<Message> textArea;
	public QqChatData(String GroupTitle){
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
