package visualsort;

public class Runner {
	
	public static void main(String[] args) {
		
		//runs default constructor in screen
		Screen scr = new Screen();
		
		//initializes thread
		Thread t1 = new Thread(scr);
		
		//runs the thread
		t1.start();
		
	}
	
}
