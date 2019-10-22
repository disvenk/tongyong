package com.resto.service.shop.service;

import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.constant.ShopCarType;
import com.resto.service.shop.entity.ShopCart;
import com.resto.service.shop.mapper.ShopCartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopCartService extends BaseService<ShopCart, Integer> {

    @Autowired
    private ShopCartMapper shopcartMapper;

    @Override
    public BaseDao<ShopCart, Integer> getDao() {
        return shopcartMapper;
    }

    public List<ShopCart> listUserAndShop(ShopCart shopcart) {
        return shopcartMapper.listUserAndShop(shopcart);
    }

    public Integer updateShopCart(ShopCart shopCart) {
        //先查询当前客户是否有该商品的 购物车的条目
        Integer number = shopCart.getNumber();
        Integer oldNumber = new Integer(0);
        ShopCart shopCartItem = shopcartMapper.selectShopCartItem(shopCart);
        if(shopCartItem != null){
            oldNumber = shopCartItem.getNumber();
        }
        if(shopCart.getShopType().equals(ShopCarType.TAOCAN)){
            insertShopCart(shopCart);
            return shopCart.getId();
        } else if (shopCart.getShopType().equals(ShopCarType.TAOCANDANPIN)) {
            insertShopCart(shopCart);
            return shopCart.getId();
        } else if (shopCart.getShopType().equals(ShopCarType.DANPIN)) {
            shopCartItem = shopcartMapper.selectShopCartItem(shopCart);
        } else if (shopCart.getShopType().equals(ShopCarType.CAOBAO)){
            if(shopCartItem == null){
                insertShopCart(shopCart);
                return shopCart.getId();
            }else{
                shopCartItem.setNumber(shopCart.getNumber());
                shopcartMapper.updateShopCartItem(shopCartItem);
                shopcartMapper.delMealItem(shopCartItem.getId().toString());
                return shopCartItem.getId();
            }
        } else if (shopCart.getShopType().equals(ShopCarType.CAOBAODANPIN)){
            insertShopCart(shopCart);
        } else if (shopCart.getShopType().equals(ShopCarType.XINGUIGE)){
            insertShopCart(shopCart);
            return shopCart.getId();
        } else if (shopCart.getShopType().equals(ShopCarType.XINGUIGECAOBAODANPIN)){
            insertShopCart(shopCart);
        }
        if(shopCart.getShopType().equals(ShopCarType.DANPIN)){
            if (shopCartItem == null && number > 0) {
                logger.info(shopCart.getRecommendArticleId());
                insertShopCart(shopCart);
                return number;
            } else if (shopCartItem != null && number > 0) {
                shopCartItem.setNumber(number);
                shopcartMapper.updateShopCartItem(shopCartItem);
                return number - oldNumber;
            } else if (shopCart != null){
                if(number <= 0 &&  shopCart.getId() != null){
                    shopcartMapper.delMealArticle(shopCart.getId().toString());
                }else if(number <= 0 &&  shopCart.getId() == null &&  shopCartItem != null && shopCartItem.getId() != null){
                    shopcartMapper.delMealArticle(shopCartItem.getId().toString());
                }
            }
        }
        return 0;
    }

    private void deleteShopCartItem(Integer id) {
        shopcartMapper.deleteByPrimaryKey(id);
    }

    private Integer insertShopCart(ShopCart shopCart) {
        shopcartMapper.insertSelective(shopCart);
        Integer farId = shopCart.getId();
        return farId;
    }

    public void clearShopCart(String customerId, Integer distributionModeId, String shopDetailId) {
        shopcartMapper.clearShopCart(customerId, distributionModeId, shopDetailId);
    }

    public void clearShopCart(String customerId, String shopDetailId) {
        shopcartMapper.clearAllShopCart(customerId, shopDetailId);
    }

    public void clearShopCartGeekPos(String userId, String shopId) {
        shopcartMapper.clearShopCartGeekPos(userId, shopId);
    }

    public List<ShopCart> listUserShopCart(String userId, String shopId, Integer distributionModeId) {
        return  shopcartMapper.listUserShopCart(userId, shopId, distributionModeId);
    }

    public void delMealArticle(String id) {
        shopcartMapper.delMealArticle(id);
    }

    public void delMealItem(String articleId) {
        shopcartMapper.delMealItem(articleId);
    }

    public ShopCart selectByUuId(String uuid) {
        return shopcartMapper.selectByUuId(uuid);
    }

    public void deleteCustomerArticle(String customerId, String articleId) {
        shopcartMapper.deleteCustomerArticle(customerId, articleId);
    }
}
