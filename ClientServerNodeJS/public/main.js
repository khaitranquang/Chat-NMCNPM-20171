/**
 * Created by Tran Quang Khai on 10/6/2017.
 */
$(function () {
    var FADE_TIME = 150;    //ms
    var TYPING_TIME_LENGTH = 400;
    var CORLOR = [
        '#e21400', '#91580f', '#f8a700', '#f78b00',
        '#58dc00', '#287b00', '#a8f07a', '#4ae8c4',
        '#3b88eb', '#3824aa', '#a700ff', '#d300e7'
    ];

    //Initialize variables
    var $window = $(window);
    var $usernameInput = $('#txtUsername');   //Input for username
    var $messages = $('#listMessages');       //Message Area
    var $inputMessage = $('#inputMessages');    //Input message input box

    var $loginPage = $('#loginForm');
    var $chatPage = $('#chatForm');

    //Prompt for setting a username
    var username;
    var connected = false;
    var typing = false;
    var lastTypingTime;
    var $currentInput = $usernameInput.focus();

    var socket = io();

    // function addParticipantsMessage(data) {
    //     var message = '';
    //     if (data.numUsers === 1) {
    //         message += "There's 1 participant";
    //     }
    //     else {
    //         message += "There are " + data.numUsers + "participants";
    //     }
    //     log(message);
    // }
    //
    // // Set the client's username
    // function setUsername() {
    //     ////////////////////////////////////////////////////
    //     username =  cleanInput($usernameInput.val().trim());
    //     /////////////////////////////////////////////////////
    //
    //     //If the username is valid
    //     if (username) {
    //         $loginPage.fadeOut();
    //         $chatPage.show();
    //         $loginPage.off('click');    // Remove an event handler
    //         $currentInput = $inputMessage.focus();
    //
    //         //Tell the server your username
    //         socket.emit('add user', username);
    //     }
    // }
    //
    // // Sends a chat message
    // function sendMessage() {
    //     var message = $inputMessage.val();
    //     // Prevent markup from being injected into the message
    //     message = clearInput(message);
    //     /////////////////////////////////////////////////
    //
    //     // if there is a non-empty message and a socket connection
    //     if(message && connected) {
    //         $inputMessage.val('');
    //         //Add Chat Message - add a JSON
    //         addChatMessage({
    //             username: username,
    //             message: message
    //         });
    //         //Tell server to execute 'new message' and send along one parameter
    //         socket.emit('new message', message);
    //     }
    // }
    //
    // // Log a message
    // function log(message, options) {
    //
    //
    //
    // }
    //





});