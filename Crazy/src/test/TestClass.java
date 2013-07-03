package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

public class TestClass {
	public static String getFileContent(String filepath){
		StringBuffer sb = new StringBuffer();
		try {
			FileReader fr=new FileReader(filepath);
			BufferedReader br=new BufferedReader(fr);
			
			String s=null;
			while ((s=br.readLine())!=null) {
				sb.append(s+"\n");
			}
		} catch (FileNotFoundException e) {
			System.out.println("__________________");
			System.out.println(filepath+" not found");
		} catch (IOException e) {
			System.out.println("__________________");
			System.out.println(filepath+" IO error");
		}
		return sb.toString();
	}

}
