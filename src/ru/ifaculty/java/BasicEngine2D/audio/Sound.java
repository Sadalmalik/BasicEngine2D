/**
 *	Author:		Kaleb|Sadalmalik (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
**/

package ru.ifaculty.java.BasicEngine2D.audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Sound
	{
	private	static	int		scale=500;
	public	static	void	setScale2D(	int s	)	{	if(s>0){scale=s;}	}
	public	static	int		getScale2D(			)	{	return(scale);		}
	
	private	SoundSource source = null;
	public	void		setSource(	SoundSource S	)	{	if(S!=null)source=S;	}
	public	SoundSource	getSource(					)	{	return(source);			}

	private	Thread			thread	=	null	;
	private SourceDataLine	line	=	null	;
	private FloatControl	pan		=	null	;
	private FloatControl	gain	=	null	;
	
	public	void	dropBalance	(			)	{	if( pan != null )	pan.setValue(0);	}
	public	void	setBalance	( float val )	{	val=(val<-1)?(-1):((val>1)?(1):(val));	if( pan != null )	pan.setValue(val);	}
	public	void	setVolume	( float val )	{	val=(val< 0)?( 0):((val>1)?(1):(val));	if( gain!=null )	gain.setValue(interval(gain.getMinimum(),gain.getMaximum(),val));	}
	public	float	interval(float A, float B, float t)	{	return( (B-A)*t+A );	}
	public	void	position(float x, float y)	//	2D sound
		{
		setBalance	( x / ( Math.abs(x)+scale ) );									////	вроде ничего сложного? Рациональная сигмоида =) http://ru.wikipedia.org/wiki/Сигмоида
	//	setVolume	((float)( s / Math.sqrt( x*x + y*y + 5 ) ));					////	не катит - резкая граница затухания
		setVolume	((float)( 1 / Math.cosh(Math.sqrt( x*x + y*y )/(3*scale)) ));	////	гуглите солитоны! Это много лучше чем 1/(R^2) !
		}
	
	public	Sound( SoundSource source )		{	this.source = source;	}
	public	Sound( String name )			{	this.source = SoundSourceLoader.getSoundSource( name );			}
	public	Sound( String name , int div )	{	this.source = SoundSourceLoader.getSoundSource( name , div );	}
	
	private boolean destroy=false;
	private boolean ready=false;
	private boolean play=false;
	private void init()
		{
		if(!source.ready )	return;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, source.format);
		
		try	{
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(source.format);
			}
		catch (LineUnavailableException e)	{	e.printStackTrace();	return;	}
		catch (Exception e)					{	e.printStackTrace();	return;	}
		
		if (line.isControlSupported(FloatControl.Type.PAN))			{	pan =	(FloatControl) line.getControl(FloatControl.Type.PAN);			}
		if (line.isControlSupported(FloatControl.Type.MASTER_GAIN))	{	gain =	(FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);	}
		
		setVolume(1);
		setBalance(0);
		
		ready=true;
		}
	@SuppressWarnings("deprecation")
	public void destroy()
		{
		loop=0;	play=false;
		time=0;	ready=false;
		if( line != null )
			{
			line.drain();
			line.close();
			line=null;
			}
		if( thread!=null )
			{
			thread.stop();		//	!
			thread.destroy();	//	!!!
			}
		}
	private	int		loop=1;
	private	int		time=0;
	private	void	process()
		{
		if( source==null )	return;
		if(!source.ready )	return;
		if( !ready )		init();
		
		line.start();
		int	enable=0;
		while( !destroy && loop>0 && time<source.sound.length  )
			{
			if( Thread.interrupted() )		{		break;		}
			if( time>=source.sound.length )	{	loop--;	time=0;	}

			enable  =  source.sound.length - time ;
			if( enable>source.FrameSize ) enable=source.FrameSize;
			line.write(source.sound, time, enable);
			
			time+=enable;
			if( time>=source.sound.length )	{	loop--;	time=0;	}
			
			while(!play)	sleep(20);
			}
		play=false;
		sleep(1000);
		if( line!=null )	line.stop();
		destroy();
		}
	
	public	void	play()						{	play( 0 , 0 , 1 );	}
	public	void	play( int l )				{	play( 0 , 0 , l );	}
	public	void	play( float x , float y )	{	play( x , y , 1 );	}
	public	void	play( float x , float y , int l )
		{
		loop=(l>1)?(l):(1);
		if( !ready )init();	//	если здесь это не делать - position(x,y) не даст результата
		if( thread==null )
			{
			thread = new Thread	(	new Runnable()	{	public void run() {process();}	}	);
			thread.setName(source.toString());
			thread.start();
			}
		position(x,y);
		play=true;
		}
	public	void	pause()	{	play=false;	}
	public	void	stop()	{	play=false;	time=0;	}
	public	void	setTime(int t)	{	time = t*source.FrameSize;		}
	public	int		getTime()		{	return( time/source.FrameSize );	}
	public	int		getMaxTime()	{	return( source.sound.length/source.FrameSize );	}
	
	private void sleep(long t)
		{	try	{	Thread.sleep(t);	}	catch(InterruptedException e)	{e.printStackTrace();}	}
	}
