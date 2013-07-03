package pcs.basic;


public interface ICredentialService{
	public String getDeviceCode();
	public String getAccessToken(String device_code);
	public void savePcsUser(String deviceCode, String xAuthToken);
}
