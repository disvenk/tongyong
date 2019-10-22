package com.resto.service.shop.service;

import com.resto.conf.util.ApplicationUtils;
import com.resto.core.base.BaseDao;
import com.resto.core.base.BaseService;
import com.resto.service.shop.constant.OrderState;
import com.resto.service.shop.constant.RedType;
import com.resto.service.shop.entity.*;
import com.resto.service.shop.exception.AppException;
import com.resto.service.shop.mapper.AppraiseMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AppraiseService extends BaseService<Appraise, String>{

    @Autowired
    private AppraiseMapper appraiseMapper;

    @Autowired
	OrderService orderService;
    
    @Autowired
    ArticleService articleService;
    
    @Autowired
    ShowPhotoService showPhotoService;

    @Autowired
    RedConfigService redConfigService;
    
    @Autowired
    AccountService accountService;
    
    @Autowired
    CustomerService customerService;
    
    @Autowired
    RedPacketService redPacketService;
    
    @Override
    public BaseDao<Appraise, String> getDao() {
        return appraiseMapper;
    }

	public List<Appraise> updateAndListAppraise(String currentShopId, Integer currentPage, Integer showCount, Integer maxLevel,
												Integer minLevel) {
		List<Appraise> appraiseList = appraiseMapper.listAppraise(currentShopId, currentPage, showCount, maxLevel, minLevel);
		for (Appraise appraise : appraiseList) {
			if(StringUtils.isBlank(appraise.getFeedback())){
				String text = getFeedBackText(appraise);
				appraise.setFeedback(text);
				update(appraise);
			}
		}
		return appraiseList;
	}

	private String getFeedBackText(Appraise appraise) {
		String articleId = appraise.getArticleId();
		if(appraise.getType()==1){
			Article art = articleService.selectById(articleId);
			if(art!=null){
				return art.getName();
			}
		}else if(appraise.getType()==2&&StringUtils.isNumeric(articleId)){
			ShowPhoto sp = showPhotoService.selectById(Integer.parseInt(articleId));
			if(sp!=null){
				return sp.getTitle();
			}
		}
		return "";
	}

	public Map<String, Object> appraiseCount(String currentShopId) {
	    return appraiseMapper.appraiseCount(currentShopId);
	}

	public List<Map<String, Object>> appraiseMonthCount(String currentShopId) {
		return appraiseMapper.appraiseMonthCount(currentShopId);
	}

	public Appraise saveAppraise(Appraise appraise) throws AppException {
		Order order= orderService.selectById(appraise.getOrderId());
		if(order.getAllowAppraise()){
//			String pic = getPicture(appraise);
//			appraise.setPictureUrl(pic);
			appraise.setId(ApplicationUtils.randomUUID());
			appraise.setCreateTime(new Date());
			appraise.setStatus((byte)1);
			appraise.setShopDetailId(order.getShopDetailId());
			BigDecimal redMoney= rewardRed(order);
			appraise.setRedMoney(redMoney);
			appraise.setBrandId(order.getBrandId());
			insert(appraise);
			order.setOrderState(OrderState.HASAPPRAISE);
			order.setAllowAppraise(false);
			order.setAllowCancel(false);
			order.setAllowContinueOrder(false);
			orderService.update(order);
		}else{
			logger.error("订单不允许评论:	"+order.getId());
			throw new AppException(AppException.ORDER_NOT_ALL_APPRAISE);
		}
		return appraise;
	}

	private BigDecimal rewardRed(Order order) {
		BigDecimal money = redConfigService.nextRedAmount(order);
		Customer cus = customerService.selectById(order.getCustomerId());
		if(money.compareTo(BigDecimal.ZERO)>0){
			accountService.addAccount(money,cus.getAccountId(), " 评论奖励红包:"+money,AccountLog.APPRAISE_RED_PACKAGE,order.getShopDetailId());
            RedPacket redPacket = new RedPacket();
            redPacket.setId(ApplicationUtils.randomUUID());
            redPacket.setRedMoney(money);
            redPacket.setCreateTime(new Date());
            redPacket.setCustomerId(cus.getId());
            redPacket.setBrandId(order.getBrandId());
            redPacket.setShopDetailId(order.getShopDetailId());
            redPacket.setRedRemainderMoney(money);
            redPacket.setRedType(RedType.APPRAISE_RED);
			redPacket.setOrderId(order.getId());
            redPacketService.insert(redPacket);
			logger.info("评论奖励红包: "+money+" 元"+order.getId());
		}
		return money;
	}

	public Appraise selectDetailedById(String appraiseId) {
		Appraise appraise = appraiseMapper.selectDetailedById(appraiseId);
		return appraise;
	}

	public Appraise selectDeatilByOrderId(String orderId) {
		List<Appraise> apprises = appraiseMapper.selectDeatilByOrderId(orderId);
		if(apprises.size() > 0){
			return apprises.get(0);
		}else{
			return null;
		}
	}

	public Appraise selectAppraiseByCustomerId(String customerId,String shopId) {
		return appraiseMapper.selectAppraiseByCustomerId(customerId,shopId);
	}

	public List<Appraise> selectCustomerAllAppraise(String customerId, Integer currentPage, Integer showCount) {
		return appraiseMapper.selectCustomerAllAppraise(customerId, currentPage, showCount);
	}

	public int selectByCustomerCount(String customerId) {
		return appraiseMapper.selectByCustomerCount(customerId);
	}

	public List<Appraise> selectByGoodAppraise() {
		return appraiseMapper.selectByGoodAppraise();
	}
	
}
