package com.resto.shop.web.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.resto.brand.core.enums.BucketNameType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;

/**
 * @author carl
 * @date 18/9/13 14:21
 */
public class OssUtil {

    private static  final String ENDPOINT = "http://oss-cn-shanghai.aliyuncs.com";

    private static final String ACCESSKEYID = "LTAINfbQqw049TwX";

    private static  final String ACCESSKEYSECRET = "B2xY7kzv6bkRJi8K1yUsLPYE3bjoD7";

    private static final String MINETYPE = MediaType.APPLICATION_JSON_VALUE;

    private static final Long EXPIRETIME = 3600L * 1000 * 24 * 365 * 10;

    private static OSSClient  ossClient;

    private static ObjectMetadata meta = null;

    /**
     *
     */
    public static String uploadFile(MultipartFile file, String bucketNameType){

        ossClient = new OSSClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);
        meta = new ObjectMetadata();

        try {
            InputStream in = file.getInputStream();
            String name = file.getOriginalFilename();
            String orginalFileName = file.getOriginalFilename();
            MultipartFile multipartFile = new MockMultipartFile(name,orginalFileName,MINETYPE,in);
            InputStream inputStream = multipartFile.getInputStream();
            uploadFileOSS(inputStream, name, bucketNameType);

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

    public static String uploadFileOSS(InputStream inputStream, String fileName, String bucketNameType) {

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
            PutObjectResult putResult = ossClient.putObject(bucketNameType,fileName, inputStream, objectMetadata);
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


    public static String uploadDescription(String fileName, byte[] bytes) {
        ossClient = new OSSClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);

        String ret = "";
        //如果不是用base64上传，byte[] bytes参数改为一个Inputstream
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(bytes.length);//使用Inputstream后填写Inputstream.available()
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setHeader("Pragma", "no-cache");
        objectMetadata.setContentType("text/plain");
        objectMetadata.setContentDisposition("inline;filename=" + fileName);
         ossClient.putObject(BucketNameType.PIC_ARTICLE, fileName, new ByteArrayInputStream(bytes), objectMetadata);
        Date expiration = new Date(System.currentTimeMillis() + EXPIRETIME);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(BucketNameType.PIC_ARTICLE, fileName, expiration);
        if (url != null) {
            return url.toString();
        }
        return "";
    }
}










