package tests;

import java.lang.Thread.State;
import java.util.ArrayDeque;

public class WorkQueue implements Runnable {
	private Thread[] threads;
	private volatile boolean isRunning = true;
	ArrayDeque<Job> jobQueue = new ArrayDeque<Job>();

	public WorkQueue() {
		int totalThreads = Runtime.getRuntime().availableProcessors() - 1;
		threads = new Thread[totalThreads];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(this);
			threads[i].start();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRunning) {
			System.out.println(" Operation from thread #: " + Thread.currentThread().getId());
			if (!performJob()) {
				synchronized (jobQueue) {
					try {
						jobQueue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public int getThreadCount() {
		return threads.length;
	}

	public void shutdown() {
		completeAllJobs();
		isRunning = false;
		synchronized (jobQueue) {
			jobQueue.notifyAll();
		}
	}

	public void addJob(Job j) {
		synchronized (jobQueue) {
			jobQueue.add(j);
			jobQueue.notify();
		}
	}

	public boolean performJob() {
		Job j = null;
		synchronized (jobQueue) {
			if (!jobQueue.isEmpty()) {
				j = jobQueue.remove();
			}
		}
		if (j != null) {
			j.perform();
			return true;
		} else {
			return false;
		}
	}
	
	public void completeAllJobs() {
		while(!jobQueue.isEmpty()) {
			performJob();
		}
		
		for(int i=0; i<threads.length; i++) {
			if(threads[i].getState() != State.WAITING) {
				i=-1;
			}
		}
	}

}
