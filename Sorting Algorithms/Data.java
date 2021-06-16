import java.util.ArrayList;
import java.util.Random;

public class Data {

	// generate random numbers in a range
	public int getRandomNumber(int min, int max)	{
		
		Random random = new Random();
		return random.nextInt(((max - min) + 1) + min);
		
	}
	
	// create an ArrayList to hold non-repeating random integers and return it
	public ArrayList<Integer> getRandomNonRepeatingIntegers(int size)	{
		
		ArrayList<Integer> list = new ArrayList<Integer>(size);
		
		while(list.size() < size)	{
			
			// get a random number between 1 and 1 million
			int random = getRandomNumber(1, 1000000);
			
			// if the current random number isn't in the ArrayList, add it
			if(!list.contains(random))	{
				list.add(random);
			}
			
		}
		
		return list;
		
	}
	
	
}
