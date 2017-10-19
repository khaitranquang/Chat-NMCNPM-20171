import Socket = SocketIO.Socket;
import {Main} from "./Main";
export class User {
    addedUser = false;
    username: string;

    constructor(public socket: Socket) {
        //socket.on('login');



        socket.on('add user', this.onAddUser);
        socket.on('new message', this.onNewMsg);
        // socket.on('typing', this.onTyping);
        // socket.on('stop typing', this.onStopTyping);

        socket.on('disconnect', this.onDisconnect);

    }

    onAddUser = (username: string) => {
        if (this.addedUser) return;

        // We store the username in the socket session for this client
        this.username = username;
        ++Main.numUsers;
        this.addedUser = true;
        this.socket.emit('login', {
           numUsers: Main.numUsers
        });

        // echo globally (all client) that a person has connected
        this.socket.broadcast.emit('user joined', {
            username: this.username,
            numUsers: Main.numUsers
        });
    }



}