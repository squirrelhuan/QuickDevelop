package cn.demomaster.huan.quickdevelop.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.IStudent;
import cn.demomaster.qdlogger_library.QDLogger;


public class StudentService extends Service {
    public String TAG ="StudentService";
    private IStudent.Stub stub = new IStudent.Stub() {
        @Override
        public void addStudentInfoReq(StudtInfo studtInfo) {
            Log.d(TAG, "姓名：" + studtInfo.getName()
                    + " 数学成绩：" + studtInfo.getMathScore()
                    + " 英语成绩：" + studtInfo.getEnglishScore());
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        QDLogger.e(TAG,"onBind");
        return stub;
    }
}
