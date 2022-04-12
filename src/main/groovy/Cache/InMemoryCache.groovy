package Cache

import Cache.Cache
import Models.Pokemon
import com.google.inject.Singleton
import com.google.inject.Inject

import java.util.concurrent.ConcurrentHashMap

@Singleton
abstract class InMemoryCache<T,A>{
    ConcurrentHashMap<T, A> cache = new ConcurrentHashMap<>()

    boolean addToCache(T key, A value){
        cache[key] = value
        return true
    }

    boolean removeFromCache(T key){
        def c = cache.remove(key)
        return c != null
    }

    Object getFromCache(T key){
        return cache[key] ?: [] as Object
    }

    ConcurrentHashMap getAllCache(){
        return cache
    }
}
