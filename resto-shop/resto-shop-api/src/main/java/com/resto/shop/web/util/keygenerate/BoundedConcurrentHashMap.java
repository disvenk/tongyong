package com.resto.shop.web.util.keygenerate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedConcurrentHashMap<K, V> extends LinkedHashMap<K, V> {

    private static final Log logger = LogFactory.getLog(BoundedConcurrentHashMap.class);
    private static final long serialVersionUID = 2615986629983154259L;

    private static final int DEFAULT_CAPACITY = 389;

    protected volatile int capacity;

    private final Lock lock = new ReentrantLock();
//	private final Lock readLock = lock.readLock();
//	private final Lock writeLock = lock.writeLock();

    public BoundedConcurrentHashMap(int capacity) {
        super(capacity * 4  / 3 + 1, 0.75f, true);
        this.capacity = capacity;
    }

    public BoundedConcurrentHashMap() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {;
        boolean ret = size() > capacity;
        if(logger.isDebugEnabled()) {
            if(ret) {
                logger.debug("removeEldestEntry size: "+size()+", capacity: "+capacity);
            }
        }
        return ret;
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            super.clear();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Object clone() {
        lock.lock();
        try {
            return super.clone();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        lock.lock();
        try {
            return super.equals(o);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public int hashCode() {
        lock.lock();
        try {
            return super.hashCode();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            return super.toString();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        lock.lock();
        try {
            return super.containsValue(value);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        lock.lock();
        try {
            return super.get(key);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        lock.lock();
        try {
            return super.containsKey(key);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        lock.lock();
        try {
            return super.isEmpty();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        //throw new UnsupportedOperationException();
        return new HashSet<K>(super.keySet());
    }

    @Override
    public V put(K key, V value) {
        lock.lock();
        try {
            return super.put(key, value);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        lock.lock();
        try {
            super.putAll(m);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        lock.lock();
        try {
            return super.remove(key);
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return super.size();
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }
}
