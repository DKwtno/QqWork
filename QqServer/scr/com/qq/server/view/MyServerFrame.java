/**
 * 这是服务器端的控制界面，可以完成启动服务器，关闭服务器
 * 可以管理和监控用户.
 */
package com.qq.server.view;


import javax.swing.*;

import com.qq.info.ConnectAccess;
import com.qq.server.model.ManageClientThread;
import com.qq.server.model.MyQqServer;
import com.qq.server.model.SerConClientThread;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
public class MyServerFrame extends JFrame  {

	
	JPanel jp1,jp2,jp_menu;
	JButton jb1,jb2;
	JLabel jlb1,jlb2,jlb3;
	static JList<String> jl;
	JTabbedPane jtb;
	static String[] str;
	static JList<String> jl_online;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 MyServerFrame mysf=new MyServerFrame();
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
		GridLayout gl=new GridLayout(1,2);
		gl.setHgap(160);
		jp_menu=new JPanel(null);
		jp1=new JPanel(gl);
		jp2=new JPanel(null);
//		CardLayout card=new CardLayout();
		jlb1=new JLabel("所有用户账号&密码:");
		jlb1.setBounds(50,20,150,30);
		jlb2=new JLabel("所有在线用户:");
		jlb2.setBounds(400,20,100,30);
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
		jb1.setFont(new Font("雅黑",Font.BOLD,31));
		jb2.addActionListener(new BreakOut()); 
		jb2.setFont(new Font("雅黑",Font.BOLD,31));
		jp2.add(jsp);
		jp2.add(jsp2);
		jp2.setBounds(50,60,800,500);
		jp2.setVisible(true);
		jlb1.setVisible(true);
		
		//处理开始选项卡
		jp_menu.add(jlb1);
		jp_menu.add(jlb2);
		jp_menu.add(jp2);
				
		//添加选项卡

		jtb=new JTabbedPane();
		jtb.add("开始", jp_menu);
		
		
		this.setLayout(new BorderLayout(10,10));
		this.add(jp1,"North");
		this.add(jtb,"Center");
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
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
	

}
