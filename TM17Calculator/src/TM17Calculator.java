import java.awt.EventQueue;
import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ItemListener;
import java.io.UnsupportedEncodingException;
import java.awt.event.ItemEvent;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TM17Calculator {

	private JFrame frame;
	private String address;
	private JTextField textField;

	/*** Launch the application. */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TM17Calculator window = new TM17Calculator();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/*** Create the application. */
	public TM17Calculator() {
		initialize();
	}

	/*** Initialize the contents of the frame. */

	private void initialize() {

		// load up arraylists with data from db
		// in the future, maybe load data dynamically instead of storing it all at once

		ArrayList<String> pokemon = new ArrayList<String>(255);
		ArrayList<String> moves = new ArrayList<String>(255);
		ArrayList<String> items = new ArrayList<String>(255);

		String jdbc = "jdbc:derby:database;create=true;useUnicode=yes;characterEncoding=UTF-8";

		try {
			Connection connection = DriverManager.getConnection(jdbc);

			// add pokemon to the pokemon arraylist
			String sql = "SELECT pokemon FROM table1";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				pokemon.add(rs.getString("pokemon"));
			}

			// add moves to the move arraylist
			sql = "SELECT move FROM table1";
			rs = statement.executeQuery(sql);

			while (rs.next()) {
				moves.add(rs.getString("move"));
			}

			// add items to the items arraylist
			sql = "SELECT item FROM table1";
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				items.add(rs.getString("item"));
			}

//			  String shutdownURL = "jdbc:derby:;shutdown=true";
//			  DriverManager.getConnection(shutdownURL);

		} catch (SQLException e) {

			if (e.getSQLState().equals("XJ015")) {
				System.out.println("DerbyShutdown.");
			} else {
				e.printStackTrace();
			}
		}

		// create GUI elements
		frame = new JFrame();
		frame.getContentPane().setEnabled(false);
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblFinalBoxName = new JLabel("Result shows up here");
		lblFinalBoxName.setBounds(436, 472, 135, 25);
		frame.getContentPane().add(lblFinalBoxName);

		// default selection is "change species" as seen in the next code block, so show
		// pokemon list by default
		JComboBox comboBoxChoiceTwo = new JComboBox();
		comboBoxChoiceTwo.setToolTipText("Pokemon/Item/Move that you want");
		comboBoxChoiceTwo.setModel(new DefaultComboBoxModel(pokemon.toArray()));
		comboBoxChoiceTwo.setBounds(21, 195, 152, 33);
		frame.getContentPane().add(comboBoxChoiceTwo);
		setAddress("?,?e");

		// Options that are planned to be supported, stat exp is not yet implemented.
		// Add array elements to the combo box to start.
		String choiceOneOptions[] = { "Change Species", "Change Held Item", "Move 1", "Move 2", "Move 3", "Move 4",
				"HP Stat exp", "Attack Stat exp", "Defense Stat exp", "Speed Stat exp", "Special Stat exp",
				"Atk/Def DVs", "Spe/Spc DVs", "Friendship", "Pokerus", "Level", "Item #2 ID", "Item #2 Quantity", "Ball Item #2 ID", "Ball Item #2 Quantity"};
		JComboBox comboBoxChoiceOne = new JComboBox(choiceOneOptions);
		comboBoxChoiceOne.setToolTipText("Edit Party Pokemon #3");

		comboBoxChoiceOne.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {
					String choice = comboBoxChoiceOne.getSelectedItem().toString();

					// when a choice is made, see what the user clicked on and change info in the
					// other combo box
					// for example if Change Species is clicked on right here it shows list of
					// pokemon

					textField.setEnabled(false);
					textField.setEditable(false);
					textField.setVisible(false);
					comboBoxChoiceTwo.setEnabled(true);
					
					if (choice == "Change Species") {
						comboBoxChoiceTwo.setModel(new DefaultComboBoxModel(pokemon.toArray()));
						setAddress("?,?e");
					} else if (choice == "Change Held Item") {
						comboBoxChoiceTwo.setModel(new DefaultComboBoxModel(items.toArray()));
						setAddress("?,?f");
					}

					else if (choice.contains("Move")) {
						comboBoxChoiceTwo.setModel(new DefaultComboBoxModel(moves.toArray()));

						if (choice == "Move 1")
							setAddress("?,?g");
						if (choice == "Move 2")
							setAddress("?,?h");
						if (choice == "Move 3")
							setAddress("?,?i");
						if (choice == "Move 4")
							setAddress("?,?j");
					}

					else if (choice == "Item #2 ID") {
						comboBoxChoiceTwo.setModel(new DefaultComboBoxModel(items.toArray()));
						setAddress("PK,'s?");
					} else if (choice == "Ball Item #2 ID") {
						comboBoxChoiceTwo.setModel(new DefaultComboBoxModel(items.toArray()));
						setAddress("PK, A");
					}

					else if (choice.contains("exp")) {
						textField.setVisible(true);
						textField.setEnabled(true);
						textField.setEditable(true);
						
						comboBoxChoiceTwo.setEnabled(false);

						if (choice == "HP Stat exp")	
							setAddress(" ?,?p");
						else if (choice == "Attack Stat exp")
							setAddress("?,?r");
						else if (choice == "Defense Stat exp")
							setAddress("?,?t");
						else if (choice == "Speed Stat exp")
							setAddress("?,?v");
						else if (choice == "Special Stat exp")
							setAddress("?,?x");
						
						textField.setToolTipText("Set to FF for max stat experience.");

					}

					else if (choice.contains("DV")) {
						textField.setVisible(true);
						textField.setEnabled(true);
						textField.setEditable(true);
						comboBoxChoiceTwo.setEnabled(false);
					
						if (choice == "Atk/Def DVs")	{
							setAddress("?,?z");
							textField.setToolTipText("Set to FF for perfect DVs. Set to FA for shiny male or 7A for shiny female");
						}
							
						else if (choice == "Spe/Spc DVs")	{
							setAddress("?,9b");
							textField.setToolTipText("Set to FF for perfect DVs. Set to AA for shiny or FF for any hidden power with 70 base power");
						}
					}

					else if (choice == "Friendship") {
						textField.setVisible(true);
						textField.setEnabled(true);
						textField.setEditable(true);
						comboBoxChoiceTwo.setEnabled(false);

						setAddress("?,9g");
						textField.setToolTipText("Set to FF for maximum friendship, to 00 for minimum");
					}

					else if (choice == "Pokerus") {
						textField.setVisible(true);
						textField.setEnabled(true);
						textField.setEditable(true);
						comboBoxChoiceTwo.setEnabled(false);

						setAddress("?,9h");
						textField.setToolTipText("Set to 01 to active pokerus, 00 for off");
					}

					else if (choice == "Level") {
						textField.setVisible(true);
						textField.setEnabled(true);
						textField.setEditable(true);
						comboBoxChoiceTwo.setEnabled(false);

						setAddress("?,9k");
						textField.setToolTipText("Use a Rare Candy on the Pokemon after changing (set to desired level minus 1)");
					}
					
					else if (choice == "Item #2 Quantity") {
						textField.setVisible(true);
						textField.setEnabled(true);
						textField.setEditable(true);
						comboBoxChoiceTwo.setEnabled(false);

						setAddress("PK,'t?");
						textField.setToolTipText("Enter how many items you want (1-255)");
					}
					
					else if (choice == "Ball Item #2 Quantity") {
						textField.setVisible(true);
						textField.setEnabled(true);
						textField.setEditable(true);
						comboBoxChoiceTwo.setEnabled(false);

						setAddress("PK♀AA");
						textField.setToolTipText("Enter how many items you want (1-255)");
					}

				}

			}
		});

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				String text = textField.getText().toUpperCase();

				String choice = comboBoxChoiceOne.getSelectedItem().toString();
				System.out.println(choice);

				if (choice == "Level" || choice.contains("Quantity")) {
					String hex = decimalToHex(Integer.parseInt(text)).toUpperCase();
					System.out.println("hex: " + hex);

					String result = runDBQuery("SELECT text FROM table1 WHERE hex='" + hex + "'");
					lblFinalBoxName.setText(getAddress() + result);
				}

				if (text.length() == 2 && choice != "Level" && !choice.contains("Quantity")) {
					String result = runDBQuery("SELECT text FROM table1 WHERE hex='" + text + "'");
					lblFinalBoxName.setText(getAddress() + result);
				}
			}
		});
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setVisible(false);
		textField.setBounds(212, 63, 152, 33);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		comboBoxChoiceOne.setMaximumRowCount(6);
		comboBoxChoiceOne.setBounds(21, 63, 152, 33);
		frame.getContentPane().add(comboBoxChoiceOne);
		comboBoxChoiceTwo.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {

					// get current selections of both combo boxes
					String choiceOne = comboBoxChoiceOne.getSelectedItem().toString();
					String choiceTwo = comboBoxChoiceTwo.getSelectedItem().toString();

					// change combo box selections to match column names in the database

					if (choiceOne == "Change Species")
						choiceOne = "pokemon";
					else if (choiceOne == "Change Held Item" || choiceOne == "Item #2 ID"
							|| choiceOne == "Ball Item #2 ID")
						choiceOne = "item";
					else if (choiceOne.contains("Move"))
						choiceOne = "move";

					String result = runDBQuery("SELECT text FROM table1 WHERE " + choiceOne + " = '" + choiceTwo + "'");
					String finalBoxName = getAddress() + result;
					lblFinalBoxName.setText(finalBoxName);

				}
			}
		});

		JLabel lblBoxNameLabel = new JLabel("Change box 1 name to:");
		lblBoxNameLabel.setBounds(240, 472, 152, 25);
		frame.getContentPane().add(lblBoxNameLabel);
		
		JLabel lblNewLabel = new JLabel("Hover mouse over elements for instructions");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel.setBounds(173, 11, 516, 33);
		frame.getContentPane().add(lblNewLabel);

	}

	private void setAddress(String address) {
		this.address = address;
	}

	private String getAddress() {
		return address;
	}

	private String runDBQuery(String sql) {

		String jdbc = "jdbc:derby:database;create=true;useUnicode=yes;characterEncoding=UTF-8";
		String write = null;

		try {
			// connect to database and run the sql query
			Connection connection = DriverManager.getConnection(jdbc);
			System.out.println(sql);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);

			// get results, we're only searching in the 'text' column for this
			while (rs.next()) {

				System.out.println(rs.getString("text"));
				write = rs.getString("text");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return write;

	}

	private String decimalToHex(int numberToConvert) {

		String result = Integer.toHexString(numberToConvert);
		return result;

	}
}
