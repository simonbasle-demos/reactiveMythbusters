package reactive.mythbusters.exp4;

import reactive.mythbusters.support.EpisodeService;

import org.springframework.boot.SpringApplication;

/**
 * @author Simon Baslé
 */
public class Experiment4OnlyForGui {

	public static void main(String[] args) {
		//TODO make something else than a GUI?
		new Controller(new EpisodeService()).sse();
		try { Thread.sleep(5000); }catch (InterruptedException e) { e.printStackTrace(); }
	}

	public EpisodeService episodeService() {
		return new EpisodeService();
	}

}
