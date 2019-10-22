package com.resto.shop.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.resto.brand.web.model.Platform;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@JsonInclude(Include.NON_NULL)
public class Article implements Serializable{

    /**
     * 单品
     */
    public static final int ARTICLE_TYPE_SIGNLE = 1;
    /**
     * 套餐
     */
    public static final int ARTICLE_TYPE_MEALS = 2;

    private String id;

    private String name;

    private String nameAlias;

    private String nameShort;

    private String photoBig;

    private String photoSmall;

    private String ingredients;

    private String description;

    private Boolean isEmpty;

    private Integer sort;

    private Boolean activated;

    private Boolean state;

    private Integer remainNumber;

    private Long saleNumber;

    private Long showSaleNumber;

    private Date updateTime;

    private Date createTime;

    private String shopDetailId;

    private String articleFamilyId;

    private String createUserId;

    private String updateUserId;

    private BigDecimal price;

    private BigDecimal fansPrice;

    /**
     * 供应时间关联字段，用于储存 菜品的折扣百分比
     */
    private int discount;

    private Boolean hasMultPrice;

    private String hasUnit;

    private String peference;

    private String unit;

    private ArticleFamily articleFamily;

    private Boolean showBig;

    private Boolean showDesc;

    private Boolean isRemind;

    private String controlColor;

    private Integer articleType;

    private Long likes;

    private List<MealAttr> mealAttrs;

    private Integer isHidden;

    /**
     * 用于保存 类型名称
     */
    private String articleFamilyName;

    private List<ArticlePrice> articlePrices = new ArrayList<>();
    private Integer[] supportTimes;
    private Integer[] kitchenList;

    //工作日库存
    private Integer stockWorkingDay;

    //周末库存
    private Integer stockWeekend;

    //菜品预警库存数量设置
    private Integer inventoryWarning;


    private Integer currentWorkingStock;

    //推荐餐品包id
    private String recommendId;


    //规格属性
    private List<Unit> units;

    private Integer newUnit;

    private Integer recommendCount;

    private String pId;

    //饿了吗名称
    private String elemeName;

    private List<Platform> platforms;

    private String photoSquare;

    private Integer mealFeeNumber;

    private Integer isMainFood;

    //虚拟餐品包id
    private Integer virtualId;

    //餐品首字母
    private String initials;

    private  Integer count;

    private String recommendCategoryId;

    private Integer monthlySales;          //菜品月销售量

    private Integer photoType;             //菜品图片显示   1大图  2小图  3无图 4超大图  5正方形图

    private String photoLittle;            //菜品图片类型2  小图存放地址

    private String photoSuper;             //菜品图片类型4  超大图存放地址

    private String photoSquareOriginal;    //菜品图片类型5  正方形图存放地址

    private Integer openCatty;             //是否开启称斤买卖菜品

    private BigDecimal cattyMoney;         //称斤买卖 价格 单位（500g  斤）

    private Integer needRemind;            //是否需要优先提醒添加此菜品

    private String gifUrl;                 //gif图片路径

    private Integer weightPackageId;       //重量包id

    private List<KitchenGroup> kitchenGroups;

    private List<Integer> kitchenGroupIdList;

    //菜品库新增字段
    private Boolean openArticleLibrary;

    private String articleId;

    private Integer menuId;

    private String recommendationDegree;

    private String mealOutTime;

    private String articleKind;

    private String articleLabel;

    private String articleHot;

    private String articleComponent;

    @Setter
    @Getter
    private String activatedName;

    @Setter
    @Getter
    private String articleTypeName;

    public List<Integer> getKitchenGroupIdList() {
        return kitchenGroupIdList;
    }

    public void setKitchenGroupIdList(List<Integer> kitchenGroupIdList) {
        this.kitchenGroupIdList = kitchenGroupIdList;
    }

    public List<KitchenGroup> getKitchenGroups() {
        return kitchenGroups;
    }

    public void setKitchenGroups(List<KitchenGroup> kitchenGroups) {
        this.kitchenGroups = kitchenGroups;
    }

    public Integer getWeightPackageId() {
        return weightPackageId;
    }

    public void setWeightPackageId(Integer weightPackageId) {
        this.weightPackageId = weightPackageId;
    }

    public String getPhotoSuper() {
        return photoSuper;
    }

    public void setPhotoSuper(String photoSuper) {
        this.photoSuper = photoSuper;
    }

    public String getPhotoSquareOriginal() {
        return photoSquareOriginal;
    }

    public void setPhotoSquareOriginal(String photoSquareOriginal) {
        this.photoSquareOriginal = photoSquareOriginal;
    }

    public Integer getNeedRemind() {
        return needRemind;
    }

    public void setNeedRemind(Integer needRemind) {
        this.needRemind = needRemind;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public Integer getOpenCatty() {
        return openCatty;
    }

    public void setOpenCatty(Integer openCatty) {
        this.openCatty = openCatty;
    }

    public BigDecimal getCattyMoney() {
        return cattyMoney;
    }

    public void setCattyMoney(BigDecimal cattyMoney) {
        this.cattyMoney = cattyMoney;
    }

    public Integer getMonthlySales() {
        return monthlySales;
    }

    public void setMonthlySales(Integer monthlySales) {
        this.monthlySales = monthlySales;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public Integer getVirtualId() {
        return virtualId;
    }

    public void setVirtualId(Integer virtualId) {
        this.virtualId = virtualId;
    }

    public Integer getIsMainFood() {
        return isMainFood;
    }

    public void setIsMainFood(Integer isMainFood) {
        this.isMainFood = isMainFood;
    }

    public Integer getMealFeeNumber() {
        return mealFeeNumber;
    }

    public void setMealFeeNumber(Integer mealFeeNumber) {
        this.mealFeeNumber = mealFeeNumber;
    }

    public String getPhotoSquare() {
        return photoSquare;
    }

    public void setPhotoSquare(String photoSquare) {
        this.photoSquare = photoSquare;
    }

    final public List<Platform> getPlatforms() {
        return platforms;
    }

    final public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    final public String getElemeName() {
        return elemeName;
    }

    final public void setElemeName(String elemeName) {
        this.elemeName = elemeName;
    }

    final public String getpId() {
        return pId;
    }

    final public void setpId(String pId) {
        this.pId = pId;
    }

    final public Integer getRecommendCount() {
        return recommendCount;
    }

    final public void setRecommendCount(Integer recommendCount) {
        this.recommendCount = recommendCount;
    }

    final public Integer getIsHidden() {
        return isHidden;
    }

    final public void setIsHidden(Integer isHidden) {
        this.isHidden = isHidden;
    }

    final public Integer getNewUnit() {
        return newUnit;
    }

    final public void setNewUnit(Integer newUnit) {
        this.newUnit = newUnit;
    }

    final public List<Unit> getUnits() {
        return units;
    }

    final public void setUnits(List<Unit> units) {
        this.units = units;
    }

    final public String getRecommendId() {
        return recommendId;
    }

    final public void setRecommendId(String recommendId) {
        this.recommendId = recommendId;
    }

    final public Integer getCurrentWorkingStock() {
        return currentWorkingStock;
    }

    final public void setCurrentWorkingStock(Integer currentWorkingStock) {
        this.currentWorkingStock = currentWorkingStock;
    }

    final public Integer getStockWorkingDay() {
        return stockWorkingDay;
    }

    final public void setStockWorkingDay(Integer stockWorkingDay) {
        this.stockWorkingDay = stockWorkingDay;
    }

    final public Integer getStockWeekend() {
        return stockWeekend;
    }

    final public void setStockWeekend(Integer stockWeekend) {
        this.stockWeekend = stockWeekend;
    }

    public ArticleFamily getArticleFamily() {
        return articleFamily;
    }

    public void setArticleFamily(ArticleFamily articleFamily) {
        this.articleFamily = articleFamily;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getNameAlias() {
        return nameAlias;
    }

    public void setNameAlias(String nameAlias) {
        this.nameAlias = nameAlias == null ? null : nameAlias.trim();
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort == null ? null : nameShort.trim();
    }

    public String getPhotoBig() {
        return photoBig;
    }

    public void setPhotoBig(String photoBig) {
        this.photoBig = photoBig == null ? null : photoBig.trim();
    }

    public String getPhotoSmall() {
        return photoSmall;
    }

    public void setPhotoSmall(String photoSmall) {
        this.photoSmall = photoSmall == null ? null : photoSmall.trim();
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients == null ? null : ingredients.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Boolean getIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(Boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Integer getRemainNumber() {
        return remainNumber;
    }

    public void setRemainNumber(Integer remainNumber) {
        this.remainNumber = remainNumber;
    }

    public Long getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(Long saleNumber) {
        this.saleNumber = saleNumber;
    }

    public Long getShowSaleNumber() {
        return showSaleNumber;
    }

    public List<ArticlePrice> getArticlePrices() {
        return articlePrices;
    }

    public void setArticlePrices(List<ArticlePrice> articlePrices) {
        this.articlePrices = articlePrices;
    }

    public void setShowSaleNumber(Long showSaleNumber) {
        this.showSaleNumber = showSaleNumber;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getShopDetailId() {
        return shopDetailId;
    }

    public void setShopDetailId(String shopDetailId) {
        this.shopDetailId = shopDetailId == null ? null : shopDetailId.trim();
    }

    public String getArticleFamilyId() {
        return articleFamilyId;
    }

    public void setArticleFamilyId(String articleFamilyId) {
        this.articleFamilyId = articleFamilyId == null ? null : articleFamilyId.trim();
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getFansPrice() {
        return fansPrice;
    }

    public void setFansPrice(BigDecimal fansPrice) {
        this.fansPrice = fansPrice;
    }

    public Boolean getHasMultPrice() {
        return hasMultPrice;
    }

    public void setHasMultPrice(Boolean hasMultPrice) {
        this.hasMultPrice = hasMultPrice;
    }

    public String getHasUnit() {
        return hasUnit;
    }

    public void setHasUnit(String hasUnit) {
        this.hasUnit = hasUnit;
    }

    public Integer[] getSupportTimes() {
        return supportTimes;
    }

    public void setSupportTimes(Integer[] supportTimes) {
        this.supportTimes = supportTimes;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPeference() {
        return peference;
    }

    public void setPeference(String peference) {
        this.peference = peference;
    }

    public Integer[] getKitchenList() {
        return kitchenList;
    }

    public void setKitchenList(Integer[] kitchenList) {
        this.kitchenList = kitchenList;
    }

    public String getArticleFamilyName() {
        return articleFamilyName;
    }

    public void setArticleFamilyName(String articleFamilyName) {
        this.articleFamilyName = articleFamilyName;
    }

    public Boolean getShowBig() {
        return showBig;
    }

    public String getControlColor() {
        return controlColor;
    }

    public void setShowBig(Boolean showBig) {
        this.showBig = showBig;
    }

    public void setControlColor(String controlColor) {
        this.controlColor = controlColor;
    }

    public Boolean getShowDesc() {
        return showDesc;
    }

    public Boolean getIsRemind() {
        return isRemind;
    }

    public void setShowDesc(Boolean showDesc) {
        this.showDesc = showDesc;
    }

    public void setIsRemind(Boolean isRemind) {
        this.isRemind = isRemind;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public List<MealAttr> getMealAttrs() {
        return mealAttrs;
    }

    public void setMealAttrs(List<MealAttr> mealAttrs) {
        this.mealAttrs = mealAttrs;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getRecommendCategoryId() {
        return recommendCategoryId;
    }

    public void setRecommendCategoryId(String recommendCategoryId) {
        this.recommendCategoryId = recommendCategoryId;
    }

    public Integer getPhotoType() {
        return photoType;
    }

    public void setPhotoType(Integer photoType) {
        this.photoType = photoType;
    }

    public String getPhotoLittle() {
        return photoLittle;
    }

    public void setPhotoLittle(String photoLittle) {
        this.photoLittle = photoLittle;
    }

    public Integer getInventoryWarning() {
        return inventoryWarning;
    }

    public void setInventoryWarning(Integer inventoryWarning) {
        this.inventoryWarning = inventoryWarning;
    }

    public Boolean getOpenArticleLibrary() {
        return openArticleLibrary;
    }

    public void setOpenArticleLibrary(Boolean openArticleLibrary) {
        this.openArticleLibrary = openArticleLibrary;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getRecommendationDegree() {
        return recommendationDegree;
    }

    public void setRecommendationDegree(String recommendationDegree) {
        this.recommendationDegree = recommendationDegree;
    }

    public String getMealOutTime() {
        return mealOutTime;
    }

    public void setMealOutTime(String mealOutTime) {
        this.mealOutTime = mealOutTime;
    }

    public String getArticleKind() {
        return articleKind;
    }

    public void setArticleKind(String articleKind) {
        this.articleKind = articleKind;
    }

    public String getArticleLabel() {
        return articleLabel;
    }

    public void setArticleLabel(String articleLabel) {
        this.articleLabel = articleLabel;
    }

    public String getArticleHot() {
        return articleHot;
    }

    public void setArticleHot(String articleHot) {
        this.articleHot = articleHot;
    }

    public String getArticleComponent() {
        return articleComponent;
    }

    public void setArticleComponent(String articleComponent) {
        this.articleComponent = articleComponent;
    }
}