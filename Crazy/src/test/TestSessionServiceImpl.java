package test;

import java.util.HashMap;
import java.util.Map;

import crazy.CrazyAppHelper;

import pcs.basic.ISessionService;

public class TestSessionServiceImpl implements ISessionService {

	private static String session_id="this_is_test_session_id";
	private static Map<String, Object> sessionMap=new HashMap<String, Object>();
	
	@Override
	public String getSessionId() {
		// TODO Auto-generated method stub
		return session_id;
	}

	@Override
	public void storeValue(String key, Object value) {
		CrazyAppHelper.writeLine("put [%s:%s] to test session", key,value);
		sessionMap.put(key, value);
	}

	@Override
	public Object retreiveValue(String key) {
		CrazyAppHelper.writeLine("get [%s:%s] from test session", key,sessionMap.get(key));
		return sessionMap.get(key);
	}

}
