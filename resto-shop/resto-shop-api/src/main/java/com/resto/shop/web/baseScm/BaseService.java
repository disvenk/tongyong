package com.resto.shop.web.baseScm;




import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.PageResult;
import com.resto.shop.web.baseScm.exception.DataAccessException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhaojingyang on 2015/8/15.
 */
public interface BaseService<T extends Serializable> {

    /**
     * 新建对象
     *
     * @param t 对象
     * @return z主键id
     * @throws
     */
     int insert(T t) throws DataAccessException;

    /**
     * 新建对象或更新对象
     *
     * @param t
     * @return
     */
     int insertOrUpdate(T t) throws DataAccessException;

    /**
     * 更新对象
     *
     * @param t 对象
     * @return
     * @throws
     */
     int update(T t) throws DataAccessException;

    /**
     * ID查找对象
     *
     * @param id 对象ID
     * @return
     * @throws
     */
     T getById(Long id) throws DataAccessException;

    /**
     * ID查找对象，会行锁
     *
     * @param id 对象ID
     * @return
     * @throws
     */
     T getByIdForUpdate(Long id) throws DataAccessException;

    /**
     * 按条件查找
     *
     * @param query
     * @return
     */
     List<T> query(BaseQuery<T> query) throws DataAccessException;

    /**
     * 分页查找
     *
     * @param query
     * @return
     * @throws DataAccessException
     */
     PageResult<T> query4Page(BaseQuery<T> query) throws DataAccessException;

    /**
     * ID删除对象
     *
     * @param id
     * @return
     * @throws
     */
     int deleteById(Long id) throws DataAccessException;
    /**
     * ID删除对象 物理删除
     *
     * @param id
     * @return
     * @throws
     */
     int deletePhysicalById(Long id) throws DataAccessException;


    /**
     * 删除对象
     *
     * @param query
     * @return
     * @throws
     */
     int delete(BaseQuery<T> query) throws DataAccessException;

    /**
     * 按条件计数
     *
     * @param query
     * @return
     * @throws DataAccessException
     */
     long count(BaseQuery<T> query) throws DataAccessException;

}
