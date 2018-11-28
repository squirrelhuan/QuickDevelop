package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Folder;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;

/**
 * Created by Squirrel桓 on 2018/11/28.
 */
public class PictureManager {

    /**
     * 从SDCard加载图片
     */
    public static void loadImageForSDCard(final Context context, final DataCallback callback) {
        //由于扫描图片是耗时的操作，所以要在子线程处理。
        new Thread(new Runnable() {
            @Override
            public void run() {
                //扫描图片
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = context.getContentResolver();
                Cursor mCursor = mContentResolver.query(mImageUri, new String[]{
                                MediaStore.Images.Media.DATA,
                                MediaStore.Images.Media.DISPLAY_NAME,
                                MediaStore.Images.Media.DATE_ADDED,
                                MediaStore.Images.Media._ID},
                        null,
                        null,
                        MediaStore.Images.Media.DATE_ADDED);

                ArrayList<Image> images = new ArrayList<>();
                //读取扫描到的图片
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(
                            mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    //获取图片名称
                    String name = mCursor.getString(
                            mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                    //获取图片时间
                    long time = mCursor.getLong(
                            mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                    images.add(new Image(path, time, name));
                }
                mCursor.close();
                Collections.reverse(images);
                callback.onSuccess(splitFolder(images));
            }
        }).start();
    }

    /**
     * 把图片按文件夹拆分，第一个文件夹保存所有的图片
     */
    private static ArrayList<Folder> splitFolder(ArrayList<Image> images) {
        ArrayList<Folder> folders = new ArrayList<>();
        folders.add(new Folder("全部图片", images));
        if (images != null && !images.isEmpty()) {
            int size = images.size();
            for (int i = 0; i < size; i++) {
                String path = images.get(i).getPath();
                String name = getFolderName(path);
                if (TextUtils.isEmpty(name)) {
                    Folder folder = getFolder(name, folders);
                    folder.addImage(images.get(i));
                }
            }
        }
        return folders;
    }

    /**
     * 根据图片路径，获取图片文件夹名称
     */
    private static String getFolderName(String path) {
        if (TextUtils.isEmpty(path)) {
            String[] strings = path.split(File.separator);
            if (strings.length >= 2) {
                return strings[strings.length - 2];
            }
        }
        return "";
    }

    private static Folder getFolder(String name, List<Folder> folders) {
        if (folders != null && !folders.isEmpty()) {
            int size = folders.size();
            for (int i = 0; i < size; i++) {
                Folder folder = folders.get(i);
                if (name.equals(folder.getName())) {
                    return folder;
                }
            }
        }
        Folder newFolder = new Folder(name);
        folders.add(newFolder);
        return newFolder;
    }

    public interface DataCallback {
        void onSuccess(ArrayList<Folder> folders);
    }

}
