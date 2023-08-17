package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.demomaster.qdlogger_library.QDLogger;

public class QuickMediaUitl {


    /**
     * 获取文件类型
     *
     * @param dataPath
     * @return
     */
    public static String getMediaFileType(String dataPath) {
        if (!TextUtils.isEmpty(dataPath)) {
            android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            try {
                mmr.setDataSource(dataPath);
                return mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mmr.release();
            }
        }
        return null;
    }

    public static Bitmap getAlbumPicture(Context context, String dataPath) {
        if (TextUtils.isEmpty(dataPath)) {
            return null;
        }
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(dataPath);
            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
            QDLogger.println("title:" + title + ",album:" + album + ",artist:" + artist + ",duration:" + duration);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        byte[] data = mmr.getEmbeddedPicture();
        Bitmap albumPicture = null;
        if (data != null) {
            //获取bitmap对象
            albumPicture = BitmapFactory.decodeByteArray(data, 0, data.length);
            //获取宽高
            int width = albumPicture.getWidth();
            int height = albumPicture.getHeight();
            //Log.w("DisplayActivity","width = "+width+" height = "+height);
            // 创建操作图片用的Matrix对象
            Matrix matrix = new Matrix();
            // 计算缩放比例
           /* float sx = ((float) 120 / width);
            float sy = ((float) 120 / height);
            // 设置缩放比例
            matrix.postScale(sx, sy);*/
            // 建立新的bitmap，其内容是对原bitmap的缩放后的图
            albumPicture = Bitmap.createBitmap(albumPicture, 0, 0, width, height, matrix, false);
            return albumPicture;
        } else {
            return null;
            /*albumPicture = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_001);
            return albumPicture;*/
        }
    }

    private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");

    /**
     * 从文件当中获取专辑封面位图
     */
    public static Bitmap getArtworkFromFile(Context context, long songid, long albumid) {
        Bitmap bm = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            FileDescriptor fd = null;
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/"
                        + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    fd = pfd.getFileDescriptor();
                }
            } else {
                try {
                    Uri uri = ContentUris.withAppendedId(albumArtUri, albumid);
                    ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                    if (pfd != null) {
                        fd = pfd.getFileDescriptor();
                    }
                } catch (Exception e) {
                    QDLogger.e("未找到歌曲对应的封面:songid=" + songid);
                    return null;
                }
            }
            options.inSampleSize = 1;
            // 只进行大小判断
            options.inJustDecodeBounds = true;
            // 调用此方法得到options得到图片大小
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            // 我们的目标是在800pixel的画面上显示
            // 所以需要调用computeSampleSize得到图片缩放的比例，根据需要开启这一行代码
//            options.inSampleSize = 100;
            // 我们得到了缩放的比例，现在开始正式读入Bitmap数据
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            //根据options参数，减少所需要的内存
            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }


    public static Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        Bitmap bitmap = null;
        try {
            retriever.setDataSource(videoPath);
            bitmap = retriever.getFrameAtTime();
            if (bitmap != null) {
                return bitmap;
            } else {
                File videoFile = new File(videoPath);
                String tempVideoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_video.mp4";
                if (videoFile.exists()) {
                    videoFile.renameTo(new File(tempVideoPath));
                    bitmap = BitmapFactory.decodeFile(tempVideoPath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

}
