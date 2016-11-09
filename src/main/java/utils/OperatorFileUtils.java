package utils;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import operatorthread.OnlyReadThread;
import sharepreference.Contants;

/**
 * Created by dave.shi on 2016/5/10.
 */
public class OperatorFileUtils {
    private static final String TAG = OperatorFileUtils.class.getSimpleName() + "_";
    public static File fileDir = Environment.getExternalStorageDirectory();
    //    public static File logFile = new File(fileDir+File.separator);
    public static File logFile = new File(fileDir + File.separator + "flush_test_log");
    public static boolean isNeedRead = true;
    public static boolean isNeedWrite = true;
    public static boolean isCreateFile = true;
    public static boolean isRandomAccess = true;
    private Bundle operatorParams = new Bundle();
    Handler handler;
    private Utils utils = new Utils();
    private static Thread mMoniterThread;
    private long mOverTime;

    public OperatorFileUtils(Handler handler) {
        this.handler = handler;
    }

    public void firstWrite200M(int fileSize) {
        Log.v(TAG, "firstWrite200M");
        if (fileDir.exists()) {
            File file = new File(fileDir + File.separator + "flash_tool");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024 * 1024];
                for (int i = 0; i < buffer.length; i++) buffer[i] = (byte) 0xAA;
                for (int i = 0; i < fileSize; i++) {
                    fileOutputStream.write(buffer);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                saveLog(TAG + ":pre write=>FileNotFoundException");
            } catch (IOException e) {
                e.printStackTrace();
                saveLog(TAG + ":pre write=>IOException");
            }
        }
    }

    /***
     * @param loop:循环次数
     * @param fileSize:文件大小
     * @param checkPeriod:校验周期
     */

    public synchronized void onlyReadFromStorage(int loop, int fileSize, int checkPeriod) {
        Log.v("OperatorFileUtils", "onlyReadFromStorage===");
        int loopCount = 0;
        File file = new File(fileDir + File.separator + "flash_tool");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            while (loopCount < loop) {
                saveLog("读取循环次数:" + loopCount);
                FileInputStream fileInputStream = new FileInputStream(file);
                loopCount++;
                Message message = new Message();
                operatorParams.putInt(Contants.READ_LOOP_KEY, loopCount);
                operatorParams.putInt("readLoopTotal", loop);
                operatorParams.putString(Contants.LOG_PATH_KEY, logFile.getAbsolutePath());
                operatorParams.putString(Contants.CHECK_ERROR_KEY, "");
                byte[] buffer = new byte[1024 * 1024];
                for (int i = 0; i < fileSize; i++) {
                    Log.v(TAG, "read only--------------" + utils.getCurrentTime());
                    saveLog("read loop=" + loopCount);
                    synchronized (OnlyReadThread.class) {
                        if (!isNeedRead) {
                            Log.v(TAG, "isNeedRead----");
                            return;
                        }
                    }
                    int len = fileInputStream.read(buffer);
                    if (checkPeriod > 0 && loopCount % checkPeriod == 0) {
                        for (int j = 0; j < len; j++) {
                            if (buffer[j] != (byte) 0xAA) {
                                saveLog("读取校验出错，出错时循环次数为：" + loopCount);
                                operatorParams.putString(Contants.CHECK_ERROR_KEY, "读取校验错误,当前错误字节：" + buffer[j]);
                                break;
                            }
                        }
                    }

                }
                message.obj = operatorParams;
                handler.sendMessage(message);
                fileInputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            saveLog(TAG + ":read=>FileNotFoundException");
        } catch (IOException e) {
            e.printStackTrace();
            saveLog(TAG + ":read=>IOException");
        }
    }

    /**
     * * @Param: speed, file write speed in byte/second, 0 means random speed
     *
     * @param packageSize kb,每次写入的文件大小,0，默认为 1024
     * @param fileSize    MB写入的总的文件大小
     * @param speed       kb/s,每秒写入的速率
     * @param checkPeriod 校验周期
     * @param leftSpace   剩余空间
     */
    public synchronized void onlyWriteToStorage(int loop, int fileSize, int packageSize, int speed, int checkPeriod, int leftSpace) {
        int loopcount = 0;
        File file = new File(fileDir + File.separator + "flash_tool");
        boolean createIs = false;
        try {
            if (!file.exists()) {
                createIs = file.createNewFile();
                Log.d(TAG, "createTs = " + createIs);
            }
            if (packageSize == 0) {
                packageSize = 1024;
            }
            if (packageSize > 15000) {
                packageSize = 15000;
            }
            byte[] buffer = new byte[packageSize * 1024];
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte) 0xAA;
            }
            while (loopcount < loop) {
                saveLog("当前写入速率：" + speed);
                saveLog("当前写入包大小：" + packageSize);
                FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                Message message = new Message();
                operatorParams.putInt(Contants.WRITE_LOOP_KEY, loopcount);
                operatorParams.putInt("writeLoopCount", loop);
                operatorParams.putString(Contants.LOG_PATH_KEY, logFile.getAbsolutePath());
                operatorParams.putString(Contants.CHECK_ERROR_KEY, "");
                message.obj = operatorParams;
                if (checkPeriod > 0 && loopcount % checkPeriod == 0) {
                    //开始校验
                    for (int i = 0; i < buffer.length; i++) {
                        if (buffer[i] != (byte) 0xAA) {
                            operatorParams.putString(Contants.CHECK_ERROR_KEY, "写入校验错误,当前错误字节：" + buffer[i]);
                            saveLog("只写模式检验出错前循环次数：" + loopcount);
                            break;
                        }
                    }
                }
                handler.sendMessage(message);
                saveLog("写入循环次数：" + loopcount);
                for (int i = 0; i < fileSize * 1024 * 1024 / buffer.length; i++) {
                    Log.i(TAG, "onlyWrite i==" + i + "loopCount=" + loopcount + "--threadId=" + Thread.currentThread().getId());
                    if (!isNeedWrite) {
                        return;
                    }

                    fileOutputStream.write(buffer);
                    if (speed > 1000000) {
                        speed = 1000000;
                    }
                    int sleep;
                    if (speed == 0) sleep = (int) (Math.random() * 1000); // random to [0 - 999]ms
                    else sleep = 1000 * packageSize / speed;
                    SystemClock.sleep(sleep);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                if (leftSpace < 0) {
                    leftSpace = 10;
                }
                int sdFreeSize = (int) utils.getSDFreeSize();
                if (sdFreeSize < leftSpace) {
                    Log.v(TAG, "======删除文件");
                    saveLog("剩余空间为：" + utils.getSDFreeSize() + "--此时删除文件");
                    file.delete();
                    loopcount++;
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            saveLog(TAG + ":write=>IOException");
        } catch (IOException e) {
            e.printStackTrace();
            saveLog(TAG + ":write=>IOException");
        }
    }

    public void setIsNeedRead(boolean isNeedRead) {
        this.isNeedRead = isNeedRead;
    }

    public void setIsNeedWrite(boolean isNeedWrite) {
        this.isNeedWrite = isNeedWrite;
    }

    public void setIsCreateFile(boolean isCreateFile) {
        this.isCreateFile = isCreateFile;
    }

    public void setIsRandomAccess(boolean isRandomAccess) {
        this.isRandomAccess = isRandomAccess;
    }

    public void setIsNeedRunCpu(boolean mRunning) {
        this.mRunning = mRunning;
    }

    private void saveLog(String log) {
        File file = logFile;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            byte[] buffer = log.getBytes();
            fileOutputStream.write(buffer);
            fileOutputStream.write("\r\n".getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 大文件随机访问
     *
     * @param loop
     * @param fileSize 预先创建的文件大小
     */
    public void largeFileRandomAccessFile(int loop, int fileSize) {
        int loopSize = 0;
        File file = new File(fileDir + File.separator + "flush_tool");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            while (loopSize < loop) {

                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                randomAccessFile.seek(file.length());
                loopSize++;
                Message message = new Message();
                message.obj = operatorParams;
                operatorParams.putInt(Contants.RANDOW_ACCESS_LOOP_KEY, loopSize);
                operatorParams.putInt("randomAccessLoopCount", loop);
                operatorParams.putString(Contants.LOG_PATH_KEY, logFile.getAbsolutePath());
                handler.sendMessage(message);
                onlyWriteToStorage(1, fileSize, 1024, 0, -1, -1);
                byte[] readBuff = new byte[64];
                for (int i = 0; i < fileSize * 1024 / readBuff.length; i++) {
                    if (!isRandomAccess) {//終止线程
                        return;
                    }
                    Log.i(TAG, "largeFileAccess");
                    randomAccessFile.seek((long) Math.random() * file.length());
                    randomAccessFile.read(readBuff);
                }
                randomAccessFile.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param loop,循环次数
     * @param fileSize,文件大小,-1，创建随机文件
     * @param random                  随机文件范围
     */
    public void createFile(int loop, int fileSize, int random) {
        File file = new File(fileDir + File.separator + "flush_tool");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            if (fileSize == -1) {
                fileSize = (int) Math.random() * random;
            }
            onlyWriteToStorage(loop, fileSize, 0, 0, -1, -1);
        } catch (Exception e) {
        }
    }


    volatile static boolean mRunning = true;

    /**
     * 加CPU负载达到规定值
     *
     * @param maxCpu 规定值
     */
    public static void runCpuMax(final int maxCpu) {
        //获取到的结果

        //耗时操作，所以写在子线程中
        mMoniterThread = new Thread() {
            String result = "";
            StringBuffer sb;
            //执行shell命令的过程Process
            Process process = null;
            private String digits;

            @Override
            public void run() {
                Log.d(TAG, "mMoniterThread run");
                while (mRunning) {
                    try {
                        //执行shell命令，查询cpu的使用率
                        process = Runtime.getRuntime().exec("top -n 1");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader
                            (process.getInputStream()));
                    //字符串缓冲类型数据，存储获得到的结果
                    sb = new StringBuffer();
                    try {
                        while ((result = br.readLine()) != null) {
                            if (result.trim().length() < 1) {
                                continue;
                            } else {
                                sb.append(result);
                                break;
                            }
                        }
                        String[] arrResult = sb.toString().split(",");
                        //拿到Use CPU的使用率，并分割成数组，前面的为Use，后面的为使用率
                        if (arrResult.length > 0) {
                            String[] userResult = arrResult[0].split(" ");
                            int p = userResult[1].lastIndexOf('%');
                            if (p > 0) digits = userResult[1].substring(0, p);
                        }

                        Log.d(TAG, "cpu digit =" + digits);

                        int j = Integer.parseInt(digits);

                        if (j < 80) {
                            runCpu();
                        } else if (j >= 80 && j <= maxCpu) {
                            //TODO 如果CPU负载达到了目标值

                        } else if (j > maxCpu) {
                            MyThread t = mThreads.get(0);
                            t.flag = true;
                            mThreads.remove(t);
                            Log.d(TAG, "线程" + t.getName() + "已退出了");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Log.d(TAG, "exiting moniter thread");
            }
        };
        mMoniterThread.start();
    }

    /**
     * 结束当前cpu的一些任务，并退出activity
     */
    public static void cpuOver() {
        try {
            mRunning = false;
            mMoniterThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "mThreads.size() = " + mThreads.size());
        for (MyThread t : mThreads) {
            try {
                t.flag = true;
                t.join();
                Log.d(TAG, "线程 " + t.getName() + " 已退出~");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static List<MyThread> mThreads = new ArrayList<MyThread>();

    /**
     * 用Fibonacci数列提高cpu使用率
     */
    public static void runCpu() {
        MyThread thread = new MyThread();
        thread.start();
        mThreads.add(thread);
        Log.d(TAG, "list size =" + mThreads.size());
    }

    public static class MyThread extends Thread {
        public volatile boolean flag = false;

        @Override
        public void run() {
            //进行斐波那契数列运算
            int a = 1, b = 1;
            Log.d(TAG, "fibonacci start");
            while (true) {
                int c = a + b;
                a = b;
                b = c;
                if (b >= Integer.MAX_VALUE / 3) {
                    a = 1;
                    b = 1;
                }
                if (flag) {
                    break;
                }
            }
            Log.d(TAG, "fibonacci over");
        }
    }


}
