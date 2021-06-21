package cn.demomaster.huan.quickdeveloplibrary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import cn.demomaster.qdlogger_library.MappedByteBufferHelper;

public class TestMappedByteBuffer {
    private static int length = 1024000;//1G

    private abstract static class Tester {
        private String name;

        public Tester(String name) {
            this.name = name;
        }

        public void runTest() {
            System.out.print(name + ": ");
            long start = System.currentTimeMillis();
            test();
            System.out.println(System.currentTimeMillis() - start + " ms");
        }

        public abstract void test();
    }

    private static Tester[] testers = {
            new Tester("Stream RW") {
                public void test() {
                    try (FileInputStream fis = new FileInputStream(
                            "src/a.txt");
                         DataInputStream dis = new DataInputStream(fis);
                         FileOutputStream fos = new FileOutputStream(
                                 "src/a.txt");
                         DataOutputStream dos = new DataOutputStream(fos);) {

                        byte b = (byte) 0;
                        for (int i = 0; i < length / 2; i++) {
                            dos.writeBytes("A");
                        }
                        dos.flush();
                        /*while (dis.read()!= -1) {
                        }*/
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Tester("Mapped RW") {
                public void test() {
                    try /*(FileChannel channel = FileChannel.open(Paths.get("src/b.txt"),
                            StandardOpenOption.READ, StandardOpenOption.WRITE);)*/ {
                        MappedByteBufferHelper.map(Paths.get("src/c.txt").toFile(), 0, length);

                        //MappedByteBuffer mapBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, length);
                        char b = 'B';
                        for (int i = 0; i < length / 2; i++) {
                            //mapBuffer.put((byte)0);
                            mapBuffer.putChar(b);
                        }
                        mapBuffer.flip();
                        unmap(mapBuffer);
                        /*while(mapBuffer.hasRemaining()) {
                            mapBuffer.get();
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
            new Tester("Mapped PRIVATE") {
                public void test() {
                    try (FileChannel channel = FileChannel.open(Paths.get("src/c.txt"),
                            StandardOpenOption.READ, StandardOpenOption.WRITE);) {
                        MappedByteBuffer mapBuffer = channel.map(FileChannel.MapMode.PRIVATE, 0, length);
                        char c = 'C';
                        for (int i = 0; i < length / 2; i++) {
                            // mapBuffer.put((byte)0);
                            mapBuffer.putChar(c);
                        }
                        mapBuffer.flip();
                       /* while(mapBuffer.hasRemaining()) {
                            mapBuffer.get();
                        }*/
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    };

    public static void main(String[] args) {
        for (Tester tester : testers) {
            tester.runTest();
        }
    }

    /**
     * 測試前需要在src目录下新建a.txt,b.txt,c.txt
     */
    @org.junit.Test
    public void test() {
        //System.out.println(length/1024/1024);
       /* for(Tester tester:testers) {
            tester.runTest();
        }*/
        String filePath = "src/c.txt";
        File file = new File(filePath);
        System.out.println(file.length());
        MappedByteBufferHelper.MyMappedByteBuffer byteBuffer = MappedByteBufferHelper.map(Paths.get(filePath).toFile(), fileSize);
        //MappedByteBuffer mapBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, length);
        for (int i = 0; i < 30; i++) {
            String b = "b1234567890";
            byteBuffer.put(b.getBytes());
            String c = "\n";
            byteBuffer.put(c.getBytes());
            System.out.println(file.length());
        }
        byteBuffer.flip();
        System.out.println(file.length());
        //unmap(mapBuffer);
    }

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


    private static long fileSize = 100;
    static MappedByteBuffer mapBuffer;


    public void write(String s) {
        mapBuffer.put(s.getBytes());
        mapBuffer.flip();
    }
}