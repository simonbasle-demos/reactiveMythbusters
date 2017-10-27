package reactive.mythbusters.exp5;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

import reactive.mythbusters.support.Episode;
import reactive.mythbusters.support.EpisodeService;

/**
 * @author Simon Baslé
 */
public class EpisodesPublisher implements Flow.Publisher<Episode> {

	@Override
	public void subscribe(Flow.Subscriber<? super Episode> subscriber) {
		subscriber.onSubscribe(new EpisodesSubscription(subscriber));
	}

	private static class EpisodesSubscription implements Flow.Subscription {

		private final Flow.Subscriber<? super Episode> target;
		private final List<Episode>                    episodes;

		//TODO we need to track request and cancellation
		private int produced;

		EpisodesSubscription(Flow.Subscriber<? super Episode> target) {
			this.target = target;
			this.episodes = new EpisodeService().topEpisodes();
		}

		@Override
		public void request(long n) {
			if (n <= 0) throw new IllegalArgumentException("Spec. Rule 3.9 - Cannot request a non strictly positive number");

			//TODO safely update the request and cap it to Long.MAX_VALUE

			//TODO choose between fastpath() and slowpath().
		}

		@Override
		public void cancel() {
			//TODO implement cancellation?
		}

		void fastpath() {
			//emit all the data
			for (int i = 0; i < episodes.size(); i++) {
				Episode episode = episodes.get(i);

				//TODO BUT take cancellation into account

				//TODO AND ensure not to repeat data from a previous small request
			}

			//TODO then complete, once again taking cancellation into account
		}

		void slowpath(long n) {
			Flow.Subscriber<? super Episode> tgt = this.target;
			int index = this.produced;
			int end = episodes.size();
			int emit = 0;

			//TODO drain loop that:
			//TODO  - support cancellation
			//TODO  - emit relevant data and update emit/index
			//TODO  - completes if needed (exits loop)
			//TODO  - updates the request (-emit) and produced (index) if r == emit
			//TODO  - exits if no more request, otherwise resets emit
		}
	}
}
