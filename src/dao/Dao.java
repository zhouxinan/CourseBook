package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import bean.Course;
import bean.User;
import exception.LoginServletException;
import org.json.*;

public class Dao {
	private static String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://127.0.0.1:3306/CourseBook?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss E");

	// your username and password
	String dbUsername = "root";
	String dbPassword = "balalaxiaomoxian";

	private static Dao dao;

	private Dao() {
	};

	public static Dao getInstance() {
		if (dao == null) {
			dao = new Dao();
		}

		return dao;
	}

	static {
		try {
			Class.forName(driver).newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public User login(String email, String password) throws SQLException, LoginServletException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm
					.executeQuery("select * from user where email='" + email + "' and password = '" + password + "'");
			if (results.next()) {
				if (results.getInt("user_state") == User.STATE_DISABLED)
					throw new LoginServletException("你的账号暂未启用");
				if (results.getInt("user_state") == User.STATE_DELETED)
					throw new LoginServletException("你的账号已注销");
				User user = new User();
				user.setUserID(results.getInt("userID"));
				user.setUsername(results.getString("username"));
				user.setEmail(email);
				user.setAvatarPath(results.getString("avatarPath"));
				user.setMotto(results.getString("motto"));
				user.setType(results.getInt("user_type"));
				return user;
			} else {
				throw new LoginServletException("邮箱或密码错误");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public User register(String username, String email, String password) throws SQLException, LoginServletException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery("select * from user where username='" + username + "'");
			if (results.next()) {
				throw new LoginServletException("用户名已被注册");
			}
			results.close();
			results = sm.executeQuery("select * from user where email='" + email + "'");
			if (results.next()) {
				throw new LoginServletException("邮箱已被注册");
			}
			results.close();
			// default avatar path is default.jpg
			sm.executeUpdate("insert into user(username, password, email, avatarPath, motto) values('" + username
					+ "', '" + password + "', '" + email + "', 'default.jpg', '这个人太懒，什么都没说……')");
			results = sm.executeQuery("select * from user where email='" + email + "'");
			if (results.next()) {
				User user = new User();
				user.setUserID(results.getInt("userID"));
				user.setUsername(username);
				user.setEmail(email);
				user.setAvatarPath(results.getString("avatarPath"));
				user.setMotto(results.getString("motto"));
				return user;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public User getUserByID(String userID) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery("select * from user where userID='" + userID + "'");
			if (results.next()) {
				User user = new User();
				user.setUserID(results.getInt("userID"));
				user.setUsername(results.getString("username"));
				user.setEmail(results.getString("email"));
				user.setAvatarPath(results.getString("avatarPath"));
				user.setMotto(results.getString("motto"));
				return user;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public JSONObject follow(User user, int toUserID) throws SQLException {
		int fromUserID = user.getUserID();
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			sm.executeUpdate(
					"insert into follows(fromUserID, toUserID) values('" + fromUserID + "', '" + toUserID + "')");
			return getFollowInfo(user, toUserID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public JSONObject defollow(User user, int toUserID) throws SQLException {
		int fromUserID = user.getUserID();
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			sm.executeUpdate(
					"delete from follows where fromUserID='" + fromUserID + "' and toUserID='" + toUserID + "'");
			return getFollowInfo(user, toUserID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public boolean checkFollow(User user, int toUserID) throws SQLException {
		int fromUserID = user.getUserID();
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"select * from follows where fromUserID='" + fromUserID + "' and toUserID='" + toUserID + "'");
			if (results.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return false;
	}

	public JSONObject getFollowInfo(User user, int userID) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		JSONObject returnObj = new JSONObject();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			if (user != null) {
				results = sm.executeQuery("select * from follows where fromUserID='" + user.getUserID()
						+ "' and toUserID='" + userID + "'");
				if (results.next()) {
					returnObj.put("isFollowed", true);
				} else {
					returnObj.put("isFollowed", false);
				}
				results.close();
			}
			results = sm.executeQuery("select COUNT(*) as followerCount from follows where toUserID='" + userID + "'");
			if (results.next()) {
				returnObj.put("followerCount", results.getString("followerCount"));
			}
			results.close();
			results = sm
					.executeQuery("select COUNT(*) as followingCount from follows where fromUserID='" + userID + "'");
			if (results.next()) {
				returnObj.put("followingCount", results.getString("followingCount"));
			}
			results.close();
			results = sm.executeQuery(
					"select * from follows where fromUserID='" + userID + "' ORDER BY followTime DESC LIMIT 5");
			List<JSONObject> followingList = new LinkedList<JSONObject>();
			while (results.next()) {
				JSONObject obj = new JSONObject();
				String toUserID = results.getString("toUserID");
				obj.put("userID", toUserID);
				User toUser = getUserByID(toUserID);
				obj.put("avatarPath", toUser.getAvatarPath());
				followingList.add(obj);
			}
			results.close();
			returnObj.put("followingList", followingList);
			results = sm.executeQuery(
					"select * from follows where toUserID='" + userID + "' ORDER BY followTime DESC LIMIT 5");
			List<JSONObject> followerList = new LinkedList<JSONObject>();
			while (results.next()) {
				JSONObject obj = new JSONObject();
				String fromUserID = results.getString("fromUserID");
				obj.put("userID", fromUserID);
				User fromUser = getUserByID(fromUserID);
				obj.put("avatarPath", fromUser.getAvatarPath());
				followerList.add(obj);
			}
			returnObj.put("followerList", followerList);
			return returnObj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<JSONObject> getPopularUserList(int popularUserNumber) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"select * from user inner join (SELECT toUserID, COUNT(*) as popularity from follows GROUP BY toUserID) as A on user.userID = A.toUserID ORDER BY popularity DESC LIMIT "
							+ popularUserNumber);
			List<JSONObject> popularUserList = new LinkedList<JSONObject>();
			while (results.next()) {
				JSONObject obj = new JSONObject();
				obj.put("userID", results.getString("userID"));
				obj.put("avatarPath", results.getString("avatarPath"));
				obj.put("username", results.getString("username"));
				obj.put("motto", results.getString("motto"));
				popularUserList.add(obj);
			}
			return popularUserList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public boolean modifyPassword(User user, String oldPassword, String newPassword) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		int userID = user.getUserID();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery("select password from user where userID='" + userID + "'");
			if (results.next()) {
				String password = results.getString("password");
				if (password.equals(oldPassword)) {
					results.close();
					sm.executeUpdate("UPDATE user SET password='" + newPassword + "' WHERE userID=" + userID);
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return false;
	}

	public boolean modifyMotto(User user, String newMotto) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		int userID = user.getUserID();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			sm.executeUpdate("UPDATE user SET motto='" + newMotto + "' WHERE userID=" + userID);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return false;
	}

	public boolean modifyAvatarPath(User user, String newAvatarPath) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		int userID = user.getUserID();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			sm.executeUpdate("UPDATE user SET avatarPath='" + newAvatarPath + "' WHERE userID=" + userID);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return false;
	}

	public int addMessage(User user, String receiverUsername, String content) throws SQLException {
		Connection con = null;
		Statement sm = null;
		PreparedStatement psm = null;
		ResultSet results = null;
		int fromUserID = user.getUserID();
		content = content.replace("'", "''");
		int newMessageID = 0;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery("select userID from user where username='" + receiverUsername + "'");
			if (results.next()) {
				String toUserID = results.getString("userID");
				results.close();
				psm = con.prepareStatement("insert into messages(fromUserID, toUserID, content) values('" + fromUserID
						+ "', '" + toUserID + "', '" + content + "')", Statement.RETURN_GENERATED_KEYS);
				psm.executeUpdate();
				results = psm.getGeneratedKeys();
				if (results.next()) {
					newMessageID = results.getInt(1);
					return newMessageID;
				}
			} else {
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return -1;
	}

	public List<JSONObject> getAllMessagesToUser(User user, boolean isRead) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		int toUserID = user.getUserID();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery("select * from messages where toUserID='" + toUserID + "' and isRead='"
					+ (isRead ? 1 : 0) + "' ORDER BY sendTime DESC");
			List<JSONObject> messageList = new LinkedList<JSONObject>();
			while (results.next()) {
				JSONObject obj = new JSONObject();
				String fromUserID = results.getString("fromUserID");
				User fromUser = getUserByID(fromUserID);
				obj.put("userID", fromUserID);
				obj.put("username", fromUser.getUsername());
				obj.put("avatarPath", fromUser.getAvatarPath());
				obj.put("messageID", results.getString("messageID"));
				obj.put("content", results.getString("content"));
				messageList.add(obj);
			}
			return messageList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<JSONObject> getAllMessagesFromUser(User user) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		int fromUserID = user.getUserID();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"select * from messages where fromUserID='" + fromUserID + "' ORDER BY sendTime DESC");
			List<JSONObject> messageList = new LinkedList<JSONObject>();
			while (results.next()) {
				JSONObject obj = new JSONObject();
				String toUserID = results.getString("toUserID");
				User toUser = getUserByID(toUserID);
				obj.put("userID", toUserID);
				obj.put("username", toUser.getUsername());
				obj.put("avatarPath", toUser.getAvatarPath());
				obj.put("messageID", results.getString("messageID"));
				obj.put("content", results.getString("content"));
				messageList.add(obj);
			}
			return messageList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public JSONObject getMessageByID(User user, String messageID) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		int userID = user.getUserID();
		JSONObject messageObj = new JSONObject();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			sm.executeUpdate(
					"UPDATE messages SET isRead=1 WHERE toUserID='" + userID + "' and messageID='" + messageID + "'");
			results = sm.executeQuery("select * from messages where messageID='" + messageID + "' and (fromUserID='"
					+ userID + "' or toUserID='" + userID + "')");
			if (results.next()) {
				String fromUserID = results.getString("fromUserID");
				User fromUser = getUserByID(fromUserID);
				messageObj.put("fromUserID", fromUserID);
				messageObj.put("fromUsername", fromUser.getUsername());
				messageObj.put("fromUserAvatarPath", fromUser.getAvatarPath());
				messageObj.put("content", results.getString("content"));
			}
			return messageObj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public String getUnreadMessageCount(User user) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		int userID = user.getUserID();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery("select count(*) from messages where toUserID='" + userID + "' and isRead='0'");
			if (results.next()) {
				String count = results.getString("count(*)");
				return count;
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public Course getCourseByID(int courseID) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		Course course = new Course();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"select * from course INNER JOIN user on course.teacherID=user.userID where courseID='" + courseID
							+ "'");
			if (results.next()) {
				course.setCourseID(courseID);
				course.setCourseSN(results.getString("courseSN"));
				course.setCourseName(results.getString("courseName"));
				course.setCourseCredit(results.getInt("courseCredit"));
				course.setTeacherID(results.getInt("teacherID"));
				course.setTeacherName(results.getString("username"));
			} else {
				return null;
			}
			results.close();
			results = sm.executeQuery(
					"select avg(rate) from (select rate from answers where courseID=" + courseID + ") as A");
			if (results.next()) {
				course.setRate(results.getDouble("avg(rate)"));
			} else {
				return null;
			}
			results.close();
			results = sm.executeQuery("select * from course_section where courseID='" + courseID + "'");
			ArrayList<Integer> sectionList = new ArrayList<Integer>();
			while (results.next()) {
				sectionList.add(results.getInt("sectionID"));
			}
			course.setSectionList(sectionList);
			return course;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<JSONObject> getAnswer(int courseID, int startingIndex, int numberOfAnswers) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"select * from answers as A left join (select answerID, COUNT(*) as replyCount from replies GROUP BY answerID) as B on A.answerID=B.answerID where courseID='"
							+ courseID + "' ORDER BY A.answerID ASC LIMIT " + startingIndex + "," + numberOfAnswers);
			List<JSONObject> answerList = new LinkedList<JSONObject>();
			while (results.next()) {
				JSONObject obj = new JSONObject();
				String userID = results.getString("userID");
				User user = getUserByID(userID);
				obj.put("answerID", results.getString("answerID"));
				obj.put("userID", userID);
				obj.put("username", user.getUsername());
				obj.put("avatarPath", user.getAvatarPath());
				obj.put("motto", user.getMotto());
				obj.put("content", results.getString("content"));
				obj.put("answerTime", dateFormat.format(results.getTimestamp("answerTime")));
				obj.put("replyCount", results.getInt("replyCount"));
				obj.put("rate", results.getInt("rate"));
				answerList.add(obj);
			}
			return answerList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public JSONObject addAnswer(User user, int courseID, String content, int rate) throws SQLException {
		Connection con = null;
		PreparedStatement sm = null;
		ResultSet results = null;
		content = content.replace("'", "''");
		int userID = user.getUserID();
		int newAnswerID = 0;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.prepareStatement("insert into answers(courseID, userID, content, rate) values('" + courseID
					+ "', '" + userID + "', '" + content + "', '" + rate + "')", Statement.RETURN_GENERATED_KEYS);
			sm.executeUpdate();
			results = sm.getGeneratedKeys();
			if (results.next()) {
				newAnswerID = results.getInt(1);
			}
			results.close();
			results = sm.executeQuery("select * from answers where answerID='" + newAnswerID + "'");
			if (results.next()) {
				JSONObject obj = new JSONObject();
				obj.put("answerID", newAnswerID);
				obj.put("userID", userID);
				obj.put("username", user.getUsername());
				obj.put("avatarPath", user.getAvatarPath());
				obj.put("motto", user.getMotto());
				obj.put("content", results.getString("content"));
				obj.put("answerTime", dateFormat.format(results.getTimestamp("answerTime")));
				obj.put("replyCount", 0);
				obj.put("rate", results.getInt("rate"));
				return obj;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<JSONObject> getReply(int answerID) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery("select * from replies where answerID='" + answerID + "' ORDER BY replyID DESC");
			List<JSONObject> replyList = new LinkedList<JSONObject>();
			while (results.next()) {
				JSONObject obj = new JSONObject();
				String userID = results.getString("userID");
				User user = getUserByID(userID);
				obj.put("userID", userID);
				obj.put("username", user.getUsername());
				obj.put("avatarPath", user.getAvatarPath());
				obj.put("content", results.getString("content"));
				obj.put("replyTime", dateFormat.format(results.getTimestamp("replyTime")));
				replyList.add(obj);
			}
			return replyList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<JSONObject> addReply(User user, int answerID, String content) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		content = content.replace("'", "''");
		int userID = user.getUserID();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			sm.executeUpdate("insert into replies(answerID, userID, content) values('" + answerID + "', '" + userID
					+ "', '" + content + "')");
			return getReply(answerID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<JSONObject> getAnswerListByUserID(int userID) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"select * from answers natural join course inner join user on course.teacherID=user.userID where answers.userID='"
							+ userID + "' ORDER BY answerID DESC");
			List<JSONObject> answerList = new LinkedList<JSONObject>();
			while (results.next()) {
				JSONObject obj = new JSONObject();
				obj.put("courseID", results.getString("courseID"));
				obj.put("courseName", results.getString("courseName"));
				obj.put("courseSN", results.getString("courseSN"));
				obj.put("teacherName", results.getString("userName"));
				obj.put("content", results.getString("content"));
				obj.put("answerTime", dateFormat.format(results.getTimestamp("answerTime")));
				answerList.add(obj);
			}
			return answerList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<Course> getCourseListByTeacherID(int userID) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"select * from course inner join user on course.teacherID = user.userID where teacherID='" + userID
							+ "' ORDER BY courseID DESC");
			List<Course> courseList = new LinkedList<Course>();
			while (results.next()) {
				Course course = new Course();
				course.setCourseID(results.getInt("courseID"));
				course.setCourseSN(results.getString("courseSN"));
				course.setCourseName(results.getString("courseName"));
				course.setTeacherName(results.getString("username"));
				courseList.add(course);
			}
			return courseList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<Course> getPopularCourseList(int numberOfPopularCourses) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"select * from course inner join user on course.teacherID=user.userID natural join (SELECT courseID, COUNT(*) as popularity from user_course GROUP BY courseID) as a order by popularity DESC LIMIT "
							+ numberOfPopularCourses);
			List<Course> courseList = new LinkedList<Course>();
			while (results.next()) {
				Course course = new Course();
				course.setCourseID(results.getInt("courseID"));
				course.setCourseSN(results.getString("courseSN"));
				course.setCourseName(results.getString("courseName"));
				course.setTeacherName(results.getString("username"));
				courseList.add(course);
			}
			return courseList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<Course> searchCourseByKeyword(String keyword) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"select * from course inner join user on course.teacherID=user.userID where courseName like '%"
							+ keyword + "%' or courseSN like '%" + keyword + "%' or user.username like '%" + keyword
							+ "%'");
			List<Course> courseList = new LinkedList<Course>();
			while (results.next()) {
				Course course = new Course();
				course.setCourseID(results.getInt("courseID"));
				course.setCourseSN(results.getString("courseSN"));
				course.setCourseName(results.getString("courseName"));
				course.setTeacherName(results.getString("username"));
				courseList.add(course);
			}
			return courseList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<User> searchUsernameByKeyword(String keyword) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery("select * from user where username like '%" + keyword + "%'");
			List<User> userList = new LinkedList<User>();
			while (results.next()) {
				User user = new User();
				user.setUserID(results.getInt("userID"));
				user.setUsername(results.getString("username"));
				user.setAvatarPath(results.getString("avatarPath"));
				user.setMotto(results.getString("motto"));
				userList.add(user);
			}
			return userList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<JSONObject> getTrendEntryListForUser(User user) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		int userID = user.getUserID();
		List<JSONObject> trendEntryList = new LinkedList<JSONObject>();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery("select * from answers inner join (select * from follows where fromUserID='"
					+ userID
					+ "') as C on C.toUserID=answers.userID natural join user as B natural join course inner join user as A on course.teacherID=A.userID ORDER BY answerTime");
			while (results.next()) {
				JSONObject obj = new JSONObject();
				obj.put("isFollowingCourse", "0");
				obj.put("userID", results.getString("userID"));
				obj.put("avatarPath", results.getString("avatarPath"));
				obj.put("username", results.getString("B.username"));
				obj.put("courseID", results.getString("courseID"));
				obj.put("courseSN", results.getString("courseSN"));
				obj.put("courseName", results.getString("courseName"));
				obj.put("teacherName", results.getString("A.username"));
				obj.put("content", results.getString("content"));
				obj.put("time", dateFormat.format(results.getTimestamp("answerTime")));
				trendEntryList.add(obj);
			}
			results.close();
			results = sm.executeQuery(
					"SELECT * from user_course as A inner join answers as B on A.courseID=B.courseID inner join user as C on B.userID=C.userID inner join course as D on D.courseID = B.courseID inner join user as E on E.userID=D.teacherID where A.userID='"
							+ userID + "' order by answerTime desc");
			while (results.next()) {
				JSONObject obj = new JSONObject();
				obj.put("isFollowingCourse", "1");
				obj.put("userID", results.getString("B.userID"));
				obj.put("avatarPath", results.getString("avatarPath"));
				obj.put("username", results.getString("C.username"));
				obj.put("courseID", results.getString("courseID"));
				obj.put("courseSN", results.getString("courseSN"));
				obj.put("courseName", results.getString("courseName"));
				obj.put("teacherName", results.getString("E.username"));
				obj.put("content", results.getString("content"));
				obj.put("time", dateFormat.format(results.getTimestamp("answerTime")));
				trendEntryList.add(obj);
			}
			// Collections.sort(trendEntryList, new MyComparator());
			return trendEntryList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<JSONObject> getDiscoveryEntryListForUser() throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		List<JSONObject> discoveryEntryList = new LinkedList<JSONObject>();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"select * from answers natural join user as B natural join course inner join user as A on course.teacherID=A.userID ORDER BY answerTime");
			while (results.next()) {
				JSONObject obj = new JSONObject();
				obj.put("time", dateFormat.format(results.getTimestamp("answerTime")));
				obj.put("courseName", results.getString("courseName"));
				obj.put("courseSN", results.getString("courseSN"));
				obj.put("courseID", results.getString("courseID"));
				obj.put("userID", results.getString("userID"));
				obj.put("username", results.getString("B.username"));
				obj.put("avatarPath", results.getString("avatarPath"));
				obj.put("teacherName", results.getString("A.username"));
				obj.put("content", results.getString("content"));
				discoveryEntryList.add(obj);
			}
			// Collections.sort(discoveryEntryList, new MyComparator());
			return discoveryEntryList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public JSONObject getChartDataForCourseAnswerRate(int courseID) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		JSONObject obj = new JSONObject();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"SELECT rate, COUNT(*) as rate_count from answers where courseID=" + courseID + " group by rate");
			while (results.next()) {
				obj.put(results.getString("rate"), results.getString("rate_count"));
			}
			return obj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public boolean followCourse(User user, int courseID) throws SQLException {
		int userID = user.getUserID();
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			sm.executeUpdate("insert into user_course(userID, courseID) values('" + userID + "', '" + courseID + "')");
			return isFollowingCourse(user, courseID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return false;
	}

	public boolean isFollowingCourse(User user, int courseID) throws SQLException {
		int userID = user.getUserID();
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"SELECT * from user_course where userID='" + userID + "' and courseID='" + courseID + "'");
			if (results.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return false;
	}

	public boolean defollowCourse(User user, int courseID) throws SQLException {
		int userID = user.getUserID();
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			sm.executeUpdate("delete from user_course where userID='" + userID + "' and courseID='" + courseID + "'");
			return isFollowingCourse(user, courseID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return false;
	}

	public JSONObject getSectionGrade(Course course, int sectionID) throws SQLException {
		Connection con = null;
		Statement sm = null;
		ResultSet results = null;
		JSONObject returnObj = new JSONObject();
		try {
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			sm = con.createStatement();
			results = sm.executeQuery(
					"select student_grade,COUNT(*) as count from grade natural join (SELECT * from course_section where courseID='"
							+ course.getCourseID() + "' and sectionID = '" + sectionID
							+ "') as A group by student_grade order by student_grade desc");
			while (results.next()) {
				returnObj.put(results.getString("student_grade"), results.getString("count"));
			}
			results.close();
			results = sm.executeQuery(
					"select COUNT(*) as count from grade natural join (SELECT * from course_section where courseID='"
							+ course.getCourseID() + "' and sectionID = '" + sectionID + "') as A");
			if (results.next()) {
				returnObj.put("count", results.getString("count"));
			}
			return returnObj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sm != null) {
				sm.close();
			}
			if (con != null) {
				con.close();
			}
			if (results != null) {
				results.close();
			}
		}
		return null;
	}
}