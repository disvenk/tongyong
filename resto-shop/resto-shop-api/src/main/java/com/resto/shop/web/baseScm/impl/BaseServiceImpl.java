package com.resto.shop.web.baseScm.impl;



import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.BaseService;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.PageResult;
import com.resto.shop.web.baseScm.exception.DataAccessException;

import java.io.Serializable;
import java.util.List;

/**
 * User: <a href="mailto:lenolix@163.com">李星</a>
 * Version: 1.0.0
 * Since: 14/11/6 上午12:31
 */
public abstract class BaseServiceImpl<DO extends Serializable> implements BaseService<DO> {

    public abstract BaseDao<DO> getDao();

    @Override
    public int insert(DO aDo) throws DataAccessException {
        return this.getDao().insert(aDo);
    }

    @Override
    public int insertOrUpdate(DO aDo) throws DataAccessException {
        return this.getDao().insertOrUpdate(aDo);
    }

    @Override
    public int update(DO aDo) throws DataAccessException {
        return this.getDao().update(aDo);
    }

    @Override
    public DO getById(Long id) throws DataAccessException {
        return this.getDao().getById(id);
    }

    @Override
    public DO getByIdForUpdate(Long id) throws DataAccessException {
        return this.getDao().getByIdForUpdate(id);
    }

    @Override
    public List<DO> query(BaseQuery<DO> query) throws DataAccessException {
        return this.getDao().query(query);
    }

    @Override
    public PageResult<DO> query4Page(BaseQuery<DO> query) throws DataAccessException {
        PageResult<DO> pResult = new PageResult<DO>();
        pResult.setCurrentPage(query.getCurrentPage());
        pResult.setPageSize(query.getPageSize());
        long count = this.count(query);
        List<DO> result = this.getDao().query4Page(query);
        pResult.setTotalItem((int) count);
        pResult.setResult(result);
        return pResult;
    }

    @Override
    public int deleteById(Long id) throws DataAccessException {
        return this.getDao().deleteById(id);
    }

    @Override
    public int deletePhysicalById(Long id) throws DataAccessException {
        return this.getDao().deletePhysicalById(id);
    }


    @Override
    public int delete(BaseQuery<DO> query) throws DataAccessException {
        return this.getDao().delete(query);
    }

    @Override
    public long count(BaseQuery<DO> query) throws DataAccessException {
        return this.getDao().count(query);
    }


}

