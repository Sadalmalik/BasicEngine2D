/**
 *	Author:		Kaleb|Sadalmalik (i just use different nicknames :D, i'm Gleb)
 *	License:	http://www.wtfpl.net/txt/copying/
**/

package ru.ifaculty.java.BasicEngine2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class Wrap
	{
	public	int xSize=0, ySize=0;
	public	int xHalf=0, yHalf=0;
	public	int xMouse=0, yMouse=0;
	public	boolean mouseIn=false;
	
	public	Wrap()				{	this(50);	}
	public	Wrap( int delay )	{	stepDelay=delay;	latsStepTime=System.currentTimeMillis();	}
	
	public	int stepDelay=50;
	public	long latsStepTime;
	
	public	abstract void init();
	public	abstract void step();
	public	abstract void rend(GraphicsWrap G);
	public	abstract void exit();
	
	public	abstract void keyPressAction(KeyEvent E);
	public	abstract void keyRelizAction(KeyEvent E);
	public	abstract void keyTypedAction(KeyEvent E);
	
	public	abstract void mousePressAction(MouseEvent E);
	public	abstract void mouseRelizAction(MouseEvent E);
	
	public	static void sleep(long t)	{ BasicEngine2D.sleep(t); }	//	Обёртка для обёртки
	}
