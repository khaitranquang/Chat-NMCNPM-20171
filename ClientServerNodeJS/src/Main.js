"use strict";
exports.__esModule = true;
var express = require("express");
var http = require("http");
var SocketIO = require("socket.io");
var User_1 = require("./User");
var Main = /** @class */ (function () {
    function Main() {
        var _this = this;
        this.app = express();
        this.server = http.createServer(this.app);
        this.io = SocketIO(this.server);
        this.port = 3000;
        this.users = [];
        this.onListen = function () {
            console.log('Server listening at port %d', _this.port);
        };
        this.onConnect = function (socket) {
            _this.users.push(new User_1.User(socket));
        };
        // Routing
        this.app.use(express.static(__dirname + '/../public'));
        // Chatroom
        this.io.on('connection', this.onConnect);
        this.server.listen(this.port, this.onListen);
    }
    Main.numUsers = 0;
    return Main;
}());
exports.Main = Main;
new Main();
