/**
*
* The CloudResource Allocator implements an application that
* helps users in purchasing cloud resource instances based
* on their CPU needs & price affordable.
* 
*
* @author  Anantharuban
* @version 1.5
* @since   2020-09-18 
* 
*/

package main.java;
import java.util.*;

public class CloudResourceAllocator {
	/**
     * Launches the application and gets Input for purchase
     * from the users.
     * 
     * @param args
     */
	public static void main(String[] args) {
		Globals.initializeInstances();
		while(true)
		{
			System.out.println("****************************************************************");
			System.out.println("Choose your choice of purchase");
			System.out.println("****************************************************************");
			System.out.println("[1] Purchase based on minimum CPU need");
			System.out.println("[2] Purchase based on maximum price affordable");
			System.out.println("[3] Purchase based on both minimum CPU need & price affordable");
			System.out.println("[4] Exit");
			System.out.println("");
			System.out.println("Enter your choice (1 or 2 or 3 or 4):");
			int cpus=0, hours=0;
			double price=0.0;
			PriceCalculator pricecalc = new PriceCalculator();
			Scanner scanObj = new Scanner(System.in);
			int choice = scanObj.nextInt();
			int result = 0;
			switch (choice)
			{
			case 1:
				System.out.println("Enter the minimum no. of CPUs needed :");
				cpus = scanObj.nextInt();
				System.out.println("Enter the no. of Hours the servers are needed :");
				hours = scanObj.nextInt();
				System.out.println("");
				if(cpus<=0 || hours<=0)
				{
					System.out.println("Enter valid input");
					break;
				}
				else
				{
					result = pricecalc.get_costs(hours, cpus, 0.0);
					if (result==0)
					{
						System.out.println("The Entered request could not be serviced");
						break;
					}
					else
						break;
				}
				case 2:
					System.out.println("Enter the maximum price affordable :");
					price = scanObj.nextDouble();
					System.out.println("Enter the no. of Hours the servers are needed :");
					hours = scanObj.nextInt();
					System.out.println("");
					if(price<=0.0 || hours<=0)
					{
						System.out.println("Enter valid input");
						break;
					}
					else
					{
						result = pricecalc.get_costs(hours, 0, price);
						if (result==0)
						{
							System.out.println("The Entered request could not be serviced");
							break;
						}
						else
							break;
					}
				case 3:
					System.out.println("Enter the minimum no. of CPUs needed :");
					cpus = scanObj.nextInt();
					System.out.println("Enter the maximum price affordable :");
					price = scanObj.nextDouble();
					System.out.println("Enter the no. of Hours the servers are needed :");
					hours = scanObj.nextInt();
					System.out.println("");
					if(cpus<=0 || price<=0.0 || hours<=0)
					{
						System.out.println("Enter valid input");
						break;
					}
					else
					{
						result = pricecalc.get_costs(hours, cpus, price);
						if (result==0)
						{
							System.out.println("The Entered request could not be serviced");
							break;
						}
						else
							break;
					}
				case 4:
					scanObj.close();
					System.exit(1);
				default:
					System.out.println("Enter valid choice");
					break;	
			}
		}
	}
}