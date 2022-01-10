package com.example.monitoringinfluxdb2.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
      return parseFromQueryResult( queryResult);
//      return queryResult;
    }
    return null;
  }

  public HashMap<String, List<Object>> parseFromQueryResult(List<FluxTable> queryResult) {

    List<List<Object>> datas = new ArrayList<>(new ArrayList<>());
    String[] strings = {"result", "table", "_start", "_stop", "_time", "_value", "_field", "_measurement", "cpu", "host"};
    HashMap<String, List<Object>>  map = new HashMap<>();
    for (FluxTable fluxTable : queryResult) {
      List<FluxRecord> records = fluxTable.getRecords();
      int recordSize = fluxTable.getColumns().size();

      for (int i=0;i<recordSize;i++){
        ArrayList<Object> value = new ArrayList<>();
        for (FluxRecord fluxRecord : records){
          value.add(fluxRecord.getValueByIndex(i));
          System.out.println("");
        }
        map.put(strings[i], value);
      }
    }

    System.out.println(map);

//    List<QueryResult.Result> results = queryResult.getResults();
//    for (QueryResult.Result result : results) {
//      if (result != null && !result.hasError() && result.getSeries() != null) {
//        for (QueryResult.Series series : result.getSeries()) {
//          datas = series.getValues();
//          break;
//        }
//      }
//    }
    return map;
  }
}
