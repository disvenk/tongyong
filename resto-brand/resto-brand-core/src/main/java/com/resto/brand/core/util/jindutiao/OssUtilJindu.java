package com.resto.brand.core.util.jindutiao;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.resto.brand.core.util.PutObjectProgressListener;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @author yanjuan
 * @date 18/1/8 18:21
 */
public class OssUtilJindu {

    //华东2 外网Endpoint oss-cn-shanghai.aliyuncs.com
    //华东2 内网Endpoint oss-cn-shanghai-internal.aliyuncs.com

    private static  final String ENDPOINT = "http://oss-cn-shanghai.aliyuncs.com";

    private static final String ACCESSKEYID = "LTAINfbQqw049TwX";

    private static  final String ACCESSKEYSECRET = "B2xY7kzv6bkRJi8K1yUsLPYE3bjoD7";

    private static  final  String BUCKETNAME = "newpos";

    private static String downloadFile = "d://ceshi.xlsx";

    private static final String MINETYPE = MediaType.APPLICATION_JSON_VALUE;

    private static final Long EXPIRETIME = 3600L * 1000 * 24 * 365 * 10;

    private static OSSClient  ossClient;

    private static ObjectMetadata meta = null;

    static {
        ossClient = new OSSClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);
        meta = new ObjectMetadata();
    }

    /**
     *
     */
    public static String uploadFile(HttpServletRequest request, File file, String bucketNameType){
        try {
            String name = file.getName();
            InputStream inputStream = new FileInputStream(file);
            uploadFileOSS(request,file,inputStream, name, bucketNameType);

            Date expiration = new Date(System.currentTimeMillis() + EXPIRETIME);
            // 生成URL
            URL url = ossClient.generatePresignedUrl(bucketNameType, name, expiration);
            if (url != null) {
                return url.toString();
            }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
    }

    public static String uploadFileOSS(HttpServletRequest request, File file, InputStream inputStream, String fileName, String bucketNameType) {

        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
//            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentType("application/zip");
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            PutObjectResult putResult = ossClient.putObject(new PutObjectRequest(bucketNameType, fileName, file).
                    <PutObjectRequest>withProgressListener(new PutObjectProgressListener(request.getSession())));
            ret = putResult.getETag();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    /**
     * @Description: 根据key获取oss服务器上的图片地址
     * @param key
     * @return
     * @ReturnType:String
     */
    public static String getImgURl(String key){
        Date expires = new Date(System.currentTimeMillis()+EXPIRETIME);
        GeneratePresignedUrlRequest generatePresignedUrlRequest ;
        if(key.startsWith("msplatform")){
            generatePresignedUrlRequest =new GeneratePresignedUrlRequest(BUCKETNAME, key);
        }else {
            generatePresignedUrlRequest =new GeneratePresignedUrlRequest(BUCKETNAME, key);
        }
        generatePresignedUrlRequest.setExpiration(expires);
        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }



    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }


    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public static String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + EXPIRETIME);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(BUCKETNAME, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }


    public  static void getObject() throws IOException{
        try {
            DownloadFileRequest downloadFileRequest = new DownloadFileRequest(BUCKETNAME, "1519896702555.xlsx");
            // Sets the local file to download to
            downloadFileRequest.setDownloadFile(downloadFile);
            // Sets the concurrent task thread count 5. By default it's 1.
            downloadFileRequest.setTaskNum(5);
            // Sets the part size, by default it's 100K.
            downloadFileRequest.setPartSize(1024 * 1024 * 1);
            // Enable checkpoint. By default it's false.
            downloadFileRequest.setEnableCheckpoint(true);

            DownloadFileResult downloadResult = ossClient.downloadFile(downloadFileRequest);

            ObjectMetadata objectMetadata = downloadResult.getObjectMetadata();
            System.out.println(objectMetadata.getETag());
            System.out.println(objectMetadata.getLastModified());
            System.out.println(objectMetadata.getUserMetadata().get("meta"));

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    public static String getOssFilePath(String fileName, String uploadFilePath) {
        URL uri = null;
        OSSClient client = ossClient;
        //上传图片
        String key = "image/" + fileName; //指定文件上传到bucket下面的那个文件夹下及文件名
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        boolean isSuccess = uploadFilePath(client, BUCKETNAME, key, uploadFilePath);//指定bucket
        if (isSuccess) {
            uri = client.generatePresignedUrl(BUCKETNAME, key, expiration);
        }
        return uri.toString();
    }

    public static boolean uploadFilePath(OSSClient client, String bucketName, String key, String filePath) {
        int MAX_TRY = 3;
        int downloadTurn = 0;
        boolean uploadSuccess = false;
        while (downloadTurn < MAX_TRY) {
            try {
                File file = new File(filePath);
                if ((!file.exists()) || file.length() == 0) {
                    uploadSuccess = false;
                    break;
                }
                ObjectMetadata objectMeta = new ObjectMetadata();
                objectMeta.setContentLength(file.length());
                if (!client.doesObjectExist(bucketName, key)) {
                    InputStream input = new FileInputStream(file);
                    client.putObject(bucketName, key, input, objectMeta);
                    System.out.println(filePath + "上传成功!");
                    uploadSuccess = true;
                    break;
                } else {
                    uploadSuccess = true;
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return uploadSuccess;
    }

}










