package reactive.mythbusters.exp6;

import java.time.Duration;

import org.junit.Test;
import reactive.mythbusters.support.EpisodeService;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Simon Baslé
 */
public class Experiment6Test {

	private Experiment6 experiment = new Experiment6();
	private EpisodeService service = new EpisodeService();

	@Test
	public void topEpisodes() throws Exception {
		StepVerifier.create(experiment.topEpisodes())
		            .expectNextSequence(service.topEpisodes())
		            .expectComplete()
		            .verify();
	}

	@Test
	public void getDescriptionError() throws Exception {
		StepVerifier.create(experiment.describedEpisodes())
		            .assertNext(ep -> assertThat(ep.getTitle()).isEqualTo("Explosions A to Z"))
		            .assertNext(ep -> assertThat(ep.getTitle()).isEqualTo("Salsa Escape"))
		            .assertNext(ep -> assertThat(ep.getTitle()).isEqualTo("Youtube Special"))
		            .expectErrorMessage("Failed to fetch description for Episode 8, the description couldn't escape from Alcatraz")
		            .verify();
	}

	@Test
	public void oneEpisodePerSecond() throws Exception {
		StepVerifier.create(experiment.oneEpisodeTitlePerSecond())
		            .expectNextCount(8)
		            .verifyComplete();
	}

	@Test
	public void oneEpisodePerSecondVirtualTime() throws Exception {
//		Flux<String> flux = experiment.oneEpisodeTitlePerSecond();
//
//		StepVerifier.withVirtualTime(() -> flux)
//		            .thenAwait(Duration.ofSeconds(1))
//		            .expectNext("Explosions A to Z")
//		            .expectNoEvent(Duration.ofSeconds(1))
//		            .expectNext("Salsa Escape")
//		            .thenAwait(Duration.ofMinutes(1))
//		            .expectNext("Youtube Special",
//				            "Alcatraz Escape",
//				            "Titanic Survival",
//				            "Duct Tape Island",
//				            "MacGyver Myths",
//				            "Brown Note")
//		            .expectComplete()
//		            .verify(Duration.ofSeconds(2));

		StepVerifier.withVirtualTime(experiment::oneEpisodeTitlePerSecond)
		            .expectSubscription()
		            .expectNoEvent(Duration.ofSeconds(1))
		            .expectNext("Explosions A to Z")
		            .thenAwait(Duration.ofSeconds(4))
		            .expectNext("Salsa Escape", "Youtube Special",
				            "Alcatraz Escape", "Titanic Survival")
		            .thenAwait(Duration.ofMinutes(1))
		            .expectNext("Duct Tape Island", "MacGyver Myths", "Brown Note")
		            .verifyComplete();
	}

}