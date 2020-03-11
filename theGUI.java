package localGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class theGUI extends JFrame {

	private JPanel contentPane;
	private JTable hfaTable;
	private JTable vcTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		
		JLabel generalLabel2 = new JLabel("Do NOT go back up once you have already moved forward. If you do not select anything, defaults are in place. ");
		generalLabel2.setBounds(6, 24, 697, 16);
		generalPanel.add(generalLabel2);
		
		
		//entity code goes here
		JButton entityBtn = new JButton("Choose Entity");
		entityBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new chooseEntity();
				chooseEntity.chooseEntityScreen();
			}
		});
		entityBtn.setBounds(275, 64, 150, 29);
		generalPanel.add(entityBtn);
		
		
		//column code goes here
		JButton columnsBtn = new JButton("Choose Columns");
		columnsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new chooseColumns();
				chooseColumns.chooseColumnsScreen();
			}
		});
		columnsBtn.setBounds(275, 105, 150, 29);
		generalPanel.add(columnsBtn);
		
		
		//join code goes here
		JButton joinBtn = new JButton("Choose Joins");
		joinBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new chooseJoins();
				chooseJoins.chooseJoinsScreen();
			}
		});
		joinBtn.setBounds(275, 146, 150, 29);
		generalPanel.add(joinBtn);
		
		
		//years code goes here
		JButton yearsBtn = new JButton("Choose Years");
		yearsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new chooseYears();
				chooseYears.chooseYearsScreen();
			}
		});
		yearsBtn.setBounds(275, 187, 150, 29);
		generalPanel.add(yearsBtn);
		
		
		//conditional code goes here
		JButton conditionalBtn = new JButton("Choose Conditional");
		conditionalBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new chooseConditional();
				chooseConditional.chooseConditionalScreen();
			}
		});
		conditionalBtn.setBounds(275, 228, 150, 29);
		generalPanel.add(conditionalBtn);
		
				
		//limit code goes here
		JButton outLimitBtn = new JButton("Choose Limit");
		outLimitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new chooseLimit();
				chooseLimit.chooseLimitScreen();
			}
		});
		outLimitBtn.setBounds(275, 269, 150, 29);
		generalPanel.add(outLimitBtn);
		
		
		//output code goes here
		JButton outputBtn = new JButton("Generate Result");
		outputBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new generateResult();
				generateResult.generateResultScreen();
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
