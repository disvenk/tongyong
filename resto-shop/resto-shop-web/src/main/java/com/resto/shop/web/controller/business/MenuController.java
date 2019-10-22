 package com.resto.shop.web.controller.business;

 import com.resto.brand.core.entity.Result;
 import com.resto.brand.web.model.BrandSetting;
 import com.resto.brand.web.model.ShopDetail;
 import com.resto.brand.web.service.BrandSettingService;
 import com.resto.brand.web.service.ShopDetailService;
 import com.resto.shop.web.controller.GenericController;
 import com.resto.shop.web.dto.MenuNumDto;
 import com.resto.shop.web.model.Menu;
 import com.resto.shop.web.model.MenuArticle;
 import com.resto.shop.web.model.MenuShop;
 import com.resto.shop.web.model.ShopBrandUser;
 import com.resto.shop.web.service.*;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;
 import org.springframework.web.servlet.ModelAndView;

 import javax.validation.Valid;
 import java.math.BigDecimal;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.List;

@Controller
@RequestMapping("menu")
public class MenuController extends GenericController{

	@Autowired
	MenuService menuService;

	@Autowired
	private BrandSettingService brandSettingService;

	@Autowired
	private ShopBrandUserService shopBrandUserService;

	@Autowired
	private ShopDetailService shopDetailService;

	@Autowired
	private MenuShopService menushopService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private MenuArticleService menuarticleService;
	
	@RequestMapping("/tangshi_list")
    public ModelAndView tangshi_list(){
		BrandSetting setting = brandSettingService.selectByBrandId(getCurrentBrandId());
		if(setting != null && setting.getOpenArticleLibrary().equals(new Integer(1))){ //开启菜品库功能
			return new ModelAndView("menu/tangshi_list");
		}else{
			return new ModelAndView("menu/none");
		}
    }

	 @RequestMapping("/waimai_list")
	 public ModelAndView list(){
		 BrandSetting setting = brandSettingService.selectByBrandId(getCurrentBrandId());
		 if(setting != null && setting.getOpenArticleLibrary().equals(new Integer(1))){ //开启菜品库功能
			 return new ModelAndView("menu/waimai_list");
		 }else{
			 return new ModelAndView("menu/none");
		 }
	 }

	@RequestMapping("/tangshiListAll")
	@ResponseBody
	public List<Menu> tangshiListAll(String menuState){
		List<Menu> list = menuService.selectMenuTypeList(menuState,1);
		list.forEach(s ->{
				if(s.getCreateUser()!=null){
					ShopBrandUser createUser = shopBrandUserService.selectById(s.getCreateUser());
					s.setCreateUser(createUser.getUsername());
				}
			    if(s.getUpdateUser()!=null) {
					ShopBrandUser updateUser = shopBrandUserService.selectById(s.getUpdateUser());
				   s.setUpdateUser(updateUser.getUsername());
			    }
		});
		return list;
	}

	@RequestMapping("/tangshiNum")
	@ResponseBody
	public MenuNumDto tangshiNum(){
		MenuNumDto menuNumDto=new MenuNumDto();
		menuNumDto.setAllNum(menuService.selectMenuTypeList("-1",1).size());
		menuNumDto.setEnableNum(menuService.selectMenuTypeList("1",1).size());
		menuNumDto.setNotEnableNum(menuService.selectMenuTypeList("0",1).size());
		return menuNumDto;
	}

	@RequestMapping("/waimaiNum")
	@ResponseBody
	public MenuNumDto waimaiNum(){
		MenuNumDto menuNumDto=new MenuNumDto();
		menuNumDto.setAllNum(menuService.selectMenuTypeList("-1",2).size());
		menuNumDto.setEnableNum(menuService.selectMenuTypeList("1",2).size());
		menuNumDto.setNotEnableNum(menuService.selectMenuTypeList("0",2).size());
		return menuNumDto;
	}

	 @RequestMapping("/waimaiListAll")
	 @ResponseBody
	 public List<Menu> waimaiListAll(String menuState){
		 List<Menu> list = menuService.selectMenuTypeList(menuState,2);
		 list.forEach(s ->{
			 if(s.getCreateUser()!=null){
				 ShopBrandUser createUser = shopBrandUserService.selectById(s.getCreateUser());
				 s.setCreateUser(createUser.getUsername());
			 }
			 if(s.getUpdateUser()!=null) {
				 ShopBrandUser updateUser = shopBrandUserService.selectById(s.getUpdateUser());
				 s.setUpdateUser(updateUser.getUsername());
			 }
		 });
		 return list;
	 }
	
	@RequestMapping("list_one")
	@ResponseBody
	public Result list_one(Long id){
		Menu menu = menuService.selectById(id);
		return getSuccessResult(menu);
	}
	
	@RequestMapping("create")
	@ResponseBody
	public Result create(@Valid Menu menu){
		menu.setCreateUser(getCurrentUserId());
		menu.setMenuVersion("1.0");
		if(menu.getStartTime().equals("")){
			menu.setStartTime(null);
		}
		if(menu.getEndTime().equals("")){
			menu.setEndTime(null);
		}
		menuService.insert(menu);
		return Result.getSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public Result modify(@Valid Menu menu) throws ParseException {
		Date date = new Date();//获得系统时间.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
		String nowTime = sdf.format(date);
		Date time = sdf.parse(nowTime);
		if(menu.getMenuState()==false){
			List<MenuShop> menuShops = menushopService.selectByMenuId(menu.getId().toString());
			if(menuShops!=null && !menuShops.isEmpty()){
				List<String> sList=new ArrayList<>();
				menuShops.forEach(p -> {
					sList.add(p.getShopDetailId());
				});
				List<MenuShop> list = menushopService.checkShopMenu(sList,menu.getMenuType().intValue());
				if(list!=null && !list.isEmpty()){
					list.forEach(o -> {
						o.setState(false);
						o.setUpdateTime(time);
						menushopService.update(o);
						articleService.insertMenuArticle(o.getMenuId(),o.getShopDetailId(),2);
					});
				}
			}
		}
		menu.setUpdateUser(getCurrentUserId());
		menu.setUpdateTime(nowTime);
		if(menu.getStartTime().equals("")){
			menu.setStartTime(null);
		}
		if(menu.getEndTime().equals("")){
			menu.setEndTime(null);
		}
		menuService.update(menu);
		return Result.getSuccess();
	}

	 @RequestMapping("copy")
	 @ResponseBody
	 public Result copy(String id){
		 Menu menu = menuService.selectById(Long.valueOf(id));
		 List<MenuArticle> menuArticles = menuarticleService.selectListMenuId(menu.getId().toString());
		 if(menu.getParentId()!=null){
			 List<Menu> list = menuService.selectParentIdList(menu.getParentId());
			 if(list!=null && !list.isEmpty()){
				 BigDecimal num1 = new BigDecimal(list.get(0).getMenuVersion());
				 BigDecimal num2 = new BigDecimal("0.1");
				 BigDecimal num=num1.add(num2);
				 menu.setMenuVersion(num.toString());
				 menu.setParentId(menu.getParentId());
			 }
		 }else{
			 List<Menu> list = menuService.selectParentIdList(menu.getId().intValue());
			 if(list!=null && !list.isEmpty()){
				 BigDecimal num1 = new BigDecimal(list.get(0).getMenuVersion());
				 BigDecimal num2 = new BigDecimal("0.1");
				 BigDecimal num=num1.add(num2);
				 menu.setMenuVersion(num.toString());
				 menu.setParentId(menu.getId().intValue());
			 }else{
				 BigDecimal num1 = new BigDecimal(menu.getMenuVersion());
				 BigDecimal num2 = new BigDecimal("0.1");
				 BigDecimal num=num1.add(num2);
				 menu.setMenuVersion(num.toString());
				 menu.setParentId(menu.getId().intValue());
			 }
		 }
		 menu.setCreateUser(getCurrentUserId());
		 menu.setUpdateUser(null);
		 menu.setUpdateTime(null);
		 menu.setId(null);
		 menu.setMenuState(true);
		 menuService.insert(menu,menuArticles);
		 return Result.getSuccess();
	 }
	
	@RequestMapping("delete")
	@ResponseBody
	public Result delete(Long id){
		menuService.delete(id);
		return Result.getSuccess();
	}

	@RequestMapping("/byShop")
	@ResponseBody
	public List<ShopDetail> byShop(String name){
		List<ShopDetail> list = shopDetailService.selectByShopAndByName(getCurrentBrandId(),name);
		return list;
	}
}
