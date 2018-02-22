package reactive.mythbusters.exp5;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicLong;

import reactive.mythbusters.support.Episode;
import reactive.mythbusters.support.EpisodeService;

/**
 * @author Simon Baslé
 */
public class EpisodesPublisher3 implements Flow.Publisher<Episode> {

	private static class EpisodesSubscription implements Flow.Subscription {

		private final Flow.Subscriber<? super Episode> target;
		private final List<Episode>                    episodes;

		int index = 0;

		EpisodesSubscription(Flow.Subscriber<? super Episode> target) {
			this.target = target;
			this.episodes = new EpisodeService().topEpisodes();
		}

		@Override
		public void request(long n) {
			final Flow.Subscriber<? super Episode> a = target;

			for (int i = index; i != Math.min(n, episodes.size()); i++) {
				a.onNext(episodes.get(i));
				index++;
			}

			if (index >= episodes.size()) {
				a.onComplete();
			}
		}

		@Override
		public void cancel() {
			//uh?
		}
	}

	@Override
	public void subscribe(Flow.Subscriber<? super Episode> subscriber) {
		subscriber.onSubscribe(new EpisodesSubscription(subscriber));
	}
}
