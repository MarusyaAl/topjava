<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add new meal</title>

    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h2>Add new meal</h2>
<form method="POST" action='meals.jsp' name="formAddMeal">
    <h5>Description:
        </h5><input
                type="text" name="description"
                value="<c:out value="${meal.description}" />"/> <br/>
            <h5>Calories :
                </h5><input
                        type="text" name="calories"
                        value="<c:out value="${meal.calories}" />"/> <br/>
                    <h5>DateTime : </h5>
                        <input type="datetime-local" value="${meal.dateTime}" name="dateTime" required><br/><br/><br/>
                        <input type="submit" value="Submit"/>
</form>
</body>
</html>
