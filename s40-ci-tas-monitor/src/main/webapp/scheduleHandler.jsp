<%@page import="com.nokia.ci.tas.common.monitor.*"%><%
try{
	ScheduleUtils.endScheduler();
	out.print("success");
}catch(Exception e){
	out.print(e.getMessage());
}
%>