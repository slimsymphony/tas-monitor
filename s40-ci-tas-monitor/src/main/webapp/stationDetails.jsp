<%@page contentType="text/html;charset=UTF-8"%>
<%@page import="com.nokia.ci.tas.commons.*"%>
<%@page import="com.nokia.ci.tas.communicator.monitor.*"%>
<%@page import="com.nokia.ci.tas.communicator.monitor.model.*"%>
<%@page import="java.util.*"%>
<%
	String host = request.getParameter( "host" );
	int port = Integer.parseInt( request.getParameter( "port" ) );
	TestStation tc = new TestStation( host, port ); 
	/*Request req = new Request();
	req.addItem( ItemType.TESTNODE.name() );
	req.addItem( ItemType.PRODUCT.name() );
	req.addItem( ItemType.TEST.name() );
	//req.addItem( ItemType.LOG.name() );
	Response res = MonitorClient.sendRequest( tc, req );*/
	Map<TestStation, Response> statusMap = StationCache.get( );
	Response res = statusMap.get( tc );
	if(res == null){
		Request req = new Request();
		req.addItem( ItemType.TESTNODE.name() );
		req.addItem( ItemType.PRODUCT.name() );
		req.addItem( ItemType.TEST.name() );
		res = MonitorClient.sendRequest( tc, req );
	}
	List<MessageItem<?>> items = res.getItems();
	TestNode tn = null;
	ProductAdapter pa = null;
	TestAdapter ta = null;
	LogInfo li = null;
	for( MessageItem<?> mi : items ){
		if( mi.getItemName().equalsIgnoreCase("Test") ){
			ta = (TestAdapter)mi;
		}
		if( mi.getItemName().equalsIgnoreCase("TestNode") ){
			tn = (TestNode)mi;
		}
		if( mi.getItemName().equalsIgnoreCase("Log") ){
			li = (LogInfo)mi;
		}
		if( mi.getItemName().equalsIgnoreCase("Product") ){
			pa = (ProductAdapter)mi;
		}
	}
%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>TAS Monitor Main</title>
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
</script>

</head>
<body>
	<button class="ui-button ui-widget ui-state-default ui-corner-all" onclick="location.reload()">Refresh</button>
	<div id="container">
		<div id="accordion">
			<%if(ta!=null){%>
			<h3><a href="#">Test Status</a></h3>
			<div>
				<ul>
				<%
				if(ta.getTests()==null){
					out.println("<li>Currently No Test Running on this Test Station.</li>");
				} else {
					for( Test t : ta.getTests().keySet()) {
						String status = ta.getTests().get(t);
				%>
				<li>
				<label>Test:</label><%= t.toString().replaceAll("\n","<br/>") %></br>
				<label>Current Operation:</label> <%=status %>
				</li>
				<%	} 
				}
				%>
				</ul>
			</div>
			<%} %>
			<%if(pa!=null){%>
			<h3><a href="#">Target Status</a></h3>
			<div>
				<ul>
			<% 
			if(pa.getProducts()!=null) {
				for( Product p : pa.getProducts() ){
			%>
				<li>
					<%=p.toString().trim().replaceAll("\n","<br/>") %>
				</li>
			<%	}
			}else{
				out.println("<li>Currently No Target connected to this Test Station.</li>");
			}
			%>
				</ul>
			</div>
			<%} %>
			
			<% if( tn !=null ){ %>
			<h3><a href="#">Test Station Info</a></h3>
			<div>
				<ul>
					<li>
						<label>OS</label><br/>
						<%for( Map.Entry<String,String> entry: tn.getOs().entrySet()){%>
						<span><label><%=entry.getKey() %>:</label><%=entry.getValue() %></span><br/>
						<%} %>
					</li>
					<li>
						<label>CPU</label><br/>
						<%for( Map.Entry<String,String> entry: tn.getCpu().entrySet()){%>
						<span><label><%=entry.getKey() %>:</label><%=entry.getValue() %></span><br/>
						<%} %>
					</li>
					<li>
						<label>MEMORY</label><br/>
						<%for( Map.Entry<String,String> entry: tn.getMemory().entrySet()){
							float val = Float.parseFloat(entry.getValue());
							if(entry.getKey().equalsIgnoreCase("FreePhysicalMemory"))
								val = val*1024;
						%>
						<span><label><%=entry.getKey() %>:</label><%=val / 1000000f %>MB</span><br/>
						<%} %>
					</li>
					<li>
						<label>DISK</label><br/>
						<%for( Map.Entry<String,String> entry: tn.getDisk().entrySet()){%>
						<span><label><%=entry.getKey() %>:</label><%= Float.parseFloat(entry.getValue()) / 1000000000f %>GB</span><br/>
						<%} %>
					</li>
					<li>
						<label>NETWORK</label>&nbsp;&nbsp;<a href="#" onclick="fold('network')">switch</a><br/>
						<div id="network" style="display:none">
						<%for( String line: tn.getNetwork()){%>
						<span><%= line.replaceAll("\n","<br/>") %></span><br/>
						<%} %>
						</div>
					</li>
						
					<li>
						<label>PROCESS</label>&nbsp;&nbsp;<a href="#" onclick="fold('process')">switch</a><br/>
						<div id="process" style="display:none">
						<%for( Map.Entry<Integer,String>entry: tn.getProcesses().entrySet()){%>
						<span><label><%=entry.getKey() %>:</label><%=entry.getValue()%></span><br/>
						<%} %>
						</div>
					</li>				
				</ul>
			</div>
			<%} %>
			<% if( li !=null ){ %>
			<h3><a href="#">Logs</a></h3>
			<div>
				<ul>
				<%if(li.getLogs()!=null){
					for(Map.Entry<String,byte[]> entry: li.getLogs().entrySet()){
				%>
					<li>
						<label><%=entry.getKey() %></label> <a href="#" onclick="fold('<%=entry.getKey().replaceAll("\\.","") %>')">switch</a><br/>
						<div id="<%=entry.getKey().replaceAll("\\.","") %>" style="display:none">
						<%= new String( MonitorUtils.decompress(entry.getValue()), "ISO-8859-1").trim().replaceAll("<","[").replaceAll(">","]").replaceAll("\n","<br/>") %>
						</div>
					</li>	
				<% 	}
				%>
				<%} %>
				</ul>
			</div>
			<% } %>
		</div>
	</div>
</body>
</html>