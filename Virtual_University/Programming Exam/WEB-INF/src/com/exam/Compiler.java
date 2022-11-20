/*
 * Created on Jul 17, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author narges
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exam;

import java.io.*;
import java.util.Random;

public class Compiler {
	
    private Runtime rt;
    private int type;
    private FileWriter fw;
    private String tu;
    public String compilerMessage;
    int path;

	public Compiler(){}
	public Compiler(String p,String t) throws Exception
	{
	  System.out.println(p);
      System.out.println(t);
      if ( p.equals("") ) {
        System.out.println("Please wirte a program!");
        compilerMessage = "Please wirte a program!";
        //System.exit(0);
      }
      else{
    	try{
            Random r = new Random();
            path = r.nextInt(50);
            //System.out.println(path);

			tu=t.toUpperCase();
            if 	( tu.equals("JAVA") ) {

				type=0;
				File file=new File("/tmp/program" + path + ".java");
                fw = new FileWriter(file);
            }
			else if ( tu.equals("C") ) {

                type=1;
				fw = new FileWriter("/tmp/program" + path + ".C");
			}
            else if (tu.equals("C++") ) {
                type =2;
                fw = new FileWriter("/tmp/program" + path + ".cc");
            }
            else
            {
 // if type not supported, should display this...
                 compilerMessage=new String("Please select a supported programing language.");
                 throw new Exception("Unexpected programming language");
            }
			fw.write(p);
			fw.close();
            doCompile();


	    }//try
		
		catch(Exception e){

		  System.out.println(e.getMessage());
		}
      }
   }
	
  public void doCompile() {

    rt = Runtime.getRuntime();
    String[] command;
    InputStream err;
    StringBuffer sb = new StringBuffer();
    int[] error = new int[1000];
    int size = 0 , i =0 ;

    if ( type == 0)
    	command = new String[]{"javac", "/tmp/program" + path + ".java"};
    else if  (type ==1)
        command = new String[]{"gcc", "/tmp/program" + path + ".C"};
    else //if (type == 2)
        command = new String[]{"gcc", "/tmp/program" + path + ".cc"};


    try{
        Process child =rt.exec(command);
        err = child.getErrorStream();
        while ( (error[i] = err.read() )  !=  -1  ) {
              ++i;
              ++size;
        }
        err.close();
        if( size == 0)
		{
			System.out.println("Congradulation!");
			compilerMessage ="Congradulation!";

		}
		else
        {
			System.out.println("Program has error.");
            for ( i=0; i < size ;i++)
            {
				sb.append ( (char) error[i] );
            }
            //if (!sb.toString().matches("error"))
            //    sb.append("\n Congradulation! You have no errors, Can you remove  warnings too?");

        }
        if ( size != 0 ) {

           if (type==0)
           {                //for future extension
              compilerMessage= colorizeJava(sb);
           }
           else if (type==1 || type==2)
           {
              compilerMessage= colorizeC(sb);
           }
        }
	}//try

	catch(Exception a){
		System.out.println("Exception" + a.getMessage() );
		//compilerMessage= new String ("ERROR IN COMpILING ") ;
        compilerMessage = a.getMessage();
	}
  }
/*****************************************************************************************************/
    private String colorizeC(StringBuffer sb)
        {
            int i=0;
            try
            {
            while (i<sb.length())
            {

                if (sb.charAt(i)=='`')
                    //sb.append(colorizeText(sb,i));
                {
                 sb.insert(i ,"<font color=\"#0000FF\">");
                 i=i+  "<font color=\"#0000FF\">".length()+1;
                 while (sb.charAt(i)!='\'')
                     i++;
                 i++;
                 sb.insert(i,"</font>");

                }


                if (sb.charAt(i)=='\n')
                {
                    sb.deleteCharAt(i);
                    sb.insert(i,"<br>");
                }

                if ( i+8<sb.length() && sb.substring(i,i+8).equalsIgnoreCase(": error:"))
                {
                    sb.insert(i+8 ,"<font color=\"#FF0000\">");
                    while (sb.charAt(i)!='\n')
                        if(sb.charAt(i)=='`')
                            {
                            sb.insert(i,"</font>");
                            sb.insert(i+7 ,"<font color=\"#0000FF\">");
                                         i=i+ 7+ "<font color=\"#0000FF\">".length()+1;
                                         while (sb.charAt(i)!='\'')
                                             i++;
                                         i++;
                                         sb.insert(i,"</font>");
                                         sb.insert(i+7 ,"<font color=\"#FF0000\">");
                                         i=i+"<font color=\"#FF0000\">".length();
                            }
                        else
                            i++;
                    sb.insert(i,"</font>");
                }

                if ( i+10<sb.length() && sb.substring(i,i+10).equalsIgnoreCase(": warning:"))
                {
                    sb.insert(i+10 ,"<font color=\"#800080\">");
                    while (sb.charAt(i)!='\n'){
                        if(sb.charAt(i)=='\'')
                         {
                             sb.insert(i,"</font>");
                             sb.insert(i+7 ,"<font color=\"#0000FF\">");
                             i=i+ 7+ "<font color=\"#0000FF\">".length()+1;
                             while (sb.charAt(i)!='\'')
                                  i++;
                             i++;
                             sb.insert(i,"</font>");
                             sb.insert(i+7 ,"<font color=\"#FF0000\">");
                             i=i+"<font color=\"#FF0000\">".length();
                         }
                        else
                            i++;
                    }
                    sb.insert(i,"</font>");

                }
                i++;

            }
            }
            catch (Exception e)
            {
               System.out.println(e.getMessage());
            }
            return sb.toString();
        }

/*****************************************************************************************************/
       private String colorizeJava(StringBuffer sb)
       {
           int i=0;
            try
            {
                while (i<sb.length())
                {
                    if(sb.charAt(i)=='\n'){
                        sb.deleteCharAt(i);
                        sb.insert(i,"<br>");
                    }

                    i++;

                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }


            return sb.toString();
       }



}
	
 
	
