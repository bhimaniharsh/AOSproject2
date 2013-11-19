import java.util.ArrayList;
import java.util.List;


public class CheckPoint {

	/**
	 * @param args
	 */
	private List<Integer> LLR = new ArrayList<Integer>();
	private List<Integer> LLS = new ArrayList<Integer>();
	private List<Integer> FLS = new ArrayList<Integer>();
	private int checkpointInitiatorId ;
	private List<Message> messageReceived = new ArrayList<Message>();
	private List<Message> messageSend = new ArrayList<Message>();
	public List<Integer> getLLR() {
		return LLR;
	}
	public void setLLR(List<Integer> lLR) {
		LLR = lLR;
	}
	public List<Integer> getLLS() {
		return LLS;
	}
	public void setLLS(List<Integer> lLS) {
		LLS = lLS;
	}
	public List<Integer> getFLS() {
		return FLS;
	}
	public void setFLS(List<Integer> fLS) {
		FLS = fLS;
	}
	public int getCheckpointInitiatorId() {
		return checkpointInitiatorId;
	}
	public void setCheckpointInitiatorId(int checkpointInitiatorId) {
		this.checkpointInitiatorId = checkpointInitiatorId;
	}
	public List<Message> getMessageReceived() {
		return messageReceived;
	}
	public void setMessageReceived(List<Message> messageReceived) {
		this.messageReceived = messageReceived;
	}
	public List<Message> getMessageSend() {
		return messageSend;
	}
	public void setMessageSend(List<Message> messageSend) {
		this.messageSend = messageSend;
	}
	
}
