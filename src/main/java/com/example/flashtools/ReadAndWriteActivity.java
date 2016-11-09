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

public class ReadAndWriteActivity extends Activity implements View.OnClickListener {
    private RelativeLayout readWriteContainer, loopContainer, fileSizeContainer;
    private CheckBox checkBox;
    private TextView loopTitle, loopCount;
    private TextView fileSizeTitle, fileSize;

    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_and_write);
        initView();
    }

    private void initView() {
        readWriteContainer = (RelativeLayout) findViewById(R.id.read_and_write_container);
        readWriteContainer.setOnClickListener(this);
        loopContainer = (RelativeLayout) findViewById(R.id.loop_container);
        loopContainer.setOnClickListener(this);
        fileSizeContainer = (RelativeLayout) findViewById(R.id.file_size_container);
        fileSizeContainer.setOnClickListener(this);

        checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChildBackground(checkBox.isChecked());
            }
        });
        loopTitle = (TextView) loopContainer.findViewById(R.id.loop_count_text);
        loopCount = (TextView) loopContainer.findViewById(R.id.loop_count);
        int loopCountSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.READ_WRITE_LOOP);
        if (loopCountSize != -1) {
            loopCount.setText(String.valueOf(loopCountSize));
        }
        fileSizeTitle = (TextView) fileSizeContainer.findViewById(R.id.file_size_text);
        fileSize = (TextView) fileSizeContainer.findViewById(R.id.file_size);
        int readwriteFileSize = SharePreferenceHelper.getInstance(this).getChangeData(SharePreferenceHelper.Config.READ_WRITE_FILESIZE);
        if (readwriteFileSize != -1) {
            fileSize.setText(String.valueOf(readwriteFileSize));
        }
        setChildBackground(checkBox.isChecked());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.read_and_write_container:
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                setChildBackground(checkBox.isChecked());
                break;
            case R.id.loop_container:
                new DialogHelp(this).createNumDialog(loopTitle, loopCount, SharePreferenceHelper.Config.READ_WRITE_LOOP);
                break;
            case R.id.file_size_container:
                new DialogHelp(this).createNumDialog(fileSizeTitle, fileSize, SharePreferenceHelper.Config.READ_WRITE_FILESIZE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("currentItem", 3);
        bundle.putBoolean("isChecked", checkBox.isChecked());

        bundle.putInt("loop", Integer.parseInt(loopCount.getText().toString()));
        bundle.putInt("fileSize", Integer.parseInt(fileSize.getText().toString()));
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    private void setChildBackground(boolean isChecked) {
        if (isChecked) {
            loopContainer.setEnabled(true);
            loopTitle.setTextColor(getResources().getColor(R.color.black));
            loopCount.setTextColor(getResources().getColor(R.color.black));
            fileSizeContainer.setEnabled(true);
            fileSizeTitle.setTextColor(getResources().getColor(R.color.black));
            fileSize.setTextColor(getResources().getColor(R.color.black));
        } else {
            loopContainer.setEnabled(false);
            loopTitle.setTextColor(getResources().getColor(R.color.gray));
            loopCount.setTextColor(getResources().getColor(R.color.gray));
            fileSizeContainer.setEnabled(false);
            fileSizeTitle.setTextColor(getResources().getColor(R.color.gray));
            fileSize.setTextColor(getResources().getColor(R.color.gray));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharePreferenceHelper.getInstance(this).saveReadStatus(checkBox.isChecked());
    }
}
