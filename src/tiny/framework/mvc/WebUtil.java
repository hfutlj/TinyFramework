package tiny.framework.mvc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tiny.framework.constant.TinyConstant;

public class WebUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

	/**
	 * 重定向请求
	 */
	public static void redirectRequest(String path, HttpServletRequest request,
			HttpServletResponse response) {
		String rePath = request.getContextPath() + path ;
		try {
			response.sendRedirect(rePath);
		} catch (IOException e) {
			logger.error("tinyFramework:=====  an error found when redirect to {}" , rePath);
			throw new RuntimeException();
		}
	}
	
	// 返回Html页面
	public static void writeHtml( String path, HttpServletRequest request,HttpServletResponse response) {
		String pagePath = TinyConstant.WEBPATH + "/" + path + ".html";
		try {
			//request.getRequestDispatcher(path).forward(request, response);
			File page = new File(pagePath);
			FileReader fileReader = new FileReader(page);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = "";
 			while((line = reader.readLine())!=null) {
 				response.getWriter().write(line);
 			}
			response.getWriter().flush();
			fileReader.close();
			reader.close();
		} catch (Exception e) {
			logger.error("tinyFramework:=====  an error found on return html page: {} cause {}" , pagePath , e);
			e.printStackTrace();
		}
	}
	
	// 回写Json字符串
	public static void writeJson(HttpServletResponse response, String result) {
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 跳转至特殊页面
	 */
	public static void jumpToSpicialPage() {
		
	}
	
	/**
	 * 发送错误
	 */
	public static void sendError(int code , String message , HttpServletResponse response ) {
		try {
			response.sendError(code , message);
		} catch (IOException e) {
			logger.error("tinyFramework:=====  ,send error code fail {}", e);
            throw new RuntimeException(e);
		}
	}

}
