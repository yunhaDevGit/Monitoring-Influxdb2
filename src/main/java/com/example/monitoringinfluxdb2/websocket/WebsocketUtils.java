package com.example.monitoringinfluxdb2.websocket;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WebsocketUtils {

  public static final ConcurrentHashMap<String, SendingMessageFacadeAbstract> sendingMessageFacadeAbstractConcurrentHashMap = new ConcurrentHashMap<String, SendingMessageFacadeAbstract>();
  private static final Logger logger = LoggerFactory.getLogger(WebsocketUtils.class);

  public static void removedThread(String websocketSessionId) {
    Thread sendingMessageFacadeAbstract = null;
    synchronized (sendingMessageFacadeAbstractConcurrentHashMap) {
      try {
        sendingMessageFacadeAbstract = sendingMessageFacadeAbstractConcurrentHashMap.get(websocketSessionId);
        sendingMessageFacadeAbstract.interrupt();
        logger.info("Next websocket thread has interrupted : [{}]", websocketSessionId);
      } catch (SecurityException e) {
        logger.error("Security     ERROR [{}] ::::: {}", websocketSessionId, e.getMessage());
      } catch (NullPointerException e) {
        logger.error("Null Pointer ERROR [{}] ::::: {}", websocketSessionId, e.getMessage());
      } catch (Exception e) {
        logger.error("Exception    ERROR [{}] ::::: {}", websocketSessionId, e.getMessage());
      } finally {
        if (sendingMessageFacadeAbstract != null) {
          if (sendingMessageFacadeAbstract.isAlive()) {
            sendingMessageFacadeAbstract.interrupt();
          } else {
            logger.info("SendingMessageFacadeAbstract already dead : [{}]", websocketSessionId);
          }
        } else {
          logger.info("SendingMessageFacadeAbstract is null      : [{}]", websocketSessionId);
        }
        try {
          sendingMessageFacadeAbstractConcurrentHashMap.remove(websocketSessionId);
        } catch (Exception e) {
          logger.error("Error while removing websocket thread    : [{}]", e.getMessage());
        }
      }

      if (sendingMessageFacadeAbstractConcurrentHashMap != null) {
        logger.info("Now remaining thread in HashMap size : [{}]", sendingMessageFacadeAbstractConcurrentHashMap.size());
        Enumeration<SendingMessageFacadeAbstract> enumeration = sendingMessageFacadeAbstractConcurrentHashMap.elements();
        while (enumeration.hasMoreElements()) {
          SendingMessageFacadeAbstract sendingMessageFacadeAbstract1 = enumeration.nextElement();
          logger.info("Now remaining thread in HashMap id  : [{}]", sendingMessageFacadeAbstract1.getWebsocketSessionId());
        }
      }
    }
  }
}
