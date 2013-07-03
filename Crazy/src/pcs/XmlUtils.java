package pcs;

public class XmlUtils {
	public static String escapeURI(String uri){
		uri=uri.replaceAll("&", "&amp;");
		uri=uri.replaceAll("'", "&apos;");
		uri=uri.replaceAll("\"", "&quot;");
		uri=uri.replaceAll(">", "&gt;");
		uri=uri.replaceAll("<", "&lt;");
		return uri;
	}
	public static String unEscapeURI(String uri){
		uri=uri.replaceAll( "&amp;","&");
		uri=uri.replaceAll( "&apos;","'");
		uri=uri.replaceAll("&quot;","\"");
		uri=uri.replaceAll( "&gt;",">");
		uri=uri.replaceAll( "&lt;","<");
		return uri;
	}

}
