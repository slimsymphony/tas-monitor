<%@page contentType="text/html;charset=UTF-8"%><%@page import="com.nokia.ci.tas.commons.*"%><%@page import="com.nokia.ci.tas.communicator.monitor.*"%><%@page import="com.nokia.ci.tas.common.monitor.*"%><%@page import="com.nokia.ci.tas.communicator.monitor.model.*"%><%@page import="com.nokia.ci.tas.service.monitor.*"%><%@page import="com.nokia.ci.tas.service.monitor.model.*"%><%@page import="java.util.*"%><%
com.nokia.ci.tas.service.monitor.MonitorClient smc = new com.nokia.ci.tas.service.monitor.MonitorClient();
com.nokia.ci.tas.communicator.monitor.MonitorClient cmc = null;
int type = Integer.parseInt(request.getParameter("type"));
String op = request.getParameter("op");
String param = request.getParameter("param");
String host = request.getParameter("host");
int port = Integer.parseInt(request.getParameter("port"));
if(type==0){//smc
	if("stopTest".equalsIgnoreCase(op)){
		com.nokia.ci.tas.service.monitor.MonitorClient.stopTest( host, port, param );
	}else if("cleanClients".equalsIgnoreCase( op )){
		com.nokia.ci.tas.service.monitor.MonitorClient.cleanClients( host, port, param );
	}else if("removeTarget".equalsIgnoreCase( op )){
		com.nokia.ci.tas.service.monitor.MonitorClient.removeTarget( host, port, param );
	}else if("resetTarget".equalsIgnoreCase( op )){
		com.nokia.ci.tas.service.monitor.MonitorClient.resetTarget( host, port, param );
	}
}else if (type==1){//cmc
	if("stopTest".equalsIgnoreCase(op)){
		com.nokia.ci.tas.communicator.monitor.MonitorClient.stopTest( host, port, param );
	}else if("freeProduct".equalsIgnoreCase(op)){
		com.nokia.ci.tas.communicator.monitor.MonitorClient.freeProduct( host, port, param );
	}else if("removeProduct".equalsIgnoreCase(op)){
		com.nokia.ci.tas.communicator.monitor.MonitorClient.removeProduct( host, port, param );
	}
}
%>