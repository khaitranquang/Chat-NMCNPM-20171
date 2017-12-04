$(document).ready(function () {
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

    var IconTemplate;
    IconTemplate = function (urlIcon) {
        this.showIcon = function () {
            return function() {
                var $icon;
                $icon = $($('.icon_template').clone().html());
                $icon.find('.imageIcon').attr("src", urlIcon);
                $('.modal-content').append($icon);
                // $icon.addClass('messIcon');
                return $icon.addClass('appeared');
            };
        }(this);

        this.iconMessage = function () {
            return function() {
                var $icon;
                $icon = $($('.icon_template').clone().html());
                $icon.find('.imageIcon').attr("src", urlIcon);
                $icon.find('.imageIcon').css({
                    width: '128px',
                    height: '128px',
                    margin: '0 0 20px 10%'
                });
                $('.messages').append($icon);
                return $icon.addClass('appeared');
            };
        }(this);
    };

    var UserTemplate;
    UserTemplate = function(avatarUrl, userName) {
        this.showUser = function() {
            return function () {
                var $userOnline = $($('.user_template').clone().html());
                $userOnline.find('.avatarListUser').attr("src", avatarUrl);
                $userOnline.find('.nameListUser').html(userName);
                $('.listUser').append($userOnline);
                return $userOnline.addClass('appeared');
            };
        }(this);
    }

    $(function () {
        var getMessageText, message_side, sendMessage;
        message_side = 'left';
        
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

        sendIcon = function (urlIcon) {
            var $messages = $('.messages');
            var icon = new IconTemplate(urlIcon);

            icon.iconMessage();
            return $messages.animate({ scrollTop: $messages.prop('scrollHeight') }, 300);
        }

        showUserOnline = function (avatarUrl, userName) {
            var $listUser = $('.listUser');
            var userTemplate = new UserTemplate(avatarUrl, userName);
            userTemplate.showUser();
            return $listUser.animate({ scrollTop: $listUser.prop('scrollHeight') }, 300);
        }


        /*
         * Process WebSocket
         */
        const webSocket = new WebSocket("ws://localhost:8080/BTLCNPM/chatroom");
        var $room = $('.room');
        var $btnShowIcon = $('#btnIcon');
        var listUserOnline = [];
        var numberUser;
        var listIcon = [];
        var thisUser;
        var thisAvatarUrl;
        var roomId;

        $room.click(function() {
            roomId = $(this).attr('id');
            var changeRoomJSON = {
                status:"changeroom",
                room: Number(roomId)
            }
            webSocket.send(JSON.stringify(changeRoomJSON));
            $('.messages').empty();
            $('.chat_window').show();
        });
        
        webSocket.onopen = function (event) {
            
        }

        /* Handle event listen messages from server */
        webSocket.onmessage = function (event) {
            var jsonFromServer = JSON.parse(event.data);
            console.log(jsonFromServer);
            var status = jsonFromServer.status;

            if (status === "onlinelist") {
                numberUser = jsonFromServer.number;
                for (var i = 0; i < jsonFromServer.userlist.length; i++) {
                    // listUserOnline.push(jsonFromServer.userlist[i]);

                    /* Update view in list user Online Area */

                    //showUserOnline(jsonFromServer.userlist[i].avatarurl, jsonFromServer.userlist[i].username);
                }

                // for (var i = 0; i < listUserOnline.length; i++) {
                //     console.log(listUserOnline)
                // }

                thisUser = jsonFromServer.userlist[jsonFromServer.number-1].username;
                thisAvatarUrl = jsonFromServer.userlist[jsonFromServer.number-1].avatarurl;

            }

            if (status === "iconlist") {
                for (var i = 0; i < jsonFromServer.icon.length; i++) {
                    listIcon.push(jsonFromServer.icon[i]);
                }
            }

            if (status === "text") {
                var userSend = jsonFromServer.from;
                var avatarUrlSend = jsonFromServer.avatarurl;
                var font = jsonFromServer.font;
                var size = jsonFromServer.size;
                var mess = jsonFromServer.message;

                // $('.avatarUser:last').css('background-image', 'url(' + avatarUrlSend + ')');
                $('.avatarUser:last').attr({
                    src: avatarUrlSend    
                });
                // if (mess === "Online!") {
                //     listUserOnline.push({
                //         avatarurl: avatarUrlSend,
                //         username: userSend
                //     });
                //     $('.listUser').empty();
                //     for (var i = 0; i < listUserOnline.length; i++) {
                //         showUserOnline(listUserOnline[i].avatarurl, listUserOnline[i].username);
                //     }
                // }
                sendMessage(userSend + " said: " + mess);
            }

            if (status === "icon") {
                var userSend = jsonFromServer.from;
                var avatarUrlSend = jsonFromServer.avatarurl;
                var urlImgIcon = jsonFromServer.message;

                sendIcon(urlImgIcon);
                $modal.css("display", "none");
            }

        }

        /* Send Message */
        $('.send_message').click(function (e) {
            var sendTextJSON = {
                status:"text",
                font:"Arial",
                size:12,
                message:getMessageText()
            }
            webSocket.send(JSON.stringify(sendTextJSON));
        });

        $('.message_input').keyup(function (e) {
            if (e.which === 13) {
                var sendTextJSON = {
                    status:"text",
                    font:"Arial",
                    size:12,
                    message:getMessageText()
                }
                return webSocket.send(JSON.stringify(sendTextJSON));
            }
        });


        /*
         * Show list icon
         */
        var numberOfClickBtnIcon = 0;       /* quantity of click on button show icon */
        var $modal = $('#myModal');
        // Get the button that opens the modal
        var $btnCloseIcon = $("#btnCloseIcon");

        // Get the <span> element that closes the modal
        var $spanClose = $("#btnCloseIcon");

        // When the user clicks the button, open the modal 
        $btnShowIcon.click(function(event) {
            numberOfClickBtnIcon ++;
            if (numberOfClickBtnIcon > 1) {
                return $modal.css("display", "block");
            }
            else {
                for (var i = 0; i < listIcon.length; i++) {
                    var icon = new IconTemplate(listIcon[i]);
                    icon.showIcon();
                }
                return $modal.css("display", "block");
            }
        }); 

        // When the user clicks on <span> (x), close the modal
        $btnCloseIcon.click(function(event) {
            $modal.css("display", "none");
        });

        /* Send Icon - Request to server */
        $(document).on('click', 'img.imageIcon', function(e) {
            var urlImgIcon = $(this).attr("src");
            var sendIconJSON = {
                status:"icon",
                message:urlImgIcon
            }
            webSocket.send(JSON.stringify(sendIconJSON));
        });

    });















}.call(this));