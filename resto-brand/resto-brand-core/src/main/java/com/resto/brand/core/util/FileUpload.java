package com.resto.brand.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件
 * 创建人：FH 创建时间：2014年12月23日
 * @version
 */
public class FileUpload {

	/**
	 * @param file 			//文件对象
	 * @param filePath		//上传路径
	 * @param fileName		//文件名
	 * @return  文件名
	 */
	public static File fileUp(MultipartFile file, String filePath, String fileName,String type){
		String extName = ""; // 扩展名格式：
		try {
			if (file.getOriginalFilename().lastIndexOf(".") >= 0){
				extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			}
			File upfile = copyFile(file.getInputStream(), filePath, fileName+extName,type);
			return upfile;
		} catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}
	
	/**
	 * 写文件到当前目录的upload目录中
	 * 
	 * @param in
	 * @param
	 * @throws IOException
	 */
	public static File copyFile(InputStream in, String dir, String realName,String type)
			throws IOException {
		File file = new File(dir, realName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		boolean isImage = false;
		String [] arr = {".jpg",".png",".gif","bmp"};
		for(int i = 0 ;i<arr.length;i++){
		    
		   if(realName.endsWith(arr[i])){
		       isImage=true;
		       try{
		    	   if("false".equals(type)){
		    		   FileUtils.copyInputStreamToFile(in, file);
		    	   }else{
		    		   ImgCompress.getFileOutPutStream(in, 500, 250, new FileOutputStream(file));
		    	   }
		       }catch(IOException e){
		           FileUtils.copyInputStreamToFile(in, file);
		       }
               break;
		   }
		   
		}
		
		if(!isImage){
		    FileUtils.copyInputStreamToFile(in, file);
		}
		System.out.println("save new file :"+file.getAbsolutePath());
		return file;
	}
}
