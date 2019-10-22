package com.resto.brand.core.util;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;

import java.io.File;
import java.net.URL;
/**
 * Created by KONATA on 2017/2/20.
 */
public class FtpTool {



    /**
     * 获取FTP客户端对象
     *
     * @return
     * @throws Exception
     */
    public static FTPClient getClient(String path,String userName,String passWord) throws Exception {
        FTPClient client = new FTPClient();
        client.setCharset("utf-8");
        client.setType(FTPClient.TYPE_BINARY);
        client.connect(path);
        client.login(userName,passWord);
        return client;
    }
    /**
     * 注销客户端连接
     *
     * @param client
     *            FTP客户端对象
     * @throws Exception
     */
    private void logout(FTPClient client) throws Exception {
        if (client != null) {
            try {
                // 有些FTP服务器未实现此功能，若未实现则会出错
                client.logout(); // 退出登录
            } catch (FTPException fe) {
            } catch (Exception e) {
                throw e;
            } finally {
                if (client.isConnected()) { // 断开连接
                    client.disconnect(true);
                }
            }
        }
    }
    /**
     * 创建目录
     *
     * @param client
     *            FTP客户端对象
     * @param dir
     *            目录
     * @throws Exception
     */
    public static void mkdirs(FTPClient client, String dir) throws Exception {
        if (dir == null) {
            return;
        }
        dir = dir.replace("//", "/");
        String[] dirs = dir.split("/");
        for (int i = 0; i < dirs.length; i++) {
            dir = dirs[i];
            if (!StringUtils.isEmpty(dir)) {
                if (!exists(client, dir)) {
                    client.createDirectory(dir);
                }
                client.changeDirectory(dir);
            }
        }
    }
    /**
     * 获取FTP目录
     *
     * @param url
     *            原FTP目录
     * @param dir
     *            目录
     * @return
     * @throws Exception
     */
    private URL getURL(URL url, String dir) throws Exception {
        String path = url.getPath();
        if (!path.endsWith("/") && !path.endsWith("//")) {
            path += "/";
        }
        dir = dir.replace("//", "/");
        if (dir.startsWith("/")) {
            dir = dir.substring(1);
        }
        path += dir;
        return new URL(url, path);
    }

    /**
     * 判断文件或目录是否存在
     *
     * @param client
     *            FTP客户端对象
     * @param dir
     *            文件或目录
     * @return
     * @throws Exception
     */
    public static boolean exists(FTPClient client, String dir) throws Exception {
        return getFileType(client, dir) != -1;
    }
    /**
     * 判断当前为文件还是目录
     *
     * @param client
     *            FTP客户端对象
     * @param dir
     *            文件或目录
     * @return -1、文件或目录不存在 0、文件 1、目录
     * @throws Exception
     */
    public static int getFileType(FTPClient client, String dir) throws Exception {
        FTPFile[] files = null;
        try {
            files = client.list(dir);
        } catch (Exception e) {
            return -1;
        }
        if (files.length > 1) {
            return FTPFile.TYPE_DIRECTORY;
        } else if (files.length == 1) {
            FTPFile f = files[0];
            if (f.getType() == FTPFile.TYPE_DIRECTORY) {
                return FTPFile.TYPE_DIRECTORY;
            }
            String path = dir + "/" + f.getName();
            try {
                int len = client.list(path).length;
                if (len == 1) {
                    return FTPFile.TYPE_DIRECTORY;
                } else {
                    return FTPFile.TYPE_FILE;
                }
            } catch (Exception e) {
                return FTPFile.TYPE_FILE;
            }
        } else {
            try {
                client.changeDirectory(dir);
                client.changeDirectoryUp();
                return FTPFile.TYPE_DIRECTORY;
            } catch (Exception e) {
                return -1;
            }
        }
    }

    /**
     * 上传目录
     *
     * @param client
     *            FTP客户端对象
     * @param parentUrl
     *            父节点URL
     * @param file
     *            目录
     * @throws Exception
     */
    private void uploadFolder(FTPClient client, URL parentUrl, File file,
                              boolean del) throws Exception {
        client.changeDirectory(parentUrl.getPath());
        String dir = file.getName(); // 当前目录名称
        URL url = getURL(parentUrl, dir);
        if (!exists(client, url.getPath())) { // 判断当前目录是否存在
            client.createDirectory(dir); // 创建目录
        }
        client.changeDirectory(dir);
        File[] files = file.listFiles(); // 获取当前文件夹所有文件及目录
        for (int i = 0; i < files.length; i++) {
            file = files[i];
            if (file.isDirectory()) { // 如果是目录，则递归上传
                uploadFolder(client, url, file, del);
            } else { // 如果是文件，直接上传
                client.changeDirectory(url.getPath());
                client.upload(file);
                if (del) { // 删除源文件
                    file.delete();
                }
            }
        }
    }

    /**
     * 删除目录
     *
     * @param client
     *            FTP客户端对象
     * @param url
     *            FTP URL
     * @throws Exception
     */
    private void deleteFolder(FTPClient client, URL url) throws Exception {
        String path = url.getPath();
        client.changeDirectory(path);
        FTPFile[] files = client.list();
        String name = null;
        for (FTPFile file : files) {
            name = file.getName();
            // 排除隐藏目录
            if (".".equals(name) || "..".equals(name)) {
                continue;
            }
            if (file.getType() == FTPFile.TYPE_DIRECTORY) { // 递归删除子目录
                deleteFolder(client, getURL(url, file.getName()));
            } else if (file.getType() == FTPFile.TYPE_FILE) { // 删除文件
                client.deleteFile(file.getName());
            }
        }
        client.changeDirectoryUp();
        client.deleteDirectory(url.getPath()); // 删除当前目录
    }
    /**
     * 下载文件夹
     *
     * @param client
     *            FTP客户端对象
     * @param url
     *            FTP URL
     * @param localDir
     *            本地存储目录
     * @throws Exception
     */
    private void downloadFolder(FTPClient client, URL url, String localDir)
            throws Exception {
        String path = url.getPath();
        client.changeDirectory(path);
        // 在本地创建当前下载的文件夹
        File folder = new File(localDir + "/" + new File(path).getName());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        localDir = folder.getAbsolutePath();
        FTPFile[] files = client.list();
        String name = null;
        for (FTPFile file : files) {
            name = file.getName();
            // 排除隐藏目录
            if (".".equals(name) || "..".equals(name)) {
                continue;
            }
            if (file.getType() == FTPFile.TYPE_DIRECTORY) { // 递归下载子目录
                downloadFolder(client, getURL(url, file.getName()), localDir);
            } else if (file.getType() == FTPFile.TYPE_FILE) { // 下载文件
                client.download(name, new File(localDir + "/" + name));
            }
        }
        client.changeDirectoryUp();
    }

}
