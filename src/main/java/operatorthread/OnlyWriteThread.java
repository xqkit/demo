package operatorthread;

import android.os.Bundle;
import android.os.Handler;

import utils.OperatorFileUtils;

/**
 * Created by dave.shi on 2016/5/10.
 */
public class OnlyWriteThread extends Thread {
    Handler handler;
    private Bundle params;

    public OnlyWriteThread(Handler handler, Bundle params) {
        this.handler = handler;
        this.params = params;
        operatorFileUtils = new OperatorFileUtils(handler);
    }

    private boolean isRun;
    private OperatorFileUtils operatorFileUtils;

    public void setRun(boolean isRun) {
        this.isRun = isRun;
        operatorFileUtils.setIsNeedWrite(this.isRun);
        operatorFileUtils.setIsNeedRunCpu(this.isRun);
    }

    @Override
    public void run() {
        operatorFileUtils.onlyWriteToStorage
                (params.getInt("loop"), params.getInt("fileSize"),
                        params.getInt("packageSize"), params.getInt("speed"),
                        params.getInt("checkPeriod"), params.getInt("leftSpace"));
    }

}
