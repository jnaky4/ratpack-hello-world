package Cache

import Cache.Cache
import Models.Pokemon
import com.google.inject.Singleton
import com.google.inject.Inject

import java.util.concurrent.ConcurrentHashMap

@Singleton
abstract class InMemoryCache<T,A>{
    ConcurrentHashMap<T, A> cache = new ConcurrentHashMap<>()

//    @Inject
//    InMemoryCache(){}

    boolean addToCache(T key, A value){
        println "Adding $key"
        cache.put(key, value)
        println "Done"
        return true
    }

    boolean removeFromCache(T key){
        def c = cache.remove(key)
        return c != null
    }

    Object getFromCache(T key){
        println key
        def item = cache[key]
        println item
        return item
//        return cache[key] ?: [] as Object
    }

    ConcurrentHashMap getAllCache(){
        return cache
    }
}
