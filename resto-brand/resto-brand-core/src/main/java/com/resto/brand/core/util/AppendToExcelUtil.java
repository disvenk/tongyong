package com.resto.brand.core.util;

import org.apache.poi.hssf.usermodel.*;

import java.io.*;
import java.util.List;
import java.util.Map;

public class AppendToExcelUtil {

    /**
     * 在已有的Excel文件中插入新的数据的入口方法
     */
    public static void insertRows(String excelPath, Integer insertStartPointer, String[][] items) {
        HSSFWorkbook wb = returnWorkBookGivenFileHandle(excelPath);
        HSSFSheet sheet = wb.getSheetAt(0);
        for (int i=0;i<items.length;i++){
            HSSFRow row = sheet.createRow(insertStartPointer);
            row.setHeightInPoints(20f);
            for(int j=0;j<items[i].length;j++){
                HSSFRichTextString value = new HSSFRichTextString(items[i][j]);
                row.createCell(j).setCellValue(value);
            }
            insertStartPointer++;
        }
        saveExcel(wb,excelPath);
    }
    /**
     * 保存工作薄
     * @param wb
     */
    public static void saveExcel(HSSFWorkbook wb, String excelPath) {
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(excelPath);
            wb.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 得到一个已有的工作薄的POI对象
     * @return
     */
    public static HSSFWorkbook returnWorkBookGivenFileHandle(String excelPath) {
        HSSFWorkbook wb = null;
        FileInputStream fis = null;
        File f = new File(excelPath);
        try {
            if (f != null) {
                fis = new FileInputStream(f);
                wb = new HSSFWorkbook(fis);
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return wb;
    }
}
