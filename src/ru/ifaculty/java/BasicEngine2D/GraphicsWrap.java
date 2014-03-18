/**
 *	Author:		Kaleb|Sadalmalik (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
**/

package ru.ifaculty.java.BasicEngine2D;

import java.awt.Color;
import java.awt.Graphics2D;

import ru.ifaculty.java.BasicEngine2D.graphic.Sprite;
import ru.ifaculty.java.BasicEngine2D.graphic.SpriteSource;

public class GraphicsWrap
	{
	public	int	xHalf, yHalf;
	public	Graphics2D	graph;
	public	GraphicsWrap( Graphics2D g , int xh , int yh )
		{	graph=g;	xHalf=xh;	yHalf=yh;	}

	public	void	setColor( Color c )	{	graph.setColor( c );	}

	public	void	drawLine( int x1 , int y1 , int x2 , int y2 )		{	graph.drawLine( xHalf+x1 , yHalf-y1 , xHalf+x2 , yHalf-y2 );	}
	
	public	void	drawOval( int x , int y , int width , int height )	{	graph.drawOval( xHalf+x-width>>1 , yHalf-y-height>>1 , width , height );	}
	public	void	fillOval( int x , int y , int width , int height )	{	graph.fillOval( xHalf+x-width>>1 , yHalf-y-height>>1 , width , height );	}
	public	void	drawRect( int x , int y , int width , int height )	{	graph.drawRect( xHalf+x-width>>1 , yHalf-y-height>>1 , width , height );	}
	public	void	fillRect( int x , int y , int width , int height )	{	graph.fillRect( xHalf+x-width>>1 , yHalf-y-height>>1 , width , height );	}
	
	public	void	drawRoundRect( int x , int y , int width , int height , int arcWidth , int arcHeight )	{	graph.drawRoundRect( xHalf+x-width>>1 , yHalf-y-height>>1 , width , height , arcWidth , arcHeight );	}
	public	void	fillRoundRect( int x , int y , int width , int height , int arcWidth , int arcHeight )	{	graph.fillRoundRect( xHalf+x-width>>1 , yHalf-y-height>>1 , width , height , arcWidth , arcHeight );	}
	
	public	void	drawSprite( Sprite s )
		{	s.draw( this );	}
	public	void	drawSpriteSource( SpriteSource s , int frame , int x , int y )
		{	s.draw(graph,frame,x+xHalf,yHalf-y);	}

	public	void drawRawString( String s , int x , int y )			{	graph.drawString( s , x , y );	}
	public	void drawString( String s , int x , int y )				{	drawString( s , x , y , Color.WHITE );	}
	public	void drawString( String s , int x , int y , Color c  )
		{
		graph.setColor( Color.BLACK );
		graph.drawString( s ,xHalf+x-1,yHalf- y );
		graph.drawString( s ,xHalf+x+1,yHalf- y );
		graph.drawString( s ,xHalf+ x ,yHalf-y-1);
		graph.drawString( s ,xHalf+ x ,yHalf-y+1);
		graph.setColor( c );
		graph.drawString( s ,xHalf+ x ,yHalf- y );
		}
	}
