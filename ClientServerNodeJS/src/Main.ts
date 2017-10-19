import * as express from "express";
import * as http from "http";
import * as SocketIO from "socket.io";
import Socket = SocketIO.Socket;
import {User} from "./User";

export class Main {
    app = express();
    server = http.createServer(this.app);
    io = SocketIO(this.server);
    port = process.env.PORT || 3000;

    // Array saves all users
    users = [];
    static numUsers = 0;

    constructor() {
        // Routing
        this.app.use(express.static(__dirname + '/../public'));
        // Chat room
        this.io.on('connection', this.onConnect);
        this.server.listen(this.port, this.onListen);
    }

    onListen = () => {
        console.log('Server is listening at port %d', this.port);
    };

    onConnect = (socket: Socket) => {
        this.users.push(new User(socket));
    };

}

new Main();