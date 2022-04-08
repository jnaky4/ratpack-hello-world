package Models

import Enums.Type

//response model
class Pokemon {
    String name
    int id
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
                moves: pr.moves.collect {
                    it.move.name
                }
        )
    }
}
