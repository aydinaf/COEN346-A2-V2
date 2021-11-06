/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	public int currentPhilosopher;
	public int chopsticksCount;
	public int philosophersCount;
	
	public enum States {THINKING, TALKING, HUNGRY, EATING};
	States philStates[];

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		philosophersCount = piNumberOfPhilosophers;
		chopsticksCount = philosophersCount;
		philStates = new States[philosophersCount];
		initialization_code();
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * You may need to add more procedures for task 5
	 * -------------------------------
	 */
	
	//TODO: Initialization
	public void initialization_code() { 
		for (int i = 0; i < philosophersCount; i++) {
			philStates[i] = States.THINKING;
		}
	}
		


	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 * @throws InterruptedException 
	 */
	public synchronized void pickUp(final int piTID) throws InterruptedException
	{
		System.out.println(piTID);
		philStates[piTID - 1] = States.HUNGRY;
		//TODO: Check if TEST is correct
		//TEST()
		if ( (philStates[piTID - 1] == States.HUNGRY) && (philStates[(piTID + philosophersCount-1) % philosophersCount] != States.EATING) &&  (philStates[(piTID) % philosophersCount] != States.EATING) ) {
			philStates[piTID - 1] = States.EATING;
		}
		else {
			System.out.println("WAITING()...");
			wait();
		}
		//TODO: Does it need a return()?
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID) //throws InterruptedException
	{
		philStates[piTID - 1] = States.THINKING;
		notify();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 * @throws InterruptedException 
	 */
	public synchronized void requestTalk(final int piTID) throws InterruptedException
	{
		int i = 1;
		while( i < philosophersCount-1) {
			if( philStates[(piTID + i) % philosophersCount] == States.TALKING) {
				wait(); //TODO: Is this correct?
			}
			i++;
		}
		if(philStates[piTID] != States.EATING) {philStates[piTID] = States.TALKING;}
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk(final int piTID)
	{
		philStates[piTID] = States.THINKING;
		notify();
	}
}

// EOF
