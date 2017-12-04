package config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import websocket.WSEnd;




@WebListener
public class Config implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute("userRepository", WSEnd.userRepository);
        event.getServletContext().setAttribute("roomRepository", WSEnd.roomRepository);
    }

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}