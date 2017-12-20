/**
 * 我的好友列表,(也包括我的群组，黑名单)
 */
package com.qq.client.view;

import com.qq.client.model.QqClientUser;
import com.qq.client.tools.*;
import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.common.User;

import javax.swing.*;
import javax.xml.bind.Marshaller.Listener;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
public class QqFriendList extends JFrame implements ActionListener,MouseListener{

	//处理第一张卡片.
	
	JPanel jphy1,jphy2,jphy3;
	JButton jphy_jb1,jphy_jb2,jphy_jb3;
	JScrollPane jsp1;
	String owner;
	//处理第二张卡片(我的群组).
	
	JPanel jpmsr1,jpmsr2,jpmsr3;
	JButton jpmsr_jb1,jpmsr_jb2,jpmsr_jb3;
	JScrollPane jsp2;
	JLabel []jb1s,jb1s2;
	//把整个JFrame设置成CardLayout
	CardLayout cl;
	
	JPopupMenu popUpMenu ;
	JMenu editMenu;
	public static String thisowner;
	public static boolean isManager=false;
	public static JLabel pointer=null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QqFriendList qqFriendList=new QqFriendList("Friend");
	}
	//更新群组状态
	public void updateGroup(LinkedList<String> link){
		for(String i:link)
		{
			jb1s2[Integer.parseInt(i)-1].setEnabled(true);
			//每一个更新过后都需要获得该群的群员名单
			try {
				ObjectOutputStream oos=new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(owner).getS().getOutputStream());
				Message m=new Message();
				m.setMesType(MessageType.message_ret_groupchatmember);
				m.setSender(owner);
				m.setCon(i);
				oos.writeObject(m);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	//更新在线的好友情况
	public void upateFriend(Message m)
	{
		String onLineFriend[]=m.getCon().split(" ");
		if(m.getMesType().equals(MessageType.message_exit)){
			for(int i=0;i<onLineFriend.length;i++)
			{
				jb1s[Integer.parseInt(onLineFriend[i])-1].setEnabled(false);
			}
		}
		else{
			for(int i=0;i<onLineFriend.length;i++)
			{
				jb1s[Integer.parseInt(onLineFriend[i])-1].setEnabled(true);
			}
		}
	}
	
	public QqFriendList(String ownerId)
	{	
		
		QqFriendList.thisowner=ownerId;
		//处理右键菜单
		popUpMenu =new JPopupMenu();
		editMenu= new JMenu("邀请添加到群组");
		for(int i=0;i<20;i++){
			JMenuItem jmi=new JMenuItem(String.valueOf(i+1));
			jmi.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JMenuItem temp=(JMenuItem)e.getSource();
					if(pointer==null){
						JOptionPane.showMessageDialog(null, "发生错误（未选定目标）！");
						return;
					}
					//添加群成员
					
					try {
						//检查是否是群主
						if(ManageQqGroupMember.getQqGroupMemebr(temp.getText().trim()).getFirst().getSender()
								.equals(ownerId)){
							isManager=true;
							//如果未注册
							QqClientUser qqClientUser=new QqClientUser();
							User u = new User();
							u.setType(3);// 2表示注册
							u.setUserId(pointer.getText().trim());
							if (qqClientUser.checkId(u)){
								JOptionPane.showMessageDialog(null, "该用户未注册！");
								return;
							}
							//如果已经在群内
							for(Message i:ManageQqGroupMember.getQqGroupMemebr(temp.getText().trim())){
								if(i.getSender().equals(pointer.getText().trim()))
								{
									JOptionPane.showMessageDialog(null, "已添加该成员！");
									return;
								}
							}
							//如果人数过多
							if(ManageQqGroupMember.getQqGroupMemebr(temp.getText().trim()).size()>=QqGroupChat.MAXIMUM_GROUP_MEMBER){
								JOptionPane.showMessageDialog(null, "该群人数过多！");
								return;
							}
							Message m=new Message();
							m.setCon(temp.getText().trim());
							m.setMesType(MessageType.message_add_groupchatmember);
							m.setSendTime(new Date().toString());
							m.setSender(pointer.getText().trim());
							pointer=null;
							ObjectOutputStream oos=new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(ownerId)
									.getS().getOutputStream());
							oos.writeObject(m);
							//添加后需要返回新的名单
							ObjectOutputStream oos2=new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(owner).getS().getOutputStream());
							Message ms=new Message();
							ms.setSender(owner);
							ms.setMesType(MessageType.message_ret_groupchatmember);
							ms.setCon(temp.getText().trim());
							oos2.writeObject(ms);
							//添加完成
							JOptionPane.showMessageDialog(null, "成功添加群成员！");
						}
						
						else{
							JOptionPane.showMessageDialog(null, "只有群主有权限进行此操作！");
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "发生错误（指向空）！");
					}
					
				}
			});
			editMenu.add(jmi);
		}
		popUpMenu.add(editMenu);
		popUpMenu.add(new JMenuItem("获取相关信息（并没有完成）"));
		popUpMenu.add(new JMenuItem("凑格子"));
		popUpMenu.add(new JMenuItem("凑格子"));
		popUpMenu.add(new JMenuItem("凑格子"));
		popUpMenu.add(new JMenuItem("凑格子"));
		this.setResizable(false);
		this.owner=ownerId.trim();
		//处理第一张卡片(显示好友列表)
		jphy_jb1=new JButton("我的好友");
		jphy_jb2=new JButton("我的群组");
		jphy_jb2.addActionListener(this);
		jphy_jb3=new JButton("黑名单");
		jphy1=new JPanel(new BorderLayout());
		//假定有50个好友
		jphy2=new JPanel();
		//给jphy2，初始化50好友.
		jb1s =new JLabel[50];
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					String str=owner;
					User u=new User();
					u.setType(10);//10表示退出
					u.setUserId(str);
					if(new QqClientUser().UserExit(u))
						System.out.println("更新好友列表完成");
					ClientConServerThread ccst=ManageClientConServerThread.getClientConServerThread(str.trim());
					ManageClientConServerThread.removeClientConServerThread(str.trim());
					ManageQqFriendList.removeQqFriendList(str.trim());
				} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
				}
				System.exit(0);
			}
			});
		for(int i=0;i<jb1s.length;i++)
		{
			jb1s[i]=new JLabel((i+1)+"",new ImageIcon(getClass().getResource("head.png")),JLabel.LEFT);
			jb1s[i].setEnabled(false);
			jb1s[i].setFont(new Font("雅黑",Font.PLAIN,20));
			if(jb1s[i].getText().equals(ownerId))
			{
				jb1s[i].setEnabled(true);
			}

			jb1s[i].add(popUpMenu);
			jb1s[i].addMouseListener(this);
			jphy2.add(jb1s[i]);
		}
		jphy2.setLayout(new BoxLayout(jphy2,BoxLayout.Y_AXIS ));
		jphy3=new JPanel(new GridLayout(2,1));
		//把两个按钮加入到jphy3
		jphy3.add(jphy_jb2);
		jphy3.add(jphy_jb3);
		
		
		jsp1=new JScrollPane(jphy2);
		
		//对jphy1,初始化
		jphy1.add(jphy_jb1,"North");
		jphy1.add(jsp1,"Center");
		jphy1.add(jphy3,"South");
		
		
		//处理第二张卡片
		
		
		jpmsr_jb1=new JButton("我的好友");
		jpmsr_jb1.addActionListener(this);
		jpmsr_jb2=new JButton("我的群组");
		jpmsr_jb3=new JButton("黑名单");
		jpmsr1=new JPanel(new BorderLayout());
		jpmsr2=new JPanel();
		
		
		//给jphy2，初始化20我的群组.
		jb1s2=new JLabel[20];
		
		for(int i=0;i<jb1s2.length;i++)
		{
			jb1s2[i]=new JLabel((i+1)+"",new ImageIcon(getClass().getResource("group.png")),JLabel.LEFT);
			jb1s2[i].setFont(new Font("雅黑",Font.PLAIN,20));
			jb1s2[i].setEnabled(false);
			jb1s2[i].addMouseListener(new groupChatAction());
			jpmsr2.add(jb1s2[i]);
		}
		jpmsr2.setLayout(new BoxLayout(jpmsr2,BoxLayout.Y_AXIS ));
		
		jpmsr3=new JPanel(new GridLayout(2,1));
		//把两个按钮加入到jphy3
		jpmsr3.add(jpmsr_jb1);
		jpmsr3.add(jpmsr_jb2);
		
		
		jsp2=new JScrollPane(jpmsr2);
		
		
		//对jphy1,初始化
		jpmsr1.add(jpmsr3,"North");
		jpmsr1.add(jsp2,"Center");
		jpmsr1.add(jpmsr_jb3,"South");
		
		
		cl=new CardLayout();
		this.setLayout(cl);
		this.add(jphy1,"1");
		this.add(jpmsr1,"2");
		//在窗口显示自己的编号.
		this.setTitle(ownerId);
		this.setSize(280, 660);
		this.setVisible(true);
		
		
	}
	class groupChatAction implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			//如果左键点击两次
			if(e.getClickCount()==2&&e.getButton()==MouseEvent.BUTTON1){

				JLabel temp=(JLabel)e.getSource();
				if(ManageQqGroupMember.getQqGroupMemebr(temp.getText().trim()).getFirst().getSender()
						.equals(owner)){
					isManager=true;
					}
				QqGroupChat qgct;
				//如果群已经被打开了，则显示在最前端
				if((qgct=ManageQqGroupChat.getQqGroupChat(temp.getText().trim()))!=null){
					qgct.setAlwaysOnTop(true);
					qgct.setAlwaysOnTop(false);
					return;
				}
				//如果该群组已经被申请，则跳出QqGroupChat，并添加到manage类
				if(temp.isEnabled()){
					LinkedList<Message> lm=ManageQqGroupMember.getQqGroupMemebr(temp.getText().trim());
					boolean flag=false;
					for(Message i:lm){
						if(i.getSender().equals(owner))
							flag=true;
					}
					if(!flag){
						JOptionPane.showMessageDialog(null, "你未在此群内！");
						return;
					}
					QqGroupChat qqGroupChat=new QqGroupChat(temp.getText(), owner);
					ManageQqGroupChat.addQqGroupChat(temp.getText().trim(), qqGroupChat);
					return;
				}
				
				Object[] options={"是的","我再想想"};
				int choice=JOptionPane.showOptionDialog(null, "是否要申请一个聊天组？", "申请QQ群", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				//申请群组
				if(choice==0){
					JLabel group=(JLabel)e.getSource();
					JOptionPane.showMessageDialog(null, "申请完毕！");
					for(JLabel jlb:jb1s2){
						if(e.getSource()==jlb){
							jlb.setEnabled(true);
						}
					}
					//接下来要给server传递相关信息，添加到群的database
					try {
						ObjectOutputStream oos=new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(owner).getS().getOutputStream());
						Message m=new Message();
						m.setSender(owner);
						m.setCon(group.getText().trim());
						Date currentTime = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String dateString = formatter.format(currentTime);
						m.setSendTime(dateString);
						m.setMesType(MessageType.message_add_groupchatmember);
						oos.writeObject(m);
						ObjectOutputStream oos2=new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(owner).getS().getOutputStream());
						Message ms=new Message();
						ms.setSender(owner);
						ms.setMesType(MessageType.message_ret_groupchatmember);
						ms.setCon(group.getText().trim());
						oos2.writeObject(ms);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			JLabel jl=(JLabel)e.getSource();
			jl.setFont(new Font("雅黑",Font.BOLD,20));
			jl.setForeground(Color.red);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			JLabel jl=(JLabel)e.getSource();
			jl.setFont(new Font("雅黑",Font.PLAIN,20));
			jl.setForeground(Color.black);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//如果点击了群组按钮，就显示第二张卡片
		if(arg0.getSource()==jphy_jb2)
		{
			cl.show(this.getContentPane(), "2");
		}else if(arg0.getSource()==jpmsr_jb1){
			cl.show(this.getContentPane(), "1");
		}
	}
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//右键显示菜单
		if(arg0.getButton() == MouseEvent.BUTTON1)//点击鼠标左键
            popUpMenu.setVisible(false);//弹出菜单消失
    	
		//响应用户双击的事件，并得到好友的编号.
		if(arg0.getClickCount()==2&&arg0.getButton()==MouseEvent.BUTTON1)
		{	
			//得到该好友的编号
			String friendNo=((JLabel)arg0.getSource()).getText().trim();
			//说明已经打开了，让窗口出现在最前端
			QqChat qct;
			if((qct=ManageQqChat.getQqChat(this.owner+" "+friendNo))!=null){
				qct.setAlwaysOnTop(true);
				qct.setAlwaysOnTop(false);
				return;
			}
			//System.out.println("你希望和 "+friendNo+" 聊天");
			QqChat qqChat=new QqChat(this.owner,friendNo);
			qqChat.isOpen=true;
			//把聊天界面加入到管理类
			ManageQqChat.addQqChat(this.owner+" "+friendNo,qqChat);
			
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLabel jl=(JLabel)arg0.getSource();
		jl.setFont(new Font("雅黑",Font.BOLD,20));
		jl.setForeground(Color.red);
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLabel jl=(JLabel)arg0.getSource();
		jl.setFont(new Font("雅黑",Font.PLAIN,20));
		jl.setForeground(Color.black);
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		 if(arg0.getButton() == MouseEvent.BUTTON3)//点击右键
		{ 
			popUpMenu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
			pointer=(JLabel)arg0.getSource();
		}
	}

}
