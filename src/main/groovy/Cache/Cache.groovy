package Cache

import java.util.concurrent.ConcurrentHashMap

interface Cache<T, A>{
    boolean addToCache(T, A);
    boolean removeFromCache(T);
    A getFromCache(T);
    ConcurrentHashMap<T, A> getAllCache();
}