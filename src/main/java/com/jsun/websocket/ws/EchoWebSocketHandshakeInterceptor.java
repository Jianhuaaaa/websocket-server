package com.jsun.websocket.ws;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class EchoWebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            //To solving *The extension [x-webkit-deflate-frame] is not supported* problem
            if (request.getHeaders().containsKey("Sec-WebSocket-Extensions")) {
                request.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");
            }
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;

            HttpSession session = servletRequest.getServletRequest().getSession();
            if (session == null) {
                return false;
            }
            //To separate WebSocketHandlers
            HttpServletRequest req = servletRequest.getServletRequest();
            String xShopId = req.getParameter("wsId");
            String xAppId = req.getParameter("appId");

            if (StringUtils.isEmpty(xAppId) || StringUtils.isEmpty(xShopId)) {
                return false;
            }
            attributes.put("wsId", xShopId);
            return super.beforeHandshake(request, response, wsHandler, attributes);
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {
        super.afterHandshake(request, response, wsHandler, exception);
    }
}
