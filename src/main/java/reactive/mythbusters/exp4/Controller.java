package reactive.mythbusters.exp4;

import java.time.Duration;

import reactive.mythbusters.support.DescribedEpisode;
import reactive.mythbusters.support.Episode;
import reactive.mythbusters.support.EpisodeService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Simon Baslé
 */
@RestController
public class Controller {

	private final EpisodeService service;

	public Controller(EpisodeService service) {
		this.service = service;
	}

	@GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> sse() {
		return Flux.fromIterable(service.topEpisodes())
		           .flatMap(ep -> service.getDescription(ep)
		                                 .map(desc -> desc.substring(0, 140) + "...")
		                                 .onErrorReturn("<MISSING DESCRIPTION>")
		                                 .map(desc -> new DescribedEpisode(ep.getNumber(),
				                                 ep.getTitle(), desc)))
		           .map(Episode::toString)
		           .delayElements(Duration.ofSeconds(1))
		           .concatWith(Mono.just("<END OF DATA>"));
	}
}
