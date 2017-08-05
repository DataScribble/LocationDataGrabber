package com.rishabh.locationdatagrabber2;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

/**
 * Created by rishabh on 5/8/17.
 */

public class DetectedActivitiesIntentService extends IntentService {
  protected static final String TAG = "detection_is";
  private FirebaseDatabase database;
  private DatabaseReference myRefActivities;

  /**
   * This constructor is required, and calls the super IntentService(String)
   * constructor with the name for a worker thread.
   */
  public DetectedActivitiesIntentService() {
    // Use the TAG to name the worker thread.
    super(TAG);
  }

  @Override
  public void onCreate() {
    super.onCreate();

    database = FirebaseDatabase.getInstance();
    myRefActivities = database.getReference("activity");
  }

  /**
   * Handles incoming intents.
   * @param intent The Intent is provided (inside a PendingIntent) when requestActivityUpdates()
   *               is called.
   */
  @Override
  protected void onHandleIntent(Intent intent) {
    ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

    // Get the list of the probable activities associated with the current state of the
    // device. Each activity is associated with a confidence level, which is an int between
    // 0 and 100.
    ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

    // Log each activity.
    Log.i(TAG, "activities detected");

    for(DetectedActivity thisActivity: detectedActivities){
      String type = String.valueOf(thisActivity.getType());
      String confidence = String.valueOf(thisActivity.getConfidence());
      String time = String.valueOf(System.currentTimeMillis());

      MyActivity myActivity = new MyActivity(type, confidence, time);
      myRefActivities.push().setValue(myActivity);


    }
  }
}
