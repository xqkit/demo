package com.example.flashtools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sharepreference.DialogHelp;
import sharepreference.SharePreferenceHelper;

public class OnlyWriteActivity extends Activity implements View.OnClickListener {
    private RelativeLayout onlyWriteContainer;
    public static final String TAG = "OnlyWriteActivity";
    private CheckBox checkBox;

    private RelativeLayout loopContainer;
    private TextView loopCountTextView;
    private TextView loopCount;

    private RelativeLayout fileSizeContainer;
    private TextView fileSizeTextView;
    private TextView fileSize;

    private RelativeLayout packageSizeContainer;
    private TextView packageTextView;
    private TextView packageSize;

    private RelativeLayout randomRateContainer;
    private CheckBox randomRateCheckbox;
    private TextView randomRateTextView;

    private RelativeLayout writeRateContainer;
    private TextView writeRateText;
    private TextView writeRate;

    private RelativeLayout checkPeriodContainer;
    private TextView checkPeriodTitle;
    private TextView checkPeriodCount;

    private RelativeLayout leftSpaceContainer;
    private TextView leftSpaceTitle;
    private TextView leftSpaceCount;

    DialogHelp dialogHelp;
    private RelativeLayout mCpuFreqContainer;
    private CheckBox mCpuCheckBox;
    private RelativeLayout mSetCpuContainer;
    private TextView mCpuFreqTv;
    private TextView mCpuFreq;
    private TextView mCpuFreqTv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_write);
        initView();
    }

    void initView() {
        dialogHelp = new DialogHelp(this);
        onlyWriteContainer = (RelativeLayout) findViewById(R.id.only_write_container);
        onlyWriteContainer.setOnClickListener(this);
        checkBox = (CheckBox) onlyWriteContainer.findViewById(R.id.checkbox);
        checkBox.setChecked(SharePreferenceHelper.getInstance(this).getWriteChecked());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChildBackgroud(checkBox.isChecked());
            }
        });
        //add by frank.x  ------------------------------------------------------------
        mCpuFreqContainer = (RelativeLayout) findViewById(R.id.cpu_freq_container);
        mCpuFreqContainer.setOnClickListener(this);
        mCpuFreqTv1 = (TextView) findViewById(R.id.cpu_freq_tv);
        mCpuCheckBox = (CheckBox) findViewById(R.id.cpu_checkbox);
        mCpuCheckBox.setOnClickListener(this);
        mSetCpuContainer = (RelativeLayout) findViewById(R.id.set_cpu_container);
        mSetCpuContainer.setOnClickListener(this);
        mCpuFreqTv = (TextView) findViewById(R.id.cpu_freq_value_tv);
        mCpuFreq = (TextView) findViewById(R.id.cpu_freq);
        int cpuFreq = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.ONLY_WRITE_CPU_FREQ);
        if (cpuFreq >= 80) { //Cpu最低限80
            mCpuFreq.setText(String.valueOf(cpuFreq));
        }
        //-------------------------------------------------------------------------

        loopContainer = (RelativeLayout) findViewById(R.id.loop_container);
        loopContainer.setOnClickListener(this);
        loopCountTextView = (TextView) loopContainer.findViewById(R.id.loop_count_text);
        loopCount = (TextView) loopContainer.findViewById(R.id.loop_count);
        int writeLoopCount = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.ONLY_WRITE_LOOP);
        if (writeLoopCount != -1) {
            loopCount.setText(String.valueOf(writeLoopCount));
        }
        fileSizeContainer = (RelativeLayout) findViewById(R.id.file_size_container);
        fileSizeContainer.setOnClickListener(this);
        fileSizeTextView = (TextView) fileSizeContainer.findViewById(R.id.file_size_text);
        fileSize = (TextView) fileSizeContainer.findViewById(R.id.file_size);
        int writeFileSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.ONLY_WRITE_FILESIZE);
        if (writeFileSize != -1) {
            fileSize.setText(String.valueOf(writeFileSize));
        }
        packageSizeContainer = (RelativeLayout) findViewById(R.id.package_size_container);
        packageSizeContainer.setOnClickListener(this);
        packageTextView = (TextView) packageSizeContainer.findViewById(R.id.package_size_text);
        packageSize = (TextView) packageSizeContainer.findViewById(R.id.package_size);
        int writePackageSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.ONLY_WRITE_PACKAGESIZE);
        if (writePackageSize != -1) {
            packageSize.setText(String.valueOf(writePackageSize));
        }

        randomRateContainer = (RelativeLayout) findViewById(R.id.random_rate_container);
        randomRateContainer.setOnClickListener(this);
        randomRateCheckbox = (CheckBox) randomRateContainer.findViewById(R.id.random_rate_checkbox);
        randomRateTextView = (TextView) randomRateContainer.findViewById(R.id.random_rate_text);

        writeRateContainer = (RelativeLayout) findViewById(R.id.write_rate_container);
        writeRateContainer.setOnClickListener(this);
        writeRateText = (TextView) writeRateContainer.findViewById(R.id.write_rate_text);
        writeRate = (TextView) writeRateContainer.findViewById(R.id.write_rate);
        int writeRateSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.ONLY_WRITE_RATE);
        if (writeRateSize != -1) {
            writeRate.setText(String.valueOf(writeRateSize));
        }

        checkPeriodContainer = (RelativeLayout) findViewById(R.id.check_period_container);
        checkPeriodContainer.setOnClickListener(this);
        checkPeriodTitle = (TextView) checkPeriodContainer.findViewById(R.id.check_period_text);
        checkPeriodCount = (TextView) checkPeriodContainer.findViewById(R.id.check_period);
        int checkPeriod = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.ONLY_WRITE_CHECK_PERIOD);
        if (checkPeriod != -1) {
            checkPeriodCount.setText(String.valueOf(checkPeriod));
        }

        leftSpaceContainer = (RelativeLayout) findViewById(R.id.left_space_container);
        leftSpaceContainer.setOnClickListener(this);
        leftSpaceCount = (TextView) leftSpaceContainer.findViewById(R.id.left_space_size);
        leftSpaceTitle = (TextView) leftSpaceContainer.findViewById(R.id.left_space_text);
        int leftSpace = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.ONLY_WRITE_LEFT_SPACE);
        if (leftSpace != -1) {
            leftSpaceCount.setText(String.valueOf(leftSpace));
        }
        setChildBackgroud(checkBox.isChecked());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.only_write_container:
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                setChildBackgroud(checkBox.isChecked());
                break;
            case R.id.loop_container:
                dialogHelp.createNumDialog(loopCountTextView, loopCount, SharePreferenceHelper.Config.ONLY_WRITE_LOOP);
                break;
            case R.id.file_size_container:
                dialogHelp.createNumDialog(fileSizeTextView, fileSize, SharePreferenceHelper.Config.ONLY_WRITE_FILESIZE);
                break;
            case R.id.package_size_container:
                dialogHelp.createNumDialog(packageTextView, packageSize, SharePreferenceHelper.Config.ONLY_WRITE_PACKAGESIZE);
                break;
            case R.id.random_rate_container:
                if (randomRateCheckbox.isChecked()) {
                    randomRateCheckbox.setChecked(false);
                    writeRateContainer.setEnabled(true);
                    writeRateText.setTextColor(getResources().getColor(R.color.black));
                    writeRate.setTextColor(getResources().getColor(R.color.black));
                } else {
                    randomRateCheckbox.setChecked(true);
                    writeRateContainer.setEnabled(false);
                    writeRateText.setTextColor(getResources().getColor(R.color.text_color_gray));
                    writeRate.setTextColor(getResources().getColor(R.color.text_color_gray));
                }
                break;
            case R.id.write_rate_container:
                dialogHelp.createNumDialog(writeRateText, writeRate, SharePreferenceHelper.Config.ONLY_WRITE_RATE);
                break;
            case R.id.check_period_container:
                dialogHelp.createNumDialog(checkPeriodTitle, checkPeriodCount, SharePreferenceHelper.Config.ONLY_WRITE_CHECK_PERIOD);
                break;
            case R.id.left_space_container:
                dialogHelp.createNumDialog(leftSpaceTitle, leftSpaceCount, SharePreferenceHelper.Config.ONLY_WRITE_LEFT_SPACE);
                break;
            case R.id.cpu_freq_container:
                if (mCpuCheckBox.isChecked()) {
                    mCpuCheckBox.setChecked(false);
                    mSetCpuContainer.setEnabled(true);
                    mCpuFreqTv.setTextColor(getResources().getColor(R.color.black));
                    mCpuFreq.setTextColor(getResources().getColor(R.color.black));
                } else {
                    mCpuCheckBox.setChecked(true);
                    mSetCpuContainer.setEnabled(false);
                    mCpuFreqTv.setTextColor(getResources().getColor(R.color.text_color_gray));
                    mCpuFreq.setTextColor(getResources().getColor(R.color.text_color_gray));
                }
                break;
            case R.id.set_cpu_container:
                dialogHelp.createNumDialog(mCpuFreqTv, mCpuFreq, SharePreferenceHelper.Config.ONLY_WRITE_CPU_FREQ);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharePreferenceHelper.getInstance(this).saveWriteStatus(checkBox.isChecked());

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("currentItem", 2);
        bundle.putBoolean("isChecked", checkBox.isChecked());

        bundle.putInt("loop", Integer.parseInt(loopCount.getText().toString()));
        bundle.putInt("fileSize", Integer.parseInt(fileSize.getText().toString()));
        bundle.putInt("packageSize", Integer.parseInt(packageSize.getText().toString()));
        bundle.putInt("checkPeriod", Integer.parseInt(checkPeriodCount.getText().toString()));
        bundle.putInt("leftSpace", Integer.parseInt(leftSpaceCount.getText().toString()));
        bundle.putBoolean("isRandomSpeed", randomRateCheckbox.isChecked());
        bundle.putBoolean("isCpu", mCpuCheckBox.isChecked());
        if (randomRateCheckbox.isChecked()) {
            bundle.putInt("speed", 0);
        } else {
            bundle.putInt("speed", Integer.parseInt(writeRate.getText().toString()));
        }
        if (mCpuCheckBox.isChecked()) {
            bundle.putInt("cpu", 80);
        } else {
            bundle.putInt("cpu", Integer.parseInt(mCpuFreq.getText().toString()));
        }
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    //更改背景颜色
    private void setChildBackgroud(boolean isChecked) {
        if (isChecked) {
            loopContainer.setEnabled(true);
            loopCountTextView.setTextColor(getResources().getColor(R.color.black));
            loopCount.setTextColor(getResources().getColor(R.color.black));

            fileSizeContainer.setEnabled(true);
            fileSize.setTextColor(getResources().getColor(R.color.black));
            fileSizeTextView.setTextColor(getResources().getColor(R.color.black));

            packageSizeContainer.setEnabled(true);
            packageTextView.setTextColor(getResources().getColor(R.color.black));
            packageSize.setTextColor(getResources().getColor(R.color.black));

            randomRateCheckbox.setEnabled(true);
            randomRateContainer.setEnabled(true);
            randomRateTextView.setTextColor(getResources().getColor(R.color.black));

            writeRateContainer.setEnabled(true);
            writeRateText.setTextColor(getResources().getColor(R.color.black));
            writeRate.setTextColor(getResources().getColor(R.color.black));

            checkPeriodContainer.setEnabled(true);
            checkPeriodCount.setTextColor(getResources().getColor(R.color.black));
            checkPeriodTitle.setTextColor(getResources().getColor(R.color.black));

            leftSpaceContainer.setEnabled(true);
            leftSpaceCount.setTextColor(getResources().getColor(R.color.black));
            leftSpaceTitle.setTextColor(getResources().getColor(R.color.black));

            //add by frank.x
            mCpuFreqContainer.setEnabled(true);
            mCpuFreqTv.setTextColor(getResources().getColor(R.color.black));
            mCpuFreq.setTextColor(getResources().getColor(R.color.black));
            mCpuFreqTv1.setTextColor(getResources().getColor(R.color.black));
            mSetCpuContainer.setEnabled(true);
        } else {
            loopContainer.setEnabled(false);
            loopCountTextView.setTextColor(getResources().getColor(R.color.gray));
            loopCount.setTextColor(getResources().getColor(R.color.gray));

            fileSizeContainer.setEnabled(false);
            fileSize.setTextColor(getResources().getColor(R.color.gray));
            fileSizeTextView.setTextColor(getResources().getColor(R.color.gray));

            packageSizeContainer.setEnabled(false);
            packageTextView.setTextColor(getResources().getColor(R.color.gray));
            packageSize.setTextColor(getResources().getColor(R.color.gray));

            randomRateCheckbox.setEnabled(false);
            randomRateContainer.setEnabled(false);
            randomRateTextView.setTextColor(getResources().getColor(R.color.gray));

            writeRateContainer.setEnabled(false);
            writeRateText.setTextColor(getResources().getColor(R.color.gray));
            writeRate.setTextColor(getResources().getColor(R.color.gray));

            checkPeriodContainer.setEnabled(false);
            checkPeriodCount.setTextColor(getResources().getColor(R.color.gray));
            checkPeriodTitle.setTextColor(getResources().getColor(R.color.gray));

            leftSpaceContainer.setEnabled(false);
            leftSpaceCount.setTextColor(getResources().getColor(R.color.gray));
            leftSpaceTitle.setTextColor(getResources().getColor(R.color.gray));

            //add by frank.x
            mCpuFreqContainer.setEnabled(false);
            mCpuFreqTv.setTextColor(getResources().getColor(R.color.gray));
            mCpuFreq.setTextColor(getResources().getColor(R.color.gray));
            mCpuFreqTv1.setTextColor(getResources().getColor(R.color.gray));
            mSetCpuContainer.setEnabled(false);

        }
    }
}
