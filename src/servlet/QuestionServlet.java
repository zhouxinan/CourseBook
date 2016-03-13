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
 * Servlet implementation class QuestionServlet
 */
@WebServlet("/QuestionServlet")
public class QuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuestionServlet() {
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
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		Dao dao = Dao.getInstance();
		String action = request.getParameter("action");
		User user = (User) request.getSession().getAttribute("user");
		if (action.equals("getAnswer")) {
			String questionID = request.getParameter("questionID");
			String startingIndex = request.getParameter("startingIndex");
			String numberOfAnswers = request.getParameter("numberOfAnswers");
			try {
				PrintWriter out = response.getWriter();
				out.println(dao.getAnswer(Integer.parseInt(questionID),
						Integer.parseInt(startingIndex),
						Integer.parseInt(numberOfAnswers)));
				out.close();
				return;
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (action.equals("getReply")) {
			String answerID = request.getParameter("answerID");
			try {
				PrintWriter out = response.getWriter();
				if (user != null) {
					out.println(dao.getReply(Integer.parseInt(answerID)));
				} else {
					out.println("-1");
				}
				out.close();
				return;
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (user == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		if (action.equals("addQuestion")) {
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			try {
				int newQuestionID = dao.addQuestion(user, title, content);
				response.sendRedirect("question.jsp?id=" + newQuestionID);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (action.equals("addReply")) {
			String answerID = request.getParameter("answerID");
			String content = request.getParameter("content");
			try {
				PrintWriter out = response.getWriter();
				out.println(dao.addReply(user, Integer.parseInt(answerID),
						content));
				out.close();
				return;
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (action.equals("addAnswer")) {
			String questionID = request.getParameter("questionID");
			String content = request.getParameter("content");
			try {
				PrintWriter out = response.getWriter();
				out.println(dao.addAnswer(user, Integer.parseInt(questionID),
						content));
				out.close();
				return;
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
