package crazy;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.httpclient.HttpStatus;
import org.json.JSONException;

import pcs.SipsUtils;
import pcs.basic.ICreator;
import pcs.basic.ICredentialService;
import pcs.basic.ISessionService;
import test.TestCredentialCreatorImpl;
import test.TestCredentialServiceImpl;
import test.TestSessionCreatorImpl;
import test.TestSessionServiceImpl;

import com.hp.sbs.commons.sbsconfig.Config;
import com.hp.sbs.commons.webcontent.http.HttpUtils;
import com.hp.sbs.container.external.eprint.doc.PrintJobDoc.DocumentFormat;
import com.hp.sbs.container.platform.service.coordination.exception.CoordinationClientException;
import com.hp.sbs.container.sip.SipRequest;
import com.hp.sbs.container.sip.SipResponse;
import com.hp.sbs.container.sip.WebResponse;

public class CrazyAppHelper {
	/**
	 * just for debug
	 * @param text
	 */
	public static void writeLine(String fmt,Object...args) {
		System.out.printf(fmt+"\n\n", args);
	}

	public static String getRootPath(){
		if(getAppRunMode().equals("test"))
			return CrazyConst.TEST_BAIDU_DISK_ROOT_PATH;
		else {
			return CrazyConst.BAIDU_DISK_ROOT_PATH;
		}
	}
	/**
	 * get the credential creator for credential service
	 * @return
	 */
	public static ICreator<ICredentialService> getCredentialCreator() {
		ICreator<ICredentialService> cCreator = null;
		String mode = getAppRunMode();
		if (mode.equals("test")) {
			cCreator = new TestCredentialCreatorImpl();
		} else {
			cCreator = new CredentialCreatorImpl();
		}
		return cCreator;
	}

	/**
	 * get the session creator for session service
	 * @return
	 */
	public static ICreator<ISessionService> getSessionCreator() {
		ICreator<ISessionService> sCreator = null;
		String mode = getAppRunMode();
		if (mode.equals("test")) {
			sCreator = new TestSessionCreatorImpl();
		} else {
			sCreator = new SessionCreatorImpl();
		}
		return sCreator;
	}

	/**
	 * get the App's current run mode
	 * @return
	 */
	public static String getAppRunMode() {
		Properties prop = Config.getProperties("CrazyConfig.properties");
		String mode = Config.getStringProperty(prop, "run_mode");
		writeLine("This App runs in [%s] mode!",mode);
		return mode;

	}

	/**
	 * get device code from httprequest or session or credential service
	 * @param sipReq
	 * @return
	 */
	public static String getDeviceCode(SipRequest sipReq) {
		String device_code = (String) sipReq
				.getAttribute(CrazyConst.DEVICE_CODE);
		if (device_code != null && !device_code.equals("")) {
			return device_code;
		}
		String sessionId = sipReq.getTemplParameter(CrazyConst.SESSION_ID);
		if (sessionId != null) {
			final String sId = SipsUtils
					.unEscapeTemplatedURIComponent(sessionId);
			ISessionService sessionService = getSessionCreator()
					.setBeanParameters(new HashMap<String, Object>() {
						{
							put("session_id", sId);
							put("version", false);
							put("encrypt", true);
						}
					}).create();
			device_code = (String) sessionService.retreiveValue(CrazyConst.DEVICE_CODE);
			if (device_code != null && !device_code.equals(""))
				return device_code;
		}
		device_code = getCredentialCreator().setBeanParameter(null, sipReq)
				.create().getDeviceCode();
		sipReq.setAttribute(CrazyConst.DEVICE_CODE, device_code);
		return device_code;
	}

	/**
	 * get xAuth token from httprequest or session or credential service
	 * @param sipReq
	 * @return
	 */
	public static String getAccessToken(SipRequest sipReq) {
		String token = (String) sipReq.getTemplParameter(CrazyConst.ACCESS_TOKEN);
		if (token != null && !token.equals("")) {
			sipReq.setAttribute(CrazyConst.ACCESS_TOKEN, token);
			return token;
		}
		token = (String) sipReq.getAttribute(CrazyConst.ACCESS_TOKEN);
		if (token != null && !token.equals("")) {
			return token;
		}
		String dc = getDeviceCode(sipReq);
		if (dc == null || dc.equals(""))
			return null;
		String sessionId = sipReq.getTemplParameter(CrazyConst.SESSION_ID);
		ISessionService sessionService = null;
		if (sessionId != null) {
			final String sId = SipsUtils
					.unEscapeTemplatedURIComponent(sessionId);
			sessionService = getSessionCreator().setBeanParameters(
					new HashMap<String, Object>() {
						{
							put("session_id", sId);
							put("version", false);
							put("encrypt", true);
						}
					}).create();
			token = (String) sessionService.retreiveValue(CrazyConst.ACCESS_TOKEN);
			if (token != null && !token.equals("")) {
				sipReq.setAttribute(CrazyConst.ACCESS_TOKEN, token);
				return token;
			}
		}
		token = getCredentialCreator().setBeanParameter(null, sipReq).create()
				.getAccessToken(dc);
		sipReq.setAttribute(CrazyConst.ACCESS_TOKEN, token);
		if (sessionService != null)
			sessionService.storeValue(CrazyConst.ACCESS_TOKEN, token);
		return token;

	}

	public static boolean isDirectory(SipRequest sipReq, SipResponse sipRes,
			String real_path) {
		WebResponse response = null;
		String accessToken = getAccessToken(sipReq);
		real_path = HttpUtils.escapeURIComponent(real_path);
		String url = "https://pcs.baidu.com/rest/2.0/pcs/file?method=meta&access_token="
				+ accessToken + "&path=" + real_path;
		try {
			response = WebAccess.getResponse(url, "GET", null, null, null);
			if (response.getStatus() == HttpStatus.SC_OK) {
				String c = new String(response.getBody(), "UTF-8");
				BaiduMeta meta = BaiduJSON.getMeta(c);
				return meta.isDirectory();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			writeLine(e.getMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			writeLine(e.getMessage());
		}
		return true;
	}

	/**
	 * judge if the file is printable
	 * @param sipReq
	 * @param sipRes
	 * @param real_path
	 * @return
	 */
	public static boolean isPrintable(SipRequest sipReq, SipResponse sipRes,
			String real_path) {
		DocumentFormat[] formats = DocumentFormat.values();
		int i = real_path.lastIndexOf(".");
		if (i <= 0 || i == real_path.length() - 1)
			return false;
		String extendedName = real_path.substring(i + 1);
		for (DocumentFormat df : formats) {
			if (df.toString().equalsIgnoreCase(extendedName))
				return true;
		}
		return false;
	}

	public static String getThumbnailUrl(String accessToken, String real_path,
			int width, int height) {
		real_path = HttpUtils.escapeURIComponent(real_path);
		return "https://pcs.baidu.com/rest/2.0/pcs/thumbnail?method=generate&access_token="
				+ accessToken
				+ "&path="
				+ real_path
				+ "&quality=100&width="
				+ width + "&height=" + height;
	}

	public static String getThumbnailUrl(String accessToken, String real_path) {
		int width = 400;
		int height = 400;
		return getThumbnailUrl(accessToken, real_path, width, height);
	}

	/**
	 * get document format according to file path
	 * @param real_path
	 * @return
	 */
	public static DocumentFormat getDocumentFormat(String real_path) {
		int i = real_path.lastIndexOf(".");
		String fmt = real_path.substring(i + 1).toUpperCase();
		try {
			DocumentFormat df = DocumentFormat.valueOf(fmt);
			return df;
		} catch (IllegalArgumentException e) {
			writeLine("Not support format: %s", fmt);
			e.printStackTrace();
			return null;
		}
	}

}
