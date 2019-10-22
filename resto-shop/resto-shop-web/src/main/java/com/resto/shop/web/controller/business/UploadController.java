package com.resto.shop.web.controller.business;

import com.aliyun.oss.OSSClient;
import com.resto.brand.core.entity.PictureResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.FileUpload;
import com.resto.brand.core.util.OssPictureUtils;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Article;
import com.resto.shop.web.service.ArticleService;
import com.resto.shop.web.util.UploadFilesUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequestMapping("upload")
@RestController
public class UploadController extends GenericController {

	@Resource
	private BrandService brandService;
	
	@Resource
	private ShopDetailService shopDetailService; 

	@Resource
	private ArticleService articleService;

	// @RequestMapping("file")
	// public String uploadFile(MultipartFile file,HttpServletRequest request){
	// String type = request.getParameter("type");
	// String systemPath = request.getServletContext().getRealPath("");
	// systemPath = systemPath.replaceAll("\\\\", "/");
	// int lastR = systemPath.lastIndexOf("/");
	// systemPath = systemPath.substring(0,lastR)+"/";
	// String filePath = "upload/files/"+DateFormatUtils.format(new Date(),
	// "yyyy-MM-dd");
	// File finalFile = FileUpload.fileUp(file,
	// systemPath+filePath,UUID.randomUUID().toString(),type);
	// return filePath+"/"+finalFile.getName();
	// }

	@RequestMapping("file")
	@ResponseBody
	public Result uploadFile(MultipartFile file, HttpServletRequest request) throws IOException {
		String type = request.getParameter("type");
		String systemPath = request.getServletContext().getRealPath("");
		systemPath = systemPath.replaceAll("\\\\", "/");
		int lastR = systemPath.lastIndexOf("/");
		systemPath = systemPath.substring(0, lastR) + "/";
		String filePath = "upload/files/" + DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		File finalFile = FileUpload.fileUp(file, systemPath + filePath, UUID.randomUUID().toString(), type);
//        //测试
//        OSSClient ossClient = new OSSClient("https://oss-cn-shanghai.aliyuncs.com", "LTAIRTuAQCP1sqDG", "jPC6yvVyqX1vFcSTeyjgLrRmSIh46W");
//        PutObjectResult putObjectResult =  ossClient.putObject(new PutObjectRequest("canjia", "test.jpg", finalFile));
//        putObjectResult.getETag();

		PictureResult picResult = UploadFilesUtil.uploadPic(finalFile);
		if(picResult.getError() == 0){
			return getSuccessResult(picResult.getUrl());
		}else{
			return new Result(false);
		}
	}


//	@RequestMapping("moveFile")
//	@ResponseBody
	public Result moveFile() throws Exception {
		Brand barnd = brandService.selectById(getCurrentBrandId());
		String baseUrl = barnd.getWechatImgUrl();
		List<ShopDetail> shopDetails = shopDetailService.selectByBrandId(getCurrentBrandId());
//		for(ShopDetail shopDetail : shopDetails ){
			String shopId = getCurrentShopId();
			List<Article> list = articleService.selectList(shopId);
			List<Article> newList = new ArrayList<>();
			System.out.println("【店铺ID】："+shopId+"\n【菜品数量】："+list.size());
			for (Article article : list) {
				try {
		            Thread.sleep(1000);
		        } catch (InterruptedException e) {
		            System.out.println("睡眠失败");
		        }
				if (article.getPhotoSmall()!=null && article.getPhotoSmall().startsWith("upload/files/")) {
					String oldPath = baseUrl + article.getPhotoSmall();
					String localPath = download(article.getId(),oldPath);
					if(localPath!=null && localPath!=""){
						File file = new File(localPath);
						PictureResult result = UploadFilesUtil.uploadPic(file);
						System.out.println("【上传到资源服务器成功】"+result.getUrl());
						if (result.getError() == 0) {//判断是否成功
							Article updateArticle = new Article();
							updateArticle.setId(article.getId());
							updateArticle.setPhotoSmall(result.getUrl());
							newList.add(updateArticle);
						}
					}
				} else {
	                    System.out.println("\n【该菜品没有上传图片】Id：" + article.getId());
                }
			}
			for (Article article : newList) {
				try {
		            Thread.sleep(500);
		        } catch (InterruptedException e) {
		            System.out.println("睡眠失败");
		        }
				articleService.updateArticleImg(article);
				System.out.println("【修改成功】articleId："+article.getId());
			}
			System.out.println("【修改菜品信息成功】\n shopID："+shopId+"\n 数量："+newList.size());
//		}
		return new Result("上传成功！", true);
	}

	@RequestMapping("moveResourceFile")
  @ResponseBody
    public Result moveResourceFile() throws Exception {
	    String baseUrl = "C:/Users/yz/Desktop/picture/";
        List<Article> list = articleService.selectHasResourcePhotoList(getCurrentBrandId());//查询菜品中有 资源服务器的图片
        OSSClient ossClient = new OSSClient(OssPictureUtils.ENDPOINT, OssPictureUtils.ACCESSKEYID, OssPictureUtils.ACCESSKEYSECRET);
        //        //测试
        if(!list.isEmpty()){
            for(Article article :list){
                String oldpath = article.getPhotoSmall().replace("http://106.14.44.167/group1/M00/",""); //oldpath=00/06/ag4sp1hQsYyAZjt5AABv9qtrYEI785.jpg
                String key = article.getPhotoSmall().substring(38,article.getPhotoSmall().length());
                //拼接文件
                File finalFile = new File(baseUrl+oldpath);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("睡眠失败");
                }
                if(!finalFile.exists()){
                    //说明是5-19后上传的图片
                    System.out.println(article.getPhotoSmall());
                }else{
                    //上传到Oss上
//                    PutObjectResult putObjectResult =  ossClient.putObject(new PutObjectRequest("articlepicture", key, finalFile));
//                    System.out.println("是否是上传的图片"+putObjectResult.getETag().equals(key));
                    //更新url
                    article.setPhotoSmall("https://articlepicture.oss-cn-shanghai.aliyuncs.com/"+key);
                    articleService.update(article);
                }
            }
        }

        return Result.getSuccess();
    }



    public static String download(String id,String urlString) throws Exception {
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
				String fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
				File file = new File("D:\\temp\\" + fileName);
				FileOutputStream fops = new FileOutputStream(file);
				fops.write(btImg);
				fops.flush();
				fops.close();
				localPath = "D:\\temp\\" + urlString.substring(urlString.lastIndexOf("/") + 1);
			} else {
				System.out.println("没有从该连接获得内容");
			}

		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("【上传失败】id："+id+"   url："+urlString);
		}
		return localPath;
	}



}
