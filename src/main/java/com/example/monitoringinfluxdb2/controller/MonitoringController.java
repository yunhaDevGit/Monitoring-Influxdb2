package com.example.monitoringinfluxdb2.controller;

import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MonitoringController {

  String token = "6cpMVgC-vMzXs1eKAUOaRtk8y3kGcJsWJDFVglB4HW7ljLP09HhWbb7VhO54dJ9AvBkR4hIqTHWMVkTa2PaU3Q==";
  String bucket = "testCRUD";
  String org = "test";

  InfluxDBClient client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray());

  @GetMapping("/view/monitoring")
  public String getMonitoringPage() {
    return "monitoring";
  }

  @GetMapping("/")
  public String readInfluxDBData() {

    return "";
  }

  @PostMapping("/write/data")
  public String writeInfluxDBData() {
    Point point = Point
        .measurement("mem")
        .addTag("host", "host1")
        .addField("used_percent", 23.43234543)
        .time(Instant.now(), WritePrecision.NS);
    WriteApiBlocking writeApi = client.getWriteApiBlocking();
    writeApi.writePoint(bucket, org, point);

    return "";
  }

  @DeleteMapping("/delete/data")
  public void deleteInfluxDBData() {
    DeleteApi deleteApi = client.getDeleteApi();

    try {

      OffsetDateTime start = OffsetDateTime.now().minus(1, ChronoUnit.HOURS);
      OffsetDateTime stop = OffsetDateTime.now();

      deleteApi.delete(start, stop, "", "testCRUD", "test");

    } catch (InfluxException ie) {
      System.out.println("InfluxException: " + ie);
    }
  }
}
