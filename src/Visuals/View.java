package Visuals;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import StarSystem.*;




public class View extends JFrame implements WindowListener,ActionListener {

	private StarSystemContainer listSystem = new StarSystemContainer();
	private List<String> listGov = new ArrayList<String>();
	private List<JCheckBox> listGovCheckBox = new ArrayList<JCheckBox>();
	private TableRowSorter<MainTableModel> mainTableSorter;
	private TableRowSorter<SupplyTableModel> supplyTableSorter;
	
	public View()
	{
		LoadMapFile();
		LoadPlayerFile();

		int winX = 960;
		int winY = 720;
		Dimension dimWindow = new Dimension(winX,winY);
	    
		
		//Window Configuration
		addWindowListener(this);
		setTitle("Endless Sky Cargo Profit Calculator");
		setSize(dimWindow);
		setMinimumSize(dimWindow);
		JTabbedPane tabbedPane = new JTabbedPane();
		this.add(tabbedPane);

		JTable mainTable = new JTable(new MainTableModel(listSystem,listGovCheckBox,listGov));
		mainTableSorter = new TableRowSorter<MainTableModel>((MainTableModel) mainTable.getModel());
		
		mainTable.setPreferredScrollableViewportSize(dimWindow);
		mainTable.setFillsViewportHeight(true);
		//mainTable.setAutoCreateRowSorter(true);
		mainTable.setRowSorter(mainTableSorter);
		JTable supplyTable = new JTable(new SupplyTableModel(listSystem,listGovCheckBox));
		supplyTableSorter = new TableRowSorter<SupplyTableModel>((SupplyTableModel) supplyTable.getModel());
		
		supplyTable.setPreferredScrollableViewportSize(dimWindow);
		supplyTable.setFillsViewportHeight(true);
		//mainTable.setAutoCreateRowSorter(true);
		supplyTable.setRowSorter(supplyTableSorter);

		JPanel pnlGovCheckBox = new JPanel();
		pnlGovCheckBox.setLayout(new GridLayout(4,0));
		for(int i = 0; i < listGov.size(); i++)
		{
			JCheckBox myJCB = new JCheckBox(listGov.get(i));
			myJCB.setName(String.format("CheckBox%d", i));
			myJCB.setSelected(true);
			pnlGovCheckBox.add(myJCB);
			listGovCheckBox.add(myJCB);
			
		}
		
		tabbedPane.addTab("Main Table", null, new JScrollPane(mainTable),
		        "All Entries, filtered by Governments tab");
		tabbedPane.addTab("Governments", null, new JScrollPane(pnlGovCheckBox),
		        "All Entries, filtered by Governments tab");
		tabbedPane.addTab("Supply", null, new JScrollPane(supplyTable),
		        "All Entries, filtered by Governments tab");

		setVisible(true);
	}
	
	protected void LoadMapFile()
	{
		try {
			File dir = new File(".");
			File fin = new File(dir.getCanonicalPath() + File.separator + "map.txt");
			Boolean SysFound;
			StarSystem CurrentSystem=new StarSystem("");
			FileInputStream fis = new FileInputStream(fin);

			// Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			
			while ((line = br.readLine()) != null) {
			 
				if(line.startsWith("\ttrade"))
				{
					line.trim();
					
					List<String> list = new ArrayList<String>();
					Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(line);
					while (m.find())
					    list.add(m.group(1).replace("\"", "")); // Add .replace("\"", "") to remove surrounding quotes.


					//System.out.println(list);
					
					String goods = list.get(1);
					int cost = Integer.parseInt(list.get(2));
					
					
					for(int i =0; i < StarSystem.CommoditiesList.length; i++)					
					{
						if(goods.equals(StarSystem.CommoditiesList[i]))
							CurrentSystem.setCost(i, cost);
					}

				}
				else if(line.startsWith("\tgovernment"))
				{
					line.trim();
					String gov = line.substring(12).replace("\"", "");

					if(!this.listGov.contains(gov))
						this.listGov.add(gov);
				    CurrentSystem.setGovernment(listGov.indexOf(gov));

				}
				else if(line.startsWith("system "))
				{
					SysFound = true;
					CurrentSystem = new StarSystem(line.substring(7).replace("\"", ""));
					this.listSystem.add(CurrentSystem);
				}
			}

			br.close();

		} catch (IOException e) {

			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(0);
		}
		int unhab = this.listGov.indexOf("Uninhabited");
		int top = this.listSystem.size();
		for(int i = 0; i < top;i++)
		{
			if(this.listSystem.get(i).getGovernment() == unhab)
			{
				this.listSystem.remove(i);
				i--;
				top--;
			}
		}
	}
	protected void LoadPlayerFile()
	{
		try {
			File dir = new File(".");
			File fin = new File(dir.getCanonicalPath() + File.separator + "savefile.txt");
			Boolean SysFound;
			FileInputStream fis = new FileInputStream(fin);
			// Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			boolean economy = false;
			int[] econTrans = new int[StarSystem.CommoditiesList.length];
			while ((line = br.readLine()) != null) {
			 
				if(line.startsWith("visited"))
				{
					economy = false;
					StarSystem mySystem=null;
					line.trim();
					List<String> list = new ArrayList<String>();
					Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(line);

					while (m.find())
					    list.add(m.group(1).replace("\"", "")); // Add .replace("\"", "") to remove surrounding quotes.
					
					String sysName = list.get(1);
					for(StarSystem s: this.listSystem)
						if(s.getName().equals(sysName))
							s.setVisited(true);
				}
				else if(line.startsWith("economy"))
				{
					economy = true;
				}
				else if(!line.startsWith("\t"))
				{
					economy = false;
				}
				else if(economy)
				{

					StarSystem mySystem=null;
					List<String> list = new ArrayList<String>();
					Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(line.trim());

					while (m.find())
					    list.add(m.group(1).replace("\"", "")); // Add .replace("\"", "") to remove surrounding quotes.
					
					//System.out.printf("economy %s\n",list.get(0).trim());
					if(line.startsWith("\tsystem"))
					{
						
						for(int i = 1; i < list.size(); i++)
						{
							for(int j = 0; j < StarSystem.CommoditiesList.length;j++)
							{
								if(StarSystem.CommoditiesList[j].equalsIgnoreCase(list.get(i)))
										econTrans[i-1] = j;
							}
						}
					}
					else
					{
						//System.out.printf("Cost %s\n",list);
						String sysName = list.get(0).trim().replace("\"", "");
						for(StarSystem s :this.listSystem)
							if(s.getName().equals(sysName))
							{
								for(int i = 1; i < list.size(); i++)
								{
									s.setSupply(econTrans[i-1],Integer.parseInt(list.get(i)));
								}
							}
					}
					
				}
			}

			br.close();

		} catch (IOException e) {

			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}


	public static void main(String[] args) {

		new View();
	}
	
	@Override public void actionPerformed(ActionEvent arg0) 
	{	
	}
		
		
	class MainTableModel extends AbstractTableModel {
		
		private StarSystemContainer listSystem;
		private List<JCheckBox> boxes;
		private List<String> govs;
		public MainTableModel(StarSystemContainer listOfSystems, List<JCheckBox> boxes, List<String> govs)
		{
			this.listSystem = listOfSystems;
			this.boxes = boxes;
			this.govs = govs;
		}

        public int getColumnCount() {
            return StarSystem.CommoditiesList.length+3;
        }
 
        public int getRowCount() {
            return this.listSystem.size();
        }
 
        public String getColumnName(int col) {
            switch(col)
            {
	            case 0: return "Name";
	            case 1: return "Government";
	            case 2: return "Visited";
	            default:return StarSystem.CommoditiesList[col-3];
            }
        }
 
        public Object getValueAt(int row, int col) {

            switch(col)
            {
	            case 0: return this.listSystem.get(row).getName();
	            case 1: return this.govs.get(this.listSystem.get(row).getGovernment());
	            case 2: return this.listSystem.get(row).wasVisited();
	            default:return this.listSystem.get(row).getCost(col-3);
            }

        }
 
        @SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
	}
	class SupplyTableModel extends AbstractTableModel {
		
		private StarSystemContainer listSystem;
		List<JCheckBox> boxes;
		
		public SupplyTableModel( StarSystemContainer listSystem,List<JCheckBox> boxes)
		{
			this.listSystem = listSystem;
			this.boxes = boxes;
		}

        public int getColumnCount() {
            return StarSystem.CommoditiesList.length+1;
        }
 
        public int getRowCount() {
            return this.listSystem.size();
        }
 
        public String getColumnName(int col) {
            switch(col)
            {
	            case 0: return "Name";

	            default:return StarSystem.CommoditiesList[col-1];
            }
        }
 
        public Object getValueAt(int row, int col) {

            switch(col)
            {
	            case 0: return this.listSystem.get(row).getName();
	            default:return this.listSystem.get(row).getSupply(col-1);
            }

        }
 
        @SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
	}



	@Override public void windowClosing(WindowEvent ev)     { System.exit(0);}
	@Override public void windowActivated(WindowEvent ev) 	{ }
	@Override public void windowDeactivated(WindowEvent ev)	{ }
	@Override public void windowDeiconified(WindowEvent ev) { }
	@Override public void windowIconified(WindowEvent ev) 	{ }
	@Override public void windowOpened(WindowEvent ev) 		{ }
	@Override public void windowClosed(WindowEvent ev) 		{ }
}
