package com.resto.service.brand.mapper;


import com.resto.core.base.BaseDao;
import com.resto.service.brand.entity.User;
import java.util.List;

public interface UserMapper extends BaseDao<User, Long> {
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