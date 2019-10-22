package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.constant.TicketType;
import com.resto.shop.web.constant.TicketTypeNew;
import com.resto.shop.web.dao.KitchenPrinterDtoMapper;
import com.resto.shop.web.dao.PrinterMapper;
import com.resto.shop.web.model.Printer;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.service.PrinterService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
/**
 *
 */
@Component
@Service
public class PrinterServiceImpl extends GenericServiceImpl<Printer, Integer> implements PrinterService {

    @Resource
    private PrinterMapper printerMapper;

    @Autowired
	private ShopDetailService shopDetailService;

    @Autowired
	private KitchenPrinterDtoMapper kitchenPrinterDtoMapper;

    @Autowired
	private PosService posService;

    @Override
    public GenericDao<Printer, Integer> getDao() {
        return printerMapper;
    }

	@Override
	public List<Printer> selectListByShopId(String shopId) {
		return printerMapper.selectListByShopId(shopId);
	}



	@Override
	public List<Printer> selectByShopAndType(String orderId, int reception) {
		return printerMapper.selectByShopAndType(orderId,reception);
	}


	@Override
	public Integer checkError(String shopId) {
		return printerMapper.checkError(shopId) ;
	}

	@Override
	public List<Printer> selectQiantai(String shopId,Integer type) {
		return printerMapper.selectQiantai(shopId,type);
	}

	@Override
	public Map<String, Object> openCashDrawer(String orderId,String shopId) {
    	Printer printer = printerMapper.getCashPrinter(shopId);
		Map<String, Object> result = new HashMap();
		if(printer != null){
			result.put("TABLE_NO","2");
			result.put("KITCHEN_NAME",printer.getName());
			result.put("PORT",printer.getPort());
			result.put("ORDER_ID",orderId);
			result.put("IP",printer.getIp());
			String print_id = ApplicationUtils.randomUUID();
			result.put("PRINT_TASK_ID", print_id);
			result.put("ADD_TIME", new Date().getTime());
			Map<String, Object> data = new HashMap<>();
			result.put("DATA",data);
			result.put("STATUS", 0);
			result.put("TICKET_TYPE", TicketType.OPENCASHDRAW);
		}
		return result;
	}


	@Override
	public Map<String, Object> openCashDrawerNew(String orderId, String shopId) {
		Printer printer = printerMapper.getCashPrinter(shopId);
		Map<String, Object> result = new HashMap();
		if(printer != null){
			result.put("TABLE_NO","2");
			result.put("KITCHEN_NAME",printer.getName());
			result.put("PORT",printer.getPort());
			result.put("ORDER_ID",orderId);
			result.put("IP",printer.getIp());
			String print_id = ApplicationUtils.randomUUID();
			result.put("PRINT_TASK_ID", print_id);
			result.put("ADD_TIME", new Date().getTime());
			Map<String, Object> data = new HashMap<>();
			result.put("DATA",data);
			result.put("STATUS", 0);
			result.put("TICKET_TYPE", TicketTypeNew.COMMAND);
			result.put("TICKET_MODE", TicketTypeNew.OPEN_CASH_DRAWER);
		}
		return result;
	}

	@Override
	public List<Printer> selectListNotSame(String shopId) {
		List<Printer> ticket =  printerMapper.selectTicketNotSame(shopId);
		List<Printer> label =  printerMapper.selectLabelNotSame(shopId);
		List<Printer> result = new ArrayList<>();
		result.addAll(ticket);
		result.addAll(label);
		return result;
	}

	@Override
	public List<Printer> selectPrintByType(String shopId, Integer type) {
		return printerMapper.selectPrintByType(shopId, type);
	}

	@Override
	public void deletePrinter(Integer id, String shopId, String brandId) {
		ShopDetail shopDetail = shopDetailService.selectShopByStatus(brandId,shopId);
    	if(shopDetail.getEnableDuoDongXian() !=1){
			kitchenPrinterDtoMapper.deleteByShopIdAndPrinterId(id,shopId);
		}
		super.delete(id);
	}

	@Override
	public int insertPrinter(Printer printer,String brandId) {
		int count = printerMapper.insertSelective(printer);
		//消息通知newpos后台发生变化
		try{
			ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
			shopMsgChangeDto.setBrandId(brandId);
			shopMsgChangeDto.setShopId(printer.getShopDetailId());
			shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBPRINTER);
			shopMsgChangeDto.setType("add");
			shopMsgChangeDto.setId(printer.getId().toString());
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
			log.error("添加打印机时给newpos推送失败！");
		}
		return count;
	}

	@Override
	public void updateSpareIpByIp(String ip, String spareIp) {
		printerMapper.updateSpareIpByIp(ip, spareIp);
	}
}
