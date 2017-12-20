/**
 * 这是与好友聊天的界面
 * 因为客户端，要处于读取的状态，因此我们把它做成一个线程
 */
package com.qq.client.view;

import com.qq.client.tools.*;
import com.qq.client.data.QqChatData;
import com.qq.client.data.QqGroupChatData;
import com.qq.client.model.*;
import com.qq.common.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.text.*;
public class QqChat extends JFrame implements ActionListener{

	JTextArea jta;
	JTextField jtf;
	JButton jb,jb2;
	JPanel jp;
	JScrollPane jsp;
	String ownerId;
	String friendId;
	String title;
	QqChatData qqGChatD;
	boolean online=false;
	public boolean isOpen=false;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QqChat qqChat=new QqChat("1","s");
	}
	
	public QqChat(String ownerId,String friend)
	{	
		title=ownerId+" "+friend;
		//缓存信息池
		if(ManageQqChatData.getQqChatData(title)==null)
		{
			qqGChatD=new QqChatData(title);
			ManageQqChatData.addQqChatData(title, qqGChatD);
		}
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ManageQqChat.removeQqChat(ownerId.trim()+" "+friendId.trim());
				dispose();
			}
		});
		this.setResizable(false);
		this.ownerId=ownerId;
		this.friendId=friend;
		jta=new JTextArea();
		jtf=new JTextField(15);
		jb=new JButton("发送");
		jb2=new JButton("历史记录");
		jb2.addActionListener(this);
		jb.addActionListener(this);
		jp=new JPanel(new FlowLayout());
		jp.add(jtf,"North");
		jp.add(jb,"Center");
		jta.setEditable(false);
		jta.setLineWrap(true);
		jsp=new JScrollPane(jta);
		for(int i=0;i<50;i++){
			if(ManageQqFriendList.getQqFriendList(ownerId)!=null&&ManageQqFriendList.getQqFriendList(ownerId).jb1s[i].getText().trim().equals(friend)){
				if(ManageQqFriendList.getQqFriendList(ownerId).jb1s[i].isEnabled())
					online=true;
				else
					online=false;
			}
		}
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(jb2,"North");
		this.add(jsp,"Center");
		this.add(jp,"South");
		if(online)
			this.setTitle(ownerId+" 正在和 "+friend+"（在线中）"+" 聊天");
		else
			this.setTitle(ownerId+" 正在和 "+friend+"（离线中）"+" 聊天");
		this.setIconImage((new ImageIcon(getClass().getResource("qq.gif")).getImage()));
		this.setSize(700, 400);
		this.setVisible(true);
		showAllMessage(ManageQqChatData.getQqChatData(title).getText());
		
	}
	//提示收到消息
	public void hint(Message m){
		JOptionPane.showMessageDialog(null,m.getSender()+" 给您发送消息了！");
	}
	//显示所有信息池内的信息
	private void showAllMessage(LinkedList<Message> lm){
		this.jta.setFont(new Font("微软雅黑",Font.PLAIN,20));
		for(Message i:lm){
			String info=i.getSendTime()+"  "+i.getSender()+" 对 "+"  "+i.getGetter()+" 说:"+"\n"+"     "+i.getCon()+"\r\n";
			this.jta.append(info);
		}
		this.jta.setCaretPosition(jta.getDocument().getLength());
	}
	//显示历史记录
	public void showHistory(Message m){
		String info=m.getSendTime()+"  "+m.getSender()+" 对 "+m.getGetter()+" 说:"+"\n"+"     "+m.getCon()+"\r\n";
		this.jta.setFont(new Font("微软雅黑",Font.PLAIN,20));
		this.jta.append(info);
		this.jta.setCaretPosition(jta.getDocument().getLength());
	}
	//写一个方法，让它显示消息
	public void showMessage(Message m)
	{
		try {
			ManageQqChatData.getQqChatData(title).addText(m);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String info=m.getSendTime()+"  "+m.getSender()+" 对 "+m.getGetter()+" 说:"+"\n"+"     "+m.getCon()+"\r\n";
		this.jta.setFont(new Font("微软雅黑",Font.PLAIN,20));
		this.jta.append(info);
		this.jta.setCaretPosition(jta.getDocument().getLength());
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource()==jb2)
		{
			//如果用户点击了历史记录按钮
			Message m=new Message();
			m.setGetter(friendId.trim());
			m.setSender(ownerId.trim());
			m.setCon("用户要历史记录");
			m.setMesType(MessageType.message_history);
			jtf.setText("");
			jta.setText("");
			//发送给服务器.
			try {
				ObjectOutputStream oos=new ObjectOutputStream
				(ManageClientConServerThread.getClientConServerThread(this.ownerId).getS().getOutputStream());
				oos.writeObject(m);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
			
		}
		
		if(arg0.getSource()==jb)
		{
			//如果用户点击了发送按钮
			if(jtf.getText().equals(""))
				return;
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(currentTime);
			Message m=new Message();
			m.setMesType(MessageType.message_comm_mes);
			m.setSender(this.ownerId.trim());
			m.setGetter(this.friendId.trim());
			m.setCon(jtf.getText());
			m.setSendTime(dateString);
			jtf.setText("");
			showMessage(m);
			//发送给服务器.
			try {
				ObjectOutputStream oos=new ObjectOutputStream
				(ManageClientConServerThread.getClientConServerThread(this.ownerId).getS().getOutputStream());
				oos.writeObject(m);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
			
		}
			
	}

//	public void run() {
//		// TODO Auto-generated method stub
//		while(true)
//		{
//			try {
//				//读取[如果读不到就等待.]
//				ObjectInputStream ois=new ObjectInputStream(QqClientConServer.s.getInputStream());
//				
//				Message m=(Message)ois.readObject();
//				
//				//显示
//				String info=m.getSender()+" 对 "+m.getGetter()+" 说:"+m.getCon()+"\r\n";
//				this.jta.append(info);
//				
//				
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//				// TODO: handle exception
//			}
//		
//			
//			
//		}
//		
//	}

}
