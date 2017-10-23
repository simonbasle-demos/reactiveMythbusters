package reactive.mythbusters.support;

/**
 * @author Simon Baslé
 */
public class Episode {

	final String title;
	final int number;

	public Episode(int number, String title) {
		this.title = title;
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public int getNumber() {
		return number;
	}
}
