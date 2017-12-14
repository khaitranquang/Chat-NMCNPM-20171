package controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.User;
import repository.UserRepository;
import websocket.WSEnd;

@SuppressWarnings("serial")
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserRepository userRepository = (UserRepository) getServletContext().getAttribute("userRepository");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		byte[] bytes = username.getBytes(StandardCharsets.UTF_8);
		username = new String(bytes, StandardCharsets.UTF_8);
		bytes = password.getBytes(StandardCharsets.UTF_8);
		password = new String(bytes, StandardCharsets.UTF_8);
		HttpSession session = req.getSession();
		if (userRepository.checkUser(username, password)) {
			boolean flag = true;
			for (String str : WSEnd.onlineList) {
				if (str.equals(username)) {
					flag = false;
				}
			}
			if (flag) {
				session.setAttribute("login", "logined");
				User user = userRepository.getUserByUserName(username);
				req.setAttribute("user", user);
				session.setAttribute("user", user);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/pages/room.jsp");
				dispatcher.forward(req, resp);
			}
			else {
				resp.sendRedirect("/BTLCNPM/");
			}
		} else {
			resp.sendRedirect("/BTLCNPM/");

		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("bam");
		HttpSession session = req.getSession();
		if (session.getAttribute("login") != null) {
			resp.sendRedirect("/BTLCNPM/pages/room.jsp");
		} else {
			resp.sendRedirect("/BTLCNPM/");
		}
	}
}
