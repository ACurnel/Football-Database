//package localGUI;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;


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
		
		
		//join code goes here
		JButton joinBtn = new JButton("Choose Joins");
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
					joinCmd += (" and "+input+"."+conCol+conValue+" ");

					sqlStatementConditional += (" and "+input+"."+conCol+conValue+" ");
					
					
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
					//drop down to select number of results
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
		ryTeamList.setMaximumRowCount(12);
		ryTeamList.setBounds(217, 111, 117, 27);
		q3Panel.add(ryTeamList);
		
		JTextArea ryOutput = new JTextArea();
		ryOutput.setEditable(false);
		ryOutput.setBounds(282, 209, 162, 27);
		q3Panel.add(ryOutput);
		
		JLabel ryOutputLabel = new JLabel("Output");
		ryOutputLabel.setBounds(340, 187, 61, 16);
		q3Panel.add(ryOutputLabel);
		
		JButton ryButton = new JButton("Generate Result");
		ryButton.setBounds(390, 110, 190, 29);
		q3Panel.add(ryButton);
		
		JLabel ryTeamLabel = new JLabel("Team");
		ryTeamLabel.setBounds(254, 94, 61, 16);
		q3Panel.add(ryTeamLabel);
		
		
		
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
		
		JComboBox confrenceList = new JComboBox();
		confrenceList.setMaximumRowCount(12);
		confrenceList.setBounds(206, 87, 126, 27);
		q4Panel.add(confrenceList);
		
		JLabel conferenceLabel = new JLabel("Conference");
		conferenceLabel.setBounds(206, 70, 126, 16);
		q4Panel.add(conferenceLabel);
		
		JButton hfaButton = new JButton("Generate Result");
		hfaButton.setBounds(385, 86, 187, 29);
		q4Panel.add(hfaButton);
		
		JLabel hfaOutputLabel = new JLabel("Output");
		hfaOutputLabel.setBounds(338, 126, 44, 16);
		q4Panel.add(hfaOutputLabel);
		
		JScrollPane hfaScrollPane = new JScrollPane();
		hfaScrollPane.setBounds(285, 154, 152, 185);
		q4Panel.add(hfaScrollPane);
		
		hfaTable = new JTable();
		hfaScrollPane.setViewportView(hfaTable);
		
		
		
		//******************************* QUESTION 5 *******************************\\
		
		JPanel turfPanel = new JPanel();
		tabbedPane.addTab("Turf", null, turfPanel, null);
	
		
		
	}
}
