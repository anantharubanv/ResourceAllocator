package main.java;
/**
*
* The Globals holds all the global static variables needed
* in the application and a function to initialize all the
* available Instance values
*
* @author  Anantharuban
* @version 1.4
* @since   2020-09-18 
* 
*/
/*Branch code learn*/

import java.util.*;

public class Globals {
	public static LinkedHashMap <String, LinkedHashMap<String, Double>> instanceMap;
	public static LinkedHashMap<String, Double> innerInstanceMap;
	public static LinkedHashMap<String, Integer> serverTypeMap;
	/**
     * Initializes all the Available Instance price values based on the 
     * region where the data centers reside. 
     * 
     */
	public static void initializeInstances()
	{
		instanceMap = new LinkedHashMap <String, LinkedHashMap<String, Double>>();
		innerInstanceMap = new LinkedHashMap<String, Double>();
		innerInstanceMap.put("large",0.12);
		innerInstanceMap.put("xlarge",0.23);
		innerInstanceMap.put("2xlarge",0.45);
		innerInstanceMap.put("4xlarge",0.774);
		innerInstanceMap.put("8xlarge",1.4);
		innerInstanceMap.put("10xlarge",2.82);
		instanceMap.put("us-east",innerInstanceMap);
		innerInstanceMap = new LinkedHashMap<String, Double>();
		innerInstanceMap.put("large",0.14);
		innerInstanceMap.put("2xlarge",0.413);
		innerInstanceMap.put("4xlarge",0.89);
		innerInstanceMap.put("8xlarge",1.3);
		innerInstanceMap.put("10xlarge",2.97);
		instanceMap.put("us-west",innerInstanceMap);
		innerInstanceMap = new LinkedHashMap<String, Double>();
		innerInstanceMap.put("large",0.11);
		innerInstanceMap.put("xlarge",0.20);
		innerInstanceMap.put("4xlarge",0.67);
		innerInstanceMap.put("8xlarge",1.18);
		instanceMap.put("asia",innerInstanceMap);
		serverTypeMap = new LinkedHashMap<String, Integer> ();
		serverTypeMap.put("large",1);
		serverTypeMap.put("xlarge",2);
		serverTypeMap.put("2xlarge",4);
		serverTypeMap.put("4xlarge",8);
		serverTypeMap.put("8xlarge",16);
		serverTypeMap.put("10xlarge",32);	
	}
}