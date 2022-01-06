package com.example.monitoringinfluxdb2.controller;

import com.example.monitoringinfluxdb2.service.MonitoringService;
import com.example.monitoringinfluxdb2.websocket.SendingMessageFacadeAbstract;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MonitoringController {
  private static final Logger logger = LoggerFactory.getLogger(MonitoringController.class);
  public static final ConcurrentHashMap<String, SendingMessageFacadeAbstract> sendingMessageFacadeAbstractConcurrentHashMap = new ConcurrentHashMap<String, SendingMessageFacadeAbstract>();

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  private MonitoringService monitoringService;

  @GetMapping("/view")
  public String getMonitoringPage() {
    return "monitoring";
  }

  @GetMapping("/api/test")
  @ResponseBody
  public HashMap hello(@RequestParam("hi") String hi) {
    HashMap result = new HashMap();
    result.put("message", hi);

    return result;
  }

  @GetMapping("/test/view")
  public String getTestPage() {
    return "test";
  }


  @MessageMapping("/graph") // /app/monitoring/graph
//  @SendTo("/topic/cpuUtilization")
  public void cpuMonitoringGraph(@Headers Map headersMap) throws ParseException {
    String secWebsocketKey = (String) headersMap.get("simpSessionId");
    SendingMessage sendingMessage = new SendingMessage(secWebsocketKey, simpMessagingTemplate);
    sendingMessage.setDestination("/topic/cpuUtilization/");
    sendingMessage.start();
    sendingMessageFacadeAbstractConcurrentHashMap.put(secWebsocketKey, sendingMessage);

  }

  private class SendingMessage extends SendingMessageFacadeAbstract {

    public SendingMessage(String websocketSessionId, SimpMessagingTemplate simpMessagingTemplate) {
      super(websocketSessionId, simpMessagingTemplate, monitoringService);
    }

    public void run() {
      try {
        _sendingMessage(false);
      } catch (Exception e) {
        logger.debug("SendingMessage ERROR ::::::::::::::: {}", e.getMessage());
      }
    }
  }
}
