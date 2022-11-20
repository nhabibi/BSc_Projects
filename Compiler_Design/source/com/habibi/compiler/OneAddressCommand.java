package com.habibi.compiler;

/** This class Contains parts of one command in virtual machine code.
 **/
public class OneAddressCommand{
 /** If command is a labeld_command , this field is set to generated label 
  * for it.
  */   
	private String label;
 /** Contains operator part of command.
  */       
	private String part1;
 /** contain operand part of command if it has.
  */       
	private String part2;
	
	public OneAddressCommand(String label, String part1, String part2)
	{
		this.label = label;
		this.part1 = part1;
		this.part2 = part2;
	}
	
	public OneAddressCommand(String part1, String part2)
	{
		this.part1 = part1;
		this.part2 = part2;
	}
	
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
	
	public void setPart1(String part1){
		this.part1 = part1;
	}
	
	public String getPart1(){
		return part1;
	}
	
	public void setPart2(String part2){
		this.part2 = part2;
	}
	
	public String getPart2(){
		return part2;
	}
/** for print command ,it method is called.
 */	
	public String toString(){
		String s = "";
		int size = 0;
		if(getLabel()!= null){
			s = getLabel();
			size = s.length();
		}
		for (int i = 10; i > size; i--){
			s = s+" ";
		}
		s+=getPart1();
		for (int i = 10; i > getPart1().length(); i--){
			s = s+" ";
		}
		s+=getPart2();
		return s;
	}
}

