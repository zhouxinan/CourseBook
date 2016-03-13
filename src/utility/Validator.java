package utility;

import exception.LoginServletException;
import java.util.regex.*;

public class Validator {
	public static boolean validateUsername(String username)
			throws LoginServletException {
		int usernameLength = username.length();
		if (usernameLength == 0) {
			throw new LoginServletException("请输入用户名！");
		}
		if (usernameLength < 2 || usernameLength > 16) {
			throw new LoginServletException("用户名长度必须是2～16个字符！");
		}
		return true;
	}

	public static boolean validatePassword(String password)
			throws LoginServletException {
		int passwordLength = password.length();
		if (passwordLength == 0) {
			throw new LoginServletException("请输入密码！");
		}
		if (passwordLength < 6 || passwordLength > 16) {
			throw new LoginServletException("密码长度必须是6～16个字符！");
		}
		Pattern pattern = Pattern.compile("[0-9]+");
		Matcher matcher = pattern.matcher(password);
		if (matcher.matches() == true) {
			throw new LoginServletException("密码不允许是纯数字！");
		}
		pattern = Pattern.compile("[A-Za-z0-9]+");
		matcher = pattern.matcher(password);
		if (matcher.matches() == false) {
			throw new LoginServletException("密码只能包含数字和字母！");
		}
		return true;
	}

	public static boolean validateEmail(String email)
			throws LoginServletException {
		if (email.length() == 0) {
			throw new LoginServletException("请输入邮箱！");
		}
		Pattern pattern = Pattern
				.compile("[a-zA-Z0-9]+@([a-zA-Z0-9]+\\.)+([a-zA-Z0-9]+)");
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches() == false) {
			throw new LoginServletException("邮箱格式错误！");
		}
		return true;
	}

}
