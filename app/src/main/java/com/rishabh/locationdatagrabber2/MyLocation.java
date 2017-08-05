package com.rishabh.locationdatagrabber2;

/**
 * Created by rishabh on 5/8/17.
 */

public class MyLocation {

  String latitude;
  String longitude;
  String accuracy;
  String time;


  public MyLocation(){
  }

  public MyLocation(String latitude, String longitude, String accuracy, String time) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.accuracy = accuracy;
    this.time = time;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(String accuracy) {
    this.accuracy = accuracy;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
