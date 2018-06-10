package tiny.framework.tools;


public class ClassUtil {
	
	public static boolean isNumber(Class<?> type) {
		if (type == null) {
			return false;
		}
		if (Number.class.isAssignableFrom(type)) {
			return true;
		}
		if (type.equals(int.class) || type.equals(double.class) || type.equals(long.class) || type.equals(float.class)
				|| type.equals(short.class) || type.equals(byte.class)) {
			return true;
		}
		return false;
	}
	
	/**
	 * ��type�����������򷵻ض�Ӧ�����֣�Ĭ��ֵ��0
	 * ���type�������������򷵻�null
	 * @param obj
	 * @param type
	 * @return
	 */
	public static Object getNumber(Object obj , Class<?> type) {
		if( type == null ) {
			return null;
		}
		if(byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)) {
			return ConvertUtil.convertByte(obj);
		}
		if(short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
			return ConvertUtil.convertShort(obj);
		}
		if(int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type) ){
			return ConvertUtil.convertInt(obj);
		}
		if(float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
			return ConvertUtil.convertFloat(obj);
		}
		if(double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
			return ConvertUtil.convertDouble(obj);
		}
		if(long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
			return ConvertUtil.convertLong(obj);
		}
		
		return null;
	}
	

}
