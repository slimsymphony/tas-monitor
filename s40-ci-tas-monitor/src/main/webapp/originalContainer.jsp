<%@page contentType="text/html;charset=UTF-8"%>
<%
String loc = request.getParameter("location");
%>
<html>
<head>
<meta http-equiv="refresh" content="30">
<style type="text/css">
iframe {
	text-align: center;
	width: 99%;
	height: 90%;
	border-style: solid;
	border-color: brown;
}
</style>
<script>
function ref(){
	document.getElementById("ifm").src='http://<%=loc%>';
}
</script>
</head>
<body>
<button onclick="self.location.href='serviceMain.jsp'">Back</button>
<button onclick="ref()">refresh</button><br/>
<iframe id="ifm" src="http://<%=loc%>"></iframe>
</body>
</html>