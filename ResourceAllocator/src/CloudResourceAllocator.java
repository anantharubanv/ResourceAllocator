import java.util.*;

public class CloudResourceAllocator {

	public static void main(String[] args) {
		Globals.initializeInstances();
		for (String dataRegionName : Globals.instanceMap.keySet())  
	    { 
	        Globals.totalDataCenterRegions++;
	    }
		while(true)
		{
			System.out.println("****************************************************************");
			System.out.println("Choose your choice of purchase");
			System.out.println("****************************************************************");
			System.out.println("[1] Purchase based on minimum CPU need");
			System.out.println("[2] Purchase based on maximum price affordable");
			System.out.println("[3] Purchase based on both minimum CPU need & price affordable");
			System.out.println("");
			System.out.println("Enter your choice (1 or 2 or 3):");
			Scanner scanObj = new Scanner(System.in);
			int cpus=0, hours=0;
			double price=0.0;
			PriceCalculator pricecalc = new PriceCalculator();
			int choice = scanObj.nextInt();
			switch (choice)
			{
			case 1:
				System.out.println("Enter the minimum no. of CPUs needed :");
				cpus = scanObj.nextInt();
				System.out.println("Enter the no. of Hours the servers are needed :");
				hours = scanObj.nextInt();
				if(cpus<=0 || hours<=0)
				{
					System.out.println("Enter valid input");
					break;
				}
				else
				{
					pricecalc.get_costs(hours, cpus, 0.0);
					break;
				}
				case 2:
					System.out.println("Enter the maximum price affordable :");
					price = scanObj.nextDouble();
					System.out.println("Enter the no. of Hours the servers are needed :");
					hours = scanObj.nextInt();
					if(price<=0.0 || hours<=0)
					{
						System.out.println("Enter valid input");
						break;
					}
					else
					{
						pricecalc.get_costs(hours, 0, price);
						break;
					}
				case 3:
					System.out.println("Enter the minimum no. of CPUs needed :");
					cpus = scanObj.nextInt();
					System.out.println("Enter the maximum price affordable :");
					price = scanObj.nextDouble();
					System.out.println("Enter the no. of Hours the servers are needed :");
					hours = scanObj.nextInt();
					if(cpus<=0 || price<=0.0 || hours<=0)
					{
						System.out.println("Enter valid input");
						break;
					}
					else
					{
						pricecalc.get_costs(hours, cpus, price);
						break;
					}
				default:
					break;	
			}
		}
	}
}