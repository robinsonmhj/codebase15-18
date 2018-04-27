package com.tmxxxx.core;

import java.util.Set;

public class ClusterBasedRule extends Rule{
	private Set<String> hostSet;

	public Set<String> getHostSet() {
		return hostSet;
	}

	public void setHostSet(Set<String> hostSet) {
		this.hostSet = hostSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((hostSet == null) ? 0 : hostSet.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClusterBasedRule other = (ClusterBasedRule) obj;
		if (hostSet == null) {
			if (other.hostSet != null)
				return false;
		} else if (!hostSet.equals(other.hostSet))
			return false;
		return true;
	}
	
	

}
