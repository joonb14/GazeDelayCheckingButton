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
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Camera2BasicFragment extends Fragment
        implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    private static final String[] VIDEO_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "MOBED_Camera2";


    /**
     * The margin size of Button
     */
    private Button myBtn;
    private RelativeLayout.LayoutParams params;
    private int leftmargin;
    private int topmargin;
    private TextView textView;

    public static Camera2BasicFragment newInstance() {
        return new Camera2BasicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera2_basic, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        try {
            view.findViewById(R.id.MyButton).setOnClickListener(this);
        }
        catch (NullPointerException e) {
            Log.e(TAG, "Attempt to invoke virtual method 'void android.view.View.setOnClickListener(android.view.View$OnClickListener)' on a null object reference");
            e.printStackTrace();
        }
        textView = view.findViewById(R.id.textView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.MyButton: {
                    myBtn = (Button) view.findViewById(R.id.MyButton);
    //                myBtn.setClickable(false);
                    params = (RelativeLayout.LayoutParams)myBtn.getLayoutParams();
                    final int button_size = 224;
                    final int left_margin = 53;
                    final int top_margin = 136;
                    leftmargin = params.leftMargin + button_size/2;
                    topmargin = params.topMargin + button_size/2;
                    appendLog("1,"+System.currentTimeMillis()+","+leftmargin+","+topmargin+","+CameraActivity.getOrientation()+","+CameraActivity.getGyroData()+","+CameraActivity.getAcceleroData());
                    Log.d(TAG, "1,"+CameraActivity.getOrientation());
                    CameraActivity.getCount();
                    CameraActivity.addCount();
                    String count = "Count: "+CameraActivity.getCount();
                    textView.setText(count);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                    long seed = System.currentTimeMillis();
                    Random rand = new Random(seed);
                        public void run() {
                            int row =rand.nextInt(7)+1; //1~7
                            int col =rand.nextInt(5)+1; //1~4
                            int topmargin = 100+(row-1)*top_margin+button_size*(row-1);
                            int leftmargin = col*left_margin+button_size*(col-1);
                            params.topMargin = topmargin;
                            params.leftMargin = leftmargin;
                            myBtn.setLayoutParams(params);
                            topmargin+=button_size/2;
                            leftmargin+=button_size/2;
                            appendLog("0,"+System.currentTimeMillis()+","+leftmargin+","+topmargin+","+CameraActivity.getOrientation()+","+CameraActivity.getGyroData()+","+CameraActivity.getAcceleroData());
                            Log.d(TAG, "0,"+CameraActivity.getOrientation());
                        }
                    }, 1000);
                }
            }
        }
        catch (NullPointerException e) {
            Log.e(TAG, "Attempt to invoke virtual method 'void android.view.View.setOnClickListener(android.view.View$OnClickListener)' on a null object reference");
            e.printStackTrace();
        }
    }
    public void appendLog(String text)
    {
        File logFile = new File("sdcard/DelayLog/log.csv");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append("Category(0:button set 1:button pressed),time,gazeX,gazeY,pitch,roll,gyroX,gyroY,gyroZ,accelX,accelY,accelZ");
                buf.newLine();
                buf.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
