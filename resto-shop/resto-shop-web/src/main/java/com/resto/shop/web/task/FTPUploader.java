package com.resto.shop.web.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
 
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * 上传文件到FTP文件服务器
 * @author Administrator
 *
 */
public class FTPUploader {
	
	public String username = "";
    public String password = "";
    public String ip = "";
    public Integer port = 21;
 
    public FTPUploader(String username, String password, String ip, int port) {
        this.username = username;
        this.password = password;
        this.ip = ip;
        this.port = port;
    }
 
    public static void main(String[] args) {
    	//			364383
    	//访问路径								http://op.test.restoplus.cn/upload/image/ftp/lmx.jpg
    	//设置 tomcat IP 访问限制				http://blog.csdn.net/z507263441/article/details/40742075
    	//Linux中如何添加/删除FTP用户并设置权限		http://www.tuicool.com/articles/YRfEn2A
    	FTPUploader  ftpUploader = new FTPUploader("ftptest", "123456",
                "139.196.222.42", 21);
    	
    	File f = new File("D:\\www(1).zip");
        try {
            ftpUploader.upload(f, "www(1).zip", "/");
            System.out.println("上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    
    public void upload(File file, String remoteFileName, String remoteFilePath)
            throws Exception {
        upload(ip, port, username, password, file, remoteFilePath,
                remoteFileName);
    }
 
    private void upload(String ip, int port, String userName, String password,
            File file, String remotePathName, String remoteName)
            throws Exception {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip, port);
            ftpClient.login(userName, password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            upload(ftpClient, file, remotePathName, remoteName);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("upload to ftp faild");
        } finally {
            if (ftpClient != null && ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                }
            }
        }
    }
 
    private void upload(FTPClient ftpClient, File file, String remotePathName,
            String remoteName) throws Exception {
        changeDirectory(ftpClient, remotePathName);
        uploadFile(ftpClient, file, remotePathName, remoteName);
        backToRootDirectory(ftpClient);
    }
 
    private void changeDirectory(FTPClient ftpClient, String path)
            throws IOException {
        int nextSeperator = path.indexOf("/", 1);
        String currentPath = null;
        if (nextSeperator < 0) {
            nextSeperator = path.length();
            currentPath = path.substring(1, nextSeperator);
            changeDirectory0(ftpClient, currentPath);
            return;
        } else {
            currentPath = path.substring(1, nextSeperator);
            changeDirectory0(ftpClient, currentPath);
            changeDirectory(ftpClient, path.substring(nextSeperator));
        }
    }
 
    private void changeDirectory0(FTPClient ftpClient, String path)
            throws IOException {
        if (!ftpClient.changeWorkingDirectory(path)) {
            ftpClient.makeDirectory(path);
        }
        ftpClient.changeWorkingDirectory(path);
    }
 
    private void backToRootDirectory(FTPClient ftpClient) throws IOException {
        ftpClient.changeWorkingDirectory("/");
    }
 
    private void uploadFile(FTPClient ftpClient, File file,
            String remotePathName, String remoteName) throws Exception {
        String localPathName = file.getAbsolutePath();
        FTPFile[] files = ftpClient.listFiles(remoteName);
        if (files.length == 1) {
            if (!ftpClient.deleteFile(remoteName)) {
                throw new Exception("fail to delete remote file ["
                        + remotePathName + "] before uploading");
            }
        }
        File f = new File(localPathName);
        InputStream is = new FileInputStream(f);
        if (!ftpClient.storeFile(remoteName, is)) {
            is.close();
        }
        is.close();
    }
}