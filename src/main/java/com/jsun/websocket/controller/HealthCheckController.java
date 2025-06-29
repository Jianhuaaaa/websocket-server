package com.jsun.websocket.controller;

import com.jsun.websocket.ws.EchoWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1/healthCheck", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
public class HealthCheckController {
    @Autowired
    EchoWebSocketHandler echoWebSocketHandler;

    @GetMapping(value = "/inform")
    public String inform() throws IOException {
        log.info("calling api: /api/v1/healthCheck/inform");
        echoWebSocketHandler.sendMessage("001", "Communication between server and client");
        return "server";
    }
}
