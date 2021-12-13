window.INFLUX_ENV = {
  /** InfluxDB v2 URL, '/influxdb' relies upon proxy to forward to the target influxDB */
  url: 'http://192.168.120.6:8086',
  /** InfluxDB authorization token */
  token: '-__8vAz2r4iDK-dx03d3JyXTf_CWHVUzsOnWlUEYaWqw71PBKJO4xgveJCvgcnvC1IndYad894u8FkmTqFRKeQ==',
  /** InfluxDB organization */
  org: 'cloudit',
  /** InfluxDB bucket used for onboarding and write requests. */
  bucket: 'cloudit',

  /** The following properties are used ONLY in the onboarding example */
  /** InfluxDB user  */
  username: 'cloudit',
  /** InfluxDB password  */
  password: 'qwe1212!Q',
}