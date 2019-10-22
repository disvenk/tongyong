package com.resto.shop.web.dao;

import com.resto.shop.web.posDto.ArticleSupport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KONATA on 2017/8/9.
 */
public interface PosMapper  {
   List<ArticleSupport>  selectArticleSupport(@Param("shopId") String shopId);
}
