package reactive.mythbusters.exp7;

/**
 * @author Simon Baslé
 */
public class Experiment7Debug {


	public static void main(String[] args) {
		RandomFluxService service = new RandomFluxService();
		service.getFlux(f -> f.map(i -> 100 / i))
		       .subscribe();
	}

}
