package cn.zzs.beanutils;

import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * @ClassName: MyBeanUtils
 * @Description: 用于将表单提交的数据封装为对象
 * @author: zzs
 * @date: 2019年9月1日 下午1:40:00
 */
public class MyBeanUtils {
	/**
	 * @Title: setProperty
	 * @Description: 将指定值封装到目标类的对应字段中
	 * @author: zzs
	 * @date: 2019年9月1日 下午1:44:08
	 * @param target
	 * @param propertyName
	 * @param value
	 * @return: Object
	 */
	private static Object setProperty(Object target,String propertyName,String value) {
		PropertyDescriptor descriptor = null;
		try {
			//获得属性的描述对象
			descriptor =PropertyUtils.getPropertyDescriptor(target, propertyName);
			//获得属性的类型
			Class<?> type = descriptor.getPropertyType();
			//将传入的字符串value转换为type属性
			Object newValue = ConvertUtils.convert(value, type);
			//给target类propertyName属性注入值
			PropertyUtils.setSimpleProperty(target, propertyName, newValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}
	
    /**
     * 
     * @Title: populate
     * @Description: 用于将表单数据封装进指定对象
     * @author: zzs
     * @date: 2019年9月1日 下午1:43:28
     * @param target
     * @param valueMap
     * @return: Object
     */
	public static Object populate(Object target,Map<String, String> valueMap) {
		for(Entry<String, String> entry : valueMap.entrySet()) {
			setProperty(target,entry.getKey(),entry.getValue());
	    }	
		return target;
	}
}
