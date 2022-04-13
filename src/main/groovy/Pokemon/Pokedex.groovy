package Pokemon

import Models.Move
import Models.Pokemon
import ratpack.exec.Promise

interface Pokedex {
    Promise<String> getResources(String resource)
    Promise<Pokemon> getPokemon(String path)
    Promise<Move> getMove(String path)

    Promise<Pokemon> fetchPokemon(String path)
    Promise<Pokemon> getAvailableResources()
 }