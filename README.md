
# BeanUtils

## 简介  
`BeanUtils`用于把对象的属性数据封装到对象中（数据来源一般是表单、xml或properties文件）。  

注意，Bean类必须具备`getter`和`setter`方法。另外，`BeanUtils`重载的方法不多，所有很多对`name`和`value`的判断和转换，比较低效。  

补充：在明确name和value的情况下，可以直接调用`PropertyUtils`和`ConvertUtils`进行类型转换和属性注入。

## 使用例子
### 需求
使用BeanUtils模拟将用户提交的表单封装为对象，并自定义MyBeanUtils来解决BeanUtils封装表单低效的问题。  

### 工程环境
JDK：1.8.0_201  
maven：3.6.1  
IDE：Spring Tool Suites4 for Eclipse  

### 主要步骤
只要调用BeanUtils的api就可以完成对象封装：
1. `populate(bean, map)`：将整个表单封装到指定对象；
2. `setProperty(bean, name, value)`：将键值对封装到指定对象；


### 创建项目
项目类型Maven Project，打包方式jar

### 引入依赖
```xml
<dependency>
	<groupId>junit</groupId>
	<artifactId>junit</artifactId>
	<version>4.12</version>
	<scope>test</scope>
</dependency>
<!-- BeanUtils -->
<dependency>
	<groupId>commons-beanutils</groupId>
	<artifactId>commons-beanutils</artifactId>
	<version>1.9.3</version>
</dependency>
```
编写BeanUtilsTest
路径：`cn.zzs.beanutils`
```java
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
```

### BeanUtils的不足和解决办法
#### BeanUtils的方法概况
一般常用方法就前四个。
```java
//给bean对象的name属性赋值value  
public static void setProperty(final Object bean, final String name, final Object value)  
//和setProperty基本一样  
public static void copyProperty(final Object bean, final String name, final Object value)  
//将map中存放的键值对属性数据封装给bean对象  
public static void populate(final Object bean, final Map<String, ? extends Object> properties)  
//将orig类的属性值封装到dest对象  
public static void copyProperties(final Object dest, final Object orig)  
//将bean对象的属性转化为键值对存入map  
public static Map<String, String> describe(final Object bean)  
//获得bean对象指定属性的值  
public static String getProperty(final Object bean, final String name)  
public static String[] getArrayProperty(final Object bean, final String name)    
//获得数组或集合的属性的元素的值，name的格式为：propert[index]  
public static String getIndexedProperty(final Object bean, final String name)  
//获得Map的属性的元素的值,name的格式为：propert(key)  
public static String getMappedProperty(final Object bean, final String name)
```

#### BeanUtils的不足分析
通过源码可以看到，以`setProperties`为例，本质上是利用`ConvertUtils`将传入的数据进行转换，再利用`PropertyUtils`调用对象的`setter`方法进行属性注入。  

另外，`BeanUtils`重载的方法不多，参数列表中`value`是`Object`，当然，这是为了通用性考虑的。但是，通过源码可以发现，传入的`name`需要判断属于哪种格式，`value`需要判断属于哪种类型，而且还有繁琐的类型转换等，所以比较低效。  

而实际上我们一般传入的表单中，`name`一般为`simple`格式，`value`为`String`类型。并不需要那么多的判断和转换工作。  

#### BeanUtils不足的解决办法
在明确`name`和`value`的情况下，可以直接调用`PropertyUtils`和`ConvertUtils`进行属性注入。下面是自定义的`MyBeanUtils`。 这个时候再次封装表单就会高效很多。  

`PropertyUtils`和`ConvertUtils`分别用于属性注入和类型转换，是很不错的工具，实际工作中可以使用，避免重复造轮子。
```java
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
```
#### ConvertUtils的介绍
`ConvertUtils`用于将传入的对象转换为指定类型，而且还可以注册自定义转换器。常用方法如下：
```java
//将value转换为String  
public static String convert(Object value)  
//将字符串转换为clazz类实例  
public static Object convert(String value, Class clazz)  
//将字符串数组转换为clazz类实例  
public static Object convert(String[] values, Class clazz)  
//将value转换为targetType类实例  
public static Object convert(Object value, Class targetType)      
//注册转换器，可传入自定义转换器  
public static void register(Converter converter, Class clazz)  
//删除转换器  
public static void deregister(Class clazz)  
```
#### PropertyUtils的介绍
`Properties`用于获取`bean`的属性的描述对象、值、`setter/getter`方法和类型，以及设置属性的值。常用方法如下：
```java
//获得bean中指定属性的描述对象  
public static PropertyDescriptor getPropertyDescriptor(Object bean,String name)  
//获得bean中所有属性的描述对象  
public static PropertyDescriptor[] getPropertyDescriptors(Class beanClass)  
public static PropertyDescriptor[] getPropertyDescriptors(Object bean)  
//获得bean中指定属性的类型              
public static Class getPropertyType(Object bean, String name)  
//获得指定属性的getter和setter方法  
public static Method getReadMethod(PropertyDescriptor descriptor)  
public static Method getWriteMethod(PropertyDescriptor descriptor)    
//赋值orig的属性到dest  
public static void copyProperties(Object dest, Object orig)  
//将bean的属性转换为键值对数据的Map  
public static Map describe(Object bean)  
//获取bean的指定属性的值  
public static Object getIndexedProperty(Object bean, String name)  
public static Object getMappedProperty(Object bean, String name)  
public static Object getProperty(Object bean, String name)  
public static Object getSimpleProperty(Object bean, String name)  
//设置bean的指定属性的值  
public static void setIndexedProperty(Object bean, String name, Object value)  
public static void setMappedProperty(Object bean, String name, Object value)
public static void setProperty(Object bean, String name, Object value)  
public static void setSimpleProperty(Object bean, String name, Object value)      
```

> 学习使我快乐！！
