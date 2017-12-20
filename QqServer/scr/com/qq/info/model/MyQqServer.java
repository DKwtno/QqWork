/**
 * 如何用于注册？
 * 这是qq服务器，它在监听，等待某个qq客户端，来连接
 */
package com.qq.server.model;
import com.qq.common.*;
import com.qq.info.ConnectAccess;
import com.qq.info.MessageHistory;
import com.qq.server.view.MyServerFrame;

import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.io.*;
public class MyQqServer {
	
	public MyQqServer()
	{
		
		try {
			
			//在9999监听
			System.out.println("我是服务器，在9999监听");
			ServerSocket ss=new ServerSocket(9999);
			//阻塞,等待连接
			while(true)
			{	
				MyServerFrame.updateOnline();
				System.out.println("等待客户中...");
				Socket s=ss.accept();
				//接收客户端发来的信息.
				ConnectAccess ca=new ConnectAccess();
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				User u=(User)ois.readObject();
				System.out.println("服务器接收到用户id:"+u.getUserId()+"  密码:"+u.getPasswd());
				Message m=new Message();
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				//退出返回10
				if(u.getType()==10){
					SerConClientThread.notifyOther(u.getUserId(),1);
					ManageClientThread.removeClientThread(u.getUserId());
					m.setMesType("9");
					oos.writeObject(m);
				}
				//账号不重复返回7
				if (u.getType()==3){
					if (!ca.idCheck(u.getUserId())) {
						//返回一个id不重复的信息
						System.out.println("账号可以用");
						m.setMesType(MessageType.message_id);
						oos.writeObject(m);

					} else {
						System.out.println("账号重复");
						m.setMesType("2");
						oos.writeObject(m);
						//关闭Socket
						s.close();

					} 
				}
				//当为注册时
				if (u.getType()==2){
					if (ca.Register(u.getUserId(), u.getPasswd())) {
						//返回一个成功注册的信息报

						m.setMesType("6");
						oos.writeObject(m);

					} else {
						m.setMesType("2");
						oos.writeObject(m);
						//关闭Socket
						s.close();

					} 
				}
				//当为登录时
				if (u.getType()==1) {
					if (ca.LogIn(u.getUserId(), u.getPasswd())&&ManageClientThread.getClientThread(u.getUserId())==null) {
						
						LinkedList<Message> lm=ConnectAccess.getUnreadMessage(u.getUserId().trim());
						//表示有未读消息
						if(lm!=null){
							@SuppressWarnings("unchecked")
							//使用一个clone来反射原链表中的项
							LinkedList<Message> lmm=(LinkedList<Message>)lm.clone();
							for(Message i:lmm){
								if(i.getSender().contains("g")){
									boolean flag=false;
									String temp=i.getSender();
									String str="";
									for(int j=2;j<temp.length();j++){
										if(temp.charAt(j)=='&')
											break;
										str+=temp.charAt(j);
									}
									str=str.trim();
									//判断成员是否仍在该组中
									for(Message ms:ConnectAccess.getGroupMember(str)){
										if(ms.getSender().equals(i.getGetter())){
											flag=true;
										}
									}
									if(!flag){
										lm.remove(i);
									}
								}
							}
							m.setMesType(MessageType.message_unread);
							oos.writeObject(m);
							oos.writeObject(lm);
						}
						else{
							//返回一个成功登陆的信息报
							m.setMesType("1");
							oos.writeObject(m);
						}
						//这里就单开一个线程，让该线程与该客户端保持通讯.
						SerConClientThread scct = new SerConClientThread(s);
						ManageClientThread.addClientThread(u.getUserId(), scct);
						//启动与该客户端通信的线程.
						scct.start();

						//并通知其它在线用户.
						scct.notifyOther(u.getUserId(),0);
					} 
					else if(ca.LogIn(u.getUserId(), u.getPasswd())&&ManageClientThread.getClientThread(u.getUserId())!=null){
						m.setMesType(MessageType.message_already_online);
						oos.writeObject(m);
						//关闭Socket
						s.close();
					}
					else {
						m.setMesType("2");
						oos.writeObject(m);
						//关闭Socket
						s.close();

					} 
				}
				
				
			}	
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
			
		}
		
	}
	
}
