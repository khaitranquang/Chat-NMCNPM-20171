<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <meta charset="utf-8">
    <title>Chat</title>
    <link rel="stylesheet" href="css/styleChat.css" type="text/css">
    <script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>

</head>

<body>
    <div class="chat_window">
        <!-- Menu Area -->
        <div class="top_menu">
            <div class="buttons">
                <div class="button close"></div>
                <div class="button minimize"></div>
                <div class="button maximize"></div>
            </div>
            <div class="title">Welcome to Chat Room</div>
        </div>

        <!-- Chat Area -->
        <ul class="messages">

        </ul>

        <!-- Bottom Area - Text Field -->
        <div class="bottom_wrapper clearfix">
            <div class="message_input_wrapper">
                <input class="message_input" placeholder="Type your message here...">
            </div>
            <div class="send_message">
                <div class="icon"></div>
                <div class="text">Send</div>
            </div>
        </div>
    </div>

    <div class="message_template">
        <li class="message">
            <div class="avatar"></div>
            <div class="text_wrapper">
                <div class="text"></div>
            </div>
        </li>
    </div>

    <!--<script>-->
        <!--/**-->
         <!--* Created by Tran Quang Khai on 11/23/2017.-->
         <!--*/-->
        <!--$(document).ready(function () {-->

            <!--var websocket = new WebSocket("ws://localhost:8080/BTLCNPM/chatroom");-->
            <!--/*-->
             <!--* Receiving messages (here is JSON) from server-->
             <!--*/-->
            <!--websocket.onmessage = function (event) {-->

                <!--var jsonFromServer = JSON.parse(event.data);-->
                <!--var statusFromServer = jsonFromServer.status;-->

                <!--switch (statusFromServer) {-->
                    <!--case "confirm":-->
                        <!--var messFromServer = jsonFromServer.message;-->
                        <!--if (messFromServer == "OKE") {-->
                            <!--/* Set list icon from server */-->
                            <!--//.......-->
                        <!--}-->
                        <!--break;-->

                    <!--case "text":-->
                        <!--var from = jsonFromServer.from;-->
                        <!--var message = jsonFromServer.message;-->
                        <!--sendMessage(from + ": " + message);-->
                        <!--break;-->
                <!--}-->
            <!--};-->

            <!--/* Define all variables */-->
            <!--var currentUser = localStorage.getItem("currentUser");-->
            <!--var $btnSend = $('.send_message');-->
            <!--var listIcon;-->


            <!--/* Define a Message object */-->
            <!--var Message;-->
            <!--Message = function (arg) {-->
                <!--this.text = arg.text, this.message_side = arg.message_side;-->
                <!--this.draw = function (_this) {-->
                    <!--return function () {-->
                        <!--var $message;-->
                        <!--$message = $($('.message_template').clone().html());-->
                        <!--$message.addClass(_this.message_side).find('.text').html(_this.text);-->
                        <!--$('.messages').append($message);-->
                        <!--return setTimeout(function () {-->
                            <!--return $message.addClass('appeared');-->
                        <!--}, 0);-->
                    <!--};-->
                <!--}(this);-->
                <!--return this;-->
            <!--};-->

            <!--/* Get text from input */-->
            <!--var getMessageText, message_side='left', sendMessage, showNotification;-->
            <!--getMessageText = function () {-->
                <!--var $message_input;-->
                <!--$message_input = $('.message_input');-->
                <!--return $message_input.val();-->
            <!--};-->

            <!--sendMessage = function (text) {-->
                <!--var $messages, message;-->
                <!--if (text.trim() === '') {-->
                    <!--return;-->
                <!--}-->
                <!--$('.message_input').val('');-->
                <!--$messages = $('.messages');-->
                <!--message_side = 'left';-->
                <!--message = new Message({-->
                    <!--text: text,-->
                    <!--message_side: message_side-->
                <!--});-->
                <!--message.draw();-->
                <!--return $messages.animate({ scrollTop: $messages.prop('scrollHeight') }, 300);-->
            <!--};-->

            <!--/* Show notifications when user login or log out */-->
            <!--showNotification = function (notification) {-->
                <!--return $('.messages').append(notification);-->
            <!--};-->



            <!--/*-->
             <!--* Send event 'chat' - Send messages to server-->
             <!--* Then server send messages to all users-->
             <!--*/-->
            <!--$btnSend.click(function () {-->
                <!--var jsonClient = {-->
                    <!--status:"text",-->
                    <!--user:currentUser,-->
                    <!--message:getMessageText(),-->
                    <!--room:0-->
                    <!--//from:currentUser;-->
                <!--};-->
                <!--websocket.send(JSON.stringify(jsonClient));-->
                <!--return sendMessage(getMessageText());-->
            <!--});-->

            <!--$('.message_input').keyup(function (e) {-->
                <!--if (e.which === 13) {-->
                    <!--var jsonClient = {-->
                        <!--status:"text",-->
                        <!--user:currentUser,-->
                        <!--message:getMessageText(),-->
                        <!--room:0-->
                        <!--//from:currentUser;-->
                    <!--};-->
                    <!--websocket.send(JSON.stringify(jsonClient));-->
                    <!--return sendMessage(getMessageText());-->
                <!--}-->
            <!--});-->

        <!--});-->
    <!--</script>-->
    <script src="js/main.js"></script>

</body>
</html>