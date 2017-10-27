package reactive.mythbusters.exp5;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.reactivestreams.Subscription;
import reactive.mythbusters.support.Episode;
import reactive.mythbusters.support.EpisodeService;
import reactor.adapter.JdkFlowAdapter;
import reactor.core.Exceptions;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Simon Baslé
 */
public class Experiment5Flow {

	public static void main(String[] args) {
		//TODO attempt at implementing Flow.Publisher, test the attempts

		testFlow(new EpisodesPublisher1(), 1);
		testFlow(new EpisodesPublisher2(), 2);
		testFlow(new EpisodesPublisher3(), 3);
		testFlow(new EpisodesPublisher(), 4);
	}

	private static void testFlow(Flow.Publisher<Episode> publisher, int attempt) {
		System.out.println("\n\nAttempt " + attempt);

		Flux<Episode> episodeFlux = JdkFlowAdapter.flowPublisherToFlux(publisher);

		try {
			assertFull(episodeFlux);
			System.out.println(attempt + " assertFull ok");
		} catch (Throwable e) {
			System.out.println(attempt + " Failed assertFull test: " + e);
		}

		try {
			assertCancel(episodeFlux);
			System.out.println(attempt + " assertCancel ok");
		} catch (Throwable e) {
			System.out.println(attempt + " Failed assertCancel test: " + e);
		}

		try {
			assertBackpressure(episodeFlux);
			System.out.println(attempt + " assertBackpressure ok");
		} catch (Throwable e) {
			System.out.println(attempt + " Failed assertBackpressure test: " + e);
		}

		try {
			assertBackpressureConcurrent(episodeFlux);
			System.out.println(attempt + " assertBackpressureConcurrent ok");
		} catch (Throwable e) {
			System.out.println(attempt + " Failed assertBackpressureConcurrent test: " + e);
		}
	}

	private static void assertFull(Flux<Episode> episodeFlux) {
		List<String> titles = episodeFlux
				.map(Episode::getTitle)
				.collectList()
				.block(Duration.ofSeconds(1));

		assertThat(titles)
				.as("Unbounded request")
				.containsExactlyElementsOf(EpisodeService.EPISODE_NAMES);
	}

	private static void assertCancel(Flux<Episode> episodeFlux) {
		List<String> titles = episodeFlux
				.onBackpressureError()
				.take(3)
				.map(Episode::getTitle)
				.collectList()
				.block(Duration.ofSeconds(1));

		assertThat(titles)
				.as("Cancellation")
				.containsExactlyElementsOf(EpisodeService.EPISODE_NAMES.subList(0, 3));
	}

	private static void assertBackpressure(Flux<Episode> episodeFlux) {
		StepVerifier.create(episodeFlux.map(Episode::getTitle), 0)
		            .expectSubscription()
		            .thenRequest(2)
		            .expectNext("Explosions A to Z", "Salsa Escape")
		            .expectNoEvent(Duration.ofMillis(300))
		            .thenRequest(2)
		            .expectNext("Youtube Special", "Alcatraz Escape")
		            .expectNoEvent(Duration.ofMillis(300))
		            .thenCancel()
		            .verify(Duration.ofSeconds(1));
	}

	private static void assertBackpressureConcurrent(Flux<Episode> episodeFlux) {
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		List<String> collector = Collections.synchronizedList(new ArrayList<>());
		CountDownLatch latch = new CountDownLatch(3);
		AtomicReference<RuntimeException> error = new AtomicReference<>();
		BaseSubscriber<String> sub = new BaseSubscriber<>() {

			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				//do not immediately request
			}

			@Override
			protected void hookOnNext(String value) {
				collector.add(value);
			}

			@Override
			protected void hookOnError(Throwable throwable) {
				error.set(Exceptions.propagate(throwable));
			}
		};

		episodeFlux.map(Episode::getTitle)
		           .subscribe(sub);

		executorService.submit(() -> {
			sub.request(2);
			latch.countDown();
		});
		executorService.submit(() -> {
			sub.request(1);
			latch.countDown();
		});
		executorService.submit(() -> {
			sub.request(1);
			latch.countDown();
		});

		try {
			latch.await(2, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			throw new IllegalStateException("Failed waiting for requests", e);
		}

		sub.cancel();
		executorService.shutdownNow();
		assertThat(error.get()).isNull();
		assertThat(collector).containsExactly(
				"Explosions A to Z", "Salsa Escape",
				"Youtube Special", "Alcatraz Escape"
		);
	}

}
