window.INFLUX_ENV = {
  /** InfluxDB v2 URL, '/influxdb' relies upon proxy to forward to the target influxDB */
  url: 'http://192.168.120.6:8086',
  /** InfluxDB authorization token */
  token: '{token}',
  /** InfluxDB organization */
  org: '{organization}',
  /** InfluxDB bucket used for onboarding and write requests. */
  bucket: '{bucket}',

  /** The following properties are used ONLY in the onboarding example */
  /** InfluxDB user  */
  username: '{username}',
  /** InfluxDB password  */
  password: '{password}',
}