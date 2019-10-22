package com.resto.brand.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
public class UserOrderExcelUtil<T> {
    /**
     * 导出Excel的方法  
     * @param headers 表头
     * @param result 结果集  
     * @param out 输出流  
     * @throws Exception
     */
	public void ExportExcel(String[][] headers ,String[] columns, Collection<T> result,OutputStream out,Map<String, String>map) throws Exception {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(map.get("reportTitle"));
        
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 标题样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        // 设置水平居中
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直居中
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 标题字体
        HSSFFont titleFont = workbook.createFont();
        titleFont.setFontName("微软雅黑");
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        titleStyle.setFont(titleFont);
        // 正文样式
        HSSFCellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.cloneStyleFrom(titleStyle);
        // 正文字体
        HSSFFont bodyFont = workbook.createFont();
        bodyFont.setFontName("宋体");
        bodyFont.setColor(HSSFColor.BLACK.index);
        bodyFont.setFontHeightInPoints((short) 12);
        bodyStyle.setFont(bodyFont);
        HSSFRow row = null;
        HSSFCell cell = null;
        short num = new Short(map.get("num"));
        //设置前三行的内容
        for(int i=0;i<2;i++){
        	row = sheet.createRow(i);
        	row.setHeight((short) 300);
        	cell = row.createCell(0);
        	cell.setCellType(HSSFCell.ENCODING_UTF_16);
        	switch (i) {
			case 0:
		        cell.setCellValue(new HSSFRichTextString(map.get("reportType")));
		     // 指定合并区域  
	             sheet.addMergedRegion(new CellRangeAddress(0,(short)0,0,(short)num));
				break;
			case 1:
				cell.setCellValue(new HSSFRichTextString("统计时间："+map.get("beginDate")+"至"+map.get("endDate")));
				// 指定合并区域  
	             sheet.addMergedRegion(new CellRangeAddress(3,(short)3,0,(short)num));
				break;
			default:
				break;
			}
        	 HSSFCellStyle cellStyle = workbook.createCellStyle();  
             cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐  
             cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐  
             cellStyle.setWrapText(true);// 指定单元格自动换行  
             // 设置单元格字体  
             HSSFFont font = workbook.createFont();  
             font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
             font.setFontName("宋体");  
             font.setFontHeight((short) 250);  
             cellStyle.setFont(font);  
             cell.setCellStyle(cellStyle);  
        }
        	
            // 产生表格标题行
            row = sheet.createRow(4);
            // 设置行高
            row.setHeightInPoints(25f);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell2 = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i][0]);
                cell2.setCellValue(text);
                cell2.setCellStyle(titleStyle);
                // 设置列宽
                sheet.setColumnWidth(i, Integer.parseInt(headers[i][1]) * 256);
            }
        
        // 遍历集合数据，产生数据行
        if(result != null){    
            int index = 5;    
            for(T t:result){   
            //  Field[] fields = t.getClass().getDeclaredFields();  
                row = sheet.createRow(index); 
                row.setHeightInPoints(20f);
                index++;  
                for(int i = 0; i < columns.length; i++) {  
                    HSSFCell cell3 = row.createCell(i);  
//                  Field field = fields[i];  
//                  String fieldName = field.getName();  
                    String fieldName = columns[i];  
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);  
                    Class tCls = t.getClass();  
                    Method getMethod = tCls.getMethod(getMethodName, new Class[]{});  
                   // getMethod.getReturnType().isInstance(obj);
                    Object value = getMethod.invoke(t, new Class[]{});  
                    String textValue = null;  
                    if(value == null) {  
                        textValue = "";  
                    }else if (value instanceof Date) {  
                        Date date = (Date) value;  
                        SimpleDateFormat sdf = new SimpleDateFormat(map.get("timeType"));  
                         textValue = sdf.format(date);  
                     }  else if (value instanceof byte[]) {  
                        // 有图片时，设置行高为60px;  
                        row.setHeightInPoints(60);  
                        // 设置图片所在列宽度为80px,注意这里单位的一个换算  
                        sheet.setColumnWidth(i, (short) (35.7 * 80));  
                        // sheet.autoSizeColumn(i);  
                        byte[] bsValue = (byte[]) value;  
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,  
                              1023, 255, (short) 6, index, (short) 6, index);  
                        anchor.setAnchorType(2);  
                        patriarch.createPicture(anchor, workbook.addPicture(  
                              bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));  
                     } else{  
                        //其它数据类型都当作字符串简单处理  
                        textValue = value.toString();  
                     }  
                      
                    if(textValue!= null){  
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");    
                        Matcher matcher = p.matcher(textValue);  
                        if(matcher.matches()){  
                           //是数字当作double处理  
                           cell.setCellValue(Double.parseDouble(textValue));  
                           HSSFCellStyle cellStyle = workbook.createCellStyle();
                           cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
                           cell.setCellStyle(cellStyle);
                        }else{  
                           HSSFRichTextString richString = new HSSFRichTextString(textValue);  
//                           HSSFFont font3 = workbook.createFont();  
//                           font3.setColor(HSSFColor.BLUE.index);  
//                           richString.applyFont(font3);  
                           cell3.setCellValue(richString);  
                        }  
                     }  
                }  
            }       
        }    
        workbook.write(out);    
      
        
//       
    }
    
    public void download(String path, HttpServletResponse response) {  
        try {  
            // path是指下载的文件的路径。  
            File file = new File(path);  
            // 取得文件名。  
            String filename = file.getName();  
            // 以流的形式下载文件。  
            InputStream fis = new BufferedInputStream(new FileInputStream(path));  
            byte[] buffer = new byte[fis.available()];  
            fis.read(buffer);  
            fis.close();  
            // 清空response  
            response.reset();  
            // 设置response的Header  
            String s = new String(filename.getBytes());
            
            response.addHeader("Content-Disposition", "attachment;filename="  
                    + toUtf8String(s));  
            response.addHeader("Content-Length", "" + file.length());  
            OutputStream toClient = new BufferedOutputStream(  
                    response.getOutputStream());  
            response.setContentType("application/vnd.ms-excel;charset=utf-8");  
            toClient.write(buffer);  
            toClient.flush();  
            toClient.close();  
            
            if(file.exists()){
            	file.delete();
            }
    } catch (IOException ex) {  
            ex.printStackTrace();  
        }  
    }  
    
    public static String toUtf8String(String s){
    	StringBuffer sb = new StringBuffer();
    	for (int i=0;i<s.length();i++){
    	char c = s.charAt(i);
    	if (c >= 0 && c <= 255){sb.append(c);}
    	else{
    	byte[] b;
    	try { b = Character.toString(c).getBytes("utf-8");}
    	catch (Exception ex) {
    	System.out.println(ex);
    	b = new byte[0];
    	}
    	for (int j = 0; j < b.length; j++) {
    	int k = b[j];
    	if (k < 0) k += 256;
    	sb.append("%" + Integer.toHexString(k).toUpperCase());
    	}
    	}
    	}
    	return sb.toString();
    	}
    
    
}
