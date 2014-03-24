package ru.ifaculty.java.BasicEngine2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import ru.ifaculty.java.BasicEngine2D.audio.SoundSourceLoader;
import ru.ifaculty.java.BasicEngine2D.graphic.Sprite;
import ru.ifaculty.java.BasicEngine2D.graphic.SpriteSourceLoader;

public class BasicEngine2D
	{
	public		static	final	String	name		=	"Basic Engine 2D"	;
	public		static	final	int		preVersion	=			0			;
	public		static	final	int 	subVersion	=			2			;
	public		static	final	int 	reliz		=			1			;
	public		static	final	String	auth		=		 "Kaleb"		;
	@SuppressWarnings("unused")	//всё норм, компилятору не нравятся условия на статичных данных... но мы-то знаем что данные ещё будут меняться!)
	public		static	String	info()	{	return(name+", version "+preVersion+"."+subVersion+"."+((reliz>9)?(reliz):("0"+reliz))+", by "+auth);	}	//	No dead code! >:@

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Базовые инициализации, окно
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private	BasicEngine2D(){}

	private	static	Display			display=null;
	private	static	Display.listner	listner=null;
	static
		{
		//	Вообще весь этот финт с выносом контроля окна в Display довольно сомнителен
		//	Ибо код не сильно уменьчшился. Но всё же какая-то логика в этом есть - если кому-либо понадобиться - можно использовать Display отдельно
		listner = new Display.listner()
			{
			public void close()	{	execute=false;	}
			public void rend(Graphics2D G)				{	render(G);	}
			public void keyPressAction(KeyEvent K)		{	if( wrap!=null )	wrap.keyPressAction( K );	}
			public void keyRelizAction(KeyEvent K)		{	if( wrap!=null )	wrap.keyRelizAction( K );	}
			public void keyTypedAction(KeyEvent K)		{	if( wrap!=null )	wrap.keyTypedAction( K );	}
			public void mousePressAction(MouseEvent M)	{	if( wrap!=null )	wrap.mousePressAction( M );	}
			public void mouseRelizAction(MouseEvent M)	{	if( wrap!=null )	wrap.mouseRelizAction( M );	}
			};
		display = new Display( listner );
		}
	public	static	Display	getDisplay			()				{	return display ;	}
	public	static	void	setWindowTitle		(String title)	{	if ( display!=null )	display.setWindowTitle(title);		}
	public	static	void	setWindowPosition	(int x, int y)	{	if ( display!=null )	display.setWindowPosition(x,y);		}
	public	static	void	setWindowSize		(int x, int y)	{	if ( display!=null )	display.setWindowSize(x,y);			}
	public	static	void	setWindowResizable	(boolean flag)	{	if ( display!=null )	display.setWindowResizable(flag);	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Настраиваемые параметры		BitSet, и зафиг я вручную парился?
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public	static	boolean	execute=false;
	private	static	BitSet	state=new BitSet(8);
	static	{	showFPS(true);	autoScaleSound2D(true);	exitOnEnd(true);	optimize(true);	}

	public	static	void	showAllInfo(boolean f)			{	showFPS(f);	showWindowInfo(f);	showMouseInfo(f);	showSourceInfo(f);	}
	
	public	static	void	showFPS(boolean flag)			{	state.set(0,flag);	}
	public	static	void	showWindowInfo(boolean flag)	{	state.set(1,flag);	}
	public	static	void	showMouseInfo(boolean flag)		{	state.set(2,flag);	}
	public	static	void	showSourceInfo(boolean flag)	{	state.set(3,flag);	}
	public	static	void	autoScaleSound2D(boolean flag)	{	state.set(4,flag);	}
	public	static	void	exitOnEnd(boolean flag)			{	state.set(5,flag);	}
	public	static	void	optimize(boolean flag)			{	state.set(6,flag);	}
	
	public	static	boolean	isShowFPS()						{	return state.get(0);	}
	public	static	boolean	isShowWindowInfo()				{	return state.get(1);	}
	public	static	boolean	isShowMouseInfo()				{	return state.get(2);	}
	public	static	boolean	isShowSourceInfo()				{	return state.get(3);	}
	public	static	boolean	isAutoScaleSound2D()			{	return state.get(4);	}
	public	static	boolean	isExitOnEnd()					{	return state.get(5);	}
	public	static	boolean	isOptimize()					{	return state.get(6);	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Обёртка и главный цикл
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private	static	Wrap wrap;
	public	static	void setWrap( Wrap W )	{wrap=W;}
	private static	void ident()
		{
		if( wrap==null || display==null )return;
		wrap.xSize	=	display.xSize;		wrap.ySize	=	display.ySize	;
		wrap.xHalf	=	display.xHalf;		wrap.yHalf	=	display.yHalf	;
		wrap.xMouse	=	display.xMouse;		wrap.yMouse	=	display.yMouse	;
		}
	
	private	static	Event	calcFPS, step, anim;
	public	static	void	setStepDelay	(long d)	{	if(step!=null)	step.setDelay(d);	}	//	контроль скрытых эвентов... иначе никак =( Нет в мире идеальных концепций!
	public	static	void	setStepFreqency	(float f)	{	if(step!=null)	step.setFreqency(f);}	//	не убивать хорошую же концепцию?
	public	static	void	setAnimDelay	(long d)	{	if(anim!=null)	anim.setDelay(d);	}
	public	static	void	setAnimFreqency	(float f)	{	if(anim!=null)	anim.setFreqency(f);}
	
	private	static	List<Event>events	=	new ArrayList<Event>();			//	Список вторичных событий создаваемых извне
	public	static	void	addEvent(Event E)	{	events.add(E);		}
	public	static	void	delEvent(Event E)	{	events.remove(E);	}

	public	static	int		FPS=0, FPSCounter=0;
	public	static	void	start( Wrap W )		{	setWrap( W );	start();	}
	public	static	void	start()
		{
		if( wrap==null )		{	return;					}
		if( !display.ready() )	{	display.createWindow();	}
		
//		Кросота!!! дефолтные эвенты
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
			ident();
			{{	{{step.test();}}		{{next=min(next,step.nextTime());}}		}}
			for( Event T : events )
			{{	{{T.test();}}			{{next=min(next,T.nextTime());}}		}}
			display.rend();
			{{	{{anim.test();}}		{{next=min(next,anim.nextTime());}}		}}
			SpriteSourceLoader.loadingStep();
			SoundSourceLoader.loadingStep();
			FPSCounter++;
			next -= System.currentTimeMillis() ;
			if( queue()==0 && isOptimize() && next>1 )	sleep(next);
			//	всё свободное время спим. Это очень важно для соседних потоков.
			//	Например звука, который почему-то нельзя упаковать в один поток(sic!)... ПОЗОР СТАНДАРТНЫМ ЛИБАМ ЗА ЭТО
			}
		
		wrap.exit();
		
		if( display.ready() )	{	display.destroyWindow();	}
		if( isExitOnEnd() )		{	System.exit(0);		}
		}
	private	static	int		queue	(				)	{	return(SpriteSourceLoader.queue()+SoundSourceLoader.queue());	}
	private	static	long	min		(long a, long b )	{	return(a<b?a:b);	}
	public	static	void	sleep	(	long t		)	{	try {	Thread.sleep(t);	} catch (InterruptedException e) {	e.printStackTrace();	}	}
	public	static	void	stop	(				)	{	execute=false;	}
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
	public	static void render( Graphics2D G )
		{
		G.setColor( background );
		G.fillRect( 0 , 0 , display.xSize , display.ySize );
		G.setColor( infoColor );
		GraphicsWrap GW = new GraphicsWrap( G , display.xHalf , display.yHalf );
		
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
			y+=15;	GW.drawRawString("size",20,y);		GW.drawRawString(display.xSize+" "+display.ySize,80,y);
			y+=15;	GW.drawRawString("center",20,y);	GW.drawRawString(display.xHalf+" "+display.yHalf,80,y);
			y+=10;
			}
		if( isShowMouseInfo() )
			{
			y+=15;	GW.drawRawString("Mouse",20,y);
			y+=15;	GW.drawRawString("position",20,y);		GW.drawRawString(display.xMouse+" "+display.yMouse,80,y);
			y+=15;	GW.drawRawString("in window",20,y);		GW.drawRawString(""+display.mouseIn,80,y);
			y+=10;
			}
		if( isShowSourceInfo() )
			{
			GW.drawString(SpriteSourceLoader.getState(),10,display.ySize-35);
			GW.drawString(SoundSourceLoader.getState(),10,display.ySize-15);
			}
		}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Загрузка ресурсов
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean last;
	private static String sourceFolder = "resources/";
	private static String sourceInnerPath = "";
	private static String sourceOuterPath = "";
	public static void setSourceFolder	( String s )	{	if(s!=null)sourceFolder=s;		}	//	������ �� �������� � ����
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
