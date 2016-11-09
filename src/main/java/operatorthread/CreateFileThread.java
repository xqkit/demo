package operatorthread;

import android.os.Bundle;
import android.os.Handler;

import utils.OperatorFileUtils;


/**
 * Created by dave.shi on 2016/5/12.
 */
public class CreateFileThread extends Thread {
    Handler handler;
    Bundle params;
    private OperatorFileUtils operatorFileUtils;
    public CreateFileThread(Handler handler,Bundle params){
        this.handler = handler;
        this.params = params;
        operatorFileUtils = new OperatorFileUtils(handler);
    }
    public void setRun(boolean isRun){
        operatorFileUtils.setIsNeedRead(isRun);
        operatorFileUtils.setIsCreateFile(isRun);
    }

    @Override
    public void run() {
        super.run();
        operatorFileUtils.createFile(params.getInt("loop"),params.getInt("fileSize"),params.getInt("random"));
    }
}
