/**
 *	Author:		Kaleb(Sadalmalik) (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
**/

package ru.ifaculty.java.BasicEngine2D.graphic;

public class SpriteSourceLoader
	{
	public static int available=0, loaded=0, wreck=0;
	public static SpriteSource head, midd, tail;

	public static int	queue()
		{	return (available-loaded-wreck);	}
	public static void add( SpriteSource s )
		{
		if( head==null ){	head=midd=tail=s;	}
		else{
			tail.next=s;		tail=s;
			if( midd==null )	midd=s;
			}
		available++;
		}
	public static SpriteSource getSpriteSource( String image )
		{	return( getSpriteSource( image , 0 , 0 ) );	}
	public static SpriteSource getSpriteSource( String image , int x , int y )
		{
		SpriteSource temp;
		if( head!=null )
			{
			boolean ign=false;
			if( x==0 || y==0 )
				{	ign=true;	}
			for( temp=head ; temp!=null ; temp=temp.next )
				{
				if( temp.FileName.equals(image) )
					{
					if( ( temp.scale & ign ) || ( temp.xSize==x & temp.ySize==y ) )	return( temp );
					}
				}
			}
		temp = new SpriteSource( image , x , y );
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
		return("Graphics data loaded "+loaded+" from "+available+" ( "+(((int)(10000*loaded/(double)available))/100.0)+"% ), wreck "+wreck+"( "+(((int)(10000*wreck/(double)available))/100.0)+"% )");
		}
	public static String getFullDump()
		{
		StringBuilder ret = new StringBuilder();
		SpriteSource temp;
		ret.append("Sprite Loader data:\n");
		if( head!=null )
			{
			for( temp=head ; temp!=null ; temp=temp.next )
				ret.append(temp.toString()).append('\n');
			}
		else{	ret.append("No data.");	}
		return( ret.toString() );
		}
	}
