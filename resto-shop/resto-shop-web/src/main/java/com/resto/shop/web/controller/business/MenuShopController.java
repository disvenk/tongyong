 package com.resto.shop.web.controller.business;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.alibaba.fastjson.JSONObject;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.shop.web.CheckShopMenuDto;
import com.resto.shop.web.constant.Common;
import com.resto.shop.web.model.Menu;
import com.resto.shop.web.service.ArticleService;
import com.resto.shop.web.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.shop.web.controller.GenericController;
import com.resto.brand.core.entity.Result;
import com.resto.shop.web.model.MenuShop;
import com.resto.shop.web.service.MenuShopService;

@Controller
@RequestMapping("menushop")
public class MenuShopController extends GenericController{

	@Autowired
	private MenuShopService menushopService;

	@Autowired
	private ShopDetailService shopDetailService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	MenuService menuService;

	@RequestMapping("/list")
    public void list(){
    }

	@RequestMapping("/list_all")
	@ResponseBody
	public Result listData(){
        try {
            List<MenuShop> menuShopList = menushopService.selectList();
            menuShopList.forEach(menuShop -> {
                menuShop.setTypeDescribe(menuShop.getType().equals(Common.YES) ? "堂食菜单" : "外卖菜单");
                String shopName = getCurrentShopDetails().stream().filter(shopDetail -> shopDetail.getId().equalsIgnoreCase(menuShop.getShopDetailId()))
                        .map(ShopDetail::getName).findFirst().orElse("--");
                menuShop.setShopName(shopName);
            });
            JSONObject object = new JSONObject();
            //筛选出启用的菜单信息
            List<MenuShop> enableList = menuShopList.stream().filter(menuShop -> menuShop.getState()).collect(Collectors.toList());
            //筛选出禁用的菜单信息
            List<MenuShop> disableList = menuShopList.stream().filter(menuShop -> !menuShop.getState()).collect(Collectors.toList());
            object.put("enableList", enableList);
            object.put("enableListCount", enableList.size());
            object.put("disableList", disableList);
            object.put("disableListCount", disableList.size());
            return getSuccessResult(object);
        } catch (Exception e) {
            log.error("查询菜单列表出错：" + e.getMessage());
            e.printStackTrace();
            return new Result(false);
        }
    }

	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		MenuShop menushop = menushopService.selectById(id);
		return getSuccessResult(menushop);
	}

	@RequestMapping("listMenuShop")
	@ResponseBody
	public List<MenuShop> listMenuShop(String menuId){
		List<MenuShop> list = menushopService.selectByMenuId(menuId);
		return list;
	}

	@RequestMapping("create")
	@ResponseBody
	public Result create(@RequestBody List<MenuShop> menushops){
		List<String> sList=new ArrayList<>();
		menushops.forEach(p -> {
			sList.add(p.getShopDetailId());
		});
		List<MenuShop> list = menushopService.checkShopMenu(sList,menushops.get(0).getType());
		if(list!=null && !list.isEmpty()){
			list.forEach(o -> {
				menushopService.delete(o.getId());
				articleService.insertMenuArticle(o.getMenuId(),o.getShopDetailId(),2);
			});
		}
        articleService.insertMenuArticle(menushops.get(0).getMenuId(),null,2);
		menushopService.deleteMenuId(menushops.get(0).getMenuId().toString());
		Menu menu = menuService.selectById(menushops.get(0).getMenuId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		menushops.forEach(s -> {
			s.setBrandId(getCurrentBrandId());
			s.setState(true);
			s.setCreateTime(new java.sql.Date(System.currentTimeMillis()));
            if(menu.getMenuCycle()==2){
                try {
                    s.setStartTime(sdf.parse(menu.getStartTime()));
                    s.setEndTime(sdf.parse(menu.getEndTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
			menushopService.insert(s);
			articleService.insertMenuArticle(s.getMenuId(),s.getShopDetailId(),1);
		});
		return Result.getSuccess();
	}

	@RequestMapping("checkShopMenu")
	@ResponseBody
	public Result checkShopMenu(@RequestBody CheckShopMenuDto checkShopMenuDto){
		List<MenuShop> list = menushopService.checkShopMenu(checkShopMenuDto.getShops(),checkShopMenuDto.getType());
		if(list!=null && !list.isEmpty()){
			List<String> str=new ArrayList<>();
			list.forEach(s -> {
				if(!s.getMenuId().equals(checkShopMenuDto.getMenuId())){
					ShopDetail shopDetail = shopDetailService.selectById(s.getShopDetailId());
					str.add(shopDetail.getName());
				}
			});
			if(str.size()>0){
				return new Result(str.toString()+"门店已有启用的菜单，是否强制启用",false);
			}
		}
		return Result.getSuccess();
	}

	@RequestMapping("removeShop")
	@ResponseBody
	public Result removeShop(String menuId){
		List<MenuShop> menushops = menushopService.selectByMenuId(menuId);
		menushops.forEach(s -> {
			articleService.insertMenuArticle(Long.valueOf(menuId),s.getShopDetailId(),2);
		});
		menushopService.deleteMenuId(menuId);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid MenuShop menushop){
		menushopService.update(menushop);
		return Result.getSuccess();
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		menushopService.delete(id);
		return Result.getSuccess();
	}
}
