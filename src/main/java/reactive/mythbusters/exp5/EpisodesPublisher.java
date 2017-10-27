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

		private volatile boolean cancelled;
		private volatile long    requested;
		static final AtomicLongFieldUpdater<EpisodesSubscription> REQUESTED =
				AtomicLongFieldUpdater.newUpdater(EpisodesSubscription.class, "requested");

		EpisodesSubscription(Flow.Subscriber<? super Episode> target) {
			this.target = target;
			this.episodes = new EpisodeService().topEpisodes();
		}

		@Override
		public void request(long n) {
			if (n <= 0) throw new IllegalArgumentException("Spec. Rule 3.9 - Cannot request a non strictly positive number");

			for(;;) {
				long r = requested;
				long nr = r + n;
				if (nr < 0L) nr = Long.MAX_VALUE;

				if (REQUESTED.compareAndSet(this, r, nr)) {
					break;
				}
			}

			if (requested == Long.MAX_VALUE) {
				fastpath();
			}
			else {
				slowpath(n);
			}
		}

		@Override
		public void cancel() {
			this.cancelled = true;
		}

		void fastpath() {
			for (int i = 0; i < episodes.size(); i++) {
				Episode episode = episodes.get(i);

				if (cancelled) {
					return;
				}
				if (i >= this.produced) {
					this.produced++;
					target.onNext(episode);
				}
			}

			if (cancelled) {
				return;
			}
			target.onComplete();
		}

		void slowpath(long n) {
			Flow.Subscriber<? super Episode> tgt = this.target;
			int index = this.produced;
			int end = episodes.size();
			int emit = 0;

			for(;;) {
				if (cancelled) {
					return;
				}

				while (emit != n && index != end) {
					tgt.onNext(episodes.get(index));

					if (cancelled) {
						return;
					}

					emit++;
					index++;
				}

				if (cancelled) {
					return;
				}

				if (index == end) {
					tgt.onComplete();
					return;
				}

				//now update request and produced
				long r = requested;
				if (r == emit) {
					this.produced = index;
					r = REQUESTED.addAndGet(this, -emit);
					if (r == 0) {
						return;
					}
					emit = 0;
				}
			}
		}
	}
}
