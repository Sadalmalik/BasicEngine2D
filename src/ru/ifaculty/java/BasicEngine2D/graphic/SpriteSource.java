/**
 *	Author:		Kaleb|Sadalmalik (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
**/

package ru.ifaculty.java.BasicEngine2D.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import ru.ifaculty.java.BasicEngine2D.BasicEngine2D;
import ru.ifaculty.java.BasicEngine2D.Source;


public class SpriteSource extends Source
	{
	public static Color notLoad = Color.YELLOW;
	public static Color isBreak = Color.RED;
	
	SpriteSource next;
	
	public String getError()
		{
		switch( error )
			{
			case 100:	return"Can't cut image to parts!";
			}
		return("Unknown error!");
		}
	public String getState()
		{
		return( "[ width "+xSize+" px, height "+ySize+" px, frames "+Frames+" ]" );
		}
	
	public BufferedImage[] img;
	public short xSize, ySize;	//	Окей, мы все знаем что картинки размером больше чем 32000 точек на 32000
	public short xHalf, yHalf;	//	точек не существуют. А если и есть - у нас они не появятся
	public boolean scale=false;	//	облегчаем себе поиск по сигнатуре
	public int Frames=0;
	
	public SpriteSource( String image )
		{
		FileName = image;
		xSize	=	xHalf	=	0	;
		ySize	=	yHalf	=	0	;
		scale	=	true;
		}
	public SpriteSource( String image , int width , int height )
		{
		FileName = image;
		xSize=(short)width;		ySize=(short) height	;
		xHalf=(short)(xSize/2);	yHalf=(short)(ySize/2)	;
		if( xSize==0 || ySize==0 )	scale	=	true	;
		}
	public boolean	hitTest( int index , int xOff , int yOff , int x , int y )
		{
		if( !ready )	return( false );
		
		int px = BasicEngine2D.getDisplay().xHalf+xOff-xHalf;	//	как некрасиво... Возможно я зря начал все эти попытки приведения систем координат и их следует убрать?
		int py = BasicEngine2D.getDisplay().yHalf-yOff-yHalf;
		
		if( x>=px && x<px+xSize && y>=py && y<py+ySize )
			{
			while( index<0 )	index+=Frames;
			return( (img[index% Frames].getRGB(x,y)&0xFF000000) != 0 );
			}
		
		return( false );
		}
	public void draw(Graphics2D graph, int index, int xOff, int yOff)
		{	draw(graph,index,xOff,yOff,null);	}
	public void draw(Graphics2D graph, int index, int xOff, int yOff, Color filter)
		{
		int sx, sy;
		int px = xOff-xHalf;
		int py = yOff-yHalf;
		if( px<-xSize || px>BasicEngine2D.getDisplay().xSize || py<-ySize || py>BasicEngine2D.getDisplay().ySize )return;
		Color T=graph.getColor();
		if( ready )
			{
			while( index<0 )	index+=Frames;
			graph.drawImage(img[index% Frames], px, py, xSize, ySize, null);
			if( filter!=null )
				{
				graph.setColor(filter);
				graph.fillRect(px, py, xSize, ySize);
				}
			}
		else{	//	Если изображения по какой-то причине не готовы - рисуем пустой квадратик.
			graph.setColor( (wreck)?(isBreak):(notLoad) );
			sx=xSize>10?xSize:10;
			sy=ySize>10?ySize:10;
			graph.drawRect(px, py, sx, sy);
			graph.drawLine(px+sx, py, px, py+sy);
			}
		graph.setColor(T);
		}
	
	public	void load()
		{
		ready=false;
		BufferedImage temp = getImage( FileName );
		if( temp!=null )
			{
			int xFrame = temp.getWidth(null);
			int yFrame = temp.getHeight(null);
			int counter_x, counter_y ;
			boolean set=false;
			if( scale )
				{
				xSize=(short)xFrame;	ySize=(short)yFrame;
				xHalf=(short)(xSize/2);	yHalf=(short)(ySize/2);
				counter_x = 1;	counter_y = 1;
				}
			else{
				counter_x = xFrame/xSize;	//	Число кадров в строке
				counter_y = yFrame/ySize;	//	Число кадров в столбце
				set=true;
				}
			Frames = counter_x*counter_y;
			if( Frames>0 && Frames<1000000 )//	страшно представить анимацию с таким числом кадров...
				{							//	Вернее представить то не проблема, но учитывая что я всё делаю из рассчёта на небольшие игры - это не нужно.
				if( Frames==1 && set )
					System.out.println("WARNING: passible incorrect frame scale selected!");
				img = new BufferedImage[Frames];
				for( int f=0 ; f<Frames ; f++ )
					img[f] = temp.getSubimage( (f%counter_x)*xSize, (f/counter_x)*ySize, xSize, ySize );
				ready=true;
				}
			else{
				error=100;
				img=null;
				wreck=true;
				}
			}
		else{
			error=1;
			img=null;
			wreck=true;
			}
		}

	private BufferedImage getImage( String source )
		{
		BufferedImage img = null;
		InputStream INP = BasicEngine2D.getResource(source);
		inner = BasicEngine2D.last;
		if( INP!=null )
			{
			try	 {		img = ImageIO.read( INP );	INP.close();		}
			catch (Exception ex){	ex.printStackTrace();	img=null;	}
			}
		return( img );
		}
	}
