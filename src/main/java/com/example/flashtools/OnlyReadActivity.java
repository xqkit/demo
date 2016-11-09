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

public class OnlyReadActivity extends Activity implements View.OnClickListener {
    private RelativeLayout onlyReadContainer;
    private CheckBox checkBox;

    private RelativeLayout loopContainer, fileSizeContainer, checkPeriodContainer;
    private TextView loopTitle, loopCount;
    private TextView fileSizeTitle, fileSize;
    private TextView checkPeriodTitle, checkPeriodCount;

    private DialogHelp dialogHelp = new DialogHelp(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_read);
        initView();
    }

    void initView() {
        onlyReadContainer = (RelativeLayout) findViewById(R.id.only_read_container);
        onlyReadContainer.setOnClickListener(this);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setChecked(SharePreferenceHelper.getInstance(this).getReadChecked());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChildBackgroud(checkBox.isChecked());
            }
        });
        loopContainer = (RelativeLayout) findViewById(R.id.loop_container);
        loopContainer.setOnClickListener(this);
        loopTitle = (TextView) loopContainer.findViewById(R.id.loop_count_text);
        loopCount = (TextView) loopContainer.findViewById(R.id.loop_count);
        int loop = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.ONLY_READ_LOOP);
        if (loop != -1) {
            loopCount.setText(String.valueOf(loop));
        }
        fileSizeContainer = (RelativeLayout) findViewById(R.id.file_size_container);
        fileSizeContainer.setOnClickListener(this);
        fileSize = (TextView) fileSizeContainer.findViewById(R.id.file_size);
        int readFileSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.ONLY_READ_FILESIZE);
        if (readFileSize != -1) {
            fileSize.setText(String.valueOf(readFileSize));
        }
        fileSizeTitle = (TextView) fileSizeContainer.findViewById(R.id.file_size_text);
        checkPeriodContainer = (RelativeLayout) findViewById(R.id.check_period_container);
        checkPeriodContainer.setOnClickListener(this);
        checkPeriodTitle = (TextView) checkPeriodContainer.findViewById(R.id.check_period_text);
        checkPeriodCount = (TextView) checkPeriodContainer.findViewById(R.id.check_period);
        int checkPeriod = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.ONLY_READ_CHECK_PERIOD);
        if (checkPeriod != -1) {
            checkPeriodCount.setText(String.valueOf(checkPeriod));
        }
        setChildBackgroud(checkBox.isChecked());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.only_read_container:
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                setChildBackgroud(checkBox.isChecked());
                break;
            case R.id.loop_container:
                dialogHelp.createNumDialog(loopTitle, loopCount, SharePreferenceHelper.Config.ONLY_READ_LOOP);
                break;
            case R.id.file_size_container:
                dialogHelp.createNumDialog(fileSizeTitle, fileSize, SharePreferenceHelper.Config.ONLY_READ_FILESIZE);
                break;
            case R.id.check_period_container:
                dialogHelp.createNumDialog(checkPeriodTitle, checkPeriodCount, SharePreferenceHelper.Config.ONLY_READ_CHECK_PERIOD);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("currentItem", 1);
        bundle.putBoolean("isChecked", checkBox.isChecked());

        bundle.putInt("loop", Integer.parseInt(loopCount.getText().toString()));
        bundle.putInt("fileSize", Integer.parseInt(fileSize.getText().toString()));
        bundle.putInt("checkPeriod", Integer.parseInt(checkPeriodCount.getText().toString()));
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharePreferenceHelper.getInstance(this).saveReadStatus(checkBox.isChecked());
    }

    //更改背景颜色
    private void setChildBackgroud(boolean isChecked) {
        if (isChecked) {
            loopContainer.setEnabled(true);
            loopTitle.setTextColor(getResources().getColor(R.color.black));
            loopCount.setTextColor(getResources().getColor(R.color.black));

            fileSizeContainer.setEnabled(true);
            fileSize.setTextColor(getResources().getColor(R.color.black));
            fileSizeTitle.setTextColor(getResources().getColor(R.color.black));

            checkPeriodContainer.setEnabled(true);
            checkPeriodTitle.setTextColor(getResources().getColor(R.color.black));
            checkPeriodCount.setTextColor(getResources().getColor(R.color.black));
        } else {
            loopContainer.setEnabled(false);
            loopTitle.setTextColor(getResources().getColor(R.color.gray));
            loopCount.setTextColor(getResources().getColor(R.color.gray));

            fileSizeContainer.setEnabled(false);
            fileSize.setTextColor(getResources().getColor(R.color.gray));
            fileSizeTitle.setTextColor(getResources().getColor(R.color.gray));

            checkPeriodContainer.setEnabled(false);
            checkPeriodTitle.setTextColor(getResources().getColor(R.color.gray));
            checkPeriodCount.setTextColor(getResources().getColor(R.color.gray));
        }
    }
}
