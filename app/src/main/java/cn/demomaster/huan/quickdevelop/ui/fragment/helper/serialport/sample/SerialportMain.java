/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.demomaster.huan.quickdevelop.ui.fragment.helper.serialport.sample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.quickpermission_library.PermissionHelper;

@ActivityPager(name = "Serialport", preViewClass = TextView.class, resType = ResType.Custome)
public class SerialportMain extends QDActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialport_main);

        final Button buttonSetup = findViewById(R.id.ButtonSetup);
        buttonSetup.setOnClickListener(v -> startActivity(new Intent(SerialportMain.this, SerialPortPreferences.class)));

        final Button buttonConsole = findViewById(R.id.ButtonConsole);
        buttonConsole.setOnClickListener(v -> startActivity(new Intent(SerialportMain.this, ConsoleActivity.class)));

        final Button buttonLoopback = findViewById(R.id.ButtonLoopback);
        buttonLoopback.setOnClickListener(v -> startActivity(new Intent(SerialportMain.this, LoopbackActivity.class)));

        final Button button01010101 = findViewById(R.id.Button01010101);
        button01010101.setOnClickListener(v -> startActivity(new Intent(SerialportMain.this, Sending01010101Activity.class)));

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        PermissionHelper.requestPermission(mContext, permissions, null);
    }
}
