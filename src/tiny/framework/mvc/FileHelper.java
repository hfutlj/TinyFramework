package tiny.framework.mvc;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tiny.framework.constant.TinyConstant;
import tiny.framework.core.TinyHandlerInvoker;
import tiny.framework.mvc.TinyHandler;
import tiny.framework.tools.ConvertUtil;

public class FileHelper {
	private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);

//	private static ServletFileUpload fileUpload;

//	@SuppressWarnings("unchecked")
//	public static List<Object> getMultipartParam(HttpServletRequest request) {
//		// 文件参数
//		List<TFile> fileList = new ArrayList<>();
//		// 普通参数
//		Map<String, String> normalParam = new HashMap<>();
//
//		List<Object> params = new ArrayList<>();
//		fileUpload = new ServletFileUpload(new DiskFileItemFactory());
//		try {
//			List<FileItem> fileItems = fileUpload.parseRequest(request);
//			logger.debug("request转文件参数为:{}", params);
//			for (FileItem fileItem : fileItems) {
//				String name = fileItem.getFieldName();
//				if (fileItem.isFormField()) {
//					normalParam.put(name, fileItem.getString(TinyConstant.def_encode));
//				} else { // 文件上传
//					String filedName = fileItem.getFieldName();
//					String fileName = fileItem.getName();
//					String contentType = fileItem.getContentType();
//					long size = fileItem.getSize();
//					InputStream in = fileItem.getInputStream();
//					TFile tFile = new TFile(filedName ,fileName, size, contentType, in);
//					logger.debug("文件上传的文件类型参数为：", tFile);
//					fileList.add(tFile);
//				}
//			}
//		} catch (Exception e) {
//			logger.error("文件参数获取出错： {}", e);
//			e.printStackTrace();
//		} finally {
//			params.add(fileList.get(0));
//		}
//
//		return params;
//	}

	/**
	 * 获取Multipart类型的post表单参数列表
	 * 
	 * @param request
	 * @param handler
	 * @return
	 */
	public static List<Object> getMultipartParamList(HttpServletRequest request, TinyHandler handler) {
		List<Object> paramList = new ArrayList<>();
		if (!"POST".equalsIgnoreCase(request.getMethod()) || !ServletFileUpload.isMultipartContent(request)) {
			logger.debug("tinyFramework:=====  ,only support multipart type post file; {}" , request.getRequestURI());
			//logger.debug("文件上传的表单类型必须为multipart型的post提交");
			return null;
		}
		
		try {
			ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
			@SuppressWarnings("unchecked")
			List<FileItem> fileItems = servletFileUpload.parseRequest(request);
			Map<String, Class<?>> nameTypeMap = TinyHandlerInvoker.getNameTypeMap(handler);
			// 获取Multipart类型表单的参数列表
			Map<String, List<Object>> paramsMap = new HashMap<>();
			for (FileItem fileItem : fileItems) {
				if (fileItem.isFormField()) {
					// 普通类型
					String name = fileItem.getFieldName();
					String value = fileItem.getString(TinyConstant.DEF_ENCODING);
					if (paramsMap.get(name) == null) {
						List<Object> list = new ArrayList<>();
						list.add(value);
						paramsMap.put(name, list);
					} else {
						paramsMap.get(name).add(value);
					}
				} else {
					// 文件类型
					String name = fileItem.getFieldName();
					TFile tFile = new TFile(fileItem.getFieldName(),fileItem.getName(),  fileItem.getSize(),
							fileItem.getContentType(), fileItem.getInputStream());
					if(paramsMap.get(name) == null) {
						List<Object> list = new ArrayList<>();
						list.add(tFile);
						paramsMap.put(name, list);
					}else {
						paramsMap.get(name).add(tFile);
					}
				}
			}
			logger.info("multipart参数映射{}",paramsMap);
			logger.info("形参映射{}" , nameTypeMap);
			// 参数解析
			for(Entry<String, Class<?>> entry : nameTypeMap.entrySet()) {
				String name = entry.getKey();
				Class<?> type = entry.getValue();
				Object value = null;
				if(paramsMap.get(name)!=null) {
					value = paramsMap.get(name).toArray();
				}
				paramList.add(ConvertUtil.convertParam(value, type));
			}
			
		} catch (FileUploadException | IOException e) {
			e.printStackTrace();
		}
		return paramList;

	}

}
