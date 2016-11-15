package com.example.flashtools;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import operatorthread.CreateFileThread;
import operatorthread.FileRandomAccessThread;
import operatorthread.OnlyReadThread;
import operatorthread.OnlyWriteThread;
import sharepreference.Contants;
import sharepreference.SharePreferenceHelper;
import utils.OperatorFileUtils;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView onlyRead, onlyWrite, readAndWrite, largeFileCreate, smallFileCreate, randomFileCreate, largeFileRandomAccess;
    private OnlyReadThread readThread;
    private OnlyWriteThread writeThread;
    private CreateFileThread createFileThread;
    private FileRandomAccessThread randomAccessThread;
    private TextView start;
    public boolean mIsWriteOnly = false;
    public boolean mIsCpu = false;
    public static final String TAG = "MainActivity";

    private static final int READ_ONLY = 1;
    private static final int WRITE_ONLY = 2;
    private static final int READ_AND_WRITE = 3;
    private static final int LARGE_FILE_CREATE = 4;
    private static final int SMALL_FILE_CREATE = 5;
    private static final int RANDOM_FILE_CREATE = 6;
    private static final int LARGE_FILE_RANDOM_ACCESS = 7;

    /**
     * 用于执行线程的参数
     */
    private Bundle params = new Bundle();
    private int currentItem = 0;
    /**
     * 用于处理读写内容出错的情况
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null) {
                Bundle bundle = (Bundle) msg.obj;
                Intent intent = new Intent();
                intent.setAction(StartOperatorActivity.action);
                intent.putExtras(bundle);
                MainActivity.this.sendBroadcast(intent);
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bootCompleted(getParams());
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (checkSelfPermission(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 2);
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        }
        if (checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, 4);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    void initView() {
        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        readThread = new OnlyReadThread(handler, params);
        writeThread = new OnlyWriteThread(handler, params);
        createFileThread = new CreateFileThread(handler, params);
        randomAccessThread = new FileRandomAccessThread(handler, params);
        onlyRead = (TextView) findViewById(R.id.only_read);
        onlyRead.setOnClickListener(this);
        onlyWrite = (TextView) findViewById(R.id.only_write);
        onlyWrite.setOnClickListener(this);

        start = (TextView) findViewById(R.id.start);
        start.setOnClickListener(this);
        readAndWrite = (TextView) findViewById(R.id.read_and_write);
        readAndWrite.setOnClickListener(this);
        largeFileCreate = (TextView) findViewById(R.id.large_file_create);
        largeFileCreate.setOnClickListener(this);
        smallFileCreate = (TextView) findViewById(R.id.small_file_create);
        smallFileCreate.setOnClickListener(this);
        randomFileCreate = (TextView) findViewById(R.id.random_file_create);
        randomFileCreate.setOnClickListener(this);
        largeFileRandomAccess = (TextView) findViewById(R.id.large_file_random_access);
        largeFileRandomAccess.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.only_read:
                intent = new Intent(this, OnlyReadActivity.class);
                intent.putExtra("flag", READ_ONLY);
                startActivityForResult(intent, READ_ONLY);
                break;
            case R.id.only_write:
                intent = new Intent(this, OnlyWriteActivity.class);
                intent.putExtra("flag", WRITE_ONLY);
                startActivityForResult(intent, WRITE_ONLY);
                break;
            case R.id.start:
                startRun();
                break;
            case R.id.read_and_write:
                intent = new Intent(this, ReadAndWriteActivity.class);
                startActivityForResult(intent, READ_AND_WRITE);
                break;
            case R.id.large_file_create:
                intent = new Intent(this, CreateFileActivity.class);
                intent.putExtra("flag", LARGE_FILE_CREATE);
                startActivityForResult(intent, LARGE_FILE_CREATE);
                break;
            case R.id.small_file_create:
                intent = new Intent(this, CreateFileActivity.class);
                intent.putExtra("flag", SMALL_FILE_CREATE);
                startActivityForResult(intent, SMALL_FILE_CREATE);
                break;
            case R.id.random_file_create:
                intent = new Intent(this, CreateFileActivity.class);
                intent.putExtra("flag", RANDOM_FILE_CREATE);
                startActivityForResult(intent, RANDOM_FILE_CREATE);
                break;
            case R.id.large_file_random_access:
                intent = new Intent(this, CreateFileActivity.class);
                intent.putExtra("flag", LARGE_FILE_RANDOM_ACCESS);
                startActivityForResult(intent, LARGE_FILE_RANDOM_ACCESS);
                break;
            default:
                break;
        }
    }

    /**
     * 开始运行
     */
    private void startRun() {
        if (currentItem == READ_ONLY) {
            startReadOnly();
        } else if (currentItem == WRITE_ONLY) {
            startWriteOnly();
        } else if (currentItem == READ_AND_WRITE) {
            startReadAndWrite();
        } else if (currentItem == LARGE_FILE_CREATE
                || currentItem == SMALL_FILE_CREATE
                || currentItem == RANDOM_FILE_CREATE) {
            startCreateFile();
        } else if (currentItem == LARGE_FILE_RANDOM_ACCESS) {
            startRandomAccessFile();
        }
        if (currentItem != 0) {
            Intent intent = new Intent(this, StartOperatorActivity.class);
            intent.putExtra("currentItem", currentItem);
            intent.putExtra("flagTime", System.currentTimeMillis());
            intent.putExtra("mIsWriteOnly", mIsWriteOnly);
            intent.putExtra("mIsCpu", mIsCpu);
            startActivity(intent);
        }
    }

    /**
     * 开机自启动
     */
    private void bootCompleted(Bundle bundle) {
        if (bundle != null && bundle.getInt("loop", -1) != -1) {
            this.params = bundle;/**保存退出前的参数*/
        } else {
            return;
        }
        currentItem = SharePreferenceHelper.getInstance(this).getChangeData("currentItem");
        if (!getIntent().getBooleanExtra("isBootCompleted", false)) {
            return;
        }
        startRun();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            int currentItem = bundle.getInt("currentItem");
            boolean isChecked = bundle.getBoolean("isChecked");
            boolean isCpu = bundle.getBoolean("isCpu");
            this.mIsCpu = isCpu;
            if (isChecked) {
                this.currentItem = currentItem;
                SharePreferenceHelper.getInstance(this).saveChangeData("currentItem", currentItem);
            }
            changeBackgound(currentItem);

            params.putInt("loop", bundle.getInt("loop"));
            params.putInt("fileSize", bundle.getInt("fileSize"));
            params.putInt("packageSize", bundle.getInt("packageSize"));
            params.putInt("speed", bundle.getInt("speed"));
            params.putInt("random", bundle.getInt("random"));
            params.putInt("checkPeriod", bundle.getInt("checkPeriod"));
            params.putInt("leftSpace", bundle.getInt("leftSpace"));
            params.putInt("cpu", bundle.getInt("cpu"));

            //数据持久化保存
            SharePreferenceHelper.getInstance(this).saveChangeData("loop", bundle.getInt("loop"));
            SharePreferenceHelper.getInstance(this).saveChangeData("fileSize", bundle.getInt("fileSize"));
            SharePreferenceHelper.getInstance(this).saveChangeData("packageSize", bundle.getInt("packageSize"));
            SharePreferenceHelper.getInstance(this).saveChangeData("speed", bundle.getInt("speed"));
            SharePreferenceHelper.getInstance(this).saveChangeData("random", bundle.getInt("random"));
            SharePreferenceHelper.getInstance(this).saveChangeData("cpu", bundle.getInt("cpu"));
            SharePreferenceHelper.getInstance(this).saveChangeData("leftSpace", bundle.getInt("leftSpace"));
        }
    }

    private Bundle getParams() {
        Bundle bundle = new Bundle();
        bundle.putInt("loop", SharePreferenceHelper.getInstance(this).getChangeData("loop"));
        bundle.putInt("fileSize", SharePreferenceHelper.getInstance(this).getChangeData("fileSize"));
        bundle.putInt("packageSize", SharePreferenceHelper.getInstance(this).getChangeData("packageSize"));
        bundle.putInt("speed", SharePreferenceHelper.getInstance(this).getChangeData("speed"));
        bundle.putInt("random", SharePreferenceHelper.getInstance(this).getChangeData("random"));
        bundle.putInt("leftSpace", SharePreferenceHelper.getInstance(this).getChangeData("leftSpace"));
        return bundle;
    }

    //只读
    private void startReadOnly() {
        stopisRunningThread();
        SystemClock.sleep(500);
        readThread = new OnlyReadThread(handler, params);
        readThread.setRun(true);
        readThread.start();
    }

    //只写
    private void startWriteOnly() {
        stopisRunningThread();
        SystemClock.sleep(500);
        writeThread.setRun(true);
        mIsWriteOnly = true;
        writeThread = new OnlyWriteThread(handler, params);
        writeThread.start();
        //add by frank.x
        int cpuMax = params.getInt("cpu");
        Log.d(TAG, "cpuMax = " + cpuMax + " mIsCpu = " + mIsCpu);
        if (mIsCpu) {
            OperatorFileUtils.runCpuMax(cpuMax);
        }
    }

    //读写,先停止read和write的单独线程，再重新运行两个线程
    private void startReadAndWrite() {
        stopisRunningThread();
        SystemClock.sleep(500);
        readThread.setRun(true);
        writeThread.setRun(true);
        readThread = new OnlyReadThread(handler, params);
        readThread.start();
        writeThread = new OnlyWriteThread(handler, params);
        writeThread.start();
    }

    //大文件创建和小文件创建
    private void startCreateFile() {
        stopisRunningThread();
        SystemClock.sleep(500);
        createFileThread.setRun(true);
        createFileThread = new CreateFileThread(handler, params);
        createFileThread.start();
    }

    //随机访问文件
    private void startRandomAccessFile() {
        stopisRunningThread();
        SystemClock.sleep(500);
        randomAccessThread.setIsRun(true);
        randomAccessThread = new FileRandomAccessThread(handler, params);
        randomAccessThread.start();
    }

    private void stopisRunningThread() {
        readThread.setRun(false);
        writeThread.setRun(false);
        createFileThread.setRun(false);
        randomAccessThread.setIsRun(false);
//        if (mIsWriteOnly) {
//            OperatorFileUtils.cpuOver();
//            mIsWriteOnly = false;
//        }
    }

    /**
     * @param currentItem 如果当前项勾选了才会更改背景颜色
     */
    private void changeBackgound(int currentItem) {
        recoveryItemBackground();
        if (currentItem == Contants.READ_ONLY) {
            recoveryItemBackground();
            onlyRead.setBackgroundResource(R.color.color_green);
        } else if (currentItem == Contants.WRITE_ONLY) {
            recoveryItemBackground();
            onlyWrite.setBackgroundResource(R.color.color_green);
        } else if (currentItem == Contants.READ_AND_WRITE) {
            recoveryItemBackground();
            readAndWrite.setBackgroundResource(R.color.color_green);
        } else if (currentItem == Contants.LARGE_FILE_CREATE) {
            recoveryItemBackground();
            largeFileCreate.setBackgroundResource(R.color.color_green);
        } else if (currentItem == Contants.SMALL_FILE_CREATE) {
            recoveryItemBackground();
            smallFileCreate.setBackgroundResource(R.color.color_green);
        } else if (currentItem == Contants.RANDOM_FILE_CREATE) {
            recoveryItemBackground();
            randomFileCreate.setBackgroundResource(R.color.color_green);
        } else if (currentItem == Contants.LARGE_FILE_RANDOM_ACCESS) {
            recoveryItemBackground();
            largeFileRandomAccess.setBackgroundResource(R.color.color_green);
        }
    }

    /**
     * 恢复mainContainer的背景颜色
     */
    private void recoveryItemBackground() {
        onlyRead.setBackgroundResource(R.color.io_control_bg);
        onlyWrite.setBackgroundResource(R.color.io_control_bg);
        readAndWrite.setBackgroundResource(R.color.io_control_bg);
        largeFileCreate.setBackgroundResource(R.color.io_control_bg);
        smallFileCreate.setBackgroundResource(R.color.io_control_bg);
        randomFileCreate.setBackgroundResource(R.color.io_control_bg);
        largeFileRandomAccess.setBackgroundResource(R.color.io_control_bg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
