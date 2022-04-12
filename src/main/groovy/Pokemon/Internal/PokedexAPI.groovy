package Pokemon.Internal

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

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokedexAPI implements Pokedex, Service{

    private String baseUrl
    private HttpClient httpClient
    private ObjectMapper mapper

    @Inject
    PokedexAPI(HttpClient httpClient){
        this.httpClient = httpClient
        this.baseUrl = "https://pokeapi.co/api/v2"
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
    Promise<Pokemon> getPokemon(String pokeID_orName){
//        println "GETTING A POKEMON $pokeID_orName"
        getApi("pokemon/$pokeID_orName").map {
            //mapper will map matching values from the json response to the class fields
            //https://www.baeldung.com/jackson-object-mapper-tutorial
            mapper.readValue(it, PokemonResponse)
        }.map{ PokemonResponse pr ->
            Pokemon.from(pr)
        }
    }

    Promise<Map<String,String>> getResources() {
        getApi( "",2000).map { response ->
            mapper.readValue(response, Map)
        }.map { Map<String, String> it ->
            it.collectEntries { key, value ->
                [(key): value.replace(baseUrl, "http://localhost:5050")]
            } as Map<String, String>
        }
    }

    @Override
    Promise<ResultsResponse> getResources(String resource){
//        println "get a Resource $resource"
//        def response = getApi(resource, 2000)
//        new JsonSlurper().parseText(response)
        getApi(resource, 2000).map { response ->
            def rr = mapper.readValue(response, ResultsResponse)
            rr.results.collect{ it ->
                it["url"] = it["url"].replace(baseUrl, "http://localhost:5050")
            }
            rr
        }


    }
}
