<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal form</title>

    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h2>Meal form</h2>
<form method="POST" action='meals'>
    <input type="hidden" name="id" required
           value="${meal.id}" /> <br/>
    <h5>Description:
    </h5><input
        type="text" name="description" required
        value="${meal.description}" /><br/>
    <h5>Calories :
    </h5><input
        type="text" name="calories" required
        value="${meal.calories}"/> <br/>
    <h5>DateTime : </h5>
    <input type="datetime-local" value="${meal.dateTime}" name="dateTime" required><br/><br/><br/>
    <button type="submit">Save</button>

</form>
</body>
</html>
