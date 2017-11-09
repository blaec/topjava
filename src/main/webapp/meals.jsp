<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Meals</title>
    <style>
        .normal {color: green;}
        .exceeded {color: red;}
    </style>
</head>
<body>
    <h2><a href="index.html">Home</a></h2>
    <h3>Meal list</h3>
    <hr>
    <table border ="1" cellpadding="8" cellspacing="1">
        <thead>
            <tr>
                <th>Date</th>
                <th>Description</th>
                <th>Calories</th>
            </tr>
        </thead>
        <c:forEach items="${mealsList}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealWithExceed" />
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                    <%--<fmt:parseDate value="${meal.dateTime}" pattern="y-M-dd'T'H:m" var="parsedDate" />--%>
                    <%--<fmt:formatDate value="${parsedDate}" pattern="yyyy.MM.dd HH:mm" />--%>
                    <%=TimeUtil.toString(meal.getDateTime())%>
                </td>
                <td><c:out value="${meal.description}" /></td>
                <td>${meal.calories}</td>
            </tr>
        </c:forEach>
    </table>

</body>
</html>
