import java.awt.EventQueue;
import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TM17Calculator {

	private JFrame frame;
	private String address;
	private JTextField textField;

	private HashMap<String, String> hashmap = new HashMap<String, String>();

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

	private void createHashMap() {
		for (int i = 0; i < getChoiceOneOptions().length; i++) {
			hashmap.put(getChoiceOneOptions()[i], getAddressList()[i]);
		}
	}

	/*** Create the application. */
	public TM17Calculator() {
		createHashMap();
		initialize();
	}

	/*** Initialize the contents of the frame. */

	private void initialize() {

		// create GUI elements
		frame = new JFrame();
		frame.getContentPane().setEnabled(false);
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblFinalBoxName = new JLabel("Result shows up here");
		lblFinalBoxName.setBounds(436, 472, 135, 25);
		frame.getContentPane().add(lblFinalBoxName);

		// default selection is "change species" as seen in the next code block
		// so show pokemon list by default
		JComboBox comboBoxChoiceTwo = new JComboBox();
		comboBoxChoiceTwo.setToolTipText("Pokemon/Item/Move that you want");
		comboBoxChoiceTwo
				.setModel(new DefaultComboBoxModel(runDBQuery("SELECT pokemon FROM table1", "pokemon").toArray()));
		comboBoxChoiceTwo.setBounds(21, 195, 152, 33);
		frame.getContentPane().add(comboBoxChoiceTwo);
		setAddress("Change Species");

		// Add array elements to the combo box to start.
		JComboBox comboBoxChoiceOne = new JComboBox(getChoiceOneOptions());
		comboBoxChoiceOne.setToolTipText("Edit Party Pokemon #3");

		comboBoxChoiceOne.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {
					String choice = comboBoxChoiceOne.getSelectedItem().toString();

					textField.setEnabled(false);
					textField.setEditable(false);
					textField.setVisible(false);
					comboBoxChoiceTwo.setEnabled(true);

					setAddress(choice);
					// show the textfield and disable the other combo box if its not needed for the
					// current selection
					if (choice.contains("exp") || choice.contains("DV") || choice.contains("Friendship")
							|| choice.contains("Pokerus") || choice.contains("Level")
							|| choice.contains("Item #2 Quantity") || choice.contains("Ball Item #2 Quantity")) {

						textField.setVisible(true);
						textField.setEnabled(true);
						textField.setEditable(true);

						comboBoxChoiceTwo.setEnabled(false);

						textField.setToolTipText(
								"Set to FF for max stat experience or a decimal number for item quantities (1-255).");
					}

					else if (choice.contains("Move")) {
						comboBoxChoiceTwo.setModel(
								new DefaultComboBoxModel(runDBQuery("SELECT move FROM table1", "move").toArray()));
					} else if (choice.contains("Item")) {
						comboBoxChoiceTwo.setModel(
								new DefaultComboBoxModel(runDBQuery("SELECT item FROM table1", "item").toArray()));
					}

				}

			}
		});

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				// get and change user values to uppercase so it matches values in db
				String text = textField.getText().toUpperCase();

				// get the current selected option of the combo box
				String choice = comboBoxChoiceOne.getSelectedItem().toString();

				// change user input from a decimal number to hex, then lookup hex in db to
				// change to text to enter in the game
				if (text.length() >= 1 && choice == "Level" || choice.contains("Quantity")) {
					String hex = decimalToHex(Integer.parseInt(text)).toUpperCase();
					System.out.println("hex: " + hex);

					String result = runDBQuery("SELECT text FROM table1 WHERE hex='" + hex + "'", "text").get(0);
					lblFinalBoxName.setText(getAddress() + result);
				}

				// else expect hex values from user input
				if (text.length() == 2 && choice != "Level" && !choice.contains("Quantity")) {
					String result = runDBQuery("SELECT text FROM table1 WHERE hex='" + text + "'", "text").get(0);
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

					// this can return only 1 element, so return first element
					String result = runDBQuery("SELECT text FROM table1 WHERE " + choiceOne + " = '" + choiceTwo + "'",
							"text").get(0);
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

	private void setAddress(String choice) {

		String result = hashmap.get(choice);
		this.address = result;

	}

	private String getAddress() {
		return address;
	}

	private ArrayList<String> runDBQuery(String sql, String column) {

		String jdbc = "jdbc:derby:database;create=true;useUnicode=yes;characterEncoding=UTF-8";
		ArrayList<String> results = new ArrayList<String>();

		try {
			// connect to database and run the sql query
			Connection connection = DriverManager.getConnection(jdbc);
			System.out.println(sql);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);

			// get results that appear in the desired column
			while (rs.next()) {
				results.add(rs.getString(column));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return results;

	}

	private String decimalToHex(int numberToConvert) {

		String result = Integer.toHexString(numberToConvert);
		return result;

	}

	private String[] getChoiceOneOptions() {

		String choiceOneOptions[] = { "Change Species", "Change Held Item", "Move 1", "Move 2", "Move 3", "Move 4",
				"HP Stat exp", "Attack Stat exp", "Defense Stat exp", "Speed Stat exp", "Special Stat exp",
				"Atk/Def DVs", "Spe/Spc DVs", "Friendship", "Pokerus", "Level", "Item #2 ID", "Item #2 Quantity",
				"Ball Item #2 ID", "Ball Item #2 Quantity" };

		return choiceOneOptions;
	}

	private String[] getAddressList() {
		String addressList[] = { "?,?e", "?,?f", "?,?g", "?,?h", "?,?i", "?,?j", "PK,'s?", "PK, A", "?,?p", "?,?r",
				"?,?t", "?,?v", "?,?x", "?,?z", "?,9b", "?,9g", "?,9h", "?,9k", "PKn't?", "PK♀AA" };

		return addressList;
	}
}
