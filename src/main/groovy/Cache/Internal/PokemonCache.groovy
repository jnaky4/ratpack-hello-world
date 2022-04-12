package Cache.Internal

import Cache.InMemoryCache
import Models.Pokemon;

import java.util.concurrent.ConcurrentHashMap;

class PokemonCache extends InMemoryCache{
//    ConcurrentHashMap<Integer, Pokemon> cache = new ConcurrentHashMap<>()

    boolean addToCache(Integer key , Pokemon value) {
        return super.addToCache(key, value)
    }

    boolean removeFromCache(Integer key) {
        return super.removeFromCache(key)
    }

    Object getFromCache(String key) {
        return super.getFromCache(key)
    }

    ConcurrentHashMap getAllCache() {
        return super.getAllCache()
    }

//
//    @Override
//    ConcurrentHashMap getAllCache() {
//        return cache
//    }
//
//    @Override
//    boolean removeFromCache(Integer key) {
//        def pokemon = cache.remove(key)
//        return pokemon != null
//    }
//
//    @Override
//    Pokemon getFromCache(Integer key) {
//        return cache[key] ?: [] as Pokemon
//    }
}