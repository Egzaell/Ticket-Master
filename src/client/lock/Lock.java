package client.lock;

public class Lock {

	private boolean isLocked = false;

	public synchronized void lock() throws InterruptedException {
		while (isLocked) {
			wait();
		}
		isLocked = true;
	}

	public synchronized void unlock() {
		isLocked = false;
		notify();
		try {
			wait(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}