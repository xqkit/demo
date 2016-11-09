package operatorthread;

import android.os.Bundle;
import android.os.Handler;

import utils.OperatorFileUtils;


/**
 * Created by dave.shi on 2016/5/12.
 */
public class FileRandomAccessThread extends Thread {
    Handler handler;
    Bundle params;
    private OperatorFileUtils operatorFileUtils;
    boolean isRun;
    public FileRandomAccessThread(Handler handler, Bundle params){
        this.handler = handler;
        this.params = params;
        operatorFileUtils = new OperatorFileUtils(handler);
    }
    public void setIsRun(boolean isRun){
        this.isRun = isRun;
        operatorFileUtils.setIsRandomAccess(isRun);
    }

    @Override
    public void run() {
        operatorFileUtils.largeFileRandomAccessFile(params.getInt("loop"),params.getInt("fileSize"));
    }
}
