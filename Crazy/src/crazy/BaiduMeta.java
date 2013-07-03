package crazy;

public class BaiduMeta extends BaiduFile{

	private boolean ifhassubdir;
	private int filenum;
	public boolean isIfhassubdir() {
		return ifhassubdir;
	}
	public void setIfhassubdir(boolean ifhassubdir) {
		this.ifhassubdir = ifhassubdir;
	}
	public int getFilenum() {
		return filenum;
	}
	public void setFilenum(int filenum) {
		this.filenum = filenum;
	}
}
