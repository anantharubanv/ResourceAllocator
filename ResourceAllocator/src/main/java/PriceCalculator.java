package main.java;
import java.util.*;

public class PriceCalculator {
	private int totalCalculatorWithCPU(int hours, int cpus)
	{
		LinkedHashMap <String, LinkedHashMap<String, Integer>> finalServerCountDetails = new LinkedHashMap <String, LinkedHashMap<String, Integer>>();
		LinkedHashMap <String, Integer> finalInnerServerCountDetails;
		LinkedHashMap <String, Double> finalServerPriceDetails = new LinkedHashMap <String, Double>();
		int validRequest = 0;
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
			if(cpus%(serverWeight.firstElement())!=0)
			{
				tempcpus = tempcpus - (cpus%(serverWeight.firstElement())) + (serverWeight.firstElement());
			}
			int serverWeightCount = serverWeight.size();
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
			if (dataRegionValid > 0)
			{
				validRequest++;
				for(i=0;i<serverWeightCount;i++)
				{
					totalCost = totalCost + (serverPrice.get(i)*serverCount.get(i));
				}
				totalCost = totalCost * hours;
				totalCost = Math.round(totalCost * 100.0)/100.0;
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
				finalServerCountDetails.put(dataRegionName, finalInnerServerCountDetails);
			}
		}
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
	private int totalCalculatorWithPrice(int hours, double price)
	{
		LinkedHashMap <String, LinkedHashMap<String, Integer>> finalServerCountDetails = new LinkedHashMap <String, LinkedHashMap<String, Integer>>();
		LinkedHashMap <String, Integer> finalInnerServerCountDetails;
		LinkedHashMap <String, Double> finalServerPriceDetails = new LinkedHashMap <String, Double>();
		int dataRegionValid = 0;
		for (String dataRegionName : Globals.instanceMap.keySet())  
	    { 
			finalInnerServerCountDetails = new LinkedHashMap<String, Integer>();
			int tempprice = (int)((price*1000.0)/hours);
			Vector<Integer> serverWeight = new Vector<Integer>();
			Vector<Integer> serverPrice = new Vector<Integer>();
			Vector<Integer> serverCount = new Vector<Integer>();
			int i=0,j=0;
			double totalCost = 0.0;
			LinkedHashMap<String,Double> ServerDetails = Globals.instanceMap.get(dataRegionName);
			for (String ServerTypeName : ServerDetails.keySet()){
				serverWeight.add(Globals.serverTypeMap.get(ServerTypeName));
				serverPrice.add((int)(ServerDetails.get(ServerTypeName)*1000));
				serverCount.add(0);
			}
			int serverWeightCount = serverWeight.size();
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
				totalCost = totalCost * hours / 1000.0;
				totalCost = Math.round(totalCost * 100.0)/100.0;
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
				finalServerCountDetails.put(dataRegionName, finalInnerServerCountDetails);
			}
	    }
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
			if(cpus%(serverWeight.firstElement())!=0)
			{
				tempcpus = tempcpus - (cpus%(serverWeight.firstElement())) + (serverWeight.firstElement());
			}
			int serverWeightCount = serverWeight.size();
			double knapsackArray[][]=new double[serverWeightCount+1][tempcpus+1];
			
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
			if(knapsackArray[serverWeightCount][tempcpus] != Double.MAX_VALUE && 
					totalCost <= price)
			{
				dataRegionValid++;
				while (i > 0 && j > 0) {
					if (knapsackArray[i][j] == knapsackArray[i - 1][j])
						i--;
					else {
						j=j-serverWeight.get(i-1);
						serverCount.set(i-1,serverCount.get(i-1)+1);
					}
				}
			
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
				finalServerCountDetails.put(dataRegionName, finalInnerServerCountDetails);
			}
	    }
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