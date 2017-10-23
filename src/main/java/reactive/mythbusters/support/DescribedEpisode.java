package reactive.mythbusters.support;

/**
 * @author Simon Baslé
 */
public class DescribedEpisode extends Episode {

	final String description;

	public DescribedEpisode(int number, String title, String description) {
		super(number, title);
		this.description = description == null ? "" : description;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "Episode " + number + " - " + title
				+ "\n" + description;
	}
}
