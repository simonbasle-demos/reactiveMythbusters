package reactive.mythbusters.exp9;

import reactive.mythbusters.support.EpisodeService;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author Simon Baslé
 */
public class Experiment9Concurrent {

	public static void main(String[] args) {
		EpisodeService episodeService = new EpisodeService();

		//How do I make this code run in parallel?
		Flux.fromIterable(episodeService.topEpisodes())
		    .filter(ep -> ep.getNumber() != 8)
		    .parallel(3)
		    .runOn(Schedulers.newParallel("DescriptionPool"))
		    .log("episode description fetch")
		    .flatMap(episodeService::getDescription)
		    .sequential()
		//How do I ensure I show the data in a specific thread (ie the UI thread)?
		    .publishOn(Schedulers.newSingle("UI Thread"))
		    .log("description notification")
		    .blockLast();
	}

}
