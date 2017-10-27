package reactive.mythbusters;

import reactive.mythbusters.support.DescribedEpisode;
import reactive.mythbusters.support.EpisodeService;
import reactive.mythbusters.support.StringUtils;
import reactor.core.publisher.Flux;

/**
 * @author Simon Baslé
 */
public class Experiment2InherentlyComplex {

	public static void main(String[] args) throws InterruptedException {
		EpisodeService service = new EpisodeService();

		Flux.fromIterable(service.topEpisodes())
		    //TODO fetch the description for each episode
		    //TODO transform it it to keep only 140 chars via StringUtils.truncateForTweet
		    .concatMap(ep -> service.getDescription(ep)
		                          .map(StringUtils::truncateForTweet)
		                          .map(desc -> new DescribedEpisode(ep, desc))
		    )
		    //TODO print the full episode info (description included), but a short message rather than stacktrace in case of error
		    .subscribe(System.out::println,
				    e -> System.err.println("Error getting description of episode: " + e));

		Thread.sleep(2000);
	}

}
