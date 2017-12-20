/**
 * 这是客户端和服务器端保持通讯的线程.
 */
package com.qq.client.tools;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLDocument.Iterator;

import com.qq.client.view.QqChat;
import com.qq.client.view.QqFriendList;
import com.qq.client.view.QqGroupChat;
import com.qq.common.*;
public class ClientConServerThread extends Thread {

	private Socket s;
	
	//构造函数
	public ClientConServerThread(Socket s)
	{
		this.s=s;
	}
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		while(true)
		{
			//不停读取从服务器端发来的消息
			try {
				Message m;
				LinkedList<Message> lm=null;
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				m=(Message)ois.readObject();
					
			/*	System.out.println("读取到从服务发来的消息"+ m.getSender() +" 给 "+m.getGetter()+" 内容"+
						m.getCon());*/
				if(m.getMesType().equals(MessageType.message_remove_group_member)||m.getMesType().equals(MessageType.message_add_groupchatmember)){
					//需要对现有的groupchat进行更新
					//要先remove，再添加add
					//如果退出的人是自己
					QqFriendList qfl;
					if(m.getMesType().equals(MessageType.message_remove_group_member)&&
							(qfl=ManageQqFriendList.getQqFriendList(m.getSender()))!=null){
						if(ManageQqGroupChat.getQqGroupChat(m.getCon())==null){
							new QqGroupChat(m.getCon(), m.getSender());
						}
						JOptionPane.showMessageDialog(null, "你已被踢出该群！");
						ManageQqGroupChat.getQqGroupChat(m.getCon()).dispose();
						ManageQqGroupChat.removeQqGroupChat(m.getCon());
					}
					if(m.getMesType().equals(MessageType.message_add_groupchatmember)&&
							(qfl=ManageQqFriendList.getQqFriendList(m.getSender()))!=null){
						JOptionPane.showMessageDialog(null, "你被邀请加入群"+m.getCon());
					}
					ManageQqGroupMember.removeQqGroupMemebr(m.getCon());
					ObjectOutputStream oos=new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(QqFriendList.thisowner).getS().getOutputStream());
					Message ms=new Message();
					ms.setMesType(MessageType.message_ret_groupchatmember);
					ms.setCon(m.getCon());
					ms.setSender(QqFriendList.thisowner);
					oos.writeObject(ms);
				}
				if(m.getMesType().equals(MessageType.message_group_mess)){
					QqGroupChat qqGC=ManageQqGroupChat.getQqGroupChat(m.getGetter());
					if(qqGC==null){
						qqGC=new QqGroupChat(m.getGetter(), QqFriendList.thisowner);
						ManageQqGroupChat.addQqGroupChat(m.getGetter(), qqGC);
						qqGC.hint(m);
					}
					qqGC.showMessage(m);
				}
				if(m.getMesType().equals(MessageType.message_history)){
					//把从服务器获得历史消息，显示到该显示的聊天界面
					lm=(LinkedList<Message>)ois.readObject();
					QqChat qqChat=ManageQqChat.getQqChat(m.getSender()+" "+m.getGetter());
					if(qqChat==null)
						qqChat=ManageQqChat.getQqChat(m.getGetter()+" "+m.getSender());
					if(qqChat==null)
						throw new Exception("没找到QqChat");
					//显示
					if(lm!=null)
						for(Message i:lm)
							qqChat.showHistory(i);
				}
				if(m.getMesType().equals(MessageType.message_comm_mes))
				{
					//把从服务器获得消息，显示到该显示的聊天界面
					QqChat qqChat=ManageQqChat.getQqChat(m.getGetter()+" "+m.getSender());
					
					if(qqChat==null&&ManageClientConServerThread.getClientConServerThread(m.getGetter())!=null){
						qqChat=new QqChat(m.getGetter(),m.getSender());
						ManageQqChat.addQqChat(m.getGetter()+" "+m.getSender(),qqChat);
						qqChat.hint(m);
					}
					//显示
					qqChat.showMessage(m);
				}
				if(m.getMesType().equals(MessageType.message_exit))
				{
					System.out.println("客户端接收到"+m.getSender()+"已退出");
					String getter=m.getGetter();
					System.out.println("getter="+getter);
					//修改相应的好友列表.
					QqFriendList qqFriendList=ManageQqFriendList.getQqFriendList(getter);
					
				//	if(qqFriendList)
					//更新在线好友.
					if(qqFriendList!=null)
					{
						System.out.println("（ClientConServer）"+getter+"已更新");
						qqFriendList.upateFriend(m);
					}
				}
				if(m.getMesType().equals(MessageType.message_group)){
					LinkedList<String> slm=(LinkedList<String>)ois.readObject();
					QqFriendList qqFriendList=ManageQqFriendList.getQqFriendList(m.getSender());
					if(qqFriendList!=null)
					{
						System.out.println("（qqGroup）"+m.getSender()+"已更新");
						qqFriendList.updateGroup(slm);
					}
					
				}
				if(m.getMesType().equals(MessageType.message_ret_groupchatmember)){
					lm=(LinkedList<Message>)ois.readObject();
					ManageQqGroupMember.removeQqGroupMemebr(m.getCon());
					ManageQqGroupMember.addQqGroupMemeber(m.getCon(), lm);
					if(ManageQqGroupChat.getQqGroupChat(m.getCon())!=null){
						ManageQqGroupChat.getQqGroupChat(m.getCon()).updateGroupMember();
					}
				}
				
				if(m.getMesType().equals(MessageType.message_ret_onLineFriend))
				{
					System.out.println("客户端接收到"+m.getCon());
					String con=m.getCon();
					String friends[]=con.split(" ");
					String getter=m.getGetter();
					System.out.println("getter="+getter);
					//修改相应的好友列表.
					QqFriendList qqFriendList=ManageQqFriendList.getQqFriendList(getter);
					
				//	if(qqFriendList)
					//更新在线好友.
					if(qqFriendList!=null)
					{
						System.out.println("（ClientConServer）"+getter+"已更新");
						qqFriendList.upateFriend(m);
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
				// TODO: handle exception
			}
		}
		JOptionPane.showMessageDialog(null, "你已掉线~");
		if(ManageQqFriendList.getQqFriendList(QqFriendList.thisowner)!=null){
			ManageQqFriendList.getQqFriendList(QqFriendList.thisowner).dispose();
		}
	}

	public Socket getS() {
		return s;
	}

	public void setS(Socket s) {
		this.s = s;
	}
	
}
