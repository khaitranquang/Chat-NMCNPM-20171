package controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import repository.UserRepository;

@SuppressWarnings("serial")
@WebServlet("/register")
public class RegisterServlet extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UserRepository userRepository = (UserRepository) getServletContext().getAttribute("userRepository");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String age = req.getParameter("age");
		String fullname = req.getParameter("fullname");
		byte[] bytes = fullname.getBytes(StandardCharsets.ISO_8859_1);
		fullname = new String(bytes, StandardCharsets.UTF_8);
		
		bytes = username.getBytes(StandardCharsets.ISO_8859_1);
		username = new String(bytes, StandardCharsets.UTF_8);
		bytes = password.getBytes(StandardCharsets.ISO_8859_1);
		password = new String(bytes, StandardCharsets.UTF_8);
		System.out.println(fullname);
		if(userRepository.getUserByUserName(username)==null) {
			try {
				userRepository.saveUser(username, password, "http://localhost:8080/BTLCNPM//logo/bk-logo.png", Integer.parseInt(age), fullname);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				resp.getWriter().write("Nope");
			}
			resp.sendRedirect("/BTLCNPM/");
		}
		else {
			resp.getWriter().write("Có r");
		}
	}

}
