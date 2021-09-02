package com.aprikoosi.demo.websocket.config;

import com.aprikoosi.demo.websocket.WebSocketServerEndpoint;
import com.aprikoosi.demo.websocket.handler.CustomWebSocketHandler;
import com.aprikoosi.demo.websocket.interceptor.CustomHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;

/**
 * @Author: liufeng
 * @Date: 2021/8/26 18:44
 * @Description:
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig  implements WebSocketConfigurer {
	@Resource
	CustomWebSocketHandler handler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(handler, "/ws/server").addInterceptors(new CustomHandshakeInterceptor());
		registry.addHandler(handler, "/ws/server/sockJs").addInterceptors(new CustomHandshakeInterceptor()).withSockJS();
	}

	@Bean
	public CustomWebSocketHandler textMessageHandler() {
		return new CustomWebSocketHandler();
	}
	/**
	 * 这个是我们自己定义的加了 @ServerEndpoint注解的类，
	 * 注入到 Spring 容器中，交给 Spring容器管理生命周期
	 * @return {@link WebSocketServerEndpoint}
	 */
	@Bean
	public WebSocketServerEndpoint webSocketServerEndpoint() {
		return new WebSocketServerEndpoint();
	}

	/**
	 * 【服务器节点】
	 * ！！！重要
	 * 注入这个bean实例后，会自动注册使用了@ServerEndpoint注解声明的对象,没有的话会报404
	 *
	 * 注：如果使用独立的servlet容器，而不是直接使用springboot的内置容器，
	 * 就不要注入ServerEndpointExporter，因为它将由容器自己提供和管理，
	 * 这里是因为我用的 springboot的内置Tomcat，所以需要注入
	 * @return {@link ServerEndpointExporter}
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
