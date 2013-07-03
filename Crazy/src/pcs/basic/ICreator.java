package pcs.basic;

import java.util.Map;

public interface ICreator<T> {
	public ICreator<T> setBeanParameter(String name,Object value);
	public ICreator<T> setBeanParameters(Map<String, Object> name_value_pairs);
	public T create();
}
