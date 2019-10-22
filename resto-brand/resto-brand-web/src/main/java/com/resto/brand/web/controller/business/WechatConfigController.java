package com.resto.brand.web.controller.business;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.FileUpload;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.WechatConfigService;

@Controller
@RequestMapping("wechatconfig")
public class WechatConfigController extends GenericController{

	@Resource
	WechatConfigService wechatconfigService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<WechatConfig> listData(){
		return wechatconfigService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		WechatConfig wechatconfig = wechatconfigService.selectById(id);
		return getSuccessResult(wechatconfig);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid WechatConfig wechatConfig){
		wechatconfigService.insert(wechatConfig);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid WechatConfig wechatConfig){
		if(wechatConfig.getPayCertPath().length() < 10 ||wechatConfig.getPayCertPath().indexOf(".p12") == -1){
			wechatConfig.setPayCertPath("");
		}
		wechatconfigService.update(wechatConfig);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		wechatconfigService.delete(id);
		return Result.getSuccess();
	}
	
	@RequestMapping("uploadFile")
	@ResponseBody
	public String uploadFile(MultipartFile file,HttpServletRequest request){
		String filePath = "/root/uploads/payChartFiles/";
		File finalFile= FileUpload.fileUp(file, filePath,UUID.randomUUID().toString(),null);
		log.info("路径为："+finalFile.getAbsolutePath());
		return finalFile.getAbsolutePath();
	}
}
