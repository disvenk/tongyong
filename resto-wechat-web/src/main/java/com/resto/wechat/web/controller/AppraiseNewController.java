package com.resto.wechat.web.controller;

import com.resto.api.appraise.service.NewAppraiseCommentService;
import com.resto.api.appraise.service.NewAppraiseFileService;
import com.resto.api.appraise.service.NewAppraisePraiseService;
import com.resto.api.appraise.service.NewAppraiseService;
import com.resto.api.article.service.NewArticleService;
import com.resto.api.customer.entity.*;
import com.resto.api.customer.service.NewCustomerService;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.MQSetting;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.api.appraise.entity.Appraise;
import com.resto.api.appraise.entity.AppraisePraise;
import com.resto.api.appraise.entity.AppraiseComment;
import com.resto.api.appraise.entity.AppraiseFile;
import com.resto.api.article.entity.Article;
import com.resto.wechat.web.util.ApiUtils;
import com.resto.wechat.web.util.AppUtils;
import com.resto.wechat.web.util.ImageUtil;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resto.brand.core.util.HttpClient.doPostAnsc;

/**
 * Created by carl on 2016/11/20.
 */
@RequestMapping("appraiseNew")
@RestController
public class AppraiseNewController extends GenericController {

    @Resource
    NewAppraiseService newAppraiseService;

    @Resource
    NewAppraisePraiseService newAppraisePraiseService;

    @Resource
    NewAppraiseCommentService newAppraiseCommentService;

    @Resource
    NewAppraiseFileService newAppraiseFileService;

    @Resource
    ShopDetailService shopDetailService;

    @Resource
    NewArticleService newArticleService;

    @Resource
    com.resto.brand.web.service.ShowPhotoService showPhotoService;

    @Resource
    NewCustomerService newCustomerService;

    @RequestMapping("/save/praise")
    public void savePraise(String appraiseId, HttpServletRequest request, HttpServletResponse response){
        AppUtils.unpack(request, response);

        AppraisePraise appraisePraise = newAppraisePraiseService.selectByAppraiseIdCustomerId(getCurrentBrandId(),appraiseId, getCurrentCustomerId());

        Map map = new HashMap(4);
        map.put("brandName", getCurrentBrand().getBrandName());
        map.put("fileName", getCurrentCustomerId());
        map.put("type", "UserAction");
        if(appraisePraise == null){
            AppraisePraise ap = new AppraisePraise();
            ap.setId(ApplicationUtils.randomUUID());
            ap.setAppraiseId(appraiseId);
            ap.setCreateTime(new Date());
            ap.setCustomerId(getCurrentCustomerId());
            ap.setIsDel(0);
            ap.setShopDetailId(getCurrentShopId());
            ap.setBrandId(getCurrentBrandId());
            newAppraisePraiseService.updateCancelPraise(getCurrentBrandId(),ap);
            map.put("content", "用户:"+getCurrentCustomer().getNickname()+"点赞了Id为:"+ap.getId()+"的这条评论,请求服务器地址为:" + MQSetting.getLocalIP());
        }else if(appraisePraise.getIsDel() == 1){
            newAppraisePraiseService.updateCancelPraise(getCurrentBrandId(),appraiseId, getCurrentCustomerId(), 0);
            map.put("content", "用户:"+getCurrentCustomer().getNickname()+"点赞了Id为:"+appraisePraise.getId()+"的这条评论,请求服务器地址为:" + MQSetting.getLocalIP());
        }
        doPostAnsc(map);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("success", true);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/del/praise")
    public void delPraise(String appraiseId, HttpServletRequest request, HttpServletResponse response){
        AppUtils.unpack(request, response);

        newAppraisePraiseService.updateCancelPraise(getCurrentBrandId(),appraiseId, getCurrentCustomerId(), 1);
        Map map = new HashMap(4);
        map.put("brandName", getCurrentBrand().getBrandName());
        map.put("fileName", getCurrentCustomerId());
        map.put("type", "UserAction");
        map.put("content", "用户:"+getCurrentCustomer().getNickname()+"取消点赞了Id为:"+appraiseId+"的这条评论,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("success", true);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/save/comment")
    public void saveComment(AppraiseComment ac, HttpServletRequest request, HttpServletResponse response){
        AppUtils.unpack(request, response);

        ac.setId(ApplicationUtils.randomUUID());
        ac.setCreateTime(new Date());
        ac.setIsDel(0);
        ac.setShopDetailId(getCurrentShopId());
        ac.setBrandId(getCurrentBrandId());
        newAppraiseCommentService.insertComment(getCurrentBrandId(),ac);
        Map map = new HashMap(4);
        map.put("brandName", getCurrentBrand().getBrandName());
        map.put("fileName", getCurrentCustomerId());
        map.put("type", "UserAction");
        map.put("content", "用户:"+getCurrentCustomer().getNickname()+"评论了Id为:"+ac.getAppraiseId()+"的这条评论,请求服务器地址为:" + MQSetting.getLocalIP());
        doPostAnsc(map);
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");

        json.put("message", "请求成功");
        json.put("success", true);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/my/appraise")
    public void myAppraise(Integer currentPage, Integer showCount, HttpServletRequest request, HttpServletResponse response){
        AppUtils.unpack(request, response);

        List<Appraise> list = newAppraiseService.selectCustomerAllAppraise(getCurrentBrandId(),getCurrentCustomerId(), currentPage, showCount);
        for(Appraise appraise : list){
            appraise.setAppraisePraises(newAppraisePraiseService.appraisePraiseList(getCurrentBrandId(),appraise.getId()));
            appraise.setAppraiseComments(newAppraiseCommentService.appraiseCommentList(getCurrentBrandId(),appraise.getId()));
            appraise.setAppraiseFiles(newAppraiseFileService.appraiseFileList(getCurrentBrandId(),appraise.getId()));
            appraise.setShopName(shopDetailService.selectByPrimaryKey(appraise.getShopDetailId()).getName());
        }

        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/my/appraiseCount")
    public void myAppraiseCount(HttpServletRequest request, HttpServletResponse response){
        AppUtils.unpack(request, response);

        JSONObject list = new JSONObject();
        list.put("appraiseCount", newAppraiseService.selectByCustomerCount(getCurrentBrandId(),getCurrentCustomerId()));
        list.put("commentCount", newAppraiseCommentService.selectByCustomerCount(getCurrentBrandId(),getCurrentCustomerId()));
        list.put("praiseCount", newAppraisePraiseService.selectByCustomerCount(getCurrentBrandId(),getCurrentCustomerId()));

        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("data", list);
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    /**
     * 填补之前好评的图片（前500条）
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/test")
    public void testAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AppUtils.unpack(request, response);

        List<Appraise> appraises = newAppraiseService.selectByGoodAppraise(getCurrentBrandId());
        for(Appraise appraise : appraises){
            AppraiseFile file = new AppraiseFile();
            file.setId(ApplicationUtils.randomUUID());
            file.setAppraiseId(appraise.getId());
            file.setShopDetailId(appraise.getShopDetailId());
            file.setCreateTime(appraise.getCreateTime());
            file.setSort(1);
            if(appraise.getArticleId().length() >= 30){
                Article article = newArticleService.dbSelectByPrimaryKey(getCurrentBrandId(),appraise.getArticleId());
                if(article != null){
                    file.setFileUrl(article.getPhotoSmall());
                    if(article.getPhotoSquare() == null){
                        String photoSquare = ImageUtil.imageBytesScale(article.getPhotoSmall(),request);
                        file.setPhotoSquare(photoSquare);
                        article.setPhotoSquare(photoSquare);
                        newArticleService.dbUpdate(getCurrentBrandId(),article);
                    }else{
                        file.setPhotoSquare(article.getPhotoSquare());
                    }
                }
            }else{
                com.resto.brand.web.model.ShowPhoto showPhoto = showPhotoService.selectById(Integer.parseInt(appraise.getArticleId()));
                if(showPhoto != null){
                    file.setFileUrl(showPhoto.getPicUrl());
                    if(showPhoto.getPhotoSquare() == null){
                        String photoSquare = ImageUtil.imageBytesScale(showPhoto.getPicUrl(),request);
                        file.setPhotoSquare(photoSquare);
                        showPhoto.setPhotoSquare(photoSquare);
                        showPhotoService.update(showPhoto);
                    }else{
                        file.setPhotoSquare(showPhoto.getPhotoSquare());
                    }
                }
            }
            newAppraiseFileService.dbSave(getCurrentBrandId(),file);
        }
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "图片复制完成");
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }

    @RequestMapping("/new/appraiseCustomer")
    public void appraiseCustomer(String appraiseId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AppUtils.unpack(request, response);
        Appraise appraise = newAppraiseService.dbSelectByPrimaryKey(getCurrentBrandId(),appraiseId);
        Customer customer = newCustomerService.dbSelectByPrimaryKey(getCurrentBrandId(),appraise.getCustomerId());
        JSONObject json = new JSONObject();
        json.put("statusCode", "200");
        json.put("message", "请求成功");
        json.put("data", new JSONObject(customer));
        ApiUtils.setBackInfo(response, json); // 返回信息设置
    }
}
