package com.resto.shop.web.controller.business;

import com.google.zxing.WriterException;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.Encrypter;
import com.resto.brand.core.util.QRCodeUtil;
import com.resto.brand.web.model.Brand;
import com.resto.brand.web.model.ShopDetail;
import com.resto.brand.web.model.TableQrcode;
import com.resto.brand.web.service.BrandService;
import com.resto.brand.web.service.RedisService;
import com.resto.brand.web.service.ShopDetailService;
import com.resto.brand.web.service.TableQrcodeService;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.Area;
import com.resto.shop.web.posDto.ShopMsgChangeDto;
import com.resto.shop.web.service.AreaService;
import com.resto.shop.web.service.PosService;
import com.resto.shop.web.util.NewPosTransmissionUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by KONATA on 2017/4/6.
 */
@Controller
@RequestMapping("tablemanager")
public class TableManagerController extends GenericController {

    @Autowired
    TableQrcodeService tableQrcodeService;

    @Autowired
    ShopDetailService shopDetailService;

    @Autowired
    BrandService brandService;

    @Autowired
    AreaService areaService;

    @Autowired
    private PosService posService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/list")
    public void list() {
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<TableQrcode> listData() {
        List<TableQrcode> tableQrcodes = tableQrcodeService.selectUsedByShopId(getCurrentShopId());
        for (TableQrcode tableQrcode : tableQrcodes) {
            Object sta = redisService.get(getCurrentShopId() + tableQrcode.getTableNumber() + "status");
            if (sta==null){
                tableQrcode.setSeatState(true);
            }else if ((Boolean) sta){
                tableQrcode.setSeatState(true);
            }else if (!(Boolean) sta){
                tableQrcode.setSeatState(false);
            }
        }
        return tableQrcodes;
    }

    /**
     * 释放服务桌号
     *
     * @param state
     * @param tableNumber
     */
    @RequestMapping("/removeTable")
    @ResponseBody
    public Result removeTable(boolean state, Integer tableNumber) {
        log.info(">>>>>释放服务桌号状态>>>>>>>>>>"+state);
        log.info(">>>>>释放服务桌号>>>>>>>>>>"+tableNumber);
        try {
            if (!state) {
//                RedisUtil.remove(getCurrentShopId() + tableNumber + "status");
                redisService.remove(getCurrentShopId() + tableNumber + "status");
            }
            return Result.getSuccess();
        } catch (Exception e) {
            log.error(">>>>>>>>>>释放桌号失败>>>>>>>>>>>>>>>"+e);
        }

        return null;
    }



    /**
     * 释放本地桌号
     *
     * @param state
     * @param tableNumber
     */
    @RequestMapping("/removeTableLocal")
    @ResponseBody
    public Result removeTableLocal(boolean state, Integer tableNumber) {
        log.info(">>>>>释放本地桌号状态>>>>>>>>>>"+state);
        log.info(">>>>>释放本地桌号>>>>>>>>>>"+tableNumber);
        try {
            if (!state) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders httpHeaders = new HttpHeaders();
                MediaType type = MediaType.parseMediaType("application/json");
                httpHeaders.setContentType(type);
                httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
//                JSONObject jsonObj = JSONObject.fromObject(orderVo);
//                HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), httpHeaders);
                String result = restTemplate.postForObject("http://139.196.222.42:8580/pos/LocalPosSyncData/updateTableState?brandId="+getCurrentBrandId()+"&shopId="+getCurrentShopId()+"&tableNumber="+tableNumber.toString()+"&state=1", null, String.class);
                JSONObject json = JSONObject.fromObject(result);
                boolean success = (boolean)json.get("success");
                if (success) {
                    return Result.getSuccess();
                }
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>释放桌号失败>>>>>>>>>>>>>>>"+e);
        }

        return null;
    }

    //
//
//
//    @RequestMapping("list_one")
//    @ResponseBody
//    public Result list_one(Long id){
//        Area area = areaService.selectById(id);
//        return getSuccessResult(area);
//    }
//
//    @RequestMapping("create")
//    @ResponseBody
//    public Result create(@Valid Area area){
//        area.setShopDetailId(getCurrentShopId());
//        areaService.insert(area);
//        return Result.getSuccess();
//    }
//
    @RequestMapping("modify")
    @ResponseBody
    public Result modify(@Valid TableQrcode tableQrcode) {
        Area area = areaService.selectById(tableQrcode.getAreaId());
        tableQrcode.setAreaName(area.getName());
        tableQrcodeService.update(tableQrcode);
        //消息通知newpos后台发生变化
        ShopMsgChangeDto shopMsgChangeDto=new ShopMsgChangeDto();
        shopMsgChangeDto.setBrandId(getCurrentBrandId());
        shopMsgChangeDto.setShopId(getCurrentShopId());
        shopMsgChangeDto.setTableName(NewPosTransmissionUtils.TBTABLEQRCODE);
        shopMsgChangeDto.setType("modify");
        shopMsgChangeDto.setId(tableQrcode.getId().toString());
        try{
            posService.shopMsgChange(shopMsgChangeDto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Result.getSuccess();
    }

    //
    @RequestMapping("download")
    public void download(Long id, HttpServletRequest request, HttpServletResponse response) throws IOException, WriterException {
        ShopDetail shopDetail = shopDetailService.selectByPrimaryKey(getCurrentShopId());
        Brand brand = brandService.selectById(shopDetail.getBrandId());
        String conteng = "http://" + brand.getBrandSign() + ".restoplus.cn/wechat/index?vv=" + Encrypter.encrypt(String.valueOf(id));
        QRCodeUtil.createQRCode(conteng, "jpg", response.getOutputStream());
    }
}
