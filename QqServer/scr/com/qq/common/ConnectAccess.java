package com.qq.common;
/**
 * 主要用来连接数据库，并且能进行注册、登录验证、忘记密码、清除账号操作。
 * 
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectAccess {
	private  String id;
	private  String pw;
	private String mail;
//------------------------------------------------
	//清除账号，要求原账号密码
	public void Clear(String userId,String passwd) throws Exception{
		this.id=userId;
		this.pw=passwd;
		//如果账号密码正确，则进行删除账号操作
		if(LogIn(id,pw)){
			
		}
	}
//------------------------------------------------
	//忘记密码输入邮箱检查功能
	public void PwForgot(String mail){
		this.mail=mail;
	}
//------------------------------------------------
	//密码修改功能
	public void PwModifier(){
		
	}
//------------------------------------------------
	//注册功能（要求能检查注册的账号是否重复，密码是否六位以上）
	public void Register(String userId,String passwd){
		this.id=userId;
		this.pw=passwd;
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java课程/Qq_exercise/Database.mdb");
			PreparedStatement pstm = con.prepareStatement("insert into qq_user_data(PW,Account) values(?,?)");
			pstm.setString(1, pw);
			pstm.setString(2, id);
	        pstm.executeUpdate();
		}
		catch(Exception e) {
	           // TODOAuto-generated catch block
	            e.printStackTrace();
	        }
	}
//------------------------------------------------
	//登录功能
	public boolean LogIn(String userId,String passwd) throws Exception {  
		this.id=userId;
		this.pw=passwd;
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");     
			Connection con=DriverManager.getConnection("jdbc:Access:///c:/Users/weizhiwei/Desktop/java课程/Qq_exercise/Database.mdb");
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
	            e.printStackTrace();
	            return false;
	        }
	}
}  
