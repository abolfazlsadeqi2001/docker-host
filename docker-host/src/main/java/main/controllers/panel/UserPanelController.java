package main.controllers.panel;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import main.general.authentication.AuthenticatorFront;
import main.general.authentication.models.User;

@Controller
public class UserPanelController {
	// TODO write on incorrect test must go to authentication and have a div error otherwise have correct value on panel
	@RequestMapping("/panel")
	public String getPanel(@CookieValue(name = "telephone",required = true) String telephone,
			@CookieValue(name = "password",required = true) String password,
			Model model) {
		try {
			User user = AuthenticatorFront.login(telephone, password);
			model.addAttribute(user);
			return "user-panel";
		} catch (Exception e) {
			User user = new User();
			user.setExceptionMessage(e.getMessage());
			return "authentication";
		}
	}
	
	// TODO write test for this
	@RequestMapping(value="/charge")
	public String charge (@RequestParam(name="telephone",required=true) String telephone,
			@RequestParam(name="password",required=true) String password,
			@RequestParam(name="cost",required=true)int cost,
			Model model) {
		try {
			User user = AuthenticatorFront.login(telephone, password);
			user.addMoney(cost);
			model.addAttribute(user);
			return "user-panel";
		} catch (Exception e) {
			User user = new User();
			user.setExceptionMessage(e.getMessage());
			model.addAttribute(user);
			return "authentication";
		}
	}
}
