import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class MainKT {

	private static HashMap<Integer,String> configuration = new HashMap<Integer,String>();
	private static int nodeId;
	private static List<Integer> neighbourNodeIds=new ArrayList<Integer>();
	private static int timer;  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	if(args.length > 0){
			loadConfiguration(args[0]); //loads the configuration file
			//loadConfiguration("config.txt"); //loads the configuration file

			//Creates a node for processing the message
			Node node = new Node(nodeId,configuration,neighbourNodeIds,timer);
			//Cerates client thread which will send the message
			Thread senderThread = new Thread(node);
			
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			senderThread.start();
			//displayInput();
		}

	}
		
	private static void displayInput() {
		System.out.println("Node : "+nodeId);
		System.out.println("Timer : "+timer);
		System.out.println("Address : "+configuration.get(nodeId));
		System.out.println("Neighours : ");
		Iterator itr = neighbourNodeIds.iterator();
		while(itr.hasNext()){
			System.out.println(" "+itr.next()+" ");
		}
	}

	private static void loadConfiguration(String configurationFileName) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(configurationFileName)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String localhostname;
		try {
			localhostname = java.net.InetAddress.getLocalHost().getHostName();
			String line = null;
			while ((line = br.readLine()) != null) {
				if(line.length() > 0 ){
					String[] splitString = line.split(" ");
					//Scanner scanner = new Scanner(line);
					int configNodeId = Integer.parseInt(splitString[0]);
					String hostName = splitString[1];
					int portNo = Integer.parseInt(splitString[2]);
						
						configuration.put(configNodeId,hostName+"#"+portNo);
						if(localhostname.equalsIgnoreCase(hostName)){ // id for current node.
							nodeId = configNodeId;	
							String neighbourList = splitString[3];
							String[] neighbours = neighbourList.split(",");
							for (int i = 0; i < neighbours.length; i++) {
								neighbourNodeIds.add(Integer.parseInt(neighbours[i]));
							}
							timer = Integer.parseInt(splitString[4]);
							System.out.println("Timer value : "+timer);
							System.out.println("Node id: "+nodeId);
						}
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	
}
