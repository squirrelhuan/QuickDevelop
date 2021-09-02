package cn.demomaster.huan.quickdeveloplibrary.ui.file;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

public class TextDetailFragment extends QuickFragment {
    @Override
    public View onGenerateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_file_text_detail, null);
        return mView;
    }
    TextView tv_content;
    @Override
    public void initView(View rootView) {
        tv_content= findViewById(R.id.tv_content);
        String filePath = getArguments().getString("FILE_PATH_KEY");
        readFile(new File(filePath));
    }

    private void readFile(File file) {
        if (file == null) {
            return;
        }
        setTitle(file.getName());
        FileReadTask task = new FileReadTask(this);
        task.execute(file);
    }

    private static class FileReadTask extends AsyncTask<File, String, Void> {
        private WeakReference<TextDetailFragment> mReference;

        public FileReadTask(TextDetailFragment fragment) {
            mReference = new WeakReference<>(fragment);
        }

        @Override
        protected Void doInBackground(File... files) {
            try {
                FileReader fileReader = new FileReader(files[0]);
                BufferedReader br = new BufferedReader(fileReader);
                String textLine;
                while ((textLine = br.readLine()) != null) {
                    publishProgress(textLine);
                }
                br.close();
                fileReader.close();
            } catch (IOException e) {
                QDLogger.e(e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (mReference.get() != null) {
                QDLogger.e("onProgressUpdate:"+values[0]);
                mReference.get().tv_content.append(values[0]);
               // mReference.get().mContentAdapter.append(values[0]);
            }
        }
    }
}
