package com.example.monitoringinfluxdb2.websocket;

import com.example.monitoringinfluxdb2.service.MonitoringService;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.MimeTypeUtils;

public abstract class SendingMessageFacadeAbstract extends Thread {

  private static final Logger logger = LoggerFactory.getLogger(SendingMessageFacadeAbstract.class);

  private String destination;
  private String websocketSessionId;
  private SimpMessagingTemplate simpMessagingTemplate;
  private MonitoringService monitoringService;
  private Object cpuUtilization;
  private long monitoringCycle = 1;

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getDestination() {
    return destination;
  }

  public String getWebsocketSessionId() {
    return websocketSessionId;
  }

  public void setWebsocketSessionId(String websocketSessionId) {
    this.websocketSessionId = websocketSessionId;
  }

  public SendingMessageFacadeAbstract(String websocketSessionId, SimpMessagingTemplate simpMessagingTemplate, MonitoringService monitoringService) throws NullPointerException {
    this.websocketSessionId = websocketSessionId;
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.monitoringService = monitoringService;
  }

  protected void _sendingMessage(boolean stop) {

    long pullingTime = 0l;
    try{
      while (!stop) {
        JSONObject jsonObject = new JSONObject();
        pullingTime += 1000l;

        // 5초마다 influxdb에서 데이터를 불러와서 client에게 반환
        if (pullingTime >= (1000l * 5l)) {
          cpuUtilization = monitoringService.getMonitoringData();
//          jsonObject.put("cpuUtilization",
//              new JSONParser().parse(new Gson().toJson(cpuUtilization)));

          SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
          accessor.setContentType(MimeTypeUtils.APPLICATION_JSON);
          simpMessagingTemplate.convertAndSend("/topic/cpuUtilization", cpuUtilization,
              accessor.getMessageHeaders());
          pullingTime = 0l;
        }
        sleep((monitoringCycle * 1000l));
      }
    } catch (MessagingException me) {
      logger.error("ERROR [MessagingException] :::::: {}", me.getMessage());
      WebsocketUtils.removedThread(websocketSessionId);

    } catch (Exception e) {
      e.printStackTrace();
      logger.error("ERROR [Error when send message to user] :::::: {}", e.getMessage());
      WebsocketUtils.removedThread(websocketSessionId);
    }
  }
}
