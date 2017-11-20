/**
 * 未完成！！
 * 这是客户端连接服务器的后台
 */
package com.qq.client.model;
import com.qq.client.tools.*;
import java.util.*;
import java.net.*;
import java.io.*;
import com.qq.common.*;
public class QqClientConServer {

	
	public  Socket s;
	
	//发送第一次请求
	public boolean sendLoginInfoToServer(Object o)
	{
		boolean b=false;
		try {
			System.out.println("登录中");
			s=new Socket("127.0.0.1",9999);
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(o);
			
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			
			Message ms=(Message)ois.readObject();
			//这里就是验证用户登录的地方
			if(ms.getMesType().equals("1"))
			{
				//就创建一个该qq号和服务器端保持通讯连接得线程
				ClientConServerThread ccst=new ClientConServerThread(s);
				//启动该通讯线程
				ccst.start();
				ManageClientConServerThread.addClientConServerThread
				(((User)o).getUserId(), ccst);
				b=true;
			}else{
				//关闭Scoket
				s.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return b;
	}
	
	public boolean sendRegisterInfoToServer(Object o)
	{
		boolean b=false;
		try {
			System.out.println("注册中");
			s=new Socket("127.0.0.1",9999);
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(o);
			
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			
			Message ms=(Message)ois.readObject();
			//这里就是验证用户登录的地方
			if(ms.getMesType().equals("6"))
			{
				
				b=true;
			}else{
				//关闭Scoket
				s.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return b;
	}
	public boolean sendIdInfoToServer(Object o)
	{
		boolean b=false;
		try {
			System.out.println("检查账号中");
			s=new Socket("127.0.0.1",9999);
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(o);
			
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			
			Message ms=(Message)ois.readObject();
			//这里就是验证用户登录的地方,7代表账号不重复
			if(ms.getMesType().equals("7"))
			{
				b=true;
			}else{
				//关闭Scoket
				s.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return b;
	}
}
