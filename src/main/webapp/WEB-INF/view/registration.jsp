<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
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
        <form action="" method="post">
            <label for="name">First name:</label>
            <input type="text" name="name" id="name">
            <label for="lastName">Last name:</label>
            <input type="text" name="lastName" id="lastName">
            <label for="username">Username: </label><label id="usernameTaken">ALREADY TAKEN</label>
            <input type="text" name="username" id="username" required>
            <label for="email">E-mail:</label>
            <input type="email" name="email" id="email" required>
            <label for="password">Password:</label>
            <input type="password" name="password" id="password" required>
            <label for="password2">Confirm password:</label>
            <input type="password" name="password2" id="password" required>
            <input type="submit" value="Send" id="button">
        </form>
	</main>

	<footer id="podnozje">
	</footer>
</body>
</html>