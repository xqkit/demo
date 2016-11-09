package sharepreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dave.shi on 2016/5/10.
 */
public class SharePreferenceHelper {
    private static Context mContext;
    private static SharePreferenceHelper instance;

    public static SharePreferenceHelper getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new SharePreferenceHelper();
        }
        return instance;
    }

    //保存只读状态
    public void saveReadStatus(boolean readIsChecked) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("_readCheck", readIsChecked);
        editor.commit();
    }

    //保存只写状态
    public void saveWriteStatus(boolean writeIsChecked) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("_writeCheck", writeIsChecked);
        editor.commit();
    }

    //保存读写状态
    public void saveReadAndWriteStatus(boolean isReadAndWrite) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("_readAndWrite", isReadAndWrite);
        editor.commit();
    }

    public boolean getReadChecked() {
        SharedPreferences sharePreference = mContext.getSharedPreferences("_status", Context.MODE_PRIVATE);
        boolean isReadChecked = sharePreference.getBoolean("_readCheck", false);
        return isReadChecked;
    }

    public boolean getWriteChecked() {
        SharedPreferences sharePreference = mContext.getSharedPreferences("_status", Context.MODE_PRIVATE);
        boolean isWriteChecked = sharePreference.getBoolean("_writeCheck", false);
        return isWriteChecked;
    }

    public boolean getReadAndWriteChecked() {
        SharedPreferences sharePreference = mContext.getSharedPreferences("_status", Context.MODE_PRIVATE);
        boolean isReadAndWriteChecked = sharePreference.getBoolean("_readAndWrite", false);
        return isReadAndWriteChecked;
    }

    public void deleteFile() {
        SharedPreferences sharePreference = mContext.getSharedPreferences("_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePreference.edit();
        editor.clear().commit();
    }

    /**
     * 保存整形数据
     */
    public void saveChangeData(String key, int value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获取整形数据
     */
    public int getChangeData(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("_status", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    public class Config {
        public static final String ONLY_READ_LOOP = "_readLoopCount";
        public static final String ONLY_READ_FILESIZE = "_readFileSize";
        public static final String ONLY_READ_CHECK_PERIOD = "_readCheckPeriod";

        public static final String ONLY_WRITE_LOOP = "_writeLoopCount";
        public static final String ONLY_WRITE_FILESIZE = "_writeFileSize";
        public static final String ONLY_WRITE_PACKAGESIZE = "_writePackageSize";
        public static final String ONLY_WRITE_RATE = "_writeRate";
        public static final String ONLY_WRITE_CHECK_PERIOD = "_writeCheckPeriod";
        public static final String ONLY_WRITE_LEFT_SPACE = "_writeLeftSpace";
        public static final String ONLY_WRITE_CPU_FREQ = "_writeCpuFreq";

        public static final String READ_WRITE_LOOP = "_readWriteLoopCount";
        public static final String READ_WRITE_FILESIZE = "_readWriteFileSize";

        public static final String CARETE_LARGE_FILE_LOOP = "_create_large_file_loop";
        public static final String CREATE_LARGE_FILE_SIZE = "_create_large_file_size";

        public static final String CREATE_SMALL_FILE_LOOP = "_create_small_file_loop";
        public static final String CREATE_SMALL_FILE_SIZE = "_create_small_file_size";

        public static final String CREATE_RANDOM_FILE_LOOP = "_create_random_file_loop";
        public static final String CREATE_RANDOM_FILE_SIZE = "_create_random_file_size";

        public static final String FILE_RANDOM_ACCESS_LOOP = "_file_random_access_loop";
        public static final String FILE_RANDOM_ACCESS_SIZE = "_file_random_access_size";
    }
}
