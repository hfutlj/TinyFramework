package tiny.framework.mvc;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tiny.framework.tools.DateUtil;

public class TFile {
	private static final Logger logger = LoggerFactory.getLogger(TFile.class);
	
	private String filedName;
	private String fileName;
	private long size;
	private String contentType;
	private InputStream in;
	
	private String savePath;

	public TFile(String filedName, String fileName, long size, String contentType, InputStream in) {
		super();
		this.fileName = fileName;
		this.filedName = filedName;
		this.size = size;
		this.contentType = contentType;
		this.in = in;
	}

	public void save(String path) {
		byte[] buff = new byte[4 * 1024];
		if(StringUtils.isEmpty(path)) {
			path = fileName+"_"+DateUtil.getNowDateString();
		}
		File file = new File(path);
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			while (in.read(buff) != -1) {
				out.write(buff);
			}
		} catch (IOException e) {
			logger.error("tinyFramework:=====  ,fail to save file , {} , cause {}" , path , e);
		} finally {
			if(out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("文件流关闭出错");
				}
			}
		}
	}

	@Override
	public String toString() {
		return "TFile [fileName=" + fileName + ", size=" + size + ", contentType=" + contentType + ", in=" + in
				+ ", savePath=" + savePath + "]";
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getFiledName() {
		return filedName;
	}

	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}

}
