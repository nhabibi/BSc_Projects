
<html>
<title>  Your Inbox  </title>
<body>

<%! 
      PwdChecker  checker = new PwdChecker();
%>

<jsp:useBean     id="info"    class="UserData"    scope="page"  />
<jsp:setProperty  name="info"   property="*"   />

<%  if (   ! checker.usernameExists(  info.getUsername()  )  ) {
         // request.setParameter( "badUsername" , "Yes" );
          response.sendRedirect( "login.jsp?badUsername=" + info.getUsername()  );
       }
       else if  (  ! checker.pwdCorrect ( info.getUsername() , info.getPassword()  )  ) {
         // request.setParameter( "badPassword" , "Yes" );
          response.sendRedirect( "login.jsp?badPassword=Yes" );
       }

       else {
               session.setAttribute("username" , info.getUsername() );
               session.setAttribute("password" , info.getPassword() );
               out.println("<br><font   color=red  size=6 >"  + "Hello, " + "</font> <font   color=green  size=6>" + session.getAttribute("username") + "</font>" +  "<font   color=red  size=6>.You have successfully logged in!" + "</font>" );

       }
%>

</body>
</html>
      
     
