<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src='https://www.google.com/recaptcha/api.js?hl=en'></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
$(document).ready(function() {
    $('#usernameTaken').hide();
    $('#username').on('input', function() {
        console.log($('#username').val());
        $.ajax({
            type: "POST",
            url: "/registration/nameCheck",
            data: $('#username').val(),
            contentType: "text/plain"
        })
            .done(function( data ) {
                console.log(data);
                if ( data == true ) {
                    $('#usernameTaken').show();
                } else {
                    $('#usernameTaken').hide();
                }
            });
    });
});
</script>
</head>
<body>
	<header>
	</header>
	<nav>
	</nav>
	<main id="sadrzaj">
	    <form:form method="POST" action="" modelAttribute="registrationForm">
        <table>
            <tr>
                <td><form:label path="name">Name</form:label></td>
                <td><form:input path="name" value="${model.registrationForm.name}"/></td>
            </tr>
            <tr>
                <td><form:label path="lastName">Last Name</form:label></td>
                <td><form:input path="lastName" value="${model.registrationForm.lastName}"/></td>
            </tr>
            <tr>
                <td><form:label path="username">Username</form:label><div id="usernameTaken">USERNAME TAKEN</div></td>
                <td><form:input path="username" id="username" value="${model.registrationForm.username}"/></td>
            </tr>
            <tr>
                <td><form:label path="email">Email</form:label></td>
                <td><form:input path="email" value="${model.registrationForm.email}" type="email"/></td>
            </tr>
            <tr>
                <td><form:label path="password">Password</form:label></td>
                <td><form:input path="password" value="${model.registrationForm.password}" type="password"/></td>
            </tr>
            <tr>
                <td><form:label path="password2">Confirm password</form:label></td>
                <td><form:input path="password2" value="${model.registrationForm.password2}" type="password"/></td>
            </tr>
            <tr>
                <div class="g-recaptcha"
                          data-sitekey="${recaptchaSiteKey}"></div>
            </tr>
            <tr>
                <td><input type="submit" value="Submit"/></td>
            </tr>
        </table>
        </form:form>
	</main>

	<footer id="podnozje">
	</footer>
</body>
</html>