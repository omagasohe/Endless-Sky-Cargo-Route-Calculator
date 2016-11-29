package Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataFile 
{
	private String path;
	private DataNode root = new DataNode();
	
	public DataFile(String file)
	{
		this.path = file;
		load();
	}
	public void load()
	{
		try {
			int lastIndent = 0, thisIndent = 0;
			DataNode LastNode = root;
			File dir = new File(".");
			File fin = new File(dir.getCanonicalPath() + File.separator + "example.txt");
			FileInputStream fis = new FileInputStream(fin);
			

			// Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			Stack<Integer> stackw = new Stack<Integer>();
			
			while ((line = br.readLine()) != null) 
			{
			 	//Indent Level
				int t = line.length();

				line = line.trim();
				thisIndent = t - line.length();
				
				//Blank line.
				if(line.length() == 0)
					continue;
				
				if(line.startsWith("#"))
					continue;
				
				if(thisIndent == 0 )
				{
					LastNode = root;
					stackw.clear();
				} 
				else if( thisIndent!= 0 && thisIndent <= stackw.peek())
				{
					while(thisIndent <= stackw.peek())
					{
						stackw.pop();
						LastNode = LastNode.getParent();
					}
				}

				
				if(LastNode == null)
				{
					LastNode = root;
				}
				
				DataNode node = new DataNode(LastNode);
				stackw.push(thisIndent);
				LastNode = node;
				
				Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(line);
				while (m.find())
				    node.addToken(m.group(1).replace("\"", "")); // Add .replace("\"", "") to remove surrounding quotes.

			}
			
			br.close();

		} catch (IOException e) {

			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(0);
		}

	}
	public ListIterator<DataNode> GetNodes()
	{
		return root.begin();
	}
}
