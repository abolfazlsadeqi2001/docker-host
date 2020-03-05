package main.controllers.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * this class handle the login and register requests (just inputs from /authentication/index.html)
 * @author abolfazlsadeqi2001
 *
 */
@Controller
public class AuthenticationController {
	
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public String login(@RequestParam(defaultValue = "nothing",name="tel") String tel,
			@RequestParam(defaultValue = "nothing",name="password") String password) {
		return "redirect: /authentication/index.html";
	}
}
