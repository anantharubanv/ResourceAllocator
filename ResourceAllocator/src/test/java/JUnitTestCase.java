package test.java;


import static org.junit.Assert.*;

import main.java.Globals;
import main.java.PriceCalculator;

import org.junit.Test;
public class JUnitTestCase {

	@Test
	public void test() {
		Globals.initializeInstances();
		PriceCalculator object = new PriceCalculator();
		assertEquals(1,object.get_costs(2, 20, 0.0));
		assertEquals(1,object.get_costs(6,0,30.0));
		assertEquals(1,object.get_costs(3,120,40.0));
		assertEquals(0,object.get_costs(2,0,0.1));
		assertEquals(0,object.get_costs(3,120,4.0));
	}

}
