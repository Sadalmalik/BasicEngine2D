package ru.ifaculty.java.BasicEngine2D.inet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TEST
	{
	public static void main(String[]args) throws IOException
		{
		OutputData O;
		InputData I;
		
		O = new OutputData();
		DataOutputStream	DOUT= new DataOutputStream	( O );
		DOUT.writeInt(50539);
		DOUT.writeUTF("TEST1");
		DOUT.writeUTF("TEST2");
		DOUT.writeFloat((float)(6.25e-7));
		DOUT.writeUTF("a");
		DOUT.flush();
		
		I = new InputData( O.data );
		DataInputStream		DINP= new DataInputStream	( I );
		
		System.out.println( DINP.available() + "	" + I.available() + "	" + DINP.readInt	()	);
		System.out.println( DINP.available() + "	" + I.available() + "	" + DINP.readUTF	()	);
		System.out.println( DINP.available() + "	" + I.available() + "	" + DINP.readUTF	()	);
		System.out.println( DINP.available() + "	" + I.available() + "	" + DINP.readFloat	()	);
		System.out.println( DINP.available() + "	" + I.available() + "	" + DINP.readUTF	()	);
		System.out.println( DINP.available() + "	" + I.available() );
		
		DOUT.close();
		DINP.close();
		//*/
		}
	}
