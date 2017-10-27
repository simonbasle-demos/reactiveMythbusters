package reactive.mythbusters.exp6;

import java.time.Duration;

import reactive.mythbusters.support.DescribedEpisode;
import reactive.mythbusters.support.Episode;
import reactive.mythbusters.support.EpisodeService;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author Simon Baslé
 */
public class Experiment6 {

	public static void main(String[] args) {
		//TODO go edit the test
		System.err.println("Let's meet in /src/main/test/reactive/mythbusters/exp6/Experiment6Test instead");
	}


	private EpisodeService service = new EpisodeService();

	public Flux<Episode> topEpisodes() {
		return Flux.fromIterable(service.topEpisodes());
	}

	public Flux<DescribedEpisode> describedEpisodes() {
		return topEpisodes()
				.concatMap(ep -> service.getDescription(ep)
				                        .map(desc -> new DescribedEpisode(ep, desc)));
	}

	public Flux<String> oneEpisodeTitlePerSecond() {
		return topEpisodes()
				.map(Episode::getTitle)
				.delayElements(Duration.ofSeconds(1), Schedulers.newElastic("test"))
				.doOnNext(title -> System.out.println("Broadcasting " + title));
	}

}
