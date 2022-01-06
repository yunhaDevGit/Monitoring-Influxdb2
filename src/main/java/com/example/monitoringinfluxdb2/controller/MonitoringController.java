package com.example.monitoringinfluxdb2.controller;

import com.example.monitoringinfluxdb2.service.MonitoringService;
import com.example.monitoringinfluxdb2.websocket.SendingMessageFacadeAbstract;
import com.google.gson.Gson;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  public void cpuMonitoringGraph() throws ParseException {
    SendingMessage sendingMessage = new SendingMessage(simpMessagingTemplate);
    sendingMessage.setDestination("/topic/cpuUtilization/");
    sendingMessage.start();


    Object switchStructureInfoList;
    JSONObject jsonObject = new JSONObject();
    switchStructureInfoList = monitoringService.getMonitoringData();
    jsonObject.put("switchStructureInfoList",
        new JSONParser().parse(new Gson().toJson(switchStructureInfoList)));

    SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
    accessor.setContentType(MimeTypeUtils.APPLICATION_JSON);
    simpMessagingTemplate.convertAndSend("/topic/cpuUtilization", jsonObject, accessor.getMessageHeaders());
  }

  private class SendingMessage extends SendingMessageFacadeAbstract {

    public SendingMessage(SimpMessagingTemplate simpMessagingTemplate) {
      super(simpMessagingTemplate, monitoringService);
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
