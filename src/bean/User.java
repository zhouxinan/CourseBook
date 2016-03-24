package bean;

public class User {
	private int userID;
	private String username;
	private String email;
	private String avatarPath;
	private String motto;
	private int type;
	private int state;
	public static final int STATE_ENABLED = 0;
	public static final int STATE_DISABLED = 1;
	public static final int STATE_DELETED = 2;
	public static final int TYPE_STUDENT = 0;
	public static final int TYPE_TEACHER = 1;
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
