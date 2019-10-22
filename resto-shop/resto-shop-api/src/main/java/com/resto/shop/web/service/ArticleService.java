package com.resto.shop.web.service;

import com.resto.brand.core.generic.GenericService;
import com.resto.brand.web.dto.ArticleSellDto;
import com.resto.shop.web.dto.ArticleSellCountDto;
import com.resto.shop.web.dto.KitchenDto;
import com.resto.shop.web.model.Article;
import com.resto.shop.web.model.ArticleStock;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ArticleService extends GenericService<Article, String> {

	List<Article> selectList(String currentShopId);

	List<Article> selectList(String brandId,String currentShopId);

	Article save(Article article,String brandId,String shopId);

	int update(Article article,String brandId,String shopId);

	Article selectFullById(String id,String show);

	Article selectFullById(String id,String show,String brandId,String shopId);

	/**
	 * 通过店铺Id以及配送模式查询
	 * @param currentShopId
	 * @param distributionModeId
	 * @return
	 */
	List<Article> selectListFull(String currentShopId, Integer distributionModeId,String show);

	/**
	 * 根据店铺id,推荐类型id查询所有推荐菜品
	 * @param currentShopId
	 * @param recommendCcategoryId
	 * @param show
	 * @return
	 */
	List<Article> selectListByShopIdRecommendCategory(String currentShopId, String recommendCcategoryId, String show);

	/**
	 * 根据 是否 谷清 查询菜品信息
	 * @param isEmpty
	 * @return
	 */
	List<Article> selectListByIsEmpty(Integer isEmpty,String shopId);
	
	/**
	 * 根据 菜品Id 设置谷清
	 * @param articleId
	 */
	void changeEmpty(Integer isEmpty,String articleId);
    
	void addLikes(String articleId);
	
	void updateLikes(String articleId,Long likes);

	/**
	 * 初始化库存
	 */
	void initStock();

	List<ArticleStock> getStock(String shopId,String familyId,Integer empty,Integer activated);

	Boolean clearStock(String articleId,String shopId);

	Boolean editStock(String articleId,Integer count,String shopId);

	Boolean setActivated(String articleId,Integer activated);

	List<Article> getSingoArticle(String shopId);

	List<Article> getSingoArticleAll(String shopId);

	void deleteRecommendId(String recommendId);



	/**
	 * 菜品库分配菜品
	 */
	void assignArticle(String [] shopList,String articleList[]);

	/**
	 * 分配套餐
	 * @param shopList
	 * @param articleList
     */
	void assignTotal(String [] shopList,String articleList[]);

	/**
	 * 删除单品时候校验
	 * @param id
	 * @return
     */
	List<Article> delCheckArticle(String id);

	void updatePhotoSquare(@Param("id") String id, @Param("photoSquare") String photoSquare);
	
	void updateArticleImg(Article article);

	void addArticleLikes(String articleId);
	
	List<Article> selectsingleItem(String shopId);
	
	List<ArticleSellDto> callOrderArtcile(Map<String, Object> selectMap);

	Article selectByPrimaryKey(String articleId);

	/**
	 * 根据分类查询分类下的所有菜品
	 */
	List<Article> getArticleListByFamily(String shopId, String articleFamilyId, Integer currentPage, Integer showCount);

	/**
	 * 修改菜品名称的首字母
	 * @param initials
	 * @param articleId
     */
	void updateInitialsById(String initials, String articleId);

	/**
	 * 不分条件下所有的菜品
	 * @return
     */
	List<Article> selectArticleList();

    /**
     * 根据菜品类型查询菜品
     * @param selectMap
     * @return
     */
    List<ArticleSellDto> selectArticleByType(Map<String, Object> selectMap);

    /**
     * 根据菜品类型查询菜品总销量和总销售额
     * @param selectMap
     * @return
     */
    Map<String, Object> callArticleOrderCount(Map<String, Object> selectMap);

    List<String> selectArticleSort(Map<String, Object> selectMap);

    /**
     * 新pos根据菜品分类查询 菜品(在供应时间内)
     * @param shopId
     * @param page
     * @param size
     * @param familyId
     * @return
     */
    List<Article> selectnewPosListByFamillyId(String shopId,Integer page,Integer size,String familyId);

    /**
     * 查询资源服务器图片
     * @param currentBrandId
     * @return
     */
    List<Article> selectHasResourcePhotoList(String currentBrandId);

	/**
	 * 根据虚拟餐品id查询菜品
	 * @param virtualId
	 * @return
	 */
	List<Article> getArticleList(Integer virtualId,String shopId);


	int updateArticle(String articleId,String shopId);

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
	 * 根据 店铺ID 查询店铺下的所有菜品数据
	 * Pos2.0 数据拉取接口			By___lmx
	 * @param shopId
	 * @return
	 */
	List<Article> selectArticleByShopId(String shopId);



	List<ArticleSellCountDto> findArticleByLastCountTime(String shopId,String lastCountTime);


	/**
	 * 获得所有预点餐的菜品
	 * @param shopId
	 * @return
	 */
	List<com.resto.api.article.entity.Article> getArticleBefore(String shopId,String tableNumber,String customerId);

	/**
	 * 获取页面端菜品预警库存数量统计
	 * @return
	 */
	/*int[] getStockNum(String shopId);*/

	List<KitchenDto> selectTree(String brandId,String shopId);

	void saveArticleAndkitchenAndPrinter(String aritcleId,String brandId,String shopId,List<Integer> kitchenGroupIds);

	void upDateArticleAndkitchenAndPrinter(String articleId,String brandId,String shopId,List<Integer> kitchenGroupIds);

	/**
	 * 每日恢复库存,供定时任务使用
	 */
	void  updateCurrentWorkingStock();

    /**
     * 通过菜单Id、店铺Id向店铺插入菜单里的菜品
     * @param menuId
     * @param shopDetailId
     */
	void insertMenuArticle(Long menuId, String shopDetailId, Integer operation);

	List<ArticleSellDto> callOrderArtcileNew(Map<String, Object> selectMap);

	List<ArticleSellDto> selectArticleByTypeNew(Map<String, Object> selectMap);

    Map<String,Object> callArticleOrderCountNew(Map<String, Object> selectMap);

    Article selectArticleByArticleId(String articleId, String brandId, String shopId);
}
