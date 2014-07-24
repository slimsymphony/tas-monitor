<%@page contentType="text/html;charset=UTF-8"%><%@page import="com.nokia.ci.tas.service.monitor.*"%><%@page import="com.nokia.ci.tas.service.monitor.model.*"%><%@page import="java.util.*"%><%
	try {
		String op = request.getParameter( "op" );
		TasServiceManager tm = TasServiceManager.getInstance();
		String host = request.getParameter( "host" );
		int port = Integer.parseInt( request.getParameter( "port" ) );
		int servicePort = Integer.parseInt( request.getParameter( "servicePort" ) );
		if ( "register".equals( op ) ) {
			tm.addStation( new TasService( host, port, servicePort ) );
		} else if ( "delete".equals( op ) ) {
			tm.deleteStation( new TasService( host, port ) );
		} else if ( "modify".equals( op ) ) {
			String newHost = request.getParameter( "newHost" );
			int newPort = Integer.parseInt( request.getParameter( "newPort" ) );
			int newServicePort = Integer.parseInt( request.getParameter( "newServicePort" ) );
			tm.updateStation( new TasService( host, port, servicePort ), new TasService( newHost, newPort,newServicePort ) );
		}
		out.print("success");
	} catch ( Exception e ) {
		out.print(e.getMessage());
	} 
%>