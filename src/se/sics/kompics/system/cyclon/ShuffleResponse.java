package se.sics.kompics.system.cyclon;

import java.util.UUID;

import se.sics.kompics.system.common.PeerAddress;
import se.sics.kompics.system.common.PeerMessage;

public class ShuffleResponse extends PeerMessage {
	private static final long serialVersionUID = -5022051054665787770L;
	private final UUID requestId;
	private final DescriptorBuffer randomBuffer;

//-------------------------------------------------------------------	
	public ShuffleResponse(UUID requestId, DescriptorBuffer randomBuffer, PeerAddress source, PeerAddress destination) {
		super(source, destination);
		this.requestId = requestId;
		this.randomBuffer = randomBuffer;
	}

//-------------------------------------------------------------------	
	public UUID getRequestId() {
		return requestId;
	}

//-------------------------------------------------------------------	
	public DescriptorBuffer getRandomBuffer() {
		return randomBuffer;
	}
	
//-------------------------------------------------------------------	
	public int getSize() {
		return 0;
	}
}
