package com.resto.brand.core.util;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

public  class  FastDFSClient{

    private TrackerClient trackerClient = null;
    private TrackerServer trackerServer = null;
    private StorageServer storageServer = null;
    private StorageClient1 storageClient1= null;

    public  FastDFSClient(String conf) throws Exception{
        if(conf.contains("classpath:")){
            conf = conf.replace("classpath:",this.getClass().getResource("/").getPath());
        }
        ClientGlobal.init(conf);
        trackerClient = new TrackerClient();
        trackerServer = trackerClient.getConnection();
        storageServer = null;
        storageClient1 = new StorageClient1(trackerServer,storageServer);

    }

    /**
     * 上传文件的方法
     * @param fileName 文件全路径
     * @param extName 文件扩展名 不包含(.)
     * @param metas 文件扩展信息
     * @return
     * @throws Exception
     */
    public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws Exception{
        String result = storageClient1.upload_file1(fileName,extName,metas);
        return  result;
    }

    public String uploadFile(String fileName) throws Exception{
        return  uploadFile(fileName,null,null);
    }

    public String uploadFile(String fileName, String extName) throws Exception{
        String result = storageClient1.upload_file1(fileName,extName,null);
        return  result;
    }

    /**
     * 上传文件的方法
     * @param fileContent
     * @param extName
     * @param metas
     * @return
     * @throws Exception
     */
    public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) throws Exception{
        String result = storageClient1.upload_file1(fileContent,extName,metas);
        return  result;
    }

    public String uploadFile(byte[] fileContent) throws Exception{
        String result = storageClient1.upload_file1(fileContent,null,null);
        return  result;
    }

    public String uploadFile(byte[] fileContent, String extName) throws Exception{
        String result = storageClient1.upload_file1(fileContent,extName,null);
        return  result;
    }


}