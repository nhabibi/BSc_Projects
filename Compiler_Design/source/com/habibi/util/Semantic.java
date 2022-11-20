package com.habibi.util;

import java.util.Hashtable;

/** This class is Symbol_Table of compiler.
 *   has methods for add variable, search a specified variable
 */
public class Semantic{
    /** for create alias
     */
	private int counterVariableInt=0;
     /** for create alias
      */   
	private int counterVariableChar=0;
    /** for create alias
     */    
        private int counterVariableFloat=0;
        /** The data structure of symbol_table
         */ 
	private Hashtable symbolTable=new Hashtable();
	/** Adds a new variable to symbol_table
         */
	public void add(Variable variable){
		
		String alias;
		//if (variable.getSize()==-1)
			if (variable.getType()==1)
				alias="_variableChar"+counterVariableChar++;
			else if (variable.getType()==0)
				alias="_variableInt"+counterVariableInt++;
                        else
                                alias="_variableFloat"+counterVariableFloat++;

		variable.setAlias(alias);
		symbolTable.put(variable.getName(),variable);
	}
        /** checks if variable is defined befor or not
         */ 
	public boolean isDefined(String name){
		return symbolTable.get(name)!=null;
	}
//	public int getSize(String name){
//		return ((Variable) symbolTable.get(name)).getSize();
//	}
        /** return alias of variable
         */
	public String getAlias(String variableName){
		if (!isDefined(variableName))
			return variableName;
		return ((Variable)symbolTable.get(variableName)).getAlias();
	}
	public Variable getInformation(String variable){
		return (Variable) symbolTable.get(variable);
	}
}

