
//This class parses the command arguments,create instance of Input,Translator and output and initializes them.

import java.util.Vector;

public class Solve {

	private boolean isSuppress = false;
	private Vector fileNames ;
	private Translator translator;
	
	public Solve() {
		
		fileNames = new Vector();
		System.out.println();
	}
/**********************************************************************/	
	public void start(String[] args) {
		
		processCommandArgs( args );
		
        Output output = new Output( isSuppress );
        
		translator = new Translator( output );
                
        Input input = new Input( fileNames , translator,  output );
		
		translator.printSentences();
		
		translator.solvePuzzle();
				
		output.printAnswer();
		
	}
/**********************************************************************/
/*This method parses the command arguments.
If -h exsits in arguments ignores all others and prints a usage message,
else, if there is -q, sets the output mode to "suppress".
It assumes any thing else "Input file" and saves it in Input class 
*/	
	private void  processCommandArgs(String[] args) {
		
		for (int i =0; i< args.length; ++i)
			
		     if ( args[i].equals("-q") )
				//set mode to suppressing
				isSuppress = true;
		     
			 else if ( args[i].equals("-h") || args[i].startsWith("-") ){
				printHelpMess();
			    System.exit(0);
			}
			//it assumed that it is a file name 
			else
				fileNames.addElement( args[i] );
   }
/**********************************************************************/
	private void printHelpMess() {
		
		System.out.println("\nThis program is a general-purpose solver for a specific class of puzzles." +
				           "\n\nUsage: java Solve [-options] [FILE...]" +
				           "\n\nwhere options include: " +
				           "\n    -h      prints this message" +
				           "\n    -q      suppresses the output of program" +
				           "\n\n[FILE ...]  are file names that input will be read from." +
				           "If you don't specify files,input will be read from standard input." );
	}
	
/**********************************************************************/	
	public static void main(String[] args) {
		
		Solve solve = new Solve();
	 
		solve.start(args);
	}
}//class
