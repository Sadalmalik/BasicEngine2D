/**
 *	Author:		Kaleb(Sadalmalik) (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
**/

package ru.ifaculty.java.BasicEngine2D;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Display
	{
	public	static	abstract	class	listner
		{
		public	Display		display;
		
		public	abstract	void	close();
		public	abstract	void	rend(Graphics2D G);
		
		public	abstract	void	keyPressAction(KeyEvent K);
		public	abstract	void	keyRelizAction(KeyEvent K);
		public	abstract	void	keyTypedAction(KeyEvent K);
		
		public	abstract	void	mousePressAction(MouseEvent e);
		public	abstract	void	mouseRelizAction(MouseEvent e);
		}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Blah-blah-blah-blah-blah
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected	static	JFrame	WINDOW;
	protected	static	Canvas	CANVAS;
	protected	static	WindowAdapter	myWindowAdapter;
	protected	static	KeyListener		myKeyListener;
	protected	static	MouseListener	myMouseListener;
	
	public	Display()
		{
		myWindowAdapter = new WindowAdapter()
			{
			public void windowClosing(WindowEvent arg0) { if( L!=null ) L.close(); }	//	это немного быдлокодерски...
			};
		myKeyListener = new KeyListener()
			{
			public void keyPressed	(KeyEvent K)	{if( L!=null ) L.keyPressAction(K);}
			public void keyReleased	(KeyEvent K)	{if( L!=null ) L.keyRelizAction(K);}
			public void keyTyped	(KeyEvent K)	{if( L!=null ) L.keyTypedAction(K);}
			};
		myMouseListener = new MouseListener()
			{
			public void mouseClicked	(MouseEvent e){}
			public void mouseEntered	(MouseEvent e){}
			public void mouseExited		(MouseEvent e){}
			public void mousePressed	(MouseEvent e){if( L!=null ) L.mousePressAction(e);}
			public void mouseReleased	(MouseEvent e){if( L!=null ) L.mouseRelizAction(e);}
			};
		}
	public	Display				( listner l )	{	this();				setListner(l);			}
	public	void	setListner	( listner l )	{	if(l!=null)	{	L=l;	L.display=this;	}	}
	private	listner	L=null;
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Работа с окном
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public	void	createWindow(  )						{	createWindow( sx , sy , null );	}
	public	void	createWindow( int xSize , int ySize )	{	createWindow( xSize , ySize , null );	}
	public	void	createWindow( int xSize , int ySize , String title )
		{
		CANVAS = new Canvas();
		WINDOW = new JFrame();
		WINDOW.add(CANVAS);
		
		WINDOW.addWindowListener(myWindowAdapter);
		WINDOW.addKeyListener	(myKeyListener	);
		CANVAS.addMouseListener	(myMouseListener);

		sx=xSize;
		sy=ySize;
		Title=title;
		px=WINDOW.getX();
		py=WINDOW.getY();
		
		updateWindow();
		
		WINDOW.setLocationRelativeTo(null);
		WINDOW.setFocusable(true);
		/**	HOTFIX:	при наличие внуртеннего объекта если на него поподает фокус, окно перестаёт регистрировать кнопки.	**/
		CANVAS.setFocusable(false);	
		WINDOW.setVisible(true);
		}
	
	public	boolean	ready()
		{
		return( WINDOW!=null );
		}
	private void	updateWindow()
		{
		if( CANVAS!=null )	{	CANVAS.setSize	( sx , sy );	}
		if( WINDOW!=null )
			{
			if( Title!=null	)			WINDOW.setTitle(Title);
			WINDOW.setResizable(res);	WINDOW.setBounds(px,py,1,1);
			WINDOW.pack();
			}
		}
	
	private	String	Title="Display";
	private	int		px=100, py=100;
	private	int		sx=800, sy=600;
	private	boolean	res=false;
	
	public	void	setWindowTitle		(String title)	{	if( title!=null	)Title=title;	updateWindow();	}
	public	void	setWindowPosition	(int x, int y)	{	px=x>0?x:px;	py=y>0?y:py;	updateWindow();	}
	public	void	setWindowSize		(int x, int y)	{	sx=x>0?x:sx;	sy=y>0?y:sy;	updateWindow();	}
	public	void	setWindowResizable	(boolean flag)	{	res=flag;						updateWindow();	}
	
	public	void	destroyWindow()
		{
		if( WINDOW==null )	return;
		WINDOW.removeWindowListener	(myWindowAdapter);
		WINDOW.removeKeyListener	(myKeyListener	);
		CANVAS.removeMouseListener	(myMouseListener);
		WINDOW.setVisible(false);
		WINDOW.dispose();
		WINDOW.remove(CANVAS);
		CANVAS=null;
		WINDOW=null;
		}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Получение состояния окна и мышки
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public	int xSize, ySize;
	public	int xHalf, yHalf;
	public	void updateOffset()
		{
		if( CANVAS==null ) return;
		xSize	=	CANVAS.getWidth()	;		xHalf	=	(xSize+1)/2	;
		ySize	=	CANVAS.getHeight()	;		yHalf	=	(ySize+1)/2	;
		}
	
	public	int xMouse, yMouse;
	public	boolean mouseIn=false;
	public	void updateMouse()
		{
		if( CANVAS==null ) return;
		Point P = CANVAS.getMousePosition();
		if( P!=null )
			{
			mouseIn	=		true		;
			xMouse	=		P.x-xHalf	;
			yMouse	=	ySize-P.y-yHalf	;
			}
		else{	mouseIn=false;	}
		}
	public	void	update()
		{
		updateOffset();
		updateMouse();
		}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Рендер
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public	void rend()
		{
		BufferStrategy bs = CANVAS.getBufferStrategy();
		if( bs == null ){	CANVAS.createBufferStrategy(3);	return;	}
		Graphics2D G = (Graphics2D)bs.getDrawGraphics();
		
		if( L!=null ) L.rend( G );
		
		G.dispose();
		bs.show();
		update();
		}
	}
