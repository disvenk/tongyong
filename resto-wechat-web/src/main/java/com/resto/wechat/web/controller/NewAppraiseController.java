package com.resto.wechat.web.controller;

import com.resto.api.appraise.dto.NewAppraiseCustomerDto;
import com.resto.api.appraise.entity.AppraiseComment;
import com.resto.api.appraise.entity.AppraisePraise;
import com.resto.api.appraise.service.NewAppraiseCommentService;
import com.resto.api.appraise.service.NewAppraiseNewService;
import com.resto.api.appraise.service.NewAppraisePraiseService;
import com.resto.api.article.service.NewArticleService;
import com.resto.brand.web.model.ShareSetting;
import com.resto.brand.web.service.ShareSettingService;
import com.resto.api.appraise.exception.AppException;
import com.resto.api.appraise.entity.AppraiseNew;
import com.resto.shop.web.model.*;
import com.resto.api.article.entity.Article;
import com.resto.shop.web.service.*;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.util.EmojiFilter;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RequestMapping("newAppraise")
@RestController
public class NewAppraiseController extends GenericController {

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private AppraiseNewService appraiseNewService;

    @Resource
    private NewAppraiseNewService newAppraiseNewService;

    @Resource
    private ShareSettingService shareSettingService;

    @Resource
    private NewAppraiseCommentService newAppraiseCommentService;

    @Resource
    private NewAppraisePraiseService newAppraisePraiseService;

    @Resource
    private NewArticleService newArticleService;


    @RequestMapping("/appraiseType")
    public void listAppraise(HttpServletRequest request, HttpServletResponse response) {
        //解决跨域问题
        AppUtils.unpack(request, response);
        //List<NewAppraise> list = newAppraiseService.selectWeight(getCurrentShopId());
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
       // json.put("data", list);
        ApiUtils.setBackInfo(response, json);
    }

    @RequestMapping("/customerOrderArticle")
    public void listArticle(String orderId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        List<OrderItem> orderItems = orderItemService.listArticle(orderId);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("data", orderItems);
        ApiUtils.setBackInfo(response, json);
    }

    /**
     * 把评论的数据添加到数据库中
     * @param appraiseNew
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/addAppraise")
    public void addAppraise(@RequestBody AppraiseNew appraiseNew, HttpServletRequest request, HttpServletResponse response) throws AppException, com.resto.shop.web.exception.AppException {
        AppUtils.unpack(request, response);
        appraiseNew.setContent(EmojiFilter.filterEmoji(appraiseNew.getContent()));
        appraiseNew.setCustomerId(getCurrentCustomerId());
        appraiseNew.setShopDetailId(getCurrentShopId());
        String redMoney=appraiseNewService.addAppraise(appraiseNew);
        ShareSetting shareSetting = shareSettingService.selectByBrandId(getCurrentBrandId());
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("shareSetting",new JSONObject(shareSetting));
        json.put("redMoney",redMoney);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/appraiseCustomer")
    public void appraiseCustomer(Integer currentPage, Integer showCount,Integer level, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        //用户昵称，头像，时间，总分，标签，菜品踩赞，菜品图片，评论内容
        List<NewAppraiseCustomerDto> appraiseCustomers = newAppraiseNewService.ListAppraiseCustomer(getCurrentBrandId(),currentPage, showCount,level,getCurrentShopId());
        appraiseCustomers.forEach(s ->{
            List<AppraisePraise> appraisePraiseList= newAppraisePraiseService.appraisePraiseList(getCurrentBrandId(),String.valueOf(s.getAppraiseId()));
            if(appraisePraiseList!=null && !appraisePraiseList.isEmpty()){
                s.setAppraisePraises(appraisePraiseList);
            }
            List<AppraiseComment> appraiseCommentList= newAppraiseCommentService.appraiseCommentList(getCurrentBrandId(),String.valueOf(s.getAppraiseId()));
            if(appraiseCommentList!=null && !appraiseCommentList.isEmpty()){
                s.setAppraiseComments(appraiseCommentList);
            }
            s.getAppraiseSteps().forEach(a -> {
                if(a.getArticleId()==null){
                    s.setAppraiseSteps(new ArrayList<>());
                }else {
                    Article article=newArticleService.dbSelectByPrimaryKey(getCurrentBrandId(),a.getArticleId());
                    if(article!=null){
                        a.setPictureUrl(article.getPhotoSmall());
                    }else{
                        a.setPictureUrl("");
                    }
                }
            });
        });
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", appraiseCustomers);
        ApiUtils.setBackInfo(response, json); //  返回信息设置
    }

    @RequestMapping("/myAppraiseCustomer")
    public void myAppraiseCustomer(Integer currentPage, Integer showCount, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        //用户自己评论用户昵称，头像，时间，总分，标签，菜品踩赞，菜品图片，评论内容
        List<NewAppraiseCustomerDto> appraiseCustomers = newAppraiseNewService.ListAppraiseCustomerId(getCurrentBrandId(),currentPage, showCount,getCurrentCustomerId(),getCurrentShopId());
        appraiseCustomers.forEach(s ->{
            List<AppraisePraise> appraisePraiseList = newAppraisePraiseService.appraisePraiseList(getCurrentBrandId(),String.valueOf(s.getAppraiseId()));
            if(appraisePraiseList!=null && !appraisePraiseList.isEmpty()){
                s.setAppraisePraises(appraisePraiseList);
            }
            List<AppraiseComment> appraiseCommentList = newAppraiseCommentService.appraiseCommentList(getCurrentBrandId(),String.valueOf(s.getAppraiseId()));
            if(appraiseCommentList!=null && !appraiseCommentList.isEmpty()){
                s.setAppraiseComments(appraiseCommentList);
            }
            s.getAppraiseSteps().forEach(a -> {
                Article article=newArticleService.dbSelectByPrimaryKey(getCurrentBrandId(),a.getArticleId());
                if(article!=null){
                    a.setPictureUrl(article.getPhotoSmall());
                }else{
                    a.setPictureUrl("");
                }
            });
        });
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", appraiseCustomers);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/my/appraiseCount")
    public void myAppraiseCount(HttpServletRequest request, HttpServletResponse response){
        AppUtils.unpack(request, response);
        JSONObject list = new JSONObject();
        list.put("appraiseCount", newAppraiseNewService.selectByCustomerCount(getCurrentBrandId(),getCurrentCustomerId()));
        list.put("commentCount", newAppraiseCommentService.selectByCustomerCount(getCurrentBrandId(),getCurrentCustomerId()));
        list.put("praiseCount", newAppraisePraiseService.selectByCustomerCount(getCurrentBrandId(),getCurrentCustomerId()));
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/getAppraiseInfo")
    public void getAppraiseInfo(String appraiseId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        NewAppraiseCustomerDto newAppraiseCustomerDto = newAppraiseNewService.selectByAppraiseId(getCurrentBrandId(),appraiseId);
        List<AppraisePraise> appraisePraiseList = newAppraisePraiseService.appraisePraiseList(getCurrentBrandId(),appraiseId);
        if(appraisePraiseList!=null && !appraisePraiseList.isEmpty()){
            newAppraiseCustomerDto.setAppraisePraises(appraisePraiseList);
        }
        List<AppraiseComment> appraiseCommentList = newAppraiseCommentService.appraiseCommentList(getCurrentBrandId(),appraiseId);
        if(appraiseCommentList!=null && !appraiseCommentList.isEmpty()){
            newAppraiseCustomerDto.setAppraiseComments(appraiseCommentList);
        }
        newAppraiseCustomerDto.getAppraiseSteps().forEach(a -> {
            Article article=newArticleService.dbSelectByPrimaryKey(getCurrentBrandId(),a.getArticleId());
            if(article!=null){
                a.setPictureUrl(article.getPhotoSmall());
            }else{
                a.setPictureUrl("");
            }
        });
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", new net.sf.json.JSONObject().fromObject(newAppraiseCustomerDto));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }


    @RequestMapping("/appraiseCount")
    public void appraiseCountNew(HttpServletRequest request, HttpServletResponse response) throws Exception {
        AppUtils.unpack(request, response);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> appraiseCount = newAppraiseNewService.appraiseCount(getCurrentBrandId(),getCurrentShopId());
        List<Map<String, Object>> appraiseMonthCount = newAppraiseNewService.appraiseMonthCount(getCurrentBrandId(),getCurrentShopId());
        resultMap.put("appraiseCount", appraiseCount);
        resultMap.put("appraiseMonthCount", appraiseMonthCount);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("data", resultMap);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }
}