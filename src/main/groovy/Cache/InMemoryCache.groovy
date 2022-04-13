package Cache

import Cache.Cache
import Models.Pokemon
import com.google.inject.Singleton
import com.google.inject.Inject

import java.util.concurrent.ConcurrentHashMap

abstract class InMemoryCache<T,A>{
    ConcurrentHashMap<T, A> cache = new ConcurrentHashMap<>()

    boolean addToCache(T key, A value){
        println "Adding $key"
        cache.put(key, value)
        return true
    }

    boolean removeFromCache(T key){
        def c = cache.remove(key)
        return c != null
    }

    Object getFromCache(T key){
        return cache[key] ?: null
    }

    ConcurrentHashMap getAllCache(){
        return cache
    }
}
