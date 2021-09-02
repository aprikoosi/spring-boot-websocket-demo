package com.aprikoosi.demo.websocket;

import javax.websocket.*;

/**
 * @Author: liufeng
 * @Date: 2021/8/26 18:33
 * @Description:
 */
@ClientEndpoint
public class WebsocketClientEndpoint {

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Connected to endpoint: " + session.getBasicRemote());
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println(message);
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}
}
