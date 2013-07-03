package crazy;

import java.util.Map;

import com.hp.sbs.container.platform.service.coordination.exception.CoordinationClientException;
import com.hp.sbs.container.platform.service.loadbalancer.coordination.ICoordinationSession;

import pcs.basic.ICreator;
import pcs.basic.ISessionService;

public class SessionCreatorImpl implements ICreator<ISessionService> {

	public static String SIP_ID="sip_id";
	public static String CLOUD_ID="cloud_id";
	public static String TIME_OUT="time_out";
	public static String VERSION="version";
	public static String ENCRYPT="encrypt";
	public static String SESSION_ID="session_id";
	
	private SessionServiceImpl service;
	public SessionCreatorImpl(){
		this.service=new SessionServiceImpl();
	}
	@Override
	public ICreator<ISessionService> setBeanParameter(String name, Object value) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public ICreator<ISessionService> setBeanParameters(
			Map<String, Object> name_value_pairs) {
		Object sipId=name_value_pairs.get(SIP_ID);
		Object cloudId=name_value_pairs.get(CLOUD_ID);
		Object timeOut=name_value_pairs.get(TIME_OUT);
		Object version=name_value_pairs.get(VERSION);
		Object encrypt=name_value_pairs.get(ENCRYPT);
		Object session_id=name_value_pairs.get(SESSION_ID);
		try {
			if(name_value_pairs.size()==5){
				ICoordinationSession session = this.service.getCoordinationSessionService().createSession((String)sipId, (String)cloudId,
						(Integer)timeOut, false, (Boolean)encrypt);
				this.service.setSession(session);
				this.service.setSessionId(session.getId());
			}
			else if(name_value_pairs.size()==3){
				ICoordinationSession session = this.service.getCoordinationSessionService().getSession((String)session_id,
						(Boolean)version, (Boolean)encrypt);
				this.service.setSession(session);
				this.service.setSessionId(session.getId());
			}
		} catch (CoordinationClientException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public ISessionService create() {
		// TODO Auto-generated method stub
		return service;
	}

}
