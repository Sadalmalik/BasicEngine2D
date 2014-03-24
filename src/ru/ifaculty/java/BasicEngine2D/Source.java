/**
 *	Author:		Kaleb(Sadalmalik) (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
**/

package ru.ifaculty.java.BasicEngine2D;

public abstract class Source
	{
	public String	FileName;
	public boolean	ready=false;
	public boolean	wreck=false;
	public boolean	inner=false;
	public int		error=0;

	public String toString()
		{
		String ret = "Resource '"+FileName+"'";
		if( wreck )
			{
			ret+=" wreck, error: ";
			switch( error )
				{
				case 0 :	ret+="no error... O.o ? Passible global engine error! Communicate with the developer.";	break;
				case 1 :	ret+="File not found!";	break;
				default:	ret+=getError();
				}
			}
		else if( ready )	{	ret+=" ready, "+((inner)?("inner file"):("outer file"))+", "+getState();	}
		else				{	ret+=" not ready, waiting load.";}
		return( ret );
		}
	public abstract String getError();
	public abstract String getState();
	}
