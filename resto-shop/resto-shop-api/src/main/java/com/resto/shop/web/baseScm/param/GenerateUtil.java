package com.resto.shop.web.baseScm.param;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 * User: 心远
 * Date: 14/12/8
 * Time: 下午3:29
 */
public class GenerateUtil {

    public static List<Object> generateObjectListFromResultSet(Class clazz, Map<String, String> columnNames, ResultSet resultSet) {
        List<Object> list = new LinkedList<Object>();
        Field[] fields = clazz.getDeclaredFields();
        if (columnNames == null || columnNames.size() == 0) {
            return list;
        }
        try {
            while (resultSet.next()) {
                Object ob = clazz.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    String fieldName = fields[i].getName().replace("$cglib_prop_", "");
                    String columnName = columnNames.get(fieldName);
                    String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method setMethod = clazz.getDeclaredMethod(setMethodName, new Class[]{fields[i].getType()});
                    setMethod.invoke(ob, resultSet.getObject(columnName));
                }

                list.add(ob);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
