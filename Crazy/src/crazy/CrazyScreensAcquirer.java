package crazy;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONException;
import org.json.JSONObject;

import pcs.SipsUtils;
import pcs.basic.ISessionService;


import com.hp.sbs.commons.logger.ILogger;
import com.hp.sbs.commons.logger.LoggerFactory;
import com.hp.sbs.commons.webcontent.http.HttpUtils;
import com.hp.sbs.container.impl.sip.SipApp;
import com.hp.sbs.container.platform.service.coordination.exception.CoordinationClientException;
import com.hp.sbs.container.sip.SipRequest;
import com.hp.sbs.container.sip.SipResponse;
import com.hp.sbs.container.sip.WebResponse;
import com.hp.sbs.container.sip.exception.AcquireException;
import com.hp.sbs.container.sip.exception.SipException;


public class CrazyScreensAcquirer {

	static final long serialVersionUID = 1L;

	private static final ILogger logger = LoggerFactory
			.getLogger(CrazyScreensAcquirer.class.getPackage().getName());
	private SipApp apps;
	
	public SipApp getApps() {
		return apps;
	}

	public void setApps(SipApp apps) {
		this.apps = apps;
	}

	public void getAllDocumentScreen(SipRequest sipReq, SipResponse sipRes)
			throws SipException {
		WebResponse response = null;
		String accessToken = CrazyAppHelper.getAccessToken(sipReq);
		String escaped_path = (String) sipReq.getTemplParameter("path");
		String real_path=CrazyAppHelper.getRootPath();
		if (escaped_path != null && !escaped_path.equals(""))
			real_path = SipsUtils.unEscapeTemplatedURIComponent(escaped_path);
		if (!CrazyAppHelper.isDirectory(sipReq, sipRes, real_path)) {
			getPrintScreen(sipReq, sipRes, real_path);
			return;
		}
		String url = "https://pcs.baidu.com/rest/2.0/pcs/file?method=list&access_token="
				+ accessToken + "&path=" + HttpUtils.escapeURIComponent(real_path);
		CrazyAppHelper.writeLine("GET:%s",url);
		try {
			response = WebAccess.getResponse(url, "GET", null, null, null);
			if (response.getStatus() == HttpStatus.SC_OK) {
				String c = new String(response.getBody(), "UTF-8");
				List<BaiduFile> files = BaiduJSON.listFiles(c);
				sipReq.setAttribute(CrazyConst.CURRENT_DIR, real_path);
				sipReq.setAttribute(CrazyConst.FILES_IN_CURRENT_DIR, files);
				String sessionId=sipReq.getTemplParameter(CrazyConst.SESSION_ID);
				if(sessionId!=null){
					sessionId=SipsUtils.unEscapeTemplatedURIComponent(sessionId);
					sipReq.setAttribute(CrazyConst.SESSION_ID, sessionId);
				}
				sipRes.setCacheMaxAge(0);
				apps.includeXMLPage(CrazyConst.JSP_PATH + "documentList.jsp",
						sipReq, sipRes);
				return;
			} else {
				String c = new String(response.getBody(), "UTF-8");
				BaiduError be = BaiduJSON.getError(c);
				getInfoScreen(sipReq, sipRes, be.getErrorCode(),
						be.getErrorMsg());
				return;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("UnsupportedEncodingException", "UnsupportedEncodingException in getAllDocumentScreen", e);
			throw new AcquireException(CrazyConst.E_AQUIRE_SERVICE.code, CrazyConst.E_AQUIRE_SERVICE.key, CrazyConst.E_AQUIRE_SERVICE.msg);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error("JSONException", "JSONException in getAllDocumentScreen", e);
			throw new AcquireException(CrazyConst.E_AQUIRE_SERVICE.code, CrazyConst.E_AQUIRE_SERVICE.key, CrazyConst.E_AQUIRE_SERVICE.msg);
		} 

	}
	
	public void getPictureDocumentScreen(SipRequest sipReq, SipResponse sipRes)throws SipException {

		int pageSize=CrazyConst.STREAM_FILE_PAGE_SIZE;
		int pageStart=0;//default value
		String p=sipReq.getTemplParameter(CrazyConst.STREAM_FILE_PAGE_DIRECTION);
		if(p!=null&&!p.equals("")){
			pageStart=Integer.parseInt(sipReq.getTemplParameter(CrazyConst.STREAM_FILE_START_INDEX));
			if(p.equals(CrazyConst.STREAM_FILE_NEXT_PAGE))
				pageStart+=pageSize;
			else if(p.endsWith(CrazyConst.STREAM_FILE_PREV_PAGE))
				pageStart-=pageSize;
		}
		WebResponse response = null;
		String accessToken = CrazyAppHelper.getAccessToken(sipReq);
		String url = "https://pcs.baidu.com/rest/2.0/pcs/stream?method=list&access_token="
				+ accessToken + "&type=image&limit="+pageSize+"&start="+pageStart;
		try {
			response = WebAccess.getResponse(url, "GET", null, null, null);
			if (response.getStatus() == HttpStatus.SC_OK) {
				String c = new String(response.getBody(), "UTF-8");
				List<BaiduFile> files = BaiduJSON.listFiles(c);
				sipReq.setAttribute(CrazyConst.FILES_IN_CURRENT_DIR, files);
				sipReq.setAttribute(CrazyConst.STREAM_FILE_START_INDEX, pageStart+"");
				String sessionId=sipReq.getTemplParameter(CrazyConst.SESSION_ID);
				if(sessionId!=null){
					sessionId=SipsUtils.unEscapeTemplatedURIComponent(sessionId);
					sipReq.setAttribute(CrazyConst.SESSION_ID, sessionId);
				}
				sipRes.setCacheMaxAge(0);
				apps.includeXMLPage(CrazyConst.JSP_PATH + "all_pictures.jsp",
						sipReq, sipRes);
				return;
			} else {
				String c = new String(response.getBody(), "UTF-8");
				BaiduError be = BaiduJSON.getError(c);
				getInfoScreen(sipReq, sipRes, be.getErrorCode(),
						be.getErrorMsg());
				return;
			}
		}  catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("UnsupportedEncodingException", "UnsupportedEncodingException in getPictureScreen", e);
			throw new AcquireException(CrazyConst.E_AQUIRE_SERVICE.code, CrazyConst.E_AQUIRE_SERVICE.key, CrazyConst.E_AQUIRE_SERVICE.msg);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error("JSONException", "JSONException in getPictureScreen", e);
			throw new AcquireException(CrazyConst.E_AQUIRE_SERVICE.code, CrazyConst.E_AQUIRE_SERVICE.key, CrazyConst.E_AQUIRE_SERVICE.msg);
		} 
	}

	public void getPlainDocumentScreen(SipRequest sipReq, SipResponse sipRes)throws SipException{
		int pageSize=CrazyConst.STREAM_FILE_PAGE_SIZE;
		int pageStart=0;//default value
		String p=sipReq.getTemplParameter(CrazyConst.STREAM_FILE_PAGE_DIRECTION);
		if(p!=null&&!p.equals("")){
			pageStart=Integer.parseInt(sipReq.getTemplParameter(CrazyConst.STREAM_FILE_START_INDEX));
			if(p.equals(CrazyConst.STREAM_FILE_NEXT_PAGE))
				pageStart+=pageSize;
			else if(p.endsWith(CrazyConst.STREAM_FILE_PREV_PAGE))
				pageStart-=pageSize;
		}
		WebResponse response = null;
		String accessToken = CrazyAppHelper.getAccessToken(sipReq);
		String url = "https://pcs.baidu.com/rest/2.0/pcs/stream?method=list&access_token="
				+ accessToken + "&type=doc&limit="+pageSize+"&start="+pageStart;
		CrazyAppHelper.writeLine("get Doc url:%s",url);
		try {
			response = WebAccess.getResponse(url, "GET", null, null, null);
			if (response.getStatus() == HttpStatus.SC_OK) {
				String c = new String(response.getBody(), "UTF-8");
				List<BaiduFile> files = BaiduJSON.listFiles(c);
				sipReq.setAttribute(CrazyConst.FILES_IN_CURRENT_DIR, files);
				sipReq.setAttribute(CrazyConst.STREAM_FILE_START_INDEX, pageStart+"");
				String sessionId=sipReq.getTemplParameter(CrazyConst.SESSION_ID);
				if(sessionId!=null){
					sessionId=SipsUtils.unEscapeTemplatedURIComponent(sessionId);
					sipReq.setAttribute(CrazyConst.SESSION_ID, sessionId);
				}
				sipRes.setCacheMaxAge(0);
				apps.includeXMLPage(CrazyConst.JSP_PATH + "all_plain_document.jsp",
						sipReq, sipRes);
				return;
			} else {
				String c = new String(response.getBody(), "UTF-8");
				BaiduError be = BaiduJSON.getError(c);
				getInfoScreen(sipReq, sipRes, be.getErrorCode(),
						be.getErrorMsg());
				return;
			}
		}  catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("UnsupportedEncodingException", "UnsupportedEncodingException in getPlainDocumentScreen", e);
			throw new AcquireException(CrazyConst.E_AQUIRE_SERVICE.code, CrazyConst.E_AQUIRE_SERVICE.key, CrazyConst.E_AQUIRE_SERVICE.msg);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error("JSONException", "JSONException in getPlainDocumentScreen", e);
			throw new AcquireException(CrazyConst.E_AQUIRE_SERVICE.code, CrazyConst.E_AQUIRE_SERVICE.key, CrazyConst.E_AQUIRE_SERVICE.msg);
		} 
	}
	public void getPrintScreen(SipRequest sipReq, SipResponse sipRes,
			String real_path) throws SipException {
		if(CrazyAppHelper.isPrintable(sipReq, sipRes, real_path)){
			String thumbnail = CrazyAppHelper.getThumbnailUrl(CrazyAppHelper.getAccessToken(sipReq), real_path);
			sipReq.setAttribute("thumbnail", thumbnail);
			sipReq.setAttribute("path", real_path);
			sipRes.setCacheMaxAge(0);
			apps.includeXMLPage(CrazyConst.JSP_PATH + "preparePrint.jsp", sipReq, sipRes);
			return;
		}
		else {
			ResourceBundle rb=sipReq.getResourceBundle();
			String title=rb.getString(CrazyConst.FILE_NOT_PRINTABLE_TITLE);
			String msg=rb.getString(CrazyConst.FILE_NOT_PRINTABLE);
			getInfoScreen(sipReq, sipRes, title, msg);
			return;
		}
		
	}
	public void getInfoScreen(SipRequest sipReq, SipResponse sipRes,
			String heading, String info) throws SipException {
		sipReq.setAttribute(CrazyConst.ERROR, heading);
		sipReq.setAttribute(CrazyConst.ERROR_DESCRIPTION, info);
		sipRes.setCacheMaxAge(0);
		apps.includeXMLPage(CrazyConst.JSP_PATH + "informationScreen.jsp",
				sipReq, sipRes);
	}
	public void getRegisterScreen(SipRequest sipReq,SipResponse sipRes) throws SipException {
		WebResponse response=null;
		String sessionId = sipReq.getTemplParameter(CrazyConst.SESSION_ID);
		sessionId=SipsUtils.unEscapeTemplatedURIComponent(sessionId);
		
		
		try {
			String url="https://openapi.baidu.com/oauth/2.0/device/code?client_id=" +CrazyConst.API_KEY+
					"&response_type=device_code&scope=basic,netdisk";
			response=WebAccess.getResponse(url, "GET", null, null, null);
			if(response.getStatus()==HttpStatus.SC_OK){
				String content=new String(response.getBody(),"UTF-8");
				JSONObject js=new JSONObject(content);
				if(js.has(CrazyConst.ERROR)){
					getInfoScreen(sipReq, sipRes,"Error in registerScreen", js.getString(CrazyConst.ERROR_DESCRIPTION));
					return;
				}
				String device_code=js.getString(CrazyConst.DEVICE_CODE);
				String user_code=js.getString(CrazyConst.USER_CODE);
				String verification_url=js.getString(CrazyConst.VERIFICATION_URL);
				/**
				 * not use until now, keep for future
				 */
				String qrcode_url=js.getString(CrazyConst.QRCODE_URL);
				int expires_in=js.getInt(CrazyConst.EXPIRES_IN);
				int interval=js.getInt(CrazyConst.INTERVAL);
				//Save device code as user name in credential service
				CrazyAppHelper.getCredentialCreator().setBeanParameter(null, sipReq).create().savePcsUser(device_code, null);
				
				final String sId=sessionId;
				ISessionService sessionHelper =CrazyAppHelper.getSessionCreator()
						.setBeanParameters(new HashMap<String, Object>() {{
							put("session_id",sId);
							put("version", false);
							put("encrypt", true);
						}}).create();
				sessionId = sessionHelper.getSessionId();
				sessionHelper.storeValue(CrazyConst.DEVICE_CODE, device_code);
				sessionHelper.storeValue(CrazyConst.USER_CODE, user_code);
				/**
				 * not use until now, keep for future
				 */
				sessionHelper.storeValue(CrazyConst.VERIFICATION_URL, verification_url);
				sessionHelper.storeValue(CrazyConst.QRCODE_URL, qrcode_url);
				sessionHelper.storeValue(CrazyConst.EXPIRES_IN, expires_in);
				sessionHelper.storeValue(CrazyConst.INTERVAL, interval);
				/**
				 * show user code and verification url to user
				 */
				sipReq.setAttribute(CrazyConst.USER_CODE, user_code);
				sipReq.setAttribute(CrazyConst.VERIFICATION_URL, verification_url);
				sipReq.setAttribute(CrazyConst.SESSION_ID, sessionId);
				
				sipRes.setCacheMaxAge(0);
				apps.includeXMLPage(CrazyConst.JSP_PATH+"waitRegister.jsp", sipReq, sipRes);
				return;
			}
			else {
				String content=new String(response.getBody(),"UTF-8");
				JSONObject js=new JSONObject(content);
				getInfoScreen(sipReq, sipRes,"Error", js.getString(CrazyConst.ERROR_DESCRIPTION));
				return;
			}
		}  catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("UnsupportedEncodingException", "UnsupportedEncodingException in getRegisterScreen", e);
			throw new AcquireException(CrazyConst.E_AQUIRE_SERVICE.code, CrazyConst.E_AQUIRE_SERVICE.key, CrazyConst.E_AQUIRE_SERVICE.msg);
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error("JSONException", "JSONException in getRegisterScreen", e);
			throw new AcquireException(CrazyConst.E_AQUIRE_SERVICE.code, CrazyConst.E_AQUIRE_SERVICE.key, CrazyConst.E_AQUIRE_SERVICE.msg);
		} 
		
	}
	public void getAccessTokenScreen(SipRequest sipReq,SipResponse sipRes) throws SipException{
		String errorString="Can't get access token";
		String device_code=CrazyAppHelper.getDeviceCode(sipReq);
		if(device_code==null||device_code.equals("")){
			getInfoScreen(sipReq, sipRes,"Error in get access token screen", "Can't get the device code");
			return;
		}
		String accessUrl = "https://openapi.baidu.com/oauth/2.0/token?grant_type=device_token&"
				+ "code="+device_code
				+ "&client_id="+CrazyConst.API_KEY
				+ "&client_secret="+CrazyConst.SECRET_KEY;
		WebResponse response;
		String content="";
		response=WebAccess.getResponse(accessUrl, "GET", null, null, null);
		try {
			if(response.getStatus()==HttpStatus.SC_OK){
				content=new String(response.getBody(),"UTF-8");
				JSONObject js=new JSONObject(content);
				if(js.has(CrazyConst.ERROR)){
					getInfoScreen(sipReq, sipRes,"Error in get access token", js.getString(CrazyConst.ERROR_DESCRIPTION));
					return;
				}
				String access_token=js.getString(CrazyConst.ACCESS_TOKEN);
				//Must store the refresh token for future
				String refresh_token=js.getString(CrazyConst.REFRESH_TOKEN);
				String sessionId=sipReq.getTemplParameter(CrazyConst.SESSION_ID);
				CrazyAppHelper.getCredentialCreator().setBeanParameter(null, sipReq).create().savePcsUser(device_code, access_token);
				
				sessionId=SipsUtils.unEscapeTemplatedURIComponent(sessionId);
				/*
				 * store access token in session
				 */
				final String sId=sessionId;
				ISessionService sessionHelper =CrazyAppHelper.getSessionCreator()
						.setBeanParameters(new HashMap<String, Object>() {{
							put("session_id",sId);
							put("version", false);
							put("encrypt", true);
						}}).create();
				sessionHelper.storeValue(CrazyConst.ACCESS_TOKEN, access_token);
				sessionHelper.storeValue(CrazyConst.REFRESH_TOKEN, refresh_token);
				sipReq.setAttribute(CrazyConst.SESSION_ID, sessionHelper.getSessionId());
				
				sipRes.setCacheMaxAge(0);
				apps.includeXMLPage(CrazyConst.JSP_PATH+"viewTypeList.jsp", sipReq, sipRes);
				return;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorString=errorString.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorString=errorString.toString();
		} 
		getInfoScreen(sipReq, sipRes,"Error in get access token",errorString);
	}
	public void getMainScreen(SipRequest sipReq, SipResponse sipRes) throws SipException {
		String authToken = CrazyAppHelper.getAccessToken(sipReq);
		if (authToken != null && !authToken.equals("")) {
			String sessionId = sipReq.getTemplParameter(CrazyConst.SESSION_ID);
			sessionId=SipsUtils.unEscapeTemplatedURIComponent(sessionId);
			final String sId=sessionId;
			ISessionService sessionHelper =CrazyAppHelper.getSessionCreator()
					.setBeanParameters(new HashMap<String, Object>() {{
						put("session_id",sId);
						put("version", false);
						put("encrypt", true);
					}}).create();
			sessionId = sessionHelper.getSessionId();
			sipReq.setAttribute(CrazyConst.SESSION_ID, sessionId);
			sipReq.setAttribute(CrazyConst.ACCESS_TOKEN, authToken);
			sessionHelper.storeValue(CrazyConst.ACCESS_TOKEN, authToken);
			
			sipRes.setCacheMaxAge(0);
			apps.includeXMLPage(CrazyConst.JSP_PATH + "viewTypeList.jsp",
					sipReq, sipRes);
			return;
		}
		else{
			getRegisterScreen(sipReq, sipRes);
		} 

	}
	public void getWelcomeScreen(SipRequest sipReq, SipResponse sipRes)throws SipException {
		final String sipId = sipReq.getTemplParameter(CrazyConst.SIP);
		String uniqueId = sipReq.getUniqueId();
		String sessionId = null;
		if (uniqueId == null) {
			uniqueId = "cb3d1c291087c516f1ee63023742548e55812fbd";
		}
		String cloudId = sipReq.getCloudId();
		if (cloudId == null || cloudId.equals("null") || cloudId.equals("123")) {
			cloudId = CrazyConst.CLOUD_ID;
		}
		final String cId=cloudId;
		ISessionService sessionHelper =CrazyAppHelper.getSessionCreator()
				.setBeanParameters(new HashMap<String, Object>() {{
					put("sip_id",sipId);
					put("cloud_id",cId);
					put("time_out",1000 * 60 * 30);
					put("version", false);
					put("encrypt", true);
				}}).create();
		sessionId = sessionHelper.getSessionId();
		sipReq.setAttribute(CrazyConst.SESSION_ID, sessionId);
		
		sipRes.setCacheMaxAge(0);
		apps.includeXMLPage(CrazyConst.JSP_PATH + "CrazyMainCui.jsp", sipReq,
				sipRes);
	}
}
