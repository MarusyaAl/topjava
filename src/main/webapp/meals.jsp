<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.topjava.util.MealsUtil" %>
<%@ page import="static ru.javawebinar.topjava.util.MealsUtil.meals" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h2><p>${name}</p></h2>
<table border="1" cellpadding="8" cellspacing="0" style="margin: auto">
    <tr bgcolor="#5f9ea0">
        <th>Дата</th>
        <th>Описание</th>
        <th>Калории</th>
        <th>Редактирование</th>
        <th>Удаление</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr bgcolor="#faf0e6">
        <c:if test="${meal.excess}">
            <tr bgcolor="#cd5c5c">
        </c:if>
        <td>${meal.dateTime.format(dtf)}

        </td>
        <td>${meal.description}
        </td>
        <td>${meal.calories}
        </td>
        <td>Edit</td>
        <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

