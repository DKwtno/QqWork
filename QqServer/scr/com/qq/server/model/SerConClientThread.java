/**
 * 功能：是服务器和某个客户端的通信线程
 */
package com.qq.server.model;

import java.util.*;

import javax.swing.JOptionPane;

import java.net.*;
import java.io.*;
import com.qq.common.*;
import com.qq.info.ConnectAccess;
import com.qq.info.MessageHistory;
public class SerConClientThread  extends Thread{

	Socket s;
	MessageHistory mh;
	public boolean stop=false;
	public SerConClientThread(Socket s)
	{
		//把服务器和该客户端的连接赋给s
		this.s=s;
	}
	
	//让该线程去通知其它用户
	public static void notifyOther(String iam,int inn)
	{
		//得到所有在线的人的线程
		if(inn==1){
			try {
				ManageClientThread.getClientThread(iam).stop=true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ManageClientThread.removeClientThread(iam);
			try {
				if(ManageClientThread.getClientThread(iam.trim())==null)
					System.out.println("（SerConClient）移除"+iam);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HashMap hm=ManageClientThread.hm;
		Iterator it=hm.keySet().iterator();
		
		while(it.hasNext())
		{
			Message m=new Message();
			m.setCon(iam);
			if(inn==1)
				m.setMesType(MessageType.message_exit);
			else
				m.setMesType(MessageType.message_ret_onLineFriend);
			//取出在线人的id
			String onLineUserId=it.next().toString();
			try {
				ObjectOutputStream oos=new ObjectOutputStream(ManageClientThread.getClientThread(onLineUserId).s.getOutputStream());
				m.setGetter(onLineUserId);
				oos.writeObject(m);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
		}
	}
	
	public void run()
	{
		
		while(true)
		{
			if(stop)
				break;
			//这里该线程就可以接收客户端的信息.
			try {
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				Message m=(Message)ois.readObject();
				
				System.out.println(m.getSender()+" 给 "+m.getGetter()+" 说:"+m.getCon());
				
				//对从客户端取得的消息进行类型判断，然后做相应的处理
				if(m.getMesType().equals(MessageType.message_remove_group_member)){
					//移除群员
					//con为群名称
					ConnectAccess.removeGroupMember(m.getCon(), m);
					//要通知那个被踢的人
					//区分被踢和自己退出
					if(m.getSendTime().equals("踢出")){
						SerConClientThread sc1= ManageClientThread.getClientThread(m.getSender().trim());
						if(sc1==null){
							//对方不在线当然不用通知
						}
						else
						{	
	//						mh=new MessageHistory(m);
							ObjectOutputStream oos=new ObjectOutputStream(sc1.s.getOutputStream());
							oos.writeObject(m);
						}
					}
					//移除后通知所有群员
					LinkedList<Message> lm=ConnectAccess.getGroupMember(m.getCon());
					for(Message i:lm){
						SerConClientThread sc= ManageClientThread.getClientThread(i.getSender().trim());
						if(sc==null){
							//对方不在线当然不用通知
						}
						else
						{	
//							mh=new MessageHistory(m);
							ObjectOutputStream oos=new ObjectOutputStream(sc.s.getOutputStream());
							oos.writeObject(m);
						}
					}
				}
				if(m.getMesType().equals(MessageType.message_group_mess)){
					//getter是群名，sender是发出人，con是内容
					LinkedList<Message> lm1=ConnectAccess.getGroupMember(m.getGetter());
					String sender=m.getSender();
					for(Message i:lm1){
						//对发出信息的人不需要管
						if(i.getSender().equals(sender))
							continue;
						SerConClientThread sc= ManageClientThread.getClientThread(i.getSender().trim());
						if(sc==null){
							//对方不在线,消息标志为未读
							m.setRead(false);
							Message ms=new Message();
							ms.setCon(m.getCon());
							ms.setGetter(i.getSender());
							ms.setRead(false);
							ms.setSendTime(m.getSendTime());
							ms.setSender("g_"+m.getGetter()+"&"+m.getSender());
							ConnectAccess.storeUnreadMessage(ms);
							ConnectAccess.storeGroupHistory(m.getGetter(), m);
						}
						else
						{	
							ConnectAccess.storeGroupHistory(m.getGetter(), m);
							m.setRead(true);
//							mh=new MessageHistory(m);
							ObjectOutputStream oos=new ObjectOutputStream(sc.s.getOutputStream());
							oos.writeObject(m);
						}
					}
				}
				
				
				if(m.getMesType().equals(MessageType.message_group)){
					LinkedList<String> lm1=ConnectAccess.getGroupName();
					SerConClientThread sc= ManageClientThread.getClientThread(m.getSender().trim());
					ObjectOutputStream oos=new ObjectOutputStream(sc.s.getOutputStream());
					oos.writeObject(m);
					oos.writeObject(lm1);
				}
				if(m.getMesType().equals(MessageType.message_add_groupchatmember)){
					//加完成员之后希望所有客户端都更新数据
					ConnectAccess.addGroupMember(m.getCon(), m);
					LinkedList<Message> lm=ConnectAccess.getGroupMember(m.getCon());
					for(Message i:lm){
						SerConClientThread sc= ManageClientThread.getClientThread(i.getSender().trim());
						if(sc==null){
							//对方不在线
						}
						else
						{	
//							mh=new MessageHistory(m);
							ObjectOutputStream oos=new ObjectOutputStream(sc.s.getOutputStream());
							oos.writeObject(m);
						}
					}
				}
				//对所有群成员返回该群的成员变化
				//sender是发送者，con是群组名称
				if(m.getMesType().equals(MessageType.message_ret_groupchatmember)){
					LinkedList<Message> lm1=ConnectAccess.getGroupMember(m.getCon());
					for(Message i:lm1){
						SerConClientThread sc= ManageClientThread.getClientThread(i.getSender().trim());
						if(sc==null){
							//对方不在线
						}
						else{
							ObjectOutputStream oos=new ObjectOutputStream(sc.s.getOutputStream());
							oos.writeObject(m);
							oos.writeObject(lm1);
						}
					}
				} 
				if(m.getMesType().equals(MessageType.message_exit)){
					ManageClientThread.removeClientThread(m.getSender());
				}
				if(m.getMesType().equals(MessageType.message_history)){
					LinkedList<Message> lm=MessageHistory.getMessageHistory(m);
					SerConClientThread sc= ManageClientThread.getClientThread(m.getSender().trim());
					ObjectOutputStream oos=new ObjectOutputStream(sc.s.getOutputStream());
					Iterator<Message> it=lm.iterator();
					while (it.hasNext()) {
						Message temp=(Message)it.next();
						temp.setMesType(MessageType.message_history);
					}
					oos.writeObject(m);
					oos.writeObject(lm);
					
				}
				if(m.getMesType().equals(MessageType.message_comm_mes))
				{
					//一会完成转发.
					//取得接收人的通信线程
					SerConClientThread sc= ManageClientThread.getClientThread(m.getGetter().trim());

					if(sc==null){
						//对方不在线
						mh=new MessageHistory(m);
						ConnectAccess.storeUnreadMessage(m);
					}
					else
					{	
						m.setRead(true);
						mh=new MessageHistory(m);
						ObjectOutputStream oos=new ObjectOutputStream(sc.s.getOutputStream());
						oos.writeObject(m);
					}
				}
				if(m.getMesType().equals(MessageType.message_get_onLineFriend))
				{
					System.out.println(m.getSender()+" 要好友列表");
					
					//把在服务器的好友给该客户端返回.
					String res=ManageClientThread.getAllOnLineUserid();
					Message m2=new Message();
					m2.setMesType(MessageType.message_ret_onLineFriend);
					m2.setCon(res);
					m2.setGetter(m.getSender());
					ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(m2);
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "出错了！");
				break;
				// TODO: handle exception
			}
			
			
		}
		
		
	}
}
