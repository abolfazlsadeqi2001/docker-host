package main.controllers.panel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserPanelController {
	@RequestMapping("/panel")
	public String getPanel() {
		return "user-panel";
	}
}
