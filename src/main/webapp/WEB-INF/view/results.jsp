<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML>
<html>
	<head>
		<title>Temp monitor</title>
		<link href="<c:url value="/resources/css/styles.css" />" rel="stylesheet" type="text/css" />
		<%-- TODO GIM use in PRd the min version d3.min-v4.5.js --%>
		<script type="text/javascript" src="<c:url value="/resources/vendors/d3js/d3-v4.5.js" />" ></script> 
		<%-- Alternative: reference directly D3.org site "http://d3js.org/d3.v3.min.js" --%>
		<%-- We need c:url to add the context path into the URL
		  c:url correctly resolves "/resources/css/styles.css" -> "http://hostname/mastermind/resources/css/styles.css"
		  If we used: <link href="/resources/css/styles.css" ..> the browser would issue a GET for  "http://hostname/resources/css/styles.css
		  and would get an 404 (not found) as response
		 --%>
	</head>

	<body>
		<%-- Since the dataset used in diagram.js is defined here. 
		    The order of inclusion is IMPORTANT. since diagram.js is defined "defer" it will be loaded 
		    when the DOM is loaded (just before the "DOMContentLoaded" event) 
		  : include first the inline JS and then the diagram.js !!  --%> 
		<!--  Script Inline  -->
		<script  type="text/javascript">
			function getDataSet() {
				console.log("dataSet="+ samples.samples);
				return ${samples.samples};
			}
			
			function getJsonData() {
				//console.log("jsonData="+ jsonData);
				return ${jsonData};
			}
		</script>

		<!--  Script from static resource - defer == get executed after DOM is loaded
		 alternatives: 
		  - place script tag just before the closing body tag
		  - use  document.addEventListener("DOMContentLoaded", drawDiagram(), false);
		      (or even window.addEventListener("load", drawDiagramAfterPageWasfullyLoaded(), false);)
		  see > https://www.kirupa.com/html5/running_your_code_at_the_right_time.htm
		 
		 -->
		<script defer type="text/javascript"  src="<c:url value="/resources/js/diagram.js" />"></script>
	
<!-- 		<h1 id="title">Temperature Monitor</h1> -->
		<jsp:include page="./fragments/header.jsp"/>
<%-- 		<c:url value="/evaluate" var="formAction" >
			Comment following line if you prefer not to have sessionId in URL
			<c:param name="sessionId" value="${moveForm.sessionId}"/>
		</c:url> --%>
		
		<div class="file-name">
			<span class="file-label">File Name:</span> 
			&nbsp;<c:out value="${file}"/>&nbsp;&nbsp;&nbsp;
			<span class="file-ok">OK </span> 
		</div>
		
		<form:form modelAttribute="evaluationParams" action="showResults" cssStyle="param-form" autocomplete="off" method="post" name="evaluationParams">
			 <%-- bind to evaluateParams --%>
		
			<form:errors path="minTemp" cssClass="validation-error"/><br/>
			
			Min temperature (&deg;C)&nbsp;
			<form:input path="minTemp" type="text" autofocus="autofocus" autocomplete="off"/><br/>

			Max temperature (&deg;C)&nbsp;
			<form:input path="maxTemp" type="text" autofocus="autofocus" autocomplete="off"/><br/>
						
			Ignore before &nbsp;
			<form:input path="ignoreDataBefore" type="text" autofocus="autofocus" autocomplete="off"/><br/>
						
			Ignore after &nbsp; 
			<form:input path="ignoreDataAfter" type="text" autofocus="autofocus" autocomplete="off"/><br/>

				<%-- Uncomment this if you prefer not to have sessionId in URL
 					<form:input path="sessionId" type="hidden" /> <!-- bind to moveForm.sessionId--> 
 				--%>
 			<br/>
			<button>Evaluate</button>
		</form:form>
		<br/><br/>		
			

		<%-- This is where the histogram will be created by the JS script diagram.js --%>
		<div id="diagram"></div>
		
		
		<%-- TODO GIM: let table be generated by D3js using same json data as histogram? --%>
		<%-- Table with temperatures, date, result --%>
		<c:if test="${not empty samples && showTable}">
			<div id="sample-area">
				<table id="sample-table">
					<c:forEach items="${samples.samples}" var="sample">
						<tr class="sample sample-${evaluationParams.eval(sample).toString()}">
							<td class="point-no">${sample.pointNumber}</td>
							<td class="timestamp">
								<fmt:formatDate value="${sample.timestamp}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<td class="temp">
								${sample.temperature}
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</c:if>
				
		<c:if test="${empty samples}">
			<div id="sample-table-error">&lt; No samples !&gt;</div>
		</c:if>
		<jsp:include page="./fragments/footer.jsp"/>
</body>
</html>