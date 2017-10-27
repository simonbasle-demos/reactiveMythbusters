package reactive.mythbusters.exp5;

import java.util.List;
import java.util.concurrent.Flow;

import reactive.mythbusters.support.Episode;
import reactive.mythbusters.support.EpisodeService;

/**
 * @author Simon Baslé
 */
public class EpisodesPublisher2 implements Flow.Publisher<Episode> {

	private static class EpisodesSubscription implements  Flow.Subscription {

		private final Flow.Subscriber<? super Episode> target;
		private final List<Episode>                    episodes;

		EpisodesSubscription(Flow.Subscriber<? super Episode> target) {
			this.target = target;
			this.episodes = new EpisodeService().topEpisodes();
		}

		@Override
		public void request(long n) {
			episodes.stream()
			        .limit(n)
			        .forEach(target::onNext);
		}

		@Override
		public void cancel() {
			//uh?
		}
	}

	@Override
	public void subscribe(Flow.Subscriber<? super Episode> subscriber) {
		//TODO maybe we need a Subscription?
		subscriber.onSubscribe(new EpisodesSubscription(subscriber));
	}
}
