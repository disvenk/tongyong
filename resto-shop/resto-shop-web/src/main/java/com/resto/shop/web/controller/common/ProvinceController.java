package com.resto.shop.web.controller.common;

import com.resto.brand.web.model.Area;
import com.resto.brand.web.model.City;
import com.resto.brand.web.model.District;
import com.resto.brand.web.model.Province;
import com.resto.brand.web.service.AreaService;
import com.resto.brand.web.service.CityService;
import com.resto.brand.web.service.DistrictService;
import com.resto.brand.web.service.ProvinceService;
import com.resto.shop.web.controller.GenericController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;


/**
 * 省份 城市 区（县）
 **/
@Controller
@RequestMapping(value = "/province")
public class ProvinceController extends GenericController{
	
    @Resource
    private ProvinceService provinceService;
    @Resource
    private CityService cityService;
    @Resource
    private DistrictService districtService;
    @Resource
    private AreaService brandAreaService;

    @RequestMapping("/list")
    public void list(){
    }

    @RequestMapping("/list_area")
    @ResponseBody
    public List<Area> list_area(){

        List<Area> areas = brandAreaService.selectList();

        return areas;
    }

	@RequestMapping("/list_province")
	@ResponseBody
	public List<Province> list_province(){

        List<Province> provinces = provinceService.selectList();

        return provinces;
	}

    @RequestMapping("/list_city")
    @ResponseBody
    public List<City> list_city(){

        List<City> cities = cityService.selectList();


        return cities;
    }
    @RequestMapping("/list_district")
    @ResponseBody
    public List<District> list_district(){
        List<District> districts = districtService.selectList();
        return districts;
    }



}
