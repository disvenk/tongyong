package com.resto.shop.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

@SuppressWarnings({"resource","unused"})
public class CodeGenerateFromJavaTemp {
    //final  static  String bathPath = "C:/0821/resto-shop/";//项目存放的基础位置";

	//final  static  String BasePath = "C:/0821/resto-shop/";
	final  static  String BasePath = "F:\\preject\\resto-shop\\";

	final static String DaoPath = BasePath+"resto-shop-server/src/main/java/com/resto/shop/web/dao/";
	final static String ServicePath = BasePath+"resto-shop-api/src/main/java/com/resto/shop/web/service/";
	final static String ServiceImpl = BasePath+"resto-shop-server/src/main/java/com/resto/shop/web/service/impl/";
	final static String JspPath = BasePath+"resto-shop-web/src/main/webapp/WEB-INF/views/";
	final static String ControllerPath = BasePath+"resto-shop-web/src/main/java/com/resto/shop/web/controller/business/";
	final static String DaoMapperPath = DaoPath;
	final static String TempPath = BasePath+"resto-shop-api/source-temps/";
	final static String ModelClassName =BasePath+"resto-shop-api/src/main/java/com/resto/shop/web/model/";
	final static String ModelClassPackage="com.resto.shop.web.model";
	static String classpath = CodeGenerateFromJavaTemp.class.getResource("/").getFile();

	public static void main(String[] args) throws Exception {

		/**
		 * arr [classSimpleName,idType,tableName]
		 * 实体类型 主键  和  表名
		 */
		String [][]  classPrimay = new String[][]{
			//		new String[]{"MaterialStock","Long","doc_material_stock"},
			//		new String[]{"ArticleBom","Long","md_rul_article_bom_head"},
			//		new String[]{"upplier","Long","md_supplier"},
			//		new String[]{"SupplierPrice","Long","md_supplier_price_head"},
			//		new String[]{"MaterialStock","Long","doc_material_stock"},
					new String[]{"OrderItemActual","Long","tb_order_item_actual"},
		};


		for (String[] cp: classPrimay) {
			String modelName = cp[0];
			if(modelName==null){
				continue;
			}
			String primaryKey = cp[1];
			String tableName = cp[2];
			String mapperDir = DaoMapperPath+modelName+"Mapper.xml";
			
			Class<?> className = Class.forName(ModelClassPackage+"."+modelName);
//			generaterJspFile(className);
			
//			generaterControllerFile(modelName,primaryKey);

//			generaterSelectListSqlMapper(mapperDir,tableName,modelName);
			/**
			 * 生成 service 和 impl
			 */
			generaterServiceAndImpl(modelName,primaryKey);

			/**
			 * 让所有的Mapper 继承 GenericDao
			 */
			extendGenericDao(modelName,primaryKey);
		}
	}
	
	private static String[][] getModelClass() throws ClassNotFoundException {
		File file = new File(ModelClassName);
		File[] fileList = file.listFiles();
		String [][]  classPrimay = new String [fileList.length][3];
		for(int i=0;i<fileList.length;i++){
			File f = fileList[i];
			String className = ModelClassPackage+"."+f.getName().substring(0,f.getName().length()-5);
			Class<?> clazz = Class.forName(className);
			try {
				String classSimpleName = clazz.getSimpleName();
				String idType = clazz.getDeclaredField("id").getType().getSimpleName();
				String tableName = getTableName(classSimpleName,"tb");
				String []  string = new String[]{classSimpleName,idType,tableName};
				classPrimay[i]=string;
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return classPrimay;
	}

	private static String getTableName(String classSimpleName, String before) {
		StringBuffer b = new StringBuffer();
		b.append(before);
		for(char c:classSimpleName.toCharArray()){
			if(c>='A'&&c<='Z'){
				String add_ = "_"+c;
				b.append(add_.toLowerCase());
			}else{
				b.append(c);
			}
		}
		return b.toString();
	}

	private static void generaterJspFile(Class<?> className) throws IOException {
		String modelNameLower = className.getSimpleName().toLowerCase();
		String jspTempFilePath = TempPath+"list-temp.jspt";
		String targetFilePath = JspPath+modelNameLower+"/list.jsp";
		Field[] f = className.getDeclaredFields();
		StringBuffer formBody = new StringBuffer();
		StringBuffer columnObj = new StringBuffer();
		Map<String,String> map = new HashMap<String,String>();
		String [] ignoreField = new String[]{
				"id","createTime"
		}; 
		for (Field field : f) {
			String fieldName = field.getName();
			if(Arrays.asList(ignoreField).contains(fieldName)){
				continue;
			}
			String temp = "<div class=\"form-group\">\n"+
						  "    <label>{{name}}</label>\n"+
						  "    <input type=\"text\" class=\"form-control\" name=\"{{name}}\" v-model=\"m.{{name}}\">\n"+
						  "</div>\n";
			Map<String,String> data = new HashMap<String,String>();
			data.put("name", fieldName);
			String formGroup = readLineParam(temp, data);
			formBody.append(formGroup);
			
			String columnsTemp = "{                 \n"+
								 "	title : \"{{name}}\",\n"+
								 "	data : \"{{name}}\",\n"+
								 "},                 \n";
			String columnsGroup = readLineParam(columnsTemp, data);
			columnObj.append(columnsGroup);
		}
		map.put("modelNameLower", modelNameLower);
		map.put("formBody", formBody.toString());
		map.put("columnObj", columnObj.toString());
		generater(jspTempFilePath, targetFilePath, true, map);
	}

	public static void generaterSelectListSqlMapper(final String mapperDir, final String tableName, final String modelName) throws DocumentException, IOException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					addSelectToMapper(mapperDir, "selectList", "select * from "+tableName, modelName);
				} catch (DocumentException | IOException e) {
					e.printStackTrace();
				}
			}
		}).start();;
	}

	public static void addSelectToMapper(String mapperPath,String sqlId,String sql,String resultType) throws DocumentException, IOException{
		File mapperFile = new File(mapperPath);
		if(mapperFile.exists()){
			SAXReader reader = new SAXReader();
			Document document = reader.read(mapperFile);
			Element root = document.getRootElement();
			List<?> list = root.selectNodes("select[@id='"+sqlId+"']");
			if(list.isEmpty()){
				DocumentFactory f = DocumentFactory.getInstance();
				Element select = f.createElement("select");
				select.addAttribute("id", sqlId);
				select.addAttribute("resultType", resultType);
				select.addText("\n    "+sql+"\n");
				root.add(select);
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setNewlines(true);
				format.setEncoding("utf-8");
				XMLWriter writer = new XMLWriter(new FileWriter(mapperFile),format);
				writer.write(document);
				writer.flush();
				writer.close();
				System.out.println(mapperPath+" 添加 \n"+select.asXML() +" \n--------------成功 \n");
			}else{
				System.err.println(resultType+":"+sqlId+"已存在");
			}
		}else{
			System.err.println(mapperFile.getAbsolutePath()+" 不存在");
		}
	}
	
	public static void generaterControllerFile(String modelName, String primaryKey) throws Exception {
		String controllerTempFile =TempPath+"controller-temp.javat";
		String fileName = modelName+"Controller.java";
		boolean isOverWrite = true;
		Map<String,String> data = new HashMap<String,String>();
		data.put("modelName", modelName);
		data.put("primaryKey", primaryKey);
		data.put("modelNameLower", modelName.toLowerCase());
		generater(controllerTempFile, ControllerPath+fileName,false, data);
	}

	public static void extendGenericDao(String modelName,String primaryKey) throws Exception{
		String daoTargetPackage = "com.resto.shop.web.dao";
		String mapperName = modelName+"Mapper";
		extendGenericDao(modelName,primaryKey,mapperName,daoTargetPackage);
	}
	
	private static void extendGenericDao(String modelName, String primaryKey, String mapperName, String daoTargetPackage) throws Exception {
		String targetFilePath = DaoPath+"/"+mapperName+".java";
		String genericDaoName = "GenericDao";
		String genericDao = "com.resto.brand.core.generic."+genericDaoName;
		File daoFile = new File(targetFilePath);
		if(daoFile.exists()){
			StringBuffer newFile = new StringBuffer();
			BufferedReader br = new BufferedReader(new FileReader(daoFile));
			String line = "";
			boolean importGenericDao = false;
			boolean isImport = false;
			while((line=br.readLine())!=null){
				if(line.contains("interface")&&!line.contains("extends")){
					line = line.substring(0, line.lastIndexOf("{"))+" extends "+genericDaoName+"<"+modelName+","+primaryKey+"> {";
				}
				newFile.append(line+"\n");
				if(line.trim().startsWith("import")&&!isImport){
					importGenericDao=true;
				}
				if(importGenericDao){
					newFile.append("import "+genericDao+";"+"\n");
					importGenericDao=false;
					isImport = true;
				}
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(daoFile));
			writer.write(newFile.toString());
			writer.flush();
		}else{
			System.err.println("文件不存在	"+daoFile.getAbsolutePath());
		}
	}
	
	
	public static void generaterServiceAndImpl(String modelName,String primaryKey) throws Exception {
		String serviceInterfaceTempFilePath = TempPath+"service-temp.javat";
		String serviceImplementTempFilePath = TempPath+"service-impl-temp.javat";
		String targetPackage = "com.resto.shop.web.service";
		String targetPackageImpl = "com.resto.shop.web.service.impl";
		String targetFileName = modelName+"Service.java";
		String targetFileNameImpl = modelName+"ServiceImpl.java";
		boolean isOverWrite = true;
		Map<String,String> data = new HashMap<String,String>();
		data.put("modelName", modelName);
		data.put("primaryKey", primaryKey);
		data.put("modelNameLower", modelName.toLowerCase());
		generater(serviceInterfaceTempFilePath, ServicePath+targetFileName,false, data);
		generater(serviceImplementTempFilePath, ServiceImpl+targetFileNameImpl,false, data);
		
	}


	


	
	public static void generater(String tempFilePath,String targetPath,boolean isOverWrite,Map<String,String> data) throws IOException{
		File tempFile = new File(tempFilePath);
		File targetFile = new File(targetPath);
		if(tempFile.exists()){
			if(targetFile.exists()&&!isOverWrite){
				System.err.println("文件已存在！"+targetFile.getAbsolutePath());
			}else if(isOverWrite&&targetFile.exists()){
				targetFile.delete();
				targetFile.createNewFile();
			}else{
				if(!targetFile.getParentFile().exists()){
					targetFile.getParentFile().mkdirs();
				}
				targetFile.createNewFile();
			}
			BufferedReader reader = new BufferedReader(new FileReader(tempFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
			tempFileComplier(reader,writer,data);
		}else{
			System.err.println("模板文件不存在！"+tempFile.getAbsolutePath());
		}
	}

	private static void tempFileComplier(BufferedReader reader, BufferedWriter writer, Map<String, String> data) throws IOException {
		String line = "";
		while((line=reader.readLine())!=null){
			String lineRepalce = readLineParam(line,data);
			System.out.println(lineRepalce);
			writer.write(lineRepalce+"\n");
			writer.flush();
		}
		writer.close();
	}

	private static String readLineParam(String line, Map<String, String> data) {
		Pattern pattern = Pattern.compile("\\{\\{[\\w]{0,}\\}\\}");
		Matcher m = pattern.matcher(line);
		while(m.find()){
			String mp = m.group();
			String key = mp.substring(2).substring(0, mp.length()-4);
			line = line.replace(mp, data.get(key)==null?"":data.get(key));
		}
		return line;
	}
}
