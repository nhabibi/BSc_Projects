
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

public class TestCompiler {

	public static void main(String[] args) {

		String p1 ="#include <stdlib.h>main (  { prin(hg) char  a }";
		String p2 = "public class program {String s;   }";
        String p3=" #include <stdlib.h> #include <stdio.h>  int main() {  int a; return (0); } ";
        String p4="class test {}";

	try{
        Compiler c = new Compiler(p3,"c");
    }
    catch (Exception e) {
        System.out.println(e.getMessage());
        System.exit(1);
    }

	}
}
