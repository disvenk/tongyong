package com.resto.brand.core.entity;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 实现自己的比较器
 */
public class MyComaparator implements Comparator<Object>, Serializable {
	@Override
	public int compare(Object obj1, Object obj2) {
		int num1 = (int) obj1;
		int num2 = (int) obj2;
		if(num1>num2){
			return -1;
		}else if(num1>num2){
			return 1;
		}else {
			return 0;
		}

	}
}
