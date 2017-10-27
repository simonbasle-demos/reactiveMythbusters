package reactive.mythbusters.support;

/**
 * @author Simon Baslé
 */
public class RankedEpisode extends DescribedEpisode {

	final int rank;

	public RankedEpisode(int number, String title, int rank, String description) {
		super(number, title, description);
		this.rank = rank;
	}

	public RankedEpisode(Episode ep, int rank, String description) {
		super(ep.getNumber(), ep.getTitle(), description);
		this.rank = rank;
	}

	public int getRank() {
		return rank;
	}

	@Override
	public String toString() {
		return "#" + rank + ": " + super.toString();
	}
}
