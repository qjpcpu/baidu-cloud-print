package pcs.basic;

public interface ISessionService{

	public String getSessionId();
	public void storeValue(String key, Object value);
	public Object retreiveValue(String key);
}
