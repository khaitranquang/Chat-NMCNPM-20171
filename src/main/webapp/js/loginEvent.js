/**
 * @author TranQuangKhai
 */
$(document).ready(function () {
    var websocket = new WebSocket("ws://localhost:8080/BTLCNPM/chatroom");
	var divLogin = $('#form-login');
	var divSignUp = $('#form-signup');
	var onLogin = $('#onLogin');
	var onSignUp = $('#onSignUp');
	var $btnLogin = $('#btnLogin');
	var $btnSignup = $('#btnSignin');
	var $formLogin = $('#form-login');
	var $formSignup = $('#form-signup');
			
	onLogin.click(function () {
		divLogin.show();
		divSignUp.hide();
		onLogin.css({"color":"#FFF",
					 "border-bottom": "4px solid white"});
		onSignUp.css({"color":"#606468",
					  		  "border-bottom": "4px solid #434a52"});
	});

	onSignUp.click(function () {
		divLogin.hide();
		divSignUp.show();
		onSignUp.css({"color":"#FFF",
					  "border-bottom": "4px solid white"});
		onLogin.css({"color":"#606468",
					 "border-bottom": "4px solid #434a52"});
	});

	/*
	 * Send a request login to server
	 * Check text field is empty??
	 */
	function sendRequestLogin() {
        var $user = $('#user');
        var username = $user.val();
        var password = $('#pass').val();
        /* Check username and password */
        if (username === "" || password === "") {
            alert("Text is empty!!!");
            return false;
        }
    }

    $formLogin.submit(function () {
		return sendRequestLogin();
    });

    /*
     * Check request sign in
     */
    function sendRequestSignin() {
        var fullName = $('#register-fullname').val();
        var age = $('#register-age').val();
        var user = $('#register-user').val();
        var pass =  $('#register-pass').val();
        var rePass = $('#register-re-pass').val();
        if (fullName.trim() === "" || age.trim() === "" ||
            user.trim() === "" || pass.trim() === "" || rePass.trim() === "") {
            alert("Text is empty!!!");
            return false;
        }
        // var rx = new RegExp('^\d+$');
        if (age != parseInt(age, 10)) {
            alert("Tuổi phải là số" + age);
            return false;
        }
        if (pass != rePass) {
            alert("Nhập lại đúng mất khẩu mới");
            return false;
        }
        return true;
    }

    $formSignup.submit(function () {
        return sendRequestSignin();
    });

});
	