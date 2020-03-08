package main.filters.authentication;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import main.filters.FilterParent;
import main.general.authentication.AuthenticatorFront;

/**
 * this class prepare a filter which works for includation expressions and
 * doesn't work for excludation expressions<br>
 * <b>For each path that you make or change you have to check it on the
 * includation or excludation expressions or not</b>
 * 
 * @author abolfazlsadeqi2001
 *
 */
@Component
@Order(1)
public class AuthenticationFilter extends FilterParent implements Filter {

	/**
	 * in
	 * {@link main.controllers.authentication.AuthenticationController#login(String, String, org.springframework.ui.Model)}
	 * 
	 * and
	 * 
	 * in
	 * {@link main.controllers.authentication.AuthenticationController#register(String, String, org.springframework.ui.Model)}
	 * 
	 * if default value changes for in isInIncludation condition must changed
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String path = httpRequest.getRequestURI();

		if (isInExcludation(path)) {
			chain.doFilter(request, response);
			return;// do not execute the following codes
		}

		if (isInIncludation(path)) {
			try {
				// get all cookies then fill the password cookie and telephone cookie but
				// 'correct values' (default value ='nothing')
				Cookie[] cookies = httpRequest.getCookies();
				// I place the cookies in exception so that if we have null pointer exception
				// for reading cookies it handle then go to authentication path
				Cookie passwordCookie = new Cookie("password", "nothing");
				Cookie telephoneCookie = new Cookie("telephone", "nothing");
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("telephone")) {
						telephoneCookie = new Cookie("telephone", cookie.getValue());
					}

					if (cookie.getName().equals("password")) {
						passwordCookie = new Cookie("password", cookie.getValue());
					}
				}
				// placed on authenticator front if it has any problem (Exception) mean our
				// values wrong and must redirect to authentication page
				AuthenticatorFront.login(telephoneCookie.getValue(), passwordCookie.getValue());
				// continue to your path
				chain.doFilter(request, response);
			} catch (Exception e) {
				httpResponse.sendRedirect("/authentication");
			}
		}
	}

	@Override
	protected String[] excludationPathExpression() {
		return new String[] { "^/login$", "^/register$", "^/authentication$", "^/favicon$" };
	}

	@Override
	protected String[] includationPathExpression() {
		return new String[] { ".*" };
	}
}
