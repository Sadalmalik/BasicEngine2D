/**
 *	Author:		Kaleb|Sadalmalik (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
**/

package ru.ifaculty.java.BasicEngine2D;

public abstract class Event
	{
	public	Event	next=null;
	
	private	long	nextTime=0;
	public	long	delay=0;
	public	boolean	active=true;
	public	String	info=null;

	public	Event(long delay)				{	this.delay=delay;	}
	public	Event(long delay,String info)	{	this.delay=delay;	this.info=info;	}
	
	public	void	setDelay(long d)		{	delay=d;				}
	public	void	setFreqency(float f)	{	delay=(long)(1000/f);	}
	public	void	start()					{	active=true;		}
	public	void	stop()					{	active=false;		}
	public	long	nextTime()				{	return(nextTime);	}
	
	public	void test()
		{
		if( !active )	return;
		long time = System.currentTimeMillis();
		if( time>nextTime )
			{	nextTime=time+delay;	exec();	}
		}

	public	abstract void exec();	//	Увы, но только замыкания или сверхглобальные переменные могут сюда попасть.
									//	Недавно в Java были введены lambda-expression-ы, но тут их некуда пихать:
									//	всё равно понадобятся все переменные указанные выше.
									//	Так что лямбды идут лесом!
	
	public	String	toString()
		{
		String ev = "[ event , delay "+delay+" , active "+active+" ";
		if( info!=null && info.length()>0 )	return(ev+", "+info+" ");
		return(ev+"]");
		}
	}
