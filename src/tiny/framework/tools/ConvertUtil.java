package tiny.framework.tools;

import java.lang.reflect.Array;

import org.apache.commons.lang.StringUtils;


/**
 * 类型转换
 * @author lee
 *
 */
public class ConvertUtil {

	/**
	 * 参数类型解析器
	 * @param sourceObj
	 * @param type
	 * @return
	 */
	public static Object convertParam(Object sourceObj, Class<?> targetType) {
		if (sourceObj == null || targetType == null) {
			return null;
		}
		Class<?> type = targetType;
		if (targetType.isArray()) {
			type = targetType.getComponentType();
		}
		// 为了通用性将sourceObj包装成数组解析
		Object array = sourceObj;
		if (!sourceObj.getClass().isArray()) {
			array = Array.newInstance(sourceObj.getClass(), 1);
		}

		Object res = Array.newInstance(type, Array.getLength(array));
		for (int i = 0; i < Array.getLength(array); i++) {
			//Array.set(res, i, null);
			Object element = Array.get(array, i);
			if (CharSequence.class.isAssignableFrom(type)) {
				// 字符串类型
				Array.set(res, i, convertString(element));
				continue;
			}
			if(ClassUtil.getNumber(element, type) != null) {
				// 数字类型
				Array.set(res, i, ClassUtil.getNumber(element, type));
				continue;
			}
//			if(TFile.class.isAssignableFrom(type)) {
//				// 文件类型
//				TFile tFile = null;
//				try {
//					tFile = (TFile)element;
//				} catch (ClassCastException e) {
//					tFile = null;
//				}
//				Array.set(res, i, tFile);
//				continue;
//			}
			// TODO JavaBean等类型
		}
		
		if(targetType.isArray()) {
			return res;
		}else {
			return Array.get(res, 0);
		}
		
	}
	
	public static long convertLong(Object obj) {
		return convertLongDef(obj, 0L);
	}

	public static long convertLongDef(Object obj, long defultValue) {
		String strValue = convertString(obj);
		long longValue = defultValue;
		if (StringUtils.isNotEmpty(strValue)) {
			try {
				longValue = Long.parseLong(strValue);
			} catch (NumberFormatException e) {
				longValue = defultValue;
			}
		}
		return longValue;
	}

	public static int convertInt(Object obj) {
		return convertIntDef(obj, 0);
	}

	public static int convertIntDef(Object obj, int defultValue) {
		String strValue = convertString(obj);
		int intValue = defultValue;
		if (StringUtils.isNotEmpty(strValue)) {
			try {
				intValue = Integer.parseInt(strValue);
			} catch (NumberFormatException e) {
				intValue = defultValue;
			}

		}
		return intValue;
	}

	public static double convertDouble(Object obj) {
		return convertDoubleDef(obj, 0D);
	}

	public static double convertDoubleDef(Object obj, double defValue) {
		String strValue = convertString(obj);
		double doubleValue = defValue;
		if (StringUtils.isNotEmpty(strValue)) {
			try {
				doubleValue = Double.parseDouble(strValue);
			} catch (NumberFormatException e) {
				doubleValue = defValue;
			}
		}
		return doubleValue;
	}

	public static byte convertByte(Object obj) {
		return convertByteDef(obj, (byte) 0);
	}

	public static byte convertByteDef(Object obj, byte defValue) {
		byte byteValue = defValue;
		String strValue = convertString(obj);
		if (StringUtils.isNotEmpty(strValue)) {
			try {
				byte b = Byte.parseByte(strValue);
				byteValue = b;
			} catch (NumberFormatException e) {
				byteValue = defValue;
			}
		}
		return byteValue;
	}
	
	public static short convertShort(Object obj) {
		return convertShortDef(obj, (short) 0);
	}
	
	public static short convertShortDef(Object obj , short defValue) {
		short shortValue = defValue;
		String StrValue = convertString(obj);
		if(StringUtils.isNotEmpty(StrValue)) {
			try {
				short s = Short.parseShort(StrValue);
				shortValue = s;
			} catch (NumberFormatException e) {
				shortValue = defValue;
			}
		}
		return shortValue;
	}
	
	public static float convertFloat(Object obj) {
		return convertFloatDef(obj, 0f);
	}
	
	public static float convertFloatDef(Object obj , float defValue) {
		float floatValue = defValue;
		String strValue = convertString(obj);
		if(StringUtils.isNotBlank(strValue)) {
			try {
				float f = Float.parseFloat(strValue);
				floatValue = f;
			} catch (NumberFormatException e) {
				floatValue = defValue;
			}
		}
		return floatValue;
	}

	/**
	 * 返回obj的string形式，如果obj为null返回空字符串
	 */
	public static String convertString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

}
