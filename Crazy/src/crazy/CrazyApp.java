package crazy;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletOutputStream;

import org.apache.commons.httpclient.Header;
import pcs.SipsUtils;

import com.hp.sbs.commons.logger.ILogger;
import com.hp.sbs.commons.logger.LoggerFactory;
import com.hp.sbs.commons.security.oauth.exception.OAuthException;
import com.hp.sbs.container.external.eprint.IPrintService;
import com.hp.sbs.container.external.eprint.doc.PrintJobDoc;
import com.hp.sbs.container.external.eprint.doc.PrintJobDoc.DocumentFormat;
import com.hp.sbs.container.external.eprint.doc.PrintJobDoc.Orientation;
import com.hp.sbs.container.external.eprint.exception.PrintServiceException;
import com.hp.sbs.container.impl.sip.SipApp;
import com.hp.sbs.container.sip.SIPServiceFinder;
import com.hp.sbs.container.sip.SipRequest;
import com.hp.sbs.container.sip.SipResponse;
import com.hp.sbs.container.sip.WebResponse;
import com.hp.sbs.container.sip.exception.AcquireException;
import com.hp.sbs.container.sip.exception.PrintException;
import com.hp.sbs.container.sip.exception.SipException;


public class CrazyApp extends SipApp {
	static final long serialVersionUID = 1L;

	private static final ILogger logger = LoggerFactory
			.getLogger(CrazyApp.class.getPackage().getName());

	private CrazyScreensAcquirer screensAcquirer;
	@Override
	public void initSip(ServletConfig config) {
		super.initSip(config);
		screensAcquirer=new CrazyScreensAcquirer();
		screensAcquirer.setApps(this);
	}
	@Override
	public void doMainCui(SipRequest sipReq, SipResponse sipRes)
			throws SipException {
		logger.trace("In CrazyApp::doMainCui()");
		String screen = sipReq.getTemplParameter(CrazyConst.SCREEN);
		CrazyAppHelper.writeLine("Screen=" + screen);
		doMainCuiScreens(sipReq, sipRes, screen);
		logger.trace("Completed CrazyApp::doMainCui()");
	}

	private void doMainCuiScreens(SipRequest sipReq, SipResponse sipRes,
			String screen) throws SipException {
		if (screen == null || screen.equals("")||screen.equals(CrazyConst.SCREEN_WELCOME)) {
			screensAcquirer.getWelcomeScreen(sipReq, sipRes);
			return;
		} else if (screen.equals(CrazyConst.SCREEN_MAIN)) {
			screensAcquirer.getMainScreen(sipReq, sipRes);
			return;
		} else if (screen.equals(CrazyConst.SCREEN_REGISTER)) {
			screensAcquirer.getRegisterScreen(sipReq,sipRes);
			return;
		} else if (screen.equals(CrazyConst.SCREEN_ACCESSTOKEN)) {
			screensAcquirer.getAccessTokenScreen(sipReq, sipRes);
			return;
		} else if (screen.equals(CrazyConst.SCREEN_ALL_DOCUMENT)) {
			screensAcquirer.getAllDocumentScreen(sipReq, sipRes);
			return;
		}else if(screen.equals(CrazyConst.SCREEN_PICTURE_DOCUMENT)){
			screensAcquirer.getPictureDocumentScreen(sipReq,sipRes);
			return;
		}else if(screen.equals(CrazyConst.SCREEN_PLAIN_DOCUMENT)){
			screensAcquirer.getPlainDocumentScreen(sipReq,sipRes);
			return;
		}

	}

	public String submitPrintJob(SipRequest sipReq, SipResponse sipRes)
			throws AcquireException, PrintException, OAuthException {
		logger.trace("In CrazyApp::submitPrintJob()");

		String cloudId = sipReq.getCloudId();
		String uniqueIdNVPair = sipReq.getUniqueIdNVPair();
		String path = sipReq.getTemplParameter(CrazyConst.BAIDU_FILE_PATH);
		String accessToken = CrazyAppHelper.getAccessToken(sipReq);
		String url = sipReq.sipCapInterestUrl("/do/print") + "/path/" + path
				+ "/access_token/" + accessToken + "?" + uniqueIdNVPair;
		
		String mediaSizeName = sipReq.getPrinterCapability()
				.getRegionDefPaperSize();
		String jobId = null;
		DocumentFormat df = CrazyAppHelper.getDocumentFormat(SipsUtils.unEscapeTemplatedURIComponent(path));
		try {
			PrintJobDoc printJobDoc = new PrintJobDoc(url, df,
					Orientation.Portrait, mediaSizeName, 1);
			// invoke ePrint service
			jobId = ((IPrintService) SIPServiceFinder
					.findService(IPrintService.class.getName())).invokeJob(
					cloudId, printJobDoc);
		} catch (PrintServiceException pse) {
			String msg = CrazyConst.E_EPRINT_JOB_SUBMIT.msg;
			logger.error("PrintServiceException", msg, pse);
			throw new PrintException(CrazyConst.E_EPRINT_JOB_SUBMIT.code,
					CrazyConst.E_EPRINT_JOB_SUBMIT.key, msg, pse);
		}

		logger.trace("Completed CrazyApp::submitPrintJob()");
		return jobId;
	}

	@Override
	public void doPrint(SipRequest sipReq, SipResponse sipRes)
			throws AcquireException, PrintException {
		logger.trace("In CrazyApp::doPrint()");
		String accessToken = CrazyAppHelper.getAccessToken(sipReq);
		String path = sipReq.getTemplParameter(CrazyConst.BAIDU_FILE_PATH);
		path=SipsUtils.unEscapeTemplatedURIComponent(path);
		String printUrl = "https://pcs.baidu.com/rest/2.0/pcs/file?method=download&access_token="
				+ accessToken + "&path=" + path;
		try {
			sipRes.sendRedirect(printUrl);
		} catch (IOException e) {
			String msg = CrazyConst.E_CONTENT_ACQUISITION.msg;
			logger.error("IOException", msg);
			throw new AcquireException(CrazyConst.E_CONTENT_ACQUISITION.code,
					CrazyConst.E_CONTENT_ACQUISITION.key, msg);
		}
		logger.trace("Completed CrazyApp::doPrint()");
	}

	public void my_doPrint(SipRequest sipReq, SipResponse sipRes)
			throws AcquireException, PrintException {
		logger.trace("In CrazyApp::doPrint()");
		String accessToken = CrazyAppHelper.getAccessToken(sipReq);
		String path = sipReq.getTemplParameter(CrazyConst.BAIDU_FILE_PATH);
		path=SipsUtils.unEscapeTemplatedURIComponent(path);
		String printUrl = "https://pcs.baidu.com/rest/2.0/pcs/file?method=download&access_token="
				+ accessToken + "&path=" + path;
		WebResponse response = null;
		try {
			response = WebAccess.getResponse(printUrl, "GET", null, null, null);
			String contentType=null;
			Header[] headers=response.getHeaders();
			for(int i=0;i<headers.length;i++){
				if(headers[i].getName().equalsIgnoreCase("Content-Type")){
					contentType=headers[i].getValue();
					break;
				}
			}
			byte[] data=response.getBody();
			sipRes.setContentType(contentType);
			sipRes.setContentLength(data.length);
			
			ServletOutputStream sos=sipRes.getOutputStream();
			sos.write(data);
			sos.flush();
			
		} catch (Exception e) {
			String msg = CrazyConst.E_CONTENT_ACQUISITION.msg;
			logger.error("IOException", msg);
			throw new AcquireException(CrazyConst.E_CONTENT_ACQUISITION.code,
					CrazyConst.E_CONTENT_ACQUISITION.key, msg);
		}
		logger.trace("Completed CrazyApp::doPrint()");
	}

}