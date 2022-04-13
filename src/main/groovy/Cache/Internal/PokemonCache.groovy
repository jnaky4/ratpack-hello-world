package Cache.Internal

import Cache.InMemoryCache
import Models.Pokemon

import com.google.inject.Singleton
import com.google.inject.Inject

import java.util.concurrent.ConcurrentHashMap;

@Singleton
class PokemonCache{
    ConcurrentHashMap<Integer, Pokemon> cache = new ConcurrentHashMap<>()

    boolean addToCache(Integer key, Pokemon value) {
        cache.put(key, value)
        return true
    }

    boolean removeFromCache(Integer key) {
        def c = cache.remove(key)
        return c != null
    }

    Pokemon getFromCache(Integer key) {
        return cache[key] ?: null
    }

    ConcurrentHashMap getAllCache() {
        return cache
    }

//class PokemonCache extends InMemoryCache{
//    ConcurrentHashMap<Integer, Pokemon> cache = new ConcurrentHashMap<>()

//    boolean addToCache(Integer key, Pokemon value) {
////        println key
////        println "${value.id}  ${value.name} ${value.moves} "
//        return super.addToCache(key, value)
//    }
//
//    boolean removeFromCache(Integer key) {
//        return super.removeFromCache(key)
//    }
//
//    Pokemon getFromCache(String key) {
//        return super.getFromCache(key) as Pokemon
//    }
//
//    ConcurrentHashMap getAllCache() {
//        return super.getAllCache()
//    }
}