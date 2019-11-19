package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.model.QDFile;
import cn.demomaster.huan.quickdeveloplibrary.util.terminal.ADBHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.terminal.DeviceModel;
import cn.demomaster.huan.quickdeveloplibrary.util.terminal.ProcessResult;

public class FileUtil {


    /**
     * 写数据到SD中的文件
     *
     * @param fileName
     * @param write_str
     * @throws IOException
     */
    public static void writeFileSdcardFile(String dirPath, String fileName,
                                           String write_str, boolean append) throws IOException {
        writeFileSdcardFile(dirPath + "/" + fileName, write_str, append);
    }

    public static void writeFileSdcardFile(String fileName,
                                           String write_str, boolean append) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream fout = new FileOutputStream(file, append);
            byte[] bytes = write_str.getBytes();

            fout.write(bytes);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 读SD中的文件
    public static String readFileSdcardFile(String fileName) throws IOException {
        String res = null;
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, "UTF-8");
            //res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile())
                return deleteSingleFile(delFile);
            else
                return deleteDirectory(delFile);
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
                return false;
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
                flag = deleteDirectory(file
                        .getAbsolutePath());
                if (!flag)
                    break;
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
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static File uriToFile(Uri uri, Context context) {
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

    public static interface OnSearchListener {
        void onResult(QDFile qdFile);
    }
}
