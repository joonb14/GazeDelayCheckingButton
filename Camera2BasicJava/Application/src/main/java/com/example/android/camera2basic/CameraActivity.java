/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.camera2basic;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private SensorManager mSensorManager;

    private Sensor mGyroSensor = null;
    private Sensor mAccelerometer = null;
    private static int count;
    private static SharedPreferences sf;
    private static final String[] PERMISSIONS = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static double gyroX;
    private static double gyroY;
    private static double gyroZ;

    private  static double accX;
    private  static double accY;
    private  static double accZ;
    private static final int REQ_PERMISSION = 1000;
    private String TAG = "MOBED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MOBED
        String dir_path = Environment.getExternalStorageDirectory() + "/DelayLog";

        if (!dir_exists(dir_path)){
            File directory = new File(dir_path);
            directory.mkdirs();
        }

        File file = new File("sdcard/DelayLog/.nomedia");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sf = getPreferences(Context.MODE_PRIVATE);
        count = sf.getInt("count",0);
        checkPermission();

        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public boolean dir_exists(String dir_path)
    {
        boolean ret = false;
        File dir = new File(dir_path);
        if(dir.exists() && dir.isDirectory())
            ret = true;
        return ret;
    }

    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(gyroListener, mGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(acceleroListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(gyroListener);
        mSensorManager.unregisterListener(acceleroListener);
    }

    public static int getCount(){
        count = sf.getInt("count",0);
        return count;
    }

    public static SharedPreferences getSF(){
        return sf;
    }

    public static int addCount(){
        count+=1;
        SharedPreferences.Editor editor = sf.edit();
        editor.putInt("count",count);
        editor.commit();
        return count;
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check permission status
            if (!hasPermissions(PERMISSIONS)) {

                requestPermissions(PERMISSIONS, REQ_PERMISSION);
            } else {
                checkPermission(true);
            }
        } else {
            checkPermission(true);
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private boolean hasPermissions(String[] permissions) {
        int result;
        // Check permission status in string array
        for (String perms : permissions) {
            if (perms.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                if (!Settings.canDrawOverlays(this)) {
                    return false;
                }
            }
            result = ContextCompat.checkSelfPermission(this, perms);
            if (result == PackageManager.PERMISSION_DENIED) {
                // When if unauthorized permission found
                return false;
            }
        }
        // When if all permission allowed
        return true;
    }

    private void checkPermission(boolean isGranted) {
        if (isGranted) {
            //
        } else {
            Log.d(TAG, "not granted permissions");
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION:
                if (grantResults.length > 0) {
                    boolean PermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (PermissionAccepted) {
                        checkPermission(true);
                    } else {
                        checkPermission(false);
                    }
                }
                break;
        }
    }
    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor mGyroSensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            gyroX = event.values[0];
            gyroY = event.values[1];
            gyroZ = event.values[2];
            //SLog.d("MOBED","Gyro: "+gyroX+ " "+gyroY+" "+gyroZ+"rad/s");
        }
    };
    public SensorEventListener acceleroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor mGyroSensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            accX = event.values[0];
            accY = event.values[1];
            accZ = event.values[2];
            //Log.d("MOBED","Accelerometer: "+accX+ " "+accY+" "+accZ+"m/s^2");
        }
    };
    public static String getGyroData(){
        return gyroX+ ","+gyroY+","+gyroZ;
    }
    public static String getAcceleroData(){
        return accX+ ","+accY+","+accZ;
    }
}
