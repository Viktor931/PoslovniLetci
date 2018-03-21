<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
</head>

<body>
	<header>
	</header>
	<nav>
	</nav>
	<main>
	<table>
        <thead>
            <tr>
                <td><b>Username</b></td>
                <td><b>Email</b></td>
                <td><b>Password</b></td>
            </tr>
        </thead>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>
                    <c:out value="${user.username}" />
                </td>
                <td>
                    <c:out value="${user.userModel.email}" />
                </td>
                <td>
                    <c:out value="${user.userModel.password}" />
                </td>
            </tr>
        </c:forEach>
    </table>
	</main>
	<footer>
	</footer>
</body>

</html>