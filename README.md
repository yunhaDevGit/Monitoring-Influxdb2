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
  <head>
    <meta charset="utf-8">
    <title>Example: Flux Query Results visualized with Giraffe</title>
  </head>

  <body id="home">
    <main id="root"></main>
  </body>
  </html>
```

4.mustache plugin 설치
 
  ![](https://lh5.googleusercontent.com/H3U5e69gU3btKtR1BUVCw8U61R2_-77HXSDBf42kIrotbYFKlbsvHa0vJ9FvGo3bW4WRFeGANucTLIBwxOJyYhIPZkLtJrMgoeTnIEw7A6-zDkwUSVtuhO9lDtcV8A_qPWfjsHin_e1A)


5. import React, React-DOM, Giraffe (`<head> </head>` 에 삽입)

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

6. Graph Element 생성

  ```html
  <body>
  <h1>Example: Flux Query Results visualized with Giraffe</h1>
  <fieldset>
    <legend>Graph Visualization</legend>
    <div style="width: 100%;height: 500px; border: 1px solid grey; margin-bottom: 10px;" id="graphView">
    </div>
  </fieldset>
  </body>
```

7. InfluxDB connection을 위한 influxdb 설정 작성

    - env_browser.js 파일 생성
        ```js
        window.INFLUX_ENV = {
            /** InfluxDB v2 URL, '/influxdb' relies upon proxy to forward to the target influxDB */
            url: 'http://{url}:8086',
            /** InfluxDB authorization token */
            token: {token},
            /** InfluxDB organization */
            org: {oraganization},
            /** InfluxDB bucket used for onboarding and write requests. */
            bucket: {bucket-name},

            /** The following properties are used ONLY in the onboarding example */
            /** InfluxDB user  */
            username: {username},
            /** InfluxDB password  */
            password: {password},
          }
        ```
  
8. InfluxDB API 인스턴스 생성
  
   @influxdata/influxdb-client 패키지는 InfluxDB v2에 최적화 된 쓰기, query API를 제공한다.

    https://influxdata.github.io/influxdb-client-js/influxdb-client.html


   QueryAPI : InfluxDB에 쿼리 요청 및 실행된 쿼리 결과를 반환하는 메서드 제공한다.

    https://docs.influxdata.com/influxdb/v2.1/api/#operation/PostQuery
  
  ```html
  <script>
    // get query from request parameter or use default
    // DB Connection 및 쿼리문 작성
    fluxQuery = new URLSearchParams(window.location.search).get('fluxQuery');
    if (!fluxQuery){
      // range(start: -15m) -> 지난 15분 동안의 데이터를 가져온다
      fluxQuery = `from(bucket:"cloudit") |> range(start: -15m)   |> filter(fn: (r) => r["_measurement"] == "cpu")
         |> filter(fn: (r) => r["_field"] == "usage_system")
         |> filter(fn: (r) => r["cpu"] == "cpu0" or r["cpu"] == "cpu1" or r["cpu"] == "cpu2")`
    }
    const fluxQueryInput = document.getElementById('fluxQuery')
    fluxQueryInput.value =  fluxQuery

    // create query API
    const {url, token, org} = window.INFLUX_ENV // loaded in ./env_browser.js
    const influxDBClient = window['@influxdata/influxdb-client']
    const influxDB = new influxDBClient.InfluxDB({url, token})
    const queryApi = influxDB.getQueryApi(org)

    // execute query and fill query data into giraffe table
    const giraffe = window['@influxdata/giraffe']

    ...
  </script>  
  ```


9.  React 요소 연결

   @influxdb-client-giraffe 패키지에서는 `queryToTable`라는 InfluxDB V2의 쿼리 수행 후 Giraffe 시각화에 적합한 데이터를 테이블에 반환해주는 역할을 하는 함수를 제공한다. 

    table 설정 및 렌더링

  ```html
  <script>
    ...

      function lineRenderResults({error, table}){
        if (error){
          // render error message
          return React.createElement('center', null, error.toString())
        } else if (table.length) {
          // render giraffe plot
          const lineConfig = {
            table: table,
            layers: [{
              type: 'line',
              x: '_time',
              y: '_value',
              fill: [],
              colors: ['#31C0F6', '#A500A5', '#FF7E27']
            }],
            valueFormatters: {
              _time: giraffe.timeFormatter({
                timeZone: 'Asia/Seoul',
                format: 'YYYY-MM-DD HH:mm:ss',
              }),
            }
          };
          return React.createElement(giraffe.Plot, {config: lineConfig})
        } else {
          // render empty table recevied
          return React.createElement('center', null, 'Empty Table Received')
        }
      }

      function queryAndVisualizeLine() {
        influxDBClientGiraffe.queryToTable(
            queryApi,
            fluxQueryInput.value,
            giraffe.newTable
        ). then(table => {
          console.log('queryToTable returns', table)
          ReactDOM.render(
              React.createElement(lineRenderResults, {table}),
              document.getElementById('lineView')
          );
        }). catch(error => {
          console.log('queryToTable fails', error)
          ReactDOM.render(
              React.createElement(lineRenderResults, {error}),
              document.getElementById('lineView')
          );
        })
      }

    ...
  </script?
  ```




### References

[1] Giraffe Github : https://github.com/influxdata/giraffe/tree/b2415f38cb9bd69733e138ead7f6ef8805864475

[2] influxdb-client-js : https://influxdata.github.io/influxdb-client-js/influxdb-client.html

[4] Giraffe Quick Start : https://www.npmjs.com/package/@influxdata/giraffe

[5] Giraffe Visualization Tutorial : https://www.influxdata.com/blog/giraffe-visualization-library-and-influxdb/

[6] Giraffe Sample : https://github.com/influxdata/giraffe/blob/master/examples/Listing_of_giraffe_samples.md

[7] Giraffe Graph Types : https://github.com/influxdata/giraffe/blob/7cc04910f4fbd5b066f5e12a3bc75857b5e582ce/giraffe/src/types/index.ts#L172 , https://github.com/influxdata/giraffe/blob/16121abc084edef65e554d132cb2dc7012a7c5ff/giraffe/src/constants/index.ts#L57
