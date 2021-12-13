package com.example.monitoringinfluxdb2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MonitoringController {

  @GetMapping("/view/monitoring")
  public String getMonitoringPage() {
    return "monitoring";
  }
}
