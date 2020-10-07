package fr.triedge.mon.server;

public class ServerTask extends Thread{
	
	private boolean running = true;

	public ServerTask(String name) {
		this.setName(name);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
