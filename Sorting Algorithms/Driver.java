import java.util.ArrayList;
import java.util.Collections;

public class Driver {

	public static void main(String[] args) {
		
		Data data = new Data();
		Sorting sort = new Sorting();
		Timer timer = new Timer();
		
		// change this variable to see how it effects the different algorithms with different sizes
		final int N = 100;
		
		// the unsorted list that generates N amount of random numbers with no duplicates
		ArrayList<Integer> unsortedList = data.getRandomNonRepeatingIntegers(N);
		
		// a temporary list that will store the sorted version
		ArrayList<Integer> sortedList = new ArrayList<Integer>(N);
		
		// set them equal
		sortedList = unsortedList;
		
		// insertion sort with unsorted list
		System.out.println("Insertion sort unsorted:");
		timer.start();
		sort.InsertionSort(sortedList);
		timer.stop();
		timer.getTimeEllapsed();
		
		// insertion sort with sorted list, best case
		System.out.println("\nInsertion sort sorted:");
		timer.start();
		sort.InsertionSort(sortedList);
		timer.stop();
		timer.getTimeEllapsed();
		sortedList = unsortedList;
		
		// quick sort with unsorted
		System.out.println("\nQuick sort unsorted");
		timer.start();
		sort.QuickSort(sortedList);
		timer.stop();
		timer.getTimeEllapsed();
		
		// quick sort with sorted list, best case since both sides of pivot point are even when using median
		System.out.println("\nQuick sort sorted");
		timer.start();
		sort.QuickSort(sortedList);
		timer.stop();
		timer.getTimeEllapsed();
		sortedList = unsortedList;
		
		// heap sort with unsorted
		System.out.println("\nHeap sort unsorted");
		timer.start();
		for(int i = sortedList.size() / 2 - 1; i >= 0; i--) {
		        sort.HeapSort(sortedList, i, sortedList.size());
		}

		for(int i = sortedList.size() - 1; i >= 0; i--) {
			Collections.swap(sortedList, i, 0);
		    sort.HeapSort(sortedList, 0, i);
		}
		
		timer.stop();
		timer.getTimeEllapsed();
		
		System.out.println("\nHeap sort sorted");
		timer.start();
		for(int i = sortedList.size() / 2 - 1; i >= 0; i--) {
	        sort.HeapSort(sortedList, i, sortedList.size());
		}

		for(int i = sortedList.size() - 1; i >= 0; i--) {
			Collections.swap(sortedList, i, 0);
			sort.HeapSort(sortedList, 0, i);
		}
		timer.stop();		
		timer.getTimeEllapsed();
		sortedList = unsortedList;
		
		// merge sort with unsorted
		System.out.println("\nMergeSort with unsorted:");
		timer.start();
		sort.MergeSort(sortedList);
		timer.stop();
		timer.getTimeEllapsed();
		
		// merge sort with sorted
		System.out.println("\nMergeSort with sorted:");
		timer.start();
		sort.MergeSort(sortedList);
		timer.stop();
		timer.getTimeEllapsed();
		
		
	}
	

}
