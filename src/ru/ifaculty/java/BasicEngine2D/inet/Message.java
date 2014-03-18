/**
 *	Author:		Kaleb|Sadalmalik (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
 *
 *	This code is not ready :D
**/

package ru.ifaculty.java.BasicEngine2D.inet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Message
	{
	public static final int stage_0_notused		=	0	;
	public static final int stage_1_encoding	=	1	;
	public static final int stage_2_sending		=	2	;
	public static final int stage_3_receiving	=	3	;
	public static final int stage_4_decoding	=	4	;
	
	private	int stage	=stage_0_notused;
	private	int MID		=	-1	;
	private	int FROM	=	-1	;	//	User id
	private	int TO		=	-1	;	//	User id
	private	int LEN		=	-1	;
	private	byte[]data;
	private	OutputData	O	;
	private	InputData	I	;
	
	public Message()
		{
		}
	
	public	OutputStream getOutputStream() throws IOException
		{
		if( stage!=stage_0_notused )
			throw new IOException("Already in use!");
		stage = stage_1_encoding;
		O = new OutputData();
		return(O);
		}
	
	public	InputStream getInputStream() throws IOException
		{
		if( stage!=stage_3_receiving )
			throw new IOException("Data not received!");
		stage = stage_4_decoding;
		I = new InputData(data);
		return(I);
		}
	
	public void save( DataOutputStream DOUT ) throws IOException
		{
		
		}
	}
