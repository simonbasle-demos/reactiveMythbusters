package reactive.mythbusters.support;

/**
 * @author Simon Baslé
 */
public class StringUtils {

	public static String truncateForTweet(String original) {
		if (original.length() <= 140) return original;
		return original.substring(0, 137) + "...";
	}

}
