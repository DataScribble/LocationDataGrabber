package com.rishabh.locationdatagrabber2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  private static int REQUEST= 102;
  TextView btnStart;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getLocationPermission(this);

    btnStart = (TextView) findViewById(R.id.btnStart);

    btnStart.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startService(new Intent(MainActivity.this , MyLocationService.class));
      }
    });

  }


  public static boolean getLocationPermission(Activity activity) {
    if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
          REQUEST);
    } else {
      return true;
    }
    return false;
  }
}
