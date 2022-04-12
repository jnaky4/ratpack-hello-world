package Service


import Cache.Internal.PokemonCache
import Models.Pokemon
import Pokemon.Internal.PokedexAPI

import ratpack.exec.ExecResult
import ratpack.exec.Execution
import ratpack.exec.Promise
import ratpack.exec.util.ParallelBatch
import ratpack.http.client.HttpClient
import ratpack.service.Service
import ratpack.service.StartEvent

import java.util.concurrent.TimeUnit

import com.google.inject.Inject
import com.google.inject.Singleton

@Singleton
class PokemonCachingService implements Service{
    private HttpClient httpClient
    private PokemonCache cache
    private long updateInterval
    private PokedexAPI api

    @Inject
    PokemonCachingService(HttpClient httpClient, PokemonCache cache, PokedexAPI api){
        this.httpClient = httpClient
        this.updateInterval = TimeUnit.DAYS.toMillis(1)
        this.cache = cache
        this.api = api
    }

    @Override
    void onStart(StartEvent event) throws Exception{
        scheduleCacheUpdate()
    }

    private scheduleCacheUpdate(){
        Execution.fork().onComplete{
            Execution.current().controller.executor.schedule(this.&scheduleCacheUpdate, this.updateInterval, TimeUnit.MILLISECONDS)
        }.onError { e ->
            println "Failed to schedule cache update $e.stackTrace"
        }.onComplete{
            println "Caching Succeeded"
        }.start{
            println "Starting Caching"
            cachePokemon().then{
                it.each{
                    cache.addToCache(it.key, it.value)
                }
            }
        }
    }

    private Promise<Map<Integer, Pokemon>> cachePokemon(){
        def promises = []
        for(i in 1..2){ // current number of pokemon 897
            promises.add(api.getPokemon("$i").map{ Pokemon p -> p })
        }
        ParallelBatch.of(promises).yieldAll().map{
            def t = it.collectEntries { ExecResult exRes ->
                if (exRes.isSuccess()) {
                    def pokemon = exRes.value as Pokemon
                    [(pokemon.id): pokemon]
                } else {
                    println "Failed exec result $exRes.error"
                    [:]
                }
            } as Map<String, Pokemon>
        }
    }
}
