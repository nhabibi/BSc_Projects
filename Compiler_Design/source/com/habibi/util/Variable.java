package com.habibi.util;

/** This Class defines objects that enter in Symbol Table.
 *  @author Narges Habibi
 */

class Variable {
    
        /** Name of Variable
         */
	private String name;
        /** Type of Variable 
         *  0 for character
         *  1 for integer
         *  2 float
         */
	private int type; 
        /** Contain value of variable
         */
        private int value;
        /** always contians -1
         */
        private int size; // 	private int value;
	/** The alias name for variable , used by Codegenerator
         */ 
	private String alias;
	public static int INTEGER=0;
	public static int CHARACTER=1;
        public static int FLOAT=2;
	
	/**constructor for this class that makes a variable containing
	*its name, type and size
        */
	public Variable(String name, int type)//, int size)
	{
		this.name = name;
		this.type = type;
	
	}
	
	//sets the name of the variable
	public void setName(String name)
	{
		this.name = name;
	}
	
	
	//gets the name of the variable
	public String getName()
	{
		return name;
	}
	
	//sets the type of the variable
	public void setType(int type)
	{
		this.type = type;
	
	}
	
	//gets the type of the variable
	public int getType()
	{
		return type;
	}
	

	public void setValue(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
	
	
	public void setAlias(String alias)
	{
		this.alias = alias;
	}
	
	public String getAlias()
	{
		return alias;
	}
	public int hashCode(){
		return name.hashCode();
	}
	
}
