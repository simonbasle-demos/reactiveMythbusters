package reactive.mythbusters.support;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import reactor.core.publisher.Mono;

/**
 * @author Simon Baslé
 */
public class EpisodeService {

	final Random rng = new Random();

	public static final List<String> EPISODE_NAMES = Arrays.asList(
			"Explosions A to Z",
			"Salsa Escape","Youtube Special",
			"Alcatraz Escape",
			"Titanic Survival",
			"Duct Tape Island",
			"MacGyver Myths",
			"Brown Note"
	);

	private static final List<Episode> TOP = Arrays.asList(
			new Episode(20, "Explosions A to Z"),
			new Episode(117, "Youtube Special"),
			new Episode(26, "Salsa Escape"),
			new Episode(8, "Alcatraz Escape"),
			new Episode(190, "Titanic Survival"),
			new Episode(179, "Duct Tape Island"),
			new Episode(100, "MacGyver Myths"),
			new Episode(25, "Brown Note")
	);

	public Mono<String> getDescription(Episode episode) {
		return getDescription(episode.getNumber())
				.delayElement(Duration.ofMillis(300 * rng.nextInt(3)));
	}

	public String getDescriptionSync(Episode episode) {
		return getDescription(episode).block();
	}

	public String getDescriptionSync(int episodeNumber) {
		return getDescription(episodeNumber).block();
	}

	public Mono<String> getDescription(int episodeNumber) {
		switch (episodeNumber) {
			case 25: return Mono.just("This episode first aired on February 16, 2005, and featured three fascinating and completely unrelated myths: Is “Chinese water torture” really torturous? If a person gets shot with a gun, is there enough momentum to propel them backwards? And, most importantly, are our childhood South Park dreams confirmed—is there really a musical note that can make people crap their pants?");
			case 100: return Mono.just("In this legendary episode, Adam and Jamie subject themselves to a series of tests in order to see if they could match TV character MacGyver’s ingenuity. For those who aren’t familiar, MacGyver was a fictional secret agent, known for solving insanely complex issues by fashioning ridiculous things out of totally ordinary other things. In each of the four [completely ridiculous] tests Adam and Jamie had one hour to complete the task. Unbelievably, they succeeded in all but one, which was developing film with household items. ");
			case 179: return Mono.just("In the premiere of the 2012 season, Adam and Jamie are stranded on a tropical island with nothing but a literal pallet of duct tape. The goal was to survive and escape the island, which the two managed to do in spectacular fashion. Some of their mind-bending accomplishments included using duct tape to find fresh food and water, build a distress signal, fashion makeshift clothing, and building a seaworthy craft reliable enough to actually carry them away from the island.");
			case 190: return Mono.just("The Internet rejoiced to learn that we weren’t idiots for saying, “I’m pretty sure Jack could have totally fit on that door” at the end of Titanic, as Jamie and Adam proved that Titanic director James Cameron got it wrong, BECAUSE SCIENCE! Trying everything from hopping on a giant board in the middle of water, to legitimately replicating hypothermia conditions with a “human body” made of ballistics gelatin and carefully regulated heat coils, Jamie and Adam thoroughly approached Jack’s demise from every scientific (and not-so-scientific) angle. As if that wasn’t epic enough, the crew also busted a myth surrounding a rocket-propelled surfboard, because why not.");
			case 8: return Mono.error(new IllegalStateException("Failed to fetch description for Episode 8, the description couldn't escape from Alcatraz"));
			case 26: return Mono.just("Is it realistic that a prisoner used salsa to corrode the bars on his windows enough to escape prison? Can a silk shirt soaked with urine provide enough strength to bend the bars of a prison cell? And more importantly—and the only real reason why this episode is as famous as it is—what happens when you load an empty cement truck with 850 pounds of ammonium nitrate/fuel oil and then blow it up? Pure goddamn magic; that’s what!");
			case 117: return Mono.just("We’d be willing to wager a pretty penny that if you’re the kind of science nerd who loves Mythbusters, you’re also the kind of person who can get lost for hours on YouTube, learning about all of the world’s most intricate and ridiculous things. Yes? Yes. And that’s why the Mythbusters YouTube episode will always be held near and dear to the hearts of fans everywhere. In this episode, Jamie, Adam, and the rest of the team test out the “match bomb” myth (confirmed), the LEGO Ball myth (busted), the spinning tire fire myth (busted), and HouseholdHacker’s famous “How to Create a High-Def speaker for under a buck” video (painfully busted). To this day, the LEGO Ball myth is one of the most talked about Mythbusters segments of all time.");
			case 20: return Mono.just("Last but not least, considered by many to be the most incredible episode of Mythbusters ever aired, “Explosions A to Z” is pretty much exactly what you’d expect: the Mythbusters team reviews an A-to-Z list of explosions that both take a look back on the hundreds of explosions throughout the show’s history, as well as highlight some interesting new ones. As a bonus, you’ll see another clip of the famous cement truck explosion mentioned earlier in this list (because “C” is for cement truck, naturally), as well as “L” for loud, “R” for rockets, and “X” for uhhh, blowing up xylophones. ");
			default: return Mono.error(new IllegalArgumentException("Unknown Episode: " + episodeNumber));
		}
	}

	public List<Episode> topEpisodes() {
		return new ArrayList<>(TOP);
	}

}
