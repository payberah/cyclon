package se.sics.kompics.simulator.snapshot;

import java.util.ArrayList;

import se.sics.kompics.system.common.PeerAddress;
import se.sics.kompics.system.cyclon.CyclonDescriptor;

public class PeerInfo {
	private int selectedTimes = 0;
	private ArrayList<CyclonDescriptor> partners;


//-------------------------------------------------------------------
	public void updatePartners(ArrayList<CyclonDescriptor> partners) {
		this.partners = partners;
	}

//-------------------------------------------------------------------
	public ArrayList<CyclonDescriptor> getPartners() {
		return this.partners;
	}

//-------------------------------------------------------------------
	public void incSelectedTimes() {
		this.selectedTimes++;
	}

//-------------------------------------------------------------------
	public int getSelectedTimes() {
		return this.selectedTimes;
	}

//-------------------------------------------------------------------
	public boolean isPartner(PeerAddress peer) {
		for (CyclonDescriptor desc : this.partners) {
			if (desc.getPeerAddress().equals(peer))
				return true;
		}
		
		return false;
	}

}
