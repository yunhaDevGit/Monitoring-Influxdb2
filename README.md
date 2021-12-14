# Monitoring-Influxdb2

Telegraf + InfluxDB 설치 : [[Monitoring] telegraf + influxdb 2.1](https://github.com/yunhaDevGit/TIL/blob/main/Monitoring/TIL_211209_telegraf%2Binfluxdb.md) 

Telegraf를 통해 수집한 메트릭 정보를 InfluxDB에 담은 후, 수집된 데이터를 조회하여 Giraffe 라이브러리를 사용하여 시각화합니다.

(Giraffe는 InfluxDB 2.0 UI를 구현하는데 사용되는 React 기반 시각화 라이브러리입니다.)

- Spring Boot '2.6.1'
- InflxuDB 2.1.1
- telegraf
- docker


## Quick Start

1. Java Application 생성 (Build Tool : Gradle)

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/c68b0c67-b20e-4405-ad47-333273b73b91/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20211214%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20211214T005518Z&X-Amz-Expires=86400&X-Amz-Signature=d61de670fe4f5ee734b66fb12c06892498490bbfd8af689db88299128286c4f9&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

2. Controller 생성

```java
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
```

3. monitoring.mustache 생성

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Giraffe</title>
</head>

<body id="home">
  <main id="root"></main>
</body>
</html>
```

4.mustache plugin 설치
 
![](https://lh5.googleusercontent.com/H3U5e69gU3btKtR1BUVCw8U61R2_-77HXSDBf42kIrotbYFKlbsvHa0vJ9FvGo3bW4WRFeGANucTLIBwxOJyYhIPZkLtJrMgoeTnIEw7A6-zDkwUSVtuhO9lDtcV8A_qPWfjsHin_e1A)


5. import React, React-DOM, Giraffe (<head> </head> 에 삽입)

```html
<head>
  <title>Example: Flux Query Results visualized with Giraffe</title>
  <script>
    // required by react
    window.process = {
      env : 'development'
    }
    // required by giraffe
    global = window
  </script>
<!--  React, React-DOM, Firaffe 추가-->
  <script src="https://unpkg.com/react@17.0.0/umd/react.development.js"></script>
  <script src="https://unpkg.com/react-dom@17.0.0/umd/react-dom.development.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/@influxdata/giraffe@0.41.0/dist/index.js"></script>
  <script src="https://unpkg.com/@influxdata/influxdb-client/dist/index.browser.js"></script>
  <script src="https://unpkg.com/@influxdata/influxdb-client-giraffe/dist/index.js"></script>
  <script src="/js/env_browser.js"></script>
</head>
```

6. 





### Java Example

1. Java Application 생성 (Build Tool : Gradle)

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/c68b0c67-b20e-4405-ad47-333273b73b91/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20211214%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20211214T005518Z&X-Amz-Expires=86400&X-Amz-Signature=d61de670fe4f5ee734b66fb12c06892498490bbfd8af689db88299128286c4f9&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

2. InfluxDB UI 접속 > Data > Client Libraries (Java) 선택

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/c940f2f2-abef-453d-ab7d-470b58d7c354/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20211214%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20211214T005529Z&X-Amz-Expires=86400&X-Amz-Signature=c55cc00f94466b84f48e7807a6213fa3fa56b9ee5b067bd67c6067ae170b8753&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/644b2acb-8046-4eac-b4bc-85257dd4d22a/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20211214%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20211214T005541Z&X-Amz-Expires=86400&X-Amz-Signature=d0c5a369a0de40153c764d83c2f60557a47b9e0e84511ccf9bb4c766c725d7cf&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

해당 페이지를 참고하여 influxdb와 연동

3. build.gradle에 dependency 추가
    
    ```powershell
    implementation "com.influxdb:influxdb-client-java:3.1.0"
    ```
    
4. 연동 후 read 구현
    
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

### References

[1] Giraffe Github : https://github.com/influxdata/giraffe/tree/b2415f38cb9bd69733e138ead7f6ef8805864475

[2] Giraffe Quick Start : https://www.npmjs.com/package/@influxdata/giraffe

[3] Giraffe Visualization Tutorial : https://www.influxdata.com/blog/giraffe-visualization-library-and-influxdb/

[4] Giraffe Sample : https://github.com/influxdata/giraffe/blob/master/examples/Listing_of_giraffe_samples.md

[5] Giraffe Graph Types : https://github.com/influxdata/giraffe/blob/7cc04910f4fbd5b066f5e12a3bc75857b5e582ce/giraffe/src/types/index.ts#L172 , https://github.com/influxdata/giraffe/blob/16121abc084edef65e554d132cb2dc7012a7c5ff/giraffe/src/constants/index.ts#L57
