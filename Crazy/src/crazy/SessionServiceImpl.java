package crazy;

import java.util.Map;

import pcs.basic.ISessionService;

import com.hp.sbs.commons.logger.ILogger;
import com.hp.sbs.commons.logger.LoggerFactory;
import com.hp.sbs.container.platform.service.coordination.ICoordinationSessionService;
import com.hp.sbs.container.platform.service.coordination.exception.CoordinationClientException;
import com.hp.sbs.container.platform.service.loadbalancer.coordination.ICoordinationData;
import com.hp.sbs.container.platform.service.loadbalancer.coordination.ICoordinationSession;
import com.hp.sbs.container.sip.SIPServiceFinder;

public class SessionServiceImpl implements ISessionService{

	protected static ILogger logger = LoggerFactory
			.getLogger(SessionServiceImpl.class.getPackage().getName());
	ICoordinationSession session;
	ICoordinationData coordData;
	String sessionId;

	public ICoordinationSession getSession() {
		return session;
	}

	public void setSession(ICoordinationSession session) {
		this.session = session;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public static String SIP_ID="sip_id";
	public static String CLOUD_ID="cloud_id";
	public static String TIME_OUT="time_out";
	public static String VERSION="version";
	public static String ENCRYPT="encrypt";
	public static String SESSION_ID="session_id";

	public SessionServiceImpl(){}

	/**
	 * Retrieve Session Id
	 * 
	 * @return session Id
	 */
	public String getSessionId() {
		return sessionId;
	}

	public ICoordinationSessionService getCoordinationSessionService() {
		return (ICoordinationSessionService) SIPServiceFinder
				.findService(ICoordinationSessionService.class.getName());
	}

	public void storeValue(String key, Object value) {
		try {
			coordData = session.getData(key);
			coordData.putValue(value);
		} catch (CoordinationClientException e) {
			logger.error(
					"CoordinationClientException",
					"CoordinationClientException exception occured while storing authToken",
					e);
		}
	}

	public Object retreiveValue(String key) {
		try {
			coordData = session.getData(key);
			return coordData.getValue();
		} catch (CoordinationClientException e) {
			logger.error(
					"CoordinationClientException",
					"CoordinationClientException exception occured while retrieving AUTH_TOKEN",
					e);
			return null;
		}

	}

	

	
}
