<%--
  Created by IntelliJ IDEA.
  User: yelele
  Date: 09-Jan-20
  Time: 2:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>ListAllExcel</title>
    <link rel="stylesheet" type="text/css" href="../statics/css/table.css">
</head>
<body>
    <h2>All the excel in system</h2>
    <table class="table" width="60%">
        <tr>
            <td>No.</td>
            <td>FileName</td>
            <td>ModifyDate</td>
            <td>View</td>
            <td>Generate Scripts</td>
            <td>Download Scripts</td>
        </tr>

        <c:forEach items="${fileNameList}" var="files" varStatus="i">
        <tr>
            <td>${i.index + 1}</td>
            <td>${files.fileName}</td>
            <td>${files.modifyDate}</td>
            <td>N/A</td>
            <td><a href="generateScripts?fileName=${files.fileName}">GenerateScripts</a></td>
            <td><a href="downloadScripts?fileName=${files.fileName}">DownloadScripts</a></td>
        </tr>
        </c:forEach>
    </table>

</body>
</html>
