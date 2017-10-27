package reactive.mythbusters.exp7;

import java.util.Random;
import java.util.function.Function;

import reactor.core.publisher.Flux;

/**
 * @author Simon Baslé
 */
public class RandomFluxService {

	public Flux<Integer> getFlux(
			Function<? super Flux<Integer>, Flux<Integer>> operation) {
		Random rng = new Random();
		if (rng.nextBoolean()) {
			return Flux.just(1, 2, 0, 3, 4)
			           .compose(operation)
			           .checkpoint("fixed values", true);
		}
		return Flux.just(
				rng.nextInt(5),
				rng.nextInt(5),
				rng.nextInt(5),
				rng.nextInt(5),
				rng.nextInt(5))
		           .compose(operation)
		           .checkpoint("random values", true);
	}
}
