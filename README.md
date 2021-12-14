# Monitoring-Influxdb2

Telegraf + InfluxDB 설치 : [[Monitoring] telegraf + influxdb 2.1](https://www.notion.so/Monitoring-telegraf-influxdb-2-1-04baa86ab1b840a7a15591772f758513) 

1. Java Application 생성 (Build Tool : Gradle)

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/c68b0c67-b20e-4405-ad47-333273b73b91/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20211214%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20211214T005518Z&X-Amz-Expires=86400&X-Amz-Signature=d61de670fe4f5ee734b66fb12c06892498490bbfd8af689db88299128286c4f9&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

1. InfluxDB UI 접속 > Data > Client Libraries (Java) 선택

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/c940f2f2-abef-453d-ab7d-470b58d7c354/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20211214%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20211214T005529Z&X-Amz-Expires=86400&X-Amz-Signature=c55cc00f94466b84f48e7807a6213fa3fa56b9ee5b067bd67c6067ae170b8753&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/644b2acb-8046-4eac-b4bc-85257dd4d22a/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20211214%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20211214T005541Z&X-Amz-Expires=86400&X-Amz-Signature=d0c5a369a0de40153c764d83c2f60557a47b9e0e84511ccf9bb4c766c725d7cf&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

해당 페이지를 참고하여 influxdb와 연동

1. build.gradle에 dependency 추가
    
    ```powershell
    implementation "com.influxdb:influxdb-client-java:3.1.0"
    ```
    
2. 연동 후 read 구현
    
    ```java
    
    import com.influxdb.client.InfluxDBClient;
    import com.influxdb.client.InfluxDBClientFactory;
    import com.influxdb.query.FluxRecord;
    import com.influxdb.query.FluxTable;
    import java.util.List;
    
    public class InfluxDB2Example {
    
      public static void main(String[] args) {
    // You can generate an API token from the "API Tokens Tab" in the UI
        String token = "-__8vAz2r4iDK-dx03d3JyXTf_CWHVUzsOnWlUEYaWqw71PBKJO4xgveJCvgcnvC1IndYad894u8FkmTqFRKeQ==";
        String bucket = "cloudit";
        String org = "cloudit";
    
        InfluxDBClient client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray());
    
        // range(start: -12h) : 현재를 기준으로 한 시간 범위
        // usage_system field를 기준으로 필터링
        String query = "from(bucket: \"cloudit\") |> range(start: -12h) |> filter(fn: (r) =>\n"
            + "    r._field == \"usage_system\" and\n"
            + "    r.cpu =~ /cpu[0-9*]/\n"
            + "  ) ";
        List<FluxTable> tables = client.getQueryApi().query(query, org);
    
        for (FluxTable table : tables) {
          for (FluxRecord record : table.getRecords()) {
            System.out.println(record);
            System.out.println(record.getValue());
          }
        }
      }
    }
    ```
