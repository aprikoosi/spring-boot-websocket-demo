package com.aprikoosi.demo.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @Author: liufeng
 * @Date: 2021/8/26 17:16
 * @Description: WebSocket Server -> 【接收连接类】
 */
/*
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 *注解的值用来指定【Endpoint：接收消息的终端地址】将被用于监听用户连接的终端访问URL地址,
 * 客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint(value = "/ws/server/{userId}")
public class WebSocketServerEndpoint {

	private Logger logger = LoggerFactory.getLogger(WebSocketServerEndpoint.class);

	private static String userId;

	//连接时执行
	@OnOpen
	public void onOpen(@PathParam("userId") String userId, Session session) throws IOException {
		if (WebSocketServerEndpoint.userId == null) {
			if (userId !=null) {
				WebSocketServerEndpoint.userId = userId;
			}
			else {
				userId = session.getRequestParameterMap().get("userId").get(0);
				WebSocketServerEndpoint.userId = userId;
			}
		}
		logger.debug("新连接：{}",WebSocketServerEndpoint.userId);
	}

	//关闭时执行
	@OnClose
	public void onClose(){
		logger.debug("连接：{} 关闭", WebSocketServerEndpoint.userId );
	}

	//收到消息时执行
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		logger.debug("收到用户{}的消息{}", WebSocketServerEndpoint.userId ,message);
		session.getBasicRemote().sendText("<span style='color: green;'>收到用户ID: "+ WebSocketServerEndpoint.userId +" 的消息: " + message+"</span>"); //回复用户
	}

	//连接错误时执行
	@OnError
	public void onError(Session session, Throwable error){
		logger.debug("用户id为：{}的连接发送错误", WebSocketServerEndpoint.userId );
		error.printStackTrace();
	}
}
