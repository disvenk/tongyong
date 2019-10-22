package com.resto.shop.web.baseScm;




import com.resto.shop.web.baseScm.domain.BaseQuery;
import com.resto.shop.web.baseScm.domain.UpdateByQuery;
import com.resto.shop.web.baseScm.exception.DataAccessException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhaojingyang on 2015/8/15.
 */
public interface BaseDao<DO extends Serializable> {

    /**
     * 添加实体
     *
     * @param entity 要添加的实体对象
     * @return
     */
     int insert(DO entity) throws DataAccessException;


    /**
     * 新建对象或更新对象
     *
     * @param entity 要保存的实体对象
     * @return
     */
     int insertOrUpdate(DO entity) throws DataAccessException;


    /**
     * 保存实体
     *
     * @param entity 要保存的实体对象
     * @return
     */
     int update(DO entity) throws DataAccessException;


    /**
     * 更新对象
     *
     * @return
     * @throws
     */
     int updateByQuery(UpdateByQuery<DO> uDO) throws DataAccessException;


    /**
     * 根据主键删除实体
     *
     * @param pk 主键
     * @return
     */
     int deleteById(Long pk) throws DataAccessException;

    /**
     * 根据主键删除实体 物理删除
     *
     * @param pk 主键
     * @return
     */

     int deletePhysicalById(Long pk) throws DataAccessException;

    /**
     * 根据主键删除实体
     *
     * @param query 参数
     * @return
     */
     int delete(BaseQuery<DO> query) throws DataAccessException;

    /**
     * 根据主键返回指定的实体对象
     *
     * @param pk 主键
     * @return
     */
     DO getById(Long pk) throws DataAccessException;

    /**
     * 根据主键返回指定的实体对象, 会锁表
     *
     * @param pk
     * @return
     * @throws DataAccessException
     */
     DO getByIdForUpdate(Long pk) throws DataAccessException;

    /**
     * 根据sql mapping中定义的sql名称，以及传入的参数来查找实体集
     *
     * @param query 参数
     * @return
     */
     List<DO> query(BaseQuery<DO> query) throws DataAccessException;

    /**
     * 分页查询
     * 根据sql mapping中定义的sql名称，以及传入的参数来查找实体集
     *
     * @param query 参数
     * @return
     */
     List<DO> query4Page(BaseQuery<DO> query) throws DataAccessException;

    /**
     * 根据sql mapping中定义的sql名称，以及传入的参数来查找统计集
     *
     * @param query 参数
     * @return
     */
     long count(BaseQuery<DO> query) throws DataAccessException;

}
