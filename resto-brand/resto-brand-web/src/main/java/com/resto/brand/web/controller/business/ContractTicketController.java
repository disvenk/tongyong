 package com.resto.brand.web.controller.business;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.brand.web.model.ContractTicket;
import com.resto.brand.web.service.ContractTicketService;

@Controller
@RequestMapping("contractticket")
public class ContractTicketController extends GenericController{

	@Resource
	ContractTicketService contractticketService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<ContractTicket> listData(){
		return contractticketService.selectList();
	}
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		ContractTicket contractticket = contractticketService.selectById(id);
		return getSuccessResult(contractticket);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid ContractTicket brand){
		contractticketService.insert(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid ContractTicket brand){
		contractticketService.update(brand);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		contractticketService.delete(id);
		return Result.getSuccess();
	}
}
