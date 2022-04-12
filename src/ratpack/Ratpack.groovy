import Pokemon.Internal.PokedexAPI
import Pokemon.Pokedex
import Service.PokemonCachingService

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

import ratpack.http.Status

ratpack {
	bindings {
		bind(PokemonCachingService)
		bind(Pokedex, PokedexAPI) //binding PokedexAPI as the implementation of Pokedex interface
	}

	handlers {
		//https://pokeapi.co/docs/v2#info
		get { Pokedex p ->
			p.getResources().then {
				render(json(it))
			}
		}

		get(":resource") { Pokedex p ->
			if(request.getPath() == "favicon.ico") return
			p.getResources(request.getPath()).then {
				render(json(it))
			}
		}

		get(":resource/:id_or_name") { Pokedex p ->
			switch (pathTokens.resource) {
				case ("pokemon"):
					p.getPokemon(pathTokens.id_or_name).then {
						render(json(it))
					}
					break
				default:
					p.getResources(request.getPath()).then {
						render(json(it))
					}
			}
		}
	}


//		prefix("pokemon"){ //:<path token> ? -> optional
//			get(":id_or_name") { Pokedex p ->
//				p.getPokemon(pathTokens.id_or_name).then{
//					render(json(it))
//				}
//			}
//			get{ Pokedex p ->
//				p.getResources(request.getPath()).then{
//					render(json(it))
//				}
//			}
//		}
//		get(":token"){Pokedex p ->
//			p.getResources(request.getPath()).then{
//				render(json(it))
//			}
//		}
//	}
}