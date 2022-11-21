import java.util.Vector;
import java.util.StringTokenizer;


class Output {
	
	private boolean isSuppress;
	public Vector questions;
	private Vector answers;
	
   public Output( boolean isSuppress ) {//constructor
		
		this.isSuppress = isSuppress;
		questions = new Vector();
		answers = new Vector();
		
   }
/*****************************************************************************/
   
   //for add element to Question Vector by other class
   public void addQuestion(String que){
	    questions.addElement( que );
   }
   
/*****************************************************************************/   
   //pars Question & find answers, then add them to answers Vector 
   public void parseQuestions( Vector solve, String msg ) {
   	
   	  if( !msg.equals("") ){
   	  	answers.addElement( msg );
   	  	return;
   	  }
   	  
	  StringTokenizer sti;
      String nextToken = "";
	  String name = "";
	  String color = "";
	  String occupation = "";
		
	  for (int i=0; i < questions.size() ; ++i) {
			
		sti = new StringTokenizer( (String) questions.elementAt(i));
		nextToken = sti.nextToken();
		name = color = occupation = "";
		
		if ( nextToken.equals("Where") ) {
			nextToken = sti.nextToken();//does
			nextToken = sti.nextToken(); // the | name
			if ( nextToken.equals("the") ){
				occupation = sti.nextToken();
				int w;
				for( w = 0; w < solve.size(); w++ ){
					String[] str = (String[])solve.elementAt(w);
					if( str[ 2 ].equals( occupation )){//if there is solved
						if( !str[ 1 ].equals("") )
							answers.addElement( "The " + occupation + " lives in the " + str[ 1 ] + " house.");
						else
							answers.addElement("I don't know.");
						break;
					}
				}
				if( w == solve.size())
					answers.addElement("I don't know.");
				
			}
			else {//name
				name = nextToken;
				int n;
				for( n = 0; n < solve.size(); n++ ){
					String[] str = (String[])solve.elementAt(n);
					if( str[ 0 ].equals( name )){ //if there is solved
						if( !str[ 1 ].equals("") )
							answers.addElement( name + " lives in the " + str[ 1 ] + " house.");
						else
							answers.addElement("I don't know.");
						break;
					}
				}
				if( n == solve.size())
					answers.addElement("I don't know.");
				
				}
				
			}
		
		else if ( nextToken.equals("Who") ) {
			nextToken = sti.nextToken(); //is | lives
			if ( nextToken.equals("is") ) {
				sti.nextToken(); //the
				occupation = sti.nextToken();
				int w;
				for( w = 0; w < solve.size(); w++ ){
					String[] str = (String[])solve.elementAt(w);
					if( str[ 2 ].equals( occupation )){
						if( !str[ 0 ].equals(""))
							answers.addElement(str[ 0 ] + " is the " + occupation + "." );
						else 
							answers.addElement("I don't know.");
						break;
					}
				}
				if( w == solve.size())
					answers.addElement("I don't know.");
			}
			else {//lives
				sti.nextToken(); //in
				sti.nextToken(); //the
				color = sti.nextToken(); //color
				int c;
				for( c = 0; c < solve.size(); c++ ){
					String[] str = (String[])solve.elementAt(c);
					if( str[ 1 ].equals( color )){
						if( !str[ 0 ].equals(""))
							answers.addElement( str[ 0 ] + " lives in the " + color + " house.");
						else 
							answers.addElement("I don't know.");
						break;
					}
				}
				if( c == solve.size())
					answers.addElement("I don't know.");
			}
		}
		
		else { //What
			nextToken = sti.nextToken(); // do | dose
			if ( nextToken.equals("dose") ) {
				nextToken = sti.nextToken(); //name | the
				if ( nextToken.equals("the") ) {
					sti.nextToken(); //occupant
					sti.nextToken(); //of
					sti.nextToken(); //the
					color = sti.nextToken(); //color
					int c;
					for( c = 0; c < solve.size(); c++ ){
						String[] str = (String[])solve.elementAt(c);
						if( str[ 1 ].equals( color )){
							if( !str[ 2 ].equals(""))
								answers.addElement( "The " + str[ 2 ] + " lives in the " + color + " house.");
							else 
								answers.addElement("I don't know.");
							break;
						}
					}
					if( c == solve.size())
						answers.addElement("I don't know.");
				}
				else {//name
					name = nextToken;
					int n;
					for( n = 0; n < solve.size(); n++ ){
						String[] str = (String[])solve.elementAt(n);
						if( str[ 0 ].equals( name ) ){
							if( !str[ 2 ].equals("") )
								answers.addElement( name + " is the " + str[ 2 ] + ".");
							else
								answers.addElement("I don't know.");
						}
					}
					if( n == solve.size() )
						answers.addElement("I don't know.");
						
				}
			}
			else { //do
				sti.nextToken(); //you
				sti.nextToken(); //know
				sti.nextToken(); //about
				nextToken = sti.nextToken(); //the | name
				if ( nextToken.equals("the") ) {
					if ( sti.countTokens() == 2 ) { //color house
						color = sti.nextToken();
						int c;
						for( c = 0; c < solve.size(); c++ ){
							String[] str = (String[])solve.elementAt(c);
							if( str[ 1 ].equals( color )){
								if( !str[ 0 ].equals("") && !str[ 2 ].equals(""))
									answers.addElement( str[ 0 ] + " is the " + str[ 2 ] + " and lives in the " + color + " house.");
								else	if( !str[ 0 ].equals(""))
									answers.addElement( str[ 0 ] + " lives in the " + color + " house.");
								else	if( !str[ 2 ].equals(""))
									answers.addElement( "The" + str[ 2 ] + " lives in the " + color + " house.");
								else
									answers.addElement("Nothing.");
								break;
							}
						}
						if( c == solve.size())
							answers.addElement("Nothing.");
					}
					else {
						occupation = sti.nextToken();
						int w;
						for( w = 0; w < solve.size(); w++ ){
							String[] str = (String[])solve.elementAt(w);
							if( str[ 2 ].equals(occupation)){
								if( !str[ 0 ].equals("") && !str[ 1 ].equals(""))
									answers.addElement( str[ 0 ] + " is the " + occupation + " and lives in the " + str[ 1 ] + " house.");
								else	if( !str[ 0 ].equals(""))
									answers.addElement( str[ 0 ] + " is the " + occupation + "." );
								else	if( !str[ 1 ].equals(""))
									answers.addElement( "The" + occupation + " lives in the " + str[ 1 ] + " house.");
								else
									answers.addElement("Nothing.");
								break;
							}
						}
						if ( w == solve.size())
							answers.addElement("Nothing.");
							
					}
				}
				else { //name
					name = nextToken;
					int n;
					for( n = 0; n < solve.size(); n++ ){
						String[] str = (String[])solve.elementAt(n);
						if( str[ 0 ].equals( name )){
							if( !str[ 1 ].equals("") && !str[ 2 ].equals(""))
								answers.addElement( name + " is the " + str[ 2 ] + " and lives in the " + str[ 1 ] + " house.");
							else	if( !str[ 1 ].equals("") )
								answers.addElement( name + " lives in the " + str[ 1 ] + " house.");
							else	if( !str[ 2 ].equals("") )
								answers.addElement( name + " is the " + str[ 2 ] + ".");
							else
								answers.addElement("Nothing.");
							break;
						}
					}
					if( n == solve.size())
						answers.addElement("Nothing.");
						
				}
			}
		}
	}//for
   	
   }

/*************************************************************************************/
   
   //for print answer, according to entry order
   public void printAnswer(){
       
      if ( isSuppress == true ) {
   	  for( int i = 0; i < answers.size(); i++ ) 
              System.out.println( ( String ) answers.elementAt(i));
       }
       else {
           if ( answers.size() < questions.size() ) { //impossible
              for( int i = 0; i < questions.size(); i++ ) 
                System.out.println( "Q: " + ( String ) questions.elementAt(i) + "?");
              	System.out.println( answers.elementAt(0));
           }
           else {
                for( int i = 0; i < answers.size(); i++ ) {
                   System.out.println( "Q: " + ( String ) questions.elementAt(i) + "?"); 
                   System.out.println( "A: " + ( String ) answers.elementAt(i)); 
                }//for   
           }//else
       }//else
   }

/**************************************************************************/
}//class
