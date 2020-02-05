<%--
  Created by IntelliJ IDEA.
  User: yelele
  Date: 03-Jan-20
  Time: 3:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <a href="account/findAll">测试SpringMVC/Mybatis(annotations)查询</a>
    <br><a href="person/findAllPerson">测试SpringMVC/Mybatis(XML)查询</a>
    <h3>测试包</h3>

    <form action="account/save" method="post">
        姓名：<input type="text" name="name" /><br/>
        金额：<input type="text" name="money" /><br/>
        <input type="submit" value="保存"/><br/>
    </form>
</body>
</html>
