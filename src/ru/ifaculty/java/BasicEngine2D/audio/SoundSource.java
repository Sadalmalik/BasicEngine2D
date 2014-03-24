/**
 *	Author:		Kaleb|Sadalmalik (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
**/

package ru.ifaculty.java.BasicEngine2D.audio;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import ru.ifaculty.java.BasicEngine2D.BasicEngine2D;
import ru.ifaculty.java.BasicEngine2D.Source;

public class SoundSource extends Source
	{
	SoundSource next;
	
	public String getError()
		{
		switch( error )
			{
			case 200:	return"Unsupported Audio File!";
			case 201:	return"Audio stream Read error!";
			case 202:	return"Data reading error!";
			}
		return("Unknown error!");
		}
	public String getState()
		{
		return( "[ " + format.toString() + " ]" );
		}
	
	public byte[] sound = null;
	public AudioFormat format=null;
	public int FrameDivisor = 1;
	public int FrameSize;

	public SoundSource( String sound )				{	this( sound , 1 );	}
	public SoundSource( String sound , int div )	{	FileName = sound;	FrameDivisor = div;	}
	public void load()
		{
		if( ready )	return;
		
		InputStream INP = BasicEngine2D.getResource(FileName);	inner = BasicEngine2D.last;
		if( INP==null )	{	error=1;	wreck=true; return; }
		AudioInputStream AIS = null;
		try	{	AIS = AudioSystem.getAudioInputStream(INP);	}
		catch (UnsupportedAudioFileException e1){	error=200;	wreck=true;	return;	}
		catch (IOException e1)					{	error=201;	wreck=true;	return;	}
		if(	AIS == null	)	{	wreck=true;	return; }
		
		format = AIS.getFormat();
		
		int BytesRead = 0;
		FrameSize = (int)( format.getSampleRate() * format.getFrameSize() );
		sound = new byte[0];
		
		try	{
			sound = new byte[AIS.available()];
			BytesRead = AIS.read(sound, 0, sound.length);
			if( BytesRead!=sound.length )	throw new IOException("Incorrect size of audio data!");
			INP.close();	AIS.close();
			}
		catch (IOException e)
			{
			error=202;
			sound=null;
			wreck=true;
			return;
			}
		//	Всё, звук загружен, его можно использовать!
		FrameSize/=FrameDivisor;
		ready = true;
		}
	public	Sound	play()								{	if( !ready ){return(null);}	Sound S = new Sound( this );	S.play();	return(S);	}
	public	Sound	play( int l )						{	if( !ready ){return(null);}	Sound S = new Sound( this );	S.play(l);	return(S);	}
	public	Sound	play( float x , float y )			{	if( !ready ){return(null);}	Sound S = new Sound( this );	S.play(x,y);	return(S);	}
	public	Sound	play( float x , float y , int l )	{	if( !ready ){return(null);}	Sound S = new Sound( this );	S.play(x,y,l);	return(S);	}
	}
