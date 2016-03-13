package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import dao.Dao;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet("/MessageServlet")
public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MessageServlet() {
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		Dao dao = Dao.getInstance();
		String action = request.getParameter("action");

		if (action.equals("addMessage")) {
			String receiverUsername = request.getParameter("receiverUsername");
			String content = request.getParameter("content");
			try {
				PrintWriter out = response.getWriter();
				out.print(dao.addMessage(user, receiverUsername, content));
				out.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (action.equals("getAllSentMessages")) {
			try {
				PrintWriter out = response.getWriter();
				out.println(dao.getAllMessagesFromUser(user));
				out.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (action.equals("getAllReadMessages")) {
			try {
				PrintWriter out = response.getWriter();
				out.println(dao.getAllMessagesToUser(user, true));
				out.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (action.equals("getAllUnreadMessages")) {
			try {
				PrintWriter out = response.getWriter();
				out.println(dao.getAllMessagesToUser(user, false));
				out.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (action.equals("getUnreadMessageCount")) {
			try {
				PrintWriter out = response.getWriter();
				out.print(dao.getUnreadMessageCount(user));
				out.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (action.equals("getMessageByID")) {
			String messageID = request.getParameter("messageID");
			try {
				PrintWriter out = response.getWriter();
				out.print(dao.getMessageByID(user, messageID));
				out.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
