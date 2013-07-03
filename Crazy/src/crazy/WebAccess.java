package crazy;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;

import com.hp.sbs.commons.logger.ILogger;
import com.hp.sbs.commons.logger.LoggerFactory;
import com.hp.sbs.container.sip.IWebContent;
import com.hp.sbs.container.sip.SIPServiceFinder;
import com.hp.sbs.container.sip.WebResponse;

public class WebAccess {

	protected static ILogger logger = LoggerFactory.getLogger(WebAccess.class.getPackage().getName());
	private static IWebContent webContent=
			(IWebContent) SIPServiceFinder.findService(IWebContent.class.getName());
	private static IWebContent getWebContent() {
		return webContent;
	}
	public static WebResponse getResponse(String url, String method, RequestEntity reqEntity, MultipartRequestEntity multiPartReqentity,List<Header> headerList){
	    logger.trace("Entering getResponse()");  
	    WebResponse webRes = null;
	    try {
	        logger.info(" Fetching URl=== "+url);
	        logger.debug(" Fetching headerList=== "+headerList);
	        logger.debug(" Fetching URl=== "+url);
	        logger.debug(" Fetching headerList=== "+headerList);
	        if(url!=null){
	            if(method.equals("GET")){
	                webRes= getWebContent().fetchURL(url,headerList);
	                logger.info(" WebResponse === "+new String(webRes.getBody(), "UTF-8"));
	                logger.trace("Exiting getResponse()");  
	                return webRes;
	            }else if(method.equals("POST")&&reqEntity!=null){
	                    webRes= getWebContent().fetchURL(url,headerList,reqEntity);
	                    logger.info(" WebResponse === "+new String(webRes.getBody(), "UTF-8"));
	                    logger.trace("Exiting getResponse()");
	                    return webRes;
	                }else if(method.equals("MULTIPART")&&multiPartReqentity!=null){
	                        webRes= getWebContent().fetchURL(url,headerList,multiPartReqentity);
	                        logger.info(" WebResponse === "+new String(webRes.getBody(), "UTF-8"));
	                        logger.trace("Exiting getResponse()");
	                        return webRes;
	                    }else{
	                        return null;
	                    }
	        }else{
	            return null;
	        }
	    } catch (HttpException httpe) {
	        httpe.printStackTrace();
	        logger.info("Http Exception");
	    } catch (IOException ioe) {
	        ioe.printStackTrace();
	        logger.info("IOException Exception");
	    }
		return null;
	    
}
}
