/**
 *	Author:		Kaleb|Sadalmalik (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
 *
 *	This code is not ready :D
**/

package ru.ifaculty.java.BasicEngine2D.inet;

import java.io.IOException;
import java.io.InputStream;

public class InputData extends InputStream	//	Поток из буфера
	{
	private int count;
	private	byte data[];
	
	public	InputData( byte[]data )
		{
		count=0;	this.data=data;
		}
	
	public	int read() throws IOException
		{
		if( data==null || count>=data.length )	{	return (-1) ;	}
		return( (data[count]>=0?0:256) + data[count++] );
		}
	public	int available()
		{
		if( data==null ) return (0);
		return( data.length-count );
		}
	public	void close()			{	data=null;	count=0;	}
	public	boolean markSupported()	{	return(false);	}
	}
