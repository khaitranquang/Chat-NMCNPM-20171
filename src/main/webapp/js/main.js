/**
 * 
 */
$(document).ready(function () {
    var websocket = new WebSocket("ws://localhost:8080/BTLCNPM/chatroom");
	var divLogin = $('#form-login');
	var divSignUp = $('#form-signup');
	var onLogin = $('#onLogin');
	var onSignUp = $('#onSignUp');
	var btnLogin = $('#btnLogin');
	var btnRedirect = $('#btnRedirect');
			
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
	 */
	function sendRequestLogin() {
        var user = $('#user');
        var username = user.val();
        /* Check username and password */
        if (username === "") {
            alert("Text is empty!!!");
            return false;
        }
        /*  Login - Send a JSON Object
         *      status:"login"
         *      username: This is current user
         *      room: Default is room 0
         */
        
    }

    btnLogin.click(function () {
		return sendRequestLogin();
    });


    /* Define all variables */
    //var currentUser = localStorage.getItem("currentUser");
    var $btnSend = $('.send_message');
    var listIcon;

    /*
     * Receiving messages (here is JSON) from server
     */
    websocket.onmessage = function (event) {

        var jsonFromServer = JSON.parse(event.data);
        var statusFromServer = jsonFromServer.status;

        switch (statusFromServer) {
            case "confirm":
                var messFromServer = jsonFromServer.message;
                if (messFromServer == "OKE") {
                    /* Set list icon from server */
                    //.......
                }
                break;

            case "text":
                var from = jsonFromServer.from;
                var message = jsonFromServer.message;

                sendMessage(from + ": " + message);
                break;
        }
    };




    /* Define a Message object */
    var Message;
    Message = function (arg) {
        this.text = arg.text, this.message_side = arg.message_side;
        this.draw = function (_this) {
            return function () {
                var $message;
                $message = $($('.message_template').clone().html());
                $message.addClass(_this.message_side).find('.text').html(_this.text);
                $('.messages').append($message);
                return setTimeout(function () {
                    return $message.addClass('appeared');
                }, 0);
            };
        }(this);
        return this;
    };

    /* Get text from input */
    var getMessageText, message_side='left', sendMessage, showNotification;
    getMessageText = function () {
        var $message_input;
        $message_input = $('.message_input');
        return $message_input.val();
    };

    sendMessage = function (text) {
        var $messages, message;
        if (text.trim() === '') {
            return;
        }
        $('.message_input').val('');
        $messages = $('.messages');
        message_side = 'left';
        message = new Message({
            text: text,
            message_side: message_side
        });
        message.draw();
        return $messages.animate({ scrollTop: $messages.prop('scrollHeight') }, 300);
    };

    /* Show notifications when user login or log out */
    showNotification = function (notification) {
        return $('.messages').append(notification);
    };



    /*
     * Send event 'chat' - Send messages to server
     * Then server send messages to all users
     */
    $btnSend.click(function () {
        var jsonClient = {
            status:"text",
            user:currentUser,
            message:getMessageText(),
            room:0
            //from:currentUser;
        };
        websocket.send(JSON.stringify(jsonClient));
        return sendMessage(getMessageText());
    });

    $('.message_input').keyup(function (e) {
        if (e.which === 13) {
            var jsonClient = {
                status:"text",
                user:currentUser,
                message:getMessageText(),
                room:0
                //from:currentUser;
            };
            websocket.send(JSON.stringify(jsonClient));
            return sendMessage(getMessageText());
        }
    });

});
	