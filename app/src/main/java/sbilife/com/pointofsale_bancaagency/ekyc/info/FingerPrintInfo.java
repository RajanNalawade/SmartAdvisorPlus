// The present software is not subject to the US Export Administration Regulations (no exportation license required), May 2012
package sbilife.com.pointofsale_bancaagency.ekyc.info;

import com.morpho.morphosmart.sdk.CompressionAlgorithm;

public class FingerPrintInfo extends MorphoInfo
{
	private static FingerPrintInfo mInstance				= null;
	private CompressionAlgorithm compressionAlgorithm	= CompressionAlgorithm.MORPHO_NO_COMPRESS;
	private int						compressRatio			= 10;
	private boolean					latentDetect			= false;

	public static FingerPrintInfo getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new FingerPrintInfo();
			mInstance.reset();
		}
		return mInstance;
	}

	private FingerPrintInfo()
	{
	}

	@Override
	public String toString()
	{
		return "compressionAlgorithm:\t" + compressionAlgorithm + "\r\n"  + "\r\n" + "latentDetect:\t" + latentDetect;
	}

	public void reset()
	{
		setCompressionAlgorithm(CompressionAlgorithm.MORPHO_NO_COMPRESS);
		setLatentDetect(false);
	}


	public CompressionAlgorithm getCompressionAlgorithm()
	{
		return compressionAlgorithm;
	}

	public void setCompressionAlgorithm(CompressionAlgorithm compressionAlgorithm)
	{
		this.compressionAlgorithm = compressionAlgorithm;
	}

	public boolean isLatentDetect()
	{
		return latentDetect;
	}

	public void setLatentDetect(boolean isLatentDetect)
	{
		this.latentDetect = isLatentDetect;
	}

	public int getCompressRatio()
	{
		return compressRatio;
	}

	public void setCompressRatio(int compressRatio)
	{
		this.compressRatio = compressRatio;
	}
}
