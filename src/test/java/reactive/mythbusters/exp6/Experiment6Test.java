package reactive.mythbusters.exp6;

import java.time.Duration;

import org.junit.Test;
import reactive.mythbusters.support.DescribedEpisode;
import reactive.mythbusters.support.Episode;
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
		Flux<Episode> toTest = experiment.topEpisodes();

		//TODO test the flux of top episodes
	}

	@Test
	public void getDescriptionError() throws Exception {
		Flux<DescribedEpisode> toTest = experiment.describedEpisodes();

		//TODO test an erroring Flux
		//"Explosions A to Z"
		//"Salsa Escape"
		//"Youtube Special"
		//"Failed to fetch description for Episode 8, the description couldn't escape from Alcatraz"
	}

	@Test
	public void oneEpisodePerSecond() throws Exception {
		Flux<String> toTest = experiment.oneEpisodeTitlePerSecond();

		//TODO test delayed Flux, look at the test duration
	}

	@Test
	public void oneEpisodePerSecondVirtualTime() throws Exception {
		//TODO run the test below, look at the test duration and try to improve it
		Flux<String> flux = experiment.oneEpisodeTitlePerSecond();

		StepVerifier.withVirtualTime(() -> flux)
		            .expectSubscription()
		            .expectNoEvent(Duration.ofSeconds(1))
		            .expectNext("Explosions A to Z")
		            .thenAwait(Duration.ofSeconds(4))
		            .expectNext("Salsa Escape", "Youtube Special",
				            "Alcatraz Escape", "Titanic Survival")
		            .thenAwait(Duration.ofMinutes(1))
		            .expectNext("Duct Tape Island", "MacGyver Myths", "Brown Note")
		            .expectComplete()
		            .verify(Duration.ofSeconds(1));
	}

}