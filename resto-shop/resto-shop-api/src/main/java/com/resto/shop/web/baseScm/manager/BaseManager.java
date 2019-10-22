package com.resto.shop.web.baseScm.manager;




import com.resto.shop.web.baseScm.BaseDao;
import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.PageResult;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.baseScm.exception.DataAccessException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhaojingyang on 2015/8/15.
 */
public abstract class BaseManager<DO extends Serializable> {

    public abstract BaseDao<DO> getDao();

    public int insert(DO aDo) throws DataAccessException {
        return  this.getDao().insert(aDo);
    }

    public int insertOrUpdate(DO aDo) throws DataAccessException {
        return this.getDao().insertOrUpdate(aDo);
    }

    public int update(DO aDo) throws DataAccessException {
        return this.getDao().update(aDo);
    }

    public int updateByQuery(UpdateByQuery<DO> uDO) throws DataAccessException {
        return this.getDao().updateByQuery(uDO);
    }

    public DO getById(Long id) throws DataAccessException {
        return this.getDao().getById(id);
    }

    public DO getByIdForUpdate(Long id) throws DataAccessException {
        return this.getDao().getByIdForUpdate(id);
    }

    public List<DO> query(BaseQuery<DO> query) throws DataAccessException {
        return this.getDao().query(query);
    }

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

    public int deleteById(Long id) throws DataAccessException {
        return this.getDao().deleteById(id);
    }

    public int deletePhysicalById(Long id) throws DataAccessException {
        return this.getDao().deletePhysicalById(id);
    }

    public int delete(BaseQuery<DO> query) throws DataAccessException {
        return this.getDao().delete(query);
    }

    public long count(BaseQuery<DO> query) throws DataAccessException {
        return this.getDao().count(query);
    }


}

