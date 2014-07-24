<%@page contentType="text/html;charset=UTF-8"%>
<%@page import="com.nokia.ci.tas.commons.*"%>
<%@page import="com.nokia.ci.tas.common.monitor.*"%>
<%@page import="com.nokia.ci.tas.service.monitor.*"%>
<%@page import="com.nokia.ci.tas.service.monitor.model.*"%>
<%@page import="java.util.*"%>
<%
	TasServiceManager tsm = TasServiceManager.getInstance();
	List<TasService> tss = tsm.getAllServices();
	Map<TasService,Response> statusMap = new HashMap<TasService,Response>();
	if( tss == null ) tss = new ArrayList<TasService>();
	for( TasService ts : tss ){
		statusMap.put(ts, MonitorClient.checkStatus( ts.getHost(), ts.getPort() ));
	}
%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>TAS Service Main</title>
<link rel="stylesheet" type="text/css"
	href="css/cupertino/jquery-ui-1.8.22.custom.css" />
<link rel="stylesheet" type="text/css" href="css/shadowbox.css">
<link rel="stylesheet" type="text/css" href="css/iealert/style.css" />
<style type="text/css">
body {
	text-align: left;
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
	margin-right: 10px;
	border: 0;
	cursor: pointer;
}

.l2 {
	font-family: Trebuchet MS, Tahoma, Verdana, Arial, sans-serif;
	color: blue;
	margin-right: 5px;
	border: 0;
	font-size: 10pt;
	cursor: pointer;
}

span {
	font-style: italic;
	margin-right: 10px;
}

/* tooltip styling. by default the element to be styled is .tooltip  */
.tooltip {
	display: none;
	background: transparent url("images/black_arrow.png");
	font-size: 15px;
	height: 85px;
	width: 350px;
	padding: 25px;
	color: #eee;
}
</style>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.22.custom.min.js"></script>
<script type="text/javascript" src="js/jquery.tools.min.js"></script>
<script type="text/javascript" src="js/shadowbox.js"></script>
<script type="text/javascript" src="js/iealert.min.js"></script>
<script type="text/javascript">
$(function() {
	$("label[title]").tooltip();
	$("#tabs").tabs();
	$('#registerBtn').click(function(){
		var hostV = window.prompt("Please input new host-Name/ip-address");
		if( hostV == null || hostV=='' ){
			alert("HostName/IP can't be Empty");
			return;
		}
		var portV = window.prompt("Please input new port number");
		if( portV == null || portV=='' || !$.isNumeric(portV) ){
			alert("Port should be a number and Not Empty");
			return;
		}
		var servicePortV = window.prompt("Please input new service port number");
		if( servicePortV == null || servicePortV=='' || !$.isNumeric(servicePortV) ){
			alert("Port should be a number and Not Empty");
			return;
		}
		$.post( "tasHandler.jsp", 
				{ op:'register' ,host:hostV , port:portV, servicePort:servicePortV }, 
				function(data,status){
					if($.trim(data)=='success'){
						alert("TAS Service Register success!");
						location.reload();
					}else{
						alert("TAS Service Register failure["+data+"],please ask administrator for help");
					}
				},
				'text'
		);
	});
});

function modify(hostV,portV,servicePortV){
	var hostV2 = window.prompt("Please input new host-Name/ip-address",hostV);
	if( hostV2 == null ){
		alert("HostName/IP can't be Empty");
		return;
	}
	var portV2 = window.prompt("Please input new port number",portV);
	if( portV2 == null || !$.isNumeric(portV2) ){
		alert("Port should be a number and Not Empty");
		return;
	}
	var servicePortV2 = window.prompt("Please input new service port number");
	if( servicePortV2 == null || servicePortV2=='' || !$.isNumeric(servicePortV2) ){
		alert("Port should be a number and Not Empty");
		return;
	}
	$.post( "tasHandler.jsp", 
			{ op:'modify' ,host:hostV , port:portV, servicePort:servicePortV, newHost:hostV2, newPort:portV2, newServicePort:servicePortV2 }, 
			function(data,status){
				if($.trim(data)=='success'){
					alert("TAS Service Update success!");
					location.reload();
				}else{
					alert("TAS Service Update failure["+data+"],please ask administrator for help");
				}
			},
			'text'
	);
}

function del(hostV,portV){
	$.post( "tasHandler.jsp", 
			{ op:'delete' ,host:hostV , port:portV }, 
			function(data,status){
				if($.trim(data)=='success'){
					alert("TAS Service Unregister success!");
					location.reload();
				}else{
					alert("TAS Service Unregister failure["+data+"],please ask administrator for help");
				}
			},
			'text'
	);
}

function original(hostV,portV){
	self.location.href='originalContainer.jsp?location='+hostV+":"+portV;
}

function detail(hostV,portV){
	window.showModalDialog("tasDetails.jsp?host="+hostV+"&port="+portV, "", "dialogHeight:800px;dialogWidth:1000px;");
}

function refresh(){
	location.reload();
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

window.setTimeout(refresh, 30000);
</script>

</head>
<body>
	<div>
		<!-- <h2>Here lists all the registered TAS Service.</h2> -->
		<button class="ui-button ui-widget ui-state-default ui-corner-all"
			onclick="location.reload()">Refresh</button>
		<button id="registerBtn"
			class="ui-button ui-widget ui-state-default ui-corner-all">Register
			New TAS Service</button>
		<button class="ui-button ui-widget ui-state-default ui-corner-all"
			onclick="window.open('http://becim010:8007/')">Go To CI</button>
		<ul>
			<%
				for ( TasService ts : tss ) {
			%>
			<li>
				<label>HOST:</label> <span><%=ts.getHost()%></span> 
				<label>PORT:</label> <span><%=ts.getPort()%></span>
				<label>ServicePORT:</label> <span><%=ts.getServicePort()%></span>  
				<label>STATUS:</label>
				<span>
					<%
						Response rp = statusMap.get( ts );
						if ( rp != null && rp.getItems() != null && rp.getItems().size() != 0 ) {
 							TestNodeAdapter pa = ( TestNodeAdapter ) rp.getItems().get( 0 );
 							TacAdapter ta = ( TacAdapter )  rp.getItems().get( 1 );
 							int stationNum = 0;
 							if( pa.getNodes() != null )
	 							stationNum = pa.getNodes().size();
 							int clientNum = 0;
 							//StringBuffer sb = new StringBuffer(50);
 							if( ta.getClients() != null ){
 								clientNum = ta.getClients().size();
	 							/*for( String hp : ta.getClients().keySet() ){
	 								sb.append( hp ).append("<br/>");
	 							}*/
 							}
 					%>
						<img height="25px" src="images/running.png" />
						<label class="l2">Stations :</label><span><%=stationNum%></span>
 						<label class="l2">Clients :</label><span><%=clientNum %></span> 						
 					<%
 						} else {
 					%> 
 						<img height="25px" src="images/stop.png" /><span>Not Connected&nbsp;</span>
 						<label class="l2">Stations :</label><span>0</span>
 						<label class="l2">Clients :</label><span>0</span>
 				<%
 						}
 				%>
				</span> 
				<span>
					<button onclick="modify('<%=ts.getHost()%>',<%=ts.getPort()%>,<%=ts.getServicePort()%>)" class="ui-button ui-widget ui-state-default ui-corner-all">Modify</button>
					<button onclick="del('<%=ts.getHost()%>',<%=ts.getPort()%>)" class="ui-button ui-widget ui-state-default ui-corner-all">UnRegister</button>
					<%
						if ( statusMap.get( ts ) != null ) {
					%>
					<button onclick="detail('<%=ts.getHost()%>',<%=ts.getPort()%>)" class="ui-button ui-widget ui-state-default ui-corner-all">Details</button>
					<button onclick="original('<%=ts.getHost()%>',<%=ts.getServicePort()%>)" class="ui-button ui-widget ui-state-default ui-corner-all">Original</button>
					<button onclick="operation('<%=ts.getHost()%>',<%=ts.getPort()%>,'cleanClients','*')" class="ui-button ui-widget ui-state-default ui-corner-all">Clean Clients</button>
					<%
						}
					%>
				</span>
			</li>
			<%
				}
			%>
		</ul>
	</div>
</body>
</html>
