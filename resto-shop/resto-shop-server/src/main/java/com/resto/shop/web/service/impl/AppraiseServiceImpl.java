package com.resto.shop.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.appraise.entity.Appraise;
import com.resto.api.appraise.service.NewAppraiseService;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.dto.AppraiseShopDto;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.brand.web.service.WeChatService;
import com.resto.brand.web.service.WechatConfigService;
import com.resto.shop.web.constant.OrderState;
import com.resto.shop.web.constant.RedType;
import com.resto.shop.web.exception.AppException;
import com.resto.shop.web.model.*;
import com.resto.shop.web.producer.MQMessageProducer;
import com.resto.shop.web.report.AppraiseMapperReport;
import com.resto.shop.web.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
@Service
public class AppraiseServiceImpl extends GenericServiceImpl<Appraise, String> implements AppraiseService {


	@Resource
	private NewAppraiseService newAppraiseService;

	@Resource
	private AppraiseMapperReport appraiseMapperReport;

    @Resource
    OrderService orderService;

    @Resource
    ArticleService articleService;

    @Resource
    ShowPhotoService showPhotoService;

    @Resource
    RedConfigService redConfigService;

    @Resource
    AccountService accountService;

    @Resource
    CustomerService customerService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    RedPacketService redPacketService;

	@Resource
	ParticipantService participantService;

	@Resource
	BrandSettingService brandSettingService;

	@Resource
	WechatConfigService wechatConfigService;

	@Autowired
	MQMessageProducer mqMessageProducer;

	@Autowired
	WeChatService weChatService;

	@Autowired
	ShopDetailService shopDetailService;

	@Override
	public List<Appraise> updateAndListAppraise(String currentShopId, Integer currentPage, Integer showCount, Integer maxLevel,
			Integer minLevel) {
		ShopDetail shopDetail = shopDetailService.selectById(currentShopId);
		//List<Appraise> appraiseList = appraiseMapper.listAppraise(currentShopId, currentPage, showCount, maxLevel, minLevel);
		List<Appraise> appraiseList = newAppraiseService.listAppraise(shopDetail.getBrandId(),currentShopId, currentPage, showCount, maxLevel, minLevel);
		for (Appraise appraise : appraiseList) {
			if(StringUtils.isBlank(appraise.getFeedback())){
				String text = getFeedBackText(appraise);
				appraise.setFeedback(text);
				newAppraiseService.dbUpdate(shopDetail.getBrandId(),appraise);
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


	@Override
	@Transactional
	public Appraise saveAppraise(Appraise appraise) throws AppException {
		Order order= orderService.selectById(appraise.getOrderId());
		BrandSetting brandSetting =  brandSettingService.selectByBrandId(order.getBrandId());
		if(order.getAllowAppraise() && (order.getGroupId() == null || "".equals(order.getGroupId()))){
			appraise.setId(ApplicationUtils.randomUUID());
			appraise.setCreateTime(new Date());
			appraise.setStatus((byte)1);
			appraise.setShopDetailId(order.getShopDetailId());
			BigDecimal redMoney= rewardRed(appraise.getCustomerId(), order, brandSetting);
			appraise.setRedMoney(redMoney);
			appraise.setBrandId(order.getBrandId());
			appraise = newAppraiseService.dbSave(order.getBrandId(),appraise);
			order.setOrderState(OrderState.HASAPPRAISE);
			order.setAllowAppraise(false);
			order.setAllowCancel(false);
			orderService.update(order);
		}else if(order.getAllowAppraise() && order.getGroupId() != null && !"".equals(order.getGroupId())){
			//判断用户是否已经评论
			Appraise a = newAppraiseService.selectByOrderIdCustomerId(order.getBrandId(),appraise.getOrderId(), appraise.getCustomerId());
			if(a != null){
				log.error("订单不允许评论:	"+order.getId());
				throw new AppException(AppException.ORDER_NOT_ALL_APPRAISE);
			}
			appraise.setId(ApplicationUtils.randomUUID());
			appraise.setCreateTime(new Date());
			appraise.setStatus((byte)1);
			appraise.setShopDetailId(order.getShopDetailId());
			BigDecimal redMoney= rewardRed(appraise.getCustomerId(), order, brandSetting);
			appraise.setRedMoney(redMoney);
			appraise.setBrandId(order.getBrandId());
			newAppraiseService.dbSave(order.getBrandId(),appraise);
			//仅修改  够餐组下面的单人的领取红包记录
			participantService.updateAppraiseByOrderIdCustomerId(order.getId(), appraise.getCustomerId());
			//查询该够餐组 下面是否还存在未领取红包的记录   如没有则订单状态变成11
			List<Participant> participants = participantService.selectNotAppraiseByGroupId(order.getGroupId(), order.getId());
			if(participants.size() == 0){
				order.setOrderState(OrderState.HASAPPRAISE);
				order.setAllowAppraise(false);
			}
			order.setAllowCancel(false);
			orderService.update(order);
		}else{
			log.error("订单不允许评论:	"+order.getId());
			throw new AppException(AppException.ORDER_NOT_ALL_APPRAISE);
		}
		return appraise;
	}

	private BigDecimal rewardRed(String customerId, Order order, BrandSetting brandSetting) {
		BigDecimal money = redConfigService.nextRedAmount(order);
		Customer cus = customerService.selectById(customerId);
		String uuid = ApplicationUtils.randomUUID();
		if(money.compareTo(BigDecimal.ZERO)>0){
			if(brandSetting.getDelayAppraiseMoneyTime() == 0){
				accountService.addAccount(money,cus.getAccountId(), " 评论奖励红包:"+money,AccountLog.APPRAISE_RED_PACKAGE,order.getShopDetailId());
			}
            RedPacket redPacket = new RedPacket();
            redPacket.setId(uuid);
            redPacket.setRedMoney(money);
            redPacket.setCreateTime(new Date());
            redPacket.setCustomerId(cus.getId());
            redPacket.setBrandId(order.getBrandId());
            redPacket.setShopDetailId(order.getShopDetailId());
            redPacket.setRedRemainderMoney(money);
            redPacket.setRedType(RedType.APPRAISE_RED);
			redPacket.setOrderId(order.getId());
			if(brandSetting.getDelayAppraiseMoneyTime() != 0){
				redPacket.setState(0);
			}
            redPacketService.insert(redPacket);
			log.info("评论奖励红包: "+money+" 元"+order.getId());
			if(brandSetting.getDelayAppraiseMoneyTime() != 0){
				shareDelayMsg(cus, brandSetting);
				RedPacket rp = redPacketService.selectById(uuid);
				mqMessageProducer.sendShareGiveMoneyMsg(rp, brandSetting.getDelayAppraiseMoneyTime() * 1000);
			}
		}
		return money;
	}

	private void shareDelayMsg(Customer customer, BrandSetting brandSetting) {
		StringBuffer msg = new StringBuffer();
		WechatConfig config = wechatConfigService.selectByBrandId(customer.getBrandId());
		msg.append("感谢您的评价，红包将在" + brandSetting.getDelayAppraiseMoneyTime() / 60 + "分钟后发放至您的账户~");
		weChatService.sendCustomerMsgASync(msg.toString(), customer.getWechatId(), config.getAppid(), config.getAppsecret());
	}

	private String getPicture(Appraise appraise) {
		String pic=null;
		int type = appraise.getType();
		switch (type) {
		case 1: //好评
			String itemId = appraise.getArticleId();
			OrderItem item = orderItemService.selectById(itemId);
			String articleId  = item.getArticleId();
			if(articleId.contains("@")){
				articleId = articleId.split("@")[0];
			}
			Article article = articleService.selectById(articleId);
			if(article!=null){
				pic = article.getPhotoSmall();
				articleService.addLikes(article.getId());
				appraise.setArticleId(article.getId());
			}
			break;
		case 2:
			if(StringUtils.isNumeric(appraise.getArticleId())){
				Integer showPhotoId = Integer.parseInt(appraise.getArticleId());
				ShowPhoto showPhoto = showPhotoService.selectById(showPhotoId);
				if(showPhoto!=null){
					pic = showPhoto.getPicUrl();
				}
			}
			break;
		default:
			pic=null;
			break;
		}
		return pic ;
	}

	@Override
	public GenericDao<Appraise, String> getDao() {
		return null;
	}


	@Override
	public List<AppraiseShopDto> selectAppraiseShopDto(Map<String, Object> selectMap) {
		return appraiseMapperReport.selectAppraiseShopDto(selectMap);
	}
}
