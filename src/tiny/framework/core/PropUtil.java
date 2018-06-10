package tiny.framework.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PropUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropUtil.class);
	private static final Properties prop = new Properties();
	static {
		InputStream is = null;
		try {
			String propPath = "application.properties";
			if (!propPath.endsWith(".properties")) {
				propPath += ".properties";
			}
			//TODO 此代码在web项目下使用时的路径，需待修改
			String webPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			if(webPath.endsWith("/")) {
				webPath+="/";
			}
			logger.debug("tinyFramework:===== config file path = : " + webPath + propPath);
			File propFile = new File(webPath + propPath);
			is = new FileInputStream(propFile);
			prop.load(is);
		} catch (Exception e) {
			logger.error("tinyFramework:===== fail load config file : ", e);
			//System.out.println(e);
			throw new RuntimeException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getField(String key) {
		return prop.getProperty(key, "");
	}

	public static String getField(String key , String defaultValue) {
		return prop.getProperty(key, defaultValue);
	}
}
