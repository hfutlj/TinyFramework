package tiny.framework.mvc;

import java.util.Map;

/**
 * 
 * @author lijun
 *
 */
public class View {
	
	private String path;
	private Map<String, String> paramMap;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Map<String, String> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}
	
	

}
