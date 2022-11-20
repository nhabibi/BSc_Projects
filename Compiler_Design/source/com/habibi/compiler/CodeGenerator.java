/*
 * CodeGenerator.java
 *
 * Created on November 30, 2004, 8:29 PM
 */

package com.habibi.compiler;

import com.habibi.util.NodeInterface;
import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import com.habibi.compiler.sym;
import com.habibi.compiler.*;

/** This class generate intermediate code from syntax tree
 *  and returns an ArrayList from OneAddressCommnadss.
 * @author  Narges Habibi
 */
public class CodeGenerator {
    
    
    /** generated code returned via this field;
     */     
        private  ArrayList commands = new ArrayList();
	private  int label=0;
   /** Holds a refrence to Symbol_Table
    */     
	private Semantic symbolTable;
	
	private void setSymbolTable(Semantic symbolTable){
		this.symbolTable = symbolTable;
	}
	
	protected Semantic getSymbolTable(){
		return symbolTable;
	}
	public String getAliasFromSymbolTable(String name){
		return getSymbolTable().getAlias(name);
	}
	public CodeGenerator(Semantic symbolTable){
		setSymbolTable(symbolTable);
	}
	/** generates next label for use in commans
         **/
	public String generateLabel(){
		return"_LABEL"+label++ ;
	}
	public void setCommands(ArrayList commands){
		this.commands = commands;
	}
	public ArrayList getCommands(){
		return commands;
	}
	/** Adds next command to ArrayList
         */
	public void addCommand(String part1,String part2){
		OneAddressCommand command=new OneAddressCommand(part1,part2);
		getCommands().add(command);
	}
	public void addCommand(String label,String part1,String part2){
		OneAddressCommand command=new OneAddressCommand(label,part1,part2);
		getCommands().add(command);
	}
	/** Calls Parser ,recieves syntax tree from it and Generate
         * intermmediate codes and returns them to MainFrame
         */  
	public ArrayList generateCode(String source){
		try{
			
			parser p = new parser(new Scanner(new CharArrayReader(source.toCharArray())));
			NodeInterface root = (NodeInterface ) p.parse().value;
//               	System.out.println(root.getChildAt(0).getRoot().toString());
                 	root.printLevelOrder();
			generateBody(root);
			addCommand("STOP","");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getCommands();
	}
        
        public void generateBody(NodeInterface node){
            
            if (node.getIntValueOfRoot()==sym.BODY)
		generateExecution_part( ((NodeInterface)node).getChildAt(1) ); //shak
		
	   else  
		System.out.println("BODY expected.");
        }
//////////////////////////////////////////////////////////////////////////////        
        public void generateExecution_part(NodeInterface node){
            
            if (node.getIntValueOfRoot()==sym.EXECUTION_PART){
                	Iterator iter = node.getChildrenIterator();
			while (iter.hasNext()){
				NodeInterface element =(NodeInterface) iter.next();
				generateStatement(element);
			}
	   }
            else  
			System.out.println("EXECUTION_PART expected.");
        }
        
//////////////////////////////////////////////////////////////////////////////
        public void generateStatement(NodeInterface node) {
            
            int type = node.getIntValueOfRoot();
            switch (type) {
                
                case sym.LABELED_STAT   :    generateLabeld_stat(node);  break;
                case sym.FOR_STAT       :    generateFor(node);          break;
                case sym.GOTO           :    generateGoto(node);         break;
                case sym.IF_STAT        :    generateIf(node);           break;
                case sym.IF_ELSE_STAT   :    generateIf_Else(node);      break;
                case sym.SCANF_STAT     :    generateScanf(node);        break;
                case sym.PRINTF_STAT    :    generatePrintf(node);       break;
                case sym.WHILE_STAT     :    generateWhile(node);        break;
                case sym.ASSIGN_STAT    :    generateAssign_stat(node);  break;
                default                 :    System.out.println("Unknown statement.");
          }
        }
///////////////////////////////////////////////////////////////////////////////
        
     private void generateLabeld_stat(NodeInterface node){
          String label = generateLabel(); //It is wrong!!!
          //OneAddressCommand command = new  OneAddressCommand(label+":" , "" , "");
          addCommand(label+":" ,"" ,"");
          generateStatement( ((NodeInterface)node).getChildAt(1) );
     }
 //////////////////////////////////////////////////////////////////////////////    
     private void generateFor(NodeInterface node){
         
         String codition = genetareLabel();
         String  afterfor = generateLabel();
         generateAssign_stat(((NodeInterface)node).getChildAt(0));
         addCommand(condition+":" ,"" ,"");
         generateLogicExp(((NodeInterface)node).getChildAt(1));
         addCommand("POP","ACC1");
         addCommand("AC1CMP","0");
         addCommand("JEQ",afterfor);
         generateExecution_part(((NodeInterface)node).getChildAt(3));
         int part3 = ((NodeInterface)node).getChildAt(2).getIntValueOfRoot();
         if ( part3 == sym.ASSIGN_STAT )
             generateAssign_stat(((NodeInterface)node).getChildAt(2));
         else      
             generateUnary();
         addCommand("JMP",condition);
         addCommand(afterfor+":" , "" , "");         
     }
///////////////////////////////////////////////////////////////////////////////     
     private void generateGoto(NodeInterface node){
         
          String label = generateLabel(); //It is wrong!!!
          //OneAddressCommand command = new  OneAddressCommand("JMP" , label);
          addCommand("JMP" , label);
     }
 //////////////////////////////////////////////////////////////////////////////    
     private void generateIf(NodeInterface node){
     
                generateLogicExp(node.getChildAt(0));
		addCommand("POP","ACC1");
		addCommand("AC1CMP","0");
		String label= generateLabel();
		addCommand("JEQ",label);
		generateStatement(node.getChildAt(1));
		addCommand(label+":","","");
     }
//////////////////////////////////////////////////////////////////////////////     
     private void generateIf_Else(NodeInterface node){
         
                generatelogicExp(node.getChildAt(0));
		addCommand("POP","ACC1");
		addCommand("AC1CMP","0");
		String label= generateLabel();
		String label2= generateLabel();
		addCommand("JEQ",label);
		generateStatement(node.getChildAt(1));
		addCommand("JMP",label2);
		addCommand(label+":","","");
		generateStatement(node.getChildAt(2));
		addCommand(label2+":","","");
     }
 ///////////////////////////////////////////////////////////////////////////////    
     private void generateScanf(NodeInterface node){
         
         Iterator iter = node.getChildrenIterator();
		while (iter.hasNext()){
	              NodeInterface element =(NodeInterface) iter.next();
		      //OneAddressCommand command = new  OneAddressCommand("SCANF" ,getAliasFromSymbolTable(node.getChildAt(0).getRoot().toString()) );
                      addCommand("SCANF",getAliasFromSymbolTable(node.getChildAt(0).getRoot().toString()) ); 
	        }
     }
 ///////////////////////////////////////////////////////////////////////////////    
     private void generatePrintf(NodeInterface node){
         
         Iterator iter = node.getChildrenIterator();
		while (iter.hasNext()){
	              NodeInterface element =(NodeInterface) iter.next();
		      //OneAddressCommand command = new  OneAddressCommand("SCANF" ,getAliasFromSymbolTable(node.getChildAt(0).getRoot().toString()) );
                      addCommand("PRINTF",getAliasFromSymbolTable(node.getChildAt(0).getRoot().toString()) ); 
               }
        }
 ///////////////////////////////////////////////////////////////////////////////    
     private void generateWhile(NodeInterface node){
         
                String loop=generateLabel();
                String afterwhile=generateLabel();
		addCommand(label+":","","");
		generateLogicExp(node.getChildAt(0));
		addCommand("POP","ACC1");
		addCommand("AC1CMP","1");
		addCommand("JNE",afterwhile);
                generateStatement(node.getChildAt(0));
                addCommand("JMP",loop);
                addCommand(afterwhile , "" ,"");
         
     }
 ///////////////////////////////////////////////////////////////////////////////    
     private void generateAssign_stat(NodeInterface node){
         
         //String id  = getAliasFromSymbolTable(node.getChildAt(0).getRoot().toString()) ; 
         int type = ((NodeInterface)node).getIntValueOfRoot();
         int typeofEQ =  ((NodeInterface)node).getChildAt(1).getIntValueOfRoot();
          generateArithmaticCode( ((NodeInterface)node).getChildAt(1) );
          generateLoad( ((NodeInterface)node).getChildAt(0));
          addCommand("POP" , "ACC2");
                                
         switch (type){
             case sym.EQ       : if (typeofEQ == Sym.STRING_LITERAL )
                                     addCommand("PUSH" , "ACC1");
                                 break;
             case sym.PLUSEQ   : addCommand("ADD" , "ACC1" , "ACC2");
                                 break;
             case sym.MINUSEQ  : addCommand("SUB" , "ACC1" , "ACC2");
                                 break;
             case sym.MULTEQ   : addCommand("MUL" , "ACC1" , "ACC2");
                                 break;
             case sym.DIVEQ    : addCommand("DIV" , "ACC1" , "ACC2");
                                 break;
             case sym.MODEQ    : addCommand("MOD" , "ACC1" , "ACC2");
                                 break;
     }    
     addCommand("POP" , "ACC1");
     generateSave();
   }
///////////////////////////////////////////////////////////////////////////////
     public void generateLoad(NodeInterface node){
		if (node.getRoot() instanceof Character)
			addCommand("ACC1=" ,Integer.toString((int) node.getRoot().toString().charAt(0)));
		else
		addCommand("ACC1=" ,getAliasFromSymbolTable(node.getRoot().toString()));
		addCommand("PUSH","ACC1");
	}
 //////////////////////////////////////////////////////////////////////////////
     public void generateSave(NodeInterface node){
		
			addCommand("POP","ACC1");
			addCommand("ACC1>",getAliasFromSymbolTable(node.getRoot().toString()));
			addCommand("PUSH","ACC1"); //side effect
		
	}
 ////////////////////////////////////////////////////////////////////////////// 
     public void generateArithmaticCode(NodeInterface node){
		generateExecution_part(node.getChildAt(0));
		generateExecution_part(node.getChildAt(1));
		
		addCommand("POP" ,"ACC1");
		addCommand("POP" ,"ACC2");
		
		int type= node.getIntValueOfRoot();
		switch(type){
			case sym.PLUS :
				addCommand("ACC2+","ACC1");
				break;
			case sym.MULT :
				addCommand("ACC2*","ACC1");
				break;
			case sym.DIV :
				addCommand("ACC2/","ACC1");
				break;
			case sym.MINUS:
				addCommand("ACC2-","ACC1");
				break;
                        case sym.MOD:         
                                addCommand("ACC2%","ACC1");
                                break;
		}
		addCommand("PUSH" ,"ACC2");
	}
///////////////////////////////////////////////////////////////////////////////
     public void generateUnary(NodeInterface node){
		generateExecution_part(node.getChildAt(0));
		addCommand("POP" ,"ACC1");
		int type= node.getIntValueOfRoot();
		switch(type){
			case sym.PLUSPLUS :
				addCommand("ACC1+","1");
				///added
				if(node.getChildAt(0).getIntValueOfRoot() == sym.IDENTIFIER)
					addCommand("ACC1>",getAliasFromSymbolTable(node.getChildAt(0).getRoot().toString()));
				else
					addCommand("ACC1>",getAliasFromSymbolTable(node.getChildAt(0).getChildAt(0).getRoot().toString()));
				break;
			case sym.MINUSMINUS:
				addCommand("ACC1-","1");
				if(node.getChildAt(0).getIntValueOfRoot() == sym.IDENTIFIER)
					addCommand("ACC1>",getAliasFromSymbolTable(node.getChildAt(0).getRoot().toString()));
				else
					addCommand("ACC1>",getAliasFromSymbolTable(node.getChildAt(0).getChildAt(0).getRoot().toString()));
				break;
			case sym.UNARYMINUS :
				addCommand("ACC1*","-1");
				break;
		}
		addCommand("PUSH" ,"ACC1");
	}
 ///////////////////////////////////////////////////////////////////////////////
     
}       
         
         
          
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
