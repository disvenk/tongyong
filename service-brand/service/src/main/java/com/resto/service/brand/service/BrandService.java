package com.resto.service.brand.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.brand.entity.Brand;
import com.resto.service.brand.entity.DatabaseConfig;
import com.resto.service.brand.entity.WechatConfig;
import com.resto.service.brand.mapper.BrandMapper;
import com.resto.service.brand.mapper.DatabaseConfigMapper;
import com.resto.service.brand.mapper.WechatConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by KONATA on 2017/9/5.
 */
@Service
public class BrandService extends BaseService<Brand, String> {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private WechatConfigMapper wechatConfigMapper;

    @Autowired
    private DatabaseConfigMapper databaseConfigMapper;

    public BaseDao<Brand, String> getDao() {
        return brandMapper;
    }

    /**
     * 查询出 品牌 的信息（包括关联的表）
     */
    public Brand selectById(String id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        WechatConfig wechatConfig = wechatConfigMapper.selectByPrimaryKey(brand.getWechatConfigId());
        DatabaseConfig databaseConfig = databaseConfigMapper.selectByPrimaryKey(brand.getDatabaseConfigId());
        //将查询出的关联内容 保存在实体中
        brand.setWechatConfig(wechatConfig);
        brand.setDatabaseConfig(databaseConfig);
        return super.selectById(id);
    }

    public Brand selectBySign(String brandSign) {
        Brand brand= brandMapper.selectBySign(brandSign);
        WechatConfig config = wechatConfigMapper.selectByPrimaryKey(brand.getWechatConfigId());
        brand.setWechatConfig(config);
        return brand;
    }

    public Brand selectBrandBySetting(String settingId) {
        return brandMapper.selectBrandBySetting(settingId);
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
     * 查询所有品牌
     * @return
     */
    public List<Brand> selectList(){
        return brandMapper.selectList();
    }
}
