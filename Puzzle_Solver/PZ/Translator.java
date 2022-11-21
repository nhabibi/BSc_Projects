
//This class, saves parses all sentences and save result in PuzzleSolver class.

import java.util.Vector;
import java.util.StringTokenizer;

class Translator {
	
	private Vector sentences;
	private PuzzleSolver puzzleSolver;
	private Output output;
	
	public Translator(Output output){
		
		sentences = new Vector();
		puzzleSolver = new PuzzleSolver();
                this.output = output;
	}
/***************************************************************/
	public void addSentence(String sen){
		
		sentences.addElement( sen );
	}
/***************************************************************/
//For test	
    public void printSentences(){
    	
    	for (int i =0 ; i<sentences.size();++i )
    		System.out.println( (String) sentences.elementAt(i) + ".");
		System.out.println();
    }
	
/***************************************************************/
	public void solvePuzzle() {
		
		parseSentences();
		puzzleSolver.cubeSolve();
		puzzleSolver.solving();
		output.parseQuestions( puzzleSolver.getSolve(), puzzleSolver.getMsg());
	}
/***************************************************************/
/* This method, parses each sentence according to given BNF grammer and 
 * fills 5 data structurs in PuzzleSolver class :
 * 1- a vector of person names
 * 2- a vector of works
 * 3- a vector of house colors  
 * 4- a vector of String array of  "X is X"  relations.
 * 5- a vector of String array of  "X is not X"  relations.
 */	
	private void parseSentences() {
		
		StringTokenizer sti;
		String nextToken = "";
		String name = "";
		String color = "";
		String occupation = "";
		
		for (int i=0; i < sentences.size() ; ++i) {
			
			sti = new StringTokenizer( (String) sentences.elementAt(i));
			nextToken = sti.nextToken();
			name = color = occupation = "";
			
			if ( nextToken.equals("There") ) { //There is a Color house.
				sti.nextToken(); //is
				sti.nextToken(); //a
				color = sti.nextToken(); //color
				puzzleSolver.addColor( color );
				sti.nextToken();//house
			}
			
			else if ( nextToken.equals("The") ) { //The Occupation ...
				occupation = sti.nextToken(); //occupation
				puzzleSolver.addWork( occupation );
				nextToken = sti.nextToken(); //lives | does
				if ( nextToken.equals("lives") ) {
					nextToken = sti.nextToken(); //in | around
					if ( nextToken.equals("in") ) {
						sti.nextToken();//the
						color = sti.nextToken();
						puzzleSolver.addColor( color );
						puzzleSolver.addEquals( new String[]{"" , color , occupation} );
					}
					else {//around here
						
						//nothing
					}	
				}		
				else {//dose
					sti.nextToken();//not
					sti.nextToken();//live
					sti.nextToken();//in
					sti.nextToken();//the
					color = sti.nextToken();
					puzzleSolver.addColor( color );
					puzzleSolver.addnEquals( new String[]{"" , color , occupation });
				}
				
				if ( color.equals(occupation) ) {
					System.out.println("color == occupation is illegal.");
					System.exit(0);
				}
			}
			
			else { //name 
					
				name = nextToken;
				puzzleSolver.addName( name );
				nextToken = sti.nextToken();
				if ( nextToken.equals("lives") ){
					nextToken = sti.nextToken(); //in | around
					if ( nextToken.equals("in")) {
						sti.nextToken(); //the
						color = sti.nextToken(); //color
						puzzleSolver.addColor( color );
						puzzleSolver.addEquals( new String[]{name , color , ""} );
					}
					else { //around here
						//nothing
					}
				}
				else if ( nextToken.equals("is")) {
					nextToken = sti.nextToken(); //the | not
					if ( nextToken.equals("the")) {
						 occupation = sti.nextToken();
						 puzzleSolver.addWork( occupation );
						 puzzleSolver.addEquals( new String[]{name , "" , occupation });
						 
					}
					else {//not
						sti.nextToken(); //the
						occupation = sti.nextToken();
						puzzleSolver.addWork( occupation );
						puzzleSolver.addnEquals( new String[]{ name , "" , occupation });
					}
				}	
				else {//dose
					sti.nextToken(); //not
					sti.nextToken(); //lives
					sti.nextToken(); //in
					sti.nextToken(); //the
					color = sti.nextToken();
					puzzleSolver.addColor( color );
					puzzleSolver.addnEquals( new String[]{ name , color , ""} );
				}
				
				if ( name.equals(color)  || name.equals(occupation) ) {
					System.out.println("name == occupation or name == color,is illegal.");
					System.exit(0);
				}
			}
			
	   }//for
	}
/***************************************************************/
 }//class

