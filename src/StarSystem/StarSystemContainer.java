package StarSystem;

import java.util.*;

@SuppressWarnings("serial")
public class StarSystemContainer extends ArrayList<StarSystem> {

	public StarSystemContainer()
	{
		super();
	}
	
	public StarSystem findByName(String name)
	{
		for(int i = 0; i <this.size(); i ++)
			if(this.get(i).equals(name))
				return this.get(i);
		return null;
	}
	
	
}
