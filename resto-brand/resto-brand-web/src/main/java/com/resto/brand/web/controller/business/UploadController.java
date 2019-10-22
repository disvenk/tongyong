package com.resto.brand.web.controller.business;

import com.resto.brand.core.entity.PictureResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.FileUpload;
import com.resto.brand.core.util.OssUtil;
import com.resto.brand.core.util.UploadFilesUtil;
import com.resto.brand.web.controller.GenericController;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@RequestMapping("upload")
@RestController
public class UploadController extends GenericController {


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
		PictureResult picResult = UploadFilesUtil.uploadPic(finalFile);
		if(picResult.getError() == 0){
			return getSuccessResult(picResult.getUrl());
		}else{
			return new Result(false);
		}
	}
}
