package com.example.flashtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sharepreference.Contants;
import sharepreference.DialogHelp;
import sharepreference.SharePreferenceHelper;

public class CreateFileActivity extends Activity implements View.OnClickListener {
    private RelativeLayout fileCreateContainer, loopContainer, testSizeContainer;
    private TextView fileCreateTitle;
    private CheckBox checkBox;
    private TextView loopText, loop;
    private TextView fileSizeText, fileSize;

    private DialogHelp help;
    private int currentItem;
//    private TextView mCpuFreqTv;
//    private TextView mCpuFreq;
//    private RelativeLayout mCpuFrequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_file);
        currentItem = getIntent().getIntExtra("flag", -1);
        initView();

    }

    private void initView() {
        help = new DialogHelp(this);
        fileCreateContainer = (RelativeLayout) findViewById(R.id.create_file_container);
        fileCreateContainer.setOnClickListener(this);
        //add by frank.x
//        mCpuFrequency = (RelativeLayout) findViewById(R.id.cpu_container);
//        mCpuFrequency.setOnClickListener(this);

        fileCreateTitle = (TextView) fileCreateContainer.findViewById(R.id.create_file_text);
        if (currentItem == Contants.LARGE_FILE_CREATE) {
            fileCreateTitle.setText(getString(R.string.create_large_file));
        } else if (currentItem == Contants.SMALL_FILE_CREATE) {
            fileCreateTitle.setText(getString(R.string.create_small_file));
        } else if (currentItem == Contants.RANDOM_FILE_CREATE) {
            fileCreateTitle.setText(getString(R.string.create_random_file));
        } else if (currentItem == Contants.LARGE_FILE_RANDOM_ACCESS) {
            fileCreateTitle.setText(getString(R.string.random_file_access));
        }
        loopContainer = (RelativeLayout) findViewById(R.id.loop_container);
        loopContainer.setOnClickListener(this);
        testSizeContainer = (RelativeLayout) findViewById(R.id.file_size_container);
        testSizeContainer.setOnClickListener(this);
        checkBox = (CheckBox) fileCreateContainer.findViewById(R.id.checkbox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChildIsEnable(checkBox.isChecked());
            }
        });
        loopText = (TextView) loopContainer.findViewById(R.id.loop_count_text);
        loop = (TextView) loopContainer.findViewById(R.id.loop_count);
        int loopSize = 0;
        if (currentItem == Contants.LARGE_FILE_CREATE) {
            loopSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.CARETE_LARGE_FILE_LOOP);
        } else if (currentItem == Contants.SMALL_FILE_CREATE) {
            loopSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.CREATE_SMALL_FILE_LOOP);
        } else if (currentItem == Contants.RANDOM_FILE_CREATE) {
            loopSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.CREATE_RANDOM_FILE_LOOP);
        } else if (currentItem == Contants.LARGE_FILE_RANDOM_ACCESS) {
            loopSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.FILE_RANDOM_ACCESS_LOOP);
        }
        if (loopSize != -1) {
            loop.setText(String.valueOf(loopSize));
        }
        fileSizeText = (TextView) testSizeContainer.findViewById(R.id.file_size_text);
        fileSize = (TextView) testSizeContainer.findViewById(R.id.file_size);
        int fileCreateSize = 0;
        if (currentItem == Contants.LARGE_FILE_CREATE) {
            fileCreateSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.CREATE_LARGE_FILE_SIZE);
        } else if (currentItem == Contants.SMALL_FILE_CREATE) {
            fileCreateSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.CREATE_SMALL_FILE_SIZE);
        } else if (currentItem == Contants.RANDOM_FILE_CREATE) {
            fileCreateSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.CREATE_RANDOM_FILE_SIZE);
        } else if (currentItem == Contants.LARGE_FILE_RANDOM_ACCESS) {
            fileCreateSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.FILE_RANDOM_ACCESS_SIZE);
        }
        if (fileCreateSize != -1) {
            fileSize.setText(String.valueOf(fileCreateSize));
        }
        //add by frank.x
//        mCpuFreqTv = (TextView) findViewById(R.id.cpu_freq_text);
//        mCpuFreq = (TextView) findViewById(R.id.cpu_freq);
//        int cpuFreq = 85;
//        if (currentItem == Contants.LARGE_FILE_CREATE) {
//            cpuFreq = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.CREATE_LARGE_FILE_SIZE);
//        } else if (currentItem == Contants.SMALL_FILE_CREATE) {
//            cpuFreq = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.CREATE_SMALL_FILE_SIZE);
//        } else if (currentItem == Contants.RANDOM_FILE_CREATE) {
//            cpuFreq = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.CREATE_RANDOM_FILE_SIZE);
//        } else if (currentItem == Contants.LARGE_FILE_RANDOM_ACCESS) {
//            cpuFreq = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.FILE_RANDOM_ACCESS_SIZE);
//        }
//        if (fileCreateSize != -1) {
//            mCpuFreq.setText(String.valueOf(cpuFreq));
//        }

        setChildIsEnable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_file_container:
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                setChildIsEnable(checkBox.isChecked());
                break;
            case R.id.loop_container:
                String loopkey = null;
                if (currentItem == Contants.LARGE_FILE_CREATE) {
                    loopkey = SharePreferenceHelper.Config.CARETE_LARGE_FILE_LOOP;
                } else if (currentItem == Contants.SMALL_FILE_CREATE) {
                    loopkey = SharePreferenceHelper.Config.CREATE_SMALL_FILE_LOOP;
                } else if (currentItem == Contants.RANDOM_FILE_CREATE) {
                    loopkey = SharePreferenceHelper.Config.CREATE_RANDOM_FILE_LOOP;
                } else if (currentItem == Contants.LARGE_FILE_RANDOM_ACCESS) {
                    loopkey = SharePreferenceHelper.Config.FILE_RANDOM_ACCESS_LOOP;
                }
                help.createNumDialog(loopText, loop, loopkey);
                break;
            case R.id.file_size_container:
                String filekey = null;
                if (currentItem == Contants.LARGE_FILE_CREATE) {
                    filekey = SharePreferenceHelper.Config.CREATE_LARGE_FILE_SIZE;
                } else if (currentItem == Contants.SMALL_FILE_CREATE) {
                    filekey = SharePreferenceHelper.Config.CREATE_SMALL_FILE_SIZE;
                } else if (currentItem == Contants.RANDOM_FILE_CREATE) {
                    filekey = SharePreferenceHelper.Config.CREATE_RANDOM_FILE_SIZE;
                } else if (currentItem == Contants.LARGE_FILE_RANDOM_ACCESS) {
                    filekey = SharePreferenceHelper.Config.FILE_RANDOM_ACCESS_SIZE;
                }
                help.createNumDialog(fileSizeText, fileSize, filekey);
                break;
//            case R.id.cpu_container:
//
//                break;
            default:
                break;
        }
    }

    private void setChildIsEnable(boolean isChecked) {
        if (!isChecked) {
            loopContainer.setEnabled(false);
            loopText.setTextColor(getResources().getColor(R.color.text_color_gray));
            loop.setTextColor(getResources().getColor(R.color.text_color_gray));
            testSizeContainer.setEnabled(false);
            fileSizeText.setTextColor(getResources().getColor(R.color.text_color_gray));
            fileSize.setTextColor(getResources().getColor(R.color.text_color_gray));
            //add by frank.x
//            mCpuFrequency.setEnabled(false);
//            mCpuFreq.setTextColor(getResources().getColor(R.color.text_color_gray));
//            mCpuFreqTv.setTextColor(getResources().getColor(R.color.text_color_gray));
        } else {
            loopContainer.setEnabled(true);
            loopContainer.setBackgroundColor(getResources().getColor(R.color.divide_line_bg));
            loopText.setTextColor(getResources().getColor(R.color.black));
            loop.setTextColor(getResources().getColor(R.color.black));

            testSizeContainer.setEnabled(true);
            testSizeContainer.setBackgroundColor(getResources().getColor(R.color.divide_line_bg));
            fileSizeText.setTextColor(getResources().getColor(R.color.black));
            fileSize.setTextColor(getResources().getColor(R.color.black));
            //add by frank.x
//            mCpuFrequency.setEnabled(true);
//            mCpuFrequency.setBackgroundColor(getResources().getColor(R.color.divide_line_bg));
//            mCpuFreq.setTextColor(getResources().getColor(R.color.black));
//            mCpuFreqTv.setTextColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("currentItem", currentItem);
        bundle.putBoolean("isChecked", checkBox.isChecked());
        bundle.putInt("loop", Integer.parseInt(loop.getText().toString()));
        if (currentItem == Contants.RANDOM_FILE_CREATE) {
            bundle.putInt("fileSize", -1);
            bundle.putInt("random", Integer.parseInt(fileSize.getText().toString()));
        } else {
            bundle.putInt("fileSize", Integer.parseInt(fileSize.getText().toString()));
        }

        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }
}
