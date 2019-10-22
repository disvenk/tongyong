package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.dao.BrandSettingMapper;
import com.resto.brand.web.dao.TemplateFlowMapper;
import com.resto.brand.web.dao.WechatConfigMapper;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
@Service
public class BrandSettingServiceImpl extends GenericServiceImpl<BrandSetting, String> implements BrandSettingService {

    @Resource
    private BrandSettingMapper brandsettingMapper;
    @Resource
    BrandService brandService;

    @Resource
    BrandAccountService accountService;

    @Resource
	AccountSettingService accountSettingService;

    @Resource
	BrandAccountService brandAccountService;

	@Resource
	private TemplateService templateService;

	@Resource
	private TemplateFlowMapper templateFlowMapper;

	@Resource
	private WechatConfigMapper wechatConfigMapper;

	@Autowired
	WeChatService weChatService;

    @Override
    public GenericDao<BrandSetting, String> getDao() {
        return brandsettingMapper;
    }

	@Override
	public void insertSelective(BrandSetting brandSetting) {
		brandsettingMapper.insertSelective(brandSetting);
	}


	@Override
	public BrandSetting selectByBrandId(String brandId) {
		BrandSetting setting = brandsettingMapper.selectByBrandId(brandId);
		if(setting==null){
			setting = new BrandSetting();
			setting.setId(ApplicationUtils.randomUUID());
			this.insert(setting);
			Brand brand = brandService.selectById(brandId);
			brand.setBrandSettingId(setting.getId());
			brandService.update(brand);
		}
		return setting;
	}


	@Override
	public void updateWechatChargeConfig(String settingId, String configId) {
		brandsettingMapper.updateWechatChargeConfig(settingId, configId);
	}

    @Override
    public BrandSetting selectByAppid(String appid) {
        return brandsettingMapper.selectByAppid(appid);
    }

    @Override
    public List<BrandSetting> selectListByState() {
        return brandsettingMapper.selectListByState();
    }

    @Override
    public void updateBrandSetting(BrandSetting bs) {

		//获取品牌信息
		Brand brand = brandService.selectBrandBySetting(bs.getId());


		//判断是否已经有了账户
		BrandAccount account = accountService.selectByBrandId(brand.getId());

		AccountSetting accountSetting = accountSettingService.selectByBrandSettingId(bs.getId());

		if (bs.getOpenBrandAccount() != null && bs.getOpenBrandAccount() == 1) {//开启品牌账户功能
			if (null == account) {////并且以前没开启过(有账户)
				BrandAccount a = new BrandAccount();
				a.setBrandId(brand.getId());//品牌id
				a.setBrandSettingId(bs.getId());//品牌设置id
				a.setCreateTime(new Date());
				accountService.insert(a);
			}else {//开启品牌账户功能 -- 以前有开启过
				Integer id = account.getId();
				account = new BrandAccount();
				account.setId(id);
				account.setUpdateTime(new Date());
				accountService.update(account);
			}
			if (null == accountSetting) {
				//品牌账户设置默认值
				accountSetting = new AccountSetting();
				accountSetting.setBrandSettingId(bs.getId());
				BrandAccount b = brandAccountService.selectByBrandSettingId(bs.getId());
				if(b!=null){
					accountSetting.setAccountId(b.getId());
					accountSettingService.insert(accountSetting);
				}
			}else {
				Long id = accountSetting.getId();
				accountSetting = new AccountSetting();
				accountSetting.setId(id);
				accountSetting.setUpdateTime(new Date());
				accountSettingService.update(accountSetting);
			}
		}
		BrandSetting brandSetting = brandsettingMapper.selectByPrimaryKey(bs.getId());
		//开启微信模板消息升级版，添加对应的模板id
		if(!brandSetting.getTemplateEdition().equals(bs.getTemplateEdition())){
			if(bs.getTemplateEdition() != null && bs.getTemplateEdition()==1){
				List<Template> list=templateService.selectList();
				for(Template template:list){
					Brand brand_= brandService.selectBrandBySetting(bs.getId());
					WechatConfig wechatConfig = wechatConfigMapper.selectByPrimaryKey(brand_.getWechatConfigId());
					String res = weChatService.getTemplate(template.getTemplateNumber(),wechatConfig.getAppid(), wechatConfig.getAppsecret());
					if(!"".equals(res)) {
						JSONObject access = new JSONObject(res);
						String templateId = access.optString("template_id");
						if (!"".equals(templateId)) {
							TemplateFlow templateFlow = new TemplateFlow();
							templateFlow.setAppid(wechatConfig.getAppid());
							templateFlow.setTemplateNumber(template.getTemplateNumber());
							templateFlow.setTemplateId(templateId);
							templateFlowMapper.insertSelective(templateFlow);
						}
					}
				}
			}else{
				List<Template> list=templateService.selectList();
				for(Template template:list){
					Brand brand_ = brandService.selectBrandBySetting(bs.getId());
					WechatConfig wechatConfig = wechatConfigMapper.selectByPrimaryKey(brand_.getWechatConfigId());
					List<TemplateFlow> templateFlowList=templateFlowMapper.selectTemplateId(wechatConfig.getAppid(),template.getTemplateNumber());
					if(templateFlowList!=null&&!templateFlowList.isEmpty()){
						for(TemplateFlow templateFlow:templateFlowList){
							weChatService.delTemplate(templateFlow.getTemplateId(),wechatConfig.getAppid(),wechatConfig.getAppsecret());
						}
					}
					templateFlowMapper.deleteAllAppId(template.getTemplateNumber(),wechatConfig.getAppid());
				}
			}
		}
		//关闭品牌功能 --不用考虑
		brandsettingMapper.updateByPrimaryKeySelective(bs);
	}

    @Override
    public BrandSetting posSelectByBrandId(String brandId) {
        return brandsettingMapper.posSelectByBrandId(brandId);
    }

}
