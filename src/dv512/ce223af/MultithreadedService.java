package dv512.ce223af;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * File:	MultithreadedService.java
 * Course: 	21HT - Operating Systems - 1DV512
 * Author: 	Christoffer Eid (ce223af)
 * Date: 	January 2022
 */


// You can implement additional fields and methods in code below, but
// you are not allowed to rename or remove any of it!

// Additionally, please remember that you are not allowed to use any third-party libraries

public class MultithreadedService {

    // TODO: implement a nested public class titled Task here
    // which must have an integer ID and specified burst time (duration) in milliseconds,
    // see below
    // Add further fields and methods to it, if necessary
    // As the task is being executed for the specified burst time, 
    // it is expected to simply go to sleep every X milliseconds (specified below)

  public class Task implements Runnable {
    int id;
    long burstTimeMs;
    long sleepTimeMs;

    public Task(int id, long burstTimeMs, long sleepTimeMs) {
      this.id = id;
      this.burstTimeMs = burstTimeMs;
      this.sleepTimeMs = sleepTimeMs;
    }

    @Override
    public void run() {
      try {
        System.out.println("Running Task " + id);
        while (burstTimeMs <= 0) {
          Thread.sleep(sleepTimeMs); 
          burstTimeMs -= sleepTimeMs;
        }
        System.out.println("Task " + id + " done.");
        Thread.currentThread().interrupt();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      
    }
  }

    // Random number generator that must be used for the simulation
	Random rng;
  ExecutorService pool;

    // ... add further fields, methods, and even classes, if necessary

	public MultithreadedService (long rngSeed) {
        this.rng = new Random(rngSeed);
    }


	public void reset() {
		// TODO - remove any information from the previous simulation, if necessary
    }
    

    // If the implementation requires your code to throw some exceptions, 
    // you are allowed to add those to the signature of this method
    public void runNewSimulation(final long totalSimulationTimeMs,
        final int numThreads, final int numTasks,
        final long minBurstTimeMs, final long maxBurstTimeMs, final long sleepTimeMs) {
        reset();

        pool = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numTasks; i++) {
          pool.execute(new Task(i, rng.nextLong(maxBurstTimeMs-minBurstTimeMs)+minBurstTimeMs, sleepTimeMs));
        }
        pool.shutdown();
        try {
          if (!pool.awaitTermination(totalSimulationTimeMs, TimeUnit.MILLISECONDS)) {
            pool.shutdownNow();
            if (!pool.awaitTermination(totalSimulationTimeMs, TimeUnit.MILLISECONDS)) {
              System.err.println("Pool did not terminate");
            }
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
          pool.shutdownNow();
          Thread.currentThread().interrupt();
        }
       

        // TODO:
        // 1. Run the simulation for the specified time, totalSimulationTimeMs
        // 2. While the simulation is running, use a fixed thread pool with numThreads
        // (see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Executors.html#newFixedThreadPool(int) )
        // to execute Tasks (implement the respective class, see above!)
        // 3. The total maximum number of tasks is numTasks, 
        // and each task has a burst time (duration) selected randomly
        // between minBurstTimeMs and maxBurstTimeMs (inclusive)
        // 4. The implementation should assign sequential task IDs to the created tasks (0, 1, 2...)
        // and it should assign them to threads in the same sequence (rather any other scheduling approach)
        // 5. When the simulation time is up, it should make sure to stop all of the currently executing
        // and waiting threads!

    }


    public void printResults() {
        // TODO:
        System.out.println("Completed tasks:");
        // 1. For each *completed* task, print its ID, burst time (duration),
        // its start time (moment since the start of the simulation), and finish time
        
        System.out.println("Interrupted tasks:");
        // 2. Afterwards, print the list of tasks IDs for the tasks which were currently
        // executing when the simulation was finished/interrupted
        
        System.out.println("Waiting tasks:");
        // 3. Finally, print the list of tasks IDs for the tasks which were waiting for execution,
        // but were never started as the simulation was finished/interrupted
	}




    // If the implementation requires your code to throw some exceptions, 
    // you are allowed to add those to the signature of this method
    public static void main(String args[]) {
		// TODO: replace the seed value below with your birth date, e.g., "20001001"
		final long rngSeed = 19960315;  
				
        // Do not modify the code below — instead, complete the implementation
        // of other methods!
        MultithreadedService service = new MultithreadedService(rngSeed);
        
        final int numSimulations = 1; // TODO: Was 3.
        final long totalSimulationTimeMs = 15*1000L; // 15 seconds
        
        final int numThreads = 4;
        final int numTasks = 30;
        final long minBurstTimeMs = 1*1000L; // 1 second  
        final long maxBurstTimeMs = 10*1000L; // 10 seconds
        final long sleepTimeMs = 100L; // 100 ms

        for (int i = 0; i < numSimulations; i++) {
            System.out.println("Running simulation #" + i);

            service.runNewSimulation(totalSimulationTimeMs,
                numThreads, numTasks,
                minBurstTimeMs, maxBurstTimeMs, sleepTimeMs);

            System.out.println("Simulation results:"
					+ "\n" + "----------------------");	
            service.printResults();

            System.out.println("\n");
        }

        System.out.println("----------------------");
        System.out.println("Exiting...");
        
        // If your program has not completed after the message printed above,
        // it means that some threads are not properly stopped! -> this issue will affect the grade
    }
}
