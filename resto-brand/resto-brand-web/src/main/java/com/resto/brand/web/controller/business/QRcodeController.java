package com.resto.brand.web.controller.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.resto.brand.web.controller.GenericController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.QRCodeUtil;
import com.resto.brand.web.service.BrandService;

/**
 * 用于生成普通二维码
 * @author Lmx
 *
 */
@Controller
@RequestMapping("qrcode")
public class QRcodeController extends GenericController {

	@Resource
	BrandService brandService;

	@RequestMapping("/list")
	public void list() {
	}

	@RequestMapping("/run")
	@ResponseBody
	public Result run(String content,HttpServletResponse response,HttpServletRequest request) {
		FileInputStream fis = null;
		File file = null;
	    response.setContentType("image/gif");
	    String fileName = System.currentTimeMillis()+"";
	    try {
	    	QRCodeUtil.createQRCode(content, getFilePath(request, null), fileName);
	        OutputStream out = response.getOutputStream();
	        file = new File(getFilePath(request, fileName));
	        fis = new FileInputStream(file);
	        byte[] b = new byte[fis.available()];
	        fis.read(b);
	        out.write(b);
	        out.flush();
	    } catch (Exception e) {
	         e.printStackTrace();
	    } finally {
	        if (fis != null) {
	            try {
	               fis.close();
	            } catch (IOException e) {
	            	e.printStackTrace();
	            }   
	        }
	        System.gc();//手动回收垃圾，清空文件占用情况，解决无法删除文件
	        file.delete();
	    }
		return null;
	}
	

}
