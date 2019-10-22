package com.resto.shop.web.controller.business;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.WriterException;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.FileToZip;
import com.resto.brand.core.util.QRCodeUtil;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.controller.GenericController;

@RequestMapping("qrcode")
@Controller
public class QrCodeController extends GenericController{
	@Resource
	ShopDetailService shopDetailService;
	@Resource
	BrandService brandService;

	@RequestMapping("/list")
	public void list() {
	}

	@RequestMapping("/queryBrands")
	@ResponseBody
	public List<Brand> queryBrands() {
		List<Brand> brands = brandService.selectList();
		return brands;
	}

	@RequestMapping("/queryShops")
	@ResponseBody
	public List<ShopDetail> queryShops() {
		List<ShopDetail> shops = shopDetailService.selectByBrandId(getCurrentBrandId());
		return shops;
	}

	@RequestMapping("/run")
	@ResponseBody
	public Result run(String shopId,String shopName, Integer beginTableNumber, Integer endTableNumber, String ignoreNumber ,HttpServletRequest request)
			throws IOException, InterruptedException, WriterException {
		Brand brand = brandService.selectById(getCurrentBrandId());
		String brandSign = brand.getBrandSign();
		String fileSavePath = getFilePath(request,null);
		deleteFile(new File(fileSavePath));//删除历史生成的文件
		String filepath = getFilePath(request,shopName);
		for (int i = beginTableNumber; i <= endTableNumber; i++) {//循环生成二维码
			if (ignoreNumber(i,ignoreNumber)) {
				String fileName =  i + ".jpg" ;
				String contents = getShopUrl(brandSign, shopId, i);
				QRCodeUtil.createQRCode(contents, filepath, fileName);
			}
		}
		//打包
		FileToZip.fileToZip(filepath, fileSavePath, shopName);
		Result result = new Result(true);
		result.setMessage(shopName+".zip");
		System.gc();//手动回收垃圾，清空文件占用情况，解决无法删除文件
		return result;
	}

	@RequestMapping("/downloadFile")
	public String donloadFile(String fileName ,HttpServletRequest request,
							  HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
		String downLoadPath = getFilePath(request,fileName);
		try {
			long fileLength = new File(downLoadPath).length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename="
					+ new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null){
				bis.close();
			}
			if (bos != null){
				bos.close();
			}
		}
		return null;
	}

	/**
	 * 判断是否包含 要忽略的值
	 * @param index
	 * @param ignoreNumber
	 * @return
	 */
	public boolean ignoreNumber(int index,String ignoreNumber){
		boolean flag = true;
		if(ignoreNumber!=null && !("").equals(ignoreNumber)){
			ignoreNumber = ignoreNumber.replaceAll("，", ",");
			String[] ignoreNumbers = ignoreNumber.split(",");//保存当前要忽略的值
			for(String number : ignoreNumbers){
				if((index + "").indexOf(number) != -1){
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 得到 店铺专属的链接
	 * @param brandSign	品牌标识
	 * @param shopId	店铺ID
	 * @param tableNumber 座号
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getShopUrl(String brandSign,String shopId,int tableNumber) throws UnsupportedEncodingException{
		String url = "http://"+brandSign+".restoplus.cn/wechat/index?subpage=tangshi&shopId=" + shopId
				+ "&tableNumber=" + tableNumber;
		return url;
	}

	/**
	 * 得到要 保存 二维码文件夹的路径
	 * @param request
	 * @param fileName
	 * @return
	 */
	public String getFilePath(HttpServletRequest request,String fileName){
		String systemPath = request.getServletContext().getRealPath("");
		systemPath = systemPath.replaceAll("\\\\", "/");
		int lastR = systemPath.lastIndexOf("/");
		systemPath = systemPath.substring(0,lastR)+"/";
		String filePath = "qrCodeFiles/";
		if(fileName!=null){
			filePath += fileName;
		}
		return systemPath+filePath;
	}

	/**
	 * 删除上次生成的二维码文件
	 */
	public static void deleteFile(File file){
		System.gc();//手动回收垃圾，清空文件占用情况，解决无法删除文件
		if(file.isFile() && file.exists()){//表示该文件不是文件夹
			file.delete();
		}else if(file.isDirectory()){
			//首先得到当前的路径
			String[] childFilePaths = file.list();
			for(String childFilePath : childFilePaths){
				File childFile=new File(file.getAbsolutePath()+File.separator+childFilePath);
				deleteFile(childFile);
			}
			file.delete();
		}
	}

	
	@RequestMapping("/qrRun")
	@ResponseBody
	public Result qrRun(String content,HttpServletResponse response,HttpServletRequest request) {
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
