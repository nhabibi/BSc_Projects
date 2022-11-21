
//This class only reads sentences and question from input and remove additional space and tab from them and finally save each in form of "word1 word2 ..." in translator

import java.util.*;
import java.io.*;

class Input {
	
	private Vector input;
	private Translator translator;
	private Output output;
	
	public Input( Vector input, Translator translator , Output output ){
		
		this.input = input;
		this.translator = translator;
		this.output = output;
		start();
	}
/*************************************************************/
/*This method, calls read() method with aproppriate input stream.
If any file name does not exist,it calls read with "standard input",
else, for each file name, calls read() with file stream.
*/	
	private void start(){
		
		if ( input.size() == 0 ){
			
			read( new BufferedReader( new InputStreamReader (System.in) ) );
		}
		
		else {
			 Enumeration name = input.elements();
			 while( name.hasMoreElements() ) {
			 	
			  try {
			 	   read( new BufferedReader( new FileReader( (String) name.nextElement() ) ) );
			  }
			  catch(FileNotFoundException fnte){
			 		
			 	System.out.println(fnte.getMessage());
			 	System.exit(0);
			 }
			 
		   }//while
		}//else			 
			 	
  }
/**********************************************************/
/* This method, reads from input stream(to end) and creates 1 sentence
 or 1 question for addToTranslatorOrOutput() method by appending components
 of it.
 */	
	private void read(BufferedReader input){
		
		try {
		     String line = "";
			 String result = "";
			 int index = -1;			 
			while( (line = input.readLine()) != null ) {
		    	
			   while ( ! line.equals("") ) {
			   	
			   	   if ( line.indexOf(".") != -1 ) {
			   	   	
			   	      index = line.indexOf(".") ;
			   	      addToTranslatorOrOutput( result + line.substring(0 , index + 1) );
			   	      result = "";
			   	      if ( index == line.length()-1 ) //end of line
			   	      	 line = "";
			   	      else
					     line = line.substring( index+1 );
			   	   }	  
				   else if ( line.indexOf("?") != -1 ) { 
				  	
					  index = line.indexOf("?");
					  addToTranslatorOrOutput( result + line.substring(0 , index + 1) );
					  result = "";
					  if ( index == line.length()-1 )
					  	line = "";
					  else
				          line = line.substring( index+1 );
				   }
                   else {//if after last word dose not exists '\b' or '\t' then :
				     result +=  " " +  line + " ";
				     line = "";
                   }   
			   }  	   
		   } 
		}//try	     
		catch(IOException ioe) {
        	System.out.println( ioe.getMessage() );
        	System.exit(0);
        }
  }
/*********************************************************/
/* This methos, removes any \t,\b and \n from sentences and questions and
 creates one, with 1 "\b" between words.
 */	
	private void addToTranslatorOrOutput(String line) {
		
		StringBuffer sb = new StringBuffer();
		String result = "";
		char last = line.charAt( line.length() -1 );
		line = line.substring( 0 , line.length() -1 );
		StringTokenizer sti = new StringTokenizer( line );
		
		while( sti.hasMoreTokens() ) {
			
			sb.append( " " );
		    sb.append( sti.nextToken() );
	   }    
		result = sb.toString().trim();
				
		if( last == '.')
			translator.addSentence( result );
		else if ( last == '?')
			output.addQuestion( result );
		else
			System.out.println("Error in addToTranslator");
		
		return;
	}	
/**********************************************************/
 }//class

