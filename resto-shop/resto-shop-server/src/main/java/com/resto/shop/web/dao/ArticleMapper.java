package com.resto.shop.web.dao;

import com.resto.brand.core.generic.GenericDao;
import com.resto.shop.web.dto.ArticleSellCountDto;
import com.resto.shop.web.dto.KitchenDto;
import com.resto.shop.web.model.Article;
import com.resto.shop.web.model.ArticleStock;
import com.resto.shop.web.model.KitchenGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ArticleMapper extends GenericDao<Article, String>{
    int deleteByPrimaryKey(String id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

	List<Article> selectList(@Param(value = "shopId") String currentShopId);

	List<Article> selectListByShopIdAndDistributionId(String currentShopId, Integer distributionModeId);

	/**
	 * 根据店铺id，推荐类型id查询所有推荐菜品
	 * @param currentShopId
	 * @param recommendCcategoryId
	 * @return
	 */
	List<Article> selectListByShopIdRecommendCategory(String currentShopId, String recommendCcategoryId);

	List<Article> selectBySupportTimeId(@Param("times") List<Integer> supportTimes,@Param("shopId") String currentShopId);
	
	/**
	 * 根据是否谷清 查询菜品信息
	 * @param isEmpty
	 * @return
	 */
	List<Article> selectListByIsEmpty(@Param("isEmpty") Integer isEmpty,@Param("shopId")String shopId);
	
	/**
	 * 根据菜品 Id 设置谷清
	 * @param articleId
	 */
	void changeEmpty(@Param("isEmpty") Integer isEmpty,@Param("articleId") String articleId);

	void updateLikes(String articleId, Long likes);

	void addLikes(String articleId);

	void initSuitStock();

	void initSize();

	List<ArticleStock> getStock(@Param("shopId") String shopId, @Param("familyId") String familyId,
								@Param("empty") Integer empty,@Param("freeDay") Integer freeDay,@Param("activated")Integer activated);

	Integer clearStock(@Param("articleId")String articleId,@Param("emptyRemark") String emptyRemark);





//	Integer cleanPriceAll(@Param("articleId")String articleId,@Param("emptyRemark") String emptyRemark);

	Integer editStock(@Param("articleId")String articleId,@Param("count")Integer count,@Param("emptyRemark") String emptyRemark);

//	Integer editPriceStock(@Param("articleId")String articleId,@Param("count")Integer count,@Param("emptyRemark") String emptyRemark);

	void initSizeCurrent();

	void clearMain(@Param("articleId")String articleId,@Param("emptyRemark") String emptyRemark);

	void initEmpty();
	
	/**
	 * 设置 菜品 下架（0）/上架（1） 
	 * @param articleId
	 * @param activated
	 * @return
	 */
	int setActivate(@Param("articleId")String articleId,@Param("activated")Integer activated);

	List<Article> getSingoArticle(String shopId);

	List<Article> getSingoArticleAll(String shopId);

	int deleteRecommendId(String recommendId);

//	int saveLog(@Param("result") Integer result,@Param("taskId") String taskId);


	int selectPidAndShopId(@Param("shopId") String shopId,@Param("articleId") String articleId);


	/**
	 * 得到套餐下的全部子品
	 * @param articleId
	 * @return
     */
	List<Article> getArticleByMeal(String articleId);

	Article selectByPid(@Param("pId") String pId,@Param("shopId") String shopId);

	List<Article> selectByName(@Param("name") String name,@Param("shopId") String shopId);

	List<Article> delCheckArticle(String id);

	void updatePhotoSquare(@Param("id") String id, @Param("photoSquare") String photoSquare);

	void addArticleLikes(String articleId);
	
	List<Article> selectsingleItem(@Param("shopId") String shopId);

	/**
	 * 根据分类查询分类下的所有菜品
	 */
	List<Article> getArticleListByFamily(@Param("times") List<Integer> supportTimes, @Param("shopId")String shopId, @Param("articleFamilyId")String articleFamilyId, @Param("currentPage")Integer currentPage, @Param("showCount")Integer showCount);

	void updateInitialsById(@Param("initials") String initials, @Param("articleId") String articleId);

	List<Article> selectArticleList();

    List<String> selectArticleSort(Map<String, Object> selectMap);

    /**
     * 查询在供应时间内 查询分类所有菜品(分页)
     * @param supportTimes
     * @param shopId
     * @param familyId
     * @return
     */
    List<Article> selectnewPosListByFamillyId(@Param("times") List<Integer> supportTimes, @Param("shopId") String shopId,@Param("articleFamilyId") String familyId);

    /**
     * 查询菜品中图片是在资源服务器上的
     * @param currentBrandId
     * @return
     */
    List<Article> selectHasResourcePhotoList(String currentBrandId);

	/**
	 * 更新该餐品库存 （-1）（无规格）
	 * @param articleId 餐品id
	 * @return
	 */
	Boolean updateArticleStock(@Param("articleId") String articleId,@Param("type") String type,@Param("count") Integer count);

	/**
	 * 库存为0时设置沽清	---	tb_article
	 * @param articleId
	 * @return
	 */
	Boolean setEmpty(String articleId);

	/**
	 * 还原库存时重置售罄状态	---	tb_article
	 * @param articleId
	 * @return
	 */
	Boolean setEmptyFail(String articleId);

	/**
	 * 将单品最低库存设置为 套餐库存
	 * @return
	 */
	Boolean setStockBySuit(@Param("shopId")String shopId);

	List<Article> getStockBySuit(String shopId);

	/**
	 * 菜品月销售量
	 * @param articleId
	 * @param time
     * @return
     */
	Integer selectSumByMonthlySales(@Param("articleId") String articleId, @Param("time") String time);

	/**
	 * 根据 店铺ID 查询店铺下的所有菜品数据
	 * Pos2.0 数据拉取接口			By___lmx
	 * @param shopId
	 * @return
	 */
	List<Article> selectArticleByShopId(@Param("shopId") String shopId);

    List<ArticleSellCountDto> findArticleByLastCountTime(@Param("shopId")String shopId ,@Param("lastCountTime") String lastCountTime);

	List<Article> getArticleBefore(String shopId);

	List<KitchenDto> selectByArticle(@Param("articleId") String articleId);

	List<KitchenGroup> selectByArticleAndShopId(@Param("articleId") String articleId,@Param("shopId") String shopId);

	/**
	 * 恢复菜品当前库存为工作日库存
	 */
    void updateCurrentWorkingStock();

	/**
	 * 初始化有规格餐品库存
	 */
	void updateArticlePriceCurrentWorkingStock();

	/**
	 * 初始化套餐库存
	 */
	void updatePackageCurrentWorkingStock();

	/**
	 * 初始化有规格餐品主品库存(等于其子品库存之和)
	 */
	void updateBigCurrentWorkingStock();

	/**
	 * 根据虚拟餐品id查询菜品
	 * @param virtualId
	 * @return
	 */
	List<Article> getArticleList(@Param("virtualId")Integer virtualId,@Param("shopId")String shopId);


	int updateArticle(@Param("articleId")String articleId,@Param("shopId")String shopId);

	void deleteByMenuIdAndShopId(@Param("menuId") Long menuId, @Param("shopDetailId") String shopDetailId);

    Article selectArticleByArticleId(String articleId);
}