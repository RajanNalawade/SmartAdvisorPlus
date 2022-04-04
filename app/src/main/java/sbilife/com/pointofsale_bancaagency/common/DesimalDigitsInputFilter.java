package sbilife.com.pointofsale_bancaagency.common;

import android.text.InputFilter;
import android.text.Spanned;

public class DesimalDigitsInputFilter implements InputFilter
{

	private int decimaldigits;

	public DesimalDigitsInputFilter(int decimaldigits) {
		// TODO Auto-generated constructor stub
		this.decimaldigits=decimaldigits;
	}

	//@Override
	public CharSequence filter(CharSequence source, int start, int end,
			Spanned dest, int dstart, int dend) {
		// TODO Auto-generated method stub

		int dotPos = -1;
		int len = dest.length();
		for (int i = 0; i < len; i++) 
		{
			char c= dest.charAt(i);
			if(c=='.')
			{
				dotPos=i;
				break;
			}

		}
		if (dotPos>=0)
		{
			if(source.equals("."))
			{
				return "";
			}
			//If text is entered before the dot
			if(dend<=dotPos)
			{
				return null;
			}
			if(len-dotPos > decimaldigits)
			{
				return "";
			}
		}

		return null;
	}


}
