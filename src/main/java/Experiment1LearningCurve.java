import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Simon Baslé
 */
public class Experiment1LearningCurve {

	private static List<String> EPISODE_NAMES = Arrays.asList(
			"Brown Note", "MacGyver Myths", "Duct Tape Island", "Titanic Survival",
			"Alcatraz Escape", "Salsa Escape", "Youtube Special", "Explosions A to Z"
	);

	private final Random rng = new Random();

	private void imperativeStyle() {
		try {
			for (String episode : EPISODE_NAMES) {
				System.out.println("\t" + episode);
				Thread.sleep(500 + 500 * rng.nextInt(3));
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void reactiveStyle() {
		Flux<String> flux =
		//<editor-fold desc="How could I do that using Flux?">
				Flux.fromIterable(EPISODE_NAMES)
		                        .map(name -> "\t" + name)
		                        .delayUntil(it -> Mono.delay(Duration.ofMillis(
				                        500 + 500 * rng.nextInt(3))));
		//</editor-fold>

		//<editor-fold desc="Nothing happens...">
		//...until you subscribe
//		flux.subscribe(System.out::println, e -> System.err.println("A error occurred: " + e));
		//</editor-fold>

		//<editor-fold desc="Still Nothing?">
		//		CountDownLatch latch = new CountDownLatch(1);
//		flux.doFinally(it -> latch.countDown())
//		    .subscribe(System.out::println, e -> System.err.println("A error occurred: " + e));
//		try {
//			latch.await();
//		}
//		catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		//</editor-fold>

		//<editor-fold desc="But we can do even better!">
		//		flux.doOnNext(System.out::println)
//		    .blockLast();
		//</editor-fold>
	}

	public static void main(String[] args) {
		Experiment1LearningCurve experiment = new Experiment1LearningCurve();

		System.out.println("imperative style:");
		experiment.imperativeStyle();

		System.out.println("reactive style:");
		experiment.reactiveStyle();
	}

}
