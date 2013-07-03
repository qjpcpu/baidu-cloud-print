package crazy;

public class BaiduFile {

	private String fileId;
	private String filePath;
	private String createTime;
	private String modifyTime;
	private String md5;
	private long size;
	private boolean isDirectory;
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public boolean isDirectory() {
		return isDirectory;
	}
	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	public String getFileName(){
		int i=filePath.lastIndexOf("/");
		if(i>=0&&(i+1)<filePath.length())
			return filePath.substring(i+1);
		return filePath;
	}
	
}
