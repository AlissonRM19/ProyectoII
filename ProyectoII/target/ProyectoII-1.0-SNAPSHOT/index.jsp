<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Evaluación de expresiones matemáticas" %>
</h1>
<br/>
<form method="post" action="hello-servlet">
    <table>
        <tr>
            <td>Expresión</td>
            <td><input type="text" name="expresionrecibida"/></td>
        </tr>
        <tr colspan="2">
            <td><input type="submit" value="Enviar"/></td>
        </tr>
        <tr colspan="2">
            <td><input type="submit" value="Historial"/></td>
        </tr>
    </table>
</form>
<a href="hello-servlet">Historial</a>
</body>
</html>