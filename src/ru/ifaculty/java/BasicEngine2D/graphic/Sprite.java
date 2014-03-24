/**
 *	Author:		Kaleb|Sadalmalik (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
**/

package ru.ifaculty.java.BasicEngine2D.graphic;

import java.util.ArrayList;
import java.util.List;

import ru.ifaculty.java.BasicEngine2D.GraphicsWrap;

public class Sprite
	{
	//****************************************************************************************************************************************//
	public	static	List<Sprite>sprites = new ArrayList<Sprite>();
	public	static	void	stepAll()	{	for( Sprite s : sprites )	{	s.step();	}	}
	//****************************************************************************************************************************************//
	
	

	//****************************************************************************************************************************************//
	private	boolean run=true;
	private	boolean ready=false;
	public	boolean fixed=false;
	private	SpriteSource source = null;
	private	int		frame=0, size;
	public	float	x, y;
	public	int		xHalf=-1, yHalf=-1;
	public	int	size(){	return(size);	}

	public	Sprite( SpriteSource src )								{	sprites.add(this);	source = src;	}
	public	Sprite( SpriteSource src , int xh , int yh )			{	sprites.add(this);	source = src;	setSize(xh,yh);	}
	public	Sprite( String image )									{	sprites.add(this);	source = SpriteSourceLoader.getSpriteSource( image );			}
	public	Sprite( String image , int x , int y )					{	sprites.add(this);	source = SpriteSourceLoader.getSpriteSource( image , x , y );	}
	public	Sprite( String image , int x , int y , int xh , int yh ){	sprites.add(this);	source = SpriteSourceLoader.getSpriteSource( image , x , y );	setSize(xh,yh);	}
	
	public	boolean	isPlay(){	return run;	}
	public	void	play()	{	run=true;	}
	public	void	stop()	{	run=false;	}
	
	private	Action[]actions=null;	//	А ВОТ ТУТ ВОЗМОЖНО и стоило бы использовать лямбды. Но я пока подожду с этим.
	public	void	setAction( int F , Action A )	{	if( F>=0 && F<actions.length )		actions[F]=A;	}
	public	Action	getAction( int F )	{	if( F>=0 && F<actions.length )return(actions[F]);return null;	}
	public	void	setSize( int xh , int yh )	{	xHalf=xh;	yHalf=yh;	}
	
	private	void	init()
		{
		System.out.println("init");
		if( source==null || !source.ready )	return;
		size = source.img.length;actions=new Action[size];
		if( xHalf==-1 )	{	xHalf=source.xHalf;	yHalf=source.yHalf;	}
		ready = true;
		}
	//****************************************************************************************************************************************//
	
	

	//****************************************************************************************************************************************//
	public	void	move( int f )
		{
		frame=f%size;
		}
	public	void	step()
		{
		if( !ready && source.ready )
			{	init();	}
		if( !run || !ready )	return;
		if( actions!=null && frame<actions.length )
			if( actions[frame]!=null )
				actions[frame].exec(this);
		frame=(frame+1)%size;
		}
	public	void	draw( GraphicsWrap graph )
		{
		if( !ready || source!=null )
			graph.drawSpriteSource( source , frame , (int)(x + 0.5f) , (int)(y + 0.5f) );
		}
	//****************************************************************************************************************************************//
	
	
	
	//****************************************************************************************************************************************//
	public	boolean	hitTest( float x , float y )
		{
		return hitTest( (int)(x+.5f) , (int)(y+.5f) );
		}
	public	boolean	hitTest( int x , int y )
		{	return( hitPower(x,y)!=0 );	}
	public	int	hitPower( int x , int y )
		{
		if( source==null || !source.ready )return(0);
		int tx = (int)( x - ( this.x - source.xHalf ) ) ;
		int ty = (int)( ( this.y + source.yHalf ) - y ) ;
		if(	tx>=0 && tx<source.img[frame].getWidth()
		&&	ty>=0 && ty<source.img[frame].getHeight()	)
			{
			if( !ready )	return( 0 );
			return( (source.img[frame].getRGB(tx,ty)>>24)&0xFF );
			}
		return( 0 );
		}
	public	static	boolean	hitTest( Sprite A , Sprite B )
		{
		int dx = (int)( A.x - B.x + 0.5f ), dy = (int)( A.y - B.y + 0.5f );
		return( (dx<0?-dx:dx)<A.xHalf+B.xHalf && (dy<0?-dy:dy)<A.yHalf+B.yHalf );
		}
	
	public	static	void	collide( Sprite A , Sprite B )
		{
		if( A.fixed && B.fixed )	return;
		int dx = (int)( A.x - B.x + 0.5f );	if( dx<0 )	dx=-dx;
		int	dy = (int)( A.y - B.y + 0.5f );	if( dy<0 )	dy=-dy;
		int ddx = A.xHalf+B.xHalf-dx;
		int ddy = A.yHalf+B.yHalf-dy;
		if( ddx<0 || ddy<0 )		return;
		if( A.fixed &&!B.fixed )	collideFixed(B,A,ddx,ddy);
		if(!A.fixed && B.fixed )	collideFixed(A,B,ddx,ddy);
		if(!A.fixed &&!B.fixed )	collideFree(A,B,ddx,ddy);
		}
	private	static	void	collideFixed( Sprite A , Sprite B , int ddx , int ddy )
		{
		if( ddx<ddy )	{	if( A.x>B.x ){	A.x+=ddx;	}	if( A.x<B.x ){	A.x-=ddx;	}	}
		else			{	if( A.y>B.y ){	A.y+=ddy;	}	if( A.y<B.y ){	A.y-=ddy;	}	}
		}
	private	static	void	collideFree( Sprite A , Sprite B , int ddx , int ddy  )
		{
		if( ddx<ddy )	{	if( A.x>B.x ){	A.x+=ddx/2;	B.x-=ddx/2;	}	if( A.x<B.x ){	A.x-=ddx/2;	B.x+=ddx/2;	}	}
		else			{	if( A.y>B.y ){	A.y+=ddy/2;	B.y-=ddy/2;	}	if( A.y<B.y ){	A.y-=ddy/2;	B.y+=ddy/2;	}	}
		}
	//****************************************************************************************************************************************//
	}
