package com.smp.findlocal;

public enum ClassifiedSources
{
	BKPGE, CRAIG, EBAYC, INDEE, KIJIJ;

	@Override
	public String toString()
	{
		switch (this)
		{
			case BKPGE:
				return "Backpage";
			case CRAIG:
				return "Craigslist";
			case EBAYC:
				return "eBay Classifieds";
			case INDEE:
				return "Indeed";
			case KIJIJ:
				return "Kijiji";
		}
		return "";
	}

	public String getCode()
	{
		switch (this)
		{
			case BKPGE:
				return "BKPGE";
			case CRAIG:
				return "CRAIG";
			case EBAYC:
				return "EBAYC";
			case INDEE:
				return "INDEE";
			case KIJIJ:
				return "KIJIJ";
		}
		return "";
	}
}
