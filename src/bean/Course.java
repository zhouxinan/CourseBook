package bean;

public class Course {
	private int courseID;
	private String courseSN;
	private String courseName;
	private int courseCredit;
	private int teacherID;
	private String teacherName;
	private int sectionID;
	private double rate;

	public int getCourseID() {
		return courseID;
	}

	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}

	public String getCourseSN() {
		return courseSN;
	}

	public void setCourseSN(String courseSN) {
		this.courseSN = courseSN;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCourseCredit() {
		return courseCredit;
	}

	public void setCourseCredit(int courseCredit) {
		this.courseCredit = courseCredit;
	}

	public int getTeacherID() {
		return teacherID;
	}

	public void setTeacherID(int teacherID) {
		this.teacherID = teacherID;
	}

	public int getSection() {
		return sectionID;
	}

	public void setSection(int sectionID) {
		this.sectionID = sectionID;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
}
