package tiny.framework.tools;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
	
	
	public static synchronized String getNowDateString() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

}
