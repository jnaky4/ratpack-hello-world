package Pokemon

import Models.Pokemon
import ratpack.exec.Promise

interface Pokedex {
    Promise<String> getResources(String resource)
    Promise<Pokemon> getPokemon(String path)
}