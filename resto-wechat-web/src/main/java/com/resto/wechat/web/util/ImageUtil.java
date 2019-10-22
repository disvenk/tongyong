package com.resto.wechat.web.util;

import com.resto.brand.core.util.UploadFilesUtil;
import com.resto.api.customer.entity.Customer ;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created by carl on 2016/11/28.
 */
public class ImageUtil {

    public static String imageBytesScale(String filePath, HttpServletRequest request) throws IOException {
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0,lastR)+"/";
        systemPath = systemPath.replace("wechat","shop");
        try{
            File localFile = null;
            boolean flagLocal = false;
            if(filePath.indexOf("http://")>-1)
            {
                flagLocal = true;
                String localPath = download(filePath, request);
                localFile = new File(localPath);
            }
            else
            {
                localFile = new File(systemPath + "/" + filePath);
            }
            BufferedImage image = ImageIO.read(localFile);
            int width = image.getWidth();
            int height = image.getHeight();
            int cha = width - height;
            double scale; // 压缩比
            int scaledW = 0; // 新压缩后宽度
            int scaledH = 0; // 新压缩后高度
            BufferedImage outputImage = null;
            if (cha > 0) {
                if (width > 512) {
                    scale = (double) 512 / (double) width;
                    scaledW = (int) (scale * width); // 新压缩后宽度
                    scaledH = (int) (scale * height); // 新压缩后高度
                }
            } else {
                if (height > 512) {
                    scale = (double) 512 / (double) height;
                    scaledW = (int) (scale * width); // 新压缩后宽度
                    scaledH = (int) (scale * height); // 新压缩后高度
                }
            }
//        Image img = image.getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
            if (scaledW == 0) {
                scaledW = width;
                scaledH = height;
            }
            int cha1 = scaledW - scaledH;
            int cha2 = scaledH - scaledW;

            int endWH = 0;
            if(scaledW < scaledH){
                endWH = scaledW;
            }else{
                endWH = scaledH;
            }
            outputImage = new BufferedImage(endWH, endWH, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = (Graphics2D) outputImage.getGraphics();
            //截图
            if (scaledW < scaledH) {
                image = image.getSubimage(0, cha2 / 2, endWH, endWH);
            } else if (scaledW > scaledH) {
                image = image.getSubimage(cha1 / 2, 0, endWH, endWH);
            }
            graphics.drawImage(image, 0, 0, endWH, endWH, null);
            graphics.dispose();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean flag = ImageIO.write(outputImage, "jpg", out);
            byte[] bytes = out.toByteArray();
            String uuId = UUID.randomUUID().toString() + ".jpg";

            int count = filePath.lastIndexOf("/");
            String savePath = filePath.substring(0,count);
            savePath = savePath + "/" + uuId;
            if (bytes.length < 3 || savePath.equals("")) {
                return null;
            }
            File file = new File(systemPath + "/" +  uuId);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
            fos.close();

            String lastPath = UploadFilesUtil.uploadFile(systemPath + "/" +  uuId);
            if(flagLocal){
                UploadFilesUtil.deleteFile(localFile);
            }
            return lastPath;
        }catch (Exception e){
            System.out.println("【上传失败】");
            return null;
        }
    }

    public static String download(String urlString, HttpServletRequest request) throws Exception {
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0,lastR)+"/";
        systemPath = systemPath + "informationPhoto/";

        String localPath = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);

            InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            inStream.close();
            byte[] btImg = outStream.toByteArray();

            if (null != btImg && btImg.length > 0) {
                String fileName = UUID.randomUUID().toString() + ".jpg";
                File file = new File(systemPath + fileName);
                FileOutputStream fops = new FileOutputStream(file);
                fops.write(btImg);
                fops.flush();
                fops.close();
                localPath = systemPath + fileName;
            } else {
                System.out.println("没有从该连接获得内容");
            }

        } catch (Exception e) {
//			e.printStackTrace();
            System.out.println("【上传失败】");
        }
        return localPath;
    }

    /**
     * 给图片添加图片水印
     * @param destImageFile 目标图像地址
     */
    public final static void pressImage(Customer customer, String shopPhoto, String destImageFile, HttpServletRequest request) {
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0,lastR)+"/";
        systemPath = systemPath + "informationPhoto/";

        try {
            String localHeadPhotoPath = download(customer.getHeadPhoto(), request);
            scale(localHeadPhotoPath, systemPath + customer.getId() + "headPhoto.jpg", 100, 100);
            scaleYuan(systemPath + customer.getId() + "headPhoto.jpg", systemPath + customer.getId() + "headPhoto2.jpg", 100, 100);
            File img = null;
            if(shopPhoto.indexOf("http://")>-1){
                String localPath = download(shopPhoto, request);
                img = new File(localPath);
            }else{
                img = new File(shopPhoto);
            }
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            // 水印文件
            Image src_biao = ImageIO.read(new File(systemPath + customer.getId() + "headPhoto2.jpg"));
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) 1.0));
            g.drawImage(src_biao, width - wideth_biao - 462, height - height_biao - 965, wideth_biao, height_biao, null);
            // 水印文件结束
            g.dispose();
            ImageIO.write((BufferedImage) image,  "JPEG", new File(destImageFile));
            deleteFile(img);
            deleteFile(new File(localHeadPhotoPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给图片添加文字水印
     * @param customer
     * @param outImgPath
     * @param request
     */
    public final static void pressText(String text,Customer customer, String outImgPath, HttpServletRequest request) {
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0,lastR)+"/";
        systemPath = systemPath + "informationPhoto/";
        try {
            File img = new File(systemPath + customer.getId() + "shuiyin2.jpg");
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            Font font = new Font("PingFang SC Regular", Font.PLAIN, 26);
            g.setColor(Color.WHITE); //根据图片的背景设置水印颜色
            g.setFont(font);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) 1.0));
            g.drawString(text, 200, 108);
            g.dispose();
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(outImgPath);
            ImageIO.write(bufImg, "JPEG", outImgStream);
            outImgStream.flush();
            outImgStream.close();
            deleteFile(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void pressOneText(String text, Customer customer, String outImgPath, HttpServletRequest request) {
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0,lastR)+"/";
        systemPath = systemPath + "informationPhoto/";
        try {
            File img = new File(systemPath + customer.getId() + "shuiyin2.jpg");
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            Font font = new Font("PingFang SC Regular", Font.PLAIN, 26);
            g.setColor(Color.WHITE); //根据图片的背景设置水印颜色
            g.setFont(font);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) 1.0));
            g.drawString(text, 200, 120);
            g.dispose();
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(outImgPath);
            ImageIO.write(bufImg, "JPEG", outImgStream);
            outImgStream.flush();
            outImgStream.close();
            deleteFile(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void pressShopName(String text, Customer customer, String outImgPath, HttpServletRequest request) {
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0,lastR)+"/";
        systemPath = systemPath + "informationPhoto/";
        try {
            File img = new File(systemPath + customer.getId() + "shuiyin3.jpg");
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            Font font = new Font("PingFang SC Regular", Font.PLAIN, 26);
            g.setColor(Color.WHITE); //根据图片的背景设置水印颜色
            g.setFont(font);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) 1.0));
            g.drawString(text, 200, 143);
            g.dispose();
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(outImgPath);
            ImageIO.write(bufImg, "JPEG", outImgStream);
            outImgStream.flush();
            outImgStream.close();
            deleteFile(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static void pressQrcode(String filePath, Customer customer, String outImgPath, HttpServletRequest request) {
        String systemPath = request.getServletContext().getRealPath("");
        systemPath = systemPath.replaceAll("\\\\", "/");
        int lastR = systemPath.lastIndexOf("/");
        systemPath = systemPath.substring(0,lastR)+"/";
        systemPath = systemPath + "informationPhoto/";

        try {
            File img = new File(systemPath + customer.getId() + "shuiyin.jpg");
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            // 水印文件
            scale(filePath, filePath, 220, 220);
            Image src_biao = ImageIO.read(new File(filePath));
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) 1.0));
            g.drawImage(src_biao, width - wideth_biao - 210, height - height_biao - 243, wideth_biao, height_biao, null);
            // 水印文件结束
            g.dispose();
            ImageIO.write((BufferedImage) image,  "JPEG", new File(outImgPath));
            deleteFile(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转成圆形
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     */
    public final static void scaleYuan(String srcImageFile, String result,int newWidth, int newHeight) throws IOException {
        BufferedImage bi1 = ImageIO.read(new File(srcImageFile));
        // 根据需要是否使用 BufferedImage.TYPE_INT_ARGB
        BufferedImage image = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());
        Graphics2D g2 = image.createGraphics();
        image = g2.getDeviceConfiguration().createCompatibleImage(bi1.getWidth(), bi1.getHeight(), Transparency.TRANSLUCENT);
        g2 = image.createGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fill(new Rectangle(image.getWidth(), image.getHeight()));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        g2.setClip(shape);
        // 使用 setRenderingHint 设置抗锯齿
        g2.drawImage(bi1, 0, 0, null);
        g2.dispose();
        try {
            ImageIO.write(image, "PNG", new File(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缩放图像（按比例缩放）   或者给定比例
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     */
    public final static void scale(String srcImageFile, String result,int newWidth, int newHeight) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
            int width = newWidth;
            int height = newHeight;
            Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            ImageIO.write(tag, "jpg", new File(result));// 输出到文件流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算text的长度（一个中文算两个字符）
     * @param text
     * @return
     */
    public final static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }

    public static void deleteFile(File file) {
        System.gc();
        if(file.isFile() && file.exists()) {
            file.delete();
        } else if(file.isDirectory()) {
            String[] childFilePaths = file.list();
            String[] arr$ = childFilePaths;
            int len$ = childFilePaths.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String childFilePath = arr$[i$];
                File childFile = new File(file.getAbsolutePath() + File.separator + childFilePath);
                deleteFile(childFile);
            }

            file.delete();
        }

    }
}
