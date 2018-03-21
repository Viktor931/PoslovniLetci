<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src='https://www.google.com/recaptcha/api.js?hl=en'></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
var usernameTaken ='<c:out value="${usernameTaken}"/>';
var passwordsNotEqual ='<c:out value="${passwordsNotEqual}"/>';
var nameCapitalLetter ='<c:out value="${nameCapitalLetter}"/>';
var lastNameCapitalLetter ='<c:out value="${lastNameCapitalLetter}"/>';
var passwordErrors ='<c:out value="${passwordErrors}"/>';
$(document).ready(function() {
    $('#username').on('input', function() {
        $.ajax({
            type: "POST",
            url: "/registration/nameCheck",
            data: $('#username').val(),
            contentType: "text/plain"
        })
            .done(function( data ) {
                if ( data == true ) {
                    $('#username')[0].setCustomValidity("Username taken");
                } else {
                    $('#username')[0].setCustomValidity("");
                }
            });
    });
    $("#password, #password2").change(function(){
        if($("#password").val() == $("#password2").val()){
            $("#password")[0].setCustomValidity("");
            $("#password2")[0].setCustomValidity("");
        } else {
            $("#password")[0].setCustomValidity(passwordsNotEqual);
            $("#password2")[0].setCustomValidity(passwordsNotEqual);
        }
    });
    $("#name").keyup(function(){
        if(/^[ABCČĆDĐEFGHIJKLMNOPRSŠTUVZŽ]/.test($("#name").val())){
            $("#name")[0].setCustomValidity("");
        } else {
            $("#name")[0].setCustomValidity(nameCapitalLetter);
        }
    });
    $("#lastName").keyup(function(){
        if(/^[ABCČĆDĐEFGHIJKLMNOPRSŠTUVZŽ]/.test($("#lastName").val())){
            $("#lastName")[0].setCustomValidity("");
        } else {
            $("#lastName")[0].setCustomValidity(lastNameCapitalLetter);
        }
    });
    $("#password").keyup(function(){
        var loz = $("#password").val();
        var ele = $("#password")[0];
        if(loz.length > 4 && loz.length < 16 && /[ABCČĆDĐEFGHIJKLMNOPRSŠTUVZŽ].*[ABCČĆDĐEFGHIJKLMNOPRSŠTUVZŽ]/.test(loz)
        && /[abcčćdđefghijklmnoprsštuvzž].*[abcčćdđefghijklmnoprsštuvzž]/.test(loz) && /\d/.test(loz)){
            ele.setCustomValidity("");
        } else {
            ele.setCustomValidity(passwordErrors);
        }
    });
    $('form').submit(function () {
        var data = $.trim($('#name').val()) + $.trim($('#lastName').val()) + $.trim($('#username').val()) + $.trim($('#email').val()) + $.trim($('#password').val()) + $.trim($('#password2').val());
        console.log(":::" + data);
        if(data){
            return false;
        }
    });
});
</script>
</head>
<body>
	<header>
	</header>
	<nav>
	</nav>
	<main>
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
                <td><form:label path="username">Username</form:label></td>
                <td><form:input path="username" id="username" value="${model.registrationForm.username}" required="required"/></td>
            </tr>
            <tr>
                <td><form:label path="email">Email</form:label></td>
                <td><form:input path="email" value="${model.registrationForm.email}" type="email" required="required"/></td>
            </tr>
            <tr>
                <td><form:label path="password">Password</form:label></td>
                <td><form:input path="password" value="${model.registrationForm.password}" type="password" required="required"/></td>
            </tr>
            <tr>
                <td><form:label path="password2">Confirm password</form:label></td>
                <td><form:input path="password2" value="${model.registrationForm.password2}" type="password" required="required"/></td>
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