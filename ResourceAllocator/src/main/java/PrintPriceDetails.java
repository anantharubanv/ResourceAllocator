/**
*
* The PrintPriceDetails holds the function to print
* the final values to the users.
*
* @author  Anantharuban
* @version 1.4
* @since   2020-09-19
*  
*/

package main.java;
import java.util.*;
import java.util.Map.Entry;

public class PrintPriceDetails{
	/**
     * Sorts the result based on total value and prints the final result in the given format.
     * ( Here the result is printed but with the LinkedHashMap Arguments
     * JSON could be generated and passed to other services if needed ).
     * 
     * @param finalServerCountDetails - holds count of servers in each server type as Value and Region name as Key
     * @param finalServerPriceDetails - holds total cost of servers as Value and Region name as Key
     * 
     */
	public void printServerPriceDetails(LinkedHashMap <String, LinkedHashMap<String, Integer>> finalServerCountDetails, 
							LinkedHashMap <String, Double> finalServerPriceDetails)
	{
		LinkedHashMap<String, Integer> innerFinalServerCountDetails;
		List<Map.Entry<String, Double>> priceEntries = new ArrayList<Map.Entry<String, Double>>(finalServerPriceDetails.entrySet());
		// LinkedHashMap is sorted based on the total_cost generated for each Region
		Collections.sort(priceEntries, new Comparator<Map.Entry<String, Double>>(){
			public int compare(Entry<String,Double>priceEntry1, Entry<String, Double>priceEntry2){
				if (priceEntry1.getValue() < priceEntry2.getValue()) return -1;
				if (priceEntry1.getValue() > priceEntry2.getValue()) return 1;
				return 0;
			}
		});
		finalServerPriceDetails.clear();
		int totalDataregions = 0, totalServerTypes=0;
		int totalDataregionsCount = 0,totalServerTypesCount=0;
		for(Map.Entry<String, Double> entry : priceEntries)
		{
			finalServerPriceDetails.put(entry.getKey(),entry.getValue());
			totalDataregions++;
		}
		System.out.println("[");
		for (String dataRegionName : finalServerPriceDetails.keySet())  
	    {  
			System.out.println("\t{");
	    	System.out.println("\t\t\"region\" : \""+dataRegionName+"\",");
	        System.out.println("\t\t\"total_cost\" : \"$"+finalServerPriceDetails.get(dataRegionName)+"\",");
	        System.out.println("\t\t\"servers\" : [");
	        innerFinalServerCountDetails = new LinkedHashMap<String, Integer>();
	        innerFinalServerCountDetails = finalServerCountDetails.get(dataRegionName);
	        for(String ServerTypeName : innerFinalServerCountDetails.keySet())
	        {
	        	totalServerTypes++;
	        }
	        for(String ServerTypeName : innerFinalServerCountDetails.keySet())
	        {
	        	totalServerTypesCount++;
	        	if(totalServerTypesCount == totalServerTypes)
	        	{
	        		System.out.println("\t\t\t(\""+ServerTypeName+"\","+innerFinalServerCountDetails.get(ServerTypeName)+")");
	        	}
	        	else
	        	{
	        		System.out.println("\t\t\t(\""+ServerTypeName+"\","+innerFinalServerCountDetails.get(ServerTypeName)+"),");
	        	}
	        }
	        System.out.println("\t\t]");
	        totalDataregionsCount++;
	        if(totalDataregionsCount == totalDataregions)
	        {
	        	System.out.println("\t}");
	        }
	        else
	        {
	        	System.out.println("\t},");
	        }
	    }
        System.out.println("]");
	}
}