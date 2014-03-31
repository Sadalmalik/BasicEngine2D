package ru.ifaculty.java.BasicEngine2D;
import java.awt.Color;
import java.awt.Graphics2D;

import ru.ifaculty.java.BasicEngine2D.graphic.Sprite;
import ru.ifaculty.java.BasicEngine2D.graphic.SpriteSource;

public class GraphicsWrap
	{
	public	int	xHalf, yHalf;
	public	Graphics2D	context;
	public	GraphicsWrap( Graphics2D g , int xh , int yh )
		{	context=g;	xHalf=xh;	yHalf=yh;	}

	public	void	setColor( Color c )	{	context.setColor( c );	}

	public	void	drawLine( int x1 , int y1 , int x2 , int y2 )		{	context.drawLine( xHalf+x1 , yHalf-y1 , xHalf+x2 , yHalf-y2 );	}

	public	void	drawOval( int x , int y , int width , int height )	{	context.drawOval( xHalf+x-width>>1 , yHalf-y-height>>1 , width , height );	}
	public	void	fillOval( int x , int y , int width , int height )	{	context.fillOval( xHalf+x-width>>1 , yHalf-y-height>>1 , width , height );	}
	public	void	drawRect( int x , int y , int width , int height )	{	context.drawRect( xHalf+x-width>>1 , yHalf-y-height>>1 , width , height );	}
	public	void	fillRect( int x , int y , int width , int height )	{	context.fillRect( xHalf+x-width>>1 , yHalf-y-height>>1 , width , height );	}

	public	void	drawRoundRect( int x , int y , int width , int height , int arcWidth , int arcHeight )	{	context.drawRoundRect( xHalf+x-width>>1 , yHalf-y-height>>1 , width , height , arcWidth , arcHeight );	}
	public	void	fillRoundRect( int x , int y , int width , int height , int arcWidth , int arcHeight )	{	context.fillRoundRect( xHalf+x-width>>1 , yHalf-y-height>>1 , width , height , arcWidth , arcHeight );	}

	public	void	drawSprite( Sprite s )
		{	s.draw( this );	}
	public	void	drawSpriteSource( SpriteSource s , int frame , int x , int y )
		{	s.draw(context,frame,x+xHalf,yHalf-y);	}

	public	void drawRawString( String s , int x , int y )			{	context.drawString( s , x , y );	}
	public	void drawString( String s , int x , int y )				{	drawString( s , x , y , Color.WHITE );	}
	public	void drawString( String s , int x , int y , Color c  )
		{
		context.setColor( Color.BLACK );
		context.drawString( s ,xHalf+x-1,yHalf- y );
		context.drawString( s ,xHalf+x+1,yHalf- y );
		context.drawString( s ,xHalf+ x ,yHalf-y-1);
		context.drawString( s ,xHalf+ x ,yHalf-y+1);
		context.setColor( c );
		context.drawString( s ,xHalf+ x ,yHalf- y );
		}
	}
