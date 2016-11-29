package Node;

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;

public class DataNode 
{
	private DataNode parent;
	private	List<DataNode> children = new ArrayList<DataNode>();
	private List<String> tokens = new ArrayList<String>();
	private List<String> comments = new ArrayList<String>();
	
	public DataNode()
	{
		this.parent = null;
	}
	/**
	 * Adds this node to parent.children, and sets this.parent.
	 * @param parent
	 */
	public DataNode(DataNode parent)
	{
		this.parent = parent;
		parent.addChild(this);
	}
	public boolean hasChildren()
	{
		return !children.isEmpty();
	}
	
	public int size()
	{
		return tokens.size();
	}
	
	public Double getValue(int index)
	{
		return Double.parseDouble(getToken(index));
	}
	
	public String getToken(int index)
	{
		return tokens.get(index);
	}
	
	public boolean addToken(String str)
	{
		return tokens.add(str);
	}
	public boolean addComments(String str)
	{
		return comments.add(str);
	}
	public boolean addValue(Double number)
	{
		return tokens.add(number.toString());
	}
	
	public ListIterator<DataNode> begin()
	{
		return children.listIterator();
	}
	
	public boolean addChild(DataNode child)
	{
		return children.add(child);
	}

	public DataNode getParent() {
		return parent;
	}


}
