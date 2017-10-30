package reactive.mythbusters.exp4;

import java.time.Duration;

import reactive.mythbusters.support.DescribedEpisode;
import reactive.mythbusters.support.Episode;
import reactive.mythbusters.support.EpisodeService;
import reactive.mythbusters.support.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Simon Baslé
 */
public class Controller {

	private final EpisodeService service;

	public Controller(EpisodeService service) {
		this.service = service;
	}

	public void sse() {
		Flux.fromIterable(service.topEpisodes())
		           .flatMap(ep -> service.getDescription(ep)
		                                 .map(StringUtils::truncateForTweet)
		                                 .onErrorReturn("<MISSING DESCRIPTION>")
		                                 .map(desc -> new DescribedEpisode(ep.getNumber(),
				                                 ep.getTitle(), desc)))
		           .map(Episode::toString)
		           .delayElements(Duration.ofSeconds(1))
		           .concatWith(Mono.just("<END OF DATA>"))
		    //TODO do something less GUI
		.subscribe(System.out::println);
	}
}
