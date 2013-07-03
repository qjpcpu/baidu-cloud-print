package test;

import crazy.CrazyAppHelper;
import pcs.basic.ICredentialService;

public class TestCredentialServiceImpl implements ICredentialService {

	private static String device_code=null;
	private static String access_token=null;
	public TestCredentialServiceImpl(){}
	@Override
	public String getDeviceCode() {
		device_code="2a44a2ac93a311dd957363e33f533a51";
		//device_code="450725eb8dabc035caa7e660ce1da9e5";
		CrazyAppHelper.writeLine("get device code:[%s] from test credential service", device_code);
		return device_code;
	}

	@Override
	public String getAccessToken(String device_code) {
		access_token="3.7665f3693463d7c9328e07599940bc9d.2592000.1368601027.1610656893-621208";
		//access_token="3.dd02cf44167b44db11fd5669131a2604.2592000.1368601599.2284653471-621208";
		CrazyAppHelper.writeLine("get access token:[%s] from test credential service", access_token);
		return access_token;
	}

	@Override
	public void savePcsUser(String deviceCode, String xAuthToken) {
	    TestCredentialServiceImpl.device_code=deviceCode;
	    TestCredentialServiceImpl.access_token=xAuthToken;
	    CrazyAppHelper.writeLine("save deviceCode:%s xAuthToken:%s to test credential service", deviceCode,xAuthToken);
	}

}
