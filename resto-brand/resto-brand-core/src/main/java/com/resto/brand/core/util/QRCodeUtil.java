package com.resto.brand.core.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/** 
 * 生成二维码的工具类 
 * 原文参考：http://blog.csdn.net/sanfye/article/details/45749139
 * @author lmx
 */  
public class QRCodeUtil {



    private static final int BLACK = 0xFF000000;					//用于二维码的颜色  
    private static final int WHITE = 0xFFFFFFFF; 					//用于二维码背景色  
    private static final int WIDTH = 300;							//二维码的宽度
    private static final int HEIGHT = 300;							//二维码的高度
    private static final String FORMAT = "gif";						//二维码的图片格式
    private static final boolean isLogo = false;					//是否生成logo
    private static final String logeFilePath = "D:\\logo.jpg";		//logo的文件路径
    
    static Logger log = LoggerFactory.getLogger(QRCodeUtil.class);
    
    
    private QRCodeUtil() {
    }
    
    
    
    /**
     * 生成 二维码
     * @param contents	二维码内容
     * @param filePath	图片保存的路径
     * @param fileName	图片的名称
     * @throws IOException
     * @throws WriterException
     */
    public static void createQRCode(String contents,String filePath,String fileName) throws IOException, WriterException{  
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
         // 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");	// 内容所使用字符集编码   
        hints.put(EncodeHintType.MARGIN, 1);				//设置二维码边的空度，非负数  
        
        BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,//要编码的内容  
                //编码类型，目前zxing支持：Aztec 2D,CODABAR 1D format,Code 39 1D,Code 93 1D ,Code 128 1D,  
                //Data Matrix 2D , EAN-8 1D,EAN-13 1D,ITF (Interleaved Two of Five) 1D,  
                //MaxiCode 2D barcode,PDF417,QR Code 2D,RSS 14,RSS EXPANDED,UPC-A 1D,UPC-E 1D,UPC/EAN extension,UPC_EAN_EXTENSION  
                BarcodeFormat.QR_CODE,  
                WIDTH, //条形码的宽度  
                HEIGHT, //条形码的高度  
                hints);//生成条形码时的一些配置,此项可选  
        // 生成二维码  
        File outputFile = new File(filePath+File.separator+fileName);//指定输出路径  
        if (!outputFile.exists()) {
        	outputFile.mkdirs();
		}
        QRCodeUtil.writeToFile(bitMatrix, FORMAT, outputFile);  
    }

    /**
     * 生成二维码  通过流
     */
    public static void createQRCode(String contents,String format,OutputStream outputStream) throws IOException, WriterException{
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        // 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");	// 内容所使用字符集编码
        hints.put(EncodeHintType.MARGIN, 1);				//设置二维码边的空度，非负数
        BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,//要编码的内容
                //编码类型，目前zxing支持：Aztec 2D,CODABAR 1D format,Code 39 1D,Code 93 1D ,Code 128 1D,
                //Data Matrix 2D , EAN-8 1D,EAN-13 1D,ITF (Interleaved Two of Five) 1D,
                //MaxiCode 2D barcode,PDF417,QR Code 2D,RSS 14,RSS EXPANDED,UPC-A 1D,UPC-E 1D,UPC/EAN extension,UPC_EAN_EXTENSION
                BarcodeFormat.QR_CODE,
                WIDTH, //条形码的宽度
                HEIGHT, //条形码的高度
                hints);//生成条形码时的一些配置,此项可选
        // 生成二维码
        QRCodeUtil.writeToStream(bitMatrix,format,outputStream);
    }
    
    /**
     * 生成二维码图片
     * @param matrix
     * @return
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {  
        int width = matrix.getWidth();  
        int height = matrix.getHeight();  
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        for (int x = 0; x < width; x++) {  
            for (int y = 0; y < height; y++) {  
                image.setRGB(x, y,  (matrix.get(x, y) ? BLACK : WHITE));  
            }  
        }  
        return image;  
    }  
  
    /**
     * 写入到文件
     * @param matrix
     * @param format
     * @param file
     * @throws IOException
     */
    public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {  
        BufferedImage image = toBufferedImage(matrix);  
        if(isLogo){
        	//设置logo图标 
            image = LogoMatrix(image,logeFilePath);  
        }
        if (!ImageIO.write(image, format, file)) {  
            throw new IOException("无法将文件   " + file + "  格式化为" + format);  
        }else{  
            log.info(file.getName()+"   ---> 二维码生成成功！");
        }
    }

    public static void writeToFileInformation(BitMatrix matrix, String format, File file, String brandLogoPath) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        //设置logo图标
        image = LogoMatrix(image,brandLogoPath);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("无法将文件   " + file + "  格式化为" + format);
        }else{
            log.info(file.getName()+"   ---> 二维码生成成功！");
        }
    }

    /**
     * 写入到流
     * @param matrix
     * @param format
     * @param stream
     * @throws IOException
     */
    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {  
    	BufferedImage image = toBufferedImage(matrix);
        if(isLogo) {
            //设置logo图标
            image = LogoMatrix(image, logeFilePath);
        }
        if (!ImageIO.write(image, format, stream)) {  
            throw new IOException("Could not write an image of format " + format);  
        }
    }  
    
    
    
    
    /**
     * 二维码 添加 logo图标 处理的方法
     * 模仿微信自动生成二维码效果，有圆角边框，logo和二维码间有空白区，logo带有灰色边框 
     * @param matrixImage	被写入的图片对象
     * @param logeFilePath	logo的路径
     * @return
     * @throws IOException
     */
    public static BufferedImage LogoMatrix(BufferedImage matrixImage,String logeFilePath) throws IOException{
    	File logoFile = new File(logeFilePath);
        if(logoFile.exists()){
        	//读取二维码图片，并构建绘图对象   
            Graphics2D g2 = matrixImage.createGraphics();  
            int matrixWidth = matrixImage.getWidth();  
            int matrixHeigh = matrixImage.getHeight();  
            //读取Logo图片  
            BufferedImage logo = ImageIO.read(logoFile); 
            //开始绘制图片  
            g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5, null);//绘制       
            BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);   
            g2.setStroke(stroke);// 设置笔画对象  
            //指定弧度的圆角矩形  
            RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5,20,20);  
            g2.setColor(Color.white);  
            g2.draw(round);// 绘制圆弧矩形  
              
            //设置logo 有一道灰色边框  
            BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);   
            g2.setStroke(stroke2);// 设置笔画对象  
            RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth/5*2+2, matrixHeigh/5*2+2, matrixWidth/5-4, matrixHeigh/5-4,20,20);  
            g2.setColor(new Color(128,128,128));  
            g2.draw(round2);// 绘制圆弧矩形  
            g2.dispose();  
            matrixImage.flush() ;  
        }else{
        	log.error("logo 文件不存在，需修改 logo 文件位置");
        }
        return matrixImage ;  
    }

    /**
     * 生成 二维码  带领字的logo
     * @param contents	二维码内容
     * @param filePath	图片保存的路径
     * @param fileName	图片的名称
     * @throws IOException
     * @throws WriterException
     */
    public static void createQRCodeInformation(String contents, String filePath, String fileName, String brandLogoPath) throws IOException, WriterException{
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        // 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");	// 内容所使用字符集编码
        hints.put(EncodeHintType.MARGIN, 1);				//设置二维码边的空度，非负数

        BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,//要编码的内容
                //编码类型，目前zxing支持：Aztec 2D,CODABAR 1D format,Code 39 1D,Code 93 1D ,Code 128 1D,
                //Data Matrix 2D , EAN-8 1D,EAN-13 1D,ITF (Interleaved Two of Five) 1D,
                //MaxiCode 2D barcode,PDF417,QR Code 2D,RSS 14,RSS EXPANDED,UPC-A 1D,UPC-E 1D,UPC/EAN extension,UPC_EAN_EXTENSION
                BarcodeFormat.QR_CODE,
                WIDTH, //条形码的宽度
                HEIGHT, //条形码的高度
                hints);//生成条形码时的一些配置,此项可选
        // 生成二维码
        File outputFile = new File(filePath + fileName);//指定输出路径
        // 生成二维码
        QRCodeUtil.writeToFileInformation(bitMatrix, FORMAT, outputFile, brandLogoPath);
    }
}  