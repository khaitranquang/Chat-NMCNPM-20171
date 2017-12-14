/**
 * @author TranQuangKhai
 */

$(document).ready(function () {
    /*
     * Message Object: clone from message_template
     * @param: text - content of message
     * @param: message_side - message's side
     *  draw() - this message will be appear with css
     */
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

    /*
     * IconTemplate Object: clone from icon_template
     * @param: urlIcon - this is url of icon
     *  showIcon() - Show icon in modal box
     *  iconMessage() - Append this icon to message area
     */
    var IconTemplate;
    IconTemplate = function (urlIcon) {
        this.showIcon = function () {
            return function() {
                var $icon;
                $icon = $($('.icon_template').clone().html());
                $icon.find('.imageIcon').attr("src", urlIcon);
                $('.modal-content').append($icon);
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

    /*
     * UserTemplate Object - clone from user_template
     * @param: avatarUrl - This is avater of user send
     * @param: userName - user's name
     *  showUser() - Append this user in list user online area
     */
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
        
        /* Get text from input text 
         * @return: text
         */
        getMessageText = function () {
            var $message_input;
            $message_input = $('.message_input');
            return $message_input.val();
        };

        /* Send Message - message will be appear in list message area 
         * @param: text - content of this message
         */
        sendMessage = function (text) {
            var $messages, message;
            /* If text is empty, the message will not send */
            if (text.trim() === '') {
                return;
            }
            $('.message_input').val('');
            $messages = $('.messages');
            message_side = 'left';
            /* Define a new message object */
            message = new Message({
                text: text,
                message_side: message_side
            });
            message.draw();
            return $messages.animate({ scrollTop: $messages.prop('scrollHeight') }, 300);
        };

        /*
         * Send icon to message list
         * @param: urlIcon - url of icon
         */
        sendIcon = function (urlIcon) {
            var $messages = $('.messages');
            var icon = new IconTemplate(urlIcon);

            icon.iconMessage();
            return $messages.animate({ scrollTop: $messages.prop('scrollHeight') }, 300);
        }

        /*
         * showUserOnline - when new user login, this user will be append to userListOnline
         * @param: avatarUrl - url avater of user
         * @param: userName
         */
        showUserOnline = function (avatarUrl, userName) {
            var $listUser = $('.listUser');
            var userTemplate = new UserTemplate(avatarUrl, userName);
            userTemplate.showUser();
            return $listUser.animate({ scrollTop: $listUser.prop('scrollHeight') }, 300);
        }


        /* ************************************************************
         *          Process WebSocket - Handle event
         * Define variables:
         *  $room - a room in listRoom (html)
         *  $btnShowIcon - button show icon (html)
         *  listUserOnline - (array)
         *  numberUser - (int)
         *  listIcon - list icon recieved from server (array)
         *  thisUser - name of current user (string)
         *  thisAvatarUrl - avatarUrl of current user (string)
         *  roomId - id of room that is selected (string)
         * ************************************************************/
        const webSocket = new WebSocket("ws://localhost:8080/BTLCNPM/chatroom");
        var $room = $('.room');
        var $btnShowIcon = $('#btnIcon');
        var listUserOnline = [];
        var numberUser;
        var listIcon = [];
        var thisUser;
        var thisAvatarUrl;
        var roomId;

        /*
         * When a room is clicked - Send request changeRoom to server
         * Then show message area and list room online
         */
        $room.click(function() {
            roomId = $(this).attr('id');
            var changeRoomJSON = {
                status:"changeroom",
                room: Number(roomId)
            }
            webSocket.send(JSON.stringify(changeRoomJSON));
            // $('.listUser').empty();
            // listUserOnline.splice(0, listUserOnline.length);
            $('.messages').empty();
            $('.chat_window').show();
        });

        /* Sending request Logout */
        var $btnLogout = $('#btnLogout');
        $btnLogout.click(function () {
            var logoutJSON = {
                status:"logout"
            };
            webSocket.send(JSON.stringify(logoutJSON));
        });

        /*
         * When btnApplayChangePass is clicked - Send JSON changePass Request To Server
         */
        var $btnApplyChangePass = $('#applyChangePass');
        $btnApplyChangePass.click(function () {
            var oldPass = $('#oldPass').val();
            var newPass = $('#newPass').val();
            var reNewPass = $('#reNewPass').val();
            if (oldPass === "" || newPass === "" || reNewPass === "") {
                alert('Không để trống các trường dữ liệu');
                return;
            }
            if (newPass != reNewPass) {
                alert('Nhập lại đúng mật khẩu mới');
                return;
            }
            var changePassJSON =  {
                status: "changepassword",
                oldpassword: oldPass,
                password: newPass
            };
            webSocket.send(JSON.stringify(changePassJSON));
            $('#oldPass').val('');
            $('#newPass').val('');
            $('#reNewPass').val('');
        });

        webSocket.onopen = function (event) {
            
        }

        /* 
         * Handle event: Listen messages from server 
         */
        webSocket.onmessage = function (event) {
            var jsonFromServer = JSON.parse(event.data);
            console.log(jsonFromServer);
            var status = jsonFromServer.status;

            /* 
             * Receieving online list from server
             * And push this list to listUserOnline
             *  thisUser is the last user in listUserOnline
             */
            if (status === "onlinelist") {
                listUserOnline.splice(0, listUserOnline.length);
                numberUser = jsonFromServer.number;
                for (var i = 0; i < jsonFromServer.userlist.length; i++) {
                    listUserOnline.push(jsonFromServer.userlist[i]);
                }
                thisUser = jsonFromServer.userlist[jsonFromServer.number-1].username;
                thisAvatarUrl = jsonFromServer.userlist[jsonFromServer.number-1].avatarurl;
            }

            /* Handle event update room */
            if (status === "update") {
                var arr = jsonFromServer.message;
                for (var i = 0; i < arr.length; i++) {
                    var roomId = arr[i].room;
                    var number = arr[i].number;
                    var $room = $('#' + roomId);
                    var $numberOnl = $room.find($('span.numbOnline'));
                    $numberOnl.html('Online: ' + number);
                }
            }

            /* Handle event change Password */
            if (status === "changepassword") {
                var mess = jsonFromServer.message;
                if (mess === "OK") alert('Đổi mật khẩu thành công');
                else if (mess === "WRONG") alert("Mật khẩu cũ không đúng");
                else if (mess === "NO") alert("Lỗi server - Vui lòng thử lại sau");
            }

            /*
             * Receiving list icon from server and push to listIcon (arr)
             */
            if (status === "iconlist") {
                listIcon.splice(0, listIcon.length);
                for (var i = 0; i < jsonFromServer.icon.length; i++) {
                    listIcon.push(jsonFromServer.icon[i]);
                }
            }

            /*
             * Receiving text from server when somebody send message
             * Then show this to list message area
             */
            if (status === "text") {
                var userSend = jsonFromServer.from;
                var avatarUrlSend = jsonFromServer.avatarurl;
                var font = jsonFromServer.font;
                var size = jsonFromServer.size;
                var mess = jsonFromServer.message;
                /* Set avatar */
                $('.avatarUser:last').attr({
                    src: avatarUrlSend    
                });
                /* When other user login */
                if (mess === "Online!") {
                    /* If userSend isn't exists on listUserOnline list, we push this user into listUserOnline */
                    if (checkUserExists(userSend) === false) {
                        listUserOnline.push({avatarurl:avatarUrlSend, username:userSend});
                    }
                    /* Clear all listUser area and reset listUser Online Area */
                    $('.listUser').empty();
                    for (var i = 0; i < listUserOnline.length; i++) {
                        showUserOnline(listUserOnline[i].avatarurl, listUserOnline[i].username);
                    }
                }
                /* When somebody logout */
                if (mess === "Offline!") {
                    var indexOfUserOff = listUserOnline.indexOf({
                        avatarurl:avatarUrlSend,
                        username:userSend
                    });
                    listUserOnline.splice(indexOfUserOff, 1);
                    $('.listUser').empty();
                    for (var i = 0; i < listUserOnline.length; i++) {
                        showUserOnline(listUserOnline[i].avatarurl, listUserOnline[i].username);
                    }
                }
                sendMessage(userSend + " said: " + mess);
            }

            /* Handle event sombody send icon */
            if (status === "icon") {
                var userSend = jsonFromServer.from;
                var avatarUrlSend = jsonFromServer.avatarurl;
                var urlImgIcon = jsonFromServer.message;
                sendIcon(urlImgIcon);
                $modal.css("display", "none");
            }

            /* Receiving response from server - Handle event change avatar */
            if (status === "avatar") {
                var messFromServer = jsonFromServer.message;
                console.log(messFromServer);
                if (messFromServer === "OK") {
                    var newAvatarUrl = jsonFromServer.avatarurl;
                    console.log(newAvatarUrl);
                }
            }

            /* Receiving response from Server - Change Pass successfully? */
            if (status === "changepassword") {
                var messFromServer = jsonFromServer.message;
                if (messFromServer === 'OKE') {
                    alert('Đổi mật khẩu thành công');
                }
                else if (messFromServer === 'WRONG') {
                    alert('Đổi mật khẩu thất bại - Kiểm tra lại mật khẩu cũ');
                }
                else if (messFromServer === 'NO') {
                    alert('Server lỗi - Vui lòng thử lại sau');
                }
            }

            /* Receiving response changeAvatar successfully */
            if (status === "avatar") {
                var messFromServer = jsonFromServer.message;
                if (messFromServer === "OK") {
                    var newAvatarUrl = jsonFromServer.avatarurl;
                    alert("Thay đổi avatar thành công");
                    $('.avatar').attr("src", newAvatarUrl);
                }
                else if (messFromServer === "EXIST") {
                    alert('Avatar đã tồn tại - Hoặc server lỗi - Thử lại sau');
                }
            }
        }

        /* Are one user exists listUserOnline ??? 
         *  @param: userName - username of User need check
         *  @return: true if user exists on list User online
         */
        function checkUserExists(userName) {
            var flag = false;
            for (var i = 0; i < listUserOnline.length; i++) {
                if(userName === listUserOnline[i].username) flag = true;
            }
            return flag;
        }

        /*
         * Current User click button send message or press enter
         * Sending a JSON to server
         */
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
         * Show listIcon in modal box when click $btnShowIcon
         */
        var $modal = $('#myModal');
        /* When the user clicks the button, open the modal and show icon from listIcon */
        $btnShowIcon.click(function(event) {
            $('.modal-content').empty();
            for (var i = 0; i < listIcon.length; i++) {
                var icon = new IconTemplate(listIcon[i]);
                icon.showIcon();
            }
            return $modal.css("display", "block");
        });
        /* When the user clicks anywhere outside of the modal, close modal */
        window.onclick = function(event) {
            var modal = document.getElementById('myModal');
            if (event.target == modal) {
                $modal.css("display", "none");
            }

        };

        /* Send Icon - Send a JSON - Request to server */
        $(document).on('click', 'img.imageIcon', function(e) {
            var urlImgIcon = $(this).attr("src");
            var sendIconJSON = {
                status:"icon",
                message:urlImgIcon
            };
            webSocket.send(JSON.stringify(sendIconJSON));
        });


        /*
         * Selected a image - Handle event change avatar
         * When an image is selected, we get filename and convert to base64
         * Sending JSON request to server
         */
        var $btnUpload = $('#uploadImg');
        var $btnChooseFile = $('#fileChooser');

        $btnChooseFile.change(function () {
            var fileName = $('input[type=file]').val().replace(/C:\\fakepath\\/i, '');
            $('#urlImage').html(fileName);
        });

        function base64(file, callback){
            var coolFile = {};
            function readerOnload(e){
                var base64 = btoa(e.target.result);
                coolFile.base64 = base64;
                callback(coolFile)
            };

            var reader = new FileReader();
            reader.onload = readerOnload;

            var file = file[0].files[0];
            coolFile.filetype = file.type;
            coolFile.size = file.size;
            coolFile.filename = file.name;
            reader.readAsBinaryString(file);
        }

        $btnUpload.click(function () {
            return base64( $('input[type="file"]'), function(data){
                var fileName = $('input[type=file]').val().split('\\').pop();
                var base64Img = data.base64;
                console.log(typeof base64Img);

                var sendChangeAvatarJSON = {
                    status:"avatar",
                    name:fileName,
                    image:base64Img
                };
                console.log(sendChangeAvatarJSON);
                return webSocket.send(JSON.stringify(sendChangeAvatarJSON));
            });
        });


    });

}.call(this));