package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.Validator;
import bean.User;
import dao.Dao;
import exception.LoginServletException;

/**
 * Servlet implementation class SettingsServlet
 */
@WebServlet("/SettingsServlet")
public class SettingsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SettingsServlet() {
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
		if (user == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		if (action.equals("modifyPassword")) {
			String oldPassword = request.getParameter("oldPassword");
			String newPassword = request.getParameter("newPassword");
			String newPasswordRepeat = request
					.getParameter("newPasswordRepeat");
			PrintWriter out = response.getWriter();
			try {
				if (!newPassword.equals(newPasswordRepeat)) {
					out.print("两次输入的新密码不一致！");
					return;
				}
				if (newPassword.equals("")) {
					out.print("新密码不能为空！");
					return;
				}
				Validator.validatePassword(newPassword);
				if (dao.modifyPassword(user, oldPassword, newPassword)) {
					out.print("修改密码成功！");
				} else {
					out.print("旧密码错误！");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LoginServletException e) {
				// TODO Auto-generated catch block
				out.print(e.getMessage());
			} finally {
				out.close();
			}
		} else if (action.equals("modifyMotto")) {
			PrintWriter out = response.getWriter();
			String motto = request.getParameter("motto");
			try {
				out.print(dao.modifyMotto(user, motto));
				request.getSession().setAttribute("user",
						dao.getUserByID("" + user.getUserID())); // renew
																	// session.
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.close();
		}
	}

}
