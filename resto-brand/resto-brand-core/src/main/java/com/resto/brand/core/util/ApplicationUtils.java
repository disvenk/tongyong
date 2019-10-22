package com.resto.brand.core.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ApplicationUtils : 程序工具类，提供大量的便捷方法
 *
 * @author StarZou
 * @since 2014-09-28 22:31
 */
public class ApplicationUtils {

	public static final String baseDomain="restoplus.cn";
	
    /**
     * 产生一个36个字符的UUID
     *
     * @return UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }
    
    /**
     * md5加密
     *
     * @param value 要加密的值
     * @return md5加密后的值
     */
    public static String md5Hex(String value) {
        return DigestUtils.md5Hex(value);
    }

    /**
     * sha1加密
     *
     * @param value 要加密的值
     * @return sha1加密后的值
     */
    public static String sha1Hex(String value) {
        return DigestUtils.sha1Hex(value);
    }

    /**
     * sha256加密
     *
     * @param value 要加密的值
     * @return sha256加密后的值
     */
    public static String sha256Hex(String value) {
        return DigestUtils.sha256Hex(value);
    }
    
    
    /**
     * 提取某个对象集合中的某个字段，并返回数组
     * @param objects 需要提取的对象集合
     * @param method  提取的方法 
     * @param valueType   提取出来的类型
     * @throws ReflectiveOperationException
     */
	@SuppressWarnings("unchecked")
	public static <T> List<T> ObjectsToFields(Collection<?> objects,String method, Class<T> valueType) throws ReflectiveOperationException{
		List<T> fields = new ArrayList<T>();
		for (Object obj : objects) {
			Method m = obj.getClass().getMethod(method);
			fields.add((T) m.invoke(obj));
		}
		return fields;
		
	}

	/**
	 * 将字段集合 转换成某个 对象集合
	 * @param <T>
	 * @param fields 字段集合
	 * @param method 要调用的方法
	 * @param objectType 要转换成的对象类型
	 * @throws ReflectiveOperationException
	 */
	public static <T>List<T> FieldsToObject(Collection<?> fields, String method,Class<T> objectType) throws ReflectiveOperationException{
		List<T> objects = new ArrayList<T>();
		for(Object fd :fields){
			T object = objectType.newInstance();
			Method m = object.getClass().getMethod(method,fd.getClass());
			m.invoke(object, fd);
			objects.add(object);
		}
		objects.remove(null);
		return objects;
	}

	/**
	 * 将对象转换为json格式数据
	 * @param result
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static String Object2Json(Object result) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper om = new ObjectMapper();
		return om.writeValueAsString(result);
	}

	/**
	 * 平台密码通用加密方法
	 * @param password
	 */
	public static String pwd(String password) {
		return sha1Hex(md5Hex(password));
	}

	public static <T,S>Map<T,S> convertCollectionToMap(Class<T> t, Collection<S> obj) {
		return convertCollectionToMap(t,obj,"getId");
	}
	
	public static <T,S>Map<T,S> convertCollectionToMap(Class<T> t, Collection<S> obj,	String methodName) {
		Map<T,S> map = new HashMap<>();
		for(S o:obj){
			Method m;
			try {
				m = o.getClass().getMethod(methodName);
				@SuppressWarnings("unchecked")
				T id = (T) m.invoke(o);
				map.put(id, o);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public static void main(String[] args){
		System.out.println(pwd("12345"));

	}
}
