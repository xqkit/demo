package sharepreference;
import utils.Utils;
public class Contants {
	/**只读模式*/
	public static final int READ_ONLY = 1;
	/**ֻ只写模式*/
	public static final int WRITE_ONLY = 2;
	/**读写模式*/
	public static final int READ_AND_WRITE = 3;
	/**大文件创建*/
	public static final int LARGE_FILE_CREATE = 4;
	/**小文件创建*/
	public static final int SMALL_FILE_CREATE = 5;
	/**随即文件创建*/
	public static final int RANDOM_FILE_CREATE = 6;
	/**大文件随即文件创建*/
	public static final int LARGE_FILE_RANDOM_ACCESS = 7;
	/**ֱ*/
	public static final int DIRECTORY_JUMP_CREATE = 8;

	public static final String READ_LOOP_KEY = "readLoop"+ Utils.getprocessPid();
	public static final String WRITE_LOOP_KEY = "writeLoop"+Utils.getprocessPid();
	public static final String CHECK_ERROR_KEY = "check_error"+Utils.getprocessPid();
	public static final String RANDOW_ACCESS_LOOP_KEY = "randomAccessLoop"+Utils.getprocessPid();
	public static final String LOG_PATH_KEY = "logPath"+Utils.getprocessPid();
}
