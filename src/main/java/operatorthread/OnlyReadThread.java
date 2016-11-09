package operatorthread;

import android.os.Bundle;
import android.os.Handler;

import utils.OperatorFileUtils;


/**
 * Created by dave.shi on 2016/5/10.
 */
public class OnlyReadThread extends Thread {
    Handler handler;
    Bundle params;
    private OperatorFileUtils operatorFileUtils;
    public OnlyReadThread(Handler handler,Bundle params){
        this.handler = handler;
        this.params = params;
        operatorFileUtils = new OperatorFileUtils(handler);
    }

    public void setRun(boolean isRun){
        operatorFileUtils.setIsNeedRead(isRun);
    }

    @Override
    public void run() {
            operatorFileUtils.firstWrite200M(200);
            operatorFileUtils.onlyReadFromStorage(params.getInt("loop"),params.getInt("fileSize"),params.getInt("checkPeriod"));
    }
}
