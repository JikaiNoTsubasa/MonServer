package fr.triedge.mon.server;

public class TaskPingAgents extends ServerTask{
	
	private Server server;
	private int milliseconds;
	
	public TaskPingAgents(Server server, int milliseconds) {
		super("TaskAgentPing");
		setServer(server);
		setMilliseconds(milliseconds);
	}

	@Override
	public void run() {
		while(isRunning()) {
			try {
				Thread.sleep(getMilliseconds());
				getServer().pingAllAgents();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public int getMilliseconds() {
		return milliseconds;
	}

	public void setMilliseconds(int milliseconds) {
		this.milliseconds = milliseconds;
	}

}
