package reactive.mythbusters.exp7;

/**
 * @author Simon Baslé
 */
public class Experiment7Debug {


	//TODO 1. run and look at the stack trace
	public static void main(String[] args) {
		RandomFluxService service = new RandomFluxService();
		//TODO 2. see if getFlux can be improved
		service.getFlux(f -> f.map(i -> 100 / i))
		       //TODO 3. can we do some useful logging?
		       .subscribe();

	}

}
