package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.Dao;
import bean.User;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/FileUploadServlet")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// upload settings
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

	private String uploadPath;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileUploadServlet() {
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
		// checks if the user has logged in
		request.setCharacterEncoding("utf-8");
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		// checks if the request actually contains upload file
		if (!ServletFileUpload.isMultipartContent(request)) {
			PrintWriter out = response.getWriter();
			out.println("Error: Your form must have enctype=multipart/form-data.");
			out.close();
			return;
		}
		// configures upload settings
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// sets memory threshold - beyond which files are stored in disk
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		// sets temporary location to store files
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		ServletFileUpload upload = new ServletFileUpload(factory);

		// sets maximum size of upload file
		upload.setFileSizeMax(MAX_FILE_SIZE);

		// sets maximum size of request (include file + form data)
		upload.setSizeMax(MAX_REQUEST_SIZE);

		Dao dao = Dao.getInstance();
		String action = request.getParameter("action");
		String newAnswerContent = "";
		int questionID = 0;
		// constructs the directory path to store upload file
		// this path is relative to application's directory
		if (action.equals("uploadUserAvatar")) {
			uploadPath = getServletContext().getRealPath("") + File.separator
					+ "img/avatar";
		} else if (action.equals("addAnswerWithImage")) {
			uploadPath = getServletContext().getRealPath("") + File.separator
					+ "img/upload";
		}
		// creates the directory if it does not exist
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}

		try {
			// parses the request's content to extract file data
			List<FileItem> formItems = upload.parseRequest(request);
			if (formItems != null && formItems.size() > 0) {
				// iterates over form's fields
				for (FileItem item : formItems) {
					if (item.isFormField()) {
						String fieldName = item.getFieldName();
						if (fieldName.equals("newAnswerContent")) {
							newAnswerContent = item.getString("UTF-8");
						} else if (fieldName.equals("questionID")) {
							questionID = Integer.parseInt(item.getString());
						}
					}
				}
				for (FileItem item : formItems) {
					if (!item.isFormField()) {
						String fileName = UUID.randomUUID().toString();
						String filePath = uploadPath + File.separator
								+ fileName;
						File storeFile = new File(filePath);
						// saves the file on disk
						item.write(storeFile);
						if (action.equals("uploadUserAvatar")) {
							dao.modifyAvatarPath(user, fileName);
							request.getSession().setAttribute("user",
									dao.getUserByID("" + user.getUserID())); // renew
																				// session.
							request.getSession().setAttribute(
									"saveAvatarResponseMessage", "头像修改成功!");
							response.sendRedirect("settings.jsp");
						} else if (action.equals("addAnswerWithImage")) {
							newAnswerContent += "<img src=\"img/upload/"
									+ fileName + "\" />";
							dao.addAnswer(user, questionID, newAnswerContent);
							response.sendRedirect("question.jsp?id="
									+ questionID);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
