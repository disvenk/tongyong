 package com.resto.shop.web.controller.business;

 import com.resto.brand.core.entity.Result;
 import com.resto.brand.web.service.RedisService;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.model.Account;
 import com.resto.shop.web.service.AccountService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;

 import javax.annotation.Resource;
 import javax.validation.Valid;
 import java.util.List;

@Controller
@RequestMapping("account")
public class AccountController extends GenericController{

	@Resource
	AccountService accountService;
	
	@Autowired
	RedisService redisService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<Account> listData(){
		return accountService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(String id){
		Account account = accountService.selectById(id);
		return getSuccessResult(account);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid Account brand){
		accountService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Account brand){
		accountService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(String id){
		accountService.delete(id);
		return Result.getSuccess();
	}

	@RequestMapping("test")
	public void test(){
		redisService.set("test","1",100L);
		String value = String.valueOf(redisService.get("test"));
		System.out.println(redisService.get("test"));
	}
}
