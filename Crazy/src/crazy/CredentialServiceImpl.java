package crazy;

import java.util.Iterator;
import java.util.Map;

import com.hp.sbs.commons.logger.ILogger;
import com.hp.sbs.commons.logger.LoggerFactory;
import com.hp.sbs.commons.sbsconfig.Config;
import com.hp.sbs.container.platform.service.credential.ICredentialManagementService;
import com.hp.sbs.container.platform.service.credential.exception.CMSClientException;
import com.hp.sbs.container.sip.SIPServiceFinder;
import com.hp.sbs.container.sip.SipRequest;

import pcs.basic.ICredentialService;

public class CredentialServiceImpl implements ICredentialService {

	protected static ILogger logger = LoggerFactory.getLogger(CredentialServiceImpl.class.getPackage().getName());

	static final String SECRET_DEFAULT = "1127bd7a6e76102d92b012313d008df2";
	
	public CredentialServiceImpl(){}
	SipRequest sipReq;
	public void setSipReq(SipRequest sipReq) {
		this.sipReq = sipReq;
	}
	@Override
	public String getDeviceCode() {
		logger.trace("Entering isUserRemembered");
		ICredentialManagementService cmsService = null;
		String cloudId = sipReq.getCloudId();
		if(cloudId==null||cloudId.equals("null")||cloudId.equals("123")){
			cloudId=CrazyConst.CLOUD_ID;
		}
		String secret = Config.Security.getePrintOauthConsumerToken();
		if(secret==null){ 
			secret = SECRET_DEFAULT; 
		}
		String sipId = sipReq.getTemplParameter(CrazyConst.SIP);
		logger.debug("isUserRemembered() sip id:"+sipId+" "+"clouid:"+cloudId+" "+"secret:"+secret);
		
		cmsService=getCredentialManagementService();
		try
		{
			java.util.List<String> users = cmsService.getUserList(sipId, cloudId,secret);
			Iterator<String> userIter = users.iterator();
			logger.debug("List of registered users fetched in getDeviceCode:"+users.toString());
			String userName = "";
			if(userIter.hasNext() && !users.isEmpty() && users.size() != 0 && users!= null){
				if((userName=(String)userIter.next())!=null){
					logger.debug("Determines whether any remembered user is available or not and get the only remembered user (last saved user):"+userName);
					logger.trace("Exiting getDeviceCode");
					return userName;
				}else{
					logger.trace("Exiting getDeviceCode");
					return null;
				}
			}else{
				logger.trace("Exiting isUserRemembered");
				return null;
			}
		}catch (CMSClientException cmsce){
			logger.error(
					"CMSClientException",
					"CMSClientException exception occured in isUserRemembered",	cmsce);
			return null;
		}
	}

	@Override
	public String getAccessToken(String device_code) {
		logger.trace("Entering getLoginPassword");
		String sipId = sipReq.getTemplParameter(CrazyConst.SIP);
		String cloudId = sipReq.getCloudId();
		ICredentialManagementService cmsService = null;
		String secret = Config.Security.getePrintOauthConsumerToken();
			if(secret==null){ 
				secret = SECRET_DEFAULT; 
			}
		if(cloudId==null||cloudId.equals("null")||cloudId.equals("123")){
			cloudId=CrazyConst.CLOUD_ID;
		}
		cmsService=getCredentialManagementService();
		String loginPassword = null;
		try {
			loginPassword = cmsService.getPasswordForUser(sipId, cloudId, device_code,secret);
		} catch (CMSClientException cmsce) {
			logger.error(
					"CMSClientException",
					"CMSClientException exception occured while deleting credentials in CMS", cmsce);
			return null;
		}
		logger.trace("Exiting getLoginPassword");
		return loginPassword;
	}

	@Override
	public void savePcsUser(String deviceCode, String xAuthToken) {
		logger.trace("Entering saveUser");
		ICredentialManagementService cmsService = null;
		String secret = Config.Security.getePrintOauthConsumerToken();
		if(secret==null){ 
			secret = SECRET_DEFAULT; 
		}
		String sipId = sipReq.getTemplParameter(CrazyConst.SIP);
		
		logger.trace("secret::"+secret);
		logger.trace("sipId::"+sipId);
		
		cmsService=getCredentialManagementService();
		
		String cloudId = sipReq.getCloudId();
		if(cloudId==null||cloudId.equals("null")||cloudId.equals("123")){
			cloudId=CrazyConst.CLOUD_ID;
		}
		logger.trace("cms cloudId::"+cloudId);
		String uniqueId = sipReq.getUniqueId();
		
		logger.debug("Unique id login:"+uniqueId);
		logger.debug( "User name ::" + deviceCode);
		
		try{
			logger.debug("CMS new save logic...");
			/* ****Storing data in credential management **** */
			logger.debug("Saving data to CMS....");
			logger.debug("User listing in Login (true, false) before :"+cmsService.getUserList(sipId, cloudId,secret));
			if(xAuthToken==null){
				logger.debug("Password not stored for sipId "+ sipId +" CloudId "+cloudId);
				cmsService.saveCredentials(sipId, cloudId,deviceCode,secret);
			}else{
				logger.debug("Email and Password are stored for sipId "+ sipId +" CloudId "+cloudId);
			cmsService.saveCredentials(sipId, cloudId,deviceCode,xAuthToken,secret);
			}
			logger.debug("User listing in saveUser:"+cmsService.getUserList(sipId, cloudId,secret));
		}catch (CMSClientException cmsce){
			logger.error(
					"CMSClientException",
					"CMSClientException exception occured while storing credentials in CMS",	cmsce);
		}catch (Exception e){
			logger.error(
					"Exception",
					"Unknown Exception exception occured while storing user data in Login",	e);
		}
		logger.trace("Exiting saveUser");

	}
	
	private static ICredentialManagementService getCredentialManagementService() {		
    	return (ICredentialManagementService)SIPServiceFinder.findService(ICredentialManagementService.class.getName());
    }
	
	

}
