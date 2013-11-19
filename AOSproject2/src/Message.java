
public class Message {

	private MessageTypes  messageType;
	private int senderId;
	private int destinationId;
	private int instanceInitiatorId;
	private String content;
	private int label;
	
	
	
	public Message(MessageTypes messageType, int senderId, int destinationId,
			int instanceInitiatorId, String content, int label) {
		
		this.messageType = messageType;
		this.senderId = senderId;
		this.destinationId = destinationId;
		this.instanceInitiatorId = instanceInitiatorId;
		this.content = content;
		this.label = label;
	}
	public MessageTypes getMessageType() {
		return messageType;
	}
	public void setMessageType(MessageTypes messageType) {
		this.messageType = messageType;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public int getDestinationId() {
		return destinationId;
	}
	public void setDestinationId(int destinationId) {
		this.destinationId = destinationId;
	}
	public int getInstanceInitiatorId() {
		return instanceInitiatorId;
	}
	public void setInstanceInitiatorId(int instanceInitiatorId) {
		this.instanceInitiatorId = instanceInitiatorId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
}
