package cn.demomaster.huan.quickdeveloplibrary.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.demomaster.huan.quickdeveloplibrary.model.QDFile;
import cn.demomaster.huan.quickdeveloplibrary.util.audio.ByteUtils;
import cn.demomaster.huan.quickdeveloplibrary.util.terminal.ADBHelper;
import cn.demomaster.qdlogger_library.QDLogger;

public class QDFileUtil {

    /**
     * 保存Bitmap到sdcard
     *
     * @param b 得到的图片
     */
    public static String saveBitmap(Context context, Bitmap b) {
        String path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        long dataTake = System.currentTimeMillis();
        String imgPath = path + File.separator + dataTake + ".jpg";
        return QDFileUtil.saveBitmap(b, imgPath);
    }

    /**
     * @param context
     * @param filename 是文件全名，包括后缀哦
     * @param listener
     */
    public static void updateMediaFile(Context context, String filename, MediaScannerConnection.OnScanCompletedListener listener) {
//        MediaScannerConnection.scanFile(context,
//                new String[]{filename}, null, listener);
        MediaScannerConnection.scanFile(
                context.getApplicationContext(),
                new String[]{filename},
                null,listener);
    }

    /**
     * 根据图片文件路径获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapFromPath(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }

    public static String saveBitmap(Bitmap b, String imgPath) {
        try {
            createFile(new File(imgPath));
            FileOutputStream fout = new FileOutputStream(imgPath);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);//代表压缩(100-100)%
            bos.flush();
            bos.close();
            return imgPath;
        } catch (IOException e) {
            QDLogger.e(e);
        }
        return null;
    }

    /**
     * 写数据到SD中的文件
     *
     * @param fileName
     * @param write_str
     * @throws IOException
     */
    public static void writeFileSdcardFile(String dirPath, String fileName,
                                           String write_str, boolean append) {
        File file = new File(endWithSeparator(dirPath) + fileName);
        writeFileSdcardFile(file, write_str, append);
    }

    public static void writeFileSdcardFile(String fileName,
                                           byte[] bytes, boolean append) {
        writeFile(new File(fileName), bytes, append);
    }

    public static String endWithSeparator(String filePath) {
        if (filePath != null && !filePath.endsWith(File.separator)) {
            return filePath + File.separator;
        }
        return filePath;
    }

    public static void writeFileSdcardFile(String fileName,
                                           String write_str, boolean append) {
        File file = new File(fileName);
        writeFileSdcardFile(file, write_str, append);
    }

    public static void writeFileSdcardFile(File file,
                                           String write_str, boolean append) {
        writeFile(file, write_str.getBytes(), append);
    }

    public static void writeFile(File file,
                                 byte[] bytes, boolean append) {
        FileOutputStream fout = null;
        try {
            if (!file.exists()) {
                createFile(file);
            }
            if (file.exists()) {
                fout = new FileOutputStream(file, append);
                fout.write(bytes);
                fout.flush();
                fout.close();
            }
        } catch (Exception e) {
            QDLogger.e(e);
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String readFileSdcardFile(String fileName) throws IOException {
        return readFileSdcardFile(new File(fileName));
    }

    // 读SD中的文件
    public static String readFileSdcardFile(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        String res = null;
        try {
            FileInputStream fin = new FileInputStream(file);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, Charset.forName("UTF-8"));
            //res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            QDLogger.e(e);
        }
        return res;
    }

    // 读SD中的文件
    public static String readFileSdcardFile(InputStream is) {
        String text = null;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer, Charset.forName("UTF-8"));
        } catch (IOException e) {
            QDLogger.e(e);
        }
        return text;
    }

    public static File sss(Context context, Uri uri) {
        File file = null;
        //把文件保存到沙盒
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);// MediaStore.Images.Media.DATA
            String displayName = cursor.getString(column_index);
            try {
                InputStream is = contentResolver.openInputStream(uri);
                File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + displayName);
                FileOutputStream fos = new FileOutputStream(cache);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    FileUtils.copy(is, fos);
                }
                file = cache;
                fos.close();
                is.close();
                //String str = QDFileUtil.readFileSdcardFile(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return file;
    }

    /**
     * 从项目中res文件目录下复制文件到指定目录
     *
     * @param context
     * @param path
     * @param resourceId
     * @return
     */
    public static String copyFromResource(Context context, String path, int resourceId) {
        File file = new File(path);
        final BufferedInputStream in = new BufferedInputStream(context.getResources().openRawResource(resourceId));
        final BufferedOutputStream out;
        try {
            out = new BufferedOutputStream(context.openFileOutput(file.getName(), Context.MODE_PRIVATE));
            byte[] buf = new byte[1024];
            int size = in.read(buf);
            while (size > 0) {
                out.write(buf, 0, size);
                size = in.read(buf);
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    public static byte[] readResource(Context context, int resourceId) {
        final InputStream is = context.getResources().openRawResource(resourceId);
        byte[] tt = new byte[1024 * 10];
        byte[] bytes1 = new byte[0];
        int len;
        try {
            while (((len = is.read(tt)) != -1)) {
                bytes1 = ByteUtils.merger(bytes1, tt);
                //System.out.println("接收信息：" + qdMessage.getTime() + "," + reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes1;
    }

    public static String getFromAssets(Context context, String fileName) {
        InputStream is;
        String text = null;
        try {
            is = context.getResources().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer, Charset.forName("UTF-8"));
        } catch (IOException e) {
            QDLogger.e(e);
        }
        return text;
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param delFile 要删除的文件夹或文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String delFile) {
        File file = new File(delFile);
        if (file == null || !file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteSingleFile(file.getAbsolutePath());
            } else {
                return deleteDirectory(file.getAbsolutePath());
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private static boolean deleteSingleFile(String filePath) {
        File file = new File(filePath);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                //Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
                File tmp = new File("tmp123756743543");
                file.renameTo(tmp);
                return tmp.delete();
            }
        } else {
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    private static boolean deleteDirectory(String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(file.getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        return dirFile.delete();
    }

    public static List<File> getEmptyFiles(String rootPath) {
        File file = new File(rootPath);
        if (file.isDirectory()) {
            List<File> files = new ArrayList<>();
            QDLogger.println("正在读取:" + file.getPath());
            if (Objects.requireNonNull(file.listFiles()).length == 0) {
                QDLogger.println("空文件夹:" + file.getPath());
                files.add(file);
            } else {
                for (File file1 : Objects.requireNonNull(file.listFiles())) {
                    List<File> fileList = getEmptyFiles(file1.getPath());
                    if (fileList != null) {
                        files.addAll(fileList);
                    }
                }
            }
            return files;
        }
        return null;
    }

    public static File uriToFile(Uri uri, Context context) {
        if (uri == null || context == null) {
            return null;
        }
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                String buff = "(" +
                        MediaStore.Images.ImageColumns.DATA +
                        "=" +
                        "'" +
                        path +
                        "'" +
                        ")";
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff, null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
            return new File(path);
        } else {
            return new File(uri.getPath());
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 文件创建时间
     */
    public static void getFileCreatTime(final File file, final OnSearchListener listener) {
        if (file == null || !file.exists()) {
            return;
        }
        String rs = null;
        final StringBuilder sb = new StringBuilder();
        final ADBHelper adbHelper = ADBHelper.getInstance();
        adbHelper.execute(String.format("stat -t %s ", file.getAbsolutePath()), result -> {
            if (result != null && result.getResult() != null) {
                String r = result.getResult();
                QDLogger.i(r);
                String[] arr = r.split(" ");
                QDFile qdFile = new QDFile(file);
                qdFile.setModifyTime(Integer.parseInt(arr[arr.length - 2]));
                listener.onResult(qdFile);
                return;
            }
            listener.onResult(null);
        });
        if (0 != sb.length()) {
            rs = sb.toString();
        }
        System.out.println(rs);
    }

    /**
     * 文件创建时间
     */
    public static void getFileCreatTime2(final File file, final OnSearchListener listener) {
        if (file == null || !file.exists()) {
            return;
        }
        String rs = null;
        final StringBuilder sb = new StringBuilder();
        final ADBHelper adbHelper = ADBHelper.getInstance();
        adbHelper.execute(String.format("ls -l %s ", file.getParentFile().getAbsolutePath()), result -> {
            QDLogger.i(result.getResult());
            if (result != null && result.getResult() != null) {
                String r = result.getResult();
                if (r.contains("\n\r")) {
                    String[] arr = r.split("\n\r");
                    List<String> list = Arrays.asList(arr);
                    for (int i = 1; i < list.size(); i++) {
                        String name = list.get(i);
                        if (name.endsWith(file.getName())) {
                            //QDLogger.e("-------------------------"+name);
                            if (name.contains(" ")) {
                                String[] strings = name.split(" ");
                                if (strings != null && strings.length >= 2) {
                                    if (strings[strings.length - 1].equals(file.getName())) {
                                        QDFile qdFile = new QDFile(file);
                                        qdFile.setCreatTimeStr(strings[strings.length - 3] + " " + strings[strings.length - 2]);
                                        listener.onResult(qdFile);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        if (0 != sb.length()) {
            rs = sb.toString();
        }
        System.out.println(rs);
    }

    public static boolean existsFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static String createFile(File file) {
        try {
            if (!file.exists()) {
                //创建目录之后再创建文件
                createDir(file.getParentFile().getAbsolutePath());
                if (file.getParentFile().exists()) {
                    file.createNewFile();
                }
            }
        } catch (Exception e) {
            QDLogger.e(e);
        }
        return file.getAbsolutePath();
    }

    /**
     * 创建文件夹
     *
     * @param dirPath
     * @return
     */
    public static String createDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dirPath;
    }

    public static void renameFile(String oldPath, String newPath) {
        File oleFile = new File(oldPath);
        File newFile = new File(newPath);
        //执行重命名
        oleFile.renameTo(newFile);
    }

    /**
     * 拼接文件路径
     *
     * @param downloadDirectory
     * @param fileName
     * @return
     */
    public static String genateFilePath(String downloadDirectory, String fileName) {
        if (TextUtils.isEmpty(downloadDirectory) || TextUtils.isEmpty(fileName)) {
            return null;
        }
        if (downloadDirectory.endsWith(File.separator)) {
            downloadDirectory = downloadDirectory.substring(0, downloadDirectory.length() - 1);
        }
        if (fileName.startsWith(File.separator)) {
            fileName = downloadDirectory.substring(1);
        }
        return downloadDirectory + File.separator + fileName;
    }

    /**
     * 剪切
     * @param file
     * @param mp3
     */
    public static boolean moveTo(File file, File newFile) {
        return file.renameTo(newFile);
    }

    public interface OnSearchListener {
        void onResult(QDFile qdFile);
    }

    /**
     * 通过反射调用获取内置存储和外置sd卡根路径(通用)
     *
     * @param mContext    上下文
     * @param is_removale 是否可移除，false返回内部存储路径，true返回外置SD卡路径
     * @return
     */
    public static String getStoragePath(Context mContext, boolean is_removale) {
        String path = "";
        //使用getSystemService(String)检索一个StorageManager用于访问系统存储功能。
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);

            for (int i = 0; i < Array.getLength(result); i++) {
                Object storageVolumeElement = Array.get(result, i);
                path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    //return path;
                }
            }
        } catch (Exception e) {
            QDLogger.e(e);
        }
        return path;
    }

    /**
     * 获取存储路径
     *
     * @return 所有可用于存储的不同的卡的位置，用一个List来保存
     */
    public static List<String> getExtSDCardPathList() {
        List<String> paths = new ArrayList<>();
        String extFileStatus = Environment.getExternalStorageState();
        File extFile = Environment.getExternalStorageDirectory();
        //首先判断一下外置SD卡的状态，处于挂载状态才能获取的到
        if (extFileStatus.equals(Environment.MEDIA_MOUNTED) && extFile.exists() && extFile.isDirectory() && extFile.canWrite()) {
            //外置SD卡的路径
            paths.add(extFile.getAbsolutePath());
        }
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int mountPathIndex = 1;
            while ((line = br.readLine()) != null) {
                // format of sdcard file system: vfat/fuse
                if ((!line.contains("fat") && !line.contains("fuse") && !line
                        .contains("storage"))
                        || line.contains("secure")
                        || line.contains("asec")
                        || line.contains("firmware")
                        || line.contains("shell")
                        || line.contains("obb")
                        || line.contains("legacy") || line.contains("data")) {
                    continue;
                }
                String[] parts = line.split(" ");
                int length = parts.length;
                if (mountPathIndex >= length) {
                    continue;
                }
                String mountPath = parts[mountPathIndex];
                if (!mountPath.contains("/") || mountPath.contains("data")
                        || mountPath.contains("Data")) {
                    continue;
                }
                File mountRoot = new File(mountPath);
                if (!mountRoot.exists() || !mountRoot.isDirectory()
                        || !mountRoot.canWrite()) {
                    continue;
                }
                boolean equalsToPrimarySD = mountPath.equals(extFile
                        .getAbsolutePath());
                if (equalsToPrimarySD) {
                    continue;
                }
                //扩展存储卡即TF卡或者SD卡路径
                paths.add(mountPath);
            }
        } catch (IOException e) {
            QDLogger.e(e);
        }
        return paths;
    }


    /**
     * 写数据到SD中的文件
     *
     * @param fileName
     * @param write_str
     * @throws IOException
     */
    public static void writeFileSdcardFile(String filePath, String fileName,
                                           String write_str) {
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(filePath + "/" + fileName);
            FileOutputStream fout = new FileOutputStream(file);
            byte[] bytes = write_str.getBytes();

            fout.write(bytes);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            QDLogger.e(e);
        }
    }


    /*File historyFile = new File(historyRoot, "history-" + System.currentTimeMillis() + ".csv");
    try (
    OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(historyFile), StandardCharsets.UTF_8)) {
        out.write(history);
        return Uri.parse("file://" + historyFile.getAbsolutePath());
    } catch (IOException ioe) {
        Log.w(TAG, "Couldn't access file " + historyFile + " due to " + ioe);
        return null;
    }*/

    /**
     * 通过uri获取绝对路径
     *
     * @param context
     * @param contentUri
     * @return
     */
//    public static String getRealPathFromURI(Context context, Uri contentUri) {
//        String res = null;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//            cursor.close();
//        }
//        return res;
//    }
    /**
     * 根据URI获取文件真实路径（兼容多张机型）
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion >= 19) { // api >= 19
                return getRealPathFromUriAboveApi19(context, uri);
            } else { // api < 19
                return getRealPathFromUriBelowAPI19(context, uri);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String type = documentId.split(":")[0];
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                filePath = getDataColumn(context, contentUri, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            } else if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else {
                //Log.e("路径错误");
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument2(Context context, Uri fileUri) {
// 获取 ContentResolver 实例
        ContentResolver contentResolver = context.getContentResolver();
// 调用 getUriPermissions() 方法来检查该 URI 是否具有访问外部存储的权限
        Cursor cursor = contentResolver.query(fileUri, null, null, null, null);
        if (cursor != null ) {
            while(cursor.moveToNext()) {

            }
        } else {
            // 如果无法获取到解析信息，执行其他操作
        }
        return false;
    }
    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isSdcardExit() {
        return (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
    }

    public static Uri getUrifromFile(Context context, String authority, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getUrifromPath1(context, authority, file.getAbsolutePath());
        } else {
            return Uri.fromFile(file);
        }
    }

    public static Uri getUrifromPath1(Context context, String authority, String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File file = new File(path);
            String fileType = context.getContentResolver().getType(Uri.fromFile(file));
            QDLogger.e("getUrifromPath1 fileType=" + fileType + ",authority=" + authority);
            if (!file.isDirectory() && !TextUtils.isEmpty(fileType)) {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, path);
                Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);//步骤二：Android 7.0及以上获取文件 Uri
                return uri;
            } else {
                if (file.isDirectory()) {
                    //"cn.demomaster.huan.quickdeveloplibrary.fileprovider"
                    Uri fileUri = FileProvider.getUriForFile(context, authority, file);
                    QDLogger.e("fileUri=" + fileUri.toString());
                    return fileUri;
                }
                return null;
            }
            //fileUri = FileProvider.getUriForFile(context, "cn.demomaster.huan.quickdeveloplibrary.fileprovider", pictureFile);
        } else {
            return Uri.parse(path);
        }
    }

    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.#");

    /**
     * 单位换算
     *
     * @param size      单位为B
     * @param isInteger 是否返回取整的单位
     * @return 转换后的单位
     */
    public static String formatFileSize(long size, boolean isInteger) {
        DecimalFormat df = isInteger ? fileIntegerFormat : fileDecimalFormat;
        String fileSizeString;
        if (size < 1024 && size >= 0) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1024 * 1024) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1024 * 1024 * 1024) {
            fileSizeString = df.format((double) size / (1024 * 1024)) + "M";
        } else {
            fileSizeString = df.format((double) size / (1024 * 1024 * 1024)) + "G";
        }
        return fileSizeString;
    }

    /**
     * SDCARD是否存
     */
    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取SDCARD剩余存储空间
     *
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 获取SDCARD总的存储空间
     *
     * @return
     */
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 获取系统总内存
     *
     * @param context 可传入应用程序上下文。
     * @return 总内存大单位为B。
     */
    public static long getTotalMemorySize(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024L;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     *
     * @param context 可传入应用程序上下文。
     * @return 当前可用内存单位为B。
     */
    public static long getAvailableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static String getFileUniqueKey(File file) {
        return getFileMD5(file) + file.length();
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /*************************************************/
    protected static final String MIMETYPES_PROPERTIES = "FileTypes.properties";
    protected static Properties mFileTypes;

    private static void setFileTypes(Context context) {
        try {
            mFileTypes = new Properties();
            mFileTypes.load(context.getAssets().open(MIMETYPES_PROPERTIES));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileType(Context context, File file) {
        if (file == null || !file.exists() || file.length() < 11) {
            return null;
        }
        if (mFileTypes == null) {
            setFileTypes(context);
        }
        String header = getByteHeader(file,10);
        QDLogger.println("header=" + header);
        String fileSuffix = mFileTypes.getProperty(header);
        /*
         * 优化处理：在不同的设备上同样类型的文件，文件头前面内容未必一致，可能只有前几个一致，后面就不同了
         * （例如：jpg类型文件，在不同手机上，lennovo k900前10个是一致的，但是MI3只有前5个字符一致，后面是不一样的，所有一些情况进行特殊处理）当整个头文件失败后，
         * 在进行前5个字符截取对比处理，优化具体如下：
         */
        if (TextUtils.isEmpty(fileSuffix)) {
            Iterator keyList = mFileTypes.keySet().iterator();
            //并不是所有的文件格式前10 byte（jpg）都一致，前五个byte一致即可
            String key, keySearchPrefix = header.substring(0, 5);
            while (keyList.hasNext()) {
                key = (String) keyList.next();
                if (key.contains(keySearchPrefix)) {
                    fileSuffix = mFileTypes.getProperty(key);
                    break;
                }
            }
        }

        //前5个字符截取对比处理没有找到，则进行特殊处理
        if (TextUtils.isEmpty(fileSuffix)) {
            header = getByteHeader(file,3);
            fileSuffix = mFileTypes.getProperty(header);
        }
        return fileSuffix;
    }

    public String getFileType(byte[] bytes) {
        if (bytes == null || bytes.length < 11) {
            return null;
        }
        String header = bytesToHexString(subarray(bytes, 0, 10));
        String fileSuffix = mFileTypes.getProperty(header);
        /*
         * 优化处理：在不同的设备上同样类型的文件，文件头前面内容未必一致，可能只有前几个一致，后面就不同了
         * （例如：jpg类型文件，在不同手机上，lennovo k900前10个是一致的，但是MI3只有前5个字符一致，后面是不一样的，所有一些情况进行特殊处理）当整个头文件失败后，
         * 在进行前5个字符截取对比处理，优化具体如下：
         */
        if (TextUtils.isEmpty(fileSuffix)) {
            Iterator keyList = mFileTypes.keySet().iterator();
            //并不是所有的文件格式前10 byte（jpg）都一致，前五个byte一致即可
            String key, keySearchPrefix = header.substring(0, 5);
            while (keyList.hasNext()) {
                key = (String) keyList.next();
                if (key.contains(keySearchPrefix)) {
                    fileSuffix = mFileTypes.getProperty(key);
                    break;
                }
            }
        }
        //前5个字符截取对比处理没有找到，则进行特殊处理
        if (TextUtils.isEmpty(fileSuffix)) {
            header = bytesToHexString(subarray(bytes, 0, 3));
            fileSuffix = mFileTypes.getProperty(header);
        }
        return fileSuffix;
    }

    public byte[] subarray(final byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return new byte[0];
        }

        final byte[] subarray = new byte[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    private static String getByteHeader(File file,int len) {
        InputStream input = null;
        String value = null;
        try {
            input = new FileInputStream(file);
            byte[] b = new byte[len];
            input.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                //IoUtils.closeSecure(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    /**
     * 当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径，否则就调用getCacheDir()方法来获取缓存路径。前者获取到的就是 /sdcard/Android/data//cache 这个路径，而后者获取到的是 /data/data//cache 这个路径。
     *   注意：这两种方式的缓存都会在卸载app的时候被系统清理
     *
     * @param context
     * @return
     */
    public static String getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir == null) {
                cachePath = context.getCacheDir().getPath();
            } else {
                cachePath = externalCacheDir.getPath();
            }
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static Uri fileToUri(Context context, File file){
        if (file != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            } else {
                return  Uri.fromFile(file);
            }
        }
        return null;
    }

    public static List<File> getFilesFromDir(String filePath) {
        List<File> filespath = new ArrayList<>();
        File f = new File(filePath);
        if (f.exists()) {
            try {
                File[] files = f.listFiles();// 列出所有文件
                for (File file : files) {
                    if(file.length()>0) {
                        filespath.add(file);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return filespath;
    }

    public static boolean isHttpUrl(String urls) {
        Pattern pattern = Pattern.compile("^(http|https)://");
        Matcher matcher = pattern.matcher(urls);
        return matcher.find();
    }
}
