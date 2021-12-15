package com.example.monitoringinfluxdb2.controller;

import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MonitoringController {

  @Value("${influx.org}")
  private String org;

  @Value("${influx.bucket}")
  private String bucket;

  @Value("${influx.url}")
  private String url;

  @Value("${influx.token}")
  private String token;

  private InfluxDBClient influxDBClient;

  @GetMapping("/view/monitoring")
  public String getMonitoringPage() {
    return "monitoring";
  }

  @GetMapping("/read/data")
  public void readInfluxDBData() {
    influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray());

    String query = "from(bucket: \"" + bucket + "\") |> range(start: -1h) |> filter(fn: (r) => r[\"_measurement\"] == \"mem\")";
    List<FluxTable> tables = influxDBClient.getQueryApi().query(query, org);

    for (FluxTable table : tables) {
      for (FluxRecord record : table.getRecords()) {
        System.out.println(record.getValue());
      }
    }

    influxDBClient.close();
  }

  @PostMapping("/write/data")
  public void writeInfluxDBData() {
    influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray());

    Point point = Point
        .measurement("mem")
        .addTag("host", "host1")
        .addField("used_percent", 345.4564)
        .time(Instant.now(), WritePrecision.NS);
    WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
    writeApi.writePoint(bucket, org, point);

    influxDBClient.close();
  }

  @DeleteMapping("/delete/data")
  public void deleteInfluxDBData() {
    influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray());
    DeleteApi deleteApi = influxDBClient.getDeleteApi();

    try {

      // 현지 시간~5분 전에 들어온 데이터 삭제
      OffsetDateTime start = OffsetDateTime.now().minus(5, ChronoUnit.MINUTES);
      OffsetDateTime stop = OffsetDateTime.now();

      deleteApi.delete(start, stop, "", "testCRUD", "test");

      influxDBClient.close();
    } catch (InfluxException ie) {
      System.out.println("InfluxException: " + ie);
    }
  }
}
