/**
 * 功能:qq客户端登录界面
 */
package com.qq.client.view;
import com.qq.common.*;
import com.qq.client.tools.*;
import java.io.*;
import javax.swing.*;

import com.qq.client.model.QqClientUser;
import com.qq.common.User;

import java.awt.*;
import java.awt.event.*;
public class QqClientLogin extends JFrame implements ActionListener{

	//定义北部需要的组件
	
	JLabel jbl1;
	
	//定义中部需要的组件
	//中部有三个JPanel,有一个叫选项卡窗口管理
	JTabbedPane jtp;
	JPanel jp2,jp3;
	JLabel jp2_jbl1,jp2_jbl2,jp2_jbl4,jp3_jbl1,jp3_jbl2;
	JButton jp2_jb1,jp2_jb3,jp3_jb1,jp3_jb2,jp3_jb3,jp3_jb4;
	JTextField jp2_jtf,jp3_jtf1,jp3_jtf2;
	JPasswordField jp2_jpf;
	JCheckBox jp2_jcb1,jp2_jcb2;
	
	
	//定义南部需要的组件
	JPanel jp1;
	JButton jp1_jb1;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QqClientLogin qqClientLogin=new QqClientLogin();
	}
	
	public QqClientLogin()
	{	
		//处理北部
		jbl1=new JLabel(new ImageIcon("image/tou.png"));
		
		//处理中部
		jp2=new JPanel(new GridLayout(3,3));
		jp3=new JPanel(new FlowLayout(FlowLayout.CENTER,60,3));
		jp2_jbl1=new JLabel("QQ账号：",JLabel.CENTER);
		jp2_jbl2=new JLabel("QQ密码：",JLabel.CENTER);
		
		jp2_jb1=new JButton("注册帮助");
		jp2_jb3=new JButton("忘记密码");
		jp2_jb3.setForeground(Color.blue);
		jp2_jtf=new JTextField();
		jp2_jpf=new JPasswordField();
		jp2_jcb1=new JCheckBox("隐身登录");
		jp2_jcb2=new JCheckBox("记住密码");
		jp2_jcb1.setFont(new Font("宋体",Font.BOLD,20));
		jp2_jcb2.setFont(new Font("宋体",Font.BOLD,20));
		jp2_jbl1.setFont(new Font("微软雅黑",Font.PLAIN,24));
		jp2_jb1.setFont(new Font("宋体",Font.PLAIN,20));
		jp2_jbl2.setFont(new Font("微软雅黑",Font.PLAIN,24));
		jp2_jb3.setFont(new Font("宋体",Font.BOLD,20));
		
		//把控件按照顺序加入到jp2
		jp2.add(jp2_jbl1);
		jp2.add(jp2_jtf);
		jp2.add(jp2_jb1);
		jp2.add(jp2_jbl2);
		jp2.add(jp2_jpf);
		jp2.add(jp2_jb3);
		jp2.add(jp2_jcb1);
		jp2.add(jp2_jcb2);
		//创建选项卡窗口
		jtp=new JTabbedPane();
		jtp.add("QQ登录",jp2);
		jtp.add("用户注册",jp3);
		
		//处理注册界面
		jp3_jbl1=new JLabel("注册账号：");
		jp3_jbl2=new JLabel("注册密码：");
		jp3_jtf1=new JTextField(20);
		jp3_jtf2=new JTextField(20);
		jp3_jb1=new JButton("检查账号");
		jp3_jb2=new JButton("注册账号");
		jp3_jbl1.setFont(new Font("微软雅黑",Font.PLAIN,22));
		jp3_jb1.setFont(new Font("宋体",Font.PLAIN,20));
		jp3_jbl2.setFont(new Font("微软雅黑",Font.PLAIN,22));
		jp3_jb2.setFont(new Font("宋体",Font.PLAIN,20));
		jp3.add(jp3_jbl1);
		jp3.add(jp3_jtf1);
		jp3.add(jp3_jbl2);
		jp3.add(jp3_jtf2);
		jp3.add(jp3_jb1);
		jp3.add(jp3_jb2);
		
		
		//处理南部
		jp1=new JPanel();
		jp1_jb1=new JButton(new ImageIcon("image/denglu.png"));
		//响应用户点击登录
		jp1_jb1.addActionListener(this);
		
		
		//把三个按钮放入到jp1
		jp1.add(jp1_jb1);
		//添加按键功能
		jp3_jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				checkActionPerformed(event);
			}});
		jp3_jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				registerActionPerformed(event);
			}});
		jp2_jb3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				forgetActionPerformed(event);
			}});
		jp2_jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				helpActionPerformed(event);
			}});
		
		this.add(jbl1,"North");
		this.add(jtp,"Center");
		//把jp1放在南部
		this.add(jp1,"South");
		this.setSize(650, 500);
		this.setDefaultCloseOperation(3);
		this.setVisible(true);
	}
	//如果点击检查账号
	public void checkActionPerformed(ActionEvent event) {
		QqClientUser qqClientUser=new QqClientUser();
		User u=new User();
		u.setType(3);//3表示检查账号是否重复
		u.setUserId(jp3_jtf1.getText().trim());
		u.setPasswd(jp3_jtf2.getText().trim());
		
		if(qqClientUser.checkId(u))
		{
			JOptionPane.showMessageDialog(this,"账号可以使用");
		}else{
			JOptionPane.showMessageDialog(this,"账号重复！请重新注册！");
		}
	}
	//如果点击注册账号
	public void registerActionPerformed(ActionEvent event) {
		QqClientUser qqClientUser=new QqClientUser();
		User u=new User();
		u.setType(2);//2表示注册
		u.setUserId(jp3_jtf1.getText().trim());
		u.setPasswd(jp3_jtf2.getText().trim());
		
		if(qqClientUser.checkRegister(u))
		{
			JOptionPane.showMessageDialog(this,"注册成功！请记住您的账号密码");
		}else{
			JOptionPane.showMessageDialog(this,"注册失败！");
		}
	}
	
	//如果用户点击忘记密码
	public void forgetActionPerformed(ActionEvent event) {
		JFrame jff=new JFrame();
		JLabel jlf=new JLabel();
		JLabel jl2f=new JLabel();
		jff.setDefaultCloseOperation(2);
		jff.setSize(400,340);
		jff.setLayout(null);
		jlf.setText("忘了就忘了吧，重新注册个。");
		jl2f.setText("忘记密码：");
		jl2f.setFont(new Font("黑体",Font.BOLD,22));
		jlf.setFont(new Font("宋体",Font.BOLD,18));
		jl2f.setBounds(40, 40, 300, 40);
		jlf.setBounds(50, 100, 300, 30);
		jff.add(jlf);
		jff.add(jl2f);
		jff.setVisible(true);
	}
	
	//如果用户点击注册帮助
	public void helpActionPerformed(ActionEvent event) {
		JFrame jf=new JFrame();
		JLabel jl=new JLabel();
		JLabel jl2=new JLabel();
		jf.setDefaultCloseOperation(2);
		jf.setSize(400,340);
		jf.setLayout(null);
		jl.setText("请按照登录页面上的注册卡填写。");
		jl2.setText("帮助：");
		jl2.setFont(new Font("黑体",Font.BOLD,22));
		jl.setFont(new Font("宋体",Font.BOLD,18));
		jl2.setBounds(40, 40, 100, 40);
		jl.setBounds(50, 100, 300, 30);
		jf.add(jl);
		jf.add(jl2);
		jf.setVisible(true);
	}
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//如果用户点击登录
		if(arg0.getSource()==jp1_jb1)
		{
			QqClientUser qqClientUser=new QqClientUser();
			User u=new User();
			u.setType(1);//1表示登录
			u.setUserId(jp2_jtf.getText().trim());
			u.setPasswd(new String(jp2_jpf.getPassword()));
			
			if(qqClientUser.checkUser(u))
			{
				try {
					//把创建好友列表的语句提前.
					QqFriendList qqList=new QqFriendList(u.getUserId());
					ManageQqFriendList.addQqFriendList(u.getUserId(), qqList);
					
					//发送一个要求返回在线好友的请求包.
					ObjectOutputStream oos=new ObjectOutputStream
					(ManageClientConServerThread.getClientConServerThread(u.getUserId()).getS().getOutputStream());
					
					//做一个Message
					Message m=new Message();
					m.setMesType(MessageType.message_get_onLineFriend);
					//指明我要的是这个qq号的好友情况.
					m.setSender(u.getUserId());
					oos.writeObject(m);
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
				
				
			
				//关闭掉登录界面
				this.dispose();
			}else{
				JOptionPane.showMessageDialog(this,"用户名密码错误");
			}
		}
		
		}
	}

