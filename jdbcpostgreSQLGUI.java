import java.sql.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;


//import java.sql.DriverManager;
/*
Robert lightfoot
CSCE 315
9-25-2019
 */
public class jdbcpostgreSQLGUI {
	public static void main(String args[]) throws FileNotFoundException {
		dbSetup my = new dbSetup();
    ArrayList<String> colToJoin = new ArrayList<String>();
    // ImageIcon icon = new ImageIcon ("nand.png");
    String finalout = "";
    String temp_outputType = "";
    Statement stmt = null;
		// Building the connection
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/team_nand", my.user,
					my.pswd);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} // end try catch
		Object[] StartQuery = {"Start a Query"};
      
     JOptionPane.showOptionDialog(null, "College Football Statistics Database", "Team_NAND Database", JOptionPane.YES_OPTION, JOptionPane.PLAIN_MESSAGE, null, StartQuery, null);
		String output = "";
	/*	try {
			// create a statement object
			Statement stmt = conn.createStatement();
			// create an SQL statement
			// ENTITIES*/
			String[] questions = {"General Query", "Question 1", "Question 3", "Question 4", "Question 5", "Bonus"};
			String qinput = (String) JOptionPane.showInputDialog(null, "Choose now...", "Select your query", JOptionPane.QUESTION_MESSAGE, null, questions, questions[0]);
			
			switch(qinput) {
				case "General Query":	
					try {
						// create a statement object
						stmt = conn.createStatement();
						// create an SQL statement
						// ENTITIES
					String[] choices = { "Players", "Teams", "Seasons", "Player_Records", "Games", "Stadiums", "Positions", "Offensive_Records", "Defensive_Records", "Conferences", "Statistics", "Drives" };
					String input = (String) JOptionPane.showInputDialog(null, "Choose now...", "Selcect entity",
							JOptionPane.QUESTION_MESSAGE, null, // Use
																// default
																// icon
							choices, // Array of choices
							choices[0]); // Initial choice
					System.out.println(input);
					String sqlStatement = "";
					
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
		
					ArrayList<String> SelectedColumnList = new ArrayList<String>(); // List holding desired column names
					for (int i = 0; i < columnlist.size(); i++) {
						if (columnchecks.get(i))
							SelectedColumnList.add(columnlist.get(i));
					}
					//System.out.println(SelectedColumnList.size()); // used for debugging
					
					String columnsSelected = ""; // formatted string of columns to input
					for (int i = 0; i < SelectedColumnList.size(); i++) {
						if (i!=SelectedColumnList.size()-1)
							columnsSelected = columnsSelected + SelectedColumnList.get(i) + ",";
						else
							columnsSelected = columnsSelected + SelectedColumnList.get(i) + " ";
					}
					
		
		      /*****************************************************************************/
		        //get names of columns
		      ResultSet resultNumCol = stmt.executeQuery("SELECT * FROM "+input+" limit 1");
		      ResultSetMetaData rsmd = resultNumCol.getMetaData();
		      ArrayList<String> columnsJoin = new ArrayList<String>();
		      for(int i = 1; i <= rsmd.getColumnCount(); i++){
		        columnsJoin.add(rsmd.getColumnName(i));
		      }
		       //Dsiplay the columns that can be joined
		      JOptionPane.showMessageDialog(null, "Available columns to Join (only team_code and player_code working):\n"); 
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
		      ArrayList<String> columnsToPrint = SelectedColumnList;
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
		      String joinCmd = "select ";
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
		
		      /****************************************************************************/
		
		
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
		      String YearsToQuery = "";
		
		      if(YearsSelected.size() > 0){
		        YearsToQuery = " where ";
		        for(int i = 0; i < (YearsSelected.size()-1); i++){
		          YearsToQuery += (input+".year="+YearsSelected.get(i) + " or ");
		        }
		        YearsToQuery += (input+".year="+YearsSelected.get(YearsSelected.size()-1));
		      }
		
		      /****************************************************************************/
		      joinCmd += YearsToQuery;
		      /***************************************************************************/
		
		          //create a statement object
		       
		      
		
		       
		       //drop down to select number of results
		       String[] numEnt = {"1","2","3","4","5","10","20","30","40","50","100","250","500", "ALL"};
		       String input_numEnt = (String) JOptionPane.showInputDialog(
		       null, "Select number of results desired...", "Number of Results", JOptionPane.QUESTION_MESSAGE, null, numEnt, numEnt[0]);
		       System.out.println(input_numEnt);
		       
		       //send output to file or on screen
		       String[] typeChoices = {"On Screen", "out.txt"};
		       String outputType = (String) JOptionPane.showInputDialog(
		       null, "Select output type...", "Output Type", JOptionPane.QUESTION_MESSAGE, null, typeChoices, typeChoices[0]);
		       System.out.println(outputType);
		
		       //sql command
		       // String sqlStatement = "";
		       // if(input == "offensive_records")
		       //    sqlStatement = "SELECT player_code FROM offensive_records limit " + input_numEnt;
		       // else if(input == "team_code")
		       //    sqlStatement = "SELECT name FROM teams limit " + input_numEnt;
		       // //send statement to DBMS
		       // ResultSet result = stmt.executeQuery(sqlStatement);
		
		       // //creating output
		       // while (result.next()) {
		       //  // if(input == "offensive_records")
		       //   finalout += result.getString("player_code")+"\n";
		        
		       //  else if(input == "team_code")
		       //    finalout += result.getString("name")+"\n";
		        
		       //determine output type
		       if(outputType == "On Screen") 
		         temp_outputType = "On Screen";
		       else if(outputType == "out.txt")
		         temp_outputType = "out.txt";
		       // }
		
		      /****************************************************************************/
		
		      if(input_numEnt.equals("ALL"))
            joinCmd = joinCmd;
          else
            joinCmd += " limit "+input_numEnt;

		      if(colToJoin.size() ==0)
		        joinCmd = "select * from teams limit 0";
		      // System.out.println(joinCmd);
		
		
		            // sets up SQL input and obtains result, then formats it
          if(input_numEnt.equals("ALL"))
            sqlStatement = "Select " + columnsSelected + "From " + input + " "+YearsToQuery;
          else
            sqlStatement = "Select " + columnsSelected + "From " + input + " "+YearsToQuery+" limit "+input_numEnt;
		      // System.out.println(sqlStatement); // used for debugging
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
		
		
		
		
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error accessing Database.");
				}
		    if (colToJoin.size() == 0){
				  // JOptionPane.showMessageDialog(null, output);
		      finalout = output;
		    }
		
		
		         //on screen output
		    if(temp_outputType == "On Screen") {
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
		     else if (temp_outputType == "out.txt") {
		       //send output to text file
		       PrintWriter out = new PrintWriter("out.txt");
		       out.println(finalout);
		       out.close();
		     }
		
		    	break;
		    case "Question 1":
		    	try {
					stmt = conn.createStatement();
				
				//CODE HERE	
					
		    	} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error accessing Database.");
				}
		    	
		   		break;
		   		
		    case "Question 3":
		    	try {
					stmt = conn.createStatement();
				
				//CODE HERE	
					
		    	} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error accessing Database.");
				}
		    	
		   		break;
		   		
		    case "Question 4":
		    	try {
					stmt = conn.createStatement();
				
				//CODE HERE	
					
		    	} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error accessing Database.");
				}
		    	
		   		break;
		   		
		    case "Question 5":
		    	try {
					stmt = conn.createStatement();
				
				//CODE HERE	
					
		    	} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error accessing Database.");
				}
		    	
		   		break;
		   		
		    case "Bonus":
		    	try {
					stmt = conn.createStatement();
				
				//CODE HERE	
					
		    	} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error accessing Database.");
				}
		    	
		   		break;
		   		
		    default:
		    	break;
			}
		// closing the connection
		try {
			conn.close();
			JOptionPane.showMessageDialog(null, "Connection Closed.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Connection NOT Closed.");
		} // end try catch
	}// end main
}// end Class