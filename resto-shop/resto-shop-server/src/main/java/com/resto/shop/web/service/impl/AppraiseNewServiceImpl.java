package com.resto.shop.web.service.impl;

import javax.annotation.Resource;
import com.alibaba.dubbo.config.annotation.Service;
import com.resto.api.appraise.entity.*;
import com.resto.api.appraise.service.NewAppraiseGradeService;
import com.resto.api.appraise.service.NewAppraiseNewService;
import com.resto.api.appraise.service.NewAppraiseStepService;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.model.BrandSetting;
import com.resto.brand.web.model.ShareSetting;
import com.resto.brand.web.model.WechatConfig;
import com.resto.brand.web.service.BrandSettingService;
import com.resto.brand.web.service.ShareSettingService;
import com.resto.brand.web.service.WeChatService;
import com.resto.brand.web.service.WechatConfigService;
import com.resto.shop.web.constant.OrderState;
import com.resto.shop.web.constant.RedType;
import com.resto.shop.web.datasource.DataSourceContextHolder;
import com.resto.shop.web.dto.AppraiseNewBrandDto;
import com.resto.shop.web.dto.AppraiseNewShopDetailDto;
import com.resto.shop.web.dto.AppraiseNewShopDto;
import com.resto.shop.web.exception.AppException;
import com.resto.shop.web.model.*;
import com.resto.shop.web.producer.MQMessageProducer;
import com.resto.shop.web.report.AppraiseMapperReport;
import com.resto.shop.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
@Service
public class AppraiseNewServiceImpl extends GenericServiceImpl<AppraiseNew, Long> implements AppraiseNewService {

    @Resource
    private NewAppraiseNewService newAppraiseNewService ;

    @Resource
    private NewAppraiseGradeService newAppraiseGradeService;

    @Resource
    private NewAppraiseStepService newAppraiseStepService;


    @Resource
    private OrderService orderService;

    @Resource
    private ParticipantService participantService;

    @Resource
    private RedPacketService redPacketService;

    @Resource
    private RedConfigService redConfigService;

    @Resource
    private CustomerService customerService;

    @Resource
    private AccountService accountService;

    @Resource
    private WechatConfigService wechatConfigService;

    @Resource
    private BrandSettingService brandSettingService;


    @Resource
    private ShareSettingService shareSettingService;

    @Resource
    private MQMessageProducer mqMessageProducer;

    @Autowired
    private WeChatService weChatService;

    @Resource
    private AppraiseMapperReport appraiseMapperReport;


    @Override
    @Transactional
    public String addAppraise(AppraiseNew appraisenew) throws AppException{
        Order order= orderService.selectById(appraisenew.getOrderId());
        BrandSetting brandSetting =  brandSettingService.selectByBrandId(order.getBrandId());
        BigDecimal redMoney=new BigDecimal(0);
        if(order.getAllowAppraise() && (order.getGroupId() == null || "".equals(order.getGroupId()))){
            redMoney= rewardRed(appraisenew.getCustomerId(), order, brandSetting);
            appraisenew.setRedMoney(redMoney);
            AppraiseNew aNew = newAppraiseNewService.add(order.getBrandId(),appraisenew);
            log.info("新版保存成功返回评论id------------------>"+aNew.getId());
            List<AppraiseGrade> appraiseGrades = appraisenew.getAppraiseGrades();
            for (AppraiseGrade  appraiseGrade : appraiseGrades){
                appraiseGrade.setAppraiseId(aNew.getId());
                newAppraiseGradeService.dbSave(order.getBrandId(),appraiseGrade);
            }
            List<AppraiseStep > appraiseSteps = appraisenew.getAppraiseSteps();
            if(appraiseSteps!=null){
                for (AppraiseStep appraiseStep :appraiseSteps) {
                    appraiseStep.setAppraiseId(aNew.getId());
                    newAppraiseStepService.dbSave(order.getBrandId(),appraiseStep);
                }
            }
            order.setOrderState(OrderState.HASAPPRAISE);
            order.setAllowAppraise(false);
            order.setAllowCancel(false);
            orderService.update(order);
            //评论推送消息
            ShareSetting setting = shareSettingService.selectValidSettingByBrandId(DataSourceContextHolder.getDataSourceName());
            if(setting!=null){
                boolean isCanShare = isCanShareNew(setting,appraisenew);
                log.info("拥有分享好评设置,ID:"+setting.getId());
                if(isCanShare){
                    //发送分享通知!
                    Long delayTime = setting.getDelayTime() == null ? 120000 : setting.getDelayTime() * 1000L;
                    log.info("可以分享 "+delayTime+"ms 后发送通知");
                    appraisenew.setBrandId(setting.getBrandId());
                    mqMessageProducer.sendShareMsgNew(appraisenew,delayTime);
                }
            }
        }else if(order.getAllowAppraise() && order.getGroupId() != null && !"".equals(order.getGroupId())){
            //判断用户是否已经评论
            AppraiseNew  a = newAppraiseNewService.selectByOrderIdCustomerId(order.getBrandId(),appraisenew.getOrderId(), appraisenew.getCustomerId());
            if(a != null){
                log.error("订单不允许评论:	"+order.getId());
                throw new AppException(AppException.ORDER_NOT_ALL_APPRAISE);
            }
            redMoney= rewardRed(appraisenew.getCustomerId(), order, brandSetting);
            appraisenew.setRedMoney(redMoney);
            AppraiseNew aNew = newAppraiseNewService.add(order.getBrandId(),appraisenew);
            List<AppraiseGrade> appraiseGrades = appraisenew.getAppraiseGrades();
            for (AppraiseGrade appraiseGrade : appraiseGrades){
                appraiseGrade.setAppraiseId(aNew.getId());
                newAppraiseGradeService.dbSave(order.getBrandId(),appraiseGrade);
            }
            List<AppraiseStep> appraiseSteps = appraisenew.getAppraiseSteps();
            if(appraiseSteps!=null){
                for (AppraiseStep appraiseStep :appraiseSteps) {
                    appraiseStep.setAppraiseId(aNew.getId());
                    newAppraiseStepService.dbSave(order.getBrandId(),appraiseStep);
                }
            }
            //仅修改  够餐组下面的单人的领取红包记录
            participantService.updateAppraiseByOrderIdCustomerId(order.getId(), appraisenew.getCustomerId());
            //查询该够餐组 下面是否还存在未领取红包的记录   如没有则订单状态变成11
            List<Participant> participants = participantService.selectNotAppraiseByGroupId(order.getGroupId(), order.getId());
            if(participants.size() == 0){
                order.setOrderState(OrderState.HASAPPRAISE);
                order.setAllowAppraise(false);
            }
            order.setAllowCancel(false);
            orderService.update(order);
            //评论推送消息
            ShareSetting setting = shareSettingService.selectValidSettingByBrandId(DataSourceContextHolder.getDataSourceName());
            if(setting!=null){
                boolean isCanShare = isCanShareNew(setting,appraisenew);
                log.info("拥有分享好评设置,ID:"+setting.getId());
                if(isCanShare){
                    //发送分享通知!
                    Long delayTime = setting.getDelayTime() == null ? 120000 : setting.getDelayTime() * 1000L;
                    log.info("可以分享 "+delayTime+"ms 后发送通知");
                    appraisenew.setBrandId(setting.getBrandId());
                    mqMessageProducer.sendShareMsgNew(appraisenew,delayTime);
                }
            }
        }else{
            log.error("订单不允许评论:	"+order.getId());
            throw new AppException(AppException.ORDER_NOT_ALL_APPRAISE);
        }
        return redMoney.toString();
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

    private boolean isCanShareNew(ShareSetting setting, com.resto.api.appraise.entity.AppraiseNew appraiseNew) {
        log.info("Setting,minLevel:"+setting.getMinLevel());
        log.info("appraise,minLevel:"+appraiseNew.getLevel());
        log.info("Setting,getMinLength:"+setting.getMinLength());
        log.info("appraise,getContent:"+appraiseNew.getContent().length());
        if(setting.getMinLevel()<=appraiseNew.getLevel()&&setting.getMinLength()<=appraiseNew.getContent().length()){
            log.info("开始分享:");
            return true;
        }
        log.info("不可分享:");
        return false;
    }

    @Override
    public GenericDao<AppraiseNew, Long> getDao() {
        return null;
    }

    @Override
    public List<AppraiseNewBrandDto> selectAppraiseNewBrand(String beginDate, String endDate, String brandId) {
        return appraiseMapperReport.selectAppraiseNewBrand(beginDate,endDate,brandId);
    }

    @Override
    public List<AppraiseNewShopDto> selectAppraiseNewShop(String beginDate, String endDate, String shopId) {
        return appraiseMapperReport.selectAppraiseNewShop(beginDate,endDate,shopId);
    }

    @Override
    public List<AppraiseNewShopDetailDto> selectAppraiseNewShopDetail(String beginDate, String endDate, String shopId) {
        return appraiseMapperReport.selectAppraiseNewShopDetail(beginDate,endDate,shopId);
    }
}
