/*
*Semester Project
*Name: David Saechao
*Section number: 4
*/

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class Bank implements ActionListener {
	static CardLayout contentPaneLayout;
	static int row = 0;
	static float updatedBal = 0;	//Keep track of updated balance
	static DecimalFormat df = new DecimalFormat("#################0.00");	//Format to two decimal places
	static Object[] emptyRow = new Object[7];	//Used to create an empty row on table
	static ArrayList<Transactions> transactionsSave = new ArrayList<Transactions>();	//Keep track of transactions
	static String[] transTypeH = {"Deposit", "Automatic Deposit", "ATM Withdrawl", "Check", "Debit Card"};	//Choices for JCombo box
	static String[] searchTableH = {"Date", "Trans Type.", "Check No.", "Trans. Description", "Payment/Debit(-)", "Deposit/Credit(+)", "Balance"}; //Column headers for table
	static JComboBox<String> transCombo = new JComboBox<String>(transTypeH);
	static DefaultTableModel searchDefTable = new DefaultTableModel(searchTableH,0);
	static JTable searchTable = new JTable(searchDefTable);
	static DefaultTableModel viewDeleteDefTable = new DefaultTableModel(searchTableH,0);
	static JTable viewDeleteTable = new JTable(viewDeleteDefTable);
	static JTextField accountNametf = new JTextField(15);
	static JTextField createAccNametf = new JTextField(15);	
	static JTextField createInitBaltf = new JTextField(15);
	static JTextField loadTranstf = new JTextField(15);
	static JTextField transSaveDatetf = new JTextField(15);
	static JTextField transSaveCheckNotf = new JTextField(15);
	static JTextField transSaveDescrtf = new JTextField(15);
	static JTextField transSavePayDebtf = new JTextField(15);
	static JTextField transSaveDepCredtf = new JTextField(15);
	static JTextField balancetf = new JTextField("0.0", 15);
	static JButton createButton = new JButton("Create an account");
	static JButton transButton = new JButton("Load trans from a file");
	static JButton addTransButton = new JButton("Add New Transactions");
	static JButton searchTransButton = new JButton("Search Transactions");
	static JButton sortTransButton = new JButton("Sort Transactions");
	static JButton viewDeleteTransButton = new JButton("View/Delete Transactions");
	static JButton backupTransButton = new JButton("Backup Transactions");
	static JButton exitButton = new JButton("Exit");
	static JButton createCreateButton = new JButton("Create");
	static JButton createCancelButton = new JButton("Cancel");
	static JButton transLoadButton = new JButton("Load");
	static JButton transCancelButton = new JButton("Cancel");
	static JButton transSaveButton = new JButton("Save New Transaction");
	static JButton transTopMenuButton = new JButton("Top Menu");
	static JButton searchButton = new JButton("Search");
	static JButton searchTopMenuButton = new JButton("Top Menu");
	static JButton sortButton = new JButton("Sort");
	static JButton sortTopMenuButton = new JButton("Top Menu");
	static JButton deleteSelectedButton = new JButton("Delete Selected");
	static JButton viewDelTopMenuButton = new JButton("Top Menu");
	static JPanel main = new JPanel();
	static JPanel addNewTransP = new JPanel(new BorderLayout());
	static JPanel searchTransP = new JPanel(new BorderLayout());
	static JPanel sortTransP = new JPanel(new BorderLayout());
	static JPanel viewDeleteTransP = new JPanel(new BorderLayout());
	static JPanel contentPanel;

 	public void actionPerformed(ActionEvent a) {
		Object source = a.getSource();
		if (source==createCancelButton||source==transCancelButton||source==transTopMenuButton||
			source==searchTopMenuButton||source==sortTopMenuButton||source==viewDelTopMenuButton) {
			contentPaneLayout.show(contentPanel, "Checkbook start");
		}
		if (source==createButton) {
			contentPaneLayout.show(contentPanel, "Create");
		}
		if (source==createCreateButton) {
			try {
				String tempName = createAccNametf.getText();
				float init = Float.parseFloat(createInitBaltf.getText());
				String tempBal = df.format(init); //Format to 2 decimal places
				accountNametf.setText(tempName);
				balancetf.setText(tempBal);
				} catch (NumberFormatException e) {
					System.out.println("Check your inputs on creating an account");
				}
			contentPaneLayout.show(contentPanel, "Checkbook start");
		}
		if (source==transButton) {
			contentPaneLayout.show(contentPanel, "Load Transactions");
		}
		if (source==transLoadButton) {	//Load transactions from file
			try {    
				String file = loadTranstf.getText(); 
				FileInputStream fis = new FileInputStream (file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				accountNametf.setText(file);
				Transactions obj;
				viewDeleteDefTable.setRowCount(0);	//Reset table by deleting all rows
				row = 0;		//Reset row count
				while (true) {
					obj = (Transactions)ois.readObject();
					if (obj == null) {
						break;
					}
					String saveDate = obj.getDate();
					String transType = obj.getType();
					int checkNo = obj.getCheckNo();
					String transDescription = obj.getTransDescription();				
					float payDeb = obj.getPayDeb();
					float depCred = obj.getDepCred();
					float amount = obj.getUpdatedBal();
					balancetf.setText(""+amount+updatedBal);	
					viewDeleteDefTable.addRow(emptyRow);
					viewDeleteDefTable.setValueAt(saveDate,row,0);
					viewDeleteDefTable.setValueAt(transType,row,1);
					viewDeleteDefTable.setValueAt(transDescription,row,3);
					viewDeleteDefTable.setValueAt(amount,row,6);
					//Check to see if values were used
					if (checkNo == 0) {
						viewDeleteDefTable.setValueAt("",row,2);
					} else {
						viewDeleteDefTable.setValueAt(checkNo,row,2);
					}
					if (payDeb == 0) {
						viewDeleteDefTable.setValueAt("",row,4);
					} else {
						viewDeleteDefTable.setValueAt(payDeb,row,4);
					}
					if (depCred == 0) {
						viewDeleteDefTable.setValueAt("",row,5);
					} else {
						viewDeleteDefTable.setValueAt(depCred,row,5);
					}
					row++;
				}
				ois.close();
				} catch(EOFException e) {
					System.out.println(e.toString());
					contentPaneLayout.show(contentPanel, "Checkbook start");
				} catch(ClassNotFoundException e) {
					System.out.println(e.toString());
				} catch(FileNotFoundException e){
					 System.out.println(e.toString());
				} catch(IOException e){
					 e.printStackTrace();
				}
		}
		if (source==addTransButton) {
			contentPaneLayout.show(contentPanel, "Add New Transactions");
		}
		if (source==transSaveButton) {
			try {
				String saveDate = transSaveDatetf.getText();
				String transType = transCombo.getSelectedItem().toString();
				String transDescription = transSaveDescrtf.getText();
				String strBal = balancetf.getText();	//String representation of updated balance used to format decimal places
				updatedBal = Float.parseFloat(strBal);
				switch (transType) {	//Depending on transaction type perform certain operations and make table
					case "Deposit": case "Automatic Deposit":	
					String strDepCred = df.format(Float.parseFloat(transSaveDepCredtf.getText()));
					float depCred = Float.parseFloat(strDepCred);
					strBal = df.format(updatedBal + depCred);
					updatedBal = Float.parseFloat(strBal);					
					balancetf.setText(""+updatedBal);
					transactionsSave.add(new Transactions(saveDate,transType,0,transDescription,0,depCred,updatedBal));
					viewDeleteDefTable.addRow(emptyRow);
					viewDeleteDefTable.setValueAt(saveDate,row,0);
					viewDeleteDefTable.setValueAt(transType,row,1);
					viewDeleteDefTable.setValueAt(transDescription,row,3);
					viewDeleteDefTable.setValueAt(depCred,row,5);
					viewDeleteDefTable.setValueAt(updatedBal,row,6);
					break;
					case "Check":	
					int checkNo = Integer.parseInt(transSaveCheckNotf.getText());
					String strPayDeb = df.format(Float.parseFloat(transSavePayDebtf.getText()));
					float payDeb = Float.parseFloat(strPayDeb);
					strBal = df.format(updatedBal - payDeb);
					updatedBal = Float.parseFloat(strBal);
					balancetf.setText(""+updatedBal);
					transactionsSave.add(new Transactions(saveDate,transType,checkNo,transDescription,payDeb,0,updatedBal));
					viewDeleteDefTable.addRow(emptyRow);
					viewDeleteDefTable.setValueAt(saveDate,row,0);
					viewDeleteDefTable.setValueAt(transType,row,1);
					viewDeleteDefTable.setValueAt(checkNo,row,2);
					viewDeleteDefTable.setValueAt(transDescription,row,3);
					viewDeleteDefTable.setValueAt(payDeb,row,4);
					viewDeleteDefTable.setValueAt(updatedBal,row,6);
					break;
					case "ATM Withdrawl": case "Debit Card":
					String strPayDeb1 = df.format(Float.parseFloat(transSavePayDebtf.getText()));
					float payDeb1 = Float.parseFloat(strPayDeb1);
					strBal = df.format(updatedBal - payDeb1);
					updatedBal = Float.parseFloat(strBal);
					balancetf.setText(""+updatedBal);
					transactionsSave.add(new Transactions(saveDate,transType,0,transDescription,payDeb1,0,updatedBal));
					viewDeleteDefTable.addRow(emptyRow);
					viewDeleteDefTable.setValueAt(saveDate,row,0);
					viewDeleteDefTable.setValueAt(transType,row,1);
					viewDeleteDefTable.setValueAt(transDescription,row,3);
					viewDeleteDefTable.setValueAt(payDeb1,row,4);
					viewDeleteDefTable.setValueAt(updatedBal,row,6);
					break;
				}
				row++;	//Increment row for next row of transactions to be added onto table
				transCombo.setSelectedIndex(0);	//Reset JComboBox to first item
				transSaveDatetf.setText("");			//Clear all text
				transSaveCheckNotf.setText("");
				transSaveDescrtf.setText("");
				transSavePayDebtf.setText("");
				transSaveDepCredtf.setText("");
				} catch (NumberFormatException e) {
					System.out.println("Check your inputs");
				}
				contentPaneLayout.show(contentPanel, "Checkbook start");
			}
		if (source==searchTransButton) {
			contentPaneLayout.show(contentPanel, "Search Transactions");
		}
		if (source==sortTransButton) {
			contentPaneLayout.show(contentPanel, "Sort Transactions");
		}
		if (source==viewDeleteTransButton) {	
			contentPaneLayout.show(contentPanel, "View/Delete Transactions");
		}
		if (source==deleteSelectedButton) {			//Delete selected row
			viewDeleteDefTable.removeRow(viewDeleteTable.getSelectedRow());
			row--;
		}
		if (source==backupTransButton) {	//Save to file
			String file = accountNametf.getText();
			try {	
				FileOutputStream fos = new FileOutputStream (file, false);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				Transactions[] transactions = transactionsSave.toArray(new Transactions[transactionsSave.size()]);
				for (int i = 0; i < transactions.length;i++) {
					oos.writeObject(transactions[i]);
				}
				oos.close();
				} catch(FileNotFoundException e){
					 System.out.println(e.toString());
				} catch(NotSerializableException e) {
					 System.out.println(e.toString());
				} catch(IOException e){
					 e.printStackTrace();
				}
		}
		if (source==exitButton) {
			System.exit(0);
		}
	}			 
		public static void main(String[] args) {
		
		JFrame frame = new JFrame("Checkbook");
		contentPanel = (JPanel) frame.getContentPane();
		contentPanel.setLayout(contentPaneLayout=new CardLayout());
		frame.setSize(750,245);
		Container contentPane = frame.getContentPane();
		
		//Main window
		JPanel topPanel = new JPanel(new GridLayout(1, 0));
		JLabel title = new JLabel("Use The Buttons below To Manage Transactions", JLabel.CENTER);
		title.setFont(new Font("", Font.BOLD, 16));
		topPanel.add(title);
		
		JPanel centerPanel = new JPanel(new FlowLayout());
		JLabel accountNameLabel = new JLabel("Account Name:");
		accountNametf.setHorizontalAlignment(JTextField.RIGHT);
		accountNametf.setEditable(false);
		JLabel balanceLabel = new JLabel("Balance:");
		balancetf.setHorizontalAlignment(JTextField.RIGHT);
		balancetf.setEditable(false);
		
		centerPanel.add(accountNameLabel);
		centerPanel.add(accountNametf);
		centerPanel.add(balanceLabel);
		centerPanel.add(balancetf);
	
		JPanel bottomPanel = new JPanel(new GridLayout(2,4));
		bottomPanel.add(createButton);
		bottomPanel.add(transButton);
		bottomPanel.add(addTransButton);
		bottomPanel.add(searchTransButton);
		bottomPanel.add(sortTransButton);
		bottomPanel.add(viewDeleteTransButton);
		bottomPanel.add(backupTransButton);
		bottomPanel.add(exitButton);
		frame.setVisible(true);
		
		Box mainP = Box.createVerticalBox();
		mainP.add(topPanel);
		mainP.add(centerPanel);
		main.add(mainP);
		main.add(bottomPanel);
		
		//Create Account card
		Box createP = Box.createVerticalBox();
		JPanel createTop = new JPanel(new GridLayout(1,0));
		JLabel createLabel = new JLabel("Create a New account", JLabel.CENTER);
		createLabel.setFont(new Font("", Font.BOLD, 16));
		createTop.add(createLabel);
		
		JPanel createCenter1 = new JPanel(new FlowLayout());		
		createCenter1.add(new JLabel("Account Name:"));
		createCenter1.add(createAccNametf);
		JPanel createCenter2 = new JPanel(new FlowLayout());
		createCenter2.add(new JLabel("Initial Balance:"));
		createCenter2.add(createInitBaltf);
		
		JPanel createCenter = new JPanel(new GridLayout(2,0));
		createCenter.add(createCenter1);
		createCenter.add(createCenter2);
		
		JPanel createBottom = new JPanel(new FlowLayout());
		createBottom.add(createCreateButton);
		createBottom.add(createCancelButton);
		
		createP.add(createTop);
		createP.add(createCenter);
		createP.add(createBottom);
		
		//Load transactions card
		Box transP = Box.createVerticalBox();
		JPanel transTop = new JPanel(new GridLayout(1,0));
		JLabel loadLabel = new JLabel("Load Transactions From a File", JLabel.CENTER);
		loadLabel.setFont(new Font("", Font.BOLD, 16));
		transTop.add(loadLabel);
		
		JPanel transCenter = new JPanel(new FlowLayout());
		transCenter.add(new JLabel("Account Name:"));
		transCenter.add(loadTranstf);
		
		JPanel transBottom = new JPanel(new FlowLayout());
		transBottom.add(transLoadButton);
		transBottom.add(transCancelButton);
		
		transP.add(transTop);
		transP.add(transCenter);
		transP.add(transBottom);
		
		//Add new transactions card
		JPanel addNewTransCenter = new JPanel(new GridLayout(6,2));
		addNewTransCenter.add(new JLabel("Date", JLabel.RIGHT));
		addNewTransCenter.add(transSaveDatetf);
		addNewTransCenter.add(new JLabel("Trans. type", JLabel.RIGHT));
		addNewTransCenter.add(transCombo);
		addNewTransCenter.add(new JLabel("Check No.", JLabel.RIGHT));
		addNewTransCenter.add(transSaveCheckNotf);
		addNewTransCenter.add(new JLabel("Trans. Description", JLabel.RIGHT));
		addNewTransCenter.add(transSaveDescrtf);
		addNewTransCenter.add(new JLabel("Payment/Debit(-)", JLabel.RIGHT));
		addNewTransCenter.add(transSavePayDebtf);
		addNewTransCenter.add(new JLabel("Deposit/Credit(+)", JLabel.RIGHT));
		addNewTransCenter.add(transSaveDepCredtf);
		
		JPanel addNewTransBottom = new JPanel(new FlowLayout());
		addNewTransBottom.add(transSaveButton);
		addNewTransBottom.add(transTopMenuButton);
		
		addNewTransP.add(addNewTransCenter,BorderLayout.CENTER);
		addNewTransP.add(addNewTransBottom, BorderLayout.SOUTH);		
		
		//Search transactions card
		JPanel searchTransTop = new JPanel();
		searchTransTop.add(new JLabel("Search Transactions by Transaction Date/Type/Check No./Description", JLabel.CENTER));
		
		JScrollPane searchScrollPane = new JScrollPane();
		searchTransP.add(searchScrollPane, BorderLayout.CENTER);
		JScrollPane tmp = new JScrollPane(searchTable);
		searchScrollPane.setViewport(tmp.getViewport());
		searchTransP.add(searchScrollPane);
		
		Box searchTransBottom = Box.createVerticalBox();
		JPanel searchTransString = new JPanel(new FlowLayout());
		searchTransString.add(new JLabel("Search String:"));
		searchTransString.add(new JTextField(15));
		JPanel searchTransButtonP = new JPanel(new FlowLayout());
		searchTransButtonP.add(searchButton);
		searchTransButtonP.add(searchTopMenuButton);
		
		searchTransBottom.add(searchTransString);
		searchTransBottom.add(searchTransButtonP);
		
		searchTransP.add(searchTransTop, BorderLayout.NORTH);
		searchTransP.add(searchTransBottom, BorderLayout.SOUTH);
		
		//Sort transactions card
		JPanel sortTransButtonP = new JPanel(new FlowLayout()); 
		JRadioButton byType = new JRadioButton("By Type", true);
		JRadioButton byDate = new JRadioButton("By Date");
		sortTransButtonP.add(byType);
		sortTransButtonP.add(byDate);
				
		Box sortTransTop = Box.createVerticalBox();
		JLabel sortLabel = new JLabel("Sort Transactions", JLabel.CENTER);
		sortLabel.setFont(new Font("", Font.BOLD, 16));
		sortLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		sortTransTop.add(sortLabel);
		sortTransTop.add(sortTransButtonP);
		
		JPanel sortTransBottom = new JPanel(new FlowLayout());
		sortTransBottom.add(sortButton);
		sortTransBottom.add(sortTopMenuButton);
		
		sortTransP.add(sortTransTop, BorderLayout.NORTH);
		sortTransP.add(sortTransBottom, BorderLayout.SOUTH);
		
		//View and delete transactions card
		JPanel viewDeleteTransTop = new JPanel();
		viewDeleteTransTop.add(new JLabel("Transactions Currently in the Checkbook", JLabel.CENTER));
		
		JScrollPane viewDeleteScrollPane = new JScrollPane();
		viewDeleteTransP.add(viewDeleteScrollPane, BorderLayout.CENTER);
		JScrollPane tmp1 = new JScrollPane(viewDeleteTable);
		viewDeleteScrollPane.setViewport(tmp1.getViewport());
		viewDeleteTransP.add(viewDeleteScrollPane);
		
		JPanel viewDeleteTransBottom = new JPanel(new FlowLayout());
		viewDeleteTransBottom.add(deleteSelectedButton);
		viewDeleteTransBottom.add(viewDelTopMenuButton);
		
		viewDeleteTransP.add(viewDeleteTransTop, BorderLayout.NORTH);
		viewDeleteTransP.add(viewDeleteTransBottom, BorderLayout.SOUTH);
		
		//Adding cards
		contentPanel.add("Checkbook start", main);
		contentPanel.add("Create", createP);
		contentPanel.add("Load Transactions", transP);
		contentPanel.add("Add New Transactions", addNewTransP);
		contentPanel.add("Search Transactions", searchTransP);
		contentPanel.add("Sort Transactions", sortTransP);
		contentPanel.add("View/Delete Transactions", viewDeleteTransP);
		contentPaneLayout.show(contentPane, "Card 1");
		
		//Registering buttons to action listener
		ActionListener AL = new Bank();
		createButton.addActionListener(AL);
		createCreateButton.addActionListener(AL);
		createCancelButton.addActionListener(AL);
		transButton.addActionListener(AL);
		transSaveButton.addActionListener(AL);
		transLoadButton.addActionListener(AL);
		transCancelButton.addActionListener(AL);
		addTransButton.addActionListener(AL);
		transCombo.addActionListener(AL);
		transTopMenuButton.addActionListener(AL);
		searchTransButton.addActionListener(AL);
		searchTopMenuButton.addActionListener(AL);
		sortTransButton.addActionListener(AL);
		sortTopMenuButton.addActionListener(AL);
		viewDeleteTransButton.addActionListener(AL);
		deleteSelectedButton.addActionListener(AL);
		viewDelTopMenuButton.addActionListener(AL);
		backupTransButton.addActionListener(AL);
		exitButton.addActionListener(AL);
		frame.setVisible(true);
	}
}