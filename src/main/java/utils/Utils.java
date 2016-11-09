package utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dave.shi on 2016/5/13.
 */
public class Utils {
    public long getSDFreeSize(){
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize)/1024 /1024; //单位MB
    }
    //获取时间
    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
    public static String getTimeFormat(long time) {
        long day = time / (1000*60*60*24);
        time = time % ((1000*60*60*24));
        long hour = time/(1000*60*60);
        time = time%(1000*60*60);
        long minu = time/(1000*60);
        time = time%(1000*60);
        long mm = time/1000;
        return day+"天 "+hour+"时 "+minu+"分"+mm+"秒";
    }
    public static String getprocessPid(){
        return "_"+android.os.Process.myPid();
    }
}
