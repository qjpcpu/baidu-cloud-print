package test;

import java.util.Map;

import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;

import crazy.SessionServiceImpl;

import pcs.basic.ICreator;
import pcs.basic.ISessionService;

public class TestSessionCreatorImpl implements
		ICreator<ISessionService> {

	private TestSessionServiceImpl service;
	public TestSessionCreatorImpl(){
		this.service=new TestSessionServiceImpl();
	}
	@Override
	public ICreator<ISessionService> setBeanParameter(String name, Object value) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public ICreator<ISessionService> setBeanParameters(
			Map<String, Object> name_value_pairs) {
		for(Map.Entry<String, Object> pair: name_value_pairs.entrySet()	){
			service.storeValue(pair.getKey(), pair.getValue());
		}
		return this;
	}

	@Override
	public ISessionService create() {
		// TODO Auto-generated method stub
		return service;
	}

}
