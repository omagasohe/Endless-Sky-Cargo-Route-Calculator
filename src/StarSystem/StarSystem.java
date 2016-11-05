package StarSystem;

public class StarSystem {
	public static String[] CommoditiesList = {"Clothing","Electronics","Equipment","Food","Heavy Metals","Industrial","Luxury Goods","Medical","Metal","Plastic"};
	
	private String Name;
	private int Government;
	
	private int[] costs = new int[CommoditiesList.length];
	private int[] supply = new int[CommoditiesList.length];
	private boolean visited;
	
	
	public StarSystem(String name)
	{
		this.Name = name;
	}
	
	public void setCost(int idx, int cost)
	{
		this.costs[idx] = cost;
	}
	
	public int getCost(int idx)
	{
		return this.costs[idx];
	}
	public int setSupply(int idx, int qty) {
		return this.supply[idx] = qty;
	}

	public int getSupply(int idx) {
		return this.supply[idx];
	}
	public boolean wasVisited()
	{
		return this.visited;
	}
	public void setVisited(boolean status)
	{
		this.visited = status;
	}

	public String getName() {
		return this.Name;
	}

	public int getGovernment() {
		return Government;
	}

	public void setGovernment(int government) {
		Government = government;
	}

}
