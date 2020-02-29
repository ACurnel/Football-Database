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
			// REQUEST COLUMN LIST
			sqlStatement = "Select * from " + input + " limit 0";
			ResultSet columns = stmt.executeQuery(sqlStatement);
			ResultSetMetaData columnstuff = columns.getMetaData();
			int colcnt = columnstuff.getColumnCount();
			ArrayList<String> columnlist = new ArrayList<String>();
			for (int i = 1; i < colcnt; i++) {
				columnlist.add(columnstuff.getColumnName(i));
			}
			/*JOptionPane.showMessageDialog(null, "Available Columns:\n");
			for (int j = 0; j < columnlist.size(); j++) {
			//System.out.println(columnlist.get(j) + "\n");
				output += columnlist.get(j) + "\n";
			}*/
			ArrayList<JCheckBox> ColumnBoxes = new ArrayList<JCheckBox>();
			Object[] columncontent = new Object[2*columnlist.size()];
			for (int i = 0; i < columnlist.size(); i++) {
				ColumnBoxes.add(new JCheckBox());
				columncontent[(2*i)] = columnlist.get(i);
				columncontent[(2*i)+1] = ColumnBoxes.get(i);
			}
			JOptionPane.showConfirmDialog(null, columncontent, "Which Columns Would You Like To Display?", JOptionPane.OK_OPTION);
			ArrayList<Boolean> columnchecks = new ArrayList<Boolean>();
			for (int i = 0; i < columnlist.size(); i++) {
				columnchecks.add(ColumnBoxes.get(i).isSelected());
			}
			String columnsSelected = "";
			ArrayList<String> SelectedColumnList = new ArrayList<String>();
			for (int i = 0; i < columnlist.size(); i++) {
				if (columnchecks.get(i))
					SelectedColumnList.add(columnlist.get(i));
			}
			System.out.println(SelectedColumnList.size());
			for (int i = 0; i < SelectedColumnList.size(); i++) {
				if (i!=SelectedColumnList.size()-1)
					columnsSelected = columnsSelected + SelectedColumnList.get(i) + ",";
				else
					columnsSelected = columnsSelected + SelectedColumnList.get(i) + " ";
			}
			//if (columnchecks.get(columnlist.size()-1))
				//columnsSelected += columnlist.get(columnlist.size()-1);
			sqlStatement = "Select " + columnsSelected + "From " + input + " limit 10;";
			System.out.println(sqlStatement);
			ResultSet columnsResult = stmt.executeQuery(sqlStatement);
			for (int i = 0; i < SelectedColumnList.size(); i++) {
				if (i!=SelectedColumnList.size()-1)
					output = output + SelectedColumnList.get(i) + ", ";
				else
					output = output + SelectedColumnList.get(i);
			}
			output += "\n______________________________________________\n";
			while(columnsResult.next()) {
				for (int i = 0; i < SelectedColumnList.size(); i++) {
					if (i!=SelectedColumnList.size()-1)
						output = output + columnsResult.getString(SelectedColumnList.get(i)) + ", ";
					else
						output = output + columnsResult.getString(SelectedColumnList.get(i));
				}
				output += "\n";
			}
			
			/*if (input == "offensive_records")
				sqlStatement = "SELECT player_code FROM offensive_records limit 10";
			else if (input == "team_code")
				sqlStatement = "SELECT name FROM teams limit 10";
			// send statement to DBMS
			ResultSet result = stmt.executeQuery(sqlStatement);

			// OUTPUT
			JOptionPane.showMessageDialog(null, "Player code from offensive_records");
			// System.out.println("______________________________________");
			while (result.next()) {
				// System.out.println(result.getString("cus_lname"));
				if (input == "offensive_records")
					output += result.getString("player_code") + "\n";
				else if (input == "team_code")
					output += result.getString("name") + "\n";
			}*/
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error accessing Database.");
		}
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
