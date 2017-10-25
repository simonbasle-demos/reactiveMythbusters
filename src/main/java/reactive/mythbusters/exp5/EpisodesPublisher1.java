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
		//looks easy, right?
		EpisodeService service = new EpisodeService();
		for (Episode e : service.topEpisodes()) {
			subscriber.onNext(e);
		}
	}
}
