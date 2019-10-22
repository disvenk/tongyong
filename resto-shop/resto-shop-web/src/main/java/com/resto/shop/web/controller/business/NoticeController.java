package com.resto.shop.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.config.SessionKey;
import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.Notice;
import com.resto.shop.web.service.NoticeService;

@Controller
@RequestMapping("notice")
public class NoticeController extends GenericController{

	@Resource
	NoticeService noticeService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<Notice> listData(){
		return noticeService.selectListAllByShopId(getCurrentShopId());
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		Notice notice = noticeService.selectById(id);
		notice.setSupportTimes(noticeService.getSupportTime(id));
		return getSuccessResult(notice);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid Notice notice,HttpServletRequest request){
		notice.setShopDetailId(request.getSession().getAttribute(SessionKey.CURRENT_SHOP_ID).toString());
		Notice no = noticeService.create(notice);
		noticeService.bindSupportTime(no);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Notice brand){
		noticeService.update(brand);
		noticeService.bindSupportTime(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("chageStatus")
	@ResponseBody
	public Result chageStatus(Notice notice){
		noticeService.update(notice);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		noticeService.delete(id);
		return Result.getSuccess();
	}
}
