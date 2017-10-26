/**
 * Created by Tran Quang Khai on 10/6/2017.
 */
var socket = io("http://localhost:3000");

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

$(document).ready(function () {
    $("#loginForm").show();
    $("#chatForm").hide();

    $("#btnRegister").click(function () {
        // Emit username to server (node)
        socket.emit("client-send-username", $("#txtUsername").val());
    });

    $("#btnLogout").click(function () {
        // Emit to server: I want to logout
        socket.emit("i-want-to-logout");
        $("#chatForm").hide(1000);
        $("#loginForm").show(2000);
    });

});