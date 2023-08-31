package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    HandShakeHandler handShakeHandler = new HandShakeHandler(new DefaultHandshakeHandler());

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(handShakeHandler.getDummyWebSocketHandler(), "/chat/*")
                .setHandshakeHandler(handShakeHandler)
                .setAllowedOrigins("*")
                .addInterceptors(ocppInterceptor());

        
    }



    @Bean
    public HandshakeInterceptor ocppInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request,
                                           ServerHttpResponse response,
                                           WebSocketHandler webSocketHandler,
                                           Map<String, Object> map) throws Exception {

                // Get the URI segment corresponding to the auction id during handshake
                String path = request.getURI().getPath();
                List<String> req = new WebSocketHttpHeaders(request.getHeaders()).getSecWebSocketProtocol();
                System.out.println("Protocol = " + req);
                String cp = path.substring(path.lastIndexOf('/') + 1);
                System.out.println("Charge Point Id is => " + cp);
                // This will be added to the websocket session
                map.put("cpId", cp);

                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

            }
        };
    }

}