package reactive.mythbusters;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import reactive.mythbusters.support.EpisodeService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Simon Baslé
 */
public class Experiment1LearningCurve {

	private static final List<String> EPISODE_NAMES = EpisodeService.EPISODE_NAMES;

	private void imperativeStyle() {
		try {
			for (String episode : EPISODE_NAMES) {
				System.out.println("\t" + episode);
				Thread.sleep(500);
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void reactiveStyle() {
		Flux<String> flux =
		//TODO How could I do that using Flux?
			null;
		//TODO nothing happens?

		//TODO alternative 1

		//TODO better alternative
	}

	public static void main(String[] args) {
		Experiment1LearningCurve experiment = new Experiment1LearningCurve();

		System.out.println("imperative style:");
		experiment.imperativeStyle();

		System.out.println("reactive style:");
		experiment.reactiveStyle();
	}

}
