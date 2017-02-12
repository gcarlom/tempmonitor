<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE HTML>
<html>
	<head>
	<title>Temp monitor</title>
	<link href="<c:url value="/resources/css/styles.css" />" rel="stylesheet" type="text/css" />

	<%-- We need c:url to add the context path into the URL
	  c:url correctly resolves "/resources/css/styles.css" -> "http://hostname/mastermind/resources/css/styles.css"
	  If we used: <link href="/resources/css/styles.css" ..> the browser would issue a GET for  "http://hostname/resources/css/styles.css
	  and would get an 404 (not found) as response
	 --%>

	</head>
	<body>
		<jsp:include page="./fragments/header.jsp"/>
		
<!-- 		<h1 id="title">Temperature Monitor</h1> -->
		
		<%-- 
			<c:url value="/evaluate" var="formAction" >
			Comment following line if you prefer not to have sessionId in URL
			<c:param name="sessionId" value="${moveForm.sessionId}"/>
			</c:url>
	 	--%>
		<form:form modelAttribute="fileBucket" action="uploadSamplesSingleFile" 
				method="post" id="file-upload-form" enctype="multipart/form-data">
			File: 
		    <form:input type="file" path="file" id="input-file" name="file" />
			<form:errors cssClass="error-message" path="file" /><br/><br/>
		    <input type="submit" value="Upload" />
		</form:form>
		
			 <%-- bind to evaluateParams --%>
		
		<%--
		<br/><br/>OLD Form 
		<form method="POST" action="uploadSamples" enctype="multipart/form-data">
		    <input type="file" name="file" /><br/><br/>
		    <input type="submit" value="Upload" />
		</form>
		 --%>
		
		
		
		
		<jsp:include page="./fragments/footer.jsp"/>

	</body>
</html>