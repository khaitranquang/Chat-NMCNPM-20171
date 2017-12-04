<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
             "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>WsChat</title>
<script type="text/javascript">
	var websocket = new WebSocket("ws://localhost:8080/BTLCNPM/chatroom");
	websocket.onmessage = function processMessage(message){
		var jsonData =JSON.parse(message.data);
		if(jsonData.message!=null) messagesTextArea += jsonData.message +"\n";
	}
	function sendMessage(){
		websocket.send(messageText.value);
		messageText="";
	}
</script>
</head>
<body>
<textarea id="messagesTextArea" readonly="readonly" rows="10" cols="45"></textarea><br></br>
<input type="text" id="messageText" size="50"/>
<input type="button" value="Chat" onclick="sendMessage();"/>
</body>
</html>