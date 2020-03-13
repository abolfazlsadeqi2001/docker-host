package main.controllers.authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.general.authentication.AuthenticatorFront;
import main.general.authentication.models.User;

/**
 * this class handle the login and register requests (just inputs from
 * /authentication)<br>
 * @author abolfazlsadeqi2001
 * If the default value for telephone and password or their names on cookie change you have to change on the following classes
 * @see
 * {@link main.filters.authentication.AuthenticationFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
 */
@Controller
public class AuthenticationController {

	@RequestMapping("/authentication")
	public String authentication(Model model) {
		User user = new User();
		model.addAttribute(user);
		return "/authentication";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam(defaultValue = "nothing", name = "telephone") String telephone,
			@RequestParam(defaultValue = "nothing", name = "password") String password,
			HttpServletResponse res,
			Model model) {
		User user = new User();
		model.addAttribute(user);
		try {
			AuthenticatorFront.login(telephone, password);
			// save cookies
			Cookie telephoneCookie = new Cookie("telephone",telephone);
			Cookie passwordCookie = new Cookie("password",password);
			res.addCookie(telephoneCookie);
			res.addCookie(passwordCookie);
			// go to user panel
			return "/user-panel";
		} catch (Exception e) {
			user.setExceptionMessage(e.getMessage());
			return "/authentication";
		}
	}
	
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public String register(@RequestParam(defaultValue = "nothing", name = "telephone") String telephone,
			@RequestParam(defaultValue = "nothing", name = "password") String password,
			@RequestParam(defaultValue = "nothing", name = "name") String name,
			@RequestParam(defaultValue = "nothing", name = "family") String family,
			@RequestParam(defaultValue = "0" , name = "age") int age,
			HttpServletResponse res,
			Model model) {
		try {
			User user = AuthenticatorFront.register(telephone, password,name,family,age);
			// save cookies
			Cookie telephoneCookie = new Cookie("telephone",telephone);
			Cookie passwordCookie = new Cookie("password",password);
			res.addCookie(telephoneCookie);
			res.addCookie(passwordCookie);
			// setup model then go to user panel
			model.addAttribute(user);
			return "/user-panel";
		} catch (Exception e) {
			User user = new User();
			user.setExceptionMessage(e.getMessage());
			model.addAttribute(user);
			return "/authentication";
		}
	}
}
