package tiny.framework.mvc;


import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;


/**
 * 视图解析器
 * @author lijun
 *
 */
public class ViewResolver {
	
	// 解析方法返回值
	public static void resolve(HttpServletRequest request ,HttpServletResponse response, Object result) {
		if(response == null) {
			return;
		}
		// 视图字段
		if(result instanceof View) {
			View view = (View) result;
			Map<String, String> params = view.getParamMap();
			for(Entry<String, String> entry : params.entrySet()) {
				request.setAttribute(entry.getKey(), entry.getValue());
			}
			WebUtil.writeHtml(view.getPath(), request, response);
		}
		//Json写回
		if(result instanceof JSON) {
			WebUtil.writeJson(response, (String)result);
		}
		
		// TODO 文件上传等处理
	}

}
