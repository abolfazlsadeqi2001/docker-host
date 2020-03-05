package main.controllers.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import general.authentication.Authenticator;

/**
 * this class handle the login and register requests (just inputs from
 * /authentication/index.html)
 * 
 * @author abolfazlsadeqi2001
 *
 */
@Controller
public class AuthenticationController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam(defaultValue = "nothing", name = "tel") String tel,
			@RequestParam(defaultValue = "nothing", name = "password") String password) {
		try {
			Authenticator.login(tel, password);
		} catch (Exception e) {
			// change to template path then write the exception as model attribute
			return "redirect:/authentication/index.html";
		}

		// TODO change it to user panel address
		return "/user/panel";
	}
	
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public String register(@RequestParam(defaultValue = "nothing", name = "tel") String tel,
			@RequestParam(defaultValue = "nothing", name = "password") String password) {
		try {
			Authenticator.register(tel, password);
		} catch (Exception e) {
			// change to template path then write the exception as model attribute
			return "redirect:/authentication/index.html";
		}

		// TODO change it to user panel address
		return "/user/panel";
	}
}
