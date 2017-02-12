<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>
  <head>
    <title>Mastermind</title>
    <link href="<c:url value="/resources/css/styles.css" />" rel="stylesheet" type="text/css" />

 <%-- we need c:url to add the context path into the URL
  c:url correctly resolves "/resources/css/styles.css" -> "http://hostname/mastermind/resources/css/styles.css"
  If we used: <link href="/resources/css/styles.css" ..> the browser would issue a GET for  "http://hostname/resources/css/styles.css
  and would get an 404 (not found) as response
 --%>

 	</head>
		<body>
		<jsp:include page="./fragments/header.jsp" />
		
		<p>
		Sorry, an error occurred:<br/>
		${errorMessage}
		</p>
		<p>
		Go to the 
		<a href="<c:url value='/'/>"> Home</a>
		page.
		</p>


	<jsp:include page="./fragments/footer.jsp"/>

</body>
</html>