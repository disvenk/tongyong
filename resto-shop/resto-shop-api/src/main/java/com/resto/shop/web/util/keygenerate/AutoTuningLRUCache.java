package com.resto.shop.web.util.keygenerate;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 自调节LRU缓存；保证目标命中率的前提下，最大程度节省内存；
 * 
 *
 *
 * @param <K>
 * @param <V>
 */
public class AutoTuningLRUCache<K, V> extends BoundedConcurrentHashMap<K, V> {
    //缓存一般在初始化时设定好的，所以instances不考虑线程安全
    public static final List<AutoTuningLRUCache<?, ?>> instances = new LinkedList<AutoTuningLRUCache<?, ?>>();

    public final String name;
    private final AtomicLong hitcount = new AtomicLong(1); //防止除0
    private final AtomicLong misscount = new AtomicLong(0);
    private int maxCacheSize = super.capacity;
    private int minCacheSize = super.capacity / 4;
    private int targetHitRate = 10000; //目标命中率，百分数乘以1万后的值，默认100%
    private int targetHitRateDvalue = 2; //当前命中率与目标命中率差距多少时才做调整
    private volatile long autoTuningInterval = 5 * 1000; //调节时间间隔
    private long minTuningInterval = 5 * 1000; //最小调节时间间隔
    private static long maxTuningInterval = 10 * 60 * 1000; //最大调节时间间隔10分钟
    private volatile long lastTuningTime = System.currentTimeMillis();


    public AutoTuningLRUCache(String name) {
        super();
        this.name = name;
        instances.add(this);
    }


    public AutoTuningLRUCache(String name, int capacity) {
        super(capacity);
        this.name = name;
        instances.add(this);
    }


    public AutoTuningLRUCache(String name, int maxCacheSize, int minCacheSize) {
        super(maxCacheSize);
        if (minCacheSize > maxCacheSize) {
            throw new IllegalArgumentException("minCacheSize:" + minCacheSize + ",maxCacheSize" + maxCacheSize);
        }
        this.name = name;
        this.minCacheSize = minCacheSize;
        instances.add(this);
    }


    @Override
    public V get(Object key) {
        V value = super.get(key);
        if (value != null) {
            hitcount.incrementAndGet();
        }
        else {
            misscount.incrementAndGet();
        }
        tuning();
        return value;
    }


    @Override
    public V put(K key, V value) {
        tuning();
        return super.put(key, value);
    }


    private void tuning() {
        long now = System.currentTimeMillis();
        if (now - lastTuningTime < this.autoTuningInterval) {
            return;
        }
        lastTuningTime = now;
        boolean tuned = false; //expand = false, shrink = false;
        long dvalue = getHitRate() - targetHitRate;
        if (dvalue > targetHitRateDvalue) {
            if (this.capacity > this.minCacheSize) {
                this.capacity--;
                super.put(null, null);
                super.remove(null);
                tuned = true;
            }
        }
        else if (dvalue < -targetHitRateDvalue) {
            if (this.capacity < this.maxCacheSize) {
                this.capacity++;
                tuned = true;
            }
        }
        if (tuned) {
            long newInterval = this.autoTuningInterval / 2;
            this.autoTuningInterval = newInterval > this.minTuningInterval ? newInterval : this.minTuningInterval;
        }
        else {
            long newInterval = this.autoTuningInterval * 2;
            this.autoTuningInterval = newInterval < maxTuningInterval ? newInterval : maxTuningInterval;
        }
    }


    public void resetHitCount() {
        lastTuningTime = System.currentTimeMillis();
        this.misscount.set(0);
    }


    public String report() {
        return new StringBuilder("MaxCacheSize:").append(maxCacheSize).append(",MinCacheSize:").append(minCacheSize)
            .append(",CurrentCacheSize:").append(this.capacity).append(",CurrentDataSize:").append(this.size())
            .append(",TargetHitRate:").append(targetHitRate).append(",CurrentHitRate:").append(getHitRate())
            .append(",TargetHitRateDvalue:").append(targetHitRateDvalue).append(",autoTuningInterval:")
            .append(autoTuningInterval).append(",minTuningInterval:").append(minTuningInterval).toString();
    }


    /**
     * 命中率为98.76%则返回9876； 命中率为95.4%则返回9540
     */
    public long getHitRate() {
        return hitcount.longValue() * 10000 / (hitcount.longValue() + misscount.longValue());
    }


    public void setMaxCacheSize(int maxCacheSize) {
        if (minCacheSize > maxCacheSize) {
            throw new IllegalArgumentException("minCacheSize:" + minCacheSize + ",maxCacheSize" + maxCacheSize);
        }
        this.maxCacheSize = maxCacheSize;
    }


    public void setMinCacheSize(int minCacheSize) {
        if (minCacheSize > maxCacheSize) {
            throw new IllegalArgumentException("minCacheSize:" + minCacheSize + ",maxCacheSize" + maxCacheSize);
        }
        this.minCacheSize = minCacheSize;
    }


    public int getMaxCacheSize() {
        return this.maxCacheSize;
    }


    public int getMinCacheSize() {
        return minCacheSize;
    }


    public int getCurrentCacheSize() {
        return this.capacity;
    }


    public int getTargetHitRate() {
        return targetHitRate;
    }


    public void setTargetHitRate(int targetHitRate) {
        this.targetHitRate = targetHitRate;
    }

    private static final long serialVersionUID = 1L;


    public long getAutoTuningInterval() {
        return autoTuningInterval;
    }


    public void setAutoTuningInterval(long autoTuningInterval) {
        this.autoTuningInterval = autoTuningInterval;
        this.minTuningInterval = autoTuningInterval;
    }


    public int getTargetHitRateDvalue() {
        return targetHitRateDvalue;
    }


    public void setTargetHitRateDvalue(int targetHitRateDvalue) {
        this.targetHitRateDvalue = targetHitRateDvalue;
    }

}
