<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.topjava.util.MealsUtil" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<h2><p>Подсчет калорий</p></h2>
<p><a href="/topjava">Home</a></p>
<p><a href="meals?action=create">Add Meal</a></p>
<table border="1" cellpadding="8" cellspacing="0" style="margin: auto">
    <tr bgcolor="#faf0e6">
        <th>Дата</th>
        <th>Описание</th>
        <th>Калории</th>
        <th>Редактирование</th>
        <th>Удаление</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <%--
              <c><tr bgcolor=test="${meal.excess}"?"#5f9ea0":"#cd5c5c">
              </c>   --%>

        <tr bgcolor="#5f9ea0">
        <c:if test="${meal.excess}">
            <tr bgcolor="#cd5c5c">
        </c:if>
        <td>${meal.dateTime.format(dtf)}
        </td>
        <td>${meal.description}
        </td>
        <td>${meal.calories}
        </td>
        <td><a href="meals?action=update&id=${meal.id}">Edit</a></td>
        <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

