package com.example.monitoringinfluxdb2.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MonitoringService {

  @Value("${influx.org}")
  private String org;

  @Value("${influx.bucket}")
  private String bucket;

  @Value("${influx.url}")
  private String url;

  @Value("${influx.token}")
  private String token;

  private InfluxDBClient influxDBClient;

  public Object getMonitoringData() {
    influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray());
    String query =
        "from(bucket:\"cloudit\") |> range(start: -1m)   |> filter(fn: (r) => r[\"_measurement\"] == \"cpu\")\n"
            + "       |> filter(fn: (r) => r[\"_field\"] == \"usage_system\")\n"
            + "       |> filter(fn: (r) => r[\"cpu\"] == \"cpu0\")";

    List<FluxTable> queryResult = influxDBClient.getQueryApi().query(query, org);
    if (queryResult != null) {
//      return parseFromQueryResult( queryResult);
      return queryResult;
    }
    return null;
  }

  public List<List<Object>> parseFromQueryResult(List<FluxTable> queryResult) {
    List<List<Object>> datas = new ArrayList<>(new ArrayList<>());
    for (FluxTable fluxTable : queryResult) {
      List<FluxRecord> records = fluxTable.getRecords();
      for (FluxRecord fluxRecord : records) {
//        datas = fluxRecord
      }
    }
//    List<QueryResult.Result> results = queryResult.getResults();
//    for (QueryResult.Result result : results) {
//      if (result != null && !result.hasError() && result.getSeries() != null) {
//        for (QueryResult.Series series : result.getSeries()) {
//          datas = series.getValues();
//          break;
//        }
//      }
//    }
    return datas;
  }
}
