<%@page contentType="text/html;charset=UTF-8"%><%@page import="com.nokia.ci.tas.common.monitor.*"%><%@page import="java.util.*"%><%
String name = request.getParameter("name");
String pass = request.getParameter("pass");
String op = request.getParameter("op");
String param = request.getParameter("param");
try{
System.err.println( name + " want to " + op + " with " + param );
Map<String,String> userInfos = AuthService.authenticateUser( name, pass );
if(userInfos==null)
	out.print("false");
else
	out.print("true");
}catch(Exception ex){
	out.print("false");
}
%>