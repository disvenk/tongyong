package com.resto.shop.web.baseScm.helper;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/9/2 0002.
 */
public class GenerateJavaBeanOfTable implements Serializable {

    private static  Connection connection;
    private static PreparedStatement UserQuery;
    /*mysql url的连接字符串*/
//    private static String url = "jdbc:mysql://rds64fw2qrd8q0eg95nmo.mysql.rds.aliyuncs.com:3306/shop_manager?useUnicode=true&characterEncoding=UTF-8";
    private static String url = "jdbc:mysql://139.196.222.42:3306/shop_manager?useUnicode=true&characterEncoding=UTF-8";
    //账号
    private static String user = "resto";
    //密码
    private static String password = "restoplus";
    private Vector<String> vector = new Vector<String>();
    //mysql jdbc的java包驱动字符串
    private static String driverClassName = "com.mysql.jdbc.Driver";



    private static ConcurrentHashMap<String,String> cMap =new ConcurrentHashMap<String, String>();


    static {
        cMap.put("BigDecimal","import java.math.BigDecimal;");
        cMap.put("Date","import java.util.Date;");
        cMap.put("String","import java.lang.String;");
        cMap.put("Long","import java.lang.Long;");
        cMap.put("Integer","import java.lang.Integer;");
        cMap.put("Float","import  java.lang.Float;");
        cMap.put("Double","import java.lang.Double;");
        cMap.put("Boolean","import java.lang.Boolean;");
    }

    static {
        try {//驱动注册
            Class.forName(driverClassName);
            if (connection == null || connection.isClosed())
                //获得链接
                connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Oh,not");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Oh,not");
        }
    }

    public  GenerateJavaBeanOfTable(){

    }

    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        GenerateJavaBeanOfTable.connection = connection;
    }

    public static Map<String,Object> doAction(String table){
        String sql = "select * from "+table;

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            //获取数据库的元数据
            ResultSetMetaData metadata = statement.getMetaData();
            //数据库的字段个数
            int len = metadata.getColumnCount();
            //字段名称
            String[] colnames = new String[len+1];
            //字段类型 --->已经转化为java中的类名称了
            List<String> colTypes = new ArrayList<String>();
            for(int i= 1;i<=len;i++){
                colnames[i] = metadata.getColumnName(i); //获取字段名称
                colTypes.add(i-1,sqlType2JavaType(metadata.getColumnTypeName(i))); //获取字段类型
            }

            Map<String,Object> returnMap = new HashMap<String, Object>();
            returnMap.put("colnames",colnames);
            returnMap.put("colTypes",colTypes);
            return returnMap;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * mysql的字段类型转化为java的类型*/
    private static  String sqlType2JavaType(String sqlType) {

        if(sqlType.equalsIgnoreCase("bit")){
            return "Integer";
        }else if(sqlType.equalsIgnoreCase("tinyint")){
            return "Integer";
        }else if(sqlType.equalsIgnoreCase("tinyint")){
            return "Integer";
        }else if(sqlType.equalsIgnoreCase("smallint")){
            return "Short";
        }else if(sqlType.equalsIgnoreCase("int")){
            return "Integer";
        }else if(sqlType.equalsIgnoreCase("bigint")){
            return "Long";
        }else if(sqlType.equalsIgnoreCase("float")){
            return "Float";
        }
        else if(sqlType.equalsIgnoreCase("decimal")){
            return "BigDecimal";
        }
        else if(sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")){
            return "Double";
        }else if(sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text")){
            return "String";
        }else if(sqlType.equalsIgnoreCase("datetime") ||sqlType.equalsIgnoreCase("date")){
            return "Date";
        }else if(sqlType.equalsIgnoreCase("image")){
            return "Blod";
        }else if(sqlType.equalsIgnoreCase("timestamp")){
            return "Timestamp";
        }

        return null;
    }
    /*获取整个类的字符串并且输出为java文件
     * */
    public static StringBuffer getClassStr(String table,String [] colnames,List<String> colTypes,String modelPath){
        //输出的类字符串
        StringBuffer str = new StringBuffer("package com.resto.scm.web.model;"+"\r\n");

        //校验
        if(null == colnames && null == colTypes)
          return new StringBuffer();

       //拼接包名称，和 导入包

        //str.append("import com.resto.common.dao.domain.BaseParms;"+"\r\n");

        str.append("import java.io.Serializable;"+"\r\n");

        if(colTypes.contains("BigDecimal")){
            str.append(cMap.get("BigDecimal")+"\r\n");
        }
        if(colTypes.contains("Date")){
            str.append(cMap.get("Date")+"\r\n");
        }

        if(colTypes.contains("String")){
            str.append(cMap.get("String")+"\r\n");
        }
        if(colTypes.contains("Long")){
            str.append(cMap.get("Long")+"\r\n");
        }
        if(colTypes.contains("Integer")){
            str.append(cMap.get("Integer")+"\r\n");
        }
        if(colTypes.contains("Float")){
            str.append(cMap.get("Float")+"\r\n");
        }

        if(colTypes.contains("Double")){
            str.append(cMap.get("Double")+"\r\n");
        }
        if(colTypes.contains("Boolean")){
            str.append(cMap.get("Boolean")+"\r\n");
        }



        //拼接
        str.append("public class "+GetTuoFeng(table)+"  implements  Serializable  {\r\n");
        //拼接属性
        for(int index=1; index < colnames.length ; index++){
            str.append(getAttrbuteString(colnames[index],colTypes.get(index-1)));
        }
        //拼接get，Set方法
        for(int index=1; index < colnames.length ; index++){
            str.append(getGetMethodString(colnames[index],colTypes.get(index-1)));
            str.append(getSetMethodString(colnames[index],colTypes.get(index-1)));
        }
        str.append("}\r\n");

        //输出到文件中
        File file = new File(modelPath+GetTuoFeng(table)+".java");
        BufferedWriter write = null;

        try {
            write = new BufferedWriter(new FileWriter(file));
            write.write(str.toString());
            write.close();
        } catch (IOException e) {

            e.printStackTrace();
            if (write != null)
                try {
                    write.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
        return str;
    }

    /* 获取字段字符串*/
    public static StringBuffer getAttrbuteString(String name, String type) {
        if(!check(name,type)) {
            System.out.println("类中有属性或者类型为空");
            return null;
        }
        String format = String.format("    private %s %s;\n\r", new String[]{type,toLowerCaseFist(GetTuoFeng(name))});
        return new StringBuffer(format);
    }
    /*
     * 校验name和type是否合法*/
    public static boolean check(String name, String type) {
        if("".equals(name) || name == null || name.trim().length() ==0){
            return false;
        }
        return !"".equals(type) && type != null && type.trim().length() != 0;

    }
    /*
     * 获取get方法字符串*/
    private static  StringBuffer getGetMethodString(String name, String type) {
        if(!check(name,type)) {
            System.out.println("类中有属性或者类型为空");
            return null;
        }
        String Methodname = "get"+GetTuoFeng(name);
        String format = String.format("    public %s %s(){\n\r", type,Methodname);
        format += String.format("        return this.%s;\r\n", toLowerCaseFist(GetTuoFeng(name)));
        format += "    }\r\n";
        return new StringBuffer(format);
    }

    //将名称首字大写，并且变成驼峰
    private static String GetTuoFeng(String name) {
        name = name.trim();
        String[] split = name.split("_");
        StringBuffer  sb = new StringBuffer();
        for (String str: split){
            sb.append(toUpperCaseOfFist(str));
        }
        return sb.toString();
    }
    //将名称首字符小写
    public static String toLowerCaseFist(String name) {
        if(name.length() > 1){
            name = name.substring(0, 1).toLowerCase()+name.substring(1);
        }else
        {
            name = name.toLowerCase();
        }
        return name;
    }


    //将名称首字符大写
    private static String toUpperCaseOfFist(String name) {
        if(name.length() > 1){
            name = name.substring(0, 1).toUpperCase()+name.substring(1);
        }else
        {
            name = name.toUpperCase();
        }
        return name;
    }

    /*
     * 获取字段的get方法字符串*/
    private static Object getSetMethodString(String name, String type) {
        if(!check(name,type)) {
            System.out.println("类中有属性或者类型为空");
            return null;
        }
        String Methodname = "set"+GetTuoFeng(name);
        String attr = toLowerCaseFist(GetTuoFeng(name));
        String format = String.format("    public void %s(%s %s){\n\r", Methodname,type,attr);
        format += String.format("        this.%s = %s;\r\n", attr,attr);
        format += "    }\r\n";
        return new StringBuffer(format);
    }

    /*
    * 获取字段的get方法字符串*/
    public static  void getEntityOfTable(String table,String modelPath) {
       // GenerateJavaBeanOfTable bean = new GenerateJavaBeanOfTable();
        //获取表类型和表名的字段名
        String[] colnames= {}; // 列名数组
        //列名类型数组
        List<String> colTypes = new ArrayList<String>();

        Map<String, Object> stringObjectMap = doAction(table);
        colnames = (String[]) stringObjectMap.get("colnames");
        colTypes =(List<String>) stringObjectMap.get("colTypes");
        getClassStr(table,colnames,colTypes,modelPath);

    }

    public static void main(String[] args) {


        //数据库中的表名
        List<String> tables=new ArrayList<String>();
//        tables.add("doc_stk_in_plan_detail");
//        tables.add("doc_stk_in_plan_header");
      //  tables.add("md_rul_article_bom_detail");
       // tables.add("md_rul_article_bom_detail_history");

       // tables.add("md_rul_article_bom_head");
      //  tables.add("md_rul_article_bom_head_history");
         tables.add("md_material_consume_opt");
         tables.add("md_material_consume_detail");
  //      tables.add("md_material");
 //
 //       tables.add("md_rul_article_bom_detail");
 //       tables.add("md_rul_article_bom_head");
 //        tables.add("md_supplier");
 //        tables.add("do_operate_log");
 //        tables.add("doc_material_stock");
 //        tables.add("md_supplier");


        //tables.add("md_supplier_contact");
//        tables.add("md_supplier_price_head");
//        tables.add("md_supplier_price_detail_history");
//       tables.add("md_supplier_price_detail");
        // tables.add("md_unit");


                 
      for (String table: tables) {
          getEntityOfTable(table,"E:\\temp\\entity\\");

        }


    }

}
