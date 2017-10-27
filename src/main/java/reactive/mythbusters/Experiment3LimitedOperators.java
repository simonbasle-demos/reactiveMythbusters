package reactive.mythbusters;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.reactivestreams.Publisher;
import reactive.mythbusters.support.DescribedEpisode;
import reactive.mythbusters.support.Episode;
import reactive.mythbusters.support.EpisodeService;
import reactive.mythbusters.support.RankedEpisode;
import reactive.mythbusters.support.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

/**
 * @author Simon Baslé
 */
public class Experiment3LimitedOperators {

	private final EpisodeService service = new EpisodeService();
	private final Random rng = new Random();

	public void imperativeRank() {
		List<Episode> episodes = service.topEpisodes();

		long start = System.currentTimeMillis();
		for (int i = 0; i < episodes.size(); i++) {
			try {
				Thread.sleep(500 + 500 * rng.nextInt(3));
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}

			Episode episode = episodes.get(i);
			String description;
			try {
				description = service.getDescriptionSync(episode.getNumber())
				                     .substring(0, 140) + "...\n";
			}
			catch (Exception e) {
				System.err.println("Error getting sync description for episode " + episode.getNumber() + ": " + e);
				description = "<NO DESCRIPTION>";
			}

			long end = System.currentTimeMillis();
			long delay = (end - start);
			//we truncate the delay displayed to its hundreds
			System.out.println("delay: " + (delay / 100) * 100 + "ms");
			start = end;
			System.out.println(new RankedEpisode(episode.getNumber(),
					episode.getTitle(), i, description));
		}
	}

	private Flux<Episode> displayDelay(Flux<?> source) {
		return source
				.map(obj -> {
					if (obj instanceof Tuple2) {
						@SuppressWarnings("unchecked") Tuple2<Long, Episode> t2 = (Tuple2<Long, Episode>) obj;
						//we truncate the delay displayed to its hundreds
						System.out.println("delay: " + (t2.getT1() / 100) * 100 + "ms");
						return t2.getT2();
					}
					else return (Episode) obj;
		});
	}

	public Flux<RankedEpisode> reactiveRank() {
		Function<? super Episode, Publisher<? extends DescribedEpisode>> fetchDescription = ep ->
				service.getDescription(ep.getNumber())
				       .map(StringUtils::truncateForTweet)
				       .map(desc -> new DescribedEpisode(ep, desc));

		Flux<RankedEpisode> result =
				Flux.fromIterable(service.topEpisodes())
				    //TODO change to introduce an everchanging random delay
//				    .delayElements(Duration.ofMillis(500 + 500 * rng.nextInt(3)))
                    .delayUntil(it -> Mono.delay(Duration.ofMillis(500 + 500 * rng.nextInt(3))))
                    //TODO how could we verify the delay?
                    //.something()
                    .elapsed()
                    .compose(this::displayDelay)
                    //TODO change to keep order and delay error when fetching description
//                    .flatMap(fetchDescription)
                    .concatMapDelayError(fetchDescription, true, 8)
                    //TODO change to correctly add the ranking
//                    .map(ep -> new RankedEpisode(ep.getNumber(), ep.getTitle(),
//		                    -1, ep.getDescription()));
                    .zipWith(Flux.range(1, 100), (ep, rank) ->
		                    new RankedEpisode(ep, rank, ep.getDescription()));

		return result;
	}

	public static void main(String[] args) throws InterruptedException {
		Experiment3LimitedOperators experiment = new Experiment3LimitedOperators();

		System.out.println("imperative style:");
		experiment.imperativeRank();

		System.out.println("reactive style:");
		final CountDownLatch latch = new CountDownLatch(1);
		experiment.reactiveRank()
		          //due to random delays, we'll use a latch for this example to run through
		          .doFinally(it -> latch.countDown())
		          .subscribe(System.out::println, e -> System.err.println("Error getting description of episode: " + e));

		try { latch.await(1, TimeUnit.MINUTES); } catch (InterruptedException e) { e.printStackTrace(); }
	}

}
