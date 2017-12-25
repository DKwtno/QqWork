package com.qq.client.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import com.qq.client.data.QqGroupChatData;
import com.qq.client.tools.ManageClientConServerThread;
import com.qq.client.tools.ManageQqGroupChat;
import com.qq.client.tools.ManageQqGroupChatData;
import com.qq.client.tools.ManageQqGroupMember;
import com.qq.common.Message;
import com.qq.common.MessageType;

public class QqGroupChat extends JFrame implements ActionListener {
	//一个讨论组最多容纳五人
	static final int MAXIMUM_GROUP_MEMBER=5;
	static final int MAXIMUM_GROUP_APPLY=20;
	int number;
	String title;
	String ownerId;
	JPanel jp_left,jp_right,jp,jp_summury,jp_member;
	JTextArea jta,jta_text,jta_history;
	JFrame jf;
	JList<String> list_member;
	JTextField jtf;
	JLabel jbl;
	JScrollPane jsp,jsp1;
	JButton jb,jb_exit,jb_release,jb_history;
	QqGroupChatData qqGChatD;
	static LinkedList<Message> str;
	static String[] member;
	public static void main(String[] args) {
		QqGroupChat qq=new QqGroupChat("1","1");
		//测试初始化
	}
	//怎么对一个已经打开的项目刷新？尤其是要更改一些已经显示的文件时
	public void updateGroupMember(){
		if((str=ManageQqGroupMember.getQqGroupMemebr(title))==null)
			return;
		int count=0;
		for(int i=0;i<member.length;i++){
			member[i]="";
		}
		for(Message i:str){
			if(count<member.length)
				member[count++]=i.getSender();
			else
				try {
					throw new Exception("读取群成员错误！");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
		if(list_member==null||member==null){
			return;
		}
		list_member.setListData(member);
		list_member.setFont(new Font("雅黑", Font.PLAIN, 18));
//		jp_member.updateUI();
//		jp_right.updateUI();
	}
	
	
	public QqGroupChat(final String groupId,String qqId){
		this.setResizable(false);
		if(ManageQqGroupChatData.getQqGroupChatData(groupId)==null)
		{
			qqGChatD=new QqGroupChatData(groupId);
			ManageQqGroupChatData.addQqGroupChatData(groupId, qqGChatD);
		}
		ManageQqGroupChat.addQqGroupChat(groupId, this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ManageQqGroupChat.removeQqGroupChat(groupId);
				dispose();
			}
		});
		str=ManageQqGroupMember.getQqGroupMemebr(groupId);
		member=new String[MAXIMUM_GROUP_MEMBER];
		int count=0;
		if(str!=null)
			for(Message i:str){
				if(count<member.length)
					member[count++]=i.getSender();
				else
					try {
						throw new Exception("读取群成员错误！");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		this.ownerId=qqId;
		this.title=groupId;
		this.setLayout(null);
		jp_left=new JPanel(new BorderLayout());
		jp_right=new JPanel(null);
		jp_summury=new JPanel(new BorderLayout());
		jp_member=new JPanel(new BorderLayout());
		jb=new JButton("发送");
		jtf=new JTextField(30);
		jta=new JTextArea();
		jsp=new JScrollPane(jta);
		jb_history=new JButton("历史记录");
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jp=new JPanel(new FlowLayout());
		jbl=new JLabel("成员列表；");
		jta_text=new JTextArea();
		list_member=new JList<String>(member);
		jb_exit=new JButton("退出该群");
		jb_release=new JButton("踢出该群");
		jb_release.setEnabled(QqFriendList.isManager);
		
		jp_left.setBounds(0,0,500, 500);
		jp_right.setBounds(500,0,300, 500);
		//jp_right.setBackground(Color.black);
		//对西边进行操作
		
		jta.setEditable(false);
		jta.setLineWrap(true);
		
		jp.add(jtf,"North");
		jp.add(jb,"Center");
		jb_history.addActionListener(this);
		jp_left.add(jb_history,"North");
		jp_left.add(jsp,"Center");
		jp_left.add(jp,"South");
		//jp_left.setBackground(Color.red);
		jb.addActionListener(this);
		
		//对东边进行操作
		
		jta_text.setEditable(false);
		jta_text.setLineWrap(true);
		jta_text.append("入群须知：");
		jta_text.append("\n       成员列表第一位即为群主，群主可以邀请其他人入群。");
		jta_text.setFont(new Font("雅黑", Font.BOLD, 20));
		jbl.setFont(new Font("雅黑",  Font.BOLD +Font.ITALIC, 20));
		jbl.setForeground(Color.blue);
		jb_exit.setBounds(0,465,100,30);
		jb_exit.setForeground(Color.red);
		jb_exit.addActionListener(this);
		jb_exit.setFont(new Font("",Font.BOLD,13));
		jb_release.setBounds(170,465,100,30);
		jb_release.addActionListener(this);
		jp_summury.setBounds(0,0,300,290);
		jp_summury.add(jta_text);
		jp_member.setBounds(0,290,300,170);
		jp_member.add(jbl,"North");
		jp_member.add(list_member);
		jp_right.add(jp_summury);
		jp_right.add(jp_member);
		jp_right.add(jb_exit);
		jp_right.add(jb_release);
		if(member!=null)
			updateGroupMember();
		
		this.add(jp_left,"West");
		this.add(jp_right,"East");
		this.setTitle(title+"群"+" ( "+ownerId+" )");		
		this.setIconImage((new ImageIcon(getClass().getResource("qq.gif")).getImage()));
		this.setSize(800, 555);
		this.setVisible(true);
		showAllMessage(ManageQqGroupChatData.getQqGroupChatData(groupId).getText());
	}
	//人满了就不能再添加了
	//还要限制已经申请过的不能再申请了
	public void showHistory(LinkedList<Message> lm){
		jf=new JFrame(title+" 群的历史记录");
		jta_history=new JTextArea();
		jsp1=new JScrollPane(jta_history);
		jf.add(jsp1);
		jf.setSize(800, 600);
		jf.setVisible(true);
		jta_history.setFont(new Font("微软雅黑",Font.PLAIN,20));
		jta_history.setEditable(false);
		for(Message i:lm){
			String info=i.getSendTime()+"  "+i.getSender()+" 说:"+"\n"+"     "+i.getCon()+"\r\n";
			jta_history.append(info);
		}
		jta_history.setCaretPosition(jta_history.getDocument().getLength());
	}
	public void hint(Message m){
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource("14.wav"));
			Clip  clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null,m.getGetter()+"群有未读消息！");
	}
	private void showAllMessage(LinkedList<Message> lm){
		this.jta.setFont(new Font("微软雅黑",Font.PLAIN,20));
		for(Message i:lm){
			String info=i.getSendTime()+"  "+i.getSender()+" 说:"+"\n"+"     "+i.getCon()+"\r\n";
			this.jta.append(info);
		}
		this.jta.setCaretPosition(jta.getDocument().getLength());
	}
	public void showMessage(Message m)
	{
		try {
			ManageQqGroupChatData.getQqGroupChatData(title).addText(m);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String info=m.getSendTime()+"  "+m.getSender()+" 说:"+"\n"+"     "+m.getCon()+"\r\n";
		this.jta.setFont(new Font("微软雅黑",Font.PLAIN,20));
		this.jta.append(info);
		this.jta.setCaretPosition(jta.getDocument().getLength());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//点击历史记录
		if(e.getSource()==jb_history){
			if(jf!=null){
				jf.dispose();
				jf=null;
				jta_history=null;
			}
			try {
				ObjectOutputStream oos=new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream());
				//con是群名，sender是用户
				Message m=new Message();
				m.setMesType(MessageType.message_ret_group_history);
				m.setSender(ownerId);
				m.setCon(title);
				oos.writeObject(m);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		//点击退出群组
		if(e.getSource()==jb_exit){
			int choice=JOptionPane.showConfirmDialog(null, "是否确定退出该群？");
			if(choice==0){
				JOptionPane.showMessageDialog(null, "您已退出该群！");
				//接下来要给server一个removeGroupMember的Message
				try {
					ObjectOutputStream oos=new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream());
					Message m=new Message();
					m.setMesType(MessageType.message_remove_group_member);
					//sender是要移出去的人，con是群组名
					m.setSender(ownerId);
					m.setCon(title);
					m.setSendTime(new Date().toString());
					oos.writeObject(m);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ManageQqGroupChat.removeQqGroupChat(title);
				LinkedList<Message> temp=ManageQqGroupMember.getQqGroupMemebr(title);
				for(Message i:temp){
					if(i.getSender().equals(ownerId)){
						temp.remove(i);
						break;
					}
				}
				ManageQqGroupMember.removeQqGroupMemebr(title);
				ManageQqGroupMember.addQqGroupMemeber(title, temp);
				this.dispose();
			}
		}
		//点击踢出该群
		if(e.getSource()==jb_release){
			if(list_member.isSelectionEmpty()||list_member.getSelectedValue()==null){
				JOptionPane.showMessageDialog(null, "请选中要踢出群的对象！");
				return;
			}
			String outer=list_member.getSelectedValue();
			if(outer.equals(ownerId)){
				JOptionPane.showMessageDialog(null, "请不要踹自己的屁股！");
				return;
			}
			int choice=JOptionPane.showConfirmDialog(null, "是否确定将"+outer+"踢出该群？");
			if(choice==0){
				//将组员踢出该群
				Message out=new Message();
				out.setMesType(MessageType.message_remove_group_member);
				out.setSender(outer);
				out.setCon(title);
				out.setSendTime("踢出");
				try {
					new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream()).writeObject(out);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for(int i=0;i<member.length;i++){
					if(member[i].equals(outer)){
						for(int j=i;j<member.length-1;j++){
							member[j]=member[j+1];
						}
						member[member.length-1]="";
						break;
					}
				}
				list_member.setListData(member);
				list_member.setFont(new Font("雅黑", Font.PLAIN, 18));
				JOptionPane.showMessageDialog(null, "完成操作！");
			}
		}
		if(e.getSource()==jb)
		{
			//如果用户点击了发送按钮
			if(jtf.getText().equals(""))
				return;
			Date currentTime = new Date();
			//sender是本人，getter是群名
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(currentTime);
			Message m=new Message();
			m.setMesType(MessageType.message_group_mess);
			m.setSender(this.ownerId.trim());
			m.setCon(jtf.getText());
			m.setSendTime(dateString);
			m.setGetter(title);
			jtf.setText("");
			showMessage(m);
			//发送给服务器.
			try {
				ObjectOutputStream oos=new ObjectOutputStream
				(ManageClientConServerThread.getClientConServerThread(this.ownerId).getS().getOutputStream());
				oos.writeObject(m);
			} catch (Exception ex) {
				ex.printStackTrace();
				// TODO: handle exception
			}
			
			
		}
	}

}
