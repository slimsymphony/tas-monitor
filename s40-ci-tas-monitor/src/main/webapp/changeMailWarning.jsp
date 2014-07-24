<%@page contentType="text/html;charset=UTF-8"%><%@page import="com.nokia.ci.tas.common.monitor.*"%><%@page import="com.nokia.ci.tas.service.monitor.model.*"%><%@page import="com.nokia.ci.tas.service.monitor.model.*"%><%@page import="java.util.*"%><%
	try{
		String target = request.getParameter("target");
		if(target!=null&&target.equals("false")){
			com.nokia.ci.tas.common.monitor.MailUtils.enableSend = false;
		}else{
			com.nokia.ci.tas.common.monitor.MailUtils.enableSend = true;
		}
		out.write("success");
	}catch(Exception e){
		out.write(e.getMessage());
	}
%>