package reactive.mythbusters;

import java.util.ArrayList;
import java.util.List;

import reactive.mythbusters.support.DescribedEpisode;
import reactive.mythbusters.support.Episode;
import reactive.mythbusters.support.EpisodeService;
import reactive.mythbusters.support.RankedEpisode;
import reactor.core.publisher.Flux;

/**
 * @author Simon Baslé
 */
public class Experiment3LimitedOperators {

	private final EpisodeService service = new EpisodeService();

	private void imperativeRank() {
		List<Episode> episodes = service.topEpisodes();
		List<RankedEpisode> rankedEpisodes = new ArrayList<>(episodes.size());

		for (int i = 0; i < episodes.size(); i++) {
			Episode episode = episodes.get(i);
			try {
				String description = service.getDescriptionSync(episode)
						.substring(0, 140) + "...\n";
				rankedEpisodes.add(new RankedEpisode(episode.getNumber(),
						episode.getTitle(), i, description));
			}
			catch (Exception e) {
				System.err.println("Error getting sync description: " + e);
			}
		}

		rankedEpisodes.forEach(System.out::println);
	}

	private void reactiveRank() {
//		Flux.fromIterable(service.topEpisodes())
//		    //fetch the description for each episode, transform it to keep only 140 chars
//		    .flatMap(ep -> service.getDescription(ep)
//		                          .map(desc -> desc.substring(0, 140) + "...\n")
//		                          .map(desc -> new DescribedEpisode(ep.getNumber(),
//				                          ep.getTitle(),
//				                          desc))
//		    )
//		    //print the full episode info (description included)
//		    //but a short message rather than stacktrace in case of error
//		    .subscribe(System.out::println,
//				    e -> System.err.println("Error getting description of episode: " + e));

		Flux.fromIterable(service.topEpisodes())
		    //keep order and delay errors
		    .flatMapSequentialDelayError(ep -> service.getDescription(ep)
		                                              .map(desc -> desc.substring(0, 140) + "...\n")
		                                              .map(desc -> new DescribedEpisode(ep.getNumber(),
				                                              ep.getTitle(),
				                                              desc))
				    , 2, 2)
		    //add the ranking
		    .zipWith(Flux.range(1, 100), (ep, rank) -> new RankedEpisode(
				    ep.getNumber(), ep.getTitle(), rank, ep.getDescription()))
		    .subscribe(System.out::println,
				    e -> System.err.println("Error getting description of episode: " + e));

		try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
	}

	public static void main(String[] args) throws InterruptedException {
		Experiment3LimitedOperators experiment = new Experiment3LimitedOperators();

		System.out.println("imperative style:");
		experiment.imperativeRank();

		System.out.println("reactive style:");
		experiment.reactiveRank();
	}

}
