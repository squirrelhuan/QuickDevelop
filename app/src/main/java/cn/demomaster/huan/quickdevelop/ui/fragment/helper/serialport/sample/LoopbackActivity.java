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

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

import cn.demomaster.huan.quickdevelop.R;

public class LoopbackActivity extends SerialPortActivity {

    byte mValueToSend;
    boolean mByteReceivedBack;
    Object mByteReceivedBackSemaphore = new Object();
    Integer mIncoming = new Integer(0);
    Integer mOutgoing = new Integer(0);
    Integer mLost = new Integer(0);
    Integer mCorrupted = new Integer(0);

    SendingThread mSendingThread;
    TextView mTextViewOutgoing;
    TextView mTextViewIncoming;
    TextView mTextViewLost;
    TextView mTextViewCorrupted;

    private class SendingThread extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                synchronized (mByteReceivedBackSemaphore) {
                    mByteReceivedBack = false;
                    try {
                        if (mOutputStream != null) {
                            mOutputStream.write(mValueToSend);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    mOutgoing++;
                    // Wait for 100ms before sending next byte, or as soon as
                    // the sent byte has been read back.
                    try {
                        mByteReceivedBackSemaphore.wait(100);
                        if (mByteReceivedBack) {
                            // Byte has been received
                            mIncoming++;
                        } else {
                            // Timeout
                            mLost++;
                        }
                        runOnUiThread(() -> {
                            mTextViewOutgoing.setText(mOutgoing.toString());
                            mTextViewLost.setText(mLost.toString());
                            mTextViewIncoming.setText(mIncoming.toString());
                            mTextViewCorrupted.setText(mCorrupted.toString());
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialport_loopback);
        mTextViewOutgoing = findViewById(R.id.TextViewOutgoingValue);
        mTextViewIncoming = findViewById(R.id.TextViewIncomingValue);
        mTextViewLost = findViewById(R.id.textViewLostValue);
        mTextViewCorrupted = findViewById(R.id.textViewCorruptedValue);
        if (mSerialPort != null) {
            mSendingThread = new SendingThread();
            mSendingThread.start();
        }
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {

        synchronized (mByteReceivedBackSemaphore) {
            int i;
            for (i = 0; i < size; i++) {
                if ((buffer[i] == mValueToSend) && (!mByteReceivedBack)) {
                    mValueToSend++;
                    // This byte was expected
                    // Wake-up the sending thread
                    mByteReceivedBack = true;
                    mByteReceivedBackSemaphore.notify();
                } else {
                    // The byte was not expected
                    mCorrupted++;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mSendingThread != null)
            mSendingThread.interrupt();
        super.onDestroy();
    }
}
