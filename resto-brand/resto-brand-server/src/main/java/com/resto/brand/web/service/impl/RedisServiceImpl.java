package com.resto.brand.web.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.resto.brand.core.util.SerializeUtil;
import com.resto.brand.web.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Component
@Service
public class RedisServiceImpl implements RedisService {

    private static JedisPool jedisPool;//注入JedisPool

    public void setJedisPool(JedisPool jedisPool) {
        RedisServiceImpl.jedisPool = jedisPool;
    }


    /**
     * 插入缓存
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean set(String key, Object value) {
        return set(key, value, 3600 * 24L);
    }

    /**
     * 插入缓存(设置存活时间)
     *
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    @Override
    public boolean set(String key, Object value, Long expireTime) {
        if (StringUtils.isNotBlank(key)) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                boolean exists = exists(key);
                String result;
                if (exists) {
                    result = jedis.set(key.getBytes(), SerializeUtil.serialize(value), "XX".getBytes(), "EX".getBytes(), expireTime);
                } else {
                    result = jedis.set(key.getBytes(), SerializeUtil.serialize(value), "NX".getBytes(), "EX".getBytes(), expireTime);
                }
                if (StringUtils.equals(result, "OK")) {
                    return true;
                }

            } catch (Exception e) {
                jedisPool.returnBrokenResource(jedis);
                e.printStackTrace();
                return false;
            } finally {
                if (null != jedisPool) {
                    jedisPool.returnResource(jedis);
                }
            }
        }
        return false;
    }

    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    @Override
    public boolean remove(String key) {
        if (StringUtils.isNotBlank(key)) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                Long del = jedis.del(key);
                if (del == 1) {
                    return true;
                }
            } catch (Exception e) {
                jedisPool.returnBrokenResource(jedis);
                e.printStackTrace();
                return false;
            } finally {
                if (null != jedisPool) {
                    jedisPool.returnResource(jedis);
                }
            }
        }
        return false;
    }

    /**
     * 查询缓存
     *
     * @param key
     * @return
     */
    @Override
    public Object get(String key) {
        Object value = null;
        if (StringUtils.isNotBlank(key)) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                byte[] bytes = jedis.get(key.getBytes());
                value = SerializeUtil.unserialize(bytes);
            } catch (Exception e) {
                jedisPool.returnBrokenResource(jedis);
                e.printStackTrace();
            } finally {
                if (null != jedisPool) {
                    jedisPool.returnResource(jedis);
                }
            }
        }
        return value;

    }

    /**
     * 刷新缓存
     *
     * @param key
     */
    @Override
    public void removePattern(String key) {
        if (StringUtils.isNotBlank(key)) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                Set<String> keys = jedis.keys(key);
                for (String k : keys) {
                    jedis.del(k);
                }
            } catch (Exception e) {
                jedisPool.returnBrokenResource(jedis);
                e.printStackTrace();
            } finally {
                if (null != jedisPool) {
                    jedisPool.returnResource(jedis);
                }
            }
        }
    }

    @Override
    public void cleanShopDetail(String shopId) {
        remove(shopId + "info");
    }

    @Override
    public void clean(String brandId, String brandSign) {
        remove(brandId);
        remove(brandId + "shopList");
        remove(brandId + "setting");
    }

    @Override
    public boolean setnxTime(String key, Object value, int expireTime) {
        boolean setnx = false;
        if (StringUtils.isNotBlank(key)) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                Long result = jedis.setnx(key, String.valueOf(value));
                if(result == 1){
                    setnx = true;
                }
                if (expireTime > 0 && setnx) {
                    jedis.expire(key, expireTime);
                }
            } catch (Exception e) {
                jedisPool.returnBrokenResource(jedis);
                e.printStackTrace();
            } finally {
                if (null != jedisPool) {
                    jedisPool.returnResource(jedis);
                }
            }
        }
        return setnx;
    }

    @Override
    public boolean setnx(String key, Object value) {
        return setnxTime(key, value, 0);
    }


    /**
     * 判断缓存中是否有对应的key
     *
     * @param key
     * @return
     */
    private boolean exists(String key) {
        boolean exists = false;
        if (StringUtils.isNotBlank(key)) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                exists = jedis.exists(key);
            } catch (Exception e) {
                jedisPool.returnBrokenResource(jedis);
                e.printStackTrace();
            } finally {
                if (null != jedisPool) {
                    jedisPool.returnResource(jedis);
                }
            }
        }
        return exists;
    }

}
