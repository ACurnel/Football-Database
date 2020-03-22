//package localGUI;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;


public class theGUI extends JFrame {

	private JPanel contentPane;
	private JTable hfaTable;
	private JTable vcTable;
	private String joinCmd = "select ";
	private ArrayList<String> SelectedColumnList = new ArrayList<String>(); // List holding desired column names
	private ArrayList<String> colToJoin = new ArrayList<String>();
	private String YearsToQuery = "";
	private String input_numEnt;
	private Statement stmt = null;
	private String sqlStatement = "";
	private String finalout = "";
	private ArrayList<String> columnsToPrint = new ArrayList<String>();
	private String columnsSelected = ""; // formatted string of columns to input
	private Connection conn = null;
	private String sqlStatementConditional = "";
	private String entity1;
	private String entity2;
	private String sjCmd;
	private ArrayList<String> SelectedColumnListE1 = new ArrayList<String>();
	private ArrayList<String> SelectedColumnListE2 = new ArrayList<String>();
	private ArrayList<String> columnlist = new ArrayList<String>();
	private ArrayList<String> columnlistEntity1 = new ArrayList<String>();
	private ArrayList<String> savedSelectedColumnListE1 = new ArrayList<String>();
	private ArrayList<String> savedSelectedColumnListE2 = new ArrayList<String>();
	private String q3sqlStatement = "";
	private String turfsqlStatement = "";

	/**
	 * Launch the application.
	 */

	
	public static void main(String[] args) throws FileNotFoundException{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					theGUI frame = new theGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}

	/**
	 * Create the frame.
	 */
	String input = "";
	public theGUI() {
		setTitle("TEAM NAND DATABASE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 749, 459);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		//******************************* HOME *******************************\\
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(6, 6, 737, 425);
		contentPane.add(tabbedPane);
		
		JPanel homePanel = new JPanel();
		homePanel.setBackground(UIManager.getColor("CheckBox.background"));
		tabbedPane.addTab("Home", null, homePanel, null);
		homePanel.setLayout(null);
		
		JLabel hometxt1 = new JLabel("Welcome to the Team NAND Football Statistics Database Tool. \n\n");
		hometxt1.setBounds(152, 125, 483, 16);
		homePanel.add(hometxt1);
		
		JLabel hometxt2 = new JLabel("Here you will be able to search college football stats from 2005-2013.\n\n");
		hometxt2.setBounds(128, 153, 507, 16);
		homePanel.add(hometxt2);
		
		JLabel hometxt4 = new JLabel("Navigate through the tool by using the above tabs. Enjoy!");
		hometxt4.setBounds(173, 181, 462, 16);
		homePanel.add(hometxt4);
		
		JLabel hometxt3 = new JLabel("Created by: David Chapa, Alex Christian, Avery Curnel and Skyelar Head");
		hometxt3.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
		hometxt3.setBounds(189, 327, 446, 16);
		homePanel.add(hometxt3);
		
		
		
		//******************************* GENERAL ******************************* \\
		
		JPanel generalPanel = new JPanel();
		tabbedPane.addTab("General", null, generalPanel, null);
		generalPanel.setLayout(null);
		
		JLabel generalLabel1 = new JLabel("Here you will be able to make a general query. Start at the top and move your way down the list.");
		generalLabel1.setBounds(55, 6, 603, 16);
		generalPanel.add(generalLabel1);
		
		JLabel generalLabel2 = new JLabel("Do NOT go back up once you have already moved down. If you do not select anything, defaults are in place. ");
		generalLabel2.setBounds(6, 24, 697, 16);
		generalPanel.add(generalLabel2);
		
		
		//entity code goes here
		JButton entityBtn = new JButton("Choose Entity");
		entityBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] choices = { "Players", "Teams", "Seasons", "Player_Records", "Games", "Stadiums", "Positions", "Offensive_Records", "Defensive_Records", "Conferences", "Statistics", "Drives" };
				input = (String) JOptionPane.showInputDialog(null, "Choose now...", "Selcect entity",
							JOptionPane.QUESTION_MESSAGE, null, // Use
																// default
																// icon
							choices, // Array of choices
							choices[0]); // Initial choice
				// System.out.println(input);
			}
		});

		entityBtn.setBounds(275, 64, 150, 29);
		generalPanel.add(entityBtn);
		
		
		//column code goes here
		JButton columnsBtn = new JButton("Choose Columns");
		columnsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
											// Building the connection
		//conn = null;
				dbSetup my = new dbSetup();
				try {

					Class.forName("org.postgresql.Driver");
					conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/team_nand", my.user,
						my.pswd);
				} catch (Exception f) {
					f.printStackTrace();
					System.err.println(f.getClass().getName() + ": " + f.getMessage());
					System.exit(0);
		} // end try catch	
				stmt = conn.createStatement();

					// Queries 0 rows to parse columns
				sqlStatement = "Select * from " + input + " limit 0";
				ResultSet columns = stmt.executeQuery(sqlStatement);
		      // choices = { "players", "teams", "seasons", "player_records", "games", "Stadiums", "Positions", "Offensive_Records", "Defensive_Records", "Conferences", "Statistics", "Drives" };

				ResultSetMetaData columnstuff = columns.getMetaData();
				int colcnt = columnstuff.getColumnCount();
					ArrayList<String> columnlist = new ArrayList<String>(); // holds columns in input entity
					for (int i = 1; i <= colcnt; i++) {
						columnlist.add(columnstuff.getColumnName(i));
					}
					// Creates check box options for each column by adding the new box and column name
					ArrayList<JCheckBox> ColumnBoxes = new ArrayList<JCheckBox>(); // holds check boxes
					Object[] columncontent = new Object[columnlist.size()];
					for (int i = 0; i < columnlist.size(); i++) {
						ColumnBoxes.add(new JCheckBox(columnlist.get(i)));
						columncontent[i] = ColumnBoxes.get(i);
					}
					
					JPanel cols = new JPanel(new BorderLayout(4,4));
					JPanel checkpanel = new JPanel(new GridLayout(0,6));
					cols.add(checkpanel, BorderLayout.CENTER);
					for (int i = 0; i < ColumnBoxes.size(); i++) {
						checkpanel.add(ColumnBoxes.get(i));
						
					}
					// Displays dialog box showing column check boxes, then creates list of desired columns
					JOptionPane.showConfirmDialog(null, cols, "Select columns", JOptionPane.DEFAULT_OPTION);
					ArrayList<Boolean> columnchecks = new ArrayList<Boolean>(); // array of true/false values corresponding to desired columns
					for (int i = 0; i < columnlist.size(); i++) {
						columnchecks.add(ColumnBoxes.get(i).isSelected());
					}

					
					for (int i = 0; i < columnlist.size(); i++) {
						if (columnchecks.get(i))
							SelectedColumnList.add(columnlist.get(i));
					}
					//System.out.println(SelectedColumnList.size()); // used for debugging
					
					for (int i = 0; i < SelectedColumnList.size(); i++) {
						if (i!=SelectedColumnList.size()-1)
							columnsSelected = columnsSelected + SelectedColumnList.get(i) + ",";
						else
							columnsSelected = columnsSelected + SelectedColumnList.get(i) + " ";
					}
					if(SelectedColumnList.size()==0){
						for(String i : columnlist){
							SelectedColumnList.add(i);
						}
						columnsSelected = "*";

					}

				}catch (Exception colume){
					System.out.println("Error");
				}
			}
		});
		columnsBtn.setBounds(275, 105, 150, 29);
		generalPanel.add(columnsBtn);
		
		
		//join code goes here (rename variable)
		JButton joinBtn = new JButton("Rename Variable");
		joinBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					ResultSet resultNumCol = stmt.executeQuery("SELECT * FROM "+input+" limit 1");
					ResultSetMetaData rsmd = resultNumCol.getMetaData();
					ArrayList<String> columnsJoin = new ArrayList<String>();
					for(int i = 1; i <= rsmd.getColumnCount(); i++){
						columnsJoin.add(rsmd.getColumnName(i));
					}
		        
		      // joinBoxes.setSize(150, 1000);
		      // joinBoxes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					ArrayList<JCheckBox> joinCheck = new ArrayList<JCheckBox>();
					Object[] columncontentJoin = new Object[2*columnsJoin.size()];
					ArrayList<String> colDisplayed = new ArrayList<String>();
					int countT = 0;

					for(String i : columnsJoin){
		        // System.out.println("i="+i);
						if( !i.equals("player_code") && input.equals("Players"))
							if(i.contains("code") ){
								JCheckBox Box = new JCheckBox(i); 
								joinCheck.add(Box);
								columncontentJoin[(2*countT)+1] = joinCheck.get(countT++);
								colDisplayed.add(i);
							}

							if(!i.equals("team_code") && input.equals("Teams"))
								if(i.contains("code") ){
									JCheckBox Box = new JCheckBox(i); 
									joinCheck.add(Box);
									columncontentJoin[(2*countT)+1] = joinCheck.get(countT++);
									colDisplayed.add(i);
								}

								if((!input.equals("Teams")) && (!input.equals("Players")))
									if(i.contains("code") ){
										JCheckBox Box = new JCheckBox(i); 
										joinCheck.add(Box);
										columncontentJoin[(2*countT)+1] = joinCheck.get(countT++);
										colDisplayed.add(i);
									}


								}

								JOptionPane.showConfirmDialog(null, columncontentJoin, "Select columns to join", JOptionPane.DEFAULT_OPTION);

		      //FIXWHENMERGE: columns to print should be set to the selected entities from Alex's code
								columnsToPrint = SelectedColumnList;
		      // columnsToPrint.add("team_code");
		      // columnsToPrint.add("drive_number");
		      // columnsToPrint.add("start_period");
		      // columnsToPrint.add("yards");

		      // colToJoin = new ArrayList<String>();
								for(int i = 0; i < joinCheck.size(); i++){
									if(joinCheck.get(i).isSelected())
										colToJoin.add(colDisplayed.get(i));
								}


		      //FIXWHENMERGE: remove limits and ensure they are taken care of elsewhere
		      //FIXWHENMERGE: ensure input is current entity selected 
								input = input.toLowerCase();
								for(String i: colToJoin){

									if(i.equals("team_code") ){
										joinCmd += ("teams.name, ");
										for(String j: columnsToPrint){
											if(colToJoin.contains(j) == false){
												joinCmd += (input+"."+j);
												if (columnsToPrint.indexOf(j) != columnsToPrint.size()-1)
													joinCmd += ", ";
												else
													joinCmd += " ";
											}
										}
										joinCmd += (" from "+input+" inner join ");
										joinCmd += ("teams on " + input +".team_code = teams.team_code");


									}
									else if(i.equals("player_code")){
										joinCmd += ("players.first_name, players.last_name, ");
										for(String j: columnsToPrint){
											if(colToJoin.contains(j) == false){
												joinCmd += (input+"."+j);
												if (columnsToPrint.indexOf(j) != columnsToPrint.size()-1)
													joinCmd += ", ";
												else
													joinCmd += " ";
											}
										}
										joinCmd += (" from "+input+" inner join ");
										joinCmd += ("players on "+ input +".player_code = players.player_code");
									}
								}

				}catch (Exception joinE){
					System.out.println("Error 404: 0x1df9999032");
				}
		
			}
		});
		joinBtn.setBounds(275, 146, 150, 29);
		generalPanel.add(joinBtn);
		
		
		//years code goes here
		JButton yearsBtn = new JButton("Choose Years");
		yearsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					//Creating an array list of the years
					ArrayList<String> Years = new ArrayList<String>();
					for(int i=2005; i <= 2013; i++){
						Years.add(Integer.toString(i));
					}

		      //Creating all of the checkboxes
					ArrayList<JCheckBox> Boxes = new ArrayList<JCheckBox>();
					ArrayList<Boolean> YearBoxValues = new ArrayList<Boolean>();
					Object[] YearsMsgContent = new Object[Years.size()];
					for (int i = 0; i < Years.size(); i++) {
						Boxes.add(new JCheckBox(Years.get(i)));
						YearsMsgContent[i] = Boxes.get(i);
					}

		      //Set a boolean for each box (checked or not checked)
					JOptionPane.showConfirmDialog(null, YearsMsgContent, "Year Selection", JOptionPane.DEFAULT_OPTION); 
					for (int i = 0; i < Years.size(); i++){
						YearBoxValues.add((Boxes.get(i)).isSelected());
					}

		      //All years that were selected are placed in an array list
					ArrayList<String> YearsSelected = new ArrayList<String>();
					for(int i = 0; i < Years.size(); i++){
						if(YearBoxValues.get(i) == true){
							YearsSelected.add(Years.get(i));
						}
					}

		      //Make a string of tthe years that will be queried

					if(YearsSelected.size() > 0){
						YearsToQuery = " where ";
						for(int i = 0; i < (YearsSelected.size()-1); i++){
							YearsToQuery += (input+".year="+YearsSelected.get(i) + " or ");
						}
						YearsToQuery += (input+".year="+YearsSelected.get(YearsSelected.size()-1));
					}

					/****************************************************************************/
					joinCmd += YearsToQuery;

				} catch (Exception yearsE){

				}
	
			}
		});
		yearsBtn.setBounds(275, 187, 150, 29);
		generalPanel.add(yearsBtn);
		
		
		//conditional code goes here
		JButton conditionalBtn = new JButton("Choose Conditional");
		conditionalBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					SelectedColumnList.add("done");
					Object[] SelectedColumnListArray = SelectedColumnList.toArray();
					if(YearsToQuery.equals("")){
						joinCmd += " where ";
						// sqlStatement += " where ";
					}
					String conValue, conCol;

					while(true){
					conCol = (String) JOptionPane.showInputDialog(null, "Select column for conditional value", "",
							JOptionPane.QUESTION_MESSAGE, null, // Use
																// default
																// icon
							SelectedColumnListArray, // Array of choices
							SelectedColumnListArray[0]); // Initial choice
					if(conCol.equals("done")){
						SelectedColumnList.remove(SelectedColumnList.size()-1);
						break;
					}
					conValue = JOptionPane.showInputDialog("Enter conditional value ex: (operator)value, operators: =, <=, >=:");
					if(YearsToQuery.equals("")){
						joinCmd += (" "+input+"."+conCol+conValue+" ");
						sqlStatementConditional += (" "+input+"."+conCol+conValue+" ");
					}
					else{
						joinCmd += (" and "+input+"."+conCol+conValue+" ");
						sqlStatementConditional += (" and "+input+"."+conCol+conValue+" ");
					}
					
					
				}

				} catch (Exception conditionalE){
					System.out.println("Error 404: 0x2feab211");
				}
		
			}
		});
		conditionalBtn.setBounds(275, 228, 150, 29);
		generalPanel.add(conditionalBtn);
		
				
		//limit code goes here
		JButton outLimitBtn = new JButton("Choose Limit");
		outLimitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					//text box to enter number of results
					input_numEnt = (String) JOptionPane.showInputDialog("Enter number of desired results, leave blank for full list");
					if(!input_numEnt.equals(""))
						joinCmd += (" limit "+input_numEnt);
					System.out.println("input_numEnt: "+input_numEnt);
				} catch (Exception limitE){
					System.out.println("Error 404: 0xFFFFFF");
				}
		
			}
		});
		outLimitBtn.setBounds(275, 269, 150, 29);
		generalPanel.add(outLimitBtn);
		
		
		//output code goes here
		JButton outputBtn = new JButton("Generate Result");
		outputBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String output = "";
					 //send output to file or on screen
					String[] typeChoices = {"On Screen", "out.txt"};
					String outputType = (String) JOptionPane.showInputDialog(
						null, "Select output type...", "Output Type", JOptionPane.QUESTION_MESSAGE, null, typeChoices, typeChoices[0]);
					System.out.println(outputType);

					if(colToJoin.size() ==0)
						joinCmd = "select * from teams limit 0";
		      // System.out.println(joinCmd);

		            // sets up SQL input and obtains result, then formats it
		            // System.out.println("columns selected: "+columnsSelected+"|");
		           // System.out.println("Columns selected:"+columnsSelected+"|");
					if(columnsSelected.equals("")){
						dbSetup my = new dbSetup();
						try {
							Class.forName("org.postgresql.Driver");
							conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/team_nand", my.user,
								my.pswd);
						} catch (Exception f) {
							f.printStackTrace();
							System.err.println(f.getClass().getName() + ": " + f.getMessage());
							System.exit(0);
						} // end try catch	
						columnsSelected = " * ";
						stmt = conn.createStatement();

						//ensure selectedColumnList is correct when all columns are selected
										// Queries 0 rows to parse columns
						String tempStatement = "Select * from " + input + " limit 0";
						ResultSet columns = stmt.executeQuery(tempStatement);
		      // choices = { "players", "teams", "seasons", "player_records", "games", "Stadiums", "Positions", "Offensive_Records", "Defensive_Records", "Conferences", "Statistics", "Drives" };

						ResultSetMetaData columnstuff = columns.getMetaData();
						int colcnt = columnstuff.getColumnCount();
						ArrayList<String> columnlist = new ArrayList<String>(); // holds columns in input entity
						for (int i = 1; i <= colcnt; i++) {
						SelectedColumnList.add(columnstuff.getColumnName(i));
						}
					}
				if(YearsToQuery.equals("") && !sqlStatementConditional.equals(""))
					YearsToQuery = " where ";
				if(input_numEnt.equals(""))
						sqlStatement = "Select " + columnsSelected + "From " + input + " "+YearsToQuery + sqlStatementConditional;
					else
						sqlStatement = "Select " + columnsSelected + "From " + input + " "+YearsToQuery+sqlStatementConditional+" limit "+input_numEnt;

		      System.out.println(sqlStatement); // used for debugging
		      ResultSet columnsResult = stmt.executeQuery(sqlStatement);
		      // outputs column headers
					for (int i = 0; i < SelectedColumnList.size(); i++) {
						if (i!=SelectedColumnList.size()-1)
							output = output + SelectedColumnList.get(i) + ", ";
						else
							output = output + SelectedColumnList.get(i);
					}
					output += "\n_____________\n";
		      // outputs rows
					while(columnsResult.next()) {
						for (int i = 0; i < SelectedColumnList.size(); i++) {
							if (i!=SelectedColumnList.size()-1)
								output = output + columnsResult.getString(SelectedColumnList.get(i)) + ", ";
							else
								output = output + columnsResult.getString(SelectedColumnList.get(i));
						}
						output += "\n";
					}



					System.out.println("joinCmd: "+joinCmd);

					ResultSet joinRes = stmt.executeQuery(joinCmd);
					ResultSetMetaData joinResmd = joinRes.getMetaData();

					String joinResStr = "";
					for(String i: columnsToPrint){
						if(columnsToPrint.indexOf(i) != columnsToPrint.size()-1)
							joinResStr += i+", ";
						else
							joinResStr +=i+"\n";
					}
					joinResStr += "\n_____________\n";

					while(joinRes.next()){
						for(int i = 1; i <= joinResmd.getColumnCount(); i++){
							if(i != joinResmd.getColumnCount())
								joinResStr += joinRes.getString(i)+", ";
							else
								joinResStr += joinRes.getString(i);
						}
						joinResStr += "\n";
					}
					
					if(colToJoin.size()>0){
		        // JOptionPane.showMessageDialog(null, joinResStr);
						finalout = joinResStr;
					}
					else
						finalout = output;

					      //on screen output
					if(outputType == "On Screen") {
		       //normal output (non-scrolling)
		       //JOptionPane.showMessageDialog(null,output);

		       //scrolling output
						JTextArea textArea = new JTextArea(finalout);
						JScrollPane scrollPane = new JScrollPane(textArea);  
						textArea.setLineWrap(true);  
						textArea.setWrapStyleWord(true); 
						scrollPane.setPreferredSize( new Dimension( 600, 500 ) );

		       JOptionPane.showMessageDialog(null, scrollPane, "Query Result", JOptionPane.YES_NO_OPTION);//,icon);
		   }

		          //text file output
		   else if (outputType == "out.txt") {
		       //send output to text file
		   	PrintWriter out = new PrintWriter("out.txt");
		   	out.println(finalout);
		   	out.close();
		   }

		}catch (Exception genE){
					System.out.println("Error 404: 0x000000 "+genE);
				}

			}
		});
		outputBtn.setBounds(275, 314, 150, 29);
		generalPanel.add(outputBtn);
		
		
		
		//******************************* SIMPLE JOINS *******************************\\
		
		JPanel joinPanel = new JPanel();
		tabbedPane.addTab("Simple Joins", null, joinPanel, null);
		joinPanel.setLayout(null);
		
		JLabel sjLabel = new JLabel("Here you will be able to make a more specific search on the database using simple joins.");
		sjLabel.setBounds(88, 6, 559, 16);
		joinPanel.add(sjLabel);
		
		//entity code goes here
		JButton sjEntityBtn = new JButton("Choose Entities");
		sjEntityBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					String[] choices = { "Players", "Teams", "Seasons", "Player_Records", "Games", "Stadiums", "Positions", "Offensive_Records", "Defensive_Records", "Conferences", "Statistics", "Drives" };
					entity1 = (String) JOptionPane.showInputDialog(null, "Choose now...", "Selcect entity 1",
							JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]); 
					entity2 = (String) JOptionPane.showInputDialog(null, "Choose now...", "Selcect entity 2",
							JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]); 				
			}
		});
		sjEntityBtn.setBounds(287, 34, 150, 29);
		joinPanel.add(sjEntityBtn);
		
		
		
		//column code goes here
		JButton sjColumnsBtn = new JButton("Choose Columns");
		sjColumnsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {	// Building the connection
				dbSetup my = new dbSetup();
				try {
				
				Class.forName("org.postgresql.Driver");
				conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/team_nand", my.user,
				my.pswd);
				} catch (Exception f) {
				f.printStackTrace();
				System.err.println(f.getClass().getName() + ": " + f.getMessage());
				System.exit(0);
				} // end try catch	
				stmt = conn.createStatement();
				
				// Queries 0 rows to parse columns
				sjCmd = "Select * from " + entity1 + " limit 0";
				ResultSet columns = stmt.executeQuery(sjCmd);
				
				ResultSetMetaData columnstuff = columns.getMetaData();
				int colcnt = columnstuff.getColumnCount();
				// A columnlist = new ArrayList<String>(); // holds columns in input entity
				for (int i = 1; i <= colcnt; i++) {
				columnlist.add(columnstuff.getColumnName(i));
				}
				// Creates check box options for each column by adding the new box and column name
				ArrayList<JCheckBox> ColumnBoxes = new ArrayList<JCheckBox>(); // holds check boxes
				Object[] columncontent = new Object[columnlist.size()];
				for (int i = 0; i < columnlist.size(); i++) {
				ColumnBoxes.add(new JCheckBox(columnlist.get(i)));
				columncontent[i] = ColumnBoxes.get(i);
				}
				
				JPanel cols = new JPanel(new BorderLayout(4,4));
				JPanel checkpanel = new JPanel(new GridLayout(0,6));
				cols.add(checkpanel, BorderLayout.CENTER);
				for (int i = 0; i < ColumnBoxes.size(); i++) {
				checkpanel.add(ColumnBoxes.get(i));
				
				}
				// Displays dialog box showing column check boxes, then creates list of desired columns
				JOptionPane.showConfirmDialog(null, cols, "Select columns", JOptionPane.DEFAULT_OPTION);
				ArrayList<Boolean> columnchecks = new ArrayList<Boolean>(); // array of true/false values corresponding to desired columns
				for (int i = 0; i < columnlist.size(); i++) {
				columnchecks.add(ColumnBoxes.get(i).isSelected());
				}
				
				//ArrayList<String> SelectedColumnListE1 = new ArrayList<String>();
				for (int i = 0; i < columnlist.size(); i++) {
				if (columnchecks.get(i))
					SelectedColumnListE1.add(columnlist.get(i));
				}
				
				
				sjCmd = "Select * from " + entity2 + " limit 0";
				columns = stmt.executeQuery(sjCmd);
				
				columnstuff = columns.getMetaData();
				colcnt = columnstuff.getColumnCount();

				columnlistEntity1 = new ArrayList<String>(columnlist);
				columnlist.clear();
				for (int i = 1; i <= colcnt; i++) {
					columnlist.add(columnstuff.getColumnName(i));
				}
				// Creates check box options for each column by adding the new box and column name
				ColumnBoxes = new ArrayList<JCheckBox>(); // holds check boxes
				columncontent = new Object[columnlist.size()];
				for (int i = 0; i < columnlist.size(); i++) {
				ColumnBoxes.add(new JCheckBox(columnlist.get(i)));
				columncontent[i] = ColumnBoxes.get(i);
				}
				
				cols = new JPanel(new BorderLayout(4,4));
				checkpanel = new JPanel(new GridLayout(0,6));
				cols.add(checkpanel, BorderLayout.CENTER);
				for (int i = 0; i < ColumnBoxes.size(); i++) {
				checkpanel.add(ColumnBoxes.get(i));
				
				}
				// Displays dialog box showing column check boxes, then creates list of desired columns
				JOptionPane.showConfirmDialog(null, cols, "Select columns for "+entity1+" then "+entity2, JOptionPane.DEFAULT_OPTION);
				columnchecks = new ArrayList<Boolean>(); // array of true/false values corresponding to desired columns
				for (int i = 0; i < columnlist.size(); i++) {
				columnchecks.add(ColumnBoxes.get(i).isSelected());
				}
				
				//ArrayList<String> SelectedColumnListE2 = new ArrayList<String>();
				for (int i = 0; i < columnlist.size(); i++) {
				if (columnchecks.get(i))
					SelectedColumnListE2.add(columnlist.get(i));
				}
				
				sjCmd = "Select ";
				for (int i = 0; i < SelectedColumnListE1.size(); i++  ) {
					if (i == SelectedColumnListE1.size()-1 && SelectedColumnListE2.size() == 0)
						sjCmd = sjCmd + entity1 + "." + SelectedColumnListE1.get(i) + " ";
					else
						sjCmd = sjCmd + entity1 + "." + SelectedColumnListE1.get(i) + ", ";
				}
				
				for (int i = 0; i < SelectedColumnListE2.size(); i++  ) {
					if (i == SelectedColumnListE2.size()-1)
						sjCmd = sjCmd + entity2 + "." + SelectedColumnListE2.get(i) + " ";
					else
						sjCmd = sjCmd + entity2 + "." + SelectedColumnListE2.get(i) + ", ";
					
				}
				
				sjCmd = sjCmd + "from " + entity1 + " inner join " + entity2 + " on ";
				//System.out.println(sjCmd);
				
				}
				catch(Exception columnError) {
					System.out.println("Error 404: uh oh, what happened?");
					}
			}
			
		});
		sjColumnsBtn.setBounds(287, 75, 150, 29);
		joinPanel.add(sjColumnsBtn);
		
		
		//intersection code goes here
		JButton sjIntersectionBtn = new JButton("Choose Interesect");
		sjIntersectionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] choices = { "player_code", "team_code", "conference_code", "game_code", "stadium_code" };
				String codeIntersect = (String) JOptionPane.showInputDialog(null, "Choose now...", "Selcect Intersection",
						JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]); 
				
				sjCmd = sjCmd + entity1 + "." + codeIntersect + " = " + entity2 + "." + codeIntersect;
				//System.out.println(sjCmd);
			}
		});
		sjIntersectionBtn.setBounds(287, 116, 150, 29);
		joinPanel.add(sjIntersectionBtn);
		
		
		//years code goes here
		JButton sjYearsBtn = new JButton("Choose Years");
		sjYearsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					//Creating an array list of the years
					ArrayList<String> Years = new ArrayList<String>();
					for(int i=2005; i <= 2013; i++){
						Years.add(Integer.toString(i));
					}

		      //Creating all of the checkboxes
					ArrayList<JCheckBox> Boxes = new ArrayList<JCheckBox>();
					ArrayList<Boolean> YearBoxValues = new ArrayList<Boolean>();
					Object[] YearsMsgContent = new Object[Years.size()];
					for (int i = 0; i < Years.size(); i++) {
						Boxes.add(new JCheckBox(Years.get(i)));
						YearsMsgContent[i] = Boxes.get(i);
					}

		      //Set a boolean for each box (checked or not checked)
					JOptionPane.showConfirmDialog(null, YearsMsgContent, "Year Selection", JOptionPane.DEFAULT_OPTION); 
					for (int i = 0; i < Years.size(); i++){
						YearBoxValues.add((Boxes.get(i)).isSelected());
					}

		      //All years that were selected are placed in an array list
					ArrayList<String> YearsSelected = new ArrayList<String>();
					for(int i = 0; i < Years.size(); i++){
						if(YearBoxValues.get(i) == true){
							YearsSelected.add(Years.get(i));
						}
					}

		      //Make a string of the years that will be queried

					if(YearsSelected.size() > 0){
						YearsToQuery = " where (";
						for(int i = 0; i < (YearsSelected.size()-1); i++){
							YearsToQuery += (entity1 + ".year=" + YearsSelected.get(i)+ " or " );
						}
						YearsToQuery += (entity1+".year="+YearsSelected.get(YearsSelected.size()-1)+")");
					}

					/****************************************************************************/
					sjCmd += YearsToQuery;
					//System.out.println(sjCmd);

				} catch (Exception yearsE){
					
				}
			}
		});
		sjYearsBtn.setBounds(287, 157, 150, 29);
		joinPanel.add(sjYearsBtn);
		
		
		
		//conditional code goes here		
		JButton sjConditionalBtn = new JButton("Choose Conditional");
		sjConditionalBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{

					savedSelectedColumnListE1 = new ArrayList<String>(SelectedColumnListE1);
					savedSelectedColumnListE2 = new ArrayList<String>(SelectedColumnListE2);

					SelectedColumnListE1 = columnlistEntity1;
					SelectedColumnListE2 = columnlist;
					SelectedColumnListE1.add("done");
					SelectedColumnListE2.add("done");
					Object[] SelectedColumnListE1Array = SelectedColumnListE1.toArray();
					Object[] SelectedColumnListE2Array = SelectedColumnListE2.toArray();
					
					if(YearsToQuery.equals("")){
						sjCmd += " where ";
					}
					String conValueE1 = "", conColE1, conValueE2, conColE2;
					
					
					//conditions for entity 1
					while(true){
						conColE1 = (String) JOptionPane.showInputDialog(null, "Select columns for conditional values from " + entity1, "",
								JOptionPane.QUESTION_MESSAGE, null, SelectedColumnListE1Array, SelectedColumnListE1Array[0]); 
						if(conColE1.equals("done")){
							SelectedColumnListE1.remove(SelectedColumnListE1.size()-1);
							break;
						}
						conValueE1 = JOptionPane.showInputDialog("Enter conditional value (# ex: >=5) (string ex: = 'Manziel')");
						if(YearsToQuery.equals("")){
							sjCmd += (entity1+"."+conColE1+conValueE1+" ");
						}
						else{
							sjCmd += (" and "+entity1+"."+conColE1+conValueE1+" ");
						}
					}
					
					
					//conditions for entity 2
					while(true){
						conColE2 = (String) JOptionPane.showInputDialog(null, "Select columns for conditional values from " + entity2, "",
								JOptionPane.QUESTION_MESSAGE, null, SelectedColumnListE2Array, SelectedColumnListE2Array[0]); 
						if(conColE2.equals("done")){
							SelectedColumnListE2.remove(SelectedColumnListE2.size()-1);
							break;
						}
						conValueE2 = JOptionPane.showInputDialog("Enter conditional value (# ex: >=5) (string ex: = 'Manziel')");
						if(YearsToQuery.equals("") &&  conValueE1 == ""){
							sjCmd += (" "+entity2+"."+conColE2+conValueE2+" ");
						}
						else{
							sjCmd += (" and "+entity2+"."+conColE2+conValueE2+" ");
						}
					}
					
					//System.out.println(sjCmd);
				} catch (Exception conditionalE){
					System.out.println("Error 404: 0x2feab211");
				}

			}
		});
		sjConditionalBtn.setBounds(287, 198, 150, 29);
		joinPanel.add(sjConditionalBtn);		
		
		
		
		//limit code goes here
		JButton sjLimitBtn = new JButton("Choose Limit");
		sjLimitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					//text box to enter number of results
					input_numEnt = (String) JOptionPane.showInputDialog("Enter number of desired results, leave blank for full list");
					if(!input_numEnt.equals(""))
						sjCmd += (" limit "+input_numEnt);
					
					System.out.println(sjCmd);
				} catch (Exception limitE){
					System.out.println("Error 404: 0xFFFFFF");
				}
			}
		});
		sjLimitBtn.setBounds(287, 239, 150, 29);
		joinPanel.add(sjLimitBtn);
		
		JButton sjOutputBtn = new JButton("Generate Result");
		sjOutputBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {	// Building the connection
					dbSetup my = new dbSetup();
					try {
					
					Class.forName("org.postgresql.Driver");
					conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/team_nand", my.user,
					my.pswd);
					} catch (Exception f) {
					f.printStackTrace();
					System.err.println(f.getClass().getName() + ": " + f.getMessage());
					System.exit(0);
					} // end try catch	
					stmt = conn.createStatement();
					ResultSet sj = stmt.executeQuery(sjCmd);
					ResultSetMetaData sjmd = sj.getMetaData();

					
					String[] typeChoices = {"On Screen", "out.txt"};
					String outputType = (String) JOptionPane.showInputDialog(
						null, "Select output type...", "Output Type", JOptionPane.QUESTION_MESSAGE, null, typeChoices, typeChoices[0]);
					
					
					
					String finalout = "";
					
					//setting up column headers
					SelectedColumnListE1 = new ArrayList<String>(savedSelectedColumnListE1);
					SelectedColumnListE2 = new ArrayList<String>(savedSelectedColumnListE2);

					for (int i = 0; i < SelectedColumnListE1.size(); i++) {
						if (i<= SelectedColumnListE1.size()-1)
							finalout += SelectedColumnListE1.get(i) + ", ";
						else if (i == SelectedColumnListE1.size()-1 && SelectedColumnListE2.size() == 0)
							finalout += SelectedColumnListE1.get(i);
					}
					
					for (int i = 0; i < SelectedColumnListE2.size(); i++) {
						if (i!=SelectedColumnListE2.size()-1)
							finalout += SelectedColumnListE2.get(i) + ", ";
						else 
							finalout += SelectedColumnListE2.get(i);
					}
					
					finalout += "\n_____________\n";
					
					//setting up rows
					while(sj.next()) {
						for(int i = 1; i <= sjmd.getColumnCount() ;i++){
							if (i != sjmd.getColumnCount())
								finalout += sj.getString(i) + ", ";
							else
								finalout += sj.getString(i);
						}
						
						finalout += "\n";
					}
					
					//System.out.println(finalout);
					
					//on screen output
					if(outputType == "On Screen") {
						//scrolling output
						JTextArea textArea = new JTextArea(finalout);
						JScrollPane scrollPane = new JScrollPane(textArea);  
						textArea.setLineWrap(true);  
						textArea.setWrapStyleWord(true); 
						scrollPane.setPreferredSize( new Dimension( 600, 500 ) );

						JOptionPane.showMessageDialog(null, scrollPane, "Query Result", JOptionPane.YES_NO_OPTION);//,icon);
				   }

					//text file output
					else if (outputType == "out.txt") {  
					   	PrintWriter out = new PrintWriter("out.txt");
					   	out.println(finalout);
					   	out.close();
					}
				}catch(Exception outputE) {System.out.println("error: could not output");}
			}
		});
		sjOutputBtn.setBounds(287, 280, 150, 29);
		joinPanel.add(sjOutputBtn);
		
		
		//******************************* QUESTION 1 *******************************\\
		
		JPanel q1Panel = new JPanel();
		tabbedPane.addTab("Victory Chain", null, q1Panel, null);
		q1Panel.setLayout(null);
		
		JLabel vcLabel = new JLabel("Select two teams to see the how one is better than the other.");
		vcLabel.setBounds(175, 38, 438, 16);
		q1Panel.add(vcLabel);
		
		JComboBox victorTeamList = new JComboBox();
		victorTeamList.setBounds(111, 124, 123, 27);
		q1Panel.add(victorTeamList);
		
		JComboBox loserTeamList = new JComboBox();
		loserTeamList.setBounds(299, 124, 123, 27);
		q1Panel.add(loserTeamList);
		
		JLabel victorLabel = new JLabel("Victor");
		victorLabel.setBounds(152, 107, 82, 16);
		q1Panel.add(victorLabel);
		
		JLabel loserLabel = new JLabel("Loser");
		loserLabel.setBounds(344, 107, 78, 16);
		q1Panel.add(loserLabel);
		
		JButton vcButton = new JButton("Generate Result");
		vcButton.setBounds(488, 123, 163, 29);
		q1Panel.add(vcButton);
		
		JLabel vcOutputLabel = new JLabel("Output");
		vcOutputLabel.setBounds(344, 179, 78, 16);
		q1Panel.add(vcOutputLabel);
		
		JScrollPane vcScrollPane = new JScrollPane();
		vcScrollPane.setBounds(285, 199, 163, 163);
		q1Panel.add(vcScrollPane);
		
		vcTable = new JTable();
		vcScrollPane.setViewportView(vcTable);
		
		
		
		//******************************* QUESTION 3 *******************************\\
		
		JPanel q3Panel = new JPanel();
		tabbedPane.addTab("Rushing Yards", null, q3Panel, null);
		q3Panel.setLayout(null);
		
		JLabel ryLabel = new JLabel("Input a given team and the team with the most rushing yards against them will be displayed.");
		ryLabel.setBounds(70, 38, 640, 16);
		q3Panel.add(ryLabel);
		

		JComboBox ryTeamList = new JComboBox();
		ryTeamList.setMaximumRowCount(256);
		ryTeamList.setBounds(152, 111, 182, 27);
		q3Panel.add(ryTeamList);
		
		JTextArea ryOutput = new JTextArea();
		ryOutput.setEditable(false);
		ryOutput.setBounds(282, 209, 250, 27);
		q3Panel.add(ryOutput);
		
		JLabel ryOutputLabel = new JLabel("Output");
		ryOutputLabel.setBounds(340, 187, 61, 16);
		q3Panel.add(ryOutputLabel);
		
		JButton ryButton = new JButton("Generate Result");
		ryButton.setBounds(390, 110, 190, 29);

		
		q3Panel.add(ryButton);
		
		JLabel ryTeamLabel = new JLabel("Team");
		ryTeamLabel.setBounds(230, 95, 61, 16);
		q3Panel.add(ryTeamLabel);

		
		q3sqlStatement = "select distinct name from Teams order by name ASC;";
		try {
			
			try {
				dbSetup q3my = new dbSetup();
				Class.forName("org.postgresql.Driver");
				conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/team_nand", q3my.user,
					q3my.pswd);
			} catch (Exception q3f) {
				q3f.printStackTrace();
				System.err.println(q3f.getClass().getName() + ": " + q3f.getMessage());
				System.exit(0);
			} // end try catch	
			stmt = conn.createStatement();
			ResultSet q3TeamNames = stmt.executeQuery(q3sqlStatement);
			while (q3TeamNames.next()) {
				ryTeamList.addItem(q3TeamNames.getString(1));
			}
			
			ryButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String q3Team = (String)ryTeamList.getSelectedItem();
					String q3TeamCode = "";
					q3sqlStatement = "select distinct team_code from Teams where name = '" + q3Team + "';";
					try {
						ResultSet q3TC = stmt.executeQuery(q3sqlStatement);
						while (q3TC.next()) {
							q3TeamCode = q3TC.getString(1);
						}
						ArrayList<String> q3GamesList = new ArrayList<String>();
						ArrayList<String> q3VisitList = new ArrayList<String>();
						ArrayList<String> q3HomeList = new ArrayList<String>();
						q3sqlStatement = "select game_code, visit_team_code, home_team_code from Seasons where visit_team_code='"+q3TeamCode+"' or home_team_code='"+q3TeamCode+"';";
						ResultSet q3GCList = stmt.executeQuery(q3sqlStatement);
						while (q3GCList.next()) {
							q3GamesList.add(q3GCList.getString(1));
							q3VisitList.add(q3GCList.getString(2));
							q3HomeList.add(q3GCList.getString(3));
						}
						if (q3GamesList.size()==0) { 
							ryOutput.setText("No Game Data Available :(");
						}
						else {
							String q3MaxRushTC = "";
							String q3RushTeamCode = "";
							int q3MaxRush = 0;
							stmt.execute("Create temporary view Q3Games as " + q3sqlStatement);
							for (int i = 0; i < q3GamesList.size(); i++) {
								if (q3TeamCode.equals(q3VisitList.get(i))) { 
									q3RushTeamCode = q3HomeList.get(i);
								}
								else if (q3TeamCode.equals(q3HomeList.get(i))) {
									q3RushTeamCode = q3VisitList.get(i);
								}
								q3sqlStatement = "select sum(Statistics.rush_yard) from Statistics inner join Q3Games on Statistics.game_code=Q3Games.game_code where team_code = '"+q3RushTeamCode+"';";
								ResultSet q3RushCount = stmt.executeQuery(q3sqlStatement);
			            		//System.out.println(q3GamesList.size());
								while (q3RushCount.next()) {
									// System.out.println(q3RushCount.getString(1));
									if (Integer.parseInt(q3RushCount.getString(1))>q3MaxRush) {
										q3MaxRush = Integer.parseInt(q3RushCount.getString(1));
										q3MaxRushTC = q3RushTeamCode;
										// System.out.println(q3MaxRush);
										// System.out.println("hi");
									}
								}		            			
							}
							stmt.execute("drop view Q3Games;");
							ResultSet q3MaxRushName = stmt.executeQuery("select distinct name from Teams where team_code='"+q3MaxRushTC+"';");
							while (q3MaxRushName.next()) {
								ryOutput.setText(q3MaxRushName.getString(1)+": "+q3MaxRush+" yards");
							}
						}
					} catch (Exception q3e) {
						q3e.printStackTrace();
					}

				}
			});
			
		} catch (Exception q3e1) {
			q3e1.printStackTrace();
		}
		
		
		
		//******************************* QUESTION 4 *******************************\\
JPanel q4Panel = new JPanel();
		tabbedPane.addTab("Home Field Advantage", null, q4Panel, null);
		q4Panel.setLayout(null);
		
		JLabel hfaLabel1 = new JLabel("The below table will rank the average home field advantage by toughest place to play within a given conference.");
		hfaLabel1.setBounds(6, 6, 704, 16);
		q4Panel.add(hfaLabel1);
		
		JLabel hfaLabel2 = new JLabel("100 - impossible to beat at home");
		hfaLabel2.setBounds(256, 24, 255, 16);
		q4Panel.add(hfaLabel2);
		
		JLabel hfaLabel3 = new JLabel("0 - will not win at home");
		hfaLabel3.setBounds(285, 42, 198, 16);
		q4Panel.add(hfaLabel3);
		

		String[] confrences = {"American Athletic Conference",
		"Atlantic 10",
		"Atlantic Coast Conference",
		"Big 12 Conference",
		"Big East Conference",
		"Big Sky",
		"Big South",
		"Big Ten Conference",
		"Colonial",
		"Conference USA",
		"Gateway",
		"Gateway Football Conference",
		"Ind",
		"Independent",
		"Ivy",
		"Metro Atlantic",
		"Mid-American Conference",
		"Mid-Eastern",
		"Mountain West",
		"Mountain West Conference",
		"MVFC",
		"Northeast",
		"Ohio Valley",
		"OVC",
		"Pac-12 Conference",
		"Pacific-10 Conference",
		"Patriot",
		"Pioneer",
		"Pioneer Football League",
		"Southeastern Conference",
		"Southern",
		"Southland",
		"Southwestern",
		"Sun Belt",
		"Sun Belt Conference",
		"Western Athletic Conference"};

		JComboBox confrenceList = new JComboBox(confrences);   //compiler warnings
		confrenceList.setMaximumRowCount(12);
		confrenceList.setBounds(206, 87, 250, 27);
		q4Panel.add(confrenceList);
		
		JLabel conferenceLabel = new JLabel("Conference");
		conferenceLabel.setBounds(206, 70, 126, 16);
		q4Panel.add(conferenceLabel);
		
		class teamName_winPercentage{
			Double winPercentage;
			String teamName;	
		}
		class stringPair{
			public stringPair(String is1, String is2){
				str1 = is1;
				str2 = is2;
			}
			String str1;
			String str2;
		}
		JButton hfaButton = new JButton("Generate Result");
				hfaButton.setBounds(475, 86, 187, 29); //385
		q4Panel.add(hfaButton);
		
		JLabel hfaOutputLabel = new JLabel("Output");
		hfaOutputLabel.setBounds(338, 126, 100, 16);
		q4Panel.add(hfaOutputLabel);
		
		JScrollPane hfaScrollPane = new JScrollPane();
		hfaScrollPane.setBounds(285, 154, 300, 185);
		q4Panel.add(hfaScrollPane);
		
		DefaultTableModel hfaModel = new DefaultTableModel();
		hfaTable = new JTable(hfaModel);
		hfaModel.addColumn("Team Name");
		hfaModel.addColumn("Home Field Advantage");

		hfaScrollPane.setViewportView(hfaTable);
		
		
		
		hfaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedConf = confrenceList.getSelectedItem().toString();
				try{
				//get all team codes and add them to array
				dbSetup my = new dbSetup();
				try {
					Class.forName("org.postgresql.Driver");
					conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/team_nand", my.user,
						my.pswd);
				} catch (Exception f) {
					f.printStackTrace();
					System.err.println(f.getClass().getName() + ": " + f.getMessage());
					System.exit(0);
				} // end try catch	
				stmt = conn.createStatement();

				//get all conference codes and names and store them into array
				ResultSet conferenceInfoQ = stmt.executeQuery("select conference_code, name from conferences");
				ArrayList<stringPair> conferenceInfo = new ArrayList<stringPair>();

				while(conferenceInfoQ.next())
					conferenceInfo.add(new stringPair(conferenceInfoQ.getString("conference_code"), conferenceInfoQ.getString("name")));

				String confCode = "";
				for(stringPair i : conferenceInfo){
					if(i.str2.equals(selectedConf)){
						confCode = i.str1;
						break;
					}
				}

				// System.out.println("code: "+confCode+" conference: "+selectedConf);


				//get list of all teams in selected conference and store them in array

				ResultSet team_codesQ = stmt.executeQuery("select distinct team_code, name from teams where conference_code = "+confCode);
				ArrayList<String> team_codes = new ArrayList<String>();
				ArrayList<String> team_names = new ArrayList<String>();

				while(team_codesQ.next()){
					team_codes.add(team_codesQ.getString("team_code"));
					team_names.add(team_codesQ.getString("name"));
				}


				//for each team find all home games, and calculate win percentage
				ArrayList<teamName_winPercentage> q4Res = new ArrayList<teamName_winPercentage>(); 
				for(int i = 0; i < team_codes.size(); i++)  {  //iterate through all teamcodes
					System.out.println(String.format("%.2f",((double)i/(double)team_codes.size() * 100))+"% complete");   //print progress
					ArrayList<String> game_codes = new ArrayList<String>();
					ArrayList<String> visit_team_codes = new ArrayList<String>();

					//get all game codes that was played at home for the current team
					ResultSet game_codesQ = stmt.executeQuery("select game_code, visit_team_code from seasons where home_team_code = "+team_codes.get(i));  //could only compare teams in same conference
					while(game_codesQ.next()){
						game_codes.add(game_codesQ.getString("game_code"));
						visit_team_codes.add(game_codesQ.getString("visit_team_code"));
					}
					Double wins = 0.0;
					for(int j = 0; j < game_codes.size(); j++){ //iterate through all home games
						ResultSet homeTeamPointsQ = stmt.executeQuery("select points from statistics where game_code = "+game_codes.get(j)+" and team_code = "+team_codes.get(i));
						int homeTeamPoints = -1;
						int visitTeamPoints = -1;

						homeTeamPointsQ.next();
						homeTeamPoints = homeTeamPointsQ.getInt("points");
						ResultSet visitTeamPointsQ = stmt.executeQuery("select points from statistics where game_code = "+game_codes.get(j)+" and team_code = "+visit_team_codes.get(j));
						visitTeamPointsQ.next();
						visitTeamPoints = visitTeamPointsQ.getInt("points");
						if(homeTeamPoints > visitTeamPoints ) 
							wins++;

					}
					teamName_winPercentage temp = new teamName_winPercentage();
					temp.teamName = team_names.get(i);
					temp.winPercentage = wins/game_codes.size() * 100;

					q4Res.add(temp);
					// System.out.println(temp.teamName + "     "+temp.winPercentage);
					//String.format("%.2f", (turfsUsedPercent.get(i)))
					hfaModel.addRow(new Object[]{temp.teamName, String.format("%.2f",temp.winPercentage)});
	
			}

		} catch(Exception q4e){
				System.out.println("Error: 0x213958");
			}
			System.out.println("100.0% complete");



			}
		});

		
		
		
		//******************************* QUESTION 5 *******************************\\
		
JPanel turfPanel = new JPanel();
		tabbedPane.addTab("Turf", null, turfPanel, null);
		turfPanel.setLayout(null);
		
		JLabel turfLabel = new JLabel("Input a given team and year to get their alltime stadium material performance statistics in that conference.");
		turfLabel.setBounds(70, 38, 750, 32);
		turfPanel.add(turfLabel);
		

		JComboBox turfTeamList = new JComboBox();
		turfTeamList.setMaximumRowCount(256);
		turfTeamList.setBounds(120, 111, 182, 27);
		turfPanel.add(turfTeamList);
		
		JComboBox turfYearList = new JComboBox();
		turfYearList.setMaximumRowCount(256);
		turfYearList.setBounds(330, 111, 60, 27);
		turfPanel.add(turfYearList);
		
		JTextArea turfOutput = new JTextArea();
		turfOutput.setEditable(false);
		turfOutput.setBounds(282, 209, 200, 160);
		turfPanel.add(turfOutput);
		
		JLabel turfOutputLabel = new JLabel("Output");
		turfOutputLabel.setBounds(340, 187, 61, 16);
		turfPanel.add(turfOutputLabel);
		
		JButton turfButton = new JButton("Generate Result");
		turfButton.setBounds(420, 110, 190, 29);
		turfPanel.add(turfButton);
		
		JLabel turfTeamLabel = new JLabel("Team");
		turfTeamLabel.setBounds(200, 95, 61, 16);
		turfPanel.add(turfTeamLabel);
		
		JLabel turfYearLabel = new JLabel("Year");
		turfYearLabel.setBounds(345, 95, 61, 16);
		turfPanel.add(turfYearLabel);
		
		turfsqlStatement = "select distinct name from Teams order by name ASC;";
		try {
			
			try {
				dbSetup turfmy = new dbSetup();
				Class.forName("org.postgresql.Driver");
				conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/team_nand", turfmy.user,
					turfmy.pswd);
			} catch (Exception turff) {
				turff.printStackTrace();
				System.err.println(turff.getClass().getName() + ": " + turff.getMessage());
				System.exit(0);
			} // end try catch	
			stmt = conn.createStatement();
			ResultSet turfTeamNames = stmt.executeQuery(turfsqlStatement);
			while (turfTeamNames.next()) {
					turfTeamList.addItem(turfTeamNames.getString(1));
			}
			ResultSet turfYears = stmt.executeQuery("Select distinct year from Teams order by year DESC;");
			while (turfYears.next()) {
				turfYearList.addItem(turfYears.getString(1));
			}
			turfButton.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		            String turfTeam = (String)turfTeamList.getSelectedItem();
		            String turfYear = (String)turfYearList.getSelectedItem();
		            String turfTeamCode = "";
		            String turfConferenceCode = "";
		            turfsqlStatement = "select distinct team_code, conference_code from Teams where name = '" + turfTeam + "' and year = '" + turfYear + "';";
		            try {
		            	ResultSet turfTC = stmt.executeQuery(turfsqlStatement);
		            	while (turfTC.next()) {
		            		turfTeamCode = turfTC.getString(1);
		            		turfConferenceCode = turfTC.getString(2);
		            	}
		            	
		            	turfsqlStatement = ""
		            	+"CREATE VIEW turfplay_count(game_code, count) AS "
		            	+"SELECT game_code, COUNT(play_number) FROM games GROUP BY game_code; "

		            	+"CREATE VIEW turfoffense_team(game_code, play, year, offense_team, offense_points, count, conference_code) AS "
		            	+"SELECT games.game_code, max(games.play_number), seasons.year, teams.name, MAX(games.offense_points), turfplay_count.count, teams.conference_code FROM games "
		            	+"INNER JOIN seasons ON games.game_code = seasons.game_code "
		            	+"INNER JOIN teams ON teams.team_code = games.offense_team_code "
		            	+"INNER JOIN turfplay_count ON games.game_code = turfplay_count.game_code "
		            	+"WHERE turfplay_count.count = games.play_number "
		            	+"GROUP BY games.game_code, seasons.year, teams.name, turfplay_count.count, teams.conference_code; "

		            	+"CREATE VIEW turfdefense_team(game_code, play, year, defense_team, defense_points, conference_code) AS "
		            	+"SELECT games.game_code, max(games.play_number), seasons.year, teams.name, MAX(games.defense_points), teams.conference_code FROM games "
		            	+"INNER JOIN seasons ON games.game_code = seasons.game_code "
		            	+"INNER JOIN teams ON teams.team_code = games.defense_team_code "
		            	+"INNER JOIN turfplay_count ON games.game_code = turfplay_count.game_code "
		            	+"WHERE turfplay_count.count = games.play_number "
		            	+"GROUP BY games.game_code, seasons.year, teams.name, turfplay_count.count, teams.conference_code; "

		            	+"CREATE VIEW turfgames AS "
		            	+"SELECT turfoffense_team.game_code, turfoffense_team.year, turfoffense_team.offense_team, turfoffense_team.offense_points, turfdefense_team.defense_team, turfdefense_team.defense_points, seasons.stadium_code, turfoffense_team.conference_code "
		            	+"FROM turfoffense_team "
		            	+"INNER JOIN turfdefense_team ON turfoffense_team.game_code = turfdefense_team.game_code AND turfoffense_team.conference_code = turfdefense_team.conference_code "
		            	+"INNER JOIN seasons ON seasons.game_code = turfoffense_team.game_code "
		            	+"WHERE turfoffense_team.offense_team != turfdefense_team.defense_team AND turfoffense_team.conference_code = turfdefense_team.conference_code "
		            	+"GROUP BY turfoffense_team.game_code, turfoffense_team.play, turfoffense_team.year, turfoffense_team.offense_team, turfoffense_team.offense_points, turfdefense_team.defense_team, turfdefense_team.defense_points, seasons.stadium_code, turfoffense_team.conference_code "
		            	+"ORDER BY turfoffense_team.year ASC; ";
		            	stmt.execute(turfsqlStatement);
		            	
		            	turfsqlStatement = 
		            	"SELECT distinct turfgames.game_code, turfgames.conference_code, stadiums.surface, turfgames.offense_team, turfgames.offense_points, turfgames.defense_team, turfgames.defense_points "
		            	+"FROM turfgames "
		            	+"INNER JOIN stadiums ON turfgames.stadium_code = stadiums.stadium_code "
		            	+"WHERE turfgames.conference_code = " + turfConferenceCode + " AND (turfgames.offense_team = '"+turfTeam+"' OR turfgames.defense_team = '"+turfTeam+"');";
		            	int AstroTurf = 0;
		            	int AstroPlay = 0;
		            	int Grass = 0;
		            	int SoftTop = 0;
		            	int SportsTurf = 0;
		            	int SportexeM = 0;
		            	int ProGrass = 0;
		            	int ProPlay = 0;
		            	int SportexePB = 0;
		            	int TurfTech = 0;
		            	int ATurf = 0;
		            	int PrestigeSystem = 0;
		            	int FieldTurf = 0;
		            	int NexTurf = 0;
		            	int AstroTurfW = 0;
		            	int AstroPlayW = 0;
		            	int GrassW = 0;
		            	int SoftTopW = 0;
		            	int SportsTurfW = 0;
		            	int SportexeMW = 0;
		            	int ProGrassW = 0;
		            	int ProPlayW = 0;
		            	int SportexePBW = 0;
		            	int TurfTechW = 0;
		            	int ATurfW = 0;
		            	int PrestigeSystemW = 0;
		            	int FieldTurfW = 0;
		            	int NexTurfW = 0;
		            	int turfTeamPts = 0;
		            	int turfOppPts = 0;
		            	Boolean turfWin = false;
		            	ResultSet turfStats = stmt.executeQuery(turfsqlStatement);
		            	while (turfStats.next()) {
		            		if (turfTeam.equals(turfStats.getString(4))) {
		            			turfTeamPts = turfStats.getInt(5);
		            			turfOppPts = turfStats.getInt(7);
		            		}
		            		else {
		            			turfTeamPts = turfStats.getInt(7);
		            			turfOppPts = turfStats.getInt(5);
		            		}
		            		if (turfTeamPts > turfOppPts) turfWin = true;
		            		else turfWin = false;
		            		switch (turfStats.getString(3)) {
		            		case "AstroTurf":
		            			if (turfWin) AstroTurfW++;
		            			AstroTurf++;
		            			break;
		            		case "AstroPlay":
		            			if (turfWin) AstroPlayW++;
		            			AstroPlay++;
		            			break;
		            		case "Grass":
		            			if (turfWin) GrassW++;
		            			Grass++;
		            			break;
		            		case "SoftTop Turf System":
		            			if (turfWin) SoftTopW++;
		            			SoftTop++;
		            			break;
		            		case "SportsTurf":
		            			if (turfWin) SportsTurfW++;
		            			SportsTurf++;
		            			break;
		            		case "Sportexe Momentum":
		            			if (turfWin) SportexeMW++;
		            			SportexeM++;
		            			break;
		            		case "ProGrass":
		            			if (turfWin) ProGrassW++;
		            			ProGrass++;
		            			break;
		            		case "ProPlay":
		            			if (turfWin) ProPlayW++;
		            			ProPlay++;
		            			break;
		            		case "Sportexe PowerBlade":
		            			if (turfWin) SportexePBW++;
		            			SportexePB++;
		            			break;
		            		case "TurfTech":
		            			if (turfWin) TurfTechW++;
		            			TurfTech++;
		            			break;
		            		case "A-Turf":
		            			if (turfWin) ATurfW++;
		            			ATurf++;
		            			break;
		            		case "Prestige System":
		            			if (turfWin) PrestigeSystemW++;
		            			PrestigeSystem++;
		            			break;
		            		case "FieldTurf":
		            			if (turfWin) FieldTurfW++;
		            			FieldTurf++;
		            			break;
		            		case "NexTurf":
		            			if (turfWin) NexTurfW++;
		            			NexTurf++;
		            			break;
		            		default:
		            		}
		            	}
		            	stmt.execute("DROP VIEW turfplay_count CASCADE;");
		            	ArrayList<String> turfsUsed = new ArrayList<String>();
		            	ArrayList<Double> turfsUsedPercent = new ArrayList<Double>();
		            	if (AstroTurf>0) {
		            		turfsUsed.add("AstroTurf");
		            		turfsUsedPercent.add(100*((double)AstroTurfW/AstroTurf));
		            	}
		            	if (AstroPlay>0) {
		            		turfsUsed.add("AstroPlay");
		            		turfsUsedPercent.add(100*((double)AstroPlayW/AstroPlay));
		            	}
		            	if (Grass>0) {
		            		// System.out.println(GrassW);
		            		// System.out.println(Grass);
		            		turfsUsed.add("Grass");
		            		turfsUsedPercent.add(100*((double)GrassW/Grass));
		            	}
		            	if (SoftTop>0) {
		            		turfsUsed.add("SoftTop Turf System");
		            		turfsUsedPercent.add(100*((double)SoftTopW/SoftTop));
		            	}
		            	if (SportsTurf>0) {
		            		turfsUsed.add("SportsTurf");
		            		turfsUsedPercent.add(100*((double)SportsTurfW/SportsTurf));
		            	}
		            	if (SportexeM>0) {
		            		turfsUsed.add("Sportexe Momentum");
		            		turfsUsedPercent.add(100*((double)SportexeMW/SportexeM));
		            	}
		            	if (ProGrass>0) {
		            		turfsUsed.add("ProGrass");
		            		turfsUsedPercent.add(100*((double)ProGrassW/ProGrass));
		            	}
		            	if (ProPlay>0) {
		            		turfsUsed.add("ProPlay");
		            		turfsUsedPercent.add(100*((double)ProPlayW/ProPlay));
		            	}
		            	if (SportexePB>0) {
		            		turfsUsed.add("Sportexe Momentum");
		            		turfsUsedPercent.add(100*((double)SportexePBW/SportexePB));
		            	}
		            	if (TurfTech>0) {
		            		turfsUsed.add("TurfTech");
		            		turfsUsedPercent.add(100*((double)TurfTechW/TurfTech));
		            	}
		            	if (ATurf>0) {
		            		turfsUsed.add("A-Turf");
		            		turfsUsedPercent.add(100*((double)ATurfW/ATurf));
		            	}
		            	if (PrestigeSystem>0) {
		            		turfsUsed.add("Prestige System");
		            		turfsUsedPercent.add(100*((double)PrestigeSystemW/PrestigeSystem));
		            	}
		            	if (FieldTurf>0) {
		            		turfsUsed.add("FieldTurf");
		            		turfsUsedPercent.add(100*((double)FieldTurfW/FieldTurf));
		            	}
		            	if (NexTurf>0) {
		            		turfsUsed.add("NexTurf");
		            		turfsUsedPercent.add(100*((double)NexTurfW/NexTurf));
		            	}
		            	String TurfOutput = "Turf        Win Percentage\n_________________________\n";
		            	if (turfsUsed.size()>0) {
			            	for (int i = 0; i < turfsUsed.size(); i++) {
			            		TurfOutput = TurfOutput + turfsUsed.get(i) + "    " + String.format("%.2f", (turfsUsedPercent.get(i))) + "%\n";
			            	}
		            	}
		            	else TurfOutput = "Not enough turf data available :(";
		            	turfOutput.setText(TurfOutput);
		            } catch (Exception turfe) {
		    			turfe.printStackTrace();
		    		}
		            
		         }
			});
			
			
		} catch (Exception turfe1) {
			turfe1.printStackTrace();
		}
		
	}
}
	
		
