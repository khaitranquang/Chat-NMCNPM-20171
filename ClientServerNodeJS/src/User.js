"use strict";
exports.__esModule = true;
var Main_1 = require("./Main");
var User = /** @class */ (function () {
    function User(socket) {
        var _this = this;
        this.socket = socket;
        this.addedUser = false;
        this.onAddUser = function (username) {
            if (_this.addedUser)
                return;
            // we store the username in the socket session for this client
            _this.username = username;
            ++Main_1.Main.numUsers;
            _this.addedUser = true;
            _this.socket.emit('login', {
                numUsers: Main_1.Main.numUsers
            });
            // echo globally (all clients) that a person has connected
            _this.socket.broadcast.emit('user joined', {
                username: _this.username,
                numUsers: Main_1.Main.numUsers
            });
        };
        this.onNewMsg = function (data) {
            _this.socket.broadcast.emit('new message', {
                username: _this.username,
                message: data
            });
        };
        this.onTyping = function () {
            _this.socket.broadcast.emit('typing', {
                username: _this.username
            });
        };
        this.onStopTyping = function () {
            _this.socket.broadcast.emit('stop typing', {
                username: _this.username
            });
        };
        this.onDisconnect = function () {
            if (_this.addedUser) {
                --Main_1.Main.numUsers;
                // echo globally that this client has left
                _this.socket.broadcast.emit('user left', {
                    username: _this.username,
                    numUsers: Main_1.Main.numUsers
                });
            }
        };
        socket.on('new message', this.onNewMsg);
        socket.on('add user', this.onAddUser);
        socket.on('typing', this.onTyping);
        socket.on('stop typing', this.onStopTyping);
        socket.on('disconnect', this.onDisconnect);
    }
    return User;
}());
exports.User = User;
