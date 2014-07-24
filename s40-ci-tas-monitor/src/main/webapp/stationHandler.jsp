<%@page contentType="text/html;charset=UTF-8"%><%@page import="com.nokia.ci.tas.communicator.monitor.*"%><%@page import="com.nokia.ci.tas.communicator.monitor.model.*"%><%@page import="java.util.*"%><%
	try {
		String op = request.getParameter( "op" );
		TestStationManager tm = TestStationManagerFactory.getInstance();
		String host = request.getParameter( "host" );
		int port = Integer.parseInt( request.getParameter( "port" ) );
		String group = request.getParameter( "group" );
		if ( "register".equals( op ) ) {
			tm.addStation( new TestStation( host, port, group ) );
		} else if ( "delete".equals( op ) ) {
			tm.deleteStation( new TestStation( host, port ) );
		} else if ( "modify".equals( op ) ) {
			String newHost = request.getParameter( "newHost" );
			int newPort = Integer.parseInt( request.getParameter( "newPort" ) );
			String newGroup = request.getParameter( "newGroup" );
			tm.updateStation( new TestStation( host, port, group ), new TestStation( newHost, newPort, newGroup ) );
		}
		out.print("success");
	} catch ( Exception e ) {
		out.print(e.getMessage());
	} 
%>