package com.example.config;


import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.List;
import java.util.Map;

public class HandShakeHandler implements HandshakeHandler {
    private final DefaultHandshakeHandler delegate;

    public HandShakeHandler(DefaultHandshakeHandler delegate) {
        this.delegate = delegate;
    }

    public WebSocketHandler getDummyWebSocketHandler() {
        return new TextWebSocketHandler();
    }

    @Override
    public boolean doHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws HandshakeFailureException {
        List<String> requestedProtocols = new WebSocketHttpHeaders(request.getHeaders()).getSecWebSocketProtocol();
        String selectedProtocol = selectProtocol(requestedProtocols);
        if (selectedProtocol != null) {
            new WebSocketHttpHeaders(response.getHeaders()).setSecWebSocketProtocol(selectedProtocol);
        }
        System.out.println(requestedProtocols);
        return delegate.doHandshake(request,response,wsHandler,attributes);
    }

    private String selectProtocol(List<String> requestedProtocols) {
        if (requestedProtocols.contains("ocpp1.6")) {
            return "ocpp1.6";
        }
        return null;
    }

}