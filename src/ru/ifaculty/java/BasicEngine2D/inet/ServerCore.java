/**
 *	Author:		Kaleb|Sadalmalik (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
 *
 *	This code is not ready :D
**/

package ru.ifaculty.java.BasicEngine2D.inet;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerCore
	{
	public static Thread T;
	public static void start()
		{
		T = new Thread( new Runnable(){public void run(){work();}} );
		}
	public static boolean work;
	public static ServerSocket serv;
	public static void work()
		{
		work=true;
		
		try {
			serv = new ServerSocket(8080);
			while( work )
				{
			//	serv.
				}
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}
		}
	}
