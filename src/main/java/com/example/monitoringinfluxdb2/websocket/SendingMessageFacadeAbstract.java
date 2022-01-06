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
  private Object switchStructureInfoList;
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

  public SendingMessageFacadeAbstract(SimpMessagingTemplate simpMessagingTemplate, MonitoringService monitoringService) throws NullPointerException {
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.monitoringService = monitoringService;
  }

  protected void _sendingMessage(boolean stop) {

    try{
      JSONObject jsonObject = new JSONObject();
      switchStructureInfoList = monitoringService.getMonitoringData();
      jsonObject.put("switchStructureInfoList",
          new JSONParser().parse(new Gson().toJson(switchStructureInfoList)));

      SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
      accessor.setContentType(MimeTypeUtils.APPLICATION_JSON);
      simpMessagingTemplate.convertAndSend("/topic/cpuUtilization", jsonObject, accessor.getMessageHeaders());

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
