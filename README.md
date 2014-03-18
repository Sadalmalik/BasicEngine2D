## Hi!
If that, I am Russian and the text translated the program:3

This "engine" (or shall I call it "framework"? ) is a wrapper round a set standard, but not too convenient libraries. So, it's cross-platform engine)

Look VERSIONS.md

License:	http://www.wtfpl.net/txt/copying/ , but remember me!)

### Basic use
To use this stuff you must extends "Wrap":
```java
public class TEST extends Wrap
	{
	public	static	void	main(String[]args)
		{
		BasicEngine2D.showAllInfo(true);	//	recommended for the first start
		BasicEngine2D.start(new TEST());
		}
	
	public void init(){}	//	This function will call after creating window, but before main cycle
	public void step(){}	//	This will call regulary
	public void rend(GraphicsWrap G){}	//	^ _ ^
	public void exit(){}	//	after all
	
	public void keyPressAction(KeyEvent E){}
	public void keyRelizAction(KeyEvent E){}
	public void keyTypedAction(KeyEvent E){}
	
	public void mousePressAction(MouseEvent E){}
	public void mouseRelizAction(MouseEvent E){}
	}
```

Basic Engine have simple Sprite and Sound realizations.
What not to be verbose I simply would will give an operation code with them:

```java
Sprite s = new Sprite( "Imagename.png" );
//	Easy?)
//	This code automaticly calling loading method, which push image to queue of loading.
//	Loading happens in a background mode and you don't need to worry about it.

Sprite s = new Sprite( "Imagename.png" , tileXize , tileYSize );
//	Such call automatically cuts the picture on a set of tiles

//	If you need to create a set of sprites from one picture, you can specify it obviously
s1 = new Sprite( "Imagename.png" );
s2 = new Sprite( "Imagename.png" );
s3 = new Sprite( "Imagename.png" );
//	Or you can use SpriteSource and create all sprites using it:
SpriteSource SRC = SpriteSourceLoader.getSpriteSource( "Imagename.png" , tileXize , tileYSize );
s4 = new Sprite( SRC );
s5 = new Sprite( SRC );
s6 = new Sprite( SRC );
//	In fact, when you create Sprite - it automaticly call SpriteSourceLoader.getSpriteSource.
//	SpriteSourceLoader search source in heap. If there no source - it will be loaded.

//	And at last draw:
s1.draw( GraphicsWrap G );
//	Yea... I create GraphicsWrap to control coordinate system
```

Operation with Sound is absolutely identical (Sound, SoundSource, SoundSourceLoader)
With a few exceptions:
 - instead of the draw method you shall use the play method which can be called anywhere. (For playing of each sound starting new Thread)
 - meanwhile you can load only.wav files
 - At the moment (on March 18, 2014) there is a problem with a sound: it plays not on all systems. And I don't know why = (

The part of a code for operation with a network is absolutely not ready and I don't know when I will finish it)

### Options

Well...
To me it is lazy that to write that here.
But I gave to all methods good names!

```java
//	All methods from BasicEngine2D
//	Window options
void	setWindowTitle		(String title)
void	setWindowPosition	(int x, int y)
void	setWindowSize		(int x, int y)
void	setWindowResizable	(boolean flag)

//	Engine options #1
void	showAllInfo(boolean flag)
void	showFPS(boolean flag)
void	showSourceInfo(boolean flag)
void	showWindowInfo(boolean flag)
void	showMouseInfo(boolean flag)
void	exitOnEnd(boolean flag)
void	autoScaleSound2D(boolean flag)	//	This option for 3D sound. It works!)
void	optimize(boolean flag)
//	Optimization consists in a pause of a basis cycle in intervals between operation of the user code
//	call optimize( false ) in init method and look at FPS

//	Engine options #2
void	setStepDelay	(long d)
void	setStepFreqency	(float f)
void	setAnimDelay	(long d)	//	This 
void	setAnimFreqency	(float f)

void setSourceFolder	( String s )	//	Sets folder name from which will be loaded resources
void setInnerPath		( String p )	//	Path to source folder in .jar file
void setOuterPath		( String p )	//	Path to source folder out of .jar file
```

That's it!
Good luck!
