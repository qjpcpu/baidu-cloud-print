package crazy;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BaiduJSON {
	public static final String LIST="list";
	public static final String FS_ID="fs_id";
	public static final String PATH="path";
	public static final String CTIME="ctime";
	public static final String MTIME="mtime";
	public static final String MD5="md5";
	public static final String SIZE="size";
	public static final String IS_DIR="isdir";
	public static final String IF_HAS_SUB_DIR="ifhassubdir";
	public static final String FILE_NUM="filenum";
	
	public static final String ERROR_CODE="error_code";
	public static final String ERROR_MSG="error_msg";
	public static final String REQUEST_ID="request_id";
	
	public static List<BaiduFile> listFiles(String jsonContent) throws JSONException{
		JSONObject root=new JSONObject(jsonContent);
		JSONArray jsArray=root.getJSONArray(LIST);
		List<BaiduFile> files=new ArrayList<BaiduFile>();
		for(int i=0;i<jsArray.length();i++){
			JSONObject tmp=(JSONObject) jsArray.get(i);
			BaiduFile file=new BaiduFile();
			file.setFileId(tmp.getString(FS_ID));
			file.setFilePath(tmp.getString(PATH));
			file.setCreateTime(tmp.getString(CTIME));
			file.setModifyTime(tmp.getString(MTIME));
			file.setMd5(tmp.getString(MD5));
			file.setSize(Long.parseLong(tmp.getString(SIZE)));
			file.setDirectory(tmp.getString(IS_DIR).equals("1"));
			files.add(file);
		}
		return files;
		
	}
	public static BaiduMeta getMeta(String jsonContent)throws JSONException{
		JSONObject root=new JSONObject(jsonContent);
		JSONObject tmp=(JSONObject)root.getJSONArray(LIST).get(0);
		BaiduMeta file=new BaiduMeta();
		file.setFileId(tmp.getString(FS_ID));
		file.setFilePath(tmp.getString(PATH));
		file.setCreateTime(tmp.getString(CTIME));
		file.setModifyTime(tmp.getString(MTIME));
		file.setSize(Long.parseLong(tmp.getString(SIZE)));
		file.setDirectory(tmp.getString(IS_DIR).equals("1"));
		file.setIfhassubdir(tmp.getString(IF_HAS_SUB_DIR).equals("1"));
		file.setFilenum(Integer.parseInt(tmp.getString(FILE_NUM)));
		return file;
	}
	public static BaiduError getError(String jsonContent) throws JSONException{
		JSONObject root=new JSONObject(jsonContent);
		BaiduError error=new BaiduError();
		error.setErrorCode(root.getString(ERROR_CODE));
		error.setErrorMsg(root.getString(ERROR_MSG));
		error.setRequestId(root.getString(REQUEST_ID));
		return error;
	}

}
