package bean;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Question {
	private int questionID;
	private int userID;
	private String username;
	private String title;
	private String content;
	private String time;

	public int getQuestionID() {
		return questionID;
	}

	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(Timestamp timestamp) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy年MM月dd日 HH:mm:ss E");
		this.time = dateFormat.format(timestamp);
	}
}
