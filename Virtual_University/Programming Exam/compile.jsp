<%@ page language="java" import="com.exam.Compiler"  %>

<html> <head> <title> Result of compiling your program ... </title> </head>
<body  bgcolor="#EBF0FB">

<br><font  color="#2C0CFB" size=4> Result </font> &nbsp 
<br><br>
<%
 Compiler ob = new Compiler(request.getParameter("program")  ,pageContext.getRequest().getParameter("choice") );
 String result = ob.compilerMessage;
 //System.out.println(result);
 out.print(result);
 %>
 <br><br>
 <%
  if(!result.equalsIgnoreCase("Congradulation!"))
  {
	%>
	<br><font  color="#2C0CFB"  size=4> Programming Language :</font> <br><br>
	<form   method="post"  action="compile.jsp" >
	<input  type="radio" name="choice" value="JAVA" <% if (request.getParameter("choice").equals("JAVA")) {%>
	<%="checked"%> <%}%>  >  <font color="#C72995"> Java   </font> &nbsp &nbsp
	<input  type="radio" name="choice" value="C" <% if (request.getParameter("choice").equals("C")) {%>
	<%="checked"%> <%}%> > <font color="#C72995"> C  </font> &nbsp &nbsp
	<input  type="radio" name="choice" value="C++" <% if (request.getParameter("choice").equals("C++")) {%>
	<%="checked"%> <%}%> >   <font color="#C72995"> C++    </font> &nbsp &nbsp
	
	<br>
	<br><br>
	<textarea name="program"  scope="session"  rows=15 cols=70 ><%=request.getParameter("program")%></textarea>
        <br> &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp&nbsp 
	&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp
        <input  type="submit"  name="submit"   value="Submit again" >
        </form>
	<%}
else
	out.print("You are done.");
 %>
      
   <br><br> <a href="select.jsp" >  Main Page </a>

</body>
</html>
