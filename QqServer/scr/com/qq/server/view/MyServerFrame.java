/**
 * 这是服务器端的控制界面，可以完成启动服务器，关闭服务器
 * 可以管理和监控用户.
 * @ weizhiwei
 */
package com.qq.server.view;


import javax.swing.*;

import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.info.ConnectAccess;
import com.qq.server.model.ManageClientThread;
import com.qq.server.model.MyQqServer;
import com.qq.server.model.SerConClientThread;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
public class MyServerFrame extends JFrame implements ActionListener,MouseListener {

	
	JPanel jp1,jp2,jp_menu,jp_diary;
	JButton jb1,jb2,jbT,jb0;
	JLabel jlb1,jlb2,jlb3;
	static JTextArea jta_diary;
	JScrollPane jsp;
	static JList<String> jl;
	JTabbedPane jtb;
	static String[] str;
	static int count=0;
	static JList<String> jl_online;
	public static void close(){
		System.exit(0);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 MyServerFrame mysf=new MyServerFrame();
	}
	public static void updateDiary(String str){
		jta_diary.append(str);
		jta_diary.setFont(new Font("宋体",Font.PLAIN,20));
		jta_diary.setCaretPosition(jta_diary.getDocument().getLength());
	}
	public static void updateOnline(){
		jl_online.setListData(ManageClientThread.getAllOnLineUserid().split(" "));
		LinkedList<String> temp=new LinkedList<>();
		for(Object o:new ConnectAccess().getUsers()){
			temp.add((String)o);
		}
		str=new String[temp.size()];
		for(int i=0;i<str.length;i++){
			str[i]=temp.get(i);
		}
		jl.setListData(str);
		jl.setFont(new Font("雅黑",Font.PLAIN,20));
		jl_online.setFont(new Font("雅黑",Font.PLAIN,20));
	}
	public MyServerFrame()
	{	
		
		jp_menu=new JPanel(null);
		jp1=new JPanel(new FlowLayout(FlowLayout.CENTER,160,10));
		jbT=new JButton("踢他下线");
		jbT.addActionListener(this);
		jp2=new JPanel(null);
//		CardLayout card=new CardLayout();
		jlb1=new JLabel("所有用户账号&密码:");
		jlb1.setFont(new Font("",Font.BOLD,18));
		jlb1.setBounds(50,20,200,30);
		jlb2=new JLabel("所有在线用户:");
		jlb2.setFont(new Font("",Font.BOLD,18));
		jlb2.setBounds(400,20,200,30);
		jb1=new JButton("启动服务器");
		jb2=new JButton("关闭服务器");
		//获取用户
		LinkedList<String> temp=new LinkedList<>();
		for(Object o:new ConnectAccess().getUsers()){
			temp.add((String)o);
		}
		str=new String[temp.size()];
		for(int i=0;i<str.length;i++){
			str[i]=temp.get(i);
		}
		jl=new JList<>(str);
		jl.setVisibleRowCount(10);
		jl.setFont(new Font("雅黑",Font.PLAIN,20));
		JScrollPane jsp=new JScrollPane(jl);
		jl_online=new JList<String>(ManageClientThread.getAllOnLineUserid().split(" "));
		jl_online.setVisibleRowCount(10);
		jl_online.setFont(new Font("雅黑",Font.PLAIN,20));
		JScrollPane jsp2=new JScrollPane(jl_online);
		jsp.setBounds(0,0,300,300);
		jsp2.setBounds(350,0,300,300);
		
		jp1.add(jb1);
		jp1.add(jb2);
		jb1.addActionListener(new SetUp());
		jb1.setFont(new Font("雅黑",Font.PLAIN,20));
		jb2.addActionListener(new BreakOut()); 
		jb2.setFont(new Font("雅黑",Font.PLAIN,20));
		jp2.add(jsp);
		jp2.add(jsp2);
		jp2.setBounds(50,60,800,500);
		jp2.setVisible(true);
		jlb1.setVisible(true);
		jbT.setVisible(true);
		jbT.setFont(new Font("",Font.BOLD,20));
		jbT.setForeground(Color.blue);
		jbT.addMouseListener(this);
		jbT.setBounds(560,20,140,30);
		
		//处理开始选项卡
		jp_menu.add(jlb1);
		jp_menu.add(jlb2);
		jp_menu.add(jp2);
		jp_menu.add(jbT);
				
		//处理日志选项卡
		jp_diary=new JPanel(null);
		jb0=new JButton("不要点我！");
		jb0.addActionListener(this);
		jb0.setFont(new Font("雅黑",Font.PLAIN,22));
		jb0.setBounds(250,350,220,30);
		jta_diary=new JTextArea();
		jta_diary.setVisible(true);
		jta_diary.setEditable(false);
		jsp=new JScrollPane(jta_diary);
		jsp.setBounds(50, 30, 640, 300);
		jp_diary.add(jsp);
		jp_diary.add(jb0);
		
		
		//添加选项卡

		jtb=new JTabbedPane();
		jtb.add("开始", jp_menu);
		jtb.add("系统日志", jp_diary);
		jtb.setFont(new Font("雅黑",Font.PLAIN,18));
		
		
		this.setLayout(new BorderLayout(10,10));
		this.add(jp1,"North");
		this.add(jtb,"Center");
		this.setSize(760, 600);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setTitle("服务器（MyServer）");
	}
	private class SetUp implements ActionListener{
		//调用线程
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			new Thread(new setup()).start();
		}
		private class setup implements Runnable{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				jb1.setEnabled(false);
				new MyQqServer();
			}
			
		}
	}

	private class BreakOut implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			System.exit(0);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==jb0){
			switch(count+1){
			case 1:JOptionPane.showMessageDialog(null, "别烦我……");break;
			case 2:JOptionPane.showMessageDialog(null, "#-_-说了别来烦我！");break;
			case 3:JOptionPane.showMessageDialog(null, "作业贼多啊……");break;
			case 4:JOptionPane.showMessageDialog(null, "感觉精力不太够");break;
			case 5:JOptionPane.showMessageDialog(null, "需要学的东西还有很多，不能懒！");break;
			case 6:JOptionPane.showMessageDialog(null, "你怎么还在点？很闲？");break;
			case 7:JOptionPane.showMessageDialog(null, "不过我可很忙的……");break;
			case 8:JOptionPane.showMessageDialog(null, "行了，不和你扯了。");break;
			case 9:JOptionPane.showMessageDialog(null, "你如果要继续点就点吧");break;
			case 10:JOptionPane.showMessageDialog(null, "我先撤了");break;
			case 11:JOptionPane.showMessageDialog(null, "留个复读机给你玩玩");break;
			case 12:JOptionPane.showMessageDialog(null, "(-_-)/~~");break;
			}
			count=(count+1)%12;
		}
		if(e.getSource()==jbT){
			if(jl_online.isSelectionEmpty()||jl_online.getSelectedValue().equals("")){
				JOptionPane.showMessageDialog(null, "请先选中一个在线用户！");
			}
			else{
				int choice=JOptionPane.showConfirmDialog(null, "是否确定将"+jl_online.getSelectedValue()+"踢下线？");
				//选择是
				if(choice==0){
					String online=jl_online.getSelectedValue();
					ObjectOutputStream oos;
					try {
						oos = new ObjectOutputStream(ManageClientThread.getClientThread(online).s.getOutputStream());
						Message m=new Message();
						m.setMesType(MessageType.message_getOut);
						oos.writeObject(m);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "操作成功！");
					SerConClientThread.notifyOther(online, 1);
					updateOnline();
				}
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource()==jbT){
			jbT.setForeground(Color.red);
		}
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource()==jbT){
			jbT.setForeground(Color.blue);
		}
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
