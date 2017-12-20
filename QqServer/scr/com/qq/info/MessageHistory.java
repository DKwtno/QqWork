/**
 * 要求存储聊天记录（离线）
 */
package com.qq.info;
import java.util.LinkedList;

import com.qq.common.Message;
public class MessageHistory {
	public MessageHistory(Message m){
		//当发送者的id大于接受者的id时，tableName为接受者+发送者，总之保证大的在前小的在后
		//不存在发送者id=接受者id的情况（除非自己给自己发）
		String tableName=Integer.valueOf(m.getGetter())>Integer.valueOf(m.getSender())?
				m.getGetter().trim()+"_"+m.getSender().trim():m.getSender().trim()+"_"+m.getGetter().trim();
		ConnectAccess.storeHistory(tableName, m);
	}
	public static LinkedList<Message> getMessageHistory(Message m){
		String tableName=Integer.valueOf(m.getGetter())>Integer.valueOf(m.getSender())?
				m.getGetter().trim()+"_"+m.getSender().trim():m.getSender().trim()+"_"+m.getGetter().trim();
		return ConnectAccess.getHistory(tableName);
	}
}
