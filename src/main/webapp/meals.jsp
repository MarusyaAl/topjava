<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.topjava.util.MealsUtil" %>
<%@ page import="static ru.javawebinar.topjava.util.MealsUtil.meals" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Princess
  Date: 07.06.2020
  Time: 12:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h2><p>${name}</p></h2>
<table border="1" cellpadding="8" cellspacing="0" style="margin: auto">
    <tr>
        <th>Дата</th>
        <th>Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th>Редактирование</th>
        <th>Удаление</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <c:if test="${meal.excess == 'true'}">
           background: black;
        </c:if>
        <tr>
            <td> ${meal.dateTime}
            </td>
            <td>${meal.description}
            </td>
            <td>${meal.calories}
            </td>
            <td>${meal.excess}
            </td>
            <td>Edit</td>
            <td>Delete</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

