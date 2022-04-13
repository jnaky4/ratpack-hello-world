package Pokemon.Internal

import Cache.Internal.PokemonCache
import Models.Move
import Models.MoveResponse
import Models.Pokemon
import Models.PokemonResponse
import Models.ResultsResponse
import Pokemon.Pokedex
import com.fasterxml.jackson.core.JsonParser
import groovy.json.JsonSlurper
import ratpack.exec.Promise
import ratpack.http.HttpUrlBuilder
import ratpack.service.Service

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import ratpack.exec.Promise
import ratpack.http.client.HttpClient
import ratpack.service.Service
import ratpack.http.HttpUrlBuilder
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature

import com.google.inject.Singleton
import com.google.inject.Inject

@Singleton
class PokedexAPI implements Pokedex, Service{
    private String baseUrl
    private HttpClient httpClient
    private ObjectMapper mapper
    private PokemonCache cache

    @Inject
    PokedexAPI(HttpClient httpClient, PokemonCache cache){
        this.httpClient = httpClient
        this.baseUrl = "https://pokeapi.co/api/v2"
        this.cache = cache

        //https://www.baeldung.com/jackson-ignore-null-fields
        //JsonInclude Non_Null ignores values not mapped from the json body to the object using the object mapper
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    private Promise<String> getApi(String path = "", int limit = 151){
        //https://ratpack.io/manual/current/api/ratpack/http/HttpUrlBuilder.html
        def uri = HttpUrlBuilder
                .base(baseUrl.toURI())
                .path(path)
                .params("limit", "$limit")
                .build()
//        println uri
        httpClient.get(uri){
            it.headers {headers ->
                headers.add("Accept", "application/json")
            }
        }.map { response ->
//            println "response" + response.statusCode
//            println response.body.text
            response.status.is2xx() ? response.body.text : response.status.code.toString()
        }
    }

    @Override
    Promise<Pokemon> getPokemon(String pokeID){
//        println "GETTING A POKEMON $pokeID_orName"
        getApi("pokemon/$pokeID").map {
            //mapper will map matching values from the json response to the class fields
            //https://www.baeldung.com/jackson-object-mapper-tutorial
            mapper.readValue(it, PokemonResponse)
        }.map{ PokemonResponse pr ->
            Pokemon.from(pr)
        }
    }

    Promise<Move> getMove(String moveID){
        getApi("move/$moveID").map{
            mapper.readValue(it, MoveResponse)
        }.map{ MoveResponse mr ->
            Move.from(mr)
        }
    }

    Promise<Pokemon> fetchPokemon(String pokeID){
        //https://ratpack.io/manual/current/api/ratpack/exec/Promise.html#sync(ratpack.func.Factory)
        Promise.sync{ // makes a promise from a non async method
            cache.getFromCache(pokeID as Integer)
        }.onError{ e ->
            println "ERROR $e.stackTrace"
        }.route({ p ->
            p != null
        }, { p ->
            p
        }).flatMap{ //if getFromCache is null, fetch the pokemon
            getPokemon(pokeID).map{p ->
                cache.addToCache(p.id, p) //add the pokemon to the cache and return the pokemon
                p
            }
        }
    }

    Promise<Map<String,String>> getAvailableResources() {
        getApi( "",151).map { response ->
            mapper.readValue(response, Map)
        }.map { Map<String, String> it ->
            it.collectEntries { key, value ->
                switch(key){
                case("pokemon"):
                case("move"):
                case("evolution-chain"):
                case("nature"):
                case("pokedex"):
                case("stat"):
                case("type"):
                case("species"):
                    [(key): value.replace(baseUrl, "http://localhost:5050")]
                        break
                    default: //remove all the ones we don't want from full api
                        [:]
                        //[(key): value]  //in case you want to see all the other api options
                        break
                }
            } as Map<String, String>
        }
    }

    @Override
    Promise<ResultsResponse> getResources(String resource){
//        println "get a Resource $resource"
        getApi(resource, 10).map { response -> //todo implement with config
            def rr = mapper.readValue(response, ResultsResponse)
            //todo replace next, previous with localhost
            rr.collect{
                it.previous = it.previous?.replace(baseUrl, "http://localhost:5050") //safe navigation operator
                it.next = it.next?.replace(baseUrl, "http://localhost:5050")
            }
            rr.results.collect{ it ->
                it["url"] = it["url"]?.replace(baseUrl, "http://localhost:5050")
            }
            rr
        }
    }
}
