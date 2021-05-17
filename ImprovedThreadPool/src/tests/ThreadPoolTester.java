package tests;

public class ThreadPoolTester {
    public static void main(String[] args) {
		WorkQueue wq=new WorkQueue();
		for(int i=0; i<1000; i++) {
			int x=i;
			Job j=()->{
				System.out.println(" Printing " + x + " Thread # " + Thread.currentThread().getId());
			};
			wq.addJob(j);
		}
		wq.shutdown();
    }
}
