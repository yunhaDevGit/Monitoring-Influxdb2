package com.example.monitoringinfluxdb2.dto;

import lombok.Data;

@Data
public class OutputMessage {

  private String from;
  private String text;
  private String time;
  public OutputMessage(String from, String text, String time) {
  }
}
