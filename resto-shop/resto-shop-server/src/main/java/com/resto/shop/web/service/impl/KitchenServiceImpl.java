package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.dao.KitchenGroupDetailMapper;
import com.resto.shop.web.dao.KitchenMapper;
import com.resto.shop.web.dao.KitchenPrinterDtoMapper;
import com.resto.shop.web.dto.kitchenPrinterDto;
import com.resto.shop.web.model.ArticleKitchen;
import com.resto.shop.web.model.Kitchen;
import com.resto.shop.web.model.OrderItem;
import com.resto.shop.web.model.Printer;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.KitchenService;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
@Service
public class KitchenServiceImpl extends GenericServiceImpl<Kitchen, Integer> implements KitchenService {

    @Resource
    private KitchenMapper kitchenMapper;


    @Autowired
	private KitchenPrinterDtoMapper KitchenPrinterDtoMapper;

    @Autowired
	private ShopDetailService shopDetailService;
	@Autowired
	private PosService posService;

	@Autowired
	private KitchenGroupDetailMapper kitchenGroupDetailMapper;
    @Override
    public GenericDao<Kitchen, Integer> getDao() {
        return kitchenMapper;
    }

	@Override
	public List<Kitchen> selectListByShopId(String shopId) {
		List<Kitchen> kitchens = kitchenMapper.selectListByShopId(shopId);
		return kitchens;
	}

	@Override
	public List<Kitchen> selectListByShopIdByStatusAll(String brandId, String shopId) {
		List<Kitchen> kitchens = kitchenMapper.selectListByShopId(shopId);
		ShopDetail shopDetail = shopDetailService.selectShopByStatus(brandId,shopId);
		if (shopDetail.getEnableDuoDongXian()!=1){
			if(kitchens !=null && kitchens.size()>0){
				for (Kitchen kitchen : kitchens){
                    List<Printer> printers = new ArrayList<>();
                    List<kitchenPrinterDto> kitchenPrinterDtos = KitchenPrinterDtoMapper.selectByKitchenAndShopId(kitchen.getId(), shopId);
					StringBuffer sb = new StringBuffer();
					if(kitchenPrinterDtos != null){
						for (int i = 0 ; i < kitchenPrinterDtos.size() ; i++){
                            Printer printer = new Printer();
							printer.setId(kitchenPrinterDtos.get(i).getPrinterId());
							printer.setName(kitchenPrinterDtos.get(i).getPrinterName());
							if(i==kitchenPrinterDtos.size()-1){
								sb.append(kitchenPrinterDtos.get(i).getPrinterName());
							}else{
								sb.append(kitchenPrinterDtos.get(i).getPrinterName()+",");
							}
							printers.add(printer);
						}
					}
					kitchen.setPrinters(printers);
					kitchen.setPrinterName(sb.toString());
					if(kitchen.getBeginTime()!= null && kitchen.getEndTime()!= null){
						kitchen.setEnableTime(kitchen.getBeginTime()+"至"+kitchen.getEndTime());
					}else{
						kitchen.setEnableTime("00:00至23:59");
					}
					if (kitchen.getStatus()!=1){
						kitchen.setStatusName("开启");
					}else{
						kitchen.setStatusName("未开启");
					}
					if (kitchen.getSort()==null){
						kitchen.setSort(1);
					}
				}
			}
		}
		return kitchens;
	}

	@Override
	public List<Kitchen> selectListByShopIdByStatus(String brandId,String shopId) {
		List<Kitchen> kitchens = kitchenMapper.selectListByShopIdByStatus(shopId);
			if(kitchens !=null && kitchens.size()>0){
				for (Kitchen kitchen : kitchens){
					List<kitchenPrinterDto> kitchenPrinterDtos = KitchenPrinterDtoMapper.selectByKitchenAndShopId(kitchen.getId(), shopId);
					StringBuffer sb = new StringBuffer();
					if(kitchenPrinterDtos != null){
						for (int i = 0 ; i < kitchenPrinterDtos.size() ; i++){
							if(i==kitchenPrinterDtos.size()-1){
								sb.append(kitchenPrinterDtos.get(i).getPrinterName());
							}else{
								sb.append(kitchenPrinterDtos.get(i).getPrinterName()+",");
							}
						}
					}
					kitchen.setPrinterName(sb.toString());
				}

		}
		return kitchens;
	}

	@Override
	public void insertSelective(Kitchen kitchen) {
    	kitchen.setBeginTime("00:00");
    	kitchen.setEndTime("23:59");
        kitchenMapper.insertSelective(kitchen);
		ShopDetail shopDetail = shopDetailService.selectShopByStatus(kitchen.getBrandId(),kitchen.getShopDetailId());
		if (shopDetail.getEnableDuoDongXian()!=1){
			kitchen.setId(kitchen.getId());
			this.insertKitchenAndPrinter(kitchen);
		}
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(kitchen.getBrandId());
        shopMsgChangeDto.setShopId(kitchen.getShopDetailId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBKITCHEN);
        shopMsgChangeDto.setType("add");
        shopMsgChangeDto.setId(kitchen.getId().toString());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
	}

	@Override
	public void updateKitchenStatus(Integer kitchenId, Integer status,String shopId) {
		kitchenMapper.updateKitchenStatus(kitchenId,status,shopId);
	}

	@Override
	public void insertKitchenAndPrinter(Kitchen kitchen){
		kitchenPrinterDto kitchenPrinterDto = new kitchenPrinterDto();
		kitchenPrinterDto.setBrandId(kitchen.getBrandId());
		kitchenPrinterDto.setShopId(kitchen.getShopDetailId());
		kitchenPrinterDto.setKitchenId(kitchen.getId());
		if (kitchen.getCiprinterList() !=null ){
			for(Integer printerId :kitchen.getCiprinterList()){
				kitchenPrinterDto.setPrinterId(printerId);
				KitchenPrinterDtoMapper.insertSelective(kitchenPrinterDto);
			}
		}
	}

	@Override
	public void saveArticleKitchen(String articleId, Integer[] kitchenList,String brandId,String shopId) {
		kitchenMapper.deleteArticleKitchen(articleId);
		//消息通知newpos后台发生变化
		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
		shopMsgChangeDto.setBrandId(brandId);
		shopMsgChangeDto.setShopId(shopId);
		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEKITCHEN);
		shopMsgChangeDto.setType("delete");
		shopMsgChangeDto.setId(articleId);
		try{
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(kitchenList!=null&&kitchenList.length>0){
			kitchenMapper.insertArticleKitchen(articleId, kitchenList);
			for(Integer num:kitchenList){
				//消息通知newpos后台发生变化
				ShopMsgChangeDto kshopMsgChangeDto=new ShopMsgChangeDto();
				kshopMsgChangeDto.setBrandId(brandId);
				kshopMsgChangeDto.setShopId(shopId);
				kshopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEKITCHEN);
				kshopMsgChangeDto.setType("add");
				kshopMsgChangeDto.setId(num.toString());
				try{
					posService.shopMsgChange(shopMsgChangeDto);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<Integer> selectIdsByArticleId(String articleId) {
		return kitchenMapper.selectIdsByArticleId(articleId);
	}

	@Override
	public List<Kitchen> selectInfoByArticleId(String articleId) {
		return kitchenMapper.selectInfoByArticleId(articleId);
	}

	@Override
	public Kitchen selectMealKitchen(OrderItem mealItems) {
		Kitchen kitchen = kitchenMapper.selectKitchenByMealsItemId(mealItems.getId());
		return kitchen;
	}

	@Override
	public Kitchen selectKitchenByOrderItem(OrderItem item,List<Long> mealAttrId) {
		return kitchenMapper.selectKitchenByOrderItem(item,mealAttrId);
	}

	@Override
	public List<Long> getMealAttrId(OrderItem orderItem) {
		return kitchenMapper.getMealAttrId(orderItem);
	}

	@Override
	public Kitchen getItemKitchenId(OrderItem orderItem) {
		return kitchenMapper.getItemKitchenId(orderItem);
	}

	@Override
	public List<ArticleKitchen> selectArticleKitchenByShopId(String shopId) {
		return kitchenMapper.selectArticleKitchenByShopId(shopId);
	}

	@Override
	public int update(Kitchen kitchen) {
		ShopDetail shopDetail = shopDetailService.selectShopByStatus(kitchen.getBrandId(),kitchen.getShopDetailId());
		if (shopDetail.getEnableDuoDongXian()!=1){
			KitchenPrinterDtoMapper.deleteByShopIdAndKitchenId(kitchen.getId(),kitchen.getShopDetailId());
			this.insertKitchenAndPrinter(kitchen);
		}
        return super.update(kitchen);
	}
	@Override
	public Integer deleteAndBrandId(Integer kitchenId,String brandId,String shopId) {
//		ShopDetail shopDetail = shopDetailService.selectShopByStatus(brandId,shopId);
			KitchenPrinterDtoMapper.deleteByShopIdAndKitchenId(kitchenId,shopId);
			kitchenGroupDetailMapper.deleteByShopDetailIdAndKitchenId(shopId,kitchenId);
		return super.delete(kitchenId);
	}

	@Override
	public Integer selectShopStatus(String brandId, String shopId) {
		ShopDetail shopDetail = shopDetailService.selectShopByStatus(brandId, shopId);
		return shopDetail.getEnableDuoDongXian();
	}

}
