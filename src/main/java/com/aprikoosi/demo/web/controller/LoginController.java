package com.aprikoosi.demo.web.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: liufeng
 * @Date: 2021/8/27 9:55
 * @Description:
 */
@Controller
public class LoginController {

	@RequestMapping("login")
	public String login(HttpServletRequest request, @RequestParam("username") String username) {
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		return "redirect:index.html";
	}

	@RequestMapping("index")
	public String index() {
		return "index.html";
	}
}
