package Models

import Enums.Type

class PokemonResponse {
    String name
    int id
    int base_experience
    int height
    int weight
    List<PokemonMovesResponse> moves
    List<PokemonStatsResponse> stats
    List<TypesResponse> types
}

class PokemonMovesResponse {
    MoveNameResponse move
}

class MoveNameResponse{
    String name
    String url
}
class PokemonStatsResponse{
    int base_stat
    StatResponse stat
}
class StatResponse{
    String name
    String url
}
class TypesResponse{
    TypeResponse type
}
class TypeResponse{
    String name
    String url
}

class Pokemon {
    int id
    String name
    List<String> types
    Map<String,Integer> stats
    int base_experience
    int height
    int weight
    List<String> moves

    static Pokemon from(PokemonResponse pr) {
        return new Pokemon(
                name: pr.name,
                id: pr.id,
                base_experience: pr.base_experience,
                height: pr.height,
                weight: pr.weight,
                moves: pr.moves.collect { //use collect to create a list from response
                    it.move.name
                },
                types: pr.types.collect {
                    it.type.name
                },
                //Use CollectEntries to create a map from a response
                stats: pr.stats.collectEntries{it ->
                    [(it.stat.name), it.base_stat]
                } as Map<String, Integer>,

        )
    }
}
