import java.util.ArrayList;
import java.util.Collections;

public class Sorting {

	// InsertionSort code from Kumaresan Perumal's answer on https://stackoverflow.com/questions/33943765/arraylist-insertion-sort
	// QuickSort code adapted from https://gist.github.com/djitz/2152957
	// HeapSort code from nathanthesnooper's answer on https://stackoverflow.com/questions/52795068/how-to-use-arraylist-in-heapsort
	// MergeSort code adapted from https://www.codexpedia.com/java/java-merge-sort-implementation/
	
	public ArrayList<Integer> InsertionSort(ArrayList<Integer> inputArray)	{
		
		 for(int i = 1;i < inputArray.size(); i++)	{

	            int key = inputArray.get(i);

	            for(int j = i-1; j >= 0;j--)	{
	            	
	                if(key<inputArray.get(j))	{

	                    inputArray.set(j+1,inputArray.get(j));

	                    if(j==0)	{
	                        inputArray.set(0, key);
	                    }
	                    
	                }
	                
	                else	{
	                    inputArray.set(j+1,key);
	                    break; 
	                }
	            }
	        }       

		return inputArray;
		
	}
	
	public ArrayList<Integer> QuickSort(ArrayList<Integer> inputArray)	{
		
		// if the inputArray is only a size of one or less, return it since it's already sorted
		if(inputArray.size() <= 1){
			return inputArray;
		}
		
		// calculate the middle point and then get the value of it
		int middle = (int) Math.ceil((double)inputArray.size() / 2);
		int pivot = inputArray.get(middle);

		// create 2 new ArrayLists to store the values lower and greater than the pivot point
		ArrayList<Integer> less = new ArrayList<Integer>();
		ArrayList<Integer> greater = new ArrayList<Integer>();
		
		for (int i = 0; i < inputArray.size(); i++) {
			
			// if the current value is less than the pivot, add it to the "less than" array, ignore the pivot when it's found, and then add to "greater than" array
			if(inputArray.get(i) <= pivot){
				if(i == middle){
					continue;
				}
				less.add(inputArray.get(i));
			}
			else{
				greater.add(inputArray.get(i));
			}
		}
		
		// create the new sorted list
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		// add in the smaller numbers
		for (int i = 0; i < less.size(); i++) {
			list.add(less.get(i));
		}
		
		// add pivot
		list.add(pivot);
		
		// add in the bigger numbers
		for (int i = 0; i < greater.size(); i++) {
			list.add(greater.get(i));
		}
		
		return list;

	}
	
	public ArrayList<Integer> HeapSort(ArrayList<Integer> inputArray, int index, int size)	{
		
        int largest = index; // root node
        int left = 2 * index + 1; // left child node
        int right = 2 * index + 2; // right child node

        if (left < size && inputArray.get(left) > inputArray.get(largest)) {
            largest = left;
        }
        if (right < size && inputArray.get(right) > inputArray.get(largest)) {
            largest = right;
        }

        if (largest != index) {
            Collections.swap(inputArray, index, largest); // swap the indexes not the values
            HeapSort(inputArray, largest, size);
        }
		
		return inputArray;
	}
	
	public ArrayList<Integer> MergeSort(ArrayList<Integer> inputArray)	{
		
        ArrayList<Integer> left = new ArrayList<Integer>();
        ArrayList<Integer> right = new ArrayList<Integer>();
        int center;
 
        if (inputArray.size() == 1) {    
            return inputArray;
        } else {
            center = inputArray.size()/2;
            // copy the left half of whole into the left.
            for (int i=0; i<center; i++) {
                    left.add(inputArray.get(i));
            }
 
            // copy the right half of whole into the new arraylist.
            for (int i=center; i<inputArray.size(); i++) {
                    right.add(inputArray.get(i));
            }
 
            // sort the left and right halves of the arraylist.
            left  = MergeSort(left);
            right = MergeSort(right);
 
            // Merge the results back together.
            Merge(left, right, inputArray);
        }
        return inputArray;
		
	}
	
    private void Merge(ArrayList<Integer> left, ArrayList<Integer> right, ArrayList<Integer> whole) {
        int leftIndex = 0;
        int rightIndex = 0;
        int wholeIndex = 0;
 
        // As long as neither the left nor the right ArrayList has
        // been used up, keep taking the smaller of left.get(leftIndex)
        // or right.get(rightIndex) and adding it at both.get(bothIndex).
        while (leftIndex < left.size() && rightIndex < right.size()) {
            if ( (left.get(leftIndex).compareTo(right.get(rightIndex))) < 0) {
                whole.set(wholeIndex, left.get(leftIndex));
                leftIndex++;
            } else {
                whole.set(wholeIndex, right.get(rightIndex));
                rightIndex++;
            }
            wholeIndex++;
        }
 
        ArrayList<Integer> rest;
        int restIndex;
        if (leftIndex >= left.size()) {
            // The left ArrayList has been use up...
            rest = right;
            restIndex = rightIndex;
        } else {
            // The right ArrayList has been used up...
            rest = left;
            restIndex = leftIndex;
        }
 
        // Copy the rest of whichever ArrayList (left or right) was not used up.
        for (int i=restIndex; i<rest.size(); i++) {
            whole.set(wholeIndex, rest.get(i));
            wholeIndex++;
        }
    }
	
}
