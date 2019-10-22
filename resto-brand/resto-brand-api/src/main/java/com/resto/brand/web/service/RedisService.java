package com.resto.brand.web.service;

public interface RedisService {

    /**
     * 插入缓存
     * @param key
     * @param value
     * @return
     */
    boolean set( String key, Object value);

    /**
     * 插入缓存(设置存活时间)
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    boolean set(String key, Object value, Long expireTime);

    /**
     * 删除缓存
     * @param key
     * @return
     */
    boolean remove(String key);

    /**
     * 查询缓存
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 刷新缓存
     * @param key
     */
    void removePattern(String key);

    /**
     * 删除店铺
     * @param shopId
     */
    void cleanShopDetail(String shopId);

    /**
     * 根据品牌id和品牌标志删除
     * @param brandId
     * @param brandSign
     */
    void clean(String brandId, String brandSign);

    /**
     * 加锁（时间）
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    boolean setnxTime(String key, Object value,int expireTime);

    /**
     * 加锁
     * @param key
     * @param value
     * @return
     */
    boolean setnx(String key, Object value);
}
