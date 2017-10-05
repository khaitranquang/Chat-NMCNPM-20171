/**
 * Created by Tran Quang Khai on 10/5/2017.
 */
/* Demo Client */
var net = require('net');

var client = net.connect(1010, 'localhost');

client.write('Hello from node.js');

client.end();