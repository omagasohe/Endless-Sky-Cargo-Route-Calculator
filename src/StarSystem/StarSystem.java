package StarSystem;

public class StarSystem {
	public static String[] CommoditiesList = {"Clothing","Electronics","Equipment","Food","Heavy Metals","Industrial","Luxury Goods","Medical","Metal","Plastic"};
	
	public String Name;
	public int Government;
	
	public int[] Costs = new int[CommoditiesList.length];
	
	public StarSystem(String name)
	{
		this.Name = name;
	}
	public void setCost(int idx, int cost)
	{
		this.Costs[idx] = cost;
	}
}
