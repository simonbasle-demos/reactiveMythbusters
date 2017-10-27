package reactive.mythbusters.exp4;

import reactive.mythbusters.support.EpisodeService;

/**
 * @author Simon Baslé
 */
public class Experiment4OnlyForGui {

	public static void main(String[] args) {
		//TODO make something else than a GUI?
	}

	public EpisodeService episodeService() {
		return new EpisodeService();
	}

}
