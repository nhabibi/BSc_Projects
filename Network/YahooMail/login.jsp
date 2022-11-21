
<html>
<title> Yahoo Mail!  </title>
<body>
<br> <p   align=center    >  <font   color="red"  bold  size=20>  YahooMail!   </font>
<p>
<% String  badUsername = request.getParameter("badUsername");
      String  badPwd = request.getParameter("badPassword");
      //System.out.println( badUsername + "  " + badPwd);

      if ( badUsername  !=  null ) { //for first time
           if ( badUsername.equals("") )
               out.print( "<br><font   color=red   size=4 >" + "Please enter a username! </font>" );
          else
               out.print("<br> <font   color=red   size=4 >" + request.getParameter("badUsername")  + " dose not exists.Please try again. </font> ");
      }
      else if ( "Yes".equals( badPwd  ) ) {
           out.print( "<br> <font   color=red   size=4>" + "Password is incorrect.Please try again. </font>");
      }
%>

<form  method=Post    action="inbox.jsp" >

<br> <font  color="green" > UserName   </font>
<input   type=Text           name="username"    value=""> 
<br>
<br>  <font  color="green" > Password   </font>
<input   type=Password    name="password"> 
<br><br>
<input   type=submit>

</form>

</body>
</html>
