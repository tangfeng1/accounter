<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title> Login | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">

<link rel="shortcut icon" href="../images/favicon.ico" />

<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css" rel="stylesheet">
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet">
<script  type="text/javascript" >
	$(document).ready(function() {
	$('#submitButton').click(function() {
	 $("#submitButton").addClass("login-focus");
	$("#accounterForm").validate({
		rules: {
			emailId: "required",
			password: "required",
			},
		messages: {
			emailId: "please enter your Email",
			password: "please enter password",
			}
		});
	});
	 
});	
</script>

<%
   String app = request.getHeader( "Nativeapp" );
   boolean isNative = ( app != null && !app.equals(""));
   if( isNative ){ %>
   <link type="text/css" rel="stylesheet" href="../css/nativeLogin.css?version=<%= version%>">
   <% } %>
</head>
	<body>
	<div id="contact"> </div>
     <div id="commanContainer">
		   <img src="../images/Accounter_logo_title.png" class="accounterLogo" />
		   <c:if test="${message != null}">
		   <div id="login_error" class="common-box">
				<span>${message} </span>
		   </div>
		   </c:if>	
		   <form id="accounterForm" method="post" action="/main/login">
		      <div class="email_password">
			    <label>Email</label>
				<br>
				<input id="mid-box"  type="text" name="emailId" tabindex="1">
			  </div>
			  <div class="email_password">
			    <label>Password</label>
				<br>
				<input id="mid-box1"  type="password" name="password" tabindex="2">
			  </div>
			  <div class="rememberMe">
			    <input id="checkbox1" type="checkbox" tabindex="4" name="staySignIn"/> 
				<label>Remember Me</label>
			  </div>
			  <div class="loginbutton">
			     <input id="submitButton" style="width:60px" type="submit" class="allviews-common-button" name="login" value="Login" tabindex="6"/>
			  </div>
		   </form>
		   <div class="form-bottom-options">
		      <a href="/main/forgotpassword" id="forget-link1" tabindex="5"> Lost your password?</a>
		   </div>
		    <div class="form-bottom-options">
		      <a href="/main/signup" id="forget-link1" tabindex="6"> Sign up Accounter?</a>
		   </div>
		</div>
		
	     <!-- Footer Section-->
		
		<div id="mainFooter"  >
	    <div>
	       <span>&copy 2011 Vimukti Technologies Pvt Ltd</span> |
	       <a target="_blank" href="/site/termsandconditions"> Terms & Conditions </a> |
	       <a target="_blank" href="/site/privacypolicy"> Privacy Policy </a> |
	       <a target="_blank" href="/site/support"> Support </a>
	    </div>
	</div>
<script type="text/javascript">

var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-24502570-1']);
_gaq.push(['_trackPageview']);

(function() {
var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();

</script>

<script type="text/javascript" charset="utf-8">
			var is_ssl = ("https:" == document.location.protocol);
			var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
		</script>
		 

		</body>
</html>