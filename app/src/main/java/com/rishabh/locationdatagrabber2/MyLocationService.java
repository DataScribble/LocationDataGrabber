package com.rishabh.locationdatagrabber2;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by rishabh on 5/8/17.
 */

public class MyLocationService extends Service
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener , ResultCallback<Status> {

  private String TAG = "MyLocationService";
  private GoogleApiClient mGoogleApiClient;
  private LocationRequest mLocationRequest;
  private FirebaseDatabase database;
  private DatabaseReference myRefLocation;

  public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 5000;

  @Override public void onCreate() {
    super.onCreate();
    buildGoogleApiClient();

    FirebaseApp.initializeApp(getBaseContext());

    // Write a message to the database
    database = FirebaseDatabase.getInstance();
    myRefLocation = database.getReference("location");

  }


  protected synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .addApi(ActivityRecognition.API)
        .build();
    mGoogleApiClient.connect();
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    Log.i(TAG, "service starting");

    return START_STICKY;
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public void onConnected(@Nullable Bundle bundle) {

    mLocationRequest = LocationRequest.create();
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    // get updates when displacement is 50 or more
    mLocationRequest.setSmallestDisplacement(50);
    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this,
        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
        this);
  }

  @Override public void onConnectionSuspended(int i) {

  }

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  @Override public void onLocationChanged(Location location) {

    String latitude = String.valueOf(location.getLatitude());
    String longitude = String.valueOf(location.getLongitude());
    String accuracy = String.valueOf(location.getAccuracy());
    String time;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      time = String.valueOf(location.getElapsedRealtimeNanos());
    }else {
      time = String.valueOf(location.getTime());
    }
    MyLocation myLocation = new MyLocation(latitude, longitude, accuracy , time);

    myRefLocation.push().setValue(myLocation);


    requestActivityUpdatesHandler();

  }

  public void requestActivityUpdatesHandler() {
    if (!mGoogleApiClient.isConnected()) {
      Log.e(TAG, "GoogleApi is not Connected");
      return;
    }
    ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
        mGoogleApiClient,
        DETECTION_INTERVAL_IN_MILLISECONDS,
        getActivityDetectionPendingIntent()
    ).setResultCallback(this);
  }


  private PendingIntent getActivityDetectionPendingIntent() {
    Intent intent = new Intent(this, DetectedActivitiesIntentService.class);

    // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
    // requestActivityUpdates() and removeActivityUpdates().
    return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }


  @Override public void onDestroy() {
    super.onDestroy();
    if (mGoogleApiClient.isConnected()) {
      mGoogleApiClient.disconnect();
    }
  }

  @Override public void onResult(@NonNull Status status) {

    if (status.isSuccess()) {
      Log.e(TAG, "Successfully added activity detection.");

    } else {
      Log.e(TAG, "Error adding or removing activity detection: " + status.getStatusMessage());
    }
  }
}
