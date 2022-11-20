package com.habibi.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import com.habibi.compiler.sym;

/** This class create Nodes of "syntax tree".
 */
public class Node implements NodeInterface{
/** this field holds pointer childs of node
 **/       	
        private ArrayList childs=new ArrayList(1);
 /** this field holds value of node       
  */
	private Object root;
	
	public Node(){
	}
	public Node(Object root)
	{
		this.root = root;
	}
	public Node(int root)
	{
		this.root = new Integer(root);
	}
	public void setChilds(List childs)
	{
		this.childs = new ArrayList(childs);
	}
        /** returns value of node
         */
	public int getIntValueOfRoot(){
		if(root instanceof Integer)
			return ((Integer)root).intValue();
		if (root instanceof String)
			return sym.IDENTIFIER;
		return -1;
	}
	protected ArrayList getChilds()
	{
		return childs;
	}
	public void setRoot(Object root)
	{
		this.root = root;
	}
	
	public Object getRoot()
	{
		return root;
	}
	
	public Iterator getChildrenIterator()
	{
		return getChilds().iterator();
	}
	
	public void addChild(NodeInterface o)
	{
		getChilds().add(o);
	}
	
	public boolean hasChildren()
	{
		return !getChilds().isEmpty();
	}
/** returns child at specified index
 **/ 	
	public NodeInterface getChildAt(int index)
	{
		return (NodeInterface) getChilds().get(index);
	}
	
	public String toString(){
		return "NODE";
	}
//	public  void printLevelOrder(){
//		Queue queue=new Queue();
//		queue.insert(this);
//		System.out.println(getRoot());
//		while (!queue.isEmpty()){
//			NodeInterface node=(NodeInterface) queue.pop();
//			Object object=node.getRoot();
//			if (node.hasChildren()){
//				Iterator childs=node.getChildrenIterator();
//				while (childs.hasNext()){
//					Object child=childs.next();
//					queue.insert(child);
//					System.out.print(object+":"+((NodeInterface) child).getRoot()+"\t");
//				}
//				System.out.println();
//			}
//		}
//	}
  /** Prints syntax tree in Level_order
   */      
        public  void printLevelOrder(){
		Queue queue=new Queue();
		queue.insert(this);
		System.out.println("\n" +find_name(getRoot()) +"\n" );
		while (!queue.isEmpty()){
			NodeInterface node=(NodeInterface) queue.pop();
			Object object=node.getRoot();
			if (node.hasChildren()){
				Iterator childs=node.getChildrenIterator();
				while (childs.hasNext()){
					Object child=childs.next();
					queue.insert(child);
					System.out.print( find_name(object) +":" +find_name(((NodeInterface) child).getRoot()) +"    ");
				}
				System.out.println("\n");
			}
		}
	}
	public void printPreOrder(){
		Stack s=new Stack();
		
		s.push(this);
		while (!s.isEmpty()){
			NodeInterface root=(NodeInterface) s.pop();
			System.out.println(root.getRoot());
			
			Iterator it=root.getChildrenIterator();
			while (it.hasNext())
				s.push(it.next());
				
		}
						 
	}
        private String find_name(Object obj){
              //System.out.println("Entered in find_name");
              int number = Integer.parseInt(obj.toString());
              switch (number){
                  case 62:  return "IF_ELSE_STAT";
                  case 53:  return "INT_LIST";
                  case 42:  return "/=";//DIVEQ;
                  case 3 :  return "printf";
                  case 50:  return "STRING_LITERAL";
                  case 36:  return ">=";//GTEQ ;
                  case 10:  return "*";//MULT;
                  case 2 :  return "main" ;
                  case 7 :  return "CHAR" ;
                  case 35:  return "<=";//LTEQ ;
                  case 43:  return "%=";//MODEQ ;
                  case 15:  return "(";//LPAREN ;
                  case 6 :  return "INT" ;
                  case 66:  return "IN_LIST";
                  case 59:  return "FOR_STAT" ;
                  case 22:  return "FOR" ;
                  case 28:  return "-";//MINUS;
                  case 16:  return ")";//RPAREN ;
                  case 48:  return "CHARACTER_LITERAL" ;
                  case 39:  return "&&";//AND ;
                  case 30:  return "!";//NOT ;
                  case 9 :  return ";";//SEMICOLON;
                  case 33:  return "<";//LT ;
                  case 68:  return "C_LIST" ;
                  case 40:  return "||";//OR ;
                  case 29:  return "-";//UNARYMINUS ;
                  case 52:  return "DCL_PART";
                  case 11:  return ",";//COMMA ;
                  case 55:  return "FLOAT_LIST";
                  case 23:  return "++";//PREPLUSPLUS ;
                  case 31:  return "/";//DIV ;
                  case 44:  return "+=";//PLUSEQ ;
                  case 18:  return "&";//NUMBERSIGN;
                  case 65:  return "SCANF_STAT";
                  case 45:  return "-=";//MINUSEQ;
                  case 27:  return "+";//PLUS ;
                  case 19:  return "IF" ;
                  case 54:  return "CHAR_LIST";
                  case 69:  return "I_LIST";
                  case 0 :  return "EOF";
                  case 58:  return "WHILE_STAT" ;
                  case 64:  return "OUT_LIST";
                  case 1 :  return "error" ;
                  case 61:  return "IF_STAT";
                  case 37:  return "==";//EQEQ ;
                  case 41:  return "*=";//MULTEQ ;
                  case 32:  return "%";//MOD ;
                  case 14:  return "=";//EQ ;
                  case 26:  return "--";//POSTMINUSMINUS ;
                  case 24:  return "++";//POSTPLUSPLUS ;
                  case 60:  return "GOTO_STAT" ;
                  case 17:  return ":";//COLON;
                  case 47:  return "FLOATING_POINT_LITERAL" ;
                  case 12:  return "{";//LBRACE ;
                  case 20:  return "ELSE" ;
                  case 56:  return "EXECUTION_PART" ;
                  case 5 :  return "GOTO" ;
                  case 63:  return "PRINTF_STAT";
                  case 21:  return "WHILE" ;
                  case 8 :  return "FLOAT";
                  case 13:  return "}";//RBRACE ;
                  case 51:  return "BODY" ;
                  case 67:  return "F_LIST" ;
                  case 25:  return "--";//PREMINUSMINUS;
                  case 57:  return "LABELED_STAT";
                  case 34:  return ">";//GT;
                  case 4 :  return "scanf" ;
                  case 38:  return "!=";//NOTEQ ;
                  case 49:  return "IDENTIFIER" ;
                  case 46:  return "INTEGER_LITERAL" ;
                  default:  return "Unkwon" ;
             }//switch

 
        }    
}

