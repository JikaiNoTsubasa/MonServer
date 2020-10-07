package fr.triedge.mon.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TaskPingAgentTester extends ServerTask{
	
	private Server server;
	private AgentInfo agent;

	public TaskPingAgentTester(Server server, AgentInfo agent) {
		super("AgentPingTester");
		setServer(server);
		setAgent(agent);
	}
	
	public void run() {
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(agent.getHostname(),agent.getPort()), 10000);
			getServer().updateStatusAgent(agent, AgentStatus.OK);
		} catch (IOException e) {
			getServer().updateStatusAgent(agent, AgentStatus.NOT_REACHABLE);
		}
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}


	public AgentInfo getAgent() {
		return agent;
	}

	public void setAgent(AgentInfo agent) {
		this.agent = agent;
	}

}
