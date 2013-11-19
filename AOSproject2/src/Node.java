import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;

public class Node implements Runnable{
	
	private List<Integer> localLLR = new ArrayList<Integer>();
	private List<Integer> localLLS = new ArrayList<Integer>();
	private List<Integer> localFLS = new ArrayList<Integer>();
	private List<Integer> neighbours = new ArrayList<Integer>();
	private List<Message> localMessageReceived = new ArrayList<Message>();
	private List<Message> localMessageSend = new ArrayList<Message>();
	private Queue<Message> applicationMessage ;
	private Queue<Message> controlMessage ;
	private Boolean willingToTake;
	private Boolean alreadyTaken;
	static ConcurrentHashMap<Integer,SctpChannel> connectionMap = new ConcurrentHashMap<Integer, SctpChannel>();
	HashMap<Integer, String> nodeConfiguration=new HashMap<Integer,String>();
	static int nodeId;
	int timer;
	SctpServerChannel sctpServerChannel;
	Thread serverThread;
	LamportClock lamportClock = new LamportClock();
	
	public Node(int nodeId,HashMap<Integer,String> configuration,List<Integer> neighbours,int timer) {
		this.nodeId = nodeId;
		this.nodeConfiguration = configuration;
		this.neighbours = neighbours;
		this.timer = timer;
		initialize();
	}
	
	private void initialize() {
		String hostAddress =  nodeConfiguration.get(nodeId);
		String[] splitAddress  = hostAddress.split("#");
		try {
			sctpServerChannel = SctpServerChannel.open();
			InetSocketAddress serverAddr = new InetSocketAddress(splitAddress[0],Integer.parseInt(splitAddress[1]));
			sctpServerChannel.bind(serverAddr);
			Thread receiverThread = new Thread(new ReceiverThread(sctpServerChannel,nodeConfiguration,lamportClock,neighbours));
			receiverThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Integer> getLocalLLR() {
		return localLLR;
	}
	public void setLocalLLR(List<Integer> localLLR) {
		this.localLLR = localLLR;
	}
	public List<Integer> getLocalLLS() {
		return localLLS;
	}
	public void setLocalLLS(List<Integer> localLLS) {
		this.localLLS = localLLS;
	}
	public List<Integer> getLocalFLS() {
		return localFLS;
	}
	public void setLocalFLS(List<Integer> localFLS) {
		this.localFLS = localFLS;
	}
	public List<Integer> getNeighbours() {
		return neighbours;
	}
	public void setNeighbours(List<Integer> neighbours) {
		this.neighbours = neighbours;
	}
	public List<Message> getLocalMessageReceived() {
		return localMessageReceived;
	}
	public void setLocalMessageReceived(List<Message> localMessageReceived) {
		this.localMessageReceived = localMessageReceived;
	}
	public List<Message> getLocalMessageSend() {
		return localMessageSend;
	}
	public void setLocalMessageSend(List<Message> localMessageSend) {
		this.localMessageSend = localMessageSend;
	}
	public Queue<Message> getApplicationMessage() {
		return applicationMessage;
	}
	public void setApplicationMessage(Queue<Message> applicationMessage) {
		this.applicationMessage = applicationMessage;
	}
	public Queue<Message> getControlMessage() {
		return controlMessage;
	}
	public void setControlMessage(Queue<Message> controlMessage) {
		this.controlMessage = controlMessage;
	}
	public Boolean getWillingToTake() {
		return willingToTake;
	}
	public void setWillingToTake(Boolean willingToTake) {
		this.willingToTake = willingToTake;
	}
	public Boolean getAlreadyTaken() {
		return alreadyTaken;
	}
	public void setAlreadyTaken(Boolean alreadyTaken) {
		this.alreadyTaken = alreadyTaken;
	}
	public ConcurrentHashMap<Integer, SctpChannel> getConnectionMap() {
		return connectionMap;
	}
	public void setConnectionMap(
			ConcurrentHashMap<Integer, SctpChannel> connectionMap) {
		connectionMap = connectionMap;
	}

	public void run() {
		connectAll();
	}	
	
	/**
	 * This methods sends connections to other nodes.
	 */
	private void connectAll() {
		Iterator itr = neighbours.iterator();
		if(nodeId != 0){
			while(itr.hasNext())
			{
				int n = Integer.parseInt(itr.next().toString());
				
				if(n < nodeId){
					String address = nodeConfiguration.get(n);
					String[] splitAddress = address.split("#");
					InetSocketAddress serverAddr = new InetSocketAddress(splitAddress[0],Integer.parseInt(splitAddress[1].toString()));
					try {
						System.out.println("Connecting to node "+ this.nodeId + " with the destination node : "+n);
						SctpChannel clientSocket = SctpChannel.open(serverAddr, 0, 0);
						clientSocket.configureBlocking(false);
						connectionMap.put(n, clientSocket);
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}	
				}
			}
		}
	}

	/**
	 * Sends message on the given Sctp Channel
	 * @param clientSock
	 * @param message
	 * @throws CharacterCodingException
	 */
	private static void sendMessage(SctpChannel clientSock, Message message) throws CharacterCodingException
    {
        ByteBuffer sendBuffer = ByteBuffer.allocate(60000);
        sendBuffer.clear();
        sendBuffer.put(serializeMessage(message));
        sendBuffer.flip();
        try {
            MessageInfo messageInfo = MessageInfo.createOutgoing(null,0);
            clientSock.send(sendBuffer, messageInfo);
        } catch (IOException ex) {
          //  logger.log(Level.SEVERE, null, ex);
        }
    }
	
	/**
	 * This method serializes Message
	 * @param msg
	 * @return
	 */
	public static byte[] serializeMessage(Message msg){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArrayOutputStream.toByteArray();
	}

		
}
