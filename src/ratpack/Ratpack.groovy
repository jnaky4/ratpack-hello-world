import Cache.Cache
import Cache.InMemoryCache
import Cache.Internal.PokemonCache
import Cache.Internal.MoveCache
import Pokemon.Internal.PokedexAPI
import Pokemon.Pokedex
import Service.PokemonCachingService

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

import ratpack.http.Status

ratpack {
	bindings {
		bind(PokemonCache)
		bind(MoveCache)
		bind(PokemonCachingService)
		bind(Pokedex, PokedexAPI) //binding PokedexAPI as the implementation of Pokedex interface
	}

	handlers {
		//https://pokeapi.co/docs/v2#info
		get { Pokedex p ->
			p.getAvailableResources().then {
				render(json(it))
			}
		}

		get(":resource") { Pokedex p ->
			if(request.getPath() == "favicon.ico") return //tokens will pick up favicon.ico from browser
			p.getResources(request.getPath()).then {
				render(json(it))
			}
		}

		//todo implement offset arg
		get(":resource/:id_or_name") { Pokedex p, PokemonCache pc, MoveCache mc->
			switch (pathTokens.resource) {
				case ("pokemon"):
					//todo fix issue so abstract getfromcache
//					render p.fetchPokemon(pathTokens.id_or_name).then{
//						render(json(it))
//					}
					def poke = pc.getFromCache(pathTokens.id_or_name as Integer)
					poke != null ? render(json(poke)) : p.getPokemon(pathTokens.id_or_name).then {
						println "${pathTokens.id_or_name} not in cache, fetching"
						pc.addToCache(it.id, it)
						render(json(it))
					}
					break
				case ("move"):
					def move = mc.getFromCache(pathTokens.id_or_name as Integer)
					move != null ? render(json(move)) : p.getMove(pathTokens.id_or_name).then {
						println "${pathTokens.id_or_name} not in cache, fetching"
						mc.addToCache(it.id, it)
						render(json(it))
					}
					p.getMove(pathTokens.id_or_name).then {
						render(json(it))
					}
					break
				//todo implement other parts of the api
				case ("evolution-chain"):
				case ("nature"):
				case ("pokedex"):
				case ("stat"):
				case ("type"):
				case ("species"):
					render("505 Not Implemented")
					break
				default:
					p.getResources(request.getPath()).then {
						render(json(it))
					}
					break
			}
		}
	}
}