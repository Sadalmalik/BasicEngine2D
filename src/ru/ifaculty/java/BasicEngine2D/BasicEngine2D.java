/**
 *	Basis engine for 2D games!
 *	Author:		Kaleb|Sadalmalik (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
**/

package ru.ifaculty.java.BasicEngine2D;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import ru.ifaculty.java.BasicEngine2D.audio.Sound;
import ru.ifaculty.java.BasicEngine2D.audio.SoundSourceLoader;
import ru.ifaculty.java.BasicEngine2D.graphic.Sprite;
import ru.ifaculty.java.BasicEngine2D.graphic.SpriteSourceLoader;

public class BasicEngine2D
	{
	public		static	final	String	name		=	"Basic Engine 2D"	;
	public		static	final	int		preVersion	=			0			;
	public		static	final	int 	subVersion	=			1			;
	public		static	final	int 	reliz		=			7			;
	public		static	final	String	auth		=		 "Kaleb"		;
	@SuppressWarnings("unused")	//всё норм, компилятору не нравятся условия на статичных данных... но мы-то знаем что данные ещё будут меняться!)
	public		static	String	info()	{	return(name+", version "+preVersion+"."+subVersion+"."+((reliz>9)?(reliz):("0"+reliz))+", by "+auth);	}	//	No dead code! >:@
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	ПРОЩЕ НЕКУДА
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public	static	void	JUST_DO_IT( Wrap W )	{	start(W);	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Базовые инициализации, окно
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected	static	JFrame	WINDOW;
	protected	static	Canvas	CANVAS;
	protected	static	WindowAdapter	myWindowAdapter;
	protected	static	KeyListener		myKeyListener;
	protected	static	MouseListener	myMouseListener;
	private		BasicEngine2D(){}
	
	public	static	void	createWindow( )							{	createWindow( 1024 , 768 , null );	}
	public	static	void	createWindow( int xSize , int ySize )	{	createWindow( xSize , ySize , null );	}
	public	static	void	createWindow( int xSize , int ySize , String title )
		{
		CANVAS = new Canvas();
		WINDOW = new JFrame();
		WINDOW.add(CANVAS);
		WINDOW.pack();
		myWindowAdapter = new WindowAdapter()
			{
			public void windowClosing(WindowEvent arg0) { BasicEngine2D.execute=false; }	//	немного быдлокодерски
			};
		myKeyListener = new KeyListener()
			{
			public void keyPressed	(KeyEvent K)	{keyPressAction(K);}
			public void keyReleased	(KeyEvent K)	{keyRelizAction(K);}
			public void keyTyped	(KeyEvent K)	{keyTypedAction(K);}
			};
		myMouseListener = new MouseListener()
			{
			public void mouseClicked	(MouseEvent e){}
			public void mouseEntered	(MouseEvent e){}
			public void mouseExited		(MouseEvent e){}
			public void mousePressed	(MouseEvent e){mousePressAction(e);}
			public void mouseReleased	(MouseEvent e){mouseRelizAction(e);}
			};
		WINDOW.addWindowListener(myWindowAdapter);
		WINDOW.addKeyListener	(myKeyListener	);
		CANVAS.addMouseListener	(myMouseListener);
		WINDOW.setResizable(false);
		CANVAS.setSize(xSize, ySize);
		WINDOW.pack();
		setWindowTitle(title);
		WINDOW.setLocationRelativeTo(null);
		WINDOW.setFocusable(true);
		/**	HOTFIX:	при наличие внуртеннего объекта если на него поподает фокус, окно перестаёт регистрировать кнопки.	**/
		CANVAS.setFocusable(false);	
		WINDOW.setVisible(true);
		}
	
	private	static	String	Title=info();
	public	static	void	setWindowTitle		(String title)	{	if( title!=null	)Title=title;	if( WINDOW!=null )WINDOW.setTitle(Title);	}
	public	static	void	setWindowPosition	(int x, int y)	{	if( WINDOW!=null )	{	WINDOW.setBounds(x,y,1,1);	WINDOW.pack();	}	}
	public	static	void	setWindowSize		(int x, int y)	{	if( CANVAS!=null )	{	CANVAS.setSize	( x , y );	WINDOW.pack();	}	}
	public	static	void	setWindowResizable	(boolean flag)	{	if( WINDOW!=null )	{	WINDOW.setResizable(flag);	WINDOW.pack();	}	}
	
	public	static	void	destroyWindow()
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
	
	public	static int xSize, ySize;
	public	static int xHalf, yHalf;
	public	static void updateOffset()
		{
		if( CANVAS==null ) return;
		xSize	=	CANVAS.getWidth()	;		xHalf	=	(xSize+1)/2	;
		ySize	=	CANVAS.getHeight()	;		yHalf	=	(ySize+1)/2	;
		if( isAutoScaleSound2D() )	Sound.setScale2D(Math.min(xHalf,yHalf));
		}
	
	public	static int xMouse, yMouse;
	public	static boolean mouseIn=false;
	public	static void updateMouse()
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

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Настраиваемые параметры
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public	static	boolean	execute=false;
	private	static	final int show_fps			=	0x00000001	;
	private	static	final int show_source_info	=	0x00000002	;
	private	static	final int show_window_info	=	0x00000004	;
	private	static	final int show_mouse_info	=	0x00000008	;
	private	static	final int optimize			=	0x00000010	;
	private	static	final int auto_scale_sound	=	0x00000020	;
	private	static	final int exit_on_end		=	0x80000000	;
	private	static	int	status = show_fps | optimize | auto_scale_sound | exit_on_end ;	//	Flag pack, запас - 32 флага

	public	static	void	showAllInfo(boolean flag)
		{
		if( flag )	{	status = status | show_fps | show_source_info | show_window_info | show_mouse_info ;	}
		else		{	status = status&~(show_fps | show_source_info | show_window_info | show_mouse_info);	}
		}
	public	static	void	showFPS(boolean flag)			{	status = (status&~show_fps)			| (flag?show_fps:0) ;			}
	public	static	void	showSourceInfo(boolean flag)	{	status = (status&~show_source_info)	| (flag?show_source_info:0) ;	}
	public	static	void	showWindowInfo(boolean flag)	{	status = (status&~show_window_info)	| (flag?show_window_info:0) ;	}
	public	static	void	showMouseInfo(boolean flag)		{	status = (status&~show_mouse_info)	| (flag?show_mouse_info:0) ;	}
	public	static	void	exitOnEnd(boolean flag)			{	status = (status&~exit_on_end)		| (flag?exit_on_end:0) ;		}
	public	static	void	optimize(boolean flag)			{	status = (status&~optimize)			| (flag?optimize:0) ;			}
	public	static	void	autoScaleSound2D(boolean flag)	{	status = (status&~show_mouse_info)	| (flag?auto_scale_sound:0) ;	}
	
	public	static	boolean	isShowFPS()						{	return( (status&show_fps) > 0 );			}
	public	static	boolean	isShowSourceInfo()				{	return( (status&show_source_info) > 0 );	}
	public	static	boolean	isShowWindowInfo()				{	return( (status&show_window_info) > 0 );	}
	public	static	boolean	isShowMouseInfo()				{	return( (status&show_mouse_info) > 0 );		}
	public	static	boolean	isExitOnEnd()					{	return( (status&exit_on_end) > 0 );			}
	public	static	boolean	isOptimize()					{	return( (status&optimize) > 0 );			}
	public	static	boolean	isAutoScaleSound2D()			{	return( (status&auto_scale_sound) > 0 );	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Обёртка и главный цикл
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private	static	Wrap wrap;
	public	static	void setWrap( Wrap W )	{wrap=W;}
	private static	void ident()
		{
		if( wrap==null )return;
		wrap.xSize=xSize;	wrap.ySize=ySize;
		wrap.xHalf=xHalf;	wrap.yHalf=yHalf;
		wrap.xMouse=xMouse;	wrap.yMouse=yMouse;
		}
	
	private	static	Event	calcFPS, step, anim;
	public	static	void	setStepDelay	(long d)	{	step.setDelay(d);	}	//	контроль скрытых эвентов... иначе никак =(
	public	static	void	setStepFreqency	(float f)	{	step.setFreqency(f);}	//	не убивать хорошую же концепцию?
	public	static	void	setAnimDelay	(long d)	{	anim.setDelay(d);	}
	public	static	void	setAnimFreqency	(float f)	{	anim.setFreqency(f);}
	
	private	static	List<Event>events	=	new ArrayList<Event>();	//	Список вторичных событий создаваемых извне
	public	static	void	addEvent(Event E)	{	events.add(E);		}
	public	static	void	delEvent(Event E)	{	events.remove(E);	}

	public	static	int		FPS=0, FPSCounter=0;
	public	static	void	updateInput()		{	updateMouse();updateOffset();ident();	}
	public	static	void	start( Wrap W )		{	setWrap( W );createWindow();start();	}
	public	static	void	start()
		{
		if( wrap==null )	{	return;			}
		if( WINDOW==null )	{	createWindow();	}
		
		//	Кросота!!! дефолтные эвенты
		calcFPS = new Event(1000,"Calc FPS")		{public void exec(){	FPS=FPSCounter;	FPSCounter=0;	}};
		step	= new Event(50,"Simulation step")	{public void exec(){	if(wrap!=null)	wrap.step();	}};
		anim	= new Event(60,"Animation step")	{public void exec(){	Sprite.stepAll();				}};
		
		wrap.init();
		execute=true;
		long next;
		
		while( execute && wrap!=null )
			{
			next=Long.MAX_VALUE;	//	Событийная модель рулез!
			{{	{{calcFPS.test();}}		{{next=min(next,calcFPS.nextTime());}}	}}
			updateInput();
			{{	{{step.test();}}		{{next=min(next,step.nextTime());}}		}}
			for( Event T : events )
			{{	{{T.test();}}			{{next=min(next,T.nextTime());}}		}}
			rend();
			{{	{{anim.test();}}		{{next=min(next,anim.nextTime());}}		}}
			SpriteSourceLoader.loadingStep();
			SoundSourceLoader.loadingStep();
			FPSCounter++;
			next -= System.currentTimeMillis() ;
			if( queue()==0 && isOptimize() && next>1 )	sleep(next);
			//	всё свободное время спим. Это очень важно для соседних потоков.
			//	Например звука, который почему-то нельзя упаковать в один поток... ПОЗОР СТАНДАРТНЫМ ЛИБАМ ЗА ЭТО
			}
		
		wrap.exit();
		
		if( WINDOW!=null )	{	destroyWindow();	}
		if( isExitOnEnd() )	{	System.exit(0);		}
		}
	private	static	int		queue	(				)	{	return(SpriteSourceLoader.queue()+SoundSourceLoader.queue());	}
	private	static	long	min		(long a, long b )	{	return(a<b?a:b);	}
	public	static	void	sleep	(	long t		)	{	try {	Thread.sleep(t);	} catch (InterruptedException e) {	e.printStackTrace();	}	}
	public	static	void	stop	(				)	{	execute=false;	}	//	это может быть вызвано из другого потока или изнутри кода.
	public	static	String	dumpEvents()
		{
		StringBuilder r = new StringBuilder("");
		r.append(calcFPS).append("\n");
		r.append(step).append("\n");
		r.append(anim).append("\n").append("\n");
		for( Event T : events )r.append(T).append("\n");
		r.append("\n");
		return( r.toString() );
		}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Рендер
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private	static Color background	=	Color.BLACK	;
	private	static Color infoColor	=	Color.WHITE	;
	public	static void setBackgroundColor	( Color c )	{	if(c!=null)background=c;	}
	public	static void setInfoColor		( Color c )	{	if(c!=null)infoColor=c;	}
	public	static void rend()
		{
		BufferStrategy bs = CANVAS.getBufferStrategy();
		if( bs == null ){	CANVAS.createBufferStrategy(3);	return;	}	//	три буфера... возможно стоит поставить два? я хз))
		Graphics2D G = (Graphics2D)bs.getDrawGraphics();
		G.setColor( background );
		G.fillRect( 0 , 0 , xSize , ySize );
		GraphicsWrap GW = new GraphicsWrap( G , xHalf , yHalf );
		
		if( wrap!=null )wrap.rend(GW);
		
		G.setColor(infoColor);
		int y=20;
		if( isShowFPS() )
			{
			GW.drawRawString("FPS: "+FPS,10,y);
			y+=10;
			}
		if( isShowWindowInfo() )
			{
			y+=15;	GW.drawRawString("Window",20,y);
			y+=15;	GW.drawRawString("size",20,y);		GW.drawRawString(xSize+" "+ySize,80,y);
			y+=15;	GW.drawRawString("center",20,y);	GW.drawRawString(xHalf+" "+yHalf,80,y);
			y+=10;
			}
		if( isShowMouseInfo() )
			{
			y+=15;	GW.drawRawString("Mouse",20,y);
			y+=15;	GW.drawRawString("position",20,y);		GW.drawRawString(xMouse+" "+yMouse,80,y);
			y+=15;	GW.drawRawString("in window",20,y);		GW.drawRawString(""+mouseIn,80,y);
			y+=10;
			}
		if( isShowSourceInfo() )
			{
			GW.drawString(SpriteSourceLoader.getState(),10,ySize-35);
			GW.drawString(SoundSourceLoader.getState(),10,ySize-15);
			}
		G.dispose();
		bs.show();
		}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Сквозная проброска управления
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private	static void keyPressAction(KeyEvent E)		{	updateInput();	if( wrap!=null )	wrap.keyPressAction( E );	}
	private	static void keyRelizAction(KeyEvent E)		{	updateInput();	if( wrap!=null )	wrap.keyRelizAction( E );	}
	private	static void keyTypedAction(KeyEvent E)		{	updateInput();	if( wrap!=null )	wrap.keyTypedAction( E );	}
	private	static void mousePressAction(MouseEvent E)	{	updateInput();	if( wrap!=null )	wrap.mousePressAction( E );	}
	private	static void mouseRelizAction(MouseEvent E)	{	updateInput();	if( wrap!=null )	wrap.mouseRelizAction( E );	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Загрузчик ресурса
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean last;
	private static String sourceFolder = "resources/";
	private static String sourceInnerPath = "";
	private static String sourceOuterPath = "";
	public static void setSourceFolder	( String s )	{	if(s!=null)sourceFolder=s;		}	//	защита от выстрела в ногу
	public static void setInnerPath		( String p )	{	if(p!=null)sourceInnerPath=p;	}
	public static void setOuterPath		( String p )	{	if(p!=null)sourceOuterPath=p;	}
	
	public static InputStream getResource( String source )
		{
		InputStream INP = null;
		INP = getInnerResource( source );
		if( INP == null )	//	Пробуем загрузить сначала из внутренних ресурсов, затем из внешних
		INP = getOuterResource( source );
		return( INP );
		}
	public static InputStream getInnerResource( String source )
		{
		InputStream INP;
		try {	INP = (BasicEngine2D.class).getResourceAsStream( "/"+sourceInnerPath+sourceFolder+source ) ;	}	catch (Exception ex)	{	INP=null;	}
		return( INP );
		}
	public static InputStream getOuterResource( String source )
		{
		InputStream INP;
		try {	INP = new FileInputStream(	sourceOuterPath+sourceFolder+source	) ;	}	catch (Exception ex)	{	INP=null;	}
		return( INP);
		}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Дамп потоков. Было нужно для отладки. В будущем можно удалить это. Наверное....
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static List<String> getRunningThreads()
		{
		List<String> threads = new ArrayList<String>();
		ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
		ThreadGroup parent;
		while ((parent = threadGroup.getParent()) != null)
			{
			if (threadGroup != null)
				{
				threadGroup = parent;
				Thread[] threadList = new Thread[threadGroup.activeCount()];
				threadGroup.enumerate(threadList);
				for (Thread thread : threadList)
					{
					threads	.add(new StringBuilder().append(thread.getThreadGroup().getName())
							.append("	:	").append(thread.getName()).append("	:	PRIORITY: ")
							.append(thread.getPriority()).toString());
					}
				}
			}
		return threads;
		}
	}
