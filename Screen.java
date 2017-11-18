package visualsort;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Random;

import javax.swing.JFrame;

public class Screen extends JFrame implements Runnable {
	
	//double buffering variables
	Image dbImage;
	Graphics dbg;
	
	//boolean for toggling mini mode for the less efficient sorting methods
	//mini basically means that the list to be sorted is 1/4 of the size
	boolean isMini = false; //false, but doesn't really matter
	
	//string for the name of the sorting algorithm that is running
	String sortingName = ""; 
	
	//dimensions of the screen. constants
	final int SCREENWIDTH = 1400; //1400
	final int SCREENHEIGHT = 750; //750
	
	//calendars for the start time and end time for each sort
	//basically the stopwatch variables
	Calendar start = Calendar.getInstance();
	Calendar end = Calendar.getInstance();
	
	//the amount of list indexes compressed into each pixel
	final int DOWNSCALE = 76; //76
	
	//the amount of list indexes compressed into each pixel for the mini list
	final int MINIDOWNSCALE = 19; //20
	
	//the size of the list
	final int SIZE = 100000; //100000
	
	//the size of the mini list, used for bad algorithms like bubble sort
	final int MINISIZE = 25000; //25000
	
	//the milliseconds waited between each sort
	final int SORTDELAY = 0; //0
	
	//the width of each visual bar
	final int BARWIDTH = 1; //1
	
	//the pixel height associated with each integer in the list
	final int PIXELHEIGHT = 1; //1
	
	//the arraylist to be sorted
	int[] list = new int[SIZE];
	
	//the miniature list, used for algorithms like bubble sort
	int[] miniList = new int[MINISIZE];	
	
	//the thread
	public void run() {
		
		try {
			
			//fills the lists with numbers at the start of the program
			list = fill(list);
			miniList = fill(miniList);
			
			/*
			 * constantly runs the sortCycle method. I decided to not use recursion
			 * to avoid the methods constantly nesting and potentially causing
			 * problems if the program runs for too long 
			 */
			while (true) {
				sortCycle();
			}
			
		} catch (Exception e) {
			System.out.println("Error in thread");
			e.printStackTrace();
		}
		
	}
	
	//default constructor
	//has screen variables
	public Screen() {
		
		//enables key input
		addKeyListener(new KeyListener());
		
		//jframe properties
		setVisible(true);
		setSize(SCREENWIDTH, SCREENHEIGHT);
		setTitle("Visual Sort");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
	}
	
	//fills the list with list.length number of integers, starting from 1
	//the value at each index is equal to its index + 1
	public int[] fill(int[] list) {
		
		for (int i = 0; i < list.length; i++) {
			
			list[i] = i + 1;
			
		}
		return list;
	}
	
	//randomizes the list
	public int[] mix(int[] list) {
		
		//initializes random object
		Random rand = new Random();
		
		for (int i = 0; i < list.length; i++) {
			
			//swaps two random spots in the list
			int random1 = rand.nextInt(list.length);
			int value1 = list[random1];
			
			//swaps the values
			list[random1] = list[i];
			list[i] = value1;
			
		}
		
		return list;
		
	}
	
	
	//cycles between the different types of sorting algorithms
	public void sortCycle() {
		
		//goes through each sorting method one by one.
		//currently ordered from most efficient to least efficient
			
		selectionSort(list);
		
		mergeSort(list);
		
		bubbleSort(list);
		
		countingSort(miniList);
		
		insertionSort(miniList);
		
		
	}
	
	/*
	 * REQUIREMENTS FOR SORTING ALGORITHMS
	 * 
	 * Must have a "repaint();" method call somewhere in the method to show
	 * the array visually updating.
	 * 
	 * must call the "reset();" method at the start, so that the sortCycle method 
	 * is a lot cleaner and it is easier to switch the method order around
	 * 
	 * must have "sortingName = 'Insert Official Name of Sorting Algorithm Here'"
	 * 
	 * To accommodate for the above two requirements, recursive sorting methods must
	 * have a overloading polymorphized version so that the array doesn't
	 * reset indefinitely. Happens for mergeSort
	 * 
	 * Must use an array, not an ArrayList. Can convert, but would be much slower
	 * 
	 */
	
	//selection sorts the array
	public void selectionSort(int[] array) {
		
		reset();
		if (array.length == SIZE) {
			isMini = false;
		}
		else {
			isMini = true;
		}
		sortingName = "Selection Sort";
		
		for (int i = 0; i < array.length; i++) {
			
			int startmin = array[i];
			int minIndex = i;
			
			//finds the minimum value after i
			for (int j = i; j < array.length; j++) {
				
				if (array[j] < array[minIndex]) {
					minIndex = j;					
				}				
			}
			array[i] = array[minIndex];
			array[minIndex] = startmin;
			
			repaint();
		}
		
	}
	
	//uses merge sort to sort the array
	public void mergeSort(int[] array) {
		
		reset();
		if (array.length == SIZE) {
			isMini = false;
		}
		else {
			isMini = true;
		}
		sortingName = "Merge Sort";
		mergeSort(array, 0, array.length - 1);
		
	}
	
	//uses overloading polymorphism to avoid having to enter the low and high values
	//and also so that the reset() methods and the string name don't constantly get reset
	public void mergeSort(int[] array, int low, int high){
		
		if(low < high){
			
			int middle = (low + high) / 2;
			mergeSort(array, low, middle);
			mergeSort(array, middle+1, high);
			merge(array, low, middle, high);
			
			//refreshes the screen
			repaint();
		}
		
	}

	//merges two arrays, used in mergeSort
	public void merge(int[] array, int low, int middle, int high){
		int[] helper = new int[array.length];
		for (int i = low; i <= high; i++) {
			helper[i] = array[i];
		}
		
		int helperLeft = low;
		int helperRight = middle+1;
		int current = low;
		
		while (helperLeft <= middle && helperRight <=high) {
			if(helper[helperLeft] <= helper[helperRight]){
				array[current] = helper[helperLeft];
				helperLeft++;
				
			}else{
				array[current] = helper[helperRight];
				helperRight++;
			}
			current ++;		
		}
		
		int remaining = middle - helperLeft;
		for (int i = 0; i <= remaining; i++) {
			array[current+i] = helper[helperLeft+ i];
		}
	}
	
	//uses bubble sort to sort the array
	public void bubbleSort(int[] array) {
		
		reset();
//		isMini = true;
		if (array.length == SIZE) {
			isMini = false;
		}
		else {
			isMini = true;
		}
		sortingName = "Bubble Sort";
		
		while (!isSorted(array)) {
			
			for (int i = 0; i < array.length - 1; i++) {
				
				if (array[i] > array[i + 1]) {
					int placeholder = array[i];
					array[i] = array[i + 1]; 
					array[i + 1] = placeholder;
				}
				
			}
			repaint();
			
		}
		
	}
	
	//uses counting sort to sort the array
	public void countingSort(int[] array) {
		
		reset();
		if (array.length == SIZE) {
			isMini = false;
		}
		else {
			isMini = true;
		}
		sortingName = "Counting Sort";
				
		for (int i = 0; i < array.length; i++) {
			
			
			//finds the number of items that are less than array[i]
			int numLessThan = 0;
			
			
			for (int j = 0; j < array.length; j++) {
				
				if (array[i] > array[j]) {
					numLessThan ++;
				}
				
			}
			
			//swaps the two values
			if (i != numLessThan) {
				
				int temp = array[i];
				array[i] = array[numLessThan];
				array[numLessThan] = temp;
				
				i = 0;
				
				repaint();
				
			}
			
		}
		
	}
	
	//uses insertion sort to sort the array
	public void insertionSort(int[] array) {
		
		reset();
		if (array.length == SIZE) {
			isMini = false;
		}
		else {
			isMini = true;
		}
		sortingName = "Insertion Sort";
		
		int unsortedIndex = 0;
		
		while (unsortedIndex < array.length) {
			
			for (int i = unsortedIndex; i > 0; i--) {
				
				if (array[i] < array[i - 1]) {
					
					int temp = array[i];
					array[i] = array[i - 1];
					array[i - 1] = temp;
					repaint();
					
				}
				
			}
			unsortedIndex ++;
			
		}
		
	}
	
	//resets all variables related to sorting the list
	public void reset() {
		
		try {
			//stops the timer
			end = Calendar.getInstance();
			repaint();
			
			//pauses so the timer can be seen
			Thread.sleep(2000);
			
			//shuffles both lists around
			mix(miniList);
			mix(list);
			
//			Thread.sleep(5000);
			
			//resets comparisons and swaps
			start = Calendar.getInstance();
			end = start;
			isMini = false;
		} catch (Exception e) {
			System.out.println("Error in reset() method"); 
		}
		
	}
	
	//returns true if the array is completely sorted
	//used in bubbleSort and for debugging
	public boolean isSorted(int[] array) {
		
		for (int i = 1; i < array.length; i++) {
			
			if (array[i] < array[i - 1]) {
				return false;
			}
			
		}
		
		return true;
		
	}
	
	//prints the parameter list, because java doesn't
	//have a good built-in one that I know of
	//only really used for debugging purposes
	public void print(int[] list) {
		
		String s = "";
		
		for (int i = 0; i < list.length; i++) {
			
			s += list[i] + ", ";
			
		}
		System.out.println(s);
	}
	
	//enables visual double buffering
	public void paint(Graphics g) {
		
		dbImage = createImage(getWidth(), getHeight());
		dbg = dbImage.getGraphics();
		paintComponent(dbg);
		g.drawImage(dbImage, 0, 0, this);
		
	}
	
	//paints stuff to the JFrame
	public void paintComponent(Graphics g) {
		
		//prints the regular list if isMini is false
		if (!isMini) {
			
			for (int i = 0; i < list.length; i+= DOWNSCALE) {
				
				int index = i / DOWNSCALE;
				
				int barheight = list[i] * PIXELHEIGHT / (DOWNSCALE * 2);
				
				
				//displays the bar
				g.fillRect(index * BARWIDTH - 1, (SCREENHEIGHT - barheight) - 5, BARWIDTH, barheight);
				
			}
			
			g.setColor(Color.black);
			
			g.drawString("List Size: " + SIZE, 50, 50);
			
		}
		//displays the miniature list if isMini is true
		else {
			
			for (int i = 0; i < miniList.length; i+= MINIDOWNSCALE) {
				
				int index = i / MINIDOWNSCALE;
				
				int barheight = miniList[i] * PIXELHEIGHT / (MINIDOWNSCALE * 2);
				
				//displays the bar
				g.fillRect(index * BARWIDTH - 1, (SCREENHEIGHT - barheight) - 5, BARWIDTH, barheight);
				
			}
			
			g.setColor(Color.black);
			
			g.drawString("List Size: " + MINISIZE, 50, 50);
			
		}
		
		//displays the timer once the list has been sorted
		g.drawString("Time taken: " 
			+ (double)(end.getTimeInMillis() - start.getTimeInMillis()) / 1000 + " seconds", 50, 75);
		
		//displays the name of the list that is currently being sorted
		g.drawString(sortingName, SCREENWIDTH / 2 - 50, 50);
		
	}
	
	//class that handles key input
	class KeyListener extends KeyAdapter {
		
		public void keyPressed(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			switch(key) {
			
			case KeyEvent.VK_Q:
				System.exit(0);
				break;
			
			}
			
		}
		
		public void keyReleased(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			switch(key) {
			
			
			
			}
			
		}
		
	}
	
}