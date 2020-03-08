package main.controllers.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import main.general.authentication.AuthenticatorFront;
import main.general.authentication.User;

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
			Model model) {
		User user = new User();
		model.addAttribute(user);
		try {
			AuthenticatorFront.login(telephone, password);
		} catch (Exception e) {
			user.setExceptionMessage(e.getMessage());
			return "/authentication";
		}

		return "/user-panel";
	}
	
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public String register(@RequestParam(defaultValue = "nothing", name = "telephone") String telephone,
			@RequestParam(defaultValue = "nothing", name = "password") String password,
			Model model) {
		User user = new User();
		model.addAttribute(user);
		try {
			AuthenticatorFront.register(telephone, password);
		} catch (Exception e) {
			user.setExceptionMessage(e.getMessage());
			return "/authentication";
		}

		return "/user-panel";
	}
}
