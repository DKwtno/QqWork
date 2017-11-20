package com.qq.client.model;

import com.qq.common.*;
public class QqClientUser {

	public boolean checkUser(User u)
	{
		return new QqClientConServer().sendLoginInfoToServer(u);
	}
	public boolean checkRegister(User u)
	{
		return new QqClientConServer().sendRegisterInfoToServer(u);
	}
	public boolean checkId(User u)
	{
		return new QqClientConServer().sendIdInfoToServer(u);
	}
}
