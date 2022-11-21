/*
 * Created on Dec 18, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
/**
 * @author c
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
import java.util.Vector;

public class PuzzleSolver {
	private Vector Name;
	private Vector Color;
	private Vector Work;
	private Vector Equals;// name, color, work
	private Vector nEquals;// name, color, work
    private int[][][] Cube;// name, color, work
    private Vector Solve;// name, color, work
    private int maxsize;
    private int[] dummy;
    private String Message;

    PuzzleSolver(){//cunstructor
      Name = new Vector();
      Color = new Vector();
      Work = new Vector();
      Equals = new Vector();
      nEquals = new Vector();
      Solve = new Vector();
      dummy = new int[]{ 0, 0, 0};
      Message = new String( "" );
      
    }

/**********************************************************************/
    
    //methodes for add to vectors by other class
	public void addName( String nm )
	{
		if( !Name.contains(nm) )
			Name.addElement( nm );
	}

/**********************************************************************/
	
	public void addColor( String cl )
	{
		if( !Color.contains(cl))
			Color.addElement( cl );
	}

/**********************************************************************/
	
	public void addWork( String wk )
	{
		if( !Work.contains(wk))
			Work.addElement( wk );
	}
	
/**********************************************************************/	

	public void addEquals( String[] eq )
	{
		Equals.addElement( eq );
	}
	
/**********************************************************************/	

	public void addnEquals( String[] neq )
	{
		nEquals.addElement( neq );
	
	}
	
/**********************************************************************/	

	//methodes for get Solve & Message to other class
	public Vector getSolve(){
		return Solve;
	}

/**********************************************************************/	

	public String getMsg(){
        return Message;
    }
	
/**********************************************************************/	

	//finding the maximum number of elements in Name, Color, Work
	private int max( int n, int c, int w){
      int max = 0;
      if( n > max )
        max = n;
      if( c > max )
        max = c;
      if( w > max )
        max = w;
      return max;

	}
	
/**********************************************************************/	

	//if this String[] is added befor
	private boolean contain( Vector vr, String[] ot){
		for( int i = 0; i < vr.size(); i++ ){
			String[] str =(String[])vr.elementAt(i);
			for( int e =0; e < str.length; e++ )
			 if( str[ e ].equals(ot[ e ]) )
				return true;
		}
		return false;	
	}
	
/**********************************************************************/	

	//for add element to Name or Color or Work that has less element of other
	private void setDummy(){
		int range = maxsize - Name.size();
		for( int n = 0; n < range; n++ ){
			Name.addElement("n" + ( n + 1));
			dummy[ 0 ]++;
		}
		range = maxsize - Color.size();
		for( int c = 0; c < range; c++ ){
			Color.addElement("c" + ( c + 1));
			dummy[ 1 ]++;
		}
		range = maxsize - Work.size();
		for( int w = 0; w < range; w++ ){
			Work.addElement("w" + (w + 1));
			dummy[ 2 ]++;
		}
			
	}

/**********************************************************************/	

	//for remove dummy elements of Name or Color or Work
	private void resetDummy(){
		for( int i = maxsize - 1; i > maxsize - dummy[ 0 ] - 1; i-- )
			for( int j = 0; j < Solve.size(); j++ ){
				String[] str = (String[])Solve.elementAt(j);
				if( str[ 0 ].equals( ( String )Name.elementAt(i) )){
					str[ 0 ] = "";
					Solve.removeElementAt(j);
					Solve.addElement(str);
				}
			}
		for( int i = maxsize - 1; i > maxsize - dummy[ 1 ] - 1; i-- )
			for( int j = 0; j < Solve.size(); j++ ){
				String[] str = (String[])Solve.elementAt(j);
				if( str[ 1 ].equals( ( String )Color.elementAt(i) )){
					str[ 1 ] = "";
					Solve.removeElementAt(j);
					Solve.addElement(str);
				}
			}
		for( int i = maxsize - 1; i > maxsize - dummy[ 2 ] - 1; i-- )
			for( int j = 0; j < Solve.size(); j++ ){
				String[] str = (String[])Solve.elementAt(j);
				if( str[ 2 ].equals( ( String )Work.elementAt(i) )){
					str[ 2 ] = "";
					Solve.removeElementAt(j);
					Solve.addElement(str);
				}
			}
	}
	
/**********************************************************************/	

	//fill Cube with received data by '0' for not equal & '1' for equal
    public void cubeSolve(){
      maxsize = max( Name.size(), Color.size(), Work.size());
      setDummy();

      Cube = new int[ maxsize ][ maxsize ][ maxsize ];

      for( int i = 0; i < maxsize; i++ )
        for( int j = 0; j < maxsize; j++ )
          for( int k = 0; k < maxsize; k++ )
            Cube[ i ][ j ][ k ] = 1;

      for( int i = 0; i < Equals.size(); i++ ){
        String[] eqstr ={"", "", ""};
        eqstr = (String[])(Equals.elementAt(i));
        
        if( eqstr[ 0 ] == "" )
          for( int n = 0; n < maxsize; n++ ){
            for ( int w = 0; w < maxsize; w++ )
              if( w != Work.indexOf(eqstr[2]) )
                Cube[ n ][ Color.indexOf(eqstr[ 1 ]) ][ w ] *= 0;
            for ( int c = 0; c < maxsize; c++ )
               if( c != Color.indexOf(eqstr[1]))
                 Cube[ n ][ c ][ Work.indexOf(eqstr[ 2 ]) ] *= 0;
      
          }
          

        if( eqstr[ 1 ] == "" )
          for( int c = 0; c < maxsize; c++ ){
            for ( int n = 0; n < maxsize; n++ )
               if( n != Name.indexOf(eqstr[ 0 ]))
                 Cube[ n ][ c ][ Work.indexOf(eqstr[ 2 ]) ] *= 0;
             for ( int w = 0; w < maxsize; w++ )
               if( w != Work.indexOf(eqstr[ 2 ]) )
                 Cube[ Name.indexOf(eqstr[ 0 ]) ][ c ][ w ] *= 0;
          }

        if( eqstr[ 2 ] == "" )
          for( int w = 0; w < maxsize; w++ ){
            for ( int n = 0; n < maxsize; n++ )
               if( n != Name.indexOf(eqstr[ 0 ]))
                 Cube[ n ][ Color.indexOf(eqstr[ 1 ]) ][ w ] *= 0;
             for ( int c = 0; c < maxsize; c++ )
                if( c != Color.indexOf(eqstr[ 1 ]))
                  Cube[ Name.indexOf(eqstr[ 0 ]) ][ c ][ w ] *= 0;
          }
      }
      
      for( int i = 0; i < nEquals.size(); i++ ){
		  String[] neqstr = { "", "", "" };
		  neqstr =  (String[])(nEquals.elementAt(i));

		  if( neqstr[ 0 ] == "" )
		  for( int n = 0; n < maxsize; n++ )
		    Cube[ n ] [ Color.indexOf(neqstr[ 1 ])][ Work.indexOf(neqstr[ 2 ])] *= 0;
		
		  if( neqstr[ 1 ] == "" )
		    for( int c = 0; c < maxsize; c++ )
		      Cube[ Name.indexOf(neqstr[ 0 ]) ] [ c ][ Work.indexOf(neqstr[ 2 ])] *= 0;
		
		  if( neqstr[ 2 ] == "" )
			for( int w = 0; w < maxsize; w++ )
			  Cube[ Name.indexOf(neqstr[ 0 ]) ] [ Color.indexOf(neqstr[ 1 ])][ w ] *= 0;


      }
    }

/****************************************************************************************/	

	//find answers of Cube    
    public void solving (){

	    String[] str = new String[ 3 ];
	    boolean bool = true;
	    while( bool && Solve.size() < maxsize ){
	    	bool = false;
		    for(int n = 0; n < maxsize; n++ ){
		    	int count = 0;
		    	for(int w = 0; w < maxsize; w++)
			  	 	for(int c = 0; c < maxsize; c++)
			  			if( Cube[ n ][ c ][ w ] == 1 ){
			  				count++;
			  				str =new String[]{( String )Name.elementAt(n)
			  						, ( String )Color.elementAt(c)
									, ( String )Work.elementAt(w)};
			  			}
			 if( count == 0 ){//if for one element all squares are zero 
			 	Solve.removeAllElements();
			 	Message = new String("That's impossible");
		 	    return;
		     }
		  			
	  	     if( count == 1 && !contain( Solve, str )){
		  	 	bool = true;
		  		Solve.addElement( str );
		  		for( int nm = 0; nm < maxsize; nm++ ){
					if( nm != Name.indexOf(str[ 0 ])){
						for( int c = 0; c < maxsize; c++ )
							Cube[ nm ][ c ][ Work.indexOf( str[ 2 ] )] *= 0;
						for( int w = 0; w < maxsize; w++ )
							Cube[ nm ][ Color.indexOf( str[ 1 ] ) ][ w ] *= 0;
					} 
		  		}
		  	 }
	       }
	    }
	    if( Solve.size() < maxsize )
	    	for(int n = 0; n < maxsize; n++ ){
		    	Vector eqone = new Vector();
		    	int count = 0;
			  	for(int w = 0; w < maxsize; w++)
			  	 	for(int c = 0; c < maxsize; c++)
			  			if( Cube[ n ][ c ][ w ] == 1 ){
			  				count++;
			  				str =new String[]{( String )Name.elementAt(n)
			  						, ( String )Color.elementAt(c)
									, ( String )Work.elementAt(w)};
			  				eqone.addElement( str );
			  			}
			  	
			    if( count > 1 ){
			    	boolean col = true;
			    	boolean wrk = true;
			    	String[] st1 = new String[ 3 ];
			    	String[] st2 = new String[ 3 ];
			    	for( int e = 1; e < eqone.size(); e++ ){
			    		st1 = ( String[] )( eqone.elementAt(e));
			    	    st2 = ( String[] )( eqone.elementAt(e - 1));
			    		if( !st1[ 1 ].equals(st2[ 1 ])) 
			    			col = false;
			    		if( !st1[ 2 ].equals(st2[ 2 ]))
			    			wrk = false;
			    	}
			    	if( col )
			    		Solve.addElement(new String[]{st2[ 0 ],st2[ 1 ],""});
					else if( wrk )
						Solve.addElement(new String[]{st2[ 0 ],"",st2[ 2 ]});
				}
			}
	resetDummy();
    }

/**********************************************************************************/	
}//class