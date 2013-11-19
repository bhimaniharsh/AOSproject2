import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;


public class ReceiverThread implements Runnable {

	SctpServerChannel sctpServerChannel;
	HashMap<Integer, String> nodeConfiguration;
	LamportClock lamportClock;
	List<Integer> neighbours;
	
	public ReceiverThread(SctpServerChannel sctpServerChannel,
			HashMap<Integer, String> nodeConfiguration,
			LamportClock lamportClock, List<Integer> neighbours) {
		this.lamportClock = lamportClock;
		this.nodeConfiguration = nodeConfiguration;
		this.sctpServerChannel = sctpServerChannel;
		this.neighbours = neighbours;
	}

	public void run() {
		List<SctpChannel> sctpChannels = new ArrayList<SctpChannel>();
		
		boolean listener;
		if((nodeConfiguration.size()-1) == Node.nodeId)
			listener = false;
		else
			listener = true;

		//Loop to accept connection
		while(listener){
			try {
				SctpChannel sctpChannel = sctpServerChannel.accept();
				sctpChannel.configureBlocking(false);
				Iterator itr = sctpChannel.getRemoteAddresses().iterator();
				while(itr.hasNext())
				{
					
					InetSocketAddress socketAddress = (InetSocketAddress)itr.next();
					System.out.println("Connected by "+Node.nodeId+" connection from : "+socketAddress.getHostName());
					String connectedHostName = socketAddress.getHostName();
					for (Entry entry : nodeConfiguration.entrySet()) {
						if(entry.getValue().toString().contains(connectedHostName)){
							Node.connectionMap.put((Integer)entry.getKey(),sctpChannel);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(Node.connectionMap.size() == (neighbours.size()))
				listener = false;
		}
		listener = true;
		while(listener){
			try {
				receiveMessage();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * Receives messages from the processes
	 * @param connectionSctpChannel
	 * @return
	 * @throws IOException
	 */
	public void receiveMessage() throws IOException{
		ByteBuffer buf = ByteBuffer.allocate(60000);
		for (Entry sctpChannelEntry : Node.connectionMap.entrySet()){ 
			SctpChannel sctpChannel = (SctpChannel)sctpChannelEntry.getValue();
			sctpChannel.receive(buf,null,null);
			buf.flip();
			if(buf.remaining() > 0) {
			    Message msgObject = deserialize(buf);
				
				//Node.messageReceiveCounter++;
				if(msgObject !=  null){
				synchronized (lamportClock) {
					lamportClock.setComparedTimeStamp(msgObject.getLabel());
				}
				handleMessage(msgObject);
				}
			}
			buf.clear();
		}
	}
	


	private void handleMessage(Message msgObject) {
		
	}

	/**
	 * This method deserializes teh given message
	 * @param byteBuffer
	 * @return
	 */
	public Message deserialize(ByteBuffer byteBuffer){
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
		ObjectInputStream objectInputStream;
		Object objectToDeserialize = null;
		try {
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			objectToDeserialize = objectInputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Message convertedObject = null;
		if (objectToDeserialize instanceof Message)
		{
			convertedObject = (Message) objectToDeserialize;
		}
		return convertedObject;
	}

}
