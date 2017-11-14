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
	JPanel jp2,jp3,jp4;
	JLabel jp2_jbl1,jp2_jbl2,jp2_jbl4,jp3_jbl1,jp4_jbl1;
	JButton jp2_jb1,jp2_jbl3;
	JTextField jp2_jtf;
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
		jp3=new JPanel(new GridLayout(3,3));
		jp4=new JPanel(new GridLayout(3,3));
		jp2_jbl1=new JLabel("QQ账号",JLabel.CENTER);
		jp2_jbl2=new JLabel("QQ密码",JLabel.CENTER);
		
		jp2_jb1=new JButton("注册帮助");
		jp2_jbl3=new JButton("忘记密码");
		jp2_jbl3.setForeground(Color.blue);
		jp2_jtf=new JTextField();
		jp2_jpf=new JPasswordField();
		jp2_jcb1=new JCheckBox("隐身登录");
		jp2_jcb2=new JCheckBox("记住密码");
		jp2_jcb1.setFont(new Font("宋体",Font.BOLD,20));
		jp2_jcb2.setFont(new Font("宋体",Font.BOLD,20));
		jp2_jbl1.setFont(new Font("微软雅黑",Font.PLAIN,26));
		jp2_jb1.setFont(new Font("宋体",Font.PLAIN,20));
		jp2_jbl2.setFont(new Font("微软雅黑",Font.PLAIN,26));
		jp2_jbl3.setFont(new Font("宋体",Font.BOLD,20));
		
		//把控件按照顺序加入到jp2
		jp2.add(jp2_jbl1);
		jp2.add(jp2_jtf);
		jp2.add(jp2_jb1);
		jp2.add(jp2_jbl2);
		jp2.add(jp2_jpf);
		jp2.add(jp2_jbl3);
		jp2.add(jp2_jcb1);
		jp2.add(jp2_jcb2);

		
		//创建选项卡窗口
		jtp=new JTabbedPane();
		jtp.add("QQ登录",jp2);
		jtp.add("用户注册",jp3);

		
		//处理南部
		jp1=new JPanel();
		jp1_jb1=new JButton(new ImageIcon("image/denglu.png"));
		//响应用户点击登录
		jp1_jb1.addActionListener(this);
		
		
		//把三个按钮放入到jp1
		jp1.add(jp1_jb1);
		
		
		this.add(jbl1,"North");
		this.add(jtp,"Center");
		//..把jp1放在南部
		this.add(jp1,"South");
		this.setSize(650, 500);
		this.setDefaultCloseOperation(3);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//如果用户点击登录
		if(arg0.getSource()==jp1_jb1)
		{
			QqClientUser qqClientUser=new QqClientUser();
			User u=new User();
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
		//如果用户点击注册
		if(arg0.getSource()==jp2_jb1)
		{
		}
	}

}
