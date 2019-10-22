package com.resto.shop.web.controller.business;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.core.util.ExcelPOIUtil;
import com.resto.brand.core.util.ExcelUtil;
import com.resto.shop.web.controller.GenericController;
import com.resto.shop.web.model.*;
import com.resto.shop.web.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resto.brand.core.util.ExcelPOIUtil.getWorkBook;

/**
 * @Auther: yunqing.yue
 * @Date: 2018/11/5/0005 13:51
 * @Description:
 */
@Controller
@RequestMapping("articleManage")
public class ArticleManageController extends GenericController {

    @Autowired
    ArticleManageService articleManageService;

    @Autowired
    ArticleFamilyService articlefamilyService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private WeightPackageService weightPackageService;

    @Autowired
    private ArticleRecommendService articleRecommendService;

    @Autowired
    SupportTimeService supporttimeService;

    @RequestMapping("/list")
    public void list() {
    }

    @RequestMapping("/list_all")
    @ResponseBody
    public List<ArticleLibrary> listData() {
        List<ArticleLibrary> libraries = articleManageService.selectAll(getCurrentBrandId());
        return libraries;
    }

    @RequestMapping("create")
    @ResponseBody
    public Result create(@RequestBody ArticleLibrary articlelibrary) {
        articlelibrary.setBrandId(getCurrentBrandId());
        articlelibrary.setCreateUser(getCurrentShopName());
        articlelibrary.setShopId(getCurrentShopId());
        articleManageService.createArticle(articlelibrary);
        return Result.getSuccess();
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String id) {
        Result result = articleManageService.deleteArticle(id);
        return result;
    }

    @RequestMapping("edit")
    @ResponseBody
    public Result modifyArticle(@RequestBody ArticleLibrary articlelibrary) {
        Result result = articleManageService.modifyArticle(articlelibrary);
        return result;
    }

    /**
     * 获取菜品类别
     *
     * @return
     */
    @RequestMapping("articleCategory")
    @ResponseBody
    public List<ArticleFamily> articleCategory() {
        List<ArticleFamily> articleFamilies = articlefamilyService.articleCategory(getCurrentBrandId());
        return articleFamilies;
    }

    /**
     * 获取规格包
     *
     * @return
     */
    @RequestMapping("getUnitsPackage")
    @ResponseBody
    public List<Unit> getUnitsPackage() {
        List<Unit> unitsPackage = unitService.getUnitsPackage();
        return unitsPackage;
    }

    /**
     * 获取单个规格包
     *
     * @return
     */
    @RequestMapping("getAnUnitsPackage")
    @ResponseBody
    public Unit getAnUnitsPackage(String unitId) {
        Unit unitsPackage = unitService.getAnUnitsPackage(unitId);
        return unitsPackage;
    }

    /**
     * 获取重量包
     *
     * @return
     */
    @RequestMapping("getWeightPackage")
    @ResponseBody
    public List<WeightPackage> getWeightPackage() {
        return weightPackageService.getWeightPackage();
    }

    /**
     * 获取推荐餐包
     *
     * @return
     */
    @RequestMapping("getRecommendPackage")
    @ResponseBody
    public List<ArticleRecommend> getRecommendPackage() {
        return articleRecommendService.getRecommendPackage();
    }

    /**
     * 获取供应时间
     *
     * @return
     */
    @RequestMapping("getSupportTimePackage")
    @ResponseBody
    public List<SupportTime> getSupportTimePackage() {
        return supporttimeService.getSupportTimePackage();
    }

    /**
     * 获取单品菜品
     *
     * @return
     */
    @RequestMapping("getProductsItem")
    @ResponseBody
    public List<ArticleLibrary> getProductsItem() {
       return articleManageService.getProductsItem();
    }


    /**
     * 导出模板
     * @param request
     * @return
     */
    @RequestMapping("downloadTemplate")
    @ResponseBody
    public Result downloadTemplate(HttpServletRequest request) {
        String fileName = "菜品模板.xls";
        String path = request.getSession().getServletContext().getRealPath(fileName);
        Map<String, String> map = new HashMap<>();
        map.put("brandName", getBrandName());
        map.put("reportType", "菜品模板");// 表的头，第一行内容
        map.put("num", "28");// 显示的位置
        map.put("reportTitle", fileName);// 表的名字
        String[][] headers = {{"销售类型\r\n必填\r\n例: 堂食、外卖、堂食+外卖", "20"}, {"菜品类型\r\n必填\r\n例: 单品、套餐", "15"}, {"菜品名称\r\n必填\r\n最多20字符", "15"}, {"菜品品牌定价\r\n必填\r\n填写纯数字", "15"}, {"粉丝价\r\n选填\r\n填写纯数字", "15"}, {"称重价格\r\n选填\r\n填写纯数字", "15"}, {"餐盒数量\r\n选填\r\n填写纯数字", "15"},{"菜品描述\r\n选填\r\n不得超过250个字符", "17"}};
        try {
            ExcelUtil<Object> excelUtil = new ExcelUtil<>();
            OutputStream out = new FileOutputStream(path);
            excelUtil.downloadTemplate(headers, out, map);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成模板出错！");
            return new Result(false);
        }
        return getSuccessResult(path);
    }

    /**
     * 下载模板
     * @param path
     * @param response
     */
    @RequestMapping("/downloadExcel")
    public void downloadExcel(String path, HttpServletResponse response) {
        ExcelUtil<Object> excelUtil = new ExcelUtil<>();
        try {
            excelUtil.download(path, response);
            JOptionPane.showMessageDialog(null, "导出成功！");
            log.info("excel导出成功");
        } catch (Exception e) {
            log.error("下载模板出错！");
            e.printStackTrace();
        }
    }

    /**
     * 导入菜品
     * @param request
     * @param response
     * @param file
     * @return
     */
    @RequestMapping("/importExcel")
    @ResponseBody
    public Result importExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file", required = true) MultipartFile file) {
        Result result = new Result();
        try {
             result = ExcelPOIUtil.checkFile(file);
            //获得Workbook工作薄对象
            Workbook workbook = getWorkBook(file);
            //得到一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                ArticleLibrary library = new ArticleLibrary();
                String value = row.getCell(0)==null?null:row.getCell(0).getStringCellValue();
                if (value.isEmpty()){
                    continue;
                }
                if (StringUtils.equals(value, "堂食")) {
                    library.setDistributionModeId(1);
                } else if (StringUtils.equals(value, "外卖")) {
                    library.setDistributionModeId(2);
                } else if (StringUtils.equals(value, "堂食+外卖")) {
                    library.setDistributionModeId(3);
                }
                String value1 = row.getCell(1)==null?null:row.getCell(1).getStringCellValue();
                if (StringUtils.equals(value1, "单品")) {
                    library.setArticleType(1);
                } else if (StringUtils.equals(value1, "套餐")) {
                    library.setArticleType(2);
                }
                library.setId(ApplicationUtils.randomUUID());
                library.setName(row.getCell(2)==null?null:row.getCell(2).getStringCellValue());
                library.setPrice(new BigDecimal(row.getCell(3)==null?0:row.getCell(3).getNumericCellValue()));
                library.setFansPrice(new BigDecimal(row.getCell(4)==null?0:row.getCell(4).getNumericCellValue()));
                library.setCattyMoney(new BigDecimal(row.getCell(5)==null?0:row.getCell(5).getNumericCellValue()));
                double v = row.getCell(6)==null?0:row.getCell(6).getNumericCellValue();
                library.setMealFeeNumber((int) v);
                library.setDescription(row.getCell(7)==null?null:row.getCell(7).getStringCellValue());
                library.setState(1L);
                library.setBrandId(getCurrentBrandId());
                articleManageService.insertSelective(library);
            }
            result.setSuccess(true);
            result.setMessage("导入成功！");
        } catch (IOException e) {
            result.setSuccess(false);
            result.setMessage("导入失败！");
            e.printStackTrace();
        }
        return result;
    }


}
