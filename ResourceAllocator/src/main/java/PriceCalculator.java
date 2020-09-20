/**
*
* The PriceCalculator holds all the functions that are needed to  
* calculate the costs and prepare the LinkedHashMaps to return 
* the quoted prices of servers to users.
*
* @author  Anantharuban
* @version 1.7
* @since   2020-09-18 
* 
*/

package main.java;
import java.util.*;

public class PriceCalculator {
	/**
	 *  
     * Calculates the price for the minimum CPUs requested by
     * the user and also constructs the LinkedHashMap of the result.
     * 
     * Logic : In this case the user just specifies the CPUs needed and there are
     * no constraints on price, so the company has to sell all server types equally.
     * (not one server type always highly chosen over the others).
     * 
     * @param hours - number of hours servers are needed by the user
     * @param cpus - number of CPUs requested by the user
     * @return - Success(1) or Failure(0)
     * 
     */
	private int totalCalculatorWithCPU(int hours, int cpus)
	{
		LinkedHashMap <String, LinkedHashMap<String, Integer>> finalServerCountDetails = new LinkedHashMap <String, LinkedHashMap<String, Integer>>();
		LinkedHashMap <String, Integer> finalInnerServerCountDetails;
		LinkedHashMap <String, Double> finalServerPriceDetails = new LinkedHashMap <String, Double>();
		int validRequest = 0;
		// Each data region is processed one by one 
		for (String dataRegionName : Globals.instanceMap.keySet())  
	    { 
			finalInnerServerCountDetails = new LinkedHashMap<String, Integer>();
			int tempcpus = cpus;
			Vector<Integer> serverWeight = new Vector<Integer>();
			Vector<Double> serverPrice = new Vector<Double>();
			Vector<Integer> serverCount = new Vector<Integer>();
			int i=0;
			double totalCost = 0.0;
			int dataRegionValid = 0;
			LinkedHashMap<String,Double> ServerDetails = Globals.instanceMap.get(dataRegionName);
			for (String ServerTypeName : ServerDetails.keySet()){
				serverWeight.add(Globals.serverTypeMap.get(ServerTypeName));
				serverPrice.add(ServerDetails.get(ServerTypeName));
				serverCount.add(0);
			}
			// Sometimes the minimum requested CPU value may not be a multiple of server type values available, so here
			// to it is made a multiple to achieve the price of minimum requested CPUs.
			if(cpus%(serverWeight.firstElement())!=0)
			{
				tempcpus = tempcpus - (cpus%(serverWeight.firstElement())) + (serverWeight.firstElement());
			}
			int serverWeightCount = serverWeight.size();
			// Almost equal number of each Server-types are assigned, so that no Server-type is always hot-selling than others.
			// Moreover by making this we would achieve a moderate total price. 
			while (tempcpus > 0)
			{
				if(tempcpus - serverWeight.get(i) >= 0)
				{
					tempcpus = tempcpus - serverWeight.get(i);
					serverCount.set(i,serverCount.get(i)+1);
					i++;
					dataRegionValid++;
					if(i>=serverWeightCount)
					{
						i=0;
					}
				}
				else
				{
					i=0;
				}
			}
			//Only if the calculated values for this Region is valid it is proceeded
			if (dataRegionValid > 0)
			{
				validRequest++;
				for(i=0;i<serverWeightCount;i++)
				{
					totalCost = totalCost + (serverPrice.get(i)*serverCount.get(i));
				}
				// Till now price for one hour is found so it is multiplied by hours
				totalCost = totalCost * hours;
				totalCost = Math.round(totalCost * 100.0)/100.0;
				// LinkedHashMap Constructed - Total price as Value and Region name as key
				finalServerPriceDetails.put(dataRegionName,totalCost);
				for(i=0;i<serverWeightCount;i++)
				{
					if(serverCount.get(i)!=0)
					{
						for (String serverTypeMapString : Globals.serverTypeMap.keySet())
						{
							if(Globals.serverTypeMap.get(serverTypeMapString) == serverWeight.get(i))
							{
								finalInnerServerCountDetails.put(serverTypeMapString,serverCount.get(i));
								break;
							}
						}						
					}
				}
				// LinkedHashMap Constructed - Count of each Server-types included as Value and Region name as key
				finalServerCountDetails.put(dataRegionName, finalInnerServerCountDetails);
			}
		}
		// If no Region values are valid, it means there is no possibility to service the request of the customer
		if(validRequest > 0)
		{
			PrintPriceDetails printDetails = new PrintPriceDetails();
			printDetails.printServerPriceDetails(finalServerCountDetails, finalServerPriceDetails);
			return 1;
		}
		else
		{
			return 0;
		}
	}
	/**
	 *  
     * Calculates the maximum CPUs that could be purchased with the price quoted
     * by the user and also constructs the LinkedHashMap of the result.
     * 
     * Logic : In this case we need to find the maximum CPUs for a given price.
     * This is same as Unbounded 0/1 Maximization knapsack problem 
     * ( But here price has to taken as the weight of the items and maximum
     * CPU count has to be achieved with the available server-types in a data-center region ).
     * (Since price is a double value here it is multiplied by 1000 and the knapsack is 
     * solved - This gives price precision of three decimal places).
     * 
     * @param hours - number of hours servers are needed by the user
     * @param price - affordable price quoted by the user
     * @return - Success(1) or Failure(0)
     * 
     */
	private int totalCalculatorWithPrice(int hours, double price)
	{
		LinkedHashMap <String, LinkedHashMap<String, Integer>> finalServerCountDetails = new LinkedHashMap <String, LinkedHashMap<String, Integer>>();
		LinkedHashMap <String, Integer> finalInnerServerCountDetails;
		LinkedHashMap <String, Double> finalServerPriceDetails = new LinkedHashMap <String, Double>();
		int dataRegionValid = 0;
		// Each data region is processed one by one 
		for (String dataRegionName : Globals.instanceMap.keySet())  
	    { 
			finalInnerServerCountDetails = new LinkedHashMap<String, Integer>();
			// price is multiplied by 1000 to make it a whole number to apply knapsack DP logic
			int tempprice = (int)((price*1000.0)/hours);
			Vector<Integer> serverWeight = new Vector<Integer>();
			Vector<Integer> serverPrice = new Vector<Integer>();
			Vector<Integer> serverCount = new Vector<Integer>();
			int i=0,j=0;
			double totalCost = 0.0;
			LinkedHashMap<String,Double> ServerDetails = Globals.instanceMap.get(dataRegionName);
			for (String ServerTypeName : ServerDetails.keySet()){
				serverWeight.add(Globals.serverTypeMap.get(ServerTypeName));
				// Each Server-type value is also made a whole number by multiplying with 1000
				serverPrice.add((int)(ServerDetails.get(ServerTypeName)*1000));
				serverCount.add(0);
			}
			int serverWeightCount = serverWeight.size();
			// Maximization Knapsack using Dynamic Programming
			int knapsackArray[][]=new int[serverWeightCount+1][tempprice+1];
			for(i=0;i<=serverWeightCount;i++)
			{
				for(j=0;j<=tempprice;j++)
				{
					if(i==0 || j==0)
						knapsackArray[i][j]=0;
					else if(serverPrice.get(i-1)<=j)
						knapsackArray[i][j] = Math.max(serverWeight.get(i-1)+knapsackArray[i][j-serverPrice.get(i-1)],
													knapsackArray[i-1][j]);
					else
						knapsackArray[i][j] = knapsackArray[i-1][j];
				}
			}
			i=serverWeightCount;
			j=tempprice;
			// If Knapsack is valid, how many count of each Server-type is being used is traced from the 2D matrix
			if(knapsackArray[serverWeightCount][tempprice] != 0)
			{
				dataRegionValid++;
				while (i > 0 && j > 0) {
					if (knapsackArray[i][j] == knapsackArray[i - 1][j])
						i--;
					else {
						j=j-serverPrice.get(i-1);
						serverCount.set(i-1,serverCount.get(i-1)+1);
					}
				}
				for(i=0;i<serverWeightCount;i++)
				{
					totalCost = totalCost + (serverPrice.get(i)*serverCount.get(i));
				}
				// Again reverting price value to double by dividing by 1000
				totalCost = totalCost * hours / 1000.0;
				totalCost = Math.round(totalCost * 100.0)/100.0;
				// LinkedHashMap Constructed - Total price as Value and Region name as key
				finalServerPriceDetails.put(dataRegionName,totalCost);
				for(i=0;i<serverWeightCount;i++)
				{
					if(serverCount.get(i)!=0)
					{
						for (String serverTypeMapString : Globals.serverTypeMap.keySet())
						{
							if(Globals.serverTypeMap.get(serverTypeMapString) == serverWeight.get(i))
							{
								finalInnerServerCountDetails.put(serverTypeMapString,serverCount.get(i));
								break;
							}
						}						
					}
				}
				// LinkedHashMap Constructed - Count of each Server-types included as Value and Region name as key
				finalServerCountDetails.put(dataRegionName, finalInnerServerCountDetails);
			}
	    }
		// If no Region values are valid, it means there is no possibility to service the request of the customer
		if(dataRegionValid>0)
		{
			PrintPriceDetails printDetails = new PrintPriceDetails();
			printDetails.printServerPriceDetails(finalServerCountDetails, finalServerPriceDetails);
			return 1;
		}
		else
		{
			return 0;
		}
	}
	/**
	 *  
     * Calculates whether the given CPU count could be purchased with the price quoted
     * by the user and also constructs the LinkedHashMap of the result.
     * 
     * Logic : In this case we need get the minimum CPUs quoted for a given price.
     * This is same as Unbounded 0/1 Minimization knapsack problem
     * ( But here server type is taken as the weight of the items and minimum price to buy
     * CPUs needed is calculated. Then the final price is checked against the quoted price 
     * to find whether it is less than quoted price.
     * 
     * @param hours - number of hours servers are needed by the user
     * @param cpus - number of CPUs requested by the user
     * @param price - affordable price quoted by the user
     * @return - Success(1) or Failure(0)
     * 
     */
	private int totalCalculatorWithCPUandPrice(int hours, int cpus, double price)
	{
		LinkedHashMap <String, LinkedHashMap<String, Integer>> finalServerCountDetails = new LinkedHashMap <String, LinkedHashMap<String, Integer>>();
		LinkedHashMap <String, Integer> finalInnerServerCountDetails;
		LinkedHashMap <String, Double> finalServerPriceDetails = new LinkedHashMap <String, Double>();
		int dataRegionValid = 0;
		for (String dataRegionName : Globals.instanceMap.keySet())  
	    { 
			finalInnerServerCountDetails = new LinkedHashMap<String, Integer>();
			int tempcpus = cpus;
			Vector<Integer> serverWeight = new Vector<Integer>();
			Vector<Double> serverPrice = new Vector<Double>();
			Vector<Integer> serverCount = new Vector<Integer>();
			int i=0,j=0;
			double totalCost = 0.0;
			LinkedHashMap<String,Double> ServerDetails = Globals.instanceMap.get(dataRegionName);
			for (String ServerTypeName : ServerDetails.keySet()){
				serverWeight.add(Globals.serverTypeMap.get(ServerTypeName));
				serverPrice.add(ServerDetails.get(ServerTypeName));
				serverCount.add(0);
			}
			// Making minimum requested CPU count a multiple of least weight Server-type
			if(cpus%(serverWeight.firstElement())!=0)
			{
				tempcpus = tempcpus - (cpus%(serverWeight.firstElement())) + (serverWeight.firstElement());
			}
			int serverWeightCount = serverWeight.size();
			double knapsackArray[][]=new double[serverWeightCount+1][tempcpus+1];
			// Minimization Knapsack using Dynamic Programming
			for(i=0;i<=tempcpus;i++)
				knapsackArray[0][i]=Double.MAX_VALUE;
			for(i=0;i<=serverWeightCount;i++)
				knapsackArray[i][0]=0;
			for(i=1;i<=serverWeightCount;i++)
			{
				for(j=1;j<=tempcpus;j++)
				{
					if(serverWeight.get(i-1)<=j)
						knapsackArray[i][j] = Math.min(serverPrice.get(i-1)+knapsackArray[i][j-serverWeight.get(i-1)],
								knapsackArray[i-1][j]);
					else
						knapsackArray[i][j] = knapsackArray[i-1][j];
				}
			}
			i=serverWeightCount;
			j=tempcpus;
			totalCost = knapsackArray[serverWeightCount][tempcpus] * hours;
			totalCost = Math.round(totalCost * 100.0)/100.0;
			// if knapsack is valid and the price found is less than the user requested price
			if(knapsackArray[serverWeightCount][tempcpus] != Double.MAX_VALUE && 
					totalCost <= price)
			{
				dataRegionValid++;
				// From the 2D matrix how many count of each Server-type is being used is traced
				while (i > 0 && j > 0) {
					if (knapsackArray[i][j] == knapsackArray[i - 1][j])
						i--;
					else {
						j=j-serverWeight.get(i-1);
						serverCount.set(i-1,serverCount.get(i-1)+1);
					}
				}
				// LinkedHashMap Constructed - Total price as Value and Region name as key
				finalServerPriceDetails.put(dataRegionName,totalCost);
				for(i=0;i<serverWeightCount;i++)
				{
					if(serverCount.get(i)!=0)
					{
						for (String serverTypeMapString : Globals.serverTypeMap.keySet())
						{
							if(Globals.serverTypeMap.get(serverTypeMapString) == serverWeight.get(i))
							{
								finalInnerServerCountDetails.put(serverTypeMapString,serverCount.get(i));
								break;
							}
						}						
					}
				}
				// LinkedHashMap Constructed - Count of each Server-types included as Value and Region name as key
				finalServerCountDetails.put(dataRegionName, finalInnerServerCountDetails);
			}
	    }
		// If no Region values are valid, it means there is no possibility to service the request of the customer
		if(dataRegionValid>0)
		{
			PrintPriceDetails printDetails = new PrintPriceDetails();
			printDetails.printServerPriceDetails(finalServerCountDetails, finalServerPriceDetails);
			return 1;
		}
		else
		{
			return 0;
		}
	}
	/**
     * 
     *  Calculates the price and CPU counts based on the case chosen
     *  and prints the result if success. 
     * 
     * @param hours - number of hours servers are needed by the user
     * @param cpus - number of CPUs requested by the user
     * @param price - affordable price quoted by the user
     * @return - Success(1) or Failure(0)
     * 
     */
	public int get_costs(int hours,int cpus,double price)
	{
		int result = 0;
		if(price == 0.0)
		{
			result = totalCalculatorWithCPU(hours,cpus);
			if(result==0)
				return 0;
			if(result==1)
				return 1;
		}
		else if(cpus == 0)
		{
			result = totalCalculatorWithPrice(hours,price);
			if(result==0)
				return 0;
			if(result==1)
				return 1;
		}
		else
		{
			result = totalCalculatorWithCPUandPrice(hours,cpus,price);
			if (result==0)
				return 0;
			if (result==1)
				return 1;
		}
		return 0;
	}
}
