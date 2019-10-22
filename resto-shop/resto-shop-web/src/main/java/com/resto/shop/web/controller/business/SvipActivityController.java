 package com.resto.shop.web.controller.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.service.BrandSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.SvipActivity;
import com.resto.shop.web.service.SvipActivityService;

@Controller
@RequestMapping("svipactivity")
public class SvipActivityController extends GenericController{

	@Resource
	SvipActivityService svipactivityService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/report")
	public String report(){
		return "svipreport/report";
	}

	@RequestMapping("/add")
	public String add(){
		return "svipactivity/add";
	}

	@RequestMapping("/list_all")
	@ResponseBody
	public List<SvipActivity> listData(){
		return svipactivityService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		SvipActivity svipactivity = svipactivityService.selectById(id);
		return getSuccessResult(svipactivity);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid SvipActivity svipactivity){
		svipactivity.setId(ApplicationUtils.randomUUID());
		if(svipactivity.getActivityName()==null || "".equals(svipactivity.getActivityName())){return new Result("活动名称不能为空",false);}
		if (svipactivity.getBeginDateTime() == null || svipactivity.getEndDateTime() == null) { return new Result("活动券开始或者结束时间不能为空", false); }
		if (svipactivity.getBeginDateTime().compareTo(svipactivity.getEndDateTime()) > 0) { return new Result("活动开始时间不能大于结束时间", false); }
		if(svipactivity.getSvipPrice()==null || BigDecimal.ZERO.compareTo(svipactivity.getSvipPrice())>=0){return new Result("付费会员价格不能为空或小于等于0",false);}
		if(svipactivity.getSvipExpireType()==0){if(svipactivity.getSvipExpire()==null || 0==svipactivity.getSvipExpire()){return new Result("会员有效周期不能为空",false);}}
		if(svipactivity.getActivityImg()==null || "".equals(svipactivity.getActivityImg())){return new Result("活动图片不能为空，请先上传图片后再保存",false);}

		Long opendAct = svipactivityService.getOpendAct();
		if(svipactivity.getActivityStatus()==1 && opendAct > 0){
			return new Result("只允许有一个活动为开启状态",false);
		}
		if(svipactivity.getSvipExpireType()==1){
			svipactivity.setSvipExpire(0);
		}
		svipactivity.setCreateTime(new Date());
		svipactivityService.insert(svipactivity);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid SvipActivity svipactivity){
		if(svipactivity.getActivityName()==null || "".equals(svipactivity.getActivityName())){return new Result("活动名称不能为空",false);}
		if (svipactivity.getBeginDateTime() == null || svipactivity.getEndDateTime() == null) { return new Result("活动券开始或者结束时间不能为空", false); }
		if (svipactivity.getBeginDateTime().compareTo(svipactivity.getEndDateTime()) > 0) { return new Result("活动开始时间不能大于结束时间", false); }
		if(svipactivity.getSvipPrice()==null || BigDecimal.ZERO.compareTo(svipactivity.getSvipPrice())>=0){return new Result("付费会员价格不能为空或小于等于0",false);}
		if(svipactivity.getSvipExpireType()==0){if(svipactivity.getSvipExpire()==null || 0==svipactivity.getSvipExpire()){return new Result("会员有效周期不能为空",false);}}
		if(svipactivity.getActivityImg()==null || "".equals(svipactivity.getActivityImg())){return new Result("活动图片不能为空，请先上传图片后再保存",false);}

		Long opendAct = svipactivityService.getOpendAct();
		Long myself = svipactivityService.getMyself(svipactivity.getId());

		if(svipactivity.getActivityStatus()==1 && opendAct > 0 && myself==0){
            //存在但不是自己
            return new Result("只允许有一个活动为开启状态",false);
        }
        if(svipactivity.getSvipExpireType()==1){
			svipactivity.setSvipExpire(0);
		}

		svipactivityService.update(svipactivity);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		svipactivityService.delete(id);
		return Result.getSuccess();
	}

	@RequestMapping("selectActById")
	@ResponseBody
	public SvipActivity selectActById(String id){
		SvipActivity svipActivity = svipactivityService.selectById(id);
		return svipActivity;
	}
}
