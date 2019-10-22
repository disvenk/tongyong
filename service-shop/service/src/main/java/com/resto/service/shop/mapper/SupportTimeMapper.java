package com.resto.service.shop.mapper;

import com.resto.core.base.BaseDao;
import com.resto.service.shop.entity.SupportTime;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupportTimeMapper extends BaseDao<SupportTime,Integer> {
    int deleteByPrimaryKey(Integer id);

    int insert(SupportTime record);

    int insertSelective(SupportTime record);

    SupportTime selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupportTime record);

    int updateByPrimaryKey(SupportTime record);

    List<SupportTime> selectList(String shopDetailId);

	void insertArticleSupportTime(@Param("articleId") String articleId, @Param("supportTimes") Integer[] supportTimes);

	void deleteArticleSupportTime(String articleId);

	List<Integer> selectByArticleId(String articleId);
}
