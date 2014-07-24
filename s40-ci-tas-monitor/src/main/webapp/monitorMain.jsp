<%@page contentType="text/html;charset=UTF-8"%>
<%@page import="com.nokia.ci.tas.commons.*"%>
<%@page import="com.nokia.ci.tas.communicator.monitor.*"%>
<%@page import="com.nokia.ci.tas.common.monitor.*"%>
<%@page import="com.nokia.ci.tas.communicator.monitor.model.*"%>
<%@page import="java.util.*"%>
<%
	response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); 
	response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
	response.addHeader("Cache-Control", "post-check=0, pre-check=0");
	response.setHeader("Pragma","no-cache"); 
	response.setDateHeader ("Expires", 0); 
	//TestStationManager tm = TestStationManagerFactory.getInstance();
	//List<TestStation> tss = tm.getAllStations();
	Map<TestStation, Response> statusMap = StationCache.get( );//new HashMap<TestStation, Response>();
	List<String> groups = StationCache.getGroups();
	/*Response res = null;
	for ( TestStation ts : tss ) {
		res = MonitorClient.isAliveSafe( ts, new String[]{ ItemType.TEST.name(), ItemType.PRODUCT.name() } );
		statusMap.put( ts, res );
	}*/
%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<title>TAS Monitor Main</title>
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

.tn{}

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

span {
	font-style: italic;
	margin-right: 10px;
}

span.MAIN{
	font-weight: bold;
	color: green;
}

span.REFERENCE{
	color: brown;
}

/* tooltip styling. by default the element to be styled is .tooltip  */
.tooltip {
	display: none;
	background: transparent url("images/black_arrow.png");
	font-size: 15px;
	height: 70px;
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
		var portV = window.prompt("Please input new port number",'5452');
		if( portV == null || portV=='' || !$.isNumeric(portV) ){
			alert("Port should be a number and Not Empty");
			return;
		}
		
		var groupV = window.prompt("Please input new group");
		if( groupV == null ){
			alert("default Group selected");
			groupV = "";
		}
		$.post( "stationHandler.jsp", 
				{ op:'register' ,host:hostV , port:portV, group:groupV }, 
				function(data,status){
					if($.trim(data)=='success'){
						alert("Station Register success!");
						location.reload();
					}else{
						alert("Station Register failure["+data+"],please ask administrator for help");
					}
				},
				'text'
		);
	});
});

function expand( divId ){
	$('#'+divId).toggle();
}

function modify(hostV,portV,groupV){
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
	var groupV2 = window.prompt("Please input new group",groupV);
	if( groupV2 == null ){
		alert("default Group selected");
		groupV2 = "";
	}
	$.post( "stationHandler.jsp", 
			{ op:'modify' ,host:hostV , port:portV, group:groupV, newHost:hostV2, newPort:portV2, newGroup:groupV2 }, 
			function(data,status){
				if($.trim(data)=='success'){
					alert("Station Update success!");
					location.reload();
				}else{
					alert("Station Update failure["+data+"],please ask administrator for help");
				}
			},
			'text'
	);
}

function del(hostV,portV){
	$.post( "stationHandler.jsp", 
			{ op:'delete' ,host:hostV , port:portV }, 
			function(data,status){
				if($.trim(data)=='success'){
					alert("Station Unregister success!");
					location.reload();
				}else{
					alert("Station Unregister failure["+data+"],please ask administrator for help");
				}
			},
			'text'
	);
}

function detail(hostV,portV){
	window.showModalDialog("stationDetails.jsp?host="+hostV+"&port="+portV, "", "dialogHeight:800px;dialogWidth:1000px;");
}
function refresh(){
	location.reload();
	/*$(".tn").foreach(function(node){
		
	});*/
}

function stopSchedule() {
	$.get( "scheduleHandler.jsp", 
			{}, 
			function(data,status){
				if($.trim(data)!='success'){
					alert("Stop schedule checker failure["+data+"],please ask administrator for help");
				}
			},
			'text'
	);
}

function switchWarning(target){
	$.post( "changeMailWarning.jsp", 
			{ target:target }, 
			function(data,status){
				if($.trim(data)=='success'){
					location.reload();
				}else{
					alert("Change Mail Warning status failure["+data+"],please ask administrator for help");
				}
			},
			'text'
	);
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
		<!-- <h2>Here lists all the registered Test Station.</h2> -->
		<button class="ui-button ui-widget ui-state-default ui-corner-all" onclick="location.reload()">Refresh</button>
		<button id="registerBtn" class="ui-button ui-widget ui-state-default ui-corner-all">Register New Test Station</button>
		<ul>
		<li> <a href='javascript:void(0)' onclick="expand('group_default')">DEFAULT GORUP</a>
		<div id="group_default">
			<ul>
			<%
				for ( TestStation ts : statusMap.keySet() ) {
					if(!"".equals(ts.getGroup())){
						continue;
					}
					Response rp = statusMap.get( ts );
					int pcnt = 0;
					StringBuffer sb = new StringBuffer(200);
					if ( rp != null ) {
						ProductAdapter pa = ( ProductAdapter )  rp.getItems().get( 1 );
						List<Product> ps = null;
						if( pa != null )
							ps = pa.getProducts();
						if( ps ==null )
							ps = new ArrayList<Product>();
						pcnt = ps.size();
						sb.append("<ol>");
						for( Product p : ps ){
							sb.append("<li>");
							if( p.isFree() ){
								sb.append( "<font color='blue'><span class='"+p.getRole()+"'>[" ).append(p.getRole()).append("]</span>:");
							}else{
								sb.append( "<font color='red'><span class='"+p.getRole()+"'>[" ).append(p.getRole()).append("]</span>").append(p.getStatus()).append(":");
							}
							sb.append(p.getFuseConnectionName()).append(":").append(p.getIMEI()).append(";").append(p.getRMCode());
							
							sb.append(":").append(p.getSIM1PhoneNumber()).append(":").append(p.getSIM2PhoneNumber( )).append( "</font>" );
							if( !p.isFree() ){
								sb.append("&nbsp;<a href='#' onclick=\"").append("operation('").append(ts.getHost()).append("',").append(ts.getPort()).append("\",'freeProduct','").append(p.getIMEI()).append("'>Free</a>");
							}
							sb.append("&nbsp;<a href='#' onclick=\"").append("operation('").append(ts.getHost()).append("',").append(ts.getPort()).append("\",'removeProduct','").append(p.getIMEI()).append("'>Remove</a>");
							sb.append("</li>");
						}
						sb.append("</ol>");
					}
			%>
			<li class="tn">
				<a href='javascript:void(0)' onclick="expand('div_<%=ts.getHost().replaceAll("\\.","_")%>')">switch</a>
				<label>HOST:</label> <span><%=ts.getHost()%></span> 
				<label>PORT:</label> <span><%=ts.getPort()%></span> 
				<label>STATUS:</label>
				<span>
					<%
						if ( rp != null ) {
							if ( rp.getItems() == null || rp.getItems().size() == 0 ) {
					%> 
					<span>Target:</span><span>0</span>
					<img height="25px" src="images/running.png" />
					<span>No Test running</span> 
					<%
 							} else {
 								TestAdapter ta = ( TestAdapter ) rp.getItems().get( 0 );
 								Map<Test, String> tests = null;
 								if( ta != null )
 									tests = ta.getTests();
 								else
 									tests = new HashMap<Test,String>();
 								if( tests == null )
 									tests = new HashMap<Test,String>();
 					%> 
 					<span>Target:</span><label><%=pcnt%></label>
 					<%
 								if( tests.size() > 0 ){%>
 						<img height="25px" src="images/testing.gif" /> 
					<% 				for ( Test test : tests.keySet() ) {
										String status = tests.get( test );
										String[] ss= {status,"Unknown"};
										if( status.indexOf( '$' )>0 ){
											ss = status.split( "\\$" );
										}
										
					 %>
					<label onclick="window.open('<%=test.getURL()%>/console')" title="<%=test.getId()+",running Case:["+ss[1]+"]"%>"><%=ss[0]%></label>
					<a href='#' onclick="operation('<%=ts.getHost() %>',<%=ts.getPort()%>,'stopTest','<%=test.getId()%>')">stop</a> 
					<%
 									}
 								} else {%>
 						<img height="25px" src="images/running.png" />				
 					<%			}
 							}
 						} else {
 					%> 
 						<img height="25px" src="images/stop.png" /><span>Not Connected&nbsp;</span> 
 					<%
 						}
 					%>
				</span> 
				<span>
					<button onclick="modify('<%=ts.getHost()%>',<%=ts.getPort()%>,'<%=ts.getGroup() %>')" class="ui-button ui-widget ui-state-default ui-corner-all">Modify</button>
					<button onclick="del('<%=ts.getHost()%>',<%=ts.getPort()%>)" class="ui-button ui-widget ui-state-default ui-corner-all">UnRegister</button>
					<%
						if ( statusMap.get( ts ) != null ) {
					%>
					<button onclick="detail('<%=ts.getHost()%>',<%=ts.getPort()%>)" class="ui-button ui-widget ui-state-default ui-corner-all">Details</button>
					<%
						}
					%>
				</span>
				<div id="div_<%=ts.getHost().replaceAll("\\.","_")%>" style="display:none"><%=sb.toString() %></div>
			</li>
			<%
				}
			%>
		</ul>
		</div>
		</li>
		<%for(String group : groups){%>
		<li><a href='javascript:void(0)' onclick="expand('group_<%=group.replaceAll("\\.","_")%>')"><%=group %></a>
		<div id="group_<%=group.replaceAll("\\.","_")%>">
			
			<ul>
			<%
				for ( TestStation ts : statusMap.keySet() ) {
					if(!group.equals(ts.getGroup())){
						continue;
					}
					Response rp = statusMap.get( ts );
					int pcnt = 0;
					StringBuffer sb = new StringBuffer(200);
					if ( rp != null ) {
						ProductAdapter pa = ( ProductAdapter )  rp.getItems().get( 1 );
						List<Product> ps = null;
						if( pa != null )
							ps = pa.getProducts();
						if( ps ==null )
							ps = new ArrayList<Product>();
						pcnt = ps.size();
						sb.append("<ol>");
						for( Product p : ps ){
							sb.append("<li>");
							if( p.isFree() ){
								sb.append( "<font color='blue'><span class='"+p.getRole()+"'>[" ).append(p.getRole()).append("]</span>:");
							}else{
								sb.append( "<font color='red'><span class='"+p.getRole()+"'>[" ).append(p.getRole()).append("]</span>").append(p.getStatus()).append(":");
							}
							sb.append(p.getFuseConnectionName()).append(":").append(p.getIMEI()).append(";").append(p.getRMCode());
							
							sb.append(":").append(p.getSIM1PhoneNumber()).append(":").append(p.getSIM2PhoneNumber( )).append( "</font>" );
							if( !p.isFree() ){
								sb.append("&nbsp;<a href='#' onclick=\"").append("operation('").append(ts.getHost()).append("',").append(ts.getPort()).append("\",'freeProduct','").append(p.getIMEI()).append("'>Free</a>");
							}
							sb.append("&nbsp;<a href='#' onclick=\"").append("operation('").append(ts.getHost()).append("',").append(ts.getPort()).append("\",'removeProduct','").append(p.getIMEI()).append("'>Remove</a>");
							sb.append("</li>");
							
						}
						sb.append("</ol>");
					}
			%>
			<li class="tn">
				<a href='javascript:void(0)' onclick="expand('div_<%=ts.getHost().replaceAll("\\.","_")%>')">switch</a>
				<label>HOST:</label> <span><%=ts.getHost()%></span> 
				<label>PORT:</label> <span><%=ts.getPort()%></span> 
				<label>STATUS:</label>
				<span>
					<%
						if ( rp != null ) {
							if ( rp.getItems() == null || rp.getItems().size() == 0 ) {
					%> 
					<span>Target:</span><span>0</span>
					<img height="25px" src="images/running.png" />
					<span>No Test running</span> 
					<%
 							} else {
 								TestAdapter ta = ( TestAdapter ) rp.getItems().get( 0 );
 								Map<Test, String> tests = null;
 								if( ta != null )
 									tests = ta.getTests();
 								else
 									tests = new HashMap<Test,String>();
 								if( tests == null )
 									tests = new HashMap<Test,String>();
 					%> 
 					<span>Target:</span><label><%=pcnt%></label>
 					<%
 								if( tests.size() > 0 ){%>
 						<img height="25px" src="images/testing.gif" /> 
					<% 				for ( Test test : tests.keySet() ) {
										String status = tests.get( test );
										String[] ss= {status,"Unknown"};
										if( status.indexOf( '$' )>0 ){
											ss = status.split( "\\$" );
										}
										
					 %>
					<label onclick="window.open('<%=test.getURL()%>/console')" title="<%=test.getId()+",running Case:["+ss[1]+"]"%>"><%=ss[0]%></label>
					<a href='#' onclick="operation('<%=ts.getHost() %>',<%=ts.getPort()%>,'stopTest','<%=test.getId()%>')">stop</a> 
					<%
 									}
 								} else {%>
 						<img height="25px" src="images/running.png" />				
 					<%			}
 							}
 						} else {
 					%> 
 						<img height="25px" src="images/stop.png" /><span>Not Connected&nbsp;</span> 
 					<%
 						}
 					%>
				</span> 
				<span>
					<button onclick="modify('<%=ts.getHost()%>',<%=ts.getPort()%>)" class="ui-button ui-widget ui-state-default ui-corner-all">Modify</button>
					<button onclick="del('<%=ts.getHost()%>',<%=ts.getPort()%>)" class="ui-button ui-widget ui-state-default ui-corner-all">UnRegister</button>
					<%
						if ( statusMap.get( ts ) != null ) {
					%>
					<button onclick="detail('<%=ts.getHost()%>',<%=ts.getPort()%>)" class="ui-button ui-widget ui-state-default ui-corner-all">Details</button>
					<%
						}
					%>
				</span>
				<div id="div_<%=ts.getHost().replaceAll("\\.","_")%>" style="display:none"><%=sb.toString() %></div>
			</li>
			<%
				}
			%>
		</ul>
		</div>
		</li>
		<%} %>
		</ul>
		<label>Mail Warning Status:<%=com.nokia.ci.tas.common.monitor.MailUtils.enableSend%></label>
		<% if(com.nokia.ci.tas.common.monitor.MailUtils.enableSend){%>
		<button onclick="switchWarning(false)">Close Mail Warning</button>
		<% }else{ %>
		<button onclick="switchWarning(true)">Start Mail Warning</button>
		<%} %>
		&nbsp;&nbsp;&nbsp;&nbsp;<button onclick="stopSchedule()">Stop Schedule check</button>
	</div>
</body>
</html>