package test;

import java.util.Map;

import pcs.basic.ICreator;
import pcs.basic.ICredentialService;

public class TestCredentialCreatorImpl implements ICreator<ICredentialService> {

	private TestCredentialServiceImpl service;
	public TestCredentialCreatorImpl(){
		service=new TestCredentialServiceImpl();
	}
	@Override
	public ICreator<ICredentialService> setBeanParameter(String name,
			Object value) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public ICreator<ICredentialService> setBeanParameters(
			Map<String, Object> name_value_pairs) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public ICredentialService create() {
		// TODO Auto-generated method stub
		return service;
	}

}
