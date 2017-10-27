package reactive.mythbusters.exp4;

import java.time.Duration;

import reactive.mythbusters.support.DescribedEpisode;
import reactive.mythbusters.support.Episode;
import reactive.mythbusters.support.EpisodeService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * @author Simon Baslé
 */
@SpringBootApplication
@EnableWebFlux
public class Experiment4OnlyForGui {

	public static void main(String[] args) {
		//TODO make something else than a GUI?
		SpringApplication.run(Experiment4OnlyForGui.class, args);
	}

	@Bean
	public EpisodeService episodeService() {
		return new EpisodeService();
	}

}
