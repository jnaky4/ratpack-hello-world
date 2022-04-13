package Models

import Enums.Status
import Enums.Type

class MoveResponse {
    int id
    String name
    int accuracy
    int power
    int pp
    int priority
}

class Move {
    int id
    String name
    int accuracy
    int power
    int pp
    int priority
//    Status status
//    Type type
//    double status_chance

    static Move from(MoveResponse mr) {
        return new Move(
                name: mr.name,
                id: mr.id,
                accuracy: mr.accuracy,
                power: mr.power,
                pp: mr.pp,
                priority: mr.priority,
        )
    }
}