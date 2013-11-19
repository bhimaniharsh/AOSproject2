
public enum MessageTypes {
	TAKE("TAKE_CHECKPOINT"),
	MAKE("MAKE_CHECKPOINT"),
	UNDO("UNDO_CHECKPOINT");
	
	private final String messageType;
	
	private MessageTypes(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageType() {
		return messageType;
	}
}

