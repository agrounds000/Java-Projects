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
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TM17Calculator {

	private JFrame frame;
	private String address;
	private JTextField textField;

	private HashMap<String, String> hashmap = new HashMap<String, String>();
	private HashMap<String, String> specialCharacterMap = new HashMap<String, String>();

	private Connection connection;
	private Statement statement;
	private ResultSet rs;

	private JLabel lblFinalBoxName;
	private JLabel lblHelpText;

	private JComboBox comboBoxChoiceOne;
	private JComboBox comboBoxChoiceTwo;

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

	private void createSpecialCharacterMap() {

		specialCharacterMap.put("Sunflora", "'vé");
		specialCharacterMap.put("Yanma", "'m♂");
		specialCharacterMap.put("Heracross", "Pk♀");
		specialCharacterMap.put("Donphan", "♂3");
		specialCharacterMap.put("Porygon2", "♂4");
		specialCharacterMap.put("Stantler", "♂5");
		specialCharacterMap.put("Smeargle", "♂6");

		specialCharacterMap.put("Zap Cannon", "'vé");
		specialCharacterMap.put("Foresight", "'m♂");
		specialCharacterMap.put("Sleep Talk", "Pk♀");
		specialCharacterMap.put("Metal Claw", "♂3");
		specialCharacterMap.put("Vital Throw", "♂4");
		specialCharacterMap.put("Morning Sun", "♂5");
		specialCharacterMap.put("Synthesis", "♂6");

		specialCharacterMap.put("TM02", "'vé");
		specialCharacterMap.put("TM03", "'m♂");
		specialCharacterMap.put("TM23", "Pk♀");
		specialCharacterMap.put("TM40", "♂3");
		specialCharacterMap.put("TM41", "♂4");
		specialCharacterMap.put("TM42", "♂5");
		specialCharacterMap.put("TM43", "♂6");

	}

	/*** Create the application. */
	private TM17Calculator() {
		initDB();
		createHashMap();
		createSpecialCharacterMap();
		initialize();
	}

	/*** Initialize the contents of the frame. */

	private void initialize() {

		// create GUI elements
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		drawLabels();
		drawTextField();
		drawComboBoxOne();
		drawComboBoxTwo();

	}

	private void setAddress(String choice) {

		String result = hashmap.get(choice);
		this.address = result;

	}

	private String getAddress() {
		return address;
	}

	private void initDB() {
		String jdbc = "jdbc:derby:database;create=true;useUnicode=yes;characterEncoding=UTF-8";

		try {

			connection = DriverManager.getConnection(jdbc);
			statement = connection.createStatement();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ArrayList<String> runDBQuery(String sql, String column) {

		ArrayList<String> results = new ArrayList<String>();

		try {

			rs = statement.executeQuery(sql);

			// get results that appear in the desired column
			while (rs.next()) {
				results.add(rs.getString(column));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return results;

	}

	private void drawLabels() {
		JLabel lblBoxNameLabel = new JLabel("Change box 1 name to:");
		lblBoxNameLabel.setBounds(240, 472, 152, 25);
		frame.getContentPane().add(lblBoxNameLabel);

		JLabel lblInstructions = new JLabel("Hover mouse over elements for instructions");
		lblInstructions.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblInstructions.setBounds(173, 11, 516, 33);
		frame.getContentPane().add(lblInstructions);

		lblFinalBoxName = new JLabel("Result shows up here");
		lblFinalBoxName.setBounds(436, 472, 135, 25);
		frame.getContentPane().add(lblFinalBoxName);

		lblHelpText = new JLabel();
		lblHelpText.setBounds(436, 72, 309, 156);
		lblHelpText.setVisible(false);
		frame.getContentPane().add(lblHelpText);
	}

	private void drawTextField() {

		textField = new JTextField();

		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				String text = textField.getText();

				// don't do anything if entered text isn't a number
				if (!Character.isDigit(e.getKeyChar())) {
					e.consume();
					return;
				}

				if (text.length() > 0) {

					int number = Integer.parseInt(text);

					if (number > 255 || number < 0) {
						textField.setText("255");
						number = 255;
						lblFinalBoxName.setText(getAddress() + " A");
					} else if (text.startsWith("0")) {
						textField.setText(null);
					} else {
						String hex = decimalToHex(number).toUpperCase();
						lblFinalBoxName.setText(getAddress() + hex);
					}
				}

			}
		});
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setVisible(false);
		textField.setBounds(212, 63, 152, 33);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
	}

	private void drawComboBoxOne() {

		// Add array elements to the combo box to start.
		comboBoxChoiceOne = new JComboBox(getChoiceOneOptions());

		comboBoxChoiceOne.setToolTipText("Edit party pokemon #3");

		comboBoxChoiceOne.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {
					String choice = comboBoxChoiceOne.getSelectedItem().toString();

					System.out.println("choice is: " + choice);

					textField.setEnabled(false);
					textField.setEditable(false);
					textField.setVisible(false);
					comboBoxChoiceTwo.setEnabled(true);
					lblHelpText.setVisible(false);

					setAddress(choice);
					// show the textfield and disable the other combo box if its not needed for the
					// current selection
					if (choice.contains("exp") || choice.contains("DV") || choice.contains("Friendship")
							|| choice.contains("Level") || choice.contains("Item #2 Quantity")
							|| choice.contains("Ball Item #2 Quantity") || choice.contains("Pokerus")) {

						textField.setVisible(true);
						textField.setEnabled(true);
						textField.setEditable(true);
						lblHelpText.setVisible(true);

						comboBoxChoiceTwo.setEnabled(false);

						textField.setToolTipText("Enter a number between (1-255).");
						setHelpText(choice);
					}

					else if (choice.contains("Move")) {
						comboBoxChoiceTwo.setModel(
								new DefaultComboBoxModel(runDBQuery("SELECT move FROM table1", "move").toArray()));
					} else if (choice.contains("Item")) {
						comboBoxChoiceTwo.setModel(
								new DefaultComboBoxModel(runDBQuery("SELECT item FROM table1", "item").toArray()));
					} else if (choice.contains("Species")) {
						comboBoxChoiceTwo.setModel(new DefaultComboBoxModel(
								runDBQuery("SELECT pokemon FROM table1", "pokemon").toArray()));
					}

				}

			}
		});

		comboBoxChoiceOne.setMaximumRowCount(6);
		comboBoxChoiceOne.setBounds(21, 63, 152, 33);
		frame.getContentPane().add(comboBoxChoiceOne);
	}

	private void drawComboBoxTwo() {

		// default selection is "change species" as seen in the next code block
		// so show pokemon list by default
		comboBoxChoiceTwo = new JComboBox();
		comboBoxChoiceTwo.setToolTipText("Pokemon/Item/Move that you want");
		comboBoxChoiceTwo
				.setModel(new DefaultComboBoxModel(runDBQuery("SELECT pokemon FROM table1", "pokemon").toArray()));
		comboBoxChoiceTwo.setBounds(21, 195, 152, 33);
		frame.getContentPane().add(comboBoxChoiceTwo);

		setAddress("Change Species");

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

					String finalBoxName;

					// if selected pokemon/item/move has as special character, pull value from
					// special hashmap and not db
					if (specialCharacterMap.containsKey(choiceTwo)) {
						finalBoxName = getAddress() + specialCharacterMap.get(choiceTwo);
						lblFinalBoxName.setText(finalBoxName);
					}

					else {

						// this can return only 1 element, so return first element
						String result = runDBQuery(
								"SELECT text FROM table1 WHERE " + choiceOne + " = '" + choiceTwo + "'", "text").get(0);
						finalBoxName = getAddress() + result;
						lblFinalBoxName.setText(finalBoxName);

					}

				}
			}
		});

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

	private void setHelpText(String choice) {

		switch (choice) {

		case "Atk/Def DVs":
			lblHelpText.setText(
					"<html>Type 255 for perfect, 250 for shiny male, 122 for shiny female, Hidden Power Types:<br>|Dark - 255| |Dragon - 254| |Ice - 253| |Psychic - 252| |Electric - 239| |Grass - 238| Water - 237| |Fire - 236| |Steel - 223| |Ghost - 222| |Bug - 221| |Rock - 220| |Ground - 207| |Poison - 206| |Flying - 205| |Fighting - 204|</html>");
			break;
		case "Spe/Spc DVs":
			lblHelpText.setText("<html>Type 255 for perfect and 70 Base Power Hidden Power, 170 for shiny</html>");
			break;
		case "Friendship":
			lblHelpText.setText(
					"<html>Type to 255 for max friendship, Set 00 for minimum (textfield doesn't accept 0's but the value is (00)</html>");
			break;
		case "Pokerus":
			lblHelpText.setText("<html>Set to (01) to enable pokerus, (00) to disable</html>");
			break;
		case "Level":
			lblHelpText.setText(
					"<html>Use a Rare Candy on the Pokemon after changing (set to desired level minus 1)</html>");
			break;
		}

		if (choice.contains("exp"))
			lblHelpText.setText("<html>Type 255 for max stat experience</html>");
		else if (choice.contains("Quantity"))
			lblHelpText.setText("<html>Type desired quantity, values over 99 may appear glitchy in items menu</html>");
	}

}
