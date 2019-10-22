package com.resto.brand.web.dao;

import java.util.List;

import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.web.model.User;

public interface UserMapper extends GenericDao<User, Long>{
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	User authentication(User user);

	User selectByUsername(String username);

	List<User> selectList();

	void modify(User user);

    User selectByTelephone(String telephone);
}