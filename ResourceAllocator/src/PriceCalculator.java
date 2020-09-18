
public class PriceCalculator {
	private void totalCalculatorWithCPU(int hours, int cpus)
	{
		System.out.println(hours);
		System.out.println(cpus);
	}
	private void totalCalculatorWithPrice(int hours, double price)
	{
		System.out.println(hours);
		System.out.println(price);
	}
	private void totalCalculatorWithCPUandPrice(int hours, int cpus, double price)
	{
		System.out.println(hours);
		System.out.println(cpus);
		System.out.println(price);
	}
	public void get_costs(int hours,int cpus,double price)
	{
		if(price == 0.0)
		{
			totalCalculatorWithCPU(hours,cpus);
		}
		else if(cpus == 0)
		{
			totalCalculatorWithPrice(hours,price);
		}
		else
		{
			totalCalculatorWithCPUandPrice(hours,cpus,price);
		}
	}
}
