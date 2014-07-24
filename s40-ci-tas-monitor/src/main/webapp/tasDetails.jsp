<%@page contentType="text/html;charset=UTF-8"%>
<%@page import="com.nokia.ci.tas.commons.*"%>
<%@page import="com.nokia.ci.tas.service.*"%>
<%@page import="com.nokia.ci.tas.service.monitor.*"%>
<%@page import="com.nokia.ci.tas.service.monitor.model.*"%>
<%@page import="java.util.*"%>
<%
	String host = request.getParameter( "host" );
	int port = Integer.parseInt( request.getParameter( "port" ) );
	Response res = MonitorClient.checkStatus(host,port);
	List<MessageItem<?>> items = res.getItems();
	TestNodeAdapter tn = null;
	TacAdapter ta = null;
	for( MessageItem<?> mi : items ){
		if( mi.getItemName().equalsIgnoreCase("TESTNODEADAPTER") ){
			tn = (TestNodeAdapter)mi;
		}
		if( mi.getItemName().equalsIgnoreCase("Client") ){
			ta = (TacAdapter)mi;
		}
	}
%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>TAS service Details</title>
<link rel="stylesheet" type="text/css"
	href="css/cupertino/jquery-ui-1.8.22.custom.css" />
<link rel="stylesheet" type="text/css" href="css/shadowbox.css">
<link rel="stylesheet" type="text/css" href="css/iealert/style.css" />
<style type="text/css">
body {
	text-align: left;
	margin-top: 50px;
	margin-left: 5%;
	margin-right: 10%;
	font-family: Trebuchet MS, Tahoma, Verdana, Arial, sans-serif;
}

iframe {
	width: 100%;
	height: 500px;
	border: 1px;
}

.main {
	text-align: left;
}

div {
	width: 100%;
	text-align: left;
}

li {
	margin-top: 10px;
}

label {
	font-family: Trebuchet MS, Tahoma, Verdana, Arial, sans-serif;
	color: red;
	margin-right: 15px;
}

span {
	font-style: italic;
	margin-right: 30px;
	margin-left: 15px;
}
</style>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.22.custom.min.js"></script>
<script type="text/javascript" src="js/shadowbox.js"></script>
<script type="text/javascript" src="js/iealert.min.js"></script>
<script type="text/javascript">
$(function() {
	$( "#accordion" ).accordion({
		collapsible: true,
		autoHeight: false,
		navigation: true
	});
});

function fold( divid ){
	var div = $('#'+divid);
	if( div.css('display') != 'block')
		div.css('display','block');
	else
		div.css('display','none');
}
function operation(host,port,op,param){
	var ret= window.confirm('Are you sure to '+op+"?");
	if(ret){
		var name = window.prompt("Please input your noe or mail");
		if(name ==null){
			alert("You should provide noe credential to use this functionality!");
			return false;
		}
		var pass = window.prompt("Please input your noe password");
		if(pass ==null){
			alert("You should provide noe credential to use this functionality!");
			return false;
		}
		$.get("login.jsp",{name:name,pass:pass,op:op,param:param},function(data,status){
			if($.trim(data)=='true'){
				$.get( "monitorTool.jsp", 
						{host:host,port:port,type:1,op:op,param:param}, 
						function(data,status){
							refresh();
						},
						'text');
			}else{
				alert("Authentication Failed!");
				return false;
			}
		},'text');
	}
}

function refresh(){
	location.reload();
}
</script>

</head>
<body>
	<button class="ui-button ui-widget ui-state-default ui-corner-all" onclick="location.reload()">Refresh</button>
	<div id="container">
		<div id="accordion">
		
			<%if(tn!=null){%>
			<h3><a href="#">Test Station Status</a></h3>
			<div>
				<ul>
				<%
				if( tn.getNodes() != null && tn.getNodes().size() > 0 ) { 
					for( Map.Entry<String,List<Product>> entry : tn.getNodes().entrySet() ) {
						String hp = entry.getKey();
						List<Product> ps = entry.getValue();
						String[] hps = hp.split(":");
				%>
					<li>
						<label>Station :</label><%= hp %></br>
						<label>Targets :</label> 
						<%
						if( ps==null||ps.size()==0 ) { 
							out.println("No Target"); 
						}else{
							out.print("<ul>");
							for( Product p: ps ){
								out.print("<li>");
								if(!p.isFree()){
									out.print("<button onclick=\"operation('"+hps[0]+"',"+hps[1]+",'resetTarget','"+p.getIMEI()+"')\">Free Target</button>");
								}
								out.print("<button onclick=\"operation('"+hps[0]+"',"+hps[1]+",'removeTarget','"+p.getIMEI()+"')\">Remove Target</button><br/>");
								out.print( p.toString().replaceAll("\n\n","").replaceAll( "\n", "<br/>" ) );
								out.print("</li>");
							}
							out.println("</ul>");
						}
						%>
					</li>
				<%	
					}
				} else out.println("<li>Currently No Test Station connected to this TAS service.</li>");
				%>
				</ul>
			</div>
			<%} %>
			
			
			<%if(ta!=null){%>
			<h3><a href="#">TAS Client Status</a></h3>
			<div>
				<ul>
			<% 
			if( ta.getClients()!=null && ta.getClients().size()>0 ) {
				for( Map.Entry<String,List<Test>> entry : ta.getClients().entrySet() ){
					String client = entry.getKey();
					List<Test> tests = entry.getValue();
			%>
				<li>
					<label>CI (TAC) :</label><span><%=client %></span><br/>
					<label>Execution Jobs:</label>
					<ul>
					<%
						for( Test test : tests ){%>
						<li">
							<label>Job:</label>-<span><%= test.toString().replaceAll("\n\n","").replaceAll("\n","<br/>") %></span>
						</li>
						<% }
					%>
					</ul>
				</li>
			<%	}
			}else{
				out.println("<li>Currently No CI TAC client connected to this TAS Service.</li>");
			}
			%>
				</ul>
			</div>
			<%} %>
			
		</div>
	</div>
</body>
</html>