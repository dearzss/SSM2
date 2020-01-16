<%--
  Created by IntelliJ IDEA.
  User: yelele
  Date: 09-Jan-20
  Time: 10:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Upload Excel</title>
</head>
<body>
<form action="excel/upload" method="post" enctype="multipart/form-data">
    <table class="form">
        <tr>
            <td class="field">Excel:</td>
            <td><input type="file" class="text" id="excelupload" name = "attachs" /></td>
            <td class="field" id="hderrorinfo" style="margin-left:0px">${sessionScope.uploadwkError}</td>
        </tr>
        <tr>
            <td colspan="2"><label class="ui-blue"><input type="submit" name="submit" value="upload" /></label></td>
        </tr>
    </table>
</form>
</body>
</html>
