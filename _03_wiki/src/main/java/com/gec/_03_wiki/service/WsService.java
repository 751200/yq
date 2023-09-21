package com.gec._03_wiki.service;

import com.gec._03_wiki.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WsService {

    @Autowired
    private WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String message,String logID){
        MDC.put("LOG_ID",logID);
        webSocketServer.sendInfo(message);
    }
}
