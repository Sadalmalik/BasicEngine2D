/**
 *	Author:		Kaleb(Sadalmalik) (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
 *
 *	This code is not ready :D
**/

package ru.ifaculty.java.BasicEngine2D.inet;

import java.io.IOException;
import java.io.OutputStream;

public class OutputData extends OutputStream	//	Поток в буфер
	{
	private	boolean closed=false;
	
	public int count=0;
	public byte data[]=new byte[2];
	
	public void close(){	flush();	closed=true;	}
	public void flush()
		{
		if( count<data.length )
			{
			byte[]temp=new byte[count];
			for( int i=0 ; i<count ; i++ )
				{	temp[i]=data[i];	}
			data=temp;
			}
		}
	public void write(int b) throws IOException
		{
		if( closed )
			throw new IOException("Output stream already closed!");
		if( count>=data.length )
			{
			byte[]temp=new byte[data.length*2];
			for( int i=0 ; i<data.length ; i++ )
				{	temp[i]=data[i];	}
			data=temp;
			}
		data[count++]=(byte)(b-(b>127?256:0));
		}
	}
