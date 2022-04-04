package sbilife.com.pointofsale_bancaagency.needanalysis;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import sbilife.com.pointofsale_bancaagency.CaptureSignature;
import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ProposerCaptureSignature;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.annuityplus.BI_AnnuityPlusActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.flexismart.FlexiSmartActivity;
import sbilife.com.pointofsale_bancaagency.flexismartplus.BI_FlexiSmartPlusActivity;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.NeedAnalysisDashboardActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.NewBusinessHomeGroupingActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.ProductBIBean;
import sbilife.com.pointofsale_bancaagency.poornsuraksha.BI_PoornSurakshaActivity;
import sbilife.com.pointofsale_bancaagency.products.eshieldNext.BI_EShieldNext;
import sbilife.com.pointofsale_bancaagency.products.new_smart_samriddhi.BI_NewSmartSamriddhiActivity;
import sbilife.com.pointofsale_bancaagency.products.saralinsurewealthplus.BI_SaralInsureWealthPlusActivity;
import sbilife.com.pointofsale_bancaagency.products.saraljeevanbima.BI_SaralJeevanBimaActivity;
import sbilife.com.pointofsale_bancaagency.products.saralpensionnew.BI_SaralPensionNewActivity;
import sbilife.com.pointofsale_bancaagency.products.smart_samriddhi.BI_SmartSamriddhiActivity;
import sbilife.com.pointofsale_bancaagency.products.smartfuturechoice.BI_SmartFutureChoicesActivity;
import sbilife.com.pointofsale_bancaagency.products.smartinsurewealthplus.BI_SmartInsureWealthPlusActivity;
import sbilife.com.pointofsale_bancaagency.products.smartplatinaassure.BI_SmartPlatinaAssureActivity;
import sbilife.com.pointofsale_bancaagency.products.smartplatinaplus.BI_SmartPlatinaPlusActivity;
import sbilife.com.pointofsale_bancaagency.retiresmart.BI_RetireSmartActivity;
import sbilife.com.pointofsale_bancaagency.sampoorncancersuraksha.BI_SampoornCancerSurakshaActivity;
import sbilife.com.pointofsale_bancaagency.saralmahaanand.BI_SaralMahaAnandActivity;
import sbilife.com.pointofsale_bancaagency.saralpension.BI_SaralPensionActivity;
import sbilife.com.pointofsale_bancaagency.saralshield.BI_SaralShieldActivity;
import sbilife.com.pointofsale_bancaagency.saralswadhan.BI_SaralSwadhanPlusActivity;
import sbilife.com.pointofsale_bancaagency.shubhnivesh.BI_ShubhNiveshActivity;
import sbilife.com.pointofsale_bancaagency.smartbachat.BI_SmartBachatActivity;
import sbilife.com.pointofsale_bancaagency.smartchamp.BI_SmartChampActivity;
import sbilife.com.pointofsale_bancaagency.smartelite.BI_SmartEliteActivity;
import sbilife.com.pointofsale_bancaagency.smartguaranteedsavings.BI_SmartGuaranteedSavingsPlanActivity;
import sbilife.com.pointofsale_bancaagency.smarthumsafar.BI_SmartHumsafarActivity;
import sbilife.com.pointofsale_bancaagency.smartincomeprotect.BI_SmartIncomeProtectActivity;
import sbilife.com.pointofsale_bancaagency.smartmoneybackgold.BI_SmartMoneyBackGoldActivity;
import sbilife.com.pointofsale_bancaagency.smartmoneyplanner.BI_SmartMoneyPlannerActivity;
import sbilife.com.pointofsale_bancaagency.smartpower.BI_SmartPowerInsuranceActivity;
import sbilife.com.pointofsale_bancaagency.smartprivilege.BI_SmartPrivilegeActivity;
import sbilife.com.pointofsale_bancaagency.smartscholar.BI_SmartScholarActivity;
import sbilife.com.pointofsale_bancaagency.smartshield.BI_SmartShieldActivity;
import sbilife.com.pointofsale_bancaagency.smartswadhanplus.BI_SmartSwadhanPlusActivity;
import sbilife.com.pointofsale_bancaagency.smartwealthassure.BI_SmartWealthAssureActivity;
import sbilife.com.pointofsale_bancaagency.smartwealthbuilder.BI_SmartWealthBuilderActivity;
import sbilife.com.pointofsale_bancaagency.smartwomenadvantage.BI_SmartWomenAdvantageActivity;

@SuppressWarnings({"deprecation"})
public class NeedAnalysisActivity extends AppCompatActivity implements
        OnItemClickListener, OnEditorActionListener {
    private ArrayList<SuggestedProdList> suggested_prod_list;
    private ArrayList<SuggestedProdList> remaining_product_list;
    private ArrayList<SuggestedProdList> protection_prod_list;
    private ArrayList<SuggestedProdList> retirement_prod_list;
    private ArrayList<SuggestedProdList> wealth_prod_list;
    private ArrayList<SuggestedProdList> chosen_prod_list;
    private ArrayList<SuggestedProdList> ulip_prod_list;
    private ArrayList<SuggestedProdList> traditional_prod_list;
    private ArrayList<SuggestedProdList> other_chosen_prod_list;
    private ArrayList<SuggestedProdList> sugg_chosen_prod_list;
    private String Child_1_edu_CurCorpus, Child_1_mar_CurCorpus,
            Child_2_edu_CurCorpus, Child_2_mar_CurCorpus,
            Child_3_edu_CurCorpus, Child_3_mar_CurCorpus,
            Child_4_edu_CurCorpus, Child_4_mar_CurCorpus;
    private String Gender = "";
    private final String SOAP_ACTION_NA_CBI_UIN_NO = "http://tempuri.org/getUINNum";
    private final String METHOD_NAME_NA_CBI_UIN_NO = "getUINNum";

    private final String SOAP_ACTION_NA_CBI_UIN_NO_OTHER_BANK = "http://tempuri.org/getURNBank";
    private final String METHOD_NAME_NA_CBI_UIN_NO_OTHER_BANK = "getURNBank";

    public static String URN_NO = "";
    private ProgressDialog progressDialog;

    // private String remaining_product_selected = null;
    private ArrayList<SuggestedProdList> child_prod_list;
    private EditText edt_email_id;// , edt_cif_code;
    private boolean validationFla1 = false;
    private String EmailAddress = "";
    // private String CIFCode = "";
    private Dialog dialogOnactivityResult;

    // private int remaining_product_selected_index = -1;
    private ImageButton Ibtn_signatureofIntermediary;
    private ImageButton Ibtn_signatureofCustomer;
    private String latestImage = "";
    private Button btn_Date;
    private final int SIGNATURE_ACTIVITY = 1;
    private String agent_sign = "";
    private String proposer_sign = "";

    private String formattedDate = "";
    /* For Async Task */
    private ProgressDialog mProgressDialog;
    private AsyncDownload asyncdownload;
    /* My Details String */
    private Switch switch_gender, switch_marital_status;
    private SeekBar sb_monthly_income, sb_monthly_income_with_emi,
            sb_current_life_insurance_coverage, sb_outstanding_home_loan,
            sb_outstanding_home_loan_other;
    private EditText edt_monthly_income, edt_yearly_income,
            edt_monthly_income_with_emi, edt_yearly_income_with_emi,
            edt_current_life_insurance_coverage, edt_outstanding_home_loan,
            edt_outstanding_home_loan_other;
    private Button btn_date_of_birth;
    private ImageView iv_women, iv_men;
    private ImageView iv_single, iv_married;
    private Spinner spnr_children, spnr_inflation_assumed,
            spnr_invesment_approch, spnr_group;
    private TextView tv_age, txt_product_chosen_1, txt_product_chosen_2,
            txt_product_chosen_3, txt_product_chosen_4, txt_product_chosen_5,
            txt_product_chosen_6;
    /* For DatePicker */
    private ListView lv_suggested_prod, lv_remaining_prod;
    private int mchildDetail = 0;
    // private final int DATE_DIALOG = 1;
    private final int DATE_DIALOG_ID = 1;
    private int DIALOG_ID;
    private int mYear;
    private int mMonth;
    private int mDay;

    private DatabaseHelper dbHelper;
    /* String for My Details */
    private String str_gender = "M";
    private String str_date_of_birth = "";
    private String str_age = "";
    private String str_marital_status = "Single";
    private String str_group = "";
    private String str_no_of_child = "";
    private String str_inflation_assumed = "";
    private String str_investment_approach = "";
    private String strMonthlyIncome = "";
    private String strYearlyIncome = "";
    private String strMonthlyIncome_emi = "0";
    private String stryearlyIncome_emi = "";
    private String strCurrentLifeInsuranceCoverage = "0";
    private String strOutstandingHomeLoan = "0";
    private String strOutstandingHomeLoanother = "0";

    /* String for My Goals */

    private String strRetirementCurrentCorpus = "0";
    private String strRetirementNoOfRealise = "0";
    private String strRetirementLifeStyle = "";
    private String strChild1Name = "";
    private String strChild1Age = "0";
    private String strChild1AgeAtEducation = "0";
    private String strChild1CorpusEducation = "0";
    private String strChild1CurrentCostEducation = "0";
    private String strChild1AgeAtMarriage = "0";
    private String strChild1CorpusMarriage = "0";
    private String strChild1CurrentCostMarriage = "0";

    private String strChild2Name = "";
    private String strChild2Age = "0";
    private String strChild2AgeAtEducation = "0";
    private String strChild2CorpusEducation = "0";
    private String strChild2CurrentCostEducation = "0";
    private String strChild2AgeAtMarriage = "0";
    private String strChild2CorpusMarriage = "0";
    private String strChild2CurrentCostMarriage = "0";

    private String strChild3Name = "";
    private String strChild3Age = "0";
    private String strChild3AgeAtEducation = "0";
    private String strChild3CorpusEducation = "0";
    private String strChild3CurrentCostEducation = "0";
    private String strChild3AgeAtMarriage = "";
    private String strChild3CorpusMarriage = "0";
    private String strChild3CurrentCostMarriage = "0";

    private String strChild4Name = "";
    private String strChild4Age = "0";
    private String strChild4AgeAtEducation = "0";
    private String strChild4CorpusEducation = "0";
    private String strChild4CurrentCostEducation = "0";
    private String strChild4AgeAtMarriage = "0";
    private String strChild4CorpusMarriage = "0";
    private String strChild4CurrentCostMarriage = "0";

    private String strNoOfYearBuyHome = "0";
    private String strCorpusForhome = "0";
    private String strCurrentCosthome = "0";
    private String strNoOfYearOthergoal = "0";
    private String strCorpusForOther = "0";
    private String strCurrentCostOther = "0";

    private List<String> group_list = new ArrayList<>();
    private List<String> children_list = new ArrayList<>();
    private List<String> inflation_list = new ArrayList<>();
    private List<String> investment_approch_list = new ArrayList<>();
    // List<String> remaining_product_list = new ArrayList<String>();
    /* For My Goals String */
    private int int_retirement = 60;
    private int int_child1Age = 0;
    private int int_child1_education_fund = 18;
    private int int_child1_marriage_fund = 25;

    private int int_child2Age = 0;
    private int int_child2_education_fund = 18;
    private int int_child2_marriage_fund = 25;

    private int int_child3Age = 0;
    private int int_child3_education_fund = 0;
    private int int_child3_marriage_fund = 0;

    private int int_child4Age = 0;
    private int int_child4_education_fund = 0;
    private int int_child4_marriage_fund = 0;

    private int int_child5Age = 0;
    private int int_child5_education_fund = 0;
    private int int_child5_marriage_fund = 0;

    private int int_wealthCreation_home = 0;
    private int int_wealthCreation_other = 1;
    private RadioGroup rg_post_reitrement;

    private SeekBar sb_retirement, sb_child1_education_corpus,
            sb_child1_education_current_cost, sb_child1_marriage_corpus,
            sb_child1_marrige_current_cost,

    sb_child2_education_corpus, sb_child2_education_current_cost,
            sb_child2_marriage_corpus, sb_child2_marrige_current_cost,

    sb_child3_education_corpus, sb_child3_education_current_cost,
            sb_child3_marriage_corpus, sb_child3_marrige_current_cost,

    sb_child4_education_corpus, sb_child4_education_current_cost,
            sb_child4_marriage_corpus, sb_child4_marrige_current_cost,

    sb_child5_education_corpus, sb_child5_education_current_cost,
            sb_child5_marriage_corpus,

    sb_wealth_creation_corpus_for_home,
            sb_wealth_creation_corpus_for_other;// sb_child5_marrige_current_cost
    private EditText edt_retirement, edt_child1_name,
            edt_child1_education_corpus, edt_child1_education_current_cost,
            edt_child1_marriage_corpus, edt_child1_marriage_current_cost,

    edt_child2_name, edt_child2_education_corpus,
            edt_child2_education_current_cost, edt_child2_marriage_corpus,
            edt_child2_marriage_current_cost,

    edt_child3_name, edt_child3_education_corpus,
            edt_child3_education_current_cost, edt_child3_marriage_corpus,
            edt_child3_marriage_current_cost,

    edt_child4_name, edt_child4_education_corpus,
            edt_child4_education_current_cost, edt_child4_marriage_corpus,
            edt_child4_marriage_current_cost,

    edt_child5_education_corpus, edt_child5_education_current_cost,
            edt_child5_marriage_corpus,

    edt_wealth_creation_corpus_for_home,
            edt_wealth_creation_current_cost_for_home,
            edt_wealth_creation_corpus_for_other,
            edt_wealth_creation_current_cost_for_other;// /edt_child5_name,edt_child5_marriage_current_cost,
    private Button btn_retirement_minus, btn_retirement_plus,
            btn_child1_age_minus, btn_child1_age_plus,
            btn_child1_education_fund_minus, btn_child1_education_fund_plus,
            btn_child1_marriage_fund_minus, btn_child1_marriage_fund_plus,

    btn_child2_age_minus, btn_child2_age_plus,
            btn_child2_education_fund_minus, btn_child2_education_fund_plus,
            btn_child2_marriage_fund_minus, btn_child2_marriage_fund_plus,

    btn_child3_age_minus, btn_child3_age_plus,
            btn_child3_education_fund_minus, btn_child3_education_fund_plus,
            btn_child3_marriage_fund_minus, btn_child3_marriage_fund_plus,

    btn_child4_age_minus, btn_child4_age_plus,
            btn_child4_education_fund_minus, btn_child4_education_fund_plus,
            btn_child4_marriage_fund_minus, btn_child4_marriage_fund_plus,

    btn_child5_age_minus, btn_child5_age_plus,
            btn_child5_education_fund_minus, btn_child5_education_fund_plus,
            btn_child5_marriage_fund_minus, btn_child5_marriage_fund_plus,

    btn_wealth_creation_home_minus, btn_wealth_creation_home_plus,
            btn_wealth_creation_other_minus, btn_wealth_creation_other_plus,
            btn_child1, btn_child2, btn_child3, btn_child4, btn_child5;
    // btn_child_header;
    private LinearLayout ll_product_chosen, ll_product_chosen_1,
            ll_product_chosen_2, ll_product_chosen_3, ll_product_chosen_4,
            ll_product_chosen_5, ll_product_chosen_6;// ll_child_header,

    // TextView tv_retirment_no_of_years, tv_child1_age,
    // tv_child1_education_fund,
    // tv_child1_marriage_fund, tv_child2_age, tv_child2_education_fund,
    // tv_child2_marriage_fund, tv_child3_age, tv_child3_education_fund,
    // tv_child3_marriage_fund, tv_child4_age, tv_child4_education_fund,
    // tv_child4_marriage_fund, tv_child5_age, tv_child5_education_fund,
    // tv_child5_marriage_fund,
    // tv_wealth_creation_home, tv_wealth_creation_other;

    private EditText edt_retirment_no_of_years, edt_child1_age,
            edt_child1_education_fund, edt_child1_marriage_fund,
            edt_child2_age, edt_child2_education_fund,
            edt_child2_marriage_fund, edt_child3_age,
            edt_child3_education_fund, edt_child3_marriage_fund,
            edt_child4_age, edt_child4_education_fund,
            edt_child4_marriage_fund, edt_child5_age,
            edt_child5_education_fund, edt_child5_marriage_fund,
            edt_wealth_creation_home, edt_wealth_creation_other;

    private LinearLayout ll_child1_details, ll_child2_details,
            ll_child3_details, ll_child4_details, ll_child5_details,
            ll_child_tab;

    private Button btn_my_details;
    private Button btn_my_goals;
    private Button btn_my_solution;
    private LinearLayout ll_my_details;
    private LinearLayout ll_my_goals;
    private LinearLayout ll_solution, ll_suggestdProdList;
    /* Declaraion for Solutions */
    private LinearLayout ln_chart_protection_top, ln_chart_protection_buttom,
            ln_chart_retirement_top, ln_chart_retirement_buttom;
    private LinearLayout ln_chart_child_top, ln_chart_child_buttom,
            ln_chart_wealth_top, ln_chart_wealth_buttom;

    private TextView txt_protection_gap, txt_protection_target_amt,
            txt_protection_corpus, txt_retirement_gap;
    private TextView txt_retirement_target_amt, txt_retirement_corpus,
            txt_child_future_gap, txt_child_future_target;
    private TextView txt_child_future_corpus;
    private TextView txt_wealth_creation_gap;
    private TextView txt_wealth_creation_target;
    private TextView txt_wealth_creation_corpus;

    private String outputlist = "";
    private String strErrorCOde = "";

    private Context context = this;
    private TextView txt_protection, txt_retirement, txt_child_future,
            txt_wealth_creation;
    LinearLayout lnchart_protection, lnchart_retirement, lnchart_child_future,
            lnchart_wealth_creation;

    private LinearLayout ll_protection, ll_retirement, ll_child,
            ll_wealth_creation, ll_summary;
    private Button iv_wealthCreation, iv_child, iv_retirement, iv_protection;
    private LinearLayout ln_trad_ulip, ln_ulip_trad;
    // private ImageView img_wealth_top_side;
    private TextView txt_wealth_smart_income_protect,
            txt_wealth_smart_guran_savings, txt_wealth_smart_money_back,
            txt_wealth_shubh_nivesh;
    private TextView txt_wealth_saral_swadhan_plus,
            txt_wealth_flexi_smart_plus, txt_wealth_money_planner,
            txt_humsafar, txt_wealth_money_planner_, txt_humsafar_,
            txt_wealth_smart_swadhan_plus, txt_wealth_smart_swadhan_plus_,
            txt_poorn_suraksha, txt_poorn_suraksha_;

    private TextView txt_wealth_saral_maha, txt_wealth_smart_elite,
            txt_wealth_smart_power_insu, txt_wealth_smart_scholar;
    private TextView txt_wealth_smart_wealth_builder;
    private TextView txt_wealth_smart_wealth_assure;

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;

    private final String SOAP_ACTION_NEED_ANALYSIS = "http://tempuri.org/getConnectLifeNeedProdRes";
    private final String METHOD_NAME_NEED_ANALYSIS = "getConnectLifeNeedProdRes";

    // private ScrollView scrollview;
    private EditText edt_focus;
    /* For Wealth Creation */
    private TextView txt_wealth_smart_income_protect_,
            txt_wealth_smart_guran_savings_, txt_wealth_smart_money_back_,
            txt_wealth_shubh_nivesh_;
    private TextView txt_wealth_saral_swadhan_plus_,
            txt_wealth_flexi_smart_plus_;

    private TextView txt_wealth_saral_maha_, txt_wealth_smart_elite_,
            txt_wealth_smart_power_insu_, txt_wealth_smart_scholar_;
    private TextView txt_wealth_smart_wealth_builder_,
            txt_wealth_smart_wealth_assure_;

    /* For Retirement planning */
    // private ImageView img_retirement_top_side;

    private TextView txt_ret_saral_pension, txt_ret_retire_smart,
            txt_ret_annuity_plus;
    /* For Protection */

    // private ImageView img_protection_top_side;

    private TextView txt_pro_smart_shield, txt_pro_saral_shield,
            txt_pro_eshield, txt_pro_grameen_bima;
    /* For Summary */
    private TextView txt_summary_protection;
    /*
     * private ImageView img_chart_protection_top_side,
     * img_chart_retirement_top_side, img_chart_child_top_side,
     * img_chart_wealth_top_side;
     */

    /* For Child */

    // child 1
    private LinearLayout ln_child_1;
    private TextView txt_child_1_tot_amt;// education target amt + marriage
    // target amt
    // education
    private TextView txt_child_1_edu_tar_amount, txt_child_1_edu_gap,
            txt_child_1_edu_corpus;
    private LinearLayout ln_chart_child_child_1_edu_top,
            ln_chart_child_child_1_edu_buttom;
    // marriage
    private TextView txt_child_1_marriage_tar_amt, txt_child_1_marriage_gap,
            txt_child_1_mrg_corpus;
    private LinearLayout ln_chart_child_child_1_mrg_top,
            ln_chart_child_child_1_mrg_buttom;

    // child 2
    private LinearLayout ln_child_2;
    private TextView txt_child_2_tot_amt;// education target amt + marriage
    // target amt
    // education
    private TextView txt_child_2_edu_tar_amount, txt_child_2_edu_gap,
            txt_child_2_edu_corpus;
    private LinearLayout ln_chart_child_child_2_edu_top,
            ln_chart_child_child_2_edu_buttom;
    // marriage
    private TextView txt_child_2_marriage_tar_amt, txt_child_2_marriage_gap,
            txt_child_2_mrg_corpus;
    private LinearLayout ln_chart_child_child_2_mrg_top,
            ln_chart_child_child_2_mrg_buttom;

    // child 3
    private LinearLayout ln_child_3;
    private TextView txt_child_3_tot_amt;// education target amt + marriage
    // target amt
    // education
    private TextView txt_child_3_edu_tar_amount, txt_child_3_edu_gap,
            txt_child_3_edu_corpus;
    private LinearLayout ln_chart_child_child_3_edu_top,
            ln_chart_child_child_3_edu_buttom;
    // marriage
    private TextView txt_child_3_marriage_tar_amt, txt_child_3_marriage_gap,
            txt_child_3_mrg_corpus;
    private LinearLayout ln_chart_child_child_3_mrg_top,
            ln_chart_child_child_3_mrg_buttom;

    // child 4
    private LinearLayout ln_child_4;
    private TextView txt_child_4_tot_amt;// education target amt + marriage
    // target amt
    // education
    private TextView txt_child_4_edu_tar_amount, txt_child_4_edu_gap,
            txt_child_4_edu_corpus;
    private LinearLayout ln_chart_child_child_4_edu_top,
            ln_chart_child_child_4_edu_buttom;
    // marriage
    private TextView txt_child_4_marriage_tar_amt, txt_child_4_marriage_gap,
            txt_child_4_mrg_corpus;
    private LinearLayout ln_chart_child_child_4_mrg_top,
            ln_chart_child_child_4_mrg_buttom;

    // private TextView txt_child_smart_champ, txt_child_scholar;
    private TextView txt_summary_retirement, txt_wealth_creation_summary;

    private TextView txt_pro_gap, txt_pro_target_amt, txt_pro_corpus;
    // private ImageView img_pro_top_side;
    private LinearLayout ln_chart_pro_top, ln_chart_pro_buttom;

    private TextView txt_retire_gap, txt_retire_target_amt, txt_retire_corpus;
    // private ImageView img_retire_top_side;
    private LinearLayout ln_chart_retire_top, ln_chart_retire_buttom;

    private TextView txt_wealthcreation_gap, txt_wealthcreation_target,
            txt_wealthcreation_corpus;
    // private ImageView img_wealthcreation_top_side;
    private LinearLayout ln_chart_wealthcreation_top, ln_wealthcreation_buttom;

    private double LifeProtCovCorpus_, LifeProtGap_, LifeProtTarget_,
            PensionTotReqCurCorpus_, PensionActAnnInvGap_, PensionActTarget_,
            ch_total_corpus, ch_total_gap, ch_total_target,
            Wealth_total_corpus, Wealth_total_gap, Wealth_total_target;

    /*
     * private ImageView img_chart_child_1_edu_top_side,
     * img_chart_child_1_mrg_top_side, img_chart_child_2_edu__top_side,
     * img_chart_child_2_mrg_top_side, img_chart_child_3_edu_top_side,
     * img_chart_child_3_mrg_top_side, img_chart_child_4_edu_top_side,
     * img_chart_child_4_mrg_top_side;
     */
    private View vw_wealth_top_side, vw_retire_top_side, vw_pro_top_side,
            vw_chart_protection_top_side, vw_chart_retirement_top_side,
            vw_chart_child_top_side, vw_chart_wealth_top_side;
    private TextView tv_mand_corpus_home, tv_mand_corpus_other,
            tv_mand_cost_other, tv_mand_cost_home;
    private ImageView iv_chid_side_arrow;
    private LinearLayout ll_btn_child_header;
    private String AgentCode = "";

    private String str_usertype = "";
    // private String ProductCategory = "";
    // private String PlanName = "";
    // private String ProductCode = "";
    private String strLicenseExpiryDate = "";
    private String strULIPCert = "";
    // newDBHelper db;

    private LinearLayout ll_wealth_builder;// , ll_wealth_builder_;

    private View ll_protection_graph, ll_wealth_graph, ll_retirement_graph,
            ll_children_graph;
    public static String str_need_analysis = "0";

    private LinearLayout ll_retirement_wealth;

    /* For Other */
    private TextView txt_wealth_creation_other, txt_wealthcreation_gap_other,
            txt_wealthcreation_corpus_other, txt_wealthcreation_target_other;
    private View vw_wealthcreation_top_side_other;
    private LinearLayout ln_chart_wealthcreation_top_other,
            ln_chart_wealthcreation_buttom_other;

    private View vw_child_1_edu_top_side, vw_child_1_mrg_top_side,
            vw_child_2_edu_top_side, vw_child_2_mrg_top_side,
            vw_child_3_edu_top_side, vw_child_3_mrg_top_side,
            vw_child_4_edu_top_side, vw_child_4_mrg_top_side;

    private LinearLayout ln_trad_ulip_child, ln_ulip_trad_child;
    private TextView txt_smart_champ_trad_ulip_child,
            txt_smart_scholar_trad_ulip_child;
    private TextView txt_smart_scholar_ulip_trad_child,
            txt_smart_champ_ulip_trad_child;
    // private Button btn_trad_ulip_child, btn_ulip_trad_child;
    private Button btn_need_output_focus;

    // /Global declaration

    private LinearLayout ln_trad_ulip_retirement, ln_ulip_trad_retirement;
    private TextView txt_saral_pension_trad_ulip_retirement,
            txt_annuity_plus_trad_ulip_retirement,
            txt_retire_smart_trad_ulip_retirement;
    private TextView txt_retire_smart_ulip_trad_retirement,
            txt_saral_pension_ulip_trad_retirement,
            txt_annuity_plus_ulip_trad_retirement;
    // private Button btn_trad_ulip_retirement, btn_ulip_trad_retirement;
    private TextView tv_child1_mrg, tv_child2_mrg, tv_child3_mrg,
            tv_child4_mrg, tv_child1_edu, tv_child2_edu, tv_child3_edu,
            tv_child4_edu;

    private TextView txt_child1_edu_text_line, txt_child2_edu_text_line,
            txt_child3_edu_text_line, txt_child4_edu_text_line,
            txt_child1_mrg_text_line, txt_child2_mrg_text_line,
            txt_child3_mrg_text_line, txt_child4_mrg_text_line,
            txt_wealth_home_text_line, txt_wealth_other_text_line,
            txt_retirement_text_line;

    private String LifeProtCovCorpus = "";
    private String LifeProtGap = "";
    private String PensionTotReqCurCorpus = "";
    private String PensionActAnnInvGap = "";
    private String CostHomeCorpus = "";
    private String CostHomeGap = "";
    private String LifeProtTillAge = "";
    private String strProd = "";// , strProdUINCode = "";
    private String Ret_Monthly_inv = "";
    private String Ret_Left_Year = "";
    private String Child_1_edu_Gap = "";
    private String Child_1_Edu_Monthly_Cost = "";
    private String Child_1_mar_Gap = "";
    private String Child_1_Mrg_Monthly_Cost = "";

    private String Child_2_edu_Gap = "";
    private String Child_2_Edu_Monthly_Cost = "";
    private String Child_2_mar_Gap = "";
    private String Child_2_Mrg_Monthly_Cost = "";

    private String Child_3_edu_Gap = "";
    private String Child_3_Edu_Monthly_Cost = "";
    private String Child_3_mar_Gap = "";
    private String Child_3_Mrg_Monthly_Cost = "";

    private String Child_4_edu_Gap = "";
    private String Child_4_Edu_Monthly_Cost = "";
    private String Child_4_mar_Gap = "";
    private String Child_4_Mrg_Monthly_Cost = "";

    private String Home_Monthly_inv = "";
    private String Other_Monthly_inv = "";

    private String CostOtherCorpus = "";
    private String CostOtherGap = "";
    private String Salutation = "";

    private String str_protection_cor_req = "";
    private String retirment_cor_req = "";

    // need analysis
    private String unique_no = "";
    private StringBuilder inputVal;

    private Map<String, String> map = new HashMap<>();

    private LinearLayout ll_seekbr_corpusSavedForToBuyHome,
            ll_editbox_corpusSavedForToBuyHome, ll_currentCostToBuyHome,
            ll_other_current_cost, ll_corpus_OtherGoal, ll_corpus_other;

    private TextView txt_star_def, txt_hash_def, tv_loading;
    private CommonMethods mCommonMethods;
    private StorageUtils mStorageUtils;

    private String str_brochure_dest_file_path = "", str_brochure_download_file_url = "";

    private SendMailTask sendMailTask;
    private UIN_NO_ServiceHit taskUinNoServiceHit;
    //private StateIdAsyncTask stateIdAsyncTask;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.layout_need_analysis);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_custom_title_need_analysis);

        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu1(this, "Need Analysis Calculator");
        mStorageUtils = new StorageUtils();

        Date();
        InitializeVariable();
        OnListener();
        FillSpinner();
        setId();

        btn_Date.setText(getCurrentDate());
        String str_sign_of_proposer = "";
        if (str_sign_of_proposer != null && !str_sign_of_proposer.equals("")) {
            byte[] signByteArray = Base64.decode(str_sign_of_proposer, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofCustomer.setImageBitmap(bitmap);
        }
        if (agent_sign != null && !agent_sign.equals("")) {
            // cb_statement.setChecked(true);
            byte[] signByteArray = Base64.decode(agent_sign, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofIntermediary.setImageBitmap(bitmap);
        }

        if (proposer_sign != null && !proposer_sign.equals("")) {
            byte[] signByteArray = Base64.decode(proposer_sign, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
                    signByteArray.length);
            Ibtn_signatureofCustomer.setImageBitmap(bitmap);
        }
        // Radio Button
        // RadioButton rb_retirement_basic = (RadioButton)
        // findViewById(R.id.rb_retirement_basic);
        RadioButton rb_retirement_comfortable = findViewById(R.id.rb_retirement_comfortable);
        // RadioButton rb_retirement_luxury = (RadioButton)
        // findViewById(R.id.rb_retirement_luxury);

        rb_retirement_comfortable.setChecked(true);
        str_inflation_assumed = "6%";

        dbHelper = new DatabaseHelper(context);
        spnr_inflation_assumed.setSelection(
                getIndex(spnr_inflation_assumed, str_inflation_assumed), false);
        // spnr_invesment_approch.setSelection(
        // getIndex(spnr_invesment_approch, str_investment_approach),
        // false);
        // spnr_children.setSelection(getIndex(spnr_children, str_no_of_child),
        // false);

        btn_my_details = findViewById(R.id.btn_my_details);
        btn_my_goals = findViewById(R.id.btn_my_goals);
        btn_my_goals.setEnabled(false);
        btn_my_goals.setClickable(false);
        btn_my_solution = findViewById(R.id.btn_my_solution);
        btn_need_output_focus = findViewById(R.id.btn_need_output_focus);
        btn_my_solution.setEnabled(false);
        btn_my_solution.setClickable(false);
        ll_my_details = findViewById(R.id.ll_my_details);
        ll_my_goals = findViewById(R.id.ll_my_goals);
        ll_solution = findViewById(R.id.ll_solution);
        ll_suggestdProdList = findViewById(R.id.ll_suggestdProdList);
        ll_seekbr_corpusSavedForToBuyHome = findViewById(R.id.ll_seekbr_corpusSavedForToBuyHome);
        ll_editbox_corpusSavedForToBuyHome = findViewById(R.id.ll_editbox_corpusSavedForToBuyHome);
        ll_currentCostToBuyHome = findViewById(R.id.ll_currentCostToBuyHome);
        ll_other_current_cost = findViewById(R.id.ll_other_current_cost);
        ll_corpus_OtherGoal = findViewById(R.id.ll_corpus_OtherGoal);
        ll_corpus_other = findViewById(R.id.ll_corpus_other);
        ll_protection_graph = findViewById(R.id.ll_protection_graph);
        ll_wealth_graph = findViewById(R.id.ll_wealth_graph);
        ll_children_graph = findViewById(R.id.ll_children_graph);
        ll_retirement_graph = findViewById(R.id.ll_retirement_graph);

        ll_my_details.setVisibility(View.VISIBLE);
        ll_my_goals.setVisibility(View.GONE);
        ll_solution.setVisibility(View.GONE);
        ll_suggestdProdList.setVisibility(View.GONE);
        Drawable drawable = getResources().getDrawable(
                R.drawable.my_details_blue);
        btn_my_details.setBackgroundDrawable(drawable);
        btn_my_details.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                // btn_my_details.setBackgroundDrawable(getResources()
                // .getDrawable(R.drawable.my_details_blue));
                // btn_my_goals.setBackgroundDrawable(getResources().getDrawable(
                // R.drawable.my_goals_white));
                //
                // btn_my_solution.setBackgroundDrawable(getResources()
                // .getDrawable(R.drawable.solution_white));
                ll_my_details.setVisibility(View.VISIBLE);
                ll_my_goals.setVisibility(View.GONE);
                ll_solution.setVisibility(View.GONE);
                ll_suggestdProdList.setVisibility(View.GONE);

                // edt_focus.requestFocus();
                btn_my_goals.requestFocus();
                setFocusable(btn_my_goals);
                hideKeyboard(NeedAnalysisActivity.this);

            }
        });
        btn_my_goals.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                // btn_my_details.setBackgroundDrawable(getResources()
                // .getDrawable(R.drawable.my_details_blue));
                //
                // btn_my_goals.setBackgroundDrawable(getResources().getDrawable(
                // R.drawable.my_goals_blue));
                //
                // btn_my_solution.setBackgroundDrawable(getResources()
                // .getDrawable(R.drawable.solution_white));
                ll_my_details.setVisibility(View.GONE);
                ll_retirement_wealth.setVisibility(View.VISIBLE);
                ll_solution.setVisibility(View.GONE);
                ll_suggestdProdList.setVisibility(View.GONE);
                ll_my_goals.setVisibility(View.VISIBLE);
                if (str_no_of_child != null) {
                    switch (str_no_of_child) {
                        case "0":
                            ll_child_tab.setVisibility(View.GONE);
                            ll_btn_child_header.setVisibility(View.GONE);
                            ll_child1_details.setVisibility(View.GONE);
                            ll_child2_details.setVisibility(View.GONE);
                            ll_child3_details.setVisibility(View.GONE);
                            ll_child4_details.setVisibility(View.GONE);
                            ll_child5_details.setVisibility(View.GONE);
                            break;
                        case "1":
                            ll_btn_child_header.setVisibility(View.VISIBLE);
                            ll_child1_details.setVisibility(View.GONE);
                            ll_child2_details.setVisibility(View.GONE);
                            ll_child3_details.setVisibility(View.GONE);
                            ll_child4_details.setVisibility(View.GONE);
                            ll_child_tab.setVisibility(View.GONE);
                            btn_child2.setVisibility(View.GONE);
                            btn_child3.setVisibility(View.GONE);
                            btn_child4.setVisibility(View.GONE);
                            btn_child5.setVisibility(View.GONE);

                            break;
                        case "2":
                            ll_btn_child_header.setVisibility(View.VISIBLE);
                            ll_child1_details.setVisibility(View.GONE);
                            ll_child2_details.setVisibility(View.GONE);
                            ll_child3_details.setVisibility(View.GONE);
                            ll_child4_details.setVisibility(View.GONE);
                            ll_child_tab.setVisibility(View.GONE);
                            btn_child3.setVisibility(View.GONE);
                            btn_child4.setVisibility(View.GONE);
                            btn_child5.setVisibility(View.GONE);

                            break;
                        case "3":
                            ll_btn_child_header.setVisibility(View.VISIBLE);
                            ll_child1_details.setVisibility(View.GONE);
                            ll_child2_details.setVisibility(View.GONE);
                            ll_child3_details.setVisibility(View.GONE);
                            ll_child4_details.setVisibility(View.GONE);
                            ll_child_tab.setVisibility(View.GONE);
                            btn_child4.setVisibility(View.GONE);
                            btn_child5.setVisibility(View.GONE);
                            break;
                        case "4":
                            ll_btn_child_header.setVisibility(View.VISIBLE);
                            ll_child1_details.setVisibility(View.GONE);
                            ll_child2_details.setVisibility(View.GONE);
                            ll_child3_details.setVisibility(View.GONE);
                            ll_child4_details.setVisibility(View.GONE);
                            ll_child_tab.setVisibility(View.GONE);
                            btn_child5.setVisibility(View.GONE);

                            break;
                        case "5":

                            break;
                    }
                }

                // edt_focus.requestFocus();
                btn_my_solution.requestFocus();
                setFocusable(btn_my_solution);
                hideKeyboard(NeedAnalysisActivity.this);

            }
        });
        btn_my_solution.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                // btn_my_details.setBackgroundDrawable(getResources()
                // .getDrawable(R.drawable.my_details_blue));
                // btn_my_goals.setBackgroundDrawable(getResources().getDrawable(
                // R.drawable.my_goals_blue));
                //
                // btn_my_solution.setBackgroundDrawable(getResources()
                // .getDrawable(R.drawable.solution_blue));
                ll_solution.setVisibility(View.VISIBLE);
                ll_my_details.setVisibility(View.GONE);
                ll_my_goals.setVisibility(View.GONE);
                ll_suggestdProdList.setVisibility(View.GONE);

                // edt_focus.requestFocus();
                btn_my_solution.requestFocus();
                setFocusable(btn_my_solution);
                hideKeyboard(NeedAnalysisActivity.this);

            }
        });

        iv_protection.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                edt_focus.setVisibility(View.GONE);
                ll_summary.setVisibility(View.GONE);
                ll_protection.setVisibility(View.VISIBLE);
                ll_retirement.setVisibility(View.GONE);
                ll_child.setVisibility(View.GONE);
                ll_wealth_creation.setVisibility(View.GONE);
                btn_my_details.setVisibility(View.GONE);
                btn_my_goals.setVisibility(View.GONE);
                btn_my_solution.setVisibility(View.GONE);

                /* Start of Protection */
                // protection
                if (str_investment_approach.equals("Conservative")) {
                    ln_trad_ulip.setVisibility(View.VISIBLE);
                    ln_ulip_trad.setVisibility(View.GONE);
                } else {
                    ln_trad_ulip.setVisibility(View.GONE);
                    ln_ulip_trad.setVisibility(View.VISIBLE);
                }

            }
        });

        iv_retirement.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                edt_focus.setVisibility(View.GONE);
                ll_summary.setVisibility(View.GONE);
                ll_protection.setVisibility(View.GONE);
                ll_retirement.setVisibility(View.VISIBLE);
                ll_child.setVisibility(View.GONE);
                ll_wealth_creation.setVisibility(View.GONE);
                btn_my_details.setVisibility(View.GONE);
                btn_my_goals.setVisibility(View.GONE);
                btn_my_solution.setVisibility(View.GONE);

                if (str_investment_approach.contentEquals("Conservative")) {
                    ln_trad_ulip_retirement.setVisibility(View.VISIBLE);
                    ln_ulip_trad_retirement.setVisibility(View.GONE);
                } else {
                    ln_trad_ulip_retirement.setVisibility(View.GONE);
                    ln_ulip_trad_retirement.setVisibility(View.VISIBLE);
                }
            }
        });
        iv_child.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                edt_focus.setVisibility(View.GONE);
                ll_summary.setVisibility(View.GONE);
                ll_protection.setVisibility(View.GONE);
                ll_retirement.setVisibility(View.GONE);
                ll_child.setVisibility(View.VISIBLE);
                ll_wealth_creation.setVisibility(View.GONE);
                btn_my_details.setVisibility(View.GONE);

                btn_my_goals.setVisibility(View.GONE);
                btn_my_solution.setVisibility(View.GONE);

                if (str_investment_approach.equalsIgnoreCase("Conservative")) {
                    ln_trad_ulip_child.setVisibility(View.VISIBLE);
                    ln_ulip_trad_child.setVisibility(View.GONE);
                } else {
                    ln_trad_ulip_child.setVisibility(View.GONE);
                    ln_ulip_trad_child.setVisibility(View.VISIBLE);
                }

                double child_count = Double.parseDouble(str_no_of_child);
                if (child_count == 0) {
                    ln_child_1.setVisibility(View.GONE);
                    ln_child_2.setVisibility(View.GONE);
                    ln_child_3.setVisibility(View.GONE);
                    ln_child_4.setVisibility(View.GONE);
                } else if (child_count == 1) {
                    ln_child_1.setVisibility(View.VISIBLE);
                    ln_child_2.setVisibility(View.GONE);
                    ln_child_3.setVisibility(View.GONE);
                    ln_child_4.setVisibility(View.GONE);
                } else if (child_count == 2) {
                    ln_child_1.setVisibility(View.VISIBLE);
                    ln_child_2.setVisibility(View.VISIBLE);
                    ln_child_3.setVisibility(View.GONE);
                    ln_child_4.setVisibility(View.GONE);
                } else if (child_count == 3) {
                    ln_child_1.setVisibility(View.VISIBLE);
                    ln_child_2.setVisibility(View.VISIBLE);
                    ln_child_3.setVisibility(View.VISIBLE);
                    ln_child_4.setVisibility(View.GONE);
                } else if (child_count == 4) {
                    ln_child_1.setVisibility(View.VISIBLE);
                    ln_child_2.setVisibility(View.VISIBLE);
                    ln_child_3.setVisibility(View.VISIBLE);
                    ln_child_4.setVisibility(View.VISIBLE);
                }
            }
        });
        iv_wealthCreation.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                edt_focus.setVisibility(View.GONE);
                ll_summary.setVisibility(View.GONE);
                ll_protection.setVisibility(View.GONE);
                ll_retirement.setVisibility(View.GONE);
                ll_child.setVisibility(View.GONE);
                ll_wealth_creation.setVisibility(View.VISIBLE);
                btn_my_details.setVisibility(View.GONE);

                btn_my_goals.setVisibility(View.GONE);
                btn_my_solution.setVisibility(View.GONE);

                // String str = "Conservative";
                if (str_investment_approach.equalsIgnoreCase("Conservative")) {
                    ln_trad_ulip.setVisibility(View.VISIBLE);
                    ln_ulip_trad.setVisibility(View.GONE);
                } else {
                    ln_trad_ulip.setVisibility(View.GONE);
                    ln_ulip_trad.setVisibility(View.VISIBLE);
                }
            }
        });

        setDefaultDate();
        sb_wealth_creation_corpus_for_other.setEnabled(false);
        sb_wealth_creation_corpus_for_other.setClickable(false);

        sb_wealth_creation_corpus_for_home.setEnabled(false);
        sb_wealth_creation_corpus_for_home.setClickable(false);

        Intent intent = getIntent();
        AgentCode = intent.getStringExtra("AgentCode");
        str_usertype = intent.getStringExtra("usertype");

        txt_wealth_smart_wealth_builder
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        SmartWealthBuilder();
                    }
                });

        txt_wealth_smart_wealth_builder_
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        SmartWealthBuilder();
                    }
                });

        txt_wealth_smart_elite.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                SmartElite();
            }
        });

        txt_wealth_smart_elite_.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                SmartElite();
            }
        });

        txt_wealth_flexi_smart_plus
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        FlexiSmartPlus();
                    }
                });

        txt_wealth_flexi_smart_plus_
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        FlexiSmartPlus();
                    }
                });

        txt_wealth_shubh_nivesh.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ShubhNivesh();
            }
        });

        txt_wealth_shubh_nivesh_.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShubhNivesh();
            }
        });

        txt_wealth_smart_guran_savings
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        SmartGuaranteed();
                    }
                });

        txt_wealth_smart_guran_savings_
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        SmartGuaranteed();
                    }
                });

        txt_wealth_smart_money_back
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        SmartMoneyBackGold();
                    }
                });

        txt_wealth_smart_money_back_
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        SmartMoneyBackGold();
                    }
                });

        txt_wealth_saral_swadhan_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        // SaralSwadhan();
                    }
                });

        txt_wealth_saral_swadhan_plus_
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        // SaralSwadhan();
                    }
                });

        txt_wealth_smart_swadhan_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        final Toast toast = Toast
                                .makeText(
                                        NeedAnalysisActivity.this,
                                        "Sorry this product is not available in Smart Advisor",
                                        Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 105, 50);
                        toast.show();
                    }
                });

        txt_wealth_smart_swadhan_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        final Toast toast = Toast
                                .makeText(
                                        NeedAnalysisActivity.this,
                                        "Sorry this product is not available in Smart Advisor",
                                        Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 105, 50);
                        toast.show();
                    }
                });

        txt_pro_smart_shield.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                SmartShield();
            }
        });

        txt_wealth_smart_scholar.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                SmartScholar();
            }
        });

        txt_wealth_smart_scholar_
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        SmartScholar();
                    }
                });

        txt_wealth_smart_power_insu
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        OnSmartPowerInsurance();
                    }
                });

        txt_wealth_smart_wealth_assure
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        OnSmartWealthAssure();
                    }
                });

        txt_wealth_smart_power_insu_
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        OnSmartPowerInsurance();
                    }
                });

        txt_wealth_saral_maha.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                OnSaralMahaAnand();
            }
        });

        txt_wealth_saral_maha_.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                OnSaralMahaAnand();
            }
        });

        txt_wealth_smart_income_protect
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        OnSmartIncomeProtect();
                    }
                });

        txt_wealth_smart_income_protect_
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        OnSmartIncomeProtect();
                    }
                });

        txt_wealth_money_planner.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                // OnSmartMoneyPlanner();
            }
        });

        txt_wealth_money_planner_
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        // OnSmartMoneyPlanner();
                    }
                });

        txt_humsafar.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                final Toast toast = Toast.makeText(NeedAnalysisActivity.this,
                        "Sorry this product is not available in Smart Advisor",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
            }
        });

        txt_humsafar_.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                final Toast toast = Toast.makeText(NeedAnalysisActivity.this,
                        "Sorry this product is not available in Smart Advisor",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
            }
        });

        txt_poorn_suraksha.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                final Toast toast = Toast.makeText(NeedAnalysisActivity.this,
                        "Sorry this product is not available in Smart Advisor",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
            }
        });

        txt_poorn_suraksha_.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                final Toast toast = Toast.makeText(NeedAnalysisActivity.this,
                        "Sorry this product is not available in Smart Advisor",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
            }
        });


        txt_pro_saral_shield.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                OnSaralShield();
            }
        });

        txt_pro_eshield.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                final Toast toast = Toast.makeText(NeedAnalysisActivity.this,
                        "Sorry this product is not available in Smart Advisor",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
            }
        });

        txt_pro_grameen_bima.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                final Toast toast = Toast.makeText(NeedAnalysisActivity.this,
                        "Sorry this product is not available in Smart Advisor",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
            }
        });

        txt_smart_champ_trad_ulip_child
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        SmartChamp();
                    }
                });

        txt_smart_scholar_trad_ulip_child
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        SmartScholar();
                    }
                });

        txt_smart_scholar_ulip_trad_child
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        SmartScholar();
                    }
                });

        txt_smart_champ_ulip_trad_child
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        SmartChamp();
                    }
                });

        txt_saral_pension_trad_ulip_retirement
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        OnSaralPension();
                    }
                });

        txt_annuity_plus_trad_ulip_retirement
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        final Toast toast = Toast
                                .makeText(
                                        NeedAnalysisActivity.this,
                                        "Sorry this product is not available in Smart Advisor",
                                        Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 105, 50);
                        toast.show();
                    }
                });

        txt_retire_smart_trad_ulip_retirement
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        RetireSmart();
                    }
                });

        txt_retire_smart_ulip_trad_retirement
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        RetireSmart();
                    }
                });

        txt_saral_pension_ulip_trad_retirement
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        OnSaralPension();
                    }
                });

        txt_annuity_plus_ulip_trad_retirement
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        final Toast toast = Toast
                                .makeText(
                                        NeedAnalysisActivity.this,
                                        "Sorry this product is not available in Smart Advisor",
                                        Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 105, 50);
                        toast.show();
                    }
                });

        edt_child1_name.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (edt_child1_name.isFocused()) {
                    if (s.length() > 0) {
                        btn_child1.setText(s);
                        btn_child1.setTextColor(getResources().getColor(
                                R.color.DarkBlue));
                        tv_child1_edu.setText(s + "'s Education");
                        tv_child1_mrg.setText(s + "'s Marriage");
                    } else {
                        btn_child1.setTextColor(getResources().getColor(
                                R.color.DarkSkyBlue));
                        tv_child1_edu.setText("Child 1 Education");
                        tv_child1_mrg.setText("Child 1 Marriage");
                        btn_child1.setText("Child 1");
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        edt_child2_name.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (edt_child2_name.isFocused()) {
                    if (s.length() > 0) {
                        btn_child2.setText(s);
                        btn_child2.setTextColor(getResources().getColor(
                                R.color.DarkBlue));
                        tv_child2_edu.setText(s + "'s Education");
                        tv_child2_mrg.setText(s + "'s Marriage");
                    } else {
                        btn_child2.setTextColor(getResources().getColor(
                                R.color.DarkSkyBlue));
                        tv_child2_edu.setText("Child 2 Education");
                        tv_child2_mrg.setText("Child 2 Marriage");
                        btn_child2.setText("Child 2");
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        edt_child3_name.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (edt_child3_name.isFocused()) {
                    if (s.length() > 0) {
                        btn_child3.setTextColor(getResources().getColor(
                                R.color.DarkBlue));
                        btn_child3.setText(s);

                        tv_child3_edu.setText(s + "'s Education");
                        tv_child3_mrg.setText(s + "'s Marriage");
                    } else {
                        btn_child3.setTextColor(getResources().getColor(
                                R.color.DarkSkyBlue));
                        btn_child3.setText("Child 3");

                        tv_child1_edu.setText("Child 3 Education");
                        tv_child1_mrg.setText("Child 3 Marriage");
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        edt_child4_name.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (edt_child4_name.isFocused()) {
                    if (s.length() > 0) {
                        btn_child4.setText(s);
                        btn_child4.setTextColor(getResources().getColor(
                                R.color.DarkBlue));
                        tv_child4_edu.setText(s + "'s Education");
                        tv_child4_mrg.setText(s + "'s Marriage");
                    } else {
                        btn_child4.setTextColor(getResources().getColor(
                                R.color.DarkSkyBlue));
                        tv_child4_edu.setText("Child 4 Education");
                        tv_child4_mrg.setText("Child 4 Marriage");
                        btn_child4.setText("Child 4");
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        // getChannelDetail();

        edt_monthly_income.setOnEditorActionListener(this);
        edt_retirement.setOnEditorActionListener(this);
        edt_child1_age.setOnEditorActionListener(this);
        edt_child1_education_fund.setOnEditorActionListener(this);
        edt_child1_marriage_fund.setOnEditorActionListener(this);
        edt_child1_education_corpus.setOnEditorActionListener(this);
        edt_child1_education_current_cost.setOnEditorActionListener(this);
        edt_child1_marriage_corpus.setOnEditorActionListener(this);
        edt_child1_marriage_current_cost.setOnEditorActionListener(this);

        edt_child2_age.setOnEditorActionListener(this);
        edt_child2_education_fund.setOnEditorActionListener(this);
        edt_child2_marriage_fund.setOnEditorActionListener(this);
        edt_child2_education_corpus.setOnEditorActionListener(this);
        edt_child2_education_current_cost.setOnEditorActionListener(this);
        edt_child2_marriage_corpus.setOnEditorActionListener(this);
        edt_child2_marriage_current_cost.setOnEditorActionListener(this);

        edt_child3_age.setOnEditorActionListener(this);
        edt_child3_education_fund.setOnEditorActionListener(this);
        edt_child3_marriage_fund.setOnEditorActionListener(this);
        edt_child3_education_corpus.setOnEditorActionListener(this);
        edt_child3_education_current_cost.setOnEditorActionListener(this);
        edt_child3_marriage_corpus.setOnEditorActionListener(this);
        edt_child3_marriage_current_cost.setOnEditorActionListener(this);

        edt_child4_age.setOnEditorActionListener(this);
        edt_child4_education_fund.setOnEditorActionListener(this);
        edt_child4_marriage_fund.setOnEditorActionListener(this);
        edt_child4_education_corpus.setOnEditorActionListener(this);
        edt_child4_education_current_cost.setOnEditorActionListener(this);
        edt_child4_marriage_corpus.setOnEditorActionListener(this);
        edt_child4_marriage_current_cost.setOnEditorActionListener(this);

        edt_wealth_creation_home.setOnEditorActionListener(this);
        edt_wealth_creation_corpus_for_home.setOnEditorActionListener(this);
        edt_wealth_creation_current_cost_for_home
                .setOnEditorActionListener(this);

        edt_wealth_creation_other.setOnEditorActionListener(this);
        edt_wealth_creation_corpus_for_other.setOnEditorActionListener(this);
        edt_wealth_creation_current_cost_for_other
                .setOnEditorActionListener(this);

        /*//Added by Tushar Kadam to get Kerala Resident
        String kerlaDiscountDetails = AppSharedPreferences.getData(context, (new CommonMethods().getKerlaDiscount()), "");
        if (TextUtils.isEmpty(kerlaDiscountDetails)) {
            stateIdAsyncTask = new StateIdAsyncTask(context, mCommonMethods.GetUserCode(context), mCommonMethods.GetUserType(context));
            stateIdAsyncTask.execute();
        }*/

    }

    public void onChildFuture(View v) {
        TextView tv_expand = findViewById(R.id.tv_expand);
        if (mchildDetail == 0) {

            tv_expand.setText("Click here to Collapse");
            ll_child_tab.setVisibility(View.VISIBLE);
            ll_retirement_wealth.setVisibility(View.GONE);
            switch (str_no_of_child) {
                case "0":
                    ll_child_tab.setVisibility(View.GONE);
                    ll_btn_child_header.setVisibility(View.GONE);
                    ll_child1_details.setVisibility(View.GONE);
                    ll_child2_details.setVisibility(View.GONE);
                    ll_child3_details.setVisibility(View.GONE);
                    ll_child4_details.setVisibility(View.GONE);
                    ll_child5_details.setVisibility(View.GONE);
                    break;
                case "1":
                    ll_child1_details.setVisibility(View.VISIBLE);
                    ll_child5_details.setVisibility(View.GONE);
                    btn_child1.setVisibility(View.VISIBLE);
                    btn_child2.setVisibility(View.GONE);
                    btn_child3.setVisibility(View.GONE);
                    btn_child4.setVisibility(View.GONE);
                    btn_child5.setVisibility(View.GONE);

                    break;
                case "2":
                    ll_child1_details.setVisibility(View.VISIBLE);
                    ll_child5_details.setVisibility(View.GONE);
                    btn_child1.setVisibility(View.VISIBLE);
                    btn_child2.setVisibility(View.VISIBLE);
                    btn_child3.setVisibility(View.GONE);
                    btn_child4.setVisibility(View.GONE);
                    btn_child5.setVisibility(View.GONE);

                    break;
                case "3":
                    ll_child1_details.setVisibility(View.VISIBLE);
                    ll_child5_details.setVisibility(View.GONE);
                    btn_child1.setVisibility(View.VISIBLE);
                    btn_child2.setVisibility(View.VISIBLE);
                    btn_child3.setVisibility(View.VISIBLE);
                    btn_child4.setVisibility(View.GONE);
                    btn_child5.setVisibility(View.GONE);
                    break;
                case "4":
                    ll_child1_details.setVisibility(View.VISIBLE);
                    ll_child5_details.setVisibility(View.GONE);
                    btn_child1.setVisibility(View.VISIBLE);
                    btn_child2.setVisibility(View.VISIBLE);
                    btn_child3.setVisibility(View.VISIBLE);
                    btn_child4.setVisibility(View.VISIBLE);
                    btn_child5.setVisibility(View.GONE);

                    break;
            }

            // Drawable drawableleft = getResources().getDrawable(
            // R.drawable.downarrow);
            // btn_child_header.setCompoundDrawablesWithIntrinsicBounds(
            // drawableleft, null, null, null);
            iv_chid_side_arrow.setImageDrawable(getResources().getDrawable(
                    R.drawable.downarrow));
            mchildDetail = 1;

        } else {
            tv_expand.setText("Click here to expand");
            // Drawable drawableleft = getResources().getDrawable(
            // R.drawable.sidearrow);
            // btn_child_header.setCompoundDrawablesWithIntrinsicBounds(
            // drawableleft, null, null, null);
            ll_retirement_wealth.setVisibility(View.VISIBLE);
            iv_chid_side_arrow.setImageDrawable(getResources().getDrawable(
                    R.drawable.sidearrow));
            ll_child_tab.setVisibility(View.GONE);
            ll_child1_details.setVisibility(View.GONE);
            ll_child2_details.setVisibility(View.GONE);
            ll_child3_details.setVisibility(View.GONE);
            ll_child4_details.setVisibility(View.GONE);
            ll_child5_details.setVisibility(View.GONE);
            mchildDetail = 0;

            // edt_focus.requestFocus();
            btn_my_goals.requestFocus();
            setFocusable(btn_my_goals);
            InputMethodManager imm = (InputMethodManager) this
                    .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }

        btn_my_goals.requestFocus();
        setFocusable(btn_my_goals);
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    public void onMen(View v) {
        str_gender = "M";
        switch_gender.setChecked(false);
        iv_men.setImageDrawable(getResources().getDrawable(
                R.drawable.men_clicked));
        iv_women.setImageDrawable(getResources().getDrawable(
                R.drawable.women_nonclicked));
    }

    public void onWomen(View v) {
        str_gender = "F";
        iv_women.setImageDrawable(getResources().getDrawable(
                R.drawable.women_clicked));
        switch_gender.setChecked(true);
        iv_men.setImageDrawable(getResources().getDrawable(
                R.drawable.men_nonclicked));
    }

    public void onSingle(View v) {
        str_marital_status = "Single";
        switch_marital_status.setChecked(false);
        iv_single.setImageDrawable(getResources().getDrawable(
                R.drawable.single_clicked));
        iv_married.setImageDrawable(getResources().getDrawable(
                R.drawable.married_nonclicked));
    }

    public void onMarried(View v) {
        str_marital_status = "Married";
        iv_single.setImageDrawable(getResources().getDrawable(
                R.drawable.single_nonclicked));
        switch_marital_status.setChecked(true);
        iv_married.setImageDrawable(getResources().getDrawable(
                R.drawable.married_clicked));
    }

    /***** Added by Priyanka Warekar - 10-11-2016 - start *****/
    private void GetMyDetailsvalues() {

        strMonthlyIncome = edt_monthly_income.getText().toString();
        strYearlyIncome = edt_yearly_income.getText().toString();

        strMonthlyIncome_emi = edt_monthly_income_with_emi.getText().toString();
        stryearlyIncome_emi = edt_yearly_income_with_emi.getText().toString();

        // strYearlyIncome.equals(o)
        // if (edt_monthly_income_with_emi.getText().toString().equals(""))
        // strMonthlyIncome_emi = "0";
        // else
        strMonthlyIncome_emi = edt_monthly_income_with_emi.getText().toString();
        // if (edt_yearly_income_with_emi.getText().toString().equals(""))
        // stryearlyIncome_emi = "0";
        // else
        stryearlyIncome_emi = edt_yearly_income_with_emi.getText().toString();
        // if
        // (edt_current_life_insurance_coverage.getText().toString().equals(""))
        // strCurrentLifeInsuranceCoverage = "0";
        // else
        strCurrentLifeInsuranceCoverage = edt_current_life_insurance_coverage
                .getText().toString();
        // if (edt_outstanding_home_loan.getText().toString().equals(""))
        // strOutstandingHomeLoan = "0";
        // else
        strOutstandingHomeLoan = edt_outstanding_home_loan.getText().toString();
        // if (edt_outstanding_home_loan_other.getText().toString().equals(""))
        // strOutstandingHomeLoanother = "0";
        // else
        strOutstandingHomeLoanother = edt_outstanding_home_loan_other.getText()
                .toString();

        str_inflation_assumed = spnr_inflation_assumed.getSelectedItem()
                .toString();

        str_investment_approach = spnr_invesment_approch.getSelectedItem()
                .toString();
    }

    /***** Added by Priyanka Warekar - 10-11-2016 - end *****/
    private void hideKeyboard(AppCompatActivity activity) {

        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    /***** Added by Priyanka Warekar - 10-11-2016 - start *****/
    private void GetMyGoalsValues() {
        // InputMethodManager imm = (InputMethodManager)
        // context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        // if (edt_retirement.getText().toString().equals(""))
        // strRetirementCurrentCorpus = "0";
        // else
        strRetirementCurrentCorpus = edt_retirement.getText().toString();
        // if (edt_retirment_no_of_years.getText().toString().equals(""))
        // strRetirementNoOfRealise = "0";
        // else
        strRetirementNoOfRealise = edt_retirment_no_of_years.getText()
                .toString();
        strChild1Name = edt_child1_name.getText().toString();
        if (edt_child1_age.getText().toString().equals(""))
            strChild1Age = "0";
        else
            strChild1Age = edt_child1_age.getText().toString();
        strChild1AgeAtEducation = edt_child1_education_fund.getText()
                .toString();
        if (edt_child1_education_corpus.getText().toString().equals(""))
            strChild1CorpusEducation = "0";
        else
            strChild1CorpusEducation = edt_child1_education_corpus.getText()
                    .toString();
        if (edt_child1_education_current_cost.getText().toString().equals(""))
            strChild1CurrentCostEducation = "0";
        else
            strChild1CurrentCostEducation = edt_child1_education_current_cost
                    .getText().toString();
        strChild1AgeAtMarriage = edt_child1_marriage_fund.getText().toString();
        if (edt_child1_marriage_corpus.getText().toString().equals(""))
            strChild1CorpusMarriage = "0";
        else
            strChild1CorpusMarriage = edt_child1_marriage_corpus.getText()
                    .toString();
        if (edt_child1_marriage_current_cost.getText().toString().equals(""))
            strChild1CurrentCostMarriage = "0";
        else
            strChild1CurrentCostMarriage = edt_child1_marriage_current_cost
                    .getText().toString();

        strChild2Name = edt_child2_name.getText().toString();
        if (edt_child2_age.getText().toString().equals(""))
            strChild2Age = "0";
        else
            strChild2Age = edt_child2_age.getText().toString();
        strChild2AgeAtEducation = edt_child2_education_fund.getText()
                .toString();
        if (edt_child2_education_corpus.getText().toString().equals(""))
            strChild2CorpusEducation = "0";
        else
            strChild2CorpusEducation = edt_child2_education_corpus.getText()
                    .toString();
        if (edt_child2_education_current_cost.getText().toString().equals(""))
            strChild2CurrentCostEducation = "0";
        else
            strChild2CurrentCostEducation = edt_child2_education_current_cost
                    .getText().toString();
        strChild2AgeAtMarriage = edt_child2_marriage_fund.getText().toString();
        if (edt_child2_marriage_corpus.getText().toString().equals(""))
            strChild2CorpusMarriage = "0";
        else
            strChild2CorpusMarriage = edt_child2_marriage_corpus.getText()
                    .toString();
        if (edt_child2_marriage_current_cost.getText().toString().equals(""))
            strChild2CurrentCostMarriage = "0";
        else
            strChild2CurrentCostMarriage = edt_child2_marriage_current_cost
                    .getText().toString();

        strChild3Name = edt_child3_name.getText().toString();
        if (edt_child3_age.getText().toString().equals(""))
            strChild3Age = "0";
        else
            strChild3Age = edt_child3_age.getText().toString();
        strChild3AgeAtEducation = edt_child3_education_fund.getText()
                .toString();
        if (edt_child3_education_corpus.getText().toString().equals(""))
            strChild3CorpusEducation = "0";
        else
            strChild3CorpusEducation = edt_child3_education_corpus.getText()
                    .toString();
        if (edt_child3_education_current_cost.getText().toString().equals(""))
            strChild3CurrentCostEducation = "0";
        else
            strChild3CurrentCostEducation = edt_child3_education_current_cost
                    .getText().toString();
        strChild3AgeAtMarriage = edt_child3_marriage_fund.getText().toString();
        if (edt_child3_marriage_corpus.getText().toString().equals(""))
            strChild3CorpusMarriage = "0";
        else
            strChild3CorpusMarriage = edt_child3_marriage_corpus.getText()
                    .toString();
        if (edt_child3_marriage_current_cost.getText().toString().equals(""))
            strChild3CurrentCostMarriage = "0";
        else
            strChild3CurrentCostMarriage = edt_child3_marriage_current_cost
                    .getText().toString();

        strChild4Name = edt_child4_name.getText().toString();
        if (edt_child4_age.getText().toString().equals(""))
            strChild4Age = "0";
        else
            strChild4Age = edt_child4_age.getText().toString();
        strChild4AgeAtEducation = edt_child4_education_fund.getText()
                .toString();
        if (edt_child4_education_corpus.getText().toString().equals(""))
            strChild4CorpusEducation = "0";
        else
            strChild4CorpusEducation = edt_child4_education_corpus.getText()
                    .toString();
        if (edt_child4_education_current_cost.getText().toString().equals(""))
            strChild4CurrentCostEducation = "0";
        else
            strChild4CurrentCostEducation = edt_child4_education_current_cost
                    .getText().toString();
        strChild4AgeAtMarriage = edt_child4_marriage_fund.getText().toString();
        if (edt_child4_marriage_corpus.getText().toString().equals(""))
            strChild4CorpusMarriage = "0";
        else
            strChild4CorpusMarriage = edt_child4_marriage_corpus.getText()
                    .toString();
        if (edt_child4_marriage_current_cost.getText().toString().equals(""))
            strChild4CurrentCostMarriage = "0";
        else
            strChild4CurrentCostMarriage = edt_child4_marriage_current_cost
                    .getText().toString();

        strNoOfYearBuyHome = edt_wealth_creation_home.getText().toString();

        // if
        // (edt_wealth_creation_corpus_for_home.getText().toString().equals(""))
        // strCorpusForhome = "0";
        // else
        strCorpusForhome = edt_wealth_creation_corpus_for_home.getText()
                .toString();
        // if (edt_wealth_creation_current_cost_for_home.getText().toString()
        // .equals(""))
        // strCurrentCosthome = "0";
        // else
        strCurrentCosthome = edt_wealth_creation_current_cost_for_home
                .getText().toString();
        strNoOfYearOthergoal = edt_wealth_creation_other.getText().toString();
        // if (edt_wealth_creation_corpus_for_other.getText().toString()
        // .equals(""))
        // strCorpusForOther = "0";
        // else
        strCorpusForOther = edt_wealth_creation_corpus_for_other.getText()
                .toString();
        // if (edt_wealth_creation_current_cost_for_other.getText().toString()
        // .equals(""))
        // strCurrentCostOther = "0";
        // else
        strCurrentCostOther = edt_wealth_creation_current_cost_for_other
                .getText().toString();

        btn_my_goals.requestFocus();
        setFocusable(btn_my_goals);

        hideKeyboard(NeedAnalysisActivity.this);

    }

    /** Added by Priyanka Warekar - 10-11-2016 - end *****/
    /***** Added by Priyanka Warekar - 10-11-2016 - start *****/
    private boolean ValidationMyGoals() {

        if (strRetirementCurrentCorpus.equals("")) {
            final Toast toast = Toast.makeText(this,
                    "Please select Current Corpus for retirement",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_retirement);
            setFocusable(edt_retirement);

            edt_retirement.requestFocus();

            hideKeyboard(NeedAnalysisActivity.this);

            return false;

        } else if (strRetirementNoOfRealise.equals("")) {
            final Toast toast = Toast.makeText(this,
                    "Please select age of Retirement", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            return false;

        } else if (!valRetirementAge()) {
            return false;
        } else if (strRetirementLifeStyle.equals("")) {
            final Toast toast = Toast.makeText(this,
                    "Please select retirement lifestyle", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            return false;

        } else {
            return true;
        }

    }

    private boolean valRetirementAge() {

        // if (!unique_no.equals("")) {
        // str_age = String.valueOf(nac_Age);
        // }

        if ((Integer.parseInt(str_age) >= 40)
                && (Integer.parseInt(strRetirementNoOfRealise) < Integer
                .parseInt(str_age))) {
            final Toast toast = Toast.makeText(this,
                    "Retirement age can not be less than current age",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_retirment_no_of_years);
            setFocusable(edt_retirment_no_of_years);
            edt_retirment_no_of_years.requestFocus();

            hideKeyboard(NeedAnalysisActivity.this);

            return false;
        } else if (Integer.parseInt(strRetirementNoOfRealise) > 80) {
            final Toast toast = Toast.makeText(this,
                    "Retirement age should be between  40 to 80",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_retirment_no_of_years);
            setFocusable(edt_retirment_no_of_years);
            edt_retirment_no_of_years.requestFocus();
            return false;

        } else if (Integer.parseInt(strRetirementNoOfRealise) < 40) {
            final Toast toast = Toast.makeText(this,
                    "Retirement age must be more than 40", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_retirment_no_of_years);
            setFocusable(edt_retirment_no_of_years);
            edt_retirment_no_of_years.requestFocus();
            return false;

        } else if (Integer.parseInt(strRetirementNoOfRealise) <= Integer
                .parseInt(str_age)) {
            Toast toast = Toast.makeText(this,
                    "Retirement age must be more than your age",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            return false;

        } else {
            return true;
        }

    }

    private boolean valHome() {
        if (!strNoOfYearBuyHome.equals("")) {
            if (!strNoOfYearBuyHome.equals("0")) {
                if (!valYearsForHome()) {
                    return false;
                } else if (strCorpusForhome.equals("")) {
                    final Toast toast = Toast
                            .makeText(
                                    this,
                                    "Under Wealth Creation section please select corpus for home",
                                    Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();

                    new CountDownTimer(9000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            toast.show();
                        }

                        public void onFinish() {
                            toast.show();
                        }
                    }.start();

                    clearFocusable(edt_wealth_creation_corpus_for_home);
                    setFocusable(edt_wealth_creation_corpus_for_home);
                    edt_wealth_creation_corpus_for_home.requestFocus();
                    return false;

                } else if (strCurrentCosthome.equals("")
                        || strCurrentCosthome.equals("0")) {
                    final Toast toast = Toast
                            .makeText(
                                    this,
                                    "Under Wealth Creation section, please enter Current Cost for Home",
                                    Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();

                    new CountDownTimer(9000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            toast.show();
                        }

                        public void onFinish() {
                            toast.show();
                        }
                    }.start();

                    clearFocusable(edt_wealth_creation_current_cost_for_home);
                    setFocusable(edt_wealth_creation_current_cost_for_home);
                    edt_wealth_creation_current_cost_for_home.requestFocus();
                    return false;

                } else if (Long.parseLong(strCurrentCosthome) < Long
                        .parseLong(strCorpusForhome)) {
                    final Toast toast = Toast
                            .makeText(
                                    this,
                                    "Under Wealth Creation section the Current Cost should be more than the Current Corpus Saved",
                                    Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();

                    new CountDownTimer(9000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            toast.show();
                        }

                        public void onFinish() {
                            toast.show();
                        }
                    }.start();

                    clearFocusable(edt_wealth_creation_current_cost_for_home);
                    setFocusable(edt_wealth_creation_current_cost_for_home);
                    edt_wealth_creation_current_cost_for_home.requestFocus();
                    return false;

                } else {
                    return true;

                }
            } else {
                strCorpusForhome = "0";
                strCurrentCosthome = "0";
                return true;

            }
        } else {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Under Wealth Creation section please select no years to buy home",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            return false;
        }
    }

    private boolean valOther() {
        if (!strNoOfYearOthergoal.equals("")) {
            if (!strNoOfYearOthergoal.equals("0")) {

                if (!valYearsForOther()) {
                    return false;
                } else if (strCorpusForOther.equals("")) {
                    final Toast toast = Toast
                            .makeText(
                                    this,
                                    "Under Wealth Creation section please select corpus for other",
                                    Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();

                    new CountDownTimer(9000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            toast.show();
                        }

                        public void onFinish() {
                            toast.show();
                        }
                    }.start();

                    clearFocusable(edt_wealth_creation_corpus_for_other);
                    setFocusable(edt_wealth_creation_corpus_for_other);
                    edt_wealth_creation_corpus_for_other.requestFocus();
                    return false;

                } else if (strCurrentCostOther.equals("")
                        || strCurrentCostOther.equals("0")) {
                    final Toast toast = Toast
                            .makeText(
                                    this,
                                    "Under Wealth Creation section, please enter Current Cost for Other Goals",
                                    Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();

                    new CountDownTimer(9000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            toast.show();
                        }

                        public void onFinish() {
                            toast.show();
                        }
                    }.start();

                    clearFocusable(edt_wealth_creation_current_cost_for_other);
                    setFocusable(edt_wealth_creation_current_cost_for_other);
                    edt_wealth_creation_current_cost_for_other.requestFocus();
                    return false;

                } else if (Long.parseLong(strCurrentCostOther) < Long
                        .parseLong(strCorpusForOther)) {
                    final Toast toast = Toast
                            .makeText(
                                    this,
                                    "Under Wealth Creation section  the Current Cost should be more than the Current Corpus Saved",
                                    Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();

                    new CountDownTimer(9000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            toast.show();
                        }

                        public void onFinish() {
                            toast.show();
                        }
                    }.start();

                    clearFocusable(edt_wealth_creation_current_cost_for_other);
                    setFocusable(edt_wealth_creation_current_cost_for_other);
                    edt_wealth_creation_current_cost_for_other.requestFocus();
                    return false;

                } else {
                    return true;
                }
            } else {
                strCorpusForOther = "0";
                strCurrentCostOther = "0";
                return true;
            }
        } else {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Under Wealth Creation section Please enter no. of years to realise other goals",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            return false;
        }

    }

    private boolean valYearsForHome() {

        if ((Integer.parseInt(strNoOfYearBuyHome) >= 1 && Integer
                .parseInt(strNoOfYearBuyHome) <= 35)) {
            return true;
        } else {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Under Wealth Creation section please select 'No. of years to buy a home' between 1 to 35",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_wealth_creation_home);
            setFocusable(edt_wealth_creation_home);
            edt_wealth_creation_home.requestFocus();

            return false;
        }

    }

    private boolean valYearsForOther() {
        if ((Integer.parseInt(strNoOfYearOthergoal) >= 1 && Integer
                .parseInt(strNoOfYearOthergoal) <= 35)) {

            return true;

        } else {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Under Wealth Creation section please select 'No. of years to realise other goals' between 1 to 35",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_wealth_creation_other);
            setFocusable(edt_wealth_creation_other);
            edt_wealth_creation_other.requestFocus();
            return false;
        }

    }

    private boolean valChild1Age() {

        if (edt_child1_age.getText().toString().equals("")) {
            final Toast toast = Toast.makeText(this,
                    "Please Enter Child 1 Age", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_child1_age);
            setFocusable(edt_child1_age);
            edt_child1_age.requestFocus();
            return false;

        } else if (Integer.parseInt(edt_child1_age.getText().toString()) > 17) {
            final Toast toast = Toast.makeText(this,
                    "Maximum age for child1 is 17", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child1_age);
            setFocusable(edt_child1_age);
            edt_child1_age.requestFocus();
            return false;

        } else if (Integer.parseInt(edt_child1_education_fund.getText()
                .toString()) < 18) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which education fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child1_education_fund);
            setFocusable(edt_child1_education_fund);
            edt_child1_education_fund.requestFocus();
            return false;
        } else if (Integer.parseInt(edt_child1_education_fund.getText()
                .toString()) > 25) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which education fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child1_education_fund);
            setFocusable(edt_child1_education_fund);
            edt_child1_education_fund.requestFocus();
            return false;
        } else if (Integer.parseInt(edt_child1_marriage_fund.getText()
                .toString()) < 18) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which marriage fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child1_marriage_fund);
            setFocusable(edt_child1_marriage_fund);
            edt_child1_marriage_fund.requestFocus();
            return false;
        } else if (Integer
                .parseInt(edt_child1_marriage_fund.getText().toString()) > 25) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which marriage fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child1_marriage_fund);
            setFocusable(edt_child1_marriage_fund);
            edt_child1_marriage_fund.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    private boolean valChild2Age() {

        if (edt_child2_age.getText().toString().equals("")) {
            final Toast toast = Toast.makeText(this,
                    "Please Enter Child 2 Age", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_child2_age);
            setFocusable(edt_child2_age);
            edt_child2_age.requestFocus();
            return false;

        } else if (Integer.parseInt(edt_child2_age.getText().toString()) > 17) {
            final Toast toast = Toast.makeText(this,
                    "Maximum  age for child2 is 17", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child2_age);
            setFocusable(edt_child2_age);
            edt_child2_age.requestFocus();
            return false;

        } else if (Integer.parseInt(edt_child2_education_fund.getText()
                .toString()) < 18) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which education fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child2_education_fund);
            setFocusable(edt_child2_education_fund);
            edt_child2_education_fund.requestFocus();
            return false;
        } else if (Integer.parseInt(edt_child2_education_fund.getText()
                .toString()) > 25) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which education fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child2_education_fund);
            setFocusable(edt_child2_education_fund);
            edt_child2_education_fund.requestFocus();
            return false;
        } else if (Integer.parseInt(edt_child2_marriage_fund.getText()
                .toString()) < 18) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which marriage fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_child2_marriage_fund);
            setFocusable(edt_child2_marriage_fund);
            edt_child2_marriage_fund.requestFocus();
            return false;
        } else if (Integer
                .parseInt(edt_child2_marriage_fund.getText().toString()) > 25) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which marriage fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_child2_marriage_fund);
            setFocusable(edt_child2_marriage_fund);
            edt_child2_marriage_fund.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean valChild3Age() {

        if (edt_child3_age.getText().toString().equals("")) {
            final Toast toast = Toast.makeText(this,
                    "Please Enter Child 3 Age", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child3_age);
            setFocusable(edt_child3_age);
            edt_child3_age.requestFocus();
            return false;

        } else if (Integer.parseInt(edt_child3_age.getText().toString()) > 17) {
            final Toast toast = Toast.makeText(this,
                    "Maximum age for child 3 is 17", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child3_age);
            setFocusable(edt_child3_age);
            edt_child3_age.requestFocus();
            return false;

        } else if (Integer.parseInt(edt_child3_education_fund.getText()
                .toString()) < 18) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which education fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child3_education_fund);
            setFocusable(edt_child3_education_fund);
            edt_child3_education_fund.requestFocus();
            return false;
        } else if (Integer.parseInt(edt_child3_education_fund.getText()
                .toString()) > 25) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which education fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child3_education_fund);
            setFocusable(edt_child3_education_fund);
            edt_child3_education_fund.requestFocus();
            return false;
        } else if (Integer.parseInt(edt_child3_marriage_fund.getText()
                .toString()) < 18) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which marriage fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child3_marriage_fund);
            setFocusable(edt_child3_marriage_fund);
            edt_child3_marriage_fund.requestFocus();
            return false;
        } else if (Integer
                .parseInt(edt_child3_marriage_fund.getText().toString()) > 25) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which marriage fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child3_marriage_fund);
            setFocusable(edt_child3_marriage_fund);
            edt_child3_marriage_fund.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    private boolean valChild4Age() {

        if (edt_child4_age.getText().toString().equals("")) {
            final Toast toast = Toast.makeText(this,
                    "Please Enter Child 4 Age", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_child4_age);
            setFocusable(edt_child4_age);
            edt_child4_age.requestFocus();
            return false;

        } else if (Integer.parseInt(edt_child4_age.getText().toString()) > 17) {
            final Toast toast = Toast.makeText(this,
                    "Maximum age for child 4 is 17", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_child4_age);
            setFocusable(edt_child4_age);
            edt_child4_age.requestFocus();
            return false;

        } else if (Integer.parseInt(edt_child4_education_fund.getText()
                .toString()) < 18) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which education fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_child4_education_fund);
            setFocusable(edt_child4_education_fund);
            edt_child4_education_fund.requestFocus();
            return false;
        } else if (Integer.parseInt(edt_child4_education_fund.getText()
                .toString()) > 25) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which education fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            clearFocusable(edt_child4_education_fund);
            setFocusable(edt_child4_education_fund);
            edt_child4_education_fund.requestFocus();
            return false;
        } else if (Integer.parseInt(edt_child4_marriage_fund.getText()
                .toString()) < 18) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which marriage fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child4_marriage_fund);
            setFocusable(edt_child4_marriage_fund);
            edt_child4_marriage_fund.requestFocus();
            return false;
        } else if (Integer
                .parseInt(edt_child4_marriage_fund.getText().toString()) > 25) {
            final Toast toast = Toast
                    .makeText(
                            this,
                            "Age at which marriage fund is required should be between 18 to 25 years.",
                            Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            clearFocusable(edt_child4_marriage_fund);
            setFocusable(edt_child4_marriage_fund);
            edt_child4_marriage_fund.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    private boolean ValChild1() {
        if (Integer.parseInt(str_no_of_child) > 0) {
            // if (strChild1Name.equals("")) {
            // Toast toast = Toast.makeText(this, "Please Enter Child 1 Name",
            // Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.TOP, 105, 50);
            // toast.show();
            // return false;
            //
            // }

            if (!valChild1Age()) {
                return false;
            } else if (edt_child1_education_fund.getText().toString().equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 1 age at education fund",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child1_education_corpus.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 1 corpus for education",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child1_education_current_cost.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please select child 1 current cost for education",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (Long.parseLong(edt_child1_education_current_cost.getText()
                    .toString()) < Long.parseLong(edt_child1_education_corpus
                    .getText().toString())) {
                final Toast toast = Toast
                        .makeText(
                                this,
                                "For "
                                        + btn_child1.getText().toString()
                                        + ", under Education need,The Current Cost should be more than the Corpus already Saved",
                                Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                clearFocusable(edt_child1_education_corpus);
                setFocusable(edt_child1_education_corpus);
                edt_child1_education_corpus.requestFocus();
                return false;

            } else if (strChild1AgeAtMarriage.equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 1 age at marriage fund",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child1_marriage_corpus.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 1 corpus for marriage",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child1_marriage_current_cost.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 1 current cost for marriage",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (Long.parseLong(edt_child1_marriage_current_cost.getText()
                    .toString()) < Long.parseLong(edt_child1_marriage_corpus
                    .getText().toString())) {
                final Toast toast = Toast
                        .makeText(
                                this,
                                "For "
                                        + btn_child1.getText().toString()
                                        + ", under Marriage need,The Current Cost should be more than the Corpus already Saved",
                                Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                clearFocusable(edt_child1_marriage_corpus);
                setFocusable(edt_child1_marriage_corpus);
                edt_child1_marriage_corpus.requestFocus();
                return false;

            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean ValChild2() {
        if (str_no_of_child.equals("2") || str_no_of_child.equals("3")
                || str_no_of_child.equals("4")) {
            // if (strChild2Name.equals("")) {
            // Toast toast = Toast.makeText(this, "Please Enter Child 2 Name",
            // Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.TOP, 105, 50);
            // toast.show();
            // return false;
            //
            // }
            if (!valChild2Age()) {
                return false;

            } else if (strChild2AgeAtEducation.equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 2 age at education fund",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child2_education_corpus.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 2 corpus for education",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child2_education_current_cost.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 2 current cost for education",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (Long.parseLong(edt_child2_education_current_cost.getText()
                    .toString()) < Long.parseLong(edt_child2_education_corpus
                    .getText().toString())) {
                final Toast toast = Toast
                        .makeText(
                                this,
                                "For "
                                        + btn_child2.getText().toString()
                                        + ", under Education need,The Current Cost should be more than the Corpus already Saved",
                                Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                clearFocusable(edt_child2_education_corpus);
                setFocusable(edt_child2_education_corpus);
                edt_child2_education_corpus.requestFocus();
                return false;

            } else if (strChild2AgeAtMarriage.equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 2 age at marriage fund",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child2_marriage_corpus.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 2 corpus for marriage",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child2_marriage_current_cost.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 2 current cost for marriage",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (Long.parseLong(edt_child2_marriage_current_cost.getText()
                    .toString()) < Long.parseLong(edt_child2_marriage_corpus
                    .getText().toString())) {
                final Toast toast = Toast
                        .makeText(
                                this,
                                "For "
                                        + btn_child2.getText().toString()
                                        + ", under Marriage need,The Current Cost should be more than the Corpus already Saved",
                                Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                clearFocusable(edt_child2_marriage_corpus);
                setFocusable(edt_child2_marriage_corpus);
                edt_child2_marriage_corpus.requestFocus();
                return false;

            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean ValChild3() {
        if (str_no_of_child.equals("3") || str_no_of_child.equals("4")) {
            // if (strChild3Name.equals("")) {
            // Toast toast = Toast.makeText(this, "Please Enter Child 3 Name",
            // Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.TOP, 105, 50);
            // toast.show();
            // return false;
            //
            // }
            if (!valChild3Age()) {
                return false;

            } else if (strChild3AgeAtEducation.equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 3 age at education fund",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child3_education_corpus.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 3 corpus for education",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child3_education_current_cost.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 3 current cost for education",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (Long.parseLong(edt_child3_education_current_cost
                    .getText().toString()) < Long
                    .parseLong(edt_child3_education_corpus.getText().toString())) {
                final Toast toast = Toast
                        .makeText(
                                this,
                                "For "
                                        + btn_child3.getText().toString()
                                        + ", under Education need,The Current Cost should be more than the Corpus already Saved",
                                Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                clearFocusable(edt_child3_education_corpus);
                setFocusable(edt_child3_education_corpus);
                edt_child3_education_corpus.requestFocus();
                return false;

            } else if (strChild3AgeAtMarriage.equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 3 age at marriage fund",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child3_marriage_corpus.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 3 corpus for marriage",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child3_marriage_current_cost.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 3 current cost for marriage",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (Long.parseLong(edt_child3_marriage_current_cost.getText()
                    .toString()) < Long.parseLong(edt_child3_marriage_corpus
                    .getText().toString())) {
                final Toast toast = Toast
                        .makeText(
                                this,
                                "For "
                                        + btn_child3.getText().toString()
                                        + ", under Marriage need,The Current Cost should be more than the Corpus already Saved",
                                Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                clearFocusable(edt_child3_education_corpus);
                setFocusable(edt_child3_education_corpus);
                edt_child3_education_corpus.requestFocus();
                return false;

            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    private boolean ValChild4() {
        if (str_no_of_child.equals("4")) {
            // if (strChild4Name.equals("")) {
            // Toast toast = Toast.makeText(this, "Please Enter Child 4 Name",
            // Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.TOP, 105, 50);
            // toast.show();
            // return false;
            //
            // }
            if (!valChild4Age()) {
                return false;

            } else if (strChild4AgeAtEducation.equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 4 age at education fund",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child4_education_corpus.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 4 corpus for education",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child4_education_current_cost.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 4 current cost for education",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (Long.parseLong(edt_child4_education_current_cost
                    .getText().toString()) < Long
                    .parseLong(edt_child4_education_corpus.getText().toString())) {
                final Toast toast = Toast
                        .makeText(
                                this,
                                "For "
                                        + btn_child4.getText().toString()
                                        + ", under Education need,The Current Cost should be more than the Corpus already Saved",
                                Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                clearFocusable(edt_child4_education_corpus);
                setFocusable(edt_child4_education_corpus);
                edt_child4_education_corpus.requestFocus();
                return false;

            } else if (strChild4AgeAtMarriage.equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 4 age at marriage fund",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child4_marriage_corpus.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 4 corpus for marriage",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (edt_child4_marriage_current_cost.getText().toString()
                    .equals("")) {
                final Toast toast = Toast.makeText(this,
                        "Please enter child 4 current cost for marriage",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                return false;

            } else if (Long.parseLong(edt_child4_marriage_current_cost.getText()
                    .toString()) < Long.parseLong(edt_child4_marriage_corpus
                    .getText().toString())) {
                final Toast toast = Toast
                        .makeText(
                                this,
                                "For "
                                        + btn_child4.getText().toString()
                                        + ", under Marriage need,The Current Cost should be more than the Corpus already Saved",
                                Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 105, 50);
                toast.show();
                new CountDownTimer(9000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.show();
                    }
                }.start();
                clearFocusable(edt_child4_marriage_corpus);
                setFocusable(edt_child4_marriage_corpus);
                edt_child4_marriage_corpus.requestFocus();
                return false;

            } else {
                return true;
            }

        } else {
            return true;
        }

    }

    /***** Added by Priyanka Warekar - 10-11-2016 - end *****/
    private boolean ValidationMyDetails() {

        if (spnr_group.getSelectedItem().toString().equals("Select")) {
            final Toast toast = Toast.makeText(this, "Please select group",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            return false;
        } else if (str_date_of_birth.equals("")) {
            final Toast toast = Toast.makeText(this,
                    "Please select Date of Birth", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();

            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();
            return false;

        } else if (spnr_children.getSelectedItem().toString().equals("Select")) {
            final Toast toast = Toast.makeText(this,
                    "Please select number of Child", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            return false;

        } else if (strMonthlyIncome.equals("0") || strMonthlyIncome.equals("")) {
            final Toast toast = Toast.makeText(this,
                    "Please select Monthly Income", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            new CountDownTimer(9000, 1000) {

                public void onTick(long millisUntilFinished) {
                    toast.show();
                }

                public void onFinish() {
                    toast.show();
                }
            }.start();

            // set focusable method
            clearFocusable(edt_monthly_income);
            setFocusable(edt_monthly_income);
            edt_monthly_income.requestFocus();

            return false;

        } else if (strYearlyIncome.equals("")) {
            Toast toast = Toast.makeText(this, "Please select Yearly Income",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            return false;

        } else if (strMonthlyIncome_emi.equals("")) {
            Toast toast = Toast.makeText(this,
                    "Please select Monthly Expenses", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            return false;

        } else if (stryearlyIncome_emi.equals("")) {
            Toast toast = Toast.makeText(this, "Please select Yearly Expenses",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            return false;

        } else if (strCurrentLifeInsuranceCoverage.equals("")) {
            Toast toast = Toast.makeText(this,
                    "Please select current life Insurance coverage",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            return false;

        } else if (strOutstandingHomeLoan.equals("")) {
            Toast toast = Toast.makeText(this,
                    "Please select outstanding home loan", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            return false;

        } else if (strOutstandingHomeLoanother.equals("")) {
            Toast toast = Toast.makeText(this,
                    "Please select outstanding home loan for other",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            return false;

        } else if (str_inflation_assumed.equals("Select")) {
            Toast toast = Toast.makeText(this,
                    "Please select inflation assumed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            return false;

        } else if (str_investment_approach.equals("Select")) {
            Toast toast = Toast.makeText(this,
                    "Please select investment approach", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
            return false;

        } else {
            return true;
        }

    }

    private void InitializeVariable() {
        switch_gender = findViewById(R.id.switch_gender);
        switch_marital_status = findViewById(R.id.switch_marital_status);
        sb_monthly_income = findViewById(R.id.sb_monthly_income);
        sb_monthly_income.incrementProgressBy(2000);
        sb_monthly_income_with_emi = findViewById(R.id.sb_monthly_income_with_emi);
        sb_current_life_insurance_coverage = findViewById(R.id.sb_current_life_insurance_coverage);
        sb_outstanding_home_loan = findViewById(R.id.sb_outstanding_home_loan);

        sb_outstanding_home_loan_other = findViewById(R.id.sb_outstanding_home_loan_other);

        edt_monthly_income = findViewById(R.id.edt_monthly_income);
        edt_yearly_income = findViewById(R.id.edt_yearly_income);
        edt_monthly_income_with_emi = findViewById(R.id.edt_monthly_income_with_emi);
        edt_yearly_income_with_emi = findViewById(R.id.edt_yearly_income_with_emi);

        edt_current_life_insurance_coverage = findViewById(R.id.edt_current_life_insurance_coverage);
        edt_outstanding_home_loan = findViewById(R.id.edt_outstanding_home_loan);
        edt_outstanding_home_loan_other = findViewById(R.id.edt_outstanding_home_loan_other);
        btn_date_of_birth = findViewById(R.id.btn_date_of_birth);
        iv_women = findViewById(R.id.iv_women);
        iv_men = findViewById(R.id.iv_men);

        iv_single = findViewById(R.id.iv_single);
        iv_married = findViewById(R.id.iv_married);

        tv_age = findViewById(R.id.tv_age);

        spnr_group = findViewById(R.id.spnr_group);
        spnr_children = findViewById(R.id.spnr_children);
        spnr_inflation_assumed = findViewById(R.id.spnr_inflation_assumed);
        spnr_invesment_approch = findViewById(R.id.spnr_invesment_approch);
        /* For Declartion of My Goals */
        rg_post_reitrement = findViewById(R.id.rg_post_reitrement);
        sb_retirement = findViewById(R.id.sb_retirement);
        sb_child1_education_corpus = findViewById(R.id.sb_child1_education_corpus);
        sb_child1_education_current_cost = findViewById(R.id.sb_child1_education_current_cost);
        sb_child1_marriage_corpus = findViewById(R.id.sb_child1_marriage_corpus);
        sb_child1_marrige_current_cost = findViewById(R.id.sb_child1_marrige_current_cost);
        sb_child2_education_corpus = findViewById(R.id.sb_child2_education_corpus);
        sb_child2_education_current_cost = findViewById(R.id.sb_child2_education_current_cost);
        sb_child2_marriage_corpus = findViewById(R.id.sb_child2_marriage_corpus);
        sb_child2_marrige_current_cost = findViewById(R.id.sb_child2_marrige_current_cost);
        sb_child3_education_corpus = findViewById(R.id.sb_child3_education_corpus);
        sb_child3_education_current_cost = findViewById(R.id.sb_child3_education_current_cost);
        sb_child3_marriage_corpus = findViewById(R.id.sb_child3_marriage_corpus);
        sb_child3_marrige_current_cost = findViewById(R.id.sb_child3_marrige_current_cost);
        sb_child4_education_corpus = findViewById(R.id.sb_child4_education_corpus);
        sb_child4_education_current_cost = findViewById(R.id.sb_child4_education_current_cost);
        sb_child4_marriage_corpus = findViewById(R.id.sb_child4_marriage_corpus);
        sb_child4_marrige_current_cost = findViewById(R.id.sb_child4_marrige_current_cost);
        sb_child5_education_corpus = findViewById(R.id.sb_child5_education_corpus);
        sb_child5_education_current_cost = findViewById(R.id.sb_child5_education_current_cost);
        sb_child5_marriage_corpus = findViewById(R.id.sb_child5_marriage_corpus);
        // sb_child5_marrige_current_cost = (SeekBar)
        // findViewById(R.id.sb_child5_marrige_current_cost);

        sb_wealth_creation_corpus_for_home = findViewById(R.id.sb_wealth_creation_corpus_for_home);

        sb_wealth_creation_corpus_for_other = findViewById(R.id.sb_wealth_creation_corpus_for_other);
        edt_retirement = findViewById(R.id.edt_retirement);
        edt_child1_name = findViewById(R.id.edt_child1_name);
        edt_child1_education_corpus = findViewById(R.id.edt_child1_education_corpus);
        edt_child1_education_current_cost = findViewById(R.id.edt_child1_education_current_cost);
        edt_child1_marriage_corpus = findViewById(R.id.edt_child1_marriage_corpus);
        edt_child1_marriage_current_cost = findViewById(R.id.edt_child1_marriage_current_cost);

        edt_child2_name = findViewById(R.id.edt_child2_name);
        edt_child2_education_corpus = findViewById(R.id.edt_child2_education_corpus);
        edt_child2_education_current_cost = findViewById(R.id.edt_child2_education_current_cost);
        edt_child2_marriage_corpus = findViewById(R.id.edt_child2_marriage_corpus);
        edt_child2_marriage_current_cost = findViewById(R.id.edt_child2_marriage_current_cost);

        edt_child3_name = findViewById(R.id.edt_child3_name);
        edt_child3_education_corpus = findViewById(R.id.edt_child3_education_corpus);
        edt_child3_education_current_cost = findViewById(R.id.edt_child3_education_current_cost);
        edt_child3_marriage_corpus = findViewById(R.id.edt_child3_marriage_corpus);
        edt_child3_marriage_current_cost = findViewById(R.id.edt_child3_marriage_current_cost);

        edt_child4_name = findViewById(R.id.edt_child4_name);
        edt_child4_education_corpus = findViewById(R.id.edt_child4_education_corpus);
        edt_child4_education_current_cost = findViewById(R.id.edt_child4_education_current_cost);
        edt_child4_marriage_corpus = findViewById(R.id.edt_child4_marriage_corpus);
        edt_child4_marriage_current_cost = findViewById(R.id.edt_child4_marriage_current_cost);

        // edt_child5_name = (EditText) findViewById(R.id.edt_child5_name);
        edt_child5_education_corpus = findViewById(R.id.edt_child5_education_corpus);
        edt_child5_education_current_cost = findViewById(R.id.edt_child5_education_current_cost);
        edt_child5_marriage_corpus = findViewById(R.id.edt_child5_marriage_corpus);
        // edt_child5_marriage_current_cost = (EditText)
        // findViewById(R.id.edt_child5_marriage_current_cost);

        edt_wealth_creation_corpus_for_home = findViewById(R.id.edt_wealth_creation_corpus_for_home);
        edt_wealth_creation_current_cost_for_home = findViewById(R.id.edt_wealth_creation_current_cost_for_home);
        edt_wealth_creation_corpus_for_other = findViewById(R.id.edt_wealth_creation_corpus_for_other);
        edt_wealth_creation_current_cost_for_other = findViewById(R.id.edt_wealth_creation_current_cost_for_other);

        btn_retirement_minus = findViewById(R.id.btn_retirement_minus);
        btn_retirement_plus = findViewById(R.id.btn_retirement_plus);
        btn_child1_age_minus = findViewById(R.id.btn_child1_age_minus);
        btn_child1_age_plus = findViewById(R.id.btn_child1_age_plus);
        btn_child1_education_fund_minus = findViewById(R.id.btn_child1_education_fund_minus);
        btn_child1_education_fund_plus = findViewById(R.id.btn_child1_education_fund_plus);
        btn_child1_marriage_fund_minus = findViewById(R.id.btn_child1_marriage_fund_minus);
        btn_child1_marriage_fund_plus = findViewById(R.id.btn_child1_marriage_fund_plus);

        btn_child2_age_minus = findViewById(R.id.btn_child2_age_minus);
        btn_child2_age_plus = findViewById(R.id.btn_child2_age_plus);
        btn_child2_education_fund_minus = findViewById(R.id.btn_child2_education_fund_minus);
        btn_child2_education_fund_plus = findViewById(R.id.btn_child2_education_fund_plus);
        btn_child2_marriage_fund_minus = findViewById(R.id.btn_child2_marriage_fund_minus);
        btn_child2_marriage_fund_plus = findViewById(R.id.btn_child2_marriage_fund_plus);

        btn_child3_age_minus = findViewById(R.id.btn_child3_age_minus);
        btn_child3_age_plus = findViewById(R.id.btn_child3_age_plus);
        btn_child3_education_fund_minus = findViewById(R.id.btn_child3_education_fund_minus);
        btn_child3_education_fund_plus = findViewById(R.id.btn_child3_education_fund_plus);
        btn_child3_marriage_fund_minus = findViewById(R.id.btn_child3_marriage_fund_minus);
        btn_child3_marriage_fund_plus = findViewById(R.id.btn_child3_marriage_fund_plus);

        btn_child4_age_minus = findViewById(R.id.btn_child4_age_minus);
        btn_child4_age_plus = findViewById(R.id.btn_child4_age_plus);
        btn_child4_education_fund_minus = findViewById(R.id.btn_child4_education_fund_minus);
        btn_child4_education_fund_plus = findViewById(R.id.btn_child4_education_fund_plus);
        btn_child4_marriage_fund_minus = findViewById(R.id.btn_child4_marriage_fund_minus);
        btn_child4_marriage_fund_plus = findViewById(R.id.btn_child4_marriage_fund_plus);

        btn_child5_age_minus = findViewById(R.id.btn_child5_age_minus);
        btn_child5_age_plus = findViewById(R.id.btn_child5_age_plus);
        btn_child5_education_fund_minus = findViewById(R.id.btn_child5_education_fund_minus);
        btn_child5_education_fund_plus = findViewById(R.id.btn_child5_education_fund_plus);
        btn_child5_marriage_fund_minus = findViewById(R.id.btn_child5_marriage_fund_minus);
        btn_child5_marriage_fund_plus = findViewById(R.id.btn_child5_marriage_fund_plus);

        btn_wealth_creation_home_minus = findViewById(R.id.btn_wealth_creation_home_minus);
        btn_wealth_creation_home_plus = findViewById(R.id.btn_wealth_creation_home_plus);
        btn_wealth_creation_other_minus = findViewById(R.id.btn_wealth_creation_other_minus);
        btn_wealth_creation_other_plus = findViewById(R.id.btn_wealth_creation_other_plus);

        edt_retirment_no_of_years = findViewById(R.id.edt_retirment_no_of_years);

        edt_child1_age = findViewById(R.id.edt_child1_age);
        edt_child1_education_fund = findViewById(R.id.edt_child1_education_fund);
        edt_child1_marriage_fund = findViewById(R.id.edt_child1_marriage_fund);

        edt_child2_age = findViewById(R.id.edt_child2_age);
        edt_child2_education_fund = findViewById(R.id.edt_child2_education_fund);
        edt_child2_marriage_fund = findViewById(R.id.edt_child2_marriage_fund);
        edt_child3_age = findViewById(R.id.edt_child3_age);
        edt_child3_education_fund = findViewById(R.id.edt_child3_education_fund);
        edt_child3_marriage_fund = findViewById(R.id.edt_child3_marriage_fund);

        edt_child4_age = findViewById(R.id.edt_child4_age);
        edt_child4_education_fund = findViewById(R.id.edt_child4_education_fund);
        edt_child4_marriage_fund = findViewById(R.id.edt_child4_marriage_fund);
        edt_child5_age = findViewById(R.id.edt_child5_age);
        edt_child5_education_fund = findViewById(R.id.edt_child5_education_fund);
        edt_child5_marriage_fund = findViewById(R.id.edt_child5_marriage_fund);

        edt_wealth_creation_home = findViewById(R.id.edt_wealth_creation_home);
        edt_wealth_creation_other = findViewById(R.id.edt_wealth_creation_other);
        ll_product_chosen = findViewById(R.id.ll_product_chosen);
        ll_product_chosen_1 = findViewById(R.id.ll_product_chosen_1);
        ll_product_chosen_2 = findViewById(R.id.ll_product_chosen_2);
        ll_product_chosen_3 = findViewById(R.id.ll_product_chosen_3);
        ll_product_chosen_4 = findViewById(R.id.ll_product_chosen_4);
        ll_product_chosen_5 = findViewById(R.id.ll_product_chosen_5);
        ll_product_chosen_6 = findViewById(R.id.ll_product_chosen_6);

        txt_product_chosen_1 = findViewById(R.id.txt_product_chosen_1);
        txt_product_chosen_2 = findViewById(R.id.txt_product_chosen_2);
        txt_product_chosen_3 = findViewById(R.id.txt_product_chosen_3);
        txt_product_chosen_4 = findViewById(R.id.txt_product_chosen_4);
        txt_product_chosen_5 = findViewById(R.id.txt_product_chosen_5);
        txt_product_chosen_6 = findViewById(R.id.txt_product_chosen_6);

        ll_child1_details = findViewById(R.id.ll_child1_details);
        ll_child2_details = findViewById(R.id.ll_child2_details);
        ll_child3_details = findViewById(R.id.ll_child3_details);
        ll_child4_details = findViewById(R.id.ll_child4_details);
        ll_child5_details = findViewById(R.id.ll_child5_details);
        ll_child_tab = findViewById(R.id.ll_child_tab);
        btn_child1 = findViewById(R.id.btn_child1);
        btn_child2 = findViewById(R.id.btn_child2);
        btn_child3 = findViewById(R.id.btn_child3);
        btn_child4 = findViewById(R.id.btn_child4);
        btn_child5 = findViewById(R.id.btn_child5);
        // btn_child_header = (Button) findViewById(R.id.btn_child_header);

        /* Initialization for Solution */
        txt_protection = findViewById(R.id.txt_protection);
        txt_retirement = findViewById(R.id.txt_retirement);
        txt_child_future = findViewById(R.id.txt_child_future);
        txt_wealth_creation = findViewById(R.id.txt_wealth_creation);

        txt_protection_gap = findViewById(R.id.txt_protection_gap);
        txt_protection_target_amt = findViewById(R.id.txt_protection_target_amt);
        txt_protection_corpus = findViewById(R.id.txt_protection_corpus);

        txt_retirement_gap = findViewById(R.id.txt_retirement_gap);
        txt_retirement_target_amt = findViewById(R.id.txt_retirement_target_amt);
        txt_retirement_corpus = findViewById(R.id.txt_retirement_corpus);

        txt_child_future_gap = findViewById(R.id.txt_child_future_gap);
        txt_child_future_target = findViewById(R.id.txt_child_future_target);
        txt_child_future_corpus = findViewById(R.id.txt_child_future_corpus);

        txt_wealth_creation_gap = findViewById(R.id.txt_wealth_creation_gap);
        txt_wealth_creation_target = findViewById(R.id.txt_wealth_creation_target);
        txt_wealth_creation_corpus = findViewById(R.id.txt_wealth_creation_corpus);

        ln_chart_protection_top = findViewById(R.id.ln_chart_protection_top);
        ln_chart_protection_buttom = findViewById(R.id.ln_chart_protection_buttom);

        ln_chart_retirement_top = findViewById(R.id.ln_chart_retirement_top);
        ln_chart_retirement_buttom = findViewById(R.id.ln_chart_retirement_buttom);

        ln_chart_child_top = findViewById(R.id.ln_chart_child_top);
        ln_chart_child_buttom = findViewById(R.id.ln_chart_child_buttom);

        ln_chart_wealth_top = findViewById(R.id.ln_chart_wealth_top);
        ln_chart_wealth_buttom = findViewById(R.id.ln_chart_wealth_buttom);

        ll_protection = findViewById(R.id.ll_protection);
        ll_retirement = findViewById(R.id.ll_retirement);
        ll_child = findViewById(R.id.ll_child);
        ll_wealth_creation = findViewById(R.id.ll_wealth_creation);
        ll_summary = findViewById(R.id.ll_summary);

        ln_trad_ulip = findViewById(R.id.ln_trad_ulip);
        ln_ulip_trad = findViewById(R.id.ln_ulip_trad);
        iv_wealthCreation = findViewById(R.id.iv_wealthCreation);
        iv_child = findViewById(R.id.iv_child);
        iv_retirement = findViewById(R.id.iv_retirement);
        iv_protection = findViewById(R.id.iv_protection);
        // iv_summary = (ImageView) findViewById(R.id.iv_summary);
        // scrollview = (ScrollView) findViewById(R.id.scrollview);

        edt_focus = findViewById(R.id.edt_focus);

        /* For Child */

        // child 1
        ln_child_1 = findViewById(R.id.ln_child_1);
        txt_child_1_tot_amt = findViewById(R.id.txt_child_1_tot_amt);
        txt_child_1_edu_tar_amount = findViewById(R.id.txt_child_1_edu_tar_amount);
        txt_child_1_edu_gap = findViewById(R.id.txt_child_1_edu_gap);
        txt_child_1_edu_corpus = findViewById(R.id.txt_child_1_edu_corpus);
        ln_chart_child_child_1_edu_top = findViewById(R.id.ln_chart_child_child_1_edu_top);
        ln_chart_child_child_1_edu_buttom = findViewById(R.id.ln_chart_child_child_1_edu_buttom);
        txt_child_1_marriage_tar_amt = findViewById(R.id.txt_child_1_marriage_tar_amt);
        txt_child_1_marriage_gap = findViewById(R.id.txt_child_1_marriage_gap);
        txt_child_1_mrg_corpus = findViewById(R.id.txt_child_1_mrg_corpus);
        ln_chart_child_child_1_mrg_top = findViewById(R.id.ln_chart_child_child_1_mrg_top);
        ln_chart_child_child_1_mrg_buttom = findViewById(R.id.ln_chart_child_child_1_mrg_buttom);

        // child 2
        ln_child_2 = findViewById(R.id.ln_child_2);
        txt_child_2_tot_amt = findViewById(R.id.txt_child_2_tot_amt);
        txt_child_2_edu_tar_amount = findViewById(R.id.txt_child_2_edu_tar_amount);
        txt_child_2_edu_gap = findViewById(R.id.txt_child_2_edu_gap);
        txt_child_2_edu_corpus = findViewById(R.id.txt_child_2_edu_corpus);
        ln_chart_child_child_2_edu_top = findViewById(R.id.ln_chart_child_child_2_edu_top);
        ln_chart_child_child_2_edu_buttom = findViewById(R.id.ln_chart_child_child_2_edu_buttom);
        txt_child_2_marriage_tar_amt = findViewById(R.id.txt_child_2_marriage_tar_amt);
        txt_child_2_marriage_gap = findViewById(R.id.txt_child_2_marriage_gap);
        txt_child_2_mrg_corpus = findViewById(R.id.txt_child_2_mrg_corpus);
        ln_chart_child_child_2_mrg_top = findViewById(R.id.ln_chart_child_child_2_mrg_top);
        ln_chart_child_child_2_mrg_buttom = findViewById(R.id.ln_chart_child_child_2_mrg_buttom);

        // child 3
        ln_child_3 = findViewById(R.id.ln_child_3);
        txt_child_3_tot_amt = findViewById(R.id.txt_child_3_tot_amt);
        txt_child_3_edu_tar_amount = findViewById(R.id.txt_child_3_edu_tar_amount);
        txt_child_3_edu_gap = findViewById(R.id.txt_child_3_edu_gap);
        txt_child_3_edu_corpus = findViewById(R.id.txt_child_3_edu_corpus);
        ln_chart_child_child_3_edu_top = findViewById(R.id.ln_chart_child_child_3_edu_top);
        ln_chart_child_child_3_edu_buttom = findViewById(R.id.ln_chart_child_child_3_edu_buttom);
        txt_child_3_marriage_tar_amt = findViewById(R.id.txt_child_3_marriage_tar_amt);
        txt_child_3_marriage_gap = findViewById(R.id.txt_child_3_marriage_gap);
        txt_child_3_mrg_corpus = findViewById(R.id.txt_child_3_mrg_corpus);
        ln_chart_child_child_3_mrg_top = findViewById(R.id.ln_chart_child_child_3_mrg_top);
        ln_chart_child_child_3_mrg_buttom = findViewById(R.id.ln_chart_child_child_3_mrg_buttom);

        // child 4
        ln_child_4 = findViewById(R.id.ln_child_4);
        txt_child_4_tot_amt = findViewById(R.id.txt_child_4_tot_amt);
        txt_child_4_edu_tar_amount = findViewById(R.id.txt_child_4_edu_tar_amount);
        txt_child_4_edu_gap = findViewById(R.id.txt_child_4_edu_gap);
        txt_child_4_edu_corpus = findViewById(R.id.txt_child_4_edu_corpus);
        ln_chart_child_child_4_edu_top = findViewById(R.id.ln_chart_child_child_4_edu_top);
        ln_chart_child_child_4_edu_buttom = findViewById(R.id.ln_chart_child_child_4_edu_buttom);
        txt_child_4_marriage_tar_amt = findViewById(R.id.txt_child_4_marriage_tar_amt);
        txt_child_4_marriage_gap = findViewById(R.id.txt_child_4_marriage_gap);
        txt_child_4_mrg_corpus = findViewById(R.id.txt_child_4_mrg_corpus);
        ln_chart_child_child_4_mrg_top = findViewById(R.id.ln_chart_child_child_4_mrg_top);
        ln_chart_child_child_4_mrg_buttom = findViewById(R.id.ln_chart_child_child_4_mrg_buttom);

        // txt_child_smart_champ = (TextView)
        // findViewById(R.id.txt_child_smart_champ);
        // txt_child_scholar = (TextView) findViewById(R.id.txt_child_scholar);

        /* For Welth Creation */
        txt_wealth_creation = findViewById(R.id.txt_wealth_creation);

        txt_wealth_creation_gap = findViewById(R.id.txt_wealth_creation_gap);
        txt_wealth_creation_target = findViewById(R.id.txt_wealth_creation_target);
        txt_wealth_creation_corpus = findViewById(R.id.txt_wealth_creation_corpus);

        ln_chart_wealth_top = findViewById(R.id.ln_chart_wealth_top);
        ln_chart_wealth_buttom = findViewById(R.id.ln_chart_wealth_buttom);

        txt_wealth_smart_income_protect = findViewById(R.id.txt_wealth_smart_income_protect);
        txt_wealth_smart_guran_savings = findViewById(R.id.txt_wealth_smart_guran_savings);
        txt_wealth_smart_money_back = findViewById(R.id.txt_wealth_smart_money_back);
        txt_wealth_shubh_nivesh = findViewById(R.id.txt_wealth_shubh_nivesh);
        txt_wealth_saral_swadhan_plus = findViewById(R.id.txt_wealth_saral_swadhan_plus);
        txt_wealth_flexi_smart_plus = findViewById(R.id.txt_wealth_flexi_smart_plus);

        txt_wealth_smart_swadhan_plus = findViewById(R.id.txt_wealth_smart_swadhan_plus);
        txt_wealth_smart_swadhan_plus_ = findViewById(R.id.txt_wealth_smart_swadhan_plus_);

        txt_wealth_money_planner = findViewById(R.id.txt_wealth_money_planner);
        txt_humsafar = findViewById(R.id.txt_humsafar);
        txt_wealth_money_planner_ = findViewById(R.id.txt_wealth_money_planner_);
        txt_humsafar_ = findViewById(R.id.txt_humsafar_);

        //poorn suraksha added by rajan 28-11-2017
        txt_poorn_suraksha = findViewById(R.id.txt_poorn_suraksha);
        txt_poorn_suraksha_ = findViewById(R.id.txt_poorn_suraksha_);

        txt_wealth_saral_maha = findViewById(R.id.txt_wealth_saral_maha);
        txt_wealth_smart_elite = findViewById(R.id.txt_wealth_smart_elite);
        txt_wealth_smart_power_insu = findViewById(R.id.txt_wealth_smart_power_insu);
        txt_wealth_smart_scholar = findViewById(R.id.txt_wealth_smart_scholar);
        txt_wealth_smart_wealth_builder = findViewById(R.id.txt_wealth_smart_wealth_builder);
        txt_wealth_smart_wealth_assure = findViewById(R.id.txt_wealth_smart_wealth_assure);

        txt_wealth_smart_income_protect_ = findViewById(R.id.txt_wealth_smart_income_protect_);
        txt_wealth_smart_guran_savings_ = findViewById(R.id.txt_wealth_smart_guran_savings_);
        txt_wealth_smart_money_back_ = findViewById(R.id.txt_wealth_smart_money_back_);
        txt_wealth_shubh_nivesh_ = findViewById(R.id.txt_wealth_shubh_nivesh_);
        txt_wealth_saral_swadhan_plus_ = findViewById(R.id.txt_wealth_saral_swadhan_plus_);
        txt_wealth_flexi_smart_plus_ = findViewById(R.id.txt_wealth_flexi_smart_plus_);

        txt_wealth_saral_maha_ = findViewById(R.id.txt_wealth_saral_maha_);
        txt_wealth_smart_elite_ = findViewById(R.id.txt_wealth_smart_elite_);
        txt_wealth_smart_power_insu_ = findViewById(R.id.txt_wealth_smart_power_insu_);
        txt_wealth_smart_scholar_ = findViewById(R.id.txt_wealth_smart_scholar_);
        txt_wealth_smart_wealth_builder_ = findViewById(R.id.txt_wealth_smart_wealth_builder_);
        txt_wealth_smart_wealth_assure_ = findViewById(R.id.txt_wealth_smart_wealth_assure_);

        ln_trad_ulip = findViewById(R.id.ln_trad_ulip);
        ln_ulip_trad = findViewById(R.id.ln_ulip_trad);

        // String str = "Conservative";

        txt_summary_protection = findViewById(R.id.txt_summary_protection);
        txt_wealth_creation_summary = findViewById(R.id.txt_wealth_creation_summary);
        txt_summary_retirement = findViewById(R.id.txt_summary_retirement);

        txt_protection = findViewById(R.id.txt_protection);

        txt_protection_gap = findViewById(R.id.txt_protection_gap);
        txt_protection_target_amt = findViewById(R.id.txt_protection_target_amt);
        txt_protection_corpus = findViewById(R.id.txt_protection_corpus);

        ln_chart_protection_top = findViewById(R.id.ln_chart_protection_top);
        ln_chart_protection_buttom = findViewById(R.id.ln_chart_protection_buttom);

        txt_pro_smart_shield = findViewById(R.id.txt_pro_smart_shield);
        txt_pro_saral_shield = findViewById(R.id.txt_pro_saral_shield);
        txt_pro_eshield = findViewById(R.id.txt_pro_eshield);
        txt_pro_grameen_bima = findViewById(R.id.txt_pro_grameen_bima);
        // img_chart_protection_top_side = (ImageView)
        // findViewById(R.id.img_chart_protection_top_side);
        // img_chart_retirement_top_side = (ImageView)
        // findViewById(R.id.img_chart_retirement_top_side);
        // img_chart_child_top_side = (ImageView)
        // findViewById(R.id.img_chart_child_top_side);
        // img_chart_wealth_top_side = (ImageView)
        // findViewById(R.id.img_chart_wealth_top_side);
        // img_protection_top_side = (ImageView)
        // findViewById(R.id.img_protection_top_side);

        txt_pro_gap = findViewById(R.id.txt_pro_gap);
        txt_pro_target_amt = findViewById(R.id.txt_pro_target_amt);
        txt_pro_corpus = findViewById(R.id.txt_pro_corpus);
        // img_pro_top_side = (ImageView) findViewById(R.id.img_pro_top_side);
        ln_chart_pro_top = findViewById(R.id.ln_chart_pro_top);
        ln_chart_pro_buttom = findViewById(R.id.ln_chart_pro_buttom);

        txt_retire_gap = findViewById(R.id.txt_retire_gap);
        txt_retire_target_amt = findViewById(R.id.txt_retire_target_amt);
        txt_retire_corpus = findViewById(R.id.txt_retire_corpus);
        // img_retire_top_side = (ImageView)
        // findViewById(R.id.img_retire_top_side);
        ln_chart_retire_top = findViewById(R.id.ln_chart_retire_top);
        ln_chart_retire_buttom = findViewById(R.id.ln_chart_retire_buttom);

        txt_wealthcreation_gap = findViewById(R.id.txt_wealthcreation_gap);
        txt_wealthcreation_target = findViewById(R.id.txt_wealthcreation_target);
        txt_wealthcreation_corpus = findViewById(R.id.txt_wealthcreation_corpus);
        // img_wealthcreation_top_side = (ImageView)
        // findViewById(R.id.img_wealthcreation_top_side);
        ln_chart_wealthcreation_top = findViewById(R.id.ln_chart_wealthcreation_top);
        ln_wealthcreation_buttom = findViewById(R.id.ln_chart_wealthcreation_buttom);

        txt_wealth_creation = findViewById(R.id.txt_wealth_creation);

        txt_wealth_creation_gap = findViewById(R.id.txt_wealth_creation_gap);
        txt_wealth_creation_target = findViewById(R.id.txt_wealth_creation_target);
        txt_wealth_creation_corpus = findViewById(R.id.txt_wealth_creation_corpus);

        ln_chart_wealth_top = findViewById(R.id.ln_chart_wealth_top);
        ln_chart_wealth_buttom = findViewById(R.id.ln_chart_wealth_buttom);

        txt_wealth_smart_income_protect = findViewById(R.id.txt_wealth_smart_income_protect);
        txt_wealth_smart_guran_savings = findViewById(R.id.txt_wealth_smart_guran_savings);
        txt_wealth_smart_money_back = findViewById(R.id.txt_wealth_smart_money_back);
        txt_wealth_shubh_nivesh = findViewById(R.id.txt_wealth_shubh_nivesh);
        txt_wealth_saral_swadhan_plus = findViewById(R.id.txt_wealth_saral_swadhan_plus);
        txt_wealth_flexi_smart_plus = findViewById(R.id.txt_wealth_flexi_smart_plus);

        txt_wealth_saral_maha = findViewById(R.id.txt_wealth_saral_maha);
        txt_wealth_smart_elite = findViewById(R.id.txt_wealth_smart_elite);
        txt_wealth_smart_power_insu = findViewById(R.id.txt_wealth_smart_power_insu);
        txt_wealth_smart_scholar = findViewById(R.id.txt_wealth_smart_scholar);
        txt_wealth_smart_wealth_builder = findViewById(R.id.txt_wealth_smart_wealth_builder);
        txt_wealth_smart_wealth_assure = findViewById(R.id.txt_wealth_smart_wealth_assure);

        txt_wealth_smart_income_protect_ = findViewById(R.id.txt_wealth_smart_income_protect_);
        txt_wealth_smart_guran_savings_ = findViewById(R.id.txt_wealth_smart_guran_savings_);
        txt_wealth_smart_money_back_ = findViewById(R.id.txt_wealth_smart_money_back_);
        txt_wealth_shubh_nivesh_ = findViewById(R.id.txt_wealth_shubh_nivesh_);
        txt_wealth_saral_swadhan_plus_ = findViewById(R.id.txt_wealth_saral_swadhan_plus_);
        txt_wealth_flexi_smart_plus_ = findViewById(R.id.txt_wealth_flexi_smart_plus_);

        txt_wealth_saral_maha_ = findViewById(R.id.txt_wealth_saral_maha_);
        txt_wealth_smart_elite_ = findViewById(R.id.txt_wealth_smart_elite_);
        txt_wealth_smart_power_insu_ = findViewById(R.id.txt_wealth_smart_power_insu_);
        txt_wealth_smart_scholar_ = findViewById(R.id.txt_wealth_smart_scholar_);
        txt_wealth_smart_wealth_builder_ = findViewById(R.id.txt_wealth_smart_wealth_builder_);
        txt_wealth_smart_wealth_assure_ = findViewById(R.id.txt_wealth_smart_wealth_assure_);

        ln_trad_ulip = findViewById(R.id.ln_trad_ulip);
        ln_ulip_trad = findViewById(R.id.ln_ulip_trad);
        // txt_ret_saral_pension = (TextView)
        // findViewById(R.id.txt_ret_saral_pension);
        // txt_ret_retire_smart = (TextView)
        // findViewById(R.id.txt_ret_retire_smart);
        // txt_ret_annuity_plus = (TextView)
        // findViewById(R.id.txt_ret_annuity_plus);

        /*
         * img_chart_child_1_edu_top_side = (ImageView)
         * findViewById(R.id.img_chart_child_1_edu_top_side);
         * img_chart_child_1_mrg_top_side = (ImageView)
         * findViewById(R.id.img_chart_child_1_mrg_top_side);
         * img_chart_child_2_edu__top_side = (ImageView)
         * findViewById(R.id.img_chart_child_2_edu__top_side);
         * img_chart_child_2_mrg_top_side = (ImageView)
         * findViewById(R.id.img_chart_child_2_mrg_top_side);
         * img_chart_child_3_edu_top_side = (ImageView)
         * findViewById(R.id.img_chart_child_3_edu_top_side);
         * img_chart_child_3_mrg_top_side = (ImageView)
         * findViewById(R.id.img_chart_child_3_mrg_top_side);
         * img_chart_child_4_edu_top_side = (ImageView)
         * findViewById(R.id.img_chart_child_4_edu_top_side);
         * img_chart_child_4_mrg_top_side = (ImageView)
         * findViewById(R.id.img_chart_child_4_mrg_top_side);
         */

        vw_wealth_top_side = findViewById(R.id.vw_wealthcreation_top_side);
        vw_retire_top_side = findViewById(R.id.vw_retire_top_side);
        vw_pro_top_side = findViewById(R.id.vw_pro_top_side);
        vw_chart_protection_top_side = findViewById(R.id.vw_chart_protection_top_side);
        vw_chart_retirement_top_side = findViewById(R.id.vw_chart_retirement_top_side);
        vw_chart_child_top_side = findViewById(R.id.vw_chart_child_top_side);
        vw_chart_wealth_top_side = findViewById(R.id.vw_chart_wealth_top_side);

        tv_mand_corpus_home = findViewById(R.id.tv_mand_corpus_home);
        tv_mand_corpus_other = findViewById(R.id.tv_mand_corpus_other);
        tv_mand_cost_other = findViewById(R.id.tv_mand_cost_other);
        tv_mand_cost_home = findViewById(R.id.tv_mand_cost_home);

        iv_chid_side_arrow = findViewById(R.id.iv_chid_side_arrow);
        ll_btn_child_header = findViewById(R.id.ll_btn_child_header);

        ll_wealth_builder = findViewById(R.id.ll_wealth_builder);
        // ll_wealth_builder_ = (LinearLayout)
        // findViewById(R.id.ll_wealth_builder_);

        ll_retirement_wealth = findViewById(R.id.ll_retirement_wealth);

        txt_wealth_creation_other = findViewById(R.id.txt_wealth_creation_other);
        txt_wealthcreation_gap_other = findViewById(R.id.txt_wealthcreation_gap_other);
        txt_wealthcreation_corpus_other = findViewById(R.id.txt_wealthcreation_corpus_other);
        txt_wealthcreation_target_other = findViewById(R.id.txt_wealthcreation_target_other);
        vw_wealthcreation_top_side_other = findViewById(R.id.vw_wealthcreation_top_side_other);
        ln_chart_wealthcreation_top_other = findViewById(R.id.ln_chart_wealthcreation_top_other);
        ln_chart_wealthcreation_buttom_other = findViewById(R.id.ln_chart_wealthcreation_buttom_other);

        ln_trad_ulip_child = findViewById(R.id.ln_trad_ulip_child);
        ln_ulip_trad_child = findViewById(R.id.ln_ulip_trad_child);
        txt_smart_champ_trad_ulip_child = findViewById(R.id.txt_smart_champ_trad_ulip_child);
        txt_smart_scholar_trad_ulip_child = findViewById(R.id.txt_smart_scholar_trad_ulip_child);
        txt_smart_scholar_ulip_trad_child = findViewById(R.id.txt_smart_scholar_ulip_trad_child);
        txt_smart_champ_ulip_trad_child = findViewById(R.id.txt_smart_champ_ulip_trad_child);

        ln_trad_ulip_retirement = findViewById(R.id.ln_trad_ulip_retirement);
        ln_ulip_trad_retirement = findViewById(R.id.ln_ulip_trad_retirement);
        txt_saral_pension_trad_ulip_retirement = findViewById(R.id.txt_saral_pension_trad_ulip_retirement);
        txt_annuity_plus_trad_ulip_retirement = findViewById(R.id.txt_annuity_plus_trad_ulip_retirement);
        txt_retire_smart_trad_ulip_retirement = findViewById(R.id.txt_retire_smart_trad_ulip_retirement);
        txt_retire_smart_ulip_trad_retirement = findViewById(R.id.txt_retire_smart_ulip_trad_retirement);
        txt_saral_pension_ulip_trad_retirement = findViewById(R.id.txt_saral_pension_ulip_trad_retirement);
        txt_annuity_plus_ulip_trad_retirement = findViewById(R.id.txt_annuity_plus_ulip_trad_retirement);

        vw_child_1_edu_top_side = findViewById(R.id.vw_child_1_edu_top_side);
        vw_child_1_mrg_top_side = findViewById(R.id.vw_child_1_mrg_top_side);
        vw_child_2_edu_top_side = findViewById(R.id.vw_child_2_edu_top_side);
        vw_child_2_mrg_top_side = findViewById(R.id.vw_child_2_mrg_top_side);
        vw_child_3_edu_top_side = findViewById(R.id.vw_child_3_edu_top_side);
        vw_child_3_mrg_top_side = findViewById(R.id.vw_child_3_mrg_top_side);
        vw_child_4_edu_top_side = findViewById(R.id.vw_child_4_edu_top_side);
        vw_child_4_mrg_top_side = findViewById(R.id.vw_child_4_mrg_top_side);

        tv_child1_mrg = findViewById(R.id.tv_child1_mrg);
        tv_child2_mrg = findViewById(R.id.tv_child2_mrg);
        tv_child3_mrg = findViewById(R.id.tv_child3_mrg);
        tv_child4_mrg = findViewById(R.id.tv_child4_mrg);
        tv_child1_edu = findViewById(R.id.tv_child1_edu);
        tv_child2_edu = findViewById(R.id.tv_child2_edu);
        tv_child3_edu = findViewById(R.id.tv_child3_edu);
        tv_child4_edu = findViewById(R.id.tv_child4_edu);

        txt_child1_edu_text_line = findViewById(R.id.txt_child1_edu_text_line);
        txt_child2_edu_text_line = findViewById(R.id.txt_child2_edu_text_line);
        txt_child3_edu_text_line = findViewById(R.id.txt_child3_edu_text_line);
        txt_child4_edu_text_line = findViewById(R.id.txt_child4_edu_text_line);
        txt_child1_mrg_text_line = findViewById(R.id.txt_child1_mrg_text_line);
        txt_child2_mrg_text_line = findViewById(R.id.txt_child2_mrg_text_line);
        txt_child3_mrg_text_line = findViewById(R.id.txt_child3_mrg_text_line);
        txt_child4_mrg_text_line = findViewById(R.id.txt_child4_mrg_text_line);
        txt_wealth_home_text_line = findViewById(R.id.txt_wealth_home_text_line);
        txt_wealth_other_text_line = findViewById(R.id.txt_wealth_other_text_line);
        txt_retirement_text_line = findViewById(R.id.txt_retirement_text_line);

        txt_star_def = findViewById(R.id.txt_star_def);
        txt_hash_def = findViewById(R.id.txt_hash_def);

        lv_remaining_prod = findViewById(R.id.lv_remaining_prod);
        lv_suggested_prod = findViewById(R.id.lv_suggested_prod);
        btn_Date = findViewById(R.id.btn_Date);
        Ibtn_signatureofIntermediary = findViewById(R.id.Ibtn_signatureofintermediary);
        Ibtn_signatureofCustomer = findViewById(R.id.Ibtn_signatureofcustomer);
        suggested_prod_list = new ArrayList<>();
        remaining_product_list = new ArrayList<>();
        protection_prod_list = new ArrayList<>();
        wealth_prod_list = new ArrayList<>();
        child_prod_list = new ArrayList<>();
        retirement_prod_list = new ArrayList<>();
        chosen_prod_list = new ArrayList<>();
        ulip_prod_list = new ArrayList<>();
        traditional_prod_list = new ArrayList<>();
        other_chosen_prod_list = new ArrayList<>();
        sugg_chosen_prod_list = new ArrayList<>();
    }

    private void FillSpinner() {

        group_list.add("Select");
        group_list.add("SBI/Retail");
        group_list.add("Other");
        mCommonMethods.fillSpinnerValue(context, spnr_group, group_list);

        children_list.add("Select");
        children_list.add("0");
        children_list.add("1");
        children_list.add("2");
        children_list.add("3");
        children_list.add("4");
        mCommonMethods.fillSpinnerValue(context, spnr_children, children_list);

        inflation_list.add("Select");
        inflation_list.add("4%");
        inflation_list.add("5%");
        inflation_list.add("6%");
        inflation_list.add("7%");
        inflation_list.add("8%");
        mCommonMethods.fillSpinnerValue(context, spnr_inflation_assumed, inflation_list);

        investment_approch_list.add("Select");
        investment_approch_list.add("Conservative");
        investment_approch_list.add("Moderate");
        investment_approch_list.add("Aggressive");
        mCommonMethods.fillSpinnerValue(context, spnr_invesment_approch, investment_approch_list);
    }

    private void remainingProductFillSpinner() {
        int flag = 0;
        ArrayList<SuggestedProdList> product_code_list = new ArrayList<>();

        product_code_list.add(new SuggestedProdList("1K", "UIN : 111L095V03",
                "Smart Wealth Builder", false));
        product_code_list.add(new SuggestedProdList("53", "UIN : 111L072V04",
                "Smart Elite", false));
        product_code_list.add(new SuggestedProdList("35", "UIN : 111N055V04",
                "Shubh Nivesh", false));
        /*product_code_list.add(new SuggestedProdList("1M", "UIN : 111N093V01",
                "Flexi Smart Plus", false));
        product_code_list.add(new SuggestedProdList("1X", "UIN : 111N097V01",
                "Smart Guaranteed Savings Plan", false));

        product_code_list.add(new SuggestedProdList("2C", "UIN : 111N106V01",
                "Smart Women Advantage", false));*/
        product_code_list.add(new SuggestedProdList("45", "UIN : 111N067V07",
                "Smart Shield", false));
        product_code_list.add(new SuggestedProdList("51", "UIN : 111L073V03",
                "Smart Scholar", false));
        product_code_list.add(new SuggestedProdList("1N", "UIN : 111N096V03",
                "Smart Money Back Gold", false));
        product_code_list.add(new SuggestedProdList("1P", "UIN : 111N098V03",
                "Smart Champ Insurance", false));
        product_code_list.add(new SuggestedProdList("1H", "UIN : 111L094V02",
                "Retire Smart", false));
        /*product_code_list.add(new SuggestedProdList("47", "UIN : 111N066V03",
                "Saral Shield", false));*/
        product_code_list.add(new SuggestedProdList("1B", "UIN : 111N085V04",
                "Smart Income Protect", false));

        /*product_code_list.add(new SuggestedProdList("50", "UIN : 111L070V02",
                "Saral Maha Anand", false));*/
        product_code_list.add(new SuggestedProdList("55", "UIN : 111L077V03",
                "Smart Wealth Assure", false));
        product_code_list.add(new SuggestedProdList("1E", "UIN : 111N088V03",
                "Saral Retirement Saver", false));
        product_code_list.add(new SuggestedProdList("1C", "UIN : 111L090V02",
                "Smart Power Insurance", false));
        product_code_list.add(new SuggestedProdList("1J", "UIN : 111N092V03",
                "Saral Swadhan+", false));
        product_code_list.add(new SuggestedProdList("1Z", "UIN : 111N104V02",
                "Smart Swadhan Plus", false));
        product_code_list.add(new SuggestedProdList("1R", "UIN : 111N101V03",
                "Smart Money Planner", false));
        product_code_list.add(new SuggestedProdList("1W", "UIN : 111N103V03",
                "Smart Humsafar", false));

        product_code_list.add(new SuggestedProdList("2B", "UIN - 111L107V03",
                "Smart Privilege", false));

        // ***** Added by Priyanka Warekar - 11-01-2017 - Start ****///
        product_code_list.add(new SuggestedProdList("2D", "UIN : 111N108V03",
                "Smart Bachat", false));
        product_code_list.add(new SuggestedProdList("2E", "UIN : 111N109V03",
                "Sampoorn Cancer Suraksha", false));

        /*added by rajan 28-11-2017*/
        product_code_list.add(new SuggestedProdList("2F", "UIN : 111N110V03",
                "Poorna Suraksha", false));

		/*product_code_list.add(new SuggestedProdList(getString(R.string.sbi_life_smart_samriddhi_code),
				"UIN : "+getString(R.string.sbi_life_smart_samriddhi_uin),
				getString(R.string.sbi_life_smart_samriddhi), false));*/

        product_code_list.add(new SuggestedProdList(getString(R.string.sbi_life_saral_insure_wealth_plus_code),
                "UIN : " + getString(R.string.sbi_life_saral_insure_wealth_plus_uin),
                getString(R.string.sbi_life_saral_insure_wealth_plus), false));

        product_code_list.add(new SuggestedProdList(getString(R.string.sbi_life_smart_insure_wealth_plus_code),
                "UIN : " + getString(R.string.sbi_life_smart_insure_wealth_plus_uin),
                getString(R.string.sbi_life_smart_insure_wealth_plus), false));

        product_code_list.add(new SuggestedProdList(getString(R.string.sbi_life_smart_platina_assure_code),
                "UIN : " + getString(R.string.sbi_life_smart_platina_assure_uin),
                getString(R.string.sbi_life_smart_platina_assure), false));

        product_code_list.add(new SuggestedProdList(getString(R.string.sbi_life_annuity_plus_code),
                "UIN : " + getString(R.string.sbi_life_annuity_plus_uin),
                getString(R.string.sbi_life_annuity_plus), false));

        product_code_list.add(new SuggestedProdList(getString(R.string.sbi_life_smart_future_choices_code),
                "UIN : " + getString(R.string.sbi_life_smart_future_choices_uin),
                getString(R.string.sbi_life_smart_future_choices), false));

        product_code_list.add(new SuggestedProdList(getString(R.string.sbi_life_saral_jeevan_bima_code),
                "UIN : " + getString(R.string.sbi_life_saral_jeevan_bima_uin),
                getString(R.string.sbi_life_saral_jeevan_bima), false));

        product_code_list.add(new SuggestedProdList(getString(R.string.sbi_life_new_smart_samriddhi_code),
                "UIN : " + getString(R.string.sbi_life_new_smart_samriddhi_uin),
                getString(R.string.sbi_life_new_smart_samriddhi), false));

        product_code_list.add(new SuggestedProdList(getString(R.string.sbi_life_saral_pension_new_code),
                "UIN : " + getString(R.string.sbi_life_saral_pension_new_uin),
                getString(R.string.sbi_life_saral_pension_new), false));

    product_code_list.add(new SuggestedProdList(getString(R.string.sbi_life_eshield_next_code),
                "UIN : " + getString(R.string.sbi_life_eshield_next_uin),
                getString(R.string.sbi_life_eshield_next), false));


        product_code_list.add(new SuggestedProdList(getString(R.string.sbi_life_smart_platina_plus_code),
                "UIN : " + getString(R.string.sbi_life_smart_platina_plus_uin),
                getString(R.string.sbi_life_smart_platina_plus), false));

        if (suggested_prod_list.size() != 0) {
            for (int i = 0; i < product_code_list.size(); i++) {
                flag = 0;
                for (int j = 0; j < suggested_prod_list.size(); j++) {
                    System.out.println(product_code_list.get(i)
                            .getProduct_Code()
                            + "    "
                            + suggested_prod_list.get(j).getProduct_Code());
                    if (product_code_list
                            .get(i)
                            .getProduct_Code()
                            .equalsIgnoreCase(
                                    suggested_prod_list.get(j)
                                            .getProduct_Code())) {
                        flag = 1;
                        // break;
                    }
                }
                if (flag == 0)
                    remaining_product_list.add(product_code_list.get(i));
            }
        }
    }

    private int getIndex(Spinner s1, String value) {

        int index = 0;

        for (int i = 0; i < s1.getCount(); i++) {
            if (s1.getItemAtPosition(i).equals(value)) {
                index = i;
            }
        }
        return index;
    }

    private void OnListener() {
        switch_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    str_gender = "F";
                    iv_women.setImageDrawable(getResources().getDrawable(
                            R.drawable.women_clicked));
                    iv_men.setImageDrawable(getResources().getDrawable(
                            R.drawable.men_nonclicked));

                } else if (!isChecked) {
                    str_gender = "M";
                    iv_men.setImageDrawable(getResources().getDrawable(
                            R.drawable.men_clicked));
                    iv_women.setImageDrawable(getResources().getDrawable(
                            R.drawable.women_nonclicked));
                }

            }

        });
        switch_marital_status
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        if (isChecked) {
                            str_marital_status = "Married";

                            iv_single.setImageDrawable(getResources()
                                    .getDrawable(R.drawable.single_nonclicked));
                            iv_married.setImageDrawable(getResources()
                                    .getDrawable(R.drawable.married_clicked));

                        } else if (!isChecked) {
                            str_marital_status = "Single";
                            iv_single.setImageDrawable(getResources()
                                    .getDrawable(R.drawable.single_clicked));
                            iv_married
                                    .setImageDrawable(getResources()
                                            .getDrawable(
                                                    R.drawable.married_nonclicked));

                        }

                    }

                });

        sb_monthly_income
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_monthly_income.setText("");
                            edt_yearly_income.setText("");
                        } else {

                            progress = progress / 2000;
                            progress = progress * 2000;
                            edt_monthly_income.setText(String.valueOf(progress));
                            int yearly = progress * 12;
                            edt_yearly_income.setText(String.valueOf(yearly));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
        sb_monthly_income_with_emi
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_monthly_income_with_emi.setText("");
                            edt_yearly_income_with_emi.setText("");
                        } else {
                            progress = progress / 2000;
                            progress = progress * 2000;
                            edt_monthly_income_with_emi.setText(String
                                    .valueOf(progress));
                            int yearly = progress * 12;
                            edt_yearly_income_with_emi.setText(String
                                    .valueOf(yearly));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_current_life_insurance_coverage
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_current_life_insurance_coverage.setText("");
                        } else {
                            progress = progress / 2000;
                            progress = progress * 2000;
                            edt_current_life_insurance_coverage.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_outstanding_home_loan
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_outstanding_home_loan.setText("");
                        } else {
                            progress = progress / 10000;
                            progress = progress * 10000;
                            edt_outstanding_home_loan.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
        sb_outstanding_home_loan_other
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_outstanding_home_loan_other.setText("");
                        } else {
                            progress = progress / 5000;
                            progress = progress * 5000;
                            edt_outstanding_home_loan_other.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        spnr_children.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position > 0) {
                    str_no_of_child = children_list.get(position);
                } else {
                    str_no_of_child = "0";
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnr_group.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
                if (position > 0) {
                    str_group = group_list.get(position);
                } else {
                    str_group = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spnr_inflation_assumed
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        if (position > 0) {
                            str_inflation_assumed = inflation_list
                                    .get(position);
                        } else {
                            str_inflation_assumed = "";
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        spnr_invesment_approch
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        if (position > 0) {
                            str_investment_approach = investment_approch_list
                                    .get(position);
                        } else {
                            str_investment_approach = "";
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        // .setOnItemSelectedListener(new OnItemSelectedListener() {
        //
        // public void onItemSelected(AdapterView<?> parent,
        // View view, int position, long id) {
        // if(
        // {
        // if(remaining_product_selected!=null)
        // {
        //
        // chosen_prod_list.remove(remaining_product_selected_index);
        // }
        // }
        // else
        // {
        // if(remaining_product_selected!=null)
        // {
        // chosen_prod_list.remove(remaining_product_selected_index);
        // }
        // System.out.println(" remaining_product_selected : "+remaining_product_selected);
        //
        // SuggestedProdList item=new SuggestedProdList("",
        // remaining_product_selected,true);
        // chosen_prod_list.add(item);
        // remaining_product_selected_index=chosen_prod_list.indexOf(item);
        // System.out.println(" index add : "+remaining_product_selected_index);
        // if(chosen_prod_list.size()<=3)
        // {
        // updateProductChosenList();
        // }
        // else
        // {
        // Toast.makeText(context,
        // "Not Allowed! You can choose maximum 3 Products!",
        // Toast.LENGTH_LONG).show();
        // chosen_prod_list.remove(item);
        // updateProductChosenList();
        // }
        // }
        // }
        //
        //
        // public void onNothingSelected(AdapterView<?> parent) {
        //
        // }
        // });

        sb_retirement.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar thumbRadiusSeek,
                                          int progress, boolean fromUser) {
                if (progress == 0) {
                    edt_retirement.setText("");
                } else {
                    progress = progress / 2000;
                    progress = progress * 2000;
                    edt_retirement.setText(String.valueOf(progress));

                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        rg_post_reitrement
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.rb_retirement_basic) {
                            strRetirementLifeStyle = "Basic";
                        } else if (checkedId == R.id.rb_retirement_comfortable) {
                            strRetirementLifeStyle = "Comfortable";
                        } else if (checkedId == R.id.rb_retirement_luxury) {
                            strRetirementLifeStyle = "Luxury";
                        } else {
                        }
                    }

                });

        sb_child1_marrige_current_cost
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child1_marriage_current_cost.setText("");
                        } else {
                            progress = progress / 100000;
                            progress = progress * 100000;
                            edt_child1_marriage_current_cost.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_child2_marrige_current_cost
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child2_marriage_current_cost.setText("");
                        } else {
                            progress = progress / 100000;
                            progress = progress * 100000;
                            edt_child2_marriage_current_cost.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
        sb_child3_marrige_current_cost
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child3_marriage_current_cost.setText("");
                        } else {
                            progress = progress / 100000;
                            progress = progress * 100000;
                            edt_child3_marriage_current_cost.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
        sb_child4_marrige_current_cost
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child4_marriage_current_cost.setText("");
                        } else {
                            progress = progress / 100000;
                            progress = progress * 100000;
                            edt_child4_marriage_current_cost.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_wealth_creation_corpus_for_home
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_wealth_creation_corpus_for_home.setText("");
                        } else {
                            progress = progress / 50000;
                            progress = progress * 50000;
                            edt_wealth_creation_corpus_for_home.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
        sb_wealth_creation_corpus_for_other
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_wealth_creation_corpus_for_other.setText("");
                        } else {
                            progress = progress / 50000;
                            progress = progress * 50000;
                            edt_wealth_creation_corpus_for_other.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
        btn_retirement_plus.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                int_retirement++;
                if (int_retirement >= 0) {
                    edt_retirment_no_of_years.setText(Integer
                            .toString(int_retirement));
                }
            }
        });
        btn_retirement_minus.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                int_retirement--;
                if (int_retirement >= 0) {
                    edt_retirment_no_of_years.setText(Integer
                            .toString(int_retirement));
                }
            }
        });

        btn_child1_age_plus.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                int_child1Age++;
                if (int_child1Age >= 0) {
                    edt_child1_age.setText(Integer.toString(int_child1Age));
                }
            }
        });
        btn_child1_age_minus.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                int_child1Age--;
                if (int_child1Age >= 0) {
                    edt_child1_age.setText(Integer.toString(int_child1Age));
                }
            }
        });
        btn_child1_marriage_fund_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child1_marriage_fund++;
                        if (int_child1_marriage_fund >= 0) {
                            edt_child1_marriage_fund.setText(Integer
                                    .toString(int_child1_marriage_fund));
                        }
                    }
                });
        btn_child1_marriage_fund_minus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child1_marriage_fund--;
                        if (int_child1_marriage_fund >= 0) {
                            edt_child1_marriage_fund.setText(Integer
                                    .toString(int_child1_marriage_fund));
                        }
                    }
                });
        btn_child1_education_fund_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child1_education_fund++;
                        if (int_child1_education_fund >= 0) {
                            edt_child1_education_fund.setText(Integer
                                    .toString(int_child1_education_fund));
                        }
                    }
                });
        btn_child1_education_fund_minus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child1_education_fund--;
                        if (int_child1_education_fund >= 0) {
                            edt_child1_education_fund.setText(Integer
                                    .toString(int_child1_education_fund));
                        }
                    }
                });

        sb_child1_education_corpus
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child1_education_corpus.setText("");
                        } else {
                            progress = progress / 50000;
                            progress = progress * 50000;
                            edt_child1_education_corpus.setText(String
                                    .valueOf(progress));

                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_child1_education_current_cost
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child1_education_current_cost.setText("");
                        } else {
                            progress = progress / 100000;
                            progress = progress * 100000;
                            edt_child1_education_current_cost.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_child1_marriage_corpus
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child1_marriage_corpus.setText("");
                        } else {
                            progress = progress / 50000;
                            progress = progress * 50000;
                            edt_child1_marriage_corpus.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        btn_child2_age_plus.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                int_child2Age++;
                if (int_child2Age >= 0) {
                    edt_child2_age.setText(Integer.toString(int_child2Age));
                }
            }
        });
        btn_child2_age_minus.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                int_child2Age--;
                if (int_child2Age >= 0) {
                    edt_child2_age.setText(Integer.toString(int_child2Age));
                }
            }
        });
        btn_child2_marriage_fund_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child2_marriage_fund++;
                        if (int_child2_marriage_fund >= 0) {
                            edt_child2_marriage_fund.setText(Integer
                                    .toString(int_child2_marriage_fund));
                        }
                    }
                });
        btn_child2_marriage_fund_minus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child2_marriage_fund--;

                        if (int_child2_marriage_fund >= 0) {
                            edt_child2_marriage_fund.setText(Integer
                                    .toString(int_child2_marriage_fund));
                        }
                    }
                });
        btn_child2_education_fund_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child2_education_fund++;
                        if (int_child2_education_fund >= 0) {
                            edt_child2_education_fund.setText(Integer
                                    .toString(int_child2_education_fund));
                        }
                    }
                });
        btn_child2_education_fund_minus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child2_education_fund--;
                        if (int_child2_education_fund >= 0) {
                            edt_child2_education_fund.setText(Integer
                                    .toString(int_child2_education_fund));
                        }
                    }
                });

        sb_child2_education_corpus
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child2_education_corpus.setText("");
                        } else {
                            progress = progress / 50000;
                            progress = progress * 50000;
                            edt_child2_education_corpus.setText(String
                                    .valueOf(progress));

                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_child2_education_current_cost
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child2_education_current_cost.setText("");
                        } else {
                            progress = progress / 100000;
                            progress = progress * 100000;
                            edt_child2_education_current_cost.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_child2_marriage_corpus
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child2_marriage_corpus.setText("");
                        } else {
                            progress = progress / 50000;
                            progress = progress * 50000;
                            edt_child2_marriage_corpus.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        btn_child3_age_plus.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                int_child3Age++;
                if (int_child3Age >= 0) {
                    edt_child3_age.setText(Integer.toString(int_child3Age));
                }
            }
        });
        btn_child3_age_minus.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                int_child3Age--;
                if (int_child3Age >= 0) {
                    edt_child3_age.setText(Integer.toString(int_child3Age));
                }
            }
        });
        btn_child3_marriage_fund_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child3_marriage_fund++;
                        if (int_child3_marriage_fund >= 0) {
                            edt_child3_marriage_fund.setText(Integer
                                    .toString(int_child3_marriage_fund));
                        }
                    }
                });
        btn_child3_marriage_fund_minus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child3_marriage_fund--;
                        if (int_child3_marriage_fund >= 0) {
                            edt_child3_marriage_fund.setText(Integer
                                    .toString(int_child3_marriage_fund));
                        }
                    }
                });
        btn_child3_education_fund_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child3_education_fund++;
                        if (int_child3_education_fund >= 0) {
                            edt_child3_education_fund.setText(Integer
                                    .toString(int_child3_education_fund));
                        }
                    }
                });
        btn_child3_education_fund_minus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child3_education_fund--;
                        if (int_child3_education_fund >= 0) {
                            edt_child3_education_fund.setText(Integer
                                    .toString(int_child3_education_fund));
                        }
                    }
                });

        sb_child3_education_corpus
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child3_education_corpus.setText("");
                        } else {
                            progress = progress / 50000;
                            progress = progress * 50000;
                            edt_child3_education_corpus.setText(String
                                    .valueOf(progress));

                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_child3_education_current_cost
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child3_education_current_cost.setText("");
                        } else {
                            progress = progress / 100000;
                            progress = progress * 100000;
                            edt_child3_education_current_cost.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_child3_marriage_corpus
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child3_marriage_corpus.setText("");
                        } else {
                            progress = progress / 50000;
                            progress = progress * 50000;
                            edt_child3_marriage_corpus.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        btn_child4_age_plus.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                int_child4Age++;
                if (int_child4Age >= 0) {
                    edt_child4_age.setText(Integer.toString(int_child4Age));
                }
            }
        });
        btn_child4_age_minus.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                int_child4Age--;
                if (int_child4Age >= 0) {
                    edt_child4_age.setText(Integer.toString(int_child4Age));
                }
            }
        });
        btn_child4_marriage_fund_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child4_marriage_fund++;
                        edt_child4_marriage_fund.setText(Integer
                                .toString(int_child4_marriage_fund));
                    }
                });
        btn_child4_marriage_fund_minus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child4_marriage_fund--;
                        edt_child4_marriage_fund.setText(Integer
                                .toString(int_child4_marriage_fund));
                    }
                });
        btn_child4_education_fund_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child4_education_fund++;
                        edt_child4_education_fund.setText(Integer
                                .toString(int_child4_education_fund));
                    }
                });
        btn_child4_education_fund_minus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child4_education_fund--;
                        edt_child4_education_fund.setText(Integer
                                .toString(int_child4_education_fund));
                    }
                });

        sb_child4_education_corpus
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child4_education_corpus.setText("");
                        } else {
                            progress = progress / 50000;
                            progress = progress * 50000;

                            edt_child4_education_corpus.setText(String
                                    .valueOf(progress));

                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_child4_education_current_cost
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child4_education_current_cost.setText("");
                        } else {
                            progress = progress / 100000;
                            progress = progress * 100000;
                            edt_child4_education_current_cost.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_child4_marriage_corpus
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child4_marriage_corpus.setText("");
                        } else {
                            progress = progress / 50000;
                            progress = progress * 50000;

                            edt_child4_marriage_corpus.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        btn_child5_age_plus.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                int_child5Age++;
                if (int_child5Age >= 0) {
                    edt_child5_age.setText(Integer.toString(int_child5Age));
                }
            }
        });
        btn_child5_age_minus.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                int_child5Age--;
                if (int_child5Age >= 0) {
                    edt_child5_age.setText(Integer.toString(int_child5Age));
                }
            }
        });
        btn_child5_marriage_fund_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child5_marriage_fund++;
                        if (int_child5_marriage_fund >= 0) {
                            edt_child5_marriage_fund.setText(Integer
                                    .toString(int_child5_marriage_fund));
                        }
                    }
                });
        btn_child5_marriage_fund_minus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child5_marriage_fund--;
                        if (int_child5_marriage_fund >= 0) {
                            edt_child5_marriage_fund.setText(Integer
                                    .toString(int_child5_marriage_fund));
                        }
                    }
                });
        btn_child5_education_fund_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child5_education_fund++;
                        if (int_child5_education_fund >= 0) {
                            edt_child5_education_fund.setText(Integer
                                    .toString(int_child5_education_fund));
                        }
                    }
                });
        btn_child5_education_fund_minus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_child5_education_fund--;
                        if (int_child5_education_fund >= 0) {
                            edt_child5_education_fund.setText(Integer
                                    .toString(int_child5_education_fund));
                        }
                    }
                });

        sb_child5_education_corpus
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child5_education_corpus.setText("");
                        } else {

                            edt_child5_education_corpus.setText(String
                                    .valueOf(progress));

                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_child5_education_current_cost
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child5_education_current_cost.setText("");
                        } else {
                            edt_child5_education_current_cost.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        sb_child5_marriage_corpus
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar thumbRadiusSeek,
                                                  int progress, boolean fromUser) {
                        if (progress == 0) {
                            edt_child5_marriage_corpus.setText("");
                        } else {
                            edt_child5_marriage_corpus.setText(String
                                    .valueOf(progress));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        btn_wealth_creation_home_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_wealthCreation_home++;
                        if (int_wealthCreation_home >= 0) {
                            edt_wealth_creation_home.setText(Integer
                                    .toString(int_wealthCreation_home));
                        }
                        /** Added by Priyanka Warekar - 10-11-2016 - start *****/
                        if (int_wealthCreation_home > 0) {
                            ll_seekbr_corpusSavedForToBuyHome
                                    .setVisibility(View.VISIBLE);
                            ll_editbox_corpusSavedForToBuyHome
                                    .setVisibility(View.VISIBLE);
                            ll_currentCostToBuyHome.setVisibility(View.VISIBLE);
                        } else {
                            ll_seekbr_corpusSavedForToBuyHome
                                    .setVisibility(View.GONE);
                            ll_editbox_corpusSavedForToBuyHome
                                    .setVisibility(View.GONE);
                            ll_currentCostToBuyHome.setVisibility(View.GONE);
                        }
                        /** Added by Priyanka Warekar - 10-11-2016 - end *****/
                    }
                });
        btn_wealth_creation_home_minus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_wealthCreation_home--;
                        if (int_wealthCreation_home >= 0) {

                            edt_wealth_creation_home.setText(Integer
                                    .toString(int_wealthCreation_home));

                            if (int_wealthCreation_home == 0) {
                                ll_seekbr_corpusSavedForToBuyHome
                                        .setVisibility(View.GONE);
                                ll_editbox_corpusSavedForToBuyHome
                                        .setVisibility(View.GONE);
                                ll_currentCostToBuyHome
                                        .setVisibility(View.GONE);

                                sb_wealth_creation_corpus_for_home
                                        .setEnabled(false);
                                sb_wealth_creation_corpus_for_home
                                        .setClickable(false);
                                edt_wealth_creation_corpus_for_home
                                        .setEnabled(false);
                                edt_wealth_creation_corpus_for_home
                                        .setClickable(false);
                                edt_wealth_creation_current_cost_for_home
                                        .setEnabled(false);
                                edt_wealth_creation_current_cost_for_home
                                        .setClickable(false);
                                sb_wealth_creation_corpus_for_home
                                        .setProgress(0);
                                edt_wealth_creation_corpus_for_home.setText("");
                                edt_wealth_creation_current_cost_for_home
                                        .setText("");
                                tv_mand_corpus_home
                                        .setVisibility(View.INVISIBLE);
                                tv_mand_cost_home.setVisibility(View.INVISIBLE);

                                strNoOfYearBuyHome = "0";
                                strCorpusForhome = "0";
                                strCurrentCosthome = "0";

                            }
                            /** Added by Priyanka Warekar - 10-11-2016 - start *****/
                            else {

                                ll_seekbr_corpusSavedForToBuyHome
                                        .setVisibility(View.VISIBLE);
                                ll_editbox_corpusSavedForToBuyHome
                                        .setVisibility(View.VISIBLE);
                                ll_currentCostToBuyHome
                                        .setVisibility(View.VISIBLE);

                            }
                            /** Added by Priyanka Warekar - 10-11-2016 - end *****/
                        }
                    }
                });

        btn_wealth_creation_other_plus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_wealthCreation_other++;
                        if (int_wealthCreation_other >= 0) {
                            edt_wealth_creation_other.setText(Integer
                                    .toString(int_wealthCreation_other));
                        }
                        /** Added by Priyanka Warekar - 10-11-2016 - start *****/
                        if (int_wealthCreation_other > 0) {
                            ll_other_current_cost.setVisibility(View.VISIBLE);
                            ll_corpus_OtherGoal.setVisibility(View.VISIBLE);
                            ll_corpus_other.setVisibility(View.VISIBLE);
                        } else {
                            ll_other_current_cost.setVisibility(View.GONE);
                            ll_corpus_OtherGoal.setVisibility(View.GONE);
                            ll_corpus_other.setVisibility(View.GONE);
                        }
                        /** Added by Priyanka Warekar - 10-11-2016 - end *****/
                    }
                });
        btn_wealth_creation_other_minus
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View view) {
                        int_wealthCreation_other--;
                        if (int_wealthCreation_other >= 0) {
                            edt_wealth_creation_other.setText(Integer
                                    .toString(int_wealthCreation_other));

                            if (int_wealthCreation_other == 0) {
                                ll_other_current_cost.setVisibility(View.GONE);
                                ll_corpus_OtherGoal.setVisibility(View.GONE);
                                ll_corpus_other.setVisibility(View.GONE);
                                sb_wealth_creation_corpus_for_other
                                        .setEnabled(false);
                                sb_wealth_creation_corpus_for_other
                                        .setClickable(false);
                                edt_wealth_creation_corpus_for_other
                                        .setEnabled(false);
                                edt_wealth_creation_corpus_for_other
                                        .setClickable(false);
                                edt_wealth_creation_current_cost_for_other
                                        .setEnabled(false);
                                edt_wealth_creation_current_cost_for_other
                                        .setClickable(false);

                                sb_wealth_creation_corpus_for_other
                                        .setProgress(0);
                                edt_wealth_creation_corpus_for_other
                                        .setText("");
                                edt_wealth_creation_current_cost_for_other
                                        .setText("");
                                tv_mand_corpus_other
                                        .setVisibility(View.INVISIBLE);
                                tv_mand_cost_other
                                        .setVisibility(View.INVISIBLE);

                                strNoOfYearOthergoal = "0";
                                strCorpusForOther = "0";
                                strCurrentCostOther = "0";
                            }
                            /** Added by Priyanka Warekar - 10-11-2016 - start *****/
                            else {
                                ll_other_current_cost
                                        .setVisibility(View.VISIBLE);
                                ll_corpus_OtherGoal.setVisibility(View.VISIBLE);
                                ll_corpus_other.setVisibility(View.VISIBLE);
                            }
                            /** Added by Priyanka Warekar - 10-11-2016 - end *****/
                        }
                    }
                });

        btn_child1.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                ll_child1_details.setVisibility(View.VISIBLE);
                ll_child2_details.setVisibility(View.GONE);
                ll_child3_details.setVisibility(View.GONE);
                ll_child4_details.setVisibility(View.GONE);
                ll_child5_details.setVisibility(View.GONE);

            }
        });
        btn_child2.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                ll_child1_details.setVisibility(View.GONE);
                ll_child2_details.setVisibility(View.VISIBLE);
                ll_child3_details.setVisibility(View.GONE);
                ll_child4_details.setVisibility(View.GONE);
                ll_child5_details.setVisibility(View.GONE);
            }
        });
        btn_child3.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                ll_child1_details.setVisibility(View.GONE);
                ll_child2_details.setVisibility(View.GONE);
                ll_child4_details.setVisibility(View.GONE);
                ll_child5_details.setVisibility(View.GONE);
                ll_child3_details.setVisibility(View.VISIBLE);
            }
        });
        btn_child4.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                ll_child1_details.setVisibility(View.GONE);
                ll_child2_details.setVisibility(View.GONE);
                ll_child3_details.setVisibility(View.GONE);
                ll_child5_details.setVisibility(View.GONE);
                ll_child4_details.setVisibility(View.VISIBLE);
            }
        });
        btn_child5.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                ll_child1_details.setVisibility(View.GONE);
                ll_child2_details.setVisibility(View.GONE);
                ll_child3_details.setVisibility(View.GONE);
                ll_child4_details.setVisibility(View.GONE);
                ll_child5_details.setVisibility(View.VISIBLE);
            }
        });
        edt_monthly_income.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (edt_monthly_income.isFocused()) {
                    if (s.length() > 0) {

                        long yearly = Long.parseLong(s.toString()) * 12;
                        edt_yearly_income.setText(String.valueOf(yearly));

                    } else {
                        edt_yearly_income.setText("");
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        edt_yearly_income.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (edt_yearly_income.isFocused()) {
                    if (s.length() > 0) {

                        long yearly = Long.parseLong(s.toString()) / 12;
                        edt_monthly_income.setText(String.valueOf(yearly));

                    } else {
                        edt_monthly_income.setText("");
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        edt_monthly_income_with_emi.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (edt_monthly_income_with_emi.isFocused()) {
                    if (s.length() > 0) {

                        long yearly = Long.parseLong(s.toString()) * 12;
                        edt_yearly_income_with_emi.setText(String
                                .valueOf(yearly));

                    } else {
                        edt_yearly_income_with_emi.setText("");
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        edt_yearly_income_with_emi.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (edt_yearly_income_with_emi.isFocused()) {
                    if (s.length() > 0) {

                        long yearly = Long.parseLong(s.toString()) / 12;
                        edt_monthly_income_with_emi.setText(String
                                .valueOf(yearly));

                    } else {
                        edt_monthly_income_with_emi.setText("");
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        edt_wealth_creation_other.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    String value = edt_wealth_creation_other.getText()
                            .toString();
                    int_wealthCreation_other = Integer.parseInt(value);
                    sb_wealth_creation_corpus_for_other.setEnabled(true);
                    sb_wealth_creation_corpus_for_other.setClickable(true);
                    edt_wealth_creation_corpus_for_other.setEnabled(true);
                    edt_wealth_creation_corpus_for_other.setClickable(true);
                    edt_wealth_creation_current_cost_for_other.setEnabled(true);
                    edt_wealth_creation_current_cost_for_other
                            .setClickable(true);
                    edt_wealth_creation_current_cost_for_other.setText("");

                    tv_mand_corpus_other.setVisibility(View.VISIBLE);
                    tv_mand_cost_other.setVisibility(View.VISIBLE);

                    strNoOfYearOthergoal = "";
                    strCorpusForOther = "";
                    strCurrentCostOther = "";

                } else {
                    sb_wealth_creation_corpus_for_other.setEnabled(false);
                    sb_wealth_creation_corpus_for_other.setClickable(false);
                    edt_wealth_creation_corpus_for_other.setEnabled(false);
                    edt_wealth_creation_corpus_for_other.setClickable(false);
                    edt_wealth_creation_current_cost_for_other
                            .setEnabled(false);
                    edt_wealth_creation_current_cost_for_other
                            .setClickable(true);

                    sb_wealth_creation_corpus_for_other.setProgress(0);
                    edt_wealth_creation_corpus_for_other.setText("");
                    edt_wealth_creation_current_cost_for_other.setText("");
                    tv_mand_corpus_other.setVisibility(View.INVISIBLE);
                    tv_mand_cost_other.setVisibility(View.INVISIBLE);

                    strNoOfYearOthergoal = "0";
                    strCorpusForOther = "0";
                    strCurrentCostOther = "0";

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        edt_wealth_creation_home.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    String value = edt_wealth_creation_home.getText()
                            .toString();
                    int_wealthCreation_home = Integer.parseInt(value);
                    sb_wealth_creation_corpus_for_home.setEnabled(true);
                    sb_wealth_creation_corpus_for_home.setClickable(true);
                    edt_wealth_creation_corpus_for_home.setEnabled(true);
                    edt_wealth_creation_corpus_for_home.setClickable(true);
                    edt_wealth_creation_current_cost_for_home.setEnabled(true);
                    edt_wealth_creation_current_cost_for_home
                            .setClickable(true);
                    edt_wealth_creation_current_cost_for_home.setText("");

                    tv_mand_corpus_home.setVisibility(View.VISIBLE);
                    tv_mand_cost_home.setVisibility(View.VISIBLE);

                    strNoOfYearBuyHome = "";
                    strCorpusForhome = "";
                    strCurrentCosthome = "";

                } else {
                    sb_wealth_creation_corpus_for_home.setEnabled(false);
                    sb_wealth_creation_corpus_for_home.setClickable(false);
                    edt_wealth_creation_corpus_for_home.setEnabled(false);
                    edt_wealth_creation_corpus_for_home.setClickable(false);
                    edt_wealth_creation_current_cost_for_home.setEnabled(false);
                    edt_wealth_creation_current_cost_for_home
                            .setClickable(true);
                    sb_wealth_creation_corpus_for_home.setProgress(0);
                    edt_wealth_creation_corpus_for_home.setText("");
                    edt_wealth_creation_current_cost_for_home.setText("");
                    tv_mand_corpus_home.setVisibility(View.INVISIBLE);
                    tv_mand_cost_home.setVisibility(View.INVISIBLE);

                    strNoOfYearBuyHome = "0";
                    strCorpusForhome = "0";
                    strCurrentCosthome = "0";
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        edt_retirment_no_of_years.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    String strvalue = edt_retirment_no_of_years.getText()
                            .toString();
                    int_retirement = Integer.parseInt(strvalue);
                } else {
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        edt_child1_age.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    String strvalue = edt_child1_age.getText().toString();
                    int_child1Age = Integer.parseInt(strvalue);
                } else {
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        edt_child1_education_fund.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    String strvalue = edt_child1_education_fund.getText()
                            .toString();
                    int_child1_education_fund = Integer.parseInt(strvalue);
                } else {
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });
        edt_child1_marriage_fund.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    String strvalue = edt_child1_marriage_fund.getText()
                            .toString();
                    int_child1_marriage_fund = Integer.parseInt(strvalue);
                } else {
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
            }
        });

        Ibtn_signatureofIntermediary
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        // if (cb_statement.isChecked()
                        // && cb_statement_need_analysis.isChecked()) {
                        latestImage = "agent";
                        windowmessagesgin();
                        // } else {
                        // dialog("Please Tick on I Agree Clause ", true);
                        // setFocusable(cb_statement);
                        // cb_statement.requestFocus();
                        // }

                    }
                });

        Ibtn_signatureofCustomer.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                // if (cb_statement.isChecked()
                // && cb_statement_need_analysis.isChecked()) {
                latestImage = "proposer";
                windowmessageProposersgin();
                /*
                 * } else { dialog("Please Tick on I Agree Clause ", true);
                 * setFocusable(cb_statement); cb_statement.requestFocus(); }
                 */

            }
        });

    }

    // FOr Date Dialog Box

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, R.style.datepickerstyle,
                        mDateSetListener, mDay, mMonth, mYear);
            default:
                break;
        }
        return null;
    }

    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
            default:
                break;

        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // if (view.isShown()) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay(DIALOG_ID);
            // }
        }
    };

    // For Displaying StartDate and EndDate after its selection
    private void updateDisplay(int id) {

        String mont = (mMonth + 1 < 10 ? "0" : "") + (mMonth + 1);
        String day = (mDay < 10 ? "0" : "") + mDay;

        StringBuilder date = new StringBuilder().append(day).append("-")
                .append(mont).append("-").append(mYear);

        Calendar present_date = Calendar.getInstance();
        int tDay = present_date.get(Calendar.DAY_OF_MONTH);
        int tMonth = present_date.get(Calendar.MONTH);
        int tYear = present_date.get(Calendar.YEAR);

        int age;

        if (tMonth == mMonth) {
            if (tDay >= mDay) {
                age = tYear - mYear;
            } else
                age = tYear - mYear - 1;
        } else if (tMonth > mMonth) {
            age = tYear - mYear;
        } else
            age = tYear - mYear - 1;

        String final_age = Integer.toString(age);

        if (final_age.contains("-")) {
            Toast toast = Toast.makeText(this, "Please select valid date",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
        } else {
            switch (id) {

                case 1:
                    str_age = final_age;
                    final_age = Integer.toString(age);
                    if (final_age.contains("-")) {
                        Toast toast = Toast.makeText(this,
                                "Please select valid date", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 105, 50);
                        toast.show();
                    } else {
                        if (18 <= age) {

                            btn_date_of_birth.setText(date);
                            tv_age.setText(final_age + " Years");
                            str_date_of_birth = getDate1(date + "");

                        } else {
                            btn_date_of_birth.setText("Select Date");
                            tv_age.setText("");
                            str_date_of_birth = "";
                            Toast toast = Toast.makeText(this,
                                    "Age should above 18 years", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP, 105, 50);
                            toast.show();
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }

    public void OnDateOfBirth(View v) {
        DIALOG_ID = 1;
        showDialog(DATE_DIALOG_ID);
    }

    private String getDate1(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("MM-dd-yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }

    private void setDefaultDate() {
        Calendar present_date = Calendar.getInstance();
        present_date.add(Calendar.YEAR, -35);
        mDay = present_date.get(Calendar.DAY_OF_MONTH);
        mMonth = present_date.get(Calendar.MONTH);
        mYear = present_date.get(Calendar.YEAR);

        // String mont = (mMonth + 1 < 10 ? "0" : "") + (mMonth + 1);
        // /String day = (mDay < 10 ? "0" : "") + mDay;

        /*
         * StringBuilder date = new StringBuilder().append(day).append("-")
         * .append(mont).append("-").append(mYear);
         */
        // btn_date_of_birth.setText(date);

    }

    public void onClickClearNeedAnalysis(View v) {

        unique_no = "";
        // count = 0;

        Intent intent = new Intent(getApplicationContext(),
                NeedAnalysisActivity.class);
        Intent i = getIntent();
        AgentCode = i.getStringExtra("AgentCode");
        str_usertype = i.getStringExtra("usertype");
        intent.putExtra("AgentCode", AgentCode);
        intent.putExtra("usertype", str_usertype);
        startActivity(intent);

    }

    public void onClickProceed(View v) {
        GetMyDetailsvalues();
        if (ValidationMyDetails()) {

            btn_my_goals.setEnabled(true);
            btn_my_goals.setClickable(true);
            btn_my_details.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.my_details_blue));

            btn_my_goals.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.my_goals_blue));

            btn_my_solution.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.solution_white));
            // Drawable drawableleft = getResources().getDrawable(
            // R.drawable.sidearrow);
            // btn_child_header.setCompoundDrawablesWithIntrinsicBounds(
            // drawableleft, null, null, null);

            iv_chid_side_arrow.setImageDrawable(getResources().getDrawable(
                    R.drawable.sidearrow));
            ll_my_details.setVisibility(View.GONE);
            ll_my_goals.setVisibility(View.VISIBLE);
            ll_solution.setVisibility(View.GONE);
            ll_suggestdProdList.setVisibility(View.GONE);

            switch (str_no_of_child) {
                case "0":
                    ll_btn_child_header.setVisibility(View.GONE);
                    ll_child_tab.setVisibility(View.GONE);
                    ll_child1_details.setVisibility(View.GONE);
                    ll_child2_details.setVisibility(View.GONE);
                    ll_child3_details.setVisibility(View.GONE);
                    ll_child4_details.setVisibility(View.GONE);
                    ll_child5_details.setVisibility(View.GONE);
                    break;
                case "1":
                    ll_btn_child_header.setVisibility(View.VISIBLE);
                    ll_child_tab.setVisibility(View.GONE);
                    ll_child1_details.setVisibility(View.GONE);
                    ll_child2_details.setVisibility(View.GONE);
                    ll_child3_details.setVisibility(View.GONE);
                    ll_child4_details.setVisibility(View.GONE);
                    ll_child5_details.setVisibility(View.GONE);
                    btn_child2.setVisibility(View.GONE);
                    btn_child3.setVisibility(View.GONE);
                    btn_child4.setVisibility(View.GONE);
                    btn_child5.setVisibility(View.GONE);

                    break;
                case "2":
                    ll_btn_child_header.setVisibility(View.VISIBLE);
                    ll_child_tab.setVisibility(View.GONE);
                    ll_child1_details.setVisibility(View.GONE);
                    ll_child2_details.setVisibility(View.GONE);
                    ll_child3_details.setVisibility(View.GONE);
                    ll_child4_details.setVisibility(View.GONE);
                    ll_child5_details.setVisibility(View.GONE);
                    btn_child3.setVisibility(View.GONE);
                    btn_child4.setVisibility(View.GONE);
                    btn_child5.setVisibility(View.GONE);

                    break;
                case "3":
                    ll_btn_child_header.setVisibility(View.VISIBLE);
                    ll_child_tab.setVisibility(View.GONE);
                    ll_child1_details.setVisibility(View.GONE);
                    ll_child2_details.setVisibility(View.GONE);
                    ll_child3_details.setVisibility(View.GONE);
                    ll_child4_details.setVisibility(View.GONE);
                    ll_child5_details.setVisibility(View.GONE);
                    btn_child4.setVisibility(View.GONE);
                    btn_child5.setVisibility(View.GONE);
                    break;
                case "4":
                    ll_btn_child_header.setVisibility(View.VISIBLE);
                    ll_child1_details.setVisibility(View.GONE);
                    ll_child2_details.setVisibility(View.GONE);
                    ll_child3_details.setVisibility(View.GONE);
                    ll_child4_details.setVisibility(View.GONE);
                    ll_child5_details.setVisibility(View.GONE);
                    btn_child5.setVisibility(View.GONE);

                    break;
                case "5":
                    ll_child_tab.setVisibility(View.GONE);
                    ll_child1_details.setVisibility(View.GONE);

                    break;
            }
            // InputMethodManager imm = (InputMethodManager) this
            // .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
            // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            // edt_focus.requestFocus();
            btn_my_goals.requestFocus();
            setFocusable(btn_my_goals);

            hideKeyboard(NeedAnalysisActivity.this);

        }
        // InputMethodManager imm = (InputMethodManager) this
        // .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        // edt_focus.requestFocus();
        btn_my_goals.requestFocus();
        setFocusable(btn_my_goals);

        hideKeyboard(NeedAnalysisActivity.this);

    }

    public void OnClickMyGoalsProceed(View v) {

        suggested_prod_list.clear();
        retirement_prod_list.clear();
        wealth_prod_list.clear();
        child_prod_list.clear();
        protection_prod_list.clear();
        remaining_product_list.clear();
        chosen_prod_list.clear();
        ulip_prod_list.clear();
        traditional_prod_list.clear();

        GetMyGoalsValues();
        if (ValidationMyGoals() && valHome() && valOther() && ValChild1()
                && ValChild2() && ValChild3() && ValChild4()
                && valRetirementAge()) {
            HidePlan();
            System.out.println("agentcode#####" + AgentCode);
            String agentcode = AgentCode.substring(2)
                    .trim();

            if (unique_no.equals("")) {
                unique_no = getneedAnalysis_Unique_No("NAC", agentcode
                        + "");
            }
            getInputNeedAnalysis();

            /*
             * createdBy = "Machindranath Yewale"; createdDate = "22/10/2013";
             * modifiedBy = "Machindranath Yewale"; modifiedDate = "22/10/2013";
             */

            // isSync = true;
            // isFlag1 = true;
            // isFlag2 = true;

            // insertDataIntoDatabase();

            GetOutput();
            btn_my_solution.setEnabled(true);
            btn_my_solution.setClickable(true);
            btn_my_details.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.my_details_blue));
            btn_my_goals.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.my_goals_blue));
            btn_my_solution.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.solution_blue));
            ll_my_details.setVisibility(View.GONE);
            ll_my_goals.setVisibility(View.GONE);
            ll_solution.setVisibility(View.VISIBLE);
            ll_summary.setVisibility(View.VISIBLE);
            ll_protection.setVisibility(View.GONE);
            ll_retirement.setVisibility(View.GONE);
            ll_wealth_creation.setVisibility(View.GONE);
            ll_child.setVisibility(View.GONE);
            ll_suggestdProdList.setVisibility(View.GONE);
            btn_my_solution.requestFocus();
            setFocusable(btn_my_solution);

            InputMethodManager imm = (InputMethodManager) this
                    .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
        btn_my_solution.requestFocus();
        setFocusable(btn_my_solution);

        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void onClickBack(View v) {
        ll_solution.setVisibility(View.VISIBLE);
        ll_my_details.setVisibility(View.GONE);
        ll_my_goals.setVisibility(View.GONE);
        ll_solution.setVisibility(View.GONE);
        ll_suggestdProdList.setVisibility(View.GONE);

        btn_my_solution.requestFocus();
        setFocusable(btn_my_solution);

        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void onClickBackMyGoals(View v) {
        btn_my_details.setEnabled(true);
        btn_my_details.setClickable(true);
        ll_solution.setVisibility(View.GONE);
        ll_my_details.setVisibility(View.VISIBLE);
        ll_my_goals.setVisibility(View.GONE);
        ll_solution.setVisibility(View.GONE);
        ll_suggestdProdList.setVisibility(View.GONE);

        btn_my_details.requestFocus();
        setFocusable(btn_my_details);

        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    public void OnClickBackSolution(View v) {
        btn_my_solution.setEnabled(true);
        btn_my_solution.setClickable(true);

        ll_my_details.setVisibility(View.GONE);
        ll_my_goals.setVisibility(View.GONE);
        ll_solution.setVisibility(View.VISIBLE);
        btn_my_details.setVisibility(View.VISIBLE);
        btn_my_goals.setVisibility(View.VISIBLE);
        btn_my_solution.setVisibility(View.VISIBLE);
        ll_summary.setVisibility(View.VISIBLE);
        ll_protection.setVisibility(View.GONE);
        ll_retirement.setVisibility(View.GONE);
        ll_wealth_creation.setVisibility(View.GONE);
        ll_child.setVisibility(View.GONE);
        ll_suggestdProdList.setVisibility(View.GONE);

        btn_my_solution.requestFocus();
        setFocusable(btn_my_solution);

        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    public void OnClickBackSummary(View v) {
        btn_my_goals.setEnabled(true);
        btn_my_goals.setClickable(true);

        ll_my_details.setVisibility(View.GONE);
        ll_my_goals.setVisibility(View.VISIBLE);
        ll_solution.setVisibility(View.GONE);
        btn_my_details.setVisibility(View.VISIBLE);
        btn_my_goals.setVisibility(View.VISIBLE);
        btn_my_solution.setVisibility(View.VISIBLE);
        ll_summary.setVisibility(View.GONE);
        ll_protection.setVisibility(View.GONE);
        ll_retirement.setVisibility(View.GONE);
        ll_wealth_creation.setVisibility(View.GONE);
        ll_child.setVisibility(View.GONE);
        ll_suggestdProdList.setVisibility(View.GONE);

        btn_my_goals.requestFocus();
        setFocusable(btn_my_goals);

        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    public void onBackClick(View v) {
        btn_my_goals.setEnabled(true);
        btn_my_goals.setClickable(true);
        ll_my_details.setVisibility(View.GONE);
        ll_my_goals.setVisibility(View.GONE);
        ll_solution.setVisibility(View.VISIBLE);
        btn_my_details.setVisibility(View.VISIBLE);
        btn_my_goals.setVisibility(View.VISIBLE);
        btn_my_solution.setVisibility(View.VISIBLE);
        ll_summary.setVisibility(View.VISIBLE);
        ll_protection.setVisibility(View.GONE);
        ll_retirement.setVisibility(View.GONE);
        ll_wealth_creation.setVisibility(View.GONE);
        ll_child.setVisibility(View.GONE);
        ll_suggestdProdList.setVisibility(View.GONE);

        btn_my_goals.requestFocus();
        setFocusable(btn_my_goals);

        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void GetWealthCreationOutput() {
        SoapObject request = new SoapObject(NAMESPACE,
                METHOD_NAME_NEED_ANALYSIS);

        // String strValue =
        // "<NeedAn><PrAge>43</PrAge><PrGe>M</PrGe><PrDependCh>0</PrDependCh><PrRetAge>60</PrRetAge><PrAnnIncome>1200000</PrAnnIncome><SpouseAnnIncome>12000</SpouseAnnIncome><PrTotAnnExp>480000</PrTotAnnExp><PrRentAnnExp>0</PrRentAnnExp><PrLibEmiHomeLoan>3000000</PrLibEmiHomeLoan><PrLibEmiHomeLoanMonPrem>4000</PrLibEmiHomeLoanMonPrem><PrLibEmiHomeLoanAnnPrem>480000</PrLibEmiHomeLoanAnnPrem><PrLibEmiPerLoan>0</PrLibEmiPerLoan><PrLibEmiPerLoanMonPrem>0</PrLibEmiPerLoanMonPrem><PrLibEmiPerLoanAnnPrem>0</PrLibEmiPerLoanAnnPrem><PrLibEmiEducationLoan>0</PrLibEmiEducationLoan><PrLibEmiEducationLoanMonPrem>0</PrLibEmiEducationLoanMonPrem><PrLibEmiEducationLoanAnnPrem>0</PrLibEmiEducationLoanAnnPrem><PrLibEmiCarLoan>0</PrLibEmiCarLoan><PrLibEmiCarLoanMonPrem>0</PrLibEmiCarLoanMonPrem><PrLibEmiCarLoanAnnPrem>0</PrLibEmiCarLoanAnnPrem><PrExpExisLifeCo>5000000</PrExpExisLifeCo><PrExpExisLifeCoverMonPrem>1500</PrExpExisLifeCoverMonPrem><PrExistingLifeInsCoverAnnPrem>18000</PrExistingLifeInsCoverAnnPrem><PrExpTradInsPol>500000</PrExpTradInsPol><PrExpTradInsPolMonPrem>3000</PrExpTradInsPolMonPrem><PrExpTradInsPolAnnPrem>36000</PrExpTradInsPolAnnPrem><PrExpUnitInsPol>0</PrExpUnitInsPol><PrExpUnitInsPolMonPrem>0</PrExpUnitInsPolMonPrem><PrExpUnitInsPolAnnPrem>0</PrExpUnitInsPolAnnPrem><PrExpExistHealthCo>0</PrExpExistHealthCo><PrExpExistHealthCoMonPrem>0</PrExpExistHealthCoMonPrem><PrExpExistHealthCorAnnPrem>0</PrExpExistHealthCorAnnPrem><PrInflRt>7</PrInflRt><PrInvApp>Moderate</PrInvApp><PrInvROI>8</PrInvROI><PrAvaiCorpFrReti>1000000</PrAvaiCorpFrReti><PrAnnPremFrReti>60000</PrAnnPremFrReti><PrAnnAvaiCorpFrFutGoal>1000000</PrAnnAvaiCorpFrFutGoal><PrInvFrFutGoal>48000</PrInvFrFutGoal><PrEduChBudExpNoYrs1>2</PrEduChBudExpNoYrs1><PrEduChBudEXp1>100000</PrEduChBudEXp1><PrEduChBudExpNoYrs2>0</PrEduChBudExpNoYrs2><PrEduChBudEXp2>0</PrEduChBudEXp2><PrEduChBudExpNoYrs3>0</PrEduChBudExpNoYrs3><PrEduChBudEXp3>0</PrEduChBudEXp3><PrEduChBudExpNoYrs4>0</PrEduChBudExpNoYrs4><PrEduChBudEXp4>0</PrEduChBudEXp4><PrMaChBudExpNoYrs1>10</PrMaChBudExpNoYrs1><PrMaChBudEXp1>500000</PrMaChBudEXp1><PrMaChBudExpNoYrs2>0</PrMaChBudExpNoYrs2><PrMaChBudEXp2>0</PrMaChBudEXp2><PrMaChBudExpNoYrs3>0</PrMaChBudExpNoYrs3><PrMaChBudEXp3>0</PrMaChBudEXp3><PrMaChBudExpNoYrs4>0</PrMaChBudExpNoYrs4><PrMaChBudEXp4>0</PrMaChBudEXp4><PrProWeCrNoofYr>7</PrProWeCrNoofYr><PrProWeCrBudExp>8500000</PrProWeCrBudExp><PrCarWeCrNoofYr>5</PrCarWeCrNoofYr><PrCarWeCrBudExp>700000</PrCarWeCrBudExp><PrOthAssWeCrNoofYr>4</PrOthAssWeCrNoofYr><PrOthAssWeCrBudExp>400000</PrOthAssWeCrBudExp><PrOthFinWeCrNoofYr>2</PrOthFinWeCrNoofYr><PrOthFinWeCrBudExp>200000</PrOthFinWeCrBudExp></NeedAn>";

        String strValue = "<NeedAn>" + "<PrAge>"
                + tv_age.getText().toString()
                + "</PrAge>"
                + "<PrGe>"
                + str_gender
                + "</PrGe>"
                + // gender
                "<PrDependCh>"
                + str_no_of_child
                + "</PrDependCh>"
                + // no of children
                "<PrRetAge>"
                + "60"
                + "</PrRetAge>"
                + // retirement age(right now 60)
                "<PrAnnIncome>"
                + strYearlyIncome
                + "</PrAnnIncome>"
                + // annual income
                "<SpouseAnnIncome>"
                + "0"
                + "</SpouseAnnIncome>"
                + "<PrTotAnnExp>"
                + stryearlyIncome_emi
                + "</PrTotAnnExp>"
                + // annual expense
                "<PrRentAnnExp>"
                + "0"
                + "</PrRentAnnExp>"
                + "<PrLibEmiHomeLoan>"
                + strOutstandingHomeLoan
                + "</PrLibEmiHomeLoan>"
                + // outstanding home loan
                "<PrLibEmiHomeLoanMonPrem>0</PrLibEmiHomeLoanMonPrem>"
                + "<PrLibEmiHomeLoanAnnPrem>0</PrLibEmiHomeLoanAnnPrem>"
                + "<PrLibEmiPerLoan>"
                + strOutstandingHomeLoanother
                + "</PrLibEmiPerLoan>"
                + // other loan
                "<PrLibEmiPerLoanMonPrem>0</PrLibEmiPerLoanMonPrem>"
                + "<PrLibEmiPerLoanAnnPrem>0</PrLibEmiPerLoanAnnPrem>"
                + "<PrLibEmiEducationLoan>0</PrLibEmiEducationLoan>"
                + "<PrLibEmiEducationLoanMonPrem>0</PrLibEmiEducationLoanMonPrem>"
                + "<PrLibEmiEducationLoanAnnPrem>0</PrLibEmiEducationLoanAnnPrem>"
                + "<PrLibEmiCarLoan>0</PrLibEmiCarLoan>"
                + "<PrLibEmiCarLoanMonPrem>0</PrLibEmiCarLoanMonPrem>"
                + "<PrLibEmiCarLoanAnnPrem>0</PrLibEmiCarLoanAnnPrem>"
                + "<PrExpExisLifeCo>"
                + strCurrentLifeInsuranceCoverage
                + "</PrExpExisLifeCo>"
                + // current life insurance coverage
                "<PrExpExisLifeCoverMonPrem>0</PrExpExisLifeCoverMonPrem>"
                + "<PrExistingLifeInsCoverAnnPrem>0</PrExistingLifeInsCoverAnnPrem>"
                + "<PrExpTradInsPol>0</PrExpTradInsPol>"
                + "<PrExpTradInsPolMonPrem>0</PrExpTradInsPolMonPrem>"
                + "<PrExpTradInsPolAnnPrem>0</PrExpTradInsPolAnnPrem>"
                + "<PrExpUnitInsPol>0</PrExpUnitInsPol>"
                + "<PrExpUnitInsPolMonPrem>0</PrExpUnitInsPolMonPrem>"
                + "<PrExpUnitInsPolAnnPrem>0</PrExpUnitInsPolAnnPrem>"
                + "<PrExpExistHealthCo>0</PrExpExistHealthCo>"
                + "<PrExpExistHealthCoMonPrem>0</PrExpExistHealthCoMonPrem>"
                + "<PrExpExistHealthCorAnnPrem>0</PrExpExistHealthCorAnnPrem>"
                + "<PrInflRt>"
                + str_inflation_assumed
                + "</PrInflRt>"
                + // inflation rate
                "<PrInvApp>"
                + str_investment_approach
                + "</PrInvApp>"
                + // investment approch is
                "<PrInvROI>0</PrInvROI>"
                + "<PrAvaiCorpFrReti>"
                + strRetirementCurrentCorpus
                + "</PrAvaiCorpFrReti>"
                + "<PrAnnPremFrReti>0</PrAnnPremFrReti>"
                + "<PrAnnAvaiCorpFrFutGoal>0</PrAnnAvaiCorpFrFutGoal>"
                + "<PrInvFrFutGoal>0</PrInvFrFutGoal>"
                + "<PrEduChBudExpNoYrs1>"
                + strChild1AgeAtEducation
                + "</PrEduChBudExpNoYrs1>"
                + // Age at which education fund is required - child 1 education
                "<PrEduChBudEXp1>"
                + strChild1CurrentCostEducation
                + "</PrEduChBudEXp1>"
                + // Current cost for education - child 1 education
                "<PrEduChBudExpNoYrs2>"
                + strChild2AgeAtEducation
                + "</PrEduChBudExpNoYrs2>"
                + // Age at which education fund is required - child 2 education
                "<PrEduChBudEXp2>"
                + strChild2CurrentCostEducation
                + "</PrEduChBudEXp2>"
                + // Current cost for education - child 2 education
                "<PrEduChBudExpNoYrs3>"
                + strChild3AgeAtEducation
                + "</PrEduChBudExpNoYrs3>"
                + // Age at which education fund is required - child 3 education
                "<PrEduChBudEXp3>"
                + strChild3CurrentCostEducation
                + "</PrEduChBudEXp3>"
                + // Current cost for education - child 3 education
                "<PrEduChBudExpNoYrs4>"
                + strChild4AgeAtEducation
                + "</PrEduChBudExpNoYrs4>"
                + // Age at which education fund is required - child 4 education
                "<PrEduChBudEXp4>"
                + strChild4CurrentCostEducation
                + "</PrEduChBudEXp4>"
                + // Current cost for education - child 4 education
                "<PrMaChBudExpNoYrs1>"
                + strChild1AgeAtMarriage
                + "</PrMaChBudExpNoYrs1>"
                + // Age at which marriage fund is required - child 1 marriage
                "<PrMaChBudEXp1>"
                + strChild1CurrentCostMarriage
                + "</PrMaChBudEXp1>"
                + // Current cost for marriage - child 1 marriage
                "<PrMaChBudExpNoYrs2>"
                + strChild2AgeAtMarriage
                + "</PrMaChBudExpNoYrs2>"
                + // Age at which marriage fund is required - child 2 marriage
                "<PrMaChBudEXp2>"
                + strChild2CurrentCostMarriage
                + "</PrMaChBudEXp2>"
                + // Current cost for marriage - child 2 marriage
                "<PrMaChBudExpNoYrs3>"
                + strChild3AgeAtMarriage
                + "</PrMaChBudExpNoYrs3>"
                + // Age at which marriage fund is required - child 3 marriage
                "<PrMaChBudEXp3>"
                + strChild3CurrentCostMarriage
                + "</PrMaChBudEXp3>"
                + // Current cost for marriage - child 3 marriage
                "<PrMaChBudExpNoYrs4>"
                + strChild4AgeAtMarriage
                + "</PrMaChBudExpNoYrs4>"
                + // Age at which marriage fund is required - child 4 marriage
                "<PrMaChBudEXp4>"
                + strChild4CurrentCostMarriage
                + "</PrMaChBudEXp4>"
                + // Current cost for marriage - child 4 marriage
                "<PrProWeCrNoofYr>0</PrProWeCrNoofYr>"
                + "<PrProWeCrBudExp>0</PrProWeCrBudExp>"
                + "<PrCarWeCrNoofYr>0</PrCarWeCrNoofYr>"
                + "<PrCarWeCrBudExp>0</PrCarWeCrBudExp>"
                + "<PrOthAssWeCrNoofYr>"
                + strNoOfYearBuyHome
                + "</PrOthAssWeCrNoofYr>"
                + // No. of Years
                // to buy a
                // home
                "<PrOthAssWeCrBudExp>"
                + strCurrentCosthome
                + "</PrOthAssWeCrBudExp>"
                + // Current
                // Cost for
                // home
                "<PrOthFinWeCrNoofYr>"
                + strNoOfYearOthergoal
                + "</PrOthFinWeCrNoofYr>"
                + // No. of Years
                // to buy a
                // other
                "<PrOthFinWeCrBudExp>" + strCurrentCostOther
                + "</PrOthFinWeCrBudExp>" + // Current
                // Cost for
                // other
                "</NeedAn>";

        request.addProperty("strInput", strValue);
        request.addProperty("reqFrom", "CON");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        // allowAllSSL();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
        try {
            androidHttpTranport.call(SOAP_ACTION_NEED_ANALYSIS, envelope);
            Object response = envelope.getResponse();

            if (!response.toString().contains("anyType{}")) {

                SoapPrimitive sa = null;
                try {
                    sa = (SoapPrimitive) envelope.getResponse();

                    outputlist = sa.toString();

                    ParseXML prsObj = new ParseXML();

                    outputlist = prsObj.parseXmlTag(outputlist, "NeedAn");
                    outputlist = new ParseXML().parseXmlTag(outputlist,
                            "ScreenData");
                    strErrorCOde = outputlist;

                    if (strErrorCOde == null) {
                        outputlist = sa.toString();
                        outputlist = prsObj.parseXmlTag(outputlist, "NeedAn");

                        // protection
                        String LifeProtCoverAmt = new ParseXML().parseXmlTag(
                                outputlist, "LifeProtCoverAmt");
                        String LifeProtReqCoverAmt = new ParseXML()
                                .parseXmlTag(outputlist, "LifeProtReqCoverAmt");

                        double LifeProtCoverAmt_ = Double
                                .parseDouble(LifeProtCoverAmt);
                        double LifeProtReqCoverAmt_ = Double
                                .parseDouble(LifeProtReqCoverAmt);

                        // Retirement

                        String PensionTotReqAmt = new ParseXML().parseXmlTag(
                                outputlist, "PensionTotReqAmt");
                        String PensionActAnnInvReq = new ParseXML()
                                .parseXmlTag(outputlist, "PensionActAnnInvReq");

                        double PensionTotReqAmt_ = Double
                                .parseDouble(PensionTotReqAmt);
                        double PensionActAnnInvReq_ = Double
                                .parseDouble(PensionActAnnInvReq);

                        // Child Future Planning

                        String WealthCreaChAnnInvReq = new ParseXML()
                                .parseXmlTag(outputlist,
                                        "WealthCreaChAnnInvReq");
                        String WealthCreaChActuAnnInvReq = new ParseXML()
                                .parseXmlTag(outputlist,
                                        "WealthCreaChActuAnnInvReq");

                        double WealthCreaChAnnInvReq_ = Double
                                .parseDouble(WealthCreaChAnnInvReq);
                        double WealthCreaChActuAnnInvReq_ = Double
                                .parseDouble(WealthCreaChActuAnnInvReq);

                        // Wealth

                        String CostOnEvDtProp = new ParseXML().parseXmlTag(
                                outputlist, "CostOnEvDtProp");
                        String CostOnEvDtOthrFin = new ParseXML().parseXmlTag(
                                outputlist, "CostOnEvDtOthrFin");

                        double CostOnEvDtProp_ = Double
                                .parseDouble(CostOnEvDtProp);
                        double CostOnEvDtOthrFin_ = Double
                                .parseDouble(CostOnEvDtOthrFin);
                        double Wealth_total = CostOnEvDtProp_
                                + CostOnEvDtOthrFin_;

                        StackedBarChart(LifeProtCoverAmt_,
                                LifeProtReqCoverAmt_, PensionTotReqAmt_,
                                PensionActAnnInvReq_, WealthCreaChAnnInvReq_,
                                WealthCreaChActuAnnInvReq_, Wealth_total);

                    } else {
                        Toast.makeText(context, "Server Not Responding.",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Server Not Responding.",
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void StackedBarChart(double LifeProtCoverAmt_,
                                 double LifeProtReqCoverAmt_, double PensionTotReqAmt_,
                                 double PensionActAnnInvReq_, double WealthCreaChAnnInvReq_,
                                 double WealthCreaChActuAnnInvReq_, double Wealth_total) {
        txt_protection.setText("Target Life Insurance Coverage Rs."
                + LifeProtReqCoverAmt_);
        txt_retirement.setText("Target amount Rs." + PensionActAnnInvReq_);
        txt_child_future.setText("Target amount Rs."
                + WealthCreaChActuAnnInvReq_);
        txt_wealth_creation.setText("Target amount Rs." + Wealth_total);

        double protection_current_corpus = 5000000.00;
        double retirement_current_corpus = 5000000.00;
        double child_future_current_corpus = 50000.00;
        double wealth_current_corpus = 5000000.00; // home + other

        double protection_saved_amount = LifeProtReqCoverAmt_
                - protection_current_corpus;
        double retirement_saved_amount = PensionActAnnInvReq_
                - retirement_current_corpus;
        double child_future_current_corpus_saved_amount = WealthCreaChActuAnnInvReq_
                - child_future_current_corpus;
        double wealth_current_corpus_saved_amount = Wealth_total
                - wealth_current_corpus;

        // protection

        double a = Math.round((protection_current_corpus * 100)
                / LifeProtReqCoverAmt_);
        double b = Math.round(100 - a);

        LayoutParams params = (LayoutParams) ln_chart_protection_top
                .getLayoutParams();
        params.height = (int) b;
        params.width = 40;

        LayoutParams params1 = (LayoutParams) ln_chart_protection_buttom
                .getLayoutParams();
        params1.height = (int) a;
        params1.width = 40;

        // retirement

        double c = Math.round((retirement_current_corpus * 100)
                / PensionActAnnInvReq_);
        double d = Math.round(100 - c);

        LayoutParams params2 = (LayoutParams) ln_chart_retirement_top
                .getLayoutParams();
        params2.height = (int) d;
        params2.width = 40;

        LayoutParams params3 = (LayoutParams) ln_chart_retirement_buttom
                .getLayoutParams();
        params3.height = (int) c;
        params3.width = 40;

        // child

        double e = Math.round((child_future_current_corpus * 100)
                / WealthCreaChActuAnnInvReq_);
        double f = Math.round(100 - e);

        LayoutParams params4 = (LayoutParams) ln_chart_child_top
                .getLayoutParams();
        params4.height = (int) f;
        params4.width = 40;

        LayoutParams params5 = (LayoutParams) ln_chart_child_buttom
                .getLayoutParams();
        params5.height = (int) e;
        params5.width = 40;

        // wealth

        double g = Math.round((wealth_current_corpus * 100) / Wealth_total);
        double h = Math.round(100 - g);

        LayoutParams params6 = (LayoutParams) ln_chart_wealth_top
                .getLayoutParams();
        params6.height = (int) h;
        params6.width = 40;

        LayoutParams params7 = (LayoutParams) ln_chart_wealth_buttom
                .getLayoutParams();
        params7.height = (int) g;
        params7.width = 40;

        txt_protection_gap.setText("Gap to target amount Rs."
                + protection_saved_amount);
        txt_protection_target_amt.setText(" < Target amount Rs."
                + LifeProtReqCoverAmt_);
        txt_protection_corpus.setText("Current Coverage Rs."
                + protection_current_corpus);

        txt_retirement_gap.setText("Gap to target amount Rs." + "\n"
                + retirement_saved_amount);
        txt_retirement_target_amt.setText(" < Target amount Rs."
                + PensionActAnnInvReq_);
        txt_retirement_corpus.setText("Current Corpus(FV*) Rs."
                + retirement_current_corpus);

        txt_child_future_gap.setText("Gap to target amount Rs." + "\n"
                + child_future_current_corpus_saved_amount);
        txt_child_future_target.setText(" < Target amount Rs."
                + WealthCreaChActuAnnInvReq_);
        txt_child_future_corpus.setText("Current Corpus(FV*) Rs."
                + child_future_current_corpus);

        txt_wealth_creation_gap.setText("Gap to target amount Rs." + "\n"
                + wealth_current_corpus_saved_amount);
        txt_wealth_creation_target.setText(" < Target amount Rs."
                + Wealth_total);
        txt_wealth_creation_corpus.setText("Current Corpus(FV*) Rs."
                + wealth_current_corpus);

    }

    private void getInputNeedAnalysis() {

        inputVal = new StringBuilder();

        inputVal.append("<?xml version='1.0' encoding='utf-8' ?><needAnalysis>");

        inputVal.append("<gender>" + str_gender + "</gender>");
        inputVal.append("<dob>" + str_date_of_birth + "</dob>");
        inputVal.append("<age>" + str_age + "</age>");
        inputVal.append("<maritalStatus>" + str_marital_status
                + "</maritalStatus>");
        inputVal.append("<childNo>" + str_no_of_child + "</childNo>");
        inputVal.append("<inflationAssumed>" + str_inflation_assumed
                + "</inflationAssumed>");
        inputVal.append("<investmentApproach>" + str_investment_approach
                + "</investmentApproach>");

        inputVal.append("<child1Name>" + strChild1Name + "</child1Name>");
        inputVal.append("<child1Age>" + strChild1Age + "</child1Age>");
        inputVal.append("<child1AgeAtEducation>" + strChild1AgeAtEducation
                + "</child1AgeAtEducation>");
        inputVal.append("<child1CorpusEducation>" + strChild1CorpusEducation
                + "</child1CorpusEducation>");
        inputVal.append("<child1CurrentCostEducation>"
                + strChild1CurrentCostEducation
                + "</child1CurrentCostEducation>");
        inputVal.append("<child1AgeAtMarriage>" + strChild1AgeAtMarriage
                + "</child1AgeAtMarriage>");
        inputVal.append("<child1CorpusMarriage>" + strChild1CorpusMarriage
                + "</child1CorpusMarriage>");
        inputVal.append("<child1CurrentCostMarriage>"
                + strChild1CurrentCostMarriage + "</child1CurrentCostMarriage>");

        inputVal.append("<child2Name>" + strChild2Name + "</child2Name>");
        inputVal.append("<child2Age>" + strChild2Age + "</child2Age>");
        inputVal.append("<child2AgeAtEducation>" + strChild2AgeAtEducation
                + "</child2AgeAtEducation>");
        inputVal.append("<child2CorpusEducation>" + strChild2CorpusEducation
                + "</child2CorpusEducation>");
        inputVal.append("<child2CurrentCostEducation>"
                + strChild2CurrentCostEducation
                + "</child2CurrentCostEducation>");
        inputVal.append("<child2AgeAtMarriage>" + strChild2AgeAtMarriage
                + "</child2AgeAtMarriage>");
        inputVal.append("<child2CorpusMarriage>" + strChild2CorpusMarriage
                + "</child2CorpusMarriage>");
        inputVal.append("<child2CurrentCostMarriage>"
                + strChild2CurrentCostMarriage + "</child2CurrentCostMarriage>");

        inputVal.append("<child3Name>" + strChild3Name + "</child3Name>");
        inputVal.append("<child3Age>" + strChild3Age + "</child3Age>");
        inputVal.append("<child3AgeAtEducation>" + strChild3AgeAtEducation
                + "</child3AgeAtEducation>");
        inputVal.append("<child3CorpusEducation>" + strChild3CorpusEducation
                + "</child3CorpusEducation>");
        inputVal.append("<child3CurrentCostEducation>"
                + strChild3CurrentCostEducation
                + "</child3CurrentCostEducation>");
        inputVal.append("<child3AgeAtMarriage>" + strChild3AgeAtMarriage
                + "</child3AgeAtMarriage>");
        inputVal.append("<child3CorpusMarriage>" + strChild3CorpusMarriage
                + "</child3CorpusMarriage>");
        inputVal.append("<child3CurrentCostMarriage>"
                + strChild3CurrentCostMarriage + "</child3CurrentCostMarriage>");

        inputVal.append("<child4Name>" + strChild4Name + "</child4Name>");
        inputVal.append("<child4Age>" + strChild4Age + "</child4Age>");
        inputVal.append("<child4AgeAtEducation>" + strChild4AgeAtEducation
                + "</child4AgeAtEducation>");
        inputVal.append("<child4CorpusEducation>" + strChild4CorpusEducation
                + "</child4CorpusEducation>");
        inputVal.append("<child4CurrentCostEducation>"
                + strChild4CurrentCostEducation
                + "</child4CurrentCostEducation>");
        inputVal.append("<child4AgeAtMarriage>" + strChild4AgeAtMarriage
                + "</child4AgeAtMarriage>");
        inputVal.append("<child4CorpusMarriage>" + strChild4CorpusMarriage
                + "</child4CorpusMarriage>");
        inputVal.append("<child4CurrentCostMarriage>"
                + strChild4CurrentCostMarriage + "</child4CurrentCostMarriage>");

        inputVal.append("</needAnalysis>");
    }

    // public void insertDataIntoDatabase() {
    //
    // M_Need_Analysis_Calculator data = new M_Need_Analysis_Calculator(
    // new String(inputVal), strMonthlyIncome, strYearlyIncome,
    // strMonthlyIncome_emi, stryearlyIncome_emi,
    // strCurrentLifeInsuranceCoverage, strOutstandingHomeLoan,
    // strOutstandingHomeLoanother, strRetirementCurrentCorpus,
    // strRetirementNoOfRealise, strRetirementLifeStyle,
    // strNoOfYearBuyHome, strCorpusForhome, strCurrentCosthome,
    // strNoOfYearOthergoal, strCorpusForOther, strCurrentCostOther,
    // createdBy, createdDate, modifiedBy, modifiedDate, isSync,
    // isFlag1, isFlag2);
    //
    // db.insertNeedAnalysisDetail(data, unique_no);
    // count = 1;
    // }

    private void GetOutput() {

        mProgressDialog = new ProgressDialog(this,
                ProgressDialog.THEME_HOLO_LIGHT);
        // String Message =
        // "This is a one time registration process, please wait till gets complete.Please Do not Touch Anywhere";
        String Message = "Please wait ,Loading...";

        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        asyncdownload.cancel(true);
                        mProgressDialog.dismiss();
                    }
                });

        mProgressDialog.setMax(100);
        mProgressDialog.show();

        asyncdownload = new AsyncDownload();
        asyncdownload.execute();

    }

    class AsyncDownload extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;

        protected String doInBackground(String... param) {
            /*
             * SoapObject request = new SoapObject(NAMESPACE,
             * METHOD_NAME_NEED_ANALYSIS);
             */
            // String strValue =
            // "<NeedAn><PrGe>F</PrGe><PrAge>30</PrAge><PrMarrSt>Married</PrMarrSt><PrDependCh>1</PrDependCh><PrAnnIncome>1500000</PrAnnIncome><PrAnnExp>200000</PrAnnExp><PrOutStaHmLoanAmt>2000000</PrOutStaHmLoanAmt><PrOutStaOthrLoanAmt>500000</PrOutStaOthrLoanAmt><currLifeInCov>2500000</currLifeInCov><currRetCorpSav>500000</currRetCorpSav><PrRetAge>60</PrRetAge><PrPostRetLife>Luxury</PrPostRetLife><PrEduChAge1>1</PrEduChAge1><PrEduChBudExpNoYrs1>18</PrEduChBudExpNoYrs1><PrEduChBudEXp1>100000</PrEduChBudEXp1><PrEduChCurrEXp1>500000</PrEduChCurrEXp1><PrEduChAge2>0</PrEduChAge2><PrEduChBudExpNoYrs2>0</PrEduChBudExpNoYrs2><PrEduChBudEXp2>0</PrEduChBudEXp2><PrEduChCurrEXp2>0</PrEduChCurrEXp2><PrEduChAge3>0</PrEduChAge3><PrEduChBudExpNoYrs3>0</PrEduChBudExpNoYrs3><PrEduChBudEXp3>0</PrEduChBudEXp3><PrEduChCurrEXp3>0</PrEduChCurrEXp3><PrEduChAge4>0</PrEduChAge4><PrEduChBudExpNoYrs4>0</PrEduChBudExpNoYrs4><PrEduChBudEXp4>0</PrEduChBudEXp4><PrEduChCurrEXp4>0</PrEduChCurrEXp4><PrMaChBudExpNoYrs1>25</PrMaChBudExpNoYrs1><PrMaChBudEXp1>200000</PrMaChBudEXp1><PrMaChCurrEXp1>500000</PrMaChCurrEXp1><PrMaChCurrExpNoYrs2>0</PrMaChCurrExpNoYrs2><PrMaChBudEXp2>0</PrMaChBudEXp2><PrMaChCurrEXp2>0</PrMaChCurrEXp2><PrMaChBudExpNoYrs3>0</PrMaChBudExpNoYrs3><PrMaChBudEXp3>0</PrMaChBudEXp3><PrMaChCurrEXp3>0</PrMaChCurrEXp3><PrMaChBudExpNoYrs4>0</PrMaChBudExpNoYrs4><PrMaChBudEXp4>0</PrMaChBudEXp4><PrMaChCurrEXp4>0</PrMaChCurrEXp4><PrProWeCrNoofYr>15</PrProWeCrNoofYr><PrProWeCrBudExp>200000</PrProWeCrBudExp><PrProWeCrCurrExp>5000000</PrProWeCrCurrExp><PrOthFinWeCrNoofYr>4</PrOthFinWeCrNoofYr><PrOthFinWeCrBudExp>30000</PrOthFinWeCrBudExp><PrOthFinWeCrCurrExp>300000</PrOthFinWeCrCurrExp></NeedAn>";
            if (strNoOfYearBuyHome.equals("")) {
                strNoOfYearBuyHome = "0";
            }

            if (strNoOfYearOthergoal.equals("")) {
                strNoOfYearOthergoal = "0";

            }
            String strValue = "<NeedAn>" + "<PrGe>" + str_gender + "</PrGe>"
                    + "<PrAge>" + str_age + "</PrAge>" + "<PrMarrSt>"
                    + str_marital_status + "</PrMarrSt>" + "<PrDependCh>"
                    + str_no_of_child + "</PrDependCh>" + "<PrAnnIncome>"
                    + strYearlyIncome + "</PrAnnIncome>" + "<PrAnnExp>"
                    + stryearlyIncome_emi + "</PrAnnExp>"
                    + "<PrOutStaHmLoanAmt>" + strOutstandingHomeLoan
                    + "</PrOutStaHmLoanAmt>" + "<PrOutStaOthrLoanAmt>"
                    + strOutstandingHomeLoanother + "</PrOutStaOthrLoanAmt>"
                    + "<currLifeInCov>" + strCurrentLifeInsuranceCoverage
                    + "</currLifeInCov>" + "<currRetCorpSav>"
                    + strRetirementCurrentCorpus + "</currRetCorpSav>"
                    + "<PrRetAge>" + strRetirementNoOfRealise + "</PrRetAge>"
                    + "<PrPostRetLife>" + strRetirementLifeStyle
                    + "</PrPostRetLife>" + "<PrEduChAge1>" + strChild1Age
                    + "</PrEduChAge1>" + "<PrEduChBudExpNoYrs1>"
                    + strChild1AgeAtEducation + "</PrEduChBudExpNoYrs1>"
                    + "<PrEduChBudEXp1>" + strChild1CorpusEducation
                    + "</PrEduChBudEXp1>" + "<PrEduChCurrEXp1>"
                    + strChild1CurrentCostEducation + "</PrEduChCurrEXp1>"
                    + "<PrEduChAge2>" + strChild2Age + "</PrEduChAge2>"
                    + "<PrEduChBudExpNoYrs2>" + strChild2AgeAtEducation
                    + "</PrEduChBudExpNoYrs2>" + "<PrEduChBudEXp2>"
                    + strChild2CorpusEducation + "</PrEduChBudEXp2>"
                    + "<PrEduChCurrEXp2>" + strChild2CurrentCostEducation
                    + "</PrEduChCurrEXp2>" + "<PrEduChAge3>" + strChild3Age
                    + "</PrEduChAge3>" + "<PrEduChBudExpNoYrs3>"
                    + strChild3AgeAtEducation + "</PrEduChBudExpNoYrs3>"
                    + "<PrEduChBudEXp3>" + strChild3CorpusEducation
                    + "</PrEduChBudEXp3>" + "<PrEduChCurrEXp3>"
                    + strChild3CurrentCostEducation + "</PrEduChCurrEXp3>"
                    + "<PrEduChAge4>" + strChild4Age + "</PrEduChAge4>"
                    + "<PrEduChBudExpNoYrs4>" + strChild4AgeAtEducation
                    + "</PrEduChBudExpNoYrs4>" + "<PrEduChBudEXp4>"
                    + strChild4CorpusEducation + "</PrEduChBudEXp4>"
                    + "<PrEduChCurrEXp4>" + strChild4CurrentCostEducation
                    + "</PrEduChCurrEXp4>" + "<PrMaChBudExpNoYrs1>"
                    + strChild1AgeAtMarriage + "</PrMaChBudExpNoYrs1>"
                    + "<PrMaChBudEXp1>" + strChild1CorpusMarriage
                    + "</PrMaChBudEXp1>" + "<PrMaChCurrEXp1>"
                    + strChild1CurrentCostMarriage + "</PrMaChCurrEXp1>"
                    + "<PrMaChCurrExpNoYrs2>" + strChild2AgeAtMarriage
                    + "</PrMaChCurrExpNoYrs2>" + "<PrMaChBudEXp2>"
                    + strChild2CorpusMarriage + "</PrMaChBudEXp2>"
                    + "<PrMaChCurrEXp2>" + strChild2CurrentCostMarriage
                    + "</PrMaChCurrEXp2>" + "<PrMaChBudExpNoYrs3>"
                    + strChild3AgeAtMarriage + "</PrMaChBudExpNoYrs3>"
                    + "<PrMaChBudEXp3>" + strChild3CorpusMarriage
                    + "</PrMaChBudEXp3>" + "<PrMaChCurrEXp3>"
                    + strChild3CurrentCostMarriage + "</PrMaChCurrEXp3>"
                    + "<PrMaChBudExpNoYrs4>" + strChild4AgeAtMarriage
                    + "</PrMaChBudExpNoYrs4>" + "<PrMaChBudEXp4>"
                    + strChild4CorpusMarriage + "</PrMaChBudEXp4>"
                    + "<PrMaChCurrEXp4>" + strChild4CurrentCostMarriage
                    + "</PrMaChCurrEXp4>" + "<PrProWeCrNoofYr>"
                    + strNoOfYearBuyHome + "</PrProWeCrNoofYr>"
                    + "<PrProWeCrBudExp>" + strCorpusForhome
                    + "</PrProWeCrBudExp>" + "<PrProWeCrCurrExp>"
                    + strCurrentCosthome + "</PrProWeCrCurrExp>"
                    + "<PrOthFinWeCrNoofYr>" + strNoOfYearOthergoal
                    + "</PrOthFinWeCrNoofYr>" + "<PrOthFinWeCrBudExp>"
                    + strCorpusForOther + "</PrOthFinWeCrBudExp>"
                    + "<PrOthFinWeCrCurrExp>" + strCurrentCostOther
                    + "</PrOthFinWeCrCurrExp>"

                    + "<Inflation>" + str_inflation_assumed.replace("%", "")
                    + "</Inflation>"

                    + "<RiskApp>" + str_investment_approach + "</RiskApp>"
                    + "</NeedAn>";

            // request.addProperty("strInput", strValue);
            // request.addProperty("reqFrom", "CON");
            //
            // SoapSerializationEnvelope envelope = new
            // SoapSerializationEnvelope(
            // SoapEnvelope.VER11);
            // envelope.dotNet = true;
            //
            // envelope.setOutputSoapObject(request);
            //
            // allowAllSSL();
            //
            // StrictMode.ThreadPolicy policy = new
            // StrictMode.ThreadPolicy.Builder()
            // .permitAll().build();
            // StrictMode.setThreadPolicy(policy);
            //
            // HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
            try {
                // androidHttpTranport.call(SOAP_ACTION_NEED_ANALYSIS,
                // envelope);
                // Object response = envelope.getResponse();

                NeedAnalysisBusinessLogic needAnalysisBusinessLogic = new NeedAnalysisBusinessLogic();

                outputlist = needAnalysisBusinessLogic
                        .getNewNeedProdRes(strValue);

                System.out.println("outputlist1:" + outputlist);
                if (!outputlist.contains("anyType{}")) {

                    // SoapPrimitive sa = null;
                    try {
                        // sa = (SoapPrimitive) envelope.getResponse();

                        // outputlist = sa.toString();

                        ParseXML prsObj = new ParseXML();

                        outputlist = prsObj.parseXmlTag(outputlist, "NeedAn");
                        // outputlist = new ParseXML().parseXmlTag(outputlist,
                        // "ScreenData");
                        strErrorCOde = outputlist;

                        if (strErrorCOde != null) {
                            // outputlist = prsObj.parseXmlTag(outputlist,
                            // "NeedAn");
                            /* Start of Summary */
                            // protection
                            LifeProtCovCorpus = new ParseXML().parseXmlTag(
                                    outputlist, "LifeProtCurrCoverAmt");
                            LifeProtGap = new ParseXML().parseXmlTag(
                                    outputlist, "LifeProtGapAmt");

                            LifeProtCovCorpus_ = Double
                                    .parseDouble(LifeProtCovCorpus);
                            LifeProtGap_ = Double.parseDouble(LifeProtGap);
                            LifeProtTarget_ = LifeProtCovCorpus_ + LifeProtGap_;
                            // Retirement

                            PensionTotReqCurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist, "RetCurrSav");
                            PensionActAnnInvGap = new ParseXML().parseXmlTag(
                                    outputlist, "RetGap");

                            PensionTotReqCurCorpus_ = Double
                                    .parseDouble(PensionTotReqCurCorpus);
                            PensionActAnnInvGap_ = Double
                                    .parseDouble(PensionActAnnInvGap);
                            PensionActTarget_ = PensionTotReqCurCorpus_
                                    + PensionActAnnInvGap_;

                            // Child Future Planning

                            // child 1 education

                            String Child_1_edu_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chEdufvCurSaving1");
                            String Child_1_edu_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chEduCorRe1");
                            Child_1_edu_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chEduGap1");

                            double Child_1_edu_CurCorpus_ = Double
                                    .parseDouble(Child_1_edu_CurCorpus);
                            double Child_1_edu_Target_ = Double
                                    .parseDouble(Child_1_edu_Target);
                            double Child_1_edu_Gap_ = Double
                                    .parseDouble(Child_1_edu_Gap);

                            // child 1 marriage

                            String Child_1_mar_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chMarfvCurSaving1");
                            String Child_1_mar_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chMarCorRe1");
                            Child_1_mar_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chMarGap1");

                            double Child_1_mar_CurCorpus_ = Double
                                    .parseDouble(Child_1_mar_CurCorpus);
                            double Child_1_mar_Target_ = Double
                                    .parseDouble(Child_1_mar_Target);
                            double Child_1_mar_Gap_ = Double
                                    .parseDouble(Child_1_mar_Gap);

                            // child 2 education

                            String Child_2_edu_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chEdufvCurSaving2");
                            String Child_2_edu_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chEduCorRe2");
                            Child_2_edu_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chEduGap2");

                            double Child_2_edu_CurCorpus_ = Double
                                    .parseDouble(Child_2_edu_CurCorpus);
                            double Child_2_edu_Target_ = Double
                                    .parseDouble(Child_2_edu_Target);
                            double Child_2_edu_Gap_ = Double
                                    .parseDouble(Child_2_edu_Gap);

                            // child 2 marriage

                            String Child_2_mar_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chMarfvCurSaving2");
                            String Child_2_mar_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chMarCorRe2");
                            Child_2_mar_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chMarGap2");

                            double Child_2_mar_CurCorpus_ = Double
                                    .parseDouble(Child_2_mar_CurCorpus);
                            double Child_2_mar_Target_ = Double
                                    .parseDouble(Child_2_mar_Target);
                            double Child_2_mar_Gap_ = Double
                                    .parseDouble(Child_2_mar_Gap);

                            // child 3 education

                            String Child_3_edu_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chEdufvCurSaving3");
                            String Child_3_edu_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chEduCorRe3");
                            Child_3_edu_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chEduGap3");

                            double Child_3_edu_CurCorpus_ = Double
                                    .parseDouble(Child_3_edu_CurCorpus);
                            double Child_3_edu_Target_ = Double
                                    .parseDouble(Child_3_edu_Target);
                            double Child_3_edu_Gap_ = Double
                                    .parseDouble(Child_3_edu_Gap);

                            // child 3 marriage

                            String Child_3_mar_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chMarfvCurSaving3");
                            String Child_3_mar_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chMarCorRe3");
                            Child_3_mar_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chMarGap3");

                            double Child_3_mar_CurCorpus_ = Double
                                    .parseDouble(Child_3_mar_CurCorpus);
                            double Child_3_mar_Target_ = Double
                                    .parseDouble(Child_3_mar_Target);
                            double Child_3_mar_Gap_ = Double
                                    .parseDouble(Child_3_mar_Gap);

                            // child 4 education

                            String Child_4_edu_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chEdufvCurSaving4");
                            String Child_4_edu_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chEduCorRe4");
                            Child_4_edu_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chEduGap4");

                            double Child_4_edu_CurCorpus_ = Double
                                    .parseDouble(Child_4_edu_CurCorpus);
                            double Child_4_edu_Target_ = Double
                                    .parseDouble(Child_4_edu_Target);
                            double Child_4_edu_Gap_ = Double
                                    .parseDouble(Child_4_edu_Gap);

                            // child 4 marriage

                            String Child_4_mar_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chMarfvCurSaving4");
                            String Child_4_mar_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chMarCorRe4");
                            Child_4_mar_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chMarGap4");

                            double Child_4_mar_CurCorpus_ = Double
                                    .parseDouble(Child_4_mar_CurCorpus);
                            double Child_4_mar_Target_ = Double
                                    .parseDouble(Child_4_mar_Target);
                            double Child_4_mar_Gap_ = Double
                                    .parseDouble(Child_4_mar_Gap);

                            ch_total_target = Child_1_edu_Target_
                                    + Child_1_mar_Target_ + Child_2_edu_Target_
                                    + Child_2_mar_Target_ + Child_3_edu_Target_
                                    + Child_3_mar_Target_ + Child_4_edu_Target_
                                    + Child_4_mar_Target_;

                            ch_total_corpus = Child_1_edu_CurCorpus_
                                    + Child_1_mar_CurCorpus_
                                    + Child_2_edu_CurCorpus_
                                    + Child_2_mar_CurCorpus_
                                    + Child_3_edu_CurCorpus_
                                    + Child_3_mar_CurCorpus_
                                    + Child_4_edu_CurCorpus_
                                    + Child_4_mar_CurCorpus_;
                            ch_total_gap = Child_1_edu_Gap_ + Child_1_mar_Gap_
                                    + Child_2_edu_Gap_ + Child_2_mar_Gap_
                                    + Child_3_edu_Gap_ + Child_3_mar_Gap_
                                    + Child_4_edu_Gap_ + Child_4_mar_Gap_;

                            // Wealth

                            String CostHomeTarged = new ParseXML().parseXmlTag(
                                    outputlist, "homeCorReq");
                            CostHomeCorpus = new ParseXML().parseXmlTag(
                                    outputlist, "homeCurSav");
                            CostHomeGap = new ParseXML().parseXmlTag(
                                    outputlist, "homeGap");

                            String CostOtherTarged = new ParseXML()
                                    .parseXmlTag(outputlist, "otheCorReq");
                            CostOtherCorpus = new ParseXML().parseXmlTag(
                                    outputlist, "otheCurSav");
                            CostOtherGap = new ParseXML().parseXmlTag(
                                    outputlist, "otheGap");

                            double CostHomeTarged_ = Double
                                    .parseDouble(CostHomeTarged);
                            double CostHomeCorpus_ = Double
                                    .parseDouble(CostHomeCorpus);
                            double CostHomeGap_ = Double
                                    .parseDouble(CostHomeGap);

                            double CostOtherTarged_ = Double
                                    .parseDouble(CostOtherTarged);
                            double CostOtherCorpus_ = Double
                                    .parseDouble(CostOtherCorpus);
                            double CostOtherGap_ = Double
                                    .parseDouble(CostOtherGap);

                            Wealth_total_corpus = CostHomeCorpus_
                                    + CostOtherCorpus_;
                            Wealth_total_gap = CostHomeGap_ + CostOtherGap_;
                            Wealth_total_target = CostHomeTarged_
                                    + CostOtherTarged_;

                            str_protection_cor_req = new ParseXML()
                                    .parseXmlTag(outputlist, "LifeProtCorReq");
                            retirment_cor_req = new ParseXML().parseXmlTag(
                                    outputlist, "RetCorReq");

                            /* End of Summary */

                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return strValue;

        }

        protected void onPostExecute(String result) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            if (running) {
                if (strErrorCOde != null) {

                    StackedSummaryBarChart(LifeProtCovCorpus_, LifeProtGap_,
                            LifeProtTarget_, PensionTotReqCurCorpus_,
                            PensionActAnnInvGap_, PensionActTarget_,
                            ch_total_corpus, ch_total_gap, ch_total_target,
                            Wealth_total_corpus, Wealth_total_gap,
                            Wealth_total_target);

                    /* Start of Protection */
                    // protection
                    if (str_investment_approach.equals("Conservative")) {
                        ln_trad_ulip.setVisibility(View.VISIBLE);
                        ln_ulip_trad.setVisibility(View.GONE);
                    } else {
                        ln_trad_ulip.setVisibility(View.GONE);
                        ln_ulip_trad.setVisibility(View.VISIBLE);
                    }
                    StackedProtectionBarChart(LifeProtCovCorpus_, LifeProtGap_,
                            LifeProtTarget_);

                    // proposed plan

                    String Protection_Plan = new ParseXML().parseXmlTag(outputlist,
                            "ProdLstLifeProtTrad").trim();

                    if (!Protection_Plan.contentEquals("")) {

                        if (!Protection_Plan.contentEquals("null")) {

                            String[] str_Pro = Protection_Plan.split("/");
                            for (int i = 0; i < str_Pro.length; i++) {

                                String str_Name = str_Pro[i].trim();
                                String[] str_Name_split = str_Name.trim()
                                        .split(";");
                                String str_Code = str_Name_split[1]
                                        .trim();
                                protection_prod_list.add(new SuggestedProdList(
                                        str_Code, str_Name_split[2]
                                        .trim(), str_Name_split[0].trim(), false));

                                if (str_usertype.equalsIgnoreCase("IMF")) {

                                    if (str_Code.contains("35")
                                            || str_Code.contains("1B")
                                            || str_Code.contains("1N")
                                            || str_Code.contains("1P")
                                            || str_Code.contains("1R")
                                            || str_Code.contains("2D")
                                            || str_Code.contains("1W")
                                            || str_Code.contains("2E") || str_Code.contains("2G") || str_Code.contains("2J")) {

                                        suggested_prod_list
                                                .add(new SuggestedProdList(
                                                        str_Code,
                                                        str_Name_split[2]
                                                                .trim(),
                                                        str_Name_split[0]
                                                                .trim(), false));
                                    }

                                } else {
                                    if (str_Code.equalsIgnoreCase("1K") || str_Code.equalsIgnoreCase("2F")) {
                                        //combo1_flag_presnt = true;
                                        //combo3_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("2D") || str_Code.equalsIgnoreCase("2F")) {
                                        //combo2_flag_presnt = true;
                                        //combo4_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("1H") || str_Code.equalsIgnoreCase("2F")) {
                                        //combo5_flag_presnt = true;

                                    }
                                    suggested_prod_list
                                            .add(new SuggestedProdList(
                                                    str_Code, str_Name_split[2].trim(),
                                                    str_Name_split[0].trim(),
                                                    false));
                                }
                                traditional_prod_list
                                        .add(new SuggestedProdList(str_Code,
                                                str_Name_split[2]
                                                        .trim(),
                                                str_Name_split[0]
                                                        .trim(), false));

                                if (str_Code.contains("47")) {
                                    txt_pro_saral_shield
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("1G")) {
                                    txt_pro_eshield.setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("45")) {
                                    txt_pro_smart_shield
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("1F")) {
                                    txt_pro_grameen_bima
                                            .setVisibility(View.VISIBLE);
                                }

                            }
                        }
                    }
                    /* End of Protection */

                    // Child Future Planning

                    // child 1 education

                    Child_1_edu_CurCorpus = new ParseXML().parseXmlTag(
                            outputlist, "chEdufvCurSaving1");
                    String Child_1_edu_Target = new ParseXML().parseXmlTag(
                            outputlist, "chEduCorRe1");
                    Child_1_edu_Gap = new ParseXML().parseXmlTag(outputlist,
                            "chEduGap1");

                    double Child_1_edu_CurCorpus_ = Double
                            .parseDouble(Child_1_edu_CurCorpus);
                    double Child_1_edu_Target_ = Double
                            .parseDouble(Child_1_edu_Target);
                    double Child_1_edu_Gap_ = Double
                            .parseDouble(Child_1_edu_Gap);

                    // child 1 marriage

                    Child_1_mar_CurCorpus = new ParseXML().parseXmlTag(
                            outputlist, "chMarfvCurSaving1");
                    String Child_1_mar_Target = new ParseXML().parseXmlTag(
                            outputlist, "chMarCorRe1");
                    Child_1_mar_Gap = new ParseXML().parseXmlTag(outputlist,
                            "chMarGap1");

                    double Child_1_mar_CurCorpus_ = Double
                            .parseDouble(Child_1_mar_CurCorpus);
                    double Child_1_mar_Target_ = Double
                            .parseDouble(Child_1_mar_Target);
                    double Child_1_mar_Gap_ = Double
                            .parseDouble(Child_1_mar_Gap);

                    // child 2 education

                    Child_2_edu_CurCorpus = new ParseXML().parseXmlTag(
                            outputlist, "chEdufvCurSaving2");
                    String Child_2_edu_Target = new ParseXML().parseXmlTag(
                            outputlist, "chEduCorRe2");
                    Child_2_edu_Gap = new ParseXML().parseXmlTag(outputlist,
                            "chEduGap2");

                    double Child_2_edu_CurCorpus_ = Double
                            .parseDouble(Child_2_edu_CurCorpus);
                    double Child_2_edu_Target_ = Double
                            .parseDouble(Child_2_edu_Target);
                    double Child_2_edu_Gap_ = Double
                            .parseDouble(Child_2_edu_Gap);

                    // child 2 marriage

                    Child_2_mar_CurCorpus = new ParseXML().parseXmlTag(
                            outputlist, "chMarfvCurSaving2");
                    String Child_2_mar_Target = new ParseXML().parseXmlTag(
                            outputlist, "chMarCorRe2");
                    Child_2_mar_Gap = new ParseXML().parseXmlTag(outputlist,
                            "chMarGap2");

                    double Child_2_mar_CurCorpus_ = Double
                            .parseDouble(Child_2_mar_CurCorpus);
                    double Child_2_mar_Target_ = Double
                            .parseDouble(Child_2_mar_Target);
                    double Child_2_mar_Gap_ = Double
                            .parseDouble(Child_2_mar_Gap);

                    // child 3 education

                    Child_3_edu_CurCorpus = new ParseXML().parseXmlTag(
                            outputlist, "chEdufvCurSaving3");
                    String Child_3_edu_Target = new ParseXML().parseXmlTag(
                            outputlist, "chEduCorRe3");
                    Child_3_edu_Gap = new ParseXML().parseXmlTag(outputlist,
                            "chEduGap3");

                    double Child_3_edu_CurCorpus_ = Double
                            .parseDouble(Child_3_edu_CurCorpus);
                    double Child_3_edu_Target_ = Double
                            .parseDouble(Child_3_edu_Target);
                    double Child_3_edu_Gap_ = Double
                            .parseDouble(Child_3_edu_Gap);

                    // child 3 marriage

                    Child_3_mar_CurCorpus = new ParseXML().parseXmlTag(
                            outputlist, "chMarfvCurSaving3");
                    String Child_3_mar_Target = new ParseXML().parseXmlTag(
                            outputlist, "chMarCorRe3");
                    Child_3_mar_Gap = new ParseXML().parseXmlTag(outputlist,
                            "chMarGap3");

                    double Child_3_mar_CurCorpus_ = Double
                            .parseDouble(Child_3_mar_CurCorpus);
                    double Child_3_mar_Target_ = Double
                            .parseDouble(Child_3_mar_Target);
                    double Child_3_mar_Gap_ = Double
                            .parseDouble(Child_3_mar_Gap);

                    // child 4 education

                    Child_4_edu_CurCorpus = new ParseXML().parseXmlTag(
                            outputlist, "chEdufvCurSaving4");
                    String Child_4_edu_Target = new ParseXML().parseXmlTag(
                            outputlist, "chEduCorRe4");
                    Child_4_edu_Gap = new ParseXML().parseXmlTag(outputlist,
                            "chEduGap4");

                    double Child_4_edu_CurCorpus_ = Double
                            .parseDouble(Child_4_edu_CurCorpus);
                    double Child_4_edu_Target_ = Double
                            .parseDouble(Child_4_edu_Target);
                    double Child_4_edu_Gap_ = Double
                            .parseDouble(Child_4_edu_Gap);

                    // child 4 marriage

                    Child_4_mar_CurCorpus = new ParseXML().parseXmlTag(
                            outputlist, "chMarfvCurSaving4");
                    String Child_4_mar_Target = new ParseXML().parseXmlTag(
                            outputlist, "chMarCorRe4");
                    Child_4_mar_Gap = new ParseXML().parseXmlTag(outputlist,
                            "chMarGap4");

                    double Child_4_mar_CurCorpus_ = Double
                            .parseDouble(Child_4_mar_CurCorpus);
                    double Child_4_mar_Target_ = Double
                            .parseDouble(Child_4_mar_Target);
                    double Child_4_mar_Gap_ = Double
                            .parseDouble(Child_4_mar_Gap);

                    StackedChildBarChart(Child_1_edu_CurCorpus_,
                            Child_1_edu_Gap_, Child_1_edu_Target_,
                            Child_1_mar_CurCorpus_, Child_1_mar_Gap_,
                            Child_1_mar_Target_, Child_2_edu_CurCorpus_,
                            Child_2_edu_Gap_, Child_2_edu_Target_,
                            Child_2_mar_CurCorpus_, Child_2_mar_Gap_,
                            Child_2_mar_Target_, Child_3_edu_CurCorpus_,
                            Child_3_edu_Gap_, Child_3_edu_Target_,
                            Child_3_mar_CurCorpus_, Child_3_mar_Gap_,
                            Child_3_mar_Target_, Child_4_edu_CurCorpus_,
                            Child_4_edu_Gap_, Child_4_edu_Target_,
                            Child_4_mar_CurCorpus_, Child_4_mar_Gap_,
                            Child_4_mar_Target_);

                    /* Start of Retirement */

                    // Retirement

                    PensionTotReqCurCorpus = new ParseXML().parseXmlTag(
                            outputlist, "RetCurrSav");
                    PensionActAnnInvGap = new ParseXML().parseXmlTag(
                            outputlist, "RetGap");

                    double PensionTotReqCurCorpus_ = Double
                            .parseDouble(PensionTotReqCurCorpus);
                    double PensionActAnnInvGap_ = Double
                            .parseDouble(PensionActAnnInvGap);
                    double PensionActTarget_ = PensionTotReqCurCorpus_
                            + PensionActAnnInvGap_;

                    StackedRetirementBarChart(PensionTotReqCurCorpus_,
                            PensionActAnnInvGap_, PensionActTarget_);

                    // // pension plan
                    //
                    // Pension_Plan = new ParseXML().parseXmlTag(outputlist,
                    // "ProdLstRet").trim();
                    //
                    // if (!Pension_Plan.contentEquals("")) {
                    //
                    // if (!Pension_Plan.contentEquals("null")) {
                    //
                    // String[] str_Pro = Pension_Plan.split("/");
                    // for (int i = 0; i < str_Pro.length; i++) {
                    // String str_Name = str_Pro[i].toString().trim();
                    // String[] str_Name_split = str_Name.trim()
                    // .split(";");
                    // String str_Code = str_Name_split[1].toString()
                    // .trim();
                    // if (str_Code.contains("1E")) {
                    // txt_ret_saral_pension
                    // .setVisibility(View.VISIBLE);
                    // } else if (str_Code.contains("1H")) {
                    // txt_ret_retire_smart
                    // .setVisibility(View.VISIBLE);
                    // } else if (str_Code.contains("22")) {
                    // txt_ret_annuity_plus
                    // .setVisibility(View.VISIBLE);
                    // }
                    //
                    // }
                    // }

                    // Retirement
                    String Retirement_Trad_Plan = new ParseXML().parseXmlTag(
                            outputlist, "ProdLstRetTrad").trim();
                    String Retirement_ULIP_Plan = new ParseXML().parseXmlTag(
                            outputlist, "ProdLstRetUnit").trim();

                    if (!Retirement_Trad_Plan.contentEquals("")) {

                        if (!Retirement_Trad_Plan.contentEquals("null")) {

                            String[] str_Pro = Retirement_Trad_Plan.split("/");
                            for (int i = 0; i < str_Pro.length; i++) {
                                String str_Name = str_Pro[i].trim();
                                String[] str_Name_split = str_Name.trim()
                                        .split(";");
                                String str_Code = str_Name_split[1]
                                        .trim();
                                retirement_prod_list.add(new SuggestedProdList(
                                        str_Code, str_Name_split[2]
                                        .trim(), str_Name_split[0].trim(), false));

                                if (str_usertype.equalsIgnoreCase("IMF")) {

                                    if (str_Code.contains("35")
                                            || str_Code.contains("1B")
                                            || str_Code.contains("1N")
                                            || str_Code.contains("1P")
                                            || str_Code.contains("1R")
                                            || str_Code.contains("2D")
                                            || str_Code.contains("1W")
                                            || str_Code.contains("2E") || str_Code.contains("2G") || str_Code.contains("2J")) {

                                        suggested_prod_list
                                                .add(new SuggestedProdList(
                                                        str_Code,
                                                        str_Name_split[2]
                                                                .trim(),
                                                        str_Name_split[0]
                                                                .trim(), false));
                                    }

                                } else {
                                   /* if (str_Code.equalsIgnoreCase("1K") || str_Code.equalsIgnoreCase("2F")) {
                                        combo1_flag_presnt = true;
                                        combo3_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("2D") || str_Code.equalsIgnoreCase("2F")) {
                                        combo2_flag_presnt = true;
                                        combo4_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("1H") || str_Code.equalsIgnoreCase("2F")) {
                                        combo5_flag_presnt = true;

                                    }*/
                                    suggested_prod_list
                                            .add(new SuggestedProdList(
                                                    str_Code, str_Name_split[2].trim(),
                                                    str_Name_split[0].trim(),
                                                    false));
                                }
                                traditional_prod_list
                                        .add(new SuggestedProdList(str_Code,
                                                str_Name_split[2]
                                                        .trim(),
                                                str_Name_split[0]
                                                        .trim(), false));

                                if (str_Code.contains("1E")) {
                                    txt_saral_pension_trad_ulip_retirement
                                            .setVisibility(View.VISIBLE);

                                    txt_saral_pension_ulip_trad_retirement
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("22")) {
                                    txt_annuity_plus_trad_ulip_retirement
                                            .setVisibility(View.VISIBLE);

                                    txt_annuity_plus_ulip_trad_retirement
                                            .setVisibility(View.VISIBLE);
                                }

                            }
                        }
                    }

                    if (!Retirement_ULIP_Plan.contentEquals("")) {

                        if (!Retirement_ULIP_Plan.contentEquals("null")) {

                            String[] str_Pro = Retirement_ULIP_Plan.split("/");
                            for (int i = 0; i < str_Pro.length; i++) {
                                String str_Name = str_Pro[i].trim();
                                String[] str_Name_split = str_Name.trim()
                                        .split(";");
                                String str_Code = str_Name_split[1]
                                        .trim();
                                retirement_prod_list.add(new SuggestedProdList(
                                        str_Code, str_Name_split[2]
                                        .trim(), str_Name_split[0].trim(), false));

                                if (str_usertype.equalsIgnoreCase("IMF")) {

                                    if (str_Code.contains("35")
                                            || str_Code.contains("1B")
                                            || str_Code.contains("1N")
                                            || str_Code.contains("1P")
                                            || str_Code.contains("1R")
                                            || str_Code.contains("2D")
                                            || str_Code.contains("1W")
                                            || str_Code.contains("2E") || str_Code.contains("2G") || str_Code.contains("2J")) {

                                        suggested_prod_list
                                                .add(new SuggestedProdList(
                                                        str_Code,
                                                        str_Name_split[2]
                                                                .trim(),
                                                        str_Name_split[0]
                                                                .trim(), false));
                                    }

                                } else {
                                    if (str_Code.equalsIgnoreCase("1K") || str_Code.equalsIgnoreCase("2F")) {
                                        //combo1_flag_presnt = true;
                                        // combo3_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("2D") || str_Code.equalsIgnoreCase("2F")) {
                                        // combo2_flag_presnt = true;
                                        // combo4_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("1H") || str_Code.equalsIgnoreCase("2F")) {
                                        // combo5_flag_presnt = true;

                                    }
                                    suggested_prod_list
                                            .add(new SuggestedProdList(
                                                    str_Code, str_Name_split[2].trim(),
                                                    str_Name_split[0].trim(),
                                                    false));

                                }
                                ulip_prod_list.add(new SuggestedProdList(
                                        str_Code, str_Name_split[2]
                                        .trim(), str_Name_split[0].trim(), false));

                                if (str_Code.contains("1H")) {
                                    txt_retire_smart_trad_ulip_retirement
                                            .setVisibility(View.VISIBLE);

                                    txt_retire_smart_ulip_trad_retirement
                                            .setVisibility(View.VISIBLE);
                                }

                            }
                        }
                    }
                    /* End of Retirement */
                    /* Start of Wealth */

                    // Wealth

                    String CostHomeTarged = new ParseXML().parseXmlTag(
                            outputlist, "homeCorReq");
                    CostHomeCorpus = new ParseXML().parseXmlTag(outputlist,
                            "homeCurSav");
                    CostHomeGap = new ParseXML().parseXmlTag(outputlist,
                            "homeGap");

                    String CostOtherTarged = new ParseXML().parseXmlTag(
                            outputlist, "otheCorReq");
                    CostOtherCorpus = new ParseXML().parseXmlTag(outputlist,
                            "otheCurSav");
                    CostOtherGap = new ParseXML().parseXmlTag(outputlist,
                            "otheGap");

                    double CostHomeTarged_ = Double.parseDouble(CostHomeTarged);
                    double CostHomeCorpus_ = Double.parseDouble(CostHomeCorpus);
                    double CostHomeGap_ = Double.parseDouble(CostHomeGap);

                    double CostOtherTarged_ = Double
                            .parseDouble(CostOtherTarged);
                    double CostOtherCorpus_ = Double
                            .parseDouble(CostOtherCorpus);
                    double CostOtherGap_ = Double.parseDouble(CostOtherGap);

                    double Wealth_total_corpus = CostHomeCorpus_
                            + CostOtherCorpus_;
                    double Wealth_total_gap = CostHomeGap_ + CostOtherGap_;
                    double Wealth_total_target = CostHomeTarged_
                            + CostOtherTarged_;

                    // StackedWealthCreationBarChart(Wealth_total_corpus,
                    // Wealth_total_gap, Wealth_total_target);

                    StackedWealthCreationBarChart(CostHomeTarged_,
                            CostHomeCorpus_, CostHomeGap_, CostOtherTarged_,
                            CostOtherCorpus_, CostOtherGap_);

                    // wealth plan

                    // String Wealth_Plan = new
                    // ParseXML().parseXmlTag(outputlist,
                    // "ProdLstWealthCre").trim();

                    // Savings_ULIP_Plan = new ParseXML().parseXmlTag(
                    // outputlist, "ProdLstWealthCre").trim();
                    String Wealth_Trad_Plan = new ParseXML().parseXmlTag(outputlist,
                            "ProdLstWealthCreTrad").trim();
                    String Wealth_ULIP_Plan = new ParseXML().parseXmlTag(outputlist,
                            "ProdLstWealthCreUnit").trim();

                    if (!Wealth_Trad_Plan.contentEquals("")) {

                        if (!Wealth_Trad_Plan.contentEquals("null")) {

                            String[] str_Pro = Wealth_Trad_Plan.split("/");
                            for (int i = 0; i < str_Pro.length; i++) {
                                String str_Name = str_Pro[i].trim();
                                String[] str_Name_split = str_Name.trim()
                                        .split(";");
                                String str_Code = str_Name_split[1]
                                        .trim();
                                wealth_prod_list.add(new SuggestedProdList(
                                        str_Code, str_Name_split[2]
                                        .trim(), str_Name_split[0].trim(), false));

                                if (str_usertype.equalsIgnoreCase("IMF")) {

                                    if (str_Code.contains("35")
                                            || str_Code.contains("1B")
                                            || str_Code.contains("1N")
                                            || str_Code.contains("1P")
                                            || str_Code.contains("1R")
                                            || str_Code.contains("2D")
                                            || str_Code.contains("1W")
                                            || str_Code.contains("2E") || str_Code.contains("2G") || str_Code.contains("2J")) {

                                        suggested_prod_list
                                                .add(new SuggestedProdList(
                                                        str_Code,
                                                        str_Name_split[2]
                                                                .trim(),
                                                        str_Name_split[0]
                                                                .trim(), false));
                                    }

                                } else {
                                   /* if (str_Code.equalsIgnoreCase("1K") || str_Code.equalsIgnoreCase("2F")) {
                                        combo1_flag_presnt = true;
                                        combo3_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("2D") || str_Code.equalsIgnoreCase("2F")) {
                                        combo2_flag_presnt = true;
                                        combo4_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("1H") || str_Code.equalsIgnoreCase("2F")) {
                                        combo5_flag_presnt = true;

                                    }*/
                                    suggested_prod_list
                                            .add(new SuggestedProdList(
                                                    str_Code, str_Name_split[2].trim(),
                                                    str_Name_split[0].trim(),
                                                    false));

                                }
                                traditional_prod_list
                                        .add(new SuggestedProdList(str_Code,
                                                str_Name_split[2]
                                                        .trim(),
                                                str_Name_split[0]
                                                        .trim(), false));

                                if (str_Code.contains("1B")) {
                                    txt_wealth_smart_income_protect
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_smart_income_protect_
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("1X")) {
                                    txt_wealth_smart_guran_savings
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_smart_guran_savings_
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("1N")) {
                                    txt_wealth_smart_money_back
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_smart_money_back_
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("35")) {
                                    txt_wealth_shubh_nivesh
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_shubh_nivesh_
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("1R")) {
                                    txt_wealth_money_planner_
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_money_planner
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("1J")) {
                                    txt_wealth_saral_swadhan_plus
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_saral_swadhan_plus_
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("1Z")) {
                                    txt_wealth_smart_swadhan_plus
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_smart_swadhan_plus_
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("1W")
                                        && !str_marital_status
                                        .equalsIgnoreCase("Single")) {

                                    txt_humsafar_.setVisibility(View.VISIBLE);
                                    txt_humsafar.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    if (!Wealth_ULIP_Plan.contentEquals("")) {

                        if (!Wealth_ULIP_Plan.contentEquals("null")) {

                            String[] str_Pro = Wealth_ULIP_Plan.split("/");
                            for (int i = 0; i < str_Pro.length; i++) {
                                String str_Name = str_Pro[i].trim();
                                String[] str_Name_split = str_Name.trim()
                                        .split(";");
                                String str_Code = str_Name_split[1]
                                        .trim();
                                wealth_prod_list.add(new SuggestedProdList(
                                        str_Code, str_Name_split[2]
                                        .trim(), str_Name_split[0].trim(), false));

                                if (str_usertype.equalsIgnoreCase("IMF")) {

                                    if (str_Code.contains("35")
                                            || str_Code.contains("1B")
                                            || str_Code.contains("1N")
                                            || str_Code.contains("1P")
                                            || str_Code.contains("1R")
                                            || str_Code.contains("2D")
                                            || str_Code.contains("1W") || str_Code.contains("2E") || str_Code.contains("2G") || str_Code.contains("2J")) {

                                        suggested_prod_list
                                                .add(new SuggestedProdList(
                                                        str_Code,
                                                        str_Name_split[2]
                                                                .trim(),
                                                        str_Name_split[0]
                                                                .trim(), false));
                                    }

                                } else {
                                    /*if (str_Code.equalsIgnoreCase("1K") || str_Code.equalsIgnoreCase("2F")) {
                                        combo1_flag_presnt = true;
                                        combo3_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("2D") || str_Code.equalsIgnoreCase("2F")) {
                                        combo2_flag_presnt = true;
                                        combo4_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("1H") || str_Code.equalsIgnoreCase("2F")) {
                                        combo5_flag_presnt = true;

                                    }*/
                                    suggested_prod_list
                                            .add(new SuggestedProdList(
                                                    str_Code, str_Name_split[2].trim(),
                                                    str_Name_split[0].trim(),
                                                    false));

                                }
                                ulip_prod_list.add(new SuggestedProdList(
                                        str_Code, str_Name_split[2]
                                        .trim(), str_Name_split[0].trim(), false));

                                if (str_Code.contains("1M")) {
                                    txt_wealth_flexi_smart_plus
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_flexi_smart_plus_
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("1K")) {
                                    ll_wealth_builder
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_smart_wealth_builder
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_smart_wealth_builder_
                                            .setVisibility(View.VISIBLE);

                                    // ll_wealth_builder_
                                    // .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("55")) {
                                    txt_wealth_smart_wealth_assure
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_smart_wealth_assure_
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("50")) {
                                    txt_wealth_saral_maha
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_saral_maha_
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("51")) {
                                    txt_wealth_smart_scholar
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_smart_scholar_
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("53")) {
                                    txt_wealth_smart_elite
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_smart_elite_
                                            .setVisibility(View.VISIBLE);
                                } else if (str_Code.contains("1C")) {
                                    txt_wealth_smart_power_insu
                                            .setVisibility(View.VISIBLE);
                                    txt_wealth_smart_power_insu_
                                            .setVisibility(View.VISIBLE);
                                }

                            }
                        }
                    }

                    /* End of Wealth */

                    // proposed plan
                    String Child_Trad_Plan = new ParseXML().parseXmlTag(outputlist,
                            "ProdLstChTrad").trim();
                    String Child_ULIP_Plan = new ParseXML().parseXmlTag(outputlist,
                            "ProdLstChUnit").trim();

                    if (!Child_Trad_Plan.contentEquals("")) {

                        if (!Child_Trad_Plan.contentEquals("null")) {

                            String[] str_Pro = Child_Trad_Plan.split("/");
                            for (int i = 0; i < str_Pro.length; i++) {
                                String str_Name = str_Pro[i].trim();
                                String[] str_Name_split = str_Name.trim()
                                        .split(";");
                                String str_Code = str_Name_split[1]
                                        .trim();

                                child_prod_list.add(new SuggestedProdList(
                                        str_Code, str_Name_split[2]
                                        .trim(), str_Name_split[0].trim(), false));

                                if (str_usertype.equalsIgnoreCase("IMF")) {

                                    if (str_Code.contains("35")
                                            || str_Code.contains("1B")
                                            || str_Code.contains("1N")
                                            || str_Code.contains("1P")
                                            || str_Code.contains("1R")
                                            || str_Code.contains("2D")
                                            || str_Code.contains("1W") || str_Code.contains("2E") || str_Code.contains("2G") || str_Code.contains("2J") || str_Code.contains("2J")) {

                                        suggested_prod_list
                                                .add(new SuggestedProdList(
                                                        str_Code,
                                                        str_Name_split[2]
                                                                .trim(),
                                                        str_Name_split[0]
                                                                .trim(), false));
                                    }

                                } else {
                                   /* if (str_Code.equalsIgnoreCase("1K") || str_Code.equalsIgnoreCase("2F")) {
                                        combo1_flag_presnt = true;
                                        combo3_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("2D") || str_Code.equalsIgnoreCase("2F")) {
                                        combo2_flag_presnt = true;
                                        combo4_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("1H") || str_Code.equalsIgnoreCase("2F")) {
                                        combo5_flag_presnt = true;

                                    }*/
                                    suggested_prod_list
                                            .add(new SuggestedProdList(
                                                    str_Code, str_Name_split[2].trim(),
                                                    str_Name_split[0].trim(),
                                                    false));
                                }
                                traditional_prod_list
                                        .add(new SuggestedProdList(str_Code,
                                                str_Name_split[2]
                                                        .trim(),
                                                str_Name_split[0]
                                                        .trim(), false));
                                if (str_Code.contains("1P")) {
                                    txt_smart_champ_trad_ulip_child
                                            .setVisibility(View.VISIBLE);

                                    txt_smart_champ_ulip_trad_child
                                            .setVisibility(View.VISIBLE);
                                }

                            }
                        }
                    }

                    if (!Child_ULIP_Plan.contentEquals("")) {

                        if (!Child_ULIP_Plan.contentEquals("null")) {

                            String[] str_Pro = Child_ULIP_Plan.split("/");
                            for (int i = 0; i < str_Pro.length; i++) {
                                String str_Name = str_Pro[i].trim();
                                String[] str_Name_split = str_Name.trim()
                                        .split(";");
                                String str_Code = str_Name_split[1]
                                        .trim();

                                child_prod_list.add(new SuggestedProdList(
                                        str_Code, str_Name_split[2]
                                        .trim(), str_Name_split[0].trim(), false));

                                if (str_usertype.equalsIgnoreCase("IMF")) {

                                    if (str_Code.contains("35")
                                            || str_Code.contains("1B")
                                            || str_Code.contains("1N")
                                            || str_Code.contains("1P")
                                            || str_Code.contains("1R")
                                            || str_Code.contains("2D")
                                            || str_Code.contains("1W") || str_Code.contains("2E") || str_Code.contains("2G")) {

                                        suggested_prod_list
                                                .add(new SuggestedProdList(
                                                        str_Code,
                                                        str_Name_split[2]
                                                                .trim(),
                                                        str_Name_split[0]
                                                                .trim(), false));
                                    }

                                } else {
                                    /*if (str_Code.equalsIgnoreCase("1K") || str_Code.equalsIgnoreCase("2F")) {
                                        combo1_flag_presnt = true;
                                        combo3_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("2D") || str_Code.equalsIgnoreCase("2F")) {
                                        combo2_flag_presnt = true;
                                        combo4_flag_presnt = true;
                                    }
                                    if (str_Code.equalsIgnoreCase("1H") || str_Code.equalsIgnoreCase("2F")) {
                                        combo5_flag_presnt = true;

                                    }*/
                                    suggested_prod_list
                                            .add(new SuggestedProdList(
                                                    str_Code, str_Name_split[2].trim(),
                                                    str_Name_split[0].trim(),
                                                    false));

                                }
                                ulip_prod_list.add(new SuggestedProdList(
                                        str_Code, str_Name_split[2]
                                        .trim(), str_Name_split[0].trim(), false));
                                if (str_Code.contains("51")) {
                                    txt_smart_scholar_trad_ulip_child
                                            .setVisibility(View.VISIBLE);

                                    txt_smart_scholar_ulip_trad_child
                                            .setVisibility(View.VISIBLE);
                                }

                            }
                        }
                    }

                    LifeProtTillAge = new ParseXML().parseXmlTag(outputlist,
                            "LifeProtTillAge");

                    // Retirement
                    Ret_Monthly_inv = new ParseXML().parseXmlTag(outputlist,
                            "RetMonthInv");

                    Ret_Monthly_inv = getformatedThousandString(Integer
                            .parseInt(getRound(getStringWithout_E(Double
                                    .valueOf(Ret_Monthly_inv)))));

                    Ret_Left_Year = new ParseXML().parseXmlTag(outputlist,
                            "RetYrLeft");

                    txt_retirement_text_line
                            .setText("A monthly investment of Rs. "
                                    + Ret_Monthly_inv
                                    + " for "
                                    + Ret_Left_Year
                                    + " years  will bridge the gap for Retirement savings");

                    // Child
                    Child_1_Edu_Monthly_Cost = new ParseXML().parseXmlTag(
                            outputlist, "chEduMonInvreq1");

                    Child_1_Edu_Monthly_Cost = getformatedThousandString(Integer
                            .parseInt(getRound(getStringWithout_E(Double
                                    .valueOf(Child_1_Edu_Monthly_Cost)))));

                    // String Child_1_Edu_Left_Year =
                    // strChild1AgeAtEducation;

                    Long Child_1_Edu_Left_Year = Long
                            .parseLong(strChild1AgeAtEducation)
                            - Long.parseLong(strChild1Age);

                    txt_child1_edu_text_line.setText("You need to Save Rs. "
                            + Child_1_Edu_Monthly_Cost
                            + " per month for coming " + Child_1_Edu_Left_Year
                            + " years");

                    Child_2_Edu_Monthly_Cost = new ParseXML().parseXmlTag(
                            outputlist, "chEduMonInvreq2");

                    Child_2_Edu_Monthly_Cost = getformatedThousandString(Integer
                            .parseInt(getRound(getStringWithout_E(Double
                                    .valueOf(Child_2_Edu_Monthly_Cost)))));

                    // String Child_2_Edu_Left_Year =
                    // strChild2AgeAtEducation;

                    Long Child_2_Edu_Left_Year = Long
                            .parseLong(strChild2AgeAtEducation)
                            - Long.parseLong(strChild2Age);

                    txt_child2_edu_text_line.setText("You need to Save Rs. "
                            + Child_2_Edu_Monthly_Cost
                            + " per month for coming " + Child_2_Edu_Left_Year
                            + " years");

                    Child_3_Edu_Monthly_Cost = new ParseXML().parseXmlTag(
                            outputlist, "chEduMonInvreq3");

                    Child_3_Edu_Monthly_Cost = getformatedThousandString(Integer
                            .parseInt(getRound(getStringWithout_E(Double
                                    .valueOf(Child_3_Edu_Monthly_Cost)))));

                    Long Child_3_Edu_Left_Year = Long
                            .parseLong(strChild3AgeAtEducation)
                            - Long.parseLong(strChild3Age);

                    // String Child_3_Edu_Left_Year =
                    // strChild3AgeAtEducation;

                    txt_child3_edu_text_line.setText("You need to Save Rs. "
                            + Child_3_Edu_Monthly_Cost
                            + " per month for coming " + Child_3_Edu_Left_Year
                            + " years");

                    Child_4_Edu_Monthly_Cost = new ParseXML().parseXmlTag(
                            outputlist, "chEduMonInvreq4");

                    Child_4_Edu_Monthly_Cost = getformatedThousandString(Integer
                            .parseInt(getRound(getStringWithout_E(Double
                                    .valueOf(Child_4_Edu_Monthly_Cost)))));

                    // String Child_4_Edu_Left_Year =
                    // strChild4AgeAtEducation;

                    Long Child_4_Edu_Left_Year = Long
                            .parseLong(strChild4AgeAtEducation)
                            - Long.parseLong(strChild4Age);

                    txt_child4_edu_text_line.setText("You need to Save Rs. "
                            + Child_4_Edu_Monthly_Cost
                            + " per month for coming " + Child_4_Edu_Left_Year
                            + " years");

                    Child_1_Mrg_Monthly_Cost = new ParseXML().parseXmlTag(
                            outputlist, "chMarMonInvreq1");

                    Child_1_Mrg_Monthly_Cost = getformatedThousandString(Integer
                            .parseInt(getRound(getStringWithout_E(Double
                                    .valueOf(Child_1_Mrg_Monthly_Cost)))));

                    // String Child_1_Mrg_Left_Year =
                    // strChild1AgeAtMarriage;

                    Long Child_1_Mrg_Left_Year = Long
                            .parseLong(strChild1AgeAtMarriage)
                            - Long.parseLong(strChild1Age);

                    txt_child1_mrg_text_line.setText("You need to Save Rs. "
                            + Child_1_Mrg_Monthly_Cost
                            + " per month for coming " + Child_1_Mrg_Left_Year
                            + " years");

                    Child_2_Mrg_Monthly_Cost = new ParseXML().parseXmlTag(
                            outputlist, "chMarMonInvreq2");

                    Child_2_Mrg_Monthly_Cost = getformatedThousandString(Integer
                            .parseInt(getRound(getStringWithout_E(Double
                                    .valueOf(Child_2_Mrg_Monthly_Cost)))));

                    // String Child_2_Mrg_Left_Year =
                    // strChild2AgeAtMarriage;
                    Long Child_2_Mrg_Left_Year = Long
                            .parseLong(strChild2AgeAtMarriage)
                            - Long.parseLong(strChild2Age);

                    txt_child2_mrg_text_line.setText("You need to Save Rs. "
                            + Child_2_Mrg_Monthly_Cost
                            + " per month for coming " + Child_2_Mrg_Left_Year
                            + " years");

                    Child_3_Mrg_Monthly_Cost = new ParseXML().parseXmlTag(
                            outputlist, "chMarMonInvreq3");

                    Child_3_Mrg_Monthly_Cost = getformatedThousandString(Integer
                            .parseInt(getRound(getStringWithout_E(Double
                                    .valueOf(Child_3_Mrg_Monthly_Cost)))));
                    // String Child_3_Mrg_Left_Year =
                    // strChild3AgeAtMarriage;

                    Long Child_3_Mrg_Left_Year = Long
                            .parseLong(strChild3AgeAtMarriage)
                            - Long.parseLong(strChild3Age);

                    txt_child3_mrg_text_line.setText("You need to Save Rs. "
                            + Child_3_Mrg_Monthly_Cost
                            + "per month for coming " + Child_3_Mrg_Left_Year
                            + " years");

                    Child_4_Mrg_Monthly_Cost = new ParseXML().parseXmlTag(
                            outputlist, "chMarMonInvreq4");

                    Child_4_Mrg_Monthly_Cost = getformatedThousandString(Integer
                            .parseInt(getRound(getStringWithout_E(Double
                                    .valueOf(Child_4_Mrg_Monthly_Cost)))));

                    // String Child_4_Mrg_Left_Year =
                    // strChild4AgeAtMarriage;

                    Long Child_4_Mrg_Left_Year = Long
                            .parseLong(strChild4AgeAtMarriage)
                            - Long.parseLong(strChild4Age);

                    txt_child4_mrg_text_line.setText("You need to Save Rs. "
                            + Child_4_Mrg_Monthly_Cost
                            + " per month for coming " + Child_4_Mrg_Left_Year
                            + " years");

                    // Wealth
                    Home_Monthly_inv = new ParseXML().parseXmlTag(outputlist,
                            "homeMonInvreq");

                    Home_Monthly_inv = getformatedThousandString(Integer
                            .parseInt(getRound(getStringWithout_E(Double
                                    .valueOf(Home_Monthly_inv)))));

                    String Home_Left_Year = strNoOfYearBuyHome;

                    txt_wealth_home_text_line.setText("You need to Save Rs. "
                            + Home_Monthly_inv + " per month for coming "
                            + Home_Left_Year + " years");

                    Other_Monthly_inv = new ParseXML().parseXmlTag(outputlist,
                            "otheMonInvreq");

                    Other_Monthly_inv = getformatedThousandString(Integer
                            .parseInt(getRound(getStringWithout_E(Double
                                    .valueOf(Other_Monthly_inv)))));

                    String Other_Left_Year = strNoOfYearOthergoal;

                    txt_wealth_other_text_line.setText("You need to Save Rs. "
                            + Other_Monthly_inv + " per month for coming "
                            + Other_Left_Year + " years");

                    // Child_Plan = new ParseXML().parseXmlTag(outputlist,
                    // "ProdLstCh").trim();
                    //
                    // // if(!Child_Plan.contains("null") ||
                    // // !Child_Plan.contains(""))
                    // if (!Child_Plan.contentEquals("")) {
                    // if (!Child_Plan.contentEquals("null")) {
                    //
                    // String[] str_Pro = Child_Plan.split("/");
                    // for (int i = 0; i < str_Pro.length; i++) {
                    // String str_Name = str_Pro[i].toString()
                    // .trim();
                    // String[] str_Name_split = str_Name.trim()
                    // .split(";");
                    // String str_Code = str_Name_split[1]
                    // .toString().trim();
                    // if (str_Code.contains("1P")) {
                    // txt_child_smart_champ
                    // .setVisibility(View.VISIBLE);
                    // } else if (str_Code.contains("51")) {
                    // txt_child_scholar
                    // .setVisibility(View.VISIBLE);
                    // }
                    // }
                    // }
                    // }
                    str_need_analysis = "1";

                    // if group = other then hide view suggested plans
                    if (str_group.equalsIgnoreCase("Other")) {
                        iv_protection.setVisibility(View.GONE);
                        iv_retirement.setVisibility(View.GONE);
                        iv_child.setVisibility(View.GONE);
                        iv_wealthCreation.setVisibility(View.GONE);
                    } else {
                        iv_protection.setVisibility(View.VISIBLE);
                        iv_retirement.setVisibility(View.VISIBLE);
                        iv_child.setVisibility(View.VISIBLE);
                        iv_wealthCreation.setVisibility(View.VISIBLE);
                    }

                }
            }
        }

        public void onSummary(View v) {
            ll_summary.setVisibility(View.VISIBLE);
            ll_protection.setVisibility(View.GONE);
            ll_retirement.setVisibility(View.GONE);
            ll_child.setVisibility(View.GONE);
            ll_wealth_creation.setVisibility(View.GONE);

            // iv_summary.setImageDrawable(getResources().getDrawable(
            // R.drawable.summary_clicked));
            //
            // iv_protection.setImageDrawable(getResources().getDrawable(
            // R.drawable.protection_nonclicked));
            // iv_retirement.setImageDrawable(getResources().getDrawable(
            // R.drawable.retirement_nonclicked));
            // iv_child.setImageDrawable(getResources().getDrawable(
            // R.drawable.child_nonclicked));
            // iv_wealthCreation.setImageDrawable(getResources().getDrawable(
            // R.drawable.wealth_creation_nonclicked));
        }

        public void onProtection(View v) {
            ll_summary.setVisibility(View.GONE);
            ll_protection.setVisibility(View.VISIBLE);
            ll_retirement.setVisibility(View.GONE);
            ll_child.setVisibility(View.GONE);
            ll_wealth_creation.setVisibility(View.GONE);

            // iv_summary.setImageDrawable(getResources().getDrawable(
            // R.drawable.summary));
            //
            // iv_protection.setImageDrawable(getResources().getDrawable(
            // R.drawable.protection_clicked));
            // iv_retirement.setImageDrawable(getResources().getDrawable(
            // R.drawable.retirement_nonclicked));
            // iv_child.setImageDrawable(getResources().getDrawable(
            // R.drawable.child_nonclicked));
            // iv_wealthCreation.setImageDrawable(getResources().getDrawable(
            // R.drawable.wealth_creation_nonclicked));

            /* Start of Protection */
            // protection
            if (str_investment_approach.equals("Conservative")) {
                ln_trad_ulip.setVisibility(View.VISIBLE);
                ln_ulip_trad.setVisibility(View.GONE);
            } else {
                ln_trad_ulip.setVisibility(View.GONE);
                ln_ulip_trad.setVisibility(View.VISIBLE);
            }

        }

        public void onRetirement(View v) {
            ll_summary.setVisibility(View.GONE);
            ll_protection.setVisibility(View.GONE);
            ll_retirement.setVisibility(View.VISIBLE);
            ll_child.setVisibility(View.GONE);
            ll_wealth_creation.setVisibility(View.GONE);

            // iv_summary.setImageDrawable(getResources().getDrawable(
            // R.drawable.summary));
            //
            // iv_protection.setImageDrawable(getResources().getDrawable(
            // R.drawable.protection_nonclicked));
            // iv_retirement.setImageDrawable(getResources().getDrawable(
            // R.drawable.retirement_clicked));
            // iv_child.setImageDrawable(getResources().getDrawable(
            // R.drawable.child_nonclicked));
            // iv_wealthCreation.setImageDrawable(getResources().getDrawable(
            // R.drawable.wealth_creation_nonclicked));
        }

        public void onChild(View v) {
            ll_summary.setVisibility(View.GONE);
            ll_protection.setVisibility(View.GONE);
            ll_retirement.setVisibility(View.GONE);
            ll_child.setVisibility(View.VISIBLE);
            ll_wealth_creation.setVisibility(View.GONE);

            // iv_summary.setImageDrawable(getResources().getDrawable(
            // R.drawable.summary));
            //
            // iv_protection.setImageDrawable(getResources().getDrawable(
            // R.drawable.protection_nonclicked));
            // iv_retirement.setImageDrawable(getResources().getDrawable(
            // R.drawable.retirement_nonclicked));
            // iv_wealthCreation.setImageDrawable(getResources().getDrawable(
            // R.drawable.wealth_creation_nonclicked));
            // iv_child.setImageDrawable(getResources().getDrawable(
            // R.drawable.child_clicked));

            double child_count = Double.parseDouble(str_no_of_child);
            if (child_count == 0) {
                ln_child_1.setVisibility(View.GONE);
                ln_child_2.setVisibility(View.GONE);
                ln_child_3.setVisibility(View.GONE);
                ln_child_4.setVisibility(View.GONE);
            } else if (child_count == 1) {
                ln_child_1.setVisibility(View.VISIBLE);
                ln_child_2.setVisibility(View.GONE);
                ln_child_3.setVisibility(View.GONE);
                ln_child_4.setVisibility(View.GONE);
            } else if (child_count == 2) {
                ln_child_1.setVisibility(View.VISIBLE);
                ln_child_2.setVisibility(View.VISIBLE);
                ln_child_3.setVisibility(View.GONE);
                ln_child_4.setVisibility(View.GONE);
            } else if (child_count == 3) {
                ln_child_1.setVisibility(View.VISIBLE);
                ln_child_2.setVisibility(View.VISIBLE);
                ln_child_3.setVisibility(View.VISIBLE);
                ln_child_4.setVisibility(View.GONE);
            } else if (child_count == 4) {
                ln_child_1.setVisibility(View.VISIBLE);
                ln_child_2.setVisibility(View.VISIBLE);
                ln_child_3.setVisibility(View.VISIBLE);
                ln_child_4.setVisibility(View.VISIBLE);
            }
        }

        public void onWealthCreation(View v) {
            ll_summary.setVisibility(View.GONE);
            ll_protection.setVisibility(View.GONE);
            ll_retirement.setVisibility(View.GONE);
            ll_child.setVisibility(View.GONE);
            ll_wealth_creation.setVisibility(View.VISIBLE);

            // iv_summary.setImageDrawable(getResources().getDrawable(
            // R.drawable.summary));
            //
            // iv_protection.setImageDrawable(getResources().getDrawable(
            // R.drawable.protection_nonclicked));
            // iv_retirement.setImageDrawable(getResources().getDrawable(
            // R.drawable.retirement_nonclicked));
            // iv_wealthCreation.setImageDrawable(getResources().getDrawable(
            // R.drawable.wealth_creation_clicked));
            // iv_child.setImageDrawable(getResources().getDrawable(
            // R.drawable.child_nonclicked));

            // String str = "Conservative";
            if (str_investment_approach.equalsIgnoreCase("Conservative")) {
                ln_trad_ulip.setVisibility(View.VISIBLE);
                ln_ulip_trad.setVisibility(View.GONE);
            } else {
                ln_trad_ulip.setVisibility(View.GONE);
                ln_ulip_trad.setVisibility(View.VISIBLE);
            }
        }

        public void GetSummaryOutput() {
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_NEED_ANALYSIS);

            // String strValue =
            // "<NeedAn><PrGe>F</PrGe><PrAge>30</PrAge><PrMarrSt>Married</PrMarrSt><PrDependCh>1</PrDependCh><PrAnnIncome>1500000</PrAnnIncome><PrAnnExp>200000</PrAnnExp><PrOutStaHmLoanAmt>2000000</PrOutStaHmLoanAmt><PrOutStaOthrLoanAmt>500000</PrOutStaOthrLoanAmt><currLifeInCov>2500000</currLifeInCov><currRetCorpSav>500000</currRetCorpSav><PrRetAge>60</PrRetAge><PrPostRetLife>Luxury</PrPostRetLife><PrEduChAge1>1</PrEduChAge1><PrEduChBudExpNoYrs1>18</PrEduChBudExpNoYrs1><PrEduChBudEXp1>100000</PrEduChBudEXp1><PrEduChCurrEXp1>500000</PrEduChCurrEXp1><PrEduChAge2>0</PrEduChAge2><PrEduChBudExpNoYrs2>0</PrEduChBudExpNoYrs2><PrEduChBudEXp2>0</PrEduChBudEXp2><PrEduChCurrEXp2>0</PrEduChCurrEXp2><PrEduChAge3>0</PrEduChAge3><PrEduChBudExpNoYrs3>0</PrEduChBudExpNoYrs3><PrEduChBudEXp3>0</PrEduChBudEXp3><PrEduChCurrEXp3>0</PrEduChCurrEXp3><PrEduChAge4>0</PrEduChAge4><PrEduChBudExpNoYrs4>0</PrEduChBudExpNoYrs4><PrEduChBudEXp4>0</PrEduChBudEXp4><PrEduChCurrEXp4>0</PrEduChCurrEXp4><PrMaChBudExpNoYrs1>25</PrMaChBudExpNoYrs1><PrMaChBudEXp1>200000</PrMaChBudEXp1><PrMaChCurrEXp1>500000</PrMaChCurrEXp1><PrMaChCurrExpNoYrs2>0</PrMaChCurrExpNoYrs2><PrMaChBudEXp2>0</PrMaChBudEXp2><PrMaChCurrEXp2>0</PrMaChCurrEXp2><PrMaChBudExpNoYrs3>0</PrMaChBudExpNoYrs3><PrMaChBudEXp3>0</PrMaChBudEXp3><PrMaChCurrEXp3>0</PrMaChCurrEXp3><PrMaChBudExpNoYrs4>0</PrMaChBudExpNoYrs4><PrMaChBudEXp4>0</PrMaChBudEXp4><PrMaChCurrEXp4>0</PrMaChCurrEXp4><PrProWeCrNoofYr>15</PrProWeCrNoofYr><PrProWeCrBudExp>200000</PrProWeCrBudExp><PrProWeCrCurrExp>5000000</PrProWeCrCurrExp><PrOthFinWeCrNoofYr>4</PrOthFinWeCrNoofYr><PrOthFinWeCrBudExp>30000</PrOthFinWeCrBudExp><PrOthFinWeCrCurrExp>300000</PrOthFinWeCrCurrExp></NeedAn>";

            String strValue = "<NeedAn>" + "<PrGe>" + str_gender + "</PrGe>"
                    + "<PrAge>" + tv_age.getText().toString() + "</PrAge>"
                    + "<PrMarrSt>" + str_marital_status + "</PrMarrSt>"
                    + "<PrDependCh>" + str_no_of_child + "</PrDependCh>"
                    + "<PrAnnIncome>" + strYearlyIncome + "</PrAnnIncome>"
                    + "<PrAnnExp>" + stryearlyIncome_emi + "</PrAnnExp>"
                    + "<PrOutStaHmLoanAmt>" + strOutstandingHomeLoan
                    + "</PrOutStaHmLoanAmt>" + "<PrOutStaOthrLoanAmt>"
                    + strOutstandingHomeLoanother + "</PrOutStaOthrLoanAmt>"
                    + "<currLifeInCov>" + strCurrentLifeInsuranceCoverage
                    + "</currLifeInCov>" + "<currRetCorpSav>"
                    + strRetirementCurrentCorpus + "</currRetCorpSav>"
                    + "<PrRetAge>" + strRetirementNoOfRealise + "</PrRetAge>"
                    + "<PrPostRetLife>" + strRetirementLifeStyle
                    + "</PrPostRetLife>" + "<PrEduChAge1>" + strChild1Age
                    + "</PrEduChAge1>" + "<PrEduChBudExpNoYrs1>"
                    + strChild1AgeAtEducation + "</PrEduChBudExpNoYrs1>"
                    + "<PrEduChBudEXp1>" + strChild1CorpusEducation
                    + "</PrEduChBudEXp1>" + "<PrEduChCurrEXp1>"
                    + strChild1CurrentCostEducation + "</PrEduChCurrEXp1>"
                    + "<PrEduChAge2>" + strChild2Age + "</PrEduChAge2>"
                    + "<PrEduChBudExpNoYrs2>" + strChild2AgeAtEducation
                    + "</PrEduChBudExpNoYrs2>" + "<PrEduChBudEXp2>"
                    + strChild2CorpusEducation + "</PrEduChBudEXp2>"
                    + "<PrEduChCurrEXp2>" + strChild2CurrentCostEducation
                    + "</PrEduChCurrEXp2>" + "<PrEduChAge3>" + strChild3Age
                    + "</PrEduChAge3>" + "<PrEduChBudExpNoYrs3>"
                    + strChild3AgeAtEducation + "</PrEduChBudExpNoYrs3>"
                    + "<PrEduChBudEXp3>" + strChild3CorpusEducation
                    + "</PrEduChBudEXp3>" + "<PrEduChCurrEXp3>"
                    + strChild3CurrentCostEducation + "</PrEduChCurrEXp3>"
                    + "<PrEduChAge4>" + strChild4Age + "</PrEduChAge4>"
                    + "<PrEduChBudExpNoYrs4>" + strChild4AgeAtEducation
                    + "</PrEduChBudExpNoYrs4>" + "<PrEduChBudEXp4>"
                    + strChild4CorpusEducation + "</PrEduChBudEXp4>"
                    + "<PrEduChCurrEXp4>" + strChild4CurrentCostEducation
                    + "</PrEduChCurrEXp4>" + "<PrMaChBudExpNoYrs1>"
                    + strChild1AgeAtMarriage + "</PrMaChBudExpNoYrs1>"
                    + "<PrMaChBudEXp1>" + strChild1CorpusMarriage
                    + "</PrMaChBudEXp1>" + "<PrMaChCurrEXp1>"
                    + strChild1CurrentCostMarriage + "</PrMaChCurrEXp1>"
                    + "<PrMaChCurrExpNoYrs2>" + strChild2AgeAtMarriage
                    + "</PrMaChCurrExpNoYrs2>" + "<PrMaChBudEXp2>"
                    + strChild2CorpusMarriage + "</PrMaChBudEXp2>"
                    + "<PrMaChCurrEXp2>" + strChild2CurrentCostMarriage
                    + "</PrMaChCurrEXp2>" + "<PrMaChBudExpNoYrs3>"
                    + strChild3AgeAtMarriage + "</PrMaChBudExpNoYrs3>"
                    + "<PrMaChBudEXp3>" + strChild3CorpusMarriage
                    + "</PrMaChBudEXp3>" + "<PrMaChCurrEXp3>"
                    + strChild3CurrentCostMarriage + "</PrMaChCurrEXp3>"
                    + "<PrMaChBudExpNoYrs4>" + strChild4AgeAtMarriage
                    + "</PrMaChBudExpNoYrs4>" + "<PrMaChBudEXp4>"
                    + strChild4CorpusMarriage + "</PrMaChBudEXp4>"
                    + "<PrMaChCurrEXp4>" + strChild4CurrentCostMarriage
                    + "</PrMaChCurrEXp4>" + "<PrProWeCrNoofYr>"
                    + strNoOfYearBuyHome + "</PrProWeCrNoofYr>"
                    + "<PrProWeCrBudExp>" + strCorpusForhome
                    + "</PrProWeCrBudExp>" + "<PrProWeCrCurrExp>"
                    + strCurrentCosthome + "</PrProWeCrCurrExp>"
                    + "<PrOthFinWeCrNoofYr>" + strNoOfYearOthergoal
                    + "</PrOthFinWeCrNoofYr>" + "<PrOthFinWeCrBudExp>"
                    + strCorpusForOther + "</PrOthFinWeCrBudExp>"
                    + "<PrOthFinWeCrCurrExp>" + strCurrentCostOther
                    + "</PrOthFinWeCrCurrExp>" + "</NeedAn>";
            request.addProperty("strInput", strValue);
            request.addProperty("reqFrom", "CON");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            // allowAllSSL();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
            try {
                androidHttpTranport.call(SOAP_ACTION_NEED_ANALYSIS, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contains("anyType{}")) {

                    SoapPrimitive sa = null;
                    try {
                        sa = (SoapPrimitive) envelope.getResponse();

                        outputlist = sa.toString();

                        ParseXML prsObj = new ParseXML();

                        outputlist = prsObj.parseXmlTag(outputlist, "NeedAn");
                        outputlist = new ParseXML().parseXmlTag(outputlist,
                                "ScreenData");
                        strErrorCOde = outputlist;

                        if (strErrorCOde == null) {
                            outputlist = sa.toString();
                            outputlist = prsObj.parseXmlTag(outputlist,
                                    "NeedAn");

                            // protection
                            LifeProtCovCorpus = new ParseXML().parseXmlTag(
                                    outputlist, "LifeProtCurrCoverAmt");
                            LifeProtGap = new ParseXML().parseXmlTag(
                                    outputlist, "LifeProtGapAmt");

                            double LifeProtCovCorpus_ = Double
                                    .parseDouble(LifeProtCovCorpus);
                            double LifeProtGap_ = Double
                                    .parseDouble(LifeProtGap);
                            double LifeProtTarget_ = LifeProtCovCorpus_
                                    + LifeProtGap_;

                            // Retirement

                            PensionTotReqCurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist, "RetCurrSav");
                            PensionActAnnInvGap = new ParseXML().parseXmlTag(
                                    outputlist, "RetGap");

                            double PensionTotReqCurCorpus_ = Double
                                    .parseDouble(PensionTotReqCurCorpus);
                            double PensionActAnnInvGap_ = Double
                                    .parseDouble(PensionActAnnInvGap);
                            double PensionActTarget_ = PensionTotReqCurCorpus_
                                    + PensionActAnnInvGap_;

                            // child 1 education

                            String Child_1_edu_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chEdufvCurSaving1");
                            String Child_1_edu_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chEduCorRe1");
                            Child_1_edu_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chEduGap1");

                            double Child_1_edu_CurCorpus_ = Double
                                    .parseDouble(Child_1_edu_CurCorpus);
                            double Child_1_edu_Target_ = Double
                                    .parseDouble(Child_1_edu_Target);
                            double Child_1_edu_Gap_ = Double
                                    .parseDouble(Child_1_edu_Gap);

                            // child 1 marriage

                            String Child_1_mar_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chMarfvCurSaving1");
                            String Child_1_mar_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chMarCorRe1");
                            Child_1_mar_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chMarGap1");

                            double Child_1_mar_CurCorpus_ = Double
                                    .parseDouble(Child_1_mar_CurCorpus);
                            double Child_1_mar_Target_ = Double
                                    .parseDouble(Child_1_mar_Target);
                            double Child_1_mar_Gap_ = Double
                                    .parseDouble(Child_1_mar_Gap);

                            // child 2 education

                            String Child_2_edu_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chEdufvCurSaving2");
                            String Child_2_edu_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chEduCorRe2");
                            Child_2_edu_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chEduGap2");

                            double Child_2_edu_CurCorpus_ = Double
                                    .parseDouble(Child_2_edu_CurCorpus);
                            double Child_2_edu_Target_ = Double
                                    .parseDouble(Child_2_edu_Target);
                            double Child_2_edu_Gap_ = Double
                                    .parseDouble(Child_2_edu_Gap);

                            // child 2 marriage

                            String Child_2_mar_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chMarfvCurSaving2");
                            String Child_2_mar_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chMarCorRe2");
                            Child_2_mar_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chMarGap2");

                            double Child_2_mar_CurCorpus_ = Double
                                    .parseDouble(Child_2_mar_CurCorpus);
                            double Child_2_mar_Target_ = Double
                                    .parseDouble(Child_2_mar_Target);
                            double Child_2_mar_Gap_ = Double
                                    .parseDouble(Child_2_mar_Gap);

                            // child 3 education

                            String Child_3_edu_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chEdufvCurSaving3");
                            String Child_3_edu_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chEduCorRe3");
                            Child_3_edu_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chEduGap3");

                            double Child_3_edu_CurCorpus_ = Double
                                    .parseDouble(Child_3_edu_CurCorpus);
                            double Child_3_edu_Target_ = Double
                                    .parseDouble(Child_3_edu_Target);
                            double Child_3_edu_Gap_ = Double
                                    .parseDouble(Child_3_edu_Gap);

                            // child 3 marriage

                            String Child_3_mar_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chMarfvCurSaving3");
                            String Child_3_mar_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chMarCorRe3");
                            Child_3_mar_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chMarGap3");

                            double Child_3_mar_CurCorpus_ = Double
                                    .parseDouble(Child_3_mar_CurCorpus);
                            double Child_3_mar_Target_ = Double
                                    .parseDouble(Child_3_mar_Target);
                            double Child_3_mar_Gap_ = Double
                                    .parseDouble(Child_3_mar_Gap);

                            // child 4 education

                            String Child_4_edu_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chEdufvCurSaving4");
                            String Child_4_edu_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chEduCorRe4");
                            Child_4_edu_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chEduGap4");

                            double Child_4_edu_CurCorpus_ = Double
                                    .parseDouble(Child_4_edu_CurCorpus);
                            double Child_4_edu_Target_ = Double
                                    .parseDouble(Child_4_edu_Target);
                            double Child_4_edu_Gap_ = Double
                                    .parseDouble(Child_4_edu_Gap);

                            // child 4 marriage

                            String Child_4_mar_CurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist,
                                            "chMarfvCurSaving4");
                            String Child_4_mar_Target = new ParseXML()
                                    .parseXmlTag(outputlist, "chMarCorRe4");
                            Child_4_mar_Gap = new ParseXML().parseXmlTag(
                                    outputlist, "chMarGap4");

                            double Child_4_mar_CurCorpus_ = Double
                                    .parseDouble(Child_4_mar_CurCorpus);
                            double Child_4_mar_Target_ = Double
                                    .parseDouble(Child_4_mar_Target);
                            double Child_4_mar_Gap_ = Double
                                    .parseDouble(Child_4_mar_Gap);

                            double ch_total_target = Child_1_edu_Target_
                                    + Child_1_mar_Target_ + Child_2_edu_Target_
                                    + Child_2_mar_Target_ + Child_3_edu_Target_
                                    + Child_3_mar_Target_ + Child_4_edu_Target_
                                    + Child_4_mar_Target_;

                            double ch_total_corpus = Child_1_edu_CurCorpus_
                                    + Child_1_mar_CurCorpus_
                                    + Child_2_edu_CurCorpus_
                                    + Child_2_mar_CurCorpus_
                                    + Child_3_edu_CurCorpus_
                                    + Child_3_mar_CurCorpus_
                                    + Child_4_edu_CurCorpus_
                                    + Child_4_mar_CurCorpus_;
                            double ch_total_gap = Child_1_edu_Gap_
                                    + Child_1_mar_Gap_ + Child_2_edu_Gap_
                                    + Child_2_mar_Gap_ + Child_3_edu_Gap_
                                    + Child_3_mar_Gap_ + Child_4_edu_Gap_
                                    + Child_4_mar_Gap_;

                            // Wealth

                            String CostHomeTarged = new ParseXML().parseXmlTag(
                                    outputlist, "homeCorReq");
                            CostHomeCorpus = new ParseXML().parseXmlTag(
                                    outputlist, "homeCurSav");
                            CostHomeGap = new ParseXML().parseXmlTag(
                                    outputlist, "homeGap");

                            String CostOtherTarged = new ParseXML()
                                    .parseXmlTag(outputlist, "otheCorReq");
                            CostOtherCorpus = new ParseXML().parseXmlTag(
                                    outputlist, "otheCurSav");
                            CostOtherGap = new ParseXML().parseXmlTag(
                                    outputlist, "otheGap");

                            double CostHomeTarged_ = Double
                                    .parseDouble(CostHomeTarged);
                            double CostHomeCorpus_ = Double
                                    .parseDouble(CostHomeCorpus);
                            double CostHomeGap_ = Double
                                    .parseDouble(CostHomeGap);

                            double CostOtherTarged_ = Double
                                    .parseDouble(CostOtherTarged);
                            double CostOtherCorpus_ = Double
                                    .parseDouble(CostOtherCorpus);
                            double CostOtherGap_ = Double
                                    .parseDouble(CostOtherGap);

                            double Wealth_total_corpus = CostHomeCorpus_
                                    + CostOtherCorpus_;
                            double Wealth_total_gap = CostHomeGap_
                                    + CostOtherGap_;
                            double Wealth_total_target = CostHomeTarged_
                                    + CostOtherTarged_;

                            StackedSummaryBarChart(LifeProtCovCorpus_,
                                    LifeProtGap_, LifeProtTarget_,
                                    PensionTotReqCurCorpus_,
                                    PensionActAnnInvGap_, PensionActTarget_,
                                    ch_total_corpus, ch_total_gap,
                                    ch_total_target, Wealth_total_corpus,
                                    Wealth_total_gap, Wealth_total_target);

                        } else {
                            Toast.makeText(context, "Server Not Responding.",
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Server Not Responding.",
                            Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void StackedSummaryBarChart(double LifeProtCovCorpus_,
                                            double LifeProtGap_, double LifeProtTarget_,
                                            double PensionTotReqCurCorpus_, double PensionActAnnInvGap_,
                                            double PensionActTarget_, double WealthCreaChCurCorpus_,
                                            double WealthCreaChGap_, double WealthCreaChTarget_,
                                            double Wealth_total_corpus, double Wealth_total_gap,
                                            double Wealth_total_target) {
            // txt_summary_protection.setText("Target Life Insurance Coverage Rs."
            // + getformatedThousandString(Integer
            // .parseInt(getRound(getStringWithout_E(Double
            // .valueOf(LifeProtTarget_))))));

            txt_summary_protection.setText("Target Life Insurance Coverage Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(str_protection_cor_req))))));

            // txt_summary_retirement.setText("Target amount Rs."
            // + getformatedThousandString(Integer
            // .parseInt(getRound(getStringWithout_E(Double
            // .valueOf(PensionActTarget_))))));

            txt_summary_retirement.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(retirment_cor_req))))));

            txt_child_future.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(WealthCreaChTarget_))))));
            txt_wealth_creation_summary.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Wealth_total_target))))));

            // protection
            double a, b;
            if (LifeProtCovCorpus_ > LifeProtTarget_) {
                a = Math.round((LifeProtTarget_ * 100) / LifeProtCovCorpus_);
                b = Math.round(100 - a);
                a = a * 1.9;
                b = b * 1.9;
                LayoutParams params = (LayoutParams) ln_chart_protection_top
                        .getLayoutParams();
                params.height = (int) a;
                params.width = 40;

                LayoutParams params1 = (LayoutParams) ln_chart_protection_buttom
                        .getLayoutParams();
                params1.height = (int) b;
                params1.width = 40;

                LayoutParams params_side = (LayoutParams) vw_chart_protection_top_side
                        .getLayoutParams();
                params_side.height = (int) a;
            } else {
                a = Math.round((LifeProtCovCorpus_ * 100) / LifeProtTarget_);
                b = Math.round(100 - a);
                a = a * 1.9;
                b = b * 1.9;
                LayoutParams params = (LayoutParams) ln_chart_protection_top
                        .getLayoutParams();
                params.height = (int) b;
                params.width = 40;

                LayoutParams params1 = (LayoutParams) ln_chart_protection_buttom
                        .getLayoutParams();
                params1.height = (int) a;
                params1.width = 40;

                LayoutParams params_side = (LayoutParams) vw_chart_protection_top_side
                        .getLayoutParams();
                params_side.height = (int) b;
            }

            double c, d;
            // retirement
            if (PensionTotReqCurCorpus_ > PensionActTarget_) {

                c = Math.round((PensionActTarget_ * 100)
                        / PensionTotReqCurCorpus_);
                d = Math.round(100 - c);
                c = c * 1.9;
                d = d * 1.9;
                LayoutParams params2 = (LayoutParams) ln_chart_retirement_top
                        .getLayoutParams();
                params2.height = (int) c;
                params2.width = 40;

                LayoutParams params3 = (LayoutParams) ln_chart_retirement_buttom
                        .getLayoutParams();
                params3.height = (int) d;
                params3.width = 40;

                LayoutParams params3_side = (LayoutParams) vw_chart_retirement_top_side
                        .getLayoutParams();
                params3_side.height = (int) c;

            } else {

                c = Math.round((PensionTotReqCurCorpus_ * 100)
                        / PensionActTarget_);
                d = Math.round(100 - c);
                c = c * 1.9;
                d = d * 1.9;
                LayoutParams params2 = (LayoutParams) ln_chart_retirement_top
                        .getLayoutParams();
                params2.height = (int) d;
                params2.width = 40;

                LayoutParams params3 = (LayoutParams) ln_chart_retirement_buttom
                        .getLayoutParams();
                params3.height = (int) c;
                params3.width = 40;

                LayoutParams params3_side = (LayoutParams) vw_chart_retirement_top_side
                        .getLayoutParams();
                params3_side.height = (int) d;
            }

            double e, f;
            // child
            if (WealthCreaChCurCorpus_ > WealthCreaChTarget_) {

                e = Math.round((WealthCreaChTarget_ * 100)
                        / WealthCreaChCurCorpus_);
                f = Math.round(100 - e);
                e = e * 1.9;
                f = f * 1.9;
                LayoutParams params4 = (LayoutParams) ln_chart_child_top
                        .getLayoutParams();
                params4.height = (int) e;
                params4.width = 40;

                LayoutParams params5 = (LayoutParams) ln_chart_child_buttom
                        .getLayoutParams();
                params5.height = (int) f;
                params5.width = 40;

                LayoutParams params5_side = (LayoutParams) vw_chart_child_top_side
                        .getLayoutParams();
                params5_side.height = (int) e;

            } else {
                e = Math.round((WealthCreaChCurCorpus_ * 100)
                        / WealthCreaChTarget_);
                f = Math.round(100 - e);
                e = e * 1.9;
                f = f * 1.9;
                LayoutParams params4 = (LayoutParams) ln_chart_child_top
                        .getLayoutParams();
                params4.height = (int) f;
                params4.width = 40;

                LayoutParams params5 = (LayoutParams) ln_chart_child_buttom
                        .getLayoutParams();
                params5.height = (int) e;
                params5.width = 40;

                LayoutParams params5_side = (LayoutParams) vw_chart_child_top_side
                        .getLayoutParams();
                params5_side.height = (int) f;

            }

            // wealth
            double g, h;
            if (Wealth_total_corpus > Wealth_total_target) {

                g = Math.round((Wealth_total_target * 100)
                        / Wealth_total_corpus);
                h = Math.round(100 - g);

                g = g * 1.9;
                h = h * 1.9;
                LayoutParams params6 = (LayoutParams) ln_chart_wealth_top
                        .getLayoutParams();
                params6.height = (int) g;
                params6.width = 40;

                LayoutParams params7 = (LayoutParams) ln_chart_wealth_buttom
                        .getLayoutParams();
                params7.height = (int) h;
                params7.width = 40;

                LayoutParams params7_side = (LayoutParams) vw_chart_wealth_top_side
                        .getLayoutParams();
                params7_side.height = (int) g;

            } else {
                g = Math.round((Wealth_total_corpus * 100)
                        / Wealth_total_target);
                h = Math.round(100 - g);
                g = g * 1.9;
                h = h * 1.9;
                LayoutParams params6 = (LayoutParams) ln_chart_wealth_top
                        .getLayoutParams();
                params6.height = (int) h;
                params6.width = 40;

                LayoutParams params7 = (LayoutParams) ln_chart_wealth_buttom
                        .getLayoutParams();
                params7.height = (int) g;
                params7.width = 40;

                LayoutParams params7_side = (LayoutParams) vw_chart_wealth_top_side
                        .getLayoutParams();
                params7_side.height = (int) h;

            }

            txt_protection_gap.setText("Gap to target Coverage Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(LifeProtGap_))))));
            txt_protection_target_amt.setText(" Target Coverage Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(LifeProtTarget_))))));
            txt_protection_corpus.setText("Current Coverage Rs. "
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(LifeProtCovCorpus_))))));

            txt_retirement_gap.setText("Gap to target amount Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(PensionActAnnInvGap_))))));
            txt_retirement_target_amt.setText(" < Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(PensionActTarget_))))));
            txt_retirement_corpus.setText("Current Corpus(FV*) Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(PensionTotReqCurCorpus_))))));

            txt_child_future_gap.setText("Gap to target amount Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(WealthCreaChGap_))))));
            txt_child_future_target.setText(" < Target amount Rs."
                    + getRound(getStringWithout_E(Double
                    .valueOf(WealthCreaChTarget_))));
            txt_child_future_corpus.setText("Current Corpus(FV*) Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(WealthCreaChCurCorpus_))))));

            txt_wealth_creation_gap.setText("Gap to target amount Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Wealth_total_gap))))));
            txt_wealth_creation_target.setText(" < Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Wealth_total_target))))));
            txt_wealth_creation_corpus.setText("Current Corpus(FV*) Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Wealth_total_corpus))))));

        }

        public void GetRetirmentOutput() {
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_NEED_ANALYSIS);

            // String strValue =
            // "<NeedAn><PrGe>F</PrGe><PrAge>30</PrAge><PrMarrSt>Married</PrMarrSt><PrDependCh>1</PrDependCh><PrAnnIncome>1500000</PrAnnIncome><PrAnnExp>200000</PrAnnExp><PrOutStaHmLoanAmt>2000000</PrOutStaHmLoanAmt><PrOutStaOthrLoanAmt>500000</PrOutStaOthrLoanAmt><currLifeInCov>2500000</currLifeInCov><currRetCorpSav>500000</currRetCorpSav><PrRetAge>60</PrRetAge><PrPostRetLife>Luxury</PrPostRetLife><PrEduChAge1>1</PrEduChAge1><PrEduChBudExpNoYrs1>18</PrEduChBudExpNoYrs1><PrEduChBudEXp1>100000</PrEduChBudEXp1><PrEduChCurrEXp1>500000</PrEduChCurrEXp1><PrEduChAge2>0</PrEduChAge2><PrEduChBudExpNoYrs2>0</PrEduChBudExpNoYrs2><PrEduChBudEXp2>0</PrEduChBudEXp2><PrEduChCurrEXp2>0</PrEduChCurrEXp2><PrEduChAge3>0</PrEduChAge3><PrEduChBudExpNoYrs3>0</PrEduChBudExpNoYrs3><PrEduChBudEXp3>0</PrEduChBudEXp3><PrEduChCurrEXp3>0</PrEduChCurrEXp3><PrEduChAge4>0</PrEduChAge4><PrEduChBudExpNoYrs4>0</PrEduChBudExpNoYrs4><PrEduChBudEXp4>0</PrEduChBudEXp4><PrEduChCurrEXp4>0</PrEduChCurrEXp4><PrMaChBudExpNoYrs1>25</PrMaChBudExpNoYrs1><PrMaChBudEXp1>200000</PrMaChBudEXp1><PrMaChCurrEXp1>500000</PrMaChCurrEXp1><PrMaChCurrExpNoYrs2>0</PrMaChCurrExpNoYrs2><PrMaChBudEXp2>0</PrMaChBudEXp2><PrMaChCurrEXp2>0</PrMaChCurrEXp2><PrMaChBudExpNoYrs3>0</PrMaChBudExpNoYrs3><PrMaChBudEXp3>0</PrMaChBudEXp3><PrMaChCurrEXp3>0</PrMaChCurrEXp3><PrMaChBudExpNoYrs4>0</PrMaChBudExpNoYrs4><PrMaChBudEXp4>0</PrMaChBudEXp4><PrMaChCurrEXp4>0</PrMaChCurrEXp4><PrProWeCrNoofYr>15</PrProWeCrNoofYr><PrProWeCrBudExp>200000</PrProWeCrBudExp><PrProWeCrCurrExp>5000000</PrProWeCrCurrExp><PrOthFinWeCrNoofYr>4</PrOthFinWeCrNoofYr><PrOthFinWeCrBudExp>30000</PrOthFinWeCrBudExp><PrOthFinWeCrCurrExp>300000</PrOthFinWeCrCurrExp></NeedAn>";
            String strValue = "<NeedAn>" + "<PrGe>" + str_gender + "</PrGe>"
                    + "<PrAge>" + tv_age.getText().toString() + "</PrAge>"
                    + "<PrMarrSt>" + str_marital_status + "</PrMarrSt>"
                    + "<PrDependCh>" + str_no_of_child + "</PrDependCh>"
                    + "<PrAnnIncome>" + strYearlyIncome + "</PrAnnIncome>"
                    + "<PrAnnExp>" + stryearlyIncome_emi + "</PrAnnExp>"
                    + "<PrOutStaHmLoanAmt>" + strOutstandingHomeLoan
                    + "</PrOutStaHmLoanAmt>" + "<PrOutStaOthrLoanAmt>"
                    + strOutstandingHomeLoanother + "</PrOutStaOthrLoanAmt>"
                    + "<currLifeInCov>" + strCurrentLifeInsuranceCoverage
                    + "</currLifeInCov>" + "<currRetCorpSav>"
                    + strRetirementCurrentCorpus + "</currRetCorpSav>"
                    + "<PrRetAge>" + strRetirementNoOfRealise + "</PrRetAge>"
                    + "<PrPostRetLife>" + strRetirementLifeStyle
                    + "</PrPostRetLife>" + "<PrEduChAge1>" + strChild1Age
                    + "</PrEduChAge1>" + "<PrEduChBudExpNoYrs1>"
                    + strChild1AgeAtEducation + "</PrEduChBudExpNoYrs1>"
                    + "<PrEduChBudEXp1>" + strChild1CorpusEducation
                    + "</PrEduChBudEXp1>" + "<PrEduChCurrEXp1>"
                    + strChild1CurrentCostEducation + "</PrEduChCurrEXp1>"
                    + "<PrEduChAge2>" + strChild2Age + "</PrEduChAge2>"
                    + "<PrEduChBudExpNoYrs2>" + strChild2AgeAtEducation
                    + "</PrEduChBudExpNoYrs2>" + "<PrEduChBudEXp2>"
                    + strChild2CorpusEducation + "</PrEduChBudEXp2>"
                    + "<PrEduChCurrEXp2>" + strChild2CurrentCostEducation
                    + "</PrEduChCurrEXp2>" + "<PrEduChAge3>" + strChild3Age
                    + "</PrEduChAge3>" + "<PrEduChBudExpNoYrs3>"
                    + strChild3AgeAtEducation + "</PrEduChBudExpNoYrs3>"
                    + "<PrEduChBudEXp3>" + strChild3CorpusEducation
                    + "</PrEduChBudEXp3>" + "<PrEduChCurrEXp3>"
                    + strChild3CurrentCostEducation + "</PrEduChCurrEXp3>"
                    + "<PrEduChAge4>" + strChild4Age + "</PrEduChAge4>"
                    + "<PrEduChBudExpNoYrs4>" + strChild4AgeAtEducation
                    + "</PrEduChBudExpNoYrs4>" + "<PrEduChBudEXp4>"
                    + strChild4CorpusEducation + "</PrEduChBudEXp4>"
                    + "<PrEduChCurrEXp4>" + strChild4CurrentCostEducation
                    + "</PrEduChCurrEXp4>" + "<PrMaChBudExpNoYrs1>"
                    + strChild1AgeAtMarriage + "</PrMaChBudExpNoYrs1>"
                    + "<PrMaChBudEXp1>" + strChild1CorpusMarriage
                    + "</PrMaChBudEXp1>" + "<PrMaChCurrEXp1>"
                    + strChild1CurrentCostMarriage + "</PrMaChCurrEXp1>"
                    + "<PrMaChCurrExpNoYrs2>" + strChild2AgeAtMarriage
                    + "</PrMaChCurrExpNoYrs2>" + "<PrMaChBudEXp2>"
                    + strChild2CorpusMarriage + "</PrMaChBudEXp2>"
                    + "<PrMaChCurrEXp2>" + strChild2CurrentCostMarriage
                    + "</PrMaChCurrEXp2>" + "<PrMaChBudExpNoYrs3>"
                    + strChild3AgeAtMarriage + "</PrMaChBudExpNoYrs3>"
                    + "<PrMaChBudEXp3>" + strChild3CorpusMarriage
                    + "</PrMaChBudEXp3>" + "<PrMaChCurrEXp3>"
                    + strChild3CurrentCostMarriage + "</PrMaChCurrEXp3>"
                    + "<PrMaChBudExpNoYrs4>" + strChild4AgeAtMarriage
                    + "</PrMaChBudExpNoYrs4>" + "<PrMaChBudEXp4>"
                    + strChild4CorpusMarriage + "</PrMaChBudEXp4>"
                    + "<PrMaChCurrEXp4>" + strChild4CurrentCostMarriage
                    + "</PrMaChCurrEXp4>" + "<PrProWeCrNoofYr>"
                    + strNoOfYearBuyHome + "</PrProWeCrNoofYr>"
                    + "<PrProWeCrBudExp>" + strCorpusForhome
                    + "</PrProWeCrBudExp>" + "<PrProWeCrCurrExp>"
                    + strCurrentCosthome + "</PrProWeCrCurrExp>"
                    + "<PrOthFinWeCrNoofYr>" + strNoOfYearOthergoal
                    + "</PrOthFinWeCrNoofYr>" + "<PrOthFinWeCrBudExp>"
                    + strCorpusForOther + "</PrOthFinWeCrBudExp>"
                    + "<PrOthFinWeCrCurrExp>" + strCurrentCostOther
                    + "</PrOthFinWeCrCurrExp>" + "</NeedAn>";
            request.addProperty("strInput", strValue);
            request.addProperty("reqFrom", "CON");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            // allowAllSSL();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
            try {
                androidHttpTranport.call(SOAP_ACTION_NEED_ANALYSIS, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contains("anyType{}")) {

                    SoapPrimitive sa = null;
                    try {
                        sa = (SoapPrimitive) envelope.getResponse();

                        outputlist = sa.toString();

                        ParseXML prsObj = new ParseXML();

                        outputlist = prsObj.parseXmlTag(outputlist, "NeedAn");
                        outputlist = new ParseXML().parseXmlTag(outputlist,
                                "ScreenData");
                        strErrorCOde = outputlist;

                        if (strErrorCOde == null) {
                            outputlist = sa.toString();
                            outputlist = prsObj.parseXmlTag(outputlist,
                                    "NeedAn");

                            // Retirement

                            PensionTotReqCurCorpus = new ParseXML()
                                    .parseXmlTag(outputlist, "RetCurrSav");
                            PensionActAnnInvGap = new ParseXML().parseXmlTag(
                                    outputlist, "RetGap");

                            double PensionTotReqCurCorpus_ = Double
                                    .parseDouble(PensionTotReqCurCorpus);
                            double PensionActAnnInvGap_ = Double
                                    .parseDouble(PensionActAnnInvGap);
                            double PensionActTarget_ = PensionTotReqCurCorpus_
                                    + PensionActAnnInvGap_;

                            StackedRetirementBarChart(PensionTotReqCurCorpus_,
                                    PensionActAnnInvGap_, PensionActTarget_);

                            // pension plan

                            String Pension_Plan = new ParseXML().parseXmlTag(
                                    outputlist, "ProdLstRet").trim();

                            if (!Pension_Plan.contentEquals("")) {

                                if (!Pension_Plan.contentEquals("null")) {

                                    String[] str_Pro = Pension_Plan.split("/");
                                    for (int i = 0; i < str_Pro.length; i++) {
                                        String str_Name = str_Pro[i]
                                                .trim();
                                        String[] str_Name_split = str_Name
                                                .trim().split(";");
                                        String str_Code = str_Name_split[1].trim();

                                        retirement_prod_list
                                                .add(new SuggestedProdList(
                                                        str_Code,
                                                        str_Name_split[2]
                                                                .trim(),
                                                        str_Name_split[0]
                                                                .trim(), false));

                                        if (str_usertype
                                                .equalsIgnoreCase("IMF")) {

                                            if (str_Code.contains("35")
                                                    || str_Code.contains("1B")
                                                    || str_Code.contains("1N")
                                                    || str_Code.contains("1P")
                                                    || str_Code.contains("1R")
                                                    || str_Code.contains("1W") || str_Code.contains("2E") || str_Code.contains("2G") || str_Code.contains("2J")) {

                                                suggested_prod_list
                                                        .add(new SuggestedProdList(
                                                                str_Code,
                                                                str_Name_split[2]
                                                                        .trim(),
                                                                str_Name_split[0]
                                                                        .trim(), false));
                                            }

                                        } else {
                                            /*if (str_Code.equalsIgnoreCase("1K") || str_Code.equalsIgnoreCase("2F")) {
                                                combo1_flag_presnt = true;
                                                combo3_flag_presnt = true;
                                            }
                                            if (str_Code.equalsIgnoreCase("2D") || str_Code.equalsIgnoreCase("2F")) {
                                                combo2_flag_presnt = true;
                                                combo4_flag_presnt = true;
                                            }
                                            if (str_Code.equalsIgnoreCase("1H") || str_Code.equalsIgnoreCase("2F")) {
                                                combo5_flag_presnt = true;

                                            }*/
                                            suggested_prod_list
                                                    .add(new SuggestedProdList(
                                                            str_Code,
                                                            str_Name_split[2]
                                                                    .trim(),
                                                            str_Name_split[0]
                                                                    .trim(),
                                                            false));
                                        }
                                        if (str_Code.contains("1E")) {
                                            txt_ret_saral_pension
                                                    .setVisibility(View.VISIBLE);
                                        } else if (str_Code.contains("1H")) {
                                            txt_ret_retire_smart
                                                    .setVisibility(View.VISIBLE);
                                        } else if (str_Code.contains("22")) {
                                            txt_ret_annuity_plus
                                                    .setVisibility(View.VISIBLE);
                                        }

                                    }
                                }
                            }

                        } else {
                            Toast.makeText(context, "Server Not Responding.",
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Server Not Responding.",
                            Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void StackedRetirementBarChart(double PensionTotReqCurCorpus_,
                                               double PensionActAnnInvGap_, double PensionActTarget_) {
            // txt_retirement.setText("Target amount Rs."
            // + getformatedThousandString(Integer
            // .parseInt(getRound(getStringWithout_E(Double
            // .valueOf(PensionActTarget_))))));

            txt_retirement.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(retirment_cor_req))))));

            double c, d;
            // retirement
            if (PensionTotReqCurCorpus_ > PensionActTarget_) {
                c = Math.round((PensionActTarget_ * 100)
                        / PensionTotReqCurCorpus_);
                d = Math.round(100 - c);
                c = c * 1.9;
                d = d * 1.9;
                LayoutParams params2 = (LayoutParams) ln_chart_retire_top
                        .getLayoutParams();
                params2.height = (int) c;
                params2.width = 40;

                LayoutParams params3 = (LayoutParams) ln_chart_retire_buttom
                        .getLayoutParams();
                params3.height = (int) d;
                params3.width = 40;

                LayoutParams params3_side = (LayoutParams) vw_retire_top_side
                        .getLayoutParams();
                params3_side.height = (int) c;

            } else {
                c = Math.round((PensionTotReqCurCorpus_ * 100)
                        / PensionActTarget_);
                d = Math.round(100 - c);
                c = c * 1.9;
                d = d * 1.9;
                LayoutParams params2 = (LayoutParams) ln_chart_retire_top
                        .getLayoutParams();
                params2.height = (int) d;
                params2.width = 40;

                LayoutParams params3 = (LayoutParams) ln_chart_retire_buttom
                        .getLayoutParams();
                params3.height = (int) c;
                params3.width = 40;

                LayoutParams params3_side = (LayoutParams) vw_retire_top_side
                        .getLayoutParams();
                params3_side.height = (int) d;

            }

            txt_retire_gap.setText("Gap to target amount Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(PensionActAnnInvGap_))))));
            txt_retire_target_amt.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(PensionActAnnInvGap_))))));
            txt_retire_corpus.setText("Current Corpus(FV*) Rs. "
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(PensionTotReqCurCorpus_))))));

        }

        public void getProtectionOutput() {
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_NEED_ANALYSIS);

            // String strValue =
            // "<NeedAn><PrGe>F</PrGe><PrAge>30</PrAge><PrMarrSt>Married</PrMarrSt><PrDependCh>1</PrDependCh><PrAnnIncome>1500000</PrAnnIncome><PrAnnExp>200000</PrAnnExp><PrOutStaHmLoanAmt>2000000</PrOutStaHmLoanAmt><PrOutStaOthrLoanAmt>500000</PrOutStaOthrLoanAmt><currLifeInCov>2500000</currLifeInCov><currRetCorpSav>500000</currRetCorpSav><PrRetAge>60</PrRetAge><PrPostRetLife>Luxury</PrPostRetLife><PrEduChAge1>1</PrEduChAge1><PrEduChBudExpNoYrs1>18</PrEduChBudExpNoYrs1><PrEduChBudEXp1>100000</PrEduChBudEXp1><PrEduChCurrEXp1>500000</PrEduChCurrEXp1><PrEduChAge2>0</PrEduChAge2><PrEduChBudExpNoYrs2>0</PrEduChBudExpNoYrs2><PrEduChBudEXp2>0</PrEduChBudEXp2><PrEduChCurrEXp2>0</PrEduChCurrEXp2><PrEduChAge3>0</PrEduChAge3><PrEduChBudExpNoYrs3>0</PrEduChBudExpNoYrs3><PrEduChBudEXp3>0</PrEduChBudEXp3><PrEduChCurrEXp3>0</PrEduChCurrEXp3><PrEduChAge4>0</PrEduChAge4><PrEduChBudExpNoYrs4>0</PrEduChBudExpNoYrs4><PrEduChBudEXp4>0</PrEduChBudEXp4><PrEduChCurrEXp4>0</PrEduChCurrEXp4><PrMaChBudExpNoYrs1>25</PrMaChBudExpNoYrs1><PrMaChBudEXp1>200000</PrMaChBudEXp1><PrMaChCurrEXp1>500000</PrMaChCurrEXp1><PrMaChCurrExpNoYrs2>0</PrMaChCurrExpNoYrs2><PrMaChBudEXp2>0</PrMaChBudEXp2><PrMaChCurrEXp2>0</PrMaChCurrEXp2><PrMaChBudExpNoYrs3>0</PrMaChBudExpNoYrs3><PrMaChBudEXp3>0</PrMaChBudEXp3><PrMaChCurrEXp3>0</PrMaChCurrEXp3><PrMaChBudExpNoYrs4>0</PrMaChBudExpNoYrs4><PrMaChBudEXp4>0</PrMaChBudEXp4><PrMaChCurrEXp4>0</PrMaChCurrEXp4><PrProWeCrNoofYr>15</PrProWeCrNoofYr><PrProWeCrBudExp>200000</PrProWeCrBudExp><PrProWeCrCurrExp>5000000</PrProWeCrCurrExp><PrOthFinWeCrNoofYr>4</PrOthFinWeCrNoofYr><PrOthFinWeCrBudExp>30000</PrOthFinWeCrBudExp><PrOthFinWeCrCurrExp>300000</PrOthFinWeCrCurrExp></NeedAn>";

            String strValue = "<NeedAn>" + "<PrGe>" + str_gender + "</PrGe>"
                    + "<PrAge>" + tv_age.getText().toString() + "</PrAge>"
                    + "<PrMarrSt>" + str_marital_status + "</PrMarrSt>"
                    + "<PrDependCh>" + str_no_of_child + "</PrDependCh>"
                    + "<PrAnnIncome>" + strYearlyIncome + "</PrAnnIncome>"
                    + "<PrAnnExp>" + stryearlyIncome_emi + "</PrAnnExp>"
                    + "<PrOutStaHmLoanAmt>" + strOutstandingHomeLoan
                    + "</PrOutStaHmLoanAmt>" + "<PrOutStaOthrLoanAmt>"
                    + strOutstandingHomeLoanother + "</PrOutStaOthrLoanAmt>"
                    + "<currLifeInCov>" + strCurrentLifeInsuranceCoverage
                    + "</currLifeInCov>" + "<currRetCorpSav>"
                    + strRetirementCurrentCorpus + "</currRetCorpSav>"
                    + "<PrRetAge>" + strRetirementNoOfRealise + "</PrRetAge>"
                    + "<PrPostRetLife>" + strRetirementLifeStyle
                    + "</PrPostRetLife>" + "<PrEduChAge1>" + strChild1Age
                    + "</PrEduChAge1>" + "<PrEduChBudExpNoYrs1>"
                    + strChild1AgeAtEducation + "</PrEduChBudExpNoYrs1>"
                    + "<PrEduChBudEXp1>" + strChild1CorpusEducation
                    + "</PrEduChBudEXp1>" + "<PrEduChCurrEXp1>"
                    + strChild1CurrentCostEducation + "</PrEduChCurrEXp1>"
                    + "<PrEduChAge2>" + strChild2Age + "</PrEduChAge2>"
                    + "<PrEduChBudExpNoYrs2>" + strChild2AgeAtEducation
                    + "</PrEduChBudExpNoYrs2>" + "<PrEduChBudEXp2>"
                    + strChild2CorpusEducation + "</PrEduChBudEXp2>"
                    + "<PrEduChCurrEXp2>" + strChild2CurrentCostEducation
                    + "</PrEduChCurrEXp2>" + "<PrEduChAge3>" + strChild3Age
                    + "</PrEduChAge3>" + "<PrEduChBudExpNoYrs3>"
                    + strChild3AgeAtEducation + "</PrEduChBudExpNoYrs3>"
                    + "<PrEduChBudEXp3>" + strChild3CorpusEducation
                    + "</PrEduChBudEXp3>" + "<PrEduChCurrEXp3>"
                    + strChild3CurrentCostEducation + "</PrEduChCurrEXp3>"
                    + "<PrEduChAge4>" + strChild4Age + "</PrEduChAge4>"
                    + "<PrEduChBudExpNoYrs4>" + strChild4AgeAtEducation
                    + "</PrEduChBudExpNoYrs4>" + "<PrEduChBudEXp4>"
                    + strChild4CorpusEducation + "</PrEduChBudEXp4>"
                    + "<PrEduChCurrEXp4>" + strChild4CurrentCostEducation
                    + "</PrEduChCurrEXp4>" + "<PrMaChBudExpNoYrs1>"
                    + strChild1AgeAtMarriage + "</PrMaChBudExpNoYrs1>"
                    + "<PrMaChBudEXp1>" + strChild1CorpusMarriage
                    + "</PrMaChBudEXp1>" + "<PrMaChCurrEXp1>"
                    + strChild1CurrentCostMarriage + "</PrMaChCurrEXp1>"
                    + "<PrMaChCurrExpNoYrs2>" + strChild2AgeAtMarriage
                    + "</PrMaChCurrExpNoYrs2>" + "<PrMaChBudEXp2>"
                    + strChild2CorpusMarriage + "</PrMaChBudEXp2>"
                    + "<PrMaChCurrEXp2>" + strChild2CurrentCostMarriage
                    + "</PrMaChCurrEXp2>" + "<PrMaChBudExpNoYrs3>"
                    + strChild3AgeAtMarriage + "</PrMaChBudExpNoYrs3>"
                    + "<PrMaChBudEXp3>" + strChild3CorpusMarriage
                    + "</PrMaChBudEXp3>" + "<PrMaChCurrEXp3>"
                    + strChild3CurrentCostMarriage + "</PrMaChCurrEXp3>"
                    + "<PrMaChBudExpNoYrs4>" + strChild4AgeAtMarriage
                    + "</PrMaChBudExpNoYrs4>" + "<PrMaChBudEXp4>"
                    + strChild4CorpusMarriage + "</PrMaChBudEXp4>"
                    + "<PrMaChCurrEXp4>" + strChild4CurrentCostMarriage
                    + "</PrMaChCurrEXp4>" + "<PrProWeCrNoofYr>"
                    + strNoOfYearBuyHome + "</PrProWeCrNoofYr>"
                    + "<PrProWeCrBudExp>" + strCorpusForhome
                    + "</PrProWeCrBudExp>" + "<PrProWeCrCurrExp>"
                    + strCurrentCosthome + "</PrProWeCrCurrExp>"
                    + "<PrOthFinWeCrNoofYr>" + strNoOfYearOthergoal
                    + "</PrOthFinWeCrNoofYr>" + "<PrOthFinWeCrBudExp>"
                    + strCorpusForOther + "</PrOthFinWeCrBudExp>"
                    + "<PrOthFinWeCrCurrExp>" + strCurrentCostOther
                    + "</PrOthFinWeCrCurrExp>" + "</NeedAn>";
            request.addProperty("strInput", strValue);
            request.addProperty("reqFrom", "CON");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            // allowAllSSL();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
            try {
                androidHttpTranport.call(SOAP_ACTION_NEED_ANALYSIS, envelope);
                Object response = envelope.getResponse();

                if (!response.toString().contains("anyType{}")) {

                    SoapPrimitive sa = null;
                    try {
                        sa = (SoapPrimitive) envelope.getResponse();

                        outputlist = sa.toString();

                        ParseXML prsObj = new ParseXML();

                        outputlist = prsObj.parseXmlTag(outputlist, "NeedAn");
                        outputlist = new ParseXML().parseXmlTag(outputlist,
                                "ScreenData");
                        strErrorCOde = outputlist;

                        if (strErrorCOde == null) {
                            outputlist = sa.toString();
                            outputlist = prsObj.parseXmlTag(outputlist,
                                    "NeedAn");

                            // protection
                            LifeProtCovCorpus = new ParseXML().parseXmlTag(
                                    outputlist, "LifeProtCurrCoverAmt");
                            LifeProtGap = new ParseXML().parseXmlTag(
                                    outputlist, "LifeProtGapAmt");

                            double LifeProtCovCorpus_ = Double
                                    .parseDouble(LifeProtCovCorpus);
                            double LifeProtGap_ = Double
                                    .parseDouble(LifeProtGap);
                            double LifeProtTarget_ = LifeProtCovCorpus_
                                    + LifeProtGap_;

                            StackedProtectionBarChart(LifeProtCovCorpus_,
                                    LifeProtGap_, LifeProtTarget_);

                            // proposed plan

                            String Protection_Plan = new ParseXML()
                                    .parseXmlTag(outputlist, "ProdLstLifeProt")
                                    .trim();

                            if (!Protection_Plan.contentEquals("")) {

                                if (!Protection_Plan.contentEquals("null")) {

                                    String[] str_Pro = Protection_Plan
                                            .split("/");
                                    for (int i = 0; i < str_Pro.length; i++) {
                                        String str_Name = str_Pro[i]
                                                .trim();
                                        String[] str_Name_split = str_Name
                                                .trim().split(";");
                                        String str_Code = str_Name_split[1].trim();

                                        protection_prod_list
                                                .add(new SuggestedProdList(
                                                        str_Code,
                                                        str_Name_split[2]
                                                                .trim(),
                                                        str_Name_split[0]
                                                                .trim(), false));

                                        if (str_usertype
                                                .equalsIgnoreCase("IMF")) {

                                            if (str_Code.contains("35")
                                                    || str_Code.contains("1B")
                                                    || str_Code.contains("1N")
                                                    || str_Code.contains("1P")
                                                    || str_Code.contains("1R")
                                                    || str_Code.contains("1W") || str_Code.contains("2E") || str_Code.contains("2G") || str_Code.contains("2J")) {

                                                suggested_prod_list
                                                        .add(new SuggestedProdList(
                                                                str_Code,
                                                                str_Name_split[2]
                                                                        .trim(),
                                                                str_Name_split[0]
                                                                        .trim(), false));
                                            }

                                        } else {
                                           /* if (str_Code.equalsIgnoreCase("1K") || str_Code.equalsIgnoreCase("2F")) {
                                                combo1_flag_presnt = true;
                                                combo3_flag_presnt = true;
                                            }
                                            if (str_Code.equalsIgnoreCase("2D") || str_Code.equalsIgnoreCase("2F")) {
                                                combo2_flag_presnt = true;
                                                combo4_flag_presnt = true;
                                            }
                                            if (str_Code.equalsIgnoreCase("1H") || str_Code.equalsIgnoreCase("2F")) {
                                                combo5_flag_presnt = true;

                                            }*/
                                            suggested_prod_list
                                                    .add(new SuggestedProdList(
                                                            str_Code,
                                                            str_Name_split[2]
                                                                    .trim(),
                                                            str_Name_split[0]
                                                                    .trim(),
                                                            false));
                                        }
                                        traditional_prod_list
                                                .add(new SuggestedProdList(
                                                        str_Code,
                                                        str_Name_split[2]
                                                                .trim(),
                                                        str_Name_split[0]
                                                                .trim(), false));
                                        if (str_Code.contains("47")) {
                                            txt_pro_saral_shield
                                                    .setVisibility(View.VISIBLE);
                                        } else if (str_Code.contains("1G")) {
                                            txt_pro_eshield
                                                    .setVisibility(View.VISIBLE);
                                        } else if (str_Code.contains("45")) {
                                            txt_pro_smart_shield
                                                    .setVisibility(View.VISIBLE);
                                        } else if (str_Code.contains("1F")) {
                                            txt_pro_grameen_bima
                                                    .setVisibility(View.VISIBLE);
                                        }

                                    }

                                }
                            }

                        } else {
                            Toast.makeText(context, "Server Not Responding.",
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Server Not Responding.",
                            Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void StackedProtectionBarChart(double LifeProtCovCorpus_,
                                               double LifeProtGap_, double LifeProtTarget_) {
            // txt_protection.setText("Target Life Insurance Coverage Rs."
            // + getformatedThousandString(Integer
            // .parseInt(getRound(getStringWithout_E(Double
            // .valueOf(LifeProtTarget_))))));

            txt_protection.setText("Target Life Insurance Coverage Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(str_protection_cor_req))))));
            double a, b;
            // protection
            if (LifeProtCovCorpus_ > LifeProtTarget_) {

                a = Math.round((LifeProtTarget_ * 100) / LifeProtCovCorpus_);
                b = Math.round(100 - a);
                a = a * 1.9;
                b = b * 1.9;
                LayoutParams params = (LayoutParams) ln_chart_pro_top
                        .getLayoutParams();
                params.height = (int) a;
                params.width = 40;

                LayoutParams params1 = (LayoutParams) ln_chart_pro_buttom
                        .getLayoutParams();
                params1.height = (int) b;
                params1.width = 40;

                LayoutParams params1_side = (LayoutParams) vw_pro_top_side
                        .getLayoutParams();
                params1_side.height = (int) a;
            } else {

                a = Math.round((LifeProtCovCorpus_ * 100) / LifeProtTarget_);
                b = Math.round(100 - a);
                a = a * 1.9;
                b = b * 1.9;
                LayoutParams params = (LayoutParams) ln_chart_pro_top
                        .getLayoutParams();
                params.height = (int) b;
                params.width = 40;

                LayoutParams params1 = (LayoutParams) ln_chart_pro_buttom
                        .getLayoutParams();
                params1.height = (int) a;
                params1.width = 40;

                LayoutParams params1_side = (LayoutParams) vw_pro_top_side
                        .getLayoutParams();
                params1_side.height = (int) b;
            }

            txt_pro_gap.setText("Gap to target Coverage Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(LifeProtGap_))))));
            txt_pro_target_amt.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(LifeProtTarget_))))));
            txt_pro_corpus.setText("Current Coverage Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(LifeProtCovCorpus_))))));

        }

        private void StackedChildBarChart(double Child_1_edu_CurCorpus_,
                                          double Child_1_edu_Gap_, double Child_1_edu_Target_,
                                          double Child_1_mar_CurCorpus_, double Child_1_mar_Gap_,
                                          double Child_1_mar_Target_, double Child_2_edu_CurCorpus_,
                                          double Child_2_edu_Gap_, double Child_2_edu_Target_,
                                          double Child_2_mar_CurCorpus_, double Child_2_mar_Gap_,
                                          double Child_2_mar_Target_, double Child_3_edu_CurCorpus_,
                                          double Child_3_edu_Gap_, double Child_3_edu_Target_,
                                          double Child_3_mar_CurCorpus_, double Child_3_mar_Gap_,
                                          double Child_3_mar_Target_, double Child_4_edu_CurCorpus_,
                                          double Child_4_edu_Gap_, double Child_4_edu_Target_,
                                          double Child_4_mar_CurCorpus_, double Child_4_mar_Gap_,
                                          double Child_4_mar_Target_) {

            double child_1_tot_target = Child_1_edu_Target_
                    + Child_1_mar_Target_;
            double child_2_tot_target = Child_2_edu_Target_
                    + Child_2_mar_Target_;
            double child_3_tot_target = Child_3_edu_Target_
                    + Child_3_mar_Target_;
            double child_4_tot_target = Child_4_edu_Target_
                    + Child_4_mar_Target_;

            txt_child_1_tot_amt.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(child_1_tot_target))))));
            txt_child_2_tot_amt.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(child_2_tot_target))))));
            txt_child_3_tot_amt.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(child_3_tot_target))))));
            txt_child_4_tot_amt.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(child_4_tot_target))))));

            txt_child_1_edu_tar_amount.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_1_edu_Target_))))) + "");
            txt_child_1_edu_gap.setText("Gap to target"

                    + " amount Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_1_edu_Gap_))))));
            txt_child_1_edu_corpus.setText("Current Corpus(FV*)"
                    + "\n"
                    + " Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_1_edu_CurCorpus_))))));
            txt_child_1_marriage_tar_amt.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_1_mar_Target_))))) + "");
            txt_child_1_marriage_gap.setText("Gap to target"
                    + " amount Rs. "
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_1_mar_Gap_))))));
            txt_child_1_mrg_corpus.setText("Current Corpus(FV*)"
                    + "\n"
                    + " Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_1_mar_CurCorpus_))))));

            txt_child_2_edu_tar_amount.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_2_edu_Target_))))) + "");
            txt_child_2_edu_gap.setText("Gap to target"

                    + " amount Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_2_edu_Gap_))))));
            txt_child_2_edu_corpus.setText("Current Corpus(FV*) Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_2_edu_CurCorpus_))))));
            txt_child_2_marriage_tar_amt.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_2_mar_Target_))))) + "");
            txt_child_2_marriage_gap.setText("Gap to target"

                    + "amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_2_mar_Gap_))))));
            txt_child_2_mrg_corpus.setText("Current Corpus(FV*) Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_2_mar_CurCorpus_))))));

            txt_child_3_edu_tar_amount.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_3_edu_Target_))))) + "");
            txt_child_3_edu_gap.setText("Gap to target"

                    + " amount Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_3_edu_Gap_))))));
            txt_child_3_edu_corpus.setText("Current Corpus(FV*) Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_3_edu_CurCorpus_))))));
            txt_child_3_marriage_tar_amt.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_3_mar_Target_))))) + "");
            txt_child_3_marriage_gap.setText("Gap to target"

                    + " amount Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_3_mar_Gap_))))));
            txt_child_3_mrg_corpus.setText("Current Corpus(FV*) Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_3_mar_CurCorpus_))))));

            txt_child_4_edu_tar_amount.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_4_edu_Target_))))) + "");
            txt_child_4_edu_gap.setText("Gap to target"

                    + " amount Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_4_edu_Gap_))))));
            txt_child_4_edu_corpus.setText("Current Corpus(FV*) Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_4_edu_CurCorpus_))))));
            txt_child_4_marriage_tar_amt.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_4_mar_Target_))))) + " ");
            txt_child_4_marriage_gap.setText("Gap to target"

                    + " amount Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_4_mar_Gap_))))));
            txt_child_4_mrg_corpus.setText("Current Corpus(FV*) Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(Child_4_mar_CurCorpus_))))));

            // child 1 edu
            double c_1_d, c_1_d_;
            if (Child_1_edu_CurCorpus_ > Child_1_edu_Target_) {
                c_1_d = Math.round((Child_1_edu_Target_ * 100)
                        / Child_1_edu_CurCorpus_);
                c_1_d_ = Math.round(100 - c_1_d);
                c_1_d = c_1_d * 1.9;
                c_1_d_ = c_1_d_ * 1.9;

                LayoutParams params_child_1_e_top = (LayoutParams) ln_chart_child_child_1_edu_top
                        .getLayoutParams();
                params_child_1_e_top.height = (int) c_1_d;
                params_child_1_e_top.width = 40;

                // LayoutParams params_child_1_e_top_img = (LayoutParams)
                // img_chart_child_1_edu_top_side
                // .getLayoutParams();
                // params_child_1_e_top_img.height = (int) c_1_d;

                LayoutParams params_child_1_e_top_img = (LayoutParams) vw_child_1_edu_top_side
                        .getLayoutParams();
                params_child_1_e_top_img.height = (int) c_1_d;

                LayoutParams params_child_1_e_buttom = (LayoutParams) ln_chart_child_child_1_edu_buttom
                        .getLayoutParams();
                params_child_1_e_buttom.height = (int) c_1_d_;
                params_child_1_e_buttom.width = 40;

            } else {
                c_1_d = Math.round((Child_1_edu_CurCorpus_ * 100)
                        / Child_1_edu_Target_);
                c_1_d_ = Math.round(100 - c_1_d);
                c_1_d = c_1_d * 1.9;
                c_1_d_ = c_1_d_ * 1.9;
                LayoutParams params_child_1_e_top = (LayoutParams) ln_chart_child_child_1_edu_top
                        .getLayoutParams();
                params_child_1_e_top.height = (int) c_1_d_;
                params_child_1_e_top.width = 40;

                // LayoutParams params_child_1_e_top_img = (LayoutParams)
                // img_chart_child_1_edu_top_side
                // .getLayoutParams();
                // params_child_1_e_top_img.height = (int) c_1_d_;

                LayoutParams params_child_1_e_top_img = (LayoutParams) vw_child_1_edu_top_side
                        .getLayoutParams();
                params_child_1_e_top_img.height = (int) c_1_d_;

                LayoutParams params_child_1_e_buttom = (LayoutParams) ln_chart_child_child_1_edu_buttom
                        .getLayoutParams();
                params_child_1_e_buttom.height = (int) c_1_d;
                params_child_1_e_buttom.width = 40;
            }

            // child 1 mrg
            double c_1_m, c_1_m_;
            if (Child_1_mar_CurCorpus_ > Child_1_mar_Target_) {

                c_1_m = Math.round((Child_1_mar_Target_ * 100)
                        / Child_1_mar_CurCorpus_);
                c_1_m_ = Math.round(100 - c_1_m);
                c_1_m = c_1_m * 1.9;
                c_1_m_ = c_1_m_ * 1.9;
                LayoutParams params_child_1_m_top = (LayoutParams) ln_chart_child_child_1_mrg_top
                        .getLayoutParams();
                params_child_1_m_top.height = (int) c_1_m;
                params_child_1_m_top.width = 40;

                // LayoutParams params_child_1_m_top_img = (LayoutParams)
                // img_chart_child_1_mrg_top_side
                // .getLayoutParams();
                // params_child_1_m_top_img.height = (int) c_1_m;

                LayoutParams params_child_1_m_top_img = (LayoutParams) vw_child_1_mrg_top_side
                        .getLayoutParams();
                params_child_1_m_top_img.height = (int) c_1_m;

                LayoutParams params_child_1_m_buttom = (LayoutParams) ln_chart_child_child_1_mrg_buttom
                        .getLayoutParams();
                params_child_1_m_buttom.height = (int) c_1_m_;
                params_child_1_m_buttom.width = 40;
            } else {

                c_1_m = Math.round((Child_1_mar_CurCorpus_ * 100)
                        / Child_1_mar_Target_);
                c_1_m_ = Math.round(100 - c_1_m);
                c_1_m = c_1_m * 1.9;
                c_1_m_ = c_1_m_ * 1.9;
                LayoutParams params_child_1_m_top = (LayoutParams) ln_chart_child_child_1_mrg_top
                        .getLayoutParams();
                params_child_1_m_top.height = (int) c_1_m_;
                params_child_1_m_top.width = 40;

                // LayoutParams params_child_1_m_top_img = (LayoutParams)
                // img_chart_child_1_mrg_top_side
                // .getLayoutParams();
                // params_child_1_m_top_img.height = (int) c_1_m_;

                LayoutParams params_child_1_m_top_img = (LayoutParams) vw_child_1_mrg_top_side
                        .getLayoutParams();
                params_child_1_m_top_img.height = (int) c_1_m_;

                LayoutParams params_child_1_m_buttom = (LayoutParams) ln_chart_child_child_1_mrg_buttom
                        .getLayoutParams();
                params_child_1_m_buttom.height = (int) c_1_m;
                params_child_1_m_buttom.width = 40;
            }

            // child 2 edu
            double c_2_d, c_2_d_;
            if (Child_2_edu_CurCorpus_ > Child_2_edu_Target_) {
                c_2_d = Math.round((Child_2_edu_Target_ * 100)
                        / Child_2_edu_CurCorpus_);
                c_2_d_ = Math.round(100 - c_2_d);

                c_2_d = c_2_d * 1.9;
                c_2_d_ = c_2_d_ * 1.9;

                LayoutParams params_child_2_e_top = (LayoutParams) ln_chart_child_child_2_edu_top
                        .getLayoutParams();
                params_child_2_e_top.height = (int) c_2_d;
                params_child_2_e_top.width = 40;

                // LayoutParams params_child_2_e_top_img = (LayoutParams)
                // img_chart_child_2_edu__top_side
                // .getLayoutParams();
                // params_child_2_e_top_img.height = (int) c_2_d;

                LayoutParams params_child_2_e_top_img = (LayoutParams) vw_child_2_edu_top_side
                        .getLayoutParams();
                params_child_2_e_top_img.height = (int) c_2_d;

                LayoutParams params_child_2_e_buttom = (LayoutParams) ln_chart_child_child_2_edu_buttom
                        .getLayoutParams();
                params_child_2_e_buttom.height = (int) c_2_d_;
                params_child_2_e_buttom.width = 40;

            } else {
                c_2_d = Math.round((Child_2_edu_CurCorpus_ * 100)
                        / Child_2_edu_Target_);
                c_2_d_ = Math.round(100 - c_2_d);

                c_2_d = c_2_d * 1.9;
                c_2_d_ = c_2_d_ * 1.9;
                LayoutParams params_child_2_e_top = (LayoutParams) ln_chart_child_child_2_edu_top
                        .getLayoutParams();
                params_child_2_e_top.height = (int) c_2_d_;
                params_child_2_e_top.width = 40;

                // LayoutParams params_child_2_e_top_img = (LayoutParams)
                // img_chart_child_2_edu__top_side
                // .getLayoutParams();
                // params_child_2_e_top_img.height = (int) c_2_d_;

                LayoutParams params_child_2_e_top_img = (LayoutParams) vw_child_2_edu_top_side
                        .getLayoutParams();
                params_child_2_e_top_img.height = (int) c_2_d_;

                LayoutParams params_child_2_e_buttom = (LayoutParams) ln_chart_child_child_2_edu_buttom
                        .getLayoutParams();
                params_child_2_e_buttom.height = (int) c_2_d;
                params_child_2_e_buttom.width = 40;

            }
            double c_2_m, c_2_m_;
            // child 2 mrg
            if (Child_2_mar_CurCorpus_ > Child_2_mar_Target_) {

                c_2_m = Math.round((Child_2_mar_Target_ * 100)
                        / Child_2_mar_CurCorpus_);
                c_2_m_ = Math.round(100 - c_2_m);

                c_2_m = c_2_m * 1.9;
                c_2_m_ = c_2_m_ * 1.9;
                LayoutParams params_child_2_m_top = (LayoutParams) ln_chart_child_child_2_mrg_top
                        .getLayoutParams();
                params_child_2_m_top.height = (int) c_2_m;
                params_child_2_m_top.width = 40;

                // LayoutParams params_child_2_m_top_img = (LayoutParams)
                // img_chart_child_2_mrg_top_side
                // .getLayoutParams();
                // params_child_2_m_top_img.height = (int) c_2_m;

                LayoutParams params_child_2_m_top_img = (LayoutParams) vw_child_2_mrg_top_side
                        .getLayoutParams();
                params_child_2_m_top_img.height = (int) c_2_m;

                LayoutParams params_child_2_m_buttom = (LayoutParams) ln_chart_child_child_2_mrg_buttom
                        .getLayoutParams();
                params_child_2_m_buttom.height = (int) c_2_m_;
                params_child_2_m_buttom.width = 40;

            } else {

                c_2_m = Math.round((Child_2_mar_CurCorpus_ * 100)
                        / Child_2_mar_Target_);
                c_2_m_ = Math.round(100 - c_2_m);

                c_2_m = c_2_m * 1.9;
                c_2_m_ = c_2_m_ * 1.9;
                LayoutParams params_child_2_m_top = (LayoutParams) ln_chart_child_child_2_mrg_top
                        .getLayoutParams();
                params_child_2_m_top.height = (int) c_2_m_;
                params_child_2_m_top.width = 40;

                // LayoutParams params_child_2_m_top_img = (LayoutParams)
                // img_chart_child_2_mrg_top_side
                // .getLayoutParams();
                // params_child_2_m_top_img.height = (int) c_2_m_;

                LayoutParams params_child_2_m_top_img = (LayoutParams) vw_child_2_mrg_top_side
                        .getLayoutParams();
                params_child_2_m_top_img.height = (int) c_2_m_;

                LayoutParams params_child_2_m_buttom = (LayoutParams) ln_chart_child_child_2_mrg_buttom
                        .getLayoutParams();
                params_child_2_m_buttom.height = (int) c_2_m;
                params_child_2_m_buttom.width = 40;

            }

            // child 3 edu
            double c_3_d, c_3_d_;
            if (Child_3_edu_CurCorpus_ > Child_3_edu_Target_) {
                c_3_d = Math.round((Child_3_edu_Target_ * 100)
                        / Child_3_edu_CurCorpus_);
                c_3_d_ = Math.round(100 - c_3_d);
                c_3_d = c_3_d * 1.9;
                c_3_d_ = c_3_d_ * 1.9;
                LayoutParams params_child_3_e_top = (LayoutParams) ln_chart_child_child_3_edu_top
                        .getLayoutParams();
                params_child_3_e_top.height = (int) c_3_d;
                params_child_3_e_top.width = 40;

                // LayoutParams params_child_3_e_top_img = (LayoutParams)
                // img_chart_child_3_edu_top_side
                // .getLayoutParams();
                // params_child_3_e_top_img.height = (int) c_3_d;

                LayoutParams params_child_3_e_top_img = (LayoutParams) vw_child_3_edu_top_side
                        .getLayoutParams();
                params_child_3_e_top_img.height = (int) c_3_d;

                LayoutParams params_child_3_e_buttom = (LayoutParams) ln_chart_child_child_3_edu_buttom
                        .getLayoutParams();
                params_child_3_e_buttom.height = (int) c_3_d_;
                params_child_3_e_buttom.width = 40;

            } else {
                c_3_d = Math.round((Child_3_edu_CurCorpus_ * 100)
                        / Child_3_edu_Target_);
                c_3_d_ = Math.round(100 - c_3_d);
                c_3_d = c_3_d * 1.9;
                c_3_d_ = c_3_d_ * 1.9;
                LayoutParams params_child_3_e_top = (LayoutParams) ln_chart_child_child_3_edu_top
                        .getLayoutParams();
                params_child_3_e_top.height = (int) c_3_d_;
                params_child_3_e_top.width = 40;

                // LayoutParams params_child_3_e_top_img = (LayoutParams)
                // img_chart_child_3_edu_top_side
                // .getLayoutParams();
                // params_child_3_e_top_img.height = (int) c_3_d_;
                LayoutParams params_child_3_e_top_img = (LayoutParams) vw_child_3_edu_top_side
                        .getLayoutParams();
                params_child_3_e_top_img.height = (int) c_3_d;

                LayoutParams params_child_3_e_buttom = (LayoutParams) ln_chart_child_child_3_edu_buttom
                        .getLayoutParams();
                params_child_3_e_buttom.height = (int) c_3_d;
                params_child_3_e_buttom.width = 40;

            }

            // child 3 mrg
            double c_3_m, c_3_m_;
            if (Child_3_mar_CurCorpus_ > Child_3_mar_Target_) {
                c_3_m = Math.round((Child_3_mar_Target_ * 100)
                        / Child_3_mar_CurCorpus_);
                c_3_m_ = Math.round(100 - c_3_m);
                c_3_m = c_3_m * 1.9;
                c_3_m_ = c_3_m_ * 1.9;
                LayoutParams params_child_3_m_top = (LayoutParams) ln_chart_child_child_3_mrg_top
                        .getLayoutParams();
                params_child_3_m_top.height = (int) c_3_m;
                params_child_3_m_top.width = 40;

                // LayoutParams params_child_3_m_top_img = (LayoutParams)
                // img_chart_child_3_mrg_top_side
                // .getLayoutParams();
                // params_child_3_m_top_img.height = (int) c_3_m;

                LayoutParams params_child_3_m_top_img = (LayoutParams) vw_child_3_mrg_top_side
                        .getLayoutParams();
                params_child_3_m_top_img.height = (int) c_3_m;

                LayoutParams params_child_3_m_buttom = (LayoutParams) ln_chart_child_child_3_mrg_buttom
                        .getLayoutParams();
                params_child_3_m_buttom.height = (int) c_3_m_;
                params_child_3_m_buttom.width = 40;

            } else {
                c_3_m = Math.round((Child_3_mar_CurCorpus_ * 100)
                        / Child_3_mar_Target_);
                c_3_m_ = Math.round(100 - c_3_m);
                c_3_m = c_3_m * 1.9;
                c_3_m_ = c_3_m_ * 1.9;
                LayoutParams params_child_3_m_top = (LayoutParams) ln_chart_child_child_3_mrg_top
                        .getLayoutParams();
                params_child_3_m_top.height = (int) c_3_m_;
                params_child_3_m_top.width = 40;

                // LayoutParams params_child_3_m_top_img = (LayoutParams)
                // img_chart_child_3_mrg_top_side
                // .getLayoutParams();
                // params_child_3_m_top_img.height = (int) c_3_m_;

                LayoutParams params_child_3_m_top_img = (LayoutParams) vw_child_3_mrg_top_side
                        .getLayoutParams();
                params_child_3_m_top_img.height = (int) c_3_m_;

                LayoutParams params_child_3_m_buttom = (LayoutParams) ln_chart_child_child_3_mrg_buttom
                        .getLayoutParams();
                params_child_3_m_buttom.height = (int) c_3_m;
                params_child_3_m_buttom.width = 40;

            }

            // child 4 edu
            double c_4_d, c_4_d_;
            if (Child_4_edu_CurCorpus_ > Child_3_mar_Target_) {

                c_4_d = Math.round((Child_4_edu_CurCorpus_ * 100)
                        / Child_3_mar_Target_);
                c_4_d_ = Math.round(100 - c_4_d);
                c_4_d = c_4_d * 1.9;
                c_4_d_ = c_4_d_ * 1.9;
                LayoutParams params_child_4_e_top = (LayoutParams) ln_chart_child_child_4_edu_top
                        .getLayoutParams();
                params_child_4_e_top.height = (int) c_4_d;
                params_child_4_e_top.width = 40;

                // LayoutParams params_child_4_e_top_img = (LayoutParams)
                // img_chart_child_4_edu_top_side
                // .getLayoutParams();
                // params_child_4_e_top_img.height = (int) c_4_d;

                LayoutParams params_child_4_e_top_img = (LayoutParams) vw_child_4_edu_top_side
                        .getLayoutParams();
                params_child_4_e_top_img.height = (int) c_4_d;

                LayoutParams params_child_4_e_buttom = (LayoutParams) ln_chart_child_child_4_edu_buttom
                        .getLayoutParams();
                params_child_4_e_buttom.height = (int) c_4_d_;
                params_child_4_e_buttom.width = 40;

            } else {

                c_4_d = Math.round((Child_4_edu_CurCorpus_ * 100)
                        / Child_4_edu_Target_);
                c_4_d_ = Math.round(100 - c_4_d);
                c_4_d = c_4_d * 1.9;
                c_4_d_ = c_4_d_ * 1.9;
                LayoutParams params_child_4_e_top = (LayoutParams) ln_chart_child_child_4_edu_top
                        .getLayoutParams();
                params_child_4_e_top.height = (int) c_4_d_;
                params_child_4_e_top.width = 40;

                // LayoutParams params_child_4_e_top_img = (LayoutParams)
                // img_chart_child_4_edu_top_side
                // .getLayoutParams();
                // params_child_4_e_top_img.height = (int) c_4_d_;

                LayoutParams params_child_4_e_top_img = (LayoutParams) vw_child_4_edu_top_side
                        .getLayoutParams();
                params_child_4_e_top_img.height = (int) c_4_d_;

                LayoutParams params_child_4_e_buttom = (LayoutParams) ln_chart_child_child_4_edu_buttom
                        .getLayoutParams();
                params_child_4_e_buttom.height = (int) c_4_d;
                params_child_4_e_buttom.width = 40;

            }

            // child 4 mrg
            double c_4_m, c_4_m_;
            if (Child_4_mar_CurCorpus_ > Child_4_mar_Target_) {
                c_4_m = Math.round((Child_4_mar_Target_ * 100)
                        / Child_4_mar_CurCorpus_);
                c_4_m_ = Math.round(100 - c_4_m);
                c_4_m = c_4_m * 1.9;
                c_4_m_ = c_4_m_ * 1.9;
                LayoutParams params_child_4_m_top = (LayoutParams) ln_chart_child_child_4_mrg_top
                        .getLayoutParams();
                params_child_4_m_top.height = (int) c_4_m;
                params_child_4_m_top.width = 40;

                // LayoutParams params_child_4_m_top_img = (LayoutParams)
                // img_chart_child_4_mrg_top_side
                // .getLayoutParams();
                // params_child_4_m_top_img.height = (int) c_4_m;

                LayoutParams params_child_4_m_top_img = (LayoutParams) vw_child_4_mrg_top_side
                        .getLayoutParams();
                params_child_4_m_top_img.height = (int) c_4_m;

                LayoutParams params_child_4_m_buttom = (LayoutParams) ln_chart_child_child_4_mrg_buttom
                        .getLayoutParams();
                params_child_4_m_buttom.height = (int) c_4_m_;
                params_child_4_m_buttom.width = 40;
            } else {
                c_4_m = Math.round((Child_4_mar_CurCorpus_ * 100)
                        / Child_4_mar_Target_);
                c_4_m_ = Math.round(100 - c_4_m);
                c_4_m = c_4_m * 1.9;
                c_4_m_ = c_4_m_ * 1.9;
                LayoutParams params_child_4_m_top = (LayoutParams) ln_chart_child_child_4_mrg_top
                        .getLayoutParams();
                params_child_4_m_top.height = (int) c_4_m_;
                params_child_4_m_top.width = 40;

                // LayoutParams params_child_4_m_top_img = (LayoutParams)
                // img_chart_child_4_mrg_top_side
                // .getLayoutParams();
                // params_child_4_m_top_img.height = (int) c_4_m_;

                LayoutParams params_child_4_m_top_img = (LayoutParams) vw_child_4_mrg_top_side
                        .getLayoutParams();
                params_child_4_m_top_img.height = (int) c_4_m_;

                LayoutParams params_child_4_m_buttom = (LayoutParams) ln_chart_child_child_4_mrg_buttom
                        .getLayoutParams();
                params_child_4_m_buttom.height = (int) c_4_m;
                params_child_4_m_buttom.width = 40;
            }

        }

        // public void getOutput() {}

        private void StackedWealthCreationBarChart(double CostHomeTarged_,
                                                   double CostHomeCorpus_, double CostHomeGap_,
                                                   double CostOtherTarged_, double CostOtherCorpus_,
                                                   double CostOtherGap_) {
            txt_wealth_creation.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(CostHomeTarged_))))));

            // wealth
            double g, h;
            if (CostHomeCorpus_ > CostHomeTarged_) {
                g = Math.round((CostHomeTarged_ * 100) / CostHomeCorpus_);
                h = Math.round(100 - g);
                g = g * 1.9;
                h = h * 1.9;
                LayoutParams params6 = (LayoutParams) ln_chart_wealthcreation_top
                        .getLayoutParams();
                params6.height = (int) g;
                params6.width = 40;

                LayoutParams params7 = (LayoutParams) ln_wealthcreation_buttom
                        .getLayoutParams();
                params7.height = (int) h;
                params7.width = 40;

                LayoutParams params7_side = (LayoutParams) vw_wealth_top_side
                        .getLayoutParams();
                params7_side.height = (int) g;

            } else {
                g = Math.round((CostHomeCorpus_ * 100) / CostHomeTarged_);
                h = Math.round(100 - g);
                g = g * 1.9;
                h = h * 1.9;
                LayoutParams params6 = (LayoutParams) ln_chart_wealthcreation_top
                        .getLayoutParams();
                params6.height = (int) h;
                params6.width = 40;

                LayoutParams params7 = (LayoutParams) ln_wealthcreation_buttom
                        .getLayoutParams();
                params7.height = (int) g;
                params7.width = 40;

                LayoutParams params7_side = (LayoutParams) vw_wealth_top_side
                        .getLayoutParams();
                params7_side.height = (int) h;

            }

            txt_wealthcreation_gap.setText("Gap to target amount Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(CostHomeGap_))))));
            txt_wealthcreation_target.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(CostHomeTarged_))))));
            txt_wealthcreation_corpus.setText("Current Corpus(FV*) Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(CostHomeCorpus_))))));

            /* For Other */

            txt_wealth_creation_other.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(CostOtherTarged_))))));

            // wealth
            double a, b;
            if (CostHomeCorpus_ > CostHomeTarged_) {
                a = Math.round((CostHomeTarged_ * 100) / CostHomeCorpus_);
                b = Math.round(100 - a);
                a = a * 1.9;
                b = b * 1.9;
                LayoutParams params6 = (LayoutParams) ln_chart_wealthcreation_top_other
                        .getLayoutParams();
                params6.height = (int) a;
                params6.width = 40;

                LayoutParams params7 = (LayoutParams) ln_chart_wealthcreation_buttom_other
                        .getLayoutParams();
                params7.height = (int) b;
                params7.width = 40;

                LayoutParams params7_side = (LayoutParams) vw_wealthcreation_top_side_other
                        .getLayoutParams();
                params7_side.height = (int) a;

            } else {
                a = Math.round((CostHomeCorpus_ * 100) / CostHomeTarged_);
                b = Math.round(100 - a);
                a = a * 1.9;
                b = b * 1.9;
                LayoutParams params6 = (LayoutParams) ln_chart_wealthcreation_top_other
                        .getLayoutParams();
                params6.height = (int) b;
                params6.width = 40;

                LayoutParams params7 = (LayoutParams) ln_chart_wealthcreation_buttom_other
                        .getLayoutParams();
                params7.height = (int) a;
                params7.width = 40;

                LayoutParams params7_side = (LayoutParams) vw_wealthcreation_top_side_other
                        .getLayoutParams();
                params7_side.height = (int) b;

            }

            txt_wealthcreation_gap_other.setText("Gap to target amount Rs."
                    + "\n"
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(CostOtherGap_))))));
            txt_wealthcreation_target_other.setText("Target amount Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(CostOtherTarged_))))));
            txt_wealthcreation_corpus_other.setText("Current Corpus(FV*) Rs."
                    + getformatedThousandString(Integer
                    .parseInt(getRound(getStringWithout_E(Double
                            .valueOf(CostOtherCorpus_))))));

        }

        String getRound(String dummy) {
            String[] dummyArr = split(dummy, ".");
            try {
                if (Integer.parseInt("" + dummyArr[1].charAt(0)) >= 5) {
                    return "" + (Integer.parseInt(dummyArr[0]) + 1);
                } else {
                    return dummyArr[0];
                }
            } catch (Exception e) {
                return dummyArr[0];
            }
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        String[] split(String original, String separator) {
            Vector nodes = new Vector();
            int index = original.indexOf(separator);
            while (index >= 0) {
                nodes.addElement(original.substring(0, index));
                original = original.substring(index + separator.length());
                index = original.indexOf(separator);
            }
            nodes.addElement(original);
            String[] result = new String[nodes.size()];
            if (nodes.size() > 0) {
                for (int loop = 0; loop < nodes.size(); loop++)
                    result[loop] = (String) nodes.elementAt(loop);
            }
            return result;
        }

        String getStringWithout_E(double doubleVal) {

            String dStr = "" + doubleVal;

            // System.out.println("******************String with e***"+dStr );
            int indexOf_E = dStr.indexOf('E');
            int indexOf_e = dStr.indexOf('e');
            String seperator = null;
            String[] strPartsByE = new String[2];
            String[] strPartsByDecimal = new String[2];
            String rawStrWithoutDecimal = null;
            if ((indexOf_E > 0) || (indexOf_e > 0)) {
                if (indexOf_E > 0) {
                    seperator = "E";
                } else if (indexOf_e > 0) {
                    seperator = "e";
                }
                strPartsByE = split(dStr, seperator);
                strPartsByDecimal = split(strPartsByE[0], ".");
                rawStrWithoutDecimal = strPartsByDecimal[0]
                        + strPartsByDecimal[1] + "00000000000000000000";

                /*
                 * String temp = rawStrWithoutDecimal.substring(0, 1 +
                 * Integer.parseInt(strPartsByE[1]));
                 */

                /*
                 * String temp1 = rawStrWithoutDecimal.substring( 1 +
                 * Integer.parseInt(strPartsByE[1]), 4 +
                 * Integer.parseInt(strPartsByE[1]));
                 */

                if (strPartsByDecimal[0].contains("-")) {
                    return (rawStrWithoutDecimal.substring(0,
                            2 + Integer.parseInt(strPartsByE[1])))
                            + "."
                            + rawStrWithoutDecimal.substring(
                            1 + Integer.parseInt(strPartsByE[1]),
                            4 + Integer.parseInt(strPartsByE[1]));

                } else {
                    return (rawStrWithoutDecimal.substring(0,
                            1 + Integer.parseInt(strPartsByE[1])))
                            + "."
                            + rawStrWithoutDecimal.substring(
                            1 + Integer.parseInt(strPartsByE[1]),
                            4 + Integer.parseInt(strPartsByE[1]));

                }

                // return (rawStrWithoutDecimal.substring(0,
                // 1 + Integer.parseInt(strPartsByE[1])))
                // + "."
                // + rawStrWithoutDecimal.substring(
                // 1 + Integer.parseInt(strPartsByE[1]),
                // 4 + Integer.parseInt(strPartsByE[1]));
            } else {
                return dStr;
            }
        }
    }

    public void OnMailPDF(View v) {

        if (chosen_prod_list.size() == 0)
            Toast.makeText(context, "Please Select Product", Toast.LENGTH_LONG)
                    .show();
        else if (proposer_sign == null || proposer_sign.equals(""))
            Toast.makeText(context, "Please get Customer's Signature",
                    Toast.LENGTH_LONG).show();
        else if (agent_sign == null || agent_sign.equals(""))
            Toast.makeText(context, "Please get Intermediary's Signature",
                    Toast.LENGTH_LONG).show();
        else
            ForgetDialog();

    }

    /*
     * public void OnAllPlanPro(View v) {
     *
     * Intent intent = new Intent(NeedAnalysisAppCompatActivity.this,
     * MainActivity.class); intent.putExtra("AgentInfo", "FALSE");
     * intent.putExtra("AgentCode", AgentCode); intent.putExtra("usertype",
     * str_usertype); intent.putExtra("ViewAll", "ViewAll");
     * startActivity(intent);
     *
     * }
     */


    private String getCurrentDate() {
        Calendar present_date = Calendar.getInstance();
        int mDay = present_date.get(Calendar.DAY_OF_MONTH);
        int mMonth = present_date.get(Calendar.MONTH) + 1;
        int mYear = present_date.get(Calendar.YEAR);

        return mDay + "-" + mMonth + "-" + mYear;

    }

    public void OnNext(View v) {


        // if group = other then generate URN else regular flow
        if (str_group.equalsIgnoreCase("Other")) {

            // generate URN Number n save
            if (mCommonMethods.isNetworkConnected(context)) {
                new UIN_NO_ServiceHit().execute(null, null, null);
            } else {
                showDialog("Please check your internet connection.");
            }
        } else {
            ll_suggestdProdList.setVisibility(View.VISIBLE);
            ll_solution.setVisibility(View.GONE);

            ll_my_details.setVisibility(View.GONE);
            ll_my_goals.setVisibility(View.GONE);
            ll_solution.setVisibility(View.GONE);
            btn_my_details.setVisibility(View.GONE);
            btn_my_goals.setVisibility(View.GONE);
            btn_my_solution.setVisibility(View.GONE);
            ll_summary.setVisibility(View.GONE);
            ll_protection.setVisibility(View.GONE);
            ll_retirement.setVisibility(View.GONE);
            ll_wealth_creation.setVisibility(View.GONE);
            ll_child.setVisibility(View.GONE);

            remainingProductFillSpinner();

            CustomSuggestedProdListAdapter dataAdapter = new CustomSuggestedProdListAdapter(this,
                    R.layout.suggested_prod_list_item, suggested_prod_list);

            CustomOtherProdListAdapter remainingdataAdapter = new CustomOtherProdListAdapter(this,
                    R.layout.suggested_prod_list_item, remaining_product_list);

            System.out.println("Size : " + remaining_product_list.size());

            // Assign adapter to ListView
            lv_suggested_prod.setAdapter(dataAdapter);
            justifyListViewHeightBasedOnChildren(lv_suggested_prod);

            lv_remaining_prod.setAdapter(remainingdataAdapter);
            justifyListViewHeightBasedOnChildren(lv_remaining_prod);

            int star_flag = 0;
            int hash_flag = 0;
            for (int i = 0; i < suggested_prod_list.size(); i++) {

                if (suggested_prod_list.get(i).getProduct_name().contains("*")) {
                    star_flag = 1;
                    // break;
                }
                if (suggested_prod_list.get(i).getProduct_name().contains("#")) {
                    hash_flag = 1;
                    // break;
                }
            }

            if (star_flag == 1 && hash_flag == 1) {
                txt_star_def.setVisibility(View.VISIBLE);
                txt_hash_def.setVisibility(View.VISIBLE);
            } else if (star_flag == 0 && hash_flag == 1) {
                txt_star_def.setVisibility(View.GONE);
                txt_hash_def.setVisibility(View.VISIBLE);
            } else if (star_flag == 1 && hash_flag == 0) {
                txt_star_def.setVisibility(View.VISIBLE);
                txt_hash_def.setVisibility(View.GONE);
            } else {
                txt_star_def.setVisibility(View.GONE);
                txt_hash_def.setVisibility(View.GONE);
            }
            // ll_suggestdProdList.setFocusable(focusable)
            // btn_my_goals.requestFocus();
            // setFocusable(btn_my_goals);
            btn_need_output_focus.requestFocus();
            setFocusable(btn_need_output_focus);

        }

        hideKeyboard(NeedAnalysisActivity.this);

    }

    public void onWealthSmartWealthBuilder1(View v) {
        SmartWealthBuilder();
    }

    public void onWealthSmartWealthBuilder2(View v) {
        SmartWealthBuilder();
    }

    public void OnSmartElite1(View v) {
        SmartElite();
    }

    public void OnSmartElite2(View v) {
        SmartElite();
    }

    public void OnFlexiPlus1(View v) {
        FlexiSmartPlus();
    }

    public void OnFlexiPlus2(View v) {
        FlexiSmartPlus();
    }

    public void OnShubhNivesh1(View v) {
        ShubhNivesh();
    }

    public void OnShubhNivesh2(View v) {
        ShubhNivesh();
    }

    public void OnSmartChamp1(View v) {
        SmartChamp();
    }

    public void OnGuaranteedSaving1(View v) {
        SmartGuaranteed();
    }

    public void OnGuaranteedSaving2(View v) {
        SmartGuaranteed();
    }

    public void OnMoneyBackGold1(View v) {
        SmartMoneyBackGold();
    }

    public void OnMoneyBackGold2(View v) {
        SmartMoneyBackGold();
    }

    public void OnSmartShield1(View v) {
        SmartShield();
    }

    public void OnSmartScholar1(View v) {
        SmartScholar();
    }

    public void OnSmartScholar2(View v) {
        SmartScholar();
    }

    public void OnSmartRetire1(View v) {
        RetireSmart();
    }

    /*
     * public void getChannelDetail() { // M_ChannelDetails list_channel_detail
     * = db.getChannelDetail(AgentCode); strLicenseExpiryDate =
     * list_channel_detail.getLicense_ExpiryDate(); strULIPCert =
     * list_channel_detail.getULIPCert(); }
     */

    private void SmartWealthBuilder() {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }

            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {
                if (strULIPCert.equalsIgnoreCase("YES")) {
                    // ProductCategory = "ULIP";
                    // PlanName =
                    // getString(R.string.sbi_life_smart_wealth_builder);
                    // ProductCode =
                    // getString(R.string.sbi_life_smart_wealth_builder_code);

                    Intent i = new Intent(this,
                            BI_SmartWealthBuilderActivity.class);
                    i.putExtra("UnitPlusSuper", 0);
                    startActivity(i);

                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Alert")
                            .setMessage(
                                    "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products")
                            .setIcon(R.drawable.desktop_icon)
                            .setNeutralButton("OK", null).show();

                    Toast.makeText(
                            this,
                            "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products",
                            Toast.LENGTH_LONG).show();

                }
                // }
                // else
                // {
                // AlertDialogMessage dialog = new AlertDialogMessage();
                // dialog.dialog(this, null, true);
                // }
            }
        } catch (Exception ignored) {

        }

    }

    private void SmartElite() {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {

                if (strULIPCert.equalsIgnoreCase("YES")) {
                    // ProductCategory = "ULIP";
                    // if(validate())
                    // {
                    // PlanName = getString(R.string.sbi_life_smart_elite);
                    // ProductCode =
                    // getString(R.string.sbi_life_smart_elite_code);
                    Intent i = new Intent(this, BI_SmartEliteActivity.class);
                    startActivity(i);

                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Alert")
                            .setMessage(
                                    "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products")
                            .setIcon(R.drawable.desktop_icon)
                            .setNeutralButton("OK", null).show();

                    Toast.makeText(
                            this,
                            "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products",
                            Toast.LENGTH_LONG).show();

                }
            }
        } catch (Exception ignored) {

        }

    }

    private void FlexiSmartPlus() {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {

                // ProductCategory = "TRADITIONAL";
                // if(validate())
                // {
                // PlanName = getString(R.string.sbi_life_flexi_smart_plus);
                // ProductCode =
                // getString(R.string.sbi_life_flexi_smart_plus_code);
                Intent i = new Intent(this, FlexiSmartActivity.class);
                startActivity(i);

            }
        } catch (Exception ignored) {

        }

    }

    private void ShubhNivesh() {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {

                // if(validate())
                // {
                // ProductCategory = "TRADITIONAL";
                // PlanName = getString(R.string.sbi_life_shubh_nivesh);
                // ProductCode = getString(R.string.sbi_life_shubh_nivesh_code);
                Intent i = new Intent(this, BI_ShubhNiveshActivity.class);
                startActivity(i);

            }
        } catch (Exception ignored) {

        }

    }

    private void SmartChamp() {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            // long diffSeconds = diff / 1000 % 60;
            // long diffMinutes = diff / (60 * 1000) % 60;
            // long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {

                // if(validate())
                // {
                // ProductCategory = "TRADITIONAL";
                // PlanName =
                // getString(R.string.sbi_life_smart_champ_insurance);
                // ProductCode =
                // getString(R.string.sbi_life_smart_champ_insurance_code);
                Intent i = new Intent(this, BI_SmartChampActivity.class);
                startActivity(i);

            }
        } catch (Exception ignored) {

        }

    }

    private void SmartGuaranteed() {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {

                // if(validate())
                // {
                // ProductCategory = "TRADITIONAL";
                // PlanName =
                // getString(R.string.sbi_life_smart_guaranteed_saving_plan);
                // ProductCode =
                // getString(R.string.sbi_life_smart_guaranteed_saving_plan_code);
                Intent i = new Intent(this,
                        BI_SmartGuaranteedSavingsPlanActivity.class);
                startActivity(i);

            }
        } catch (Exception ignored) {

        }

    }

    private void SmartMoneyBackGold() {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {

                // if(validate())
                // {
                // ProductCategory = "TRADITIONAL";
                // PlanName =
                // getString(R.string.sbi_life_smart_money_back_gold);
                // ProductCode =
                // getString(R.string.sbi_life_smart_money_back_gold_code);
                Intent i = new Intent(this, BI_SmartMoneyBackGoldActivity.class);
                startActivity(i);

            }
        } catch (Exception ignored) {

        }

    }

    /*
     * public void SaralSwadhan() { if (AgentCode.contains("IA")) {
     * Toast.makeText(this, "You are not applicable for this Product",
     * Toast.LENGTH_LONG).show(); } else { try { SimpleDateFormat format = new
     * SimpleDateFormat("yyyy-MM-dd");
     *
     * String strdate = "";
     *
     * Date date = null; SimpleDateFormat extdate = new
     * SimpleDateFormat("dd-MM-yyyy"); SimpleDateFormat newdate = new
     * SimpleDateFormat("yyyy-MM-dd"); date =
     * extdate.parse(strLicenseExpiryDate); strdate = newdate.format(date);
     *
     * Date d1 = null; Date d2 = null;
     *
     * d1 = format.parse(formattedDate); d2 = format.parse(strdate);
     *
     * // in milliseconds long diff = d2.getTime() - d1.getTime();
     *
     * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff / (60 *
     * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24; long diffDays
     * = diff / (24 * 60 * 60 * 1000); if (AgentCode.contains("IA")) { diffDays
     * = 2; } if (diffDays <= 0) {
     *
     * new AlertDialog.Builder(this) .setTitle("Alert") .setMessage(
     * "You License has been expired so you are not applicable to sell Smart Advisor Products"
     * ) .setIcon(R.drawable.desktop_icon) .setNeutralButton("OK", null).show();
     *
     * Toast.makeText( this,
     * "You License has been expired so you are not applicable to sell Smart Advisor Products"
     * , Toast.LENGTH_LONG).show();
     *
     * } else {
     *
     * // if(validate()) // { ProductCategory = "TRADITIONAL"; PlanName =
     * getString(R.string.sbi_life_saral_swadhan_plus); ProductCode =
     * getString(R.string.sbi_life_saral_swadhan_plus_code); Intent i = new
     * Intent(this, Saral.class); startActivity(i);
     *
     * } } catch (Exception e) {
     *
     * } } }
     */
    private void SmartShield() {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {

                // if(validate())
                // {
                // ProductCategory = "TRADITIONAL";
                // PlanName = getString(R.string.sbi_life_smart_shield);
                // ProductCode = getString(R.string.sbi_life_smart_shield_code);
                Intent i = new Intent(this, BI_SmartShieldActivity.class);
                startActivity(i);

            }
        } catch (Exception ignored) {

        }

    }

    private void SmartScholar() {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {
                if (strULIPCert.equalsIgnoreCase("YES")) {
                    // ProductCategory = "ULIP";
                    // if(validate())
                    // {
                    // ProductCategory = "ULIP";
                    // PlanName = getString(R.string.sbi_life_smart_scholar);
                    // ProductCode =
                    // getString(R.string.sbi_life_smart_scholar_code);
                    Intent i = new Intent(this, BI_SmartScholarActivity.class);
                    startActivity(i);

                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Alert")
                            .setMessage(
                                    "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products")
                            .setIcon(R.drawable.desktop_icon)
                            .setNeutralButton("OK", null).show();

                    Toast.makeText(
                            this,
                            "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products",
                            Toast.LENGTH_LONG).show();

                }
                // }
                // else
                // {
                // AlertDialogMessage dialog = new AlertDialogMessage();
                // dialog.dialog(this, null, true);
                // }
            }
        } catch (Exception ignored) {

        }

    }

    private void RetireSmart() {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {
                if (strULIPCert.equalsIgnoreCase("YES")) {
                    // ProductCategory = "ULIP";
                    // if(validate())
                    // {
                    // PlanName = getString(R.string.sbi_life_Retire_smart);
                    // ProductCode =
                    // getString(R.string.sbi_life_Retire_smart_code);
                    Intent i = new Intent(this, BI_RetireSmartActivity.class);
                    startActivity(i);

                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Alert")
                            .setMessage(
                                    "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products")
                            .setIcon(R.drawable.desktop_icon)
                            .setNeutralButton("OK", null).show();

                    Toast.makeText(
                            this,
                            "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products",
                            Toast.LENGTH_LONG).show();

                }
                // }
                // else
                // {
                // AlertDialogMessage dialog = new AlertDialogMessage();
                // dialog.dialog(this, null, true);
                // }
            }
        } catch (Exception ignored) {

        }

    }

    private void OnSaralShield() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {

                // ProductCategory = "TRADITIONAL";
                // PlanName = getString(R.string.sbi_life_saral_shield);
                // ProductCode = getString(R.string.sbi_life_saral_shield_code);
                Intent i = new Intent(this, BI_SaralShieldActivity.class);
                startActivity(i);

            }
        } catch (Exception ignored) {

        }

    }

    private void OnSmartIncomeProtect() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {

                // ProductCategory = "TRADITIONAL";
                // PlanName = getString(R.string.sbi_life_smart_income_protect);
                // ProductCode =
                // getString(R.string.sbi_life_smart_income_protect_code);
                Intent i = new Intent(this, BI_SmartIncomeProtectActivity.class);
                startActivity(i);

            }
        } catch (Exception ignored) {

        }

    }

    // public void OnSmartMoneyPlanner() {
    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    // try {
    // String strdate = "";
    //
    // Date date = null;
    // SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
    // SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
    // date = extdate.parse(strLicenseExpiryDate);
    // strdate = newdate.format(date);
    //
    // Date d1 = null;
    // Date d2 = null;
    //
    // d1 = format.parse(formattedDate);
    // d2 = format.parse(strdate);
    //
    // // in milliseconds
    // long diff = d2.getTime() - d1.getTime();
    //
    // long diffDays = diff / (24 * 60 * 60 * 1000);
    // if (AgentCode.contains("IA")) {
    // diffDays = 2;
    // }
    // if (diffDays <= 0) {
    //
    // new AlertDialog.Builder(this)
    // .setTitle("Alert")
    // .setMessage(
    // "You License has been expired so you are not applicable to sell Smart Advisor Products")
    // .setIcon(R.drawable.desktop_icon)
    // .setNeutralButton("OK", null).show();
    //
    // Toast.makeText(
    // this,
    // "You License has been expired so you are not applicable to sell Smart Advisor Products",
    // Toast.LENGTH_LONG).show();
    //
    // } else {
    //
    // ProductCategory = "TRADITIONAL";
    // PlanName = getString(R.string.sbi_life_smart_money_planner);
    // //ProductCode = getString(R.string.sbi_life_smart_money_planner_code);
    // Intent i = new Intent(this,
    // smartmo.class);
    // startActivity(i);
    //
    // }
    // } catch (Exception e) {
    //
    // }
    //
    // }

    private void OnSaralMahaAnand() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {
                if (strULIPCert.equalsIgnoreCase("YES")) {
                    // ProductCategory = "ULIP";
                    // if(validate())
                    // {
                    // ProductCategory = "ULIP";
                    // PlanName = getString(R.string.sbi_life_saral_maha_anand);
                    // ProductCode =
                    // getString(R.string.sbi_life_saral_maha_anand_code);
                    Intent i = new Intent(this, BI_SaralMahaAnandActivity.class);
                    startActivity(i);

                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Alert")
                            .setMessage(
                                    "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products")
                            .setIcon(R.drawable.desktop_icon)
                            .setNeutralButton("OK", null).show();

                    Toast.makeText(
                            this,
                            "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products",
                            Toast.LENGTH_LONG).show();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OnSmartWealthAssure() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {
                if (strULIPCert.equalsIgnoreCase("YES")) {
                    // ProductCategory = "ULIP";
                    // PlanName =
                    // getString(R.string.sbi_life_smart_wealth_assure);
                    // ProductCode =
                    // getString(R.string.sbi_life_smart_wealth_assure_code);
                    Intent i = new Intent(this,
                            BI_SmartWealthAssureActivity.class);
                    startActivity(i);

                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Alert")
                            .setMessage(
                                    "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products")
                            .setIcon(R.drawable.desktop_icon)
                            .setNeutralButton("OK", null).show();

                    Toast.makeText(
                            this,
                            "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products",
                            Toast.LENGTH_LONG).show();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OnSaralPension() {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {

                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {

                // if(validate())
                // {
                // ProductCategory = "TRADITIONAL";
                // PlanName = getString(R.string.sbi_life_saral_pension);
                // ProductCode =
                // getString(R.string.sbi_life_saral_pension_code);
                Intent i = new Intent(this, BI_SaralPensionActivity.class);
                startActivity(i);

            }
        } catch (Exception ignored) {

        }

    }

    private void OnSmartPowerInsurance() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String strdate = "";

            Date date = null;
            SimpleDateFormat extdate = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat newdate = new SimpleDateFormat("yyyy-MM-dd");
            date = extdate.parse(strLicenseExpiryDate);
            strdate = newdate.format(date);

            Date d1 = null;
            Date d2 = null;

            d1 = format.parse(formattedDate);
            d2 = format.parse(strdate);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /*
             * long diffSeconds = diff / 1000 % 60; long diffMinutes = diff /
             * (60 * 1000) % 60; long diffHours = diff / (60 * 60 * 1000) % 24;
             */
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (AgentCode.contains("IA")) {
                diffDays = 2;
            }
            if (diffDays <= 0) {

                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage(
                                "You License has been expired so you are not applicable to sell Smart Advisor Products")
                        .setIcon(R.drawable.desktop_icon)
                        .setNeutralButton("OK", null).show();

                Toast.makeText(
                        this,
                        "You License has been expired so you are not applicable to sell Smart Advisor Products",
                        Toast.LENGTH_LONG).show();

            } else {
                if (strULIPCert.equalsIgnoreCase("YES")) {
                    // ProductCategory = "ULIP";
                    // if(validate())
                    // {
                    // PlanName =
                    // getString(R.string.sbi_life_smart_power_insurance);
                    // ProductCode =
                    // getString(R.string.sbi_life_smart_power_insurance_code);
                    Intent i = new Intent(this,
                            BI_SmartPowerInsuranceActivity.class);
                    startActivity(i);

                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Alert")
                            .setMessage(
                                    "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products")
                            .setIcon(R.drawable.desktop_icon)
                            .setNeutralButton("OK", null).show();

                    Toast.makeText(
                            this,
                            "You are not ULIP Certified Agent, so you are not applicable to sell ULIP Products",
                            Toast.LENGTH_LONG).show();

                }

            }
        } catch (Exception ignored) {

        }

    }

    private void Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        // c.add(Calendar.DATE, 30);
        // ExpiredDate = df.format(c.getTime());
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

    }

    private void CreatePDF_Graphical(String FileName) {

        CommonForAllProd obj = new CommonForAllProd();

        String str_child1Name = btn_child1.getText().toString();
        String str_child2Name = btn_child2.getText().toString();
        String str_child3Name = btn_child3.getText().toString();
        String str_child4Name = btn_child4.getText().toString();
        int smart_wealth_builder_flag = 0;
        // String QuatationNumber = ProductHomePageActivity.quotation_Number;
        try {
            Font small_bold3 = new Font(Font.FontFamily.TIMES_ROMAN, 5,
                    Font.BOLD);
            Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD,
                    BaseColor.RED);
            Font sub_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD);

            Font white_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD, BaseColor.WHITE);
            /*
             * Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
             * Font.BOLD);
             */
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            /*
             * Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4,
             * Font.BOLD);
             */
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font small_normal1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            /*
             * Font small_italic = new Font(Font.FontFamily.TIMES_ROMAN, 8,
             * Font.ITALIC);
             */
            Font small_normal_biFont = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font big_normal_biFont = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD);
            Font medium_normal_biFont = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.NORMAL);
            Font medium_normal_underline_biFont = new Font(
                    Font.FontFamily.TIMES_ROMAN, 10, Font.UNDERLINE);

            // File mypath = new File(folder, PropserNumber +
            // "Proposalno_p02.pdf");
            File mypath = mStorageUtils.createFileToAppSpecificDir(context,FileName + ".pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 40, 30, 30, 30);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            @SuppressWarnings("unused")
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));
            document.open();
            PdfPCell cell;
            // For SBI- Life Logo starts
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext()
                    .getResources(), R.drawable.sbi_life_logo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            Image img_sbi_logo = Image.getInstance(stream.toByteArray());
            img_sbi_logo.setAlignment(Image.LEFT);
            img_sbi_logo.getSpacingAfter();
            img_sbi_logo.scaleToFit(80, 50);

            Paragraph para_img_logo = new Paragraph("");
            para_img_logo.add(img_sbi_logo);

            //document.add(para_img_logo);

            Paragraph para_img_logo_after_space_1 = new Paragraph(" ");

            PdfPTable URN_headertable = new PdfPTable(1);
            URN_headertable.setWidthPercentage(100);

            cell = new PdfPCell(img_sbi_logo);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            URN_headertable.addCell(cell);

            if (URN_NO != null) {
                if (URN_NO.equals("Fail") || URN_NO.equals("")
                        || (!FileName.equals("NA_Graphical")))
                    cell = new PdfPCell(new Phrase(""));
                else
                    cell = new PdfPCell(new Phrase(new Paragraph(" URN : "
                            + URN_NO, small_bold)));
            } else
                cell = new PdfPCell(new Phrase(new Paragraph("", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            URN_headertable.addCell(cell);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            document.add(URN_headertable);
            // For SBI- Life Logo ends

            // To draw line after the sbi logo image
            document.add(new LineSeparator());
            document.add(para_img_logo_after_space_1);

            // For the BI Smart Elite Table Header(Grey One)
            Paragraph Para_Header = new Paragraph();

            Para_Header.add(new Paragraph("Need Analysis Summary",
                    white_headerBold));


            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);
            PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
            c1.setBackgroundColor(BaseColor.DARK_GRAY);
            c1.setPadding(5);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headertable.addCell(c1);
            headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Co. Ltd \nCorporate Office: 'Natraj', M.V. Road and Western Express, Highway Junction, Andheri(East),Mumbai 400069. IRDAI Regn No. 111",
                    small_bold);
            para_address.setAlignment(Element.ALIGN_CENTER);

            Paragraph para_address1 = new Paragraph(
                    "Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113. Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
                    small_bold);
            // Paragraph para_address1 = new Paragraph(
            // "Waart",
            // urFont_gujarati_saral);

            // Paragraph para_address1 = new Paragraph(
            // "Aimat kumaar caaOhana",
            // urFont_shivaji);

            para_address1.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_address1);
            document.add(para_img_logo_after_space_1);
            document.add(headertable);
            document.add(para_img_logo_after_space_1);

            if (str_gender.equalsIgnoreCase("M")) {
                Salutation = "Sir";
            } else {
                Salutation = "Madam";
            }

            /*
             * String emailBody = "Dear " + Salutation + "" + "," + "\n" +
             * "We understand your need to secure your future and appreciate the time that you have taken to analyze your financial needs."
             * + "\n" +
             * "When you have a plan, it's easier to take financial decisions and stay on track to meet your future goals."
             * + "\n" +
             * "We are happy to present to you a roadmap to your dreams.";
             */

            // Paragraph para_emailBody = new Paragraph(emailBody, small_bold);
            // para_emailBody.setAlignment(Element.ALIGN_CENTER);
            // document.add(para_emailBody);
            // document.add(para_img_logo_after_space_1);

            Paragraph para_input;
            // if(str_gender.equalsIgnoreCase("F"))
            para_input = new Paragraph(
                    "Dear "
                            + Salutation
                            + ","
                            + "\nWe thank you for providing your personal and financial information, such as : age, income, assets, liabilities, risk profile, future financial goals etc.\n",
                    medium_normal_biFont);

            para_input.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para_input);
            document.add(para_img_logo_after_space_1);

            // For SBI- Life Logo starts
            // ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext()
            // .getResources(), R.drawable.sbi_life_logo);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            // Image img_sbi_logo = Image.getInstance(stream.toByteArray());
            // img_sbi_logo.setAlignment(Image.LEFT);
            // img_sbi_logo.getSpacingAfter();
            // img_sbi_logo.scaleToFit(50, 35);

            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            Bitmap personalbitmap = BitmapFactory.decodeResource(
                    getBaseContext().getResources(),
                    R.drawable.personal_details);
            personalbitmap.compress(Bitmap.CompressFormat.PNG, 50, stream2);
            Image img_personal_detail = Image
                    .getInstance(stream2.toByteArray());
            img_personal_detail.setAlignment(Image.LEFT);
            img_personal_detail.getSpacingAfter();
            img_personal_detail.scaleToFit(40, 40);

            ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
            Bitmap financialbitmap = BitmapFactory.decodeResource(
                    getBaseContext().getResources(),
                    R.drawable.financial_details);
            financialbitmap.compress(Bitmap.CompressFormat.PNG, 50, stream3);
            Image img_financial_detail = Image.getInstance(stream3
                    .toByteArray());
            img_financial_detail.setAlignment(Image.LEFT);
            img_financial_detail.getSpacingAfter();
            img_financial_detail.scaleToFit(40, 40);

            ByteArrayOutputStream childstream = new ByteArrayOutputStream();
            Bitmap childBitmap = ConvertToBitmap(ll_children_graph);
            childBitmap.compress(Bitmap.CompressFormat.PNG, 50, childstream);
            Image child_img_detail = Image.getInstance(childstream
                    .toByteArray());
            child_img_detail.setAlignment(Image.LEFT);
            child_img_detail.getSpacingAfter();
            child_img_detail.scaleToFit(80, 80);

            ByteArrayOutputStream retirementstream2 = new ByteArrayOutputStream();
            Bitmap retirementBitmap = ConvertToBitmap(ll_retirement_graph);
            retirementBitmap.compress(Bitmap.CompressFormat.PNG, 50,
                    retirementstream2);
            Image retirement_img_detail = Image.getInstance(retirementstream2
                    .toByteArray());
            retirement_img_detail.setAlignment(Image.LEFT);
            retirement_img_detail.getSpacingAfter();
            retirement_img_detail.scaleToFit(80, 80);

            ByteArrayOutputStream wealthstream = new ByteArrayOutputStream();
            Bitmap wealthBitmap = ConvertToBitmap(ll_wealth_graph);
            wealthBitmap.compress(Bitmap.CompressFormat.PNG, 50, wealthstream);
            Image wealth_img_detail = Image.getInstance(wealthstream
                    .toByteArray());
            wealth_img_detail.setAlignment(Image.LEFT);
            wealth_img_detail.getSpacingAfter();
            wealth_img_detail.scaleToFit(80, 80);

            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            Bitmap protectionBitmap = ConvertToBitmap(ll_protection_graph);
            protectionBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream1);
            Image protection_img_detail = Image.getInstance(stream1
                    .toByteArray());
            protection_img_detail.setAlignment(Image.LEFT);
            protection_img_detail.getSpacingAfter();
            protection_img_detail.scaleToFit(80, 80);

            ByteArrayOutputStream childinputstream = new ByteArrayOutputStream();
            Bitmap childinputBitmap = BitmapFactory.decodeResource(
                    getBaseContext().getResources(), R.drawable.children);
            childinputBitmap.compress(Bitmap.CompressFormat.PNG, 50,
                    childinputstream);
            Image child_inputimg_detail = Image.getInstance(childinputstream
                    .toByteArray());
            child_inputimg_detail.setAlignment(Image.LEFT);
            child_inputimg_detail.getSpacingAfter();
            child_inputimg_detail.scaleToFit(40, 40);

            ByteArrayOutputStream retirementinputstream2 = new ByteArrayOutputStream();
            Bitmap retirementinputBitmap = BitmapFactory.decodeResource(
                    getBaseContext().getResources(), R.drawable.retirement);
            retirementinputBitmap.compress(Bitmap.CompressFormat.PNG, 50,
                    retirementinputstream2);
            Image retirement_input_img_detail = Image
                    .getInstance(retirementinputstream2.toByteArray());
            retirement_input_img_detail.setAlignment(Image.LEFT);
            retirement_input_img_detail.getSpacingAfter();
            retirement_input_img_detail.scaleToFit(40, 40);

            ByteArrayOutputStream wealthinputstream = new ByteArrayOutputStream();
            Bitmap wealthinputBitmap = BitmapFactory
                    .decodeResource(getBaseContext().getResources(),
                            R.drawable.wealth_creation);
            wealthinputBitmap.compress(Bitmap.CompressFormat.PNG, 50,
                    wealthinputstream);
            Image wealth_input_img_detail = Image.getInstance(wealthinputstream
                    .toByteArray());
            wealth_input_img_detail.setAlignment(Image.LEFT);
            wealth_input_img_detail.getSpacingAfter();
            wealth_input_img_detail.scaleToFit(40, 40);

            ByteArrayOutputStream protectioninputstream = new ByteArrayOutputStream();
            Bitmap protectioninputBitmap = BitmapFactory.decodeResource(
                    getBaseContext().getResources(), R.drawable.protection);
            protectioninputBitmap.compress(Bitmap.CompressFormat.PNG, 50,
                    protectioninputstream);
            Image protection_input_img_detail = Image
                    .getInstance(protectioninputstream.toByteArray());
            protection_input_img_detail.setAlignment(Image.LEFT);
            protection_input_img_detail.getSpacingAfter();
            protection_input_img_detail.scaleToFit(40, 40);

            ByteArrayOutputStream checkedstream = new ByteArrayOutputStream();
            Bitmap checkedBitmap = BitmapFactory.decodeResource(
                    getBaseContext().getResources(),
                    R.drawable.checkbox_checked);
            checkedBitmap
                    .compress(Bitmap.CompressFormat.PNG, 50, checkedstream);
            Image checked_img_detail = Image.getInstance(checkedstream
                    .toByteArray());
            checked_img_detail.setAlignment(Image.LEFT);
            checked_img_detail.getSpacingAfter();
            checked_img_detail.scaleToFit(20, 20);

            ByteArrayOutputStream uncheckedstream = new ByteArrayOutputStream();
            Bitmap uncheckedBitmap = BitmapFactory.decodeResource(
                    getBaseContext().getResources(),
                    R.drawable.checkbox_unchecked);
            uncheckedBitmap.compress(Bitmap.CompressFormat.PNG, 50,
                    uncheckedstream);
            Image unchecked_img_detail = Image.getInstance(uncheckedstream
                    .toByteArray());
            unchecked_img_detail.setAlignment(Image.LEFT);
            unchecked_img_detail.getSpacingAfter();
            unchecked_img_detail.scaleToFit(20, 20);


            Paragraph personalDetails_header = new Paragraph();
            personalDetails_header.add(new Paragraph("Personal Details",
                    sub_headerBold));

            PdfPTable personalDetails_headertable = new PdfPTable(4);
            personalDetails_headertable.setSpacingBefore(4f);
            personalDetails_headertable.setWidthPercentage(100);
            PdfPCell personalDetails_headertable_cell = new PdfPCell(
                    new Phrase(personalDetails_header));
            // personalDetails_headertable_cell
            // .setBackgroundColor(BaseColor.LIGHT_GRAY);
            personalDetails_headertable_cell.setPadding(5);
            // personalDetails_headertable_cell
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // personalDetails_headertable
            // .addCell(personalDetails_headertable_cell);
            personalDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            personalDetails_headertable_cell.setBorder(Rectangle.BOTTOM);
            personalDetails_headertable_cell.setBorderColor(new BaseColor(230,
                    230, 230));
            personalDetails_headertable_cell.setColspan(4);
            // document.add(personalDetails_headertable);
            personalDetails_headertable
                    .addCell(personalDetails_headertable_cell);

            PdfPCell Nocell = new PdfPCell(new Paragraph("", small_bold));
            Nocell.setBorder(Rectangle.NO_BORDER);

            PdfPCell personal_image_cell = new PdfPCell(img_personal_detail,
                    true);
            personal_image_cell.setPadding(5);
            personal_image_cell.setRowspan(5);
            personal_image_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            personal_image_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            personal_image_cell.setBorder(Rectangle.NO_BORDER);
            personalDetails_headertable.addCell(personal_image_cell);
            /* Gender Table */
            String Gender = "";
            if (str_gender.equalsIgnoreCase("M")) {
                Gender = "Male";
            } else {
                Gender = "Female";
            }
            // PdfPTable PD_Gender = new PdfPTable(2);
            // PD_Gender.setWidthPercentage(100);
            PdfPCell Gender_cell1 = new PdfPCell(new Paragraph("Gender",
                    small_bold1));
            PdfPCell Gender_cell2 = new PdfPCell(new Paragraph(Gender,
                    small_normal1));
            // Gender_cell1.setPadding(5);
            // Gender_cell2.setPadding(5);
            Gender_cell1.setBorder(Rectangle.NO_BORDER);
            Gender_cell2.setBorder(Rectangle.NO_BORDER);

            PdfPCell DOB_cell1 = new PdfPCell(new Paragraph("DOB", small_bold1));
            PdfPCell DOB_cell2 = new PdfPCell(new Paragraph(
                    getDate(str_date_of_birth), small_normal1));
            // DOB_cell1.setPadding(5);
            // DOB_cell2.setPadding(5);

            DOB_cell1.setBorder(Rectangle.NO_BORDER);
            DOB_cell2.setBorder(Rectangle.NO_BORDER);
            PdfPCell Age_cell1 = new PdfPCell(new Paragraph("Age", small_bold1));
            PdfPCell Age_cell2 = new PdfPCell(new Paragraph(str_age,
                    small_normal1));
            // Age_cell1.setPadding(5);
            // Age_cell2.setPadding(5);
            Age_cell1.setBorder(Rectangle.NO_BORDER);
            Age_cell2.setBorder(Rectangle.NO_BORDER);

            personalDetails_headertable.addCell(Gender_cell1);
            personalDetails_headertable.addCell(DOB_cell1);
            personalDetails_headertable.addCell(Age_cell1);

            personalDetails_headertable.addCell(Gender_cell2);
            personalDetails_headertable.addCell(DOB_cell2);
            personalDetails_headertable.addCell(Age_cell2);

            PdfPCell personal_empty_cell = new PdfPCell(new Phrase(""));
            personal_empty_cell.setColspan(3);
            personal_empty_cell.setBorder(Rectangle.NO_BORDER);
            personalDetails_headertable.addCell(personal_empty_cell);

            PdfPCell MaritalStatus_cell1 = new PdfPCell(new Paragraph(
                    "Martial Status", small_bold1));
            PdfPCell MaritalStatus_cell2 = new PdfPCell(new Paragraph(
                    str_marital_status, small_normal1));
            // MaritalStatus_cell1.setPadding(5);
            // MaritalStatus_cell2.setPadding(5);
            MaritalStatus_cell1.setBorder(Rectangle.NO_BORDER);
            MaritalStatus_cell2.setBorder(Rectangle.NO_BORDER);

            /* No of Child Table */

            PdfPTable PD_NoOfChild = new PdfPTable(2);
            PD_NoOfChild.setWidthPercentage(100);
            PdfPCell NoOfChild_cell1 = new PdfPCell(new Paragraph(
                    "No. of Children", small_bold1));
            PdfPCell NoOfChild_cell2 = new PdfPCell(new Paragraph(
                    str_no_of_child, small_normal1));
            // NoOfChild_cell1.setPadding(5);
            // NoOfChild_cell2.setPadding(5);
            NoOfChild_cell1.setBorder(Rectangle.NO_BORDER);
            NoOfChild_cell2.setBorder(Rectangle.NO_BORDER);

            personalDetails_headertable.addCell(MaritalStatus_cell1);
            personalDetails_headertable.addCell(NoOfChild_cell1);
            personalDetails_headertable.addCell(Nocell);

            personalDetails_headertable.addCell(MaritalStatus_cell2);
            personalDetails_headertable.addCell(NoOfChild_cell2);
            personalDetails_headertable.addCell(Nocell);
            // document.add(PD_NoOfChild);

            /* FINANCIAL DETAILS */
            Paragraph finacialDetails_header = new Paragraph();
            finacialDetails_header.add(new Paragraph("Financial Details",
                    sub_headerBold));

            PdfPTable finacialDetails_headertable = new PdfPTable(4);
            finacialDetails_headertable.setSpacingBefore(4f);
            finacialDetails_headertable.setWidthPercentage(100);
            PdfPCell finacialDetails_headertable_cell = new PdfPCell(
                    new Phrase(finacialDetails_header));
            // finacialDetails_headertable_cell
            // .setBackgroundColor(BaseColor.LIGHT_GRAY);
            finacialDetails_headertable_cell.setPadding(5);
            finacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            // finacialDetails_headertable
            // .addCell(finacialDetails_headertable_cell);
            finacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            // document.add(finacialDetails_headertable);
            finacialDetails_headertable_cell.setBorder(Rectangle.BOTTOM);
            finacialDetails_headertable_cell.setBorderColor(new BaseColor(230,
                    230, 230));
            finacialDetails_headertable_cell.setColspan(4);
            // document.add(personalDetails_headertable);
            finacialDetails_headertable
                    .addCell(finacialDetails_headertable_cell);
            /* Monthly Income Table */

            PdfPCell financial_image_cell = new PdfPCell(img_financial_detail,
                    true);
            financial_image_cell.setPadding(5);
            financial_image_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            financial_image_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            financial_image_cell.setRowspan(4);
            financial_image_cell.setBorder(Rectangle.NO_BORDER);
            finacialDetails_headertable.addCell(financial_image_cell);

            // PdfPTable PD_MonthlyIncome = new PdfPTable(2);
            // PD_MonthlyIncome.setWidthPercentage(100);
            PdfPCell MonthlyIncome_cell1 = new PdfPCell(new Paragraph(
                    "Monthly Income", small_bold1));
            PdfPCell MonthlyIncome_cell2 = new PdfPCell(new Paragraph("Rs. "
                    + getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .valueOf(strMonthlyIncome))))),
                    small_normal1));
            // MonthlyIncome_cell1.setPadding(8);
            MonthlyIncome_cell1.setBorder(Rectangle.NO_BORDER);
            MonthlyIncome_cell2.setBorder(Rectangle.NO_BORDER);
            // document.add(PD_MonthlyIncome);

            /* Monthly Expenses Table */

            // PdfPTable PD_MonthlyExpenses = new PdfPTable(2);
            // PD_MonthlyExpenses.setWidthPercentage(100);
            PdfPCell MonthlyExpenses_cell1 = new PdfPCell(new Paragraph(
                    "Monthly Expenses", small_bold1));
            PdfPCell MonthlyExpenses_cell2 = new PdfPCell(new Paragraph("Rs. "
                    + getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .valueOf(strMonthlyIncome_emi))))),
                    small_normal1));
            // MonthlyExpenses_cell1.setPadding(8);
            // MonthlyExpenses_cell2.setPadding(5);
            MonthlyExpenses_cell1.setBorder(Rectangle.NO_BORDER);
            MonthlyExpenses_cell2.setBorder(Rectangle.NO_BORDER);

            // document.add(PD_MonthlyExpenses);
            //
            // /* Outstanding home loan Table */

            // PdfPTable PD_outstandingHomeloan = new PdfPTable(2);
            // PD_outstandingHomeloan.setWidthPercentage(100);
            PdfPCell outstandingHomeloan_cell1 = new PdfPCell(new Paragraph(
                    "Outstanding Home Loan Amount", small_bold1));
            PdfPCell outstandingHomeloan_cell2 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer
                                    .parseInt(obj.getRound(obj.getStringWithout_E(Double
                                            .valueOf(strOutstandingHomeLoan))))),
                            small_normal1));
            // outstandingHomeloan_cell1.setPadding(8);
            // outstandingHomeloan_cell2.setPadding(5);
            outstandingHomeloan_cell1.setBorder(Rectangle.NO_BORDER);
            outstandingHomeloan_cell2.setBorder(Rectangle.NO_BORDER);

            // document.add(PD_outstandingHomeloan);

            /* Outstanding home loan Other Table */

            // PdfPTable PD_outstandingHomeloanOther = new PdfPTable(2);
            // PD_outstandingHomeloanOther.setWidthPercentage(100);
            PdfPCell outstandingHomeloanOther_cell1 = new PdfPCell(
                    new Paragraph("Outstanding Loan Amount (Others)",
                            small_bold1));
            PdfPCell outstandingHomeloanOther_cell2 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer
                                    .parseInt(obj.getRound(obj.getStringWithout_E(Double
                                            .valueOf(strOutstandingHomeLoanother))))),
                            small_normal1));
            // outstandingHomeloanOther_cell1.setPadding(8);
            // outstandingHomeloanOther_cell2.setPadding(5);
            outstandingHomeloanOther_cell1.setBorder(Rectangle.NO_BORDER);
            outstandingHomeloanOther_cell2.setBorder(Rectangle.NO_BORDER);

            // document.add(PD_outstandingHomeloanOther);

            /* Current Life Insurance Coverage Table */

            // PdfPTable PD_CurrentLifeInsuranceCoverage = new PdfPTable(2);
            // PD_CurrentLifeInsuranceCoverage.setWidthPercentage(100);
            PdfPCell CurrentLifeInsuranceCoverage_cell1 = new PdfPCell(
                    new Paragraph("Current Life Insurance Coverage",
                            small_bold1));
            PdfPCell CurrentLifeInsuranceCoverage_cell2 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer
                                    .parseInt(obj.getRound(obj.getStringWithout_E(Double
                                            .valueOf(strCurrentLifeInsuranceCoverage))))),
                            small_normal1));
            // CurrentLifeInsuranceCoverage_cell1.setPadding(8);
            // CurrentLifeInsuranceCoverage_cell2.setPadding(5);
            CurrentLifeInsuranceCoverage_cell1.setBorder(Rectangle.NO_BORDER);
            CurrentLifeInsuranceCoverage_cell2.setBorder(Rectangle.NO_BORDER);

            // document.add(PD_CurrentLifeInsuranceCoverage);

            /* Inflation Table */

            // PdfPTable PD_Inflation = new PdfPTable(2);
            // PD_Inflation.setWidthPercentage(100);
            PdfPCell Inflation_cell1 = new PdfPCell(new Paragraph(
                    "Expected Inflation Rates#", small_bold1));
            PdfPCell Inflation_cell2 = new PdfPCell(new Paragraph(
                    str_inflation_assumed, small_normal1));
            // Inflation_cell1.setPadding(8);
            // Inflation_cell2.setPadding(5);
            Inflation_cell1.setBorder(Rectangle.NO_BORDER);
            Inflation_cell2.setBorder(Rectangle.NO_BORDER);

            PdfPCell RiskAppetite_cell1 = new PdfPCell(new Paragraph(
                    "Risk Appetite", small_bold1));
            PdfPCell RiskAppetite_cell2 = new PdfPCell(new Paragraph(
                    str_investment_approach, small_normal1));
            // RiskAppetite_cell1.setPadding(8);
            // RiskAppetite_cell2.setPadding(5);
            RiskAppetite_cell1.setBorder(Rectangle.NO_BORDER);
            RiskAppetite_cell2.setBorder(Rectangle.NO_BORDER);

            finacialDetails_headertable.addCell(MonthlyIncome_cell1);
            finacialDetails_headertable.addCell(MonthlyExpenses_cell1);
            finacialDetails_headertable.addCell(outstandingHomeloan_cell1);

            finacialDetails_headertable.addCell(MonthlyIncome_cell2);
            finacialDetails_headertable.addCell(MonthlyExpenses_cell2);
            finacialDetails_headertable.addCell(outstandingHomeloan_cell2);

            finacialDetails_headertable.addCell(outstandingHomeloanOther_cell1);
            // finacialDetails_headertable
            // .addCell(CurrentLifeInsuranceCoverage_cell1);
            // finacialDetails_headertable
            // .addCell(CurrentLifeInsuranceCoverage_cell2);
            finacialDetails_headertable.addCell(Inflation_cell1);
            finacialDetails_headertable.addCell(RiskAppetite_cell1);

            finacialDetails_headertable.addCell(outstandingHomeloanOther_cell2);
            finacialDetails_headertable.addCell(Inflation_cell2);
            finacialDetails_headertable.addCell(RiskAppetite_cell2);
            // document.add(PD_RiskAppetite);

            PdfPTable personalFinancialDetails_headertable = new PdfPTable(2);
            personalFinancialDetails_headertable.setSpacingBefore(2f);
            personalFinancialDetails_headertable.setWidthPercentage(100);

            PdfPCell personalFinanacialDetails_headertable_cell = new PdfPCell(
                    new Phrase("Details as Provided"));
            personalFinanacialDetails_headertable_cell.setPadding(5);
            personalFinanacialDetails_headertable_cell.setColspan(2);
            personalFinanacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            personalFinancialDetails_headertable
                    .addCell(personalFinanacialDetails_headertable_cell);

            personalFinanacialDetails_headertable_cell = new PdfPCell(
                    personalDetails_headertable);
            personalFinanacialDetails_headertable_cell.setPadding(5);
            // personalFinanacialDetails_headertable_cell.setColspan(2);
            personalFinanacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            personalFinancialDetails_headertable
                    .addCell(personalFinanacialDetails_headertable_cell);

            personalFinanacialDetails_headertable_cell = new PdfPCell(
                    finacialDetails_headertable);
            personalFinanacialDetails_headertable_cell.setPadding(5);
            // personalFinanacialDetails_headertable_cell.setColspan(2);
            personalFinanacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            personalFinancialDetails_headertable
                    .addCell(personalFinanacialDetails_headertable_cell);

            document.add(personalFinancialDetails_headertable);

            StringBuilder buffer = new StringBuilder(
                    "We have made the analysis below of your current circumstances and your insurance and financial needs, based on the information recorded above.");

            if (!str_group.equalsIgnoreCase("Other")) {
                buffer.append("The products suggested for the various needs are also presented below.");
            }

            Paragraph para_input1 = new Paragraph(buffer.toString(),
                    big_normal_biFont);
            para_input.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para_input1);
            /* Current Corpus Saved for Retirement */

            //
            document.add(para_img_logo_after_space_1);

            Paragraph para_output = new Paragraph(
                    "The analysis based on the inputs is given below: ",
                    small_normal_biFont);
            para_output.setAlignment(Element.ALIGN_CENTER);
            // document.add(para_output);

            // document.add(para_img_logo_after_space_1);
            /* Protection Output */

            Paragraph output_protection_header = new Paragraph();
            output_protection_header.add(new Paragraph("Protection",
                    sub_headerBold));

            PdfPTable output_protection_headertable = new PdfPTable(1);
            output_protection_headertable.setSpacingBefore(4f);
            output_protection_headertable.setWidthPercentage(100);
            PdfPCell output_protection_headertable_cell = new PdfPCell(
                    new Phrase(output_protection_header));
            // output_protection_headertable_cell
            // .setBackgroundColor(BaseColor.LIGHT_GRAY);
            output_protection_headertable_cell.setPadding(5);
            output_protection_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            output_protection_headertable
                    .addCell(output_protection_headertable_cell);
            output_protection_headertable
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            // document.add(output_protection_headertable);

            PdfPTable protection_output_table = new PdfPTable(3);
            protection_output_table.setWidthPercentage(100);
            protection_output_table.setWidths(new float[]{9f, 18f, 18f});

            PdfPCell protection_cell = new PdfPCell(new Phrase(
                    output_protection_header));
            protection_cell.setPadding(5);
            protection_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            protection_cell.setColspan(4);
            protection_cell.setBorder(Rectangle.NO_BORDER);
            protection_output_table.addCell(protection_cell);

            PdfPCell protection_image_cell = new PdfPCell(
                    protection_input_img_detail, true);
            protection_image_cell.setPadding(5);
            protection_image_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            protection_image_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            protection_image_cell.setRowspan(4);
            protection_image_cell.setBorder(Rectangle.NO_BORDER);
            protection_output_table.addCell(protection_image_cell);

            PdfPCell protection_output_cell_1 = new PdfPCell(new Paragraph(
                    "Current Coverage", small_bold));
            protection_output_cell_1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            protection_output_cell_1.setBorder(Rectangle.NO_BORDER);
            PdfPCell protection_output_cell_2 = new PdfPCell(new Paragraph(
                    "Gap in Protection Coverage*", small_bold));
            protection_output_cell_2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            protection_output_cell_2.setBorder(Rectangle.NO_BORDER);
            PdfPCell protection_output_cell_3 = new PdfPCell(new Paragraph(
                    "Premium", small_bold));

            protection_output_cell_3.setBorder(Rectangle.NO_BORDER);
            protection_output_cell_3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell protection_output_cell_4 = new PdfPCell(new Paragraph(
                    "Protection Coverage till age", small_bold));
            protection_output_cell_4.setBorder(Rectangle.NO_BORDER);
            protection_output_cell_4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            protection_output_cell_1.setPadding(5);
            protection_output_cell_2.setPadding(5);
            protection_output_cell_3.setPadding(5);
            protection_output_cell_4.setPadding(5);

            protection_output_table.addCell(protection_output_cell_1);
            // protection_output_table.addCell(protection_output_cell_2);
            // protection_output_table.addCell(protection_output_cell_3);
            protection_output_table.addCell(protection_output_cell_4);
            // document.add(protection_output_table);

            PdfPCell protection_output_value_cell_1 = new PdfPCell(
                    new Paragraph("Rs. "
                            + getformatedThousandString(Integer.parseInt(obj
                            .getRound(obj.getStringWithout_E(Double
                                    .valueOf(LifeProtCovCorpus))))),
                            small_normal));
            protection_output_value_cell_1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            protection_output_value_cell_1.setBorder(Rectangle.NO_BORDER);
            PdfPCell protection_output_value_cell_2 = new PdfPCell(
                    new Paragraph("Rs. "
                            + getformatedThousandString(Integer.parseInt(obj
                            .getRound(obj.getStringWithout_E(Double
                                    .valueOf(LifeProtGap))))),
                            small_normal));

            protection_output_value_cell_2.setBorder(Rectangle.NO_BORDER);
            protection_output_value_cell_2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell protection_output_value_cell_3 = new PdfPCell(
                    new Paragraph("", small_normal));
            protection_output_value_cell_3.setBorder(Rectangle.NO_BORDER);
            protection_output_value_cell_3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell protection_output_value_cell_4 = new PdfPCell(
                    new Paragraph(LifeProtTillAge + " Years", small_normal));
            protection_output_value_cell_4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            protection_output_value_cell_4.setBorder(Rectangle.NO_BORDER);

            protection_output_value_cell_1.setPadding(5);
            protection_output_value_cell_2.setPadding(5);
            protection_output_value_cell_3.setPadding(5);
            protection_output_value_cell_4.setPadding(5);

            protection_output_table.addCell(protection_output_value_cell_1);
            // protection_output_table
            // .addCell(protection_output_value_cell_2);
            // protection_output_value_table
            // .addCell(protection_output_value_cell_3);
            protection_output_table.addCell(protection_output_value_cell_4);

            PdfPTable protection_output_value_table = new PdfPTable(2);
            protection_output_value_table.setWidthPercentage(100);
            protection_output_value_table.setWidths(new float[]{10f, 8f});

            cell = new PdfPCell(
                    new Paragraph(
                            "Total Protection coverage required\n"
                                    + "Rs. "
                                    + getformatedThousandString(Integer
                                    .parseInt(obj.getRound(obj.getStringWithout_E(Double
                                            .valueOf(str_protection_cor_req))))),
                            small_normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(5);
            protection_output_value_table.addCell(cell);

            if (!str_group.equalsIgnoreCase("Other")) {
                cell = new PdfPCell(
                        new Paragraph("Suggested Products", redFont));

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPadding(5);
                protection_output_value_table.addCell(cell);
            }

            PdfPCell graph_cell = new PdfPCell(protection_img_detail, true);
            graph_cell.setPadding(5);
            graph_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            graph_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            graph_cell.setRowspan(4);
            graph_cell.setBorder(Rectangle.NO_BORDER);

            protection_output_value_table.addCell(graph_cell);
            if (!str_group.equalsIgnoreCase("Other")) {
                for (int i = 0; i < 4; i++) {
                    if (protection_prod_list.size() > i) {
                        if (protection_prod_list.get(i).getProduct_name() != null)
                            cell = new PdfPCell(new Paragraph(
                                    protection_prod_list.get(i)
                                            .getProduct_name()
                                            + "\n("
                                            + protection_prod_list.get(i)
                                            .getProduct_UIN() + ")",
                                    small_bold));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(Rectangle.NO_BORDER);
                        protection_output_value_table.addCell(cell);
                    }
                }
            }
            // document.add(para_img_logo_after_space_1);
            /* Retirement Output */
            Paragraph output_retirement_header = new Paragraph();
            output_retirement_header.add(new Paragraph("Retirement @ "
                    + strRetirementNoOfRealise + " Yrs", sub_headerBold));

            PdfPTable output_retirement_headertable = new PdfPTable(3);
            output_retirement_headertable
                    .setWidths(new float[]{9f, 18f, 18f});
            output_retirement_headertable.setWidthPercentage(100);

            PdfPCell output_retirement_headertable_cell = new PdfPCell(
                    new Phrase(output_retirement_header));
            // output_retirement_headertable_cell
            // .setBackgroundColor(BaseColor.LIGHT_GRAY);
            output_retirement_headertable_cell.setBorder(Rectangle.NO_BORDER);
            output_retirement_headertable_cell.setPadding(5);
            output_retirement_headertable_cell.setColspan(3);
            output_retirement_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            output_retirement_headertable
                    .addCell(output_retirement_headertable_cell);
            // output_retirement_headertable
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // document.add(output_retirement_headertable);

            // PdfPTable retirement_output_table = new PdfPTable(4);
            // retirement_output_table.setWidthPercentage(100);

            PdfPCell retirement_image_cell = new PdfPCell(
                    retirement_input_img_detail, true);
            retirement_image_cell.setPadding(5);
            retirement_image_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            retirement_image_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            retirement_image_cell.setRowspan(4);
            retirement_image_cell.setBorder(Rectangle.NO_BORDER);
            output_retirement_headertable.addCell(retirement_image_cell);

            PdfPCell retirement_output_cell_1 = new PdfPCell(new Paragraph(
                    "Current Saving for Retirment", small_bold));
            retirement_output_cell_1.setBorder(Rectangle.NO_BORDER);
            retirement_output_cell_1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell retirement_output_cell_2 = new PdfPCell(new Paragraph(
                    "Gap*", small_bold));
            retirement_output_cell_2.setBorder(Rectangle.NO_BORDER);
            retirement_output_cell_2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell retirement_output_cell_3 = new PdfPCell(new Paragraph(
                    "Monthly Investment Required", small_bold));
            retirement_output_cell_3.setBorder(Rectangle.NO_BORDER);

            retirement_output_cell_3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell retirement_output_cell_4 = new PdfPCell(new Paragraph(
                    "Years left to Retire", small_bold));
            retirement_output_cell_4.setBorder(Rectangle.NO_BORDER);
            retirement_output_cell_4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            // retirement_output_cell_1.setPadding(5);
            // retirement_output_cell_2.setPadding(5);
            // retirement_output_cell_3.setPadding(5);
            // retirement_output_cell_4.setPadding(5);

            output_retirement_headertable.addCell(retirement_output_cell_1);
            // output_retirement_headertable.addCell(retirement_output_cell_2);
            output_retirement_headertable.addCell(retirement_output_cell_3);

            // document.add(retirement_output_table);

            // PdfPTable retirement_output_value_table = new PdfPTable(4);
            // retirement_output_value_table.setWidthPercentage(100);
            PdfPCell retirement_output_value_cell_1 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer
                                    .parseInt(obj.getRound(obj.getStringWithout_E(Double
                                            .valueOf(PensionTotReqCurCorpus))))),
                            small_normal));
            retirement_output_value_cell_1.setBorder(Rectangle.NO_BORDER);
            retirement_output_value_cell_1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell retirement_output_value_cell_2 = new PdfPCell(
                    new Paragraph("Rs. "
                            + getformatedThousandString(Integer.parseInt(obj
                            .getRound(obj.getStringWithout_E(Double
                                    .valueOf(PensionActAnnInvGap))))),
                            small_normal));
            retirement_output_value_cell_2.setBorder(Rectangle.NO_BORDER);
            retirement_output_value_cell_2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell retirement_output_value_cell_3 = new PdfPCell(
                    new Paragraph("Rs. " + Ret_Monthly_inv, small_normal));
            retirement_output_value_cell_3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            retirement_output_value_cell_3.setBorder(Rectangle.NO_BORDER);
            PdfPCell retirement_output_value_cell_4 = new PdfPCell(
                    new Paragraph(Ret_Left_Year + " Years", small_normal));
            retirement_output_value_cell_4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            retirement_output_value_cell_4.setBorder(Rectangle.NO_BORDER);

            // retirement_output_value_cell_1.setPadding(5);
            // retirement_output_value_cell_2.setPadding(5);
            // retirement_output_value_cell_3.setPadding(5);
            // retirement_output_value_cell_4.setPadding(5);

            output_retirement_headertable
                    .addCell(retirement_output_value_cell_1);
            // output_retirement_headertable
            // .addCell(retirement_output_value_cell_2);
            output_retirement_headertable
                    .addCell(retirement_output_value_cell_3);

            output_retirement_headertable.addCell(retirement_output_cell_4);

            output_retirement_headertable.addCell(Nocell);
            output_retirement_headertable
                    .addCell(retirement_output_value_cell_4);

            output_retirement_headertable.addCell(Nocell);

            // document.add(output_retirement_headertable);
            // document.add(para_img_logo_after_space_1);

            PdfPTable retirement_output_value_table = new PdfPTable(2);
            retirement_output_value_table.setWidthPercentage(100);

            retirement_output_value_table.setWidths(new float[]{10f, 8f});
            cell = new PdfPCell(new Paragraph("Total Corpus required "
                    + "Rs. "
                    + getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .valueOf(PensionActTarget_))))),
                    small_normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(5);
            retirement_output_value_table.addCell(cell);

            if (!str_group.equalsIgnoreCase("Other")) {
                cell = new PdfPCell(
                        new Paragraph("Suggested Products", redFont));

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPadding(5);
                retirement_output_value_table.addCell(cell);
            }

            graph_cell = new PdfPCell(retirement_img_detail, true);
            graph_cell.setPadding(5);
            graph_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            graph_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            graph_cell.setRowspan(4);
            graph_cell.setBorder(Rectangle.NO_BORDER);

            retirement_output_value_table.addCell(graph_cell);

            if (!str_group.equalsIgnoreCase("Other")) {
                for (int i = 0; i < 4; i++) {
                    if (retirement_prod_list.size() > i) {
                        if (retirement_prod_list.get(i).getProduct_name() != null)
                            cell = new PdfPCell(new Paragraph(
                                    retirement_prod_list.get(i)
                                            .getProduct_name()
                                            + "\n("
                                            + retirement_prod_list.get(i)
                                            .getProduct_UIN() + ")",
                                    small_bold));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(Rectangle.NO_BORDER);
                        retirement_output_value_table.addCell(cell);
                    }
                }
            }
            // document.add(protection_output_value_table);

            PdfPTable Child_headertable = new PdfPTable(8);
            Child_headertable.setWidths(new float[]{9f, 4f, 4f, 4f, 6f, 6f,
                    6f, 6f});

            PdfPCell child_image_cell = new PdfPCell(child_inputimg_detail,
                    true);
            child_image_cell.setPadding(5);
            child_image_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            child_image_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            child_image_cell.setRowspan(5);
            child_image_cell.setBorder(Rectangle.NO_BORDER);
            Child_headertable.addCell(child_image_cell);

            if (Integer.parseInt(str_no_of_child) > 0) {

                /* Child1_education Output */
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setRowspan(2);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Age at Which money is required", small_bold3));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Education", small_bold3));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Marriage", small_bold3));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Education", small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Marriage", small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Corpus Saved", small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Current Cost", small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Corpus Saved", small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Current Cost", small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(str_child1Name, small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild1AgeAtEducation,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild1AgeAtMarriage,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild1CorpusEducation,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild1CurrentCostEducation,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild1CorpusMarriage,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild1CurrentCostMarriage,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

            }

            if (str_no_of_child.equals("2") || str_no_of_child.equals("3")
                    || str_no_of_child.equals("4")) {
                /* Child2_ Output */
                cell = new PdfPCell(new Phrase(str_child2Name, small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild2AgeAtEducation,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild2AgeAtMarriage,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild2CorpusEducation,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild2CurrentCostEducation,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild2CorpusMarriage,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild2CurrentCostMarriage,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

            }

            if (str_no_of_child.equals("3") || str_no_of_child.equals("4")) {

                /* Child 3 Output */

                cell = new PdfPCell(new Phrase(str_child3Name, small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild3AgeAtEducation,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild3AgeAtMarriage,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild3CorpusEducation,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild3CurrentCostEducation,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild3CorpusMarriage,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild3CurrentCostMarriage,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

            }

            if (str_no_of_child.equals("4")) {
                /* Child 4 Output */
                cell = new PdfPCell(new Phrase(str_child4Name, small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild4AgeAtEducation,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild4AgeAtMarriage,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild4CorpusEducation,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild4CurrentCostEducation,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild4CorpusMarriage,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild4CurrentCostMarriage,
                        small_bold3));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

            }

            // document.add(para_img_logo_after_space_1);

            Paragraph child_output_header = new Paragraph();
            child_output_header.add(new Paragraph("Children", sub_headerBold));

            PdfPTable child_input_headertable = new PdfPTable(2);
            child_input_headertable.setSpacingBefore(4f);
            child_input_headertable.setWidthPercentage(100);
            cell = new PdfPCell(new Phrase(child_output_header));
            cell.setPadding(5);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            child_input_headertable.addCell(cell);

            cell = new PdfPCell(Child_headertable);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            cell.setBorder(Rectangle.NO_BORDER);
            child_input_headertable.addCell(cell);

            PdfPTable child_output_headertable = new PdfPTable(2);
            child_output_headertable.setSpacingBefore(4f);
            child_output_headertable.setWidthPercentage(100);
            child_output_headertable.setWidths(new float[]{9f, 8f});

            cell = new PdfPCell(new Paragraph("Child's Future Planning",
                    small_normal1));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(5);
            child_output_headertable.addCell(cell);
            if (!str_group.equalsIgnoreCase("Other")) {
                cell = new PdfPCell(
                        new Paragraph("Suggested Products", redFont));

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPadding(5);
                child_output_headertable.addCell(cell);
            }
            graph_cell = new PdfPCell(child_img_detail, true);
            graph_cell.setPadding(5);
            graph_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            graph_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            graph_cell.setRowspan(4);
            graph_cell.setBorder(Rectangle.NO_BORDER);
            child_output_headertable.addCell(graph_cell);

            if (!str_group.equalsIgnoreCase("Other")) {
                for (int i = 0; i < 4; i++) {
                    if (child_prod_list.size() > i) {
                        if (child_prod_list.get(i).getProduct_name() != null)
                            cell = new PdfPCell(new Paragraph(child_prod_list
                                    .get(i).getProduct_name()
                                    + "\n("
                                    + child_prod_list.get(i).getProduct_UIN()
                                    + ")", small_bold));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(Rectangle.NO_BORDER);
                        child_output_headertable.addCell(cell);
                    }
                }
            }
            /* Wealth Output */
            document.add(para_img_logo_after_space_1);

            Paragraph output_Wealth_outut_header = new Paragraph();
            output_Wealth_outut_header.add(new Paragraph(
                    "Insurance with Saving", sub_headerBold));

            PdfPTable output_Wealth_outut_headertable;

            if (!str_group.equalsIgnoreCase("Other")) {
                output_Wealth_outut_headertable = new PdfPTable(2);
                output_Wealth_outut_headertable.setSpacingBefore(4f);
                output_Wealth_outut_headertable.setWidthPercentage(100);
            } else {
                output_Wealth_outut_headertable = new PdfPTable(1);
                output_Wealth_outut_headertable.setSpacingBefore(4f);
                output_Wealth_outut_headertable.setWidthPercentage(50);
            }

            PdfPCell output_Wealth_outut_headertable_cell = new PdfPCell(
                    new Phrase(output_Wealth_outut_header));
            // output_Wealth_outut_headertable_cell
            // .setBackgroundColor(BaseColor.LIGHT_GRAY);
            output_Wealth_outut_headertable_cell.setPadding(5);
            output_Wealth_outut_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            output_Wealth_outut_headertable_cell.setBorder(Rectangle.NO_BORDER);
            output_Wealth_outut_headertable_cell.setColspan(2);
            output_Wealth_outut_headertable
                    .addCell(output_Wealth_outut_headertable_cell);
            // output_Wealth_outut_headertable
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // document.add(output_Wealth_outut_headertable);

            PdfPTable wealth_output_table = new PdfPTable(5);
            wealth_output_table.setWidthPercentage(100);

            PdfPCell wealth_image_cell = new PdfPCell(wealth_input_img_detail,
                    true);
            wealth_image_cell.setPadding(5);
            wealth_image_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            wealth_image_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            wealth_image_cell.setRowspan(3);
            wealth_image_cell.setBorder(Rectangle.NO_BORDER);
            wealth_output_table.addCell(wealth_image_cell);

            PdfPCell wealth_output_cell_1 = new PdfPCell(new Paragraph("",
                    small_bold));

            wealth_output_cell_1.setBorder(Rectangle.NO_BORDER);
            wealth_output_cell_1.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell wealth_output_cell_2 = new PdfPCell(new Paragraph(
                    "Corpus Savings", small_bold));
            wealth_output_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell wealth_output_cell_3 = new PdfPCell(new Paragraph("Gap*",
                    small_bold));

            wealth_output_cell_3.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell wealth_output_cell_4 = new PdfPCell(new Paragraph(
                    "Current Cost", small_bold));
            wealth_output_cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell wealth_output_cell_5 = new PdfPCell(new Paragraph(
                    "No. of Years to realise", small_bold));
            wealth_output_cell_5.setHorizontalAlignment(Element.ALIGN_CENTER);

            wealth_output_cell_1.setPadding(5);
            wealth_output_cell_2.setPadding(5);
            wealth_output_cell_3.setPadding(5);
            wealth_output_cell_4.setPadding(5);
            wealth_output_cell_5.setPadding(5);

            wealth_output_table.addCell(wealth_output_cell_1);
            wealth_output_table.addCell(wealth_output_cell_5);
            wealth_output_table.addCell(wealth_output_cell_2);
            // wealth_output_table.addCell(wealth_output_cell_3);
            wealth_output_table.addCell(wealth_output_cell_4);

            // PdfPTable wealth_output_value1_table = new PdfPTable(5);
            // wealth_output_value1_table.setWidthPercentage(100);
            PdfPCell wealth_output_value1_cell_1 = new PdfPCell(new Paragraph(
                    "Dream Home", small_bold));
            wealth_output_value1_cell_1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell wealth_output_value1_cell_2 = new PdfPCell(new Paragraph(
                    "Rs. "
                            + getformatedThousandString(Integer.parseInt(obj
                            .getRound(obj.getStringWithout_E(Double
                                    .valueOf(CostHomeCorpus))))),
                    small_normal));
            wealth_output_value1_cell_2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell wealth_output_value1_cell_3 = new PdfPCell(new Paragraph(
                    "Rs. "
                            + getformatedThousandString(Integer.parseInt(obj
                            .getRound(obj.getStringWithout_E(Double
                                    .valueOf(CostHomeGap))))),
                    small_bold));

            wealth_output_value1_cell_3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell wealth_output_value1_cell_4 = new PdfPCell(new Paragraph(
                    "Rs. " + Home_Monthly_inv, small_normal));
            wealth_output_value1_cell_4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell wealth_output_value1_cell_5 = new PdfPCell(new Paragraph(
                    getformatedThousandString(Integer.parseInt(obj.getRound(obj
                            .getStringWithout_E(Double
                                    .valueOf(strNoOfYearBuyHome)))))
                            + " Years", small_normal));
            wealth_output_value1_cell_5
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            wealth_output_value1_cell_1.setPadding(5);
            wealth_output_value1_cell_2.setPadding(5);
            wealth_output_value1_cell_3.setPadding(5);
            wealth_output_value1_cell_4.setPadding(5);
            wealth_output_value1_cell_5.setPadding(5);

            wealth_output_table.addCell(wealth_output_value1_cell_1);
            wealth_output_table.addCell(wealth_output_value1_cell_5);
            wealth_output_table.addCell(wealth_output_value1_cell_2);
            // wealth_output_table.addCell(wealth_output_value1_cell_3);
            wealth_output_table.addCell(wealth_output_value1_cell_4);
            // document.add(wealth_output_value1_table);

            // PdfPTable wealth_output_value2_table = new PdfPTable(5);
            // wealth_output_value2_table.setWidthPercentage(100);
            PdfPCell wealth_output_value2_cell_1 = new PdfPCell(new Paragraph(
                    "Other Goals", small_bold));
            wealth_output_value2_cell_1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell wealth_output_value2_cell_2 = new PdfPCell(new Paragraph(
                    "Rs. "
                            + getformatedThousandString(Integer.parseInt(obj
                            .getRound(obj.getStringWithout_E(Double
                                    .valueOf(CostOtherCorpus))))),
                    small_normal));
            wealth_output_value2_cell_2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell wealth_output_value2_cell_3 = new PdfPCell(new Paragraph(
                    "Rs. "
                            + getformatedThousandString(Integer.parseInt(obj
                            .getRound(obj.getStringWithout_E(Double
                                    .valueOf(CostOtherGap))))),
                    small_bold));
            wealth_output_value2_cell_3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell wealth_output_value2_cell_4 = new PdfPCell(new Paragraph(
                    "Rs. " + Other_Monthly_inv, small_normal));
            wealth_output_value2_cell_4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell wealth_output_value2_cell_5 = new PdfPCell(new Paragraph(
                    getformatedThousandString(Integer.parseInt(obj.getRound(obj
                            .getStringWithout_E(Double
                                    .valueOf(strNoOfYearOthergoal)))))
                            + " Years", small_normal));
            wealth_output_value2_cell_5
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            wealth_output_value2_cell_1.setPadding(5);
            wealth_output_value2_cell_2.setPadding(5);
            wealth_output_value2_cell_3.setPadding(5);
            wealth_output_value2_cell_4.setPadding(5);
            wealth_output_value2_cell_5.setPadding(5);

            wealth_output_table.addCell(wealth_output_value2_cell_1);
            wealth_output_table.addCell(wealth_output_value2_cell_5);
            wealth_output_table.addCell(wealth_output_value2_cell_2);
            // wealth_output_table.addCell(wealth_output_value2_cell_3);
            wealth_output_table.addCell(wealth_output_value2_cell_4);
            // document.add(wealth_output_value2_table);
            // document.add(para_img_logo_after_space_1);

            // PdfPTable wealth_output_table_table = new PdfPTable(5);
            // wealth_output_table_table.setWidthPercentage(100);
            // wealth_output_table_table.setWidths(new float[]{2f,8f});

            PdfPCell wealth_output_cell = new PdfPCell(wealth_output_table);
            wealth_output_cell.setPadding(5);
            wealth_output_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            wealth_output_cell.setColspan(2);
            wealth_output_cell.setBorder(Rectangle.NO_BORDER);
            output_Wealth_outut_headertable.addCell(wealth_output_cell);

            PdfPTable wealth_output_value_table = new PdfPTable(2);

            wealth_output_value_table.setWidthPercentage(100);
            wealth_output_value_table.setWidths(new float[]{10f, 8f});
            cell = new PdfPCell(new Paragraph("Insurance with Savings",
                    small_bold));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(5);
            wealth_output_value_table.addCell(cell);
            if (!str_group.equalsIgnoreCase("Other")) {
                cell = new PdfPCell(
                        new Paragraph("Suggested Products", redFont));

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPadding(5);
                wealth_output_value_table.addCell(cell);
            }
            graph_cell = new PdfPCell(wealth_img_detail, true);
            graph_cell.setPadding(5);
            graph_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            graph_cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            graph_cell.setRowspan(4);
            graph_cell.setBorder(Rectangle.NO_BORDER);
            wealth_output_value_table.addCell(graph_cell);

            if (!str_group.equalsIgnoreCase("Other")) {
                for (int i = 0; i < 4; i++) {
                    if (wealth_prod_list.size() > i) {
                        if (wealth_prod_list.get(i).getProduct_name() != null)
                            cell = new PdfPCell(new Paragraph(wealth_prod_list
                                    .get(i).getProduct_name()
                                    + "\n("
                                    + wealth_prod_list.get(i).getProduct_UIN()
                                    + ")", small_bold));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(Rectangle.NO_BORDER);
                        wealth_output_value_table.addCell(cell);
                    }
                }
            }

            PdfPTable needAnalysisDetails_headertable = new PdfPTable(2);
            needAnalysisDetails_headertable.setSpacingBefore(2f);
            needAnalysisDetails_headertable.setWidthPercentage(100);

            PdfPCell protectionDetails_headertable_cell = new PdfPCell(
                    new Phrase(new Paragraph("Need Analysis Input Summary ",
                            sub_headerBold)));
            protectionDetails_headertable_cell.setPadding(5);
            protectionDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            needAnalysisDetails_headertable
                    .addCell(protectionDetails_headertable_cell);

            PdfPCell protectionDetails_headertable_cell_1 = new PdfPCell(
                    new Phrase(new Paragraph("Need Analysis Output Summary ",
                            sub_headerBold)));
            protectionDetails_headertable_cell_1.setPadding(5);
            protectionDetails_headertable_cell_1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            needAnalysisDetails_headertable
                    .addCell(protectionDetails_headertable_cell_1);

            personalFinanacialDetails_headertable_cell = new PdfPCell(
                    protection_output_table);
            personalFinanacialDetails_headertable_cell.setPadding(5);
            // personalFinanacialDetails_headertable_cell.setColspan(2);
            personalFinanacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            needAnalysisDetails_headertable
                    .addCell(personalFinanacialDetails_headertable_cell);

            personalFinanacialDetails_headertable_cell = new PdfPCell(
                    protection_output_value_table);
            personalFinanacialDetails_headertable_cell.setPadding(5);
            // personalFinanacialDetails_headertable_cell.setColspan(2);
            personalFinanacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            needAnalysisDetails_headertable
                    .addCell(personalFinanacialDetails_headertable_cell);

            personalFinanacialDetails_headertable_cell = new PdfPCell(
                    output_retirement_headertable);
            personalFinanacialDetails_headertable_cell.setPadding(5);
            // personalFinanacialDetails_headertable_cell.setColspan(2);
            personalFinanacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            needAnalysisDetails_headertable
                    .addCell(personalFinanacialDetails_headertable_cell);

            personalFinanacialDetails_headertable_cell = new PdfPCell(
                    retirement_output_value_table);
            personalFinanacialDetails_headertable_cell.setPadding(5);
            // personalFinanacialDetails_headertable_cell.setColspan(2);
            personalFinanacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            needAnalysisDetails_headertable
                    .addCell(personalFinanacialDetails_headertable_cell);

            personalFinanacialDetails_headertable_cell = new PdfPCell(
                    output_Wealth_outut_headertable);
            personalFinanacialDetails_headertable_cell.setPadding(5);
            personalFinanacialDetails_headertable_cell.setSpaceCharRatio(5f);
            // personalFinanacialDetails_headertable_cell.setColspan(2);
            personalFinanacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            needAnalysisDetails_headertable
                    .addCell(personalFinanacialDetails_headertable_cell);

            personalFinanacialDetails_headertable_cell = new PdfPCell(
                    wealth_output_value_table);
            personalFinanacialDetails_headertable_cell.setPadding(5);
            personalFinanacialDetails_headertable_cell.setSpaceCharRatio(5f);
            // personalFinanacialDetails_headertable_cell.setColspan(2);
            personalFinanacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            needAnalysisDetails_headertable
                    .addCell(personalFinanacialDetails_headertable_cell);

            if (Integer.parseInt(str_no_of_child) > 0) {
                personalFinanacialDetails_headertable_cell = new PdfPCell(
                        child_input_headertable);
                personalFinanacialDetails_headertable_cell.setPadding(5);
                personalFinanacialDetails_headertable_cell
                        .setSpaceCharRatio(5f);
                // personalFinanacialDetails_headertable_cell.setColspan(2);
                personalFinanacialDetails_headertable_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                needAnalysisDetails_headertable
                        .addCell(personalFinanacialDetails_headertable_cell);

                personalFinanacialDetails_headertable_cell = new PdfPCell(
                        child_output_headertable);
                personalFinanacialDetails_headertable_cell.setPadding(5);
                personalFinanacialDetails_headertable_cell
                        .setSpaceCharRatio(5f);
                // personalFinanacialDetails_headertable_cell.setColspan(2);
                personalFinanacialDetails_headertable_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                needAnalysisDetails_headertable
                        .addCell(personalFinanacialDetails_headertable_cell);
            }
            document.add(needAnalysisDetails_headertable);

            String para3 = "# Inflation Rates are assumed & subjective in nature";

            Paragraph para3_gapBody3 = new Paragraph(para3, small_normal_biFont);
            para3_gapBody3.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para3_gapBody3);
            document.add(para_img_logo_after_space_1);

            String para4 = "*The Monthly Investment Required is the absolute amount required and does not include the Mortality Charge, Applicable Tax & Other charges.";

            Paragraph para4_gapBody4 = new Paragraph(para4, small_normal_biFont);
            para4_gapBody4.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para4_gapBody4);
            document.add(para_img_logo_after_space_1);

            if (!str_group.equalsIgnoreCase("Other")) {
                String gapBody4 = "We request you to review the above analysis and set your priorities of the needs that you would like to address now or in the near future and take a considered decision on the suggested insurance products that you would wish to buy from us. As per your"
                        + " analysis of your needs, you may also review the other insurance products offered by SBI Life.";

                Paragraph para_gapBody4 = new Paragraph(gapBody4,
                        big_normal_biFont);
                para_gapBody4.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(para_gapBody4);
                document.add(para_img_logo_after_space_1);

                String gapBody5 = "Note: This is an illustrative projection of your future insurance and financial needs. All figures are calculated as per SBI Life's need analysis"
                        + " calculator and are based on the information provided by you, estimated cost of living and assumed inflation rate.";

                Paragraph para_gapBody5 = new Paragraph(gapBody5, new Font(
                        Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL));
                para_gapBody5.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(para_gapBody5);
                document.add(para_img_logo_after_space_1);

                String gapBody = "I have gone through the financial analysis carefully and have chosen the following products from those recommended to me, based on my financial circumstances and priorities. The products have been explained to me in detail.";

                Paragraph para_gapBody = new Paragraph(gapBody,
                        big_normal_biFont);
                para_gapBody.setAlignment(Element.ALIGN_JUSTIFIED);

                PdfPTable suggestedProductTable = new PdfPTable(1);
                suggestedProductTable.setWidthPercentage(100);
                suggestedProductTable
                        .setHorizontalAlignment(Element.ALIGN_LEFT);
                // suggestedProductTable.setWidths(new float[] { 15f });

                for (int i = 0; i < sugg_chosen_prod_list.size(); i++) {
                    cell = new PdfPCell(new Phrase((i + 1)
                            + ". SBI Life - "
                            + sugg_chosen_prod_list.get(i).getProduct_name()
                            + "  ( "
                            + sugg_chosen_prod_list.get(i).getProduct_UIN()
                            + " )"
                            + "  - "
                            + getId(map, sugg_chosen_prod_list.get(i)
                            .getProduct_name()),
                            medium_normal_underline_biFont));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setBorder(Rectangle.NO_BORDER);
                    suggestedProductTable.addCell(cell);

                    if (sugg_chosen_prod_list.get(i).getProduct_name()
                            .equals("Smart Wealth Builder"))
                        smart_wealth_builder_flag = 1;
                }

                if (sugg_chosen_prod_list.size() > 0) {
                    document.add(para_gapBody);
                    document.add(para_img_logo_after_space_1);
                    document.add(suggestedProductTable);
                }

                String gapBody1 = "";

                if (sugg_chosen_prod_list.size() > 0
                        && other_chosen_prod_list.size() > 0)
                    gapBody1 = "I would also like to purchase the following product/s which has not been recommended to me. I have gone through the product literature and fully understand the product/s.";
                else if (sugg_chosen_prod_list.size() == 0
                        && other_chosen_prod_list.size() > 0)
                    gapBody1 = "I would like to purchase the following product/s which has not been recommended to me. I have gone through the product literature and fully understand the product/s.";
                else if (sugg_chosen_prod_list.size() > 0
                        && other_chosen_prod_list.size() == 0)
                    gapBody1 = "";

                Paragraph para_gapBody1 = new Paragraph(gapBody1,
                        big_normal_biFont);
                para_gapBody1.setAlignment(Element.ALIGN_JUSTIFIED);

                document.add(para_img_logo_after_space_1);

                PdfPTable otherchosenProductTable = new PdfPTable(1);
                otherchosenProductTable.setWidthPercentage(100);
                otherchosenProductTable
                        .setHorizontalAlignment(Element.ALIGN_LEFT);
                // otherchosenProductTable.setWidths(new float[] { 15f });

                // Paragraph suggested_product_Body;
                for (int i = 0; i < other_chosen_prod_list.size(); i++) {
                    cell = new PdfPCell(new Phrase((i + 1)
                            + ".   SBI Life - "
                            + other_chosen_prod_list.get(i).getProduct_name()
                            + "  ( "
                            + other_chosen_prod_list.get(i).getProduct_UIN()
                            + " )"

                            /*+ "  - "
                            + getId(map, other_chosen_prod_list.get(i)
                            .getProduct_name())*/

                            , medium_normal_underline_biFont));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setBorder(Rectangle.NO_BORDER);
                    otherchosenProductTable.addCell(cell);

                    //
                    //
                    // if(suggested_prod_list.get(i).getProduct_name().equals("Smart Wealth Builder"))
                    // smart_wealth_builder_flag=1;
                    //
                }

                if (other_chosen_prod_list.size() > 0) {
                    document.add(para_gapBody1);
                    document.add(para_img_logo_after_space_1);
                    // Paragraph para_gapBody2 = new
                    // small_bold);
                    // para_gapBody1.setAlignment(Element.ALIGN_LEFT);
                    document.add(otherchosenProductTable);
                    document.add(para_img_logo_after_space_1);
                }
                String gapBody2 = "I have voluntarily chosen products based on my insurance needs and financial objectives.";
                Paragraph para_gapBody2 = new Paragraph(gapBody2,
                        big_normal_biFont);
                para_gapBody2.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(para_gapBody2);
                document.add(para_img_logo_after_space_1);

                String swb_line_gapBody2 = "*We suggest you to opt for Bond Fund under Smart Wealth Builder(UIN:111L095V03)";

                Paragraph swb_linepara_gapBody1 = new Paragraph(
                        swb_line_gapBody2, big_normal_biFont);
                swb_linepara_gapBody1.setAlignment(Element.ALIGN_JUSTIFIED);

                if (Integer.parseInt(str_age) > 45
                        && smart_wealth_builder_flag == 1) {
                    document.add(swb_linepara_gapBody1);
                    document.add(para_img_logo_after_space_1);
                }

                String gapBody3 = "I further confirm that I have not been compelled to purchase any of the above insurance products by the sales intermediary for availing any other financial product or facility that is offered by the bank.";
                Paragraph para_gapBody3 = new Paragraph(gapBody3,
                        big_normal_biFont);
                para_gapBody3.setAlignment(Element.ALIGN_JUSTIFIED);
                if (GetUserType().equals("CIF"))
                    document.add(para_gapBody3);

                document.add(para_img_logo_after_space_1);

                byte[] fbyt_Proposer = Base64.decode(proposer_sign, 0);
                Bitmap Proposerbitmap = BitmapFactory.decodeByteArray(
                        fbyt_Proposer, 0, fbyt_Proposer.length);

                // PdfPCell BI_PdftablePolicyHolder_signature_3 = new
                // PdfPCell();
                // BI_PdftablePolicyHolder_signature_3.setFixedHeight(60f);
                ByteArrayOutputStream PolicyHolder_signature_stream = new ByteArrayOutputStream();

                (Proposerbitmap).compress(Bitmap.CompressFormat.PNG, 50,
                        PolicyHolder_signature_stream);
                Image PolicyHolder_signature = Image
                        .getInstance(PolicyHolder_signature_stream
                                .toByteArray());

                PolicyHolder_signature.scaleToFit(90, 90);

                document.add(para_img_logo_after_space_1);
                PdfPTable BI_PdftableMarketing = new PdfPTable(3);
                BI_PdftableMarketing.setWidths(new float[]{5f, 5f, 5f});
                BI_PdftableMarketing.setWidthPercentage(100);

                byte[] fbyt_agent = Base64.decode(agent_sign, 0);
                Bitmap Agentbitmap = BitmapFactory.decodeByteArray(fbyt_agent,
                        0, fbyt_agent.length);

                // PdfPCell BI_PdftableMarketing_signature_3 = new PdfPCell();
                // BI_PdftableMarketing_signature_3.setFixedHeight(60f);
                ByteArrayOutputStream Marketing_officials_signature_stream = new ByteArrayOutputStream();

                (Agentbitmap).compress(Bitmap.CompressFormat.PNG, 50,
                        Marketing_officials_signature_stream);
                Image Marketing_officials_signature = Image
                        .getInstance(Marketing_officials_signature_stream
                                .toByteArray());

                Marketing_officials_signature.scaleToFit(90, 90);

                // BI_PdftableMarketing_signature_3
                // .setImage(Marketing_officials_signature);
                // BI_PdftableMarketing_signature_1.setPadding(5);
                // BI_PdftableMarketing_signature_2.setPadding(5);

                // BI_PdftableMarketing_signature
                // .addCell(BI_PdftableMarketing_signature_1);

                BI_PdftableMarketing.addCell(Nocell);

                BI_PdftableMarketing.addCell(Nocell);

                cell = new PdfPCell(new Paragraph(GetUserName() + " / "
                        + GetUserCode(), small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(10);
                cell.setBorder(Rectangle.NO_BORDER);
                BI_PdftableMarketing.addCell(cell);

                PdfPCell sign_cell = new PdfPCell(PolicyHolder_signature);
                sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sign_cell.setPadding(5);
                sign_cell.setBorder(Rectangle.NO_BORDER);
                BI_PdftableMarketing.addCell(sign_cell);

                BI_PdftableMarketing.addCell(Nocell);

                sign_cell = new PdfPCell(Marketing_officials_signature);
                sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sign_cell.setPadding(5);
                sign_cell.setBorder(Rectangle.NO_BORDER);
                BI_PdftableMarketing.addCell(sign_cell);

                sign_cell = new PdfPCell(new Paragraph(
                        "Customer Name/Signature", small_bold));
                sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sign_cell.setPadding(5);
                sign_cell.setBorder(Rectangle.TOP);
                BI_PdftableMarketing.addCell(sign_cell);

                BI_PdftableMarketing.addCell(Nocell);

                sign_cell = new PdfPCell(new Paragraph(
                        "Intermediary's Name/Code/Signature", small_bold));
                sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sign_cell.setBorder(Rectangle.TOP);
                sign_cell.setPadding(5);

                BI_PdftableMarketing.addCell(sign_cell);

                document.add(BI_PdftableMarketing);
                document.add(para_img_logo_after_space_1);

            } else {
            }

            PdfPTable BI_PdftableDate = new PdfPTable(1);
            BI_PdftableDate.setWidthPercentage(100);

            PdfPCell BI_Pdftable_Date_cell = new PdfPCell(new Paragraph(
                    "Date  :" + getCurrentDate(), small_normal));
            BI_Pdftable_Date_cell.setPadding(5f);
            BI_Pdftable_Date_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableDate.addCell(BI_Pdftable_Date_cell);
            document.add(BI_PdftableDate);
            document.add(para_img_logo_after_space_1);

            String para_textGap_italic = "Purchase of insurance product is voluntary. For more details on the products, risk factors, terms and conditions please read sales brochure carefully before concluding a sale. Trade logo displayed above belongs to State Bank of India and is used by SBI Life under license."
                    + "Registered and Corporate Office: SBI Life Insurance Company Limited, Natraj, M.V.Road & Western Express Highway Junction, Andheri(East), Mumbai-400069. IRDAI Registration. No. 111. CIN: L99999MH2000PLC129113\\ Website: www.sbilife.co.in\\ Email id: info@sbilife.co.in \\ Toll free no - 1800-267-9090(Between 9:00 am to 9.00 pm)";

            String textGapBody = "For more details on the products, risk factors, terms and conditions please read sales brochure carefully before concluding a sale. Trade logo displayed above belongs to State Bank of India and is used by SBI Life under license."
                    + "Registered and Corporate Office: SBI Life Insurance Company Limited, Natraj, M.V.Road & Western Express Highway Junction, Andheri(East), Mumbai-400069. IRDAI Registration. No. 111. CIN: L99999MH2000PLC129113\\ Website: www.sbilife.co.in\\ Email id: info@sbilife.co.in \\ Toll free no - 1800-267-9090(Between 9:00 am to 9.00 pm)";

            Paragraph para_textGap;
            if (GetUserType().equals("CIF"))
                para_textGap = new Paragraph(para_textGap_italic, small_normal);
            else
                para_textGap = new Paragraph(textGapBody, small_normal);
            para_textGap.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para_textGap);
            // document.add(para_img_logo_after_space_1);

            document.close();

        } catch (Exception e) {
            Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
        }

    }

    private void CreatePDF(String FileName) {

        CommonForAllProd obj = new CommonForAllProd();

        String str_child1Name = btn_child1.getText().toString();
        String str_child2Name = btn_child2.getText().toString();
        String str_child3Name = btn_child3.getText().toString();
        String str_child4Name = btn_child4.getText().toString();
        int smart_wealth_builder_flag = 0;
        // String QuatationNumber = ProductHomePageActivity.quotation_Number;
        try {
            /*
             * Font small_bold3 = new Font(Font.FontFamily.TIMES_ROMAN, 5,
             * Font.BOLD);
             */
            Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD,
                    BaseColor.RED);
            /*
             * Font sub_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
             * Font.BOLD);
             */

            Font white_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD, BaseColor.WHITE);
            /*
             * Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
             * Font.BOLD);
             */
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            /*
             * Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4,
             * Font.BOLD);
             */
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font small_normal1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            /*
             * Font small_italic = new Font(Font.FontFamily.TIMES_ROMAN, 8,
             * Font.ITALIC);
             */
            Font small_normal_biFont = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font medium_normal_biFont = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.NORMAL);
            Font medium_normal_underline_biFont = new Font(
                    Font.FontFamily.TIMES_ROMAN, 10, Font.UNDERLINE);
            Font big_normal_biFont = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD);

            // File mypath = new File(folder, PropserNumber +
            // "Proposalno_p02.pdf");
            File mypath = mStorageUtils.createFileToAppSpecificDir(context, FileName + ".pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 40, 30, 30, 30);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            @SuppressWarnings("unused")
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));
            document.open();
            PdfPCell cell;
            // For SBI- Life Logo starts
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext()
                    .getResources(), R.drawable.sbi_life_logo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            Image img_sbi_logo = Image.getInstance(stream.toByteArray());
            img_sbi_logo.setAlignment(Image.LEFT);
            img_sbi_logo.getSpacingAfter();
            img_sbi_logo.scaleToFit(80, 50);

            Paragraph para_img_logo = new Paragraph("");
            para_img_logo.add(img_sbi_logo);

            Paragraph para_img_logo_after_space_1 = new Paragraph(" ");

            PdfPTable URN_headertable = new PdfPTable(1);
            URN_headertable.setWidthPercentage(100);

            cell = new PdfPCell(img_sbi_logo);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            URN_headertable.addCell(cell);

            if (URN_NO != null) {
                if (URN_NO.equals("Fail") || URN_NO.equals("")
                        || (!FileName.equals("NA")))
                    cell = new PdfPCell(new Phrase(""));
                else
                    cell = new PdfPCell(new Phrase(new Paragraph(" URN : "
                            + URN_NO, small_bold)));
            } else
                cell = new PdfPCell(new Phrase(new Paragraph("", small_bold)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            URN_headertable.addCell(cell);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            document.add(URN_headertable);
            // For SBI- Life Logo ends

            // To draw line after the sbi logo image
            document.add(new LineSeparator());
            document.add(para_img_logo_after_space_1);

            // For the BI Smart Elite Table Header(Grey One)
            Paragraph Para_Header = new Paragraph();

            Para_Header.add(new Paragraph("Need Analysis Summary",
                    white_headerBold));


            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);
            PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
            c1.setBackgroundColor(BaseColor.DARK_GRAY);
            c1.setPadding(5);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headertable.addCell(c1);
            headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Co. Ltd \nCorporate Office: 'Natraj', M.V. Road and Western Express, Highway Junction, Andheri(East),Mumbai 400069. IRDAI Regn No. 111",
                    small_bold);
            para_address.setAlignment(Element.ALIGN_CENTER);

            Paragraph para_address1 = new Paragraph(
                    "Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113. Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
                    small_bold);

            para_address1.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_address1);
            document.add(para_img_logo_after_space_1);
            document.add(headertable);
            document.add(para_img_logo_after_space_1);

            if (str_gender.equalsIgnoreCase("M")) {
                Salutation = "Sir";
            } else {
                Salutation = "Madam";
            }


            Paragraph para_input;
            // if(str_gender.equalsIgnoreCase("F"))
            para_input = new Paragraph(
                    "Dear "
                            + Salutation
                            + ","
                            + "\nWe thank you for providing your personal and financial information, such as : age, income, assets, liabilities, risk profile, future financial goals etc.\n",
                    medium_normal_biFont);

            para_input.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para_input);
            document.add(para_img_logo_after_space_1);

            Paragraph personalDetails_header = new Paragraph();
            personalDetails_header.add(new Paragraph("Personal Details",
                    redFont));

            PdfPTable personalDetails_headertable = new PdfPTable(3);
            personalDetails_headertable.setSpacingBefore(4f);
            personalDetails_headertable.setWidthPercentage(100);

            PdfPCell Nocell = new PdfPCell(new Paragraph("", small_bold));
            Nocell.setBorder(Rectangle.NO_BORDER);

            /* Gender Table */

            if (str_gender.equalsIgnoreCase("M")) {
                Gender = "Male";
            } else {
                Gender = "Female";
            }
            // PdfPTable PD_Gender = new PdfPTable(2);
            // PD_Gender.setWidthPercentage(100);
            PdfPCell Gender_cell1 = new PdfPCell(new Paragraph("Gender",
                    small_bold1));

            // Gender_cell1.setBorder(Rectangle.LEFT);
            PdfPCell Gender_cell2 = new PdfPCell(new Paragraph(Gender,
                    small_normal1));
            // Gender_cell2.setBorder(Rectangle.LEFT);
            // Gender_cell1.setPadding(5);
            // Gender_cell2.setPadding(5);
            Gender_cell1.setBorder(Rectangle.NO_BORDER);
            Gender_cell2.setBorder(Rectangle.NO_BORDER);

            PdfPCell DOB_cell1 = new PdfPCell(new Paragraph("DOB", small_bold1));
            PdfPCell DOB_cell2 = new PdfPCell(new Paragraph(
                    getDate(str_date_of_birth), small_normal1));
            // DOB_cell1.setPadding(5);
            // DOB_cell2.setPadding(5);
            // DOB_cell1.setBorder(Rectangle.LEFT);
            // DOB_cell2.setBorder(Rectangle.LEFT);
            DOB_cell1.setBorder(Rectangle.NO_BORDER);
            DOB_cell2.setBorder(Rectangle.NO_BORDER);
            PdfPCell Age_cell1 = new PdfPCell(new Paragraph("Age", small_bold1));
            PdfPCell Age_cell2 = new PdfPCell(new Paragraph(str_age,
                    small_normal1));
            // Age_cell1.setPadding(5);
            // Age_cell2.setPadding(5);
            Age_cell1.setBorder(Rectangle.NO_BORDER);
            Age_cell2.setBorder(Rectangle.NO_BORDER);

            personalDetails_headertable.addCell(Gender_cell1);
            personalDetails_headertable.addCell(DOB_cell1);
            personalDetails_headertable.addCell(Age_cell1);

            personalDetails_headertable.addCell(Gender_cell2);
            personalDetails_headertable.addCell(DOB_cell2);
            personalDetails_headertable.addCell(Age_cell2);

            PdfPCell personal_empty_cell = new PdfPCell(new Phrase(" ",
                    small_normal));
            personal_empty_cell.setColspan(3);
            personal_empty_cell.setBorder(Rectangle.NO_BORDER);
            personalDetails_headertable.addCell(personal_empty_cell);

            PdfPCell MaritalStatus_cell1 = new PdfPCell(new Paragraph(
                    "Martial Status", small_bold1));
            PdfPCell MaritalStatus_cell2 = new PdfPCell(new Paragraph(
                    str_marital_status, small_normal1));
            // MaritalStatus_cell1.setPadding(5);
            // MaritalStatus_cell2.setPadding(5);
            MaritalStatus_cell1.setBorder(Rectangle.NO_BORDER);
            MaritalStatus_cell2.setBorder(Rectangle.NO_BORDER);

            // MaritalStatus_cell1.setBorder(Rectangle.LEFT);
            // MaritalStatus_cell2.setBorder(Rectangle.LEFT|Rectangle.BOTTOM);
            /* No of Child Table */

            PdfPTable PD_NoOfChild = new PdfPTable(2);
            PD_NoOfChild.setWidthPercentage(100);
            PdfPCell NoOfChild_cell1 = new PdfPCell(new Paragraph(
                    "No. of minor Children", small_bold1));
            PdfPCell NoOfChild_cell2 = new PdfPCell(new Paragraph(
                    str_no_of_child, small_normal1));
            // NoOfChild_cell1.setPadding(5);
            // NoOfChild_cell2.setPadding(5);
            NoOfChild_cell1.setBorder(Rectangle.NO_BORDER);
            NoOfChild_cell2.setBorder(Rectangle.NO_BORDER);

            personalDetails_headertable.addCell(MaritalStatus_cell1);
            personalDetails_headertable.addCell(NoOfChild_cell1);
            personalDetails_headertable.addCell(Nocell);

            personalDetails_headertable.addCell(MaritalStatus_cell2);
            personalDetails_headertable.addCell(NoOfChild_cell2);
            personalDetails_headertable.addCell(Nocell);
            // document.add(PD_NoOfChild);

            // personalDetails_headertable
            PdfPTable personalDetailsMain_headertable = new PdfPTable(1);
            personalDetailsMain_headertable.setSpacingBefore(4f);
            personalDetailsMain_headertable.setWidthPercentage(100);

            PdfPCell personalDetails_headertable_cell = new PdfPCell(
                    new Phrase(personalDetails_header));
            // personalDetails_headertable_cell
            // .setBackgroundColor(BaseColor.LIGHT_GRAY);
            personalDetails_headertable_cell.setPadding(5);
            // personalDetails_headertable_cell
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // personalDetails_headertable
            // .addCell(personalDetails_headertable_cell);
            personalDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            // personalDetails_headertable_cell.setBorder(Rectangle.BOTTOM);
            // personalDetails_headertable_cell.setBorderColor(BaseColor.BLACK);
            personalDetails_headertable_cell.setColspan(3);
            // document.add(personalDetails_headertable);
            personalDetailsMain_headertable
                    .addCell(personalDetails_headertable_cell);
            personalDetailsMain_headertable
                    .addCell(personalDetails_headertable);

            /* FINANCIAL DETAILS */
            Paragraph finacialDetails_header = new Paragraph();
            finacialDetails_header.add(new Paragraph("Financial Details",
                    redFont));

            PdfPTable finacialDetails_headertable = new PdfPTable(3);
            finacialDetails_headertable.setSpacingBefore(3f);
            finacialDetails_headertable.setWidthPercentage(100);

            PdfPCell MonthlyIncome_cell1 = new PdfPCell(new Paragraph(
                    "Monthly Income", small_bold1));
            PdfPCell MonthlyIncome_cell2 = new PdfPCell(new Paragraph("Rs. "
                    + getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .valueOf(strMonthlyIncome))))),
                    small_normal1));
            // MonthlyIncome_cell1.setPadding(8);
            MonthlyIncome_cell1.setBorder(Rectangle.NO_BORDER);
            MonthlyIncome_cell2.setBorder(Rectangle.NO_BORDER);
            // document.add(PD_MonthlyIncome);

            /* Monthly Expenses Table */

            // PdfPTable PD_MonthlyExpenses = new PdfPTable(2);
            // PD_MonthlyExpenses.setWidthPercentage(100);
            PdfPCell MonthlyExpenses_cell1 = new PdfPCell(new Paragraph(
                    "Monthly Expenses", small_bold1));
            PdfPCell MonthlyExpenses_cell2 = new PdfPCell(new Paragraph("Rs. "
                    + getformatedThousandString(Integer.parseInt(obj
                    .getRound(obj.getStringWithout_E(Double
                            .valueOf(strMonthlyIncome_emi))))),
                    small_normal1));
            // MonthlyExpenses_cell1.setPadding(8);
            // MonthlyExpenses_cell2.setPadding(5);
            MonthlyExpenses_cell1.setBorder(Rectangle.NO_BORDER);
            MonthlyExpenses_cell2.setBorder(Rectangle.NO_BORDER);

            // document.add(PD_MonthlyExpenses);
            //
            // /* Outstanding home loan Table */

            // PdfPTable PD_outstandingHomeloan = new PdfPTable(2);
            // PD_outstandingHomeloan.setWidthPercentage(100);
            PdfPCell outstandingHomeloan_cell1 = new PdfPCell(new Paragraph(
                    "Outstanding Home Loan Amount", small_bold1));
            PdfPCell outstandingHomeloan_cell2 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer
                                    .parseInt(obj.getRound(obj.getStringWithout_E(Double
                                            .valueOf(strOutstandingHomeLoan))))),
                            small_normal1));
            // outstandingHomeloan_cell1.setPadding(8);
            // outstandingHomeloan_cell2.setPadding(5);
            outstandingHomeloan_cell1.setBorder(Rectangle.NO_BORDER);
            outstandingHomeloan_cell2.setBorder(Rectangle.NO_BORDER);

            // document.add(PD_outstandingHomeloan);

            /* Outstanding home loan Other Table */

            // PdfPTable PD_outstandingHomeloanOther = new PdfPTable(2);
            // PD_outstandingHomeloanOther.setWidthPercentage(100);
            PdfPCell outstandingHomeloanOther_cell1 = new PdfPCell(
                    new Paragraph("Outstanding Loan Amount (Others)",
                            small_bold1));
            PdfPCell outstandingHomeloanOther_cell2 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer
                                    .parseInt(obj.getRound(obj.getStringWithout_E(Double
                                            .valueOf(strOutstandingHomeLoanother))))),
                            small_normal1));
            // outstandingHomeloanOther_cell1.setPadding(8);
            // outstandingHomeloanOther_cell2.setPadding(5);
            outstandingHomeloanOther_cell1.setBorder(Rectangle.NO_BORDER);
            outstandingHomeloanOther_cell2.setBorder(Rectangle.NO_BORDER);

            // document.add(PD_outstandingHomeloanOther);

            /* Current Life Insurance Coverage Table */

            // PdfPTable PD_CurrentLifeInsuranceCoverage = new PdfPTable(2);
            // PD_CurrentLifeInsuranceCoverage.setWidthPercentage(100);
            PdfPCell CurrentLifeInsuranceCoverage_cell1 = new PdfPCell(
                    new Paragraph("Current Life Insurance Coverage",
                            small_bold1));
            PdfPCell CurrentLifeInsuranceCoverage_cell2 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer
                                    .parseInt(obj.getRound(obj.getStringWithout_E(Double
                                            .valueOf(strCurrentLifeInsuranceCoverage))))),
                            small_normal1));
            // CurrentLifeInsuranceCoverage_cell1.setPadding(8);
            // CurrentLifeInsuranceCoverage_cell2.setPadding(5);
            CurrentLifeInsuranceCoverage_cell1.setBorder(Rectangle.NO_BORDER);
            CurrentLifeInsuranceCoverage_cell2.setBorder(Rectangle.NO_BORDER);

            // document.add(PD_CurrentLifeInsuranceCoverage);

            /* Inflation Table */

            // PdfPTable PD_Inflation = new PdfPTable(2);
            // PD_Inflation.setWidthPercentage(100);
            PdfPCell Inflation_cell1 = new PdfPCell(new Paragraph(
                    "Expected Inflation Rates#", small_bold1));
            PdfPCell Inflation_cell2 = new PdfPCell(new Paragraph(
                    str_inflation_assumed, small_normal1));
            // Inflation_cell1.setPadding(8);
            // Inflation_cell2.setPadding(5);
            Inflation_cell1.setBorder(Rectangle.NO_BORDER);
            Inflation_cell2.setBorder(Rectangle.NO_BORDER);

            PdfPCell RiskAppetite_cell1 = new PdfPCell(new Paragraph(
                    "Risk Appetite", small_bold1));
            PdfPCell RiskAppetite_cell2 = new PdfPCell(new Paragraph(
                    str_investment_approach, small_normal1));
            // RiskAppetite_cell1.setPadding(8);
            // RiskAppetite_cell2.setPadding(5);
            RiskAppetite_cell1.setBorder(Rectangle.NO_BORDER);
            RiskAppetite_cell2.setBorder(Rectangle.NO_BORDER);

            finacialDetails_headertable.addCell(MonthlyIncome_cell1);
            finacialDetails_headertable.addCell(MonthlyExpenses_cell1);
            finacialDetails_headertable.addCell(outstandingHomeloan_cell1);

            finacialDetails_headertable.addCell(MonthlyIncome_cell2);
            finacialDetails_headertable.addCell(MonthlyExpenses_cell2);
            finacialDetails_headertable.addCell(outstandingHomeloan_cell2);

            finacialDetails_headertable.addCell(personal_empty_cell);

            finacialDetails_headertable.addCell(outstandingHomeloanOther_cell1);
            // finacialDetails_headertable
            // .addCell(CurrentLifeInsuranceCoverage_cell1);
            // finacialDetails_headertable
            // .addCell(CurrentLifeInsuranceCoverage_cell2);
            finacialDetails_headertable.addCell(Inflation_cell1);
            finacialDetails_headertable.addCell(RiskAppetite_cell1);

            finacialDetails_headertable.addCell(outstandingHomeloanOther_cell2);
            finacialDetails_headertable.addCell(Inflation_cell2);
            finacialDetails_headertable.addCell(RiskAppetite_cell2);
            // document.add(PD_RiskAppetite);

            PdfPTable finacialDetailsMain_headertable = new PdfPTable(1);
            finacialDetailsMain_headertable.setSpacingBefore(3f);
            finacialDetailsMain_headertable.setWidthPercentage(100);
            PdfPCell finacialDetails_headertable_cell = new PdfPCell(
                    new Phrase(finacialDetails_header));
            // finacialDetails_headertable_cell
            // .setBackgroundColor(BaseColor.LIGHT_GRAY);
            finacialDetails_headertable_cell.setPadding(5);
            finacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            // finacialDetails_headertable
            // .addCell(finacialDetails_headertable_cell);
            finacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            // document.add(finacialDetails_headertable);
            // finacialDetails_headertable_cell.setBorder(Rectangle.BOTTOM);
            // finacialDetails_headertable_cell.setBorderColor(new
            // BaseColor(230, 230, 230));
            // finacialDetails_headertable_cell.setColspan(4);
            // document.add(personalDetails_headertable);
            finacialDetailsMain_headertable
                    .addCell(finacialDetails_headertable_cell);

            finacialDetailsMain_headertable
                    .addCell(finacialDetails_headertable);

            PdfPTable personalFinancialDetails_headertable = new PdfPTable(2);
            personalFinancialDetails_headertable.setSpacingBefore(4f);
            personalFinancialDetails_headertable.setWidthPercentage(100);

            PdfPCell personalFinanacialDetails_headertable_cell;

            personalFinanacialDetails_headertable_cell = new PdfPCell(
                    personalDetailsMain_headertable);
            personalFinanacialDetails_headertable_cell.setPadding(5);
            personalFinanacialDetails_headertable_cell
                    .setBorder(Rectangle.NO_BORDER);
            personalFinanacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            personalFinancialDetails_headertable
                    .addCell(personalFinanacialDetails_headertable_cell);

            personalFinanacialDetails_headertable_cell = new PdfPCell(
                    finacialDetailsMain_headertable);
            personalFinanacialDetails_headertable_cell.setPadding(5);
            personalFinanacialDetails_headertable_cell
                    .setBorder(Rectangle.NO_BORDER);
            personalFinanacialDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            personalFinancialDetails_headertable
                    .addCell(personalFinanacialDetails_headertable_cell);

            document.add(personalFinancialDetails_headertable);
            StringBuilder builder = new StringBuilder();

            builder.append("We have made the analysis below of your current circumstances and your insurance and financial needs, based on the information recorded above.");
            if (!str_group.equalsIgnoreCase("Other")) {
                builder.append(" The products suggested for the various needs are also presented below.");
            }

            Paragraph para_input1 = new Paragraph(builder.toString(),
                    big_normal_biFont);
            para_input.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para_input1);
            /* Current Corpus Saved for Retirement */

            //
            document.add(para_img_logo_after_space_1);

            Paragraph para_output = new Paragraph(
                    "The analysis based on the inputs is given below: ",
                    small_normal_biFont);
            para_output.setAlignment(Element.ALIGN_CENTER);
            // document.add(para_output);

            // document.add(para_img_logo_after_space_1);
            /* Protection Output */

            Paragraph output_protection_header = new Paragraph();
            output_protection_header.add(new Paragraph("Protection", redFont));

            PdfPTable protection_output_table = new PdfPTable(2);
            protection_output_table.setWidthPercentage(100);
            protection_output_table.setWidths(new float[]{18f, 18f});

            PdfPCell protection_cell = new PdfPCell(new Phrase(
                    output_protection_header));
            protection_cell.setPadding(5);
            protection_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            protection_cell.setColspan(2);
            protection_output_table.addCell(protection_cell);

            protection_cell = new PdfPCell(new Phrase(" ", small_normal));
            protection_cell.setPadding(3);
            protection_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            protection_cell.setColspan(2);
            protection_output_table.addCell(protection_cell);

            PdfPCell protection_output_cell = new PdfPCell(new Paragraph("",
                    small_bold));
            protection_output_cell.setColspan(2);

            PdfPCell protection_output_cell_1 = new PdfPCell(new Paragraph(
                    "Total Protection Coverage Required", small_bold));
            protection_output_cell_1.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell protection_output_value_cell_1 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer
                                    .parseInt(obj.getRound(obj.getStringWithout_E(Double
                                            .valueOf(str_protection_cor_req))))),
                            small_normal));
            protection_output_value_cell_1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell protection_output_cell_2 = new PdfPCell(new Paragraph(
                    "Gap in Protection Coverage*", small_bold));
            protection_output_cell_2.setHorizontalAlignment(Element.ALIGN_LEFT);

            System.out.println(LifeProtGap + " " + str_protection_cor_req);
            PdfPCell protection_output_value_cell_2 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                                    .getStringWithout_E(Double
                                            .valueOf(LifeProtGap)))))
                                    + "\n("
                                    + Math.round((Double.parseDouble(obj
                                    .stringWithoutE(LifeProtGap)) / Double.parseDouble(obj
                                    .stringWithoutE(str_protection_cor_req))) * 100)
                                    + "%)", small_normal));
            protection_output_value_cell_2
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell protection_output_cell_4 = new PdfPCell(new Paragraph(
                    "Current Protection Coverage", small_bold));
            protection_output_cell_4.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell protection_output_value_cell_3 = new PdfPCell(
                    new Paragraph("Rs. "
                            + getformatedThousandString(Integer.parseInt(obj
                            .getRound(obj.getStringWithout_E(Double
                                    .valueOf(LifeProtCovCorpus))))),
                            small_normal));
            protection_output_value_cell_3
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            protection_output_cell_1.setPadding(3);
            protection_output_cell_2.setPadding(3);
            protection_output_cell_4.setPadding(3);

            // protection_output_value_cell_1.setPadding(3);
            // protection_output_cell_2.setPadding(3);
            // protection_output_cell_4.setPadding(3);

            protection_output_table.addCell(protection_output_cell_1);
            protection_output_table.addCell(protection_output_value_cell_1);

            protection_output_table.addCell(protection_output_cell_2);
            protection_output_table.addCell(protection_output_value_cell_2);

            protection_output_table.addCell(protection_output_cell_4);
            protection_output_table.addCell(protection_output_value_cell_3);

            if (!str_group.equalsIgnoreCase("Other")) {
                PdfPCell protection_output_cell_5 = new PdfPCell(new Paragraph(
                        "Suggested Product(s)", redFont));
                protection_output_cell_5
                        .setHorizontalAlignment(Element.ALIGN_LEFT);
                protection_output_cell_5.setPadding(7);
                protection_output_cell_5.setColspan(2);
                protection_output_cell_5.setRowspan(2);

                // protection_output_table.addCell(protection_output_cell_5);
                StringBuilder protectionVal = new StringBuilder();

                for (int i = 0; i < 4; i++) {
                    if (protection_prod_list.size() > i) {
                        protectionVal.append(protection_prod_list.get(i)
                                .getProduct_name()
                                + " ("
                                + protection_prod_list.get(i).getProduct_UIN()
                                + ")\n");
                        // protection_output_value_table.addCell(cell);
                    }
                }

                PdfPCell protection_output_value_cell_5 = new PdfPCell(
                        new Paragraph(protectionVal.toString(), small_normal));

                protection_output_value_cell_5.setColspan(2);
                protection_output_value_cell_5
                        .setHorizontalAlignment(Element.ALIGN_LEFT);

                protection_output_table.addCell(protection_output_cell_5);
                protection_output_table.addCell(protection_output_value_cell_5);

            }
            // document.add(protection_output_table);

            /*
             * String Retire_total_corpus_req = obj
             * .stringWithoutE((LifeProtCovCorpus_ + Double .parseDouble(obj
             * .stringWithoutE(PensionActAnnInvGap))) + "");
             */
            /* Retirement Output */
            Paragraph output_retirement_header = new Paragraph();
            output_retirement_header.add(new Paragraph("Retirement @ "
                    + strRetirementNoOfRealise + " Yrs", redFont));

            PdfPTable output_retirement_headertable = new PdfPTable(2);
            output_retirement_headertable.setWidths(new float[]{18f, 18f});
            output_retirement_headertable.setWidthPercentage(100);
            //
            //
            PdfPCell output_retirement_headertable_cell = new PdfPCell(
                    new Phrase(output_retirement_header));
            output_retirement_headertable_cell.setPadding(5);
            output_retirement_headertable_cell.setColspan(2);
            output_retirement_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            output_retirement_headertable
                    .addCell(output_retirement_headertable_cell);

            output_retirement_headertable_cell = new PdfPCell(new Phrase(" ",
                    small_normal));
            output_retirement_headertable_cell.setPadding(3);
            output_retirement_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            output_retirement_headertable_cell.setColspan(2);
            output_retirement_headertable
                    .addCell(output_retirement_headertable_cell);

            PdfPCell retirement_output_cell_1 = new PdfPCell(new Paragraph(
                    "Total Corpus Required", small_bold));
            retirement_output_cell_1.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell retirement_output_cell_2 = new PdfPCell(new Paragraph(
                    "Gap Remaining", small_bold));
            retirement_output_cell_2.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell retirement_output_cell_3 = new PdfPCell(new Paragraph(
                    "Monthly Investment Required*", small_bold));

            retirement_output_cell_3.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell retirement_output_cell_4 = new PdfPCell(new Paragraph(
                    "Years left to Retire", small_bold));
            retirement_output_cell_4.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell retirement_output_value_cell_1 = new PdfPCell(
                    new Paragraph("Rs. "
                            + getformatedThousandString(Integer.parseInt(obj
                            .getRound(obj.getStringWithout_E(Double
                                    .valueOf(PensionActTarget_))))),
                            small_normal));
            retirement_output_value_cell_1
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            System.out.println(PensionActAnnInvGap + " " + PensionActTarget_);

            PdfPCell retirement_output_value_cell_2 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf(PensionActAnnInvGap)))))
                                    + "\n("
                                    + Math.round((Double.parseDouble(obj
                                    .stringWithoutE(PensionActAnnInvGap)) / PensionActTarget_) * 100)
                                    + "%)", small_normal));
            retirement_output_value_cell_2
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell retirement_output_value_cell_3 = new PdfPCell(
                    new Paragraph("Rs. " + Ret_Monthly_inv, small_normal));
            retirement_output_value_cell_3
                    .setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell retirement_output_value_cell_4 = new PdfPCell(
                    new Paragraph(Ret_Left_Year + " Years", small_normal));
            retirement_output_value_cell_4
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            output_retirement_headertable.addCell(retirement_output_cell_1);
            output_retirement_headertable
                    .addCell(retirement_output_value_cell_1);

            output_retirement_headertable.addCell(retirement_output_cell_2);
            output_retirement_headertable
                    .addCell(retirement_output_value_cell_2);

            output_retirement_headertable.addCell(retirement_output_cell_3);
            output_retirement_headertable
                    .addCell(retirement_output_value_cell_3);

            output_retirement_headertable.addCell(retirement_output_cell_4);
            output_retirement_headertable
                    .addCell(retirement_output_value_cell_4);
            if (!str_group.equalsIgnoreCase("Other")) {
                cell = new PdfPCell(new Paragraph("Suggested Product(s)",
                        redFont));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setColspan(2);

                cell.setRowspan(2);
                output_retirement_headertable.addCell(cell);
                StringBuilder retirementVal = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    if (retirement_prod_list.size() > i) {
                        if (retirement_prod_list.get(i).getProduct_name() != null)
                            retirementVal.append(retirement_prod_list.get(i)
                                    .getProduct_name()
                                    + " ("
                                    + retirement_prod_list.get(i)
                                    .getProduct_UIN() + ")\n");
                    }
                }

                cell = new PdfPCell(new Paragraph(retirementVal.toString(),
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(2);
                cell.setColspan(6);
                output_retirement_headertable.addCell(cell);
            }
            String wealth_home_total_corpus_req = obj.stringWithoutE((Double
                    .parseDouble(obj.stringWithoutE(CostHomeGap)) + Double
                    .parseDouble(obj.stringWithoutE(CostHomeCorpus)))
                    + "");

            String wealth_other_total_corpus_req = obj.stringWithoutE((Double
                    .parseDouble(obj.stringWithoutE(CostOtherGap)) + Double
                    .parseDouble(obj.stringWithoutE(CostOtherCorpus)))
                    + "");

            Paragraph output_Wealth_outut_header = new Paragraph();
            output_Wealth_outut_header.add(new Paragraph(
                    "Insurance with Savings", redFont));

            PdfPTable output_Wealth_outut_headertable = new PdfPTable(3);
            output_Wealth_outut_headertable.setWidthPercentage(100);

            PdfPCell output_Wealth_outut_headertable_cell = new PdfPCell(
                    new Phrase(output_Wealth_outut_header));
            // output_Wealth_outut_headertable_cell
            // .setBackgroundColor(BaseColor.LIGHT_GRAY);
            output_Wealth_outut_headertable_cell.setPadding(5);
            output_Wealth_outut_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            output_Wealth_outut_headertable_cell.setColspan(3);
            output_Wealth_outut_headertable
                    .addCell(output_Wealth_outut_headertable_cell);
            // output_Wealth_outut_headertable
            // .setHorizontalAlignment(Element.ALIGN_CENTER);
            // document.add(output_Wealth_outut_headertable);
            cell = new PdfPCell(new Paragraph("", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            output_Wealth_outut_headertable.addCell(cell);

            cell = new PdfPCell(new Paragraph("Dream Home", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            output_Wealth_outut_headertable.addCell(cell);

            cell = new PdfPCell(new Paragraph("Other", small_bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            output_Wealth_outut_headertable.addCell(cell);

            PdfPCell wealth_output_cell_1 = new PdfPCell(new Paragraph(
                    "Total Corpus Required", small_bold));

            wealth_output_cell_1.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell wealth_output_value1_cell_2 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer
                                    .parseInt(obj.getRound(obj.getStringWithout_E(Double
                                            .valueOf(wealth_home_total_corpus_req))))),
                            small_normal));
            wealth_output_value1_cell_2
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell wealth_output_value2_cell_2 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer
                                    .parseInt(obj.getRound(obj.getStringWithout_E(Double
                                            .valueOf(wealth_other_total_corpus_req))))),
                            small_normal));
            wealth_output_value2_cell_2
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            output_Wealth_outut_headertable.addCell(wealth_output_cell_1);
            output_Wealth_outut_headertable
                    .addCell(wealth_output_value1_cell_2);
            output_Wealth_outut_headertable
                    .addCell(wealth_output_value2_cell_2);

            PdfPCell wealth_output_cell_2 = new PdfPCell(new Paragraph(
                    "Gap Remaining", small_bold));
            wealth_output_cell_2.setHorizontalAlignment(Element.ALIGN_LEFT);
            //
            System.out.println(CostHomeGap + " " + CostHomeGap + " "
                    + CostHomeGap + " " + wealth_home_total_corpus_req);
            PdfPCell wealth_output_value1_cell_3 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                                    .getStringWithout_E(Double
                                            .valueOf(CostHomeGap)))))
                                    + "\n("
                                    + Math.round((Double.parseDouble(obj
                                    .stringWithoutE(CostHomeGap)) / Double.parseDouble(obj
                                    .stringWithoutE(wealth_home_total_corpus_req))) * 100)
                                    + "%)", small_normal));

            wealth_output_value1_cell_3
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell wealth_output_value2_cell_3 = new PdfPCell(
                    new Paragraph(
                            "Rs. "
                                    + getformatedThousandString(Integer.parseInt(obj.getRound(obj
                                    .getStringWithout_E(Double
                                            .valueOf(CostOtherGap)))))
                                    + "\n("
                                    + Math.round((Double.parseDouble(obj
                                    .stringWithoutE(CostOtherGap)) / Double.parseDouble(obj
                                    .stringWithoutE(wealth_other_total_corpus_req))) * 100)
                                    + "%)", small_normal));
            wealth_output_value2_cell_3
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            output_Wealth_outut_headertable.addCell(wealth_output_cell_2);
            output_Wealth_outut_headertable
                    .addCell(wealth_output_value1_cell_3);
            output_Wealth_outut_headertable
                    .addCell(wealth_output_value2_cell_3);

            PdfPCell wealth_output_cell_4 = new PdfPCell(new Paragraph(
                    "Monthly Investment Required*", small_bold));
            wealth_output_cell_4.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell wealth_output_value1_cell_4 = new PdfPCell(new Paragraph(
                    "Rs. " + Home_Monthly_inv, small_normal));
            wealth_output_value1_cell_4
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell wealth_output_value2_cell_4 = new PdfPCell(new Paragraph(
                    "Rs. " + Other_Monthly_inv, small_normal));
            wealth_output_value2_cell_4
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            output_Wealth_outut_headertable.addCell(wealth_output_cell_4);
            output_Wealth_outut_headertable
                    .addCell(wealth_output_value1_cell_4);
            output_Wealth_outut_headertable
                    .addCell(wealth_output_value2_cell_4);

            PdfPCell wealth_output_cell_5 = new PdfPCell(new Paragraph(
                    "No. of Years to realise", small_bold));
            wealth_output_cell_5.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell wealth_output_value1_cell_5 = new PdfPCell(new Paragraph(
                    getformatedThousandString(Integer.parseInt(obj.getRound(obj
                            .getStringWithout_E(Double
                                    .valueOf(strNoOfYearBuyHome)))))
                            + " Years", small_normal));
            wealth_output_value1_cell_5
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell wealth_output_value2_cell_5 = new PdfPCell(new Paragraph(
                    getformatedThousandString(Integer.parseInt(obj.getRound(obj
                            .getStringWithout_E(Double
                                    .valueOf(strNoOfYearOthergoal)))))
                            + " Years", small_normal));
            wealth_output_value2_cell_5
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            output_Wealth_outut_headertable.addCell(wealth_output_cell_5);
            output_Wealth_outut_headertable
                    .addCell(wealth_output_value1_cell_5);
            output_Wealth_outut_headertable
                    .addCell(wealth_output_value2_cell_5);

            // wealth_output_cell_1.setPadding(5);
            // wealth_output_cell_2.setPadding(5);
            // wealth_output_cell_3.setPadding(5);
            // wealth_output_cell_4.setPadding(5);
            // wealth_output_cell_5.setPadding(5);
            if (!str_group.equalsIgnoreCase("Other")) {
                cell = new PdfPCell(new Paragraph("Suggested Product(s)",
                        redFont));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setColspan(3);
                output_Wealth_outut_headertable.addCell(cell);

                StringBuilder wealthVal = new StringBuilder();

                for (int i = 0; i < 4; i++) {
                    if (wealth_prod_list.size() > i) {
                        if (wealth_prod_list.get(i).getProduct_name() != null)
                            wealthVal.append(wealth_prod_list.get(i)
                                    .getProduct_name()
                                    + " ("
                                    + wealth_prod_list.get(i).getProduct_UIN()
                                    + ")\n");
                    }
                }

                cell = new PdfPCell(new Paragraph(wealthVal.toString(),
                        small_normal));

                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setColspan(3);
                output_Wealth_outut_headertable.addCell(cell);
            }
            PdfPTable protectionRetirementWealthDetails_headertable = new PdfPTable(
                    3);
            protectionRetirementWealthDetails_headertable.setSpacingBefore(2f);
            protectionRetirementWealthDetails_headertable
                    .setWidthPercentage(100);

            PdfPCell protectionRetirementWealthDetails_headertable_cell;

            protectionRetirementWealthDetails_headertable_cell = new PdfPCell(
                    protection_output_table);
            protectionRetirementWealthDetails_headertable_cell.setPadding(5);
            protectionRetirementWealthDetails_headertable_cell
                    .setBorder(Rectangle.NO_BORDER);
            protectionRetirementWealthDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            protectionRetirementWealthDetails_headertable
                    .addCell(protectionRetirementWealthDetails_headertable_cell);

            protectionRetirementWealthDetails_headertable_cell = new PdfPCell(
                    output_retirement_headertable);
            protectionRetirementWealthDetails_headertable_cell.setPadding(5);
            protectionRetirementWealthDetails_headertable_cell
                    .setBorder(Rectangle.NO_BORDER);
            protectionRetirementWealthDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            protectionRetirementWealthDetails_headertable
                    .addCell(protectionRetirementWealthDetails_headertable_cell);

            protectionRetirementWealthDetails_headertable_cell = new PdfPCell(
                    output_Wealth_outut_headertable);
            protectionRetirementWealthDetails_headertable_cell.setPadding(5);
            protectionRetirementWealthDetails_headertable_cell
                    .setBorder(Rectangle.NO_BORDER);
            protectionRetirementWealthDetails_headertable_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            protectionRetirementWealthDetails_headertable
                    .addCell(protectionRetirementWealthDetails_headertable_cell);

            document.add(protectionRetirementWealthDetails_headertable);

            //
            PdfPTable Child_headertable = new PdfPTable(6);
            Child_headertable.setWidths(new float[]{6f, 6f, 6f, 6f, 6f, 6f});
            Child_headertable.setWidthPercentage(100);

            if (Integer.parseInt(str_no_of_child) > 0) {

                String child_1_Edu_total_corps_req = obj.stringWithoutE((Double
                        .parseDouble(Child_1_edu_CurCorpus) + Double
                        .parseDouble(Child_1_edu_Gap))
                        + "");
                String child_1_Mrg_total_corps_req = obj.stringWithoutE((Double
                        .parseDouble(Child_1_mar_CurCorpus) + Double
                        .parseDouble(Child_1_mar_Gap))
                        + "");

                cell = new PdfPCell(
                        new Phrase("Child Future Planning", redFont));
                cell.setColspan(6);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                /* Child1_education Output */
                cell = new PdfPCell();
                cell.setColspan(2);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        "Age at Which money is required", small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(3);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Total Corpus Required",
                        small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(3);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Gap Remaining", small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(3);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Monthly Investment Required*",
                        small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(3);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(str_child1Name + " ("
                        + strChild1Age + ") yrs", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setRowspan(2);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Education", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild1AgeAtEducation,
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + child_1_Edu_total_corps_req, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);
                System.out.println(Child_1_edu_Gap + " "
                        + child_1_Edu_total_corps_req);

                cell = new PdfPCell(
                        new Phrase(
                                "Rs. "
                                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(Child_1_edu_Gap)))))
                                        + "("
                                        + Math.round((Double.parseDouble(obj
                                        .stringWithoutE(Child_1_edu_Gap)) / Double.parseDouble(obj
                                        .stringWithoutE(child_1_Edu_total_corps_req))) * 100)
                                        + "%)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + Child_1_Edu_Monthly_Cost, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Marriage", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild1AgeAtMarriage,
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + child_1_Mrg_total_corps_req, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(
                        new Phrase(
                                "Rs. "
                                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(Child_1_mar_Gap)))))
                                        + "  ("
                                        + Math.round((Double.parseDouble(obj
                                        .stringWithoutE(Child_1_mar_Gap)) / Double.parseDouble(obj
                                        .stringWithoutE(child_1_Mrg_total_corps_req))) * 100)
                                        + "%)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + Child_1_Mrg_Monthly_Cost, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);
            }

            if (Integer.parseInt(str_no_of_child) > 1) {

                cell = new PdfPCell(new Phrase(" ", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(6);
                Child_headertable.addCell(cell);

                String child_2_Edu_total_corps_req = obj.stringWithoutE((Double
                        .parseDouble(Child_2_edu_CurCorpus) + Double
                        .parseDouble(Child_2_edu_Gap))
                        + "");
                String child_2_Mrg_total_corps_req = obj.stringWithoutE((Double
                        .parseDouble(Child_2_mar_CurCorpus) + Double
                        .parseDouble(Child_2_mar_Gap))
                        + "");

                cell = new PdfPCell(new Phrase(str_child2Name + " ("
                        + strChild2Age + ") yrs", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setRowspan(2);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Education", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild2AgeAtEducation,
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + child_2_Edu_total_corps_req, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(
                        new Phrase(
                                "Rs. "
                                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(Child_2_edu_Gap)))))
                                        + "  ("
                                        + Math.round((Double.parseDouble(obj
                                        .stringWithoutE(Child_2_edu_Gap)) / Double.parseDouble(obj
                                        .stringWithoutE(child_2_Edu_total_corps_req))) * 100)
                                        + "%)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + Child_2_Edu_Monthly_Cost, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Marriage", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild2AgeAtMarriage,
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + child_2_Mrg_total_corps_req, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(
                        new Phrase(
                                "Rs. "
                                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(Child_2_mar_Gap)))))
                                        + "  ("
                                        + Math.round((Double.parseDouble(obj
                                        .stringWithoutE(Child_2_mar_Gap)) / Double.parseDouble(obj
                                        .stringWithoutE(child_2_Mrg_total_corps_req))) * 100)
                                        + "%)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + Child_2_Mrg_Monthly_Cost, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);
            }

            if (Integer.parseInt(str_no_of_child) > 2) {

                cell = new PdfPCell(new Phrase(" ", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(6);
                Child_headertable.addCell(cell);

                String child_3_Edu_total_corps_req = obj.stringWithoutE((Double
                        .parseDouble(Child_3_edu_CurCorpus) + Double
                        .parseDouble(Child_3_edu_Gap))
                        + "");
                String child_3_Mrg_total_corps_req = obj.stringWithoutE((Double
                        .parseDouble(Child_3_mar_CurCorpus) + Double
                        .parseDouble(Child_3_mar_Gap))
                        + "");

                cell = new PdfPCell(new Phrase(str_child3Name + " ("
                        + strChild3Age + ") yrs", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setRowspan(2);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Education", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild3AgeAtEducation,
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + child_3_Edu_total_corps_req, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(
                        new Phrase(
                                "Rs. "
                                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(Child_3_edu_Gap)))))
                                        + "  ("
                                        + Math.round((Double.parseDouble(obj
                                        .stringWithoutE(Child_3_edu_Gap)) / Double.parseDouble(obj
                                        .stringWithoutE(child_3_Edu_total_corps_req))) * 100)
                                        + "%)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + Child_3_Edu_Monthly_Cost, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Marriage", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild3AgeAtMarriage,
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + child_3_Mrg_total_corps_req, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(
                        new Phrase(
                                "Rs. "
                                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(Child_3_mar_Gap)))))
                                        + "  ("
                                        + Math.round((Double.parseDouble(obj
                                        .stringWithoutE(Child_3_mar_Gap)) / Double.parseDouble(obj
                                        .stringWithoutE(child_3_Mrg_total_corps_req))) * 100)
                                        + "%)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + Child_3_Mrg_Monthly_Cost, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);
            }

            if (Integer.parseInt(str_no_of_child) > 3) {

                cell = new PdfPCell(new Phrase(" ", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setColspan(6);
                Child_headertable.addCell(cell);

                String child_4_Edu_total_corps_req = obj.stringWithoutE((Double
                        .parseDouble(Child_4_edu_CurCorpus) + Double
                        .parseDouble(Child_4_edu_Gap))
                        + "");
                String child_4_Mrg_total_corps_req = obj.stringWithoutE((Double
                        .parseDouble(Child_4_mar_CurCorpus) + Double
                        .parseDouble(Child_4_mar_Gap))
                        + "");

                cell = new PdfPCell(new Phrase(str_child4Name + " ("
                        + strChild4Age + ") yrs", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setRowspan(2);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Education", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild4AgeAtEducation,
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + child_4_Edu_total_corps_req, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(
                        new Phrase(
                                "Rs. "
                                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(Child_4_edu_Gap)))))
                                        + "  ("
                                        + Math.round((Double
                                        .parseDouble(Child_4_edu_Gap) / Double
                                        .parseDouble(child_4_Edu_total_corps_req)) * 100)
                                        + "%)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + Child_4_Edu_Monthly_Cost, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Marriage", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase(strChild4AgeAtMarriage,
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + child_4_Mrg_total_corps_req, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(
                        new Phrase(
                                "Rs. "
                                        + getformatedThousandString(Integer.parseInt(obj.getRound(obj.getStringWithout_E(Double
                                        .valueOf(Child_4_mar_Gap)))))
                                        + "  ("
                                        + Math.round((Double
                                        .parseDouble(Child_4_mar_Gap) / Double
                                        .parseDouble(child_4_Mrg_total_corps_req)) * 100)
                                        + "%)", small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);

                cell = new PdfPCell(new Phrase("Rs. "
                        + Child_4_Mrg_Monthly_Cost, small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                Child_headertable.addCell(cell);
            }
            if (!str_group.equalsIgnoreCase("Other")) {
                cell = new PdfPCell(
                        new Paragraph("Suggested Products", redFont));

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                cell.setColspan(6);
                Child_headertable.addCell(cell);

                StringBuilder childVal = new StringBuilder();

                for (int i = 0; i < 4; i++) {
                    if (child_prod_list.size() > i) {
                        if (child_prod_list.get(i).getProduct_name() != null)
                            childVal.append(child_prod_list.get(i)
                                    .getProduct_name()
                                    + " ("
                                    + child_prod_list.get(i).getProduct_UIN()
                                    + ")\n");
                    }
                }

                cell = new PdfPCell(new Paragraph(childVal.toString(),
                        small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(5);
                cell.setColspan(6);
                Child_headertable.addCell(cell);
            }
            if (Integer.parseInt(str_no_of_child) > 0)
                document.add(Child_headertable);

            String para3 = "# Inflation Rates are assumed & subjective in nature";

            Paragraph para3_gapBody3 = new Paragraph(para3, small_normal_biFont);
            para3_gapBody3.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para3_gapBody3);
            document.add(para_img_logo_after_space_1);

            String para4 = "*The Monthly Investment Required is the absolute amount required and does not include the Mortality Charge, Applicable Tax & Other charges.";

            Paragraph para4_gapBody4 = new Paragraph(para4, small_normal_biFont);
            para4_gapBody4.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para4_gapBody4);
            document.add(para_img_logo_after_space_1);

            StringBuilder gapBody4 = new StringBuilder();

            if (!str_group.equalsIgnoreCase("Other")) {
                gapBody4.append("We request you to review the above analysis and set your priorities of the needs that you would like to "
                        + "address now or in the near future and take a considered decision on the suggested insurance products that you would wish to buy from us. As per your"
                        + " analysis of your needs, you may also review the other insurance products offered by SBI Life.");
            } else {
                gapBody4.append("We request you to review the above analysis and set your priorities of the needs that you would like to "
                        + "address now or in the near future and take a considered decision on the insurance products that you would wish to buy from us. As per your"
                        + " analysis of your needs, you may also review the other insurance products offered by SBI Life.");
            }

            Paragraph para_gapBody4 = new Paragraph(gapBody4.toString(),
                    big_normal_biFont);
            para_gapBody4.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para_gapBody4);
            document.add(para_img_logo_after_space_1);

            String gapBody5 = "Note: This is an illustrative projection of your future insurance and financial needs. All figures are calculated as per SBI Life's need analysis"
                    + " calculator and are based on the information provided by you, estimated cost of living and assumed inflation rate.";

            Paragraph para_gapBody5 = new Paragraph(gapBody5, new Font(
                    Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL));
            para_gapBody5.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para_gapBody5);
            document.add(para_img_logo_after_space_1);

            String gapBody = "I have gone through the financial analysis carefully and have chosen the following products from those recommended to me, based on my financial circumstances and priorities. The products have been explained to me in detail.";

            Paragraph para_gapBody = new Paragraph(gapBody, big_normal_biFont);
            para_gapBody.setAlignment(Element.ALIGN_JUSTIFIED);

            PdfPTable suggestedProductTable = new PdfPTable(1);
            suggestedProductTable.setWidthPercentage(100);
            suggestedProductTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            // suggestedProductTable.setWidths(new float[] { 15f });

            for (int i = 0; i < sugg_chosen_prod_list.size(); i++) {
                cell = new PdfPCell(new Phrase((i + 1)
                        + ". SBI Life - "
                        + sugg_chosen_prod_list.get(i).getProduct_name()
                        + "  ( "
                        + sugg_chosen_prod_list.get(i).getProduct_UIN()
                        + " )"
                        + "  - "
                        + getId(map, sugg_chosen_prod_list.get(i)
                        .getProduct_name()),
                        medium_normal_underline_biFont));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                suggestedProductTable.addCell(cell);

                if (sugg_chosen_prod_list.get(i).getProduct_name()
                        .equals("Smart Wealth Builder"))
                    smart_wealth_builder_flag = 1;
            }

            if (sugg_chosen_prod_list.size() > 0) {
                document.add(para_gapBody);
                document.add(para_img_logo_after_space_1);
                document.add(suggestedProductTable);
            }

            String gapBody1 = "";

            if (!str_group.equalsIgnoreCase("Other")) {
                if (sugg_chosen_prod_list.size() > 0
                        && other_chosen_prod_list.size() > 0)
                    gapBody1 = "I would also like to purchase the following product/s which has not been recommended to me. I have gone through the product literature and fully understand the product/s.";
                else if (sugg_chosen_prod_list.size() == 0
                        && other_chosen_prod_list.size() > 0)
                    gapBody1 = "I would like to purchase the following product/s which has not been recommended to me. I have gone through the product literature and fully understand the product/s.";
                else if (sugg_chosen_prod_list.size() > 0
                        && other_chosen_prod_list.size() == 0)
                    gapBody1 = "";

                Paragraph para_gapBody1 = new Paragraph(gapBody1,
                        big_normal_biFont);
                para_gapBody1.setAlignment(Element.ALIGN_JUSTIFIED);

                document.add(para_img_logo_after_space_1);

                PdfPTable otherchosenProductTable = new PdfPTable(1);
                otherchosenProductTable.setWidthPercentage(100);
                otherchosenProductTable
                        .setHorizontalAlignment(Element.ALIGN_LEFT);
                // otherchosenProductTable.setWidths(new float[] { 15f });

                // Paragraph suggested_product_Body;
                for (int i = 0; i < other_chosen_prod_list.size(); i++) {
                    cell = new PdfPCell(new Phrase((i + 1)
                            + ".   SBI Life - "
                            + other_chosen_prod_list.get(i).getProduct_name()
                            + "  ( "
                            + other_chosen_prod_list.get(i).getProduct_UIN()
                            + " )"

                           /* + "  - "
                            + getId(map, other_chosen_prod_list.get(i)
                            .getProduct_name())*/

                            , medium_normal_underline_biFont));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setBorder(Rectangle.NO_BORDER);
                    otherchosenProductTable.addCell(cell);

                    //
                    //
                    // if(suggested_prod_list.get(i).getProduct_name().equals("Smart Wealth Builder"))
                    // smart_wealth_builder_flag=1;
                    //
                }

                if (other_chosen_prod_list.size() > 0) {
                    document.add(para_gapBody1);
                    document.add(para_img_logo_after_space_1);
                    // Paragraph para_gapBody2 = new
                    // small_bold);
                    // para_gapBody1.setAlignment(Element.ALIGN_LEFT);
                    document.add(otherchosenProductTable);
                    document.add(para_img_logo_after_space_1);
                }
                String gapBody2 = "I have voluntarily chosen products based on my insurance needs and financial objectives.";
                Paragraph para_gapBody2 = new Paragraph(gapBody2,
                        big_normal_biFont);
                para_gapBody2.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(para_gapBody2);
                document.add(para_img_logo_after_space_1);

                String swb_line_gapBody2 = "*We suggest you to opt for Bond Fund under Smart Wealth Builder(UIN:111L095V03)";

                Paragraph swb_linepara_gapBody1 = new Paragraph(
                        swb_line_gapBody2, big_normal_biFont);
                swb_linepara_gapBody1.setAlignment(Element.ALIGN_JUSTIFIED);

                if (Integer.parseInt(str_age) > 45
                        && smart_wealth_builder_flag == 1) {
                    document.add(swb_linepara_gapBody1);
                    document.add(para_img_logo_after_space_1);
                }

                String gapBody3 = "I further confirm that I have not been compelled to purchase any of the above insurance products by the sales intermediary for availing any other financial product or facility that is offered by the bank.";
                Paragraph para_gapBody3 = new Paragraph(gapBody3,
                        big_normal_biFont);
                para_gapBody3.setAlignment(Element.ALIGN_JUSTIFIED);
                if (GetUserType().equals("CIF"))
                    document.add(para_gapBody3);

                document.add(para_img_logo_after_space_1);

                byte[] fbyt_Proposer = Base64.decode(proposer_sign, 0);
                Bitmap Proposerbitmap = BitmapFactory.decodeByteArray(
                        fbyt_Proposer, 0, fbyt_Proposer.length);

                // PdfPCell BI_PdftablePolicyHolder_signature_3 = new
                // PdfPCell();
                // BI_PdftablePolicyHolder_signature_3.setFixedHeight(60f);
                ByteArrayOutputStream PolicyHolder_signature_stream = new ByteArrayOutputStream();

                (Proposerbitmap).compress(Bitmap.CompressFormat.PNG, 50,
                        PolicyHolder_signature_stream);
                Image PolicyHolder_signature = Image
                        .getInstance(PolicyHolder_signature_stream
                                .toByteArray());

                PolicyHolder_signature.scaleToFit(90, 90);

                document.add(para_img_logo_after_space_1);
                PdfPTable BI_PdftableMarketing = new PdfPTable(3);
                BI_PdftableMarketing.setWidths(new float[]{5f, 5f, 5f});
                BI_PdftableMarketing.setWidthPercentage(100);

                byte[] fbyt_agent = Base64.decode(agent_sign, 0);
                Bitmap Agentbitmap = BitmapFactory.decodeByteArray(fbyt_agent,
                        0, fbyt_agent.length);

                // PdfPCell BI_PdftableMarketing_signature_3 = new PdfPCell();
                // BI_PdftableMarketing_signature_3.setFixedHeight(60f);
                ByteArrayOutputStream Marketing_officials_signature_stream = new ByteArrayOutputStream();

                (Agentbitmap).compress(Bitmap.CompressFormat.PNG, 50,
                        Marketing_officials_signature_stream);
                Image Marketing_officials_signature = Image
                        .getInstance(Marketing_officials_signature_stream
                                .toByteArray());

                Marketing_officials_signature.scaleToFit(90, 90);

                // BI_PdftableMarketing_signature_3
                // .setImage(Marketing_officials_signature);
                // BI_PdftableMarketing_signature_1.setPadding(5);
                // BI_PdftableMarketing_signature_2.setPadding(5);

                // BI_PdftableMarketing_signature
                // .addCell(BI_PdftableMarketing_signature_1);

                BI_PdftableMarketing.addCell(Nocell);

                BI_PdftableMarketing.addCell(Nocell);

                cell = new PdfPCell(new Paragraph(GetUserName() + " / "
                        + GetUserCode(), small_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(10);
                cell.setBorder(Rectangle.NO_BORDER);
                BI_PdftableMarketing.addCell(cell);

                PdfPCell sign_cell = new PdfPCell(PolicyHolder_signature);
                sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sign_cell.setPadding(5);
                sign_cell.setBorder(Rectangle.NO_BORDER);
                BI_PdftableMarketing.addCell(sign_cell);

                BI_PdftableMarketing.addCell(Nocell);

                sign_cell = new PdfPCell(Marketing_officials_signature);
                sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sign_cell.setPadding(5);
                sign_cell.setBorder(Rectangle.NO_BORDER);
                BI_PdftableMarketing.addCell(sign_cell);

                sign_cell = new PdfPCell(new Paragraph(
                        "Customer Name/Signature", small_bold));
                sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sign_cell.setPadding(5);
                sign_cell.setBorder(Rectangle.TOP);
                BI_PdftableMarketing.addCell(sign_cell);

                BI_PdftableMarketing.addCell(Nocell);

                sign_cell = new PdfPCell(new Paragraph(
                        "Intermediary's Name/Code/Signature", small_bold));
                sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sign_cell.setBorder(Rectangle.TOP);
                sign_cell.setPadding(5);

                BI_PdftableMarketing.addCell(sign_cell);

                document.add(BI_PdftableMarketing);
                document.add(para_img_logo_after_space_1);
            } else {

            }

            PdfPTable BI_PdftableDate = new PdfPTable(1);

            BI_PdftableDate.setWidthPercentage(100);

            PdfPCell BI_Pdftable_Date_cell = new PdfPCell(new Paragraph(
                    "Date  :" + getCurrentDate(), small_normal));
            BI_Pdftable_Date_cell.setPadding(5f);
            BI_Pdftable_Date_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableDate.addCell(BI_Pdftable_Date_cell);
            document.add(BI_PdftableDate);
            document.add(para_img_logo_after_space_1);

            String para_textGap_italic = "Purchase of insurance product is voluntary. For more details on the products, risk factors, terms and conditions please read sales brochure carefully before concluding a sale. Trade logo displayed above belongs to State Bank of India and is used by SBI Life under license."
                    + "Registered and Corporate Office: SBI Life Insurance Company Limited, Natraj, M.V.Road & Western Express Highway Junction, Andheri(East), Mumbai-400069. IRDAI Registration. No. 111. CIN: L99999MH2000PLC129113\\ Website: www.sbilife.co.in\\ Email id: info@sbilife.co.in \\ Toll free no - 1800-267-9090(Between 9:00 am to 9.00 pm)";

            String textGapBody = "For more details on the products, risk factors, terms and conditions please read sales brochure carefully before concluding a sale. Trade logo displayed above belongs to State Bank of India and is used by SBI Life under license."
                    + "Registered and Corporate Office: SBI Life Insurance Company Limited, Natraj, M.V.Road & Western Express Highway Junction, Andheri(East), Mumbai-400069. IRDAI Registration. No. 111. CIN: L99999MH2000PLC129113\\ Website: www.sbilife.co.in\\ Email id: info@sbilife.co.in \\ Toll free no - 1800-267-9090(Between 9:00 am to 9.00 pm)";

            Paragraph para_textGap;
            if (GetUserType().equals("CIF"))
                para_textGap = new Paragraph(para_textGap_italic, small_normal);
            else
                para_textGap = new Paragraph(textGapBody, small_normal);
            para_textGap.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(para_textGap);
            // document.add(para_img_logo_after_space_1);

            document.close();

        } catch (Exception e) {
            Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
        }

    }

    public void oninfo(View v) {
        final Dialog d = new Dialog(this);
        d.setCancelable(true);

        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));
        d.setContentView(R.layout.layout_investment_info);

        LinearLayout ll_inflation_rate = d
                .findViewById(R.id.ll_inflation_rate);
        ll_inflation_rate.setVisibility(View.GONE);
        LinearLayout ll_risk = d.findViewById(R.id.ll_risk);
        ll_risk.setVisibility(View.VISIBLE);
        d.show();
    }

    public void onInflaation(View v) {
        final Dialog d = new Dialog(this);
        d.setCancelable(true);

        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));
        d.setContentView(R.layout.layout_investment_info);

        LinearLayout ll_inflation_rate = d
                .findViewById(R.id.ll_inflation_rate);

        ll_inflation_rate.setVisibility(View.VISIBLE);
        LinearLayout ll_risk = d.findViewById(R.id.ll_risk);
        ll_risk.setVisibility(View.GONE);
        d.show();
    }

    /*
     * public void Dialog() { final Dialog d = new Dialog(this);
     * d.setCancelable(true);
     *
     * LinearLayout ll_inflation_rate = (LinearLayout) d
     * .findViewById(R.id.ll_inflation_rate); LinearLayout ll_risk =
     * (LinearLayout) d.findViewById(R.id.ll_risk);
     *
     * d.requestWindowFeature(Window.FEATURE_NO_TITLE);
     * d.getWindow().setBackgroundDrawable( new
     * ColorDrawable(android.graphics.Color.BLACK));
     * d.setContentView(R.layout.layout_investment_info); d.show(); }
     */

    private void ForgetDialog() {
        dialogOnactivityResult = new Dialog(this);
        dialogOnactivityResult.setCancelable(false);
        dialogOnactivityResult.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnactivityResult.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));
        dialogOnactivityResult.setContentView(R.layout.forget_email_popup);
        edt_email_id = dialogOnactivityResult.findViewById(R.id.edt_email_id);
        // edt_cif_code = (EditText) d.findViewById(R.id.edt_cif_code);

        TableRow tr_CIF_Code = dialogOnactivityResult.findViewById(R.id.tr_CIF_Code);
        tr_CIF_Code.setVisibility(View.GONE);
        Button btn_submit = dialogOnactivityResult.findViewById(R.id.btn_submit);
        Button btn_cancel = dialogOnactivityResult.findViewById(R.id.btn_cancel);

        btn_submit.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                EmailAddress = edt_email_id.getText().toString();

                if (EmailAddress.equals("")) {
                    Toast.makeText(context, "Please Enter Email Address",
                            Toast.LENGTH_LONG).show();

                } else {

                    email_id_validation(EmailAddress);
                    if (validationFla1) {
                        CreatePDF(EmailAddress);

                        if (mCommonMethods.isNetworkConnected(context)) {

                            sendMail(EmailAddress);
                        } else {

                            Toast.makeText(
                                    context,
                                    "Need Analysis Pdf will be mailed once user is online",
                                    Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(context,
                                "Please Enter valid Email Address",
                                Toast.LENGTH_LONG).show();

                    }

                }

            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                dialogOnactivityResult.dismiss();

            }
        });

        dialogOnactivityResult.show();
    }

    private void email_id_validation(String email_id) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email_id);

        if (!(matcher.matches())) {
            edt_email_id.setError("Please provide the correct email address");
            validationFla1 = false;
        }
        // else if (email_id.contains("@sbi.co.in") ||
        // email_id.contains("@sbm.co.in") || email_id.contains("@sbbj.co.in")
        // || email_id.contains("@sbhyd.co.in") ||
        // email_id.contains("@sbp.co.in") || email_id.contains("@sbt.co.in") ||
        // email_id.contains("@sbi-life.com") ||
        // email_id.contains("@sbilife.co.in") ) {
        // edt_email_id.setError("Please provide the personal email address");
        // validationFla1 = false;
        // }
        else if ((matcher.matches())) {
            validationFla1 = true;
        }
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            /*
             * progressDialog = ProgressDialog.show(NeedAnalysisActivity.this,
             * "Please wait", "Sending mail", true, false);
             */

            progressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Sending mail";
            progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                    + Message + "<b></font>"));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please wait<b></font>"));
            progressDialog.setMax(100);
            progressDialog.show();
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (mCommonMethods.isNetworkConnected(context)) {

                Toast.makeText(NeedAnalysisActivity.this,
                        "PDF has been mailed Successfully", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(NeedAnalysisActivity.this,
                        "PDF will be emailed when user is online",
                        Toast.LENGTH_LONG).show();

            }

            dialogOnactivityResult.dismiss();

        }

        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private Message createMessage(String email, String subject,
                                  String messageBody, Session session, File FileName)
            throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("sbilconnectlife@sbi-life.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                email));
        message.setSubject(subject);
        message.setText(messageBody);
        // message.setFileName(FileName);
        if (FileName != null) {
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(messageBody);
            MimeBodyPart mbp2 = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(FileName);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);
            message.setContent(mp);
        }

        return message;
    }

    private void sendMail(String email) {

        String subject = "SBI Life- Need Analysis";
        String messageBody = "Dear "
                + Salutation
                + "\n"
                + "We understand your need to secure your future and appreciate the time that you have taken to analyze your financial needs. When you have a plan, it's easier to take financial decisions and stay on track to meet your future goals."
                + "\n"
                + "We are happy to present to you a roadmap to your dreams"
                + "\n"
                + "Please find the 'Need Analysis' output generated as per the inputs given by you attached ."
                + "\n"
                + "In case of any clarifications, please feel free to ask our sales team to help you out."

                + "\n"
                + "For any other assistance:"
                + "\n"
                + Html.fromHtml("<b>" + "Call us on our " + "</b>")
                + "Toll Free No.1800 267 9090 (9:00 a.m. to 9:00 p.m)"
                + "\n"
                + Html.fromHtml("<b>" + "Email us at " + "</b>")
                + "info@sbilife.co.in"

                + "\n"
                + "Insurance is the subject matter of solicitation."
                + "\n"
                + "Regards,"
                + "\n"
                + "Registered & Corporate Office: SBI Life Insurance Co. Ltd, Natraj, M.V. Road & Western Express Highway Junction, Andheri (East), Mumbai - 400 069."
                + "IRDAI Registration no. 111 issued on 29th March 2001.CIN: L99999MH2000PLC129113, Website : www.sbilife.co.in."
                + "\n"
                + "\n"
                + "This is a system generated e-mail. Please do not to reply to this mail.";

        File FileName = mStorageUtils.createFileToAppSpecificDir(context, EmailAddress + ".pdf");

        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody,
                    session, FileName);
            new SendMailTask().execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.sbi-life.com");
        properties.put("mail.smtp.port", "25");

        return Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "sbilconnectlife@sbi-life.com", "sky@12345");
            }
        });
    }

    private String getformatedThousandString(int number) {

        DecimalFormat df = new DecimalFormat("##,##,##0");
        // NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale(
        // "en", "IN"));

        // String formatedstring = NumberFormat.getNumberInstance(Locale.US)
        // .format(number);
        // return formatedstring;

        return df.format(number);
    }

    // method to set a focusable a element
    public void setFocusable(View v) {
        // TODO Auto-generated method stub
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
    }

    // method to set a clearing a element
    public void clearFocusable(View v) {
        // TODO Auto-generated method stub
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        // v.clearFocus();
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Used To Change date From mm-dd-yyyy to dd-mm-yyyy
     */
    private String getDate(String OldDate) {
        String NewDate = "";
        try {
            DateFormat userDateFormat = new SimpleDateFormat("MM-dd-yyyy");
            DateFormat dateFormatNeeded = new SimpleDateFormat("dd-MM-yyyy");
            Date date = userDateFormat.parse(OldDate);

            NewDate = dateFormatNeeded.format(date);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Integrated Service", "Error in Getting Date");
        }

        return NewDate;
    }

    // end change by devendra
    class HeaderAndFooter extends PdfPageEventHelper {

        Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD);
        Font small_bold3 = new Font(Font.FontFamily.TIMES_ROMAN, 5, Font.BOLD);
        Phrase footer;
        Phrase header;

        /*
         * Font for header and footer part.
         */

        /*
         * constructor
         */
        public HeaderAndFooter() {
            super();

            header = new Phrase("");
            footer = new Phrase(
                    "IRDAI Registration no. 111 issued on 29th March 2001"
                            + "\n"
                            + "Trade logo displayed above belongs to State Bank of India and is used by SBI Life under license."
                            + "\n"
                            + "Registered & Corporate Office: SBI Life Insurance Co. Ltd, Natraj, M.V. Road & Western Express Highway Junction, Andheri (East), Mumbai - 400 069."
                            + "\n"
                            + "Website : www.sbilife.co.in | Email : info@sbilife.co.in |CIN: L99999MH2000PLC129113"
                            + "\n"
                            + "Toll Free : 1800 267 9090 (Between 9.00 am & 9.00 pm)");
        }

        public void onEndPage(PdfWriter writer, Document document) {

            PdfContentByte cb = writer.getDirectContent();

            // header content
            // String headerContent = "";

            // header content
            String footerContent = "IRDAI Registration no. 111 issued on 29th March 2001"
                    + "\n"
                    + "Trade logo displayed above belongs to State Bank of India and is used by SBI Life under license."
                    + "\n"
                    + "Registered & Corporate Office: SBI Life Insurance Co. Ltd, Natraj, M.V. Road & Western Express Highway Junction, Andheri (East), Mumbai - 400 069."
                    + "\n"
                    + "Website : www.sbilife.co.in | Email : info@sbilife.co.in |CIN: L99999MH2000PLC129113"
                    + "\n"
                    + "Toll Free : 1800 267 9090 (Between 9.00 am & 9.00 pm)";
            /*
             * Header
             */
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    new Phrase(String.format(" %d ", writer.getPageNumber()),
                            small_bold2), document.right() - 2,
                    document.top() + 30, 0);

            /*
             * Foooter
             */
            ColumnText.showTextAligned(cb, Element.ALIGN_BOTTOM, new Phrase(
                    footerContent, small_bold3), document.left() - 2, document
                    .bottom() - 20, 0);

        }

    }

    private void HidePlan() {

        txt_wealth_smart_income_protect.setVisibility(View.GONE);
        txt_wealth_smart_guran_savings.setVisibility(View.GONE);
        txt_wealth_smart_money_back.setVisibility(View.GONE);
        txt_wealth_shubh_nivesh.setVisibility(View.GONE);
        txt_wealth_saral_swadhan_plus.setVisibility(View.GONE);
        txt_wealth_smart_swadhan_plus.setVisibility(View.GONE);

        txt_wealth_flexi_smart_plus.setVisibility(View.GONE);
        txt_wealth_money_planner.setVisibility(View.GONE);
        txt_humsafar.setVisibility(View.GONE);
        txt_wealth_money_planner_.setVisibility(View.GONE);
        txt_humsafar_.setVisibility(View.GONE);
        txt_wealth_saral_maha.setVisibility(View.GONE);
        txt_wealth_smart_elite.setVisibility(View.GONE);
        txt_wealth_smart_power_insu.setVisibility(View.GONE);
        txt_wealth_smart_scholar.setVisibility(View.GONE);
        txt_wealth_smart_wealth_builder.setVisibility(View.GONE);
        txt_wealth_smart_wealth_assure.setVisibility(View.GONE);

        /* For Wealth Creation */
        txt_wealth_smart_income_protect_.setVisibility(View.GONE);
        txt_wealth_smart_guran_savings_.setVisibility(View.GONE);
        txt_wealth_smart_money_back_.setVisibility(View.GONE);
        txt_wealth_shubh_nivesh_.setVisibility(View.GONE);
        txt_wealth_saral_swadhan_plus_.setVisibility(View.GONE);

        txt_wealth_smart_swadhan_plus_.setVisibility(View.GONE);
        txt_wealth_smart_swadhan_plus.setVisibility(View.GONE);

        txt_wealth_flexi_smart_plus_.setVisibility(View.GONE);

        txt_wealth_saral_maha_.setVisibility(View.GONE);
        txt_wealth_smart_elite_.setVisibility(View.GONE);
        txt_wealth_smart_power_insu_.setVisibility(View.GONE);
        txt_wealth_smart_scholar_.setVisibility(View.GONE);
        txt_wealth_smart_wealth_builder_.setVisibility(View.GONE);
        txt_wealth_smart_wealth_assure_.setVisibility(View.GONE);

        // txt_ret_saral_pension.setVisibility(View.GONE);
        // txt_ret_retire_smart.setVisibility(View.GONE);
        // txt_ret_annuity_plus.setVisibility(View.GONE);

        txt_pro_smart_shield.setVisibility(View.GONE);
        txt_pro_saral_shield.setVisibility(View.GONE);
        txt_pro_eshield.setVisibility(View.GONE);
        txt_pro_grameen_bima.setVisibility(View.GONE);

        ln_trad_ulip_retirement.setVisibility(View.GONE);
        ln_ulip_trad_retirement.setVisibility(View.GONE);
        txt_saral_pension_trad_ulip_retirement.setVisibility(View.GONE);
        txt_annuity_plus_trad_ulip_retirement.setVisibility(View.GONE);
        txt_retire_smart_trad_ulip_retirement.setVisibility(View.GONE);
        txt_retire_smart_ulip_trad_retirement.setVisibility(View.GONE);
        txt_saral_pension_ulip_trad_retirement.setVisibility(View.GONE);
        txt_annuity_plus_ulip_trad_retirement.setVisibility(View.GONE);
        ln_trad_ulip.setVisibility(View.GONE);
        ln_ulip_trad.setVisibility(View.GONE);
        ln_trad_ulip_child.setVisibility(View.GONE);
        ln_ulip_trad_child.setVisibility(View.GONE);

        txt_smart_champ_trad_ulip_child.setVisibility(View.GONE);
        txt_smart_champ_ulip_trad_child.setVisibility(View.GONE);

        txt_poorn_suraksha_.setVisibility(View.GONE);
        txt_poorn_suraksha.setVisibility(View.GONE);
    }


    private void windowmessageProposersgin() {

        dialogOnactivityResult = new Dialog(NeedAnalysisActivity.this);
        dialogOnactivityResult.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnactivityResult.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        dialogOnactivityResult.setContentView(R.layout.window_message_signature);
        final Button btn_save = dialogOnactivityResult.findViewById(R.id.save);
        final Button btn_cancel = dialogOnactivityResult.findViewById(R.id.cancel);

        Button btn_takeSign = dialogOnactivityResult.findViewById(R.id.takesignature);

        btn_takeSign.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                btn_save.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(NeedAnalysisActivity.this,
                        ProposerCaptureSignature.class);
                intent.putExtra("uniqueId", "10000011980_S01");
                startActivityForResult(intent, SIGNATURE_ACTIVITY);

            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                dialogOnactivityResult.dismiss();
            }
        });
        dialogOnactivityResult.show();

    }

    private void windowmessagesgin() {

        dialogOnactivityResult = new Dialog(NeedAnalysisActivity.this);
        dialogOnactivityResult.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnactivityResult.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.BLACK));

        dialogOnactivityResult.setContentView(R.layout.window_message_signature);
        final Button btn_save = dialogOnactivityResult.findViewById(R.id.save);
        final Button btn_cancel = dialogOnactivityResult.findViewById(R.id.cancel);

        Button btn_takeSign = dialogOnactivityResult.findViewById(R.id.takesignature);

        btn_takeSign.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                btn_save.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(NeedAnalysisActivity.this,
                        CaptureSignature.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);
            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                dialogOnactivityResult.dismiss();
            }
        });
        dialogOnactivityResult.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SIGNATURE_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                Bundle bundle = data.getExtras();
                String status = bundle.getString("status");
                if (status != null && status.equalsIgnoreCase("done")) {
                    Toast toast = Toast
                            .makeText(this, "Signature capture successful!",
                                    Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();
                    if (latestImage.equalsIgnoreCase("proposer")) {
                        Ibtn_signatureofCustomer
                                .setImageBitmap(ProposerCaptureSignature.scaled);
                        Bitmap signature = ProposerCaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            proposer_sign = Base64.encodeToString(
                                    signByteArray, Base64.DEFAULT);
                        }
                        Bitmap customer_Signature = CaptureSignature.scaled;
                    } else if (latestImage.equalsIgnoreCase("agent")) {
                        Ibtn_signatureofIntermediary
                                .setImageBitmap(CaptureSignature.scaled);
                        Bitmap signature = CaptureSignature.scaled;
                        if (signature != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            signature.compress(Bitmap.CompressFormat.PNG, 100,
                                    out);
                            byte[] signByteArray = out.toByteArray();
                            agent_sign = Base64.encodeToString(signByteArray,
                                    Base64.DEFAULT);
                        }
                        Bitmap agent_Signature = CaptureSignature.scaled;

                    }

                    if (dialogOnactivityResult != null)
                        dialogOnactivityResult.dismiss();

                }
            }
        }
    }

    public void dialog(String msg, boolean ismandatry) {

        final Dialog d = new Dialog(NeedAnalysisActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d.setContentView(R.layout.window_pop_up_message_with_single_options);
        TextView text_mssg_1 = d.findViewById(R.id.text_mssg_1);
        text_mssg_1.setText(msg);
        TextView text_mssg_2 = d.findViewById(R.id.text_mssg_2);
        TextView text_mssg_3 = d.findViewById(R.id.text_mssg_3);
        if (ismandatry) {
            text_mssg_2.setVisibility(View.GONE);
            text_mssg_3.setVisibility(View.GONE);
        }
        Button ok = d.findViewById(R.id.idbtnagreement);
        ok.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                d.dismiss();
            }

        });
        d.setCancelable(true);
        d.setCanceledOnTouchOutside(true);
        d.show();

    }

    private Bitmap ConvertToBitmap(View targetView) {

        targetView.buildDrawingCache();
        Bitmap b1 = targetView.getDrawingCache();
        // copy this bitmap otherwise distroying the cache will destroy
        // the bitmap for the referencing drawable and you'll not
        // get the captured view
        return b1.copy(Bitmap.Config.ARGB_8888, false);
    }

    private void justifyListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight
                + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    class CustomSuggestedProdListAdapter extends
            ArrayAdapter<SuggestedProdList> {

        private ArrayList<SuggestedProdList> prodList;
        Context context;

        CustomSuggestedProdListAdapter(Context context,
                                       int textViewResourceId, ArrayList<SuggestedProdList> countryList) {
            super(context, textViewResourceId, countryList);
            this.prodList = new ArrayList<>();
            this.prodList.addAll(countryList);
            this.context = context;
        }

        private class ViewHolder {
            TextView code, prod_name, plan_cat;
            CheckBox cb_prod_check;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            // Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.suggested_prod_list_item,
                        null);

                holder = new ViewHolder();
                holder.code = convertView.findViewById(R.id.code);
                holder.prod_name = convertView
                        .findViewById(R.id.name);

                holder.plan_cat = convertView
                        .findViewById(R.id.plan_cat);

                holder.cb_prod_check = convertView
                        .findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.cb_prod_check
                        .setOnClickListener(new OnClickListener() {

                            public void onClick(View v) {
                                CheckBox cb = (CheckBox) v;
                                SuggestedProdList node = (SuggestedProdList) cb
                                        .getTag();
                                /*
                                 * Toast.makeText(getApplicationContext(),
                                 * "Clicked on Checkbox: " + cb.getText() +
                                 * " is " + cb.isChecked(),
                                 * Toast.LENGTH_LONG).show();
                                 */
                                if (cb.isChecked()) {

                                    chosen_prod_list.add(node);
                                    sugg_chosen_prod_list.add(node);

                                    if (chosen_prod_list.size() <= 6) {
                                        updateProductChosenList();
                                    } else {
                                        chosen_prod_list.remove(node);
                                        sugg_chosen_prod_list.remove(node);
                                        updateProductChosenList();
                                        Toast.makeText(
                                                context,
                                                "You can choose maximum 6 Products!",
                                                Toast.LENGTH_LONG).show();
                                        cb.setChecked(false);
                                    }
                                } else {
                                    chosen_prod_list.remove(node);
                                    sugg_chosen_prod_list.remove(node);
                                    updateProductChosenList();
                                }
                                node.setSelected(cb.isChecked());
                            }
                        });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            SuggestedProdList country = prodList.get(position);
            holder.prod_name.setText("SBI Life " + country.getProduct_name());
            holder.code.setText("(" + country.getProduct_UIN() + ")");

            String plan_cat = "";
            if (!country.getProduct_name().equals("")) {
                /** Added by Priyanka Warekar - 10-11-2016 - start *****/
                plan_cat = getId(map,
                        country.getProduct_name().replaceAll("[*#]", ""))
                        + "";
                /** Added by Priyanka Warekar - 10-11-2016 - end *****/
            }
            holder.plan_cat.setText("(" + plan_cat + ")");

            holder.cb_prod_check.setChecked(country.isSelected());
            holder.cb_prod_check.setTag(country);

            return convertView;

        }

    }

    class CustomOtherProdListAdapter extends
            ArrayAdapter<SuggestedProdList> {

        private ArrayList<SuggestedProdList> prodList;
        Context context;

        CustomOtherProdListAdapter(Context context,
                                   int textViewResourceId, ArrayList<SuggestedProdList> countryList) {
            super(context, textViewResourceId, countryList);
            this.prodList = new ArrayList<>();
            this.prodList.addAll(countryList);
            this.context = context;
        }

        private class ViewHolder {
            TextView code, prod_name, plan_cat;
            CheckBox cb_prod_check;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            // Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.suggested_prod_list_item,
                        null);

                holder = new ViewHolder();
                holder.code = convertView.findViewById(R.id.code);
                holder.prod_name = convertView
                        .findViewById(R.id.name);
                holder.plan_cat = convertView
                        .findViewById(R.id.plan_cat);

                holder.cb_prod_check = convertView
                        .findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.cb_prod_check
                        .setOnClickListener(new OnClickListener() {

                            public void onClick(View v) {
                                CheckBox cb = (CheckBox) v;
                                SuggestedProdList node = (SuggestedProdList) cb
                                        .getTag();
                                /*
                                 * Toast.makeText(getApplicationContext(),
                                 * "Clicked on Checkbox: " + cb.getText() +
                                 * " is " + cb.isChecked(),
                                 * Toast.LENGTH_LONG).show();
                                 */
                                if (cb.isChecked()) {

                                    chosen_prod_list.add(node);
                                    other_chosen_prod_list.add(node);
                                    if (chosen_prod_list.size() <= 6) {
                                        updateProductChosenList();
                                    } else {
                                        chosen_prod_list.remove(node);
                                        other_chosen_prod_list.remove(node);
                                        updateProductChosenList();
                                        Toast.makeText(
                                                context,
                                                "You can choose maximum 6 Products!",
                                                Toast.LENGTH_LONG).show();
                                        cb.setChecked(false);
                                    }
                                } else {
                                    chosen_prod_list.remove(node);
                                    other_chosen_prod_list.remove(node);
                                    updateProductChosenList();
                                }
                                node.setSelected(cb.isChecked());
                            }
                        });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            SuggestedProdList country = prodList.get(position);
            holder.prod_name.setText("SBI Life " + country.getProduct_name());
            holder.code.setText("(" + country.getProduct_UIN() + ")");

            String plan_cat = "";
            if (!country.getProduct_name().equals("")) {
                plan_cat = getId(map, country.getProduct_name()) + "";
            }
            holder.plan_cat.setText("(" + plan_cat + ")");

            holder.cb_prod_check.setChecked(country.isSelected());
            holder.cb_prod_check.setTag(country);

            return convertView;

        }
    }

    private void setId() {

        map.put("Shubh Nivesh", "Traditional Plan");
        //map.put("Flexi Smart Plus", "Traditional Plan");
        /*map.put("Smart Guaranteed Savings Plan", "Traditional Plan");*/
        map.put("Saral Swadhan+", "Traditional Plan");
        map.put("Smart Shield", "Traditional Plan");
        //map.put("Saral Shield", "Traditional Plan");
        map.put("Smart Money Back Gold", "Traditional Plan");
        map.put("Smart Champ Insurance", "Traditional Plan");
        map.put("Smart Income Protect", "Traditional Plan");
        map.put("Saral Retirement Saver", "Traditional Plan");
        map.put("Smart Humsafar", "Traditional Plan");
        map.put("Smart Swadhan Plus", "Traditional Plan");
        //map.put("Smart Women Advantage", "Traditional Plan");
        map.put("Smart Money Planner", "Traditional Plan");
        map.put("Annuity Plus", "Traditional Plan");
        map.put("Smart Wealth Assure", "Market Linked Plan");
        map.put("Smart Power Insurance", "Market Linked Plan");
        map.put("Retire Smart", "Market Linked Plan");
        map.put("Smart Wealth Builder", "Market Linked Plan");
        map.put("Smart Elite", "Market Linked Plan");
        map.put("Smart Scholar", "Market Linked Plan");
        //map.put("Saral Maha Anand", "Market Linked Plan");
        map.put("Smart Privilege", "Market Linked Plan");
        // ***** Added by Priyanka Warekar - 11-01-2017 - Start ****///
        map.put("Smart Bachat", "Traditional Plan");

        // ***** Added by Priyanka Warekar - 11-01-2017 - End ****///

        // ***** Added by Tushar Kadam - 21-06-2017 - Start ****///
        map.put("Sampoorn Cancer Suraksha", "Traditional Plan");
        // ***** Added by Tushar Kadam - 21-06-2017 - Start ****///

        // ***** Added by rajan 28-11-2017 ****///
        map.put("Poorna Suraksha", "Traditional Plan");
        // ***** Added by rajan 28-11-2017 ****///

        //Added by Tushar 11-10-2018
        //map.put(getString(R.string.sbi_life_smart_samriddhi), "Traditional Plan");
        map.put(getString(R.string.sbi_life_saral_insure_wealth_plus), "Market Linked Plan");
        map.put(getString(R.string.sbi_life_smart_insure_wealth_plus), "Market Linked Plan");
        //Added by Tushar 09-04-2019
        map.put(getString(R.string.sbi_life_smart_platina_assure), "Traditional Plan");
        map.put(getString(R.string.sbi_life_annuity_plus), "Traditional Plan");
        map.put(getString(R.string.sbi_life_smart_future_choices), "Protection Plan");
        map.put(getString(R.string.sbi_life_saral_jeevan_bima), "Traditional Plan");
        map.put(getString(R.string.sbi_life_new_smart_samriddhi), "Traditional Plan");
        map.put(getString(R.string.sbi_life_saral_pension_new), "Traditional Plan");
        map.put(getString(R.string.sbi_life_eshield_next), "Traditional Plan");
        map.put(getString(R.string.sbi_life_smart_platina_plus), "Traditional Plan");
    }

    private String getId(Map<String, String> map, String proofName) {
        String plan_cat = "";
        for (int i = 0; i < map.size() - 1; i++) {
            plan_cat = map.get(proofName);
        }
        return plan_cat + "";
    }

    private void updateProductChosenList() {
        if (chosen_prod_list.size() <= 6) {
            if (chosen_prod_list.size() == 6) {
                txt_product_chosen_1.setText("SBI Life " + chosen_prod_list.get(0)
                        .getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(0).getProduct_UIN() + ")");
                txt_product_chosen_2.setText("SBI Life " + chosen_prod_list.get(1)
                        .getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(1).getProduct_UIN() + ")");
                txt_product_chosen_3.setText("SBI Life " + chosen_prod_list.get(2)
                        .getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(2).getProduct_UIN() + ")");
                txt_product_chosen_4.setText("SBI Life " + chosen_prod_list.get(3)
                        .getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(3).getProduct_UIN() + ")");
                txt_product_chosen_5.setText("SBI Life " + chosen_prod_list.get(4)
                        .getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(4).getProduct_UIN() + ")");
                txt_product_chosen_6.setText("SBI Life " + chosen_prod_list.get(5)
                        .getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(5).getProduct_UIN() + ")");

                ll_product_chosen.setVisibility(View.VISIBLE);
                ll_product_chosen_1.setVisibility(View.VISIBLE);
                ll_product_chosen_2.setVisibility(View.VISIBLE);
                ll_product_chosen_3.setVisibility(View.VISIBLE);
                ll_product_chosen_4.setVisibility(View.VISIBLE);
                ll_product_chosen_5.setVisibility(View.VISIBLE);
                ll_product_chosen_6.setVisibility(View.VISIBLE);
            } else if (chosen_prod_list.size() == 5) {
                txt_product_chosen_1.setText("SBI Life " + chosen_prod_list.get(0).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(0).getProduct_UIN() + ")");

                txt_product_chosen_2.setText("SBI Life " + chosen_prod_list.get(1).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(1).getProduct_UIN() + ")");

                txt_product_chosen_3.setText("SBI Life " + chosen_prod_list.get(2).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(2).getProduct_UIN() + ")");

                txt_product_chosen_4.setText("SBI Life " + chosen_prod_list.get(3).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(3).getProduct_UIN() + ")");

                txt_product_chosen_5.setText("SBI Life " + chosen_prod_list.get(4).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(4).getProduct_UIN() + ")");

                ll_product_chosen.setVisibility(View.VISIBLE);
                ll_product_chosen_1.setVisibility(View.VISIBLE);
                ll_product_chosen_2.setVisibility(View.VISIBLE);
                ll_product_chosen_3.setVisibility(View.VISIBLE);
                ll_product_chosen_4.setVisibility(View.VISIBLE);
                ll_product_chosen_5.setVisibility(View.VISIBLE);
                ll_product_chosen_6.setVisibility(View.GONE);
            } else if (chosen_prod_list.size() == 4) {
                txt_product_chosen_1.setText("SBI Life " + chosen_prod_list.get(0).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(0).getProduct_UIN() + ")");

                txt_product_chosen_2.setText("SBI Life " + chosen_prod_list.get(1).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(1).getProduct_UIN() + ")");

                txt_product_chosen_3.setText("SBI Life " + chosen_prod_list.get(2).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(2).getProduct_UIN() + ")");

                txt_product_chosen_4.setText("SBI Life " + chosen_prod_list.get(3).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(3).getProduct_UIN() + ")");

                ll_product_chosen.setVisibility(View.VISIBLE);
                ll_product_chosen_1.setVisibility(View.VISIBLE);
                ll_product_chosen_2.setVisibility(View.VISIBLE);
                ll_product_chosen_3.setVisibility(View.VISIBLE);
                ll_product_chosen_4.setVisibility(View.VISIBLE);
                ll_product_chosen_5.setVisibility(View.GONE);
                ll_product_chosen_6.setVisibility(View.GONE);
            } else if (chosen_prod_list.size() == 3) {
                txt_product_chosen_1.setText("SBI Life " + chosen_prod_list.get(0).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(0).getProduct_UIN() + ")");

                txt_product_chosen_2.setText("SBI Life " + chosen_prod_list.get(1).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(1).getProduct_UIN() + ")");

                txt_product_chosen_3.setText("SBI Life " + chosen_prod_list.get(2).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(2).getProduct_UIN() + ")");

                ll_product_chosen.setVisibility(View.VISIBLE);
                ll_product_chosen_1.setVisibility(View.VISIBLE);
                ll_product_chosen_2.setVisibility(View.VISIBLE);
                ll_product_chosen_3.setVisibility(View.VISIBLE);
                ll_product_chosen_4.setVisibility(View.GONE);
                ll_product_chosen_5.setVisibility(View.GONE);
                ll_product_chosen_6.setVisibility(View.GONE);
            } else if (chosen_prod_list.size() == 2) {
                txt_product_chosen_1.setText("SBI Life " + chosen_prod_list.get(0).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(0).getProduct_UIN() + ")");

                txt_product_chosen_2.setText("SBI Life " + chosen_prod_list.get(1).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(1).getProduct_UIN() + ")");

                ll_product_chosen.setVisibility(View.VISIBLE);
                ll_product_chosen_1.setVisibility(View.VISIBLE);
                ll_product_chosen_2.setVisibility(View.VISIBLE);
                ll_product_chosen_3.setVisibility(View.GONE);
                ll_product_chosen_4.setVisibility(View.GONE);
                ll_product_chosen_5.setVisibility(View.GONE);
                ll_product_chosen_6.setVisibility(View.GONE);
            } else if (chosen_prod_list.size() == 1) {
                txt_product_chosen_1.setText("SBI Life " + chosen_prod_list.get(0).getProduct_name()
                        + " \n ("
                        + chosen_prod_list.get(0).getProduct_UIN() + ")");

                ll_product_chosen.setVisibility(View.VISIBLE);
                ll_product_chosen_1.setVisibility(View.VISIBLE);
                ll_product_chosen_2.setVisibility(View.GONE);
                ll_product_chosen_3.setVisibility(View.GONE);
                ll_product_chosen_4.setVisibility(View.GONE);
                ll_product_chosen_5.setVisibility(View.GONE);
                ll_product_chosen_6.setVisibility(View.GONE);
            } else {
                ll_product_chosen.setVisibility(View.GONE);
                ll_product_chosen_1.setVisibility(View.GONE);
                ll_product_chosen_2.setVisibility(View.GONE);
                ll_product_chosen_3.setVisibility(View.GONE);
                ll_product_chosen_4.setVisibility(View.GONE);
                ll_product_chosen_5.setVisibility(View.GONE);
                ll_product_chosen_6.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(context, "You can choose maximum 6 Products!",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void onClickProductChosen(View v) {
        if (chosen_prod_list.size() == 0)
            Toast.makeText(context, "Please Select Product", Toast.LENGTH_LONG)
                    .show();
        else if (proposer_sign == null || proposer_sign.equals(""))
            /*
             * Toast.makeText(context, "Please get Customer's Signature",
             * Toast.LENGTH_LONG).show();
             */
            new CommonMethods().showMessageDialog(context,
                    "Please get Customer's Signature");
        else if (agent_sign == null || agent_sign.equals(""))
            /*
             * Toast.makeText(context, "Please get Intermediary's Signature",
             * Toast.LENGTH_LONG).show();
             */
            new CommonMethods().showMessageDialog(context,
                    "Please get Intermediary's Signature");
        else {

            TextView b = (TextView) v;
            String[] str = b.getText().toString().split("\n");
            strProd = str[0].trim();
            if (mCommonMethods.isNetworkConnected(context)) {
                if ((str_usertype.equalsIgnoreCase("CAG")
                        || str_usertype.equalsIgnoreCase("CIF"))
                        && strProd.equalsIgnoreCase("SBI Life "
                        + getString(R.string.sbi_life_smart_insure_wealth_plus))) {
                    mCommonMethods.showMessageDialog(context, "You are not applicable for this Product");
                    return;
                }
                if ((str_usertype.equalsIgnoreCase("AGENT")
                        || str_usertype.equalsIgnoreCase("BAP")
                        || str_usertype.equalsIgnoreCase("IMF"))
                        && strProd.equalsIgnoreCase("SBI Life "
                        + getString(R.string.sbi_life_saral_insure_wealth_plus))) {
                    mCommonMethods.showMessageDialog(context, "You are not applicable for this Product");
                    return;
                }
                taskUinNoServiceHit = new UIN_NO_ServiceHit();
                taskUinNoServiceHit.execute(null, null, null);
            } else {
                showDialog("Please check your internet connection.");
            }
        }
    }

    private void showDialog(String msg) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.window_agreement);
        TextView text = dialog.findViewById(R.id.textMessage);
        text.setText(msg);
        Button dialogButton = dialog.findViewById(R.id.idbtnagreement);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private String GetUserType() {
        String strUserType = "";
        try {
            strUserType = SimpleCrypto.decrypt("SBIL",
                    dbHelper.GetUserType());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return strUserType;
    }

    // generate URN number
    class UIN_NO_ServiceHit extends AsyncTask<String, String, String> {
        // private volatile boolean running = true;
        String UIN_NO = "";
        boolean uinflag = false;
        String inputpolicylist;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
             * progressDialog = ProgressDialog.show(context, "Please wait... ",
             * "Loading...", true, false);
             */

            progressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading...";
            progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                    + Message + "<b></font>"));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please wait...<b></font>"));
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {
                CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods
                        .setUserDetails(context);
                String strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
                String strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

                SoapObject request;
                if (!str_group.equalsIgnoreCase("Other")) {
                    request = new SoapObject(NAMESPACE,
                            METHOD_NAME_NA_CBI_UIN_NO);
                } else {
                    request = new SoapObject(NAMESPACE,
                            METHOD_NAME_NA_CBI_UIN_NO_OTHER_BANK);
                    request.addProperty("strBankName", "Other Bank");
                }
                mCommonMethods.appendSecurityParams(context, request, strCIFBDMEmailId, strCIFBDMMObileNo);
                System.out.println("request.toString() = " + request.toString());
                mCommonMethods.TLSv12Enable();
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                if (!str_group.equalsIgnoreCase("Other")) {
                    androidHttpTranport.call(SOAP_ACTION_NA_CBI_UIN_NO,
                            envelope);
                } else {
                    androidHttpTranport.call(
                            SOAP_ACTION_NA_CBI_UIN_NO_OTHER_BANK, envelope);
                }

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();
                inputpolicylist = sa.toString();
                System.out.println("request.toString() = " + inputpolicylist);
                if (Long.parseLong(inputpolicylist) > 0
                        && (inputpolicylist.length() == 10 || inputpolicylist
                        .length() == 12))
                    UIN_NO = inputpolicylist;
                else
                    uinflag = true;

                System.out.println(" URN : " + UIN_NO);
            } catch (Exception e) {

                uinflag = true;
                e.printStackTrace();
            }

            return null;

        }

        /*
         * it is shows the progress of async task
         */

        @Override
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            // mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /*
         * it is execute after complete the progress of async task
         */

        @Override
        protected void onPostExecute(String unused) {
            try {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (Exception e) {

            }

            if (!uinflag) {
                URN_NO = UIN_NO;
                // Toast.makeText(context, "URN NO : " + URN_NO,
                // Toast.LENGTH_LONG).show();

				/*try {
					CreatePDF("NA");
					CreatePDF_Graphical("NA_Graphical");
				} catch (Exception e) {
					e.printStackTrace();
				}*/
                try {
                    CreatePDF(URN_NO + "_NA");
                    CreatePDF_Graphical(URN_NO + "_NA_Graphical");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (str_group.equalsIgnoreCase("Other")) {

                    // save URN to Need Analysis table
                    dbHelper.AddNeedAnalysisDashboardDetailsForOther(
                            new ProductBIBean(URN_NO, str_group, inputVal
                                    .toString(), "<NeedAnalysisOutput>"
                                    + outputlist + "</NeedAnalysisOutput>"),
                            new DatabaseHelper(context).GetUserCode());

                    // redirect to Home Page
                    /*
                     * Intent i = new Intent(context,
                     * NewBusinessHomeGroupingActivity.class);
                     * context.startActivity(i); NeedAnalysisActivity.URN_NO="";
                     */

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.loading_window);
                    dialog.setCancelable(false);
                    TextView text = dialog
                            .findViewById(R.id.txtalertheader);
                    text.setText("URN generated successfully\nURN no. is - "
                            + URN_NO + " \nPlease check the Dashboard");
                    progressDialog.dismiss();

                    Button dialogButton = dialog
                            .findViewById(R.id.btnalert);
                    dialogButton.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {

                            dialog.dismiss();
                            NeedAnalysisActivity.URN_NO = "";
                            OthersProductListActivity.URNNumber = "";
                            OthersProductListActivity.groupName = "";
                            Intent intent = new Intent(context,
                                    NeedAnalysisDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialog.show();
                } else {
                    new RedirectToActivityTask().execute();
                }

            } else {
                URN_NO = "";
            }

        }

        private class RedirectToActivityTask extends
                AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();

                final Dialog d = new Dialog(context,
                        android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.BLACK));

                d.setContentView(R.layout.productdesc_webview);
                TextView txtHeaderName = d
                        .findViewById(R.id.txtHeaderName_wv);
                TextView txtHeaderUIN = d
                        .findViewById(R.id.txtHeaderUIN_wv);
                TextView txtHeaderPCode = d
                        .findViewById(R.id.txtHeaderPCode_wv);

                Button buttonProceed = d
                        .findViewById(R.id.buttonProceed);
                Button btnBrochurePreCalsi = d
                        .findViewById(R.id.btnBrochurePreCalsi);
                btnBrochurePreCalsi.setVisibility(View.GONE);

                Button btnBrochure = d
                        .findViewById(R.id.btnBrochure);

                RelativeLayout relativeLayoutBrochure = d.findViewById(R.id.relativeLayoutBrochure);
                LinearLayout linearLayoutProceed = d
                        .findViewById(R.id.linearLayoutProceed);

                linearLayoutProceed.setVisibility(View.VISIBLE);
                //linearLayoutBrochure.setVisibility(View.GONE);
                WebView webview = d.findViewById(R.id.webview);

                strProd = strProd.replaceAll("[*#]", "");

                if (strProd.equalsIgnoreCase("SBI Life Saral Maha Anand")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Saral Maha Anand</u>"));
                    txtHeaderUIN.setText("(UIN:111L070V02)");
                    txtHeaderPCode.setText("(Product Code : 50)");

                    webview.loadUrl("file:///android_asset/saral_maha_anand.html");

                    str_brochure_dest_file_path = "saral_maha_anand_brochure.pdf";
                    //str_brochure_download_file_url ="https://www.sbilife.co.in/saral-maha-anand-policy";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/saral-maha-anand-brochure";
                } else if (strProd.equalsIgnoreCase("SBI Life Smart Elite")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Smart Elite</u>"));
                    txtHeaderUIN.setText("(UIN:111L072V04)");
                    txtHeaderPCode.setText("(Product Code : 53)");

                    webview.loadUrl("file:///android_asset/smart_elite.html");

                    str_brochure_dest_file_path = "smart_elite_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-elite-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Smart Scholar")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Smart Scholar</u>"));
                    txtHeaderUIN.setText("(UIN:111L073V03)");
                    txtHeaderPCode.setText("(Product Code : 51)");

                    webview.loadUrl("file:///android_asset/smart_scholar.html");

                    str_brochure_dest_file_path = "smart_scholar_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-scholar-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Smart Bachat")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Smart Bachat</u>"));
                    txtHeaderUIN.setText("(UIN:111N108V03)");
                    txtHeaderPCode.setText("(Product Code : 2D)");

                    webview.loadUrl("file:///android_asset/smart_bachat.html");

                    str_brochure_dest_file_path = "smart_bacaht_brochure_english.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart_bachat_brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Smart Power Insurance")) {
                    txtHeaderName
                            .setText(Html
                                    .fromHtml("<u>SBI Life - Smart Power Insurance</u>"));
                    txtHeaderUIN.setText("(UIN:111L090V02)");
                    txtHeaderPCode.setText("(Product Code : 1C)");

                    webview.loadUrl("file:///android_asset/smart_power_insurance.html");

                    str_brochure_dest_file_path = "smart_power_insurance_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-power-insurance-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Smart Wealth Assure")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Smart Wealth Assure</u>"));
                    txtHeaderUIN.setText("(UIN:111L077V03)");
                    txtHeaderPCode.setText("(Product Code : 55)");

                    webview.loadUrl("file:///android_asset/smart_wealth_assure.html");

                    str_brochure_dest_file_path = "smart_wealth_assure_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-wealth-assure-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Smart Wealth Builder")) {
                    txtHeaderName
                            .setText(Html
                                    .fromHtml("<u>SBI Life - Smart Wealth Builder</u>"));
                    txtHeaderUIN.setText("(UIN:111L095V03)");
                    txtHeaderPCode.setText("(Product Code : 1K)");

                    webview.loadUrl("file:///android_asset/smart_wealth_builder.html");

                    str_brochure_dest_file_path = "smart_wealth_assure_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-wealth-assure-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Smart Women Advantage")) {
                    txtHeaderName
                            .setText(Html
                                    .fromHtml("<u>SBI Life - Smart Women Advantage</u>"));
                    txtHeaderUIN.setText("(UIN:111N106V01)");
                    txtHeaderPCode.setText("(Product Code : 2C)");

                    webview.loadUrl("file:///android_asset/smart_women_advantage.html");

                    str_brochure_dest_file_path = "smart_women_advantage_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-women-advantage-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Saral Swadhan+")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Saral Swadhan+</u>"));
                    txtHeaderUIN.setText("(UIN:111N092V03)");
                    txtHeaderPCode.setText("(Product Code : 1J)");

                    webview.loadUrl("file:///android_asset/saral_swadhan_plus.html");

                    str_brochure_dest_file_path = "saral_swadhan_plus_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/saral_swadhan_plus_bro_pdf";

                } else if (strProd.equalsIgnoreCase("SBI Life Smart Champ Insurance")) {
                    txtHeaderName
                            .setText(Html
                                    .fromHtml("<u>SBI Life - Smart Champ Insurance</u>"));
                    txtHeaderUIN.setText("(UIN:111N098V03)");
                    txtHeaderPCode.setText("(Product Code : 1P)");

                    webview.loadUrl("file:///android_asset/smart_champ.html");

                    str_brochure_dest_file_path = "smart_champ_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-champ-insurance-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Retire Smart")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Retire Smart</u>"));
                    txtHeaderUIN.setText("(UIN:111L094V02)");
                    txtHeaderPCode.setText("(Product Code : 1H)");

                    webview.loadUrl("file:///android_asset/retire_smart.html");

                    str_brochure_dest_file_path = "retire_smart_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/retire-smart-brochure";
                } else if (strProd.equalsIgnoreCase("SBI Life Saral Retirement Saver")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Saral Retirement Saver</u>"));
                    txtHeaderUIN.setText("(UIN:111N088V03)");
                    txtHeaderPCode.setText("(Product Code : 1E)");

                    webview.loadUrl("file:///android_asset/SaralRetirementSaver.html");

                    str_brochure_dest_file_path = "saral_pension_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/saral-retirement-saver-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Saral Shield")) {
                    // txtHeaderName.setText("SBI Life - Saral Shield");
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Saral Shield</u>"));
                    txtHeaderUIN.setText("(UIN:111N066V03)");
                    txtHeaderPCode.setText("(Product Code : 47)");

                    webview.loadUrl("file:///android_asset/saral_shield.html");

                    str_brochure_dest_file_path = "saral_shield_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/saral-shield-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Smart Shield")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Smart Shield</u>"));
                    //111N067V02
                    txtHeaderUIN.setText("(UIN:111N067V07)");
                    txtHeaderPCode.setText("(Product Code : 45)");

                    webview.loadUrl("file:///android_asset/smart_shield.html");

                    str_brochure_dest_file_path = "saral_shield_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/saral-shield-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Smart Income Protect")) {

                    txtHeaderName
                            .setText(Html
                                    .fromHtml("<u>SBI Life - Smart Income Protect</u>"));
                    txtHeaderUIN.setText("(UIN:111N085V04)");
                    txtHeaderPCode.setText("(Product Code : 1B)");

                    webview.loadUrl("file:///android_asset/smart_income_protect.html");

                    str_brochure_dest_file_path = "smart_income_protect_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-income-protect-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Flexi Smart Plus")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Flexi Smart Plus</u>"));
                    txtHeaderUIN.setText("(UIN:111N093V01)");
                    txtHeaderPCode.setText("(Product Code : 1M)");

                    webview.loadUrl("file:///android_asset/flexi_smart_plus.html");

                    str_brochure_dest_file_path = "flexi_smart_plus_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/flexi-smart-plus-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Smart Money Back Gold")) {
                    // txtHeaderName.setText("SBI Life - Smart Money Back Gold");
                    txtHeaderName
                            .setText(Html
                                    .fromHtml("<u>SBI Life - Smart Money Back Gold</u>"));
                    txtHeaderUIN.setText("(UIN:111N096V03)");
                    txtHeaderPCode.setText("(Product Code : 1N)");

                    webview.loadUrl("file:///android_asset/smart_money_back_gold.html");

                    str_brochure_dest_file_path = "smart_money_back_gold_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-money-back-gold-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Shubh Nivesh")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Shubh Nivesh</u>"));
                    txtHeaderUIN.setText("(UIN:111N055V04)");
                    txtHeaderPCode.setText("(Product Code : 35)");

                    webview.loadUrl("file:///android_asset/shubh_nivesh.html");

                    str_brochure_dest_file_path = "shubh_nivesh_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/shubh-nivesh";

                } else if (strProd
                        .equalsIgnoreCase("SBI Life Smart Guaranteed Savings Plan")) {
                    txtHeaderName
                            .setText(Html
                                    .fromHtml("<u>SBI Life - Smart Guaranteed Savings Plan</u>"));
                    txtHeaderUIN.setText("(UIN:111N097V01)");
                    txtHeaderPCode.setText("(Product Code : 1X)");

                    webview.loadUrl("file:///android_asset/smart_guaranteed.html");

                    str_brochure_dest_file_path = "smart_guaranteed_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-gauranteed-bro";

                } else if (strProd.equalsIgnoreCase("SBI Life Smart Money Planner")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Smart Money Planner</u>"));
                    txtHeaderUIN.setText("(UIN:111N101V03)");
                    txtHeaderPCode.setText("(Product Code : 1R)");

                    webview.loadUrl("file:///android_asset/smart_money_planner.html");

                    str_brochure_dest_file_path = "smart_money_planner_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-money-planner-brochure";

                } else if (strProd.equalsIgnoreCase("SBI Life Smart Humsafar")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Smart Humsafar</u>"));
                    txtHeaderUIN.setText("(UIN:111N103V03)");
                    txtHeaderPCode.setText("(Product Code : 1W)");

                    webview.loadUrl("file:///android_asset/smart_humsafar.html");

                    str_brochure_dest_file_path = "smart_humsafar_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-humsafar-brochure";
                } else if (strProd.equalsIgnoreCase("SBI Life Smart Swadhan Plus")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Smart Swadhan Plus</u>"));
                    txtHeaderUIN.setText("(UIN:111N104V02)");
                    txtHeaderPCode.setText("(Product Code : 1Z)");

                    webview.loadUrl("file:///android_asset/smart_swadhan_plus.html");

                    str_brochure_dest_file_path = "smart_swadhan_plus_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-swadhan-plus-brochure";
                } else if (strProd.equalsIgnoreCase("SBI Life Smart Privilege")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Smart Privilege</u>"));
                    txtHeaderUIN.setText("(UIN No - 111L107V03)");
                    txtHeaderPCode.setText("(Product Code:70)");
                    webview.loadUrl("file:///android_asset/smart_privilege.html");

                    str_brochure_dest_file_path = "smart_privilege_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/smart-privilege-brochure";
                } else if (strProd.equalsIgnoreCase("SBI Life Sampoorn Cancer Suraksha")) {
                    //"2E", "UIN : 111N109V03",
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Sampoorn Cancer Suraksha</u>"));
                    txtHeaderUIN.setText("(UIN No - 111N109V03)");
                    txtHeaderPCode.setText("(Product Code:2E)");
                    webview.loadUrl("file:///android_asset/sampoorn_cancer_suraksha.html");

                    str_brochure_dest_file_path = "sampoorn_cancer_suraksha_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/Sampoorn-Cancer-Suraksha-brochure";
                } else if (strProd.equalsIgnoreCase("SBI Life Poorna Suraksha")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Poorna Suraksha</u>"));
                    txtHeaderUIN.setText("(UIN No - 111N110V03)");
                    txtHeaderPCode.setText("(Product Code:2F)");

                    webview.loadUrl("file:///android_asset/poorn_suraksha.html");
                    str_brochure_dest_file_path = "poorna_suraksha_brochure.pdf";
                    str_brochure_download_file_url = "https://www.sbilife.co.in/poorna-suraksha-brochure";
                } else if (strProd.equalsIgnoreCase("SBI Life Smart Samriddhi")) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - Smart Samriddhi</u>"));
                    txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_smart_samriddhi_uin) + ")");
                    txtHeaderPCode.setText("(Product Code:2G)");

                    webview.loadUrl("file:///android_asset/smartsamriddhi.html");
                    str_brochure_dest_file_path = getString(R.string.sbi_life_smart_samriddhi_brochure_pdf);
                    str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_smart_samriddhi_brochure_link);
                } else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_saral_insure_wealth_plus))) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_saral_insure_wealth_plus) + "</u>"));
                    txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_saral_insure_wealth_plus_uin) + ")");
                    txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_saral_insure_wealth_plus_code) + ")");

                    webview.loadUrl("file:///android_asset/saral_insure_wealth_plus.html");

                    str_brochure_dest_file_path = getString(R.string.sbi_life_saral_insure_wealth_brochure_pdf);
                    str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_saral_insure_wealth_brochure_link);
                } else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_smart_insure_wealth_plus))) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_smart_insure_wealth_plus) + "</u>"));
                    txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_smart_insure_wealth_plus_uin) + ")");
                    txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_smart_insure_wealth_plus_code) + ")");

                    webview.loadUrl("file:///android_asset/smart_insure_wealth_plus.html");

                    str_brochure_dest_file_path = getString(R.string.sbi_life_smart_insure_wealth_brochure_pdf);
                    str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) + getString(R.string.sbi_life_smart_samriddhi_brochure_link);
                } else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_smart_platina_assure))) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_smart_platina_assure) + "</u>"));
                    txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_smart_platina_assure_uin) + ")");
                    txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_smart_platina_assure_code) + ")");


                    webview.loadUrl("file:///android_asset/platina.html");
                    str_brochure_dest_file_path = getString(R.string.sbi_life_smart_platina_assure_brochure_pdf);
                    str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                            getString(R.string.sbi_life_smart_platina_assure_brochure_link);
                } else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_annuity_plus))) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_annuity_plus) + "</u>"));
                    txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_annuity_plus_uin) + ")");
                    txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_annuity_plus_code) + ")");


                    webview.loadUrl("file:///android_asset/annuity_plus.html");
                    str_brochure_dest_file_path = getString(R.string.sbi_life_annuity_plus_brochure_pdf);
                    str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                            getString(R.string.sbi_life_annuity_plus_brochure_link);
                } else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_smart_future_choices))) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_smart_future_choices) + "</u>"));
                    txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_smart_future_choices_uin) + ")");
                    txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_smart_future_choices_code) + ")");


                    webview.loadUrl("file:///android_asset/smart_furure_choice.html");
                    str_brochure_dest_file_path = getString(R.string.sbi_life_smart_future_choices_brochure_pdf);
                    str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                            getString(R.string.sbi_life_smart_future_choices_brochure_link);
                } else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_saral_jeevan_bima))) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_saral_jeevan_bima) + "</u>"));
                    txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_saral_jeevan_bima_uin) + ")");
                    txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_saral_jeevan_bima_code) + ")");


                    webview.loadUrl("file:///android_asset/saral_jeevan_beema.html");
                    str_brochure_dest_file_path = getString(R.string.sbi_life_saral_jeevan_bima_brochure_pdf);
                    str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                            getString(R.string.sbi_life_saral_jeevan_bima_brochure_link);
                } else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_new_smart_samriddhi))) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_new_smart_samriddhi) + "</u>"));
                    txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_new_smart_samriddhi_uin) + ")");
                    txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_new_smart_samriddhi_code) + ")");


                    webview.loadUrl("file:///android_asset/newsmartsamriddhi.html");
                    str_brochure_dest_file_path = getString(R.string.sbi_life_new_smart_samriddhi_brochure_pdf);
                    str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                            getString(R.string.sbi_life_new_smart_samriddhi_brochure_link);
                } else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_saral_pension_new))) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_saral_pension_new) + "</u>"));
                    txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_saral_pension_new_uin) + ")");
                    txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_saral_pension_new_code) + ")");


                    webview.loadUrl("file:///android_asset/saral_pension.html");
                    str_brochure_dest_file_path = getString(R.string.sbi_life_saral_pension_new_brochure_pdf);
                    str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                            getString(R.string.sbi_life_saral_pension_new_brochure_link);
                } else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_eshield_next))) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_eshield_next) + "</u>"));
                    txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_eshield_next_uin) + ")");
                    txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_eshield_next_code) + ")");


                    webview.loadUrl("file:///android_asset/eShieldNext.html");
                    str_brochure_dest_file_path = getString(R.string.sbi_life_eshield_next_brochure_pdf);
                    str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                            getString(R.string.sbi_life_eshield_next_brochure_link);
                }else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_smart_platina_plus))) {
                    txtHeaderName.setText(Html
                            .fromHtml("<u>SBI Life - " + getString(R.string.sbi_life_smart_platina_plus) + "</u>"));
                    txtHeaderUIN.setText("(UIN No - " + getString(R.string.sbi_life_smart_platina_plus_uin) + ")");
                    txtHeaderPCode.setText("(Product Code:" + getString(R.string.sbi_life_smart_platina_plus_code) + ")");
                    webview.loadUrl("file:///android_asset/SmartPlatinaPlus.html");
                    str_brochure_dest_file_path = getString(R.string.sbi_life_smart_platina_plus_brochure_pdf);
                    str_brochure_download_file_url = getString(R.string.sbiLifeDownloadLink) +
                            getString(R.string.sbi_life_smart_platina_plus_brochure_link);
                }
                webview.getSettings().setSupportZoom(true);
                webview.getSettings().setBuiltInZoomControls(true);
                webview.getSettings().setUseWideViewPort(true);
                webview.getSettings().setLoadWithOverviewMode(true);

                d.show();
                linearLayoutProceed.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                        gotoBI();
                    }
                });
                buttonProceed.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                        gotoBI();
                    }
                });

                btnBrochure.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();

                        Intent i = new Intent(context, NABrochureActivity.class);
                        i.putExtra("BROCHURE_URL", str_brochure_download_file_url);
                        i.putExtra("BROCHURE_FILE_PATH", str_brochure_dest_file_path);
                        startActivity(i);
                    }
                });

            }

        }

    }

    private void gotoBI() {

        Intent i = null;
        /** Added by Priyanka Warekar - 10-11-2016 - start *****/

        /** Added by Priyanka Warekar - 10-11-2016 - end *****/
        if (strProd.equalsIgnoreCase("SBI Life Smart Wealth Builder"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartWealthBuilderActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Elite"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartEliteActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Shubh Nivesh"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_ShubhNiveshActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Flexi Smart Plus"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_FlexiSmartPlusActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Guaranteed Savings Plan"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartGuaranteedSavingsPlanActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Women Advantage"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartWomenAdvantageActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Shield"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartShieldActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Scholar"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartScholarActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Money Back Gold"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartMoneyBackGoldActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Champ Insurance"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartChampActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Retire Smart"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_RetireSmartActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Saral Shield"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SaralShieldActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Income Protect"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartIncomeProtectActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Saral Maha Anand"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SaralMahaAnandActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Wealth Assure"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartWealthAssureActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Saral Retirement Saver"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SaralPensionActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Power Insurance"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartPowerInsuranceActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Humsafar"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartHumsafarActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Swadhan Plus"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartSwadhanPlusActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Money Planner"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartMoneyPlannerActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Saral Swadhan+"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SaralSwadhanPlusActivity.class);
            // ***** Added by Priyanka Warekar - 11-01-2017 - Start ****///
        else if (strProd.equalsIgnoreCase("SBI Life Smart Bachat"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartBachatActivity.class);

        else if (strProd.equalsIgnoreCase("SBI Life Smart Privilege"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartPrivilegeActivity.class);

        else if (strProd.equalsIgnoreCase("SBI Life Sampoorn Cancer Suraksha"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SampoornCancerSurakshaActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Poorna Suraksha"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_PoornSurakshaActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life Smart Samriddhi"))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SmartSamriddhiActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_saral_insure_wealth_plus)))
            i = new Intent(NeedAnalysisActivity.this,
                    BI_SaralInsureWealthPlusActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_smart_insure_wealth_plus)))
            i = new Intent(NeedAnalysisActivity.this, BI_SmartInsureWealthPlusActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_smart_platina_assure)))
            i = new Intent(NeedAnalysisActivity.this, BI_SmartPlatinaAssureActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_annuity_plus)))
            i = new Intent(NeedAnalysisActivity.this, BI_AnnuityPlusActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_smart_future_choices)))
            i = new Intent(NeedAnalysisActivity.this, BI_SmartFutureChoicesActivity.class);
        else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_saral_jeevan_bima))) {
            i = new Intent(NeedAnalysisActivity.this, BI_SaralJeevanBimaActivity.class);
        } else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_new_smart_samriddhi))) {
            i = new Intent(NeedAnalysisActivity.this, BI_NewSmartSamriddhiActivity.class);
        } else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_saral_pension_new))) {
            i = new Intent(NeedAnalysisActivity.this, BI_SaralPensionNewActivity.class);
        }else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_eshield_next))) {
            i = new Intent(NeedAnalysisActivity.this, BI_EShieldNext.class);
        }else if (strProd.equalsIgnoreCase("SBI Life " + getString(R.string.sbi_life_smart_platina_plus))) {
            i = new Intent(NeedAnalysisActivity.this, BI_SmartPlatinaPlusActivity.class);
        }
        // ***** Added by Priyanka Warekar - 11-01-2017 - End ****///
        if (i != null) {
            i.putExtra("NAFlag", "1");
            i.putExtra("custDOB", str_date_of_birth);
            i.putExtra("custGender", Gender);
            i.putExtra("NaInput", inputVal.toString());
            i.putExtra("NaOutput", "<NeedAnalysisOutput>" + outputlist
                    + "</NeedAnalysisOutput>");
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Do you want to close Need Analysis Calculator?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {

                                Intent intent = new Intent(
                                        NeedAnalysisActivity.this,
                                        NewBusinessHomeGroupingActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }
                        }).setNegativeButton("No", null).show();

    }

    public void onClickHome() {
        // TODO Auto-generated method stub

        new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Do you want to close Need Analysis Calculator?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {

                                Intent intent = new Intent(
                                        NeedAnalysisActivity.this,
                                        CarouselHomeActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }).setNegativeButton("No", null).show();

    }

    private String GetUserName() {
        String strUserType = "";
        try {
            strUserType = dbHelper.GetUserName();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return strUserType;
    }

    private String GetUserCode() {
        String strUserType = "";
        try {
            strUserType = SimpleCrypto.decrypt("SBIL",
                    dbHelper.GetUserCode());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return strUserType;
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (sendMailTask != null) {
            sendMailTask.cancel(true);
        }
        if (asyncdownload != null) {
            asyncdownload.cancel(true);
        }

        if (taskUinNoServiceHit != null) {
            taskUinNoServiceHit.cancel(true);
        }
        /*if (stateIdAsyncTask != null) {
            stateIdAsyncTask.cancel(true);
        }*/
        super.onDestroy();
    }

    public String getneedAnalysis_Unique_No(String ProductCode,
                                            String agentCode) {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String formattedDate = s.format(c.getTime());
        // max++;
        String unique_num = "";

        unique_num = ProductCode + agentCode + formattedDate;

        return unique_num;
    }
}
