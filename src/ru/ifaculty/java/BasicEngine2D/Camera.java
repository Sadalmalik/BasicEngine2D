package ru.ifaculty.java.BasicEngine2D;

public class Camera
	{
	//	на замену тупым обёрткам графики и преобразованиям
	public	int x, y;
	public	boolean centering=true;
	
	public	int	xOff()	{	return(	-x	+	(centering?BasicEngine2D.getDisplay().xHalf:0)	);	}
	public	int	yOff()	{	return(	+y	+	(centering?BasicEngine2D.getDisplay().yHalf:0)	);	}
	}