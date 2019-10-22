package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.shop.web.dao.ArticleAttrMapper;
import com.resto.shop.web.dao.ArticleUnitMapper;
import com.resto.shop.web.model.ArticleAttr;
import com.resto.shop.web.model.ArticleUnit;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.ArticleAttrService;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@Component
@Service
public class ArticleAttrServiceImpl extends GenericServiceImpl<ArticleAttr, Integer> implements ArticleAttrService {

    @Resource
    private ArticleAttrMapper articleattrMapper;
    @Resource
    private ArticleUnitMapper articleUnitMapper;
	@Autowired
	private PosService posService;

    @Override
    public GenericDao<ArticleAttr, Integer> getDao() {
        return articleattrMapper;
    }

    /**
     * 根据店铺ID查询信息
     */
	@Override
	public List<ArticleAttr> selectListByShopId(String shopId) {
		List<ArticleAttr> articleAttrs = articleattrMapper.selectListByShopId(shopId);
		for(ArticleAttr articleAttr : articleAttrs){
			List<ArticleUnit> articleUnit = articleUnitMapper.selectListByAttrId(articleAttr.getId());
			articleAttr.setArticleUnits(articleUnit);
		}
		return articleAttrs;
	}

	/**
	 * 添加 信息
	 * @param articleAttr
	 */
	@Override
	public void create(ArticleAttr articleAttr,String brandId,String shopId) {
		articleattrMapper.insertInfo(articleAttr);
		//判断是否 添加了规格
		if(articleAttr.getUnits() != null && articleAttr.getUnits().length > 0){
			Integer tbArticleAttrId = articleAttr.getId();//添加完 ArticleAttr 的主键 ID
			String[] units = articleAttr.getUnits();
			String[] unitSorts = articleAttr.getUnitSorts();
			for(int i = 0; i <units.length ; i++){
				ArticleUnit articleUnit= new ArticleUnit(units[i], new BigDecimal(unitSorts[i]), tbArticleAttrId);
				articleUnitMapper.insert(articleUnit);
				//消息通知newpos后台发生变化
//				ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
//				shopMsgChangeDto.setBrandId(brandId);
//				shopMsgChangeDto.setShopId(shopId);
//				shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEUNIT);
//				shopMsgChangeDto.setType("add");
//				shopMsgChangeDto.setId(articleUnit.getId().toString());
//				try{
//					posService.shopMsgChange(shopMsgChangeDto);
//				}catch(Exception e){
//					e.printStackTrace();
//				}
			}
		}
	}

	/**
	 * 删除信息
	 */
	@Override
	public void deleteInfo(Integer id,String brandId,String shopId) {
		//删除 ArticleAttr 信息，改变  state 状态
		articleattrMapper.deleteByPrimaryKey(id);
		//删除 ArticleUnit 信息，改变  state 状态
		articleUnitMapper.deleteByAttrId(id);
		//消息通知newpos后台发生变化
		ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
		shopMsgChangeDto.setBrandId(brandId);
		shopMsgChangeDto.setShopId(shopId);
		shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEUNIT);
		shopMsgChangeDto.setType("delete");
		shopMsgChangeDto.setId(id.toString());
		try{
			posService.shopMsgChange(shopMsgChangeDto);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 修改信息
	 */
	@Override
	public void updateInfo(ArticleAttr articleAttr,String brandId,String shopId) {
		//修改  ArticleAttr 信息
		articleattrMapper.updateByPrimaryKeySelective(articleAttr);
		//删除  ArticleUnit  信息
		List<ArticleUnit> articleUnits = articleUnitMapper.selectListByAttrId(articleAttr.getId());
		for(ArticleUnit articleUnit : articleUnits){
			articleUnitMapper.deleteByPrimaryKey(articleUnit.getId());
			//消息通知newpos后台发生变化
			ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
			shopMsgChangeDto.setBrandId(brandId);
			shopMsgChangeDto.setShopId(shopId);
			shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEUNIT);
			shopMsgChangeDto.setType("delete");
			shopMsgChangeDto.setId(articleUnit.getId().toString());
			try{
				posService.shopMsgChange(shopMsgChangeDto);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		//添加  ArticleUnit  信息
		if(articleAttr.getUnits() != null && articleAttr.getUnits().length > 0){
			Integer tbArticleAttrId = articleAttr.getId();
			String[] unitIds = articleAttr.getUnitIds();
			String[] units = articleAttr.getUnits();
			String[] unitSorts = articleAttr.getUnitSorts();
			for(int i = 0; i <units.length ; i++){
				ArticleUnit articleUnit= new ArticleUnit(unitIds[i] ,units[i], new BigDecimal(unitSorts[i]), tbArticleAttrId);
				articleUnitMapper.insertSelective(articleUnit);
				//消息通知newpos后台发生变化
				ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
				shopMsgChangeDto.setBrandId(brandId);
				shopMsgChangeDto.setShopId(shopId);
				shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBARTICLEUNIT);
				shopMsgChangeDto.setType("add");
				shopMsgChangeDto.setId(articleUnit.getId().toString());
				try{
					posService.shopMsgChange(shopMsgChangeDto);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<ArticleAttr> selectListByArticleId(String articleId) {
		return articleattrMapper.selectListByArticleId(articleId);
	}

	@Override
	public int insertByAuto(ArticleAttr articleAttr) {
		return articleattrMapper.insertByAuto(articleAttr);
	}

	@Override
	public ArticleAttr selectSame(String name, String shopId) {
		return articleattrMapper.selectSame(name, shopId);
	}
}
