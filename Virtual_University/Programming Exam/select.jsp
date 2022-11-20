<%@ page language="java" import="com.exam.Compiler"  %>
<%@ page language="java" import="java.sql.*"  %>
<%@ page language="java" import="com.exam.RandomBean"  %>

<html>
<head><title > Show question </title> </head>
<body   bgcolor="#EBF0FB">

<br><font  color="blue"  size=4> Please select your favorite programming language :</font> <br><br>
<form   method="post"  action="compile.jsp" >
<input  type="radio" name="choice" value="JAVA" >  <font color="#C72995"> Java   </font> &nbsp &nbsp
<input  type="radio" name="choice" value="C"  checked>     <font color="#C72995"> C </font> &nbsp &nbsp
<input  type="radio" name="choice" value="C++" >   <font color="#C72995"> C++    </font> &nbsp &nbsp
 
<br>
<br><font color="blue"  size=3> Write a program for the given question in your selected language and submit it.<br>
your program will be compiled and the result will be sent back to you.
<br> </font> <br>
<font color="red" >ATTENTION Java Programmers : </font>If you define a Public class in your java program, you should name it "program". <br><br>
Question :
<% 
    	    Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/exam");
            Statement st = con.createStatement();
	    RandomBean rb = new RandomBean();
	    int num = rb.getResult();
            ResultSet rs = st.executeQuery("select question from exam where number = "+num);
            while(rs.next()){
	        String q = rs.getString("question");  %>
		<%= q %><br> 
		<%
		}
		%>
<br>
<textarea name="program"  rows=15 cols=70  >
</textarea>
<br> &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp  &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp
<input  type="submit"  name="submit"  scope="session" value="Submit program"  >
</form>

</body>
</html>