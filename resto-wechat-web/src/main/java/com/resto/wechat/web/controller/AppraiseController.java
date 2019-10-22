package com.resto.wechat.web.controller;

import com.resto.api.appraise.service.*;
import com.resto.api.article.service.NewArticleLibraryService;
import com.resto.api.article.service.NewArticleService;
import com.resto.api.article.service.NewArticleTopService;
import com.resto.api.customer.entity.*;
import com.resto.api.customer.entity.Customer;
import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.*;
import com.resto.brand.web.model.*;
import com.resto.brand.web.service.*;
import com.resto.api.appraise.exception.AppException ;
import com.resto.api.appraise.entity.Appraise;
import com.resto.api.appraise.entity.AppraiseFile;
import com.resto.shop.web.model.*;
import com.resto.api.article.entity.Article;
import com.resto.api.article.entity.ArticleTop;
import com.resto.api.appraise.entity.AppraiseNew;
import com.resto.shop.web.service.*;
import com.resto.shop.web.util.LogTemplateUtils;
import com.resto.wechat.web.util.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@RequestMapping("appraise")
@RestController
public class AppraiseController extends GenericController {

    @Resource
    AppraiseService appraiseService;

    @Resource
    NewAppraiseService newAppraiseService;

    @Resource
    ShareSettingService shareSettingService;

    @Resource
    RewardSettingService rewardSettingService;

    @Resource
    BrandService brandService;

    @Resource
    WechatConfigService wechatConfigService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    NewArticleService newArticleService;

    @Resource
    NewAppraisePraiseService newAppraisePraiseService;

    @Resource
    NewAppraiseCommentService newAppraiseCommentService;

    @Resource
    NewAppraiseFileService newAppraiseFileService;

    @Autowired
    SysErrorService sysErrorService;

    @Resource
    ExperienceService experienceService;

    @Resource
    NewArticleTopService newArticleTopService;

    @Resource
    NewAppraiseNewService newAppraiseNewService;

    @Resource
    NewCustomerService newCustomerService;

    @Autowired
    WeChatService weChatService;

    @Autowired
    BrandSettingService brandSettingService;

    @Autowired
    ArticleLibraryService articleLibraryService;

    @Autowired
    NewArticleLibraryService newArticleLibraryService;

    @RequestMapping("listAppraise")
    public Result listAppraise(Integer currentPage, Integer showCount, Integer maxLevel, Integer minLevel) {
        List<Appraise> list = appraiseService.updateAndListAppraise(getCurrentShopId(), currentPage, showCount, maxLevel, minLevel);
        return getSuccessResult(list);
    }

    @RequestMapping("/new/listAppraise")
    public void listAppraiseNew(Integer currentPage, Integer showCount, Integer maxLevel, Integer minLevel, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        List<Appraise> list = appraiseService.updateAndListAppraise(getCurrentShopId(), currentPage, showCount, maxLevel, minLevel);

        for(Appraise appraise : list){
            appraise.setAppraisePraises(newAppraisePraiseService.appraisePraiseList(getCurrentBrandId(),appraise.getId()));
            appraise.setAppraiseComments(newAppraiseCommentService.appraiseCommentList(getCurrentBrandId(),appraise.getId()));
            List<AppraiseFile> lists = newAppraiseFileService.appraiseFileList(getCurrentBrandId(),appraise.getId());
            List<AppraiseFile> appraiseFiles = new ArrayList<>();
            List<AppraiseFile> appraiseMessage = new ArrayList<>();
            for(AppraiseFile appraiseFile : lists){
                if(appraiseFile.getState() == 0){
                    appraiseFiles.add(appraiseFile);
                }else if (appraiseFile.getState() == 1){
                    appraiseMessage.add(appraiseFile);
                }
            }
            appraise.setAppraiseFiles(appraiseFiles);
            appraise.setAppraiseMessage(appraiseMessage);
        }

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/getAppraiseInfo")
    public void getAppraiseInfo(String appraiseId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        Appraise appraise = newAppraiseService.selectDetailedById(getCurrentBrandId(),appraiseId);
        appraise.setAppraisePraises(newAppraisePraiseService.appraisePraiseList(getCurrentBrandId(),appraise.getId()));
        appraise.setAppraiseComments(newAppraiseCommentService.appraiseCommentList(getCurrentBrandId(),appraise.getId()));
        List<AppraiseFile> list = newAppraiseFileService.appraiseFileList(getCurrentBrandId(),appraise.getId());
        List<AppraiseFile> appraiseFiles = new ArrayList<>();
        List<AppraiseFile> appraiseMessage = new ArrayList<>();
        for(AppraiseFile appraiseFile : list){
            if(appraiseFile.getState() == 0){
                appraiseFiles.add(appraiseFile);
            }else if (appraiseFile.getState() == 1){
                appraiseMessage.add(appraiseFile);
            }
        }
        appraise.setAppraiseFiles(appraiseFiles);
        appraise.setAppraiseMessage(appraiseMessage);

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", new net.sf.json.JSONObject().fromObject(appraise));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/appraiseCount")
    public Result appraiseCount() {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> appraiseCount = newAppraiseService.appraiseCount(getCurrentBrandId(),getCurrentShopId());
        List<Map<String, Object>> appraiseMonthCount = newAppraiseService.appraiseMonthCount(getCurrentBrandId(),getCurrentShopId());
        resultMap.put("appraiseCount", appraiseCount);
        resultMap.put("appraiseMonthCount", appraiseMonthCount);
        return getSuccessResult(resultMap);
    }

    @RequestMapping("/new/appraiseCount")
    public void appraiseCountNew(HttpServletRequest request, HttpServletResponse response) throws Exception {
        AppUtils.unpack(request, response);

        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> appraiseCount = newAppraiseService.appraiseCount(getCurrentBrandId(),getCurrentShopId());
        List<Map<String, Object>> appraiseMonthCount = newAppraiseService.appraiseMonthCount(getCurrentBrandId(),getCurrentShopId());
        resultMap.put("appraiseCount", appraiseCount);
        resultMap.put("appraiseMonthCount", appraiseMonthCount);

        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("data", resultMap);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("saveAppraise")
    public Result saveAppraise(Appraise appraise) throws AppException, com.resto.shop.web.exception.AppException {
        appraise.setCustomerId(getCurrentCustomerId());
        appraise = appraiseService.saveAppraise(appraise);
        return getSuccessResult(appraise);
    }

    @RequestMapping("/new/saveAppraise")
    public void saveAppraiseNew(Appraise appraise, String mediaIds, String photoIds, HttpServletRequest request, HttpServletResponse response) throws AppException, IOException, com.resto.shop.web.exception.AppException {
        AppUtils.unpack(request, response);
        //判断是否有多个标签
        String feedback=appraise.getFeedback();
        if(appraise.getFeedback().contains(",")){
            feedback = appraise.getFeedback().substring(0,appraise.getFeedback().length()-1);
        }

        //单独记录一份评价用于菜品和标签的排序
        String [] tops = appraise.getFeedback().split(",");
        for(String s:tops){
            Date date = new Date();
            ArticleTop articleTop = new ArticleTop();
            articleTop.setCreateTime(date);
            articleTop.setName(s);
            articleTop.setType(Integer.parseInt(appraise.getType().toString()));
            articleTop.setShopDetailId(getCurrentShopId());
            newArticleTopService.dbSave(getCurrentBrandId(),articleTop);
        }

        Brand brand = brandService.selectById(getCurrentBrandId());
        WechatConfig wechatConfig = wechatConfigService.selectById(brand.getWechatConfigId());
        appraise.setContent(EmojiFilter.filterEmoji(appraise.getContent()));
        appraise.setCustomerId(getCurrentCustomerId());
        appraise.setShopDetailId(getCurrentShopId());
        appraise.setFeedback(feedback);

        appraise = appraiseService.saveAppraise(appraise);
        Integer sort = 0;
        if(!StringUtils.isEmpty(appraise.getArticleId())){
            String[] ary = appraise.getArticleId().split(",");// 截取一个个菜品
            for (String item : ary) {
                Article article = getPicture(item);
                if(article != null && appraise.getLevel().intValue() == 5){
                    newArticleService.addArticleLikes(getCurrentBrandId(),article.getId());
                    newArticleLibraryService.addArticleLikes(getCurrentBrandId(),article.getArticleId());
                }
                AppraiseFile file = new AppraiseFile();
                file.setId(ApplicationUtils.randomUUID());
                file.setAppraiseId(appraise.getId());
                file.setShopDetailId(appraise.getShopDetailId());
                file.setCreateTime(new Date());
                if(article != null && article.getPhotoSmall() != null){
                    file.setFileUrl(article.getPhotoSmall());
                    if(article.getPhotoSquare() == null || article.getPhotoSquare() == ""){
                        String photoSquare = ImageUtil.imageBytesScale(article.getPhotoSmall(),request);
                        if(photoSquare != null){
                            file.setPhotoSquare(photoSquare);
                            article.setPhotoSquare(photoSquare);
                            newArticleService.updatePhotoSquare(getCurrentBrandId(),article.getId(), photoSquare);
                            sort++;
                            file.setSort(sort);
                            newAppraiseFileService.dbSave(getCurrentBrandId(),file);
                        }
                    }else{
                        file.setPhotoSquare(getPicture(item).getPhotoSquare());
                        sort++;
                        file.setSort(sort);
                        newAppraiseFileService.dbSave(getCurrentBrandId(),file);
                    }
                }
            }
        }
        if(!StringUtils.isEmpty(mediaIds)){
            String[] ary = mediaIds.split(",");// 截取一个个mediaId
            String systemPath = request.getServletContext().getRealPath("");

            for (String item : ary) {
                sort++;
                String photoPath = weChatService.downLoadWXFile(wechatConfig.getAppid(), wechatConfig.getAppsecret(), item, "upload/apprasieFiles/"+ DateFormatUtils.format(new Date(), "yyyy-MM-dd")+"/", systemPath);
                AppraiseFile file = new AppraiseFile();
                file.setId(ApplicationUtils.randomUUID());
                file.setAppraiseId(appraise.getId());
                file.setFileUrl(photoPath);
                file.setSort(sort);
                file.setPhotoSquare(ImageUtil.imageBytesScale(photoPath,request));
                file.setShopDetailId(appraise.getShopDetailId());
                file.setCreateTime(new Date());
                newAppraiseFileService.dbSave(getCurrentBrandId(),file);
            }
        }
        /*
            消息体验的标签修改 所有注册
            暂时不要删除，以后可能还会使用
            现在改为标签  不用继续图片
         */
//        if(!StringUtils.isEmpty(photoIds)){
//            String[] ary = photoIds.split(",");// photoIds
//            for (String item : ary) {
//                ShowPhoto showphoto = showPhotoService.selectById(Integer.parseInt(item));
//                AppraiseFile file = new AppraiseFile();
//                file.setId(ApplicationUtils.randomUUID());
//                file.setAppraiseId(appraise.getId());
//                file.setShopDetailId(appraise.getShopDetailId());
//                file.setCreateTime(new Date());
//                if(showphoto != null){
//                    file.setFileUrl(showphoto.getPicUrl());
//                    if(showphoto.getPhotoSquare() == null || showphoto.getPhotoSquare() == ""){
//                        String photoSquare = ImageUtil.imageBytesScale(showphoto.getPicUrl(),request);
//                        if(photoSquare != null){
//                            file.setPhotoSquare(photoSquare);
//                            showphoto.setPhotoSquare(photoSquare);
//                            showPhotoService.updatePhotoSquare(showphoto.getId(), photoSquare);
//                            sort++;
//                            file.setSort(sort);
//                            appraiseFileService.insert(file);
//                        }
//                    }else{
//                        sort++;
//                        file.setSort(sort);
//                        file.setPhotoSquare(showphoto.getPhotoSquare());
//                        appraiseFileService.insert(file);
//                    }
//                }
//
//            }
//        }
        if(!StringUtils.isEmpty(photoIds)) {
            String[] ary = photoIds.split(",");// photoIds
            for (String item : ary) {
                Experience experience = experienceService.selectById(Integer.parseInt(item));
                AppraiseFile file = new AppraiseFile();
                file.setId(ApplicationUtils.randomUUID());
                file.setAppraiseId(appraise.getId());
                file.setShopDetailId(appraise.getShopDetailId());
                file.setCreateTime(new Date());
                file.setState(1);
                file.setFileName(experience.getTitle());
                newAppraiseFileService.dbSave(getCurrentBrandId(),file);
            }
        }
        LogTemplateUtils.getAppraiseByOrderType(brand.getBrandName(),appraise,EmojiFilter.filterEmoji(appraise.getContent()));
        LogTemplateUtils.getAppraiseByUserType(brand.getBrandName(),appraise,EmojiFilter.filterEmoji(appraise.getContent()),getCurrentCustomer());
        ShareSetting shareSetting = shareSettingService.selectByBrandId(getCurrentBrandId());
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", new JSONObject(appraise));
        json.put("shareSetting",new JSONObject(shareSetting));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("getShareDetailed")
    public Result getShareDetailed(String appraiseId) {
        Appraise appraise = newAppraiseService.selectDetailedById(getCurrentBrandId(),appraiseId);
        ShareSetting setting = shareSettingService.selectByBrandId(getCurrentBrandId());
        Map<String, Object> data = new HashMap<>();
        data.put("appraise", appraise);
        data.put("setting", setting);
        return getSuccessResult(data);
    }

    @RequestMapping("/new/getShareDetailed")
    public void getShareDetailedNew(String appraiseId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);

        ShareSetting setting = shareSettingService.selectByBrandId(getCurrentBrandId());
        BrandSetting brandSetting = brandSettingService.selectByBrandId(getCurrentBrandId());
        if(brandSetting.getAppraiseEdition()==0){
            Appraise appraise = newAppraiseService.selectDetailedById(getCurrentBrandId(),appraiseId);
            Map<String, Object> data = new HashMap<>();
            data.put("appraise", appraise);
            data.put("setting", setting);
            net.sf.json.JSONObject json = new net.sf.json.JSONObject();
            json.put("statusCode", "200");
            json.put("success", true);
            json.put("message", "请求成功");
            json.put("data", data);
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }else{
            AppraiseNew appraiseNew = newAppraiseNewService.dbSelectByPrimaryKey(getCurrentBrandId(),Long.valueOf(appraiseId));
            Customer customer= newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),appraiseNew.getCustomerId());
            appraiseNew.setHeadPhoto(customer.getHeadPhoto());
            appraiseNew.setNickName(customer.getNickname());
            Map<String, Object> data = new HashMap<>();
            data.put("appraise", appraiseNew);
            data.put("setting", setting);
            net.sf.json.JSONObject json = new net.sf.json.JSONObject();
            json.put("statusCode", "200");
            json.put("success", true);
            json.put("message", "请求成功");
            json.put("data", data);
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
    }

    @RequestMapping("getRewardDetailed")
    public Result getRewardDetailed() {
        RewardSetting setting = rewardSettingService.selectByBrandId(getCurrentBrandId());
        return getSuccessResult(setting);
    }

    @RequestMapping("/new/getRewardDetailed")
    public void getRewardDetailedNew(HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        RewardSetting setting = rewardSettingService.selectByBrandId(getCurrentBrandId());

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("success", true);
        json.put("message", "请求成功");
        json.put("data", new net.sf.json.JSONObject().fromObject(setting));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/getAppraiseByOrderId")
    public void getAppraiseByOrderId(String orderId, HttpServletRequest request, HttpServletResponse response) {
        AppUtils.unpack(request, response);
        Appraise appraise = newAppraiseService.selectDeatilByOrderId(getCurrentBrandId(),orderId, getCurrentCustomerId());
        if(appraise!=null){
            JSONObject json = new JSONObject();
            json.put("statusCode", "200");
            json.put("success", true);
            json.put("message", "请求成功");
            if (appraise == null) {
                json.put("data", "");
            } else {
                json.put("data", new JSONObject(appraise));
            }
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }else{
            AppraiseNew appraiseNew = newAppraiseNewService.selectByOrderIdCustomerId(getCurrentBrandId(),orderId,getCurrentCustomerId());
            JSONObject json = new JSONObject();
            json.put("statusCode", "200");
            json.put("success", true);
            json.put("message", "请求成功");
            if (appraiseNew == null) {
                json.put("data", "");
            } else {
                json.put("data", new JSONObject(appraiseNew));
            }
            ApiUtils.setBackInfo(response, json); // 返回信息设置
        }
    }


    private Article getPicture(String id) {
        OrderItem item = orderItemService.selectById(id);
        String articleId  = item.getArticleId();
        if(articleId.contains("@")){
            articleId = articleId.split("@")[0];
        }
        Article article = newArticleService.dbSelectByPrimaryKey(getCurrentBrandId(),articleId);
        if(article!=null){
            return  article;
        }
        return null;
    }
}
