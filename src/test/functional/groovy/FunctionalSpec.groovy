import jdk.nashorn.internal.runtime.SharedPropertyMap
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.groovy.test.embed.GroovyEmbeddedApp
import ratpack.impose.ImpositionsSpec
import ratpack.impose.ServerConfigImposition
import ratpack.test.http.TestHttpClient

import static ratpack.jackson.Jackson.json
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class FunctionalSpec extends Specification{

    @AutoCleanup
    @Shared
    GroovyRatpackMainApplicationUnderTest aut

    @Shared
    TestHttpClient autHttpClient

    @AutoCleanup
    @Shared
    GroovyEmbeddedApp mockAPI

    void "test api"(){
        when:
        def t = 2

        then:
        assert 1 < t
    }

    void setup(){
        mockAPI = GroovyEmbeddedApp.ratpack {
            handlers{
                get("api/hello"){
                    render(json(["Hello"]))
                }
            }
        }

        aut = new GroovyRatpackMainApplicationUnderTest(){
//            void addImpositions(ImpositionsSpec){
//                spec.add(ServerConfigImposition.of { s ->
//                    s.object("config", config)
//                })
//            }
        }
        autHttpClient = aut.httpClient.requestSpec{ spec ->
            spec.headers { h ->
                h.add("Content Type", "application/json")
            }

        }
    }
    void cleanup(){
        aut.stop()
    }

    static boolean waitFor(Closure waitClosure, Closure failClosure){
        def timeout = 20000
        def timeoutTime = System.currentTimeMillis() + timeout
        while (System.currentTimeMillis() < timeoutTime){
            if (waitClosure()){
                return true
            }
            sleep(250)
        }
        println "Timeout Failure: ${(timeout - (timeoutTime - System.currentTimeMillis()))}"
        failClosure()
        return false
    }
}