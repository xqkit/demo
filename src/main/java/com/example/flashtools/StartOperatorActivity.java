package com.example.flashtools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import sharepreference.Contants;
import utils.OperatorFileUtils;
import utils.Utils;

public class StartOperatorActivity extends Activity implements View.OnClickListener {

    private Utils utils = new Utils();
    private TextView testPath;
    private TextView textItem;
    private TextView time;
    private TextView loopCount;
    private TextView logPath;
    private TextView stop;
    private static int currentItem;
    private static long flagTime;
    private operatorthread.UpdateTimeThread updateTimeThread;
    private boolean isStop = true;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time.setText("已运行时间：" + Utils.getTimeFormat((Long) msg.obj));
            mLeftSpace.setText("剩余内存大小：" + utils.getSDFreeSize() / 1024 + "G");
        }
    };
    private boolean mIsWriteOnly = false;
    private boolean mIsCpu = false;
    private TextView mLeftSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_operator);
        currentItem = getIntent().getIntExtra("currentItem", -1);
        flagTime = getIntent().getLongExtra("flagTime", -1);
        mIsWriteOnly = getIntent().getBooleanExtra("mIsWriteOnly", false);
        mIsCpu = getIntent().getBooleanExtra("mIsCpu", false);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void initView() {

        testPath = (TextView) findViewById(R.id.test_path);
        testPath.setText("测试路径：" + OperatorFileUtils.fileDir.getAbsolutePath());
        time = (TextView) findViewById(R.id.time);
        loopCount = (TextView) findViewById(R.id.loop_count);
        textItem = (TextView) findViewById(R.id.current_test_item);
        mLeftSpace = (TextView) findViewById(R.id.left_space);
        if (currentItem == Contants.READ_ONLY) {
            textItem.setText(getString(R.string.current_test, "ReadOnly"));
        } else if (currentItem == Contants.WRITE_ONLY) {
            textItem.setText(getString(R.string.current_test, "WriteOnly"));
        } else if (currentItem == Contants.READ_AND_WRITE) {
            textItem.setText(getString(R.string.current_test, "ReadWrite"));
        } else if (currentItem == Contants.LARGE_FILE_CREATE) {
            textItem.setText(getString(R.string.current_test, "LargeFileCreate"));
        } else if (currentItem == Contants.SMALL_FILE_CREATE) {
            textItem.setText(getString(R.string.current_test, "smallFileCreate"));
        } else if (currentItem == Contants.RANDOM_FILE_CREATE) {
            textItem.setText(getString(R.string.current_test, "randomFileCreate"));
        } else if (currentItem == Contants.LARGE_FILE_RANDOM_ACCESS) {
            textItem.setText(getString(R.string.current_test, "LargeFileRandomAccess"));
        }
        logPath = (TextView) findViewById(R.id.log_path);

        stop = (TextView) findViewById(R.id.stop);
        stop.setOnClickListener(this);
        updateTimeThread = new operatorthread.UpdateTimeThread(mHandler, flagTime);
        updateTimeThread.setIsUpdate(true);
        updateTimeThread.start();

        register();
    }

    LoopCountChangeBroadcastReceiver receiver = new LoopCountChangeBroadcastReceiver();

    private void register() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StartOperatorActivity.action);
        this.registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stop:
                createDialog("是否确定解锁？");
                break;
            default:
                break;
        }
    }

    AlertDialog.Builder builder;

    private void createDialog(String title) {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(title);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isStop = !isStop;
            }
        });
        builder.setCancelable(false);
        if (!builder.create().isShowing()) {
            builder.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (!isStop) {
            return;
        }
        if (mIsCpu) {
            OperatorFileUtils.cpuOver();
        }
        super.onBackPressed();
    }

    public static String action = "com.loopcount.change.receiver";

    public class LoopCountChangeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(action)) {
                Bundle bundle = intent.getExtras();
                changeCurrentLoop(bundle);
            }
        }
    }

    private void changeCurrentLoop(Bundle bundle) {
        if (currentItem == Contants.READ_ONLY || currentItem == Contants.READ_AND_WRITE) {
            if (bundle.getInt(Contants.READ_LOOP_KEY) != 0)
                loopCount.setText(getString(R.string.loop_count, bundle.getInt(Contants.READ_LOOP_KEY)));
            if (!TextUtils.isEmpty(bundle.getString(Contants.CHECK_ERROR_KEY))) {
                checkErrorDialog(bundle.getString(Contants.CHECK_ERROR_KEY));
            }
        } else if (currentItem == Contants.WRITE_ONLY) {
            if (bundle.getInt(Contants.WRITE_LOOP_KEY) != 0)
                loopCount.setText(getString(R.string.loop_count, bundle.getInt(Contants.WRITE_LOOP_KEY)));
            if (!TextUtils.isEmpty(bundle.getString(Contants.CHECK_ERROR_KEY))) {
                checkErrorDialog(bundle.getString(Contants.CHECK_ERROR_KEY));
            }
        } else if (currentItem == Contants.LARGE_FILE_CREATE || currentItem == Contants.SMALL_FILE_CREATE
                || currentItem == Contants.RANDOM_FILE_CREATE) {
            if (bundle.getInt(Contants.WRITE_LOOP_KEY) != 0)
                loopCount.setText(getString(R.string.loop_count, bundle.getInt(Contants.WRITE_LOOP_KEY)));
        } else if (currentItem == Contants.LARGE_FILE_RANDOM_ACCESS) {
            loopCount.setText(getString(R.string.loop_count, bundle.getInt(Contants.RANDOW_ACCESS_LOOP_KEY)));
        }
        if (!TextUtils.isEmpty(bundle.getString(Contants.LOG_PATH_KEY)))
            logPath.setText(getString(R.string.log_path, bundle.getString(Contants.LOG_PATH_KEY)));
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(receiver);
        receiver = null;
        /*停止更新时间*/
        updateTimeThread.setIsUpdate(false);
        super.onDestroy();
    }

    Dialog mDialog = null;
    View contentView;

    private void checkErrorDialog(String message) {
        if (mDialog == null) {
            mDialog = new Dialog(this);
            contentView = LayoutInflater.from(this).inflate(R.layout.check_error_view, null);
            mDialog.setContentView(contentView);
        }
        ((TextView) contentView.findViewById(R.id.message)).setText(message);
        ((TextView) contentView.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                    mDialog = null;
                }
            }
        });
        mDialog.setCancelable(false);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }
}
