import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Driver {

	public static void main(String[] args) {
		
		// create the binary search tree
		BST tree = new BST();
		
		// find the file
		File file = new File("text.txt");
		Scanner scan = null;
		
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.err.println("File 'text.txt' not found. Is it in located in the right place?");
			e.printStackTrace();
		}
		
		// line number to see which line a word appeared on
		int numberOfLines = 0;
		
		// while the file has more to read
		while(scan.hasNextLine())	{
			
			numberOfLines++;
			
			// get the current line, new line is delimiter
			String word = scan.nextLine();
			
			// stop scanning and break out of the loop when '#' is seen, close the scanner
			if(word.contains("#"))	{
				scan.close();
				break;
			}
			
			// string array, holds the current line with space delimiter
			String[] split = word.split(" ");
			
			for(int i = 0; i < split.length; i++) {

				// if it contains a period or comma, get rid of it
				split[i] = split[i].replace(".", "");
				split[i] = split[i].replace(",", "");
				
				// if a word is more than 10 characters long, cut it down to the first 10
				if(split[i].length() > 10)	{
					split[i] = split[i].substring(0, 10);
				}
				
				// insert the word into the tree along with the line number
				tree.insert(split[i], Integer.toString(numberOfLines));
				
			}
			
		}
		
		// print tree
        tree.inorder();
        
        // close scanner
        scan.close();

	}

}
