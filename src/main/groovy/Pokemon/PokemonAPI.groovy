package Pokemon

import Models.Pokemon
import Models.PokemonResponse
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
class PokemonAPI implements Service {
//    static final SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()

    private String baseUrl
    private HttpClient httpClient
    private ObjectMapper mapper

    @Inject
    PokemonAPI(HttpClient httpClient){
        this.httpClient = httpClient
        this.baseUrl = "https://pokeapi.co/api/v2"
        //https://www.baeldung.com/jackson-ignore-null-fields
        //JsonInclude Non_Null ignores values not mapped from the json body to the object using the object mapper
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    Promise<String> getApi(String path){
        def uri = HttpUrlBuilder //https://ratpack.io/manual/current/api/ratpack/http/HttpUrlBuilder.html
                .base(baseUrl.toURI())
                .path(path)
                .params("limit", "151")
                .build()

        httpClient.get(uri){ https: //ratpack.io/manual/current/api/ratpack/http/client/HttpClient.html
            it.headers {headers ->
                headers.add("Accept", "application/json")
            }
        }.map { response ->
            response.status.is2xx() ? response.body.text : response.status.code.toString()
        }
    }
    Promise<Pokemon> getPokemon(String path){

        getApi(path).map {
            //mapper will map matching values from the json response to the class fields
            //https://www.baeldung.com/jackson-object-mapper-tutorial
            mapper.readValue(it, PokemonResponse) //as PokemonResponse
        }.map{ PokemonResponse pr ->
            Pokemon.from(pr)
        }
    }

}