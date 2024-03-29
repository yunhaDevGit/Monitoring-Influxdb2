package com.example.monitoringinfluxdb2.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class StompHandler implements ChannelInterceptor {

  @Override
  public void postSend(Message message, MessageChannel channel, boolean sent) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    String sessionId = accessor.getSessionId();
    switch (accessor.getCommand()) {
      case CONNECT:
        System.out.println("connect");
        // 유저가 Websocket으로 connect()를 한 뒤 호출됨
        break;
      case DISCONNECT:
        System.out.println("disconnect");
        // 유저가 Websocket으로 disconnect() 를 한 뒤 호출됨 or 세션이 끊어졌을 때 발생함(페이지 이동~ 브라우저 닫기 등)
        WebsocketUtils.removedThread(sessionId);
        break;
      default:
        break;
    }

  }
}