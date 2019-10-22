package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.entity.DataVailedException;
import com.resto.brand.core.entity.JSONResult;
import com.resto.brand.core.entity.Result;
import com.resto.brand.core.generic.GenericDao;
import com.resto.brand.core.generic.GenericServiceImpl;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.dao.BrandUserMapper;
import com.resto.brand.web.model.BrandUser;
import com.resto.brand.web.service.BrandUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;



/**
 *
 */
@Component
@Service
public class BrandUserServiceImpl extends GenericServiceImpl<BrandUser, String> implements BrandUserService {

    @Resource
    private BrandUserMapper branduserMapper;

    @Override
    public GenericDao<BrandUser, String> getDao() {
        return branduserMapper;
    }

	@Override
	public BrandUser selectByUsername(String username) {
		return branduserMapper.selectByUsername(username);
	}

	@Override
	public BrandUser authentication(BrandUser brandUser) {
		BrandUser user = branduserMapper.authentication(brandUser);
		//判断是否为空	如果不为空则修改登入时间
		if(user!=null){
			user.setLastLoginTime(new Date());
			update(user);
		}
		return user;
	}
	
	//添加 商家用户信息
	@Override
	public Result creatBrandUser(BrandUser brandUser) {
		Result result = checkBrandUserNameExits(brandUser);
		if(!result.isSuccess()){
			return result;
		}
		brandUser.setId(ApplicationUtils.randomUUID());
		brandUser.setCreateTime(new Date());
		brandUser.setPassword(ApplicationUtils.pwd(brandUser.getPassword()));
		int insert = branduserMapper.insert(brandUser);
		if(insert>0){
			 result.setSuccess(true);
		}else {
			result.setMessage("操作失败");
			result.setSuccess(true);
		}
		return result;
	}


	/**
	*@Description:修改商家用户
	*@Author:disvenk.dai
	*@Date:17:20 2018/7/10 0010
	*/
	@Override
	public Result modifyBrandUser(BrandUser brandUser) {
		JSONResult jsonResult = new JSONResult();
		BrandUser emialUser = selectByEmial(brandUser.getEmail());
		if(emialUser!=null && !emialUser.getId().equals(brandUser.getId())){
			jsonResult.setMessage("邮箱已被使用");
			jsonResult.setSuccess(false);
			return jsonResult;
		}
		jsonResult.setSuccess(true);
		return jsonResult;
	}

	//验证 商家用户名和邮箱是否已经存在
	private Result checkBrandUserNameExits(BrandUser brandUser) {
		JSONResult jsonResult = new JSONResult();
		if(brandUser.getUsername().indexOf("@") != -1){
			jsonResult.setMessage("用户名不可以存在@");
			jsonResult.setSuccess(false);
			return jsonResult;
		}
		BrandUser oldUser = selectByUsername(brandUser.getUsername());
//		BrandUser emialUser = selectByEmial(brandUser.getEmail());
		if(oldUser!=null){
			jsonResult.setMessage("用户名已存在");
			jsonResult.setSuccess(false);
			return jsonResult;
		}
//		else if(emialUser!=null){
//			jsonResult.setMessage("邮箱已被使用");
//			jsonResult.setSuccess(false);
//			return jsonResult;
//		}
		jsonResult.setSuccess(true);
		return jsonResult;
	}

	@Override
	public void updatePwd(String id, String password) {
		password = ApplicationUtils.pwd(password);
		branduserMapper.updatePwd(id, password);
	}

	public List<BrandUser> selectListBybrandId(String currentBrandId) {
		return branduserMapper.selectListBybrandId(currentBrandId);

	}

    @Override
    public BrandUser selectOneByBrandId(String brandId) {
        return branduserMapper.selectOneByBrandId(brandId);
    }



	@Override
	public BrandUser selectUserInfoByBrandIdAndRole(String BrandId, int roleId) {
		return branduserMapper.selectUserInfoByBrandIdAndRole(BrandId,roleId);
	}

    @Override
    public Void deleteBrandUser(String id) {
        //根据id查询品牌用户
        BrandUser brandUser = branduserMapper.selectByPrimaryKey(id);
        if(brandUser!=null){
            brandUser.setState(0);
            branduserMapper.updateByPrimaryKeySelective(brandUser);
        }
        return null;
    }

	@Override
	public BrandUser loginByWaitModel(String username, String password) {
		return branduserMapper.loginByWaitModel(username, password);
	}

	@Override
	public void updateSuperPwd(String id, String password) {
		password = ApplicationUtils.pwd(password);
		branduserMapper.updateSuperPwd(id, password);
	}

    @Override
    public List<BrandUser> selectByShopId(String shopId) {
        return branduserMapper.selectByShopId(shopId);
    }

	@Override
	public BrandUser selectByEmial(String email) {
		return branduserMapper.selectByEmail(email);
	}

	@Override
    public BrandUser selectByPhone(String phone) {
        return branduserMapper.selectByPhone(phone);
    }
}
