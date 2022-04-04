package sbilife.com.pointofsale_bancaagency.common;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DialogDatePicker extends DialogFragment implements
OnDateSetListener{

	private String current_date;
	private Calendar c;
	private int year;
    private int month;
    private int day;
	public DialogDatePicker()
	{
	}

	/* interface */
    interface OnActionDatePickListener {
		void setOnSubmitListener(DatePicker view, int year,
                                 int monthOfYear, int dayOfYear);
	}

	private OnActionDatePickListener mListener;

	public void onAction(OnActionDatePickListener mListener) {
		this.mListener = mListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		c = Calendar.getInstance();
		initialiseDateParameter();
		setStyle(DialogFragment.STYLE_NORMAL, AlertDialog.THEME_HOLO_LIGHT);
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfYear) {
		mListener.setOnSubmitListener(view, year, monthOfYear, dayOfYear);
	}

	private void initialiseDateParameter()
	{
		if (current_date.equals("")) {
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		}
		else
		{
			String[] array = current_date.split("/");
			month=Integer.parseInt(array[1])-1;
			day =Integer.parseInt(array[0]);
			year =Integer.parseInt(array[2]);
		}
	}
}






