import Pokemon.Internal.PokedexAPI
import Pokemon.Pokedex

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

import ratpack.http.Status
import Pokemon.PokemonAPI

ratpack {
	bindings {
		bind(Pokedex, PokedexAPI) //binding PokedexAPI as the implementation of Pokedex
	}
	handlers{
		//https://pokeapi.co/docs/v2#info
		get{ Pokedex p ->
			p.getResources().then{
				render(json(it))
			}
		}
		prefix("pokemon"){ //:<path token> ? -> optional
			get(":id_or_name") { Pokedex p ->
				p.getPokemon(request.getPath()).then{
					render(json(it))
				}
			}
			get{ Pokedex p ->
				p.getResources(request.getPath()).then{
					render(json(it))
				}
			}
		}
		get(":token"){Pokedex p ->
			p.getResources(request.getPath()).then{
				render(json(it))
			}
		}
	}
}