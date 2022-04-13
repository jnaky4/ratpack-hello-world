package Cache.Internal

import Cache.InMemoryCache
import Models.Move

import com.google.inject.Singleton

import java.util.concurrent.ConcurrentHashMap;

@Singleton
class MoveCache extends InMemoryCache{
    boolean addToCache(Integer key, Move value) {
        return super.addToCache(key, value)
    }
    Move getFromCache(String key) {
        return super.getFromCache(key) as Move
    }
}
