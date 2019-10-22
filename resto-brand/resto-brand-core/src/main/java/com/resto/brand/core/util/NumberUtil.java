package com.resto.brand.core.util;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by yz on 2017-04-18.
 */
public class NumberUtil {

    //计算两个数百分比

    public static String getFormat(int a,int b){
        NumberFormat numberFormat = NumberFormat.getInstance();
        //设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) a / (float) b * 100);
        return  result;
    }

    public static Long  getTimes(String time) throws ParseException {
        Date date =DateUtil.fomatDate(time);
        return date.getTime();
    }

    public static void main(String[] args) throws ParseException {
        //循环修改文件夹的名字
        File file = new File("C:\\yz\\1");
        deleteFile(file);
    }

    private static boolean renameToNewFile(String src, String dest)
    {
        File srcDir = new File(src);
        boolean isOk = srcDir.renameTo(new File(dest));
        System.out.println("renameToNewFile is OK ? :" +isOk);
        return isOk;
    }


        private static void deleteFile(File file)
        {
            if(file.exists())
            { // 判断文件是否存在
                if(file.isFile())
                { // 判断是否是文件
                    file.delete(); // delete()方法 你应该知道 是删除的意思;
                }
                else if(file.isDirectory())
                { // 否则如果它是一个目录
                    File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                    for(int i = 0; i < files.length; i++)
                    { // 遍历目录下所有的文件
                         deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                    }
                }
                file.delete();
                System.out.println("deleteFile:"+file.getAbsolutePath());
            }
            else
            {
                System.out.println("所删除的文件不存在！" + '\n');
            }
        }
    }




