package reactive.mythbusters;

import reactive.mythbusters.support.DescribedEpisode;
import reactive.mythbusters.support.EpisodeService;
import reactor.core.publisher.Flux;

/**
 * @author Simon Baslé
 */
public class Experiment2InherentlyComplex {

	public static void main(String[] args) throws InterruptedException {
		EpisodeService service = new EpisodeService();

		Flux.fromIterable(service.topEpisodes())
		    //fetch the description for each episode, transform it to keep only 140 chars
		    .flatMap(ep -> service.getDescription(ep)
		                          .map(desc -> desc.substring(0, 140) + "...\n")
		                          .map(desc -> new DescribedEpisode(ep.getNumber(),
				                          ep.getTitle(),
				                          desc))
		    )
		    //print the full episode info (description included)
		    //but a short message rather than stacktrace in case of error
		    .subscribe(System.out::println,
				    e -> System.err.println("Error getting description of episode: " + e));

		Thread.sleep(3000);
	}

}
