package cn.demomaster.huan.quickdeveloplibrary.util;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.BufferOverflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import cn.demomaster.huan.quickdeveloplibrary.constant.TAG;

public class MappedByteBufferHelper {

    MappedByteBufferHelper() {

    }

    public static MyMappedByteBuffer map(File file, long size) {
        return map(file, -1, size);
    }

    public static MyMappedByteBuffer map(File file, long position, long size) {
        try {
            // FileInputStream inputStream = new FileInputStream(file);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file.getAbsoluteFile(), "rw");
            //FileChannel channel =  FileChannel.open(Paths.get(file.getAbsolutePath()),StandardOpenOption.READ, StandardOpenOption.WRITE);
            if (position == -1) {
                position = file.length();
            }
            //MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
            //FileChannel channel = FileChannel.open(Paths.get("src/c.txt"),StandardOpenOption.READ, StandardOpenOption.WRITE);) {
            return map(file, randomAccessFile, position, size);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static MyMappedByteBuffer map(File file, RandomAccessFile randomAccessFile, long position, long size) {
        MyMappedByteBuffer byteBuffer = null;
        try {
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer byteBuffer1 = channel.map(FileChannel.MapMode.READ_WRITE, position, size);
            byteBuffer = new MyMappedByteBuffer(file, randomAccessFile, position, size, byteBuffer1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return byteBuffer;
    }

    public static MyMappedByteBuffer map_log(File file, long size) {
        //去除文本中的占位符NUL
        delNulAtLastLine(file.getAbsolutePath());
        return map(file, -1, size);
    }

    /**
     * 去除文本末尾的的占位符NUL，对应的ascii码表为0
     *
     * @param fileName
     * @throws Exception
     */
    public static void delNulAtLastLine(String fileName) {
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(fileName, "rw");
            if (file != null&&file.length()>0) {
                long len = file.length();
                long start = file.getFilePointer();
                long nextend = start + len - 1;
                int i = -1;
                // 移动指针
                file.seek(nextend);
                byte[] buf = new byte[1];
                W:
                while (nextend > start) {
                    i = file.read(buf, 0, 1);
                    if (buf[0] == 0) {
                        file.setLength(nextend - start);
                    } else {
                        break W;
                    }
                    nextend--;
                    file.seek(nextend);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class MyMappedByteBuffer {
        MappedByteBuffer byteBuffer;
        RandomAccessFile randomAccessFile;
        long position;
        long length;
        long size = 10000;
        String filePath;

        public MappedByteBuffer getByteBuffer() {
            return byteBuffer;
        }

        /**
         * 写入缓存
         *
         * @param bytes
         */
        public void put(byte[] bytes) {
            try {
                byteBuffer.put(bytes);
                length += bytes.length;
            } catch (Exception e) {
                if (e instanceof BufferOverflowException) {
                    System.out.println("log缓存已满，触发扩容,当前大小：" + length + ",扩展大小：" + size + ",fileSize=" + new File(filePath).length() + ":" + new String(bytes));
                    if (randomAccessFile != null) {
                        close();
                    }
                    MyMappedByteBuffer mappedByteBuffer = map_log(new File(filePath), Math.max(size, bytes.length));
                    this.byteBuffer = mappedByteBuffer.byteBuffer;
                    put(bytes);
                } else {
                    e.printStackTrace();
                }
            }
        }

        public MyMappedByteBuffer(File file, RandomAccessFile randomAccessFile, long position, long size, MappedByteBuffer byteBuffer) {
            this.byteBuffer = byteBuffer;
            this.filePath = file.getAbsolutePath();
            this.position = position;
            this.length = position;
            this.size = size;
            this.randomAccessFile = randomAccessFile;
        }

        public void flip() {
            if (byteBuffer != null) {
                byteBuffer.flip();
            }
        }

        public void close() {
            if (byteBuffer != null) {
                byteBuffer.clear();
                //byteBuffer.flip();
            } else if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            randomAccessFile = null;
        }
    }

    /**
     * 解除内存与文件的映射
     */
    public static void unmap(MappedByteBuffer mbbi) {
        if (mbbi == null) {
            return;
        }
        try {
            Class<?> clazz = Class.forName("sun.nio.ch.FileChannelImpl");
            Method m = clazz.getDeclaredMethod("unmap", MappedByteBuffer.class);
            m.setAccessible(true);
            m.invoke(null, mbbi);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
