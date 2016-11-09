package operatorthread;

import android.os.Handler;
import android.os.Message;


/**
 * Created by dave.shi on 2016/6/28.
 */
public class UpdateTimeThread extends Thread {
    private Handler mHandler;
    long flagTime;
    private boolean isUpdate;
    public UpdateTimeThread(Handler mHandler,long flagTime){
        this.mHandler = mHandler;
        this.flagTime = flagTime;
    }
    public void setIsUpdate(boolean isUpdate){
        this.isUpdate = isUpdate;
    }
    @Override
    public void run() {
        super.run();
        while(isUpdate){
            Message message = new Message();
            message.obj = System.currentTimeMillis() - flagTime;
            mHandler.sendMessage(message);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
