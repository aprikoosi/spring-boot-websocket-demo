package com.aprikoosi.demo.websocket.handler;

import com.aprikoosi.demo.entity.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: liufeng
 * @Date: 2021/8/26 18:46
 * @Description:
 */

@Component
public class CustomWebSocketHandler extends AbstractWebSocketHandler {
	private static final Map<String, WebSocketSession> userSocketSessionMap;
	static {
		userSocketSessionMap = new HashMap<>();
	}
	/**
	 * 建立连接后
	 */
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		/*
		 * 链接成功后会触发此方法，可在此处对离线消息什么的进行处理
		 */
		userSocketSessionMap.put(session.getId(), session);
		String username = (String) session.getAttributes().get("username");
		System.out.println(username + " connect success ...");
		session.sendMessage(new TextMessage(username + " 链接成功!!"));
	}
	/**
	 * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
	 */
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		if (message.getPayloadLength() == 0)
			return;
		Message msg = new Gson().fromJson(message.getPayload().toString(), Message.class);
		msg.setDate(new Date());
		this.sendMessageToUser(msg.getTo(), new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
	}

	/**
	 * 消息传输错误处理
	 */
	public void handleTransportError(WebSocketSession session,Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		System.err.println(exception.getMessage());
		System.out.println("websocket connection closed......");
		// 移除Socket会话
		userSocketSessionMap.remove(session.getId());
	}

	/**
	 * 关闭连接后
	 */
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		System.out.println("Websocket:" + session.getId() + "已经关闭");
		Iterator<Map.Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// 移除Socket会话
		while (it.hasNext()) {
			Map.Entry<String, WebSocketSession> entry = it.next();
			if (entry.getValue().getId().equals(session.getId())) {
				userSocketSessionMap.remove(entry.getKey());
				System.out.println("Socket会话已经移除:用户ID" + entry.getKey());
				break;
			}
		}
	}

	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给所有在线用户发送消息
	 *
	 * @param message
	 * @throws IOException
	 */
	public void broadcast(final TextMessage message) throws IOException {
		Iterator<Map.Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// 多线程群发
		while (it.hasNext()) {
			final Map.Entry<String, WebSocketSession> entry = it.next();
			if (entry.getValue().isOpen()) {
//				 entry.getValue().sendMessage(message);
				new Thread(new Runnable() {
					public void run() {
						try {
							if (entry.getValue().isOpen()) {
								entry.getValue().sendMessage(message);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
	}

	/**
	 * 给某个用户发送消息
	 *
	 * @param message
	 * @throws IOException
	 */
	public void sendMessageToUser(String uid, TextMessage message)throws IOException {
		WebSocketSession session = userSocketSessionMap.get(uid);
		if (session != null && session.isOpen()) {
			session.sendMessage(message);
		}
	}
}
