package pcs;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.StringTokenizer;

public class SipsUtils {

	public static String escapeTemplatedURIComponent(String uri) {
		uri=replaceAll(uri,"/", ":");
		uri=replaceAll(uri, "?", ":");
		try {
			uri=URLEncoder.encode(uri, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return uri;
	}
	public static String unEscapeTemplatedURIComponent(String uri)  {
		try {
			uri=URLDecoder.decode(uri, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		uri=restoreAll(uri, ":", "?");
		uri=restoreAll(uri,":", "/");
		return uri;
	}
	public static String replaceAll(String url,String source,String replacement){
		return replaceAndEndnote(url, source, replacement, "(", ".");
	}
	public static String restoreAll(String url,String holder,String restore){
		return restoreWithEndnote(url, holder, restore, "(", ".");
	}
	public static String replaceAndEndnote(String text,String holderString,String replacement,String noteTag,String noteSplit){
		StringBuilder note=new StringBuilder();
		note.append(noteTag);

		String tmp=new String(text);
		int start=0;
		while ((start=tmp.indexOf(holderString,start))>=0) {
			note.append(start + noteSplit);
			start+=holderString.length();
		}
		start=note.length()-noteSplit.length();
		if(note.substring(start).equals(noteSplit))
			note.delete(start, note.length());
		
		return text.replace(holderString, replacement)+note;
	}
	
	public static String restoreWithEndnote(String text,String holderString,String replacement,String noteTag,String noteSplit){
		int start=text.lastIndexOf(noteTag);
		String note=text.substring(start);
		text=text.substring(0, start);
		if(note.length()==noteTag.length())
			return text;
		StringBuilder sb=new StringBuilder(text);
		StringTokenizer token=new StringTokenizer(note.substring(1),noteSplit);
		int[] index=new int[token.countTokens()];
		for(int i=index.length-1;i>=0;i--){
			index[i]=Integer.parseInt(token.nextToken());
		}

		int h_length=holderString.length();
		for(int i=0;i<index.length;i++){
			sb.replace(index[i], index[i]+h_length, replacement);
		}
		return sb.toString();
	}
	
}
