package com.resto.shop.web.model;

import java.io.Serializable;
import java.util.List;

public class MealAttr implements Serializable {
    private Integer id;

    private String name;

    private Integer sort;

    private String articleId;

    private Integer printSort;

    private Integer choiceType;

    private List<MealItem> mealItems;

    private Integer choiceCount;



    final public Integer getChoiceCount() {
        return choiceCount;
    }

    final public void setChoiceCount(Integer choiceCount) {
        this.choiceCount = choiceCount;
    }

    final public Integer getChoiceType() {
        return choiceType;
    }

    final public void setChoiceType(Integer choiceType) {
        this.choiceType = choiceType;
    }

    final public Integer getPrintSort() {
        return printSort;
    }

    final public void setPrintSort(Integer printSort) {
        this.printSort = printSort;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }

	public List<MealItem> getMealItems() {
		return mealItems;
	}

	public void setMealItems(List<MealItem> mealItems) {
		this.mealItems = mealItems;
	}
}