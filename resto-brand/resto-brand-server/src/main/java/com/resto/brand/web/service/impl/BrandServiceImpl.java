package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.dao.*;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 *
 */
@Component
@Service
public class BrandServiceImpl extends GenericServiceImpl<Brand, String> implements BrandService {

	@Resource
	private DatabaseConfigMapper databaseConfigMapper;
	@Resource
	private WechatConfigMapper wechatConfigMapper;
	@Resource
	private ShopDetailMapper shopDetailMapper;
    @Resource
    private BrandMapper brandMapper;
    @Resource
    private SmsAcountMapper smsAcountMapper;
    @Resource
	private DatabaseConfigMapper databaseconfigMapper;
    @Resource
    private BrandSettingService brandSettingService;

    @Resource
    private WeBrandService weBrandService;
    @Resource
    private WeBrandScoreService weBrandScoreService;

    @Resource
    private TemplateService templateService;

    @Resource
    private TemplateFlowMapper templateFlowMapper;

    static final String GET_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=";

    static final String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    @Override
    public GenericDao<Brand, String> getDao() {
        return brandMapper;
    }

	/**
	 * 根据BrandId 查询品牌信息
	 * @param brandId
	 * @return
	 */
	public Brand selectByPrimaryKey(String brandId){
    	return brandMapper.selectByPrimaryKey(brandId);
	}

    /**
     * 查询 品牌状态为1的品牌详细信息，包含该品牌的数据库配置和微信配置
     */
    @Override
	public List<Brand> selectBrandDetailInfo() {
		return brandMapper.selectBrandDetailInfo();
	}
    
    /**
     * 添加 品牌 的信息（包括关联的表）
     */
	@Override
	public void insertInfo(Brand brand) {
		//生成  WechatConfig 和 DatabaseConfig 表的 ID 值
		String wechatConfigId = ApplicationUtils.randomUUID();
		String databaseConfigId = ApplicationUtils.randomUUID();
		String brandId = ApplicationUtils.randomUUID();
		String smsAcountId = ApplicationUtils.randomUUID();
		Date date = new Date();
		//赋值
		WechatConfig wechatConfig = brand.getWechatConfig();
		wechatConfig.setId(wechatConfigId);
		
		DatabaseConfig databaseConfig = brand.getDatabaseConfig();
//		databaseConfig.setUsername(Encrypter.encrypt(databaseConfig.getUsername()));
//		databaseConfig.setPassword(Encrypter.encrypt(databaseConfig.getPassword()));
		databaseConfig.setId(databaseConfigId);
		databaseConfig.setCreateTime(date);
		
		SmsAcount smsAcount = brand.getSmsAcount();
		smsAcount.setId(smsAcountId);;
		smsAcount.setBrandId(brandId);
		smsAcount.setSmsSign(brand.getBrandSign());
		String smsRemind = smsAcount.getSmsRemind().replaceAll("，", ",");
		smsAcount.setSmsRemind(smsRemind);
		
		//添加数据
		databaseConfigMapper.insertSelective(databaseConfig);
		wechatConfigMapper.insertSelective(wechatConfig);
		smsAcountMapper.insertSelective(smsAcount);
		//关联 Brand 表的 外键
		brand.setDatabaseConfigId(databaseConfigId);
		brand.setWechatConfigId(wechatConfigId);
		brand.setId(brandId);
		brand.setCreateTime(date);
		
		//新增品牌设置
		String brandSettingId = ApplicationUtils.randomUUID();
		brand.setBrandSettingId(brandSettingId);
		BrandSetting brandSetting = new BrandSetting();
		brandSetting.setId(brandSettingId);
		brandSettingService.insertSelective(brandSetting);

		//插入小程序需要的数据
        WeBrand weBrand = new WeBrand();
        weBrand.setBrandId(brand.getId());
        weBrand.setBrandName(brand.getBrandName());
        weBrandService.insert(weBrand);

        //插入微信消息模板id
//        List<Template> list=templateService.selectList();
//        for(Template template:list){
//            String appid = wechatConfig.getAppid();
//            String appsecret=wechatConfig.getAppsecret();
//            Map<String, Object> data = new HashMap<String, Object>();
//            data.put("template_id_short", template.getTemplateNumber());
//            //获取token
//            String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
//            // 发起GET请求获取凭证
//            net.sf.json.JSONObject jsonObject = HttpClientUtil.httpsRequest(requestUrl, "GET", null);
//            String accessToken = jsonObject.getString("access_token");
//            String url = GET_TEMPLATE + accessToken;
//            String jsonData = JSON.toJSONString(data);
//            String res = "";
//            try {
//                res = HttpRequest.post(url).send(jsonData.getBytes("utf-8")).body();
//            } catch (HttpRequest.HttpRequestException | UnsupportedEncodingException e) {
//            }
//            JSONObject access = new JSONObject(res);
//            String templateId = access.optString("template_id");
//			if(!"".equals(templateId)){
//				TemplateFlow templateFlow = new TemplateFlow();
//				templateFlow.setAppid(appid);
//				templateFlow.setTemplateNumber(template.getTemplateNumber());
//				templateFlow.setTemplateId(templateId);
//				templateFlowMapper.insertSelective(templateFlow);
//			}
//        }

		brandMapper.insertSelective(brand);
		
	} 
	
	/**
	 * 查询出 品牌 的信息（包括关联的表）
	 */
	@Override
	public Brand selectById(String id) {
		Brand brand = brandMapper.selectByPrimaryKey(id);
		WechatConfig wechatConfig = wechatConfigMapper.selectByPrimaryKey(brand.getWechatConfigId());
		DatabaseConfig databaseConfig = databaseConfigMapper.selectByPrimaryKey(brand.getDatabaseConfigId());
		//将查询出的关联内容 保存在实体中
		brand.setWechatConfig(wechatConfig);
		brand.setDatabaseConfig(databaseConfig);
		return super.selectById(id);
	}

	/**
	 * 修改 品牌 信息（包括关联的表）
	 * @param brand
	 */
	@Override
	public void updateInfo(Brand brand) {
		//验证 品牌标识 是否重复
		if(validataBrandInfo(brand.getBrandSign(),brand.getId())){
			wechatConfigMapper.updateByPrimaryKey(brand.getWechatConfig());
			DatabaseConfig databaseConfig = brand.getDatabaseConfig();
			databaseConfig.setUpdateTime(new Date());
			DatabaseConfig dc = databaseconfigMapper.selectByPrimaryKey(databaseConfig.getId());
			//加密
//			if(databaseConfig.getUsername().equals("就不告诉你")){
				databaseConfig.setUsername(dc.getUsername());
//			}else{
//				databaseConfig.setUsername(Encrypter.encrypt	(databaseConfig.getUsername()));
//			}
//			if(databaseConfig.getPassword().equals("就不告诉你")){
				databaseConfig.setPassword(dc.getPassword());
//			}else{
//				databaseConfig.setPassword(Encrypter.encrypt(databaseConfig.getPassword()));
//			}
			databaseConfigMapper.updateByPrimaryKey(databaseConfig);
			SmsAcount smsAcount = brand.getSmsAcount();
			String smsRemind = smsAcount.getSmsRemind().replaceAll("，", ",");
			smsAcount.setSmsRemind(smsRemind);
			smsAcountMapper.updateByPrimaryKeySelective(smsAcount);
			brandMapper.updateByPrimaryKeySelective(brand);
		}
	}

	
	/**
	 * 查询 品牌 信息 和 该品牌旗下的店铺
	 * @return
	 */
	@Override
	public List<Brand> queryBrandAndShop() {
		List<Brand> brands = brandMapper.selectIdAndName();
		for(Brand brand : brands){
			List<ShopDetail> shopDetails = shopDetailMapper.selectIdAndName(brand.getId());
			brand.setShopDetail(shopDetails);
		}		
		return brands;
	}

	@Override
	public Brand selectBySign(String brandSign) {
		Brand brand= brandMapper.selectBySign(brandSign);
		WechatConfig config = wechatConfigMapper.selectByPrimaryKey(brand.getWechatConfigId());
		BrandSetting brandSetting = brandSettingService.selectById(brand.getBrandSettingId());
		brand.setWechatConfig(config);
		brand.setBrandSetting(brandSetting);
		return brand;
	}
	
	/**
	 * 返回 true 表示验证通过
	 * @param brandSign
	 * @param brandId
	 * @return
	 */
	@Override
	public boolean validataBrandInfo(String brandSign,String brandId) {
		boolean flag = false;
		if(StringUtils.isBlank(brandId)){
			int brandSignCount = brandMapper.validataParam("brand_sign", brandSign);
			if(brandSignCount <= 0){
				flag = true;
			}
		}else{
			Brand brand = brandMapper.selectByPrimaryKey(brandId);
			if(brand.getBrandSign().equals(brandSign)){
				flag = true;
			}else{
				int brandSignCount = brandMapper.validataParam("brand_sign", brandSign);
				if(brandSignCount <= 0){
					flag = true;
				}
			}
		}
		return flag;
	}

	@Override
	public void deleteInfo(String brandId) {
		Brand brand = brandMapper.selectByPrimaryKey(brandId);
		brandMapper.deleteByPrimaryKey(brandId);
		wechatConfigMapper.deleteByPrimaryKey(brand.getWechatConfigId());
		databaseConfigMapper.deleteByPrimaryKey(brand.getDatabaseConfigId());
	}

	@Override
	public Brand selectBrandBySetting(String settingId) {
		return brandMapper.selectBrandBySetting(settingId);
	}
}
