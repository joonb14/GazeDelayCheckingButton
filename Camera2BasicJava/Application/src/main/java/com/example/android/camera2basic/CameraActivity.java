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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private static int count;
    private static SharedPreferences sf;

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

        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }

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
    }

    public void onStop() {
        super.onStop();
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

}
