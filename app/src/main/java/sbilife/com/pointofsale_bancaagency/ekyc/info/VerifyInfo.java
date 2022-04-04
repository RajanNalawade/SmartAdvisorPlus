// The present software is not subject to the US Export Administration Regulations (no exportation license required), May 2012
package sbilife.com.pointofsale_bancaagency.ekyc.info;


public class VerifyInfo extends MorphoInfo
{
	private static VerifyInfo mInstance	= null;

	public static VerifyInfo getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new VerifyInfo();
			mInstance.reset();
		}
		return mInstance;
	}

	private VerifyInfo()
	{
	}

	private String				fileName			= "";

	@Override
	public String toString()
	{
		return  "fileName:\t" + fileName;
	}

	public void reset()
	{
		setFileName("");
	}


	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
}
