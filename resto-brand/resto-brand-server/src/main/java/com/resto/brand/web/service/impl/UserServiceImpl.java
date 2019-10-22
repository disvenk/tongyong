package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.entity.DataVailedException;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.dao.RoleMapper;
import com.resto.brand.web.dao.UserMapper;
import com.resto.brand.web.model.User;
import com.resto.brand.web.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 用户Service实现类
 *
 */
@Component
@Service
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource 
    private RoleMapper roleMapper;
    
    @Override
    public GenericDao<User, Long> getDao() {
        return userMapper;
    }

    @Override
    public User authentication(User user) {
        return userMapper.authentication(user);
    }

   

    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

	@Override
	public List<User> selectList() {
		return userMapper.selectList();
	}

	
	
	@Override
	public void modify(User user) {
		checkUsernameExits(user);
		if(user.getPassword()!=null){
			user.setPassword(ApplicationUtils.pwd(user.getPassword()));
		}
		userMapper.updateByPrimaryKeySelective(user);
	}

	private void checkUsernameExits(User user) {
		User oldUser = selectByUsername(user.getUsername());
		if(oldUser!=null){
			throw new DataVailedException("用户名已存在");
		}
	}

	@Override
	public void create(@Valid User user) {
		checkUsernameExits(user);
		user.setCreateTime(new Date());
		user.setPassword(ApplicationUtils.pwd(user.getPassword()));
		userMapper.insertSelective(user);
	}


	@Override
	public int delete(Long id) {
		roleMapper.deleteUserRoles(id);
		return super.delete(id);
	}

}
