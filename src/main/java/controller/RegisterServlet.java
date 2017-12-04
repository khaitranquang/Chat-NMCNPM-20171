package controller;

import java.io.IOException;
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
		if(userRepository.getUserByUserName(username)==null) {
			try {
				userRepository.saveUser(username, password, "http://localhost:8080/BTLCNPM//logo/bk-logo.png", 18, "Ano");
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
