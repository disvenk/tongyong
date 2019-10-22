package com.resto.brand.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class TestController {
	 public static void main(String[] args) throws IOException, InterruptedException {
		int begin = 217;
		int end = 226;
		String shopId = "e6af4aad7e254735832ea226a39eed65";
		//String logo = "http://www.topscan.com/images/2013/sample.jpg";
		for(int i=begin;i<=end;i++){
			if((i+"").indexOf("4") == -1 && (i+"").indexOf("13") == -1){
				String domain="http://micheng.restoplus.cn/wechat/index?subpage=tangshi&shopId="+shopId+"&tableNumber="+i;
				
				requestCode(domain,"B_"+i+".jpg","D:/code/micheng_米房店/B区");
			}
			Thread.sleep(1000);
		}
//		String domain="http://micheng.restoplus.cn/wechat/index?subpage=tangshi&shopId="+shopId+"&tableNumber=282";
//		requestCode(domain,"C区_"+119+".jpg","D:/code/micheng_米星店/C区");
	}

	private static void requestCode(String domain,String filename,String filepath) throws IOException {
		String apiDomain="http://qr.topscan.com/api.php?text="+URLEncoder.encode(domain,"utf-8");
		 URL url = new URL(apiDomain);    
	        // 打开连接  
	        URLConnection con = url.openConnection();  
	        //设置请求超时为5s  
	        con.setConnectTimeout(5*1000);  
	        // 输入流  
	        InputStream is = con.getInputStream();  
	      
	        // 1K的数据缓冲  
	        byte[] bs = new byte[1024];  
	        // 读取到的数据长度  
	        int len;  
	        // 输出的文件流  
	       File sf=new File(filepath);  
	       if(!sf.exists()){  
	           sf.mkdirs();  
	       }  
	       OutputStream os = new FileOutputStream(sf.getPath()+"/"+filename);  
	        // 开始读取  
	        while ((len = is.read(bs)) != -1) {  
	          os.write(bs, 0, len);  
	        }  
	        // 完毕，关闭所有链接  
	        os.close();  
	        is.close();  
	        System.out.println("下载成功:"+sf.getAbsolutePath()+"/"+filename);
	}
}
