package com.resto.brand.core.util;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by KONATA on 2017/2/7.
 */
public class CommonThirdExcel {


    /**
     * 生成excel导出流
     * @param sheetName excel名称列表
     * @param title 标题列表
     * @param data 数据
     * @throws IOException
     */
    public static byte [] writeToStream(String[] sheetName, List<? extends Object[]> title, List<? extends List<? extends Object[]>> data) throws  IOException{
        //创建并获取工作簿对象
        Workbook wb= getWorkBook( sheetName, title, data);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
            byte [] bytes = os.toByteArray();
            return bytes;
        }finally {
            if(os != null){
                os.close();
            }
        }


    }


    public static Workbook getWorkBook(String[] sheetName,List<? extends Object[]> title,List<? extends List<? extends Object[]>> data) throws IOException{
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建一个工作表sheet
        Sheet sheet ;
        //申明行
        Row row ;
        //申明单元格
        Cell cell ;
        //单元格样式
        CellStyle titleStyle=wb.createCellStyle();
        CellStyle cellStyle=wb.createCellStyle();
        //字体样式
        Font font=wb.createFont();
        //粗体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleStyle.setFont(font);
        //水平居中
        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //垂直居中
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        //水平居中
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //垂直居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        //标题数据
        Object[] title_temp;

        //行数据
        Object[] rowData;

        //工作表数据
        List<? extends Object[]> sheetData;
        //遍历sheet
        for(int sheetNumber=0;sheetNumber<sheetName.length;sheetNumber++){
            //创建工作表
            sheet=wb.createSheet();

            //设置工作表名称
            wb.setSheetName(sheetNumber, sheetName[sheetNumber]);
            //设置标题
            title_temp=title.get(sheetNumber);

            row=sheet.createRow(0);

            //写入标题
            for(int i=0;i<title_temp.length;i++){
                sheet.setColumnWidth(i, 20 * 256);
                cell=row.createCell(i);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(title_temp[i].toString());
            }

            try {
                sheetData=data.get(sheetNumber);
            } catch (Exception e) {
                continue;
            }
            //写入行数据
            for(int rowNumber=0;rowNumber<sheetData.size();rowNumber++){
                //如果没有标题栏，起始行就是0，如果有标题栏，行号就应该为1
                row=sheet.createRow(title_temp==null?rowNumber:(rowNumber+1));
                rowData=sheetData.get(rowNumber);
                for(int columnNumber=0;columnNumber<rowData.length;columnNumber++){
                    cell=row.createCell(columnNumber);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(rowData[columnNumber].toString());
                }
            }
        }
        return wb;
    }


}
