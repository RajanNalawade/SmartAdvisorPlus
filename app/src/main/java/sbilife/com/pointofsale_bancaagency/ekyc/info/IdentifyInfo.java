// The present software is not subject to the US Export Administration Regulations (no exportation license required), May 2012
package sbilife.com.pointofsale_bancaagency.ekyc.info;

public class IdentifyInfo extends MorphoInfo
{
	private static IdentifyInfo mInstance	= null;

	public static IdentifyInfo getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new IdentifyInfo();
			mInstance.reset();
		}
		return mInstance;
	}

	private IdentifyInfo()
	{
	}

	@Override
	public String toString()
	{
		return "IndentifyInfo : No Data Stored";
	}

	public void reset()
	{
	}

}
