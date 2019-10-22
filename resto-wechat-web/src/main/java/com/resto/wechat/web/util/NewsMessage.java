package com.resto.wechat.web.util;

import java.util.List;

/**
 * 文本消息
 * 
 * @author xielc
 * @date 2015-09-14
 */
public class NewsMessage extends BaseMessage {
	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<ArticleText> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<ArticleText> getArticles() {
		return Articles;
	}

	public void setArticles(List<ArticleText> articles) {
		Articles = articles;
	}
}
