package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.Validator;
import dao.Dao;
import exception.LoginServletException;
import bean.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		if (action.equals("logout")) {
			request.getSession().setAttribute("user", null);
			response.sendRedirect("login.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		Dao dao = Dao.getInstance();
		String action = request.getParameter("action");
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = null;
		if (action.equals("login")) {
			try {
				Validator.validateEmail(email);
				Validator.validatePassword(password);
				user = dao.login(email, password);
				request.getSession().setAttribute("user", user);
				response.sendRedirect("index.jsp"); // Jump to index.jsp if
													// login succeeds.
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LoginServletException e) {
				// TODO Auto-generated catch block
				request.getSession().setAttribute("error", e.getMessage());
				response.sendRedirect("login.jsp");
			}
		} else if (action.equals("register")) {
			try {
				Validator.validateUsername(username);
				Validator.validateEmail(email);
				Validator.validatePassword(password);
				user = dao.register(username, email, password);
				request.getSession().setAttribute("user", user);
				response.sendRedirect("discovery.jsp"); // Jump to discovery.jsp
														// if registry succeeds.
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LoginServletException e) {
				// TODO Auto-generated catch block
				request.getSession().setAttribute("error", e.getMessage());
				response.sendRedirect("login.jsp");
			}
		} else {
			response.sendRedirect("login.jsp");
		}
	}
}
