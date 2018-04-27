package com.tmg.gemfire.DataTypes;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Bool implements Externalizable {
	
	
	
	
	private static final int FIRST_VERSION = 0;
	public Value value;

	public enum Value {
		FALSE, TRUE;

		public static Value valueOfType(String type) {

			if (type == null)
				throw new IllegalArgumentException("NO such type:" + type);
			switch (type.toLowerCase()) {

			case "false":
				return Value.FALSE;
			case "f":
				return Value.FALSE;
			case "true":
				return Value.TRUE;
			case "t":
				return Value.TRUE;

			default:
				throw new IllegalArgumentException("No such type:" + type);
			}
		}

	};

	
	
	public Bool(){
		value=Value.FALSE;
	}
	
	
	public Bool(Value value){
		this.value=value;
	}
	
	 public void writeExternal(ObjectOutput out) throws IOException{
		 out.writeInt( FIRST_VERSION );
		 out.writeObject(value);
	 }
	
	 public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
		 	int oldVersion = in.readInt();
	        if ( oldVersion < FIRST_VERSION ) { 
	            throw new IOException( "Corrupt data stream." ); 
	        }
	        if ( oldVersion > FIRST_VERSION ) { 
	            throw new IOException( "Can't deserialize from the future." );
	        }
	        value=(Value)in.readObject();
}
	

}
