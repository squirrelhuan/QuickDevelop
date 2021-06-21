package cn.demomaster.huan.quickdeveloplibrary.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.model.QDFile;
import cn.demomaster.huan.quickdeveloplibrary.util.terminal.ADBHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.terminal.ProcessResult;
import cn.demomaster.qdlogger_library.QDLogger;

public class QDFileUtil {

    private static final String FOLDER_NAME = "gxj";

    /**
     * 初始化保存路径
     *
     * @return
     */
    private static String makeAppPath() {
        String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME;
        File f = new File(storagePath);
        if (!f.exists()) {
            f.mkdir();
        }
        return storagePath;
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param b 得到的图片
     */
    public static String saveBitmap(Bitmap b) {
        String path = makeAppPath();
        long dataTake = System.currentTimeMillis();
        String imgPath = path + "/" + dataTake + ".jpg";
        return QDFileUtil.saveBitmap(b, imgPath);
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
            FileOutputStream fout = new FileOutputStream(imgPath);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);//代表压缩(100-100)%
            bos.flush();
            bos.close();
            File file = new File(imgPath);
            QDLogger.d("save success path:" + imgPath + ",size:" + file.length());
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
                                           String write_str, boolean append) throws IOException {
        //Environment.getExternalStorageDirectory(),
        File file = new File(dirPath + File.separator + fileName);
        writeFileSdcardFile(file, write_str, append);
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
    public static String readFileSdcardFile(File file) throws IOException {
        if (file == null || !file.exists()) {
            return null;
        }
        String res = null;
        try {
            FileInputStream fin = new FileInputStream(file);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
            //res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            QDLogger.e(e);
        }
        return res;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
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

    public static String getFromAssets(Context context, String fileName) {
        InputStream is;
        String text = "";
        try {
            is = context.getResources().getAssets().open(fileName);
            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.
            text = new String(buffer, "UTF-8");
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
                File tmp = new File("tmp123");
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
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
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
        if (dirFile.delete()) {
            Log.e("--Method--", "Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功！");
            return true;
        } else {
            return false;
        }
    }

    public static List<File> getEmptyFiles(String rootPath) {
        List<File> files = new ArrayList<>();
        //String rootPath = Environment.getExternalStorageDirectory().getPath() ;
        File file = new File(rootPath);
        if (file.isDirectory()) {
            QDLogger.d("正在读取:" + file.getPath());
            if (file.listFiles().length == 0) {
                QDLogger.d("空文件夹:" + file.getPath());
                files.add(file);
            } else {
                for (File file1 : file.listFiles()) {
                    files.addAll(getEmptyFiles(file1.getPath()));
                }
            }
        }
        return files;
    }

    /**
     * 通过uri获取绝对路径
     *
     * @param context
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
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
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
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
     * Asyn文件创建时间
     */
    public static void getFileCreatTime(Context context, final File file, final OnSearchListener listener) {
        if (file == null || !file.exists()) {
            return;
        }
        String rs = null;
        final StringBuilder sb = new StringBuilder();
        Process p = null;
        final ADBHelper adbHelper = ADBHelper.getInstance();
        adbHelper.execute(String.format("stat -t %s ", file.getAbsolutePath()), new ADBHelper.OnReceiveListener() {
            @Override
            public void onReceive(ProcessResult result) {
                if (result != null && result.getResult() != null) {
                    String r = result.getResult();
                    QDLogger.i(r);
                    String[] arr = r.split(" ");
                    QDFile qdFile = new QDFile(file);
                    qdFile.setModifyTime(Integer.valueOf(arr[arr.length - 2]));
                    listener.onResult(qdFile);
                    return;
                }
                listener.onResult(null);
            }
        });
        if (0 != sb.length()) {
            rs = sb.toString();
        }
        System.out.println(rs);
    }

    /**
     * Asyn文件创建时间
     */
    public static void getFileCreatTime2(Context context, final File file, final OnSearchListener listener) {
        if (file == null || !file.exists()) {
            return;
        }
        String rs = null;
        final StringBuilder sb = new StringBuilder();
        Process p = null;
        final ADBHelper adbHelper = ADBHelper.getInstance();
        adbHelper.execute(String.format("ls -l %s ", file.getParentFile().getAbsolutePath()), new ADBHelper.OnReceiveListener() {
            @Override
            public void onReceive(ProcessResult result) {
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

    public static interface OnSearchListener {
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
     * 获取内置SD卡路径
     *
     * @return
     */
    public static String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取存储路径
     *
     * @return 所有可用于存储的不同的卡的位置，用一个List来保存
     */
    public static List<String> getExtSDCardPathList() {
        List<String> paths = new ArrayList<String>();
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
                                           String write_str) throws IOException {
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

    /***************************************************************/
    /**
     * 根据URI获取文件真实路径（兼容多张机型）
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByUri(Context context, Uri uri) {
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

                //
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
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
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

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isSdcardExit() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static Uri getUrifromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getUrifromPath1(context, file.getAbsolutePath());
        } else {
            return Uri.fromFile(file);
        }
    }

    public static Uri getUrifromPath1(Context context, String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, path);
            return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);//步骤二：Android 7.0及以上获取文件 Uri
            //fileUri = FileProvider.getUriForFile(context, "cn.demomaster.huan.quickdeveloplibrary.fileprovider", pictureFile);
        } else {
            return Uri.parse(path);
        }
    }

}
