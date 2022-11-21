
//A class for verify the given username & password.

import java.io.*;

  class PwdChecker {
	
	private static final String PATH = "c:/passwords.txt";
	private BufferedReader br;
	    
	public PwdChecker() {
	}	
				
/**********************************************************/	
	public boolean usernameExists(String username) {
		
		try{
			br = new BufferedReader( new FileReader( PATH ) ) ;
			        	
			String line = "";
			while( (line = br.readLine()) != null ){
        		
				//System.out.println( line );
				if (line.startsWith("Username:") )
					if ( line.substring(9).equals( "\"" + username + "\"" ) ) 
					   return true;
			}	   	
		}
		catch(IOException ioe1){
			System.out.println(ioe1.getMessage() + " 1");
		}
		
		return false;
	}
/************************************************************/	
	public boolean pwdCorrect(String username , String password){
		//in my way,"username" is useless!!!
		
		try{
			String line = br.readLine();
			
			//System.out.println( line );
			
			if (line.startsWith("Password:"))//it must be always true!!!
				if ( line.substring(9).equals( "\"" + password  + "\"" )) {
					
					return true;
				}		
		}
		catch(IOException ioe){
			System.out.println( ioe.getMessage() + " 4");
		}
		try{
			br.close();
		}
		catch(IOException ioe2){
			 System.out.println(ioe2.getMessage() + " 5");
		}
		return false;
	}

	
	protected void finalize() throws Throwable {
		super.finalize();
		br.close();
	}
	
	public static void main(String[] args){
		
			PwdChecker checker = new PwdChecker();
			System.out.println( checker.usernameExists( "John") );
			System.out.println( checker.pwdCorrect( "John" , "hello") );
			System.out.println( checker.usernameExists( "narges") );
			System.out.println( checker.usernameExists( "Ali") );
			System.out.println( checker.pwdCorrect( "Ali" , "123") );
	}
}//class

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
