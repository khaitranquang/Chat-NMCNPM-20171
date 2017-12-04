<%@page import="repository.UserRepository"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="entity.*,websocket.WSEnd,java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Room Information</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<link href="http://localhost:8080/BTLCNPM/css/styleRoom.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="http://localhost:8080/BTLCNPM/css/styleChat.css" type="text/css">
	<script src="http://localhost:8080/BTLCNPM/js/demo.js" type="text/javascript" charset="utf-8" async defer></script>
</head>
<body>
	<%
		User temp;
		List<Room> rooms = WSEnd.roomlist;
		if (session.getAttribute("login") != null) {
			UserRepository userRepository = WSEnd.userRepository;
			temp =(User) session.getAttribute("user");

		} else {
			temp = (User) request.getAttribute("user");
		}
		if (temp == null) {
			response.sendRedirect("/BTLCNPM/");
		}
		else{
	%>
	<div class="container-fluid">
		<div class="row">
			<!-- Left: User information and list room -->
			<div class="col-sm-2 userInformation">
				<!-- Information of user -->
				<div class="row userInfor">
					<center>
						<img class="img-responsive avatar" src=<%=temp.getAvatarUrl()%> style="border: solid 1px">
					</center>
					<h2 class="userName"><%=temp.getFullName()%></h2>
					<!-- Choose avatar Area -->
					<center>
						<input type="file" style="display: none" id="fileChooser">
						<button onclick="document.getElementById('fileChooser').click();">Choose avatar</button><br>
						<button id="uploadImg">Upload</button>
					</center>
					<span id="urlImage">Test</span> <br>
					<button id="btnChangePass" onclick="return onChangePass()"><h4>Change Password</h4></button> <br>
					<input type="password" placeholder="Enter your password" class="changePassForm" id="oldPass"><br> 
					<input type="password" placeholder="Enter new password" class="changePassForm" id="newPass"><br> 
					<input type="password" placeholder="Re-enter new password" class="changePassForm" id="reNewPass"> <br>
					<button id="applyChangePass" class="changePassForm">Apply</button>
				</div>
				
				<!-- List Room -->
				<div class="row listRoom">
					<%
						for (int i = 0; i < rooms.size(); i++) {
					%>
						<div class="row room" id="<%=i%>">
							<img src=<%=rooms.get(i).getLogourl()%> class="img-circle img-responsive logoRoom">
							<h1> <span class="titleRoom"><%=i%>: <%=rooms.get(i).getName()%></span></h1>
							<span class="description"><strong>Online: <%=rooms.get(i).getDescription()%></strong></span>
							<span class="numbOnline"><br>Online: <%=rooms.get(i).getNumberonline()%></span>
						</div>
					<%
						}
					%>
				</div>	
			</div>


			<!-- Chat Room -->
			<div class="col-sm-8">
				<div class="row">
				<div class="chat_window">
        			<!-- Menu Area -->
        			<div class="top_menu">
            			<div class="buttons">
                			<div class="button close"></div>
                			<div class="button minimize"></div>
                			<div class="button maximize"></div>
            			</div>
            			<div class="title">Welcome to Chat Room</div>
        			</div>

        			<!-- Chat Area -->
        			<ul class="messages">

        			</ul>

        			<!-- Bottom Area - Text Field -->
        			<div class="bottom_wrapper clearfix">
        				<!-- Input Message -->
            			<div class="message_input_wrapper">
                			<input class="message_input" placeholder="Type your message here...">
            			</div>

						<!-- Send Message Button -->
            			<div class="send_message">
                			<div class="iconButtonSend"></div>
                			<div class="text">Send</div>
           				</div>
						
						<!-- Send Icon Button -->
           				<div class="send_icon">	
          					<div class="text">
          						<p><span class="glyphicon glyphicon-heart btnListIcon" id="btnIcon"></span></p> 
          					</div>
           				</div>

        			</div>
    			</div>


    			<div class="message_template">
        			<li class="message">
            			<div class="avatarUserSend"></div>
            			<!-- <img class="img-circle img-responsive avatar" id="avataruser"></div> -->
            			<div class="text_wrapper">
                			<div class="text"></div>
            			</div>
       				 </li>
    			</div>
				
				<!-- list icon view Area -->
    			<div id="myModal" class="modalIcon">
  					<!-- Modal content -->
  					<ul class="modal-content">
    					<span class="close" id="btnCloseIcon">&times;</span>
  					</ul>
				</div>

				<div class="icon_template">
					<li class="icon">
						<img alt="show icon" class="img-responsive imageIcon">
					</li>
				</div>
				</div>
				
			</div>

			<!-- Online List -->
			<div class="col-sm-2 onlineList">
				<div class="row">
					<ul class="listUser">
					
					</ul>
				</div>
			</div>

			<div class="user_template">
				<li class="userOnline">
					<img alt="avatar user" class="img-responsive img-circle avatarListUser">
					<div class="nameListUser"></div>
				</li>	
			</div>

		</div>

	</div>


	<script>
		/*
		 * On/Off change pass
		 */
		var isHide = true;
		function onChangePass() {
			var $changePassForm = $('.changePassForm');
			if (isHide) {
				$changePassForm.show();
				isHide = false;
				// alert("hay lam");
			}
			else {
				$changePassForm.hide();
				isHide = true;
			}
		} 
	</script>



	<%} %>
</body>
</html>