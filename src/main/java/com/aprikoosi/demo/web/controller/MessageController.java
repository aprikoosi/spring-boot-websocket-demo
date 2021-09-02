package com.aprikoosi.demo.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.aprikoosi.demo.websocket.handler.CustomWebSocketHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

/**
 * @Author: liufeng
 * @Date: 2021/8/27 9:56
 * @Description:
 */
@Controller
@RequestMapping("/message")
public class MessageController {

	@Resource
	private CustomWebSocketHandler customWebSocketHandler;

	@RequestMapping
	public String view() {
		return "message";
	}

	@RequestMapping("/send")
	@ResponseBody
	public String send(HttpServletRequest request, @RequestParam("username") String username) throws IOException {
		TextMessage message = new TextMessage(request.getParameter("message"));
		customWebSocketHandler.sendMessageToUser(username, message);
		return "true";
	}
}
