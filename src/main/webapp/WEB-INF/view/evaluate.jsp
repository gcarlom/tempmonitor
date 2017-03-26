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
<!-- 		<h1 id="title">Temperature Monitor</h1> -->
			<jsp:include page="./fragments/header.jsp"/>		
<%-- 		<c:url value="/evaluate" var="formAction" >
			Comment following line if you prefer not to have sessionId in URL
			<c:param name="sessionId" value="${moveForm.sessionId}"/>
		</c:url> --%>
		
<%-- 		<form method="POST" action="uploadSamples" enctype="multipart/form-data"> --%>
<!-- 		    <input type="file" name="file" /><br/><br/> -->
<!-- 		    <input type="submit" value="Upload" /> -->
<%-- 		</form> --%>
		<div class="file-name">
			<span class="file-label">File Name:</span> 
			&nbsp;<c:out value="${file}"/>&nbsp;&nbsp;&nbsp;
			<span class="file-ok">OK </span> 
		</div>
		
		<form:form modelAttribute="evaluationParams" action="showResults" cssStyle="param-form" autocomplete="off" method="POST" name="evaluationParams">
			 <%-- bind to evaluateParams --%>
		
<%-- 			<form:errors path="minTemp" cssClass="validation-error"/><br/> --%>
			Min temperature (&deg;C)&nbsp;
			<form:input path="minTemp" type="text" autofocus="autofocus" autocomplete="off"/>
			<form:errors cssClass="error-message" path="minTemp" /><br/>

			Max temperature (&deg;C)&nbsp;
			<form:input path="maxTemp" type="text" autofocus="autofocus" autocomplete="off"/>
			<form:errors cssClass="error-message" path="maxTemp" /><br/>
						
			Ignore before &nbsp;
			<form:input path="ignoreDataBefore" type="text" autofocus="autofocus" autocomplete="off"/>
			<form:errors cssClass="error-message" path="ignoreDataBefore" /><br/>
						
			Ignore after &nbsp; 
			<form:input path="ignoreDataAfter" type="text" autofocus="autofocus" autocomplete="off"/>
			<form:errors cssClass="error-message" path="ignoreDataAfter" /><br/>

				<%-- Uncomment this if you prefer not to have sessionId in URL
 					<form:input path="sessionId" type="hidden" /> <!-- bind to moveForm.sessionId--> 
 				--%>
 			<br/>
			<button>Evaluate</button>
		</form:form>
					
				
		<c:if test="${empty samples}">
			<div id="sample-table-error">&lt; No samples !&gt;</div>
		</c:if>
		<jsp:include page="./fragments/footer.jsp"/>

</body>
</html>