package reactive.mythbusters.exp5;

import java.util.concurrent.Flow;

import reactive.mythbusters.support.Episode;
import reactive.mythbusters.support.EpisodeService;

/**
 * @author Simon Baslé
 */
public class EpisodesPublisher1 implements Flow.Publisher<Episode> {

	@Override
	public void subscribe(Flow.Subscriber<? super Episode> subscriber) {
		//TODO look at the Publisher and Subscriber interfaces, looks easy, right?
		EpisodeService service = new EpisodeService();

		//TODO maybe we can simply loop through the episodes?
	}
}
