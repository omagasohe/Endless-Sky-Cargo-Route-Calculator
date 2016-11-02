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

import StarSystem.StarSystem;




public class View extends JFrame implements WindowListener,ActionListener {

	static List<StarSystem> listSystem = new ArrayList<StarSystem>();
	static List<String> listGov = new ArrayList<String>();
	private List<JCheckBox> listGovCheckBox = new ArrayList<JCheckBox>();
	private TableRowSorter<MainTableModel> mainTableSorter;
	
	public View()
	{
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

		JTable mainTable = new JTable(new MainTableModel(null));
		mainTableSorter = new TableRowSorter<MainTableModel>((MainTableModel) mainTable.getModel());
		
		mainTable.setPreferredScrollableViewportSize(dimWindow);
		mainTable.setFillsViewportHeight(true);
		//mainTable.setAutoCreateRowSorter(true);
		mainTable.setRowSorter(mainTableSorter);
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
		
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
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

					if(!listGov.contains(gov))
						listGov.add(gov);
				    CurrentSystem.Government = listGov.indexOf(gov);

				}
				else if(line.startsWith("system "))
				{
					SysFound = true;
					CurrentSystem = new StarSystem(line.substring(7).replace("\"", ""));
					listSystem.add(CurrentSystem);
				}
			}

			br.close();

		} catch (IOException e) {

			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(0);
		}
		int unhab = listGov.indexOf("Uninhabited");
		int top = listSystem.size();
		for(int i = 0; i < top;i++)
		{
			if(listSystem.get(i).Government == unhab)
			{
				listSystem.remove(i);
				i--;
				top--;
			}
		}
		
		for(StarSystem s:listSystem)
		{
			System.out.printf("%-20s %-20s", s.Name, s.Government);
			for(int i =0; i < StarSystem.CommoditiesList.length; i++)					
				System.out.printf(" %8d",s.Costs[i]);
			System.out.println("");
		}
		System.out.printf("Total systems: %d",listSystem.size());
		
		for(String s:listGov)
			System.out.println(s);
		System.out.println(listGov.size());
		
		
		
		
		new View();
	}
	
	class MainTableModel extends AbstractTableModel {
		

		List<JCheckBox> boxes;
		
		public MainTableModel(List<JCheckBox> boxes)
		{

			this.boxes = boxes;
		}

        public int getColumnCount() {
            return StarSystem.CommoditiesList.length+2;
        }
 
        public int getRowCount() {
            return View.listSystem.size();
        }
 
        public String getColumnName(int col) {
            switch(col)
            {
	            case 0: return "Name";
	            case 1: return "Government";
	            default:return StarSystem.CommoditiesList[col-2];
            }
        }
 
        public Object getValueAt(int row, int col) {

            switch(col)
            {
	            case 0: return View.listSystem.get(row).Name;
	            case 1: return View.listGov.get(View.listSystem.get(row).Government);
	            default:return View.listSystem.get(row).Costs[col-2];
            }

        }
 
        @SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override public void windowClosing(WindowEvent ev)     { System.exit(0);}
	@Override public void windowActivated(WindowEvent ev) 	{ }
	@Override public void windowDeactivated(WindowEvent ev)	{ }
	@Override public void windowDeiconified(WindowEvent ev) { }
	@Override public void windowIconified(WindowEvent ev) 	{ }
	@Override public void windowOpened(WindowEvent ev) 		{ }
	@Override public void windowClosed(WindowEvent ev) 		{ }
}
