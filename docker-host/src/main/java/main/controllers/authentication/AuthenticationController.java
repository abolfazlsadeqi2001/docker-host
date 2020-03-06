package main.controllers.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import general.authentication.Authenticator;
import general.authentication.User;

/**
 * this class handle the login and register requests (just inputs from
 * /authentication/index.html)<br>
 * <b>take care about authentication path (the static folder =>authentication/index.html for first time)<br>
 * but for validation and show error exception you have to write the templetes path(authentication)</b><br>
 * <b>each change in authentication/index.html must be applied in authentication of themplates
 * @author abolfazlsadeqi2001
 *
 */
@Controller
public class AuthenticationController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam(defaultValue = "nothing", name = "tel") String tel,
			@RequestParam(defaultValue = "nothing", name = "password") String password,
			Model model) {
		User user = new User();
		model.addAttribute(user);
		try {
			Authenticator.login(tel, password);
		} catch (Exception e) {
			user.setExceptionMessage(e.getMessage());
			return "/authentication";
		}

		return "/user-panel";
	}
	
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public String register(@RequestParam(defaultValue = "nothing", name = "tel") String tel,
			@RequestParam(defaultValue = "nothing", name = "password") String password,
			Model model) {
		User user = new User();
		model.addAttribute(user);
		try {
			Authenticator.register(tel, password);
		} catch (Exception e) {
			user.setExceptionMessage(e.getMessage());
			return "/authentication";
		}

		return "/user-panel";
	}
}
