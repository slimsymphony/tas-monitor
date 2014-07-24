<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>Welcome to TAS Monitor</title>
<link rel="stylesheet" type="text/css" href="css/cupertino/jquery-ui-1.8.22.custom.css" />
<link rel="stylesheet" type="text/css" href="css/shadowbox.css">
<link rel="stylesheet" type="text/css" href="css/iealert/style.css" />
<style type="text/css">
body {
	text-align: left;
	margin-top: 40px;
	margin-left: 10%;
	margin-right: 10%;
	font-family: Trebuchet MS, Tahoma, Verdana, Arial, sans-serif;
}

iframe {
	width: 100%;
	height: 600px;
	border: 1px;
}

.header {
	font-size: 40pt;
	font-weight: bold;
	color: darkblue;
	text-align: center;
	margin-bottom: 20px;
	vertical-align: middle;
}

.content {
	text-align: center;
}

.links {
	text-align: center;
	border-style: solid;
	border-color: yellow;
}

.main {
	text-align: center;
	/*border-style:solid;
	border-color:red;*/
}

.fm {
	text-align: center;
	width: 80%;
	height: 80%;
	border-style: solid;
	border-color: brown;
}

.footer {
	margin-top: 50px;
	text-align: center;
}

#tabs {
	width: 90%;
}
</style>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.22.custom.min.js"></script>
<script type="text/javascript" src="js/shadowbox.js"></script>
<script type="text/javascript" src="js/iealert.min.js"></script>
<script type="text/javascript">
$(function() {
	$("#tabs").tabs();
	$("body").iealert();
});
function rest(){
	if ( !$.browser.msie || parseInt($.browser.version, 10) >= 9) {
		window.open('goldMine.html')
	}
}
function rest2(){
	if ( !$.browser.msie || parseInt($.browser.version, 10) >= 9) {
		window.open('flight.html')
	}
}
</script>

</head>
<body>
	<div class="header">
		<img src="images/TAS_logo.png" width="50%" />
	</div>

	<div id="tabs">
		<ul>
			<li><a href="#monitor">TAS Execution Status</a></li>
			<li><a href="#tas">TAS Service Status</a></li>
			<li><a href="#contacts">Contact</a></li>
		</ul>
		<div id="monitor">
			<iframe id="ifmonitor" src="monitorMain.jsp"></iframe>
		</div>
		<div id="tas">
			<iframe id="iftas" src="serviceMain.jsp"></iframe>
		</div>
		<div id="contacts">
			Thanks for using TAS & TAS Monitor!<br/><br/>
			<ul>
				<li>
					Any TAS related questions, please <a class="lync" href="im:sip:frank.8.wang@nokia.com">Lync</a> Wang Frank or  <a href="mailto:frank.8.wang@nokia.com?Subject=TAS Question">Mail</a> him.<br/>
				</li>
				<li>
					Any SAIT plugin related questions, please <a class="lync" href="im:sip:kelvin.zhu@nokia.com">Lync</a> Zhu Kelvin or  <a href="mailto:kelvin.zhu@nokia.com?Subject=SAIT plugin Question">Mail</a> him.<br/>
				</li>
				<li>
					Any problem related this site, contact <a href="mailto:frank.8.wang@nokia.com?Subject=TAS Monitor Question">Frank</a>
				</li>
			</ul>
			TAS is a great framework for target testing which belongs to S40 CI.<br/>Please be 
			<label title="Easter egg" style="cursor:Pointer" onclick="rest()">happy</label>
			<label title="Easter egg2" style="cursor:Pointer" onclick="rest2()"> :)</label>
		</div>
	</div>
	<div class="footer">&copy; Nokia FPD Testing Service Beijing</div>
</body>
</html>