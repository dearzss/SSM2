<%--
  Created by IntelliJ IDEA.
  User: yelele
  Date: 03-Jan-20
  Time: 3:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h2>查询所有的账户</h2>
    <c:forEach items="${list}" var="account">
        ${account.toString()}
    </c:forEach>
</body>
</html>
