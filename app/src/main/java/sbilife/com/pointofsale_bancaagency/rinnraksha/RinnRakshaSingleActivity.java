package sbilife.com.pointofsale_bancaagency.rinnraksha;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.AppSharedPreferences;
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class RinnRakshaSingleActivity extends AppCompatActivity {

    private Spinner isForStaff;
    private Spinner interestRateRange;
    private Spinner loanType;
    private Spinner isMoratoChecked;
    private Spinner premPaidBy;
    private Spinner isIntPayDuringMorato;
    private Spinner moratoPeriod;
    private Spinner isJKResident;
    private Spinner premFreq;
    private Spinner isCoBorrower;
    private Spinner loanSubCategory;
    private Spinner spnr_bankOption;
    private TableRow premTerm;
    private TableRow moratoperiod;
    private TableRow moratointpay;
    private TableRow tbrowSecondBorrower;
    private TableRow tbrowThirdBorrower;
    private TableRow LoanSubCategory;
    private TableRow tableiscoBorrower;
    private TableRow tableisMorato;
    private TableRow tableisCIR;
    /********** Modified by Akshaya on 20-oct-15 start**********/
    private Spinner option;
    private EditText CIR;
    private EditText loanTerm;
    private EditText loanAmt;
    private EditText premPayingTerm;
    private EditText loanShare1;
    private EditText loanShare2;
    private EditText loanShare3;
    private TextView membershipDate, disbursementDate;
    private TextView txt_option;
    private TextView txt_bankOption;
    private TableRow tbrw_loanshare1;
    private TableRow tbrw_loanshare2;
    private TableRow tbrw_loanshare3;
    private TableRow tbrw_option;
    /********** Modified by Akshaya on 20-oct-15 start**********/
    private TextView DOB_Borrower1;
    private TextView DOB_Borrower2;
    private TextView DOB_Borrower3;
    private Context mContext;

    private String DOB_Borrower1Str = "";
    private String DOB_Borrower2Str = "";
    private String DOB_Borrower3Str = "";
    private String membershipDateStr = "", str_rinnraksha_dob_disbursement = "";
    private String coBorrowerOptionStr = "";
    private String moratoPeriodStr = "";
    private String loanAmountStr = "";
    private String loanTermStr = "";
    private String loanTypeStr = "";
    private String loanSubCategoryStr = "";
    private String interestRateRangeStr = "";
    private String isMoratoCheckedStr = "";
    private String premPaidByStr = "";
    private String CIRStr = "";
    private String isForStaffStr = "";
    private String isJKResidentStr = "";
    private String optionStr = "";
    private String isCoBorrowerStr = "";
    private String LoanShareBorrower1Str = "";
    private String LoanShareBorrower2Str = "";
    private String LoanShareBorrower3Str = "";
    private String isIntPayDuringMoratoStr = "";
    private String premFrequency = "";
    private String strDate = "";
    private String strDate1 = "";
    private String strDate2 = "";
    private String strDate3 = "";


    //Class Declaration	
    private CommonForAllProd commonForAllProd = null;
    private RinnRakshaProperties prop = null;

    //Variable declaration
    private AlertDialog.Builder showAlert;
    //server variables
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = ServiceURL.SERVICE_URL;
    private final String METHOD_NAME_CALC = "getPremiumRinnRakshaNew";


    private CommonMethods commonMethods;
    private String str_SBI_home_loan_type = "";
    private long diffDays = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.rinnrakshamain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        mContext = this;

        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, getString(R.string.app_name));

        mContext = this;
        //Class
        prop = new RinnRakshaProperties();
        commonForAllProd = new CommonForAllProd();

        //UI elements
        isForStaff = findViewById(R.id.isForStaff);
        tbrw_loanshare1 = findViewById(R.id.tbrw_loanshare1);
        tbrw_loanshare2 = findViewById(R.id.tbrw_loanshare2);
        tbrw_loanshare3 = findViewById(R.id.tbrw_loanshare3);
        tbrw_option = findViewById(R.id.tbrw_option);
        txt_option = findViewById(R.id.txt_option);
        loanShare1 = findViewById(R.id.loanshare1);
        loanShare2 = findViewById(R.id.loanshare2);
        loanShare3 = findViewById(R.id.loanshare3);

        //Premium Frequency
        isForStaff = findViewById(R.id.isForStaff);
        String[] staffNonstaffList = {"Staff", "Non-Staff"};
        ArrayAdapter<String> staffNonstaffAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, staffNonstaffList);
        staffNonstaffAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        isForStaff.setAdapter(staffNonstaffAdapter);
        staffNonstaffAdapter.notifyDataSetChanged();


        loanType = findViewById(R.id.selplan);
        String[] loanTypeList = {"Home Loan", "Personal Loan", "Vehicle Loan", "Education Loan"};
        ArrayAdapter<String> loanTypeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, loanTypeList);
        loanTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        loanType.setAdapter(loanTypeAdapter);
        loanTypeAdapter.notifyDataSetChanged();


        isJKResident = findViewById(R.id.isJKResident);
        String[] isJKResidentList = {"No", "Yes"};
        ArrayAdapter<String> isJKREsidentAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, isJKResidentList);
        isJKREsidentAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        isJKResident.setAdapter(isJKREsidentAdapter);
        isJKREsidentAdapter.notifyDataSetChanged();


        isMoratoChecked = findViewById(R.id.isMoratoChecked);
        String[] isMoratoCheckedList = {"No", "Yes"};
        ArrayAdapter<String> isMoratoCheckedAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, isMoratoCheckedList);
        isMoratoCheckedAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        isMoratoChecked.setAdapter(isMoratoCheckedAdapter);
        isMoratoCheckedAdapter.notifyDataSetChanged();

        option = findViewById(R.id.option);
        String[] optionList = {"1", "2"};
        ArrayAdapter<String> optionListAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, optionList);
        optionListAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        option.setAdapter(optionListAdapter);
        optionListAdapter.notifyDataSetChanged();


        premPaidBy = findViewById(R.id.premPaidBy);
        String[] premPaidByList = {"Bank", "Self Paid"};
        ArrayAdapter<String> premPaidByListAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, premPaidByList);
        premPaidByListAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        premPaidBy.setAdapter(premPaidByListAdapter);
        premPaidByListAdapter.notifyDataSetChanged();


        CIR = findViewById(R.id.CIR);
        loanAmt = findViewById(R.id.loanAmt);
        loanTerm = findViewById(R.id.loanTerm);
        interestRateRange = findViewById(R.id.interestRateRange);
        premTerm = findViewById(R.id.premTerm);
        LoanSubCategory = findViewById(R.id.LoanSubCategory);
        premPayingTerm = findViewById(R.id.premPayingTerm);

        isIntPayDuringMorato = findViewById(R.id.isIntPayDuringMorato);
        String[] isIntPayDuringMoratoList = {"No", "Yes"};
        ArrayAdapter<String> isIntPayDuringMoratoAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, isIntPayDuringMoratoList);
        isIntPayDuringMoratoAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        isIntPayDuringMorato.setAdapter(isIntPayDuringMoratoAdapter);
        isIntPayDuringMoratoAdapter.notifyDataSetChanged();


        moratoPeriod = findViewById(R.id.moratoPeriod);
        isJKResident = findViewById(R.id.isJKResident);

        premFreq = findViewById(R.id.premFreq);
        String[] premFreqList = {"Single"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, premFreqList);
        premFreqAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        premFreq.setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();
        premFreq.setEnabled(false);

        moratoperiod = findViewById(R.id.tableRow14);
        moratointpay = findViewById(R.id.tableRow15);
        tbrowSecondBorrower = findViewById(R.id.tbrowSecondBorrower);
        tbrowThirdBorrower = findViewById(R.id.tbrowThirdBorrower);
        tableisCIR = findViewById(R.id.tableisCIR);

        isCoBorrower = findViewById(R.id.isCoBorrower);
        String[] isCoBorrowerList = {"Only Primary Borrower", "2 Co-Borrowers", "3 Co-Borrowers"};
        ArrayAdapter<String> isCoBorrowerAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, isCoBorrowerList);
        isCoBorrowerAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        isCoBorrower.setAdapter(isCoBorrowerAdapter);
        isCoBorrowerAdapter.notifyDataSetChanged();


        loanSubCategory = findViewById(R.id.loanSubCategory);
        tableiscoBorrower = findViewById(R.id.tableiscoBorrower);
        tableisMorato = findViewById(R.id.tableisMorato);

        membershipDate = findViewById(R.id.membershipDate);
        disbursementDate = findViewById(R.id.disbursementDate);
        membershipDate.setClickable(true);
        disbursementDate.setClickable(true);
        premTerm.setVisibility(View.VISIBLE);


        DOB_Borrower1 = findViewById(R.id.DOB_Borrower1);
        DOB_Borrower2 = findViewById(R.id.DOB_Borrower2);
        DOB_Borrower3 = findViewById(R.id.DOB_Borrower3);
        DOB_Borrower1.setClickable(true);
        DOB_Borrower2.setClickable(true);
        DOB_Borrower3.setClickable(true);
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        String Today = (mMonth + 1) + "/" + mDay + "/" + mYear;
        membershipDate.setText(Today);
        disbursementDate.setText(Today);


        //Variables
        showAlert = new AlertDialog.Builder(this);
        DecimalFormat currencyFormat = new DecimalFormat("##,##,##,###");


        //Staff is not allowed in easy access
        isForStaff.setSelection(1);
        isForStaff.setEnabled(false);

        txt_bankOption = findViewById(R.id.txt_bankOption);
        spnr_bankOption = findViewById(R.id.spnr_bankOption);
        String[] bankOptionList = {"SBI", "Associate Bank"};
        ArrayAdapter<String> bankOptionListAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, bankOptionList);
        bankOptionListAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spnr_bankOption.setAdapter(bankOptionListAdapter);
        bankOptionListAdapter.notifyDataSetChanged();


        loanType.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                resetInputScreen();
                setCIR();
            }

            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

        isForStaff.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                resetInputScreen();
                setCIR();

            }

            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

        isCoBorrower.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long arg3) {

                if (pos == 1) {
                    tbrowSecondBorrower.setVisibility(View.VISIBLE);
                    tbrowThirdBorrower.setVisibility(View.GONE);
                    if (loanType.getSelectedItem().toString().equals("Home Loan"))
                        tbrw_option.setVisibility(View.VISIBLE);
                    else
                        tbrw_option.setVisibility(View.GONE);
                } else if (pos == 2) {
                    tbrowSecondBorrower.setVisibility(View.VISIBLE);
                    tbrowThirdBorrower.setVisibility(View.VISIBLE);
                    if (loanType.getSelectedItem().toString().equals("Home Loan"))
                        tbrw_option.setVisibility(View.VISIBLE);
                    else
                        tbrw_option.setVisibility(View.GONE);
                } else {
                    tbrowSecondBorrower.setVisibility(View.GONE);
                    tbrowThirdBorrower.setVisibility(View.GONE);
                    tbrw_option.setVisibility(View.GONE);
                }

                if (option.getSelectedItem().toString().equals("2"))
                    updateOption();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        option.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long arg3) {

                if (pos == 0) {
                    tbrw_loanshare1.setVisibility(View.GONE);
                    tbrw_loanshare2.setVisibility(View.GONE);
                    tbrw_loanshare3.setVisibility(View.GONE);
                    txt_option.setText("Each borrower covered for the entire loan amount");
                } else {
                    updateOption();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        isMoratoChecked.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long arg3) {
                if (pos == 1) {
                    moratoperiod.setVisibility(View.VISIBLE);
                    moratointpay.setVisibility(View.VISIBLE);
                } else {
                    moratoperiod.setVisibility(View.GONE);
                    moratointpay.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        interestRateRange
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long arg3) {
                        setCIR();

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        loanSubCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {


                final ArrayAdapter<String> NonStaff_PersonalLoanAdatpter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, prop.NonStaff_PersonalLoan);
                final ArrayAdapter<String> NonStaff_PersonalLoan_HomeLoanEquityAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, prop.NonStaff_PersonalLoan_HomeLoanEquity);
                final ArrayAdapter<String> NonStaff_PersonalLoan_MortgageLoanAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, prop.NonStaff_PersonalLoan_MortgageLoan);
                NonStaff_PersonalLoanAdatpter.setDropDownViewResource(R.layout.spinner_dropdown);
                NonStaff_PersonalLoan_HomeLoanEquityAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
                NonStaff_PersonalLoan_MortgageLoanAdapter.setDropDownViewResource(R.layout.spinner_dropdown);


                if (isForStaff.getSelectedItem().toString().equals("Non-Staff")
                        && loanSubCategory.getSelectedItem().toString()
                        .equals("Home Loan Equity")) {
                    interestRateRange.setAdapter(NonStaff_PersonalLoan_HomeLoanEquityAdapter);
                    NonStaff_PersonalLoan_HomeLoanEquityAdapter.notifyDataSetChanged();
                } else if (isForStaff.getSelectedItem().toString()
                        .equals("Non-Staff") && loanSubCategory.getSelectedItem().toString().equals("Personal Loan")) {
                    interestRateRange.setAdapter(NonStaff_PersonalLoanAdatpter);
                    NonStaff_PersonalLoanAdatpter.notifyDataSetChanged();
                } else if (isForStaff.getSelectedItem().toString().equals("Non-Staff")
                        && loanSubCategory.getSelectedItem().toString().equals("Mortgage Loan")) {
                    interestRateRange.setAdapter(NonStaff_PersonalLoan_MortgageLoanAdapter);
                    NonStaff_PersonalLoan_MortgageLoanAdapter.notifyDataSetChanged();
                }
                setCIR();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        final DatePickerDialog.OnDateSetListener membershipDtdatepickerListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                view.clearFocus();
                membershipDate.setText(new StringBuilder()
                        .append(view.getMonth() + 1).append("/").append(view.getDayOfMonth())
                        .append("/").append(view.getYear()).append(" "));

            }
        };

        final DatePickerDialog.OnDateSetListener borrower1datepickerListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                view.clearFocus();
                DOB_Borrower1.setText(new StringBuilder()
                        .append(view.getMonth() + 1).append("/").append(view.getDayOfMonth())
                        .append("/").append(view.getYear()).append(" "));
            }
        };
        final DatePickerDialog.OnDateSetListener borrower2datepickerListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                view.clearFocus();
                DOB_Borrower2.setText(new StringBuilder()
                        .append(view.getMonth() + 1).append("/").append(view.getDayOfMonth())
                        .append("/").append(view.getYear()).append(" "));

            }
        };
        final DatePickerDialog.OnDateSetListener borrower3datepickerListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                view.clearFocus();
                DOB_Borrower3.setText(new StringBuilder()
                        .append(view.getMonth() + 1).append("/").append(view.getDayOfMonth())
                        .append("/").append(view.getYear()).append(" "));

            }
        };
//		membershipDate.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View arg0) {
//				
//
//				if(membershipDate.getText().toString().equals("Select Date"))
//				{
//					DatePickerFragment.current_date ="";
//					DatePickerFragment newFragment = new DatePickerFragment();
//					newFragment.onAction(new DatePickerFragment.OnActionDatePickListener() {
//						@Override
//						public void setOnSubmitListener(DatePicker view, int year, int monthOfYear, int dayOfYear) {
//							membershipDate.setText((monthOfYear+1)+"/"+dayOfYear+"/"+year);
//						}
//					});
//					newFragment.show(getFragmentManager(), "datePicker");
//				}
//				else
//				{
//					DatePickerFragment.current_date = membershipDate.getText().toString();
//					DatePickerFragment newFragment = new DatePickerFragment();
//					newFragment.onAction(new DatePickerFragment.OnActionDatePickListener() {
//						@Override
//						public void setOnSubmitListener(DatePicker view, int year, int monthOfYear, int dayOfYear) {
//							membershipDate.setText((monthOfYear+1)+"/"+dayOfYear+"/"+year);
//						}
//					});
//					newFragment.show(getFragmentManager(), "datePicker");
//
//				}
////				if(membershipDate.getText().toString().equals("Select Date"))
////				{
////					new DatePickerDialog(RinnRakshaSingleActivity.this,R.style.datepickerstyle,membershipDtdatepickerListener,mYear,mMonth,mDay).show();
////				}
////				else
////				{
////					String dateArray[] = membershipDate.getText().toString().split("/");
////					new DatePickerDialog(RinnRakshaSingleActivity.this,R.style.datepickerstyle,membershipDtdatepickerListener,Integer.parseInt(dateArray[2].trim()),Integer.parseInt(dateArray[0].trim())-1,Integer.parseInt(dateArray[1].trim())).show();
////				}
//			}
//		});

//		premFreq.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View arg0) {
        premFreq.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                resetInputScreen();
//				if (premFreq.equals("Single")) {
//
//					interestRateRange.setAdapter(NonStaff_HomeLoanAdapter);
//					NonStaff_HomeLoanAdapter.notifyDataSetChanged();
//				} else {
//					interestRateRange.setAdapter(NonStaff_HomeLoanAdapter_old);
//					NonStaff_HomeLoanAdapter_old.notifyDataSetChanged();
//				}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        membershipDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {


                if (membershipDate.getText().toString().equals("Select Date")) {
                    strDate = "";
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerdialog = createDateDialog(strDate, mContext, mDateSetListener);
                    datepickerdialog.show(getFragmentManager(), "Datepickerdialog");
                } else {
                    strDate = membershipDate.getText().toString();
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerdialog = createDateDialog(strDate, mContext, mDateSetListener);
                    datepickerdialog.show(getFragmentManager(), "Datepickerdialog");

                }
//				if(membershipDate.getText().toString().equals("Select Date"))
//				{
//					new DatePickerDialog(RinnRakshaActivity.this,R.style.datepickerstyle,membershipDtdatepickerListener,mYear,mMonth,mDay).show();
//				}
//				else
//				{
//					String dateArray[] = membershipDate.getText().toString().split("/");
//					new DatePickerDialog(RinnRakshaActivity.this,R.style.datepickerstyle,membershipDtdatepickerListener,Integer.parseInt(dateArray[2].trim()),Integer.parseInt(dateArray[0].trim())-1,Integer.parseInt(dateArray[1].trim())).show();
//				}
            }
        });
        disbursementDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {


                if (disbursementDate.getText().toString().equals("Select Date")) {
                    str_rinnraksha_dob_disbursement = "";
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerdialog = createDateDialog(strDate, mContext, mDisbursementDateSetListener);
                    datepickerdialog.show(getFragmentManager(), "Datepickerdialog");
                } else {
                    str_rinnraksha_dob_disbursement = disbursementDate.getText().toString();
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerdialog = createDateDialog(strDate, mContext, mDisbursementDateSetListener);
                    datepickerdialog.show(getFragmentManager(), "Datepickerdialog");

                }
//				if(membershipDate.getText().toString().equals("Select Date"))
//				{
//					new DatePickerDialog(RinnRakshaActivity.this,R.style.datepickerstyle,membershipDtdatepickerListener,mYear,mMonth,mDay).show();
//				}
//				else
//				{
//					String dateArray[] = membershipDate.getText().toString().split("/");
//					new DatePickerDialog(RinnRakshaActivity.this,R.style.datepickerstyle,membershipDtdatepickerListener,Integer.parseInt(dateArray[2].trim()),Integer.parseInt(dateArray[0].trim())-1,Integer.parseInt(dateArray[1].trim())).show();
//				}
            }
        });
        DOB_Borrower1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

//				if(DOB_Borrower1.getText().toString().equals("Select Date"))
//				{
//					new DatePickerDialog(RinnRakshaActivity.this,R.style.datepickerstyle,borrower1datepickerListener,mYear,mMonth,mDay).show();
//				}
//				else
//				{
//
//					String dateArray[] = DOB_Borrower1.getText().toString().split("/");
//					new DatePickerDialog(RinnRakshaActivity.this,R.style.datepickerstyle,
//							borrower1datepickerListener, Integer.parseInt(dateArray[2].trim()),Integer.parseInt(dateArray[0].trim())-1,Integer.parseInt(dateArray[1].trim())).show();
//				}

                if (DOB_Borrower1.getText().toString().equals("Select Date")) {
                    strDate1 = "";
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerdialog = createDateDialog(strDate1, mContext, mDateSetListener1);
                    datepickerdialog.show(getFragmentManager(), "Datepickerdialog");
                } else {
                    strDate1 = DOB_Borrower1.getText().toString();
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerdialog = createDateDialog(strDate1, mContext, mDateSetListener1);
                    datepickerdialog.show(getFragmentManager(), "Datepickerdialog");

                }

            }
        });
        DOB_Borrower2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                if (DOB_Borrower2.getText().toString().equals("Select Date")) {
                    strDate2 = "";
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerdialog = createDateDialog(strDate2, mContext, mDateSetListener2);
                    datepickerdialog.show(getFragmentManager(), "Datepickerdialog");
                } else {
                    strDate2 = DOB_Borrower2.getText().toString();
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerdialog = createDateDialog(strDate2, mContext, mDateSetListener2);
                    datepickerdialog.show(getFragmentManager(), "Datepickerdialog");

                }
//				if(DOB_Borrower2.getText().toString().equals("Select Date"))
//				{
//					new DatePickerDialog(RinnRakshaActivity.this,R.style.datepickerstyle,borrower2datepickerListener,mYear,mMonth,mDay).show();
//				}
//				else
//				{
//
//
//					String dateArray[] = DOB_Borrower2.getText().toString().split("/");
//					new DatePickerDialog(RinnRakshaActivity.this,R.style.datepickerstyle,
//							borrower2datepickerListener,Integer.parseInt(dateArray[2].trim()),Integer.parseInt(dateArray[0].trim())-1,Integer.parseInt(dateArray[1].trim())).show();
//				}

            }
        });
        DOB_Borrower3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                if (DOB_Borrower3.getText().toString().equals("Select Date")) {
                    strDate3 = "";
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerdialog = createDateDialog(strDate3, mContext, mDateSetListener3);
                    datepickerdialog.show(getFragmentManager(), "Datepickerdialog");
                } else {
                    strDate3 = DOB_Borrower3.getText().toString();
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerdialog = createDateDialog(strDate3, mContext, mDateSetListener3);
                    datepickerdialog.show(getFragmentManager(), "Datepickerdialog");

                }
//				if(DOB_Borrower3.getText().toString().equals("Select Date"))
//				{
//					new DatePickerDialog(RinnRakshaSingleActivity.this,R.style.datepickerstyle,borrower3datepickerListener,mYear,mMonth,mDay).show();
//				}
//				else
//				{
//					String dateArray[] = DOB_Borrower3.getText().toString().split("/");
//					new DatePickerDialog(RinnRakshaSingleActivity.this,R.style.datepickerstyle,
//							borrower3datepickerListener, Integer.parseInt(dateArray[2].trim()),Integer.parseInt(dateArray[0].trim())-1,Integer.parseInt(dateArray[1].trim())).show();
//				}
            }
        });
        //UI Elements
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if (valLoanAmt() && valMembershipDate() && valLoanDisbursmentDate() && valdisburstmentMembershipdate()
                        && valBorrowerDetails() && valAgeForAllBorrowers("Primary Borrower")
                        && valAgeForAllBorrowers("Second Borrower") && valAgeForAllBorrowers("Third Borrower")
                        && valLoanTerm() && valMoratoriumPeriod() && valBorrowerLoanShare()) {
                    addListenerOnSubmit();
                }

            }

        });


    }

    /*************************** Item Listener Ends here **************************************************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private final com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener mDateSetListener = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (++monthOfYear) + "/" + dayOfMonth + "/" + year;
            membershipDate.setText(date);
        }
    };
    private final com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener mDisbursementDateSetListener = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (++monthOfYear) + "/" + dayOfMonth + "/" + year;
            disbursementDate.setText(date);
        }
    };
    private final com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener mDateSetListener1 = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (++monthOfYear) + "/" + dayOfMonth + "/" + year;
            DOB_Borrower1.setText(date);
        }
    };

    private final com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener mDateSetListener2 = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (++monthOfYear) + "/" + dayOfMonth + "/" + year;
            DOB_Borrower2.setText(date);
        }
    };

    private final com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener mDateSetListener3 = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            String date = (++monthOfYear) + "/" + dayOfMonth + "/" + year;
            DOB_Borrower3.setText(date);
        }
    };

    private void addListenerOnSubmit() {


        DOB_Borrower1Str = DOB_Borrower1.getText().toString();
        DOB_Borrower2Str = DOB_Borrower2.getText().toString();
        DOB_Borrower3Str = DOB_Borrower3.getText().toString();
        membershipDateStr = membershipDate.getText().toString();
        coBorrowerOptionStr = isCoBorrower.getSelectedItem().toString();
        moratoPeriodStr = moratoPeriod.getSelectedItem().toString();
        loanAmountStr = loanAmt.getText().toString();
        loanTermStr = loanTerm.getText().toString();
        loanTypeStr = loanType.getSelectedItem().toString();
        if (loanType.getSelectedItem().toString().equals("Personal Loan")) {
            loanSubCategoryStr = loanSubCategory.getSelectedItem().toString();
        } else {
            loanSubCategoryStr = "";
        }
        interestRateRangeStr = interestRateRange.getSelectedItem().toString();
        isMoratoCheckedStr = isMoratoChecked.getSelectedItem().toString();
        premPaidByStr = premPaidBy.getSelectedItem().toString();
        CIRStr = CIR.getText().toString();
        isForStaffStr = isForStaff.getSelectedItem().toString();
        isJKResidentStr = isJKResident.getSelectedItem().toString();
        optionStr = option.getSelectedItem().toString();
        isCoBorrowerStr = isCoBorrower.getSelectedItem().toString();
        LoanShareBorrower1Str = loanShare1.getText().toString();
        LoanShareBorrower2Str = loanShare2.getText().toString();
        LoanShareBorrower3Str = loanShare3.getText().toString();
        isIntPayDuringMoratoStr = isIntPayDuringMorato.getSelectedItem().toString();
        premFrequency = premFreq.getSelectedItem().toString();

        ProgressDialog progressDialog = ProgressDialog.show(this, "Calculating", "Please Wait");
        AsyncServerHits serverHitsService = new AsyncServerHits(mContext, progressDialog);
        serverHitsService.execute();


    }


    /********** Modified by Akshaya on 20-oct-15 start**********/
    //validate membership date
    private boolean valBorrowerLoanShare() {

        StringBuilder error = new StringBuilder();

        double sumAssured1, sumAssured2, sumAssured3 = 0;
        if (loanType.getSelectedItem().toString().equals("Home Loan") && option.getSelectedItem().toString().equals("2") &&
                (isCoBorrower.getSelectedItem().toString().equals("2 Co-Borrowers") || isCoBorrower.getSelectedItem().toString().equals("3 Co-Borrowers"))) {
            int loanSharePerc1, loanSharePerc2, loanSharePerc3, sum;


            if (loanShare1.getText().toString().equals("")) {
                error.append("Please Enter 1st Borrower Loan Share in Percentage");
            } else if (loanShare2.getText().toString().equals("")) {
                error.append("Please Enter 2nd Borrower Loan Share in Percentage");
            } else if (loanShare3.getText().toString().equals("") && isCoBorrower.getSelectedItem().toString().equals("3 Co-Borrowers")) {
                error.append("Please Enter 3rd Borrower Loan Share in Percentage");
            } else {

                loanSharePerc1 = Integer.parseInt(loanShare1.getText().toString());
                loanSharePerc2 = Integer.parseInt(loanShare2.getText().toString());

                sumAssured1 = (Double.parseDouble("" + (loanSharePerc1)) / 100) * Double.parseDouble(loanAmt.getText().toString());
                sumAssured2 = (Double.parseDouble("" + (loanSharePerc2)) / 100) * Double.parseDouble(loanAmt.getText().toString());


                if (isCoBorrower.getSelectedItem().toString().equals("3 Co-Borrowers")) {

                    loanSharePerc3 = Integer.parseInt(loanShare3.getText().toString());
                    sumAssured3 = (Double.parseDouble("" + (loanSharePerc3)) / 100) * Double.parseDouble(loanAmt.getText().toString());
                    sum = loanSharePerc1 + loanSharePerc2 + loanSharePerc3;
                } else
                    sum = loanSharePerc1 + loanSharePerc2;

                if (sum != 100)
                    error.append("\nSum of the Borrower Loan Share in Percentage should be equal to 100");
                else {
                    if (sumAssured1 < 10000)
                        error.append("\nSum Assured for Borrower should not be less than Rs.10,000");
                    if (sumAssured2 < 10000)
                        error.append("\nSum Assured for Co-Borrower 1 should not be less than Rs.10,000");
                    if (sumAssured3 < 10000 && isCoBorrower.getSelectedItem().toString().equals("3 Co-Borrowers"))
                        error.append("\nSum Assured for Co-Borrower 2 should not be less than Rs.10,000");
                }

//						System.out.println(" sumAssured1 "+sumAssured1+"   "+sumAssured2+"   "+sumAssured3);
            }


            if (!error.toString().equals("")) {
                showAlert.setMessage(error);

                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                showAlert.show();
                return false;
            } else
                return true;
        } else
            return true;

    }

    /********** Modified by Akshaya on 20-oct-15 end**********/


    // Loan Term Validation
    private boolean valLoanTerm() {
        int maxLoanTerm = 0;
        int minLoanTerm;

        String error = "";

        if (loanTerm.getText().toString().equals("")) {
            error = "Please enter Loan Term";
        } else if (loanType.getSelectedItem().toString().equals("Home Loan")) {
            // System.out.println("> In Home Loan");
            if (isForStaff.getSelectedItem().toString().equals("Staff")) {
                // System.out.println("> In Home Loan");

                if (isCoBorrower.getSelectedItem().toString()
                        .equals("Only Primary Borrower")) {
                    maxLoanTerm = Math.min(
                            (840 - ((commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower1, membershipDate)) * 12)), 360);
                    // System.out.println("year diff primary borrower " +
                    // commonForAllProd.getYearDiffRinnRaksha_(DOB_Borrower1,memFormDate));

                } else if (isCoBorrower.getSelectedItem().toString()
                        .equals("2 Co-Borrowers")) {
                    maxLoanTerm = Math.min(840 - (commonForAllProd
                                    .getAgeRinnRaksha(DOB_Borrower1, membershipDate) * 12),
                            (Math.min((840 - (commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower2, membershipDate) * 12)), 360)));
                    // System.out.println("year diff 2nd borrower " +
                    // commonForAllProd.getYearDiffRinnRaksha_(DOB_Borrower2,memFormDate));

                } else if (isCoBorrower.getSelectedItem().toString()
                        .equals("3 Co-Borrowers")) {
                    maxLoanTerm = Math.min(Math.min(
                            (840 - (commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower1, membershipDate) * 12)),
                            (840 - (commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower2, membershipDate) * 12))), Math
                            .min((840 - (commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower3, membershipDate) * 12)), 360));

                }

                // System.out.println(">>>>>>>>> Max Loan Term -> "+maxLoanTerm);

                if ((Integer.parseInt(loanTerm.getText().toString()) < 96)
                        || (Integer.parseInt(loanTerm.getText().toString()) > maxLoanTerm)) {
                    error = "Please enter valid Loan Term [Min.96 Months & Max."
                            + maxLoanTerm + " Months]";

                }
            } else {

                if (isCoBorrower.getSelectedItem().toString()
                        .equals("Only Primary Borrower")) {
                    maxLoanTerm = Math.min(
                            (900 - ((commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower1, membershipDate)) * 12)), 360);
                    // System.out.println("year diff primary borrower " +
                    // commonForAllProd.getYearDiffRinnRaksha_(DOB_Borrower1,memFormDate));

                } else if (isCoBorrower.getSelectedItem().toString()
                        .equals("2 Co-Borrowers")) {
                    maxLoanTerm = Math.min(900 - (commonForAllProd
                                    .getAgeRinnRaksha(DOB_Borrower1, membershipDate) * 12),
                            (Math.min((900 - (commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower2, membershipDate) * 12)), 360)));
                    // System.out.println("year diff 2nd borrower " +
                    // commonForAllProd.getYearDiffRinnRaksha_(DOB_Borrower2,memFormDate));

                } else if (isCoBorrower.getSelectedItem().toString()
                        .equals("3 Co-Borrowers")) {
                    maxLoanTerm = Math.min(Math.min(
                            (900 - (commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower1, membershipDate) * 12)),
                            (900 - (commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower2, membershipDate) * 12))), Math
                            .min((900 - (commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower3, membershipDate) * 12)), 360));

                }

                // System.out.println(">>>>>>>>> Max Loan Term -> "+maxLoanTerm);

                if ((Integer.parseInt(loanTerm.getText().toString()) < 96)
                        || (Integer.parseInt(loanTerm.getText().toString()) > maxLoanTerm)) {
                    error = "Please enter valid Loan Term [Min.96 Months & Max."
                            + maxLoanTerm + " Months]";

                }
            }
        } else if (loanType.getSelectedItem().toString()
                .equals("Personal Loan")) {

            if (loanSubCategory.getSelectedItem().toString()
                    .equals("Personal Loan")
                    || loanSubCategory.getSelectedItem().toString()
                    .equals("Mortgage Loan")) {

                if (isCoBorrower.getSelectedItem().toString()
                        .equals("Only Primary Borrower")) {
                    maxLoanTerm = Math
                            .min((840 - ((commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower1, membershipDate)) * 12)), 120);

                    // System.out.println("max loan term"+maxLoanTerm);
                    // System.out.println("date diff "+
                    // commonForAllProd.getYearDiffRinnRaksha_(DOB_Borrower1,memFormDate));

                } else if (isCoBorrower.getSelectedItem().toString()
                        .equals("2 Co-Borrowers")) {
                    maxLoanTerm = Math.min(
                            (840 - ((commonForAllProd.getAgeRinnRaksha(
                                    membershipDate, DOB_Borrower1)) * 12)),
                            Math.min((840 - ((commonForAllProd
                                    .getAgeRinnRaksha(membershipDate,
                                            DOB_Borrower2)) * 12)), 120));
                } else if (isCoBorrower.getSelectedItem().toString()
                        .equals("3 Co-Borrowers")) {
                    maxLoanTerm = Math.min(Math.min(
                            (840 - ((commonForAllProd.getAgeRinnRaksha(
                                    membershipDate, DOB_Borrower1)) * 12)),
                            (840 - ((commonForAllProd.getAgeRinnRaksha(
                                    membershipDate, DOB_Borrower2)) * 12))),
                            Math.min((840 - ((commonForAllProd
                                    .getAgeRinnRaksha(membershipDate,
                                            DOB_Borrower3)) * 12)), 120));
                }
            } else {
                if (isCoBorrower.getSelectedItem().toString()
                        .equals("Only Primary Borrower")) {
                    maxLoanTerm = Math
                            .min((840 - ((commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower1, membershipDate)) * 12)), 360);
                }
                // not in excel sheet
                else if (isCoBorrower.getSelectedItem().toString()
                        .equals("2 Co-Borrowers")) {
                    maxLoanTerm = Math.min(
                            (840 - ((commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower1, membershipDate)) * 12)),
                            Math.min((840 - ((commonForAllProd
                                    .getAgeRinnRaksha(
                                            DOB_Borrower2, membershipDate)) * 12)), 360));
//					maxLoanTerm = Math
//							.min((840 - ((commonForAllProd.getAgeRinnRaksha(
//									DOB_Borrower2, membershipDate)) * 12)), 360);
                } else if (isCoBorrower.getSelectedItem().toString()
                        .equals("3 Co-Borrowers")) {
                    maxLoanTerm = Math.min(Math.min(
                            (840 - ((commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower1, membershipDate)) * 12)),
                            (840 - ((commonForAllProd.getAgeRinnRaksha(
                                    DOB_Borrower2, membershipDate)) * 12))),
                            Math.min((840 - ((commonForAllProd
                                    .getAgeRinnRaksha(
                                            DOB_Borrower3, membershipDate)) * 12)), 360));

                    System.out.print((840 - ((commonForAllProd.getAgeRinnRaksha(
                            membershipDate, DOB_Borrower1)) * 12)));

                    System.out.print(Math.min(
                            (840 - ((commonForAllProd.getAgeRinnRaksha(
                                    membershipDate, DOB_Borrower1)) * 12)),
                            (840 - ((commonForAllProd.getAgeRinnRaksha(
                                    membershipDate, DOB_Borrower2)) * 12))));
//					maxLoanTerm = Math
//							.min((840 - ((commonForAllProd.getAgeRinnRaksha(
//									DOB_Borrower3, membershipDate)) * 12)), 360);
                }
            }

            if (loanSubCategory.getSelectedItem().toString()
                    .equals("Home Loan Equity")) {
                minLoanTerm = 36;
            } else {
                minLoanTerm = 24;
            }

            if (Integer.parseInt(loanTerm.getText().toString()) < minLoanTerm
                    || Integer.parseInt(loanTerm.getText().toString()) > maxLoanTerm) {
                error = "Please enter valid Loan Term [Min." + minLoanTerm
                        + " Months & Max." + maxLoanTerm + " Months]";
            }

        } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan")) {
            if (isCoBorrower.getSelectedItem().toString()
                    .equals("Only Primary Borrower")) {
                maxLoanTerm = Math.min(
                        (840 - ((commonForAllProd.getAgeRinnRaksha(
                                DOB_Borrower1, membershipDate)) * 12)), 180);
            } else if (isCoBorrower.getSelectedItem().toString()
                    .equals("2 Co-Borrowers")) {
                maxLoanTerm = Math.min(840 - (commonForAllProd
                                .getAgeRinnRaksha(DOB_Borrower1, membershipDate) * 12),
                        (Math.min((840 - (commonForAllProd.getAgeRinnRaksha(
                                DOB_Borrower2, membershipDate) * 12)), 180)));
            } else if (isCoBorrower.getSelectedItem().toString()
                    .equals("3 Co-Borrowers")) {
                maxLoanTerm = Math.min(Math.min(
                        (840 - (commonForAllProd.getAgeRinnRaksha(
                                DOB_Borrower1, membershipDate) * 12)),
                        (840 - (commonForAllProd.getAgeRinnRaksha(
                                DOB_Borrower2, membershipDate) * 12))), Math
                        .min((840 - (commonForAllProd.getMonthDiff(
                                DOB_Borrower3, membershipDate))), 180));
            }
            // System.out.println(">>>>>>>>> Max Loan Term -> "+maxLoanTerm);

            if (Integer.parseInt(loanTerm.getText().toString()) < 36
                    || Integer.parseInt(loanTerm.getText().toString()) > maxLoanTerm) {
                error = "Please enter valid Loan Term [Min.36 Months & Max."
                        + maxLoanTerm + " Months]";
            }
        } else if (loanType.getSelectedItem().toString()
                .equals("Education Loan")) {
            if (isCoBorrower.getSelectedItem().toString()
                    .equals("Only Primary Borrower")) {
                maxLoanTerm = Math.min(
                        (840 - (commonForAllProd.getAgeRinnRaksha(
                                DOB_Borrower1, membershipDate) * 12)), 240);

            } else if (isCoBorrower.getSelectedItem().toString()
                    .equals("2 Co-Borrowers")) {
                maxLoanTerm = Math.min(
                        (840 - (commonForAllProd.getAgeRinnRaksha(
                                DOB_Borrower1, membershipDate) * 12)), Math
                                .min((851 - (commonForAllProd.getAgeRinnRaksha(
                                        DOB_Borrower2, membershipDate) * 12)),
                                        240));

            } else if (isCoBorrower.getSelectedItem().toString()
                    .equals("3 Co-Borrowers")) {
                maxLoanTerm = Math.min(Math.min(
                        (840 - ((commonForAllProd.getAgeRinnRaksha(
                                DOB_Borrower1, membershipDate)) * 12)),
                        (840 - ((commonForAllProd.getAgeRinnRaksha(
                                DOB_Borrower2, membershipDate)) * 12))), Math
                        .min((840 - ((commonForAllProd.getAgeRinnRaksha(
                                DOB_Borrower3, membershipDate)) * 12)), 240));
            }
            // System.out.println(">>>>>>>>> Max Loan Term -> "+maxLoanTerm);
            if (Integer.parseInt(loanTerm.getText().toString()) < 36
                    || Integer.parseInt(loanTerm.getText().toString()) > maxLoanTerm) {
                error = "Please enter valid Loan Term [Min.36 Months & Max."
                        + maxLoanTerm + " Months]";
            }
        }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        }

        return true;
    }

    public boolean valInterestRateRange() {
        String error = "";
        if (interestRateRange.getSelectedItem().toString().equals("Select Interest Rate Range")) {
            error = "Please Select Interest Rate Range";
        }
        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        }
        return true;
    }

    //Validate loan Amount
    private boolean valLoanAmt() {
        String error = "";

        //Try catch for NumberFormatException if user enters loan amount greater than Integer max limit.
        try {


            if (loanAmt.getText().toString().equals("")) {
                error = "Please enter Loan Amount";
            } else if (loanType.getSelectedItem().toString().equals("Home Loan") && Integer.parseInt(loanAmt.getText().toString()) < 10000) {
                error = "Please enter valid Loan Amount(Min. limit is Rs.10,000)";

            } else if (loanType.getSelectedItem().toString().equals("Personal Loan") && loanSubCategory.getSelectedItem().toString().equals("Mortgage Loan")) {
                if ((Integer.parseInt(loanAmt.getText().toString()) < 50000) || (Integer.parseInt(loanAmt.getText().toString()) > 10000000)) {
                    error = "Please enter Loan Amount in the range of Rs. 50,000 to Rs. 1,00,00,000";

                }
            }
            //***************** modified by vrushali chaudhari*****************************
            else if (loanType.getSelectedItem().toString().equals("Personal Loan") && loanSubCategory.getSelectedItem().toString().equals("Personal Loan")) {
                if ((Integer.parseInt(loanAmt.getText().toString()) < 10000) || (Integer.parseInt(loanAmt.getText().toString()) > 20000000)) {
                    error = "Please enter Loan Amount in the range of Rs. 10,000 to Rs. 2,00,00,000";
                }
            } else if (loanType.getSelectedItem().toString().equals("Personal Loan") && loanSubCategory.getSelectedItem().toString().equals("Home Loan Equity")) {
                if ((Integer.parseInt(loanAmt.getText().toString()) < 50000) || (Integer.parseInt(loanAmt.getText().toString()) > 200000000)) {
                    error = "Please enter Loan Amount in the range of Rs. 50,000 to Rs. 20,00,00,000";
                }
            } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan") && ((Integer.parseInt(loanAmt.getText().toString()) < 10000) || (Integer.parseInt(loanAmt.getText().toString()) > 5000000))) {
                error = "Please enter Loan Amount in the range of Rs. 10,000 to Rs. 50,00,000";
            }
//			else if (loanType.getSelectedItem().toString().equals("Education Loan")&& ((Integer.parseInt(loanAmt.getText().toString()) < 10000) || (Integer.parseInt(loanAmt.getText().toString()) > 3000000))) 
//			{
//				error = "Please enter Loan Amount in the range of Rs. 10,000 to Rs. 30,00,000";
//
//			}
            else if (loanType.getSelectedItem().toString().equals("Education Loan") && spnr_bankOption.getSelectedItem().toString().equals("Associate Bank") && ((Integer.parseInt(loanAmt.getText().toString()) < 10000) || (Integer.parseInt(loanAmt.getText().toString()) > 3000000))) {
                error = "Please enter Loan Amount for Associate Bank in the range of Rs. 10,000 to Rs. 30,00,000";

            } else if (loanType.getSelectedItem().toString().equals("Education Loan") && spnr_bankOption.getSelectedItem().toString().equals("SBI") && ((Integer.parseInt(loanAmt.getText().toString()) < 10000) || (Integer.parseInt(loanAmt.getText().toString()) > 15000000))) {
                error = "Please enter Loan Amount for SBI Bank in the range of Rs. 10,000 to Rs. 1,50,00,000";

            }
        } catch (Exception e) {
            error = "Please enter valid Loan Amount.";
        }


        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                }
            });
            showAlert.show();
            return false;
        }


        return true;
    }

    //Validate whether date of birth is entered
    private boolean valBorrowerDetails() {
        String error = "";


        if (DOB_Borrower1.getText().toString().equals("Select Date")) {
            error = "Please enter Primary Borrower Date of Birth";

        } else {
            if (loanType.getSelectedItem().toString().equals("Home Loan") || loanType.getSelectedItem().toString().equals("Vehicle Loan") || loanType.getSelectedItem().toString().equals("Personal Loan")) {
                if (isCoBorrower.getSelectedItem().toString().equals("3 Co-Borrowers")) {
                    if (DOB_Borrower2.getText().toString().equals("Select Date")) {
                        error = "Please enter Second Borrower Date of Birth";

                    } else if (DOB_Borrower3.getText().toString().equals("Select Date")) {
                        error = "Please enter Third Borrower Date of Birth";

                    }
                } else if (isCoBorrower.getSelectedItem().toString().equals("2 Co-Borrowers")) {
                    if (DOB_Borrower2.getText().toString().equals("Select Date")) {
                        error = "Please enter Second Borrower Date of Birth";

                    }
                }
            }
        }
        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                }
            });
            showAlert.show();
            return false;
        }
        return true;
    }

    //validate disbursement date
    public boolean valLoanDisbursmentDate() {
        String error = "";
        str_rinnraksha_dob_disbursement = disbursementDate.getText().toString();
        if (str_rinnraksha_dob_disbursement.equalsIgnoreCase("")
                || str_rinnraksha_dob_disbursement
                .equalsIgnoreCase("Select Date")) {
            error = "Please Select Date of First Disbursement";

            // return false;
        }

        if (!error.equalsIgnoreCase("")) {
            showAlert.setMessage(error);

            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            commonMethods.clearFocusable(disbursementDate);
                            commonMethods.setFocusable(disbursementDate);
                            disbursementDate.requestFocus();
                        }
                    });
            showAlert.show();

            return false;
        }
        return true;
    }

    public boolean valdisburstmentMembershipdate() {

        //if (rb_bank_type_SBI.isChecked()) {
        //str_rinnraksha_dob_disbursement = disbursementDate.getText().toString();

        String error = "";
        if (!str_rinnraksha_dob_disbursement.equalsIgnoreCase("")) {
            try {
                DateFormat userDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date date_membershipDate = userDateFormat.parse(membershipDate.getText().toString());
                Date date_rinnraksha_dob_disbursement = userDateFormat.parse(str_rinnraksha_dob_disbursement);
                getDateDiff(date_membershipDate, date_rinnraksha_dob_disbursement);
                if (loanType.getSelectedItem().toString().equals("Home Loan")) {
                    if (diffDays <= 90 && diffDays <= 365) {
                        str_SBI_home_loan_type = "New Home Loan";
                    } else {
                        str_SBI_home_loan_type = "Old Home Loan";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (str_rinnraksha_dob_disbursement.equalsIgnoreCase("")
                || str_rinnraksha_dob_disbursement
                .equalsIgnoreCase("Select Date")) {
            error = "Please Select Date of First Disbursement";
        } else if (diffDays > 90 && !loanType.getSelectedItem().toString().equals("Home Loan")) {
            error = "Product not allowed for old Loans more than 3 Month";
        }
//        else if (diffDays > 90 && loanType.getSelectedItem().toString().equals("Home Loan") ) {
//            error = "Product not allowed for old Loans more than 3 Month";
//        }
        else if (diffDays > 1095 && loanType.getSelectedItem().toString().equals("Home Loan")) {
            error = "Product not allowed for old Loans more than 3 Years";
        } else if (diffDays > 1095 && loanType.getSelectedItem().toString().equals("Home Loan")) {
            error = "Product not allowed for old Loans more than 3 Years";
        }

        if (!error.equalsIgnoreCase("")) {
            showAlert.setMessage(error);

            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            commonMethods.clearFocusable(disbursementDate);
                            commonMethods.setFocusable(disbursementDate);
                            disbursementDate.requestFocus();
                        }
                    });
            showAlert.show();

            return false;
        }
        return true;

    }

    //validate membership date
    private boolean valMembershipDate() {
        if (membershipDate.getText().toString().equals("Select Date")) {
            showAlert.setMessage("Please Enter MemberShip Date");

            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            showAlert.show();
            return false;
        }
        return true;
    }

    // Borrowers Age Validation
    private boolean valAgeForAllBorrowers(String whichBorrower) {
        int minAgeLimit = 0, maxAgeLimit = 0;
        premFrequency = premFreq.getSelectedItem().toString();
        if (loanType.getSelectedItem().toString().equals("Home Loan")) {
            if (premFrequency.equalsIgnoreCase("Single")) {
                minAgeLimit = prop.minAge_HomeLoan;
                maxAgeLimit = prop.maxAge_HomeLoan;
            } else {
                minAgeLimit = prop.minAge_HomeLoan;
                maxAgeLimit = 62;
            }
            if (str_SBI_home_loan_type.equalsIgnoreCase("Old Home Loan")) {
                maxAgeLimit = 60;
            }
        } else if (loanType.getSelectedItem().toString().equals("Personal Loan")) {
            minAgeLimit = prop.minAge_PersonalLoan;
            maxAgeLimit = prop.maxAge_PersonalLoan;

            if (loanSubCategory.getSelectedItem().toString().equals("Home Loan Equity")) {
                minAgeLimit = prop.minAge_PersonalLoan_HomeLoanEquity;
                maxAgeLimit = prop.maxAge_PersonalLoan_HomeLoanEquity;

            }

        } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan")) {
            minAgeLimit = prop.minAge_VehicleLoan;
            maxAgeLimit = prop.maxAge_VehicleLoan;
        } else if (loanType.getSelectedItem().toString().equals("Education Loan")) {
            minAgeLimit = prop.minAge_EducationLoan;
            maxAgeLimit = prop.maxAge_EducationLoan;
        }

        int ageBorrower = 9999;


        if (whichBorrower.equals("Primary Borrower")) {
            int ageBorrower1 = commonForAllProd.getAgeRinnRaksha(DOB_Borrower1, membershipDate);
            ageBorrower = ageBorrower1;

        } else if (whichBorrower.equals("Second Borrower") && (loanType.getSelectedItem().toString().equals("Home Loan") || loanType.getSelectedItem().toString().equals("Personal Loan") || loanType.getSelectedItem().toString().equals("Vehicle Loan")) && (isCoBorrower.getSelectedItem().toString().equals("2 Co-Borrowers") || isCoBorrower.getSelectedItem().toString().equals("3 Co-Borrowers"))) {
            int ageBorrower2 = commonForAllProd.getAgeRinnRaksha(DOB_Borrower2, membershipDate);
            ageBorrower = ageBorrower2;
        } else if (whichBorrower.equals("Third Borrower") && (loanType.getSelectedItem().toString().equals("Home Loan") || loanType.getSelectedItem().toString().equals("Personal Loan")) && isCoBorrower.getSelectedItem().toString().equals("3 Co-Borrowers")) {
            int ageBorrower3 = commonForAllProd.getAgeRinnRaksha(DOB_Borrower3, membershipDate);
            ageBorrower = ageBorrower3;
        }
        if (ageBorrower != 9999) {

            if ((ageBorrower) < minAgeLimit || (ageBorrower) > maxAgeLimit) {
                showAlert.setMessage("Please enter a valid Date of Birth for " + whichBorrower + ". Min age limit is " + minAgeLimit + " yrs & max is " + maxAgeLimit + " yrs");
                showAlert.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                showAlert.show();
                return false;
            }
        }

        return true;


    }

    private boolean valMoratoriumPeriod() {
        String error = "";
        if (loanType.getSelectedItem().toString().equals("Education Loan")) {
            if (Integer.parseInt(loanTerm.getText().toString()) < Integer.parseInt(moratoPeriod.getSelectedItem().toString())) {
                error = "Please enter Moratorium Period less than Loan Term";
            } else if ((Integer.parseInt(loanTerm.getText().toString()) - Integer.parseInt(moratoPeriod.getSelectedItem().toString())) < 12) {
                error = "Please enter Moratorium Period less than " + (Integer.parseInt(loanTerm.getText().toString()) - 12) + "";
            }

        }

        if (!error.equals("")) {
            showAlert.setMessage(error);
            showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                }
            });
            showAlert.show();
            return false;
        }

        return true;
    }


    /********************* Input screen arrangement starts here ***************************************************/

    //Set Input screen as per loan type and staff/Non Staff
    private void resetInputScreen() {

        ArrayAdapter<String> nonStaff_HomeLoanAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, prop.NonStaff_HomeLoan);
        ArrayAdapter<String> nonStaff_HomeLoanAdapter_old = new ArrayAdapter<>(this, R.layout.spinner_item, prop.NonStaff_HomeLoan_old);
        final ArrayAdapter<String> Staff_HomeLoanAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, prop.Staff_HomeLoan);
        final ArrayAdapter<String> Staff_LoanSubCategory_PersonalLoanAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, prop.Staff_LoanSubCategory_PersonalLoan);
        final ArrayAdapter<String> NonStaff_LoanSubCategory_PersonalLoanAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, prop.NonStaff_LoanSubCategory_PersonalLoan);
        final ArrayAdapter<String> Staff_PersonalLoanAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, prop.Staff_PersonalLoan);

        final ArrayAdapter<String> VechicleBorrowersAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, prop.VechicleBorrowers);
        final ArrayAdapter<String> Staff_VechicleLoanAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, prop.Staff_VechicleLoan);
        final ArrayAdapter<String> NonStaff_VechicleLoanAdatpter = new ArrayAdapter<>(this, R.layout.spinner_item, prop.NonStaff_VechicleLoan);
        final ArrayAdapter<String> Staff_EducationLoanAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, prop.Staff_EducationLoan);
        final ArrayAdapter<String> NonStaff_EducationLoanAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, prop.NonStaff_EducationLoan);
        final ArrayAdapter<String> HoamLoanBorrowersAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, prop.HoamLoanBorrowers);

        final ArrayAdapter<String> Moratorium_HomeLoanStaffAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, prop.Moratorium_HoamLoan_Staff);
        final ArrayAdapter<String> Moratorium_HomeLoanNonStaffAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, prop.Moratorium_HoamLoan_NonStaff);

        final ArrayAdapter<String> Moratorium_EducationLoanAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, prop.Moratorium_EducationLoan);
        nonStaff_HomeLoanAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        nonStaff_HomeLoanAdapter_old.setDropDownViewResource(R.layout.spinner_dropdown);
        Staff_HomeLoanAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        Staff_LoanSubCategory_PersonalLoanAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        NonStaff_LoanSubCategory_PersonalLoanAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        Staff_PersonalLoanAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

        VechicleBorrowersAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        Staff_VechicleLoanAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        NonStaff_VechicleLoanAdatpter.setDropDownViewResource(R.layout.spinner_dropdown);
        Staff_EducationLoanAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        NonStaff_EducationLoanAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        HoamLoanBorrowersAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        Moratorium_HomeLoanStaffAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        Moratorium_HomeLoanNonStaffAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        Moratorium_EducationLoanAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

        if (loanType.getSelectedItem().toString().equals("Home Loan")) {

            txt_bankOption.setVisibility(View.GONE);
            spnr_bankOption.setVisibility(View.GONE);
            premFreq.setEnabled(true);
//			premFreq.setSelection(0);
            LoanSubCategory.setVisibility(View.GONE);
            tableisMorato.setVisibility(View.VISIBLE);
            tableiscoBorrower.setVisibility(View.VISIBLE);
            tableisCIR.setVisibility(View.VISIBLE);
            moratoperiod.setVisibility(View.GONE);
            moratointpay.setVisibility(View.GONE);
            isCoBorrower.setAdapter(HoamLoanBorrowersAdapter);
            HoamLoanBorrowersAdapter.notifyDataSetChanged();
            //moratoPeriod.setAdapter(Moratorium_HoamLoanAdapter);
            //Moratorium_HoamLoanAdapter.notifyDataSetChanged();
            isMoratoChecked.setSelection(0);
            if (isForStaff.getSelectedItem().toString().equals("Staff")) {
                interestRateRange.setAdapter(Staff_HomeLoanAdapter);
                Staff_HomeLoanAdapter.notifyDataSetChanged();
                moratoPeriod.setAdapter(Moratorium_HomeLoanStaffAdapter);
                Moratorium_HomeLoanStaffAdapter.notifyDataSetChanged();
                premTerm.setVisibility(View.GONE);
            } else {
                if (premFreq.getSelectedItem().toString().equals("Single")) {
                    interestRateRange.setAdapter(nonStaff_HomeLoanAdapter);
                    nonStaff_HomeLoanAdapter.notifyDataSetChanged();
                    premTerm.setVisibility(View.GONE);
                } else {
                    interestRateRange.setAdapter(nonStaff_HomeLoanAdapter_old);
                    nonStaff_HomeLoanAdapter_old.notifyDataSetChanged();
                    premTerm.setVisibility(View.VISIBLE);
                    premPayingTerm.setText("5");
                    premPayingTerm.setEnabled(false);
                }
                moratoPeriod.setAdapter(Moratorium_HomeLoanNonStaffAdapter);
                Moratorium_HomeLoanNonStaffAdapter.notifyDataSetChanged();

            }
            if (isCoBorrower.getSelectedItem().toString().equals("Only Primary Borrower")) {
                tbrw_option.setVisibility(View.GONE);
                tbrw_loanshare1.setVisibility(View.GONE);
                tbrw_loanshare2.setVisibility(View.GONE);
                tbrw_loanshare3.setVisibility(View.GONE);
            } else if (loanType.getSelectedItem().toString().equals("Home Loan")) {
                tbrw_option.setVisibility(View.VISIBLE);
                updateOption();
            } else {
                tbrw_option.setVisibility(View.GONE);
                updateOption();
            }

        } else if (loanType.getSelectedItem().toString().equals("Personal Loan")) {
            txt_bankOption.setVisibility(View.GONE);
            spnr_bankOption.setVisibility(View.GONE);
            premFreq.setEnabled(false);
            premFreq.setSelection(0);
            premTerm.setVisibility(View.GONE);
            LoanSubCategory.setVisibility(View.VISIBLE);
            tableisCIR.setVisibility(View.VISIBLE);
            tableiscoBorrower.setVisibility(View.VISIBLE);
            tableisMorato.setVisibility(View.GONE);
            moratoperiod.setVisibility(View.GONE);
            moratointpay.setVisibility(View.GONE);
            tbrowSecondBorrower.setVisibility(View.GONE);
            tbrowThirdBorrower.setVisibility(View.GONE);
            isCoBorrower.setAdapter(HoamLoanBorrowersAdapter);
            HoamLoanBorrowersAdapter.notifyDataSetChanged();


            if (isForStaff.getSelectedItem().toString().equals("Staff")) {
                loanSubCategory.setAdapter(Staff_LoanSubCategory_PersonalLoanAdapter);
                Staff_LoanSubCategory_PersonalLoanAdapter.notifyDataSetChanged();
                interestRateRange.setAdapter(Staff_PersonalLoanAdapter);
                Staff_PersonalLoanAdapter.notifyDataSetChanged();
            } else {
                loanSubCategory.setAdapter(NonStaff_LoanSubCategory_PersonalLoanAdapter);
                NonStaff_LoanSubCategory_PersonalLoanAdapter.notifyDataSetChanged();
                interestRateRange.setAdapter(Staff_HomeLoanAdapter);
                Staff_HomeLoanAdapter.notifyDataSetChanged();

            }
            tbrw_option.setVisibility(View.GONE);
            tbrw_loanshare1.setVisibility(View.GONE);
            tbrw_loanshare2.setVisibility(View.GONE);
            tbrw_loanshare3.setVisibility(View.GONE);

        } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan")) {
            txt_bankOption.setVisibility(View.GONE);
            spnr_bankOption.setVisibility(View.GONE);
            premFreq.setEnabled(false);
            premFreq.setSelection(0);
            premTerm.setVisibility(View.GONE);
            LoanSubCategory.setVisibility(View.GONE);
            tableisMorato.setVisibility(View.GONE);
            tableiscoBorrower.setVisibility(View.VISIBLE);
            moratoperiod.setVisibility(View.GONE);
            moratointpay.setVisibility(View.GONE);
            isCoBorrower.setAdapter(VechicleBorrowersAdapter);
            VechicleBorrowersAdapter.notifyDataSetChanged();

            if (isForStaff.getSelectedItem().toString().equals("Staff")) {
                interestRateRange.setAdapter(Staff_VechicleLoanAdapter);
                Staff_VechicleLoanAdapter.notifyDataSetChanged();
            } else //Non Staff
            {
                interestRateRange.setAdapter(NonStaff_VechicleLoanAdatpter);
                NonStaff_VechicleLoanAdatpter.notifyDataSetChanged();
            }
            tbrw_option.setVisibility(View.GONE);
            tbrw_loanshare1.setVisibility(View.GONE);
            tbrw_loanshare2.setVisibility(View.GONE);
            tbrw_loanshare3.setVisibility(View.GONE);

        } else if (loanType.getSelectedItem().toString().equals("Education Loan")) {
            txt_bankOption.setVisibility(View.VISIBLE);
            spnr_bankOption.setVisibility(View.VISIBLE);
            premFreq.setEnabled(false);
            premFreq.setSelection(0);
            premTerm.setVisibility(View.GONE);
            LoanSubCategory.setVisibility(View.GONE);
            tableisCIR.setVisibility(View.VISIBLE);
            tableiscoBorrower.setVisibility(View.GONE);
            tableisMorato.setVisibility(View.GONE);
            moratoperiod.setVisibility(View.VISIBLE);
            moratointpay.setVisibility(View.VISIBLE);
            tbrowSecondBorrower.setVisibility(View.GONE);
            tbrowThirdBorrower.setVisibility(View.GONE);
            moratoPeriod.setAdapter(Moratorium_EducationLoanAdapter);
            Moratorium_EducationLoanAdapter.notifyDataSetChanged();
            if (isForStaff.getSelectedItem().toString().equals("Staff")) {
                interestRateRange.setAdapter(Staff_EducationLoanAdapter);
                Staff_EducationLoanAdapter.notifyDataSetChanged();
            } else {
                interestRateRange.setAdapter(NonStaff_EducationLoanAdapter);
                NonStaff_EducationLoanAdapter.notifyDataSetChanged();
            }

            tbrw_option.setVisibility(View.GONE);
            tbrw_loanshare1.setVisibility(View.GONE);
            tbrw_loanshare2.setVisibility(View.GONE);
            tbrw_loanshare3.setVisibility(View.GONE);
        }

    }


    //set Interest rates
    private void setCIR() {


        if (loanType.getSelectedItem().toString().equals("Home Loan")
                && interestRateRange.getSelectedItem().toString()
                .equals("Between 06.00% to 08.49%")) {
            CIR.setText("8.49");

        } else if (loanType.getSelectedItem().toString().equals("Home Loan") && interestRateRange.getSelectedItem().toString().equals("Between 8.50% to 11.50%")) {
            CIR.setText("11.50");

        } else if (loanType.getSelectedItem().toString().equals("Home Loan") && interestRateRange.getSelectedItem().toString().equals("Between 6.00% to 8.00%")) {
            CIR.setText("8.00");

        } else if (loanType.getSelectedItem().toString().equals("Home Loan") && interestRateRange.getSelectedItem().toString().equals("Between 11.50% to 12.75%")) {
            CIR.setText("12.75");

        } else if (loanType.getSelectedItem().toString().equals("Personal Loan") && interestRateRange.getSelectedItem().toString().equals("Between 7.00% to 10.00%")) {
            CIR.setText("10.00");

        } else if (loanType.getSelectedItem().toString().equals("Personal Loan") && interestRateRange.getSelectedItem().toString().equals("Between 14.00% to 16.99%")) {
            CIR.setText("17.00");

        } else if (loanType.getSelectedItem().toString().equals("Personal Loan") && interestRateRange.getSelectedItem().toString().equals("Between 13.76% to 16.99%")) {
            CIR.setText("16.99");

        } else if (loanType.getSelectedItem().toString().equals("Personal Loan") && interestRateRange.getSelectedItem().toString().equals("Between 17.00% to 20.00%")) {
            CIR.setText("20.00");

        } else if (loanType.getSelectedItem().toString().equals("Personal Loan") && interestRateRange.getSelectedItem().toString().equals("Between 10.00% to 12.99%")) {
            CIR.setText("13.00");

        } else if (loanType.getSelectedItem().toString().equals("Personal Loan") && interestRateRange.getSelectedItem().toString().equals("Between 13.00% to 16.00%")) {
            CIR.setText("16.00");

        } else if (loanType.getSelectedItem().toString().equals("Personal Loan") && interestRateRange.getSelectedItem().toString().equals("Between 7.75% to 10.75%")) {
            CIR.setText("10.75");

        } else if (loanType.getSelectedItem().toString().equals("Personal Loan") && interestRateRange.getSelectedItem().toString().equals("Between 10.76% to 13.75%")) {
            CIR.setText("13.75");

        } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan") && interestRateRange.getSelectedItem().toString().equals("Between 7% to 10%(New Auto Loan)")) {
            CIR.setText("10.00");

        } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan") && interestRateRange.getSelectedItem().toString().equals("Between 7% to 10%(Used Auto Loan)")) {
            CIR.setText("10.00");

        } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan") && interestRateRange.getSelectedItem().toString().equals("Between 7% to 10%(Two Wheelers Loan)")) {
            CIR.setText("10.00");

        } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan") && interestRateRange.getSelectedItem().toString().equals("Between 10.25% to 14%(New Auto Loan)")) {
            CIR.setText("14.00");

        } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan") && interestRateRange.getSelectedItem().toString().equals("Between 15% to 18%(Used Auto Loan)")) {
            CIR.setText("18.00");

        } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan") && interestRateRange.getSelectedItem().toString().equals("Between 15% to 18%(Two Wheelers Loan)")) {
            CIR.setText("18.00");

        } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan") && interestRateRange.getSelectedItem().toString().equals("Between 10.25% to 14%(Combo Vehicle Loan)")) {
            CIR.setText("14.00");

        } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan") && interestRateRange.getSelectedItem().toString().equals("Between 8.50% to 10.24%(New Auto Loan)")) {
            CIR.setText("10.24");

        } else if (loanType.getSelectedItem().toString().equals("Education Loan") && interestRateRange.getSelectedItem().toString().equals("Between 7.00% to 10.00%")) {
            CIR.setText("10.00");

        } else if (loanType.getSelectedItem().toString().equals("Education Loan") && interestRateRange.getSelectedItem().toString().equals("Between 12.00% to 15.00%")) {
            CIR.setText("15.00");

        } else if (loanType.getSelectedItem().toString().equals("Education Loan") && interestRateRange.getSelectedItem().toString().equals("Between 09.00% to 11.50%")) {
            CIR.setText("11.50");

        } else if (loanType.getSelectedItem().toString().equals("Education Loan") && interestRateRange.getSelectedItem().toString().equals("Between 11.51% to 15.00%")) {
            CIR.setText("15.00");

        }


    }

    private void updateOption() {
        if (isCoBorrower.getSelectedItem().toString().equals("2 Co-Borrowers") && loanType.getSelectedItem().toString().equals("Home Loan")) {
            tbrw_loanshare1.setVisibility(View.VISIBLE);
            tbrw_loanshare2.setVisibility(View.VISIBLE);
            tbrw_loanshare3.setVisibility(View.GONE);
            txt_option.setText("Each borrower covered to the extent of their liability");
        } else if (isCoBorrower.getSelectedItem().toString().equals("3 Co-Borrowers") && loanType.getSelectedItem().toString().equals("Home Loan")) {
            tbrw_loanshare1.setVisibility(View.VISIBLE);
            tbrw_loanshare2.setVisibility(View.VISIBLE);
            tbrw_loanshare3.setVisibility(View.VISIBLE);
            txt_option.setText("Each borrower covered to the extent of their liability");
        } else {
            tbrw_loanshare1.setVisibility(View.GONE);
            tbrw_loanshare2.setVisibility(View.GONE);
            tbrw_loanshare3.setVisibility(View.GONE);
            txt_option.setText("Each borrower covered for the entire loan amount");
        }
    }


    /****************************** Async Task starts here *******************************************/

    class AsyncRinnRaksha extends AsyncTask<String, Void, String> {
        final Context mContext;
        final ProgressDialog progressDialog;

        String result;
        final DecimalFormat currencyFormat;
        String totFrstYrPremFrPrimaryBorwInclST, intlSumAssuredFrPrimaryBorw, totFrstYrPremFrSecondBorwInclST, intlSumAssuredFrSecondBorw, totFrstYrPremFrThirdBorwInclST, intlSumAssuredFrThirdBorw, medReqFrPrimaryBorw, medReqFrSecondBorw, medReqFrThirdBorw, premInclServiceTaxFrAllBorw, premInclServiceTaxFrAllBorwOvr5Yrs;


        AsyncRinnRaksha(Context mContext, ProgressDialog progressDialog) {
            this.mContext = mContext;
            this.progressDialog = progressDialog;
            currencyFormat = new DecimalFormat("##,##,##,###");
        }

        @Override
        protected String doInBackground(String... param) {


            if (commonMethods.isNetworkConnected(mContext)) {

                try {

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_CALC);


                    switch (isCoBorrowerStr) {
                        case "Only Primary Borrower":

                            request.addProperty("DOB_Borrower1", DOB_Borrower1Str);
                            request.addProperty("DOB_Borrower2", "");
                            request.addProperty("DOB_Borrower3", "");
                            break;
                        case "2 Co-Borrowers":

                            request.addProperty("DOB_Borrower1", DOB_Borrower1Str);
                            request.addProperty("DOB_Borrower2", DOB_Borrower2Str);
                            request.addProperty("DOB_Borrower3", "");

                            break;
                        case "3 Co-Borrowers":
                            request.addProperty("DOB_Borrower1", DOB_Borrower1Str);
                            request.addProperty("DOB_Borrower2", DOB_Borrower2Str);
                            request.addProperty("DOB_Borrower3", DOB_Borrower3Str);

                            break;
                    }

                    request.addProperty("membershipDate", membershipDateStr);

                    request.addProperty("coBorrowerOption", coBorrowerOptionStr);

                    request.addProperty("moratoPeriod", moratoPeriodStr);

                    request.addProperty("loanAmount", loanAmountStr);

                    request.addProperty("loanTerm", loanTermStr);

                    request.addProperty("loanType", loanTypeStr);

                    //Added By Manish on 31-07-2019
                    //request.addProperty("KFC", "false");

                    //Added by bharamu on 2/1/2020
                    //added by bharamu on 2/1/2021
                    if (str_SBI_home_loan_type.equalsIgnoreCase("New Home Loan")) {
                        request.addProperty("KFC", isForStaffStr + "|Yes");
                    } else {
                        request.addProperty("KFC", isForStaffStr + "|No");
                    }

                    if (loanTypeStr.equals("Personal Loan")) {
                        request.addProperty("loanSubCategory", loanSubCategoryStr);
                    } else {
                        request.addProperty("loanSubCategory", "");
                    }

                    request.addProperty("interestRateRange", interestRateRangeStr);


                    request.addProperty("isMoratoChecked", isMoratoCheckedStr);


                    request.addProperty("premPaidBy", premPaidByStr);


                    if (loanTypeStr.equals("Education Loan")) {
                        if (isIntPayDuringMoratoStr.equals("Yes")) {

                            request.addProperty("isIntPayDuringMorato", "Yes");
                        } else if (isIntPayDuringMoratoStr.equals("No")) {

                            request.addProperty("isIntPayDuringMorato", "No");

                        }
                    } else {
                        if ((isMoratoCheckedStr.equals("Yes")) && (isIntPayDuringMoratoStr.equals("Yes"))) {
                            request.addProperty("isIntPayDuringMorato", "Yes");


                        } else if ((isMoratoCheckedStr.equals("Yes")) && (isIntPayDuringMoratoStr.equals("No"))) {
                            request.addProperty("isIntPayDuringMorato", "No");
                        } else if ((isMoratoCheckedStr.equals("No")) && (isIntPayDuringMoratoStr.equals("No"))) {
                            request.addProperty("isIntPayDuringMorato", "No");
                        }

                    }


                    try {
                        request.addProperty("CIR", CIRStr);
                    } catch (Exception e) {
                        request.addProperty("CIR", "0");
                    }

                    if (isForStaffStr.equalsIgnoreCase("true"))
                        request.addProperty("isForStaff", "yes");
                    else
                        request.addProperty("isForStaff", "no");


                    request.addProperty("isJKResident", isJKResidentStr);

                    request.addProperty("option", optionStr);

                    request.addProperty("isCoBorrower", isCoBorrowerStr);

                    if (optionStr.equals("2")) {
                        if (isCoBorrowerStr.equals("2 Co-Borrowers")) {
                            request.addProperty("LoanShareBorrower1", LoanShareBorrower1Str);
                            request.addProperty("LoanShareBorrower2", LoanShareBorrower2Str);
                            request.addProperty("LoanShareBorrower3", "0");
                        } else if (isCoBorrowerStr.equals("3 Co-Borrowers")) {
                            request.addProperty("LoanShareBorrower1", LoanShareBorrower1Str);
                            request.addProperty("LoanShareBorrower2", LoanShareBorrower2Str);
                            request.addProperty("LoanShareBorrower3", LoanShareBorrower3Str);
                        }
                    } else {
                        request.addProperty("LoanShareBorrower1", "0");
                        request.addProperty("LoanShareBorrower2", "0");
                        request.addProperty("LoanShareBorrower3", "0");
                    }


                    request.addProperty("Premfreq", premFrequency);


                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    //Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                    commonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    String SOAP_ACTION_CALC = "http://tempuri.org/getPremiumRinnRakshaNew";
                    androidHttpTransport.call(SOAP_ACTION_CALC, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    String result = response.toString();

//					System.out.println("result " + result);

                    if (!result.contains("<errorMesg>")) {
                        ParseXML prsObj = new ParseXML();

                        result = prsObj.parseXmlTag(result, "rinnRaksha");

                        totFrstYrPremFrPrimaryBorwInclST = prsObj.parseXmlTag(result, "totFrstYrPremFrPrimaryBorwInclST");
                        intlSumAssuredFrPrimaryBorw = prsObj.parseXmlTag(result, "intlSumAssuredFrPrimaryBorw");
                        totFrstYrPremFrSecondBorwInclST = prsObj.parseXmlTag(result, "totFrstYrPremFrSecondBorwInclST");
                        intlSumAssuredFrSecondBorw = prsObj.parseXmlTag(result, "intlSumAssuredFrSecondBorw");
                        totFrstYrPremFrThirdBorwInclST = prsObj.parseXmlTag(result, "totFrstYrPremFrThirdBorwInclST");
                        intlSumAssuredFrThirdBorw = prsObj.parseXmlTag(result, "intlSumAssuredFrThirdBorw");
                        medReqFrPrimaryBorw = prsObj.parseXmlTag(result, "medReqFrPrimaryBorw");
                        medReqFrSecondBorw = prsObj.parseXmlTag(result, "medReqFrSecondBorw");
                        medReqFrThirdBorw = prsObj.parseXmlTag(result, "medReqFrThirdBorw");
                        premInclServiceTaxFrAllBorw = prsObj.parseXmlTag(result, "premInclServiceTaxFrAllBorw");
                        premInclServiceTaxFrAllBorwOvr5Yrs = prsObj.parseXmlTag(result, "premInclServiceTaxFrAllBorwOvr5Yrs");


                        return "Success";

                    } else {
                        return "Server not Found. Please try after some time.";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }

            } else
                return "Please Activate Internet connection";

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            } catch (Exception e) {
                e.getMessage();
            }

            if (result.equals("Success")) {

                Intent i = new Intent(mContext, RinnRakshaSuccess.class);

                if (loanType.getSelectedItem().toString().equals("Home Loan")) {

                    i.putExtra("op", "Total First Year Premium for Primary Borrower Inclusive of Applicable Taxes is Rs." + currencyFormat.format(Double.parseDouble(totFrstYrPremFrPrimaryBorwInclST)));
                    i.putExtra("op1", "Initial Sum Assured for Primary Borrower is Rs." + currencyFormat.format(Double.parseDouble(intlSumAssuredFrPrimaryBorw)));
                    i.putExtra("op2", "Medical Requirements for Primary Borrower is " + medReqFrPrimaryBorw);

                    if (isCoBorrower.getSelectedItem().toString().equals("2 Co-Borrowers")) {
                        i.putExtra("op3", "Total First Year Premium for Second Borrower Inclusive of Applicable Taxes is Rs." + currencyFormat.format(Double.parseDouble(totFrstYrPremFrSecondBorwInclST)));
                        i.putExtra("op4", "Initial Sum Assured for Second Borrower is Rs." + currencyFormat.format(Double.parseDouble(intlSumAssuredFrSecondBorw)));
                        i.putExtra("op5", "Medical Requirements for Second Borrower is " + medReqFrSecondBorw);

                    } else if (isCoBorrower.getSelectedItem().toString().equals("3 Co-Borrowers")) {
                        i.putExtra("op3", "Total First Year Premium for Second Borrower Inclusive of Applicable Taxes is Rs." + currencyFormat.format(Double.parseDouble(totFrstYrPremFrSecondBorwInclST)));
                        i.putExtra("op4", "Initial Sum Assured for Second Borrower is Rs." + currencyFormat.format(Double.parseDouble(intlSumAssuredFrSecondBorw)));
                        i.putExtra("op5", "Medical Requirements for Second Borrower is " + medReqFrSecondBorw);
                        i.putExtra("op6", "Total First Year Premium for Third Borrower Inclusive of Applicable Taxes is Rs." + currencyFormat.format(Double.parseDouble(totFrstYrPremFrThirdBorwInclST)));
                        i.putExtra("op7", "Initial Sum Assured for Third Borrower is Rs." + currencyFormat.format(Double.parseDouble(intlSumAssuredFrThirdBorw)));
                        i.putExtra("op8", "Medical Requirements for Third Borrower is " + medReqFrThirdBorw);
                    }


                    // One Premium Installment Details
                    i.putExtra("op9", "Premium Inclusive of Applicable Taxes Payable for All Borrowers is Rs." + currencyFormat.format(Double.parseDouble(premInclServiceTaxFrAllBorw)));


                    /*Do not Print Payable Over 5 Year Details for Staff */
                    // For Non Staff
//					if(isForStaff.getSelectedItem().toString().equals("Non-Staff"))
//					{
//						i.putExtra("op10","Premium Inclusive of Applicable Taxes Payable for all Borrowers (Payable over 5 years) is Rs."+ currencyFormat.format(Double.parseDouble(premInclServiceTaxFrAllBorwOvr5Yrs)));
//
//					}


                } else if (loanType.getSelectedItem().toString().equals("Personal Loan")) {

                    i.putExtra("op", "Total First Year Premium for Primary Borrower Inclusive of Applicable Taxes is Rs." + currencyFormat.format(Double.parseDouble(totFrstYrPremFrPrimaryBorwInclST)));
                    i.putExtra("op1", "Initial Sum Assured for Primary Borrower is Rs." + currencyFormat.format(Double.parseDouble(intlSumAssuredFrPrimaryBorw)));
                    i.putExtra("op2", "Medical Requirements for Primary Borrower is " + medReqFrPrimaryBorw);

                    if (isCoBorrower.getSelectedItem().toString().equals("2 Co-Borrowers")) {
                        i.putExtra("op3", "Total First Year Premium For Second Borrower Inclusive of Applicable Taxes is Rs." + currencyFormat.format(Double.parseDouble(totFrstYrPremFrSecondBorwInclST)));
                        i.putExtra("op4", "Initial Sum Assured for Second Borrower is Rs." + currencyFormat.format(Double.parseDouble(intlSumAssuredFrSecondBorw)));
                        i.putExtra("op5", "Medical Requirements for Second Borrower is " + medReqFrSecondBorw);
                    } else if (isCoBorrower.getSelectedItem().toString().equals("3 Co-Borrowers")) {
                        i.putExtra("op3", "Total First Year Premium For Second Borrower Inclusive of Applicable Taxes is Rs." + currencyFormat.format(Double.parseDouble(totFrstYrPremFrSecondBorwInclST)));
                        i.putExtra("op4", "Initial Sum Assured for Second Borrower is Rs." + currencyFormat.format(Double.parseDouble(intlSumAssuredFrSecondBorw)));
                        i.putExtra("op5", "Medical Requirements for Second Borrower is " + medReqFrSecondBorw);
                        i.putExtra("op6", "Total First Year Premium For Third Borrower Inclusive of Applicable Taxes is Rs." + currencyFormat.format(Double.parseDouble(totFrstYrPremFrThirdBorwInclST)));
                        i.putExtra("op7", "Initial Sum Assured for Third Borrower is Rs." + currencyFormat.format(Double.parseDouble(intlSumAssuredFrThirdBorw)));
                        i.putExtra("op8", "Medical Requirements for Third Borrower is " + medReqFrThirdBorw);

                    }

                    i.putExtra("op9", "Premium Inclusive of Applicable Taxes Payable for All Borrowers is Rs." + currencyFormat.format(Double.parseDouble(premInclServiceTaxFrAllBorw)));


                } else if (loanType.getSelectedItem().toString().equals("Vehicle Loan")) {


                    i.putExtra("op", "Total First Year Premium for Primary Borrower Inclusive of Applicable Taxes is Rs." + currencyFormat.format(Double.parseDouble(totFrstYrPremFrPrimaryBorwInclST)));
                    i.putExtra("op1", "Initial Sum Assured for Primary Borrower is Rs." + currencyFormat.format(Double.parseDouble(intlSumAssuredFrPrimaryBorw)));
                    i.putExtra("op2", "Medical Requirements for Primary Borrower is " + medReqFrPrimaryBorw);

                    if (isCoBorrower.getSelectedItem().toString().equals("2 Co-Borrowers")) {
                        i.putExtra("op3", "Total First Year Premium For Second Borrower Inclusive of Applicable Taxes is Rs." + currencyFormat.format(Double.parseDouble(totFrstYrPremFrSecondBorwInclST)));
                        i.putExtra("op4", "Initial Sum Assured for Second Borrower is Rs." + currencyFormat.format(Double.parseDouble(intlSumAssuredFrSecondBorw)));
                        i.putExtra("op5", "Medical Requirements for Second Borrower is " + medReqFrSecondBorw);
                    }


                    i.putExtra("op9", "Premium Inclusive of Applicable Taxes Payable for All Borrowers is Rs." + currencyFormat.format(Double.parseDouble(premInclServiceTaxFrAllBorw)));

                } else //Education Loan
                {

                    i.putExtra("op", "Total First Year Premium for Primary Borrower Inclusive of Applicable Taxes is Rs." + currencyFormat.format(Double.parseDouble(totFrstYrPremFrPrimaryBorwInclST)));
                    i.putExtra("op1", "Initial Sum Assured for Primary Borrower is Rs." + currencyFormat.format(Double.parseDouble(intlSumAssuredFrPrimaryBorw)));
                    i.putExtra("op2", "Medical Requirement is " + medReqFrPrimaryBorw);
                    i.putExtra("op9", "Premium Inclusive of Applicable Taxes Payable is Rs." + currencyFormat.format(Double.parseDouble(premInclServiceTaxFrAllBorw)));

                }

                i.putExtra("ProductDescName", "SBI Life - RiNn Raksha");

                mContext.startActivity(i);


            } else {
                Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            }
        }


    }

    /**********************************************************   Server Hits starts from here *******************/


    class AsyncServerHits extends AsyncTask<String, Void, String> {
        final Context mContext;
        ProgressDialog progressDialog;


        AsyncServerHits(Context mContext, ProgressDialog progressDialog) {
            this.mContext = mContext;
            this.progressDialog = progressDialog;
        }

        @Override
        protected String doInBackground(String... param) {


            if (commonMethods.isNetworkConnected(mContext)) {
                try {

                    StringBuilder serviceInput = new StringBuilder();

                    serviceInput.append("");

                    switch (isCoBorrowerStr) {
                        case "Only Primary Borrower":

                            serviceInput.append("DOB_Borrower1=").append(DOB_Borrower1Str);
                            serviceInput.append(",DOB_Borrower2=" + "");
                            serviceInput.append(",DOB_Borrower3=" + "");
                            break;
                        case "2 Co-Borrowers":

                            serviceInput.append("DOB_Borrower1=").append(DOB_Borrower1Str);
                            serviceInput.append(",DOB_Borrower2=").append(DOB_Borrower2Str);
                            serviceInput.append(",DOB_Borrower3=" + "");

                            break;
                        case "3 Co-Borrowers":
                            serviceInput.append("DOB_Borrower1=").append(DOB_Borrower1Str);
                            serviceInput.append(",DOB_Borrower2=").append(DOB_Borrower2Str);
                            serviceInput.append(",DOB_Borrower3=").append(DOB_Borrower3Str);

                            break;
                    }

                    serviceInput.append(",membershipDate=").append(membershipDateStr);

                    serviceInput.append(",coBorrowerOption=").append(isCoBorrowerStr);

                    serviceInput.append(",moratoPeriod=").append(moratoPeriodStr);

                    serviceInput.append(",loanAmount=").append(loanAmountStr);

                    serviceInput.append(",loanTerm=").append(loanTermStr);

                    serviceInput.append(",loanType=").append(loanTypeStr);


                    if (loanTypeStr.equals("Personal Loan")) {
                        serviceInput.append(",loanSubCategory=").append(loanSubCategoryStr);
                    } else {
                        serviceInput.append(",loanSubCategory=" + "");
                    }

                    serviceInput.append(",interestRateRange=").append(interestRateRangeStr);


                    serviceInput.append(",isMoratoChecked=").append(isMoratoCheckedStr);


                    serviceInput.append(",premPaidBy=").append(premPaidByStr);


                    if (loanTypeStr.equals("Education Loan")) {
                        if (isIntPayDuringMoratoStr.equals("Yes")) {

                            serviceInput.append(",isIntPayDuringMorato=" + "Yes");
                        } else if (isIntPayDuringMoratoStr.equals("No")) {

                            serviceInput.append(",isIntPayDuringMorato=" + "No");

                        }
                    } else {
                        if ((isMoratoCheckedStr.equals("Yes")) && (isIntPayDuringMoratoStr.equals("Yes"))) {
                            serviceInput.append(",isIntPayDuringMorato=" + "Yes");


                        } else if ((isMoratoCheckedStr.equals("Yes")) && (isIntPayDuringMoratoStr.equals("No"))) {
                            serviceInput.append(",isIntPayDuringMorato=" + "No");
                        }

                    }


                    try {
                        serviceInput.append(",CIR=").append(CIRStr);
                    } catch (Exception e) {
                        serviceInput.append(",CIR=" + "0");
                    }


                    serviceInput.append(",isForStaff=").append(isForStaffStr);


                    serviceInput.append(",isJKResident=").append(isJKResidentStr);


                    serviceInput.append(",option").append(optionStr);

                    serviceInput.append(",isCoBorrower").append(isCoBorrowerStr);

                    if (optionStr.equals("2")) {
                        if (isCoBorrowerStr.equals("2 Co-Borrowers")) {
                            serviceInput.append(",LoanShareBorrower1").append(LoanShareBorrower1Str);
                            serviceInput.append(",LoanShareBorrower2").append(LoanShareBorrower2Str);
                            serviceInput.append(",LoanShareBorrower3" + "0");
                        } else if (isCoBorrowerStr.equals("3 Co-Borrowers")) {
                            serviceInput.append(",LoanShareBorrower1").append(LoanShareBorrower1Str);
                            serviceInput.append(",LoanShareBorrower2").append(LoanShareBorrower2Str);
                            serviceInput.append(",LoanShareBorrower3").append(LoanShareBorrower3Str);
                        }
                    }


                    //					System.out.println("serviceInput" + serviceInput.toString());

                    String METHOD_NAME_SERV = "saveEasyAccessServiceHits";
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SERV);


                    request.addProperty("serviceName", METHOD_NAME_CALC);
                    request.addProperty("services_type", "PremiumCalculator");
                    request.addProperty("serviceInput", serviceInput.toString());
                    request.addProperty("serviceReqUserId", "");

                    AppSharedPreferences sharedPreferences = new AppSharedPreferences();
                    request.addProperty("imeiNo", AppSharedPreferences.getData(mContext, sharedPreferences.getImeiNo(), null));
                    request.addProperty("strModel", AppSharedPreferences.getData(mContext, sharedPreferences.getPhoneModelNo(), null));

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    //Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                    commonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    String SOAP_ACTION_SERV = "http://tempuri.org/saveEasyAccessServiceHits";
                    androidHttpTransport.call(SOAP_ACTION_SERV, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    String result = response.toString();
//					System.out.println("result " + result.toString());

                    if (result.contains("<ErrCode>0</ErrCode>")) {

                        return "Success";
                    } else
                        return "Server not Found. Please try after some time.";

//					return "Success";

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }
            } else
                return "Please Activate Internet connection";
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            } catch (Exception e) {
                e.getMessage();
            }

            if (result.equals("Success")) {
                progressDialog = ProgressDialog.show(mContext, "Calculating", "Please Wait");
                AsyncRinnRaksha service = new AsyncRinnRaksha(mContext, progressDialog);
                service.execute();
            } else {
                Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void getDateDiff(Date Date1, Date Date2) {
        // in milliseconds
        long diff = Date1.getTime() - Date2.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        diffDays = diff / (24 * 60 * 60 * 1000);

        // diffDays = diffDays + 1;
        String calcuatedDays = String.valueOf(diffDays);

    }

    public com.wdullaer.materialdatetimepicker.date.DatePickerDialog createDateDialog(String current_date, Context mcontext, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener mDateSetListener) {
        int year, month, day;
        Calendar c = Calendar.getInstance();
        if (current_date.equals("")) {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            String[] array = current_date.split("/");
            day = Integer.parseInt(array[1]);
            month = Integer.parseInt(array[0]) - 1;
            year = Integer.parseInt(array[2]);
        }

        com.wdullaer.materialdatetimepicker.date.DatePickerDialog datepickerdialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(mDateSetListener, year, month, day);
        datepickerdialog.setThemeDark(true); //set dark them for dialog?
        datepickerdialog.vibrate(true); //vibrate on choosing date?
        datepickerdialog.dismissOnPause(true); //dismiss dialog when onPause() called?
        datepickerdialog.showYearPickerFirst(true); //choose year first?
//		datepickerdialog.setAccentColor(Color.parseColor("#0071e2")); // custom accent color
        datepickerdialog.setAccentColor(Color.parseColor("#0071e2"));
        datepickerdialog.setTitle("Please select a date"); //dialog title
//		datepickerdialog.show(, "Datepickerdialog"); //show dialog
        return datepickerdialog;
    }
}
