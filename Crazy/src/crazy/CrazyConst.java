package crazy;

import com.hp.sbs.container.sip.Fault;

public class CrazyConst {
	// Errors in Acquire Dimension
	private static long acquire_seq = 999999990002020001L;
	public static final Fault E_CONTENT_ACQUISITION = new Fault(acquire_seq++,
			"external.service.content.acquire",
			"Baidu Could Print: Error while retrieving baidu cloud file.");
	public static final Fault E_EPRINT_SERVICE = new Fault(acquire_seq++, "eprint.service", "Baidu Could Print: Server is busy, please try after some time.");
	public static final Fault E_AQUIRE_SERVICE = new Fault(acquire_seq++, "aquire.service", "Baidu Could Print: Temporarily unavailable. Please try again later.");

	
	// Errors in Print Dimension
	private static long print_seq = 999999990002030001L;
	public static final Fault E_EPRINT_JOB_SUBMIT = new Fault(print_seq++,
			"eprint.service.job.submit",
			"Baidu Could Print: Error while submitting job to ePrint service.");
	
	public static final String BAIDU_DISK_ROOT_PATH="/";
	public static final String TEST_BAIDU_DISK_ROOT_PATH="/apps/eprint/";

	public static final String JSP_PATH = "/sipcontent/project/jsp/";

	public static final String SIP = "sip";
	public static final String CLOUD_ID = "1234";

	public static final String SESSION_ID = "session_id";
	// Screens
	public static final String SCREEN = "screen";
	public static final String SCREEN_WELCOME = "welcome_screen";
	public static final String SCREEN_MAIN = "mainList";
	public static final String SCREEN_REGISTER = "register";
	public static final String SCREEN_ERROR = "error";
	public static final String SCREEN_ACCESSTOKEN = "accessTokenscreen";
	public static final String SCREEN_ALL_DOCUMENT = "all_document";
	public static final String SCREEN_PICTURE_DOCUMENT = "picture_document";
	public static final String SCREEN_PLAIN_DOCUMENT = "plain_document";
	public static final String SCREEN_PREPARE_PRINT = "prepare_print";

	// Message of baidu
	public static final String BAIDU_APP_ID = "621208";
	public static final String API_KEY = "WVB1spGcbpyMC74fOGetVZLU";
	public static final String SECRET_KEY = "5KGp6bjOEUBP3qMh1xOqFjrrFnzFceNj";

	public static final String ERROR = "error";
	public static final String ERROR_DESCRIPTION = "error_description";

	public static final String DEVICE_CODE = "device_code";
	public static final String USER_CODE = "user_code";
	public static final String VERIFICATION_URL = "verification_url";
	public static final String QRCODE_URL = "qrcode_url";
	public static final String EXPIRES_IN = "expires_in";
	public static final String INTERVAL = "interval";

	public static final String ACCESS_TOKEN = "access_token";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String SCOPE = "scope";
	public static final String SESSION_KEY = "session_key";
	public static final String SESSION_SECRET = "session_secret";

	public static final String BAIDU_FILE_PATH = "path";
	public static final String CURRENT_DIR="current_directory";
	public static final String FILES_IN_CURRENT_DIR="files";
	
	public static final String STREAM_FILE_START_INDEX="start";
	public static final int STREAM_FILE_PAGE_SIZE=20;
	public static final String STREAM_FILE_PAGE_DIRECTION="direction";
	public static final String STREAM_FILE_NEXT_PAGE="next";
	public static final String STREAM_FILE_PREV_PAGE="prev";
	
	/**
	 * below are resource string index
	 */
	public static final String FILE_PRINT_PREVIEW="prepareprint.file.preview";
	public static final String FILE_NOT_PRINTABLE="prepareprint.file.cantprint";
	public static final String FILE_NOT_PRINTABLE_TITLE="prepareprint.file.cantprint.title";
	public static final String USER_CODE_LABEL="waitregister.label.usercode";
	public static final String VER_URL_LABEL="waitregister.label.url";
	
	public static final String MAIN_LIST_ALL_FILE="main.list.all.file";
	public static final String MAIN_LIST_ALL_IMAGE="main.list.all.image";
	public static final String MAIN_LIST_ALL_DOC="main.list.all.document";
	
	public static final String ROWS_LIST_NEXT_PAGE="rows.list.next";
	public static final String ROWS_LIST_MAIN_MENU="rows.list.mainmenu";
}
