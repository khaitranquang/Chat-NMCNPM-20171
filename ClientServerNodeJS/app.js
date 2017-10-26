/**
 * Created by Tran Quang Khai on 10/5/2017.
 */
/* Demo Client */
// var net = require('net');
//
// var client = net.connect(1010, 'localhost');
//
// client.write('Hello from node.js');
//
// client.end();
var express = require("express");
var appDemo = express();
appDemo.use(express.static("./public"));
appDemo.set("view engine", "ejs");
appDemo.set("views", "./views");

var server  = require("http").Server(appDemo);
var io = require("socket.io")(server);
server.listen(3000);

// Listen connection event
io.on("connection", function (socket) {
    console.log("co ai do ket noi..." + socket.id);

    // Disconnect event
    socket.on("disconnect", function () {
       console.log(socket.id + " was disconnected");
    });

    // Tham so data cua function chinh la du lieu client gui len
    socket.on("Client-send-data", function (data) {
        console.log(socket.id + " vua gui " + data);
        // Emit to all client
        //io.sockets.emit("Server-send-data", data + "888");
        // Emit toi chinh no
        //socket.emit("Server-send-data", data + "888");
        // Broadcast
        socket.broadcast.emit("Server-send-data", data + "888");
    });
});



appDemo.get("/", function (req, res) {
    res.render("trangchu");
});