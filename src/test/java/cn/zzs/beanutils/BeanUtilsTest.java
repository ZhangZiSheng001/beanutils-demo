package cn.zzs.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.junit.Test;

/**
 * @ClassName: BeanUtilsTest
 * @Description: 测试BeanUtils
 * @author: zzs
 * @date: 2019年9月1日 下午1:26:47
 */
public class BeanUtilsTest {
	/**
	 * 使用BeanUtils模拟表单提交数据封装成一个对象
	 */
	@Test
	public void test01() throws IllegalAccessException, InvocationTargetException {
		//如果是用户提交的表单，可以从request中获取，这里模拟下。
	    //Map<String, String[]> map = request.getParameterMap();
		Map<String, String> map =  new HashMap<String, String>();
		map.put("id","9527");
		map.put("name","zzs001");
		map.put("age","18");
		map.put("gmt_create","1999-01-01");
		map.put("gmt_modified","2017-01-01");
		//创建一个封装对象
	    User user = new User();
	    //因为date是引用类型，注册类型转换器
	    ConvertUtils.register(new DateLocaleConverter(), Date.class);
    	//封装User对象
        BeanUtils.populate(user, map);
	    System.out.println(user);
	}
	
	/**
	 * 使用自定义的MyBeanUtils模拟表单提交数据封装成一个对象
	 */
	@Test
	public void test02() throws IllegalAccessException, InvocationTargetException {
		//如果是用户提交的表单，可以从request中获取，这里模拟下。
	    //Map<String, String[]> map = request.getParameterMap();
		Map<String, String> map =  new HashMap<String, String>();
		map.put("id","9527");
		map.put("name","zzs001");
		map.put("age","18");
		map.put("gmt_create","1999-01-01");
		map.put("gmt_modified","2017-01-01");
		//创建一个封装对象
	    User user = new User();
	    //因为date是引用类型，注册类型转换器
	    ConvertUtils.register(new DateLocaleConverter(), Date.class);
    	//封装User对象
        MyBeanUtils.populate(user, map);
	    System.out.println(user);
	}	
}
