package reactive.mythbusters.exp8;

import java.time.Duration;
import java.util.concurrent.ThreadFactory;

import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author Simon Baslé
 */
public class Experiment8Blocking {

	public static void main(String[] args) throws InterruptedException {
		prepareSchedulers(); //simulate 2 processors

		String msg1 = "\t#1";
		String msg2 = "\t\t#2";

		Flux.interval(Duration.ofMillis(500))
		    .subscribe(tick -> System.out.println(msg1));

		Flux.interval(Duration.ofMillis(500))
		    .subscribe(tick -> System.out.println(msg2));

		Mono.delay(Duration.ofSeconds(2))
		    .flatMapMany(i -> {
		    	//TODO wonder if I could sleep here?
			    return Flux.just(1, 2, 3);
		    })
		    .doOnNext(System.out::println)
		    .blockLast();

		safeSleep(2000);
	}

	private static void prepareSchedulers() {
		Schedulers.Factory defaultFactory = new Schedulers.Factory() {};

		Schedulers.setFactory(new Schedulers.Factory() {
			@Override
			public Scheduler newParallel(int parallelism, ThreadFactory threadFactory) {
				return defaultFactory.newParallel(2, threadFactory);
			}
		});
	}

	private static void safeSleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			throw Exceptions.propagate(e);
		}
	}

}
