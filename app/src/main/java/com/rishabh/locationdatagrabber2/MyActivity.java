package com.rishabh.locationdatagrabber2;

/**
 * Created by rishabh on 5/8/17.
 */

public class MyActivity {

  String type;
  String confidence;
  String time;

  public MyActivity(){
  }

  public MyActivity(String type, String confidence, String time) {
    this.type = type;
    this.confidence = confidence;
    this.time = time;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getConfidence() {
    return confidence;
  }

  public void setConfidence(String confidence) {
    this.confidence = confidence;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
