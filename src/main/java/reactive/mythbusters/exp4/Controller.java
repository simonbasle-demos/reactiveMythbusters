package reactive.mythbusters.exp4;

import reactive.mythbusters.support.EpisodeService;
import reactor.core.publisher.Flux;

/**
 * @author Simon Baslé
 */
public class Controller {

	private final EpisodeService service;

	public Controller(EpisodeService service) {
		this.service = service;
	}

	public Flux<String> sse() {
		//TODO top episode + truncated description with error handling
		//TODO send 1 toString per second with completion placeholder
		return Flux.just("WRONG");
	}
}
