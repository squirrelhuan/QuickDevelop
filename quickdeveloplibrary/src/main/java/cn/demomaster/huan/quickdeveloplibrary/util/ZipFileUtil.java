package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import cn.demomaster.qdlogger_library.QDLogger;

public class ZipFileUtil {

    /**
     * 在/data/data/下创建一个res文件夹，存放4个文件
     */
    private void copyDbFile(Context context, String fileName) {
        InputStream in = null;
        FileOutputStream out = null;
        String path = "/data/data/" + context.getPackageName() + "/file/res/";
        File file = new File(path + fileName);

        //创建文件夹
        File filePath = new File(path);
        if (!filePath.exists())
            filePath.mkdirs();

        if (file.exists())
            return;

        try {
            in = context.getAssets().open(fileName); // 从assets目录下复制
            out = new FileOutputStream(file);
            int length = -1;
            byte[] buf = new byte[1024];
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            out.flush();
        } catch (Exception e) {
            QDLogger.e(e);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 文件压缩
     *
     * @param sourcePath    源文件（待压缩的文件路径）
     * @param targetZipName 压缩到此处
     */
    public static void compressFile(String sourcePath, String targetZipName) throws FileNotFoundException {
        File targetFile = new File(targetZipName);

        //创建文件夹
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            throw new FileNotFoundException();
        }
        if (targetFile.exists()) {
            QDLogger.e("压缩目标文件已存在--删除");
            QDFileUtil.delete(targetFile.getAbsolutePath());
        }
        QDFileUtil.createFile(targetFile);
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(targetFile), new CRC32()));
            zip(zipOutputStream, sourceFile.getName(), sourceFile);
            zipOutputStream.flush();
            zipOutputStream.close();
        } catch (Exception e) {
            QDLogger.e(e);
        }
        QDLogger.println("压缩完成[" + targetZipName + "]");
    }

    /**
     * 解压文件
     *
     * @param context
     */
    public static void unZip(Context context) {
        String PATH = "/data/data/" + context.getPackageName() + "/file/unzip/";
        File FILE = new File("/data/data/" + context.getPackageName() + "/file/res.zip");
        try {
            upZipFile(FILE, PATH);
        } catch (IOException e) {
            QDLogger.e(e);
        }
        QDLogger.d("解压完成");
    }

    public static void zip(ZipOutputStream zipOutputStream, String name, File fileSrc) throws IOException {

        if (fileSrc.isDirectory()) {
            QDLogger.println("需要压缩的地址是目录");
            File[] files = fileSrc.listFiles();

            name = name + "/";
            zipOutputStream.putNextEntry(new ZipEntry(name));  // 建一个文件夹
            QDLogger.println("目录名: " + name);

            for (File f : files) {
                zip(zipOutputStream, name + f.getName(), f);
                QDLogger.println("目录: " + name + f.getName());
            }

        } else {
            QDLogger.println("需要压缩的地址是文件");
            zipOutputStream.putNextEntry(new ZipEntry(name));
            QDLogger.println("文件名: " + name);
            FileInputStream input = new FileInputStream(fileSrc);
            QDLogger.println("文件路径: " + fileSrc);
            byte[] buf = new byte[1024];
            int len = -1;

            while ((len = input.read(buf)) != -1) {
                zipOutputStream.write(buf, 0, len);
            }

            zipOutputStream.flush();
            input.close();
        }
    }

    /**
     * 解压缩
     * 将zipFile文件解压到folderPath目录下.
     *
     * @param zipFile    zip文件
     * @param folderPath 解压到的地址
     * @throws IOException
     */
    public static void upZipFile(File zipFile, String folderPath) throws IOException {
        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                QDLogger.d("ze.getName() = " + ze.getName());
                String dirstr = folderPath + ze.getName();
                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                QDLogger.d("str = " + dirstr);
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            QDLogger.d("ze.getName() = " + ze.getName());
            OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                try {
                    substr = new String(substr.getBytes("8859_1"), "GB2312");
                } catch (UnsupportedEncodingException e) {
                    QDLogger.e(e);
                }
                ret = new File(ret, substr);

            }
            QDLogger.d("1ret = " + ret);
            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length - 1];
            try {
                substr = new String(substr.getBytes("8859_1"), "GB2312");
                QDLogger.d("substr = " + substr);
            } catch (UnsupportedEncodingException e) {
                QDLogger.e(e);
            }
            ret = new File(ret, substr);
            QDLogger.d("2ret = " + ret);
            return ret;
        }
        return ret;
    }

}
