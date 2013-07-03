package crazy;

import java.util.Map;

import com.hp.sbs.container.sip.SipRequest;

import pcs.basic.ICreator;
import pcs.basic.ICredentialService;

public class CredentialCreatorImpl implements ICreator<ICredentialService> {
	private CredentialServiceImpl service;
	public CredentialCreatorImpl(){
		service=new CredentialServiceImpl();
	}

	@Override
	public ICreator<ICredentialService> setBeanParameter(String name,
			Object value) {
		this.service.setSipReq((SipRequest)value);
		return this;
	}

	@Override
	public ICreator<ICredentialService> setBeanParameters(
			Map<String, Object> name_value_pairs) {
		return this;
	}

	@Override
	public ICredentialService create() {
		return service;
	}

}
