import java.util.ArrayList;
import java.util.Random;

public class Program
{

	final static int NUM_PROCS = 6; // How many concurrent processes
	final static int TOTAL_RESOURCES = 30; // Total resources in the system
	final static int MAX_PROC_RESOURCES = 13; // Highest amount of resources any process could need
	final static int ITERATIONS = 30; // How long to run the program
	static int totalHeldResources; 
	static boolean found;
	static Random rand = new Random();
	
	
	public static void main(String[] args)
	{
		totalHeldResources = 0;
		// The list of processes:
		ArrayList<Proc> processes = new ArrayList<Proc>();
		ArrayList<Proc> copyProcesses = new ArrayList<Proc>();
		ArrayList<Proc> procsToRemove = new ArrayList<Proc>();
		for (int i = 0; i < NUM_PROCS; i++)
		{
			processes.add(new Proc(MAX_PROC_RESOURCES - rand.nextInt(3))); // Initialize to a new Proc, with some small range for its max
			//totalHeldResources += processes.get(i).getHeldResources(); //heldResources is instantiated to 0
		}
				
		// Run the simulation:
		for (int i = 0; i < ITERATIONS; i++)
		{
			// loop through the processes and for each one get its request
			for (int j = 0; j < processes.size(); j++)
			{
				// Get the request
				int currRequest = processes.get(j).resourceRequest(TOTAL_RESOURCES - totalHeldResources);

				// just ignore processes that don't ask for resources
				if (currRequest == 0)
					continue;
				
				// Here you have to enter code to determine whether or not the request can be granted,
				// and then grant the request if possible. Remember to give output to the console 
				// this indicates what the request is, and whether or not its granted.
				else if(currRequest < 0)
				{
					System.out.println("Process " + j + " completed and returned " + -currRequest + " resources");
					totalHeldResources += currRequest;
				}

				else
				{
					int availableResources = TOTAL_RESOURCES - totalHeldResources - currRequest;
					processes.get(j).addResources(currRequest);
					System.out.println("Process " + j + " requesting " + currRequest + " resources");
					copyProcesses.addAll(processes);
					while (copyProcesses.size() > 0)
					{
						found = false;
						for (Proc p : processes) 
						{
							if (p.getMaxResources() - p.getHeldResources() <= availableResources) 
							{
								availableResources += p.getHeldResources();
								procsToRemove.add(p);
								found = true;
							}
						}
						copyProcesses.removeAll(procsToRemove);
						procsToRemove.clear();
						if(!found)break;
					}
					
					System.out.println("\n***** STATUS *****");
					System.out.println("Total Available: " + (TOTAL_RESOURCES - totalHeldResources));
					for (int k = 0; k < processes.size(); k++)
						System.out.println("Process " + k + " holds: " + processes.get(k).getHeldResources() + ", max: " +
								processes.get(k).getMaxResources() + ", claim: " + 
								(processes.get(k).getMaxResources() - processes.get(k).getHeldResources()));
					System.out.println("***** STATUS *****\n");
					
					if(found)
					{
						totalHeldResources += currRequest;
						System.out.println("The request was granted");
					}
					else
					{
						processes.get(j).addResources(-currRequest);
						System.out.println("This request cannot be granted, since it can lead to an unsafe state");
					}
				}
			}

		}

	}
}