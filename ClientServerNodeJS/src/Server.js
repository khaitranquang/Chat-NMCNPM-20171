
/**
 * Created by Tran Quang Khai on 10/6/2017.
 */
var express = require("express");
var app = express();
app.use(express.static("./public"));
app.set("view engine", "ejs");
app.set("views", "./public");

var server  = require("http").Server(app);
var io = require("socket.io")(server);
server.listen(3000);

var users = [];


// Listen connection event
io.on("connection", function (socket) {
    console.log("Co ai do vua ket noi " + socket.id);

    // Listen when client click btnRegister (send username)
    socket.on("client-send-username", function (data) {
        console.log(data);
        //Username is exist
        if (users.indexOf(data) >= 0) {
            //Fail
            socket.emit("server-send-register-fail");
        }
        else {
            // Success
            users.push(data);
            // Declare a property username of socket
            socket.username = data;
            socket.emit("server-send-register-success", data);
            // Server emit all users to all client
            io.sockets.emit("server-send-list-users", users);
        }
    });

    socket.on("i-want-to-logout", function () {
        // Remove client want to logout
        users.splice(users.indexOf(socket.username), 1);
        // Broadcast
        socket.broadcast.emit("server-send-list-users", users);
    });
});


app.get("/", function (req, res) {
    res.render("home");
});