package fr.triedge.mon.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import fr.triedge.mon.server.logs.HtmlFormatter;

public class Server {
	
	private int serverPort;
	private boolean running = true;
	private final static Logger log = Logger.getLogger(Server.class.getName());
	
	private ArrayList<AgentInfo> agentList = new ArrayList<>();
	private ArrayList<ServerTask> tasks = new ArrayList<ServerTask>();
	
	public Server(int port) {
		setServerPort(port);
	}
	
	public void init() {
		// Setup logging
		try {
			setupLogging();
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		
		// Init ping task
		log.fine("Creating task TaskAgentPing...");
		TaskPingAgents taskPingAgent = new TaskPingAgents(this, 30000);
		tasks.add(taskPingAgent);
		log.fine("Task TaskAgentPing added to task pool");
		taskPingAgent.start();
		log.fine("Task TaskAgentPing started");
		
	}
	
	public void start() {
		try {
			ServerSocket sok = new ServerSocket(getServerPort());
			log.info("Server initialized with port: "+getServerPort());
			while (isRunning()) {
				log.info("Server waiting for connections...");
				Socket client = sok.accept();
				log.info("Client connection from: "+client.getInetAddress().getHostName()+":"+client.getPort());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void stop() {
		log.info("Server is stopping...");
		this.setRunning(false);
		log.info("Stop command sent to server");
		for (ServerTask tsk : getTasks()) {
			tsk.setRunning(false);
			log.info("Stop command sent to task "+tsk.getName());
		}
	}
	
	public synchronized void pingAllAgents() {
		for (AgentInfo ai : getAgentList()) {
			log.fine("Ping agent "+ai.getHostname()+":"+ai.getPort()+"...");
			TaskPingAgentTester tpat = new TaskPingAgentTester(this, ai);
			tpat.start();
		}
	}
	
	public synchronized void updateStatusAgent(AgentInfo agent, AgentStatus status) {
		log.info("Agent status ["+agent.getHostname()+":"+agent.getPort()+"] "+status);
	}

	public static void main(String[] args) {
		int port = 2020;
		Server server = new Server(port);
		AgentInfo i1 = new AgentInfo();
		i1.setHostname("localhost");
		i1.setPort(2021);
		server.getAgentList().add(i1);
		server.init();
		server.start();
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public ArrayList<AgentInfo> getAgentList() {
		return agentList;
	}

	public void setAgentList(ArrayList<AgentInfo> agentList) {
		this.agentList = agentList;
	}

	private void setupLogging() throws SecurityException, IOException {
		log.setLevel(Level.FINE);
		Handler[] handlers = log.getHandlers();
		HtmlFormatter formatter = new HtmlFormatter();
		for (Handler h : handlers) {
			h.setFormatter(formatter);
		}
		
		FileHandler fileTxt = new FileHandler("server/logs/server.log");
	    FileHandler fileHTML = new FileHandler("server/logs/server.html");
	
	    // create a TXT formatter
	    SimpleFormatter formatterTxt = new SimpleFormatter();
	    fileTxt.setFormatter(formatterTxt);
	    log.addHandler(fileTxt);
	
	    // create an HTML formatter
	    HtmlFormatter formatterHTML = new HtmlFormatter();
	    fileHTML.setFormatter(formatterHTML);
	    log.addHandler(fileHTML);
	}

	public ArrayList<ServerTask> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<ServerTask> tasks) {
		this.tasks = tasks;
	}

}
