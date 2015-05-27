package se.sics.kompics.system.cyclon;

import java.io.Serializable;
import java.util.ArrayList;

import se.sics.kompics.system.common.PeerAddress;

public class DescriptorBuffer implements Serializable {
	private static final long serialVersionUID = -4414783055393007206L;
	private final PeerAddress from;
	private final ArrayList<CyclonDescriptor> descriptors;

//-------------------------------------------------------------------	
	public DescriptorBuffer(PeerAddress from, ArrayList<CyclonDescriptor> descriptors) {
		super();
		this.from = from;
		this.descriptors = descriptors;
	}

//-------------------------------------------------------------------	
	public PeerAddress getFrom() {
		return from;
	}

//-------------------------------------------------------------------	
	public int getSize() {
		return descriptors.size();
	}

//-------------------------------------------------------------------	
	public ArrayList<CyclonDescriptor> getDescriptors() {
		return descriptors;
	}
}
