package com.qq.info;
/**
 * 主要用来连接数据库，并且能进行注册、登录验证、忘记密码、清除账号操作。
 * 
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

import com.qq.common.Message;

public class ConnectAccess {
	private  String id;
	private  String pw;
	private String mail;
	public static void main(String[] args) {
		//createGroup("1");
	}
	//群组未读消息还未完成，需要继续！
	//想法是直接存储到unread_message里去，getter设置成接受未读的人，sender设置成group名
	//难点在于怎么把文本里的g_给去除掉直接阅读
//	public static boolean storeUnreadGroupMessage(String groupTitle,Message m){
//		try {
//			Class.forName("com.hxtt.sql.access.AccessDriver");     
//			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
//			PreparedStatement pstm = con.prepareStatement("insert into unread_message values(?,?,?,?,?)");
//			pstm.setString(1, m.getSender());
//			pstm.setString(2, m.getGetter());
//			pstm.setString(3, m.getSendTime());
//			pstm.setString(4, m.getCon());
//			pstm.setString(5,"false");
//			
//			pstm.execute();
//				
//	        System.out.println("已添加群组未读信息");
//	        con.close();
//		}
//		catch(Exception e) {
//	           // TODOAuto-generated catch block
//	            e.printStackTrace();
//	            return false;
//	        }
//		return true;
//	}
	
	public static LinkedList<Message> getGroupHistory(String groupTitle){
		LinkedList<Message> msLink=new LinkedList<Message>();
		Message ms;
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("select * from "+"mess_g_"+groupTitle);
			ResultSet rs=pstm.executeQuery();
			while(rs.next()){
				ms=new Message();
				ms.setCon(rs.getString("Content"));
				ms.setGetter(rs.getString("Getter"));
				ms.setSender(rs.getString("Sender"));
				ms.setSendTime(rs.getString("Time"));
				if(rs.getString("isRead").equals("true"))
					ms.setRead(true);
//				else
//					ms.setRead(false);
				msLink.add(ms);
			}
	        System.out.println("已返回聊天记录");
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	        }
		return msLink;
	}
	public static void storeGroupHistory(String groupTitle,Message m){
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			ResultSet rs=con.getMetaData().getTables(null, null, "mess_g_"+groupTitle, null);
//			
//			if(rs==null){
				
//			}
			PreparedStatement pstm = con.prepareStatement("insert into "+"mess_g_"+groupTitle+" values(?,?,?,?,?)");
			pstm.setString(1, m.getSender());
			pstm.setString(2, m.getGetter());
			pstm.setString(3, m.getSendTime());
			pstm.setString(4, m.getCon());
			String str;
			if(m.isRead()){
				str="true";
			}
			else{
				str="false";
			}
			pstm.setString(5,str);
			
			pstm.execute();
				
	        System.out.println("已添加群组聊天记录");
	        con.close();
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	        }
		
		
	}
//------------------------------------------------
	public static void removeGroupMember(String groupTitle,Message m){
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			ResultSet rs=con.getMetaData().getTables(null, null, "g_"+groupTitle, null);
			PreparedStatement pstm = con.prepareStatement("delete from "+"g_"+groupTitle+" where Sender=?");
			pstm.setString(1, m.getSender());
			pstm.executeUpdate();
				
	        System.out.println("已删除群成员记录");
	        con.close();
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	        }
	}
//------------------------------------------------
	public static boolean addGroupMember(String groupTitle,Message m){
		boolean b=false;
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			ResultSet rs=con.getMetaData().getTables(null, null, "g_"+groupTitle, null);
			PreparedStatement pstm = con.prepareStatement("insert into "+"g_"+groupTitle+" values(?,?,?,?,?)");
			pstm.setString(1, m.getSender());
			pstm.setString(2, m.getGetter());
			pstm.setString(3, m.getSendTime());
			pstm.setString(4, m.getCon());
			String str;
			if(m.isRead()){
				str="true";
			}
			else{
				str="false";
			}
			pstm.setString(5,str);
			
			pstm.execute();
				
	        System.out.println("已添加群成员记录");
	        con.close();
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	            createGroup(groupTitle);
	            addGroupMember(groupTitle,m);
	        }
		return b;
	}
	
	public static void createGroup(String groupTitle){
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			ResultSet rs=con.getMetaData().getTables(null, null, groupTitle, null);
			System.out.println("创群表格");
			con.createStatement().execute("Create Table "+"g_"+groupTitle+" (Sender varchar(80),Getter varchar(80),Time varchar(80),Content varchar(80),isRead varchar(20))");
			rs=con.getMetaData().getTables(null, null, groupTitle, null);
			System.out.println("创好了");
			con.createStatement().execute("Create Table "+"mess_g_"+groupTitle+" (Sender varchar(80),Getter varchar(80),Time varchar(80),Content varchar(80),isRead varchar(20))");
			System.out.println("再创一个");
			PreparedStatement pstm=con.prepareStatement("Insert into group values(?)");
			pstm.setString(1, groupTitle);
			pstm.executeUpdate();
			System.out.println("已添加群记录");
			con.close();
		}
		catch(Exception e1){
			e1.printStackTrace();
		}
	}
//------------------------------------------------
	public static LinkedList<String> getGroupName(){
		LinkedList<String> strLink=new LinkedList<String>();
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("select * from group");
			ResultSet rs=pstm.executeQuery();
			while(rs.next()){
				strLink.add(rs.getString("groupName"));
			}
	        System.out.println("已返回所有群");
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	        }
		return strLink;
		
	}
//------------------------------------------------
	public static LinkedList<Message> getGroupMember(String groupTitle){
		LinkedList<Message> msLink=new LinkedList<Message>();
		Message ms;
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("select * from "+"g_"+groupTitle);
			ResultSet rs=pstm.executeQuery();
			while(rs.next()){
				ms=new Message();
				ms.setCon(rs.getString("Content"));
				ms.setGetter(rs.getString("Getter"));
				ms.setSender(rs.getString("Sender"));
				ms.setSendTime(rs.getString("Time"));
				msLink.add(ms);
			}
	        System.out.println("已返回群成员名单");
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	        }
		return msLink;
	}
//------------------------------------------------
	public static LinkedList<Message> getUnreadMessage(String id){
		LinkedList<Message> msLink=new LinkedList<Message>();
		Message ms;
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("select * from unread_message");
			ResultSet rs=pstm.executeQuery();
			while(rs.next()){
				ms=new Message();
				ms.setCon(rs.getString("Content"));
				ms.setGetter(rs.getString("Getter"));
				ms.setSender(rs.getString("Sender"));
				ms.setSendTime(rs.getString("Time"));
				if(rs.getString("isRead").equals("false"))
				{
					if(ms.getGetter().equals(id))
						msLink.add(ms);
					
				}
				
			}
			//把为false的都改为true
			PreparedStatement stm=con.prepareStatement("update unread_message set isRead=? where Getter=?");
			stm.setString(1, "true");
			stm.setString(2, id);
			stm.executeUpdate();
	        System.out.println("已返回未读信息");
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	        }
		return msLink;
	}
	public static LinkedList<Message> getHistory(String tableName){
		LinkedList<Message> msLink=new LinkedList<Message>();
		Message ms;
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("select * from "+tableName);
			ResultSet rs=pstm.executeQuery();
			while(rs.next()){
				ms=new Message();
				ms.setCon(rs.getString("Content"));
				ms.setGetter(rs.getString("Getter"));
				ms.setSender(rs.getString("Sender"));
				ms.setSendTime(rs.getString("Time"));
				if(rs.getString("isRead").equals("true"))
					ms.setRead(true);
//				else
//					ms.setRead(false);
				msLink.add(ms);
			}
	        System.out.println("已返回聊天记录");
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	        }
		return msLink;
	}
//	public static void createTable(String tid){
//
//		Connection con=null;
//		try {
//			Class.forName("com.hxtt.sql.access.AccessDriver");
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}     
//		try {
//			con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			String a="test";
//			String sql="creat table test(id TEXT(200), name TEXT(200)";
//			String sll="create table "+a+"(id int, name varchar(80))";
//			Statement stat = con.createStatement();  
//			stat.executeUpdate(sll);  
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	public static void createTable(String tId){
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			ResultSet rs=con.getMetaData().getTables(null, null, tId, null);
			System.out.println("创表格");
			con.createStatement().execute("Create Table "+tId+" (Sender varchar(80),Getter varchar(80),Time varchar(80),Content varchar(80),isRead varchar(20))");
			rs=con.getMetaData().getTables(null, null, tId, null);
			System.out.println("创好了");}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean storeUnreadMessage(Message m){
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("insert into unread_message values(?,?,?,?,?)");
			pstm.setString(1, m.getSender());
			pstm.setString(2, m.getGetter());
			pstm.setString(3, m.getSendTime());
			pstm.setString(4, m.getCon());
			pstm.setString(5,"false");
			
			pstm.execute();
				
	        System.out.println("已添加未读信息");
	        con.close();
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	            return false;
	        }
		return true;
	}
//------------------------------------------------
	public static void storeHistory(String tId,Message m){
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			ResultSet rs=con.getMetaData().getTables(null, null, tId, null);
//			
//			if(rs==null){
				
//			}
			PreparedStatement pstm = con.prepareStatement("insert into "+tId+" values(?,?,?,?,?)");
			pstm.setString(1, m.getSender());
			pstm.setString(2, m.getGetter());
			pstm.setString(3, m.getSendTime());
			pstm.setString(4, m.getCon());
			String str;
			if(m.isRead()){
				str="true";
			}
			else{
				str="false";
			}
			pstm.setString(5,str);
			
			pstm.execute();
				
	        System.out.println("已添加聊天记录");
	        con.close();
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	            createTable(tId);
	            storeHistory(tId,m);
	        }
	}
//------------------------------------------------
	//清除账号，要求原账号密码
	public void Delete(String userId,String passwd) throws Exception{
		this.id=userId;
		this.pw=passwd;
		//如果账号密码正确，则进行删除账号操作
		if(LogIn(id,pw)){
			try {
				Class.forName("com.hxtt.sql.access.AccessDriver");     
				Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
				PreparedStatement pstm = con.prepareStatement("delete from qq_user_data where Account=?");
				pstm.setString(1, id);
		        pstm.executeUpdate();
		        System.out.println("已删除该账号的相关记录");
			}
			catch(Exception e) {
		           // TODOAuto-generated catch block
		            e.printStackTrace();
		        }
		}
	}
//------------------------------------------------
	//检查账户是否重复
	public boolean idCheck(String userId){
		this.id=userId;
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///C:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("select * from qq_user_data");
	        ResultSet rs =pstm.executeQuery();
	        while(rs.next()) {
	            if(rs.getString("Account").equals(id))
	            	return true;//账号重复了
	        }
	        return false;
	        
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	            return false;
	        }
	}
//------------------------------------------------
	//忘记密码输入邮箱检查功能
	public String pwForgot(String userId,String mail){
		this.mail=mail;
		this.id=userId;
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("select * from qq_user_data");
	        ResultSet rs =pstm.executeQuery();
	        while(rs.next()) {
	            if(rs.getString("Account").equals(id))
	            	if(rs.getString("Mail").equals(mail))
	            		return rs.getString("PW");
	        }
	        return "输入邮箱错误";	        
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	        }
		return "输入邮箱错误";
	}
//------------------------------------------------
	//密码修改功能
	public boolean pwModifier(String userId,String passwd){
		this.id=userId;
		this.pw=passwd;
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("update qq_user_data set PW = ? where Account = ?");
			pstm.setString(1, pw);
			pstm.setString(2, id);
	        pstm.executeUpdate();
	        return true;
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	        }
		return false;
	}
//------------------------------------------------
	//注册功能（要求能检查注册的账号是否重复，密码是否六位以上）
	public boolean Register(String userId,String passwd){
		this.id=userId;
		this.pw=passwd;
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("insert into qq_user_data(PW,Account) values(?,?)");
			pstm.setString(1, pw);
			pstm.setString(2, id);
	        pstm.executeUpdate();
	        return true;
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	        }
		return false;
	}
//------------------------------------------------
	//获得所有的用户
	public Object[] getUsers(){
		ArrayList<String> str=new ArrayList<String>();
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("select * from qq_user_data");
			ResultSet rs =pstm.executeQuery();
			while(rs.next()) {
				str.add("ID: "+rs.getString("Account")+" ------------- PW: "+rs.getString("PW"));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.toArray();
	}
//------------------------------------------------
	//登录功能
	public boolean LogIn(String userId,String passwd) throws Exception {  
		this.id=userId;
		this.pw=passwd;
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("加载驱动失败");
			e1.printStackTrace();
		}    
		try { 
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("select * from qq_user_data");
	        ResultSet rs =pstm.executeQuery();
	        while(rs.next()) {
	            if(rs.getString("Account").equals(id))
	            	if(rs.getString("PW").equals(pw))
	            		return true;
	            	else
	            		return false;
	        }
	        return false;
	        
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
			System.out.println("连接数据库失败");
	            e.printStackTrace();
	            return false;
	        }
	}
}  
