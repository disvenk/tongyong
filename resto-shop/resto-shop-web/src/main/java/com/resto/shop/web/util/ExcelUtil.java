package com.resto.shop.web.util;

import com.resto.brand.core.util.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtil<T> {

    public void ExportExcel(String fileName, HttpServletRequest request, HttpServletResponse response, HSSFWorkbook workbook, String[][] headers , String[] columns, Collection<T> result, Map<String, String>map) throws Exception {


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
        //设置前四行的内容
        for(int i=0;i<3;i++){
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
                    cell.setCellValue(new HSSFRichTextString("品牌:"+map.get("brandName")));
                    // 指定合并区域
                    sheet.addMergedRegion(new CellRangeAddress(1,(short)1,0,(short)num));
                    break;
                case 2:
                    if(map.get("beginDate")!=null && map.get("endDate")!=null){
                        cell.setCellValue(new HSSFRichTextString("统计时间："+map.get("beginDate")+"至"+map.get("endDate")));
                    }else {
                        cell.setCellValue(new HSSFRichTextString("统计时间："+map.get("date")+"日"));
                    }

                    // 指定合并区域
                    sheet.addMergedRegion(new CellRangeAddress(2,(short)2,0,(short)num));
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
       int index = 5;
        if(result != null && !result.isEmpty()){
          //  int index = 5;
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

        String agent = request
                .getHeader("user-agent");//获取所使用的浏览器类型
        fileName = FileUtils.encodeDownloadFilename(fileName, agent);//按照指定的浏览器进行编码
        response.setHeader("Content-Disposition",
                "attachment;filename=" + fileName);//设置下载的文件名称

        ServletOutputStream outputStream =response
                .getOutputStream();//获取输入流
        workbook.write(outputStream);//写入导出
        // 关闭
        outputStream.close();

    }

}
