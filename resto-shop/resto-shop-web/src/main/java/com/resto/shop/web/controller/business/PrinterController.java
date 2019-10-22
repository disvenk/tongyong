package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.shop.web.config.SessionKey;
import com.resto.shop.web.constant.PrinterRange;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Printer;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.service.PrinterService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("printer")
public class PrinterController extends GenericController{

	@Resource
	PrinterService printerService;

	@Resource
	PosService posService;
	
	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public List<Printer> listData(){

		return printerService.selectListByShopId(getCurrentShopId());
	}
	@RequestMapping("/list_printer")
	@ResponseBody
	public Result listPrinter(){
		Map<String, Object> map = new HashMap<>();
		List<Printer> printers = printerService.selectListByShopId(getCurrentShopId());
		return getSuccessResult(map);
	}

	@RequestMapping("/qiantai")
	@ResponseBody
	public List<Printer> qiantai(){
		return printerService.selectQiantai(getCurrentShopId(), PrinterRange.QUYU);
	}

	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Integer id){
		Printer printer = printerService.selectById(id);
		return getSuccessResult(printer);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid Printer printer,HttpServletRequest request){
		printer.setShopDetailId(request.getSession().getAttribute(SessionKey.CURRENT_SHOP_ID).toString());
		printerService.insertPrinter(printer, getCurrentBrandId());

		//联动修改别的该ip打印机的备用打印机ip
		printerService.updateSpareIpByIp(printer.getIp(), printer.getSpareIp());
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Printer printer){
		if (printer.getBillOfAccount() == null){
			printer.setBillOfAccount(0);
		}
		if (printer.getBillOfConsumption() == null){
			printer.setBillOfConsumption(0);
		}
		if (printer.getSupportTangshi() == null){
			printer.setSupportTangshi(0);
		}
		if (printer.getSupportWaidai() == null){
			printer.setSupportWaidai(0);
		}
		if (printer.getSupportWaimai() == null){
			printer.setSupportWaimai(0);
		}
		printerService.update(printer);

		//联动修改别的该ip打印机的备用打印机ip
		printerService.updateSpareIpByIp(printer.getIp(), printer.getSpareIp());

		//消息通知newpos后台发生变化
		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
		shopMsgChangeDto.setBrandId(getCurrentBrandId());
		shopMsgChangeDto.setShopId(getCurrentShopId());
		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBPRINTER);
		shopMsgChangeDto.setType("modify");
		shopMsgChangeDto.setId(printer.getId().toString());
		try{
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
		}
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Integer id){
		printerService.deletePrinter(id,getCurrentShopId(),getCurrentBrandId());
//		//消息通知newpos后台发生变化
//		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
//		shopMsgChangeDto.setBrandId(getCurrentBrandId());
//		shopMsgChangeDto.setShopId(getCurrentShopId());
//		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBPRINTER);
//		shopMsgChangeDto.setType("delete");
//		shopMsgChangeDto.setId(id.toString());
//		try{
//			posService.shopMsgChange(shopMsgChangeDto);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		return Result.getSuccess();
	}
}
