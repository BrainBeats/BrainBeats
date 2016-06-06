/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pi.android.brainbeats.ui;

import android.os.Bundle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.pi.android.brainbeats.R;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

import com.emotiv.insight.IEdk;
import com.emotiv.insight.IEdkErrorCode;
import com.emotiv.insight.IEdk.IEE_DataChannel_t;
import com.emotiv.insight.IEdk.IEE_Event_t;

/**
 * Placeholder activity for features that are not implemented in this sample, but
 * are in the navigation drawer.
 * Activity to test the headset with the multimedia player.
 */

public class PlaceholderActivity extends BaseActivity {


    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean lock = false;
    private boolean isEnablGetData = false;
    private boolean isEnableWriteFile = false;
    int userId;
    private BufferedWriter motion_writer;
    Button Start_button,Stop_button;
    IEE_DataChannel_t[] Channel_list = {IEE_DataChannel_t.IED_AF3, IEE_DataChannel_t.IED_T7,IEE_DataChannel_t.IED_Pz,
            IEE_DataChannel_t.IED_T8,IEE_DataChannel_t.IED_AF4};
    String[] Name_Channel = {"AF3","T7","Pz","T8","AF4"};

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeholder);
        initializeToolbar();

       Intent intent = new Intent(PlaceholderActivity.this, HeadsetService.class);
       startActivity(intent);

    }




}
