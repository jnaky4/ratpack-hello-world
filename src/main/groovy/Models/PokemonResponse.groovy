package Models

class PokemonResponse {
    String name
    int id
    int base_experience
    int height
    int weight
    List<MoveResponse> moves
}

class MoveResponse{
    MoveNameResponse move
}

class MoveNameResponse{
    String name
    String url
}
