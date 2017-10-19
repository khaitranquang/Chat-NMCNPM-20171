/**
 * Created by User on 10/10/2017.
 */
/**
 * Created by Tran Quang Khai on 10/6/2017.
 */
var socket = io("http://localhost:4000");

// Register - failed
socket.on("server-send-register-fail", function () {
    alert("Tên username đã tồn tại");
});

// Register - success
socket.on("server-send-register-success", function (data) {
    $("#currentUser").html(data);
    $("#loginForm").hide(1000);
    $("#chatForm").show(2000);
});

// Client listens event "Server- send-list-users"
socket.on("server-send-list-users", function (data) {
    $("#boxContent").html("");
    data.forEach(function (i) {
        $("#boxContent").append("<div class='user'>" + i + "</div>");
    });

});

// Listen server send message
socket.on("server-send-message", function (data) {
    $("#listMessages").append("<div class='message'>" + data.username+ " :" + data.mess);
});

// Listen event who is typing
// socket.on("server-send-who-is-typing", function (data) {
//     $("#status").html(data);
// });
//
// // Listen event "Stop Typing"
// socket.on("server-send-who-is-typing", function () {
//     $("#status").html("*******************");
// });

$(document).ready(function () {
    $("#loginForm").show();
    $("#chatForm").hide();

    $("#btnRegister").click(function () {
        // Emit username to server (node)
        socket.emit("client-send-username", $("#txtUsername").val());
    });

    $("#btnLogout").click(function () {
        $("#txtMessages").val("");
        $("#listMessages").val("");
        // Emit to server: I want to logout
        socket.emit("i-want-to-logout");
        $("#chatForm").hide(1000);
        $("#loginForm").show(1000);

    });

    $("#btnSendMessage").click(function (data) {
        socket.emit("user-send-message", $("#txtMessages").val());
        $("#txtMessages").val('');
    });

    // $("#txtMessages").focusin(function () {
    //     socket.emit("i-am-typing");
    // });
    //
    // $("#txtMessages").focusout(function () {
    //     socket.emit("stop-typing");
    // });
});