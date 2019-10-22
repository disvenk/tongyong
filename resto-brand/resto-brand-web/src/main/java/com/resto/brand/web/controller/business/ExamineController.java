package com.resto.brand.web.controller.business;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.resto.brand.core.entity.email.Email;
import com.resto.brand.core.enums.State;
import com.resto.brand.core.util.EmailUtil;
import com.resto.brand.web.model.Contract;
import com.resto.brand.web.model.Income;
import com.resto.brand.web.service.ContractService;
import com.resto.brand.web.service.IncomeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.Examine;
import com.resto.brand.web.service.ExamineService;

@Controller
@RequestMapping("examine")
public class ExamineController extends GenericController{

	@Resource
	ExamineService examineService;


	@Resource
	IncomeService incomeService;

	@Resource
	ContractService contractService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<Examine> listData(){
    	List<Examine> list = examineService.selectList();
    	if(!list.isEmpty()){
			for (Examine examine:list) {
				if(examine.getRemark().length()>30){
					examine.setRemark(examine.getRemark().substring(0,30)+"....");
				}
			}
		}
    	return list;
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		Examine examine = examineService.selectById(id);
		return getSuccessResult(examine);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid Examine examine){
		Result r = new Result();
		r.setSuccess(true);
		Contract contract = contractService.selectByConstractNum(examine.getConstractNum());
		if(examine.getType()==1){//判断如果是品牌上线
			//获取该合同全部收入 和 合同签约金额
			List<Income> incomeList = incomeService.selectListByContractId(contract.getId());
			BigDecimal total = BigDecimal.ZERO;
			if(!incomeList.isEmpty()){
				for(Income income:incomeList){
					total = total.add(income.getPayMoney());
				}
			}
			//判断收的钱是否大于 签约金额    需求更改支付金额不足签约金额时也可以上线  2017-09-25  WTL
//			if(total.compareTo(contract.getSignMoney())<0){
//				r.setMessage("该合同进款总额小于签约金额。。");
//				r.setSuccess(false);
//			}
			//判断该合同编号是否已经上线
			Examine examine2 = examineService.selectByContractIdAndType(contract.getId(),examine.getType());

			if(examine2!=null){
				r.setSuccess(false);
				r.setMessage("该合同已经上线了。。");
				return r;
			}

		}else if(examine.getType()==2){//免金额发票
			//在合同上添加 已开发票 全部可开发票(已收款总额) 剩余可开
//			if(new BigDecimal(1000).compareTo(examine.getMoney())>0){//可开发票小于 申请
//				r.setMessage("该合同可开发票金额小于申请金额");
//				r.setSuccess(false);
//			}
		}else if(examine.getType()==3){//如果是退款
			//todo 页面有问题

			//测试
		}

		examine.setContractId(contract.getId());
		examineService.insert(examine);
		return r;
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Examine examine){
		examineService.update(examine);
		examine = examineService.selectById(examine.getId());
		//邮件标题
		String emailTitle = "${type}申请已被${status}";
		Map<String, String> emailTitleMap = new HashMap<>();
		emailTitleMap.putIfAbsent("type", typeName(examine.getType()));
		emailTitleMap.putIfAbsent("status", statusName(examine.getStatus()));
		StrSubstitutor substitutor = new StrSubstitutor(emailTitleMap);
		emailTitle = substitutor.replace(emailTitle);
		//邮件内容
		String emailboby =  "</br>审核类型：${typeName}" +
							"</br>${brandName}" +
							"</br>申请部门：${department}" +
							"</br>申请人：${userName}" +
							"</br>联系邮箱：${emailUrl}" +
							"</br>申请备注：${remark}" +
							"</br></br>审核状态：${status}" +
							"</br>回复：${reply}" +
							"</br><a href = 'http://bo.restoplus.cn/resto'>查看详情</a>";
		//邮件内容相关信息集合
		Map<String, String> emailBobyMap = new HashMap<>();
		//申请类型
		emailBobyMap.putIfAbsent("typeName", typeName(examine.getType()));
		//品牌名称
		if (examine.getType().equals(2)){
			emailBobyMap.putIfAbsent("brandName", "发票金额：￥"+examine.getMoney()+"</br>品牌：" + examine.getBrandName());
		}else {
			emailBobyMap.putIfAbsent("brandName", "品牌：" + examine.getBrandName());
		}
		//申请部门
		emailBobyMap.putIfAbsent("department", examine.getDepartment());
		//申请人
		emailBobyMap.putIfAbsent("userName", examine.getApplicant());
		//联系邮箱
		emailBobyMap.putIfAbsent("emailUrl", examine.getEmail());
		//申请备注
		emailBobyMap.putIfAbsent("remark", StringUtils.isNotBlank(examine.getRemark()) ? examine.getRemark() : "无");
		//审核状态
		emailBobyMap.putIfAbsent("status", (examine.getStatus().equals(1) ? "<font color = 'green'>" : "<font color = 'red'>") + statusName(examine.getStatus()) + "</font>");
		//回复
		emailBobyMap.putIfAbsent("reply", StringUtils.isNotBlank(examine.getReview()) ? examine.getReview() : "无");
		substitutor = new StrSubstitutor(emailBobyMap);
		emailboby = substitutor.replace(emailboby);
		EmailUtil.sendEmail(new Email("smtp.exmail.qq.com", "luo.liang@restoplus.cn", "luo.liang@restoplus.cn", "123456Lll", null, examine.getEmail(), null, emailTitle, emailboby, null));
		return Result.getSuccess();
	}

	@RequestMapping("modifyStatus")
	@ResponseBody
	public Result modifyStatus(Long id){
		Examine examine = examineService.selectById(id);
		examine.setStatus(State.DELETE);
		examineService.update(examine);
		return Result.getSuccess();
	}


	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		examineService.delete(id);
		return Result.getSuccess();
	}

	/**
	 * 得到当前申请类型
	 * @param type
	 * @return
	 */
	private final String typeName(Integer type){
		switch (type)
		{
			case 1:
				return "品牌上线";
			case 2:
				return "'开具发票'";
			case 3:
				return "退款";
			default:
				return "未知";
		}
	}

	/**
	 * 得到当前审核状态
	 * @param status
	 * @return
	 */
	private final String statusName(Integer status){
		switch (status){
			case 0:
				return "待审核";
			case 1:
				return "批准";
			case -1:
				return "驳回";
			default:
				return "未知";
		}
	}
}
