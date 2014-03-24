/**
 *	Author:		Kaleb|Sadalmalik (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
**/

package ru.ifaculty.java.BasicEngine2D.audio;

public class SoundSourceLoader
	{
	public static int available=0, loaded=0, wreck=0;
	public static SoundSource head, midd, tail;
	
	//	Это комментарий к коду.
	
	public static int	queue()
		{	return (available-loaded-wreck);	}
	public static void	add( SoundSource s )
		{
		if( head==null ){	head=midd=tail=s;	}
		else{
			tail.next=s;	tail=s;
			if( midd==null )midd=s;
			}
		available++;
		}
	public static SoundSource getSoundSource( String sound )
		{	return getSoundSource( sound , 1 );	}
	public static SoundSource getSoundSource( String sound , int div )
		{
		SoundSource temp;
		if( head!=null )
			{
			for( temp=head ; temp!=null ; temp=temp.next )
				if( temp.FrameDivisor==div & temp.FileName.equals(sound) )
					return( temp );
			}
		temp = new SoundSource( sound , div );
		add( temp );
		return( temp );
		}
	public static void loadingStep()
		{
		if( midd!=null )
			{
			midd.load();
			if( midd.wreck )wreck++;
			else			loaded++;
			midd=midd.next;
			}
		}
	public static String getState()
		{
		return("Sound data loaded "+loaded+" from "+available+" ( "+(((int)(10000*loaded/(double)available))/100.0)+"% ), wreck "+wreck+"( "+(((int)(10000*wreck/(double)available))/100.0)+"% )");
		}
	public static String getFullDump()
		{
		StringBuilder ret = new StringBuilder();
		SoundSource temp;
		ret.append("Sound Loader data:\n");
		if( head!=null )
			{
			for( temp=head ; temp!=null ; temp=temp.next )
				ret.append(temp.toString()).append('\n');
			}
		else{	ret.append("No data.");	}
		return( ret.toString() );
		}
	}
