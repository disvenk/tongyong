package com.resto.brand.core.util;

import com.resto.brand.core.entity.Result;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


/**
 * @Auther: yunqing.yue
 * @Date: 2018/11/9/0009 13:59
 * @Description:
 */
public class ExcelPOIUtil {

    public static Result checkFile(MultipartFile file) throws IOException {
        Result result = new Result();
        //判断文件是否存在
        if(null == file){
            result.setSuccess(false);
            result.setMessage("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if(!fileName.endsWith("xls") && !fileName.endsWith("xlsx")){
            result.setSuccess(false);
            result.setMessage("不是excel文件！");
        }
        return result;
    }

    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            Workbook workbook=null;
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith("xls")){
                //2003
                 workbook = new HSSFWorkbook(is);
                return workbook;
            }else if(fileName.endsWith("xlsx")){
                //2007
                 workbook = new XSSFWorkbook(is);//得到工作簿
                return workbook;
            }
        } catch (IOException e) {
        }
        return null;
    }
}
