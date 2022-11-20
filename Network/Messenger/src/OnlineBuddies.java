//A class for hold and manupulate clients that connected to this server
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;
import java.net.*;

class OnlineBuddies extends Hashtable {
	
	
	public void addBuddy(String ID , Socket socket){
		
		put(ID , socket);
	}
	
	public void removeBuddy(String ID) {
		
		remove(ID);
	}
	
	public void removeAll(){
		
		clear();
	}
	
    public void writeToBuddy(String ID , String mess){
    	
    	Socket socket = (Socket) get(ID);
    	try {
    		
    	   	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    	   	out.writeBytes( mess );
	   	}
    	catch(IOException ioe){
    		System.out.println( ioe.getMessage());
    		
    	}
    	
    }
   
    public int size(){
    	
    	return super.size(); //if return size(); --> stackOverFlow
    }
    
    public Enumeration getIDs() {
	
    	return keys();
    }
    
    public boolean contains(String ID){
    	return containsKey(ID);
    }
    
    public void disconnect(String name) {
    	
    	Socket socket = (Socket) get(name);
    	try {
    	     socket.close();
    	}
    	catch(IOException ioe) {
    		System.out.println( ioe.getMessage() );
    	}
    }
    
}   
