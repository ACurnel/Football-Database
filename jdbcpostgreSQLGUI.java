import java.sql.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;


//import java.sql.DriverManager;
/*
Robert lightfoot
CSCE 315
9-25-2019
 */
public class jdbcpostgreSQLGUI {
	public static void main(String args[]) {
		dbSetup my = new dbSetup();
    ArrayList<String> colToJoin = new ArrayList();
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
		JOptionPane.showMessageDialog(null, "Opened database successfully");
		String output = "";
		try {
			// create a statement object
			Statement stmt = conn.createStatement();
			// create an SQL statement
			// ENTITIES
			String[] choices = { "Players", "Teams", "Seasons", "Player_Records", "Games", "Stadiums", "Positions", "Offensive_Records", "Defensive_Records", "Conferences", "Statistics", "Drives" };
			String input = (String) JOptionPane.showInputDialog(null, "Choose now...", "What are you interested in looking up?",
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
			for (int i = 1; i < colcnt; i++) {
				columnlist.add(columnstuff.getColumnName(i));
			}
			// Creates check box options for each column by adding the new box and column name
			ArrayList<JCheckBox> ColumnBoxes = new ArrayList<JCheckBox>(); // holds check boxes
			Object[] columncontent = new Object[2*columnlist.size()];
			for (int i = 0; i < columnlist.size(); i++) {
				ColumnBoxes.add(new JCheckBox());
				columncontent[(2*i)] = columnlist.get(i);
				columncontent[(2*i)+1] = ColumnBoxes.get(i);
			}
			// Displays dialog box showing column check boxes, then creates list of desired columns
			JOptionPane.showConfirmDialog(null, columncontent, "Which Columns Would You Like To Display?", JOptionPane.OK_OPTION);
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
			
			// sets up SQL input and obtains result, then formats it
			sqlStatement = "Select " + columnsSelected + "From " + input + " limit 10;";
			// System.out.println(sqlStatement); // used for debugging
			ResultSet columnsResult = stmt.executeQuery(sqlStatement);
			// outputs column headers
			for (int i = 0; i < SelectedColumnList.size(); i++) {
				if (i!=SelectedColumnList.size()-1)
					output = output + SelectedColumnList.get(i) + ", ";
				else
					output = output + SelectedColumnList.get(i);
			}
			output += "\n______________________________________________\n";
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
        if(i.contains("code")){
          JCheckBox Box = new JCheckBox(i); 
          joinCheck.add(Box);
          columncontentJoin[(2*countT)+1] = joinCheck.get(countT++);
          colDisplayed.add(i);
        }
      }
      JOptionPane.showConfirmDialog(null, columncontentJoin, "Which Columns Would You Like To Join?", JOptionPane.DEFAULT_OPTION);
      
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
          joinCmd += ("teams on " + input +".team_code = teams.team_code limit 10");


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
          joinCmd += ("players on "+ input +".player_code = players.player_code limit 10");
        }
      }
      System.out.println(joinCmd);
      ResultSet joinRes = stmt.executeQuery(joinCmd);
      ResultSetMetaData joinResmd = joinRes.getMetaData();

      String joinResStr = "";

      while(joinRes.next()){
        for(int i = 1; i <= joinResmd.getColumnCount(); i++){
          if(i != joinResmd.getColumnCount())
            joinResStr += joinRes.getString(i)+", ";
          else
            joinResStr += joinRes.getString(i);
        }
        joinResStr += "\n";
      }

      JOptionPane.showMessageDialog(null, joinResStr);



      /****************************************************************************/

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error accessing Database.");
		}
    if (colToJoin.size() == 0)
		  JOptionPane.showMessageDialog(null, output);
		// closing the connection
		try {
			conn.close();
			JOptionPane.showMessageDialog(null, "Connection Closed.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Connection NOT Closed.");
		} // end try catch
	}// end main
}// end Class
